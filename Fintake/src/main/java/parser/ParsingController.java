package parser;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParsingController {

    private String json;

    public ParsingController() {
        this.json = "";
    }

    public Set<String> parse() {
        if(json.isEmpty()) {
            return new HashSet<>();
        } else {
            StringReader reader = new StringReader(json);
            JSONTokener jsonTokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(jsonTokener);
            return jsonObject.toMap().keySet();
        }
    }

    public void updateJsonString(String json) {
        this.json = json;
    }
}
