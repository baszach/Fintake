package file;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileController {

    private String filePath;

    public FileController() {
        this.filePath = "";
    }

    public void updatePath(String filePath) {
        this.filePath = filePath;
    }

    public void saveFile() throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath));

        //TODO write into csvWrite here (see docs)

        csvWriter.flush();
    }
}
