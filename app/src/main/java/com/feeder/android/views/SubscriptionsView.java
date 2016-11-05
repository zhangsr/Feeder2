package com.feeder.android.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.feeder.android.mvp.ISubscriptionsView;
import com.feeder.android.utils.Category;
import com.feeder.android.utils.SubscriptionAdapter;

import java.util.List;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 8/4/16
 */
public class SubscriptionsView extends ISubscriptionsView {
    private RecyclerView mRecyclerView;
    private SubscriptionAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public SubscriptionsView(Context context) {
        super(context);
        mRecyclerView = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.view_recycler, this, false);

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
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setDataSource(List<Category> categoryList) {
        mAdapter = new SubscriptionAdapter(getContext(), categoryList, mObserver);
        mRecyclerView.setAdapter(mAdapter);
    }
}
