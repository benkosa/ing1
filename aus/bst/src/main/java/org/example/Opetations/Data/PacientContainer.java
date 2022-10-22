package org.example.Opetations.Data;

import org.example.BSData;
import org.example.Shared.Comparators;
import org.example.Shared.Compare;

public class PacientContainer extends BSData<Pacient> {
    public PacientContainer(Pacient key) {
        super(key);
    }

    @Override
    public Compare compare(BSData<Pacient> data) {
        Comparators comparators = new Comparators();
        return comparators.pacientCompare(data.key, this.key);
    }
}
