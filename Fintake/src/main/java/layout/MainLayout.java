package layout;

import file.FileController;
import parser.ParsingController;
import request.RequestController;

import java.util.Optional;

public class MainLayout extends Layout {

    private final RequestController requestController;
    private final FileController fileController;
    private final ParsingController parsingController;

    public MainLayout() {
        this.requestController = new RequestController();
        this.fileController = new FileController();
        this.parsingController = new ParsingController();
        init();
    }

    public void init() {
        filePathButton.addActionListener(e -> {
            fileController.updatePath(filePathField.getText());
        });

        queryButton.addActionListener(e -> {
            requestController.updateSymbol(symbolField.getText());
            requestController.updateApiKey(apiKeyField.getText());
            Optional<String> jsonString = requestController.runQuery();
            jsonString.ifPresent(parsingController::updateJsonString);
        });

        magicButton.addActionListener(e -> {
            parsingController.parse();
            //TODO after receiving the parsed Map, display key-set for selection
        });
    }
}
