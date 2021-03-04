package parser;

import java.util.Map;

public class Parser implements ParsingCommunicator {

    private String json;

    public Parser() {
        this.json = "";
    }

    public Map<String, Object> parse() {
        //TODO parsing to a Map<String, Object>
        return null;
    }

    public void updateJsonString(String json) {
        this.json = json;
    }

//            StringReader reader = new StringReader(json);
//            JSONTokener jsonTokener = new JSONTokener(reader);
//            JSONObject jsonObject = new JSONObject(jsonTokener);

}
