package com.feeder.domain;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.feeder.common.LogUtil;
import com.feeder.common.StringUtil;
import com.feeder.model.Subscription;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

/**
 * @description:
 * @author: Match
 * @date: 10/22/16
 */

public class SubscriptionRequest extends Request<Subscription> {
    private static final String TAG = SubscriptionRequest.class.getSimpleName();
    private final Response.Listener<Subscription> mListener;

    public SubscriptionRequest(String url, Response.Listener<Subscription> mListener, Response.ErrorListener errorListener) {
        this(Method.GET, url, errorListener, mListener);
    }

    private SubscriptionRequest(int method, String url, Response.ErrorListener listener, Response.Listener<Subscription> mListener) {
        super(method, url, listener);
        this.mListener = mListener;
    }

    @Override
    protected Response<Subscription> parseNetworkResponse(NetworkResponse response) {
        String responseStr;
        try {
            responseStr = new String(response.data, StringUtil.guessEncoding(response.data));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            responseStr = new String(response.data);
        }
        Log.e(TAG, "responseStr=" + responseStr);
        Subscription subscription = FeedParser.parseSubscription(responseStr);
        if (subscription == null) {
            return Response.error(new VolleyError("Parse result an empty subscription"));
        }
        return Response.success(subscription, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(Subscription response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);

        LogUtil.d(TAG, "deliverError " + error);

        if (error == null || error.networkResponse == null) {
            return;
        }

        final int status = error.networkResponse.statusCode;
        // Handle 30x
        if (HttpURLConnection.HTTP_MOVED_PERM == status
                || status == HttpURLConnection.HTTP_MOVED_TEMP
                || status == HttpURLConnection.HTTP_SEE_OTHER) {

            final String location = error.networkResponse.headers.get("Location");
            Log.d(TAG, "Location: " + location);
            SubscriptionRequest request = new SubscriptionRequest(location, mListener, getErrorListener());
            // Construct a request clone and change the url to redirect location.
            VolleySingleton.getInstance().addToRequestQueue(request);
        }
    }
}
