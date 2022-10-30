package org.example.Opetations.Data;

import org.example.BSData;
import org.example.BSTree;
import org.example.Shared.Comparators;
import org.example.Shared.Compare;

import java.util.ArrayList;
import java.util.Date;

public class Pacient extends BSData<String> {
    private String rodneCislo;
    private String meno;
    private String priezvisko;
    private Date datumNarodenia;
    private Poistovna poistovna;
    private BSTree<String> hospitalizacie = new BSTree<>();


    public Date getDatumNarodenia() {
        return datumNarodenia;
    }

    public String getDatumNarodeniaString() {
        return datumNarodenia.toString();
    }

    public String getRodneCislo () { return rodneCislo; }
    public String getMeno () { return meno; }
    public String getPriezvisko () { return priezvisko; }

    public ArrayList<Hospitalizacia> getHospotalizacie(String nemocnica) {

        NemocnicaHosp nemocnicaHosp = (NemocnicaHosp)hospitalizacie.find(nemocnica);
        if (nemocnicaHosp == null) return null;

        return nemocnicaHosp.getHospitalizacie();
    }




    public Pacient(
            String rodneCislo,
            String meno,
            String priezvisko,
            Date datumNarodenia,
            Poistovna poistovna) {
        super(rodneCislo);
        this.rodneCislo = rodneCislo;
        this.meno = meno;
        this.priezvisko = priezvisko;
        this.datumNarodenia = datumNarodenia;
        this.poistovna = poistovna;
    }

    @Override
    public Compare compare(BSData<String> data) {
        Comparators comparators = new Comparators();
        return comparators.stringCompare(data.key, this.key);
    }

    public Poistovna getPoistovna() { return this.poistovna;};

    public boolean addHosp(String nazovNem, Hospitalizacia hosp) {
        NemocnicaHosp nemocnicaHosp = (NemocnicaHosp)this.hospitalizacie.find(nazovNem);

        //uz existuje nemocnica
        if (nemocnicaHosp != null) {
            return nemocnicaHosp.addHosp(hosp);
        // este neexistuje nemocnica
        } else {
            return this.hospitalizacie.insert(new NemocnicaHosp(hosp));
        }
    }

    public Hospitalizacia getNeukoncenaHosp(String nazovNem) {
        NemocnicaHosp nemocnica = (NemocnicaHosp)hospitalizacie.find(nazovNem);
        if (nemocnica == null) {
            return null;
        }
        for (int i = 0; i < nemocnica.getHospitalizacie().size(); i++) {
            if (nemocnica.getHospitalizacie().get(i).getKoniecHosp() == null) {
                return nemocnica.getHospitalizacie().get(i);
            }
        }
        return null;
    }

}


