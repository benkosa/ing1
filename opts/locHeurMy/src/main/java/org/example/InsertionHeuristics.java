package org.example;

import java.util.ArrayList;

public class InsertionHeuristics {



    int[] f = { 55, 70, 58, 73, 62 };

    int[][] c = {
            { 50, 10, 30, 40, 10, 50, 20, 30, 30, 45 },
            { 30, 20, 20, 40, 20, 55, 30, 30, 15, 30 },
            { 20, 20, 30, 30, 30, 40, 50, 30, 20, 20 },
            { 40, 15, 35, 30, 20, 40, 40, 30, 25, 20 },
            { 75, 20, 25, 50, 25, 50, 35, 30, 20, 30 }
    };



    public void countHeuristic() {

        int bestSolution = Integer.MAX_VALUE;
        final ArrayList<Integer> buildCities = new ArrayList<>();

        for (int i = 0; i < c.length; i++) {
            ArrayList<Integer> citiesBucket = new ArrayList<>();
            for (int k = i; k < c.length; k++) {
                citiesBucket.add(k);
                int newSolution = getBuildPrice(citiesBucket);
                for (int j = 0; j < c[0].length; j++) {
                    newSolution += getMinRow(citiesBucket, j);
                }
                if (newSolution < bestSolution) {
                    bestSolution = newSolution;
                    buildCities.clear();
                    citiesBucket.forEach(city -> buildCities.add(city));
                } else {
                    citiesBucket.remove(citiesBucket.get(citiesBucket.size()-1));
                }
            }
        }

        System.out.println(bestSolution);
        if (buildCities != null) {
            buildCities.forEach(a -> System.out.print(a + ", "));
            System.out.println();
        }
    }

    private int getMinRow(ArrayList<Integer> citiesBucket, int i) {
        int min = Integer.MAX_VALUE;
        for (int j: citiesBucket) {
            if (c[j][i] < min) {
                min = c[j][i];
            }
        }
        return min;
    }

    private int getBuildPrice(ArrayList<Integer> citiesBucket) {
        int buildPrice = 0;
        for (int i: citiesBucket) {
            buildPrice += f[i];
        }
        return buildPrice;
    }
}
