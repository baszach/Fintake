package layout;

import api.ApiController;
import api.FinancialApi;
import file.ConfigData;
import http.QueryHandler;
import org.json.JSONObject;
import parser.JsonTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static file.ConfigFileUtils.*;

public class CompanyInformationDialog extends CompanyInformationLayout {

    private final CompanyInformationLayout layout;
    private final NavigatorAction navigatorAction;
    private final ApiController apiController;
    private Map<String, List<ConfigData>> configs;
    private JsonTree jsonTree;

    public CompanyInformationDialog(NavigatorAction navigatorAction) {
        this.layout = new CompanyInformationLayout();
        this.navigatorAction = navigatorAction;
        this.apiController = new ApiController();
        init();
    }

    public CompanyInformationLayout getDialogLayout() {
        return layout;
    }

    private void init() {
        nextButton.setEnabled(false);
        setActionListeners();
        preloadApis();
        preloadConfigurations();
        loadConfigIntoLayout();
    }

    private void preloadApis() {
        apiComboBox.removeAllItems();
        for (FinancialApi api: FinancialApi.values()) {
            apiComboBox.addItem(api.name());
        }
    }

    private void preloadConfigurations() {
        configs = getAllConfigFiles().stream()
                .collect(Collectors.groupingBy(ConfigData::getApiName));
    }

    private void loadConfigIntoLayout() {
        configComboBox.removeAllItems();
        configComboBox.addItem("<None>");
        for (ConfigData configData: configs.get(getSelectedApiName())) {
            configComboBox.addItem(configData.getFileName());
        }
    }

    private void setActionListeners() {
        queryButton.addActionListener(this::queryAndDisplayKeys);
        //TODO put listener actions in extra methods
        nextButton.addActionListener(e -> {
            computeSelectedKeys();
            navigatorAction.nextAction();
        });
        apiComboBox.addActionListener(e -> {
            Object item = apiComboBox.getSelectedItem();
            if (item != null) {
                loadConfigIntoLayout();
            }
        });
        configComboBox.addActionListener(e -> {
            Object item = configComboBox.getSelectedItem();
            if (item instanceof ConfigData) {
                ConfigData configData = (ConfigData) item;
                apiKeyField.setText(configData.getApiKey());
            }
        });
    }

    private void queryAndDisplayKeys(ActionEvent event) {
        if(startQuery()) {
            try {
                displayKeysForSelection();
                nextButton.setEnabled(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "The querying process failed!");
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "The querying process failed!");
        }
    }

    private boolean startQuery() {
        updateApiControllerFields();
        Optional<String[]> result = apiController.getRequestUrls();
        AtomicBoolean querySuccessful = new AtomicBoolean(false);
        result.ifPresent(strings -> {
            QueryHandler queryHandler = new QueryHandler(result.get());
            String[] data = queryHandler.requestData();
            JsonTree.Builder builder = new JsonTree.Builder("Parent");
            for (String jsonString: data) {
                builder.addJsonObject(new JSONObject(jsonString));
            }
            try {
                jsonTree = builder.parseObjectsAndCreateTree();
                querySuccessful.set(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return querySuccessful.get();
    }

    private void displayKeysForSelection() throws IOException {
        Set<String> keySet = jsonTree.getTree().keySet();
        Optional<ConfigData> result = getConfigFileByName(apiController.getApiName());
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

    private void computeSelectedKeys() {
        Set<String> selectedKeys = getSelectedKeys();
    }

    private void updateApiControllerFields() {
        apiController.setSymbol(symbolField.getText());
        apiController.setApiKey(apiKeyField.getText());
        apiController.setApiName(getSelectedApiName());
    }

    private String getSelectedApiName() {
        return apiComboBox.getItemAt(apiComboBox.getSelectedIndex());
    }
}
