package datafetcher;

import utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A {@code DataFetcher} implementation for fetching intraday stock data.
 * <p>
 * This class extends {@code DataFetcher} to specifically handle fetching and parsing of
 * intraday stock data from an external API. It formats the request URL to include the intraday
 * interval and provides the date format and time series key specific to intraday data.
 * </p>
 *
 * @author lovenishgoyal
 * @version 1.0
 */
public class IntraDayDataFetcher extends DataFetcher {

    /**
     * Constructs an {@code IntraDayDataFetcher} with the specified API key and time interval.
     *
     * @param apiKey the API key used for authentication with the data provider
     * @param timeInterval the time interval for intraday data, e.g., "1min", "5min", etc.
     */
    public IntraDayDataFetcher(String apiKey, String timeInterval,String granularity) {
        super(apiKey, timeInterval,granularity);
    }

    /**
     * Builds the request URL for fetching intraday stock data.
     * <p>
     * The URL is constructed by formatting the base API URL with "INTRADAY" and the API key,
     * and appending the specified time interval. The resulting URL is used to request data
     * from the API.
     * </p>
     *
     * @return the request URL as a {@code String}
     */
    @Override
    String getRequestUrl() {
        String requestUrl = String.format(Constants.DATA_API_URL, "INTRADAY", api_key);
        requestUrl = requestUrl.concat("&interval=" + timeInterval);
        return requestUrl;
    }

    /**
     * Provides the date format used for parsing intraday data timestamps.
     * <p>
     * For intraday data, the format is "yyyy-MM-dd HH:mm:ss" to accommodate the more granular
     * time intervals.
     * </p>
     *
     * @return a {@code DateFormat} instance configured for intraday data
     */
    @Override
    DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Returns the time series key used in the API response for intraday data.
     * <p>
     * The key is dynamically generated based on the specified time interval, allowing the
     * correct data to be extracted from the API response.
     * </p>
     *
     * @return the time series key as a {@code String}
     */
    @Override
    String getTimeSeriesKey() {
        return "Time Series (" + timeInterval + ")";
    }
}
