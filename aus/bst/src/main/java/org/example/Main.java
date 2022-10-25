package org.example;

import org.example.Opetations.Data.Poistovna;
import org.example.Opetations.Operations;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

//        Gui gui = new Gui();
//
//        gui.start();
//
//
//        //if (true) return;
//
//        Operations oper = new Operations();
//
//        for (int i = 0; i < 10; i++) {
//            oper.Operation_12(i+" ");
//            oper.Operation_addPoistovna(i+" ");
//        }
//        for (int i = 0; i < 10; i++) {
//            oper.Operation_6(
//                    i+" ",
//                    i+" ",
//                    i+" ",
//                    "01-08-1997",
//                    null);
//        }
//
//        oper.Operation_6(
//                99+" ",
//                0+" ",
//                0+" ",
//                "01-08-1997",
//                null);
//        oper.Operation_6(
//                98+" ",
//                0+" ",
//                0+" ",
//                "01-08-1997",
//                null);
//        oper.Operation_6(
//                97+" ",
//                0+" ",
//                0+" ",
//                "01-08-1997",
//                null);
//
//
//        oper.data.nemocnice.inOrder().forEach(a -> System.out.print(a.key));
//        System.out.println();
//        oper.data.pacienti.inOrder().forEach(a -> System.out.print(a.key));
//        System.out.println();
//        oper.data.poistovne.inOrder().forEach(a -> System.out.print(a.key));
//        System.out.println();
//
//        oper.Operation_3("0 ", "0 ");
//        oper.Operation_3("99 ", "0 ");
//        oper.Operation_3("98 ", "0 ");
//        oper.Operation_3("97 ", "0 ");
//        oper.Operation_3("1 ", "0 ");
//        oper.Operation_3("2 ", "0 ");
//        oper.Operation_3("3 ", "0 ");
//        oper.Operation_8("0 ");
//        oper.Operation_4("0 ", "0 ");
//        oper.Operation_8("0 ");
//
//        oper.Operation_2("0 ", "0 ", "0 ");


        BSTTests test = new BSTTests();

        //test.testIntervalSearch();

        test.testInsertMultiple(
                0,
                100,
                Integer.MAX_VALUE
        );

        test.testInsertBalanceFindRemove(
                100000,
                100,
                Integer.MAX_VALUE
        );

        test.testRandomOperation(
                .5,
                .25,
                .25,
                0,
                1000,
                Integer.MAX_VALUE
        );
    }
}