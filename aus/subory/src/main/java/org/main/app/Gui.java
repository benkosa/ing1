package org.main.app;

import javax.swing.*;

public class Gui extends JFrame {
    private JPanel mainJpanel;
    private JPanel tabPacient;
    private JPanel tabSprava;
    private JButton nacitajButton;
    private JTextField suborDatTextField;
    private JTextField a10TextField;
    private JTextField a10000TextField;
    private JButton pridajPacientaButton;
    private JTextField rodneCisloTextField;
    private JTextField menoTextField;
    private JTextField priezviskoTextField;
    private JTextField poistovnaTextField;
    private JTextField a24101997TextField;
    private JButton zrusPacientaButton;
    private JTextField rodneCisloTextField3;
    private JButton pridajHospitalizaciuButton;
    private JButton zrusHospitalizaciuButton;
    private JTextField rodneCisloTextField2;
    private JTextField a0TextField;
    private JTextField rodneCisloTextField1;
    private JTextField a0TextField1;
    private JTextField a12102022TextField;
    private JTextField a22102022TextField;
    private JTextField diagnozaTextField;
    private JButton generovatPacientovButton;
    private JTextField a100TextField;
    private JButton generovatHospitalizacieButton;
    private JTextField a1000TextField;
    private JTextField a01012020TextField;
    private JTextField a18112022TextField;
    private JPanel tabHosp;
    private JButton vyhladatButton;
    private JTextField rodneCisloTextField4;
    private JTable table1;
    private JTable table2;
    private JButton vyhladatButton1;
    private JTextField rodneCisloTextField5;
    private JTextField a0TextField2;

    public void start() {
        this.setContentPane(mainJpanel);
        this.setTitle("s2 2022");
        this.setSize(1000, 1500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
