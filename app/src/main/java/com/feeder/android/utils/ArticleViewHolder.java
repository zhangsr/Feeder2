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
    private TextView mDescTextView;

    public ArticleViewHolder(View itemView) {
        super(itemView);

        mDescTextView = (TextView) itemView.findViewById(R.id.article_desc);
    }

    public void bind(Article article) {
        mDescTextView.setText(article.getDescription());
    }
}
