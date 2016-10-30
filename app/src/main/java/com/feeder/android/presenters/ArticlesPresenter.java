package com.feeder.android.presenters;

import com.feeder.android.mvp.IArticlesView;
import com.feeder.android.mvp.MVPPresenter;
import com.feeder.domain.ArticleController;
import com.feeder.domain.DataObserver;
import com.feeder.domain.ResponseState;

/**
 * @description:
 * @author: Match
 * @date: 10/25/16
 */

public class ArticlesPresenter implements MVPPresenter, DataObserver {
    private IArticlesView mArticlesView;
    private long mSubscriptionId;

    public ArticlesPresenter(IArticlesView articlesView, long subscriptionId) {
        mArticlesView = articlesView;
        mSubscriptionId = subscriptionId;
    }

    @Override
    public void onCreate() {
        mArticlesView.showLoading();
        mArticlesView.setDataSource(ArticleController.getInstance().getDataSource(mSubscriptionId));
    }

    @Override
    public void onStart() {
        ArticleController.getInstance().registerObserver(this);
        ArticleController.getInstance().requestData(mSubscriptionId);
        ArticleController.getInstance().requestNetwork(mSubscriptionId);
    }

    @Override
    public void onStop() {
        ArticleController.getInstance().unRegisterObserver(this);
    }

    @Override
    public void onDataResponse(ResponseState state) {
        switch (state) {
            case SUCCESS:
                mArticlesView.hideLoading();
                mArticlesView.notifyDataChanged();
                break;
            case NO_CHANGE:
                mArticlesView.hideLoading();
                break;
        }
    }
}
