package com.feeder.android.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.feeder.android.base.IArticlesView;
import com.feeder.android.base.MVPPresenter;
import com.feeder.android.base.ArticleViewObserver;
import com.feeder.android.util.Constants;
import com.feeder.android.view.article.ArticleActivity;
import com.feeder.domain.ArticleController;
import com.feeder.domain.DataObserver;
import com.feeder.domain.DataType;
import com.feeder.domain.ResponseState;
import com.feeder.model.Article;

import java.util.ArrayList;
import java.util.List;

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
        if (mSubscriptionId == ArticleController.ID_FAV) {
            ArticleController.getInstance().requestFav();
        } else {
            ArticleController.getInstance().requestData(mSubscriptionId);
            ArticleController.getInstance().requestNetwork(mSubscriptionId);
        }
    }

    @Override
    public void onStop() {
        ArticleController.getInstance().unRegisterObserver(this);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onDataChanged() {
        mArticlesView.notifyDataChanged();
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
            ArticleController.getInstance().markAllRead(true, data);
        }

        // TODO: 11/10/16 if no content and desc, shake then stay, and upload source

        Intent intent = new Intent(mActivity, ArticleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.KEY_BUNDLE_ARTICLE_ID, data.getId());
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public boolean onItemLongClick(View view, final Article data) {
        // TODO: 1/18/17 use state pattern ?
        if (data.getRead() && !data.getFavorite()) {
            List<CharSequence> menuList = new ArrayList<>();
            menuList.add(view.getResources().getString(R.string.mark_as_unread));
            new MaterialDialog.Builder(mActivity)
                    .title(data.getTitle())
                    .items(menuList.toArray(new CharSequence[menuList.size()]))
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog materialDialog, View view, int i,
                                                CharSequence charSequence) {
                            switch (i) {
                                case 0:
                                    ArticleController.getInstance().markAllRead(false, data);
                                    break;
                            }
                        }
                    }).show();
            return true;
        } else {
            return false;
        }
    }
}
