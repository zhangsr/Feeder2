package com.feeder.android.view.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.feeder.android.base.ISubscriptionsView;
import com.feeder.android.other.LinearLayoutManagerEx;

import java.util.List;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 8/4/16
 */
public class CategorySubscriptionListView extends ISubscriptionsView {
    private RecyclerView mRecyclerView;
    private CategorySubscriptionListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public CategorySubscriptionListView(Context context) {
        super(context);
        mRecyclerView = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.view_recycler, this, false);

        mLayoutManager = new LinearLayoutManagerEx(context);
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
        super.notifyDataChanged();
        mAdapter.notifyParentDataSetChanged(true);
    }

    @Override
    public void setDataSource(List<Category> categoryList) {
        mAdapter = new CategorySubscriptionListAdapter(getContext(), categoryList, mObserver);
        mRecyclerView.setAdapter(mAdapter);
    }
}
