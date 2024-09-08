package models;

public class StockVolumeData {
    private String date;
    private long volume;

    public StockVolumeData(String date, long volume) {
        this.date = date;
        this.volume = volume;
    }

    public String getDate() {
        return date;
    }

    public long getVolume() {
        return volume;
    }
}