package org.example.Opetations.Data;

import org.example.BSData;
import org.example.BSTree;
import org.example.Shared.Compare;
import org.example.Shared.Comparators;

import java.util.ArrayList;
import java.util.Date;

public class Nemocnica  extends BSData<String> {

    private final BSTree<String> pacienti = new BSTree<>();
    private final BSTree<Pacient> pacientiMena = new BSTree<>();

    private final BSTree<String> poistovne = new BSTree<>();

    private final BSTree<Date> hospitalizacie = new BSTree<>();

    private final BSTree<Hospitalizacia> hospitalizacieDate = new BSTree<>();

    public Nemocnica(String key) {
        super(key);
    }

    public BSTree<Pacient> getPacientiMena() {
        return pacientiMena;
    }

    @Override
    public Compare compare(BSData<String> data) {
        Comparators comparators = new Comparators();
        return comparators.stringCompare(data.key, this.key);
    }


    public void addPacient(Pacient pacient) {
        pacienti.insert(pacient);
        pacientiMena.insert(new PacientContainer(pacient));
    }

    public void addPoistovna(Poistovna poistovna) {
        poistovne.insert(poistovna);
    }

    public boolean addHosp(Hospitalizacia hosp) {
        if (hospitalizacie.insert(hosp) == false) {
            return false;
        };
        hospitalizacieDate.insert(new HospitalizaciaContainer(hosp));
        return true;
    }

    public ArrayList<Hospitalizacia> getNeukonceneHosp() {
        Comparators comp = new Comparators();

        Hospitalizacia tmpHosp = new Hospitalizacia(
                comp.getMinDate(),
                null,
                null,
                null,
                null
        );
        Hospitalizacia tmpHosp2 = new Hospitalizacia(
                comp.getMaxDate(),
                null,
                null,
                null,
                null
        );

        final ArrayList<BSData<Hospitalizacia>> neukonceneHosp
                = hospitalizacieDate.intervalSearch(tmpHosp, tmpHosp2);

        ArrayList<Hospitalizacia> retNeukoncenaHosp = new ArrayList<>();
        if (neukonceneHosp != null) {
            for (BSData<Hospitalizacia> hospitalizaciaBSData : neukonceneHosp) {
                Hospitalizacia hosp = hospitalizaciaBSData.key;
                    if (hosp.getKoniecHosp() == null) {
                        retNeukoncenaHosp.add(hosp);
                    }
                }
        }

        return retNeukoncenaHosp;
    }

    public Hospitalizacia removeHosp(Hospitalizacia hosp) {
        return hospitalizacieDate.remove(hosp).key;
    }

    public void insertEdited(Hospitalizacia hosp) {
        hospitalizacieDate.insert(new HospitalizaciaContainer(hosp));
    }
}
