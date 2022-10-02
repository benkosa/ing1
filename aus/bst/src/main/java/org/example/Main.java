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


        BSData<Integer> e1 = new Element(10);
        BSData<Integer> e2 = new Element(20);
        BSData<Integer> e3 = new Element(5);
        BSData<Integer> e4 = new Element(7);
        BSData<Integer> e5 = new Element(2);
        BSData<Integer> e6 = new Element(6);
        BSData<Integer> e7 = new Element(8);
        tree.insert(e1);
        tree.insert(e1);
        tree.insert(e2);
        tree.insert(e3);
        tree.insert(e4);
        tree.insert(e5);
        tree.insert(e6);
        tree.insert(e7);

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