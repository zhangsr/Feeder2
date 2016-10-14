package com.feeder.common;

import android.net.UrlQuerySanitizer;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @description:
 * @author: Match
 * @date: 8/11/16
 */
public class URLUtil {

    public static String getHost(String url) {
        try {
            URL urlObj = new URL(url);
            return urlObj.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getParam(String url, String key) {
        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
        return sanitizer.getValue(key);
    }
}
