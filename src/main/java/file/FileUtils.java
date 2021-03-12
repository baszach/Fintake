package file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    public FileUtils() throws Exception {
        throw new Exception("Do not initialize util class!");
    }

    public static void saveFile(String path, String fileContent) throws IOException {
        File file = new File(path);
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(fileContent);
        fileWriter.flush();
        fileWriter.close();
    }
}
