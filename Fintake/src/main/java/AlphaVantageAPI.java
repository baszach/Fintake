public class AlphaVantageAPI implements FinancialDataAPI {

    public String[] serveFinancialData(String symbol, int fromDate, int toDate) {
        HttpHandler httpHandler = new HttpHandler();
        String overview = httpHandler.makeServiceCall("https://www.alphavantage.co/query?function=OVERVIEW&symbol=IBM&apikey=demo");
        String incomeStatement = httpHandler.makeServiceCall("https://www.alphavantage.co/query?function=INCOME_STATEMENT&symbol=IBM&apikey=demo");
        String balanceSheet = httpHandler.makeServiceCall("https://www.alphavantage.co/query?function=BALANCE_SHEET&symbol=IBM&apikey=demo");
        String earnings = httpHandler.makeServiceCall("https://www.alphavantage.co/query?function=EARNINGS&symbol=IBM&apikey=demo");

        String[] data = new String[]{overview, earnings, incomeStatement, balanceSheet};
        return data;
    }

}
