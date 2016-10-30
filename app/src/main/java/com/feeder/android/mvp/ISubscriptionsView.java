package com.feeder.android.mvp;

import android.content.Context;

import com.feeder.model.Subscription;

import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 8/4/16
 */
public abstract class ISubscriptionsView extends MVPView {
    protected SubscriptionViewObserver mObserver;

    public ISubscriptionsView(Context context) {
        super(context);
    }

    public abstract void setDataSource(List<Subscription> subscriptionList);

    public void setObserver(SubscriptionViewObserver observer) {
        mObserver = observer;
    }
}
