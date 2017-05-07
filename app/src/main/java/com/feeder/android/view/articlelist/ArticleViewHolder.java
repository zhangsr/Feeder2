package com.feeder.android.view.articlelist;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feeder.android.util.ArticleUtil;
import com.feeder.android.util.DateUtil;
import com.feeder.android.util.HtmlUtil;
import com.feeder.common.SPManager;
import com.feeder.model.Article;
import com.google.common.base.Strings;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.Date;

import me.zsr.feeder.R;

import static com.feeder.android.util.Constants.*;

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
    private ImageView mImageView;

    public ArticleViewHolder(View itemView) {
        super(itemView);

        mItemView = itemView;
        mTitleTextView = (TextView) itemView.findViewById(R.id.article_title);
        mDescTextView = (TextView) itemView.findViewById(R.id.article_desc);
        mTimeTextView = (TextView) itemView.findViewById(R.id.article_time);
        mImageView = (ImageView) itemView.findViewById(R.id.article_image);
    }

    public void bind(Article article, View.OnClickListener listener, View.OnLongClickListener longClickListener) {
        if (article == null) {
            return;
        }
        mItemView.setOnClickListener(listener);
        mItemView.setOnLongClickListener(longClickListener);
        mTitleTextView.setText(article.getTitle());
        if (article.getRead() && !article.getFavorite()) {
            mTitleTextView.setAlpha(0.54f);
            mTitleTextView.setTextColor(mItemView.getResources().getColor(R.color.main_grey_normal));
            mImageView.setAlpha(0.24f);
        } else {
            mTitleTextView.setAlpha(0.87f);
            mTitleTextView.setTextColor(mItemView.getResources().getColor(R.color.main_grey_dark));
            mImageView.setAlpha(1f);
        }
        mDescTextView.setText(HtmlUtil.getOptimizedDesc(article.getDescription()));
        mTimeTextView.setText(formatTime(article.getPublished()));

        if (SPManager.getInt(KEY_ARTICLE_LIST_LAYOUT, LAYOUT_STYLE_RIGHT_IMAGE) == LAYOUT_STYLE_RIGHT_IMAGE) {
            mImageView.setVisibility(View.VISIBLE);
        } else {
            mImageView.setVisibility(View.GONE);
        }

        mImageView.setImageDrawable(new ColorDrawable(mItemView.getResources().getColor(R.color.main_grey_light)));
        if (!Strings.isNullOrEmpty(ArticleUtil.getContent(article))) {
            Document doc = Jsoup.parse(ArticleUtil.getContent(article));
            if (doc != null) {
                Elements images = doc.select("img");
                if (images.size() > 0) {
                    String imageurl = images.get(0).attr("src");
                    ImageLoader.getInstance().displayImage(imageurl, mImageView);
                    return;
                }
            }

            mImageView.setVisibility(View.GONE);
        }
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
