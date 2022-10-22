package org.example.Opetations;

import org.example.BSData;
import org.example.Opetations.Data.*;

import java.util.ArrayList;
import java.util.Date;

public class Operations {
    // 3, 4, 6, 8, 12

    private Date actualTime = new Date();
    private int countTime = 0;

    public Data data = new Data();

    /**
     * vyhľadanie záznamov pacienta/ov v zadanej nemocnici (identifikovaná
     * svojím názvom) podľa mena a priezviska. Po nájdení pacienta/ov je
     * potrebné zobraziť všetky evidované údaje zo zadanej nemocnice
     * rozčlenené po pacientoch.
     */
    public void Operation_2(String nazovNemocnice, String meno, String priezvisko) {
        //get nemocnica
        Nemocnica nemocnica = (Nemocnica)data.nemocnice.find(nazovNemocnice);
        if (nemocnica == null) {
            System.out.println("nemocnica neezistuje");
            return;
        }

        Pacient minPacient = new Pacient("0 ", meno, priezvisko, null, null);
        Pacient maxPacient = new Pacient("999999999/999999999", meno, priezvisko, null, null);

        nemocnica.pacientiMena.intervalSearch(minPacient, maxPacient).forEach(a-> System.out.println(a.key.getMeno()));

    }

    /**
     * vykonanie záznamu o začiatku hospitalizácie pacienta (identifikovaný
     * svojím rodným číslom) v nemocnici(identifikovaná svojím názvom)
     */
    public void Operation_3(String rcPacienta, String nazovNemocnice) {
        //get pacient
        Pacient pacient = (Pacient)data.pacienti.find(rcPacienta);
        if (pacient == null) {
            System.out.println("pacient neezistuje");
            return;
        }
        //get nemocnica
        Nemocnica nemocnica = (Nemocnica)data.nemocnice.find(nazovNemocnice);
        if (nemocnica == null) {
            System.out.println("nemocnica neezistuje");
            return;
        }
        //vytvorit hosp
        Hospitalizacia newHosp = new Hospitalizacia(
                new Date(actualTime.getTime() + countTime),
                null,
                "",
                pacient
        );
        //pridat pacientovi
        pacient.addHosp(nazovNemocnice, newHosp);
        //pridat do nemocnice
        nemocnica.addHosp(newHosp);
        nemocnica.addPacient(pacient);
        nemocnica.addPoistovna(pacient.getPoistovna());
        //pridat do poistovne
        pacient.getPoistovna().addHosp(newHosp);
    }

    /**
     * vykonanie záznamu o ukončení hospitalizácie pacienta (identifikovaný
     * svojím rodným číslom) v nemocnici (identifikovaná svojím názvom)
     */
    public void Operation_4(String rcPacienta, String nazovNemocnice) {
        Pacient pacient = (Pacient)data.pacienti.find(rcPacienta);
        if (pacient == null) {
            System.out.println("pacient neezistuje");
            return;
        }

        Hospitalizacia hosp = pacient.getNeukoncenaHosp(nazovNemocnice);
        if (hosp == null) {
            System.out.println("hosp neezistuje");
        } else {
            hosp.setKoniecHosp();
        }

    }

    /**
     * pridanie pacienta
     */
    public void Operation_6(
            String rodneCislo,
            String meno,
            String priezvisko,
            Date datumNarodenia,
            Poistovna poistovna) {

        data.addPacient(
                rodneCislo,
                meno,
                priezvisko,
                datumNarodenia,
                poistovna
        );
    }

    /**
     * výpis aktuálne hospitalizovaných pacientov vnemocnici
     * (identifikovaná svojím názvom)
     */
    public void Operation_8(String nazovNemocnice) {
        Nemocnica nemocnica = (Nemocnica)data.nemocnice.find(nazovNemocnice);
        if (nemocnica == null) {
            System.out.println("nemocnica neezistuje");
            return;
        }

        ArrayList<Hospitalizacia> neukonceneHosp = new ArrayList<>();

        for (BSData<Date> dateBSData : nemocnica.hospitalizacie.levelOrder()) {
            Hospitalizacia hosp = (Hospitalizacia)dateBSData;
            if (hosp.getKoniecHosp() == null) {
                neukonceneHosp.add(hosp);
            }
        }

        neukonceneHosp.forEach(a -> System.out.println(a.key.toString()));
        System.out.println();


    }

    /**
     * pridanie nemocnice
     */
    public void Operation_12(String nazovNemocnice) {
        data.addNemocnica(nazovNemocnice);
    }

    /**
     * pridanie poistovne
     */
    public void Operation_addPoistovna(String nazovPoistovne) {
        data.addPoistovna(nazovPoistovne);
    }
}
