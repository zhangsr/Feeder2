package com.feeder.android.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.feeder.android.base.ISubscriptionsView;
import com.feeder.android.base.MVPPresenter;
import com.feeder.android.util.AnimationHelper;
import com.feeder.android.util.StatManager;
import com.feeder.android.view.main.Category;
import com.feeder.android.util.Constants;
import com.feeder.android.base.SubscriptionViewObserver;
import com.feeder.android.view.articlelist.ArticleListActivity;
import com.feeder.common.StringUtil;
import com.feeder.domain.model.AccountModel;
import com.feeder.domain.model.ArticleModel;
import com.feeder.domain.model.DataObserver;
import com.feeder.domain.model.DataType;
import com.feeder.domain.RefreshManager;
import com.feeder.domain.model.ResponseState;
import com.feeder.domain.model.SubscriptionModel;
import com.feeder.model.Subscription;

import java.util.ArrayList;
import java.util.List;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 8/4/16
 */
public class SubscriptionsPresenter implements MVPPresenter, DataObserver, SubscriptionViewObserver {
    private ISubscriptionsView mSubscriptionView;
    private Activity mActivity;
    private List<Category> mCategoryList = new ArrayList<>();

    public SubscriptionsPresenter(Activity activity, ISubscriptionsView subscriptionsView) {
        mActivity = activity;
        mSubscriptionView = subscriptionsView;
        mSubscriptionView.setObserver(this);
    }

    @Override
    public void onCreate() {
        mSubscriptionView.showLoading();
        mCategoryList.add(new Category(SubscriptionModel.getInstance().getDataSource()));
        mSubscriptionView.setDataSource(mCategoryList);
    }

    @Override
    public void onStart() {
        SubscriptionModel.getInstance().registerObserver(this);
        SubscriptionModel.getInstance().requestData();
        ArticleModel.getInstance().registerObserver(this);
        AccountModel.getInstance().registerObserver(this);

        // TODO: 12/18/16 verify
        // TODO: 4/12/17 refresh after db return is better ?
        RefreshManager.getInstance().refreshAll(2000);
    }

    @Override
    public void onStop() {
        SubscriptionModel.getInstance().unRegisterObserver(this);
        ArticleModel.getInstance().unRegisterObserver(this);
    }

    @Override
    public void onDestroy() {
        ArticleModel.getInstance().markReadTrash();
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataResponse(ResponseState state, DataType type) {
        switch (state) {
            case SUCCESS:
                // TODO: 11/13/16 optimize : use animation
                if (type == DataType.SUBSCRIPTION) {
                    updateCategory();
                    mSubscriptionView.hideLoading();
                    mSubscriptionView.notifyDataChanged();
                } else if (type == DataType.ACCOUNT) {
                    SubscriptionModel.getInstance().requestData();
                }
                break;
            case NO_CHANGE:
                mSubscriptionView.hideLoading();
                break;
        }
    }

    @Override
    public void onItemClick(View view, Subscription data) {
        Intent intent = new Intent(mActivity, ArticleListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.KEY_BUNDLE_SUBSCRIPTION_ID, data.getId());
        bundle.putString(Constants.KEY_BUNDLE_SUBSCRIPTION_TITLE, data.getTitle());
        bundle.putString(Constants.KEY_BUNDLE_SUBSCRIPTION_ICON_URL, data.getIconUrl());
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
        AnimationHelper.overridePendingTransition(mActivity);

        StatManager.statEvent(mActivity, StatManager.EVENT_ENTER_LIST, data.getTitle());
    }

    @Override
    public boolean onItemLongClick(View view, final Subscription data) {
        List<CharSequence> menuList = new ArrayList<>();
        menuList.add(view.getResources().getString(R.string.mark_as_read));
        menuList.add(view.getResources().getString(R.string.remove_subscription));
        new MaterialDialog.Builder(mActivity)
                .title(data.getTitle())
                .items(menuList.toArray(new CharSequence[menuList.size()]))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i,
                                            CharSequence charSequence) {
                        switch (i) {
                            case 0:
                                ArticleModel.getInstance().markAllRead(true, data.getId());
                                break;
                            case 1:
                                SubscriptionModel.getInstance().delete(data);
                                break;
                        }
                    }
                }).show();
        return true;
    }

    private void updateCategory() {
        // construct temp category beyond new subscription list
        List<Category> tempCategoryList = new ArrayList<>();
        for (Subscription subscription : SubscriptionModel.getInstance().getDataSource()) {
            boolean existCategory = false;
            for (Category category : tempCategoryList) {
                if (StringUtil.equals(category.getName(), subscription.getCategory())) {
                    category.getChildList().add(subscription);
                    existCategory = true;
                    break;
                }
            }
            if (!existCategory) {
                List<Subscription> subscriptionList = new ArrayList<>();
                subscriptionList.add(subscription);
                Category category = new Category(subscriptionList);
                tempCategoryList.add(category);
            }
        }

        // remove delete
        for (Category category : mCategoryList) {
            if (tempCategoryList.indexOf(category) == -1) {
                mCategoryList.remove(category);
            }
        }

        for (Category category : tempCategoryList) {
            if (mCategoryList.indexOf(category) == -1) {
                // add new
                mCategoryList.add(category);
            } else {
                // replace exit
                int index = mCategoryList.indexOf(category);
                mCategoryList.get(index).setChildList(category.getChildList());
            }
        }
    }
}
