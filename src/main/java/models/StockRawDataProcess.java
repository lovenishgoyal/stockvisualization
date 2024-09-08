package models;

import java.text.DateFormat;

public class StockRawDataProcess {
    public StockRawDataProcess(String rawData, DateFormat df, String timeSeriesKey) {
        this.rawData = rawData;
        this.df = df;
        this.timeSeriesKey = timeSeriesKey;
    }

    String rawData;
    DateFormat df;
    String timeSeriesKey;

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public DateFormat getDf() {
        return df;
    }

    public void setDf(DateFormat df) {
        this.df = df;
    }

    public String getTimeSeriesKey() {
        return timeSeriesKey;
    }

    public void setTimeSeriesKey(String timeSeriesKey) {
        this.timeSeriesKey = timeSeriesKey;
    }


}
