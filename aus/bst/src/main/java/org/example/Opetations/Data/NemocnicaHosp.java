package org.example.Opetations.Data;

import org.example.BSData;
import org.example.Shared.Comparators;
import org.example.Shared.Compare;

import java.util.ArrayList;

public class NemocnicaHosp extends BSData<String> {

    NemocnicaHosp( Hospitalizacia hospitalizacia) {
        super(hospitalizacia.getNemocnica().key);
        addHosp(hospitalizacia);
    }

    public ArrayList<Hospitalizacia> getHospitalizacie() {
        return hospitalizacie;
    }

    private ArrayList<Hospitalizacia> hospitalizacie = new ArrayList<>();

    public boolean addHosp(Hospitalizacia hospitalizacia) {
        return hospitalizacie.add(hospitalizacia);
    }

    @Override
    public Compare compare(BSData<String> data) {
        Comparators comparators = new Comparators();
        return comparators.stringCompare(data.key, this.key);
    }
}