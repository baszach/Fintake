package file;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CsvFileUtils {

    private static final String CSV_PATH = "csv" + File.separator;

    private CsvFileUtils() throws Exception {
        throw new Exception("Do not initialize util class!");
    }

    // WRITING WITH TRY-WITH-RESOURCES BLOCK!
    public static void createAndSaveCsvFile(CsvData csvData, String fileName) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_PATH + fileName))) {
            Map<String, Object> map = csvData.map;
            map.forEach((s, o) -> {
                String[] line = {s, o.toString()};
                writer.writeNext(line);
            });
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
