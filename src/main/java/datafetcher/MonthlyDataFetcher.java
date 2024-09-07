package datafetcher;

import utils.Constants;

public class MonthlyDataFetcher extends DataFetcher {
    public MonthlyDataFetcher(String apiKey, String timeInterval) {
        super(apiKey, timeInterval);
    }

    @Override
    String getRequestUrl() {
        return String.format(Constants.DATA_API_URL, "MONTHLY", api_key);
    }

    @Override
    String getTimeSeriesKey() {
        return "Monthly Time Series";
    }
}

