package org.example.Opetations.Data;

import org.example.BSData;
import org.example.BSTree;
import org.example.Shared.Comparators;
import org.example.Shared.Compare;

import java.util.ArrayList;
import java.util.Date;

public class Poistovna extends BSData<String> {
    private String kod;

    //private BSTree<String> hospitalizacie = new BSTree<>();

    private BSTree<String> nemocniceHosp = new BSTree<>();

    public void balance() {
        nemocniceHosp.balanceTree();
    }

    public void migrate(String oldHospital, String newHospital) {
        ArrayList<Hospitalizacia> hospitalizacias = getAllHospotalizacie(oldHospital);
        if (hospitalizacias == null) return;

        for (Hospitalizacia hospitalizacia : hospitalizacias) {
            addHosp(hospitalizacia);
        }
        nemocniceHosp.remove(oldHospital);
    }

    public Poistovna(String kod) {
        super(kod);
        this.kod = kod;
    }

    @Override
    public Compare compare(BSData<String> data) {
        Comparators comparators = new Comparators();
        return comparators.stringCompare(data.key, this.key);
    }

    public ArrayList<Hospitalizacia> getHospotalizacie(String nemocnica, Date odDate, Date doDate) {

        ArrayList<Hospitalizacia> retHosp = new ArrayList<>();

        NemocnicaHospOdDo nemocnicaHospOdDo = (NemocnicaHospOdDo)this.nemocniceHosp.find(nemocnica);
        if (nemocnicaHospOdDo == null) return retHosp;

        nemocnicaHospOdDo.getHospitalizacie().levelOrder().forEach(a -> {
            Hospitalizacia aa = (Hospitalizacia)a;
            System.out.println(a.key + aa.getNemocnica().key + aa.getPacient().key + aa.getPacient().getPoistovna().key);
        });

        for (BSData<Date> dataDate : nemocnicaHospOdDo.getHospitalizacie().intervalSearch(odDate, doDate)) {
            retHosp.add((Hospitalizacia) dataDate);
        }

        return retHosp;
    }

    public ArrayList<Hospitalizacia> getAllHospotalizacie(String nemocnica) {

        ArrayList<Hospitalizacia> retHosp = new ArrayList<>();

        NemocnicaHospOdDo nemocnicaHospOdDo = (NemocnicaHospOdDo)this.nemocniceHosp.find(nemocnica);
        if (nemocnicaHospOdDo == null) return retHosp;

        nemocnicaHospOdDo.getHospitalizacie().levelOrder().forEach(a -> {
            retHosp.add((Hospitalizacia)a);
            Hospitalizacia aa = (Hospitalizacia)a;
            System.out.println(a.key + aa.getNemocnica().key + aa.getPacient().key + aa.getPacient().getPoistovna().key);
        });

        return retHosp;
    }

    public boolean addHosp(Hospitalizacia hosp) {
        String nazovNem = hosp.getNemocnica().key;
        NemocnicaHospOdDo nemocnicaHospOdDo =
                (NemocnicaHospOdDo)this.nemocniceHosp.find(nazovNem);

        // uz existuje nemocnica
        if (nemocnicaHospOdDo != null) {
            //TODO  skontrolovat
            return nemocnicaHospOdDo.addHosp(hosp);
        // este neexistuje nemocnica
        } else {
            return nemocniceHosp.insert(new NemocnicaHospOdDo(hosp));
        }


    }
}
