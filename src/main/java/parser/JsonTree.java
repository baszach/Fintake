package parser;

import org.json.JSONObject;

import java.util.List;
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
                tree.addChildByKey(key);
            } else if (value instanceof List) { //TODO Why is representation of JSONArray a List?
                parseJsonArray(tree, (List<?>) value, key);
            } else if (value instanceof Map) { // TODO Why is representation of JSONObject a Map?
                if (tree.addChildByKey(key)) {
                    parseMapToTree(tree.getChildByKey(key).get(), map);
                }
            }
        });
    }

    private void parseJsonArray(KeyTree<String> tree, List<?> list, String key) {
        list.forEach(value -> {
            if (value instanceof String) {
                tree.addChildByKey(key);
            } else if (value instanceof List) {
                parseJsonArray(tree, (List<?>) value, key);
            } else if (value instanceof Map) {
                if (tree.addChildByKey(key)) {
                    parseMapToTree(tree.getChildByKey(key).get(), (Map<String, Object>) value);
                }
            }
        });
    }
}
