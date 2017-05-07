package com.feeder.domain;

import junit.framework.Assert;

/**
 * @description:
 * @author: Match
 * @date: 22/04/2017
 */

public class AssertManager {
    private static boolean mIsEnabled = false;

    public static void setEnabled(boolean enabled) {
        mIsEnabled = enabled;
    }

    public static void fail(String msg) {
        if (!mIsEnabled) {
            return;
        }
        Assert.fail(msg);
    }
}
