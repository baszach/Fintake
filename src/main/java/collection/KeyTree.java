package collection;

import java.util.*;
import java.util.function.Predicate;

public class KeyTree<K extends Comparable<K>> implements Iterable<KeyTree<K>> { //TODO implement Collection?

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
        return removeChildrenIf(child ->
            child.key.equals(key)
        );
    }

    public boolean hasChildWithKey(K key) {
        return hasChildWith(child ->
            child.key.equals(key)
        );
    }

    public Optional<KeyTree<K>> getChildWithKey(K key) {
        return getChildWith(child ->
            child.key.equals(key)
        );
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public boolean removeChildrenIf(Predicate<KeyTree<K>> predicate) {
        boolean removed = false;
        Iterator<KeyTree<K>> it = children.iterator();
        while (it.hasNext()) {
            if (predicate.test(it.next())) {
                it.remove();
                removed = true;
            }
        }
        return removed;
    }

    public boolean hasChildWith(Predicate<KeyTree<K>> predicate) {
        for (KeyTree<K> child : children) {
            if (predicate.test(child)) {
                return true;
            }
        }
        return false;
    }

    public Optional<KeyTree<K>> getChildWith(Predicate<KeyTree<K>> predicate) {
        for (KeyTree<K> child: children) {
            if (predicate.test(child)) {
                return Optional.of(child);
            }
        }
        return Optional.empty();
    }

    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        forEach(tree -> {
            keys.add(tree.key);
        });
        return keys;
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
    public String toString() {
        if (parent != null) {
            return "[" + parent.key + "->" + this.key + "]";
        } else {
            return "[" + this.key + "]";
        }
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
}
