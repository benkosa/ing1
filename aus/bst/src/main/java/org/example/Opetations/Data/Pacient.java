package org.example.Opetations.Data;

import org.example.BSData;
import org.example.BSTree;
import org.example.Shared.Comparators;
import org.example.Shared.Compare;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Pacient extends BSData<String> {
    private String rodneCislo;
    private String meno;
    private String priezvisko;
    private Date datumNarodenia;
    private Poistovna poistovna;

    public Date getDatumNarodenia() {
        return datumNarodenia;
    }

    public String getDatumNarodeniaString() {
        return datumNarodenia.toString();
    }

    public String getRodneCislo () { return rodneCislo; }
    public String getMeno () { return meno; }
    public String getPriezvisko () { return priezvisko; }



    private BSTree<String> hospitalizacie = new BSTree<>();

    public Pacient(
            String rodneCislo,
            String meno,
            String priezvisko,
            Date datumNarodenia,
            Poistovna poistovna) {
        super(rodneCislo);
        this.rodneCislo = rodneCislo;
        this.meno = meno;
        this.priezvisko = priezvisko;
        this.datumNarodenia = datumNarodenia;
        this.poistovna = poistovna;
    }

    @Override
    public Compare compare(BSData<String> data) {
        Comparators comparators = new Comparators();
        return comparators.stringCompare(data.key, this.key);
    }

    public Poistovna getPoistovna() { return this.poistovna;};

    public static class PacientHosp extends BSData<String> {

        // key is name of hospital
        PacientHosp(String key, Hospitalizacia hospitalizacia) {
            super(key);
            addHosp(hospitalizacia);
        }

        private ArrayList<Hospitalizacia> hospitalizacie = new ArrayList<>();

        public void addHosp(Hospitalizacia hospitalizacia) {
            hospitalizacie.add(hospitalizacia);
        }

        @Override
        public Compare compare(BSData<String> data) {
            Comparators comparators = new Comparators();
            return comparators.stringCompare(data.key, this.key);
        }
    }

    public void addHosp(String nazovNem, Hospitalizacia hosp) {
        PacientHosp pacientHosp = (PacientHosp)this.hospitalizacie.find(nazovNem);

        //uz existuje nemocnica
        if (pacientHosp != null) {
            pacientHosp.addHosp(hosp);
        // este neexistuje nemocnica
        } else {
            this.hospitalizacie.insert(new PacientHosp(nazovNem, hosp));
        }
    }

    public Hospitalizacia getNeukoncenaHosp(String nazovNem) {
        PacientHosp nemocnica = (PacientHosp)hospitalizacie.find(nazovNem);
        if (nemocnica == null) {
            return null;
        }
        for (int i = 0; i < nemocnica.hospitalizacie.size(); i++) {
            if (nemocnica.hospitalizacie.get(i).getKoniecHosp() == null) {
                return nemocnica.hospitalizacie.get(i);
            }
        }
        return null;
    }

}


