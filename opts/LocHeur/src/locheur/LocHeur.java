/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package locheur;

import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author Kohani
 */
public class LocHeur {
    /**
     * @param args the command line arguments
     */
public static void main(String[] args) {
    LocationProblem LocProb;
    File file_c = new File("cij.txt");
    File file_f = new File("fi.txt");
    
    LocProb = new LocationProblem(); 
    
    LocProb.read_cij_file(file_c);     
    
    LocProb.read_fi_file(file_f);     
    LocProb.solve();
    //LocProb.solveCustom();
    LocProb.solution_output();
}
    
}
