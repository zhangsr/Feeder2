package com.feeder.android.utils;

import com.bignerdranch.expandablerecyclerview.model.SimpleParent;
import com.feeder.model.Subscription;

import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 8/4/16
 */
public class Category extends SimpleParent<Subscription> {

    public Category(List<Subscription> childItemList) {
        super(childItemList);
    }
}
