package com.feeder.domain.inoreader;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.feeder.common.SPManager;
import com.feeder.domain.net.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: Match
 * @date: 8/11/16
 */
public class InoreaderManager {
    public static final String TYPE = "ino";
    public static final String CLIENT_ID = "1000000978";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRES_AT = "expires_at";
    private static final String KEY_TOKEN_TYPE = "token_type";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static InoreaderManager sManager;
    private String mClientSecret;

    private InoreaderManager(){}

    public static InoreaderManager getInstance() {
        if (sManager == null) {
            sManager = new InoreaderManager();
        }
        return sManager;
    }

    public void init(String clientSecret) {
        mClientSecret = clientSecret;
    }

    public boolean saveToken(String tokenInJson) {
        try {
            JSONObject jsonObject = new JSONObject(tokenInJson);
            String accessToken = jsonObject.getString(KEY_ACCESS_TOKEN);
            String expiresIn = jsonObject.getString("expires_in");
            long expiresAt = Long.parseLong(expiresIn) * 1000 + System.currentTimeMillis();
            String tokenType = jsonObject.getString(KEY_TOKEN_TYPE);
            String refreshToken = jsonObject.getString(KEY_REFRESH_TOKEN);

            // TODO: 11/05/2017 save in db ?
            SPManager.setString(KEY_ACCESS_TOKEN, accessToken);
            SPManager.setString(KEY_EXPIRES_AT, String.valueOf(expiresAt));
            SPManager.setString(KEY_TOKEN_TYPE, String.valueOf(tokenType));
            SPManager.setString(KEY_REFRESH_TOKEN, String.valueOf(refreshToken));

            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    // TODO: 11/05/2017 when to refresh ?
    private void refreshToken() {
        StringRequest tokenRequest = new StringRequest(Request.Method.POST,
                "https://www.inoreader.com/oauth2/token", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                InoreaderManager.getInstance().saveToken(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("client_id", CLIENT_ID);
                params.put("client_secret", mClientSecret);
                params.put("grant_type", "refresh_token");
                params.put("refresh_token", SPManager.getString(KEY_REFRESH_TOKEN, ""));

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

    public boolean isTokenExpire() {
        long expiresAt = Long.valueOf(SPManager.getString(KEY_EXPIRES_AT, "0"));
        long timeLeft = expiresAt - System.currentTimeMillis();
        return timeLeft < 0;
    }

    public void checkAndRefreshToken() {
        if (isTokenExpire()) {
            refreshToken();
        }
    }

    private String getTokenType() {
        return SPManager.getString(KEY_TOKEN_TYPE, "");
    }

    private String getAccessToken() {
        return SPManager.getString(KEY_ACCESS_TOKEN, "");
    }

    public void requestUserInfo(final UserInfoCallBack callBack) {
        if (isTokenExpire()) {
            refreshToken();
            return;
        }

        StringRequest userInfoRequest = new StringRequest(Request.Method.POST,
                InoreaderApi.USER_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                InoUserInfo userInfo = new Gson().fromJson(response, InoUserInfo.class);
                if (userInfo != null && callBack != null) {
                    callBack.onResult(userInfo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Authorization", getTokenType() + " " + getAccessToken());
                return params;
            }
        };
        VolleySingleton.getInstance().addToRequestQueue(userInfoRequest);
    }

    public interface UserInfoCallBack {
        void onResult(InoUserInfo userInfo);
    }
}
