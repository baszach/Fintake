package api;

import java.util.Optional;

import static api.ApiFactory.buildApi;

public class ApiController {

    public String symbol;
    public String apiKey;
    public String apiName;

    public ApiController() {
        symbol = "";
        apiKey = "";
        apiName = "";
    }

    public Optional<TitledSourceUrl[]> getRequestUrls() {
        if(symbol.isEmpty() || apiKey.isEmpty() || apiName.isEmpty()) {
            return Optional.empty();
        }
        SupportedApi api = buildApi(apiName);
        return api != null ?
                Optional.of(api.createRequestUrls(symbol, apiKey)) :
                Optional.empty();
    }
}
