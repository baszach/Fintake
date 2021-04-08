package layout;

import api.ApiController;
import api.FinancialApi;
import collection.KeyTree;
import file.ConfigData;
import http.QueryHandler;
import org.json.JSONObject;
import parser.JsonKeySelectionTree;
import parser.JsonKeySelectionTree.KeySelection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static file.ConfigFileUtils.*;
import static parser.JsonKeySelectionTree.*;

public class CompanyInformationDialog extends CompanyInformationLayout {

    private final CompanyInformationLayout layout;
    private final NavigatorAction navigatorAction;
    private final ApiController apiController;
    private final Map<String, List<ConfigData>> configs;
    private JsonKeySelectionTree jsonTree;

    public CompanyInformationDialog(NavigatorAction navigatorAction) {
        this.layout = new CompanyInformationLayout();
        this.apiController = new ApiController();
        this.navigatorAction = navigatorAction;
        this.configs = new HashMap<>();
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

        //TODO remove lines below later
        symbolField.setText("IBM");
        apiKeyField.setText("demo");
    }

    private void preloadApis() {
        apiComboBox.removeAllItems();
        for (FinancialApi api: FinancialApi.values()) {
            apiComboBox.addItem(api.name());
        }
    }

    private void preloadConfigurations() {
        configs.putAll(getAllConfigFiles().stream()
                .collect(Collectors.groupingBy(ConfigData::getApiName)));
    }

    private void loadConfigIntoLayout() {
        configComboBox.removeAllItems();
        configComboBox.addItem("<None>");
        for (ConfigData configData: getMatchingConfigFiles()) {
            configComboBox.addItem(configData.getFileName());
        }
    }

    private void setActionListeners() {
        queryButton.addActionListener(this::queryAndDisplayKeys);
        //TODO put listener actions in extra methods
        nextButton.addActionListener(e -> {
            //TODO give selected keys (or whole tree) to next dialog
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
        String[] urls = apiController.getRequestUrls();
        boolean querySuccessful = false;
        QueryHandler queryHandler = new QueryHandler(urls);
        String[] data = queryHandler.requestData();
        Builder builder = new Builder("Data");
        for (String jsonString: data) {
            builder.addJsonObject(new JSONObject(jsonString));
        }
        try {
            jsonTree = builder.parseObjectsToTree();
            querySuccessful = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return querySuccessful;
    }

    private void displayKeysForSelection() throws IOException {
        Iterator<KeyTree<KeySelection>> iterator = jsonTree.getTree().depthFirstKeyTreeIterator();
        Set<String> preferenceKeySet = new HashSet<>();
        for (ConfigData configData: getMatchingConfigFiles()) {
            if (configData.getFileName().equals(getSelectedConfig())) {
                preferenceKeySet.addAll(configData.getKeys());
                break;
            }
        }
        int index = 0;
        while (iterator.hasNext()) {
            KeyTree<KeySelection> keySelectionKeyTree = iterator.next();
            KeySelection keySelection = keySelectionKeyTree.getKey();
            JCheckBox checkBox = new JCheckBox(keySelection.key());
            checkBox.setSelected(preferenceKeySet.contains(keySelection.key()));
            checkBox.addActionListener(e -> {
                keySelection.setSelected(checkBox.isSelected());
            });
            int insetBy = keySelectionKeyTree.getLevel() + 1;
            GridBagConstraints gridBagConstraints = generateConstraints(index++, insetBy);
            keysPanel.add(checkBox, gridBagConstraints);
        }
        keysPanel.revalidate();
        keysPanel.repaint();
    }

    private GridBagConstraints generateConstraints(int yIndex, int insetFactor) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridy = yIndex;
        gridBagConstraints.insets = new java.awt.Insets(6, insetFactor * 12, 0, 0);
        return gridBagConstraints;
    }

    private void updateApiControllerFields() {
        apiController.setSymbol(symbolField.getText());
        apiController.setApiKey(apiKeyField.getText());
        apiController.setApiName(getSelectedApiName());
    }

    private List<ConfigData> getMatchingConfigFiles() {
        return configs.getOrDefault(getSelectedApiName(), Collections.emptyList());
    }

    private String getSelectedApiName() {
        return apiComboBox.getItemAt(apiComboBox.getSelectedIndex());
    }

    private String getSelectedConfig() {
        return configComboBox.getItemAt(configComboBox.getSelectedIndex());
    }

}
