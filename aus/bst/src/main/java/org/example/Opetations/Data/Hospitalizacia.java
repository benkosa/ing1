package org.example.Opetations.Data;

import org.example.BSData;
import org.example.Shared.Comparators;
import org.example.Shared.Compare;

import java.util.Date;

public class Hospitalizacia extends BSData<Date> {
    private Date zaciatokHosp;
    private Date koniecHosp;

    public Date getZaciatokHosp() {
        return zaciatokHosp;
    }

    public String getZaciatokHospString() {
        return zaciatokHosp.toString();
    }

    public String getDiagnoza() {
        return diagnoza;
    }

    private String diagnoza;

    public Pacient getPacient() {
        return pacient;
    }

    private Pacient pacient;

    public Hospitalizacia(
            Date zaciatokHosp,
            Date koniecHosp,
            String diagnoza,
            Pacient pacient) {
        super(zaciatokHosp);
        this.zaciatokHosp = zaciatokHosp;
        this.koniecHosp = koniecHosp;
        this.diagnoza = diagnoza;
        this.pacient = pacient;
    }


    @Override
    public Compare compare(BSData<Date> data) {
        Comparators comparators = new Comparators();
        return comparators.dateCompare(data.key, this.key);
    }

    public Date getKoniecHosp() {return koniecHosp;}

    public void setKoniecHosp() {
        this.koniecHosp = new Date();
    };
}

