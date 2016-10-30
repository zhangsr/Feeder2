package com.feeder.android.presenters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.feeder.android.mvp.ISubscriptionsView;
import com.feeder.android.mvp.MVPPresenter;
import com.feeder.android.utils.Constants;
import com.feeder.android.mvp.SubscriptionViewObserver;
import com.feeder.android.views.ArticleListActivity;
import com.feeder.domain.DataObserver;
import com.feeder.domain.ResponseState;
import com.feeder.domain.SubscriptionController;
import com.feeder.model.Subscription;

/**
 * @description:
 * @author: Match
 * @date: 8/4/16
 */
public class SubscriptionsPresenter implements MVPPresenter, DataObserver, SubscriptionViewObserver {
    private ISubscriptionsView mSubscriptionView;
    private Context mContext;

    public SubscriptionsPresenter(Context context, ISubscriptionsView subscriptionsView) {
        mContext = context;
        mSubscriptionView = subscriptionsView;
        mSubscriptionView.setObserver(this);
    }

    @Override
    public void onCreate() {
        mSubscriptionView.showLoading();
        mSubscriptionView.setDataSource(SubscriptionController.getInstance().getDataSource());
    }

    @Override
    public void onStart() {
        SubscriptionController.getInstance().registerObserver(this);
        SubscriptionController.getInstance().requestData();
    }

    @Override
    public void onStop() {
        SubscriptionController.getInstance().unRegisterObserver(this);
    }

    @Override
    public void onDataResponse(ResponseState state) {
        switch (state) {
            case SUCCESS:
                mSubscriptionView.hideLoading();
                mSubscriptionView.notifyDataChanged();
                break;
            case NO_CHANGE:
                mSubscriptionView.hideLoading();
                break;
        }
    }

    @Override
    public void onItemClick(View view, Subscription data) {
        mSubscriptionView.showToast(data.getTitle() + " clicked");
        Intent intent = new Intent(mContext, ArticleListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.KEY_BUNDLE_SUBSCRIPTION_ID, data.getId());
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}
