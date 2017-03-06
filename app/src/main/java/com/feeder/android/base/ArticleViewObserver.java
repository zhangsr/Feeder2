package com.feeder.android.base;

import android.view.View;

import com.feeder.model.Article;

import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 10/30/16
 */

public interface ArticleViewObserver {
    void onItemClick(View view, Article data);
    boolean onItemLongClick(View view, List<Article> dataList, int pos);
}
