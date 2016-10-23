package com.feeder.android.utils;

import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.feeder.domain.VolleySingleton;
import com.feeder.model.Subscription;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 8/4/16
 */
public class SubscriptionViewHolder extends ChildViewHolder {
    private View mItemView;
    private TextView mNameTextView;
    private NetworkImageView mIconImageView;

    public SubscriptionViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mNameTextView = (TextView) itemView.findViewById(R.id.subscription_name);
        mIconImageView = (NetworkImageView) itemView.findViewById(R.id.subscription_icon);
        mIconImageView.setDefaultImageResId(R.drawable.ic_rss_feed_black_24dp);
        mIconImageView.setErrorImageResId(R.drawable.ic_rss_feed_black_24dp);
    }

    public void bind(Subscription subscription, final View.OnClickListener listener) {
        if (subscription == null) {
            return;
        }
        mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        mNameTextView.setText(subscription.getTitle());
        mIconImageView.setImageUrl(subscription.getIconUrl(), VolleySingleton.getInstance().getImageLoader());
    }
}
