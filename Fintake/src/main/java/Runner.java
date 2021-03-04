import file.FileController;
import layout.MainLayout;
import parser.Parser;
import request.RequestController;

import javax.swing.*;

public class Runner {

    public static void main(String[] args) {
        init();
    }

    public static void init() {
        MainLayout mainLayout = createMainLayout();
        JFrame frame = new JFrame();
        frame.setSize(700, 500);
        frame.add(mainLayout);
        frame.setVisible(true);
    }

    public static MainLayout createMainLayout() {
        RequestController requestController = new RequestController();
        FileController fileController = new FileController();
        Parser parserController = new Parser();
        return new MainLayout(requestController, fileController, parserController);
    }
}
