import chart.CandleChartRenderer;
import datafetcher.DataFetcher;
import datafetcher.GetDataFetcherFactory;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.OHLCData;
import org.json.JSONObject;
import models.TimeGranularity;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import models.TimeInterval;

import java.util.List;

/**
 * @author lovenishgoyal
 * @version 1.0
 */
public class UIController extends Application {
    private static final Logger logger = LoggerFactory.getLogger(UIController.class);
    private static final String API_KEY = "XTN5B7134EVRGGHK";

    @Override
    public void start(Stage primaryStage) {

        Label stockLabel = new Label("Please Stock Name:");
        TextField stockInput = new TextField();

        Label timeLabel = new Label("Please Choose Time Granularity:");
        ComboBox<String> timeGranularity = new ComboBox<>();
        timeGranularity.getItems().addAll("Daily", "Weekly", "Monthly", "Intraday");

        Label intervalLabel = new Label("Time Interval:");
        ComboBox<String> intervalComboBox = new ComboBox<>();
        intervalComboBox.getItems().addAll("1 minute", "5 minutes", "15 minutes", "30 minutes", "60 minutes");
        intervalLabel.setVisible(false); // Initially hidden
        intervalComboBox.setVisible(false); // Initially hidden

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
        gridPane.add(submitButton, 1, 3);

        // Handle Time Granularity Selection
        timeGranularity.setOnAction(e -> {
            String granularity = timeGranularity.getValue();
            if (TimeGranularity.INTRADAY.equals(granularity)) {
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
            TimeInterval timeInterval = null;

            logger.info("Given Stock Name: " + stockName);
            logger.info("Given Time Granularity: " + granularity);
            if ("Intraday".equals(granularity)) {
                logger.info("Given Time Interval: " + interval);
                timeInterval = TimeInterval.valueOf(interval.toUpperCase());
            }

            if (stockName == null || stockName.isEmpty() || granularity == null) {
                logger.error("Enter a valid stock name and time granularity");
                return;
            }

            // If "Intraday" is selected, ensure an interval is selected as well
            if ("Intraday".equals(granularity) && (interval == null || interval.isEmpty())) {
                logger.error("Time interval is not selected for Intraday data");
                return;
            }


            GetDataFetcherFactory dataFetcherFactory = new GetDataFetcherFactory();

            DataFetcher df = dataFetcherFactory.getDataFetcher(TimeGranularity.valueOf(granularity.toUpperCase()),
                    API_KEY, timeInterval);

            String data;
            // Get data from api
            try {
                data = df.getStockData(stockName);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            List<OHLCData> ohlcData = df.parseStockData(data);

            // Create candlestick chart and add to SwingNode

            JPanel chartPanel = CandleChartRenderer.createChart(ohlcData, stockName);
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

    public static void main(String[] args) {
        launch(args);
    }
}