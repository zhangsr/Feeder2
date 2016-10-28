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

    public ArticlesPresenter(IArticlesView articlesView) {
        mArticlesView = articlesView;
    }

    @Override
    public void onCreate() {
        mArticlesView.showLoading();
        mArticlesView.setDataSource(ArticleController.getInstance().getDataSource());
    }

    @Override
    public void onStart() {
        ArticleController.getInstance().registerObserver(this);
        ArticleController.getInstance().requestUpdate();
    }

    @Override
    public void onStop() {
        ArticleController.getInstance().unRegisterObserver(this);
    }

    @Override
    public void onResponse(ResponseState state) {
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
