import chart.CandleChartRenderer;
import chart.LineChartRenderer;
import datafetcher.DataFetcher;
import datafetcher.GetDataFetcherFactory;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.StockRawDataProcess;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The StockVToolController class is a JavaFX application that serves as a controller
 * for a stock visualization tool. It allows users to input stock data parameters,
 * choose the granularity, and select the chart type for visualization.
 */
public class StockVToolController extends Application {
    private static final Logger logger = LoggerFactory.getLogger(StockVToolController.class);
    private static final String API_KEY = "XTN5B7134EVRGGHK";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String SYMBOL_SEARCH_URL = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&apikey=" + API_KEY;

    /**
     * The entry point of the JavaFX application. Sets up the user interface,
     * including controls for stock data input and chart selection, and handles user actions.
     *
     * @param primaryStage The primary stage for this application, onto which
     *                     the application scene will be set.
     */
    @Override
    public void start(Stage primaryStage) {
        // Labels and input fields for stock name and time granularity
        Label stockLabel = new Label("Please Stock Name:");
        final TextField stockInput = new TextField();
        final ListView<String> suggestionList = new ListView<>();
        suggestionList.setPrefHeight(100);
        suggestionList.setVisible(false);


        Label timeLabel = new Label("Please Choose Time Granularity:");
        final ComboBox<String> timeGranularity = new ComboBox<>();
        timeGranularity.getItems().addAll(Constants.TIME_GRANULARITY_OPTIONS);

        Label intervalLabel = new Label("Time Interval:");
        final ComboBox<String> intervalComboBox = new ComboBox<>();
        intervalComboBox.getItems().addAll(Constants.TIME_INTERVAL_OPTIONS);
        intervalLabel.setVisible(false); // Initially hidden
        intervalComboBox.setVisible(false); // Initially hidden

        // Date Pickers for start and end dates
        Label startDateLabel = new Label("Start Date");
        final DatePicker startDatePicker = new DatePicker();
        Label endDateLabel = new Label("End Date");
        final DatePicker endDatePicker = new DatePicker();

        // Radio Buttons for Chart Type Selection
        final RadioButton lineChartButton = new RadioButton("Line Chart");
        final RadioButton candleChartButton = new RadioButton("Candle Chart");
        final ToggleGroup chartTypeGroup = new ToggleGroup();
        lineChartButton.setToggleGroup(chartTypeGroup);
        candleChartButton.setToggleGroup(chartTypeGroup);
        lineChartButton.setSelected(true); // Default selection

        // Layout for Radio Buttons
        VBox radioBox = new VBox(10, lineChartButton, candleChartButton);

        Button submitButton = new Button("Submit");

        // Layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(5));
        gridPane.add(stockLabel, 0, 0);
        gridPane.add(stockInput, 1, 0);
        gridPane.add(suggestionList, 1, 1); // Add suggestion list
        gridPane.add(timeLabel, 0, 2);
        gridPane.add(timeGranularity, 1, 2);
        gridPane.add(intervalLabel, 0, 3);
        gridPane.add(intervalComboBox, 1, 3);
        gridPane.add(startDateLabel, 0, 4);
        gridPane.add(startDatePicker, 1, 4);
        gridPane.add(endDateLabel, 0, 5);
        gridPane.add(endDatePicker, 1, 5);
        gridPane.add(radioBox, 0, 6, 2, 1); // Add the radio buttons before the submit button
        gridPane.add(submitButton, 1, 7);

        // Handle Time Granularity Selection
        timeGranularity.setOnAction(e -> {
            String granularity = timeGranularity.getValue();
            if ("Intraday".equalsIgnoreCase(granularity)) {
                intervalLabel.setVisible(true);
                intervalComboBox.setVisible(true);
            } else {
                intervalLabel.setVisible(false);
                intervalComboBox.setVisible(false);
            }
        });

        // Auto-complete feature for stock input
        stockInput.setOnKeyReleased(e -> {
            String query = stockInput.getText().trim();
            if (query.length() > 1) {
                try {
                    List<String> symbols = fetchStockSymbols(query);
                    if (symbols != null) {
                        suggestionList.getItems().setAll(symbols);
                        suggestionList.setVisible(true);
                    }
                } catch (Exception ex) {
                    logger.error("Error fetching stock symbols", ex);
                }
            } else {
                suggestionList.setVisible(false);
            }
        });

        // Hide suggestions on selection
        suggestionList.setOnMouseClicked(e -> {
            String selectedSymbol = suggestionList.getSelectionModel().getSelectedItem();
            if (selectedSymbol != null) {
                logger.info("selectedSymbol: " + selectedSymbol.split("--")[0]);
                stockInput.setText(selectedSymbol.split("--")[0].trim());
                suggestionList.setVisible(false);
            }
        });

        // SwingNode to display the chart
        final SwingNode swingNode = new SwingNode();

        // Submit Button Action
        submitButton.setOnAction(e -> {
            String stockName = stockInput.getText().trim();
            String granularity = timeGranularity.getValue().trim();
            String interval = intervalComboBox.getValue();

            // Calculate default dates
            LocalDate now = LocalDate.now();
            LocalDate defaultStartDate = now.minusMonths(1); // Default start date is 1 month ago
            LocalDate defaultEndDate = now; // Default end date is today

            String startDateStr = startDatePicker.getValue() != null ? startDatePicker.getValue().toString() : defaultStartDate.toString();
            String endDateStr = endDatePicker.getValue() != null ? endDatePicker.getValue().toString() : defaultEndDate.toString();

            try {
                Date startDate = dateFormat.parse(startDateStr);
                Date endDate = dateFormat.parse(endDateStr);

                if (startDate.after(endDate)) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "End Date must be after Start Date.");
                    return;
                }

                GetDataFetcherFactory dataFetcherFactory = new GetDataFetcherFactory();
                DataFetcher df = dataFetcherFactory.getDataFetcher(granularity.toUpperCase(), API_KEY, interval);
                StockRawDataProcess stockRawDataProcess = df.getStockData(stockName);

                JPanel chartPanel;
                if (chartTypeGroup.getSelectedToggle() == lineChartButton) {
                    chartPanel = LineChartRenderer.createLineChart(stockRawDataProcess, stockName, startDate, endDate);
                } else {
                    chartPanel = CandleChartRenderer.createCandleChart(stockRawDataProcess, stockName, startDate, endDate);
                }
                chartPanel.setPreferredSize(new Dimension(1000, 700));
                swingNode.setContent(chartPanel);


            } catch (ParseException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid date format. Use yyyy-MM-dd.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Data Error", "Error fetching stock data. Please try again.");
            }
        });

        // Create the main layout
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(gridPane);
        borderPane.setCenter(swingNode);

        // Create the scene and set it on the stage

        Scene scene = new Scene(borderPane, 1200, 1000);
        primaryStage.setTitle("Stock Visualization Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Fetch stock symbols from Alpha Vantage Symbol Search API.
     *
     * @param query The search query for stock symbols.
     * @return A list of stock symbols matching the query.
     * @throws Exception If an error occurs while fetching data.
     */
    private List<String> fetchStockSymbols(String query) throws Exception {
        String urlString = SYMBOL_SEARCH_URL + "&keywords=" + query;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray bestMatches = jsonResponse.getJSONArray("bestMatches");

        List<String> symbols = new ArrayList<>();
        for (int i = 0; i < bestMatches.length(); i++) {
            JSONObject match = bestMatches.getJSONObject(i);
            String symbol = match.getString("1. symbol");
            String name = match.getString("2. name");
            symbols.add(symbol + " -- " + name);
        }
        return symbols;
    }

    /**
     * Show an alert dialog with a specified type, title, and message.
     *
     * @param alertType The type of the alert (e.g., ERROR, INFORMATION).
     * @param title     The title of the alert dialog.
     * @param message   The message to display in the alert dialog.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
