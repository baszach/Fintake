package layout;

import javax.swing.*;

public class ProgramFrame extends JFrame {

    public ProgramFrame() {
        init();
    }

    private void init() {
        CompanyInformationDialog companyInfoLayout = new CompanyInformationDialog(NAVIGATOR_ACTION);
        setSize(1000, 700);
        add(companyInfoLayout);
        setVisible(true);
    }

    private static final NavigatorAction NAVIGATOR_ACTION = new NavigatorAction() {
        @Override
        public void backAction() {

        }

        @Override
        public void nextAction() {

        }
    };
}
