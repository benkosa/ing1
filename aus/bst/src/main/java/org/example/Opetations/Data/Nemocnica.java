package org.example.Opetations.Data;

import org.example.BSData;
import org.example.Shared.Compare;
import org.example.Shared.Comparators;

public class Nemocnica  extends BSData<String> {

    public Nemocnica(String key) {
        super(key);
    }

    @Override
    public Compare compare(BSData<String> data) {
        Comparators comparators = new Comparators();
        return comparators.stringCompare(data.key, this.key);
    }
}
