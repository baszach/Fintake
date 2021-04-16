package layout.companyInformation;

import api.ApiController;
import api.FinancialApi;
import collection.CacheMap;
import collection.KeyTree;
import file.ConfigData;
import http.Query;
import http.QueryHandler;
import org.json.JSONObject;
import parser.JsonKeySelectionTree;
import parser.JsonKeySelectionTree.KeySelection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static file.ConfigFileUtils.getAllConfigFiles;

public class CompanyInformationDialog {
    //TODO break down code into methods (each method does ONE activity -> no parsing and querying
    // in the same method!???)

    //TODO INVALID QUERIES ARE BEING EXECUTED AND SAVED IN CACHE?
    // Case 1: Invalid key -> empty json returned
    // Case 2: Demo key misused -> warning text returned

    private final CompanyInformationLayout layout;
    private final CompanyInformationNavigator navigatorAction;
    private final ApiController apiController;
    private final Map<String, List<ConfigData>> configsMap; // config files grouped by API
    private final CacheMap<Query, JsonKeySelectionTree> queryCache; // caches query results

    public CompanyInformationDialog(CompanyInformationNavigator navigatorAction) {
        this.layout = new CompanyInformationLayout();
        this.apiController = new ApiController();
        this.navigatorAction = navigatorAction;
        this.configsMap = new HashMap<>();
        this.queryCache = new CacheMap<>(4);
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
        configsMap.putAll(getAllConfigFiles().stream()
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
        layout.nextButton.addActionListener(e -> {
            Query query = apiController.getQuery();
            navigatorAction.nextDialog(queryCache.get(query), getMatchingConfigFiles(), query);
        });
        layout.apiComboBox.addActionListener(e -> {
            Object item = layout.apiComboBox.getSelectedItem();
            if (item != null) {
                loadConfigIntoLayout();
            }
        });
        layout.configComboBox.addActionListener(e -> {
            ConfigData configData = getSelectedConfig();
            if (configData != null) {
                layout.apiKeyField.setText(configData.getApiKey());
            }
        });
    }

    private void queryAndDisplayKeys(ActionEvent event) {
        updateApiQuery();
        if (! queryCache.containsKey(apiController.getQuery())) {
            String[] data = queryForData();
            storeKeysInTree(data);
        }
        displayKeysForSelection();
        layout.nextButton.setEnabled(true);
    }

    private String[] queryForData() {
        String[] urls = apiController.getRequestUrls();
        QueryHandler queryHandler = new QueryHandler(urls);
        return queryHandler.requestData();
    }

    private void storeKeysInTree(String[] data) {
        if(data.length > 0) {
            JsonKeySelectionTree.Builder builder = new JsonKeySelectionTree.Builder("Data");
            for (String jsonString: data) {
                builder.addJsonObject(new JSONObject(jsonString));
            }
            queryCache.put(apiController.getQuery(), builder.parseObjectsToTree());
        } else {
            JOptionPane.showMessageDialog(layout, "The querying process failed!");
        }
    }

    private void displayKeysForSelection() {
        layout.keysPanel.removeAll();
        int index = 0;
        for (Iterator<KeyTree<KeySelection>> iterator = queryCache.get(apiController.getQuery()).getTree().depthFirstKeyTreeIterator();
             iterator.hasNext();
             index++) {
            KeyTree<KeySelection> keyTree = iterator.next();
            JComponent keyComponent = buildKeyComponent(keyTree);
            GridBagConstraints gridBagConstraints = generateConstraints(index, keyTree.getLevel() + 1);
            layout.keysPanel.add(keyComponent, gridBagConstraints);
        }
        layout.keysPanel.revalidate();
        layout.keysPanel.repaint();
    }

    private JComponent buildKeyComponent(KeyTree<KeySelection> keyTree) {
        Set<String> preferenceKeySet = getPreferredKeysFromConfig();
        KeySelection keySelection = keyTree.getKey();
        if (keyTree.isLeaf()) {
            String valueRepresentation = keySelection.value().length() > 80 ? "..." : keySelection.value();
            String s = keySelection.key() + " -> " + valueRepresentation;
            JCheckBox checkBox = new JCheckBox(s);
            checkBox.setSelected(preferenceKeySet.contains(keySelection.key()));
            checkBox.addActionListener(e -> keySelection.setSelected(checkBox.isSelected()));
            return checkBox;
        } else {
            return new JLabel(keySelection.key());
        }
    }

    private Set<String> getPreferredKeysFromConfig() {
        for (ConfigData configData: getMatchingConfigFiles()) {
            if (configData.getFileName().equals(getSelectedConfigName())) {
                return configData.getKeys();
            }
        }
        return Collections.emptySet();
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

    private void updateApiQuery() {
        apiController.setQuery(
                new Query(getSelectedApiName(),
                        layout.apiKeyField.getText(),
                        layout.symbolField.getText()
                )
        );
    }

    private List<ConfigData> getMatchingConfigFiles() {
        return configsMap.getOrDefault(getSelectedApiName(), Collections.emptyList());
    }

    private String getSelectedApiName() {
        return layout.apiComboBox.getItemAt(layout.apiComboBox.getSelectedIndex());
    }

    private ConfigData getSelectedConfig() {
        String configName = getSelectedConfigName();
        for (ConfigData configData: configsMap.getOrDefault(getSelectedApiName(), Collections.emptyList())) {
            if (configData.getFileName().equals(configName))
                return configData;
        }
        return null;
    }

    private String getSelectedConfigName() {
        return layout.configComboBox.getItemAt(layout.configComboBox.getSelectedIndex());
    }
}
