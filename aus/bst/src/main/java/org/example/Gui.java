package org.example;

import org.example.Opetations.Data.Hospitalizacia;
import org.example.Opetations.Data.Nemocnica;
import org.example.Opetations.Data.Pacient;
import org.example.Opetations.Data.Poistovna;
import org.example.Opetations.Operations;
import org.example.Shared.Response;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
    private JTextField textField1;
    private JTextField textField2;
    private JTree tree1;
    private JTable table8;

    public JPanel getJPanel () {
        return this.panelMain;
    }

    private SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");

    //https://www.baeldung.com/java-random-dates
    public Date between(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
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
                        nemocnicaTextField.getText(),
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
                Response<ArrayList<Hospitalizacia>> response = operation.Operation_8(
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

                String tableValues[][] = new String[response.data.size()][tableHeader.length];


                for (int i = 0; i < response.data.size(); i++) {
                    final Hospitalizacia hosp = response.data.get(i);
                    final Pacient pacient = hosp.getPacient();
                    tableValues[i] = new String[]{
                            pacient.getMeno(),
                            pacient.getPriezvisko(),
                            pacient.getRodneCislo(),
                            pacient.getDatumNarodeniaString(),
                            pacient.getPoistovna().key,
                            hosp.getDiagnoza(),
                            hosp.getZaciatokHospString(),
                            hosp.getKoniecHospString()
                    };
                }

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
                Random gen = new Random();
                int pocetPacientov = Integer.parseInt(a10000TextField.getText());
                for (int i = 0; i < pocetPacientov; i++) {
                    operation.Operation_6(
                            (gen.nextInt(100000)+899999)+"/"+(gen.nextInt(1000)+8999),
                            i+"_meno",
                            i+"_priezvisko",
                            (gen.nextInt(27)+1)+"-"+
                                    (gen.nextInt(11)+1)+"-"+
                                    (gen.nextInt(120)+1900)
                            ,
                            operation.getData().getPoistovne().getRandomData().key
                    );
                }
            }
        });
        priraditHospitalizacieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date1;
                Date date2;

                try {
                    date1 = formatter.parse(a01012020TextField.getText());
                    date2 = formatter.parse(a21102022TextField.getText());
                } catch (ParseException ee) {
                    return;
                }

                int pocetHospitalizacii = Integer.parseInt(a1000TextField.getText());
                for (int i = 0; i < pocetHospitalizacii; i++) {
                    Pacient pacient = (Pacient) operation.getData().getPacienti().getRandomData();
                    Nemocnica nemocnica = (Nemocnica) operation.getData().getNemocnice().getRandomData();
                    Date datumHospitalizacie = between(date1, date2);
                    operation.Operation_3(pacient.key, nemocnica.key, datumHospitalizacie);
//                    operation.Operation_3(
//                            pacient,
//                            nemocnica,
//                            datumHospitalizacie,
//                            i+"diagnoza"
//                    );
                }
            }
        });
        vypisButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<ArrayList<Pacient>> response = operation.Operation_5(
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

                String tableValues[][] = new String[response.data.size()][tableHeader.length];


                for (int i = 0; i < response.data.size(); i++) {
                    final Pacient pacient = response.data.get(i);
                    tableValues[i] = new String[]{
                            pacient.getMeno(),
                            pacient.getPriezvisko(),
                            pacient.getRodneCislo(),
                            pacient.getDatumNarodeniaString(),
                            pacient.getPoistovna().key,
                    };
                }

                table1 = new JTable(tableValues, tableHeader);
                zaznamy.setText(tableValues.length+"");
                table1.setEnabled(false);
                scrollPane1.setViewportView(table1);
            }
        });
        vyhladajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<ArrayList<BSData<Pacient>>> response = operation.Operation_2(
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

                ArrayList<String[]> tableValues = new ArrayList<>();

                for (int i = 0; i < response.data.size(); i++) {
                    final Pacient pacient = response.data.get(i).key;
                    if (!(pacient.getMeno().equals(menoTextField.getText()) && pacient.getPriezvisko().equals(priezviskoTextField.getText()))) {
                        continue;
                    }

                    final ArrayList<Hospitalizacia> hospitalizacie = pacient.getHospotalizacie(a01_nemocnicaTextField.getText());
                    if (hospitalizacie != null) {
                        for (int j = 0; j < hospitalizacie.size(); j++) {
                            final Hospitalizacia hosp = hospitalizacie.get(j);
                            tableValues.add(new String[]{
                                    pacient.getMeno(),
                                    pacient.getPriezvisko(),
                                    pacient.getRodneCislo(),
                                    pacient.getDatumNarodeniaString(),
                                    pacient.getPoistovna().key,
                                    hosp.getDiagnoza(),
                                    hosp.getZaciatokHospString(),
                                    hosp.getKoniecHospString()
                            });

                        }
                    }
                }

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
                Response<ArrayList<Hospitalizacia>> response = operation.Operation_1(
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

                String tableValues[][] = new String[response.data.size()][tableHeader.length];

                for (int i = 0; i < response.data.size(); i++) {
                    Hospitalizacia hosp = response.data.get(i);
                    tableValues[i] = new String[]{
                            hosp.getPacient().getMeno(),
                            hosp.getPacient().getPriezvisko(),
                            hosp.getPacient().getRodneCislo(),
                            hosp.getPacient().getPoistovna().key,
                            hosp.getDiagnoza(),
                            hosp.getZaciatokHospString(),
                            hosp.getKoniecHospString()
                    };
                }

                table4 = new JTable(tableValues, tableHeader);
                zaznamy.setText(tableValues.length+"");
                table4.setEnabled(false);
                scrollPane4.setViewportView(table4);


            }
        });
        vypisButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<ArrayList<Hospitalizacia>> response = operation.Operation_10(
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

                response.data.sort(Comparator.comparing(o -> o.getPacient().key));

                String tableValues[][] = new String[response.data.size()][tableHeader.length];

                for (int i = 0; i < response.data.size(); i++) {
                    Hospitalizacia hosp = response.data.get(i);
                    tableValues[i] = new String[]{
                            hosp.getPacient().getMeno(),
                            hosp.getPacient().getPriezvisko(),
                            hosp.getPacient().getRodneCislo(),
                            hosp.getPacient().getPoistovna().key,
                            hosp.getDiagnoza(),
                            hosp.getZaciatokHospString(),
                            hosp.getKoniecHospString()
                    };
                }
                table5 = new JTable(tableValues, tableHeader);
                zaznamy.setText(tableValues.length+"");
                table5.setEnabled(false);
                scrollPane5.setViewportView(table5);

            }
        });
        vypisButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<ArrayList<Hospitalizacia>> response = operation.Operation_10(
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

                String tableValues[][] = new String[response.data.size()][tableHeader.length];

                for (int i = 0; i < response.data.size(); i++) {
                    Hospitalizacia hosp = response.data.get(i);
                    tableValues[i] = new String[]{
                            hosp.getPacient().getMeno(),
                            hosp.getPacient().getPriezvisko(),
                            hosp.getPacient().getRodneCislo(),
                            hosp.getPacient().getPoistovna().key,
                            hosp.getDiagnoza(),
                            hosp.getZaciatokHospString(),
                            hosp.getKoniecHospString()
                    };
                }

                zaznamy.setText(tableValues.length+"");
                table6 = new JTable(tableValues, tableHeader);
                table6.setEnabled(false);
                scrollPane6.setViewportView(table6);

            }
        });
        vypisButton5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Response<ArrayList<Nemocnica>> response = operation.Operation_13();
                errorSprava.setText(response.message);

                if (response.code != 0) {
                    return;
                }

                String tableHeader[] = {
                        "Nazov nemocnice"
                };

                String tableValues[][] = new String[response.data.size()][tableHeader.length];

                for (int i = 0; i < response.data.size(); i++) {
                    Nemocnica nemocnica = response.data.get(i);
                    tableValues[i] = new String[]{
                            nemocnica.key,
                    };
                }

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

            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
