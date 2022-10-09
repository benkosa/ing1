package org.example;

import java.util.ArrayList;
import java.util.Random;

public class BSTTests {

    public void testInsertWithDuplicities() {
        Random rand = new Random(2);

        int countElements = 0;
        System.out.println("make 999999 inserts with value (0..99)");
        System.out.println("expected result: 100");
        BSTree<Integer> tree = new BSTree<>();
        for (int i = 0; i < 999999; i++) {
            countElements+=tree.insert(new Main.Element(rand.nextInt(100))) ? 1 : 0;
        }
        System.out.println("result: " + countElements);
//        System.out.println("---------------------------------------------------");
//        System.out.println(tree.getHeight());
//        tree.levelOrder().forEach(a -> System.out.print(a.key + ","));
//        System.out.println();
//        tree.balanceTree();
//        tree.levelOrder().forEach(a -> System.out.print(a.key + ","));
//        System.out.println();
//        tree.inOrder().forEach(a -> System.out.print(a.key + ","));
//        System.out.println();
//        System.out.println(tree.inOrder().size());
//        System.out.println(tree.getHeight());
//        System.out.println("---------------------------------------------------");
    }

    public void testInsert() {
        System.out.println("test create 999999 different trees with 10 elements");
        System.out.println("expected result: no errors");
        for(int seed = 0; seed < 999999; seed++) {
            Random rand = new Random(seed);
            BSTree<Integer> tree = new BSTree<>();
            for (int i = 0; i < 10; i++) {
                tree.insert(new Main.Element(rand.nextInt(100)));
            }
        }
        System.out.println("result: no errors");
        System.out.println("---------------------------------------------------");
    }

    public void testInsertAndRemoveRoot() {
        System.out.println("test create 999999 different trees with 10 elements");
        System.out.println("remove root until tree is null");
        System.out.println("expected result: no errors");
        for(int seed = 0; seed < 999999; seed++) {
            Random rand = new Random(seed);
            BSTree<Integer> tree = new BSTree<>();
            for (int i = 0; i < 50; i++) {
                tree.insert(new Main.Element(rand.nextInt(100)));
            }
            int sizeBefore = tree.inOrder().size();
            int heightBefore = tree.getHeight();
            tree.balanceTree();
            int sizeAfter = tree.inOrder().size();
            int heightAfter = tree.getHeight();
            if (sizeBefore != sizeAfter) System.out.println("error: " + seed + " " + sizeBefore + " " + sizeAfter);
            //if (heightBefore < heightAfter) System.out.println("error: " + seed + " " + heightBefore + " " + heightAfter);
            if (heightAfter == 9) System.out.println(seed);
            while (tree.getRoot() != null) {
                //System.out.println(seed);
                tree.remove(tree.getRoot().key);
            }
        }
        System.out.println("result: no errors");
        System.out.println("---------------------------------------------------");
    }

    public void testRandomOperation(double pInsert, double pRemove, double pFind, int replication, int maxNumberOfElements, int maxValue) {
        System.out.println("Test random operations");
        System.out.println("pInsert: " + pInsert);
        System.out.println("pRemove: " + pRemove);
        System.out.println("pFind: " + pFind);
        System.out.println("replication: " + replication);
        System.out.println("maxNumberOfElements: " + maxNumberOfElements);
        System.out.println("maxValue: " + maxValue);
        System.out.println("expected result: no errors");
        if (pInsert + pRemove + pFind > 1) {
            System.out.println("error: p > 0");
            System.out.println("---------------------------------------------------");
            return;
        }

        for(int seed = 0; seed < replication; seed++) {
            Random rand = new Random(seed);
            BSTree<Integer> tree = new BSTree<>();

            for (int i = 0; i < maxNumberOfElements; i++) {
                Integer value = rand.nextInt(maxValue);
                double operation = rand.nextFloat();
                if (pInsert < operation) {
                    tree.insert(new Main.Element(value));
                } else if (pInsert + pRemove < operation) {
                    tree.remove(value);
                } else {
                    tree.find(value);
                }

            }
        }
        System.out.println("result: no errors");
        System.out.println("---------------------------------------------------");

    }

    public void testFind() {
        System.out.println("test find function by making tree, get levelOrder");
        System.out.println("array and find every element from array");
        System.out.println("expected result: no errors");

        for(int seed = 0; seed < 999999; seed++) {
            Random rand = new Random(seed);
            BSTree<Integer> tree = new BSTree<>();
            for (int i = 0; i < 10; i++) {
                tree.insert(new Main.Element(rand.nextInt(100)));
            }
            ArrayList<BSData<Integer>> insertedValues = tree.levelOrder();
            insertedValues.forEach(
                    element -> {
                        if (tree.find(element.key) == null) {
                            System.out.println("error: key not found");
                        }
                    }
            );
        }
        System.out.println("result: no errors");
        System.out.println("---------------------------------------------------");
    }


}
