import org.junit.Test;
import static org.junit.Assert.*;
import collection.KeyTree;

import java.util.Optional;

public class KeyTreeTest {

    @Test
    public void testInitialization() {
        KeyTree<String> keyTree = KeyTree.from("Duck parent");
        assertEquals("Duck parent", keyTree.getKey());
        assertNull(keyTree.getParent());
    }

    @Test
    public void testTreeOperations() {
        KeyTree<String> keyTree = KeyTree.from("Duck parent");
        /* adding child */
        boolean add1 = keyTree.addChildByKey("Duckling 1"); // true
        boolean add2 = keyTree.addChildByKey("Duckling 2"); // true
        boolean add3 = keyTree.addChildByKey("Non duckling"); // true
        boolean add4 = keyTree.addChildByKey("Duckling 1"); // false

        /* removing child */
        boolean remove1 = keyTree.removeChildByKey("Non duckling"); // true

        /* getting children */
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        KeyTree<String> get1 = keyTree.getChildWithKey("Duckling 1").get(); // Duckling 1
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        KeyTree<String> get2 = keyTree.getChildWithKey("Duckling 2").get(); // Duckling 2
        Optional<KeyTree<String>> get3 = keyTree.getChildWithKey("Non duckling"); // Optional.empty()

        /* contains children */
        boolean contains1 = keyTree.hasChildWithKey("Duckling 1"); // true
        boolean contains2 = keyTree.hasChildWithKey("Non duckling"); // false

        /* is a leaf */
        boolean isLeaf1 = keyTree.isLeaf(); // false
        boolean isLeaf2 = get1.isLeaf();

        assertTrue(add1);
        assertTrue(add2);
        assertTrue(add3);
        assertFalse(add4);
        assertTrue(remove1);
        assertEquals("Duckling 1", get1.getKey());
        assertEquals("Duckling 2", get2.getKey());
        assertEquals(Optional.empty(), get3);
        assertTrue(contains1);
        assertFalse(contains2);
        assertFalse(isLeaf1);
        assertTrue(isLeaf2);
    }
}
