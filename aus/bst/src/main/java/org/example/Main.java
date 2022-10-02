package org.example;

public class Main {

    public static class Element extends BSData<Integer> {
        Element(Integer key) {
            super(key);
        }

        @Override
        public Compare compare(BSData<Integer> data) {
            if (data.key < this.key) return Compare.LESS;
            if (data.key > this.key) return Compare.MORE;
            return Compare.EQUAL;
        }
    }

    public static void main(String[] args) {

        BSTree<Integer> tree = new BSTree<>();

        tree.insert(new Element(10));
        tree.insert(new Element(20));
        tree.insert(new Element(5));
        tree.insert(new Element(7));
        tree.insert(new Element(2));
        tree.insert(new Element(6));
        tree.insert(new Element(8));

        tree.find(2);

        //System.out.println(tree.find(2).key);
        //System.out.println(tree.find(1));

        //tree.remove(2);
        //tree.remove(7);
        //System.out.println(tree.find(7));
        //tree.remove(7);

        tree.levelOrder().forEach(n -> System.out.print(n.key.toString() + ' '));
        System.out.println();
        tree.inOrder().forEach(n -> System.out.print(n.key.toString() + ' '));
        System.out.println();
        tree.postOrder().forEach(n -> System.out.print(n.key.toString() + ' '));


    }
}