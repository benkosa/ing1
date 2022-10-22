package org.example.Opetations.Data;

import org.example.BSData;
import org.example.BSTree;
import org.example.Shared.Compare;
import org.example.Shared.Comparators;

import java.util.Date;

public class Nemocnica  extends BSData<String> {

    public final BSTree<String> pacienti = new BSTree<>();

    public final BSTree<Pacient> pacientiMena = new BSTree<>();

    public final BSTree<String> poistovne = new BSTree<>();

    public final BSTree<Date> hospitalizacie = new BSTree<>();

    public Nemocnica(String key) {
        super(key);
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

    public void addHosp(Hospitalizacia hosp) {
        hospitalizacie.insert(hosp);
    }
}
