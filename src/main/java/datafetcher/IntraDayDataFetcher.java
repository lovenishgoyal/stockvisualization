package datafetcher;

import utils.Constants;
import models.TimeGranularity;
import models.TimeInterval;

public class IntraDayDataFetcher extends DataFetcher {

    public IntraDayDataFetcher(String apiKey,TimeInterval timeInterval) {
        super(apiKey, timeInterval);
    }

    @Override
    String getRequestUrl() {
        String requestUrl = String.format(Constants.DATA_API_URL, TimeGranularity.INTRADAY.toString(), api_key);
        requestUrl = requestUrl.concat("&interval=" + timeInterval.getDisplayName());
        return requestUrl;
    }

    @Override
    String getTimeSeriesKey() {
        return "Time Series(" + timeInterval.getDisplayName() +")" ;
    }
}
