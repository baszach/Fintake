package request;

import http.HttpHandler;

import java.util.Optional;

public class RequestController {

    private String symbol;
    private String apiKey;

    public RequestController() {
        symbol = "";
        apiKey = "";
    }

    public Optional<String> runQuery() {
        if(symbol.isEmpty() || apiKey.isEmpty()) {
            return Optional.empty();
        } else {
            return requestData();
        }
    }

    public void updateSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void updateApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    private Optional<String> requestData() {
        HttpHandler httpHandler = new HttpHandler();
        String httpResponse = httpHandler.makeServiceCall("");
        return (httpResponse == null ? Optional.empty() : Optional.of(httpResponse));
    }
}
