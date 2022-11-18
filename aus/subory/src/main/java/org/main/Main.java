package org.main;

import org.main.app.Pacient;
import org.main.hashing.Hashing;
import org.main.shared.StringStore;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.Date;


public class Main {
    public static void main(String[] args) {

        Hashing<Pacient> test = new Hashing<>(
                "file.dat",
                10,
                10,
                Pacient.class,
                true
        );

        Pacient pacient = new Pacient("meno", "preizvisko", "45", 0, new Date());

        test.insert(pacient);

        System.out.println("---------------------------------------------");

        test.readWholeFile();
    }
}