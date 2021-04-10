package layout;

import file.ConfigData;
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
        //TODO choose correct parameters to give to next dialog!
        @Override
        public void next(List<ConfigData> configs, JsonKeySelectionTree jsonTree) {
            previewCsvDialog = new PreviewCsvDialog(configs, jsonTree, PREVIEW_CSV_NAVIGATOR);
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
