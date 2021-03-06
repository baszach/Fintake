package api;

public abstract class ApiFactory {

    public static final String ALPHA_VANTAGE = "alpha";
    public static final String YAHOO = "yahoo";
    public static final String OTHER = "other";

    public static FinancialDataAPI buildApi(String api) {
        switch (api) {
            case ALPHA_VANTAGE: return new AlphaVantageAPI();
            default: return new AlphaVantageAPI();
        }
    }

}
