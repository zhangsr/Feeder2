package com.feeder.android.view.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.feeder.android.util.ImageLoaderManager;
import com.feeder.model.Subscription;
import com.nostra13.universalimageloader.core.ImageLoader;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 8/4/16
 */
public class SubscriptionViewHolder extends ChildViewHolder {
    private View mItemView;
    private TextView mNameTextView;
    private ImageView mIconImageView;
    private TextView mCountTextView;

    public SubscriptionViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mNameTextView = (TextView) itemView.findViewById(R.id.subscription_name);
        mIconImageView = (ImageView) itemView.findViewById(R.id.subscription_icon);
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
        ImageLoader.getInstance().displayImage(subscription.getIconUrl(), mIconImageView, ImageLoaderManager.getSubsciptionIconOptions(mItemView.getContext()));
        if (subscription.getUnreadCount() <= 0) {
            mCountTextView.setText("");
        } else {
            mCountTextView.setText(String.valueOf(subscription.getUnreadCount()));
        }
    }
}
