package com.feeder.android.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.feeder.android.mvp.ISubscriptionsView;
import com.feeder.android.utils.Category;
import com.feeder.android.utils.SubscriptionAdapter;
import com.feeder.model.Subscription;

import java.util.ArrayList;
import java.util.List;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 8/4/16
 */
public class SubscriptionsView extends ISubscriptionsView {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public SubscriptionsView(Context context) {
        super(context);
        mRecyclerView = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.view_subscriptions, this, false);


        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        addView(mRecyclerView);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void notifyDataChanged() {

    }

    @Override
    public void setDataSource(List<Subscription> subscriptionList) {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(subscriptionList));

        mAdapter = new SubscriptionAdapter(getContext(), categoryList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
