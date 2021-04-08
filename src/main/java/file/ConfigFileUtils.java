package file;

import java.io.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ConfigFileUtils {

    private static final String CONFIGS_PATH = "configs" + File.separator;
    private static final String CONFIG_INTERNAL_SEPARATOR = ",";

    private ConfigFileUtils() throws Exception {
        throw new Exception("Do not initialize util class!");
    }

    public static void createAndSaveConfigFile(ConfigData configData) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(configData.getApiName())
                .append(",").append(configData.getApiKey());
        for (String key : configData.getKeys()) {
            builder.append(",").append(key);
        }
        FileWriter fileWriter = new FileWriter(CONFIGS_PATH + configData.getApiName());
        fileWriter.write(builder.toString());
        fileWriter.flush();
        fileWriter.close();
    }

    public static Set<ConfigData> getAllConfigFiles() {
        File configsDir = new File("configs");
        Set<ConfigData> configFiles = new HashSet<>();
        if (configsDir.isDirectory()) {
            String[] files = configsDir.list();
            if(files != null) {
                for (String file: files) {
                    String[] data = readConfigFile(file);
                    // Minimum requirement for config file
                    // -> containing a api name and key (length == 2)
                    if (data.length >= 2) {
                        configFiles.add(buildConfigData(file, data));
                    }
                }

            }
        }
        return configFiles;
    }

    // READING WITH TRY-WITH-RESOURCES BLOCK!
    //TODO consider using Stream with: Stream<String> config = Files.lines(path)
    private static String[] readConfigFile(String fileName)  {
        File configFile =  new File(CONFIGS_PATH + fileName);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString().split(CONFIG_INTERNAL_SEPARATOR);
    }

    private static ConfigData buildConfigData(String fileName, String[] data) {
        ConfigData configData = new ConfigData(fileName, data[0], data[1]);
        for (int i = 2; i < data.length; i++) {
            configData.addKeyToConfig(data[i]);
        }
        return configData;
    }
}
