package utils;

/**
 * @author lovenishgoyal
 * @version 1.0
 */
public class Constants {

    public static final String DATA_API_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_%s&apikey=%s";
    public static final String OPEN_KEY = "1. open";
    public static final String HIGH_KEY = "2. high";
    public static final String LOW_KEY = "3. low";
    public static final String CLOSE_KEY = "4. close";
    public static final String VOLUME_KEY = "5. volume";

    public static final String[] TIME_GRANULARITY_OPTIONS = {"Daily", "Weekly", "Monthly", "Intraday"};

    public static final String[] TIME_INTERVAL_OPTIONS = {"1min", "5min", "15min", "30min", "60min"};

    public static final String[] CHART_OPTIONS = {"Candle", "Line"};

}
