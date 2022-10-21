package org.example.Shared;

public class Comparators {
    public Compare stringCompare(String s1, String s2) {
        int compareResult = s1.compareTo(s2);
        if (compareResult < 0) return Compare.LESS;
        if (compareResult > 0) return Compare.MORE;
        return Compare.EQUAL;
    }
}
