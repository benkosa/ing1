package org.example;

import org.example.Opetations.Operations;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

//        Gui gui = new Gui();
//
//        gui.setContentPane(gui.getJPanel());
//        gui.setTitle("hello");
//        gui.setSize(300, 400);
//        gui.setVisible(true);
//        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        Operations oper = new Operations();

        for (int i = 0; i < 10; i++) {
            oper.Operation_12(i+" ");
            oper.Operation_6(i+" ", i+" ", i+" ", null, null);
        }
        oper.data.nemocnice.inOrder().forEach(a -> System.out.print(a.key));
        System.out.println();
        oper.data.pacienti.inOrder().forEach(a -> System.out.print(a.key));
        System.out.println();

        BSTTests test = new BSTTests();

        //test.testIntervalSearch();

        test.testInsertMultiple(
                0,
                100,
                Integer.MAX_VALUE
        );

        test.testInsertBalanceFindRemove(
                0,
                50,
                Integer.MAX_VALUE
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