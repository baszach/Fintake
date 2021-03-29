package api;

import java.util.Optional;

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
        SupportedApi api = buildApi();
        return api != null ?
                Optional.of(api.createRequestUrls(symbol, apiKey)) :
                Optional.empty();
    }

    private SupportedApi buildApi() {
        for (SupportedApi api: SupportedApi.values()) {
            if (api.isApi(apiName)) {
                return SupportedApi.valueOf(apiName);
            }
        }
        return null;
    }
}
