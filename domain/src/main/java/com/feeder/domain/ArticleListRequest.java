package com.feeder.domain;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.feeder.model.Article;
import com.feeder.model.Subscription;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 10/22/16
 */

public class ArticleListRequest extends Request<List<Article>> {
    private final Response.Listener<List<Article>> mListener;
    private Subscription mSubscription;

    public ArticleListRequest(Subscription subscription, Response.Listener<List<Article>> mListener, Response.ErrorListener listener) {
        // TODO: 10/29/16 handle exception data
        this(Method.GET, subscription.getUrl(), listener, mListener);
        mSubscription = subscription;
    }

    private ArticleListRequest(int method, String url, Response.ErrorListener listener, Response.Listener<List<Article>> mListener) {
        super(method, url, listener);
        this.mListener = mListener;
    }

    @Override
    protected Response<List<Article>> parseNetworkResponse(NetworkResponse response) {
        String responseStr;
        try {
            responseStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            responseStr = new String(response.data);
        }
        List<Article> articleList = FeedParser.parse(responseStr);
        fillData(articleList);
        return Response.success(articleList, HttpHeaderParser.parseCacheHeaders(response));
    }

    private void fillData(List<Article> list) {
        for (Article article : list) {
            article.setSubscriptionId(mSubscription.getId());
        }
    }

    @Override
    protected void deliverResponse(List<Article> response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }
}
