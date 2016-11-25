package com.feeder.android.view.main;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.feeder.model.Subscription;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 8/4/16
 */
public class CategoryViewHolder extends ParentViewHolder {
    private View mItemView;
    private ImageButton mIconButton;
    private TextView mNameTextView;
    private TextView mCountTextView;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mIconButton = (ImageButton) itemView.findViewById(R.id.category_icon);
        mNameTextView = (TextView) itemView.findViewById(R.id.category_name);
        mCountTextView = (TextView) itemView.findViewById(R.id.count_txt);
    }

    public void bind(Category category) {
        if (category == null
                || category.getChildList() == null
                || category.getChildList().size() == 0) {
            return;
        }
        mNameTextView.setText(category.getChildList().get(0).getCategory());
        int count = 0;
        for (Subscription subscription : category.getChildList()) {
            count += subscription.getUnreadCount();
        }
        if (count <= 0) {
            mCountTextView.setText("");
        } else {
            mCountTextView.setText(String.valueOf(count));
        }
        if (category.isInitiallyExpanded()) {
            mIconButton.setImageDrawable(mItemView.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_24dp));
            mCountTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        if (expanded) {
            mIconButton.setImageDrawable(mItemView.getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_white_24dp));
            mCountTextView.setVisibility(View.VISIBLE);
        } else {
            mIconButton.setImageDrawable(mItemView.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_24dp));
            mCountTextView.setVisibility(View.INVISIBLE);
        }
    }
}
