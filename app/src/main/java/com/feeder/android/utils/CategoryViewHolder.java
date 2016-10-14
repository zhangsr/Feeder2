package com.feeder.android.utils;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 8/4/16
 */
public class CategoryViewHolder extends ParentViewHolder {

    private TextView mNameTextView;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        mNameTextView = (TextView) itemView.findViewById(R.id.subscription_name);
    }

    public void bind(Category category) {
    }
}
