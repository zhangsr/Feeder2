package com.feeder.domain;

import com.feeder.common.ThreadManager;
import com.feeder.model.ArticleDao;
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
    public void requestData() {
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                mSubscriptionList.clear();
                mSubscriptionList.addAll(DBManager.getSubscriptionDao().loadAll());
                // TODO: 10/22/16 network sync
                // TODO: 10/28/16 do in background
                fillAndNotify();
            }
        });
    }

    public void insert(final Subscription subscription) {
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                DBManager.getSubscriptionDao().insertOrReplace(subscription);
                mSubscriptionList.add(subscription);
                fillAndNotify();
                ArticleController.getInstance().requestNetwork(subscription);
                // TODO: 10/18/16 how about error ?
            }
        });
    }

    /**
     * Must run in background
     */
    private void fillAndNotify() {
        for (Subscription subscription : mSubscriptionList) {
            long totalCount = DBManager.getArticleDao().queryBuilder().where(
                    ArticleDao.Properties.SubscriptionId.eq(subscription.getId())).count();
            long unreadCount = DBManager.getArticleDao().queryBuilder().where(
                    ArticleDao.Properties.SubscriptionId.eq(subscription.getId()),
                    ArticleDao.Properties.Read.eq(false)).count();
            subscription.setTotalCount(totalCount);
            subscription.setUnreadCount(unreadCount);
        }
        SubscriptionController.this.notifyAll(ResponseState.SUCCESS);
    }

    public void delete(final Subscription subscription) {
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                DBManager.getArticleDao().deleteInTx(ArticleController.getInstance()
                        .queryBySubscriptionIdSync(subscription.getId()));
                DBManager.getSubscriptionDao().delete(subscription);
                mSubscriptionList.remove(subscription);
                SubscriptionController.this.notifyAll(ResponseState.SUCCESS);
            }
        });
    }
}
