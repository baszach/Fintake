package api;

public enum FinancialApi {

    AlphaVantage {
        public String[] createRequestUrls(String symbol, String apiKey) {
            String overview = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" + symbol + "&apikey=" + apiKey;
            String incomeStatement = "https://www.alphavantage.co/query?function=INCOME_STATEMENT&symbol=" + symbol + "&apikey=" + apiKey;
            String balanceSheet = "https://www.alphavantage.co/query?function=BALANCE_SHEET&symbol=" + symbol + "&apikey=" + apiKey;
            String earnings = "https://www.alphavantage.co/query?function=EARNINGS&symbol=" + symbol + "&apikey=" + apiKey;

            return new String[]{overview, incomeStatement, balanceSheet, earnings};
        }
    },

    YahooFinance {
        public String[] createRequestUrls(String symbol, String apiKey) {
            return new String[0];
        }
    },

    OtherApi {
        public String[] createRequestUrls(String symbol, String apiKey) {
            return new String[0];
        }
    };

    public boolean isApi(String apiName) {
        return apiName.equals(this.name());
    }

    public abstract String[] createRequestUrls(String symbol, String apiKey);
}
