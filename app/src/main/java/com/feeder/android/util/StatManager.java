package com.feeder.android.util;

import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;

import java.io.File;
import java.io.FileNotFoundException;

import me.zsr.feeder.BuildConfig;

/**
 * @description:
 * @author: Match
 * @date: 11/20/16
 */

public class StatManager {
    public static final String EVENT_SET_HTML_ERROR = "set_html_error";
    public static final String EVENT_IMPORT_OPML_CLICK = "import_opml_click";
    public static final String EVENT_IMPORT_OPML_GET_FILE = "import_opml_get_file";
    public static final String EVENT_IMPORT_OPML_SUCCESS = "import_opml_success";
    public static final String EVENT_IMPORT_OPML_FAILED = "import_opml_failed";
    public static final String EVENT_MENU_SEARCH_CLICK = "menu_search_click";
    public static final String EVENT_SEARCH_RESULT_CLICK = "search_result_click";
    public static final String EVENT_CUSTOM_SOURCE_CLICK = "custom_source_click";
    public static final String EVENT_CUSTOM_SOURCE_ADD_SUCCESS = "custom_source_add_success";
    public static final String EVENT_CUSTOM_SOURCE_ADD_FAILED = "custom_source_add_failed";
    public static final String EVENT_MENU_SHARE_CLICK = "menu_share_click";
    public static final String EVENT_SHARE_ITEM_CLICK = "share_item_click";
    public static final String EVENT_SHARE_SUCCESS = "share_success";
    public static final String EVENT_SHARE_FAILED = "share_failed";
    public static final String EVENT_SHARE_CANCEl = "share_cancel";
    public static final String EVENT_ENTER_LIST = "enter_list";

    public static final String TAG_SHARE_WECHAT = "share_wechat";
    public static final String TAG_SHARE_MOMENT = "share_moment";
    public static final String TAG_SHARE_WEIBO = "share_weibo";
    public static final String TAG_SHARE_INSTAPAPER = "share_instapaper";
    public static final String TAG_SHARE_POCKET = "share_pocket";
    public static final String TAG_SHARE_EVERNOTE = "share_evernote";
    public static final String TAG_SHARE_OTHERS = "share_others";

    public static void init(Context context) {
        if (BuildConfig.DEBUG) {
            AVAnalytics.setAnalyticsEnabled(false);
            AVAnalytics.enableCrashReport(context, false);
        } else {
            AVOSCloud.initialize(context, BuildConfig.AVOS_APP_ID, BuildConfig.AVOS_CLIENT_KEY);
            AVAnalytics.setAppChannel(BuildConfig.LEANCLOUD_CHANNEL);
        }
    }

    public static void trackAppOpened(Intent intent) {
        if (!BuildConfig.DEBUG) {
            AVAnalytics.trackAppOpened(intent);
        }
    }

    public static void statEvent(Context context, String event, String tag) {
        if (!BuildConfig.DEBUG) {
            AVAnalytics.onEvent(context, event, tag);
        }
    }

    public static void statEvent(Context context, String event) {
        if (!BuildConfig.DEBUG) {
            AVAnalytics.onEvent(context, event);
        }
    }

    public static void onPause(Context context) {
        if (!BuildConfig.DEBUG) {
            AVAnalytics.onPause(context);
        }
    }

    public static void onResume(Context context) {
        if (!BuildConfig.DEBUG) {
            AVAnalytics.onResume(context);
        }
    }

    public static void uploadFile(String mark, File file) {
        if (BuildConfig.DEBUG) {
            return;
        }
        try {
            AVFile avFile = AVFile.withAbsoluteLocalPath(mark + "_" + file.getName(), file.getAbsolutePath());
            if (avFile.getSize() < 512 * 1024) {
                avFile.saveInBackground();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
