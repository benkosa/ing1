package org.example;

import org.example.Shared.Comparators;
import org.example.Shared.Compare;

import java.util.ArrayList;
import java.util.Random;



public class BSTTests {

    public static class Element extends BSData<Integer> {
        Element(Integer key) {
            super(key);
        }

        int counter = 1;

        @Override
        public Compare compare(BSData<Integer> data) {
            Comparators comparators = new Comparators();
            return comparators.intCompare(data.key, this.key);
        }
    }

    public void testInsertBalanceFindRemove(int replications, int maxNumberOfElements, int maxInt) {
        if (replications == 0) return;
        System.out.println("test: " + replications + " replications, max value: " + maxInt);
        System.out.println(" - insert " + maxNumberOfElements + " number of elements");
        System.out.println(" - balance tree");
        System.out.println(" - find all inserted nodes");
        System.out.println(" - remove tree from node");
        System.out.println("remove root until tree is null");
        System.out.println("expected result: no errors");
        BSTree<Integer> treeHeights = new BSTree<>();

        for(int seed = 0; seed < replications; seed++) {
            Random rand = new Random(seed);
            BSTree<Integer> tree = new BSTree<>();
            ArrayList<BSData<Integer>> insertedElements = new ArrayList<>();

            // create tree
            for (int i = 0; i < maxNumberOfElements; i++) {
                final Element element = new Element(rand.nextInt(maxInt));
                if (tree.insert(element)) {
                    insertedElements.add(element);
                }
            }

            // balance
            int heightBefore = tree.getHeight();
            int sizeBefore = tree.inOrder().size();
            tree.balanceTree();
            int heightAfter = tree.getHeight();
            int sizeAfter = tree.inOrder().size();
            if (sizeBefore != sizeAfter)
                System.out.println("error lost nodes: " + seed + " " + sizeBefore + " " + sizeAfter);
            System.out.print("\b\b\b\b\b");
            System.out.print(Math.round(((float)seed/replications)*100) + " %");

            //tree.checkHeight();

            // find inserted nodes
            for (BSData<Integer> element: insertedElements) {
                if (tree.find(element.key) == null) {
                    System.out.println("error: lost element " + seed);
                }
            }


            Element height = (Element)treeHeights.find(heightAfter);
            if (height == null) {
                treeHeights.insert(new Element(heightAfter));
            } else {
                height.counter++;
            }

            // remove tree
            while (tree.getRoot() != null) {
                tree.remove(tree.getRoot().key);
            }
        }
        System.out.println("best height: " + countBestHeight(maxNumberOfElements));
        System.out.println();
        int sum = 0;

        for (BSData<Integer> integerBSData : treeHeights.inOrder()) {
            Element element = (Element)integerBSData;
            System.out.println("height: " + element.key + ", occurrences: " + element.counter);
            sum+=element.key*element.counter;
        }
        System.out.println("average height: " + Math.ceil((double)sum/replications*100)/100);
        System.out.println("result: no errors");
        System.out.println("---------------------------------------------------");
    }

    public void testRandomOperation(double pInsert, double pRemove, double pFind, int replication, int maxNumberOfElements, int maxValue) {
        if (replication == 0) return;
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
                    tree.insert(new Element(value));
                } else if (pInsert + pRemove < operation) {
                    tree.remove(value);
                } else {
                    tree.find(value);
                }

            }

            System.out.print("\b\b\b\b\b");
            System.out.print(Math.round(((float)seed/replication)*100) + " %");
        }

        System.out.println();
        System.out.println("result: no errors");
        System.out.println("---------------------------------------------------");

    }

    public void testInsertMultiple(int replications, int maxNumberOfElements, int maxInt) {
        if (replications == 0) return;
        BSTree<Integer> treeHeights = new BSTree<>();

        System.out.println();

        for(int seed = 0; seed < replications; seed++) {
            Random rand = new Random(seed);
            BSTree<Integer> tree = new BSTree<>();
            ArrayList<BSData<Integer>> elementsToInsert = new ArrayList<>();

            // create tree
            for (int i = 0; i < maxNumberOfElements; i++) {
                elementsToInsert.add(new Element(rand.nextInt(maxInt)));
            }
            tree.insertMultiple(elementsToInsert);
            int heightAfter = tree.getHeight();

            Element height = (Element)treeHeights.find(heightAfter);
            if (height == null) {
                treeHeights.insert(new Element(heightAfter));
            } else {
                height.counter++;
            }

            System.out.print("\b\b\b\b\b");
            System.out.print(Math.round(((float)seed/replications)*100) + " %");

        }
        System.out.println();
        System.out.println("best height: " + countBestHeight(maxNumberOfElements));
        treeHeights.inOrder().forEach(data -> {
            Element element = (Element)data;
            System.out.println("height: " + element.key + ", occurrences: " + element.counter);
            System.out.println("---------------------------------------------------");
        });
    }

    private int countBestHeight(int numberOfElements) {
        int height = 0;
        for (int i = 1; i <= numberOfElements; i*=2) {
            height++;
        }
        return height;
    }


    public void testIntervalSearch() {
        BSTree<Integer> tree = new BSTree<>();
        for (int i = 0; i < 100; i++) {
            tree.insert(new Element(i));
        }
        tree.balanceTree();
        System.out.println(tree.intervalSearch(10, 20).size());
        tree.intervalSearch(10, 20).forEach(a -> System.out.print(a.key +" "));
        System.out.println();
    }

}
