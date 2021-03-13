package file;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CsvFileUtils extends FileUtils {

    private static final String CSV_PATH = "csv" + File.separator;

    public CsvFileUtils() throws Exception {
        super();
    }

    public static void createAndSaveCsvFile(CsvData csvData, String fileName) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(CSV_PATH + fileName));
        Map<String, Object> map = csvData.map;
        map.forEach((s, o) -> {
            String[] line = {s, o.toString()};
            writer.writeNext(line);
        });
        writer.flush();
        writer.close();
    }
}
