package com.feeder.android.view.article;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import com.feeder.android.util.Constants;
import com.feeder.android.util.DateUtil;
import com.feeder.android.view.BaseSwipeActivity;
import com.feeder.android.view.SettingsActivity;
import com.feeder.common.SPManager;
import com.feeder.common.ThreadManager;
import com.feeder.domain.DBManager;
import com.feeder.model.Article;
import com.feeder.model.ArticleDao;
import com.feeder.model.Subscription;
import com.feeder.model.SubscriptionDao;
import com.google.common.base.Strings;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 10/23/16
 */

// TODO: 10/31/16 optimize : make image match parent``
// TODO: 10/30/16 to be modularity
public class ArticleActivity extends BaseSwipeActivity {
    private HtmlTextView mContentTextView;
    private TextView mTitleTextView;
    private TextView mSubscriptionNameTextView;
    private TextView mDateTextView;
    private TextView mTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        initViews();
        Long articleId = getIntent().getExtras().getLong(Constants.KEY_BUNDLE_ARTICLE_ID);
        loadDataAsync(articleId);
    }

    private void initViews() {
        mTitleTextView = (TextView) findViewById(R.id.article_title);
        mDateTextView = (TextView) findViewById(R.id.article_date);
        mTimeTextView = (TextView) findViewById(R.id.article_time);
        mSubscriptionNameTextView = (TextView) findViewById(R.id.subscription_name);

        mContentTextView = (HtmlTextView) findViewById(R.id.article_content);
        switch (SPManager.getInt(SettingsActivity.KEY_FONT_SIZE,
                SettingsActivity.FONT_SIZE_MEDIUM)) {
            case SettingsActivity.FONT_SIZE_SMALL:
                mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.text_size_small));
                break;
            case SettingsActivity.FONT_SIZE_MEDIUM:
                mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.text_size_medium));
                break;
            case SettingsActivity.FONT_SIZE_BIG:
                mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.text_size_big));
                break;
        }
    }

    private void loadDataAsync(final Long articleId) {
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                final List<Article> articleList = DBManager.getArticleDao().queryBuilder().where(
                        ArticleDao.Properties.Id.eq(articleId)).list();
                if (articleList.size() != 1) {
                    return;
                }
                final Article article = articleList.get(0);

                final List<Subscription> subscriptionList = DBManager.getSubscriptionDao().queryBuilder().where(
                        SubscriptionDao.Properties.Id.eq(article.getSubscriptionId())).list();
                if (subscriptionList.size() != 1) {
                    return;
                }

                ThreadManager.post(new Runnable() {
                    @Override
                    public void run() {
                        setData(article, subscriptionList.get(0).getTitle());
                    }
                });
            }
        });
    }

    private void setData(Article article, String subscriptionName) {
        mTitleTextView.setText(article.getTitle());
        mDateTextView.setText(DateUtil.formatDate(this, article.getPublished()));
        mTimeTextView.setText(DateUtil.formatTime(article.getPublished()));
        mSubscriptionNameTextView.setText(subscriptionName);
        if (Strings.isNullOrEmpty(article.getContent())) {
            if (!Strings.isNullOrEmpty(article.getDescription())) {
                mContentTextView.setHtml(article.getDescription(), new HtmlHttpImageGetter(mContentTextView));
            }
        } else {
            mContentTextView.setHtml(article.getContent(), new HtmlHttpImageGetter(mContentTextView));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}
