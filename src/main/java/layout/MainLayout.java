package main.java.layout;

import main.java.api.ApiController;
import main.java.file.ConfigFileUtils;
import main.java.http.QueryHandler;
import main.java.parser.JsonParser;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MainLayout extends Layout {

    private ApiController apiController;
    private JsonParser jsonParser;

    public MainLayout() {
        init();
        try {
            preloadConfigs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        this.apiController = new ApiController();
        this.jsonParser = new JsonParser();
        setButtonListeners();
        setTextFieldListeners();
    }

    private void preloadConfigs() throws IOException {
        Optional<String[]> result = ConfigFileUtils.getFirstConfigFileData();
        if(result.isPresent()) {
            String[] configData = result.get();
            apiController.updateApi(configData[0]);
            apiKeyField.setText(configData[1]);
        }
    }

    private void setButtonListeners() {
        filePathButton.addActionListener(e -> {

        });

        queryButton.addActionListener(e -> {
            if(startQuery()) {
                displayKeysForSelection();
            }
        });

        magicButton.addActionListener(e -> {
            Set<String> keys = getSelectedKeys();
            //TODO here create CSV table using selected keys
        });
    }

    private void setTextFieldListeners() {
        symbolField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updated(e); }
            public void removeUpdate(DocumentEvent e) { updated(e); }
            public void changedUpdate(DocumentEvent e) { updated(e); }
            public void updated(DocumentEvent e) {
                apiController.updateSymbol(symbolField.getText());
            }
        });
        apiKeyField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updated(e); }
            public void removeUpdate(DocumentEvent e) { updated(e); }
            public void changedUpdate(DocumentEvent e) { updated(e); }
            public void updated(DocumentEvent e) {
                apiController.updateApiKey(apiKeyField.getText());
            }
        });
    }

    private boolean startQuery() {
//        apiController.updateApi("AlphaVantage");
        Optional<String[]> urls = apiController.getRequestUrls();
        if(urls.isPresent()) {
            QueryHandler queryHandler = new QueryHandler(urls.get());
            jsonParser.updateJsons(queryHandler.requestData());
            return true;
        }
        return false;
    }

    private void displayKeysForSelection() {
        Set<String> keySet = jsonParser.jsonsToMap().keySet();
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

    private Set<String> getSelectedKeys() {
        Set<String> keys = new HashSet<>();
        for(int i = 0; i < keysPanel.getComponentCount(); i++) {
            Component c = keysPanel.getComponent(i);
            if(c instanceof JCheckBox) {
                JCheckBox b = (JCheckBox) c;
                if (b.isSelected()) {
                    keys.add(b.getText());
                }
            }
        }
        return keys;
    }
}
