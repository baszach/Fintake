package layout.companyInformation;

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

public class CompanyInformationDialog {
    //TODO Add cache for performed queries (capacity maybe 3-5 query results)
    //TODO break down code into methods (each method does ONE activity -> no parsing and querying
    // in the same method!)
    //TODO write classes for wrapping information (e.g. a QueryInfo object may hold api and api key)


    private final CompanyInformationLayout layout;
    private final CompanyInformationNavigator navigatorAction;
    private final ApiController apiController;
    private final Map<String, List<ConfigData>> configs;
    private JsonKeySelectionTree jsonTree;

    public CompanyInformationDialog(CompanyInformationNavigator navigatorAction) {
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
        layout.nextButton.setEnabled(false);
        setActionListeners();
        preloadApis();
        preloadConfigurations();
        loadConfigIntoLayout();

        //TODO remove lines below later
        layout.symbolField.setText("IBM");
        layout.apiKeyField.setText("demo");
    }

    private void preloadApis() {
        layout.apiComboBox.removeAllItems();
        for (FinancialApi api: FinancialApi.values()) {
            layout.apiComboBox.addItem(api.name());
        }
    }

    private void preloadConfigurations() {
        configs.putAll(getAllConfigFiles().stream()
                .collect(Collectors.groupingBy(ConfigData::getApiName)));
    }

    private void loadConfigIntoLayout() {
        layout.configComboBox.removeAllItems();
        layout.configComboBox.addItem("<None>");
        for (ConfigData configData: getMatchingConfigFiles()) {
            layout.configComboBox.addItem(configData.getFileName());
        }
    }

    private void setActionListeners() {
        layout.queryButton.addActionListener(this::queryAndDisplayKeys);
        //TODO put listener actions in extra methods
        layout.nextButton.addActionListener(e -> {
            //TODO give selected keys (or whole tree) to next dialog
            navigatorAction.next(getMatchingConfigFiles(), jsonTree);
        });
        layout.apiComboBox.addActionListener(e -> {
            Object item = layout.apiComboBox.getSelectedItem();
            if (item != null) {
                loadConfigIntoLayout();
            }
        });
        layout.configComboBox.addActionListener(e -> {
            Object item = layout.configComboBox.getSelectedItem();
            if (item instanceof ConfigData) {
                ConfigData configData = (ConfigData) item;
                layout.apiKeyField.setText(configData.getApiKey());
            }
        });
    }

    private void queryAndDisplayKeys(ActionEvent event) {
        String[] data = queryForData();
        if(data.length > 0) {
            try {
                storeKeysInTree(data);
                displayKeysForSelection();
                layout.nextButton.setEnabled(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(layout, "The parsing process failed!");
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(layout, "The querying process failed!");
        }
    }

    private String[] queryForData() {
        updateApiControllerFields();
        String[] urls = apiController.getRequestUrls();
        QueryHandler queryHandler = new QueryHandler(urls);
        return queryHandler.requestData();
    }

    private void storeKeysInTree(String[] data) {
        JsonKeySelectionTree.Builder builder = new JsonKeySelectionTree.Builder("Data");
        for (String jsonString: data) {
            builder.addJsonObject(new JSONObject(jsonString));
        }
        jsonTree = builder.parseObjectsToTree();
    }

    private void displayKeysForSelection() throws IOException {
        Set<String> preferenceKeySet = new HashSet<>();
        for (ConfigData configData: getMatchingConfigFiles()) {
            if (configData.getFileName().equals(getSelectedConfig())) {
                preferenceKeySet.addAll(configData.getKeys());
                break;
            }
        }
        int index = 0;
        for (Iterator<KeyTree<KeySelection>> iterator = jsonTree.getTree().depthFirstKeyTreeIterator();
             iterator.hasNext();
             index++) {
            KeyTree<KeySelection> keySelectionKeyTree = iterator.next();
            KeySelection keySelection = keySelectionKeyTree.getKey();
            JComponent keyComponent;
            if (keySelectionKeyTree.isLeaf()) {
                keyComponent = new JCheckBox(keySelection.key());
                ((JCheckBox) keyComponent).setSelected(preferenceKeySet.contains(keySelection.key()));
                ((JCheckBox) keyComponent).addActionListener(e -> {
                    keySelection.setSelected(((JCheckBox) keyComponent).isSelected());
                });
            } else {
                keyComponent = new JLabel(keySelection.key());
            }
            int insetBy = keySelectionKeyTree.getLevel() + 1;
            GridBagConstraints gridBagConstraints = generateConstraints(index, insetBy);
            layout.keysPanel.add(keyComponent, gridBagConstraints);
        }
        layout.keysPanel.revalidate();
        layout.keysPanel.repaint();
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
        apiController.setSymbol(layout.symbolField.getText());
        apiController.setApiKey(layout.apiKeyField.getText());
        apiController.setApiName(getSelectedApiName());
    }

    private List<ConfigData> getMatchingConfigFiles() {
        return configs.getOrDefault(getSelectedApiName(), Collections.emptyList());
    }

    private String getSelectedApiName() {
        return layout.apiComboBox.getItemAt(layout.apiComboBox.getSelectedIndex());
    }

    private String getSelectedConfig() {
        return layout.configComboBox.getItemAt(layout.configComboBox.getSelectedIndex());
    }

}
