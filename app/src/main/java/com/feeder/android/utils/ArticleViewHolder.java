package com.feeder.android.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.feeder.model.Article;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 10/28/16
 */

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    private View mItemView;
    private TextView mTitleTextView;
    private TextView mDescTextView;
    private TextView mTimeTextView;

    public ArticleViewHolder(View itemView) {
        super(itemView);

        mItemView = itemView;
        mTitleTextView = (TextView) itemView.findViewById(R.id.article_title);
        mDescTextView = (TextView) itemView.findViewById(R.id.article_desc);
        mTimeTextView = (TextView) itemView.findViewById(R.id.article_time);
    }

    public void bind(Article article, View.OnClickListener listener) {
        if (article == null) {
            return;
        }
        mItemView.setOnClickListener(listener);
        mTitleTextView.setText(article.getTitle());
        mDescTextView.setText(article.getDescription());
        mTimeTextView.setText(DateUtil.formatTime(article.getPublished()));
    }
}
