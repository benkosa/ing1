package org.main.app;


import org.main.shared.DateFormat;
import org.main.shared.Response;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Operations {

    private Date actualDate = new Date();

    Data data;
    public Response opStart (String fileName, String blockFacktor, String blockNumber) {

        int blockFacktorInt = blockFacktor.isEmpty() ? 0 : Integer.parseInt(blockFacktor);
        int blockNumberInt = blockNumber.isEmpty() ? 0 : Integer.parseInt(blockNumber);

        if (blockFacktorInt != 0 && blockNumberInt != 0) {
            data = new Data(fileName, blockFacktorInt, blockNumberInt);
            return new Response(0, "success", null);
        }

        if (blockFacktorInt == 0 && blockNumberInt == 0) {
            data = new Data(fileName);
            return new Response(0, "success", null);
        }


        return new Response(1, "wrong input", null);
    }
    /**
     * 1. Vyhľadanie záznamov pacienta (identifikovaný svojím rodným číslom).
     * Po nájdení pacienta je potrebné zobraziť všetky evidované údaje vrátane
     * všetkých hospitalizácií.
     */
    Response op1(String rodneCislo) {

        Pacient pacient = data.getPacient(
                new Pacient("", "", rodneCislo, 0, new Date())
        );
        if (pacient == null) {
            return new Response<>(647, "pacient neexistuje", null);
        }

        ArrayList<Hospitalizacia> hospitalizacie = pacient.getHospitalizacie();


        String tableValues[][] = new String[hospitalizacie.size()][9];

        for (int i = 0; i < hospitalizacie.size(); i++) {
            Hospitalizacia hosp = hospitalizacie.get(i);
            tableValues[i] = new String[]{
                    pacient.getMeno(),
                    pacient.getPriezvisko(),
                    pacient.getRodneCislo(),
                    pacient.getDatumNarodenia().toString(),
                    pacient.getPoistovna()+"",
                    hosp.getIdHospitalizacie()+"",
                    hosp.getDatumZaciatku().toString(),
                    hosp.getDatumKonca() == null ? "" : hosp.getDatumKonca().toString(),
                    hosp.getDiagnoza()
            };
        }

        // nema hospitalizacie zobrazi len pacienta
        if (tableValues.length == 0) {
            tableValues = new String[1][9];
            tableValues[0] = new String[]{
                    pacient.getMeno(),
                    pacient.getPriezvisko(),
                    pacient.getRodneCislo(),
                    pacient.getDatumNarodenia().toString(),
                    pacient.getPoistovna()+"",
                    "",
                    "",
                    "",
                    ""
            };
        }

        return new Response<>(0, "success", tableValues);
    }
    /**
     * 2. Vyhľadanie hospitalizácie (definovaná id hospitalizácie) pre pacienta (definovaný
     * rodným číslom) azobrazenie všetkých údajov o nej. (Kľúč, ktorý pôjde do
     */
    Response op2(String rodneCislo, int idHospitalizacie) {

        Pacient pacient = data.getPacient(
                new Pacient("", "", rodneCislo, 0, new Date())
        );
        if (pacient == null) {
            return new Response<>(647, "pacient neexistuje", null);
        }

        ArrayList<Hospitalizacia> hospitalizacie = pacient.getHospitalizacie();

        String tableValues[][] = new String[hospitalizacie.size()][9];

        int j = 0;
        for (int i = 0; i < hospitalizacie.size(); i++) {
            Hospitalizacia hosp = hospitalizacie.get(i);
            if (hosp.getIdHospitalizacie() == idHospitalizacie) {
                tableValues[j] = new String[]{
                        pacient.getMeno(),
                        pacient.getPriezvisko(),
                        pacient.getRodneCislo(),
                        pacient.getDatumNarodenia().toString(),
                        pacient.getPoistovna() + "",
                        hosp.getIdHospitalizacie() + "",
                        hosp.getDatumZaciatku().toString(),
                        hosp.getDatumKonca() == null ? "" : hosp.getDatumKonca().toString(),
                        hosp.getDiagnoza()
                };
                j++;
            }
        }

        return new Response<>(0, "success", tableValues);
    }
    /**
     * 3. Vykonanie záznamu o začiatku hospitalizácie pacienta (identifikovaný svojím rodným
     * číslom).
     */
    Response op3(String rodneCislo, int idHosp, Date zaciatok, Date koniec, String diagonza) {
        //najdenie pacient
        Pacient pacient = data.getPacient(
                new Pacient("", "", rodneCislo, 0, new Date())
        );
        if (pacient == null) {
            return new Response<>(647, "pacient neexistuje", null);
        }

        // pridat hosp
        ArrayList<Hospitalizacia> hospitalizacie = pacient.getHospitalizacie();
        if (hospitalizacie.size() >= pacient.getHospitalizacieMax()) {
            return new Response<>(647, "bol dosiahnuty max pocet zazanmov hospitalizacii", null);
        }

        Hospitalizacia newHosp = new Hospitalizacia(idHosp, zaciatok, koniec, diagonza);

        for (Hospitalizacia hospitalizacia : hospitalizacie) {
            if (hospitalizacia.getIdHospitalizacie() == newHosp.getIdHospitalizacie()) {
                return new Response<>(647, "Hospitalizacia uz existuje", null);
            }
        }

        hospitalizacie.add(newHosp);

        //zmazanie pacienta
        data.removePacient(pacient);
        //pridanie pacienta
        data.addPacient(pacient);
        return new Response<>(0, "Hospitalizacia pridana", null);
    }
    /**
     * 4. Vykonanie záznamu o ukončení hospitalizácie pacienta (identifikovaný svojím rodným
     * číslom).
     */
    Response op4(String rodneCislo, int idHosp) {

        //najdenie pacient
        Pacient pacient = data.getPacient(
                new Pacient("", "", rodneCislo, 0, new Date())
        );
        if (pacient == null) {
            return new Response<>(647, "pacient neexistuje", null);
        }

        // najdenie hosp
        ArrayList<Hospitalizacia> hospitalizacie = pacient.getHospitalizacie();
        if (hospitalizacie.size() >= pacient.getHospitalizacieMax()) {
            return new Response<>(647, "bol dosiahnuty max pocet zazanmov hospitalizacii", null);
        }

        Hospitalizacia newHosp = new Hospitalizacia(idHosp, null, null, "");

        for (Hospitalizacia hospitalizacia : hospitalizacie) {
            if (hospitalizacia.myEqual(newHosp)) {
                //  TODO APP TIME
                hospitalizacia.ukonci(new Date());
                //zmazanie pacienta
                data.removePacient(pacient);
                //pridanie pacienta
                data.addPacient(pacient);
                return new Response<>(0, "Hospitalizacia ukoncena", null);
            }
        }
        return new Response<>(4, "Hospitalizacia neexistuje", null);
    }
    /**
     * 5. Pridanie pacienta.
     */
    Response op5(String meno, String priezvisko, String rodneCislo, int poistovna, Date datumNarodenia) {
        Pacient pacient = new Pacient(meno, priezvisko, rodneCislo, poistovna, datumNarodenia);
        if (data.addPacient(pacient)) {
            return new Response(0, "pacient pridany", null);
        } else {
            return new Response(2, "pacient uz existuje", null);
        }


    }
    /**
     * 6. Vymazanie hospitalizácie (definovaná id hospitalizácie) pre pacienta (definovaný
     * rodným číslom).
     */
    Response op6(String rodneCislo, int idHosp) {

        //najdenie pacient
        Pacient pacient = data.getPacient(
                new Pacient("", "", rodneCislo, 0, new Date())
        );
        if (pacient == null) {
            return new Response<>(647, "pacient neexistuje", null);
        }

        // najdenie hosp
        ArrayList<Hospitalizacia> hospitalizacie = pacient.getHospitalizacie();
        if (hospitalizacie.size() >= pacient.getHospitalizacieMax()) {
            return new Response<>(647, "bol dosiahnuty max pocet zazanmov hospitalizacii", null);
        }

        Hospitalizacia newHosp = new Hospitalizacia(idHosp, null, null, "");

        for (int i = 0; i < hospitalizacie.size(); i++) {
            Hospitalizacia hospitalizacia = hospitalizacie.get(i);
            if (hospitalizacia.myEqual(newHosp)) {
                //  TODO APP TIME
                hospitalizacie.remove(i);
                //zmazanie pacienta
                data.removePacient(pacient);
                //pridanie pacienta
                data.addPacient(pacient);
                return new Response<>(0, "Hospitalizacia vymazana", null);
            }
        }
        return new Response<>(4, "Hospitalizacia neexistuje", null);
    }
    /**
     * 7. Vymazanie pacienta.
     */
    Response op7(String rodneCislo) {

        Pacient pacient = new Pacient("", "", rodneCislo, 0, new Date());
        if (data.removePacient(pacient)) {
            return new Response(0, "pacient zmazany", null);
        } else {
            return new Response(2, "pacient neexistuje", null);
        }
    }

    /**
     * https://stackoverflow.com/questions/8708342/redirect-console-output-to-string-in-java
     * @return
     */
    Response opDisplatEverithing() {
        // Create a stream to hold the output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        // IMPORTANT: Save the old System.out!
        PrintStream old = System.out;
        // Tell Java to use your special stream
        System.setOut(ps);
        // Print some output: goes to your special stream

        data.hashing.readWholeFileNoValid();

        // Put things back
        System.out.flush();
        System.setOut(old);
        // Show what happened
        //System.out.println("Here: " + baos.toString());

        return new Response(0, "", baos.toString());

    }

    public Response setDate(String time) {
        DateFormat df = new DateFormat();
        Date date = df.stringToDate( time);
        if (date == null) {
            return new Response(3, "zly format datumu", null);
        }
        this.actualDate = date;

        return new Response(0, "cas nastaveny", null);
    }

    public String getDate() {
        DateFormat df = new DateFormat();
        return df.dateToString(this.actualDate);
    }

    public Response generate(int pocetPacientov, int pocetHosp, Date datumOd, Date datumDo) {
        DateFormat df = new DateFormat();
        ArrayList<Pacient> insertedPacients = new ArrayList<>();

        Random rand = new Random();

        for (int i = 0; i < pocetPacientov; i++) {
            Pacient pacient = new Pacient("meno_"+i, "priezvisko_"+i, rand.nextInt(Integer.MAX_VALUE)+"", i, df.between(new Date(0), new Date()));
            if (data.addPacient(pacient)) {
                insertedPacients.add(pacient);
            }
        }

        int countAddedHosp = 0;

        for (int i = 0; i < pocetHosp; i++) {
            Pacient pacient = insertedPacients.get(rand.nextInt(insertedPacients.size()));
            Date startDate = df.between(datumOd, datumDo);
            Date endDate = df.addDATE(startDate, Calendar.DATE, rand.nextInt(60)+1);
            if (endDate.compareTo(datumDo) > 0) {
                endDate = null;
            }
            if(op3(pacient.getRodneCislo(), i, startDate, endDate, "diagnoza_"+i).code == 0) {
                countAddedHosp++;
            }

        }

        String tableValues[][] = new String[insertedPacients.size()][5];

        for (int i = 0; i < insertedPacients.size(); i++) {
            Pacient pacient = insertedPacients.get(i);
            tableValues[i] = new String[]{
                    pacient.getMeno(),
                    pacient.getPriezvisko(),
                    pacient.getRodneCislo(),
                    pacient.getDatumNarodenia().toString(),
                    pacient.getPoistovna()+""
            };
        }

        return new Response(
                0,
                "prodanych pacientov: "+insertedPacients.size()+"\npridanych hospitalizacii: "+countAddedHosp,
                tableValues
        );
    }

}
