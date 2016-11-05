package com.feeder.android.utils;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.feeder.model.Subscription;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 8/4/16
 */
public class CategoryViewHolder extends ParentViewHolder {
    private View mItemView;
    private TextView mNameTextView;
    private TextView mCountTextView;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mNameTextView = (TextView) itemView.findViewById(R.id.category_name);
        mCountTextView = (TextView) itemView.findViewById(R.id.count_txt);
    }

    public void bind(Category category) {
        if (category == null
                || category.getChildItemList() == null
                || category.getChildItemList().size() == 0) {
            return;
        }
        mNameTextView.setText(category.getChildItemList().get(0).getCategory());
        int count = 0;
        for (Subscription subscription : category.getChildItemList()) {
            count += subscription.getUnreadCount();
        }
        if (count <= 0) {
            mCountTextView.setText("");
        } else {
            mCountTextView.setText(String.valueOf(count));
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        if (expanded) {
            mCountTextView.setVisibility(View.VISIBLE);
        } else {
            mCountTextView.setVisibility(View.INVISIBLE);
        }
    }
}
