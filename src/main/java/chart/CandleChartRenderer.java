package chart;

import models.OHLCData;
import models.StockRawDataProcess;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.OHLCDataset;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import utils.Helper;
import utils.TimeZoneDateFormat;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.swing.JPanel;

/**
 * @author lovenishgoyal
 * @version 1.0
 */

public class CandleChartRenderer {

    private static final Logger logger = LoggerFactory.getLogger(CandleChartRenderer.class);

    public static JPanel createCandleChart(StockRawDataProcess stockRawDataProcess, String stockName) throws Exception {

        List<OHLCData> ohlcDataList = getProcessedStockData(stockRawDataProcess);
        OHLCDataset dataset = Helper.convertToDataset(stockName, ohlcDataList);

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

        // Use a custom date format with the desired time zone
        TimeZoneDateFormat customDateFormat = new TimeZoneDateFormat("yyyy-MM-dd HH:mm", TimeZone.getDefault());
        dateAxis.setDateFormatOverride(customDateFormat);


        // Set default range for Y-axis to avoid out-of-bounds issue
        yAxis.setAutoRangeIncludesZero(true);

        // Configure renderer
        CandlestickRenderer renderer = new CandlestickRenderer();
        plot.setRenderer(renderer);

        // Enable zooming and panning
        plot.setDomainPannable(true); // Allow panning on x-axis
        plot.setRangePannable(true);  // Allow panning on y-axis


        // Set initial range for axes
        plot.getDomainAxis().setAutoRange(true);
        plot.getRangeAxis().setAutoRange(true);

        // Create and return a ChartPanel with the chart
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        chartPanel.setMouseWheelEnabled(true); // Enable zooming with mouse wheel
        chartPanel.setPreferredSize(new Dimension(800, 600));
        return chartPanel;
    }

    public static List<OHLCData> getProcessedStockData(StockRawDataProcess stockRawDataProcess) throws Exception {

        List<OHLCData> ohlcDataList = new ArrayList<>();

        try {
            String rawData = stockRawDataProcess.getRawData();
            String key = stockRawDataProcess.getTimeSeriesKey();

            logger.info("Key: "  + key);
            JSONObject timeSeries = new JSONObject(rawData).getJSONObject(key);
            logger.info("Time Series (Daily): " + timeSeries.toString());
            for (String dateStr : timeSeries.keySet()) {
                JSONObject dailyData = timeSeries.getJSONObject(dateStr);
                logger.info("Time Series dateStr : " + dateStr);
                logger.info("Time Series value: " + dailyData.toString());
                Date date = stockRawDataProcess.getDf().parse(dateStr);
                logger.info("date: " + date);
                logger.info("open: " + dailyData.getDouble(Constants.OPEN_KEY));
                double open = dailyData.getDouble(Constants.OPEN_KEY);
                double high = dailyData.getDouble(Constants.HIGH_KEY);
                double low = dailyData.getDouble(Constants.LOW_KEY);
                double close = dailyData.getDouble(Constants.CLOSE_KEY);
                double volume = dailyData.getDouble(Constants.VOLUME_KEY);

                ohlcDataList.add(new OHLCData(date, open, high, low, close, volume));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage());
            logger.error(e.toString());
        }
        logger.info("size" + ohlcDataList.size() );
        return ohlcDataList;
    }
}