package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
     * search for base way
     * @param data
     * @return
     */
    public static ArrayList<Integer> getBaseWay(int[][] data) {
        int mSize = data.length;

        //nepouzite vrcholy
        LinkedList<Integer> wayBucket = new LinkedList<>();
        for (int i = 0; i < mSize; i++) {
            wayBucket.add(i);
        }
        //cesta
        ArrayList<Integer> baseWay = new ArrayList<>(mSize);
        baseWay.add(wayBucket.remove());



        for (int i = 0; i < 2; i++) {
            int indexFrom  = baseWay.get(i);
            int max = -1;
            int maxIndex = -1;
            int maxWay = -1;
            int countIndex = 0;
            // hladanie maxima z nepouzitych vrcholov
            for (Integer j : wayBucket) {
                if (max < data[indexFrom][j]) {
                    max = data[indexFrom][j];
                    maxWay = j;
                    maxIndex = countIndex;
                }
                countIndex++;
            }
            //tvorenie cesty
            if (maxIndex != -1) {
                baseWay.add(maxWay);
                wayBucket.remove(maxIndex);
            }
        }

        //pridavanie do zakladnej cesty
        while (!wayBucket.isEmpty()) {
            int minIndex = Integer.MAX_VALUE;
            int minWay = Integer.MAX_VALUE;
            int countIndex = 0;
            // suctove kriterium
            for (Integer i : wayBucket) {
                int actualWay = -1;
                for (Integer j : baseWay) {
                    actualWay += data[i][j];
                }
                if (minWay > actualWay) {
                    minWay = actualWay;
                    minIndex = countIndex;
                }
                countIndex++;
            }
            // pridavanie do zakladnej trasy podla minimalneho zhorsenia
            int newNode = wayBucket.remove(minIndex);
            int minExtension = Integer.MAX_VALUE;
            int minIndex2 = Integer.MAX_VALUE;
            for (int i = 1; i < baseWay.size(); i++) {
                int extension = countExtension(data, baseWay.get(i-1), baseWay.get(i), newNode);
                if (minExtension > extension) {
                    minExtension = extension;
                    minIndex2 = i;
                }
            }
            baseWay.add(minIndex2, newNode);
        }


        baseWay.add(baseWay.get(0));

        return baseWay;
    }


    public static int countExtension(int[][] data, int i, int j, int newNode) {
        int oldDistance = data[i][j];
        int newDistance = data[newNode][i];
        newDistance += data[newNode][j];
        return newDistance - oldDistance;
    }



    /**
     * 1. pociatocnu trasu chapem
     * 2. presuniem vrchol na ine miesto a skontorlujem
     * @param args
     */
    public static void main(String[] args) {
        File fileMaticaBB = new File("Matica_BB_(0515).txt");
        int data[][] = read_file(fileMaticaBB);
        int mSize = data.length;



        ArrayList<Integer> baseWay = getBaseWay(data);

        baseWay.forEach(a -> System.out.print(a + " - "));
        System.out.println();

        int distance = 0;
        for (int i = 1; i < baseWay.size(); i++) {
            distance += data[baseWay.get(i-1)][baseWay.get(i)];
        }
        System.out.println(distance);

    }
}