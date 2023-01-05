package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

    public static int countImprovement(int[][] data, ArrayList<Integer> bestSolution, int cityIndex, int moveToIndex) {
        int improvement = 0;
        // zmazanie uzlu
        improvement -= data[bestSolution.get(cityIndex-1)][bestSolution.get(cityIndex)];
        improvement -= data[bestSolution.get(cityIndex+1)][bestSolution.get(cityIndex)];
        improvement += data[bestSolution.get(cityIndex-1)][bestSolution.get(cityIndex+1)];

        // pridanie uzlo na nove miesto
        //odstranit prepojenie
        improvement -= data[bestSolution.get(moveToIndex-1)][bestSolution.get(moveToIndex)];
        //pridanie uzlu
        improvement += data[bestSolution.get(moveToIndex-1)][bestSolution.get(cityIndex)];
        improvement += data[bestSolution.get(cityIndex)][bestSolution.get(moveToIndex)];
        //improvement += countExtension(data,cityIndex-1, cityIndex, moveToIndex);

        return improvement;
    }

    public static void moveNode(ArrayList<Integer> bestSolution, int cityIndex, int moveToIndex) {
        if (cityIndex == moveToIndex) {
            return;
        }
        if  (cityIndex < moveToIndex) {
            moveToIndex -= 1;
        }
        bestSolution.add(moveToIndex, bestSolution.remove(cityIndex));
    }

    public static void simulatedAnnealing(ArrayList<Integer> bestSolution, int[][] data, int bestDistance) {
        final int START_TEMPERATURE = 10000;
        final int COOLING_RATE = 2;
        final int NUM_ITERATIONS = 50;
        final int U = 40;

        int r = 0;


        double temperature = START_TEMPERATURE;

        Random random = new Random(10);

        // main simulated annealing loop
        while (true) {
            // opakuj po urcity W pocet opakovani
            for (int i = 0; i < NUM_ITERATIONS; i++) {
                r+=1;

                int cityIndex = random.nextInt(bestSolution.size()-2)+1;
                int moveToIndex = random.nextInt(bestSolution.size()-2)+1;

                //pripady ked nenastane zmena
                if (cityIndex == moveToIndex || cityIndex == moveToIndex-1) {
                    continue;
                }

                int newDistance = bestDistance + countImprovement(data, bestSolution, cityIndex, moveToIndex);

                // vypocitaj pravdepodobnost
                double acceptanceProbability = acceptanceProbability(bestDistance, newDistance, temperature);

                // nahodne cislo pre prijatie horiseho riesenia
                double rand = random.nextDouble();

                // ak je nove riesenie lepsie alebo acceptanceProbability je vacsia
                // ako nahodne vygenerovane cislo, akceptuj riesenie
                if (newDistance < bestDistance || rand < acceptanceProbability) { //|| rand < acceptanceProbability
                    r = 0;
                    moveNode(bestSolution, cityIndex, moveToIndex);
                    bestDistance = newDistance;
                }

                // ukonci ked
                // maximálny počet preskúmaných prechodov od prechodu k súčasnému riešeniu
                if (r == U) {
                    return;
                }

            }

            // zniz teplotu
            temperature /= COOLING_RATE;
        }


    }

    private static double acceptanceProbability(int bestDistance, int newDistance, double temperature) {
        // if the new solution is better, accept it
        //if (newDistance < bestDistance) {
        //    return 1.0;
        //}

        // if the new solution is worse, calculate the acceptance probability
        return Math.exp((double)(bestDistance - newDistance) / temperature);
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

        simulatedAnnealing(baseWay, data, distance);

        baseWay.forEach(a -> System.out.print(a + " - "));
        System.out.println();
        distance = 0;
        for (int i = 1; i < baseWay.size(); i++) {
            distance += data[baseWay.get(i-1)][baseWay.get(i)];
        }
        System.out.println(distance);




//        //ohranicenie algoritmu
//        int t = 10000;
//        //maximálny počet preskúmaných prechodov od prechodu k súčasnému riešeniu u=5
//        int u = 40;
//        //maximálny počet preskúmaných prechodov od poslednej zmeny teploty q=5
//        int q = 50;
//
//
//        //Definujte doteraz nájlepšie nájdené riešenie x ako východzie riešenie x =x0, nastavte teplotu t=tmax.
//        int x = distance;
//        //Inicializujte počet preskúmaných prechodov od prechodu k súčasnému riešeniu r=0,
//        int r = 0;
//        //inicializujte počet preskúmaných prechodov od poslednej zmeny teploty w=0.
//        int w = 0;
//
//        //vypocet xCurr
//        int xCurr = 0;
//        //Ak je f(xcurr)≤f(xi) položte i=i+1, xi= xcurr, r=0 a
//        if (xCurr <= x){
//            x = xCurr;
//            r = 0;
//            //zmenit trasu
//        } else {
//
//        }
//
//        //5. Ak r=u, končite, inak pokračujte krokom 2.


    }
}