package com.feeder.common;

/**
 * @description:
 * @author: Match
 * @date: 11/20/16
 */

public class NumberUtil {

    public static boolean equals(Long long1, Long long2) {
        if (long1 == null && long2 == null) {
            return true;
        }

        if (long1 == null || long2 == null) {
            return false;
        }

        return long1.equals(long2);
    }
}
