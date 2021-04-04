package parser;

import collection.KeyTree;
import org.json.JSONObject;

import java.util.*;

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
                    Optional<KeyTree<String>> result = tree.getChildWithKey(key);
                    result.ifPresent(resultTree -> {
                        resultTree.addChildByKey((String) value); //TODO is it good to add values? probably not...
                    });
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
            list.forEach(value -> {
                if (value instanceof String) {
                    tree.addChildByKey(key);
                } else if (value instanceof List) {
                    parseJsonArray(tree, (List<?>) value, key);
                } else if (value instanceof Map) {
                    if (tree.addChildByKey(key)) {
                        parseMapToTree(tree.getChildWithKey(key).get(), (Map<String, Object>) value);
                    }
                }
            });
        }
    }
}
