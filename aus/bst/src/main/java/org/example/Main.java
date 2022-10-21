package org.example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Gui gui = new Gui();

        gui.setContentPane(gui.getJPanel());
        gui.setTitle("hello");
        gui.setSize(300, 400);
        gui.setVisible(true);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BSTTests test = new BSTTests();

        //test.testIntervalSearch();

        test.testInsertMultiple(
                0,
                100,
                Integer.MAX_VALUE
        );

        test.testInsertBalanceFindRemove(
                0,
                10,
                100
        );

        test.testRandomOperation(
                .5,
                .25,
                .25,
                0,
                100,
                Integer.MAX_VALUE
        );
    }
}