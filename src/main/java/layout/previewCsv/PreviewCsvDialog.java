package layout.previewCsv;

import file.ConfigData;
import file.ConfigFileUtils;
import file.CsvData;
import file.CsvFileUtils;
import http.Query;
import parser.JsonKeySelectionTree;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static parser.JsonKeySelectionTree.*;

public class PreviewCsvDialog {

    private final PreviewCsvNavigator navigatorAction;
    private final OutputLayout layout;
    private final Query query;
    private final JsonKeySelectionTree jsonTree;
    private final List<ConfigData> configs;
    private Set<String> selectedKeySet;

    public PreviewCsvDialog(JsonKeySelectionTree jsonTree, List<ConfigData> configs, Query query, PreviewCsvNavigator navigatorAction) {
        this.navigatorAction = navigatorAction;
        this.layout = new OutputLayout();
        this.jsonTree = jsonTree;
        this.configs = configs;
        this.query = query;
        init();
    }

    public OutputLayout getDialogLayout() {
        return layout;
    }

    private void init() {
        this.selectedKeySet = computeSelectedKeys();
        loadView();
        setActionListeners();
    }

    private Set<String> computeSelectedKeys() {
        return selectedKeysStream()
                .map(KeySelection::key)
                .collect(Collectors.toSet());
    }

    private void loadView() {
        layout.api.setText(query.getApiName());
        layout.symbol.setText(query.getSymbol());
    }

    private void setActionListeners() {
        layout.backButton.addActionListener(e -> navigatorAction.back());
        layout.saveConfigButton.addActionListener(e -> {
            ConfigData configData = new ConfigData(layout.configField.getText(), query.getApiName(),
                    query.getApiKey(), selectedKeySet);
            ConfigFileUtils.createAndSaveConfigFile(configData);
        });
        layout.jButton2.addActionListener(e ->
                CsvFileUtils.createAndSaveCsvFile(new CsvData(computeCsvMap()), layout.csvField.getText())
        );
    }

    private Map<String, String> computeCsvMap() {
        return selectedKeysStream().collect(
                Collectors.toMap(KeySelection::key, KeySelection::value));
    }

    private Stream<KeySelection> selectedKeysStream() {
        return jsonTree.getTree().keySet().stream()
                .filter(KeySelection::isSelected);
    }
}
