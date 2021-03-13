package file;

import java.io.*;
import java.util.Optional;

public class ConfigFileUtils extends FileUtils {

    private static final String CONFIGS_PATH = "configs" + File.separator;
    private static final String FIRST_FILE = "FIRST";

    public ConfigFileUtils() throws Exception {
        super();
    }

    public static void createAndSaveConfigFile(ConfigData configData) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(configData.apiName).append(",");
        builder.append(configData.apiKey);
        for (String key: configData.getKeys()) {
            builder.append(",").append(key);
        }
        FileWriter fileWriter = new FileWriter(CONFIGS_PATH + configData.apiName);
        fileWriter.write(builder.toString());
        fileWriter.flush();
        fileWriter.close();
    }

    public static Optional<ConfigData> getFirstConfigFile() throws IOException {
        return getConfigFile(FIRST_FILE);
    }

    public static Optional<ConfigData> getConfigFile(String fileName) throws IOException {
        File configsDir = new File("configs");
        if(configsDir.isDirectory()) {
            String[] files = configsDir.list();
            if(files != null) {
                for (String file: files) {
                    if(fileName.equals(file) || fileName.equals(FIRST_FILE)) {
                        String[] data = readConfigFile(fileName = file);
                        // Minimum requirement for config file
                        // -> containing a api name and key
                        if (data.length >= 2) {
                            return Optional.of(buildConfigData(data));
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    private static String[] readConfigFile(String fileName) throws IOException {
        File configFile =  new File(CONFIGS_PATH + fileName);
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        String line = reader.readLine();
        while (line != null) {
            builder.append(line);
            line = reader.readLine();
        }
        return builder.toString().split(",");
    }

    private static ConfigData buildConfigData(String[] data) {
        ConfigData configData = new ConfigData(data[0], data[1]);
        for (int i = 2; i < data.length; i++) {
            configData.addKeyToConfig(data[i]);
        }
        return configData;
    }
}
