package stockVisualization;

import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import models.OHLCData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.OHLCDataset;
import org.json.JSONObject;
import utils.Constants;
import models.TimeGranularity;
import utils.Helper;
import utils.TimeZoneDateFormat;

import javax.swing.*;

public class StockDataFetcher {

    private static final String API_KEY = "XTN5B7134EVRGGHK";
    private static final String API_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&apikey=XTN5B7134EVRGGHK&symbol=ibm";

    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws Exception {
        JSONObject result = getStockData("IBM");
        parseStockData(result.toString());
        System.out.println(result.keySet().size());
        System.out.println(getStockData("IBM"));
    }

    // "Weekly Time Series"
    // "Time Series (Daily)"
    // "Monthly Time Series"
    // "Time Series (1min)"


    public static JSONObject getStockData(String stockSymbol) throws Exception {

        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return new JSONObject(response.toString());
    }

    public static List<OHLCData> parseStockData(String jsonObject) {
        List<OHLCData> ohlcDataList = new ArrayList<>();

        try {
            String key = "Time Series (Daily)";
            JSONObject timeSeries = new JSONObject(jsonObject).getJSONObject(key);
            for (String dateStr : timeSeries.keySet()) {
                JSONObject dailyData = timeSeries.getJSONObject(dateStr);

                Date date = dateFormat.parse(dateStr);
                double open = dailyData.getDouble(Constants.OPEN_KEY);
                double high = dailyData.getDouble(Constants.HIGH_KEY);
                double low = dailyData.getDouble(Constants.LOW_KEY);
                double close = dailyData.getDouble(Constants.CLOSE_KEY);
                double volume = dailyData.getDouble(Constants.VOLUME_KEY);

                ohlcDataList.add(new OHLCData(date, open, high, low, close, volume));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return ohlcDataList;
    }

    public static JPanel createChart(List<OHLCData> ohlcDataList, String stockName) {
        try {
            OHLCDataset dataset = Helper.convertToDataset(stockName, ohlcDataList);

            JFreeChart chart = ChartFactory.createCandlestickChart(
                    "Candlestick Chart for " + stockName,
                    "Time",
                    "Price",
                    dataset,
                    true
            );

            // Access the XYPlot, which contains the axes
            XYPlot plot = (XYPlot) chart.getPlot();

            // Access the Y-axis (price axis) and set its lower bound to 0
            NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
            yAxis.setLowerBound(0);  // Set Y-axis to start from 0

            DateAxis dateAxis = (DateAxis) plot.getDomainAxis();

            // Use a custom date format with the desired time zone
            TimeZoneDateFormat customDateFormat = new TimeZoneDateFormat("MM-dd HH:mm", TimeZone.getTimeZone("America/New_York"));
            dateAxis.setDateFormatOverride(customDateFormat);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(800, 600));
            chartPanel.setMouseWheelEnabled(true);
            chartPanel.setDomainZoomable(true);
            chartPanel.setRangeZoomable(true);
            chartPanel.setRangeZoomable(true);
            chartPanel.setZoomAroundAnchor(true);
            chartPanel.setFillZoomRectangle(true);

            CandlestickRenderer renderer = new CandlestickRenderer();
            plot.setRenderer(renderer);

            plot.setDomainPannable(true); // Allow panning
            plot.setRangePannable(true);  // Allow panning

            // Set initial range for axes
            plot.getDomainAxis().setAutoRange(true);
            plot.getRangeAxis().setAutoRange(true);

            // chartPanel.setMouseZoomable(true);

            return chartPanel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

