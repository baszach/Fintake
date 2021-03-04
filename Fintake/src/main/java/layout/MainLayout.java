package layout;

import file.FileController;
import parser.ParsingController;
import request.RequestController;

import javax.swing.*;
import java.util.Optional;
import java.util.Set;

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
            startQuery();
            displayKeysForSelection();
        });

        magicButton.addActionListener(e -> {
            //TODO here create CSV table using selected keys
        });
    }

    private void startQuery() {
        requestController.updateSymbol(symbolField.getText());
        requestController.updateApiKey(apiKeyField.getText());
        Optional<String> jsonString = requestController.runQuery();
        jsonString.ifPresent(parsingController::updateJsonString);
    }

    private void displayKeysForSelection() {
        Set<String> keySet = parsingController.parse();
        if(!keySet.isEmpty()) {
            int index = 0;
            for(String key: keySet) {
                java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = index++;
                keysPanel.add(new JCheckBox(key), gridBagConstraints);
            }
            keysPanel.repaint();
            this.repaint();
        }
    }
}
