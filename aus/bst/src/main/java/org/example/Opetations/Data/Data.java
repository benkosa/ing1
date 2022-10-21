package org.example.Opetations.Data;

import org.example.BSTree;

import java.util.Date;

public class Data {
    public final BSTree<String> nemocnice = new BSTree<>();

    public final BSTree<String> pacienti = new BSTree<>();

    public void addNemocnica(String name) {
        nemocnice.insert(new Nemocnica(name));
    }

    public void addPacient(
            String rodneCislo,
            String meno,
            String priezvisko,
            Date datumNarodenia,
            Poistovna poistovna) {

        pacienti.insert(
                new Pacient(
                        rodneCislo,
                        meno,
                        priezvisko,
                        datumNarodenia,
                        poistovna
                )
        );
    }
}
