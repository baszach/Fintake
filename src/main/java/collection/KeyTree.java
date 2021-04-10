package collection;

import java.util.*;
import java.util.function.Predicate;

public class KeyTree<K extends Comparable<K>> implements Iterable<KeyTree<K>>, Comparable<K> {

    private final K key;
    private final KeyTree<K> parent;
    private final Set<KeyTree<K>> children;
    private final int level;

    private KeyTree(K key, KeyTree<K> parent, int level) {
        this.key = key;
        this.parent = parent;
        this.children = new LinkedHashSet<>();
        this.level = level;
    }

    public static <K extends Comparable<K>> KeyTree<K> from(K key) {
        return new KeyTree<>(key, null, 0);
    }

    public KeyTree<K> getParent() {
        return parent;
    }

    public K getKey() {
        return key;
    }

    public int getLevel() {
        return level;
    }

    public boolean addChildByKey(K key) {
        return !hasChildWithKey(key) && children.add(
                new KeyTree<>(key, this, level + 1));
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
        forEach(tree -> keys.add(tree.key));
        return keys;
    }

    @Override
    public int compareTo(K other) {
        return key.compareTo(other);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[key=").append(this.key)
                .append(", level=").append(level);
        if (parent != null) {
            builder.append(", parent=").append(parent.key);
        }
        return builder.append("]").toString();
    }

    @Override
    public Iterator<KeyTree<K>> iterator() {
        return breadthFirstIterator();
    }

    public Iterator<KeyTree<K>> breadthFirstIterator() {
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

    public Iterator<KeyTree<K>> depthFirstKeyTreeIterator() {
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
