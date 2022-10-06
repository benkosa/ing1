package org.example;

import java.util.Random;

public class BSTTests {

    public void testInsertWithDuplicities() {
        Random rand = new Random();

        Integer countElements = 0;
        System.out.println("make 999999 inserts with value (0..99)");
        System.out.println("expected result: 100");
        BSTree<Integer> tree = new BSTree<>();
        for (int i = 0; i < 999999; i++) {
            countElements+=tree.insert(new Main.Element(rand.nextInt(100))) ? 1 : 0;
        }
        System.out.println("result: " + countElements);
        System.out.println("---------------------------------------------------");
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

    public void testInsertAndRemove() {
        System.out.println("test create 999999 different trees with 10 elements");
        System.out.println("remove node until tree is null");
        System.out.println("expected result: no errors");
        for(int seed = 0; seed < 999999; seed++) {
            Random rand = new Random(seed);
            BSTree<Integer> tree = new BSTree<>();
            for (int i = 0; i < 10; i++) {
                tree.insert(new Main.Element(rand.nextInt(100)));
            }
            while (tree.getRoot() != null) {
                tree.remove(tree.getRoot().key);
            }
            System.out.println("seed: " + seed);
        }
        System.out.println("result: no errors");
        System.out.println("---------------------------------------------------");
    }


}
