/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the tem
plate in the editor.
 */
package locheur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Kohani
 */
public class LocationProblem {
    //data pre lokacnu ulohu
    int M;              // pocet skladov
    int N;              // pocet zakaznikov
    int cij[][];        // matica priradovacich nakladov
    int fi[];           // vektor fixnych nakladov
    int f;              // hodnota ucelovej funkcie
    int[] x;            // umiestnene strediska
    
  public int f() {
       return f;
   }    
    
   public int getM() {
       return M;
   }

   public int getN() {
       return N;
   }
   
   public int[][] getcij()  {
        return cij;
   }
   
   public int[] getfi()  {
        return fi;
   }

      public void solve() {
          if (cij.length != M) {
              System.out.println("chybny pocet skladov");
              return;
          }
          if (cij[0].length != N) {
              System.out.println("chybny pocet zakaznikov");
              return;
          }
          if (fi.length != M) {
              System.out.println("chybny pocet nakladov");
              return;
          }
      // tu pridajte heuristiku
          int bestSolution = Integer.MAX_VALUE;
          final ArrayList<Integer> buildStorages = new ArrayList<>();

          for (int k = 0; k < cij.length; k++) {
              ArrayList<Integer> storageBucket = new ArrayList<>();
              for (int i = k; i < cij.length; i++) {
                  storageBucket.add(i);
                  int newSolution = getBuildPrice(storageBucket);
                  for (int j = 0; j < cij[0].length; j++) {
                      newSolution += getMinRow(storageBucket, j);
                  }
                  if (newSolution < bestSolution) {
                      bestSolution = newSolution;
                      buildStorages.clear();
                      storageBucket.forEach(city -> buildStorages.add(city));
                  } else {
                      storageBucket.remove(storageBucket.get(storageBucket.size()-1));
                  }
              }
          }

          f = bestSolution;
          for (int i = 0; i < buildStorages.size(); i++) {
              x[i] = buildStorages.get(i);
          }
        }

    public void solveCustom() {
        // tu pridajte heuristiku
        int bestSolution = Integer.MAX_VALUE;
        final ArrayList<Integer> buildStorages = new ArrayList<>();

//        for (int k = 0; k < cij.length; k++) {
//            for (int i = k; i < cij.length; i++) {
            ArrayList<Integer> storageBucket = new ArrayList<>();
                storageBucket.add(0);
                storageBucket.add(2);
                int newSolution = getBuildPrice(storageBucket);
                for (int j = 0; j < cij[0].length; j++) {
                    newSolution += getMinRow(storageBucket, j);
                }
                if (newSolution < bestSolution) {
                    bestSolution = newSolution;
                    buildStorages.clear();
                    storageBucket.forEach(city -> buildStorages.add(city));
                } else {
                    storageBucket.remove(storageBucket.get(storageBucket.size()-1));
                }
        //    }
        //}

        f = bestSolution;
        for (int i = 0; i < buildStorages.size(); i++) {
            x[i] = buildStorages.get(i);
        }
    }

    private int getMinRow(ArrayList<Integer> citiesBucket, int i) {
        int min = Integer.MAX_VALUE;
        for (int j: citiesBucket) {
            if (cij[j][i] < min) {
                min = cij[j][i];
            }
        }
        return min;
    }

    private int getBuildPrice(ArrayList<Integer> citiesBucket) {
        int buildPrice = 0;
        for (int i: citiesBucket) {
            buildPrice += fi[i];
        }
        return buildPrice;
    }
      
   public void solution_output() { 
        String text = "x = (" + x[0];
        for(int i=1; i< M;i++) {
            text = text + "," + x[i];
        }
        text = text + ") HUF = " + f;        
        System.out.println(text);
    }
   
   public void calculate_objective_value (){
       // tu pridajte metodu na vypocet hodnoty ucelovej funkcie
   }
   
   public void calculate_cij_matrix(){
       // metoda na vypocet matice cij z matice vzdialenosti  
   }
            
    
    public void read_cij_file(File file) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(file));            
            String line = "";
            int line_index = 0;
            int row = 0, col = 0;
            while ((line = bfr.readLine()) != null) {
                if (line_index == 0) {
                    M = Integer.parseInt(line);
                }    
                if (line_index == 1) {
                    N = Integer.parseInt(line);
                    cij = new int [M][N];
                    x   = new int[M];
                    for( int i=0; i < M; i++) {
                        x[i]=0;
                    }    
                }
                if (line_index > 1)     {
                    StringTokenizer st = new StringTokenizer(line, " ");
                    col = 0;
                    while (st.hasMoreTokens()) {
                        cij[row][col] =   Integer.parseInt(st.nextToken());
                        System.out.println("cij["+ row + "]["+ col + "]:" +cij[row][col] );
                        col++;
                    }
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
    }
    
    public void read_fi_file(File file) {
        try {
    
            BufferedReader bfr = new BufferedReader(new FileReader(file));            
            String line = "";
            int line_index = 0;
            int row = 0, col = 0;
            while ((line = bfr.readLine()) != null) {
                if (line_index == 0) {
                    M  = Integer.parseInt(line);
                    fi = new int[M];                    
                }  else     {
                    fi[row] =   Integer.parseInt(line);
                    System.out.println("fi["+ row + "]:" +fi[row]);
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
    }
    
}