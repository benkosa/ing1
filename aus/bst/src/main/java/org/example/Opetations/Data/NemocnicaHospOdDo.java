package org.example.Opetations.Data;

import org.example.BSData;
import org.example.BSTree;
import org.example.Shared.Comparators;
import org.example.Shared.Compare;

import java.util.ArrayList;
import java.util.Date;

public class NemocnicaHospOdDo extends BSData<String> {

    NemocnicaHospOdDo( Hospitalizacia hospitalizacia) {
        super(hospitalizacia.getNemocnica().key);
        addHosp(hospitalizacia);
    }

    public BSTree<Date> getHospitalizacie() {
        return hospitalizacie;
    }

    private BSTree<Date> hospitalizacie = new BSTree<>();

    public boolean addHosp(Hospitalizacia hospitalizacia) {
        return hospitalizacie.insert(hospitalizacia);
    }

    @Override
    public Compare compare(BSData<String> data) {
        Comparators comparators = new Comparators();
        return comparators.stringCompare(data.key, this.key);
    }
}