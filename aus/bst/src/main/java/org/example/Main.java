package org.example;

import java.util.Random;

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

        BSTTests test = new BSTTests();

        //test.testInsertWithDuplicities();
        //test.testInsert();
        //test.testInsertAndRemove();

        Random rand = new Random(2);
        BSTree<Integer> tree = new BSTree<>();
        for (int i = 0; i < 10; i++) {
            tree.insert(new Main.Element(rand.nextInt(100)));
        }

        tree.levelOrder().forEach(a -> System.out.print(a.key + ","));
        System.out.println();


        for (int i = 0; i< 4; i++) {
            tree.remove(tree.getRoot().key);
            tree.levelOrder().forEach(a -> System.out.print(a.key + ","));
            System.out.println();

        }

        System.out.println();
    }
}