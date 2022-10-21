package org.example.Opetations;

import org.example.Opetations.Data.Data;
import org.example.Opetations.Data.Pacient;
import org.example.Opetations.Data.Poistovna;

import java.util.Date;

public class Operations {
    // 3, 4, 6, 8, 12

    public Data data = new Data();

    /**
     * vykonanie záznamu o začiatku hospitalizácie pacienta (identifikovaný
     * svojím rodným číslom) v nemocnici(identifikovaná svojím názvom)
     */
    public void Operation_3(String rcPacienta, String nazovNemocnice) {

    }

    /**
     * vykonanie záznamu o ukončení hospitalizácie pacienta (identifikovaný
     * svojím rodným číslom) v nemocnici (identifikovaná svojím názvom)
     */
    public void Operation_4(String rcPacienta, String nazovNemocnice) {

    }

    /**
     * pridanie pacienta
     */
    public void Operation_6(
            String rodneCislo,
            String meno,
            String priezvisko,
            Date datumNarodenia,
            Poistovna poistovna) {

        data.addPacient(
                rodneCislo,
                meno,
                priezvisko,
                datumNarodenia,
                poistovna
        );
    }

    /**
     * výpis aktuálne hospitalizovaných pacientov vnemocnici
     * (identifikovaná svojím názvom)
     */
    public void Operation_8(String nazovNemocnice) {

    }

    /**
     * pridanie nemocnice
     */
    public void Operation_12(String nazovNemocnice) {
        data.addNemocnica(nazovNemocnice);
    }
}
