package org.example;

import org.example.Opetations.Data.Poistovna;
import org.example.Opetations.Operations;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Gui gui = new Gui();



        gui.start();

        BSTTests test = new BSTTests();

        test.testIntervalSearch();

        test.testInsertMultiple(
                0,
                100,
                Integer.MAX_VALUE
        );

        test.testInsertBalanceFindRemove(
                0,
                100,
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