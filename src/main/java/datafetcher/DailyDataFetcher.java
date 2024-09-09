package datafetcher;

import utils.Constants;

/**
 * A subclass of {@link DataFetcher} that handles fetching daily stock data from an API.
 * <p>
 * This class constructs the API request URL for fetching daily stock data and specifies the
 * key used to extract the time series data from the API response.
 * </p>
 *
 * @author lovenishgoyal
 * @version 1.0
 */
public class DailyDataFetcher extends DataFetcher {

    /**
     * Constructs a {@code DailyDataFetcher} instance with the specified API key and time interval.
     *
     * @param apiKey the API key used for authentication with the data provider
     * @param timeInterval the time interval for the data fetch, e.g., "1 min, 2 mins"
     */
    public DailyDataFetcher(String apiKey, String timeInterval, String granularity) {
        super(apiKey, timeInterval,granularity);
    }

    /**
     * Constructs the request URL for fetching daily stock data from the API.
     *
     * @return the URL string for the daily stock data API request
     */
    @Override
    String getRequestUrl() {
        return String.format(Constants.DATA_API_URL, "DAILY", api_key) + "&outputsize=compact";
    }

    /**
     * Returns the key used to extract the daily time series data from the API response.
     *
     * @return the key for accessing daily time series data
     */
    @Override
    String getTimeSeriesKey() {
        return "Time Series (Daily)";
    }
}
