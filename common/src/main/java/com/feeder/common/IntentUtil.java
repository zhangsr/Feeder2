package com.feeder.common;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

/**
 * @description:
 * @author: Match
 * @date: 3/2/17
 */

public class IntentUtil {

    public static void openUrl(Activity activity, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ExceptionHandler.silentHandle(e);
        }
    }
}
