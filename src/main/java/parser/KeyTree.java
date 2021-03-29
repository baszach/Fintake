package parser;

import java.util.*;

public class KeyTree<K extends Comparable<K>> implements Iterable<KeyTree<K>> {

    private final K key;
    private final KeyTree<K> parent;
    private final HashSet<KeyTree<K>> children;

    private KeyTree(K key, KeyTree<K> parent) {
        this.key = key;
        this.parent = parent;
        this.children = new HashSet<>();
    }

    public static <K extends Comparable<K>> KeyTree<K> from(K key) {
        return new KeyTree<K>(key, null);
    }

    public KeyTree<K> getParent() {
        return parent;
    }

    public K getKey() {
        return key;
    }

    public boolean addChildByKey(K key) {
        return !hasChildWithKey(key) && children.add(new KeyTree<>(key, this));
    }

    public boolean removeChildByKey(K key) {
        for (KeyTree<K> child: children) {
            if (child.key.equals(key)) {
                return children.remove(child);
            }
        }
        return false;
    }

    public boolean hasChildWithKey(K key) {
        for (KeyTree<K> child: children) {
            if (child.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public Optional<KeyTree<K>> getChildByKey(K key) {
        for (KeyTree<K> child: children) {
            if (child.key.equals(key)) {
                return Optional.of(child);
            }
        }
        return Optional.empty();
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    //TODO outsource to other class
    public List<KeyTree<K>> leaves() {
        List<KeyTree<K>> leaves = new ArrayList<>();
        forEach(tree -> {
            if (tree.isLeaf())
                leaves.add(tree);
        });
        return leaves;
    }

    //TODO outsource to other class
    //TODO improve -> method to get path from root parent to this!!!
    public List<K> getPath() {
        ArrayList<K> path = new ArrayList<>();
        KeyTree<K> currentTree = this;
        do {
            path.add(currentTree.key);
            currentTree = this.parent;
        } while(currentTree != null);
        return path;
    }

    @Override
    public String toString() { // FIXME
        StringBuilder builder = new StringBuilder();
        final int[] level = {level()};
        forEach(tree -> {
            String s;
//            if (tree.level() > level[0]) {
//                builder.append("\n");
//                level[0] += 1;
//            }
//            if (tree.parent != null) {
//                s = "[" + tree.parent.key + "->" + tree.key + "]";
//            } else {
//                s = "[" + tree.key + "]";
//            }
            s = tree.key.toString();
            builder.append(s).append(", ");
        });
        return builder.toString();
    }

    public int level() { //FIXME
        int level = 0;
        KeyTree<K> currentTree = this;
        while (currentTree.parent != null) {
            currentTree = this.parent;
            level += 1;
        }
        return level;
    }

    @Override
    public Iterator<KeyTree<K>> iterator() {
        return breadthFirstIterator();
    }

    public BreadthFirstKeyTreeIterator breadthFirstIterator() {
        return new BreadthFirstKeyTreeIterator();
    }

    private class BreadthFirstKeyTreeIterator implements Iterator<KeyTree<K>> {
        private final Queue<KeyTree<K>> queue;

        private BreadthFirstKeyTreeIterator() {
            queue = new ArrayDeque<>();
            queue.add(KeyTree.this);
        }

        @Override
        public boolean hasNext() {
            return ! queue.isEmpty();
        }

        @Override
        public KeyTree<K> next() {
            KeyTree<K> current = queue.remove();
            queue.addAll(current.children);
            return current;
        }
    }

    public DepthFirstKeyTreeIterator depthFirstIterator() {
        return new DepthFirstKeyTreeIterator();
    }

    private class DepthFirstKeyTreeIterator implements Iterator<KeyTree<K>> {
        private final Stack<KeyTree<K>> stack;

        private DepthFirstKeyTreeIterator() {
            stack = new Stack<>();
            stack.push(KeyTree.this);
        }

        @Override
        public boolean hasNext() {
            return ! stack.isEmpty();
        }

        @Override
        public KeyTree<K> next() {
            KeyTree<K> current = stack.pop();
            stack.addAll(current.children);
            return current;
        }
    }
}
