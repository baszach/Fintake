package parser;

import org.json.JSONObject;

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
            JSONObject jsonObject = new JSONObject(json);
            System.out.println(jsonObject);
            map.putAll(jsonObject.toMap());
        }
        System.out.println(map);
        return map;
    }

    public JSONObject firstObj() {
        for (String json: jsons) {
            return new JSONObject(json);
        }
        return null;
    }


}
