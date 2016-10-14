package com.feeder.android.utils;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.feeder.model.Subscription;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 8/4/16
 */
public class SubscriptionViewHolder extends ChildViewHolder {

    private TextView mNameTextView;

    public SubscriptionViewHolder(View itemView) {
        super(itemView);
        mNameTextView = (TextView) itemView.findViewById(R.id.subscription_name);
    }

    public void bind(Subscription subscription) {
        mNameTextView.setText(subscription.getTitle());
    }
}
