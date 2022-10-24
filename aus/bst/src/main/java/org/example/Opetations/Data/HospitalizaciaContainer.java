package org.example.Opetations.Data;

import org.example.BSData;
import org.example.Shared.Comparators;
import org.example.Shared.Compare;

public class HospitalizaciaContainer extends BSData<Hospitalizacia> {

    public HospitalizaciaContainer(Hospitalizacia key) {
        super(key);
    }

    @Override
    public Compare compare(BSData<Hospitalizacia> data) {
        Comparators comparators = new Comparators();
        return comparators.hospDateCompare(data.key, this.key);
    }
}
