package models;

import java.util.Date;

/**
 * Represents OHLC (Open, High, Low, Close) data for a specific date.
 * <p>
 * This class encapsulates the OHLC data along with the trading volume for a given date.
 * It is used to represent financial data points which are essential for creating various
 * types of stock charts such as candlestick charts.
 * </p>
 *
 * @author lovenishgoyal
 * @version 1.0
 */
public class OHLCData {

    private final Date date;
    private final Double open;
    private final Double high;
    private final Double low;
    private final Double close;
    private final Double volume;

    /**
     * Constructs an {@code OHLCData} instance with the specified values.
     *
     * @param date the date of the OHLC data
     * @param open the opening price
     * @param high the highest price
     * @param low the lowest price
     * @param close the closing price
     * @param volume the trading volume
     */
    public OHLCData(Date date, double open, double high, double low, double close, double volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    /**
     * Returns the date of the OHLC data.
     *
     * @return the date as a {@code Date}
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the opening price of the stock.
     *
     * @return the opening price as a {@code Double}
     */
    public Double getOpen() {
        return open;
    }

    /**
     * Returns the highest price of the stock during the trading period.
     *
     * @return the highest price as a {@code Double}
     */
    public Double getHigh() {
        return high;
    }

    /**
     * Returns the lowest price of the stock during the trading period.
     *
     * @return the lowest price as a {@code Double}
     */
    public Double getLow() {
        return low;
    }

    /**
     * Returns the closing price of the stock.
     *
     * @return the closing price as a {@code Double}
     */
    public Double getClose() {
        return close;
    }

    /**
     * Returns the trading volume of the stock.
     *
     * @return the trading volume as a {@code Double}
     */
    public Double getVolume() {
        return volume;
    }
}
