package com.feeder.android.view.articlelist;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.feeder.android.base.IArticlesView;
import com.feeder.android.base.MVPPresenter;
import com.feeder.android.presenter.ArticlesPresenter;
import com.feeder.android.util.Constants;
import com.feeder.android.view.BaseSwipeActivity;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 10/25/16
 */

public class ArticleListActivity extends BaseSwipeActivity {
    private MVPPresenter mArticlePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        LinearLayout container = (LinearLayout) findViewById(R.id.container);

        IArticlesView articlesView = new ArticlesView(this);
        LinearLayout.LayoutParams articlesViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        container.addView(articlesView, articlesViewLp);

        Long id = getIntent().getExtras().getLong(Constants.KEY_BUNDLE_SUBSCRIPTION_ID);
        mArticlePresenter = new ArticlesPresenter(this, articlesView, id);
        mArticlePresenter.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mArticlePresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mArticlePresenter.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}
