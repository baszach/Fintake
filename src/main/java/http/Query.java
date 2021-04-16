package http;

import java.util.Objects;

public class Query {

    private final String apiName;
    private final String apiKey;
    private final String symbol;

    public Query(String apiName, String apiKey, String symbol) {
        this.apiName = apiName;
        this.apiKey = apiKey;
        this.symbol = symbol;
    }

    public String getApiName() {
        return apiName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query = (Query) o;
        return Objects.equals(apiName, query.apiName) && Objects.equals(symbol, query.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiName, symbol);
    }
}
