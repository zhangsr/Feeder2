package com.feeder.android.views;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.feeder.common.StringUtil;
import com.feeder.common.URLUtil;

/**
 * @description:
 * @author: Match
 * @date: 7/28/16
 */
public class AuthInoreaderActivity extends BaseActivity {
    public String CSRF_PROTECTION_STRING = "342342";
    public String mRedirectUrl = "https://baidu.com";
    public String mClientId = "1000000978";

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
                "client_id=" + mClientId +
                "&redirect_uri=" + mRedirectUrl +
                "&response_type=code" +
                "&scope=read write" +
                "&state=" + CSRF_PROTECTION_STRING);
    }

    private void onRedirect(String url) {
        String authCode = URLUtil.getParam(url, "code");
        String state = URLUtil.getParam(url, "state");

        if (StringUtil.equals(state, CSRF_PROTECTION_STRING)) {

        } else {
            // TODO: 8/11/16 Show an error message to the user and ask them to try again
        }
    }
}
