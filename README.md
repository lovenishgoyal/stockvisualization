# **Stock Candle Visualization Tool**

The Stock Candle Visualization Tool is a Java-based application designed to visualize stock price data using candlestick charts. It supports both real-time and historical data, providing an intuitive interface for traders and analysts to analyze stock trends and make informed decisions. The tool integrates with financial APIs to fetch stock data and includes functionality for detailed data exploration.

## Features:

#### **Real-time and Historical Data:**

Fetch and visualize stock price data for various time intervals, including daily, weekly, and intraday.

#### **Candlestick Charts:**

Visualize stock prices using candlestick charts, displaying key metrics such as open, high, low, and close values for each time interval.

#### **Zoom Functionality:**

###### **Zoom In/Out Buttons:** 
- Easily zoom in or out using the "Zoom In" and "Zoom Out" buttons located beneath the chart.
###### Mouse Click Zoom:
- Quickly zoom in or out by clicking directly on the chart.

###### Selection-Based Zoom:
- Select specific regions of the chart with your mouse to zoom in on the highlighted area.
This zoom functionality allows for detailed analysis of stock movements and trends.

**Customizable Time Granularity:**
View stock data on daily, weekly, monthly, or intraday intervals, depending on your analysis needs.

**API Integration:**
Fetch stock data from financial APIs (e.g., Alpha Vantage) for accurate and up-to-date information.

**Line Chart Option:**
Includes a Line Chart option for testing purposes, providing an additional way to visualize stock price trends.

## Getting Started

1. Clone the Repository
        `git clone https://github.com/yourusername/stock-candle-visualization-tool.git`
2. For Configure API Integration Update `config.properties` file with your api key
3. Build the Project Navigate to the project directory and build the project using Maven.
4. Run the Application Execute the main class(StockVToolController) to start the application and begin visualizing stock data.
You can run command inside the project directory `mvn clean javafx:run`

## Dependencies
* JavaFX or Swing for the user interface
* JFreeChart for chart rendering
* Financial APIs (e.g., Alpha Vantage) for data fetching