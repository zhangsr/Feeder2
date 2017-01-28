package com.feeder.domain;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feeder.common.LogUtil;
import com.feeder.common.ThreadManager;
import com.feeder.model.Article;
import com.feeder.model.ArticleDao;
import com.feeder.model.Subscription;
import com.feeder.model.SubscriptionDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 10/22/16
 */

public class ArticleController extends BaseController {
    public static final long ID_FAV = -2L;
    private static final long ID_UNKNOWN = -1L;
    private static final long ID_ALL = 0L;
    private static ArticleController sArticleController;
    private List<Article> mArticleList = new ArrayList<>();
    private long mCurrentSubscriptionId = ID_UNKNOWN;

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
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                mCurrentSubscriptionId = ID_ALL;
                mArticleList.clear();
                // TODO: 11/15/16 too heavy ?
                mArticleList.addAll(DBManager.getArticleDao().queryBuilder().where(
                        ArticleDao.Properties.Trash.eq(false)).list());
                ArticleController.this.notifyAll(ResponseState.SUCCESS);
            }
        });
    }

    @Override
    protected DataType getDataType() {
        return DataType.ARTICLE;
    }

    public void requestData(final long subscriptionId) {
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                mCurrentSubscriptionId = subscriptionId;
                // TODO: 1/17/17 clear data in bg may cause IndexOutOfBound
                mArticleList.clear();
                updateMemoryIfNeed(subscriptionId, queryBySubscriptionIdSync(subscriptionId), false);
            }
        });
    }

    public void requestFav() {
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                mCurrentSubscriptionId = ID_FAV;
                mArticleList.clear();
                updateMemoryIfNeed(ID_FAV, queryFav(), false);
            }
        });
    }

    private List<Article> queryFav() {
        return DBManager.getArticleDao().queryBuilder().where(
                ArticleDao.Properties.Favorite.eq(true))
                .orderDesc(ArticleDao.Properties.Published).list();
    }

    List<Article> queryBySubscriptionIdSync(long subscriptionId) {
        QueryBuilder qb = DBManager.getArticleDao().queryBuilder();
        qb.where(ArticleDao.Properties.SubscriptionId.eq(subscriptionId),
                ArticleDao.Properties.Trash.eq(false),
                qb.or(ArticleDao.Properties.Favorite.eq(false),
                        ArticleDao.Properties.Favorite.isNull()))
                .orderDesc(ArticleDao.Properties.Published);
        return qb.list();
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

    void requestNetwork(final Subscription subscription) {
        if (subscription == null) {
            ArticleController.this.notifyAll(ResponseState.ERROR);
            return;
        }
        ArticleListRequest request = new ArticleListRequest(
                subscription,
                new Response.Listener<List<Article>>() {
                    @Override
                    public void onResponse(List<Article> response) {
                        LogUtil.d("onResponse : " + subscription.getTitle());
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
        LogUtil.d("request : " + subscription.getTitle());
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
                updateMemoryIfNeed(subscriptionId, newArticleList, true);
            }
        });
    }

    private void updateMemoryIfNeed(long subscriptionId, List<Article> articleList, boolean force) {
        if (!force) {
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
        } else {
            ArticleController.this.notifyAll(ResponseState.SUCCESS);
        }
    }

    public void markAllRead(final boolean read, final long subscriptionId) {
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                markAllRead(read, queryBySubscriptionIdSync(subscriptionId));
            }
        });
    }
    
    public void markAllRead(boolean read, Article... articles) {
        if (articles == null) {
            return;
        }
        List<Article> articleList = Arrays.asList(articles);
        markAllRead(read, articleList);
    }

    public void markAllRead(final boolean read, final List<Article> articleList) {
        if (articleList == null || articleList.size() == 0) {
            return;
        }
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                for (Article article : articleList) {
                    article.setRead(read);
                }
                DBManager.getArticleDao().updateInTx(articleList);
                updateMemoryIfNeed(articleList.get(0).getSubscriptionId(), mArticleList, true);
            }
        });
    }

    public void saveArticle(final Article article) {
        if (article == null) {
            return;
        }
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                DBManager.getArticleDao().update(article);
            }
        });
    }

    /**
     * Mark trashed to avoid pull again
     */
    public void markReadTrash() {
        // TODO: 11/9/16 print key value, ex: time cost by delete
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                QueryBuilder qb = DBManager.getArticleDao().queryBuilder();
                qb.where(
                        ArticleDao.Properties.Read.eq(true),
                        ArticleDao.Properties.Trash.eq(false),
                        qb.or(ArticleDao.Properties.Favorite.eq(false),
                                ArticleDao.Properties.Favorite.isNull()));
                List<Article> articleListToTrash = qb.list();
                for (Article article : articleListToTrash) {
                    article.setTrash(true);
                }
                DBManager.getArticleDao().updateInTx(articleListToTrash);
            }
        });
    }
}
