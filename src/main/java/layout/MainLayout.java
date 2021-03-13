package layout;

import api.ApiController;
import file.ConfigData;
import file.ConfigFileUtils;
import file.CsvData;
import file.CsvFileUtils;
import http.QueryHandler;
import parser.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
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
        Optional<ConfigData> result = ConfigFileUtils.getFirstConfigFile();
        result.ifPresent(strings -> {
            ConfigData configData = result.get();
            apiController.apiName = configData.apiName;
            apiKeyField.setText(configData.apiKey);
        });
    }

    private void setButtonListeners() {
        filePathButton.addActionListener(e -> {

        });

        queryButton.addActionListener(e -> {
            if(startQuery()) {
                try {
                    displayKeysForSelection();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        magicButton.addActionListener(e -> {
            Set<String> selectedKeys = getSelectedKeys();
            try {
                ConfigFileUtils.createAndSaveConfigFile(
                        new ConfigData(
                            apiController.apiName,
                            apiController.apiKey,
                            selectedKeys
                        )
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            String csvPath = filePathField.getText();
            HashMap<String, Object> map = new HashMap<>();
            jsonParser.jsonsToMap().forEach((s, o) -> {
                if (selectedKeys.contains(s)) {
                    map.put(s, o);
                }
            });

            CsvData csvData = new CsvData(map);
            try {
                CsvFileUtils.createAndSaveCsvFile(csvData, csvPath);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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

    private void displayKeysForSelection() throws IOException {
        Set<String> keySet = jsonParser.jsonsToMap().keySet();
        Optional<ConfigData> result = ConfigFileUtils.getConfigFile(apiController.apiName);
        Set<String> preferenceKeySet = result.isPresent() ?
                result.get().getKeys() :
                new HashSet<>();

        if (!keySet.isEmpty()) {
            int index = 0;
            for (String key : keySet) {
                java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = index++;
                JCheckBox checkBox = new JCheckBox(key);
                checkBox.setSelected(preferenceKeySet.contains(key));
                keysPanel.add(checkBox, gridBagConstraints);
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
