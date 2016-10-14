package com.feeder.android.utils;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 8/4/16
 */
public class Category implements ParentListItem {

    // a recipe contains several ingredients
    private List mSubscriptionList;

    public Category(List subscriptionList) {
        mSubscriptionList = subscriptionList;
    }

    @Override
    public List getChildItemList() {
        return mSubscriptionList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
