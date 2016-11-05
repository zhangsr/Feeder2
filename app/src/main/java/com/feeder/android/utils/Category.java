package com.feeder.android.utils;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.feeder.model.Subscription;

import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 8/4/16
 */
public class Category implements ParentListItem {

    // a recipe contains several ingredients
    private List<Subscription> mSubscriptionList;

    public Category(List<Subscription> subscriptionList) {
        mSubscriptionList = subscriptionList;
    }

    @Override
    public List<Subscription> getChildItemList() {
        return mSubscriptionList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
