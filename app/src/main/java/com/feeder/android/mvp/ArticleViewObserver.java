package com.feeder.android.mvp;

import android.view.View;

import com.feeder.model.Article;

/**
 * @description:
 * @author: Match
 * @date: 10/30/16
 */

public interface ArticleViewObserver {
    void onItemClick(View view, Article data);
    boolean onItemLongClick(View view, Article data);
}
