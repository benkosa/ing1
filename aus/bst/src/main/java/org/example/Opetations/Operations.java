package org.example.Opetations;

import org.example.BSData;
import org.example.Opetations.Data.*;
import org.example.Shared.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Operations {
    // 3, 4, 6, 8, 12

    private Date actualTime = new Date();
    private int countTime = 0;

    public Data data = new Data();

    SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy");


    public Response setActualTime(String time) {
        try {
            actualTime = formatter2.parse(time);
        } catch (ParseException e) {
            return new Response<String>(1, "Zly format alebo datum" ,null);
        }
        return new Response<String>(0, "" ,null);
    }


    /**
     * vyhľadanie záznamov pacienta/ov v zadanej nemocnici (identifikovaná
     * svojím názvom) podľa mena a priezviska. Po nájdení pacienta/ov je
     * potrebné zobraziť všetky evidované údaje zo zadanej nemocnice
     * rozčlenené po pacientoch.
     */
    public void Operation_2(String nazovNemocnice, String meno, String priezvisko) {
        //get nemocnica
        Nemocnica nemocnica = (Nemocnica)data.nemocnice.find(nazovNemocnice);
        if (nemocnica == null) {
            System.out.println("nemocnica neezistuje");
            return;
        }

        Pacient minPacient = new Pacient("", meno, priezvisko, null, null);
        Pacient maxPacient = new Pacient("999999999/999999999", meno, priezvisko, null, null);

        nemocnica.pacientiMena.intervalSearch(minPacient, maxPacient).forEach(a-> System.out.println(a.key.getMeno()));

    }

    /**
     * vykonanie záznamu o začiatku hospitalizácie pacienta (identifikovaný
     * svojím rodným číslom) v nemocnici(identifikovaná svojím názvom)
     */
    public Response Operation_3(String rcPacienta, String nazovNemocnice) {
        //get pacient
        Pacient pacient = (Pacient)data.pacienti.find(rcPacienta);
        if (pacient == null) {
            return new Response(1, "Pacient neexistuje", null);
        }
        //get nemocnica
        Nemocnica nemocnica = (Nemocnica)data.nemocnice.find(nazovNemocnice);
        if (nemocnica == null) {
            return new Response(1, "Nemocnica neexistuje", null);
        }
        //vytvorit hosp
        Hospitalizacia newHosp = new Hospitalizacia(
                new Date(actualTime.getTime() + countTime),
                null,
                "",
                pacient
        );
        //pridat pacientovi
        pacient.addHosp(nazovNemocnice, newHosp);
        //pridat do nemocnice
        nemocnica.addHosp(newHosp);
        nemocnica.addPacient(pacient);
        nemocnica.addPoistovna(pacient.getPoistovna());
        //pridat do poistovne
        pacient.getPoistovna().addHosp(newHosp);

        return new Response(0, "", null);
    }

    /**
     * vykonanie záznamu o ukončení hospitalizácie pacienta (identifikovaný
     * svojím rodným číslom) v nemocnici (identifikovaná svojím názvom)
     */
    public Response Operation_4(String rcPacienta, String nazovNemocnice) {
        Pacient pacient = (Pacient)data.pacienti.find(rcPacienta);
        if (pacient == null) {
            return new Response(1, "Pacient neexistuje", null);
        }

        Hospitalizacia hosp = pacient.getNeukoncenaHosp(nazovNemocnice);
        if (hosp == null) {
            return new Response(1, "Hospitalizacia neexistuje", null);
        } else {
            hosp.setKoniecHosp();
        }

        return new Response(0, "", null);
    }

    /**
     * pridanie pacienta
     */
    public Response Operation_6(
            String rodneCislo,
            String meno,
            String priezvisko,
            String datumNarodenia,
            String kodPoistovne) {

        Poistovna poistovna;

        // ak je null vybereme nahodne z poistovni
        if (kodPoistovne == null) {
            poistovna = (Poistovna) data.poistovne.getRandomData();
        } else {
            poistovna = (Poistovna) data.poistovne.find(kodPoistovne);
        }

        if (poistovna == null) {
            System.out.println("hosp neezistuje");
            return new Response(1, "Poistovna neexistuje", null);
        }

        Date date;

        try {
            date = formatter2.parse(datumNarodenia);
        } catch (ParseException e) {
            return new Response<String>(1, "Zly format alebo datum" ,null);
        }

        if (data.addPacient(
                rodneCislo,
                meno,
                priezvisko,
                date,
                poistovna
        ) == false) {
            return new Response<String>(1, "Pacien uz existuje" ,null);
        }

        return new Response<String>(0, "" ,null);
    }

    /**
     * výpis aktuálne hospitalizovaných pacientov vnemocnici
     * (identifikovaná svojím názvom)
     */
    public void Operation_8(String nazovNemocnice) {
        Nemocnica nemocnica = (Nemocnica)data.nemocnice.find(nazovNemocnice);
        if (nemocnica == null) {
            System.out.println("nemocnica neezistuje");
            return;
        }

        ArrayList<Hospitalizacia> neukonceneHosp = new ArrayList<>();

        for (BSData<Date> dateBSData : nemocnica.hospitalizacie.levelOrder()) {
            Hospitalizacia hosp = (Hospitalizacia)dateBSData;
            if (hosp.getKoniecHosp() == null) {
                neukonceneHosp.add(hosp);
            }
        }

        neukonceneHosp.forEach(a -> System.out.println(a.key.toString()));
        System.out.println();


    }

    /**
     * pridanie nemocnice
     */
    public Response Operation_12(String nazovNemocnice) {
        if (!data.addNemocnica(nazovNemocnice)) {
            return new Response(1, "Nemocnica uz existuje", null);
        }
        return new Response(0, "", null);
    }

    /**
     * pridanie poistovne
     */
    public Response Operation_addPoistovna(String nazovPoistovne) {
        if (!data.addPoistovna(nazovPoistovne)) {
            return new Response(1, "Poistovna uz existuje", null);
        }
        return new Response(0, "", null);
    }
}
