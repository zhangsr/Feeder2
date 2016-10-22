package com.feeder.domain;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feeder.model.Article;
import com.feeder.model.Subscription;

import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 10/22/16
 */

public class ArticleController extends BaseController {

    @Override
    public List<?> getDataSource() {
        return null;
    }

    @Override
    public void requestUpdate() {

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
