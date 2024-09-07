package datafetcher;

import utils.Constants;
import models.TimeGranularity;
import models.TimeInterval;

public class WeeklyDataFetcher extends DataFetcher {
    public WeeklyDataFetcher(String apiKey, TimeInterval timeInterval) {
        super(apiKey, timeInterval);
    }

    String getRequestUrl() {
        return String.format(Constants.DATA_API_URL, TimeGranularity.WEEKLY.toString(), api_key);
    }

    @Override
    String getTimeSeriesKey() {
        return "Weekly Time Series";
    }
}

