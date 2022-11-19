package org.main.app;

import org.main.shared.DateFormat;
import org.main.shared.Response;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

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
    private JTextField rodnecisloTextField;
    private JButton pridajHospitalizaciuButton;
    private JButton zrusHospitalizaciuButton;
    private JTextField rodnecisloTextField1;
    private JTextField a0TextField;
    private JTextField rodneCisloTextField1;
    private JTextField a0TextField1;
    private JTextField a12102022TextField;
    private JTextField a22102022TextField;
    private JTextField diagnozaTextField;
    private JButton generovatPacientovButton;
    private JTextField a100TextField;
    private JTextField a1000TextField;
    private JTextField a01012020TextField;
    private JTextField a18112022TextField;
    private JPanel tabHosp;
    private JButton vyhladatButton;
    private JTextField rodneCisloTextField4;
    private JTable table1;
    private JTable table2;
    private JButton vyhladatButton1;
    private JTextField rodnecisloTextField2;
    private JTextField a0TextField2;
    private JLabel successLabel;
    private JLabel errorLabel;
    private JScrollPane scrollPane1;
    private JButton vymazHospitalizaciuButton;
    private JTextField rodnecisloTextField3;
    private JTextField a0TextField3;
    private JButton nastavitCasButton;
    private JTextField a19112022TextField;
    private JScrollPane scrollPane2;
    private JTable table3;
    private JScrollPane scrollPane3;

    private void displayResponse(Response response) {
        if (response.code == 0) {
            errorLabel.setText("");
            successLabel.setText(response.message);
        } else {
            successLabel.setText("");
            errorLabel.setText(response.message);
        }
    }

    public Gui() {


        Operations operations = new Operations();
        DateFormat df = new DateFormat();

        a19112022TextField.setText(operations.getDate());
        nacitajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response response = operations.opStart(
                        suborDatTextField.getText(),
                        a10TextField.getText(),
                        a10000TextField.getText()
                );
                displayResponse(response);

            }
        });
        pridajPacientaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date datumNarodenia = df.stringToDate( a24101997TextField.getText());
                if (datumNarodenia == null) {
                    displayResponse(new Response(3, "zly format datumu", null));
                    return;
                }

                Response response = operations.op5(
                        menoTextField.getText(),
                        priezviskoTextField.getText(),
                        rodneCisloTextField.getText(),
                        poistovnaTextField.getText().isEmpty() ? 0 : Integer.parseInt(poistovnaTextField.getText()),
                        datumNarodenia
                );
                displayResponse(response);
            }
        });
        zrusPacientaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response response = operations.op7(
                        rodneCisloTextField1.getText()
                );
                displayResponse(response);
            }
        });
        vyhladatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String[][]> response = operations.op1(
                        rodneCisloTextField4.getText()
                );
                displayResponse(response);
                if (response.code != 0) {
                    return;
                }
                String tableHeader[] = {
                        "Meno",
                        "Priezvisko",
                        "Rodne Cislo",
                        "datum narodenia",
                        "Poistovna",
                        "id hospitalizacie",
                        "Zaciatok hospitalizacie",
                        "Koniec hospitalizacie",
                        "Diagnoza",
                };


                String tableValues[][] = response.data;
                table1 = new JTable(tableValues, tableHeader);
                //zaznamy.setText(tableValues.length+"");

                scrollPane1.setViewportView(table1);
            }
        });
        vyhladatButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String[][]> response = operations.op2(
                        rodnecisloTextField2.getText(),
                        Integer.parseInt(a0TextField2.getText())
                );
                displayResponse(response);
                if (response.code != 0) {
                    return;
                }
                String tableHeader[] = {
                        "Meno",
                        "Priezvisko",
                        "Rodne Cislo",
                        "datum narodenia",
                        "Poistovna",
                        "id hospitalizacie",
                        "Zaciatok hospitalizacie",
                        "Koniec hospitalizacie",
                        "Diagnoza",
                };


                String tableValues[][] = response.data;
                table2 = new JTable(tableValues, tableHeader);
                //zaznamy.setText(tableValues.length+"");
                scrollPane2.setViewportView(table2);
            }
        });
        pridajHospitalizaciuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date datumZacaitku = df.stringToDate( a12102022TextField.getText());
                if (datumZacaitku == null) {
                    displayResponse(new Response(3, "zly format datumu", null));
                    return;
                }

                Response<String[][]> response = operations.op3(
                        rodnecisloTextField.getText(),
                        Integer.parseInt(a0TextField1.getText()),
                        datumZacaitku,
                        null,
                        diagnozaTextField.getText()
                );
                displayResponse(response);
            }
        });
        zrusHospitalizaciuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String[][]> response = operations.op4(
                        rodnecisloTextField1.getText(),
                        Integer.parseInt(a0TextField.getText())
                );
                displayResponse(response);
            }
        });
        vymazHospitalizaciuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String[][]> response = operations.op6(
                        rodnecisloTextField3.getText(),
                        Integer.parseInt(a0TextField3.getText())
                );
                displayResponse(response);
            }
        });
        nastavitCasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String[][]> response = operations.setDate(
                        a19112022TextField.getText()
                );
                displayResponse(response);
                a19112022TextField.setText(operations.getDate());
            }
        });
        generovatPacientovButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String[][]> response = operations.generate(
                        Integer.parseInt(a100TextField.getText()),
                        Integer.parseInt(a1000TextField.getText()),
                        df.stringToDate(a01012020TextField.getText()),
                        df.stringToDate(a18112022TextField.getText())
                );
                displayResponse(response);

                String tableHeader[] = {
                        "Meno",
                        "Priezvisko",
                        "Rodne Cislo",
                        "datum narodenia",
                        "Poistovna"
                };


                String tableValues[][] = response.data;
                table3 = new JTable(tableValues, tableHeader);
                //zaznamy.setText(tableValues.length+"");
                scrollPane3.setViewportView(table3);
            }
        });
    }

    public void start() {
        this.setContentPane(mainJpanel);
        this.setTitle("s2 2022");
        this.setSize(1000, 1500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
