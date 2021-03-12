package layout;

import api.ApiController;
import file.ConfigFileUtils;
import http.QueryHandler;
import parser.JsonParser;

import javax.swing.*;
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
    }

    private void preloadConfigs() throws IOException {
        Optional<String[]> result = ConfigFileUtils.getFirstConfigFileData();
        result.ifPresent(strings -> {
            String[] configData = result.get();
            apiController.apiName = configData[0];
            apiKeyField.setText(configData[1]);
        });
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
            try {
                ConfigFileUtils.createAndSaveConfigFile(
                        apiController.apiName,
                        apiController.apiKey,
                        keys);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //TODO here create CSV table using selected keys
        });
    }

    private boolean startQuery() {
        apiController.symbol = symbolField.getText();
        apiController.apiKey = apiKeyField.getText();
        apiController.apiName = "AlphaVantage";
        Optional<String[]> urls = apiController.getRequestUrls();
        urls.ifPresent(strings -> {
            QueryHandler queryHandler = new QueryHandler(urls.get());
            jsonParser.updateJsons(queryHandler.requestData());
        });
        return urls.isPresent();
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
