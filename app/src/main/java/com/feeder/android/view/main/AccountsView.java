package com.feeder.android.view.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.feeder.android.base.IAccountsView;
import com.feeder.android.other.LinearLayoutManagerEx;
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
        mLayoutManager = new LinearLayoutManagerEx(context);
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
        super.notifyDataChanged();
        mAdapter.getItemCount();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setDataSource(List<Account> accountList) {
        mAdapter = new AccountsAdapter(accountList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
