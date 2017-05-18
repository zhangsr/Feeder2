package com.feeder.android.view;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.feeder.common.StringUtil;
import com.feeder.common.URLUtil;
import com.feeder.domain.inoreader.InoreaderManager;
import com.feeder.domain.net.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

import me.zsr.feeder.BuildConfig;

/**
 * @description:
 * @author: Match
 * @date: 7/28/16
 */
public class AuthInoreaderActivity extends BaseActivity {
    public static final int AUTH_SUCCESS = 1;
    public static final int AUTH_FAILED = 0;
    public static final String SCOPE = "read write";
    public String CSRF_PROTECTION_STRING = "342342";
    public String mRedirectUrl = "https://baidu.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: 8/9/16 progressbar
        initSystemBar();
        WebView webview = new WebView(this);
        setContentView(webview);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (StringUtil.equals(URLUtil.getHost(url), URLUtil.getHost(mRedirectUrl))) {
                    onRedirect(url);
                    return false;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(AuthInoreaderActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        webview.loadUrl("https://www.inoreader.com/oauth2/auth?" +
                "client_id=" + InoreaderManager.CLIENT_ID +
                "&redirect_uri=" + mRedirectUrl +
                "&response_type=code" +
                "&scope=" + SCOPE +
                "&state=" + CSRF_PROTECTION_STRING);
    }

    private void onRedirect(String url) {
        String authCode = URLUtil.getParam(url, "code");
        String state = URLUtil.getParam(url, "state");

        if (StringUtil.equals(state, CSRF_PROTECTION_STRING)) {
            requestToken(authCode);
        } else {
            // TODO: 8/11/16 Show an error message to the user and ask them to try again
        }
    }

    private void requestToken(final String authCode) {
        StringRequest tokenRequest = new StringRequest(Request.Method.POST,
                "https://www.inoreader.com/oauth2/token", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean success = InoreaderManager.getInstance().saveToken(response);
                setResult(success ? AUTH_SUCCESS : AUTH_FAILED);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("code", authCode);
                params.put("redirect_uri", mRedirectUrl);
                params.put("client_id", InoreaderManager.CLIENT_ID);
                params.put("client_secret", BuildConfig.INOREADER_CLIENT_SECRET);
                params.put("scope", SCOPE);
                params.put("grant_type", "authorization_code");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        VolleySingleton.getInstance().addToRequestQueue(tokenRequest);
    }
}
