package api;

public abstract class ApiFactory {

    public static final String ALPHA_VANTAGE = "AlphaVantage";
    public static final String YAHOO_FINANCE = "YahooFinance";
    public static final String OTHER = "other";

    public static FinancialDataAPI buildApi(String api) {
        switch (api) {
            case ALPHA_VANTAGE: return new AlphaVantageAPI();
            case YAHOO_FINANCE: return new YahooFinanceAPI();
            default: return new AlphaVantageAPI();
        }
    }

}
