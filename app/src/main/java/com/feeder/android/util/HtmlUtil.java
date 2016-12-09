package com.feeder.android.util;

import com.google.common.base.Strings;

import org.jsoup.Jsoup;

/**
 * @description:
 * @author: Match
 * @date: 12/9/16
 */

public class HtmlUtil {

    public static String getOptimizedDesc(String originDesc) {
        // Shrink string to optimize render time
        String result = "";
        if (Strings.isNullOrEmpty(originDesc)) {
            return result;
        }
        String parsedStr = Jsoup.parse(originDesc).text();
        int showLength = parsedStr.length() < 50 ? parsedStr.length() : 50;
        if (showLength > 0) {
            result = parsedStr.substring(0, showLength - 1);
        }
        return result;
    }
}
