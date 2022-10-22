package org.example.Shared;

import org.example.Opetations.Data.Pacient;

import java.util.Date;

public class Comparators {
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
}
