package datafetcher;

import utils.Constants;
import models.TimeGranularity;
import models.TimeInterval;

public class MonthlyDataFetcher extends DataFetcher {
    public MonthlyDataFetcher(String apiKey, TimeInterval timeInterval) {
        super(apiKey, timeInterval);
    }

    @Override
    String getRequestUrl() {
        return String.format(Constants.DATA_API_URL, TimeGranularity.MONTHLY.toString(), api_key);
    }

    @Override
    String getTimeSeriesKey() {
        return "Monthly Time Series";
    }
}

