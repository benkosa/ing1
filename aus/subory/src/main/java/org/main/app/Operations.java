package org.main.app;


import org.main.shared.Response;

import java.util.ArrayList;
import java.util.Date;

public class Operations {

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
                    hosp.getDatumKonca().toString(),
                    hosp.getDiagnoza()
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

        for (int i = 0; i < hospitalizacie.size(); i++) {
            Hospitalizacia hosp = hospitalizacie.get(i);
            if (hosp.getIdHospitalizacie() == idHospitalizacie) {
                tableValues[i] = new String[]{
                        pacient.getMeno(),
                        pacient.getPriezvisko(),
                        pacient.getRodneCislo(),
                        pacient.getDatumNarodenia().toString(),
                        pacient.getPoistovna() + "",
                        hosp.getIdHospitalizacie() + "",
                        hosp.getDatumZaciatku().toString(),
                        hosp.getDatumKonca().toString(),
                        hosp.getDiagnoza()
                };
            }
        }

        return new Response<>(0, "success", tableValues);
    }
    /**
     * 3. Vykonanie záznamu o začiatku hospitalizácie pacienta (identifikovaný svojím rodným
     * číslom).
     */
    Response op3(String rodneCislo, int idHosp, Date zaciatok, String diagonza) {
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

        Hospitalizacia newHosp = new Hospitalizacia(idHosp, zaciatok, zaciatok, diagonza);

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

}
