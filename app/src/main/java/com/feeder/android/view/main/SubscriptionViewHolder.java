package com.feeder.android.view.main;

import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
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
    private TextView mCountTextView;

    public SubscriptionViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mNameTextView = (TextView) itemView.findViewById(R.id.subscription_name);
        mIconImageView = (NetworkImageView) itemView.findViewById(R.id.subscription_icon);
        mIconImageView.setDefaultImageResId(R.drawable.ic_rss_feed_black_24dp);
        mIconImageView.setErrorImageResId(R.drawable.ic_rss_feed_black_24dp);
        mCountTextView = (TextView) itemView.findViewById(R.id.count_txt);
    }

    public void bind(Subscription subscription, final View.OnClickListener clickListener,
                     View.OnLongClickListener longClickListener) {
        if (subscription == null) {
            return;
        }
        mItemView.setOnClickListener(clickListener);
        mItemView.setOnLongClickListener(longClickListener);
        mNameTextView.setText(subscription.getTitle());
        mNameTextView.setSingleLine();
        mIconImageView.setImageUrl(subscription.getIconUrl(), VolleySingleton.getInstance().getImageLoader());
        if (subscription.getUnreadCount() <= 0) {
            mCountTextView.setText("");
        } else {
            mCountTextView.setText(String.valueOf(subscription.getUnreadCount()));
        }
    }
}
