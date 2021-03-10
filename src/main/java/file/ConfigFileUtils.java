package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class ConfigFileUtils {

    public ConfigFileUtils() throws Exception {
        throw new Exception("Do not initialize util class!");
    }

    public static Optional<String[]> getFirstConfigFileData() throws IOException {
        File configsDir = new File("configs");
        if(configsDir.isDirectory()) {
            String[] files = configsDir.list();
            if(files != null) {
                for (String fileName: files) {
                    File configFile = new File("configs\\" + fileName);
                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new FileReader(configFile));
                    String line = reader.readLine();
                    while (line != null) {
                        builder.append(line);
                        line = reader.readLine();
                    }
                    return Optional.of(builder.toString().split(","));
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<String[]> getConfigFileData(String fileName) throws IOException {
        File configsDir = new File("configs");
        if(configsDir.isDirectory()) {
            String[] files = configsDir.list();
            if(files != null) {
                for (String file: files) {
                    if(file.equals(fileName)) {
                        File configFile = new File("configs\\" + fileName);
                        StringBuilder builder = new StringBuilder();
                        BufferedReader reader = new BufferedReader(new FileReader(configFile));
                        String line = reader.readLine();
                        while (line != null) {
                            builder.append(line);
                            line = reader.readLine();
                        }
                        return Optional.of(builder.toString().split(","));
                    }
                }
            }
        }
        return Optional.empty();
    }
}
