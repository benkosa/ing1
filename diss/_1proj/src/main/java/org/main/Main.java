package org.main;

import org.main.pi.Pi;

public class Main {
    public static void main(String[] args) {

        Pi pi = new Pi();
        // praca s grafmi
        System.out.println(pi.execute(9, 10, 1000000));
        System.out.println(Math.PI);

        SwingWorkerRealTime test = new SwingWorkerRealTime();
    }
}