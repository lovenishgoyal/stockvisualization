package datafetcher;

import utils.Constants;

public class DailyDataFetcher extends DataFetcher {
    public DailyDataFetcher(String apiKey, String timeInterval) {
        super(apiKey,timeInterval);
    }

    @Override
    String getRequestUrl() {
        return String.format(Constants.DATA_API_URL, "DAILY", api_key)+"&outputsize=compact";
    }

    @Override
    String getTimeSeriesKey() {
        return "Time Series (Daily)";
    }
}
