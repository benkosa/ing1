package org.example.Opetations.Data;

import org.example.BSData;
import org.example.BSTree;
import org.example.Shared.Comparators;
import org.example.Shared.Compare;

import java.util.Date;

public class Poistovna extends BSData<String> {
    private String kod;

    public final BSTree<Date> hospitalizacie = new BSTree<>();

    Poistovna(String kod) {
        super(kod);
        this.kod = kod;
    }

    @Override
    public Compare compare(BSData<String> data) {
        Comparators comparators = new Comparators();
        return comparators.stringCompare(data.key, this.key);
    }

    public void addHosp(Hospitalizacia hosp) {
        hospitalizacie.insert(hosp);
    }
}
