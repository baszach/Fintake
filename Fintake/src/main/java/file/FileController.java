package file;

public class FileController implements FileCommunicator {

    private String filePath;

    public FileController() {
        this.filePath = "";
    }

    public void updatePath(String filePath) {
        this.filePath = filePath;
    }
}
