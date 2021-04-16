package api;

import http.Query;

public class ApiController {

    private Query query;

    public ApiController() {
        this.query = null;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public String[] getRequestUrls() {
        FinancialApi api = buildApi();
        boolean invalid = query.getSymbol().isEmpty() || query.getApiKey().isEmpty()
                || query.getApiName().isEmpty() || api == null;
        if (invalid) {
            return new String[0];
        }
        return api.createRequestUrls(query.getSymbol(), query.getApiKey());
    }

    private FinancialApi buildApi() {
        for (FinancialApi api: FinancialApi.values()) {
            if (api.isApi(query.getApiName())) {
                return FinancialApi.valueOf(query.getApiName());
            }
        }
        return null;
    }
}
