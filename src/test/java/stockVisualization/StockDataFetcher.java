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

import chart.CandleChartRenderer;
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
import utils.Helper;
import utils.TimeZoneDateFormat;

import javax.swing.*;

public class StockDataFetcher {

    private static final String API_KEY = "XTN5B7134EVRGGHK";
    private static final String API_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&apikey=XTN5B7134EVRGGHK&outputsize=compact&symbol=TSLA";

    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws Exception {
        JSONObject result = getStockData("IBM");
        System.out.println(result.keySet().size());
        System.out.println(getStockData("IBM"));
    }

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
}

