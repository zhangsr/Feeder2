package com.feeder.domain;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.feeder.model.Article;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 10/22/16
 */

public class ArticleListRequest extends Request<List<Article>> {
    private final Response.Listener<List<Article>> mListener;

    public ArticleListRequest(String url,  Response.Listener<List<Article>> mListener, Response.ErrorListener listener) {
        this(Method.GET, url, listener, mListener);
    }

    private ArticleListRequest(int method, String url, Response.ErrorListener listener, Response.Listener<List<Article>> mListener) {
        super(method, url, listener);
        this.mListener = mListener;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        String responseStr;
        try {
            responseStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            responseStr = new String(response.data);
        }
        // TODO: 10/22/16 XML string parse
        return null;
    }

    @Override
    protected void deliverResponse(List<Article> response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }
}
