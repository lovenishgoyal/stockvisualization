package chart;

import models.OHLCData;
import models.StockRawDataProcess;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import utils.TimeZoneDateFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * A utility class for creating and rendering candlestick charts based on stock price data.
 * <p>
 * This class leverages the JFreeChart library to generate candlestick charts that visualize stock price
 * data. It includes functionality for filtering data by date range, configuring zoom in and out buttons,
 * and adjusting the chart's axis and appearance.
 * </p>
 * <p>
 * Key features:
 * <ul>
 *     <li>Generates a candlestick chart for a specified stock and date range.</li>
 *     <li>Filters data based on date range and ensures valid price values.</li>
 *     <li>Provides zoom in and zoom out functionality via buttons.</li>
 *     <li>Customizes chart appearance and axis formatting.</li>
 * </ul>
 * </p>
 *
 * @author lovenishgoyal
 * @version 1.0
 */
public class CandleChartRenderer {

    private static final Logger logger = LoggerFactory.getLogger(CandleChartRenderer.class);

    /**
     * Creates a candlestick chart panel with zoom functionality based on the provided stock data and date range.
     *
     * @param stockRawDataProcess the object containing the stock raw data and parsing logic
     * @param stockName           the name of the stock to display in the chart
     * @param startDate           the start date of the data range to display
     * @param endDate             the end date of the data range to display
     * @return a JPanel containing the candlestick chart and zoom buttons
     * @throws Exception if an error occurs while creating the chart
     */
    public static JPanel createCandleChart(StockRawDataProcess stockRawDataProcess, String stockName, Date startDate, Date endDate) throws Exception {
        List<OHLCData> ohlcDataList = getProcessedStockData(stockRawDataProcess);
        // Fetch and filter out dates without data
        List<OHLCData> filteredDataList = filterData(ohlcDataList, startDate, endDate);

        // Convert filtered data into dataset for chart rendering
        OHLCDataset dataset = createOHLCDataset(stockName, filteredDataList);
        JFreeChart chart = ChartFactory.createCandlestickChart(
                stockName,       // title
                "Time",          // x-axis label
                "Price",         // y-axis label
                dataset,         // dataset
                false            // legend
        );

        // Get the plot and set axis
        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setLowerBound(0);
        System.out.println(stockRawDataProcess.getDf().toString());
        dateAxis.setDateFormatOverride(stockRawDataProcess.getDf());

        // Adjust axis range to only include available data points
        if (!filteredDataList.isEmpty()) {
            Date minDate = filteredDataList.get(0).getDate();
            Date maxDate = filteredDataList.get(filteredDataList.size() - 1).getDate();

            if (minDate.before(maxDate)) {
                dateAxis.setRange(minDate, maxDate); // Set the date axis range to the available data range
            } else {
                logger.warn("Invalid date range: minDate is not before maxDate. Using default range.");
            }
        } else {
            logger.warn("Filtered data list is empty. No data to display.");
        }

        // Configure renderer
        CandlestickRenderer renderer = new CandlestickRenderer();
        plot.setRenderer(renderer);

        // Create chart panel and add zoom buttons
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1000, 800));
        chartPanel.setMouseWheelEnabled(true); // Enable zooming with mouse wheel

        // Create zoom-in and zoom-out buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton zoomInButton = new JButton("Zoom In");
        JButton zoomOutButton = new JButton("Zoom Out");

        // Add button actions
        zoomInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoom(plot, 0.8); // Zoom in by 20%
            }
        });

        zoomOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoom(plot, 1.2); // Zoom out by 20%
            }
        });

        buttonPanel.add(zoomInButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(zoomOutButton);

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(chartPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    /**
     * Adjusts the zoom level of the chart by modifying the axis range.
     *
     * @param plot       the XYPlot to be zoomed
     * @param zoomFactor the factor by which to zoom (e.g., 0.8 for zooming in, 1.2 for zooming out)
     */
    private static void zoom(XYPlot plot, double zoomFactor) {
        DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();

        double currentMin = domainAxis.getLowerBound();
        double currentMax = domainAxis.getUpperBound();
        double range = currentMax - currentMin;
        double newRange = range / zoomFactor;

        domainAxis.setLowerBound(currentMin + (range - newRange) / 2);
        domainAxis.setUpperBound(currentMax - (range - newRange) / 2);

        double yRange = rangeAxis.getUpperBound() - rangeAxis.getLowerBound();
        double newYRange = yRange / zoomFactor;

        double newLowerBound = rangeAxis.getLowerBound() + (yRange - newYRange) / 2;
        double newUpperBound = rangeAxis.getUpperBound() - (yRange - newYRange) / 2;

        newLowerBound = Math.max(newLowerBound, 0); // Ensure lower bound is not less than 0

        rangeAxis.setLowerBound(newLowerBound);
        rangeAxis.setUpperBound(newUpperBound);
    }

    /**
     * Filters out OHLCData points that are not within the specified date range or have invalid price values.
     *
     * @param ohlcDataList the list of OHLCData points to be filtered
     * @param startDate    the start date of the date range
     * @param endDate      the end date of the date range
     * @return a list of valid OHLCData points within the specified date range
     */
    private static List<OHLCData> filterData(List<OHLCData> ohlcDataList, Date startDate, Date endDate) {
        List<OHLCData> filteredList = new ArrayList<>();
        for (OHLCData data : ohlcDataList) {
            Date date = data.getDate();
            // Check if the data is valid, for instance, by ensuring that open, high, low, and close prices are positive
            if (date != null && !date.before(startDate) && !date.after(endDate) &&
                    data.getOpen() > 0 && data.getHigh() > 0 && data.getLow() > 0 && data.getClose() > 0) {
                filteredList.add(data); // Only add valid data points
            }
        }
        filteredList.sort(Comparator.comparing(OHLCData::getDate));
        return filteredList;
    }

    /**
     * Processes raw stock data to generate a list of OHLCData points.
     *
     * @param stockRawDataProcess the object containing raw stock data and parsing logic
     * @return a list of OHLCData points extracted from the raw data
     * @throws Exception if an error occurs while processing the raw data
     */
    public static List<OHLCData> getProcessedStockData(StockRawDataProcess stockRawDataProcess) throws Exception {
        List<OHLCData> ohlcDataList = new ArrayList<>();

        try {
            String rawData = stockRawDataProcess.getRawData();
            String key = stockRawDataProcess.getTimeSeriesKey();

            JSONObject timeSeries = new JSONObject(rawData).getJSONObject(key);
            for (String dateStr : timeSeries.keySet()) {
                JSONObject dailyData = timeSeries.getJSONObject(dateStr);
                Date date = stockRawDataProcess.getDf().parse(dateStr);
                double open = dailyData.getDouble(Constants.OPEN_KEY);
                double high = dailyData.getDouble(Constants.HIGH_KEY);
                double low = dailyData.getDouble(Constants.LOW_KEY);
                double close = dailyData.getDouble(Constants.CLOSE_KEY);
                double volume = dailyData.getDouble(Constants.VOLUME_KEY);
                ohlcDataList.add(new OHLCData(date, open, high, low, close, volume));
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return ohlcDataList;
    }

    /**
     * Creates an OHLCDataset from the given stock symbol and list of OHLCData points.
     *
     * @param stockSymbol  the symbol for the stock
     * @param ohlcDataList the list of OHLCData points
     * @return an OHLCDataset for rendering the candlestick chart
     */
    public static OHLCDataset createOHLCDataset(String stockSymbol, List<OHLCData> ohlcDataList) {
        int itemCount = ohlcDataList.size();

        Date[] dates = new Date[itemCount];
        double[] highs = new double[itemCount];
        double[] lows = new double[itemCount];
        double[] opens = new double[itemCount];
        double[] closes = new double[itemCount];
        double[] volumes = new double[itemCount];

        for (int i = 0; i < itemCount; i++) {
            OHLCData data = ohlcDataList.get(i);
            dates[i] = data.getDate();
            highs[i] = data.getHigh();
            lows[i] = data.getLow();
            opens[i] = data.getOpen();
            closes[i] = data.getClose();
            volumes[i] = data.getVolume();
        }
        return new DefaultHighLowDataset(stockSymbol, dates, highs, lows, opens, closes, volumes);
    }
}
