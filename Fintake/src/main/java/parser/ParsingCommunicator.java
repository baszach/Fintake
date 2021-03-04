package parser;

import java.util.Map;

public interface ParsingCommunicator {
    Map<String, Object> parse();
    void updateJsonString(String json);
}
