package com.gitlab.amirmehdi.util;

import java.util.Calendar;

public class MarketTimeUtil {

    public static boolean isMarketOpen() {
        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY
            || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
            return false;

        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, 12);
        end.set(Calendar.MINUTE, 30);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 500);

        Calendar open = Calendar.getInstance();
        open.set(Calendar.HOUR_OF_DAY, 8);
        open.set(Calendar.MINUTE, 29);
        open.set(Calendar.SECOND, 0);
        open.set(Calendar.MILLISECOND, 500);

        return calendar.before(end) && calendar.after(open);
    }
}
