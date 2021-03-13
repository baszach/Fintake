package file;

import java.util.HashSet;
import java.util.Set;

public class ConfigData {

    public final String apiName;
    public final String apiKey;
    private final Set<String> keys;

    public ConfigData(String apiName, String apiKey) {
        this(apiName, apiKey, new HashSet<>());
    }

    public ConfigData(String apiName, String apiKey, Set<String> keys) {
        this.apiName = apiName;
        this.apiKey = apiKey;
        this.keys = keys;
    }

    public void addKeyToConfig(String key) {
        keys.add(key);
    }

    public Set<String> getKeys() {
        return keys;
    }
}
