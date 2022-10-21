package org.example.Opetations.Data;

import java.util.Date;

public class Hospitalizacia {
    private Date zaciatokHosp;
    private Date koniecHosp;
    private String diagnoza;
    private Pacient pacient;

    Hospitalizacia(
        Date zaciatokHosp,
        Date koniecHosp,
        String diagnoza,
        Pacient pacient) {
        this.zaciatokHosp = zaciatokHosp;
        this.koniecHosp = koniecHosp;
        this.diagnoza = diagnoza;
        this.pacient = pacient;
    }


}

