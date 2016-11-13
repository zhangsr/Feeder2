package com.feeder.domain;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.feeder.model.Article;
import com.feeder.model.Subscription;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 10/22/16
 */

public class ArticleListRequest extends Request<List<Article>> {
    private static final String TAG = ArticleListRequest.class.getSimpleName();
    private final Response.Listener<List<Article>> mListener;
    private Subscription mSubscription;

    public ArticleListRequest(Subscription subscription, Response.Listener<List<Article>> mListener, Response.ErrorListener errorListener) {
        // TODO: 10/29/16 handle exception data
        this(Method.GET, subscription.getUrl(), errorListener, mListener);
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
        if (articleList == null || articleList.size() == 0) {
            return Response.error(new VolleyError("Parse result an empty article list"));
        }

        fillData(articleList);
        return Response.success(articleList, HttpHeaderParser.parseCacheHeaders(response));
    }

    private void fillData(List<Article> list) {
        if (list == null) {
            return;
        }
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

    @Override
    public void deliverError(VolleyError error) {
        Log.d(TAG, "deliverError " + error);

        final int status = error.networkResponse.statusCode;
        // Handle 30x
        if (HttpURLConnection.HTTP_MOVED_PERM == status
                || status == HttpURLConnection.HTTP_MOVED_TEMP
                || status == HttpURLConnection.HTTP_SEE_OTHER) {

            final String location = error.networkResponse.headers.get("Location");
            Log.d(TAG, "Location: " + location);
            mSubscription.setUrl(location);
            ArticleListRequest request = new ArticleListRequest(mSubscription, mListener, getErrorListener());
            // Construct a request clone and change the url to redirect location.
            VolleySingleton.getInstance().addToRequestQueue(request);
        }
    }
}
