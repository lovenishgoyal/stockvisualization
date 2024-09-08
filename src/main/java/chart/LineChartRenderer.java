package chart;

import javafx.scene.chart.XYChart;
import models.StockRawDataProcess;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

/**
 * @author lovenishgoyal
 * @version 1.0
 */
public class LineChartRenderer {

    private static final Logger logger = LoggerFactory.getLogger(LineChartRenderer.class);

    public static JPanel createLineChart(StockRawDataProcess stockRawDataProcess, String stockName) throws Exception {
        // Get processed stock data
        List<XYChart.Data<Number, Number>> stockData = getProcessedStockData(stockRawDataProcess);

        // Create dataset and chart
        XYDataset dataset = createDataset(stockData);
        JFreeChart chart = ChartFactory.createXYLineChart(
                stockName + "Close Price",        // chart title
                "Date",           // x-axis label
                "Price",          // y-axis label
                dataset,          // dataset
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,             // include legend
                true,             // tooltips
                false             // urls
        );

        // Customize the plot
        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis xAxis = new DateAxis("Date");
        NumberAxis yAxis = new NumberAxis("Price");

        // Set the x-axis and y-axis
        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);

        // Use a custom date format
        xAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));

        // Create and return ChartPanel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        chartPanel.setMouseWheelEnabled(true); // Enable zooming with mouse wheel
        return chartPanel;
    }

    private static XYDataset createDataset(List<XYChart.Data<Number, Number>> stockData) {
        XYSeries series = new XYSeries("Stock Prices");

        for (XYChart.Data<Number, Number> data : stockData) {
            series.add((Number) data.getXValue(), (Number) data.getYValue());
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        return dataset;
    }

    private static List<XYChart.Data<Number, Number>> getProcessedStockData(StockRawDataProcess stockRawDataProcess) throws Exception {
        List<XYChart.Data<Number, Number>> stockData = new ArrayList<>();

        try {
            String rawData = stockRawDataProcess.getRawData();
            String key = stockRawDataProcess.getTimeSeriesKey();

            logger.info("Key: " + key);
            JSONObject timeSeries = new JSONObject(rawData).getJSONObject(key);
            logger.info("Time Series: " + timeSeries.toString());
            for (String dateStr : timeSeries.keySet()) {
                JSONObject dailyData = timeSeries.getJSONObject(dateStr);
                logger.info("Date: " + dateStr);
                Date date = stockRawDataProcess.getDf().parse(dateStr);
                double price = dailyData.getDouble(Constants.OPEN_KEY);  // Adjust based on actual JSON structure
                stockData.add(new XYChart.Data<>(date.getTime(), price));
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return stockData;
    }
}
