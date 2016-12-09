package com.feeder.android.view.articlelist;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.feeder.android.util.DateUtil;
import com.feeder.android.util.HtmlUtil;
import com.feeder.model.Article;

import java.util.Calendar;
import java.util.Date;

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

    public void bind(Article article, View.OnClickListener listener, View.OnLongClickListener longClickListener) {
        if (article == null) {
            return;
        }
        mItemView.setOnClickListener(listener);
        mItemView.setOnLongClickListener(longClickListener);
        mTitleTextView.setText(article.getTitle());
        if (article.getRead()) {
            mTitleTextView.setAlpha(0.54f);
            mTitleTextView.setTextColor(mItemView.getResources().getColor(R.color.main_grey_normal));
        } else {
            mTitleTextView.setAlpha(0.87f);
            mTitleTextView.setTextColor(mItemView.getResources().getColor(R.color.main_grey_dark));
        }
        mDescTextView.setText(HtmlUtil.getOptimizedDesc(article.getDescription()));
        mTimeTextView.setText(formatTime(article.getPublished()));
    }

    private String formatTime(Long time) {
        String result = "";
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date yestoday = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date beforeYestoday = calendar.getTime();
        if (DateUtil.isSameDay(date, today)) {
            result += DateFormat.format("HH:mm", date);
        } else if (DateUtil.isSameDay(date, yestoday)) {
            result += mItemView.getResources().getString(R.string.yestoday);
            result += DateFormat.format(" HH:mm", date);
        } else if (DateUtil.isSameDay(date, beforeYestoday)) {
            result += mItemView.getResources().getString(R.string.before_yestoday);
            result += DateFormat.format(" HH:mm", date);
        } else if (DateUtil.isSameYear(date, today)) {
            result += DateFormat.format("MM月dd日", date);
        } else {
            result += DateFormat.format("yyyy年MM月dd日", date);
        }

        return result;
    }
}
