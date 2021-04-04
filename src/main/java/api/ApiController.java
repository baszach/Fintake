package api;

import java.util.Optional;

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

    public Optional<String[]> getRequestUrls() {
        if(symbol.isEmpty() || apiKey.isEmpty() || apiName.isEmpty()) {
            return Optional.empty();
        }
        FinancialApi api = buildApi();
        return api != null ?
                Optional.of(api.createRequestUrls(symbol, apiKey)) :
                Optional.empty();
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
