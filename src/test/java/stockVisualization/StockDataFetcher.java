package stockVisualization;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.json.JSONObject;

public class StockDataFetcher {

    private static final String API_KEY = "XTN5B7134EVRGGHK";
    private static final String API_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&apikey=XTN5B7134EVRGGHK&outputsize=compact&symbol=TSLA";

    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws Exception {
        JSONObject result = getStockData("IBM");
        System.out.println(result.keySet().size());
        System.out.println(getStockData("IBM"));
    }

    public static JSONObject getStockData(String stockSymbol) throws Exception {

        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return new JSONObject(response.toString());
    }
}

