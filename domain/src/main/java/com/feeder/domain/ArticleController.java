package com.feeder.domain;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feeder.common.ThreadManager;
import com.feeder.model.Article;
import com.feeder.model.ArticleDao;
import com.feeder.model.Subscription;
import com.feeder.model.SubscriptionDao;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 10/22/16
 */

public class ArticleController extends BaseController {
    private static final long ID_UNKNOWN = -1L;
    private static final long ID_ALL = 0L;
    private static ArticleController sArticleController;
    private List<Article> mArticleList = new ArrayList<>();
    private Query<Article> mQuery;
    private long mCurrentSubscriptionId = ID_UNKNOWN;

    private ArticleController(){
        mQuery = DBManager.getArticleDao().queryBuilder().build();
    }

    public static ArticleController getInstance() {
        if (sArticleController == null) {
            sArticleController = new ArticleController();
        }
        return sArticleController;
    }

    @Override
    public List<Article> getDataSource() {
        return mArticleList;
    }

    public List<Article> getDataSource(long subscriptionId) {
        if (mCurrentSubscriptionId != subscriptionId) {
            mCurrentSubscriptionId = subscriptionId;
            mArticleList.clear();
        }
        return mArticleList;
    }

    @Override
    public void requestData() {
        ThreadManager.postDelay(new Runnable() {
            @Override
            public void run() {
                mCurrentSubscriptionId = ID_ALL;
                mArticleList.clear();
                mArticleList.addAll(mQuery.list());
                ArticleController.this.notifyAll(ResponseState.SUCCESS);
            }
        }, 1000);
    }

    public void requestData(final long subscriptionId) {
        ThreadManager.postDelay(new Runnable() {
            @Override
            public void run() {
                mCurrentSubscriptionId = subscriptionId;
                mArticleList.clear();
                updateMemoryIfNeed(subscriptionId, DBManager.getArticleDao().queryBuilder().where(
                        ArticleDao.Properties.SubscriptionId.eq(subscriptionId)).list());
            }
        }, 1000);
    }

    public void requestNetwork(final long subscriptionId) {
        // TODO: 10/30/16 time interval limit for single subscription
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                List<Subscription> subscriptionList = DBManager.getSubscriptionDao().queryBuilder().where(
                        SubscriptionDao.Properties.Id.eq(subscriptionId)).list();
                if (subscriptionList.size() != 1) {
                    ArticleController.this.notifyAll(ResponseState.ERROR);
                    return;
                }
                requestNetwork(subscriptionList.get(0));
            }
        });
    }

    public void requestNetwork(final Subscription subscription) {
        if (subscription == null) {
            ArticleController.this.notifyAll(ResponseState.ERROR);
            return;
        }
        ArticleListRequest request = new ArticleListRequest(
                subscription,
                new Response.Listener<List<Article>>() {
                    @Override
                    public void onResponse(List<Article> response) {
                        insertToDB(subscription.getId(), response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        VolleySingleton.getInstance().addToRequestQueue(request);
    }


    private void insertToDB(final long subscriptionId, final List<Article> articleList) {
        if (articleList == null) {
            return;
        }
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                List<Article> dbArticleList = DBManager.getArticleDao().queryBuilder().where(
                        ArticleDao.Properties.SubscriptionId.eq(subscriptionId)).list();
                List<Article> newArticleList = new ArrayList<>();
                for (Article article : articleList) {
                    if (!dbArticleList.contains(article)) {
                        newArticleList.add(article);
                    }
                }
                if (newArticleList.size() != 0) {
                    // TODO: 10/18/16 how about error ?
                    DBManager.getArticleDao().insertInTx(newArticleList);
                }
                updateMemoryIfNeed(subscriptionId, newArticleList);
            }
        });
    }

    private void updateMemoryIfNeed(long subscriptionId, List<Article> articleList) {
        if (subscriptionId != mCurrentSubscriptionId) {
            return;
        }
        boolean changed = false;
        for (Article article : articleList) {
            if (!mArticleList.contains(article)) {
                mArticleList.add(article);
                changed = true;
            }
        }
        if (changed) {
            ArticleController.this.notifyAll(ResponseState.SUCCESS);
        } else {
            ArticleController.this.notifyAll(ResponseState.NO_CHANGE);
        }
    }
}
