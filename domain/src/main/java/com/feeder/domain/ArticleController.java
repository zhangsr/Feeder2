package com.feeder.domain;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feeder.common.ThreadManager;
import com.feeder.model.Article;
import com.feeder.model.Subscription;

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

    private ArticleController(){}

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
                Article a1 = new Article();
                a1.setDescription("Test");
                mArticleList.add(a1);
                ArticleController.this.notifyAll(ResponseState.SUCCESS);
            }
        }, 1000);
    }

    public void requestUpdate(Subscription subscription) {
        if (subscription == null) {
            ArticleController.this.notifyAll(ResponseState.ERROR);
            return;
        }
        ArticleListRequest request = new ArticleListRequest(
                subscription.getUrl(),
                new Response.Listener<List<Article>>() {
                    @Override
                    public void onResponse(List<Article> response) {

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
}
