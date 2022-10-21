package org.example.Opetations;

import org.example.Opetations.Data.Data;

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
    public void Operation_6(String rcPacienta) {

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
