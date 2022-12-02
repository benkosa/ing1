package org.main.bst;

import java.text.SimpleDateFormat;

public class Comparators {

    private SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy");

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
}
