package models;

/**
 * @author lovenishgoyal
 * @version 1.0
 */
public enum TimeInterval {
    min_1("1min"),
    min_5("5min"),
    min_15("15min"),
    min_30("30min"),
    min_60("60min");

    private final String timeInterval;

    TimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getDisplayName() {
        return timeInterval;
    }

}
