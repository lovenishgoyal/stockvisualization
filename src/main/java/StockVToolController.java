import chart.CandleChartRenderer;
import chart.LineChartRenderer;
import datafetcher.DataFetcher;
import datafetcher.GetDataFetcherFactory;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.OHLCData;

import javax.swing.*;

import models.StockRawDataProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

import java.util.List;

/**
 * @author lovenishgoyal
 * @version 1.0
 */
public class StockVToolController extends Application {
    private static final Logger logger = LoggerFactory.getLogger(StockVToolController.class);
    private static final String API_KEY = "XTN5B7134EVRGGHK";

    @Override
    public void start(Stage primaryStage) {

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

        // Create Radio Buttons for Chart Type Selection
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
        gridPane.add(stockLabel, 0, 0);
        gridPane.add(stockInput, 1, 0);
        gridPane.add(timeLabel, 0, 1);
        gridPane.add(timeGranularity, 1, 1);
        gridPane.add(intervalLabel, 0, 2);
        gridPane.add(intervalComboBox, 1, 2);
        gridPane.add(radioBox, 0, 3, 2, 1); // Add the radio buttons before the submit button
        gridPane.add(submitButton, 1, 4);

        // Handle Time Granularity Selection
        timeGranularity.setOnAction(e -> {
            String granularity = timeGranularity.getValue();
            if ("INTRADAY".equalsIgnoreCase(granularity)) {
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
            //TimeInterval timeInterval = null;

            logger.info("Given Stock Name: " + stockName);
            logger.info("Given Time Granularity: " + granularity);
            if ("Intraday".equals(granularity)) {
                logger.info("Given Time Interval: " + interval);
                //timeInterval = TimeInterval.valueOf(interval.toUpperCase());
            }

            if (stockName == null || stockName.isEmpty() || granularity == null) {
                logger.error("Enter a valid stock name and time granularity");
                showAlert(Alert.AlertType.ERROR, "Input Error", "Stock Name and granularity are must");
                return;
            }

            // If "Intraday" is selected, ensure an interval is selected as well
            if ("Intraday".equalsIgnoreCase(granularity) && (interval == null || interval.isEmpty())) {
                logger.error("Time interval is not selected for Intraday data");
                showAlert(Alert.AlertType.ERROR, "Input Error", "Time interval is not selected for Intraday data");
                return;
            }

            GetDataFetcherFactory dataFetcherFactory = new GetDataFetcherFactory();

            DataFetcher df = dataFetcherFactory.getDataFetcher(granularity.toUpperCase(),
                    API_KEY, interval);

            StockRawDataProcess stockRawDataProcess;

            try {
                stockRawDataProcess = df.getStockData(stockName);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            JPanel chartPanel;
            try {
                if (chartTypeGroup.getSelectedToggle() == lineChartButton) {
                    chartPanel = LineChartRenderer.createLineChart(stockRawDataProcess, stockName);
                } else {
                    chartPanel = CandleChartRenderer.createCandleChart(stockRawDataProcess, stockName);
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
        primaryStage.setTitle("Stock Candle Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

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