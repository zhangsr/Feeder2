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
    /**
     * @see <a href="http://www.ietf.org/rfc/rfc0822.txt">RFC 822</a>
     */
    private static final SimpleDateFormat RFC822 = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss Z", java.util.Locale.ENGLISH);

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

    /**
     * Parses string as an RFC 822 date/time.
     */
    public static Date parseRfc822(String date) {
        try {
            return RFC822.parse(date);
        } catch (ParseException e) {
            // TODO: 8/6/15 Handle other date format !
            LogUtil.e(e.getMessage());
            return new Date();
        }
    }
}
