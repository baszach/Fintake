package layout;

import api.ApiController;
import http.QueryHandler;
import parser.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MainLayout extends Layout {

    private ApiController apiController;
    private JsonParser jsonParser;

    public MainLayout() {
        preloadConfigs();
        init();
    }

    private void preloadConfigs() {
        File configsDir = new File("configs");
        if(configsDir.isDirectory()) {
            for(String file: configsDir.list())  {
                //TODO
            }
        }
    }

    private void init() {
        this.apiController = new ApiController();
        this.jsonParser = new JsonParser();

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

    private boolean startQuery() {
        apiController.updateSymbol(symbolField.getText());
        apiController.updateApiKey(apiKeyField.getText());
        apiController.updateApi("alpha");
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
