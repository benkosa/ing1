package org.main;

import org.main.app.Gui;
import org.main.app.Hospitalizacia;
import org.main.app.Pacient;
import org.main.hashing.Hashing;
import org.main.shared.StringStore;
import org.main.shared.Tests;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.Date;


public class Main {
    public static void main(String[] args) {

        Gui gui = new Gui();

        gui.start();

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
//                10000
//        );

//        Hashing<Pacient> test = new Hashing<>(
//                "file.dat",
//                10,
//                10,
//                Pacient.class
//        );

//        Hashing<Pacient> test = new Hashing<>(
//                "file.dat",
//                Pacient.class
//        );
//
//
//        Pacient pacient = new Pacient("meno", "preizvisko", "  ", 0, new Date());
//
//        test.insert(pacient);
//
//        System.out.println(test.find(pacient).toString());
//
//        test.delete(pacient);
//
//
//
//        System.out.println("---------------------------------------------");
//
//        test.readWholeFile();

//        Hashing<Hospitalizacia> test2 = new Hashing<>(
//                "file2.dat",
//                10,
//                10,
//                Hospitalizacia.class,
//                true
//        );
//
//        Hospitalizacia hospitalizacia = new Hospitalizacia(0, new Date(), new Date(), "sdffsd");
//
//        test2.insert(hospitalizacia);
//
//        System.out.println("---------------------------------------------");
//
//        test2.readWholeFile();
    }
}