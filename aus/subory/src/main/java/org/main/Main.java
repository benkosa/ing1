package org.main;

import org.main.app.Pacient;
import org.main.dynamic_hashing.DynamicHashing;

import java.util.BitSet;


public class Main {
    public static void main(String[] args) {


        DynamicHashing<Pacient> trie = new DynamicHashing("dynamic.dat", 2, Pacient.class);

//        trie.insert(BitSet.valueOf(new long[]{69}));

//        Node node = new InternalNode();
//
//        if (node instanceof ExternalNode) {
//            System.out.println("is internal node");
//        } else  {
//            System.out.println("is not internal node");
//        }

//        Gui gui = new Gui();
//
//        gui.start();

//        Tests tests = new Tests();
//        tests.testSize();
//        tests.testRandomOperation(
//                .5,
//                .25,
//                .25,
//                10,
//                100,
//                100,
//                10,
//                1000
//        );
//
//        Hashing<Pacient> test = new Hashing<>(
//                "file.dat",
//                10,
//                10,
//                Pacient.class
//        );
//
//        test.readWholeFileNoValid();


    }
}