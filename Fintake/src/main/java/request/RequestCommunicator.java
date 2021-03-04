package request;

import java.util.Optional;

public interface RequestCommunicator {
    Optional<String> runQuery();
    void updateSymbol(String symbol);
    void updateApiKey(String apiKey);
}
