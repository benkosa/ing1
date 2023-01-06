package org.tsp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Main {

    public static int[][] data;

    public static int[][] read_file(File file) {
        int M;
        int[][] data = new int[0][];

        try {

            BufferedReader bfr = new BufferedReader(new FileReader(file));
            String line;
            int line_index = 0;
            int row = 0;
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
     */
    public static ArrayList<Integer> getBaseWay() {
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
                int extension = countExtension(baseWay.get(i-1), baseWay.get(i), newNode);
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

    /**
     * count way extension before node insert
     */
    public static int countExtension(int i, int j, int newNode) {
        int oldDistance = data[i][j];
        int newDistance = data[newNode][i];
        newDistance += data[newNode][j];
        return newDistance - oldDistance;
    }

    /**
     * count improvement before change of node position
     */
    public static int countImprovement(ArrayList<Integer> bestSolution, int cityIndex, int moveToIndex) {
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

        return improvement;
    }

    /**
     * move node in solution
     */
    public static void moveNode(ArrayList<Integer> solution, int cityIndex, int moveToIndex) {
        if (cityIndex == moveToIndex) {
            return;
        }
        if  (cityIndex < moveToIndex) {
            moveToIndex -= 1;
        }
        solution.add(moveToIndex, solution.remove(cityIndex));
    }

    /**
     * simulatedAnnealing algorithm
     * @param bestSolution solution
     */
    public static int simulatedAnnealing(
            ArrayList<Integer> bestSolution,
            final int START_TEMPERATURE,
            final int COOLING_RATE,
            final int NUM_ITERATIONS,
            final int MAX_PASSES,
            final int SEED) {

        int countPasses = 0;

        ArrayList<Integer> solution = new ArrayList<>(bestSolution);
        int bestDistance = countDistance(bestSolution);
        int solutionDistance = bestDistance;


        double temperature = START_TEMPERATURE;

        Random random = new Random(SEED);

        // main simulated annealing loop
        while (true) {
            // opakuj po urcity W pocet opakovani
            for (int i = 0; i < NUM_ITERATIONS; i++) {
                countPasses += 1;

                int cityIndex = random.nextInt(bestSolution.size()-2)+1;
                int moveToIndex = random.nextInt(bestSolution.size()-2)+1;

                //pripady ked nenastane zmena
                if (cityIndex == moveToIndex || cityIndex == moveToIndex-1) {
                    continue;
                }

                int newDistance = solutionDistance + countImprovement(bestSolution, cityIndex, moveToIndex);

                // vypocitaj pravdepodobnost
                double acceptanceProbability = acceptanceProbability(bestDistance, newDistance, temperature);

                // nahodne cislo pre prijatie horiseho riesenia
                double rand = random.nextDouble();

                // ak je nove riesenie lepsie alebo acceptanceProbability je vacsia
                // ako nahodne vygenerovane cislo, akceptuj riesenie
                if (newDistance < bestDistance || rand < acceptanceProbability) { //|| rand < acceptanceProbability
                    countPasses = 0;
                    moveNode(solution, cityIndex, moveToIndex);
                    solutionDistance = newDistance;
                }

                // prepis najlepsie riesenie
                if (newDistance < bestDistance) {
                    moveNode(bestSolution, cityIndex, moveToIndex);
                    bestDistance = newDistance;
                }

                // ukonci ked
                // maximálny počet preskúmaných prechodov od prechodu k súčasnému riešeniu
                if (countPasses >= MAX_PASSES) {
                    return bestDistance;
                }

            }

            // zniz teplotu
            temperature /= COOLING_RATE;
        }

    }

    /**
     * count acceptance probability
     */
    private static double acceptanceProbability(int bestDistance, int newDistance, double temperature) {
        return Math.exp((double)(bestDistance - newDistance) / temperature);
    }

    /**
     * count whole solution distance
     */
    private static  int countDistance(ArrayList<Integer> way) {
        int distance = 0;
        for (int i = 1; i < way.size(); i++) {
            distance += data[way.get(i-1)][way.get(i)];
        }
        return distance;
    }

    public static void main(String[] args) {
        final File fileMaticaBB = new File("Matica_BB_(0515).txt");
        data = read_file(fileMaticaBB);

        final ArrayList<Integer> baseWay = getBaseWay();

        baseWay.forEach(a -> System.out.print(a + ";"));
        System.out.println();
        int baseDistance = countDistance(baseWay);
        System.out.println(baseDistance);


        final int REPLICATIONS = 1000000;
        for (int i = 0; i < REPLICATIONS; i++) {
            if (simulatedAnnealing(
                    baseWay,
                    10000,
                    2,
                    40,
                    50,
                    i) != baseDistance) {
                baseDistance = countDistance(baseWay);
                System.out.println("seed: " + i + "; distance: " +  baseDistance);
            }
        }
        baseWay.forEach(a -> System.out.print(a + ";"));

    }
}