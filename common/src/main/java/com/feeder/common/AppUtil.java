package com.feeder.common;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * @description:
 * @author: Match
 * @date: 12/24/16
 */

public class AppUtil {

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }
}
