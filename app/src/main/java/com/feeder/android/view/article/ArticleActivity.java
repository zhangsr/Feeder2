package com.feeder.android.view.article;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.feeder.android.util.AnimationHelper;
import com.feeder.android.util.ArticleUtil;
import com.feeder.android.util.Constants;
import com.feeder.android.util.DateUtil;
import com.feeder.android.util.ShareHelper;
import com.feeder.android.util.StatManager;
import com.feeder.android.view.BaseSwipeActivity;
import com.feeder.common.AppUtil;
import com.feeder.common.IntentUtil;
import com.feeder.common.SPManager;
import com.feeder.common.ThreadManager;
import com.feeder.domain.ArticleController;
import com.feeder.domain.DBManager;
import com.feeder.model.Article;
import com.feeder.model.ArticleDao;
import com.feeder.model.Subscription;
import com.feeder.model.SubscriptionDao;
import com.google.common.base.Strings;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import me.zsr.feeder.R;

import static com.feeder.android.util.Constants.*;

/**
 * @description:
 * @author: Match
 * @date: 10/23/16
 */

// TODO: 10/30/16 to be modularity
public class ArticleActivity extends BaseSwipeActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private ScrollView mScrollView;
    private HtmlTextView mContentTextView;
    private TextView mTitleTextView;
    private TextView mSubscriptionNameTextView;
    private TextView mDateTextView;
    private TextView mTimeTextView;
    private ShareHelper mShareHelper;
    private Article mArticle;
    private long[] mIdArray;
    private int mCurrentIndex;

    private ImageButton mPreviousBtn;
    private ImageButton mNextBtn;

    private boolean mIsClickEnabled= true;
    private boolean mIsLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        initViews();
        mIdArray = getIntent().getExtras().getLongArray(Constants.KEY_BUNDLE_ARTICLE_ID);
        mCurrentIndex = getIntent().getExtras().getInt(Constants.KEY_BUNDLE_ARTICLE_INDEX);
        loadDataAsync(mIdArray[mCurrentIndex]);

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
                    case R.id.action_link:
                        StatManager.statEvent(ArticleActivity.this, StatManager.EVENT_MENU_LINK_CLICK);
                        if (mArticle == null || Strings.isNullOrEmpty(mArticle.getLink())) {
                            return false;
                        }
                        IntentUtil.openUrl(ArticleActivity.this, mArticle.getLink());
                        break;
                    case R.id.action_fav:
                        if (mArticle != null) {
                            if (mArticle.getFavorite()) {
                                mArticle.setFavorite(false);
                                item.setIcon(R.drawable.ic_star_border_white_24dp);
                                Toast.makeText(ArticleActivity.this, R.string.unfavorited, Toast.LENGTH_SHORT).show();
                            } else {
                                mArticle.setFavorite(true);
                                item.setIcon(R.drawable.ic_star_white_24dp);
                                Toast.makeText(ArticleActivity.this, R.string.favorited, Toast.LENGTH_SHORT).show();
                            }
                            ArticleController.getInstance().saveArticle(mArticle);
                        }
                        StatManager.statEvent(ArticleActivity.this, StatManager.EVENT_MENU_FAV_CLICK);
                        break;
                    case R.id.action_share:
                        showShareMenu();
                        StatManager.statEvent(ArticleActivity.this, StatManager.EVENT_MENU_SHARE_CLICK);
                        break;
                }
                return false;
            }
        });

        mScrollView = (ScrollView) findViewById(R.id.scroll_container);

        mTitleTextView = (TextView) findViewById(R.id.article_title);
        mDateTextView = (TextView) findViewById(R.id.article_date);
        mTimeTextView = (TextView) findViewById(R.id.article_time);
        mSubscriptionNameTextView = (TextView) findViewById(R.id.subscription_name);

        mContentTextView = (HtmlTextView) findViewById(R.id.article_content);
        switch (SPManager.getInt(KEY_FONT_SIZE, FONT_SIZE_MEDIUM)) {
            case FONT_SIZE_SMALL:
                mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.text_size_small));
                break;
            case FONT_SIZE_MEDIUM:
                mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.text_size_medium));
                break;
            case FONT_SIZE_BIG:
                mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.text_size_big));
                break;
        }

        mPreviousBtn = (ImageButton) findViewById(R.id.previous_btn);
        mPreviousBtn.setOnClickListener(this);
        mNextBtn = (ImageButton) findViewById(R.id.next_btn);
        mNextBtn.setOnClickListener(this);
    }

    private void loadDataAsync(final Long articleId) {
        mIsLoading = true;
        final long timeStart = System.currentTimeMillis();
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

                if (!mArticle.getRead()) {
                    ArticleController.getInstance().markAllRead(true, mArticle);
                }

                ThreadManager.post(new Runnable() {
                    @Override
                    public void run() {
                        setData(mArticle, subscriptionList.get(0).getTitle());
                        mIsLoading = false;
                        statTimeSpend(timeStart);
                    }
                });
            }
        });
    }

    private void statTimeSpend(long timeStart) {
        String tag;
        if (timeStart < 100) {
            tag = StatManager.TAG_TIME_BELOW_100_MS;
        } else if (timeStart < 300) {
            tag = StatManager.TAG_TIME_BELOW_300_MS;
        } else if (timeStart < 500) {
            tag = StatManager.TAG_TIME_BELOW_500_MS;
        } else {
            tag = StatManager.TAG_TIME_ABOVE_500_MS;
        }
        StatManager.statEvent(this, StatManager.EVENT_LOAD_ARTICLE_TIME, tag);
    }

    private void setData(Article article, String subscriptionName) {
        mTitleTextView.setText(article.getTitle());
        mDateTextView.setText(DateUtil.formatDate(this, article.getPublished()));
        mTimeTextView.setText(DateUtil.formatTime(article.getPublished()));
        mSubscriptionNameTextView.setText(subscriptionName);
        ArticleUtil.setContent(this, article, mContentTextView, subscriptionName);
        mScrollView.scrollTo(0, 0);
        if (Strings.isNullOrEmpty(article.getLink())) {
            mToolbar.getMenu().findItem(R.id.action_link).setVisible(false);
        } else {
            mToolbar.getMenu().findItem(R.id.action_link).setVisible(true);
        }
        if (article.getFavorite()) {
            mToolbar.getMenu().findItem(R.id.action_fav).setIcon(R.drawable.ic_star_white_24dp);
        } else {
            mToolbar.getMenu().findItem(R.id.action_fav).setIcon(R.drawable.ic_star_border_white_24dp);
        }

        if (mCurrentIndex == 0) {
            mPreviousBtn.setAlpha(0.2f);
            mPreviousBtn.setClickable(false);
        } else {
            mPreviousBtn.setAlpha(1f);
            mPreviousBtn.setClickable(true);
        }
        if (mCurrentIndex == mIdArray.length - 1) {
            mNextBtn.setAlpha(0.2f);
            mNextBtn.setClickable(false);
        } else {
            mNextBtn.setAlpha(1f);
            mNextBtn.setClickable(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationHelper.overridePendingTransition(this);
    }

    private void showShareMenu() {
        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(this);
        final List<Integer> contentIdList = new ArrayList<>();
        if (SPManager.getBoolean(KEY_SWITCH_SHARE_WECHAT, true)
                && AppUtil.isAppInstalled(this, Constants.PACKAGE_NAME_WECHAT)) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_menu_wechat);
            drawable.setColorFilter(getResources().getColor(R.color.main_grey_normal), PorterDuff.Mode.SRC_IN);
            adapter.add(new MaterialSimpleListItem.Builder(this)
                    .content(R.string.wechat)
                    .icon(drawable)
                    .backgroundColor(Color.WHITE)
                    .build());
            contentIdList.add(R.string.wechat);
        }
        if (SPManager.getBoolean(KEY_SWITCH_SHARE_MOMENT, true)
                && AppUtil.isAppInstalled(this, Constants.PACKAGE_NAME_WECHAT)) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_menu_moment);
            drawable.setColorFilter(getResources().getColor(R.color.main_grey_normal), PorterDuff.Mode.SRC_IN);
            adapter.add(new MaterialSimpleListItem.Builder(this)
                    .content(R.string.moment)
                    .icon(drawable)
                    .backgroundColor(Color.WHITE)
                    .build());
            contentIdList.add(R.string.moment);
        }
        if (SPManager.getBoolean(KEY_SWITCH_SHARE_WEIBO, true)
                && AppUtil.isAppInstalled(this, Constants.PACKAGE_NAME_WEIBO)) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_menu_weibo);
            drawable.setColorFilter(getResources().getColor(R.color.main_grey_normal), PorterDuff.Mode.SRC_IN);
            adapter.add(new MaterialSimpleListItem.Builder(this)
                    .content(R.string.weibo)
                    .icon(drawable)
                    .backgroundColor(Color.WHITE)
                    .build());
            contentIdList.add(R.string.weibo);
        }
        if (SPManager.getBoolean(KEY_SWITCH_SHARE_INSTAPAPER, true)
                && AppUtil.isAppInstalled(this, Constants.PACKAGE_NAME_INSTAPAPER)) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_menu_instapaper);
            drawable.setColorFilter(getResources().getColor(R.color.main_grey_normal), PorterDuff.Mode.SRC_IN);
            adapter.add(new MaterialSimpleListItem.Builder(this)
                    .content(R.string.instapaper)
                    .icon(drawable)
                    .backgroundColor(Color.WHITE)
                    .build());
            contentIdList.add(R.string.instapaper);
        }
        if (SPManager.getBoolean(KEY_SWITCH_SHARE_POCKET, true)
                && AppUtil.isAppInstalled(this, Constants.PACKAGE_NAME_POCKET)) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_menu_pocket);
            drawable.setColorFilter(getResources().getColor(R.color.main_grey_normal), PorterDuff.Mode.SRC_IN);
            adapter.add(new MaterialSimpleListItem.Builder(this)
                    .content(R.string.pocket)
                    .icon(drawable)
                    .backgroundColor(Color.WHITE)
                    .build());
            contentIdList.add(R.string.pocket);
        }
        if (SPManager.getBoolean(KEY_SWITCH_SHARE_EVERNOTE, true)
                && AppUtil.isAppInstalled(this, Constants.PACKAGE_NAME_EVERNOTE)) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_menu_evernote);
            drawable.setColorFilter(getResources().getColor(R.color.main_grey_normal), PorterDuff.Mode.SRC_IN);
            adapter.add(new MaterialSimpleListItem.Builder(this)
                    .content(R.string.evernote)
                    .icon(drawable)
                    .backgroundColor(Color.WHITE)
                    .build());
            contentIdList.add(R.string.evernote);
        }

        if (SPManager.getBoolean(KEY_SWITCH_SHARE_MORE, true)) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_menu_more);
            drawable.setColorFilter(getResources().getColor(R.color.main_grey_normal), PorterDuff.Mode.SRC_IN);
            adapter.add(new MaterialSimpleListItem.Builder(this)
                    .content(R.string.more)
                    .icon(drawable)
                    .backgroundColor(Color.WHITE)
                    .build());
            contentIdList.add(R.string.more);
        }

        if (contentIdList.size() == 0 ||
                (contentIdList.size() == 1 &&contentIdList.get(0) == R.string.more)) {
            mShareHelper.shareToOthers(mArticle);
            StatManager.statEvent(ArticleActivity.this, StatManager.EVENT_SHARE_ITEM_CLICK, StatManager.TAG_SHARE_OTHERS);
            return;
        }

        new MaterialDialog.Builder(this)
                .title(R.string.share_to)
                .adapter(adapter, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (contentIdList.get(which)) {
                            case R.string.wechat:
                                mShareHelper.shareToWechat(mArticle);
                                StatManager.statEvent(ArticleActivity.this, StatManager.EVENT_SHARE_ITEM_CLICK, StatManager.TAG_SHARE_WECHAT);
                                break;
                            case R.string.moment:
                                mShareHelper.shareToMoment(mArticle);
                                StatManager.statEvent(ArticleActivity.this, StatManager.EVENT_SHARE_ITEM_CLICK, StatManager.TAG_SHARE_MOMENT);
                                break;
                            case R.string.weibo:
                                mShareHelper.shareToWeibo(mArticle);
                                StatManager.statEvent(ArticleActivity.this, StatManager.EVENT_SHARE_ITEM_CLICK, StatManager.TAG_SHARE_WEIBO);
                                break;
                            case R.string.instapaper:
                                mShareHelper.shareToApp(mArticle, Constants.PACKAGE_NAME_INSTAPAPER);
                                StatManager.statEvent(ArticleActivity.this, StatManager.EVENT_SHARE_ITEM_CLICK, StatManager.TAG_SHARE_INSTAPAPER);
                                break;
                            case R.string.pocket:
                                mShareHelper.shareToApp(mArticle, Constants.PACKAGE_NAME_POCKET);
                                StatManager.statEvent(ArticleActivity.this, StatManager.EVENT_SHARE_ITEM_CLICK, StatManager.TAG_SHARE_POCKET);
                                break;
                            case R.string.evernote:
                                mShareHelper.shareToApp(mArticle, Constants.PACKAGE_NAME_EVERNOTE);
                                StatManager.statEvent(ArticleActivity.this, StatManager.EVENT_SHARE_ITEM_CLICK, StatManager.TAG_SHARE_EVERNOTE);
                                break;
                            case R.string.more:
                                mShareHelper.shareToOthers(mArticle);
                                StatManager.statEvent(ArticleActivity.this, StatManager.EVENT_SHARE_ITEM_CLICK, StatManager.TAG_SHARE_OTHERS);
                                break;

                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onClick(View v) {
        if (!mIsClickEnabled) {
            return;
        }
        recordClick();

        if (v == mPreviousBtn) {
            if (!mIsLoading) {
                loadDataAsync(mIdArray[--mCurrentIndex]);
            }
        } else if (v == mNextBtn) {
            if (!mIsLoading) {
                loadDataAsync(mIdArray[++mCurrentIndex]);
            }
        }
    }

    private void recordClick() {
        mIsClickEnabled = false;
        ThreadManager.postDelay(new Runnable() {
            @Override
            public void run() {
                mIsClickEnabled = true;
            }
        }, 500);
    }
}
