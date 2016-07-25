package com.feeder.android.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.feeder.android.mvp.IAccountsView;
import com.feeder.android.utils.AccountsAdapter;
import com.feeder.model.Account;

import java.util.List;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 7/18/16
 */
public class AccountsView extends IAccountsView {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public AccountsView(Context context) {
        super(context);
        mRecyclerView = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.view_accounts, this, false);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        addView(mRecyclerView);
    }

    @Override
    public void showLoading() {
        mLoadingView.setVisibility(VISIBLE);
        mRecyclerView.setVisibility(GONE);
    }

    @Override
    public void hideLoading() {
        mLoadingView.setVisibility(GONE);
        mRecyclerView.setVisibility(VISIBLE);
    }

    @Override
    public void notifyDataChanged() {
        mAdapter.getItemCount();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setDataSource(List<Account> accountList) {
        mAdapter = new AccountsAdapter(accountList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
