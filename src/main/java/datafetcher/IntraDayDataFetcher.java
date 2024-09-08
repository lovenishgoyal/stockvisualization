package datafetcher;

import utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class IntraDayDataFetcher extends DataFetcher {

    public IntraDayDataFetcher(String apiKey, String timeInterval) {
        super(apiKey, timeInterval);
    }

    @Override
    String getRequestUrl() {
        String requestUrl = String.format(Constants.DATA_API_URL, "INTRADAY", api_key);
        requestUrl = requestUrl.concat("&interval=" + timeInterval);
        return requestUrl;
    }

    DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    String getTimeSeriesKey() {
        System.out.println("---timeinterval: ---" + "Time Series (" + timeInterval + ")");
        return "Time Series (" + timeInterval + ")";
    }
}