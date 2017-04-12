package com.feeder.domain;

import com.feeder.common.ThreadManager;
import com.feeder.model.Subscription;

import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 12/18/16
 */

public class RefreshManager {

    private static RefreshManager sRefreshManager;

    public static RefreshManager getInstance() {
        if (sRefreshManager == null) {
            sRefreshManager = new RefreshManager();
        }
        return sRefreshManager;
    }

    public void refresh(Subscription... subscriptions) {
        refresh(Arrays.asList(subscriptions));
    }

    public void refresh(List<Subscription> subscriptions) {
        for (Subscription subscription : subscriptions) {
            ArticleModel.getInstance().requestNetwork(subscription);
        }
    }

    public void refreshAll(long delay) {
        ThreadManager.postDelay(new Runnable() {
            @Override
            public void run() {
                refresh(SubscriptionModel.getInstance().getDataSource());
            }
        }, delay);
    }
}
