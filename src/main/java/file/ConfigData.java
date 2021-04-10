package file;

import java.util.HashSet;
import java.util.Set;

public class ConfigData {

    private final String fileName;
    private final String apiName;
    private final String apiKey;
    private final Set<String> keys;

    public ConfigData(String fileName, String apiName, String apiKey) {
        this(fileName, apiName, apiKey, new HashSet<>());
    }

    public ConfigData(String fileName, String apiName, String apiKey, Set<String> keys) {
        this.fileName = fileName;
        this.apiName = apiName;
        this.apiKey = apiKey;
        this.keys = keys;
    }

    public String getFileName() {
        return fileName;
    }

    public String getApiName() {
        return apiName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Set<String> getKeys() {
        return new HashSet<>(keys);
    }

    public void addKeyToConfig(String key) {
        keys.add(key);
    }
}
