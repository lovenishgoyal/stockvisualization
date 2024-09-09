
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
 * A utility class for creating and rendering line charts to visualize stock price data.
 * <p>
 * This class provides methods to generate a line chart based on raw stock data. It includes functionality
 * for customizing the chart's appearance, such as setting axis labels and date formats. The class also
 * processes raw stock data to create a dataset suitable for rendering in the chart.
 * </p>
 *
 * @author lovenishgoyal
 * @version 1.0
 */
public class LineChartRenderer {


    private static final Logger logger = LoggerFactory.getLogger(LineChartRenderer.class);

    /**
     * Creates a line chart panel based on the provided stock data and date range.
     *
     * @param stockRawDataProcess the object containing the stock raw data and parsing logic
     * @param stockName           the name of the stock to display in the chart
     * @param startDate           the start date of the data range to display
     * @param endDate             the end date of the data range to display
     * @return a JPanel containing the line chart
     * @throws Exception if an error occurs while creating the chart
     */

    public static JPanel createLineChart(StockRawDataProcess stockRawDataProcess, String stockName, Date startDate, Date endDate) throws Exception {

        List<XYChart.Data<Number, Number>> stockData = getProcessedStockData(stockRawDataProcess);

        XYDataset dataset = createDataset(stockData);
        JFreeChart chart = ChartFactory.createXYLineChart(
                stockName + " Close Price", // chart title
                "Date",                   // x-axis label
                "Price",                  // y-axis label
                dataset,                  // dataset
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );

        // Customize the plot
        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis xAxis = new DateAxis("Date");
        NumberAxis yAxis = new NumberAxis("Price");

        // Set the x-axis and y-axis
        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);

        // Use a custom date format
        xAxis.setDateFormatOverride(stockRawDataProcess.getDf());

        // Create and return ChartPanel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        chartPanel.setMouseWheelEnabled(true);
        return chartPanel;
    }

    /**
     * Creates an XYDataset from the provided list of stock data points.
     *
     * @param stockData the list of stock data points
     * @return an XYDataset containing the stock data
     */
    private static XYDataset createDataset(List<XYChart.Data<Number, Number>> stockData) {
        XYSeries series = new XYSeries("Stock Prices");
        for (XYChart.Data<Number, Number> data : stockData) {
            series.add((Number) data.getXValue(), (Number) data.getYValue());
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        return dataset;
    }

    /**
     * Processes raw stock data to generate a list of XYChart.Data points.
     *
     * @param stockRawDataProcess the object containing raw stock data and parsing logic
     * @return a list of XYChart.Data points extracted from the raw data
     * @throws Exception if an error occurs while processing the raw data
     */
    private static List<XYChart.Data<Number, Number>> getProcessedStockData(StockRawDataProcess stockRawDataProcess) throws Exception {
        List<XYChart.Data<Number, Number>> stockData = new ArrayList<>();

        String rawData = stockRawDataProcess.getRawData();
        String key = stockRawDataProcess.getTimeSeriesKey();
        JSONObject timeSeries = new JSONObject(rawData).getJSONObject(key);
        for (String dateStr : timeSeries.keySet()) {
            JSONObject dailyData = timeSeries.getJSONObject(dateStr);
            Date date = stockRawDataProcess.getDf().parse(dateStr);
            double price = dailyData.getDouble(Constants.OPEN_KEY); // Adjust based on actual JSON structure
            stockData.add(new XYChart.Data<>(date.getTime(), price));
        }
        return stockData;
    }
}
