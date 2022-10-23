package org.example.Opetations.Data;

import org.example.BSTree;

import java.util.Date;

public class Data {
    public final BSTree<String> nemocnice = new BSTree<>();

    public final BSTree<String> pacienti = new BSTree<>();

    public final BSTree<String> poistovne = new BSTree<>();

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
}
