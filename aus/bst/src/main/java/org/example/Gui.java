package org.example;

import org.example.Opetations.Operations;
import org.example.Shared.Response;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

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
    private JScrollPane scrollPane;
    private JTable table;
    private JTextField nazovNemocniceTextField;
    private JPanel tabGenerator;
    private JButton generovatNemocniceButton;
    private JTextField a100TextField;
    private JButton generovatPoistovneButton;
    private JButton generovatPacientovButton;
    private JButton priraditHospitalizacieButton;
    private JTextField a20TextField;
    private JTextField a10000TextField;
    private JTextField a1000TextField;
    private JTextField a21102022TextField;
    private JTextField a01012020TextField;
    private JButton vypisButton1;
    private JTextField a1_nemocnicaTextField;
    private JTextField a01052021TextField;
    private JTextField a01062021TextField;
    private JTable table1;
    private JScrollPane scrollPane1;
    private JButton vyhladajButton;
    private JTextField a01_nemocnicaTextField;
    private JTextField menoTextField;
    private JTextField priezviskoTextField;
    private JTable table2;
    private JScrollPane scrollPane2;
    private JButton vypisButton2;
    private JTable table3;
    private JTextField a1TextField;
    private JScrollPane scrollPane3;
    private JButton vyhladajButton1;
    private JTextField rodneCisloTextField2;
    private JTextField a1_nemocnicaTextField1;
    private JTable table4;
    private JScrollPane scrollPane4;
    private JButton vypisButton3;
    private JTextField a1_poistovnaTextField;
    private JTextField a1_nemocnicaTextField2;
    private JTable table5;
    private JScrollPane scrollPane5;
    private JTable table6;
    private JButton vypisButton4;
    private JTextField a1_poistovnaTextField1;
    private JTextField a1_nemocnicaTextField3;
    private JScrollPane scrollPane6;
    private JButton vypisButton5;
    private JTable table7;
    private JScrollPane scrollPane7;
    private JLabel zaznamy;
    private JPanel tabNastavenia;
    private JButton vyvazitButton;
    private JButton migrovatButton;
    private JTextField a1_nemocnicaTextField4;
    private JTextField a0_nemocnciaTextField;
    private JButton ulozitButton;
    private JButton nacitatButton;
    private JTextField nazov_suboruTextField;
    private JTextField nazov_suboruTextField1;
    private JButton zmazatVsetkoButton;
    private JTextField diagnozaTextField;
    private JPanel tab15;
    private JButton vypisButton6;
    private JTextField a1_poistovnaTextField2;
    private JTextField a1_nemocnicaTextField5;
    private JTree tree1;
    private JTable table8;
    private JScrollPane scrollPane8;

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
                        poistovnaTextField.getText(),
                        false
                );
                errorSprava.setText(response.message);
            }
        });
        zacatHospitalizaciuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String> response = operation.Operation_3(
                        rodneCisloTextField.getText(),
                        nemocnicaTextField.getText(),
                        diagnozaTextField.getText(),
                        null,
                        null
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
        // https://stackoverflow.com/questions/22593867/why-is-my-table-not-visible-when-its-in-a-jscrollpane
        vypisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String[][]> response = operation.Operation_8(
                        nazovNemocniceTextField.getText()
                );
                errorSprava.setText(response.message);

                if (response.code != 0) {
                    return;
                }

                String tableHeader[] = {
                        "Meno",
                        "Priezvisko",
                        "Rodne Cislo",
                        "Datum narodenia",
                        "Poistovna",
                        "Diagnoza",
                        "Zaciatok hospitalizacie",
                        "Koniec hospitalizacie"
                };

                String tableValues[][] = response.data;

                table = new JTable(tableValues, tableHeader);
                zaznamy.setText(tableValues.length+"");
                table.setEnabled(false);
                scrollPane.setViewportView(table);

                }

        });
        generovatNemocniceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pocetNemocnic = Integer.parseInt(a100TextField.getText());
                for (int i = 0; i < pocetNemocnic; i++) {
                    operation.Operation_12(i+"_nemocnica");
                }
            }
        });
        generovatPoistovneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pocetPoistovni = Integer.parseInt(a20TextField.getText());
                for (int i = 0; i < pocetPoistovni; i++) {
                    operation.Operation_addPoistovna(i+"_poistovna");
                }
            }
        });
        generovatPacientovButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                operation.generujPacientov(a10000TextField.getText());
            }
        });
        priraditHospitalizacieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                operation.generujHospitalizacie(
                        a1000TextField.getText(),
                        a01012020TextField.getText(),
                        a21102022TextField.getText()
                );
            }
        });
        vypisButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String[][]> response = operation.Operation_5(
                        a1_nemocnicaTextField.getText(),
                        a01052021TextField.getText(),
                        a01062021TextField.getText()
                );
                errorSprava.setText(response.message);

                if (response.code != 0) {
                    return;
                }


                String tableHeader[] = {
                        "Meno",
                        "Priezvisko",
                        "Rodne Cislo",
                        "Datum narodenia",
                        "poistovna"
                };

                String tableValues[][] = response.data;

                table1 = new JTable(tableValues, tableHeader);
                zaznamy.setText(tableValues.length+"");
                table1.setEnabled(false);
                scrollPane1.setViewportView(table1);
            }
        });
        vyhladajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<ArrayList<String[]>> response = operation.Operation_2(
                        a01_nemocnicaTextField.getText(),
                        menoTextField.getText(),
                        priezviskoTextField.getText()
                );
                errorSprava.setText(response.message);

                if (response.code != 0) {
                    return;
                }


                String tableHeader[] = {
                        "Meno",
                        "Priezvisko",
                        "Rodne Cislo",
                        "Datum narodenia",
                        "Poistovna",
                        "Diagnoza",
                        "Zaciatok hospitalizacie",
                        "Koniec hospitalizacie"
                };

                ArrayList<String[]> tableValues = response.data;

                String tableValuesArr[][] = new String[tableValues.size()][tableHeader.length];
                tableValuesArr = tableValues.toArray(tableValuesArr);
                table2 = new JTable(tableValuesArr, tableHeader);
                zaznamy.setText(tableValuesArr.length+"");
                table2.setEnabled(false);
                scrollPane2.setViewportView(table2);
            }
        });
        vypisButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<ArrayList<String[]>> response = operation.Operation_7(
                        a1TextField.getText() //a1TextField
                );
                response = operation.Operation_7(
                        a1TextField.getText() //a1TextField
                );
                errorSprava.setText(response.message);
                if (response.code != 0) {
                    return;
                }

                ArrayList<String[]> tableValues = response.data;

                String tableHeader[] = {
                        "nemocnica",
                        "poistovna",
                        "celkovy pocet dni",
                        "den",
                        "meno",
                        "priezvisko",
                        "rodne cislo",
                        "diagnoza"
                };
                String tableValuesArr[][] = new String[tableValues.size()][tableHeader.length];
                tableValuesArr = tableValues.toArray(tableValuesArr);
                table3 = new JTable(tableValuesArr, tableHeader);
                zaznamy.setText(tableValuesArr.length+"");
                table3.setEnabled(false);
                scrollPane3.setViewportView(table3);
            }
        });
        vyhladajButton1.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String[][]> response = operation.Operation_1(
                        rodneCisloTextField2.getText(),
                        a1_nemocnicaTextField1.getText()

                );
                errorSprava.setText(response.message);

                if (response.code != 0) {
                    return;
                }
                if (response.data == null) {
                    errorSprava.setText("ziadne zaznamy");
                    return;
                }

                String tableHeader[] = {
                        "Meno",
                        "Priezvisko",
                        "Rodne Cislo",
                        "Poistovna",
                        "Diagnoza",
                        "Zaciatok hospitalizacie",
                        "Koniec hospitalizacie"
                };

                String tableValues[][] = response.data;

                table4 = new JTable(tableValues, tableHeader);
                zaznamy.setText(tableValues.length+"");
                table4.setEnabled(false);
                scrollPane4.setViewportView(table4);


            }
        });
        vypisButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String[][]> response = operation.Operation_10(
                        a1_nemocnicaTextField2.getText(),
                        a1_poistovnaTextField.getText()

                );
                errorSprava.setText(response.message);

                if (response.code != 0) {
                    return;
                }

                String tableHeader[] = {
                        "Meno",
                        "Priezvisko",
                        "Rodne Cislo",
                        "Poistovna",
                        "Diagnoza",
                        "Zaciatok hospitalizacie",
                        "Koniec hospitalizacie"
                };


                String tableValues[][] = response.data;
                table5 = new JTable(tableValues, tableHeader);
                zaznamy.setText(tableValues.length+"");
                table5.setEnabled(false);
                scrollPane5.setViewportView(table5);

            }
        });
        vypisButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String[][]> response = operation.Operation_9(
                        a1_nemocnicaTextField3.getText(),
                        a1_poistovnaTextField1.getText()

                );
                errorSprava.setText(response.message);

                if (response.code != 0) {
                    return;
                }

                String tableHeader[] = {
                        "Meno",
                        "Priezvisko",
                        "Rodne Cislo",
                        "Poistovna",
                        "Diagnoza",
                        "Zaciatok hospitalizacie",
                        "Koniec hospitalizacie"
                };

                String tableValues[][] = response.data;

                zaznamy.setText(tableValues.length+"");
                table6 = new JTable(tableValues, tableHeader);
                table6.setEnabled(false);
                scrollPane6.setViewportView(table6);

            }
        });
        vypisButton5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String[][]> response = operation.Operation_13();
                errorSprava.setText(response.message);

                if (response.code != 0) {
                    return;
                }

                String tableHeader[] = {
                        "Nazov nemocnice"
                };

                String tableValues[][] = response.data;

                zaznamy.setText(tableValues.length+"");
                table7 = new JTable(tableValues, tableHeader);
                table7.setEnabled(false);
                scrollPane7.setViewportView(table7);


            }
        });
        vyvazitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                operation.Operation_11();
            }
        });
        migrovatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response response = operation.Operation_14(
                        a0_nemocnciaTextField.getText(),
                        a1_nemocnicaTextField4.getText()
                );
                errorSprava.setText(response.message);
            }
        });
        ulozitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //nazov_suboruTextField
                operation.Operation_saveToFile(nazov_suboruTextField.getText());
            }
        });
        nacitatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //nazov_suboruTextField1
                operation.Operation_loadFromFile(nazov_suboruTextField1.getText());
            }
        });
        zmazatVsetkoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                operation.drop();
            }
        });
        vypisButton6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<String[][]> response = operation.Operation_15(
                        a1_nemocnicaTextField5.getText(),
                        a1_poistovnaTextField2.getText()

                );
                errorSprava.setText(response.message);

                if (response.code != 0) {
                    return;
                }

                String tableHeader[] = {
                        "Meno",
                        "Priezvisko",
                        "Rodne Cislo",
                        "Poistovna",
                        "Diagnoza",
                        "Zaciatok hospitalizacie",
                        "Koniec hospitalizacie"
                };


                String tableValues[][] = response.data;
                table8 = new JTable(tableValues, tableHeader);
                zaznamy.setText(tableValues.length+"");
                table8.setEnabled(false);
                scrollPane8.setViewportView(table8);

            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
