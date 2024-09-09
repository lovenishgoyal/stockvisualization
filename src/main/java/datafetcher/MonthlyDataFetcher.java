package datafetcher;

import utils.Constants;

/**
 * A {@code DataFetcher} implementation for fetching monthly stock data.
 * <p>
 * This class extends {@code DataFetcher} to specifically handle fetching and parsing of
 * monthly stock data from an external API. It formats the request URL to include the monthly
 * interval and provides the date format and time series key specific to monthly data.
 * </p>
 *
 * @author lovenishgoyal
 * @version 1.0
 */
public class MonthlyDataFetcher extends DataFetcher {

    /**
     * Constructs a {@code MonthlyDataFetcher} with the specified API key and time interval.
     *
     * @param apiKey the API key used for authentication with the data provider
     * @param timeInterval basically used to define the timeinterval
     */
    public MonthlyDataFetcher(String apiKey, String timeInterval, String granularity) {
        super(apiKey, timeInterval, granularity);
    }

    /**
     * Builds the request URL for fetching monthly stock data.
     * <p>
     * The URL is constructed by formatting the base API URL with "MONTHLY" and the API key.
     * The resulting URL is used to request data from the API.
     * </p>
     *
     * @return the request URL as a {@code String}
     */
    @Override
    String getRequestUrl() {
        return String.format(Constants.DATA_API_URL, "MONTHLY", api_key);
    }

    /**
     * Returns the time series key used in the API response for monthly data.
     * <p>
     * The key is "Monthly Time Series", which corresponds to the data structure in the API response
     * for monthly stock data.
     * </p>
     *
     * @return the time series key as a {@code String}
     */
    @Override
    String getTimeSeriesKey() {
        return "Monthly Time Series";
    }
}
