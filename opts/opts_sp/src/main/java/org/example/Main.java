package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {

    public static int[][] read_file(File file) {
        int M = 0;
        int[][] data = new int[0][];

        try {

            BufferedReader bfr = new BufferedReader(new FileReader(file));
            String line = "";
            int line_index = 0;
            int row = 0, col = 0;
            while ((line = bfr.readLine()) != null) {
                if (line_index == 0) {
                    M  = Integer.parseInt(line);
                    data = new int[M][];
                }  else     {
                    String[] tokens = line.split(" ");
                    int[] intTokens = new int[tokens.length];
                    for(int i = 0; i < intTokens.length; i++) {
                        intTokens[i] = Integer.parseInt(tokens[i]);
                    }

                    data[row] = intTokens;
                    row++;
                }
                line_index = line_index + 1;
            }
            bfr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("error " + e);
        }

        return data;
    }

    /**
     * 1. pociatocnu trasu chapem
     * 2. presuniem vrchol na ine miesto a skontorlujem
     *
     *
     * @param args
     */


    public static void main(String[] args) {
        File fileMaticaBB = new File("Matica_BB_(0515).txt");
        int data[][] = read_file(fileMaticaBB);

        for (int i = 0; i < data.length; i++) {
            System.out.println(data[i].length);
        }
        System.out.println("Hello world!");
    }
}