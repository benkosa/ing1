package org.main;

import org.main.app.Pacient;
import org.main.dynamic_hashing.DynamicHashing;
import org.main.shared.Tests;

import java.util.BitSet;
import java.util.Date;


public class Main {
    public static void main(String[] args) {

//        Pacient pacient = new Pacient("Benjamin", "Kosa", "85", 2,  new Date());
//        Pacient pacient2 = new Pacient("Benjamin", "Kosa", "88", 2,  new Date());
//        Pacient pacient3 = new Pacient("Benjamin", "Kosa", "47", 2,  new Date());
//        Pacient pacient4 = new Pacient("Benjamin", "Kosa", "4", 2,  new Date());
//
//        DynamicHashing<Pacient> dh =
//                new DynamicHashing("dynamic.dat", 2, Pacient.class);
//
//
//        System.out.println(dh.insert(pacient));
//        System.out.println(dh.insert(pacient2));
//        System.out.println(dh.insert(pacient3));
//        //System.out.println(dh.insert(pacient4));
//
//
//
//        System.out.println(dh.find(pacient).getRodneCislo());
//        System.out.println(dh.find(pacient2).getRodneCislo());
//        System.out.println(dh.find(pacient3).getRodneCislo());
//        //System.out.println(dh.find(pacient4).getRodneCislo());
//
//        System.out.println(dh.delete(pacient));
//        System.out.println(dh.delete(pacient2));
//        System.out.println(dh.delete(pacient3));
        //System.out.println(dh.delete(pacient4));
//
//        dh.readWholeFileNoValid();
        //dh.delete(pacient2);

        //dh.readWholeFileNoValid();

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

        Tests tests = new Tests();
        tests.testSize();
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
//        tests.testRandomOperationDynamic(
//                .5,
//                .25,
//                .25,
//                1000,
//                100,
//                100,
//                10
//        );

        tests.testInsertOperationDynamic(
                1000,
                3,
                100,
                2,
                -1
        );
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