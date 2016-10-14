package com.feeder.domain;

import com.feeder.common.ThreadManager;
import com.feeder.model.Subscription;

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

    private SubscriptionController(){}

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
        // TODO: 7/22/16 Test
        ThreadManager.postDelay(new Runnable() {
            @Override
            public void run() {
                if (mSubscriptionList.size() == 0) {
                    mSubscriptionList.add(new Subscription());
                    mSubscriptionList.add(new Subscription());
                    mSubscriptionList.add(new Subscription());
                    mSubscriptionList.add(new Subscription());
                    mSubscriptionList.add(new Subscription());
                    mSubscriptionList.add(new Subscription());
                    SubscriptionController.this.notifyAll(ResponseState.SUCCESS);
                } else {
                    SubscriptionController.this.notifyAll(ResponseState.NO_CHANGE);
                }
            }
        }, 3000);
    }
}
