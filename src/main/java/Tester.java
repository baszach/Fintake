import parser.KeyTree;

public class Tester {

    public static void main(String[] args) {
        KeyTree<String> tree = KeyTree.from("root");
        tree.addChildByKey("1");
        tree.addChildByKey("2");
        tree.getChildByKey("1").addChildByKey("3");
        System.out.println(tree);
    }
}
