package com.feeder.android.util;

import android.content.Context;

import com.feeder.model.Article;
import com.google.common.base.Strings;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.EmptyStackException;

/**
 * @description:
 * @author: Match
 * @date: 1/27/17
 */

public class ArticleUtil {

    // TODO: 1/27/17 context manager
    public static void setContent(Context context, Article article, HtmlTextView textView, String subscriptionName) {
        if (article == null || textView == null) {
            return;
        }

        try {
            if (Strings.isNullOrEmpty(article.getContent())) {
                if (!Strings.isNullOrEmpty(article.getDescription())) {
                    // TODO: 1/27/17 rewrite ImageGetter
                    textView.setHtml(article.getDescription(), new HtmlHttpImageGetter(textView, null, true));
                }
            } else {
                textView.setHtml(article.getContent(), new HtmlHttpImageGetter(textView, null, true));
            }
        } catch (IndexOutOfBoundsException e) {
            StatManager.statEvent(context, StatManager.EXCEPTION_SET_HTML,
                    "subscription=" + subscriptionName + ", desc=" + article.getDescription());
        } catch (EmptyStackException e) {
            StatManager.statEvent(context, StatManager.EXCEPTION_PARSE_HTML,
                    "subscription=" + subscriptionName + ", desc=" + article.getDescription()
                            + ", message=" + e.getMessage());
        }
    }

    public static String getContent(Article article) {
        if (!Strings.isNullOrEmpty(article.getContent())) {
            return article.getContent();
        }
        return article.getDescription();
    }
}
