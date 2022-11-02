package org.example.Opetations.Data;

import org.example.BSData;
import org.example.Shared.Comparators;
import org.example.Shared.Compare;

public class HospContainerRc extends BSData<Hospitalizacia> {

    public HospContainerRc(Hospitalizacia key) {
        super(key);
    }

    @Override
    public Compare compare(BSData<Hospitalizacia> data) {
        Comparators comparators = new Comparators();
        return comparators.hospCompareRc(data.key, this.key);
    }
}
