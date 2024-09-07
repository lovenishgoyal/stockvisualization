package models;

import java.util.Date;

public class OHLCData {
    private final Date date;
    private final Double open;
    private final Double high;
    private final Double low;
    private final Double close;
    private final Double volume;

    public OHLCData(Date date, double open, double high, double low, double close, double volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    // Getters for the fields
    public Date getDate() {
        return date;
    }

    public Double getOpen() {
        return open;
    }

    public Double getHigh() {
        return high;
    }

    public Double getLow() {
        return low;
    }

    public Double getClose() {
        return close;
    }

    public Double getVolume() {
        return volume;
    }
}