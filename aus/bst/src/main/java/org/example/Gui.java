package org.example;

import org.example.Opetations.Data.Hospitalizacia;
import org.example.Opetations.Data.Nemocnica;
import org.example.Opetations.Data.Pacient;
import org.example.Opetations.Operations;
import org.example.Shared.Response;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

        System.out.println(randomMillisSinceEpoch);

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
                            gen.nextInt(999999)+"/"+gen.nextInt(9999),
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
                    operation.Operation_3(
                            pacient,
                            nemocnica,
                            datumHospitalizacie,
                            i+"diagnoza"
                    );
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
