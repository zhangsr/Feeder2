package com.feeder.domain;

import com.feeder.common.ThreadManager;
import com.feeder.model.Subscription;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 8/8/16
 */
public class SubscriptionController extends BaseController {
    private static SubscriptionController sAccountController;
    private List<Subscription> mSubscriptionList = new ArrayList<>();
    private Query<Subscription> mQuery;

    private SubscriptionController(){
        mQuery = DBManager.getSubscriptionDao().queryBuilder().build();
    }

    public static SubscriptionController getInstance() {
        if (sAccountController == null) {
            sAccountController = new SubscriptionController();
        }
        return sAccountController;
    }

    @Override
    public List<Subscription> getDataSource() {
        return mSubscriptionList;
    }

    @Override
    public void requestUpdate() {
        ThreadManager.postDelay(new Runnable() {
            @Override
            public void run() {
                mSubscriptionList.clear();
                mSubscriptionList.addAll(mQuery.list());
                // TODO: 10/22/16 network sync
                // TODO: 10/28/16 do in background
                SubscriptionController.this.notifyAll(ResponseState.SUCCESS);
            }
        }, 1000);
    }

    public void insert(final Subscription subscription) {
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                DBManager.getSubscriptionDao().insertOrReplace(subscription);
                SubscriptionController.this.notifyAll(ResponseState.SUCCESS);
                // TODO: 10/18/16 how about error ?
            }
        });
    }
}
