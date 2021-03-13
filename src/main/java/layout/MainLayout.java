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
import java.util.concurrent.atomic.AtomicBoolean;

public class MainLayout extends Layout {

    private ApiController apiController;
    private JsonParser jsonParser;

    public MainLayout() {
        init();
        preloadConfigs();
    }

    private void init() {
        this.apiController = new ApiController();
        this.jsonParser = new JsonParser();
        magicButton.setEnabled(false);
        setButtonListeners();
    }

    private void preloadConfigs() {
        try {
            Optional<ConfigData> result = ConfigFileUtils.getFirstConfigFile();
            result.ifPresent(strings -> {
                ConfigData configData = result.get();
                apiController.apiName = configData.apiName;
                apiKeyField.setText(configData.apiKey);
            });
        } catch (IOException e) {
            //TODO pop-up -> something went wrong
            e.printStackTrace();
        }
    }

    private void setButtonListeners() {
        filePathButton.addActionListener(e -> {

        });

        queryButton.addActionListener(e -> {
            if(startQuery()) {
                try {
                    displayKeysForSelection();
                    magicButton.setEnabled(true);
                } catch (IOException ex) {
                    //TODO pop-up -> something went wrong
                    ex.printStackTrace();
                }
            } else {
                //TODO pop-up -> query failed
            }
        });

        magicButton.addActionListener(e -> {
            magicButton.setEnabled(false);
            Set<String> selectedKeys = getSelectedKeys();
            try { //TODO create method -> pop-up asks user, if old config shall be overwritten
                ConfigFileUtils.createAndSaveConfigFile(
                        new ConfigData(apiController.apiName,
                            apiController.apiKey,
                            selectedKeys)
                );
            } catch (Exception ex) {
                //TODO pop-up -> something went wrong
                ex.printStackTrace();
            }

            String csvPath = filePathField.getText();
            if(! csvPath.isEmpty()) {
                //TODO create method -> pop-up asks for user verification for saving csv file
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
                    //TODO pop-up -> something went wrong
                    ex.printStackTrace();
                }
            }
        });
    }

    private boolean startQuery() {
        updateApiControllerFields();
        Optional<String[]> urls = apiController.getRequestUrls();
        AtomicBoolean querySuccessful = new AtomicBoolean(false);
        urls.ifPresent(strings -> {
            QueryHandler queryHandler = new QueryHandler(urls.get());
            jsonParser.updateJsons(queryHandler.requestData());
            querySuccessful.set(true);
        });
        return querySuccessful.get();
    }

    private void updateApiControllerFields() {
        apiController.symbol = symbolField.getText();
        apiController.apiKey = apiKeyField.getText();
        apiController.apiName = "AlphaVantage";
    }

    private void displayKeysForSelection() throws IOException {
        Set<String> keySet = jsonParser.jsonsToMap().keySet();
        Optional<ConfigData> result = ConfigFileUtils.getConfigFile(apiController.apiName);
        Set<String> preferenceKeySet = result.isPresent() ?
                result.get().getKeys() :
                new HashSet<>();

        if (!keySet.isEmpty()) {
            int index = 0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = index;
            for (String key : keySet) {
                gridBagConstraints.gridy = index++;
                JCheckBox checkBox = new JCheckBox(key);
                checkBox.setSelected(preferenceKeySet.contains(key));
                keysPanel.add(checkBox, gridBagConstraints);
            }
            keysPanel.revalidate();
            keysPanel.repaint();
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
