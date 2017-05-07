package com.feeder.android.view.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feeder.domain.model.AccountModel;
import com.feeder.model.Account;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 19/04/2017
 */

public class AccountViewHolder extends RecyclerView.ViewHolder {
    private View mItemView;
    private ImageView mAvatarImageView;
    private TextView mNameTextView;
    private TextView mUnreadTextView;

    public AccountViewHolder(View itemView) {
        super(itemView);

        mItemView = itemView;
        mAvatarImageView = (ImageView) itemView.findViewById(R.id.account_avatar);
        mNameTextView = (TextView) itemView.findViewById(R.id.account_name);
        mUnreadTextView = (TextView) itemView.findViewById(R.id.account_unread_count);
    }

    public void bind(final Account account, View.OnClickListener clickListener, View.OnLongClickListener longClickListener) {
        if (account == null) {
            return;
        }
        mItemView.setOnClickListener(clickListener);
        mItemView.setOnLongClickListener(longClickListener);
        mAvatarImageView.setImageResource(R.drawable.ic_rss_feed_white_24dp);
        mNameTextView.setText(account.getName());
        mUnreadTextView.setText(account.getExt1());

        if (account == AccountModel.getInstance().getCurrentAccount()) {
            mItemView.setBackgroundColor(mItemView.getResources().getColor(R.color.main_grey_light));
        } else {
            mItemView.setBackgroundResource(R.drawable.rect_selector);
        }
    }
}
