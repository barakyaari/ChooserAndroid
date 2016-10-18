package net.cloudapp.chooser.chooser.Common;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Ben on 18/10/2016.
 */
public abstract class DateConverter {

    public static GregorianCalendar utcToCalendar (String utcDate) {
        int year = Integer.valueOf(utcDate.substring(0,4));
        int month = Integer.valueOf(utcDate.substring(5,7));
        int day = Integer.valueOf(utcDate.substring(8,10));
        int hour = Integer.valueOf(utcDate.substring(11,13));
        int minute = Integer.valueOf(utcDate.substring(14,16));
        int second = Integer.valueOf(utcDate.substring(17,19));
        return new GregorianCalendar(year,month,day,hour,minute,second);
    }

    public static String utcToShortDate (String utcDate) {
        // returns: dd.mm.yyyy
        GregorianCalendar date = utcToCalendar(utcDate);
        return date.get(Calendar.DAY_OF_MONTH) + "." +date.get(Calendar.MONTH) + "." + date.get(Calendar.YEAR);
    }

    private static String timeDiff (GregorianCalendar earlyDate, GregorianCalendar laterDate) {
        int laterTime = laterDate.get(Calendar.DAY_OF_YEAR) + laterDate.get(Calendar.YEAR)*365;
        int earlyTime = earlyDate.get(Calendar.DAY_OF_YEAR) + earlyDate.get(Calendar.YEAR)*365;
        int amount = laterTime-earlyTime;
        if (amount > 730)
            return amount/365 + " years";
        if (amount >= 365)
            return "a year";
        if (amount > 60)
            return amount/30 + " months";
        if (amount >= 30)
            return "a month";

        if (amount > 2)
            return amount + " days";
        if (amount >= 1)
            return "a day";

        laterTime = laterDate.get(Calendar.MINUTE) + laterDate.get(Calendar.HOUR)*60;
        earlyTime = earlyDate.get(Calendar.MINUTE) + earlyDate.get(Calendar.HOUR)*60;
        amount = laterTime-earlyTime;
        if (amount > 120)
            return amount/60 + " hours";
        if (amount >= 60)
            return "an hour";
        if (amount > 1)
            return amount + " minutes";
        if (amount == 1)
            return "a minute";
        return "less than a minute";
    }
}
