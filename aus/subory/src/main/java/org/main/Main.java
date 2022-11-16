package org.main;

import org.main.App.Nemocnica;
import org.main.hashing.Hashing;


public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");


        Hashing<Nemocnica> test = new Hashing<Nemocnica>("file.dat", 10);

        Nemocnica nemocnica = new Nemocnica(69);

        test.insert(nemocnica);
    }
}