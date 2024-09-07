package datafetcher;

public class GetDataFetcherFactory {
    public DataFetcher getDataFetcher(String granularity, String api, String timeInterval) {
        if (granularity == null) {
            return null;
        }
        if (granularity.equals("INTRADAY")) {
            return new IntraDayDataFetcher(api, timeInterval);
        } else if (granularity.equalsIgnoreCase("DAILY")) {
            return new DailyDataFetcher(api, timeInterval);
        } else if (granularity.equals("WEEKLY")) {
            return new WeeklyDataFetcher(api, timeInterval);
        } else if (granularity.equals("MONTHLY")) {
            return new MonthlyDataFetcher(api, timeInterval);
        }
        return null;
    }
}
