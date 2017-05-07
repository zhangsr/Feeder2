package com.feeder.android.view.main;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.feeder.android.util.StatManager;
import com.feeder.common.LogUtil;
import com.feeder.common.StringUtil;
import com.feeder.common.ThreadManager;
import com.feeder.domain.FeedlyUtils;
import com.feeder.domain.model.AccountModel;
import com.feeder.domain.model.SubscriptionModel;
import com.feeder.domain.net.SubscriptionRequest;
import com.feeder.domain.net.VolleySingleton;
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
    private String mCurrentSearchText = "";
    private LinearLayout mHeaderContainer;
    private TextView mAddCustomSourceTextView;
    private MainToolbarUIListener mListener;

    private static final String[] BLACK_LIST = new String[] {
        "http://feeds.feedburner.com/zhihu-daily"
    };

    public MainToolbarController(Activity activity, MainToolbarUIListener listener) {
        mActivity = activity;
        mListener = listener;
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
        mSearchView.setHint(mActivity.getString(R.string.add_subscription_hint));
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
                    if (likeRssAddr(newText)) {
                        mAddCustomSourceTextView.setVisibility(View.VISIBLE);
                        mAddCustomSourceTextView.setText("添加源 \"" + newText + "\"");
                    } else {
                        mAddCustomSourceTextView.setVisibility(View.GONE);
                    }
                    try {
                        if (mCanSearch) {
                            disableSearchForAWhile();
                            searchByFeedly(newText);
                        }
                    } catch (URISyntaxException | MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                mResultListView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchViewClosed() {
                mResultListView.setAdapter(null);
                mAddCustomSourceTextView.setVisibility(View.GONE);
                mResultListView.setVisibility(View.GONE);
                hideDimLayer();
            }
        });

        mHeaderContainer = (LinearLayout) mActivity.findViewById(R.id.custom_container);
        mAddCustomSourceTextView = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.result_list_item, null);
        mAddCustomSourceTextView.setVisibility(View.GONE);
        mHeaderContainer.addView(mAddCustomSourceTextView);

        mAddCustomSourceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                // TODO: 2/24/17 handle https
                if (mCurrentSearchText.startsWith("http://")) {
                    url = mCurrentSearchText;
                } else if (mCurrentSearchText.startsWith("https://")) {
                    url = mCurrentSearchText.replace("https", "http");
                } else {
                    url = "http://" + mCurrentSearchText;
                }
                addByUrl(url);
                StatManager.statEvent(mActivity, StatManager.EVENT_CUSTOM_SOURCE_CLICK);
                mSearchView.closeSearch();
            }
        });

        mResultListView = (ListView) mActivity.findViewById(R.id.result_lv);
        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mResultList.size() - 1 < position) {
                    StatManager.statEvent(mActivity, StatManager.EXCEPTION_SILENT_HANDLE, "Click Out Of Bound");
                    return;
                }
                final FeedlyResult result = mResultList.get(position);
                Subscription subscription = FeedlyUtils.result2Subscription(result,
                        AccountModel.getInstance().getCurrentAccount().getId());
                SubscriptionModel.getInstance().insert(subscription);
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
        }, 100);
    }

    private void searchByFeedly(final String input) throws URISyntaxException, MalformedURLException {
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
                            filterResult(mResultList);

                            for (FeedlyResult result : mResultList) {
                                titleList.add(result.title);
                            }

                            if (titleList.size() > 0) {
                                mResultAdapter = new ArrayAdapter<>(mActivity,
                                        R.layout.result_list_item, R.id.result_txt,
                                        titleList.toArray(new String[titleList.size()]));
                                mResultListView.setAdapter(mResultAdapter);
                            } else {
                                mResultListView.setAdapter(null);
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

    private void addByUrl(final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        VolleySingleton.getInstance().getRequestQueue().cancelAll(this);
        SubscriptionRequest request = new SubscriptionRequest(url, new Response.Listener<Subscription>() {
            @Override
            public void onResponse(Subscription response) {
                if (response == null) {
                    return;
                }
                response.setUrl(url);
                SubscriptionModel.getInstance().insert(response);
                Toast.makeText(mActivity, R.string.add_custom_success, Toast.LENGTH_SHORT).show();
                StatManager.statEvent(mActivity, StatManager.EVENT_CUSTOM_SOURCE_ADD_SUCCESS, url);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: 2/24/17 optimize retry by adding "feed" suffix
                Toast.makeText(mActivity, R.string.add_custom_failed, Toast.LENGTH_SHORT).show();
                StatManager.statEvent(mActivity, StatManager.EVENT_CUSTOM_SOURCE_ADD_FAILED, url);
            }
        });
        request.setTag(this);
        LogUtil.d("request=" + url);
        VolleySingleton.getInstance().addToRequestQueue(request);
    }

    public void onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Delay to fix imm not show
                ThreadManager.postDelay(new Runnable() {
                    @Override
                    public void run() {
                        mSearchView.showSearch();
                        showDimLayer();
                        StatManager.statEvent(mActivity, StatManager.EVENT_MENU_SEARCH_CLICK);
                    }
                }, 100);
                return true;
            }
        });
        menu.findItem(R.id.import_impl).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mListener.onImportOPMLClick();
                return true;
            }
        });
    }

    private void filterResult(List<FeedlyResult> resultList) {
        List<FeedlyResult> removeList = new ArrayList<>();
        for (FeedlyResult result : resultList) {
            for (String s : BLACK_LIST) {
                if (s.equals(result.feedId.substring(5))) {
                    removeList.add(result);
                }
            }
        }
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

    private boolean likeRssAddr(String str) {
        return Patterns.WEB_URL.matcher(str).matches();
    }

    public interface MainToolbarUIListener {
        void onImportOPMLClick();
    }
}
