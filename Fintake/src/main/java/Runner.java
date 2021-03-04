import javax.swing.*;
import java.util.Map;

public class Runner {

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("FRAME");
        JPanel jPanel = new JPanel();
        JButton button = new JButton("TExt");
        button.addActionListener(e -> {
            Map<String, Object> map = Runner2.loadData("IBM");
            Runner2.loadLayout(jPanel, map);
            jPanel.repaint();
            jFrame.repaint();
        });
        JTextField textField = new JTextField("DSAssad");
        jPanel.add(button);
        jPanel.add(textField);
        jPanel.setVisible(true);
        jFrame.add(jPanel);
        jFrame.setVisible(true);

//        AlphaVantageAPI api = new AlphaVantageAPI();
//        RequestHandler requestHandler = new RequestHandler(api, "IBM", 2015, 2020);
//        String[] financialData = requestHandler.requestFinancialData();
//        Parser parser = new Parser(financialData);
//        parser.parse();
    }
}
