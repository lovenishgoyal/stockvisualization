package datafetcher;

import utils.Constants;

public class WeeklyDataFetcher extends DataFetcher {
    public WeeklyDataFetcher(String apiKey, String timeInterval) {
        super(apiKey, timeInterval);
    }

    String getRequestUrl() {
        return String.format(Constants.DATA_API_URL, "WEEKLY", api_key);
    }

    @Override
    String getTimeSeriesKey() {
        return "Weekly Time Series";
    }
}

