package org.main;

import org.main.app.Pacient;
import org.main.hashing.Hashing;
import org.main.shared.StringStore;


public class Main {
    public static void main(String[] args) {

        Hashing<Pacient> test = new Hashing<>(
                "file.dat",
                10,
                100,
                Pacient.class
        );

        test.readWholeFile();

        //Nemocnica nemocnica = new Nemocnica(70);

        //test.insert(nemocnica);
    }
}