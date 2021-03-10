package main.java.api;

import java.util.Optional;

import static main.java.api.ApiFactory.buildApi;

public class ApiController {

    private String symbol;
    private String apiKey;
    private String apiName;

    public ApiController() {
        symbol = "";
        apiKey = "";
        apiName = "";
    }

    public Optional<String[]> getRequestUrls() {
        if(symbol.isEmpty() || apiKey.isEmpty() || apiName.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(buildApi(apiName).createRequestUrls(symbol, apiKey));
    }

    public void updateSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void updateApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void updateApi(String apiName) {
        this.apiName = apiName;
    }
}
