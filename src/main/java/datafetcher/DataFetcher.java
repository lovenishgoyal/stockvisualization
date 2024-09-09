package datafetcher;

import models.StockRawDataProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * @author lovenishgoyal
 * @version 1.0
 */

public abstract class DataFetcher {

    String api_key;
    String timeInterval;

    String timeGranularity;

    private static final Logger logger = LoggerFactory.getLogger(DataFetcher.class);

    public DataFetcher(String apiKey, String timeInterval, String granularity) {
        this.api_key = apiKey;
        this.timeInterval = timeInterval;
        this.timeGranularity = granularity;
    }

    DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    abstract String getRequestUrl();

    abstract String getTimeSeriesKey();


    public StockRawDataProcess getStockData(String stockSymbol) throws Exception {
        String requestUrl = getRequestUrl() + "&symbol=" + stockSymbol;
        logger.info("Request URL: " + requestUrl);

        HttpURLConnection connection = null;
        BufferedReader in = null;
        StringBuffer response = new StringBuffer();

        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            logger.info("Response Code: " + responseCode);

            // Check if the response code is not 200 (OK)
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("Failed to fetch stock data. HTTP Error code: " + responseCode);
            }

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            // Check for API rate limit exceeded error in the response
            if (response.toString().contains("Our standard API rate limit is 25 requests per day")) {
                throw new Exception("API rate limit exceeded: 25 requests per day.");
            }

        } catch (MalformedURLException e) {
            logger.error("Invalid URL: " + requestUrl, e);
            throw new Exception("Invalid URL provided.");
        } catch (IOException e) {
            logger.error("Network error or API is unreachable: " + e.getMessage(), e);
            throw new Exception("Failed to connect to the API. Check your network or API availability.");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("Failed to close BufferedReader: " + e.getMessage(), e);
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        // Create the StockRawDataProcess object with the received data
        return new StockRawDataProcess(response.toString(), getDateFormat(), getTimeSeriesKey(), timeGranularity, timeInterval);
    }

}
