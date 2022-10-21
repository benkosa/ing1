package org.example.Shared;

import java.util.Date;

public class Comparators {
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
}
