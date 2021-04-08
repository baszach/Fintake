package api;

public class ApiController {

    private String symbol;
    private String apiKey;
    private String apiName;

    public ApiController() {
        symbol = "";
        apiKey = "";
        apiName = "";
    }

    public String getSymbol() {
        return symbol;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiName() {
        return apiName;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String[] getRequestUrls() {
        FinancialApi api = buildApi();
        boolean invalid = symbol.isEmpty() || apiKey.isEmpty() || apiName.isEmpty() || api == null;
        if (invalid) {
            return new String[0];
        }
        return api.createRequestUrls(symbol, apiKey);
    }

    private FinancialApi buildApi() {
        for (FinancialApi api: FinancialApi.values()) {
            if (api.isApi(apiName)) {
                return FinancialApi.valueOf(apiName);
            }
        }
        return null;
    }
}
