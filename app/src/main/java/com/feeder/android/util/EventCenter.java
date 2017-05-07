package com.feeder.android.util;

import org.greenrobot.eventbus.EventBus;

/**
 * @description:
 * @author: Match
 * @date: 24/04/2017
 */

public class EventCenter {

    public static void post(Object obj) {
        EventBus.getDefault().post(obj);
    }

    public static void register(Object obj) {
        EventBus.getDefault().register(obj);
    }

    public static void unregister(Object obj) {
        EventBus.getDefault().unregister(obj);
    }
}
