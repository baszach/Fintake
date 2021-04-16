package parser;

import collection.KeyTree;
import org.json.JSONObject;

import java.util.*;

public class JsonKeySelectionTree {

    private final KeyTree<KeySelection> jsonKeysTree;

    private JsonKeySelectionTree(KeyTree<KeySelection> jsonKeysTree) {
        this.jsonKeysTree = jsonKeysTree;
    }

    public KeyTree<KeySelection> getTree() {
        return jsonKeysTree;
    }

    //TODO IMPROVE PARSING LOGIC!!!
    public static class Builder {
        private final KeyTree<KeySelection> tree;
        private final Set<JSONObject> jsonObjects;

        public Builder(String parentKey) {
            this.tree = KeyTree.from(new KeySelection(parentKey));
            this.jsonObjects = new HashSet<>();
        }

        public Builder addJsonObject(JSONObject jsonObject) {
            this.jsonObjects.add(jsonObject);
            return this;
        }

        public JsonKeySelectionTree parseObjectsToTree() {
            for (JSONObject jsonObject: jsonObjects) {
                parseMapToTree(tree, jsonObject.toMap());
            }
            return new JsonKeySelectionTree(tree);
        }

        private void parseMapToTree(KeyTree<KeySelection> tree, Map<String, Object> map) {
            map.forEach((key, value) -> {
                if (value instanceof String) {
                    tree.addChildByKey(new KeySelection(key, false, (String) value));
                } else if (value instanceof List) {
                    parseJsonArray(tree, (List<?>) value, key);
                } else if (value instanceof Map) {
                    if (tree.addChildByKey(new KeySelection(key))) {
                        //noinspection OptionalGetWithoutIsPresent
                        parseMapToTree(tree.getChildWithKey(new KeySelection(key)).get(), map);
                    }
                }
            });
        }

        private void parseJsonArray(KeyTree<KeySelection> tree, List<?> list, String key) {
            for (int i = 0; i < list.size(); i++) {
                Object value = list.get(i);
                tree.addChildByKey(new KeySelection(key));
                //noinspection OptionalGetWithoutIsPresent
                KeyTree<KeySelection> newTree = tree.getChildWithKey(new KeySelection(key)).get();
                if (value instanceof String) {
                    tree.addChildByKey(new KeySelection(key, false, (String) value));
                } else if (value instanceof List) {
                    parseJsonArray(newTree, (List<?>) value, String.valueOf(i));
                } else if (value instanceof Map) {
                    String indexKey = String.valueOf(i);
                    if (newTree.addChildByKey(new KeySelection(indexKey))) {
                        //noinspection OptionalGetWithoutIsPresent
                        parseMapToTree(newTree.getChildWithKey(new KeySelection(indexKey)).get(), (Map<String, Object>) value);
                    }
                }
            }
        }
    }

    public static class KeySelection implements Comparable<KeySelection> {
        private final String key;
        private boolean selected;
        private final String value;

        public KeySelection(String key, boolean selected, String value) {
            this.key = key;
            this.selected = selected;
            this.value = value;
        }

        public KeySelection(String key, boolean selected) {
            this(key, selected, "");
        }

        public KeySelection(String key) {
            this(key, false);
        }

        public String key() {
            return key;
        }

        public String value() {
            return value;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;
            KeySelection that = (KeySelection) other;
            return Objects.equals(key, that.key);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

        @Override
        public int compareTo(KeySelection other) {
            return key.compareTo(other.key);
        }

        @Override
        public String toString() {
            return key;
        }
    }
}
