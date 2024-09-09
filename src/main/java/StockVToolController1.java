import chart.CandleChartRenderer;
import chart.LineChartRenderer;
import datafetcher.DataFetcher;
import datafetcher.GetDataFetcherFactory;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.StockRawDataProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * The StockVToolController class is a JavaFX application that serves as a controller
 * for a stock visualization tool. It allows users to input stock data parameters,
 * choose the granularity, and select the chart type for visualization.
 */
public class StockVToolController1 extends Application {
    private static final Logger logger = LoggerFactory.getLogger(StockVToolController1.class);
    private static final String API_KEY = "XTN5B7134EVRGGHK";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
        TextField stockInput = new TextField();

        Label timeLabel = new Label("Please Choose Time Granularity:");
        ComboBox<String> timeGranularity = new ComboBox<>();
        timeGranularity.getItems().addAll(Constants.TIME_GRANULARITY_OPTIONS);

        Label intervalLabel = new Label("Time Interval:");
        ComboBox<String> intervalComboBox = new ComboBox<>();
        intervalComboBox.getItems().addAll(Constants.TIME_INTERVAL_OPTIONS);
        intervalLabel.setVisible(false); // Initially hidden
        intervalComboBox.setVisible(false); // Initially hidden

        // Date Pickers for start and end dates
        Label startDateLabel = new Label("Start Date");
        DatePicker startDatePicker = new DatePicker();
        Label endDateLabel = new Label("End Date");
        DatePicker endDatePicker = new DatePicker();

        // Radio Buttons for Chart Type Selection
        RadioButton lineChartButton = new RadioButton("Line Chart");
        RadioButton candleChartButton = new RadioButton("Candle Chart");
        ToggleGroup chartTypeGroup = new ToggleGroup();
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
        gridPane.setPadding(new Insets(10));
        gridPane.add(stockLabel, 0, 0);
        gridPane.add(stockInput, 1, 0);
        gridPane.add(timeLabel, 0, 1);
        gridPane.add(timeGranularity, 1, 1);
        gridPane.add(intervalLabel, 0, 2);
        gridPane.add(intervalComboBox, 1, 2);
        gridPane.add(startDateLabel, 0, 3);
        gridPane.add(startDatePicker, 1, 3);
        gridPane.add(endDateLabel, 0, 4);
        gridPane.add(endDatePicker, 1, 4);
        gridPane.add(radioBox, 0, 5, 2, 1); // Add the radio buttons before the submit button
        gridPane.add(submitButton, 1, 6);

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

        // SwingNode to display the chart
        SwingNode swingNode = new SwingNode();

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

            logger.info("Given Stock Name: " + stockName);
            logger.info("Given Time Granularity: " + granularity);
            if ("Intraday".equals(granularity)) {
                logger.info("Given Time Interval: " + interval);
            }

            if (stockName.isEmpty() || granularity == null) {
                logger.error("Enter a valid stock name and time granularity");
                showAlert(Alert.AlertType.ERROR, "Input Error", "Stock Name and granularity are required.");
                return;
            }

            if ("Intraday".equalsIgnoreCase(granularity) && (interval == null || interval.isEmpty())) {
                logger.error("Time interval is not selected for Intraday data");
                showAlert(Alert.AlertType.ERROR, "Input Error", "Time interval is required for Intraday data.");
                return;
            }

            Date startDate;
            Date endDate;
            try {
                startDate = dateFormat.parse(startDateStr);
                endDate = dateFormat.parse(endDateStr);
            } catch (ParseException ex) {
                logger.error("Invalid date format", ex);
                showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid date format. Use yyyy-MM-dd.");
                return;
            }

            // Ensure end date is after start date
            if (startDate != null && endDate != null && startDate.after(endDate)) {
                logger.error("Invalid date range");
                showAlert(Alert.AlertType.ERROR, "Input Error", "End Date must be after Start Date.");
                return;
            }

            GetDataFetcherFactory dataFetcherFactory = new GetDataFetcherFactory();
            DataFetcher df = dataFetcherFactory.getDataFetcher(granularity.toUpperCase(), API_KEY, interval);

            StockRawDataProcess stockRawDataProcess;

            try {
                stockRawDataProcess = df.getStockData(stockName);
            } catch (Exception ex) {
                logger.error("Error fetching stock data", ex);
                showAlert(Alert.AlertType.ERROR, "Data Error", "Error fetching stock data. Please try again.");
                return;
            }

            JPanel chartPanel;
            try {
                if (chartTypeGroup.getSelectedToggle() == lineChartButton) {
                    chartPanel = LineChartRenderer.createLineChart(stockRawDataProcess, stockName, startDate, endDate);
                } else {
                    chartPanel = CandleChartRenderer.createCandleChart(stockRawDataProcess, stockName, startDate, endDate);
                }
            } catch (Exception ex) {
                logger.error("Error creating chart", ex);
                showAlert(Alert.AlertType.ERROR, "Chart Error", "Error creating chart. Please try again.");
                return;
            }

            swingNode.setContent(chartPanel);

        });

        // Create the main layout
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(gridPane);
        borderPane.setCenter(swingNode);

        // Create the scene and set it on the stage
        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setTitle("Stock Visualization Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     *
     * @param alertType The type of alert to display (e.g., ERROR, INFORMATION).
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

    /**
     * The main entry point for launching the JavaFX application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
