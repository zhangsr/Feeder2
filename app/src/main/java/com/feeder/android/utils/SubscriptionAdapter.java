package com.feeder.android.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.feeder.model.Subscription;

import java.util.List;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 8/8/16
 */
public class SubscriptionAdapter extends ExpandableRecyclerAdapter<CategoryViewHolder, SubscriptionViewHolder> {
    private Context mContext;

    public SubscriptionAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mContext = context;
    }

    // onCreate ...
    @Override
    public CategoryViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View recipeView = LayoutInflater.from(mContext).inflate(R.layout.subscriptions_category, parentViewGroup, false);
        return new CategoryViewHolder(recipeView);
    }

    @Override
    public SubscriptionViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View ingredientView = LayoutInflater.from(mContext).inflate(R.layout.subscriptions_item, childViewGroup, false);
        return new SubscriptionViewHolder(ingredientView);
    }

    // onBind ...
    @Override
    public void onBindParentViewHolder(CategoryViewHolder categoryViewHolder, int position, ParentListItem parentListItem) {
        Category category = (Category) parentListItem;
        categoryViewHolder.bind(category);
    }

    @Override
    public void onBindChildViewHolder(SubscriptionViewHolder subscriptionViewHolder, int position, Object childListItem) {
        Subscription subscription = (Subscription) childListItem;
        subscriptionViewHolder.bind(subscription);
    }
}
