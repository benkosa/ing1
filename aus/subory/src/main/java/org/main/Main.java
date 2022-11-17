package org.main;

import org.main.App.Nemocnica;
import org.main.hashing.Hashing;


public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");


        Hashing<Nemocnica> test = new Hashing<>(
                "file.dat",
                10,
                10,
                Nemocnica.class
        );

        test.readWholeFile();

        //Nemocnica nemocnica = new Nemocnica(70);

        //test.insert(nemocnica);
    }
}