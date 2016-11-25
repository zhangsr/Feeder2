package com.feeder.android.base;

import android.content.Context;

import com.feeder.android.view.main.Category;

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

    public abstract void setDataSource(List<Category> categoryList);

    public void setObserver(SubscriptionViewObserver observer) {
        mObserver = observer;
    }
}
