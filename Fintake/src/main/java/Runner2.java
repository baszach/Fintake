import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Runner2 {

    //AAPL date2020 date2016
    public static Map<String, Object> loadData(String symbol) {
        //generate urls
        Map<String, Object> map = new HashMap<>();
        map.put("1", "meow");
        map.put("2", "muh");
        map.put("3", "wuff");
        return map;
    }

//    public Map<String, Object> loadData(String symbol, LocalDateTime fromDate,  LocalDateTime toDate, Url url) {
//        //get jsons
//        JSONParser parser = new JSONParser()
//        Map<String, Object> result = parser.parse(json);
//
//    }

    public static void loadLayout(JPanel parent, Map<String, Object> result){
        for(String child:result.keySet()){
            JCheckBox check = new JCheckBox(child);
            parent.add(check);
        }
    }
}
