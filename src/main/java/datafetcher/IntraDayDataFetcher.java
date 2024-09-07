package datafetcher;

import utils.Constants;

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

    @Override
    String getTimeSeriesKey() {
        System.out.println("---timeinterval: ---" + "Time Series (" + timeInterval + ")");
        return "Time Series (" + timeInterval + ")";
    }
}