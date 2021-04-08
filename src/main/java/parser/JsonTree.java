package parser;

import collection.KeyTree;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonTree {

    private final KeyTree<String> jsonKeysTree;

    private JsonTree(KeyTree<String> jsonKeysTree) {
        this.jsonKeysTree = jsonKeysTree;
    }

    public KeyTree<String> getTree() {
        return jsonKeysTree;
    }

    public static class Builder {
        private final KeyTree<String> tree;
        private final Set<JSONObject> jsonObjects;

        public Builder(String parentKey) {
            this.tree = KeyTree.from(parentKey);
            this.jsonObjects = new HashSet<>();
        }

        public Builder addJsonObject(JSONObject jsonObject) {
            this.jsonObjects.add(jsonObject);
            return this;
        }

        public JsonTree parseObjectsAndCreateTree() {
            for (JSONObject jsonObject: jsonObjects) {
                parseMapToTree(tree, jsonObject.toMap());
            }
            return new JsonTree(tree);
        }

        private void parseMapToTree(KeyTree<String> tree, Map<String, Object> map) {
            map.forEach((key, value) -> {
                if (value instanceof String) {
                    tree.addChildByKey(key);
                } else if (value instanceof List) {
                    parseJsonArray(tree, (List<?>) value, key);
                } else if (value instanceof Map) {
                    if (tree.addChildByKey(key)) {
                        parseMapToTree(tree.getChildWithKey(key).get(), map);
                    }
                }
            });
        }

        private void parseJsonArray(KeyTree<String> tree, List<?> list, String key) {
            for (int i = 0; i < list.size(); i++) {
                Object value = list.get(i);
                tree.addChildByKey(key);
                KeyTree<String> newTree = tree.getChildWithKey(key).get();
                if (value instanceof String) {
                    newTree.addChildByKey(key);
                } else if (value instanceof List) {
                    parseJsonArray(newTree, (List<?>) value, String.valueOf(i));
                } else if (value instanceof Map) {
                    if (newTree.addChildByKey(String.valueOf(i))) {
                        parseMapToTree(newTree.getChildWithKey(String.valueOf(i)).get(), (Map<String, Object>) value);
                    }
                }
            }
        }
    }
}
