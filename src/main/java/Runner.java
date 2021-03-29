import layout.MainLayout;

import javax.swing.*;

public class Runner {

    public static void main(String[] args) {
        init();
    }

    public static void init() {
        MainLayout mainLayout = new MainLayout();
        JFrame frame = new JFrame();
        frame.setSize(1000, 700);
        frame.add(mainLayout);
        frame.setVisible(true);
    }
}
