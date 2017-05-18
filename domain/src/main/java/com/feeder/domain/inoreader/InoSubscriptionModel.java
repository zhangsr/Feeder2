package com.feeder.domain.inoreader;

import com.feeder.common.ThreadManager;
import com.feeder.domain.model.ResponseState;
import com.feeder.domain.model.SubscriptionModel;
import com.feeder.model.Subscription;

import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 17/05/2017
 */

public class InoSubscriptionModel extends SubscriptionModel {

    @Override
    public void insert(List<Subscription> subscriptions) {

    }

    @Override
    public void updateArticleInfo() {
        ThreadManager.post(new Runnable() {
            @Override
            public void run() {
                InoSubscriptionModel.this.notifyAll(ResponseState.SUCCESS);
            }
        });
    }

    @Override
    public void delete(Subscription subscription) {

    }

    @Override
    public void requestData() {
        mSubscriptionList.clear();
        updateArticleInfo();
    }
}
