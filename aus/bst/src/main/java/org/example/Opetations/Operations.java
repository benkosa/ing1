package org.example.Opetations;

import org.example.BSData;
import org.example.Opetations.Data.*;
import org.example.Shared.Response;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Operations {
    // 3, 4, 6, 8, 12

    private Date actualTime = new Date();
    private int countTime = 0;

    public Data getData() {
        return data;
    }

    private Data data = new Data();

    private SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy");

    //https://stackoverflow.com/questions/428918/how-can-i-increment-a-date-by-one-day-in-java
    /*
     * Calendar.DATE
     */
    private static Date addDATE(Date date, int unit, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(unit, days); //minus number would decrement the days
        return cal.getTime();
    }


    public Response setActualTime(String time) {
        try {
            actualTime = formatter2.parse(time);
        } catch (ParseException e) {
            return new Response<String>(1, "Zly format alebo datum" ,null);
        }
        return new Response<String>(0, "" ,null);
    }

    /**
     * vyhľadanie záznamov pacienta (identifikovaný svojím rodným číslom) v zadanej nemocnici
     * (identifikovaná svojím názvom). Po nájdení pacienta je potrebné zobraziť všetky
     * evidované údaje.
     */
    public Response<ArrayList<Hospitalizacia>> Operation_1(String rcPacient, String nazovNemocnice) {
        //get nemocnica
        Nemocnica nemocnica = (Nemocnica)data.getNemocnice().find(nazovNemocnice);
        if (nemocnica == null) {
            return new Response(1, "Nemocnica neexistuje", null);
        }

        //get pacient
        Pacient pacient = (Pacient)nemocnica.getPacienti().find(rcPacient);
        if (pacient == null) {
            return new Response(1, "Pacient neexistuje", null);
        }

        return new Response(0, "", pacient.getHospotalizacie(nazovNemocnice));
    }


    /**
     * vyhľadanie záznamov pacienta/ov v zadanej nemocnici (identifikovaná
     * svojím názvom) podľa mena a priezviska. Po nájdení pacienta/ov je
     * potrebné zobraziť všetky evidované údaje zo zadanej nemocnice
     * rozčlenené po pacientoch.
     */
    public Response<ArrayList<BSData<Pacient>>> Operation_2(String nazovNemocnice, String meno, String priezvisko) {
        //get nemocnica
        Nemocnica nemocnica = (Nemocnica)data.getNemocnice().find(nazovNemocnice);
        if (nemocnica == null) {
            return new Response<>(1, "nemocnica neexistuje" ,null);
        }

        String minString = "";
        String maxString = "";
        for (int i = 0; i < 11; i++) {
            minString += Character.MIN_VALUE;
            maxString+=Character.MAX_VALUE;
        }


        Pacient minPacient = new Pacient(minString, meno, priezvisko, null, null);
        Pacient maxPacient = new Pacient(maxString, meno, priezvisko, null, null);

        //nemocnica.getPacientiMena().levelOrder().forEach(a-> System.out.println(a.key.getMeno()));

        ArrayList<BSData<Pacient>> pacienti = nemocnica
                .getPacientiMena()
                .intervalSearch(minPacient, maxPacient);

        //pacienti.forEach(a-> System.out.println(a.key.getMeno()));

        if (pacienti == null) pacienti = new ArrayList<>();
        return new Response<>(0, "" , pacienti);

    }

    /**
     * vykonanie záznamu o začiatku hospitalizácie pacienta (identifikovaný
     * svojím rodným číslom) v nemocnici(identifikovaná svojím názvom)
     */
    public Response Operation_3(String rcPacienta, String nazovNemocnice, Date datumHosp) {
        //get pacient
        Pacient pacient = (Pacient)data.getPacienti().find(rcPacienta);
        if (pacient == null) {
            return new Response(1, "Pacient neexistuje", null);
        }
        //get nemocnica
        Nemocnica nemocnica = (Nemocnica)data.getNemocnice().find(nazovNemocnice);
        if (nemocnica == null) {
            return new Response(1, "Nemocnica neexistuje", null);
        }

        if (datumHosp == null) datumHosp = new Date(actualTime.getTime() + countTime);
        //vytvorit hosp
        Hospitalizacia newHosp = new Hospitalizacia(
                datumHosp,
                null,
                "",
                pacient,
                nemocnica
        );
        countTime+=1;
        //pridate do vsetkych
        if (data.addHospitalizacia(newHosp) == false) {
            return new Response(1, "Chyba, opakuj pridanie", null);
        }
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
     * pzaciatok hospitalizacie pre generovanie
     */
    public Response Operation_3(Pacient pacient, Nemocnica nemocnica, Date datumZaciatku, String diagnoza) {

        //vytvorit hosp
        Hospitalizacia newHosp = new Hospitalizacia(
                datumZaciatku,
                null,
                diagnoza,
                pacient,
                nemocnica
        );
        //pridate do vsetkych
        if (data.addHospitalizacia(newHosp) == false) {
            return new Response(1, "Hospitalizacia uz existuje", null);
        }
        //pridat do nemocnice
        nemocnica.addHosp(newHosp);
        nemocnica.addPacient(pacient);
        nemocnica.addPoistovna(pacient.getPoistovna());
        //pridat pacientovi
        pacient.addHosp(nemocnica.key, newHosp);
        //pridat do poistovne
        pacient.getPoistovna().addHosp(newHosp);

        return new Response(0, "", null);
    }

    /**
     * vykonanie záznamu o ukončení hospitalizácie pacienta (identifikovaný
     * svojím rodným číslom) v nemocnici (identifikovaná svojím názvom)
     */
    public Response Operation_4(String rcPacienta, String nazovNemocnice) {
        Pacient pacient = (Pacient)data.getPacienti().find(rcPacienta);
        if (pacient == null) {
            return new Response(1, "Pacient neexistuje", null);
        }

        Hospitalizacia hosp = pacient.getNeukoncenaHosp(nazovNemocnice);
        if (hosp == null) {
            return new Response(1, "Hospitalizacia neexistuje", null);
        } else {
            hosp.setKoniecHosp();
            hosp.getNemocnica().insertEdited(hosp);
        }

        return new Response(0, "", null);
    }

    /**
     *  výpis hospitalizovaných pacientov v nemocnici (identifikovaná svojím
     *  názvom) v zadanom časovom období (od, do)
     */
    public Response<ArrayList<Pacient>> Operation_5(String nazovNemocnice, String strOd, String strDo) {
        //get nemocnica
        Nemocnica nemocnica = (Nemocnica)data.getNemocnice().find(nazovNemocnice);
        if (nemocnica == null) {
            return new Response<>(1, "Nemocnica neexistuje", null);
        }
        // parse dates
        Date dateOd;
        Date dateDo;
        Date minDate;
        try {
            dateOd = formatter2.parse(strOd);
            dateDo = formatter2.parse(strDo);
            minDate = formatter2.parse("01-01-0001");
        } catch (ParseException e) {
            return new Response<>(1, "Zly format alebo datum" ,null);
        }
        if (dateDo.compareTo(dateOd) < 0) {
            return new Response<>(1, "DatumOd > datumDo" ,null);
        }
        ArrayList<BSData<Date>> hospitalizovany = nemocnica.getHospitalizacie().intervalSearch(minDate, dateDo);

        ArrayList<Pacient> pacienti = new ArrayList<>();

        for (BSData<Date> dateBSData : hospitalizovany) {
            Hospitalizacia hosp = (Hospitalizacia)dateBSData;
            if (hosp.getKoniecHosp() == null || hosp.getKoniecHosp().compareTo(dateOd) > 0) {
                pacienti.add(hosp.getPacient());
            }
        }

        return new Response<>(0, "" ,pacienti);
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
            poistovna = (Poistovna) data.getPoistovne().getRandomData();
        } else {
            poistovna = (Poistovna) data.getPoistovne().find(kodPoistovne);
        }

        if (poistovna == null) {
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
     * vytvorenie podkladov pre účtovné oddelenie na tvorbu faktúr pre zdravotné poisťovne
     * za zadaný mesiac. Pre každú poisťovňu, ktorej pacient (pacienti) bol v zadaný
     * kalendárny mesiac hospitalizovaní aspoň jeden deň je potrebné pripraviť podklady obsahujúce:
     * • kód zdravotnej poisťovne
     * • počet dní hospitalizácii (za všetkých pacientov – napr. 98 dní)
     * • výpis hospitalizovaných pacientov v jednotlivé dni mesiaca spolu s diagnózami
     *
     *          {
     *             "nazovNemocnice": [
     *                 "kodPoistovne": {
     *                     "celkovyPocetDniHospitalizacii": 98,
     *                     "kalendar": {
     *                         "1": [],
     *                         "2": [],
     *                         "3": [],
     *                     }
     *                 }
     *             ]
     *          }
     */
    public Response<ArrayList<String[]>> Operation_7 ( String mesiac ) {

        ArrayList<String[]> tableValues = new ArrayList<>();

        for (BSData<String> stringBSData : data.getNemocnice().inOrder()) {
            Nemocnica nemocnica = (Nemocnica) stringBSData;
            for (BSData<String> bsData : nemocnica.getPoistovne().inOrder()) {
                Poistovna poistovna = (Poistovna) bsData;
                Date datumOd;
                Date datumDo;
                Date minDate;

                String dateString = "01-"+mesiac;

                try {
                    datumOd = addDATE(formatter2.parse(dateString), Calendar.DATE, -1);
                    datumDo = addDATE(formatter2.parse(dateString), Calendar.MONTH, 1);
                    //datumDo = addDATE(datumDo, Calendar.DATE, -1);
                    minDate = formatter2.parse("01-01-0001");
                } catch (ParseException e) {
                    return new Response<>(1, "Zly format alebo datum" ,null);
                }


                ArrayList<Hospitalizacia> hospitalizacias =
                        poistovna.getHospotalizacie(nemocnica.key, minDate, datumDo);
                if (hospitalizacias == null) {
                    return new Response<>(1, "niesu hospitalizacie", null);
                }

                System.out.println(datumOd.toString());
                System.out.println(datumDo.toString());

                ArrayList<Hospitalizacia> filteredHospitalizacias = new ArrayList<>();
                for (Hospitalizacia hospitalizacia : hospitalizacias) {
                    if (hospitalizacia.getKoniecHosp() == null ||
                            hospitalizacia.getKoniecHosp().compareTo(datumOd) > 0) {
                        filteredHospitalizacias.add(hospitalizacia);
                    }
                }
                filteredHospitalizacias.forEach(a -> System.out.println(a.key + a.getNemocnica().key + a.getPacient().key + a.getPacient().getPoistovna().key));
                System.out.println("------");

                int sumDays = 0;
                ArrayList<ArrayList<Hospitalizacia>> hospitalizacieDni = new ArrayList<>(31);
                for (int i = 0; i < 32; i++) {
                    hospitalizacieDni.add(new ArrayList<>());
                }


                for (Hospitalizacia hospitalizacia : filteredHospitalizacias) {
                    Date startDate =
                            hospitalizacia.getZaciatokHosp().compareTo(datumOd) > 0 ?
                                    hospitalizacia.getZaciatokHosp() : addDATE(datumOd, Calendar.DATE, 1);
                    Date endDate = hospitalizacia.getKoniecHosp() == null ?
                            datumDo : hospitalizacia.getKoniecHosp();

                    for ( ; startDate.compareTo(endDate) < 0; startDate = addDATE(startDate, Calendar.DATE, 1)) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(startDate);
                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        hospitalizacieDni.get(dayOfMonth).add(hospitalizacia);
                        sumDays++;
                    }
                }



                tableValues.add(new String[] {
                        nemocnica.key,
                        poistovna.key,
                        sumDays+"",
                        "","","","",""
                });

                for (int i = 0; i < hospitalizacieDni.size(); i++) {
                    for (Hospitalizacia hospitalizacia : hospitalizacieDni.get(i)) {
                        System.out.println(i + " " + hospitalizacia.key + " " +hospitalizacia.getPacient().key);
                        tableValues.add(new String[] {
                                "",
                                "",
                                "",
                                i+"",
                                hospitalizacia.getPacient().getMeno(),
                                hospitalizacia.getPacient().getPriezvisko(),
                                hospitalizacia.getPacient().getRodneCislo(),
                                hospitalizacia.getDiagnoza()
                        });
                    }
                }

            }
        }

        return new Response<>(0, "" ,tableValues);
    }
    /**
     * výpis aktuálne hospitalizovaných pacientov vnemocnici
     * (identifikovaná svojím názvom)
     */
    public Response<ArrayList<Hospitalizacia>> Operation_8(String nazovNemocnice) {
        Nemocnica nemocnica = (Nemocnica)data.getNemocnice().find(nazovNemocnice);
        if (nemocnica == null) {
            return new Response(1, "Nemocnica neexistuje", null);
        }

        ArrayList<Hospitalizacia> neukonceneHosp =nemocnica.getNeukonceneHosp();



//        ArrayList<BSData<Date>> hospitalizacie = nemocnica.hospitalizacie.levelOrder();
//        if (hospitalizacie != null) {
//            for (BSData<Date> dateBSData : nemocnica.hospitalizacie.levelOrder()) {
//                Hospitalizacia hosp = (Hospitalizacia) dateBSData;
//                if (hosp.getKoniecHosp() == null) {
//                    neukonceneHosp.add(hosp);
//                }
//            }
//        }

        //neukonceneHosp.forEach(a -> System.out.println(a.getKoniecHosp().toString()));

        return new Response(0, "", neukonceneHosp);
    }

    /**
     * výpis aktuálne hospitalizovaných pacientov v nemocnici (identifikovaná
     * svojím názvom) zotriedený podľa rodných čísel, ktorý sú poistencami
     * zadanej zdravotnej poisťovne (identifikovaná svojím kódom)
     */
    public Response<ArrayList<Hospitalizacia>> Operation_10(String nazovNemocnice, String kodPoistovne) {
        Poistovna poistovna = (Poistovna) data.getPoistovne().find(kodPoistovne);
        if (poistovna == null) {
            return new Response(1, "Poistovna neexistuje", null);
        }

        return new Response(0, "", poistovna.getAllHospotalizacie(nazovNemocnice));
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
     * výpis nemocníc usporiadaných podľa názvov
     */
    public Response Operation_13() {
        return new Response(0, "", data.getNemocnice().inOrder());
    }

    public void Operation_11() {

        data.getPoistovne().balanceTree();
        data.getNemocnice().balanceTree();
        data.getPacienti().balanceTree();
        data.getHospitalizacie().balanceTree();

        if (data.getPoistovne().inOrder() != null)
        for (BSData<String> stringBSData : data.getPoistovne().inOrder()) {
            Poistovna poistovna = (Poistovna) stringBSData;
            poistovna.balance();
        }

        if (data.getNemocnice().inOrder() != null)
        for (BSData<String> stringBSData : data.getNemocnice().inOrder()) {
            Nemocnica nemocnica = (Nemocnica) stringBSData;
            nemocnica.balance();
        }

        if (data.getPacienti().inOrder() != null)
        for (BSData<String> stringBSData : data.getPacienti().inOrder()) {
            Pacient pacient = (Pacient) stringBSData;
            pacient.balance();
        }
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
