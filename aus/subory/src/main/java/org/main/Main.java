package org.main;

import org.main.app.Pacient;
import org.main.dynamic_hashing.DynamicHashing;
import org.main.shared.Tests;

import java.util.BitSet;
import java.util.Date;


public class Main {
    public static void main(String[] args) {

        Pacient pacient = new Pacient("Benjamin", "Kosa", "9708014855", 2,  new Date());
        Pacient pacient2 = new Pacient("Benjamin", "Kosa", "9708014856", 2,  new Date());
        Pacient pacient3 = new Pacient("Benjamin", "Kosa", "9708014857", 2,  new Date());

        DynamicHashing<Pacient> dh =
                new DynamicHashing("dynamic.dat", 2, Pacient.class);

        dh.insert(pacient);
        dh.insert(pacient2);
        dh.insert(pacient3);

        dh.readWholeFileNoValid();

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