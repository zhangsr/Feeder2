package com.feeder.android.mvp;

import android.view.View;

import com.feeder.model.Subscription;

/**
 * @description:
 * @author: Match
 * @date: 10/23/16
 */

// TODO: 10/23/16 check this architecture (past data from view to presenter)
public interface SubscriptionViewObserver {
    void onItemClick(View view, Subscription data);
    boolean onItemLongClick(View view, Subscription data);
}
