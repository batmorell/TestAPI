package org.api.utils;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static Date convertTimestampToDate(Timestamp t) {
        if (t == null)
            return null;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(t.getTime());
        return c.getTime();
    }

    public static Timestamp convertDateToTimestamp(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return new Timestamp(c.getTimeInMillis());
    }

    public static SimpleDateFormat frenchDateFormater() {
        return new SimpleDateFormat("dd/MM/yyyy");
    }

    public static SimpleDateFormat frenchDateTimeFormater() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    public static SimpleDateFormat americanDateFormater() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    public static DateFormat americanTimeFormater() {
        return new SimpleDateFormat("HH:mm");
    }

    public static SimpleDateFormat americanDateTimeFormater() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static DateFormat UTCDateTimeFormater() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        return dateFormat;
    }

    public static Timestamp dateToTimestamp(Date pDate) {
        return new Timestamp(pDate.getTime());
    }

    public static int getDaysBetweenTwoDate(Date pFirstDate, Date pSecondDate) {
        if (pFirstDate != null && pSecondDate != null) {
            return (int) ((pSecondDate.getTime() - pFirstDate.getTime()) / (1000 * 60 * 60 * 24));
        } else {
            return 0;
        }
    }

    public static Double round(Double value, int places) {
        if (value == null) {
            return null;
        }
        if (places < 0)
            throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static double safeDivide(double pDividende, double pDiviseur) {
        if (pDiviseur == 0) {
            return 0;
        } else {
            return (pDividende / pDiviseur);
        }
    }
}

