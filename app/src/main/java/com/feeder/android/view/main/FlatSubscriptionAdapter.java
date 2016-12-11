package com.feeder.android.view.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feeder.android.base.SubscriptionViewObserver;
import com.feeder.model.Subscription;

import java.util.List;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 12/10/16
 */

public class FlatSubscriptionAdapter extends RecyclerView.Adapter<SubscriptionViewHolder> {
    private Context mContext;
    private SubscriptionViewObserver mObserver;
    private List<Category> mDataList;

    public FlatSubscriptionAdapter(Context context, @NonNull List<Category> parentItemList,
                                   SubscriptionViewObserver observer) {
        mContext = context;
        mObserver = observer;
        mDataList = parentItemList;
    }

    @Override
    public SubscriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.subscriptions_item, parent, false);
        return new SubscriptionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SubscriptionViewHolder holder, final int position) {
        holder.bind(getItem(position), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObserver.onItemClick(v, getItem(position));
            }
        }, new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return mObserver.onItemLongClick(v, getItem(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (Category category : mDataList) {
            for (Subscription subscription : category.getChildList()) {
                count++;
            }
        }
        return count;
    }

    private Subscription getItem(int position) {
        int index = 0;
        for (Category category : mDataList) {
            for (Subscription subscription : category.getChildList()) {
                if (index == position) {
                    return subscription;
                } else {
                    index++;
                }
            }
        }
        return null;
    }
}
