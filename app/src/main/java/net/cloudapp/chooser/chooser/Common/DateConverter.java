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
}
