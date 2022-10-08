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

//        test.testInsertWithDuplicities();
//        test.testInsert();
        test.testInsertAndRemoveRoot();
//        test.testRandomOperation(.5,.5, 0, 9999,100, 100);
//        test.testFind();

        //BSTree<Integer> tree = new BSTree<>();

        Random rand = new Random(53);
        BSTree<Integer> tree = new BSTree<>();
        for (int i = 0; i < 10; i++) {
            tree.insert(new Main.Element(rand.nextInt(100)));
        }
        int sizeBefore = tree.inOrder().size();
        tree.levelOrder().forEach(a -> System.out.print(a.key + ","));
        System.out.println();
        tree.balanceTree();
        tree.levelOrder().forEach(a -> System.out.print(a.key + ","));
        System.out.println();


//        tree.insert(new Element(2));
//        tree.insert(new Element(6));
//        tree.insert(new Element(1));
//        tree.insert(new Element(3));
//        tree.insert(new Element(5));
//        tree.insert(new Element(7));
//        tree.insert(new Element(4));
//
//        for (int i = 1; i <= 10; i++) tree.insert(new Element(i));
//
////        tree.levelOrder().forEach(a -> System.out.print(a.key + ","));
////        System.out.println();
////        tree.rightRotation(tree.getRootNode());
////        tree.levelOrder().forEach(a -> System.out.print(a.key + ","));
////        System.out.println();
////        tree.leftRotation(tree.getRootNode());
////        tree.levelOrder().forEach(a -> System.out.print(a.key + ","));
//
//        tree.inOrder().forEach(a -> System.out.print(a.key + ","));
//        System.out.println();
//        tree.levelOrder().forEach(a -> System.out.print(a.key + ","));
//        System.out.println();
//        tree.balanceTree();
//        tree.levelOrder().forEach(a -> System.out.print(a.key + ","));
//        System.out.println();
//        tree.inOrder().forEach(a -> System.out.print(a.key + ","));
//        System.out.println();
    }
}