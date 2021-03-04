package layout;

import file.FileController;
import parser.ParsingController;
import request.RequestController;

import javax.swing.*;
import java.util.HashSet;
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

    private void init() {
        filePathButton.addActionListener(e -> {
            fileController.updatePath(filePathField.getText());
        });

        queryButton.addActionListener(e -> {
            requestController.updateSymbol(symbolField.getText());
            requestController.updateApiKey(apiKeyField.getText());
            Optional<String> jsonString = requestController.runQuery();
            jsonString.ifPresent(parsingController::updateJsonString);

            HashSet<String> keySet = parsingController.parse();
            if(!keySet.isEmpty()) {
                for(String key: keySet) {
                    keysPanel.add(new JCheckBox(key, false));
                }
                keysPanel.repaint();
                this.repaint();
            }
        });

        magicButton.addActionListener(e -> {
            //TODO here create CSV table using selected keys
        });
    }
}
