package layout;

import file.ConfigData;
import http.Query;
import layout.companyInformation.CompanyInformationDialog;
import layout.companyInformation.CompanyInformationNavigator;
import layout.previewCsv.PreviewCsvDialog;
import layout.previewCsv.PreviewCsvNavigator;
import parser.JsonKeySelectionTree;

import javax.swing.*;
import java.util.List;

public class ProgramFrame extends JFrame {

    private CompanyInformationDialog companyInfoDialog;
    private PreviewCsvDialog previewCsvDialog;

    public ProgramFrame() {
        init();
    }

    private void init() {
        companyInfoDialog = new CompanyInformationDialog(COMPANY_INFORMATION_NAVIGATOR);
        add(companyInfoDialog.getDialogLayout());
        setSize(1000, 700);
        setVisible(true);
    }

    private final CompanyInformationNavigator COMPANY_INFORMATION_NAVIGATOR = new CompanyInformationNavigator() {
        @Override
        public void nextDialog(JsonKeySelectionTree jsonTree, List<ConfigData> configs, Query query) {
            previewCsvDialog = new PreviewCsvDialog(jsonTree, configs, query, PREVIEW_CSV_NAVIGATOR);
            add(previewCsvDialog.getDialogLayout());
            remove(companyInfoDialog.getDialogLayout());
            revalidate();
            repaint();
        }
    };

    private final PreviewCsvNavigator PREVIEW_CSV_NAVIGATOR = new PreviewCsvNavigator() {
        @Override
        public void back() {
            add(companyInfoDialog.getDialogLayout());
            remove(previewCsvDialog.getDialogLayout());
            revalidate();
            repaint();
        }

        @Override
        public void finish() {
            companyInfoDialog = new CompanyInformationDialog(COMPANY_INFORMATION_NAVIGATOR);
            add(companyInfoDialog.getDialogLayout());
            remove(previewCsvDialog.getDialogLayout());
            revalidate();
            repaint();
        }
    };
}
