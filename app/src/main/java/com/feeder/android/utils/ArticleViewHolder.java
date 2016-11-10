package com.feeder.android.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.feeder.model.Article;
import com.google.common.base.Strings;

import org.jsoup.Jsoup;

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
        if (article.getRead()) {
            mTitleTextView.setAlpha(0.54f);
            mTitleTextView.setTextColor(mItemView.getResources().getColor(R.color.main_grey_normal));
        } else {
            mTitleTextView.setAlpha(0.87f);
            mTitleTextView.setTextColor(mItemView.getResources().getColor(R.color.main_grey_dark));
        }
        mDescTextView.setText(getOptimizedDesc(article.getDescription()));
        mTimeTextView.setText(DateUtil.formatTime(article.getPublished()));
    }

    private String getOptimizedDesc(String originDesc) {
        // Shrink string to optimize render time
        String result = "";
        if (Strings.isNullOrEmpty(originDesc)) {
            return result;
        }
        String parsedStr = Jsoup.parse(originDesc).text();
        int showLength = parsedStr.length() < 50 ? parsedStr.length() : 50;
        if (showLength > 0) {
            result = parsedStr.substring(0, showLength - 1);
        }
        return result;
    }
}
