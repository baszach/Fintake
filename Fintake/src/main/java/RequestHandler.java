public class RequestHandler {

    private final FinancialDataAPI financialDataAPI;
    private final String symbol;
    private final int fromDate;
    private final int toDate;

    public RequestHandler(final FinancialDataAPI financialDataAPI, final String symbol, final int fromDate, final int toDate) {
        this.financialDataAPI = financialDataAPI;
        this.symbol = symbol;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String[] requestFinancialData() {
        return financialDataAPI.serveFinancialData(symbol, fromDate, toDate);
    }

}
