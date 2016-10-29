package com.feeder.android.utils;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 15-7-8
 */
public class DateUtil {
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    /* Hide constructor */
    private DateUtil() {}

    // TODO: 15-7-9 support en
    public static CharSequence formatDate(Context context, Long time) {
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date yestoday = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date beforeYestoday = calendar.getTime();
        if (isSameDay(date, today)) {
            return context.getResources().getString(R.string.today);
        } else if (isSameDay(date, yestoday)) {
            return context.getResources().getString(R.string.yestoday);
        } else if (isSameDay(date, beforeYestoday)) {
            return context.getResources().getString(R.string.before_yestoday);
        } else {
            return DateFormat.format("yyyy年MM月dd日 EEEE", date);
        }
    }

    public static CharSequence formatTime(Long time) {
        return TIME_FORMAT.format(new Date(time));
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
