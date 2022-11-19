package org.main.shared;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class DateFormat {


    final static String PATTERN = "dd-MM-yyyy";

    public SimpleDateFormat format=new SimpleDateFormat(PATTERN);

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


    //https://www.baeldung.com/java-random-dates
    public Date between(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }

    public Date formatToDate(String date) {
        try {
            return this.format.parse(date);
        } catch (ParseException ee) {
            return null;
        }
    }

    // https://stackoverflow.com/questions/5683728/convert-java-util-date-to-string
    public String dateToString(Date date) {
        return format.format(date);
    }
}
