package com.feeder.android.utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feeder.model.Account;

import java.util.List;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 7/20/16
 */
public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder>{
    private List<Account> mAccountList;

    public AccountsAdapter(List<Account> list) {
        mAccountList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView accountItem = (TextView) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_account, parent, false);

        ViewHolder vh = new ViewHolder(accountItem);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mNameTextView.setText(mAccountList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mAccountList == null ? 0 : mAccountList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameTextView;
        public ViewHolder(TextView v) {
            super(v);
            mNameTextView = v;
        }
    }
}
