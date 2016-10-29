package com.feeder.domain;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feeder.common.ThreadManager;
import com.feeder.model.Article;
import com.feeder.model.Subscription;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 10/22/16
 */

public class ArticleController extends BaseController {
    private static ArticleController sArticleController;
    private List<Article> mArticleList = new ArrayList<>();
    private Query<Article> mQuery;

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

    @Override
    public void requestUpdate() {
        ThreadManager.postDelay(new Runnable() {
            @Override
            public void run() {
                mArticleList.clear();
                mArticleList.addAll(mQuery.list());

                ArticleController.this.notifyAll(ResponseState.SUCCESS);
            }
        }, 1000);
    }

    public void requestNetwork(Subscription subscription) {
        if (subscription == null) {
            ArticleController.this.notifyAll(ResponseState.ERROR);
            return;
        }
        ArticleListRequest request = new ArticleListRequest(
                subscription,
                new Response.Listener<List<Article>>() {
                    @Override
                    public void onResponse(List<Article> response) {
                        // TODO: 10/29/16 remove dulplicated
                        response.size();
//                        insert(response);
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

    public void insert(final List<Article> articleList) {
        if (articleList == null) {
            return;
        }
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                DBManager.getArticleDao().insertOrReplaceInTx(articleList);
                ArticleController.this.notifyAll(ResponseState.SUCCESS);
                // TODO: 10/18/16 how about error ?
            }
        });
    }
}
