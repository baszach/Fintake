package api;

public enum SupportedApi {

    AlphaVantage {
        public TitledSourceUrl[] createRequestUrls(String symbol, String apiKey) {
            TitledSourceUrl overview = TitledSourceUrl.from(
                    "overview",
                    "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" + symbol + "&apikey=" + apiKey);
            TitledSourceUrl incomeStatement = TitledSourceUrl.from(
                    "incomeStatement",
                    "https://www.alphavantage.co/query?function=INCOME_STATEMENT&symbol=" + symbol + "&apikey=" + apiKey);
            TitledSourceUrl balanceSheet = TitledSourceUrl.from(
                    "balanceSheet",
                    "https://www.alphavantage.co/query?function=BALANCE_SHEET&symbol=" + symbol + "&apikey=" + apiKey);
            TitledSourceUrl earnings = TitledSourceUrl.from(
                    "earnings",
                    "https://www.alphavantage.co/query?function=EARNINGS&symbol=" + symbol + "&apikey=" + apiKey);

            return new TitledSourceUrl[]{overview, incomeStatement, balanceSheet, earnings};
        }
    },

    YahooFinance {
        public TitledSourceUrl[] createRequestUrls(String symbol, String apiKey) {
            return null;
        }
    };

    public boolean isApi(String apiName) {
        return apiName.equals(this.name());
    }

    public abstract TitledSourceUrl[] createRequestUrls(String symbol, String apiKey);
}
