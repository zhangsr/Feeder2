package com.feeder.android.view.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.feeder.android.base.SubscriptionViewObserver;
import com.feeder.model.Subscription;

import java.util.List;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 8/8/16
 */
public class CategorySubscriptionListAdapter extends ExpandableRecyclerAdapter<Category, Subscription, CategoryViewHolder, SubscriptionViewHolder> {
    private Context mContext;
    private SubscriptionViewObserver mObserver;

    public CategorySubscriptionListAdapter(Context context, @NonNull List<Category> parentItemList,
                                           SubscriptionViewObserver observer) {
        super(parentItemList);
        mContext = context;
        mObserver = observer;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View categoryView = LayoutInflater.from(mContext).inflate(R.layout.subscriptions_category, parentViewGroup, false);
        return new CategoryViewHolder(categoryView);
    }

    @NonNull
    @Override
    public SubscriptionViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.subscriptions_item, childViewGroup, false);
        return new SubscriptionViewHolder(itemView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull CategoryViewHolder parentViewHolder, int parentPosition, @NonNull Category parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull SubscriptionViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull final Subscription child) {
        childViewHolder.bind(child, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObserver.onItemClick(v, child);
            }
        }, new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return mObserver.onItemLongClick(v, child);
            }
        });
    }
}
