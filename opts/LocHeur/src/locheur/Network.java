/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package locheur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.StringTokenizer;

/**
 *
 * @author Kohani
 */
public class Network {
    //data pre dopravnu siet
    int numberNodes;     // pocet vrcholov v sieti
    int numberArcs;      // pocet hran v sieti
    int NodeID[];       // ID vrchola
    String NodeName[];  // Nazov vrchola
    int ArcID[];        // ID hrany
    int ArcStartNode[]; // ID zaciatocneho vrchola hrany
    int ArcEndNode[];   // ID koncoveho vrchola hrany
    int ArcLength[];    // dlzka hrany
    int dij[][];        // matica vzdialenosti    
    
    
    public void calculate_distance_matrix (){
       // tu pridajte algoritmus na spocitanie matice vzdialenosti
   }
   
   public void read_node_file(File file) {
        try {
    
            BufferedReader bfr = new BufferedReader(new FileReader(file));            
            String line = "";
            int line_index = 0;
            while ((line = bfr.readLine()) != null) {
                if (line_index == 0) {
                    numberNodes = Integer.parseInt(line);
                    NodeID = new int[numberNodes];     
                    NodeName = new String[numberNodes];   
                    dij = new int [numberNodes][numberNodes];
                }    
                if (line_index >= 1)     {
                    StringTokenizer st = new StringTokenizer(line, " ");
                    while (st.hasMoreTokens()) {
                        NodeID [line_index-1] = Integer.parseInt(st.nextToken());
                        NodeName [line_index-1] = st.nextToken();
                        System.out.println("Node ["+ (line_index-1) + "]:" +NodeID[line_index-1]+" "+ NodeName[line_index-1]);
                    }
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

   public void read_arc_file(File file) {
        try {
    
            BufferedReader bfr = new BufferedReader(new FileReader(file));            
            String line = "";
            int line_index = 0;
            while ((line = bfr.readLine()) != null) {
                if (line_index == 0) {
                    numberArcs = Integer.parseInt(line);
                    ArcID = new int[numberArcs];     
                    ArcStartNode = new int[numberArcs];   
                    ArcEndNode = new int[numberArcs];     
                    ArcLength = new int[numberArcs];
                }    
                if (line_index >= 1)     {
                    StringTokenizer st = new StringTokenizer(line, " ");
                    while (st.hasMoreTokens()) {
                        ArcID [line_index-1] = Integer.parseInt(st.nextToken());
                        ArcStartNode [line_index-1] = Integer.parseInt(st.nextToken());
                        ArcEndNode [line_index-1] = Integer.parseInt(st.nextToken());
                        ArcLength [line_index-1] = Integer.parseInt(st.nextToken());
                        System.out.println("Arc["+ (line_index-1) + "]: ID " +ArcID[line_index-1]+": "+ ArcStartNode[line_index-1]+" -> "+ArcEndNode[line_index-1]+" d:"+ArcLength[line_index-1]);
                    }
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
