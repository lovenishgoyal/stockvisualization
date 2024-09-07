package utils;

import models.OHLCData;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Helper {

    public static OHLCDataset createOHLCDataset(String stockSymbol, List<OHLCData> ohlcDataList) {
        int itemCount = ohlcDataList.size();

        Date[] dates = new Date[itemCount];
        double[] highs = new double[itemCount];
        double[] lows = new double[itemCount];
        double[] opens = new double[itemCount];
        double[] closes = new double[itemCount];
        double[] volumes = new double[itemCount];

        for (int i = 0; i < itemCount; i++) {
            OHLCData data = ohlcDataList.get(i);
            dates[i] = data.getDate();
            highs[i] = data.getHigh();
            lows[i] = data.getLow();
            opens[i] = data.getOpen();
            closes[i] = data.getClose();
            volumes[i] = data.getVolume();
        }
        return new DefaultHighLowDataset(stockSymbol, dates, highs, lows, opens, closes, volumes);
    }

    public static OHLCDataset convertToDataset(String stockName, List<OHLCData> ohlcDataList) {
        // List to store the filtered data
        List<OHLCData> filteredData = new ArrayList<OHLCData>();

        // Loop through the data and filter out missing dates or values
        for (OHLCData data : ohlcDataList) {
            if (data.getDate() != null && data.getOpen() != null &&
                    data.getHigh() != null && data.getLow() != null &&
                    data.getClose() != null && data.getVolume() != null) {
                filteredData.add(data);  // Add valid data to the filtered list
            }
        }

        // Convert the filtered data to OHLCDataset
        return createOHLCDataset(stockName, filteredData);
    }
}
