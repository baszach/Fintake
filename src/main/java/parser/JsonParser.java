package parser;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class JsonParser {

    String[] jsons;

    public JsonParser() {
        jsons = new String[0];
    }

    public void updateJsons(String... jsons) {
        this.jsons = jsons;
    }

    public Map<String, Object> jsonsToMap() {
        HashMap<String, Object> map = new HashMap<>();
        for(String json: jsons) {
            StringReader reader = new StringReader(json);
            JSONTokener jsonTokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(jsonTokener);
            map.putAll(jsonObject.toMap());
        }
        return map;
    }


}
