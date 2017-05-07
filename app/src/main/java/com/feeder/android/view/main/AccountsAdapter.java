package com.feeder.android.view.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feeder.android.base.AccountViewObserver;
import com.feeder.model.Account;

import java.util.List;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 7/20/16
 */
public class AccountsAdapter extends RecyclerView.Adapter<AccountViewHolder>{
    private List<Account> mAccountList;
    private AccountViewObserver mObserver;

    public AccountsAdapter(List<Account> list, AccountViewObserver observer) {
        mAccountList = list;
        mObserver = observer;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_account, parent, false);

        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder viewHolder, final int position) {
        if (mAccountList == null || position >= mAccountList.size()) {
            return;
        }
        viewHolder.bind(mAccountList.get(position), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObserver.onItemClick(v, mAccountList, position);
            }
        }, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return mObserver.onItemLongClick(v, mAccountList, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAccountList == null ? 0 : mAccountList.size();
    }
}
