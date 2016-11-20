package com.feeder.android.presenters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.feeder.android.mvp.IArticlesView;
import com.feeder.android.mvp.MVPPresenter;
import com.feeder.android.mvp.ArticleViewObserver;
import com.feeder.android.utils.Constants;
import com.feeder.android.views.ArticleActivity;
import com.feeder.domain.ArticleController;
import com.feeder.domain.DataObserver;
import com.feeder.domain.DataType;
import com.feeder.domain.ResponseState;
import com.feeder.model.Article;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 10/25/16
 */

public class ArticlesPresenter implements MVPPresenter, DataObserver, ArticleViewObserver {
    private Activity mActivity;
    private IArticlesView mArticlesView;
    private long mSubscriptionId;

    public ArticlesPresenter(Activity activity, IArticlesView articlesView, long subscriptionId) {
        mActivity = activity;
        mArticlesView = articlesView;
        mSubscriptionId = subscriptionId;
        mArticlesView.setObserver(this);
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
    public void onDestroy() {

    }

    @Override
    public void onDataResponse(ResponseState state, DataType dataType) {
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

    @Override
    public void onItemClick(View view, Article data) {
        if (!data.getRead()) {
            ArticleController.getInstance().markAllRead(data);
        }

        // TODO: 11/10/16 if no content and desc, shake then stay, and upload source

        Intent intent = new Intent(mActivity, ArticleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.KEY_BUNDLE_ARTICLE_ID, data.getId());
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}
