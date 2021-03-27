package parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class JsonTree {

    private final JSONObject jsonObject;
    private final KeyTree<String> jsonKeysTree;

    public JsonTree(JSONObject jsonObject, String parentKey) {
        this.jsonObject = jsonObject;
        this.jsonKeysTree = KeyTree.from(parentKey);
    }

    public void buildTree() {
        parseMapToTree(jsonKeysTree, jsonObject.toMap());
    }

    public KeyTree<String> getTree() {
        return jsonKeysTree;
    }

    private void parseMapToTree(KeyTree<String> tree, Map<String, Object> map) {
        map.forEach((key, value) -> {
            if (value instanceof String) {
                map.remove(key);
                tree.addChildByKey(key);
            } else if (value instanceof JSONArray) {
                parseJsonArray(tree, map, (JSONArray) value, key);
            } else if (value instanceof JSONObject) {
                if (tree.addChildByKey(key)) {
                    parseMapToTree(tree.getChildByKey(key), ((JSONObject) value).toMap());
                }
            }
        });
    }

    private void parseJsonArray(KeyTree<String> tree, Map<String, Object> map, JSONArray jsonArray, String key) {
        jsonArray.forEach(value -> {
            if (value instanceof String) {
                map.remove(key);
                tree.addChildByKey(key);
            } else if (value instanceof JSONArray) {
                parseJsonArray(tree, map, (JSONArray) value, key);
            } else if (value instanceof JSONObject) {
                if (tree.addChildByKey(key)) {
                    parseMapToTree(tree.getChildByKey(key), ((JSONObject) value).toMap());
                }
            }
        });
    }
}
