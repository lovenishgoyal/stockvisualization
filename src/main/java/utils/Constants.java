package utils;

import java.text.SimpleDateFormat;

/**
 * A utility class that defines constants used throughout the application.
 * <p>
 * This class provides constant values for API URLs, data keys, time granularity options,
 * time interval options, and chart types. These constants are used to configure API requests
 * and to standardize various options across different components of the application.
 * </p>
 *
 * @author lovenishgoyal
 * @version 1.0
 */
public class Constants {

    /**
     * The base URL for querying the Alpha Vantage API.
     * <p>
     * This URL is used to fetch time series data by substituting the function type (e.g., "DAILY")
     * and API key in the URL template.
     * </p>
     */
    public static final String DATA_API_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_%s&apikey=%s";

    public static final String SYMBOL_SEARCH_URL = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&apikey=%s";

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * The key for the opening price in the API response.
     */
    public static final String OPEN_KEY = "1. open";

    /**
     * The key for the highest price in the API response.
     */
    public static final String HIGH_KEY = "2. high";

    /**
     * The key for the lowest price in the API response.
     */
    public static final String LOW_KEY = "3. low";

    /**
     * The key for the closing price in the API response.
     */
    public static final String CLOSE_KEY = "4. close";

    /**
     * The key for the trading volume in the API response.
     */
    public static final String VOLUME_KEY = "5. volume";

    /**
     * An array of options for time granularity.
     * <p>
     * These options determine the frequency of data points, such as daily, weekly, monthly, or intraday.
     * </p>
     */
    public static final String[] TIME_GRANULARITY_OPTIONS = {"Daily", "Weekly", "Monthly", "Intraday"};

    /**
     * An array of options for time intervals used in intraday data fetching.
     * <p>
     * These options specify the granularity of intraday data, such as 1 minute, 5 minutes, etc.
     * </p>
     */
    public static final String[] TIME_INTERVAL_OPTIONS = {"1min", "5min", "15min", "30min", "60min"};

    /**
     * An array of options for chart types.
     * <p>
     * These options specify the type of charts that can be generated, such as candlestick or line charts.
     * </p>
     */
    public static final String[] CHART_OPTIONS = {"Candle", "Line"};

    /**
     * The string constant for intraday time granularity.
     */
    public static final String INTRADAY = "INTRADAY";

    /**
     * The string constant for daily time granularity.
     */
    public static final String DAILY = "DAILY";

    /**
     * The string constant for weekly time granularity.
     */
    public static final String WEEKLY = "WEEKLY";

    /**
     * The string constant for monthly time granularity.
     */
    public static final String MONTHLY = "MONTHLY";
}
