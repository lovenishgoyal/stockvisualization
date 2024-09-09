package datafetcher;

/**
 * A factory class for creating instances of {@code DataFetcher} based on the specified granularity.
 * <p>
 * This class provides a method to obtain the appropriate {@code DataFetcher} implementation
 * based on the granularity of the data required, such as "INTRADAY", "DAILY", "WEEKLY", or "MONTHLY".
 * </p>
 *
 * @author lovenishgoyal
 * @version 1.0
 */
public class GetDataFetcherFactory {

    /**
     * Returns a {@code DataFetcher} instance based on the specified granularity and API parameters.
     * <p>
     * The granularity determines the type of {@code DataFetcher} to return:
     * <ul>
     *     <li>"INTRADAY" - Returns an instance of {@code IntraDayDataFetcher}.</li>
     *     <li>"DAILY" - Returns an instance of {@code DailyDataFetcher}.</li>
     *     <li>"WEEKLY" - Returns an instance of {@code WeeklyDataFetcher}.</li>
     *     <li>"MONTHLY" - Returns an instance of {@code MonthlyDataFetcher}.</li>
     * </ul>
     * If the granularity is {@code null} or does not match any known values, {@code null} is returned.
     * </p>
     *
     * @param granularity the granularity of the data ("INTRADAY", "DAILY", "WEEKLY", "MONTHLY")
     * @param api the API key used for authentication with the data provider
     * @param timeInterval the time interval for the data fetch, e.g., "daily", "weekly", etc.
     * @return a {@code DataFetcher} instance appropriate for the specified granularity, or {@code null} if no match is found
     */
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
