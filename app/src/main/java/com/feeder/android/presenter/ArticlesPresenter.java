package com.feeder.android.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.feeder.android.base.IArticlesView;
import com.feeder.android.base.MVPPresenter;
import com.feeder.android.base.ArticleViewObserver;
import com.feeder.android.util.AnimationHelper;
import com.feeder.android.util.Constants;
import com.feeder.android.view.article.ArticleActivity;
import com.feeder.domain.ArticleModel;
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
        mArticlesView.setDataSource(ArticleModel.getInstance().getDataSource(mSubscriptionId));
    }

    @Override
    public void onStart() {
        ArticleModel.getInstance().registerObserver(this);
        if (mSubscriptionId == ArticleModel.ID_FAV) {
            ArticleModel.getInstance().requestFav();
        } else {
            ArticleModel.getInstance().requestData(mSubscriptionId);
            ArticleModel.getInstance().requestNetwork(mSubscriptionId);
        }
    }

    @Override
    public void onStop() {
        ArticleModel.getInstance().unRegisterObserver(this);
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
    public void onItemClick(View view, List<Article> dataList, int pos) {
        Article data = dataList.get(pos);
        if (!data.getRead()) {
            ArticleModel.getInstance().markAllRead(true, data);
        }

        // TODO: 11/10/16 if no content and desc, shake then stay, and upload source

        Intent intent = new Intent(mActivity, ArticleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLongArray(Constants.KEY_BUNDLE_ARTICLE_ID, getIdArray(dataList));
        bundle.putInt(Constants.KEY_BUNDLE_ARTICLE_INDEX, pos);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
        AnimationHelper.overridePendingTransition(mActivity);
    }

    private long[] getIdArray(List<Article> dataList) {
        if (dataList == null || dataList.size() == 0) {
            return null;
        }
        long[] idArray = new long[dataList.size()];
        for (int i = 0; i < idArray.length; i++) {
            idArray[i] = dataList.get(i).getId();
        }
        return idArray;
    }

    @Override
    public boolean onItemLongClick(View view, List<Article> dataList, int pos) {
        // TODO: 1/18/17 use state pattern ?
        final Article data = dataList.get(pos);
        List<CharSequence> menuList = new ArrayList<>();
        if (pos != 0) {
            menuList.add(view.getResources().getString(R.string.mark_all_above_as_read));
        }
        if (data.getRead()) {
            menuList.add(view.getResources().getString(R.string.mark_as_unread));
        }
        if (pos != dataList.size() - 1) {
            menuList.add(view.getResources().getString(R.string.mark_all_below_as_read));
        }
        if (menuList.size() == 0) {
            return true;
        }

        new MaterialDialog.Builder(mActivity)
                .title(data.getTitle())
                .items(menuList.toArray(new CharSequence[menuList.size()]))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i,
                                            CharSequence charSequence) {
                        if (charSequence.equals(view.getResources().getString(R.string.mark_all_above_as_read))) {

                            ArticleModel.getInstance().markAllLaterAsRead(data);
                        } else if (charSequence.equals(view.getResources().getString(R.string.mark_as_read))){
                            ArticleModel.getInstance().markAllRead(false, data);

                        } else if (charSequence.equals(view.getResources().getString(R.string.mark_all_below_as_read))){
                            ArticleModel.getInstance().markAllEarlierAsRead(data);
                        }
                    }
                }).show();
        return true;
    }
}
