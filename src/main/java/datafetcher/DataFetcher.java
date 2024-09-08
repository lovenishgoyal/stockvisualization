package datafetcher;

import models.OHLCData;
import models.StockRawDataProcess;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

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
    String timeInterval;

    private static final Logger logger = LoggerFactory.getLogger(DataFetcher.class);

    public DataFetcher(String apiKey, String timeInterval) {
        this.api_key = apiKey;
        this.timeInterval = timeInterval;
    }

    DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    abstract String getRequestUrl();

    abstract String getTimeSeriesKey();


    public StockRawDataProcess getStockData(String stockSymbol) throws Exception {

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
        logger.info("----Lovenish------");
        logger.info(response.toString());
        StockRawDataProcess dataProcess = new StockRawDataProcess(response.toString(), getDateFormat(), getTimeSeriesKey());
        return dataProcess;
    }


}
