package net.cloudapp.chooser.chooser.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Ben on 18/10/2016.
 */
public abstract class DateConverter {

    public static GregorianCalendar utcToCalendar (String utcDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        GregorianCalendar gc = new GregorianCalendar();
        try {
            gc.setTime(sdf.parse(utcDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gc;
    }

    public static GregorianCalendar applyTimeZone (GregorianCalendar calendar) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        calendar.setTimeZone(tz);
        return calendar;
    }

    public static String utcToShortDate (String utcDate) {
        // returns: dd.mm.yyyy
        GregorianCalendar date = applyTimeZone(utcToCalendar(utcDate));
        return date.get(Calendar.DAY_OF_MONTH) + "." + (date.get(Calendar.MONTH)+1) + "." + date.get(Calendar.YEAR);
    }


    public static String timeDiffFromNow (GregorianCalendar earlyDate) {
        //Compares UTC timezone!
        return timeDiff(earlyDate,(GregorianCalendar) GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC")));
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
