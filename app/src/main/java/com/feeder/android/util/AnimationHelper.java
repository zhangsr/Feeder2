package com.feeder.android.util;

import android.app.Activity;
import android.os.Build;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 1/30/17
 */

public class AnimationHelper {

    public static void overridePendingTransition(Activity activity) {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            activity.overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }
    }
}
