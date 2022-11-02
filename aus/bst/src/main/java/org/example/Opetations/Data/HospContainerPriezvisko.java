package org.example.Opetations.Data;

import org.example.BSData;
import org.example.Shared.Comparators;
import org.example.Shared.Compare;

public class HospContainerPriezvisko extends BSData<Hospitalizacia> {

    public HospContainerPriezvisko(Hospitalizacia key) {
        super(key);
    }

    @Override
    public Compare compare(BSData<Hospitalizacia> data) {
        Comparators comparators = new Comparators();
        return comparators.hospComparePriezvisko(data.key, this.key);
    }
}
