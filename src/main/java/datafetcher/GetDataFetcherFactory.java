package datafetcher;

import models.TimeGranularity;
import models.TimeInterval;

public class GetDataFetcherFactory {
    public DataFetcher getDataFetcher(TimeGranularity granularity, String api, TimeInterval timeInterval) {
        if (granularity == null) {
            return null;
        }
        if (granularity.equals(TimeGranularity.INTRADAY)) {
            return new IntraDayDataFetcher(api, timeInterval);
        } else if (granularity.equals(TimeGranularity.DAILY)) {
            return new DailyDataFetcher(api, timeInterval);
        } else if (granularity.equals(TimeGranularity.WEEKLY)) {
            return new WeeklyDataFetcher(api, timeInterval);
        } else if (granularity.equals(TimeGranularity.MONTHLY)) {
            return new MonthlyDataFetcher(api, timeInterval);
        }
        return null;
    }
}
