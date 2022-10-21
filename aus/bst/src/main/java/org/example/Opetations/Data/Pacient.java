package org.example.Opetations.Data;

import org.example.BSData;
import org.example.BSTree;
import org.example.Shared.Comparators;
import org.example.Shared.Compare;

import java.util.ArrayList;
import java.util.Date;

public class Pacient extends BSData<String> {
    private String rodneCislo;
    private String meno;
    private String priezvisko;
    private Date datumNarodenia;
    private Poistovna poistovna;

    private BSTree<String> hospitalizacie = new BSTree<>();

    Pacient(
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
            return null;
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

}


