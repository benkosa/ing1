package org.example;

import javax.lang.model.element.Element;

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

    public static void main(String[] args) throws Throwable {

        BSTree<Integer> tree = new BSTree<Integer>();


        BSData e1 = new Element(10);
        BSData e2 = new Element(20);
        BSData e3 = new Element(5);
        BSData e4 = new Element(7);
        BSData e5 = new Element(2);
        BSData e6 = new Element(6);

        tree.insert(e1);
        tree.insert(e1);
        tree.insert(e2);
        tree.insert(e3);
        tree.insert(e4);
        tree.insert(e5);
        tree.insert(e6);

        tree.find(2);

        System.out.println(tree.find(2).key);
        System.out.println(tree.find(1));

        //tree.remove(2);
        //tree.remove(7);
        //System.out.println(tree.find(7));
        tree.remove(7);


    }
}