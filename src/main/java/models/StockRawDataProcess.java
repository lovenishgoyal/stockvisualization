package models;

import java.text.DateFormat;

/**
 * Represents a container for raw stock data and related metadata used for processing.
 * <p>
 * This class holds the raw data fetched from an external source, the date format used for parsing
 * the data, and the key to access the specific time series data within the raw response.
 * </p>
 *
 * @author lovenishgoyal
 * @version 1.0
 */
public class StockRawDataProcess {

    private String rawData;
    private DateFormat df;
    private String timeSeriesKey;

    /**
     * Constructs a {@code StockRawDataProcess} instance with the specified raw data, date format, and time series key.
     *
     * @param rawData the raw data as a JSON string
     * @param df the date format used to parse the date strings in the raw data
     * @param timeSeriesKey the key to access the time series data in the raw data
     */
    public StockRawDataProcess(String rawData, DateFormat df, String timeSeriesKey) {
        this.rawData = rawData;
        this.df = df;
        this.timeSeriesKey = timeSeriesKey;
    }

    /**
     * Returns the raw data as a JSON string.
     *
     * @return the raw data
     */
    public String getRawData() {
        return rawData;
    }

    /**
     * Sets the raw data as a JSON string.
     *
     * @param rawData the raw data to set
     */
    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    /**
     * Returns the date format used to parse the date strings in the raw data.
     *
     * @return the date format
     */
    public DateFormat getDf() {
        return df;
    }

    /**
     * Sets the date format used to parse the date strings in the raw data.
     *
     * @param df the date format to set
     */
    public void setDf(DateFormat df) {
        this.df = df;
    }

    /**
     * Returns the key used to access the time series data in the raw data.
     *
     * @return the time series key
     */
    public String getTimeSeriesKey() {
        return timeSeriesKey;
    }

    /**
     * Sets the key used to access the time series data in the raw data.
     *
     * @param timeSeriesKey the time series key to set
     */
    public void setTimeSeriesKey(String timeSeriesKey) {
        this.timeSeriesKey = timeSeriesKey;
    }
}
