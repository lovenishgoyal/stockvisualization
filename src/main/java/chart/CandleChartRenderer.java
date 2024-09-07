package chart;

import datafetcher.DataFetcher;
import models.OHLCData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Helper;
import utils.TimeZoneDateFormat;
import org.jfree.chart.renderer.xy.CandlestickRenderer;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JPanel;

/**
 * @author lovenishgoyal
 * @version 1.0
 */

public class CandleChartRenderer {

    private static final Logger logger = LoggerFactory.getLogger(CandleChartRenderer.class);

    public static JPanel createChart(List<OHLCData> ohlcDataList, String stockName) {

        logger.info("size:" + ohlcDataList.size());
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
        TimeZoneDateFormat customDateFormat = new TimeZoneDateFormat("MM-dd HH:mm", TimeZone.getTimeZone("America/New_York"));
        dateAxis.setDateFormatOverride(customDateFormat);

        // Set default range for Y-axis to avoid out-of-bounds issue
        yAxis.setAutoRangeIncludesZero(true);

        // Configure renderer
        CandlestickRenderer renderer = new CandlestickRenderer();
        plot.setRenderer(renderer);

        // Enable zooming and panning
        plot.setDomainPannable(true); // Allow panning on x-axis
        plot.setRangePannable(true);  // Allow panning on y-axis
        //plot.setDomainZoomable(true); // Enable zooming on x-axis
        //plot.setRangeZoomable(true);  // Enable zooming on y-axis

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


    private static OHLCDataset createDataset(String stockName, String granularity, String interval) {
        // Simulate stock data for demo purposes
        Date[] dates = new Date[5];
        dates[0] = new Date(2024, 07, 1);
        dates[1] = new Date(2024, 07, 2);
        dates[2] = new Date(2024, 07, 3);
        dates[3] = new Date(2024, 07, 4);
        dates[4] = new Date(2024, 07, 5);
        double[] highs = {50, 55, 54, 35, 56};
        double[] lows = {30, 30, 15, 30, 49};
        double[] opens = {48, 49, 54, 52, 53};
        double[] closes = {50, 40, 45, 54, 55};
        double[] volumes = {1000, 1200, 1500, 1100, 900};

        return new DefaultHighLowDataset(stockName, dates, highs, lows, opens, closes, volumes);
    }
}