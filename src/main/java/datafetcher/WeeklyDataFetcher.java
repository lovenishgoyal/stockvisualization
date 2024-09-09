package datafetcher;

import utils.Constants;

/**
 * A {@code DataFetcher} implementation for fetching weekly stock data.
 * <p>
 * This class extends {@code DataFetcher} to specifically handle fetching and parsing of
 * weekly stock data from an external API. It formats the request URL to include the weekly
 * interval and provides the date format and time series key specific to weekly data.
 * </p>
 *
 * @author lovenishgoyal
 * @version 1.0
 */
public class WeeklyDataFetcher extends DataFetcher {

    /**
     * Constructs a {@code WeeklyDataFetcher} with the specified API key and time interval.
     *
     * @param apiKey the API key used for authentication with the data provider
     * @param timeInterval to use time interval in api
     */
    public WeeklyDataFetcher(String apiKey, String timeInterval) {
        super(apiKey, timeInterval);
    }

    /**
     * Builds the request URL for fetching weekly stock data.
     * <p>
     * The URL is constructed by formatting the base API URL with "WEEKLY" and the API key.
     * The resulting URL is used to request data from the API.
     * </p>
     *
     * @return the request URL as a {@code String}
     */
    @Override
    String getRequestUrl() {
        return String.format(Constants.DATA_API_URL, "WEEKLY", api_key);
    }

    /**
     * Returns the time series key used in the API response for weekly data.
     * <p>
     * The key is "Weekly Time Series", which corresponds to the data structure in the API response
     * for weekly stock data.
     * </p>
     *
     * @return the time series key as a {@code String}
     */
    @Override
    String getTimeSeriesKey() {
        return "Weekly Time Series";
    }
}
