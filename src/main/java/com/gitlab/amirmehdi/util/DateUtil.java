package com.gitlab.amirmehdi.util;

import com.github.eloyzone.jalalicalendar.DateConverter;
import com.github.eloyzone.jalalicalendar.JalaliDate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author H.Motallebpour
 */
public class DateUtil {

    public static Date jalaliToGregorian(int year, int month, int day) {
        JalaliDate jalaliDate = new JalaliDate(year, month, day);
        DateConverter dateConverter = new DateConverter();
        return Date.from(dateConverter.jalaliToGregorian(jalaliDate).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
