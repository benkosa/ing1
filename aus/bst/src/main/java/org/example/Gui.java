package org.example;

import org.example.Opetations.Operations;
import org.example.Shared.Response;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui extends JFrame {
    private JButton setTimeButton;
    private JPanel panelMain;
    private JTextField inputTime;
    private JTabbedPane tabbedPane1;
    private JPanel tabBase;
    private JPanel tab1;
    private JButton baseSetTime;
    private JTextField baseTimeInput;
    private JLabel tab1Label;
    private JLabel tab2InfoLabel;
    private JPanel tab2;
    private JPanel tab5;
    private JPanel tab7;
    private JPanel tab8;
    private JPanel tab9;
    private JPanel tab10;
    private JPanel tab13;
    private JButton addHospital;
    private JTextField addHospitalInput;
    private JButton pridatPoistovnuButton;
    private JButton zacatHospitalizaciuButton;
    private JButton ukoncitHospitalizacuButton;
    private JTextField nazov_poistovneTextField;
    private JTextField nemocnicaTextField;
    private JTextField rodneCisloTextField;
    private JButton pridatPacientaButton;
    private JTextField narodenie_ddHhYyyyTextField;
    private JTextField menoTextField1;
    private JTextField priezviskoTextField1;
    private JTextField rodneCisloTextField3;
    private JTextField poistovnaTextField;
    private JTextField rodneCisloTextField1;
    private JTextField nemocnicaTextField1;
    private JLabel errorSprava;
    private JButton vypisButton;
    private JTextField nazovNemocniceTextField;
    private JTable table8;

    public JPanel getJPanel () {
        return this.panelMain;
    }

    public void start() {
        this.setContentPane(this.getJPanel());
        this.setTitle("hello");
        this.setSize(1000, 1500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    Operations operation = new Operations();

    public Gui() {

        baseSetTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String> response = operation.setActualTime(baseTimeInput.getText());
                errorSprava.setText(response.message);
            }
        });
        addHospital.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String> response = operation.Operation_12(addHospitalInput.getText());
                errorSprava.setText(response.message);
            }
        });
        pridatPoistovnuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String> response = operation.Operation_addPoistovna(nazov_poistovneTextField.getText());
                errorSprava.setText(response.message);
            }
        });
        pridatPacientaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String> response = operation.Operation_6(
                        rodneCisloTextField3.getText(),
                        menoTextField1.getText(),
                        priezviskoTextField1.getText(),
                        narodenie_ddHhYyyyTextField.getText(),
                        poistovnaTextField.getText()
                );
                errorSprava.setText(response.message);
            }
        });
        zacatHospitalizaciuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String> response = operation.Operation_3(
                        rodneCisloTextField.getText(),
                        nemocnicaTextField.getText()
                );
                errorSprava.setText(response.message);
            }
        });
        ukoncitHospitalizacuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String> response = operation.Operation_4(
                        rodneCisloTextField1.getText(),
                        nemocnicaTextField1.getText()
                );
                errorSprava.setText(response.message);
            }
        });
        vypisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
