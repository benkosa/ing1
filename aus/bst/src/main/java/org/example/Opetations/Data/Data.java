package org.example.Opetations.Data;

import org.example.BSTree;

import java.util.Date;

public class Data {
    public BSTree<String> getNemocnice() {
        return nemocnice;
    }

    public BSTree<String> getPacienti() {
        return pacienti;
    }

    public BSTree<String> getPoistovne() {
        return poistovne;
    }

    private final BSTree<String> nemocnice = new BSTree<>();

    private final BSTree<String> pacienti = new BSTree<>();

    private final BSTree<String> poistovne = new BSTree<>();

    private final BSTree<Date> hospitalizacie = new BSTree<>();

    public boolean addNemocnica(String name) {
        return nemocnice.insert(new Nemocnica(name));
    }

    public boolean addPacient(
            String rodneCislo,
            String meno,
            String priezvisko,
            Date datumNarodenia,
            Poistovna poistovna) {

        return pacienti.insert(
                new Pacient(
                        rodneCislo,
                        meno,
                        priezvisko,
                        datumNarodenia,
                        poistovna
                )
        );
    }

    public boolean addPoistovna(String name) {
        return poistovne.insert(new Poistovna(name));
    }

    public boolean addHospitalizacia(Hospitalizacia hosp) {
        return hospitalizacie.insert(hosp);
    }
}
