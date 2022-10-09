package org.example;

import java.util.Random;

public class Main {

    public static void main(String[] args) {

        BSTTests test = new BSTTests();

        test.testInsertBalanceFindRemove(
                99999,
                100,
                100
        );

        test.testRandomOperation(
                .5,
                .25,
                .25,
                99999,
                100,
                100
        );
    }
}