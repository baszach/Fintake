package layout;

import api.ApiController;
import api.TitledSourceUrl;
import file.ConfigData;
import file.ConfigFileUtils;
import file.CsvData;
import file.CsvFileUtils;
import http.QueryHandler;
import parser.JsonParser;
import parser.JsonTree;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
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
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Something went wrong while preloading your configurations!");
            ex.printStackTrace();
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
                    JOptionPane.showMessageDialog(this, "The querying process failed!");
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "The querying process failed!");
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
                JOptionPane.showMessageDialog(this, "Saving configuration successful!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Could not save configuration!");
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
                    JOptionPane.showMessageDialog(this, "Saving CSV successful!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Could not save CSV file!");
                    ex.printStackTrace();
                }
            }
        });
    }

    private boolean startQuery() {
        updateApiControllerFields();
        Optional<TitledSourceUrl[]> result = apiController.getRequestUrls();
        AtomicBoolean querySuccessful = new AtomicBoolean(false);
        result.ifPresent(strings -> {
            TitledSourceUrl[] titledSourceUrls = result.get();
            String[] urls = new String[titledSourceUrls.length];
            for (int i = 0; i < urls.length; i++) {
                urls[i] = titledSourceUrls[i].getUrl();
            }
            QueryHandler queryHandler = new QueryHandler(urls); // FIXME !!!
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
        JsonTree jsonTree = new JsonTree(jsonParser.firstObj(), "PARENT");
        jsonTree.buildTree();
        System.out.println(jsonTree.getTree().toString());

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
