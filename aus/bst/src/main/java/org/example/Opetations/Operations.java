package org.example.Opetations;

import org.example.Opetations.Data.*;

import java.util.Date;

public class Operations {
    // 3, 4, 6, 8, 12

    private Date actualTime = new Date();
    private int countTime = 0;

    public Data data = new Data();

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
