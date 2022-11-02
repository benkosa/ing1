package org.example.Opetations;

import org.example.BSData;
import org.example.BSTree;
import org.example.Opetations.Data.*;
import org.example.Shared.Response;

import java.io.*;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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
    public static Date addDATE(Date date, int unit, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(unit, days); //minus number would decrement the days
        return cal.getTime();
    }

    private SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");

    //https://www.baeldung.com/java-random-dates
    public Date between(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
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
    public Response<String[][]> Operation_1(String rcPacient, String nazovNemocnice) {
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

        ArrayList<Hospitalizacia> hospitalizacias = pacient.getHospotalizacie(nazovNemocnice);

        String tableValues[][] = new String[hospitalizacias.size()][7];

        for (int i = 0; i < hospitalizacias.size(); i++) {
            Hospitalizacia hosp = hospitalizacias.get(i);
            tableValues[i] = new String[]{
                    hosp.getPacient().getMeno(),
                    hosp.getPacient().getPriezvisko(),
                    hosp.getPacient().getRodneCislo(),
                    hosp.getPacient().getPoistovna().key,
                    hosp.getDiagnoza(),
                    hosp.getZaciatokHospString(),
                    hosp.getKoniecHospString()
            };
        }

        return new Response(0, "", tableValues);
    }


    /**
     * vyhľadanie záznamov pacienta/ov v zadanej nemocnici (identifikovaná
     * svojím názvom) podľa mena a priezviska. Po nájdení pacienta/ov je
     * potrebné zobraziť všetky evidované údaje zo zadanej nemocnice
     * rozčlenené po pacientoch.
     */
    public Response<ArrayList<String[]>> Operation_2(String nazovNemocnice, String meno, String priezvisko) {
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

        ArrayList<BSData<Pacient>> pacienti = nemocnica
                .getPacientiMena()
                .intervalSearch(minPacient, maxPacient);

        if (pacienti == null) pacienti = new ArrayList<>();



        ArrayList<String[]> tableValues = new ArrayList<>();

        for (int i = 0; i < pacienti.size(); i++) {
            final Pacient pacient = pacienti.get(i).key;
            if (!(pacient.getMeno().equals(meno) && pacient.getPriezvisko().equals(priezvisko))) {
                continue;
            }

            final ArrayList<Hospitalizacia> hospitalizacie = pacient.getHospotalizacie(nazovNemocnice);
            if (hospitalizacie != null) {
                for (int j = 0; j < hospitalizacie.size(); j++) {
                    final Hospitalizacia hosp = hospitalizacie.get(j);
                    tableValues.add(new String[]{
                            pacient.getMeno(),
                            pacient.getPriezvisko(),
                            pacient.getRodneCislo(),
                            pacient.getDatumNarodeniaString(),
                            pacient.getPoistovna().key,
                            hosp.getDiagnoza(),
                            hosp.getZaciatokHospString(),
                            hosp.getKoniecHospString()
                    });

                }
            }
        }
        return new Response<>(0, "" , tableValues);
    }

    /**
     * vykonanie záznamu o začiatku hospitalizácie pacienta (identifikovaný
     * svojím rodným číslom) v nemocnici(identifikovaná svojím názvom)
     */
    public Response Operation_3(String rcPacienta, String nazovNemocnice, String diagnoza, Date datumHosp, Date koniecHosp) {
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
                koniecHosp,
                diagnoza,
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
     * zaciatok hospitalizacie pre nacitanie zo suboru
     */
    public Response Operation_3(long zaciatok, Long koniec, String diagnozaStr, String nemocnicaStr, String pacientStr){
        //get pacient
        Pacient pacient = (Pacient)data.getPacienti().find(pacientStr);
        if (pacient == null) {
            return new Response(1, "Pacient neexistuje", null);
        }
        //get nemocnica
        Nemocnica nemocnica = (Nemocnica)data.getNemocnice().find(nemocnicaStr);
        if (nemocnica == null) {
            return new Response(1, "Nemocnica neexistuje", null);
        }

        Date datumHosp = new Date(zaciatok);
        Date koniecHosp = null;

        if (koniec != null) {
            koniecHosp = new Date(koniec);
        }
        //vytvorit hosp
        Hospitalizacia newHosp = new Hospitalizacia(
                datumHosp,
                koniecHosp,
                diagnozaStr,
                pacient,
                nemocnica
        );
        //pridate do vsetkych
        if (data.addHospitalizacia(newHosp) == false) {
            return new Response(1, "Chyba, opakuj pridanie", null);
        }
        //pridat pacientovi
        pacient.addHosp(nemocnicaStr, newHosp);
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
    public Response<String[][]> Operation_5(String nazovNemocnice, String strOd, String strDo) {
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

        String tableValues[][] = new String[pacienti.size()][5];


        for (int i = 0; i < pacienti.size(); i++) {
            final Pacient pacient = pacienti.get(i);
            tableValues[i] = new String[]{
                    pacient.getMeno(),
                    pacient.getPriezvisko(),
                    pacient.getRodneCislo(),
                    pacient.getDatumNarodeniaString(),
                    pacient.getPoistovna().key,
            };
        }

        return new Response<>(0, "" ,tableValues);
    }
    /**
     * pridanie pacienta
     */
    public Response Operation_6(
            String rodneCislo,
            String meno,
            String priezvisko,
            String datumNarodenia,
            String kodPoistovne,
            Boolean fromFile) {

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

        if (fromFile) {
            date = new Date(Long.parseLong(datumNarodenia));
        } else {
            try {
                date = formatter2.parse(datumNarodenia);
            } catch (ParseException e) {
                return new Response<String>(1, "Zly format alebo datum", null);
            }
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

                ArrayList<Hospitalizacia> filteredHospitalizacias = new ArrayList<>();
                for (Hospitalizacia hospitalizacia : hospitalizacias) {
                    if (hospitalizacia.getKoniecHosp() == null ||
                            hospitalizacia.getKoniecHosp().compareTo(datumOd) > 0) {
                        filteredHospitalizacias.add(hospitalizacia);
                    }
                }

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
    public Response<String[][]> Operation_8(String nazovNemocnice) {
        Nemocnica nemocnica = (Nemocnica)data.getNemocnice().find(nazovNemocnice);
        if (nemocnica == null) {
            return new Response(1, "Nemocnica neexistuje", null);
        }

        ArrayList<Hospitalizacia> neukonceneHosp =nemocnica.getNeukonceneHosp();

        String tableValues[][] = new String[neukonceneHosp.size()][8];


        for (int i = 0; i < neukonceneHosp.size(); i++) {
            final Hospitalizacia hosp = neukonceneHosp.get(i);
            final Pacient pacient = hosp.getPacient();
            tableValues[i] = new String[]{
                    pacient.getMeno(),
                    pacient.getPriezvisko(),
                    pacient.getRodneCislo(),
                    pacient.getDatumNarodeniaString(),
                    pacient.getPoistovna().key,
                    hosp.getDiagnoza(),
                    hosp.getZaciatokHospString(),
                    hosp.getKoniecHospString()
            };
        }


        return new Response(0, "", tableValues);
    }

    public Response<String[][]> Operation_9(String nazovNemocnice, String kodPoistovne) {
        Nemocnica nemocnica = (Nemocnica)data.getNemocnice().find(nazovNemocnice);
        if (nemocnica == null) {
            return new Response(1, "Nemocnica neexistuje", null);
        }

        ArrayList<Hospitalizacia> neukonceneHosp = new ArrayList<>();

        nemocnica.getNeukonceneHosp().forEach(a -> {
            if (Objects.equals(a.getPacient().getPoistovna().key, kodPoistovne))
                neukonceneHosp.add(a);
        });

        String tableValues[][] = new String[neukonceneHosp.size()][7];

        for (int i = 0; i < neukonceneHosp.size(); i++) {
            Hospitalizacia hosp = neukonceneHosp.get(i);
            tableValues[i] = new String[]{
                    hosp.getPacient().getMeno(),
                    hosp.getPacient().getPriezvisko(),
                    hosp.getPacient().getRodneCislo(),
                    hosp.getPacient().getPoistovna().key,
                    hosp.getDiagnoza(),
                    hosp.getZaciatokHospString(),
                    hosp.getKoniecHospString()
            };
        }

        return new Response(0, "", tableValues);
    }

    /**
     * výpis aktuálne hospitalizovaných pacientov v nemocnici (identifikovaná
     * svojím názvom) zotriedený podľa rodných čísel, ktorý sú poistencami
     * zadanej zdravotnej poisťovne (identifikovaná svojím kódom)
     */
    public Response<String[][]> Operation_10(String nazovNemocnice, String kodPoistovne) {
        Nemocnica nemocnica = (Nemocnica)data.getNemocnice().find(nazovNemocnice);
        if (nemocnica == null) {
            return new Response(1, "Nemocnica neexistuje", null);
        }

        BSTree<Hospitalizacia> tree = new BSTree<>();

        nemocnica.getNeukonceneHosp().forEach(a -> {
            if (Objects.equals(a.getPacient().getPoistovna().key, kodPoistovne)) {
                tree.insert(new HospContainerRc(a));
            }
        });

        ArrayList<Hospitalizacia> neukonceneHosp = new ArrayList<>();
        tree.inOrder().forEach(a -> neukonceneHosp.add(a.key));


        String tableValues[][] = new String[neukonceneHosp.size()][7];

        for (int i = 0; i < neukonceneHosp.size(); i++) {
            Hospitalizacia hosp = neukonceneHosp.get(i);
            tableValues[i] = new String[]{
                    hosp.getPacient().getMeno(),
                    hosp.getPacient().getPriezvisko(),
                    hosp.getPacient().getRodneCislo(),
                    hosp.getPacient().getPoistovna().key,
                    hosp.getDiagnoza(),
                    hosp.getZaciatokHospString(),
                    hosp.getKoniecHospString()
            };
        }

        return new Response(0, "", tableValues);
    }

    public Response<String[][]> Operation_15(String nazovNemocnice, String kodPoistovne) {
        Nemocnica nemocnica = (Nemocnica)data.getNemocnice().find(nazovNemocnice);
        if (nemocnica == null) {
            return new Response(1, "Nemocnica neexistuje", null);
        }

        BSTree<Hospitalizacia> tree = new BSTree<>();

        nemocnica.getNeukonceneHosp().forEach(a -> {
            if (Objects.equals(a.getPacient().getPoistovna().key, kodPoistovne)) {
                tree.insert(new HospContainerPriezvisko(a));
            }
        });

        ArrayList<Hospitalizacia> neukonceneHosp = new ArrayList<>();
        tree.inOrder().forEach(a -> neukonceneHosp.add(a.key));

        String tableValues[][] = new String[neukonceneHosp.size()][7];

        for (int i = 0; i < neukonceneHosp.size(); i++) {
            Hospitalizacia hosp = neukonceneHosp.get(i);
            tableValues[i] = new String[]{
                    hosp.getPacient().getMeno(),
                    hosp.getPacient().getPriezvisko(),
                    hosp.getPacient().getRodneCislo(),
                    hosp.getPacient().getPoistovna().key,
                    hosp.getDiagnoza(),
                    hosp.getZaciatokHospString(),
                    hosp.getKoniecHospString()
            };
        }

        return new Response(0, "", tableValues);
    }
    /**
     * optimalizácia uloženia dát, ktorá všetky stromové štruktúry vyváži
     */
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
    public Response<String[][]> Operation_13() {

        ArrayList<BSData<String>> nemocnicas = data.getNemocnice().inOrder();


        String tableValues[][] = new String[nemocnicas.size()][1];

        for (int i = 0; i < nemocnicas.size(); i++) {
            Nemocnica nemocnica = (Nemocnica) nemocnicas.get(i);
            tableValues[i] = new String[]{
                    nemocnica.key,
            };
        }
        return new Response(0, "", tableValues);
    }

    /**
     * zrušenie nemocnice (celá agenda sa presunie do inej nemocnice, ktorú špecifikuje
     * používateľ (identifikovaná svojím názvom), vrátane pacientov a historických záznamov)
     */
    public Response Operation_14(String oldnemocnica, String newNemocnica) {
        Nemocnica nemocnicaZmazanie = (Nemocnica)data.getNemocnice().find(oldnemocnica);
        if (nemocnicaZmazanie == null) {
            return new Response(1, "Nemocnica na zmazanie neexistuje", null);
        }
        Nemocnica nemocnicaDest = (Nemocnica)data.getNemocnice().find(newNemocnica);
        if (nemocnicaZmazanie == null) {
            return new Response(1, "Nemocnica na vlozenie neexistuje", null);
        }

        // zmenit vsetkym hospitalizaciam pointer na staru nemocnicu
        for (BSData<Date> dateBSData : nemocnicaZmazanie.getHospitalizacie().inOrder()) {
            Hospitalizacia hosp = (Hospitalizacia) dateBSData;
            hosp.changeHospital(nemocnicaDest);
        }

        //migracia pacientom
        for (BSData<String> dateBSData : nemocnicaZmazanie.getPacienti().inOrder()) {
            Pacient pacient = (Pacient) dateBSData;
            pacient.merge(oldnemocnica, newNemocnica);
        }

        //migracia poistovniam
        for (BSData<String> dateBSData : nemocnicaZmazanie.getPoistovne().inOrder()) {
            Poistovna poistovna = (Poistovna) dateBSData;
            poistovna.migrate(oldnemocnica, newNemocnica);
        }

        //pridat hospitalizacie do novej nemocnice
        for (BSData<Date> dateBSData : nemocnicaZmazanie.getHospitalizacie().inOrder()) {
            Hospitalizacia hosp = (Hospitalizacia) dateBSData;
            nemocnicaDest.addHosp(hosp);
        }

        //pridat poistovne do novej nemocnice
        for (BSData<String> dateBSData : nemocnicaZmazanie.getPoistovne().inOrder()) {
            Poistovna poistovna = (Poistovna) dateBSData;
            nemocnicaDest.addPoistovna(poistovna);
        }

        //pridat pacientov do novej nemocnice
        for (BSData<String> dateBSData : nemocnicaZmazanie.getPacienti().inOrder()) {
            Pacient pacient = (Pacient) dateBSData;
            nemocnicaDest.addPacient(pacient);
        }

        data.getNemocnice().remove(oldnemocnica);


        return new Response(0, "Sucess", null);

    }

    // https://stackoverflow.com/questions/50257374/how-do-i-write-multiple-lines-to-a-text-file-in-java
    public Response Operation_saveToFile(String fileName) {
        ArrayList<String> poistovneArr = new ArrayList<>();
        for (BSData stringBSData : data.getPoistovne().levelOrder()) {
            Poistovna poistovna = (Poistovna) stringBSData;
            poistovneArr.add( poistovna.key );
        }
        saveToFile(fileName+"_poistovne", poistovneArr);

        ArrayList<String> nemocniceArr = new ArrayList<>();
        for (BSData stringBSData : data.getNemocnice().levelOrder()) {
            Nemocnica nemocnica = (Nemocnica) stringBSData;
            nemocniceArr.add(nemocnica.key);
        }
        saveToFile(fileName+"_nemocnice", nemocniceArr);

        ArrayList<String> pacientArr = new ArrayList<>();
        for (BSData stringBSData : data.getPacienti().levelOrder()) {
            Pacient pacient = (Pacient) stringBSData;
            pacientArr.add(
                    pacient.key+";"+
                    pacient.getMeno()+";"+
                    pacient.getPriezvisko()+";"+
                    (pacient.getDatumNarodenia() == null ? "" : pacient.getDatumNarodenia().getTime())+";"+
                    pacient.getPoistovna().key

            );
        }
        saveToFile(fileName+"_pacienti", pacientArr);

        ArrayList<String> hospArr = new ArrayList<>();
        for (BSData stringBSData : data.getHospitalizacie().levelOrder()) {
            Hospitalizacia hosp = (Hospitalizacia) stringBSData;
            hospArr.add(
                    hosp.getZaciatokHosp().getTime()+";"+
                            (hosp.getKoniecHosp() == null ? "" : hosp.getKoniecHosp().getTime())+";"+
                            hosp.getDiagnoza()+";"+
                            hosp.getNemocnica().key+";"+
                            hosp.getPacient().key

            );
        }
        saveToFile(fileName+"_hospitalizacie", hospArr);

        return null;
    }
    //https://www.codegrepper.com/code-examples/java/read+multiple+lines+from+file+in+java
    public Response Operation_loadFromFile(String fileName) {
        for (String s : loadFile(fileName + "_poistovne")) {
            Operation_addPoistovna(s);
        }
        for (String s : loadFile(fileName + "_nemocnice")) {
            Operation_12(s);
        }
        for (String s : loadFile(fileName + "_pacienti")) {
            String[] words = s.split(";");
            Operation_6(
                    words[0],//rc
                    words[1],//meno
                    words[2],//priezvisko
                    words[3],//datum narodenia
                    words[4],//poistovna
                    true
            );
        }
        for (String s : loadFile(fileName + "_hospitalizacie")) {
            String[] words = s.split(";");
            Operation_3(
                    Long.parseLong(words[0]),//zaciatok
                    words[1] == "" ? null : Long.parseLong(words[1]),//koniec
                    words[2],//diagnoza
                    words[3],//nemocnica
                    words[4] //pacient
            );
        }

        return null;
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

    public void generujHospitalizacie(String pocet, String odS, String doS) {
        Date date1;
        Date date2;
        Random rand = new Random();

        try {
            date1 = formatter.parse(odS);
            date2 = formatter.parse(doS);
        } catch (ParseException ee) {
            return;
        }

        int pocetHospitalizacii = Integer.parseInt(pocet);
        for (int i = 0; i < pocetHospitalizacii; i++) {
            Pacient pacient = (Pacient) getData().getPacienti().getRandomData();
            Nemocnica nemocnica = (Nemocnica) getData().getNemocnice().getRandomData();
            Date datumHospitalizacie = between(date1, date2);
            Date koniecHosp = new Date(datumHospitalizacie.getTime());
            koniecHosp = Operations.addDATE(koniecHosp, Calendar.DATE, rand.nextInt(40));
            if (koniecHosp.compareTo(date2) > 0) koniecHosp = null;
            Operation_3(pacient.key, nemocnica.key, i+"_diagnoza", datumHospitalizacie, koniecHosp);
        }
    }

    public void generujPacientov(String pocet) {
        Random gen = new Random();
        int pocetPacientov = Integer.parseInt(pocet);
        for (int i = 0; i < pocetPacientov; i++) {
            Operation_6(
                    (gen.nextInt(100000)+899999)+"/"+(gen.nextInt(1000)+8999),
                    i+"_meno",
                    i+"_priezvisko",
                    (gen.nextInt(27)+1)+"-"+
                            (gen.nextInt(11)+1)+"-"+
                            (gen.nextInt(120)+1900)
                    ,
                    getData().getPoistovne().getRandomData().key,
                    false
            );
        }
    }

    public void drop() {
        this.data = new Data();
    }

    private void saveToFile(String fileName, ArrayList<String> data) {
        String path = "save/"+fileName+".txt";
        //delete file content
        try {
            PrintWriter pw = new PrintWriter(path);
            pw.close();
        } catch(IOException e) {}

        try (FileWriter fstream = new FileWriter(path);
             BufferedWriter info = new BufferedWriter(fstream)) {
            for (String line : data) {
                info.write(String.format(line+"%n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> loadFile(String filename) {
        ArrayList<String> ret = new ArrayList<>();
        Scanner sc = null;
        try {
            File file = new File("save/"+filename + ".txt"); // java.io.File
            sc = new Scanner(file);     // java.util.Scanner
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                ret.add(line);
            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        finally {
            if (sc != null) sc.close();
        }
        return ret;
    }
}
