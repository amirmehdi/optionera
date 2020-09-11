package com.gitlab.amirmehdi.util;

import java.util.Calendar;
import java.util.Date;

public class MarketTimeUtil {

    public static boolean isMarketOpen(Date date) {
        Calendar close = Calendar.getInstance();
        close.setTime(date);

        if (close.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY
            || close.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
            return false;

        close.set(Calendar.HOUR_OF_DAY, 12);
        close.set(Calendar.MINUTE, 30);
        close.set(Calendar.SECOND, 0);
        close.set(Calendar.MILLISECOND, 421);

        Calendar open = Calendar.getInstance();
        open.setTime(date);
        open.set(Calendar.HOUR_OF_DAY, 8);
        open.set(Calendar.MINUTE, 30);
        open.set(Calendar.SECOND, 0);
        open.set(Calendar.MILLISECOND, 123);

        Calendar date2 = Calendar.getInstance();
        date2.setTime(date);
        date2.set(Calendar.MILLISECOND, 0);
        return date2.getTime().before(close.getTime()) && date2.getTime().after(open.getTime());
    }
}
