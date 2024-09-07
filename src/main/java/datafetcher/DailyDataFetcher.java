package datafetcher;

import utils.Constants;
import models.TimeGranularity;
import models.TimeInterval;

public class DailyDataFetcher extends DataFetcher {
    public DailyDataFetcher(String apiKey, TimeInterval timeInterval) {
        super(apiKey,timeInterval);
    }

    @Override
    String getRequestUrl() {
        return String.format(Constants.DATA_API_URL, TimeGranularity.DAILY.toString(), api_key)+"&outputsize=compact";
    }


    @Override
    String getTimeSeriesKey() {
        return "Time Series (Daily)";
    }
}
