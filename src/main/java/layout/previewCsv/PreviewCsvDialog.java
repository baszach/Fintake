package layout.previewCsv;

import file.ConfigData;
import parser.JsonKeySelectionTree;

import java.util.List;

public class PreviewCsvDialog {

    private final PreviewCsvNavigator previewCsvNavigator;
    private final OutputLayout layout;

    public PreviewCsvDialog(List<ConfigData> configs, JsonKeySelectionTree jsonTree, PreviewCsvNavigator previewCsvNavigator) {
        this.previewCsvNavigator = previewCsvNavigator;
        this.layout = new OutputLayout();
        layout.backButton.addActionListener(e -> previewCsvNavigator.back());
    }

    public OutputLayout getDialogLayout() {
        return layout;
    }
}
