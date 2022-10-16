package org.example;

import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {




        BSTTests test = new BSTTests();

        test.testInsertMultiple(
                10,
                1000000,
                Integer.MAX_VALUE
        );



//        test.testInsertBalanceFindRemove(
//                99999,
//                1000,
//                Integer.MAX_VALUE
//        );
//
//        test.testRandomOperation(
//                .5,
//                .25,
//                .25,
//                99999,
//                300,
//                300
//        );
    }
}