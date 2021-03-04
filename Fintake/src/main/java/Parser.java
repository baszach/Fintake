import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.StringReader;

public class Parser {

    private final String[] jsonText;

    public Parser(String[] jsonText) {
        this.jsonText = jsonText;
    }

    public JSONObject parse() {
        JSONObject master = new JSONObject();
        for(String s: jsonText) {
            StringReader reader = new StringReader(s);
            JSONTokener jsonTokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(jsonTokener);
            master.put("1", jsonObject);
        }

        System.out.println(master.toString());
        return master;
    }

}
