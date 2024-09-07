package datafetcher;

import models.OHLCData;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import models.TimeInterval;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lovenishgoyal
 * @version 1.0
 */

public abstract class DataFetcher {

    String api_key;
    TimeInterval timeInterval;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger logger = LoggerFactory.getLogger(DataFetcher.class);

    public DataFetcher(String apiKey, TimeInterval timeInterval) {
        this.api_key = apiKey;
        this.timeInterval = timeInterval;
    }

    abstract String getRequestUrl();

    abstract String getTimeSeriesKey();

    public String getStockData(String stockSymbol) throws Exception {

        String requestUrl = getRequestUrl() + "&symbol=" + stockSymbol;
        logger.info("requestUrl" + requestUrl);
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("output: " + response.toString());
        System.out.println("output--------: ");
        System.out.println("output--------: ");

        System.out.println("output--------: ");

        System.out.println("output--------: ");
        System.out.println("output--------: ");
        System.out.println("output--------: ");
        System.out.println("output--------: ");
        System.out.println("output--------: ");
        System.out.println("output--------: ");

        return response.toString();
    }

    public List<OHLCData> parseStockData(String jsonObject) {
        List<OHLCData> ohlcDataList = new ArrayList<>();

        logger.info("jsonObject: " + jsonObject.toString());
        logger.info("Success: ");

        try {
            String key = getTimeSeriesKey();
            logger.info("Key: "  + key);
            JSONObject timeSeries = new JSONObject(jsonObject).getJSONObject(key);
            logger.info("Time Series (Daily): " + timeSeries.toString());
            for (String dateStr : timeSeries.keySet()) {
                JSONObject dailyData = timeSeries.getJSONObject(dateStr);
                logger.info("Time Series dateStr : " + dateStr);
                logger.info("Time Series value: " + dailyData.toString());
                Date date = dateFormat.parse(dateStr);
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

        }
        logger.info("size" + ohlcDataList.size() );
        return ohlcDataList;
    }

    public static OHLCDataset getStockData(String stockSymbol, String a) {
        Date[] dates = new Date[5];
        double[] highs = {50, 55, 54, 53, 56};
        double[] lows = {45, 46, 47, 48, 49};
        double[] opens = {48, 49, 51, 52, 53};
        double[] closes = {50, 53, 52, 54, 55};
        double[] volumes = {1000, 1200, 1500, 1100, 900};

        // Create a dataset
        DefaultHighLowDataset dataset = new DefaultHighLowDataset(
                stockSymbol,
                dates,
                highs,
                lows,
                opens,
                closes,
                volumes
        );
        return dataset;
    }

}
