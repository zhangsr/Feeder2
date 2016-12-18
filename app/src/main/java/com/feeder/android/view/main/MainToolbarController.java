package com.feeder.android.view.main;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.feeder.android.util.StatManager;
import com.feeder.common.LogUtil;
import com.feeder.common.StringUtil;
import com.feeder.common.ThreadManager;
import com.feeder.domain.FeedlyUtils;
import com.feeder.domain.SubscriptionController;
import com.feeder.domain.VolleySingleton;
import com.feeder.model.FeedlyResult;
import com.feeder.model.Subscription;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 11/20/16
 */

// TODO: 11/15/16 move domain oper to domain module
public class MainToolbarController {
    private static final String TAG = MainToolbarController.class.getSimpleName();
    private Activity mActivity;
    private Toolbar mToolbar;
    private ArrayAdapter<String> mResultAdapter;
    private List<FeedlyResult> mResultList;
    private boolean mCanSearch = true;
    private ListView mResultListView;
    private View mDimLayer;
    private MaterialSearchView mSearchView;
    private String mCurrentSearchText;

    public MainToolbarController(Activity activity) {
        mActivity = activity;
        initToolbar();
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    private void initToolbar() {
        mDimLayer = mActivity.findViewById(R.id.toolbar_dim_layer);
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);

        mSearchView = (MaterialSearchView) mActivity.findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCurrentSearchText = newText;
                if (!TextUtils.isEmpty(newText)) {
                    try {
                        if (mCanSearch) {
                            disableSearchForAWhile();
                            searchFor(newText);
                        }
                    } catch (URISyntaxException | MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else {
                    mResultListView.setVisibility(View.GONE);
                }
                return true;
            }
        });
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
                mResultListView.setVisibility(View.GONE);
                hideDimLayer();
            }
        });

        mResultListView = (ListView) mActivity.findViewById(R.id.result_lv);
        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final FeedlyResult result = mResultList.get(position);
                Subscription subscription = FeedlyUtils.result2Subscription(result);
                SubscriptionController.getInstance().insert(subscription);
                mSearchView.closeSearch();
                StatManager.statEvent(mActivity, StatManager.EVENT_SEARCH_RESULT_CLICK);
            }
        });
    }

    private void disableSearchForAWhile() {
        mCanSearch = false;
        ThreadManager.postDelay(new Runnable() {
            @Override
            public void run() {
                mCanSearch = true;
            }
        }, 500);
    }

    private void searchFor(final String input) throws URISyntaxException, MalformedURLException {
        List<BasicNameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair("query", input));
        final String requestUrl = URIUtils.createURI("http", "cloud.feedly.com", -1,
                "/v3/search/feeds", URLEncodedUtils.format(params, "utf-8"), null).toString();
        VolleySingleton.getInstance().getRequestQueue().cancelAll(this);
        StringRequest request = new StringRequest(requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.d("onResponse=" + requestUrl);
                        if (!StringUtil.equals(input, mCurrentSearchText)) {
                            return;
                        }

                        try {
                            List<String> titleList = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            String json = jsonObject.getString("results");
                            Type listType = new TypeToken<List<FeedlyResult>>() {}.getType();
                            mResultList = new Gson().fromJson(json, listType);

                            for (FeedlyResult result : mResultList) {
                                titleList.add(result.title);
                            }

                            if (titleList.size() > 0) {
                                mResultListView.setVisibility(View.VISIBLE);
                                mResultAdapter = new ArrayAdapter<>(mActivity,
                                        R.layout.result_list_item, R.id.result_txt,
                                        titleList.toArray(new String[titleList.size()]));
                                mResultListView.setAdapter(mResultAdapter);
                            } else {
                                mResultListView.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        request.setTag(this);
        LogUtil.d("request=" + requestUrl);
        VolleySingleton.getInstance().addToRequestQueue(request);
    }

    public void onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mSearchView.showSearch();
                showDimLayer();
                StatManager.statEvent(mActivity, StatManager.EVENT_MENU_SEARCH_CLICK);
                return true;
            }
        });
    }

    private void showDimLayer() {
        ObjectAnimator.ofFloat(mDimLayer, "alpha", 0f, 1f).start();
        mDimLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchView.isSearchOpen()) {
                    mSearchView.closeSearch();
                }
            }
        });
    }

    private void hideDimLayer() {
        ObjectAnimator.ofFloat(mDimLayer, "alpha", 1f, 0f).start();
        mDimLayer.setOnClickListener(null);
        mDimLayer.setClickable(false);
    }

    public boolean handleBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
            return true;
        }
        return false;
    }
}
