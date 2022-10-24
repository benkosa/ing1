package org.example.Shared;

import org.example.Opetations.Data.Hospitalizacia;
import org.example.Opetations.Data.Pacient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Comparators {

    private SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy");

    public Date getMaxDate() {
        Date maxDate = new Date();
        try {
            maxDate = formatter2.parse("01-01-9999");
        } catch (ParseException e) { }

        return maxDate;
    }

    public Date getMaxDateOd() {
        Date maxDate = new Date();
        try {
            maxDate = formatter2.parse("01-01-9998");
        } catch (ParseException e) { }

        return maxDate;
    }
    public Date getMaxDateDo() {
        Date maxDate = new Date();
        try {
            maxDate = formatter2.parse("01-01-99999");
        } catch (ParseException e) { }

        return maxDate;
    }

    public Date getMinDate() {
        Date maxDate = new Date();
        try {
            maxDate = formatter2.parse("01-01-0001");
        } catch (ParseException e) { }

        return maxDate;
    }

    public Compare intCompare(int s1, int s2) {
        if (s1 < s2) return Compare.LESS;
        if (s1 > s2) return Compare.MORE;
        return Compare.EQUAL;
    }
    public Compare stringCompare(String s1, String s2) {
        int compareResult = s1.compareTo(s2);
        if (compareResult < 0) return Compare.LESS;
        if (compareResult > 0) return Compare.MORE;
        return Compare.EQUAL;
    }

    public Compare dateCompare(Date d1, Date d2) {
        int compareResult = d1.compareTo(d2);
        if (compareResult < 0) return Compare.LESS;
        if (compareResult > 0) return Compare.MORE;
        return Compare.EQUAL;
    }
    public Compare pacientCompare(Pacient p1, Pacient p2) {
        int compareNameResult = p1.getMeno().compareTo(p2.getMeno());
        int compareSurnameResult = p1.getPriezvisko().compareTo(p2.getPriezvisko());
        int compareRCResult = p1.getRodneCislo().compareTo(p2.getRodneCislo());

        if (compareNameResult < 0) return Compare.LESS;
        if (compareNameResult > 0) return Compare.MORE;
        if (compareNameResult == 0) {
            if (compareSurnameResult < 0) return Compare.LESS;
            if (compareSurnameResult > 0) return Compare.MORE;
            if (compareSurnameResult == 0) {
                if (compareRCResult < 0) return Compare.LESS;
                if (compareRCResult > 0) return Compare.MORE;
                if (compareRCResult == 0) {
                    return Compare.EQUAL;
                }
            }
        }

        return Compare.EQUAL;
    }

    public Compare hospDateCompare(Hospitalizacia h1, Hospitalizacia h2) {
        Date h1Do = h1.getKoniecHosp() == null ? getMaxDate() : h1.getKoniecHosp();
        Date h2Do = h2.getKoniecHosp() == null ? getMaxDate() : h2.getKoniecHosp();

        int compareDo = h1Do.compareTo(h2Do);
        if (compareDo < 0) return Compare.LESS;
        if (compareDo > 0) return Compare.MORE;
        if (compareDo == 0) {
            int compareOd = h1.getZaciatokHosp().compareTo(h2.getZaciatokHosp());
            if (compareOd < 0) return Compare.LESS;
            if (compareOd > 0) return Compare.MORE;
            return Compare.EQUAL;
        }

        return Compare.EQUAL;
    }
}
