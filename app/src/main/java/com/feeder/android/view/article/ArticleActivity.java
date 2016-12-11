package com.feeder.android.view.article;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.feeder.android.util.Constants;
import com.feeder.android.util.DateUtil;
import com.feeder.android.util.ShareHelper;
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

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 10/23/16
 */

// TODO: 10/31/16 optimize : make image match parent``
// TODO: 10/30/16 to be modularity
public class ArticleActivity extends BaseSwipeActivity {
    private Toolbar mToolbar;
    private HtmlTextView mContentTextView;
    private TextView mTitleTextView;
    private TextView mSubscriptionNameTextView;
    private TextView mDateTextView;
    private TextView mTimeTextView;
    private ShareHelper mShareHelper;
    private Article mArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        initViews();
        Long articleId = getIntent().getExtras().getLong(Constants.KEY_BUNDLE_ARTICLE_ID);
        loadDataAsync(articleId);

        ShareSDK.initSDK(this);
        mShareHelper = new ShareHelper(this);
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_article);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        showShareMenu();
                        break;
                }
                return false;
            }
        });

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
                mArticle = articleList.get(0);

                final List<Subscription> subscriptionList = DBManager.getSubscriptionDao().queryBuilder().where(
                        SubscriptionDao.Properties.Id.eq(mArticle.getSubscriptionId())).list();
                if (subscriptionList.size() != 1) {
                    return;
                }

                ThreadManager.post(new Runnable() {
                    @Override
                    public void run() {
                        setData(mArticle, subscriptionList.get(0).getTitle());
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
                mContentTextView.setHtml(article.getDescription(), new HtmlHttpImageGetter(mContentTextView, null, true));
            }
        } else {
            mContentTextView.setHtml(article.getContent(), new HtmlHttpImageGetter(mContentTextView, null, true));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    private void showShareMenu() {
        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(this);
        final List<Integer> contentIdList = new ArrayList<>();
        if (SPManager.getBoolean(SettingsActivity.KEY_SWITCH_SHARE_WECHAT, true)) {
            adapter.add(new MaterialSimpleListItem.Builder(this)
                    .content(R.string.wechat)
                    .icon(R.drawable.ic_menu_wechat)
                    .backgroundColor(Color.WHITE)
                    .build());
            contentIdList.add(R.string.wechat);
        }
        if (SPManager.getBoolean(SettingsActivity.KEY_SWITCH_SHARE_MOMENT, true)) {
            adapter.add(new MaterialSimpleListItem.Builder(this)
                    .content(R.string.moment)
                    .icon(R.drawable.ic_menu_moment)
                    .backgroundColor(Color.WHITE)
                    .build());
            contentIdList.add(R.string.moment);
        }
        if (SPManager.getBoolean(SettingsActivity.KEY_SWITCH_SHARE_WEIBO, true)) {
            adapter.add(new MaterialSimpleListItem.Builder(this)
                    .content(R.string.weibo)
                    .icon(R.drawable.ic_menu_weibo)
                    .backgroundColor(Color.WHITE)
                    .build());
            contentIdList.add(R.string.weibo);
        }
        if (SPManager.getBoolean(SettingsActivity.KEY_SWITCH_SHARE_INSTAPAPER, true)) {
            adapter.add(new MaterialSimpleListItem.Builder(this)
                    .content(R.string.instapaper)
                    .icon(R.drawable.ic_menu_instapaper)
                    .backgroundColor(Color.WHITE)
                    .build());
            contentIdList.add(R.string.instapaper);
        }

        new MaterialDialog.Builder(this)
                .title(R.string.share_to)
                .adapter(adapter, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (contentIdList.get(which)) {
                            case R.string.wechat:
                                mShareHelper.shareToWechat(mArticle);
                                break;
                            case R.string.moment:
                                mShareHelper.shareToMoment(mArticle);
                                break;
                            case R.string.weibo:
                                mShareHelper.shareToWeibo(mArticle);
                                break;
                            case R.string.instapaper:
                                mShareHelper.shareToInstapaper(mArticle);
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
