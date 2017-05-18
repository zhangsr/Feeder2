package com.feeder.domain.model;

import android.util.Log;

import com.feeder.common.ThreadManager;
import com.feeder.domain.DBManager;
import com.feeder.domain.RefreshManager;
import com.feeder.model.ArticleDao;
import com.feeder.model.Subscription;
import com.feeder.model.SubscriptionDao;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 17/05/2017
 */

public class LocalSubscriptionModel extends SubscriptionModel {

    @Override
    public void requestData() {
        LOG_MA("requestData");
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                final List<Subscription> list = DBManager.getSubscriptionDao().queryBuilder().where(
                        SubscriptionDao.Properties.AccountId.eq(AccountModel.getInstance().getCurrentAccount().getId())).list();
                ThreadManager.post(new Runnable() {
                    @Override
                    public void run() {
                        mSubscriptionList.clear();
                        mSubscriptionList.addAll(list);
                        // TODO: 10/22/16 network sync
                        updateArticleInfo();
                    }
                });
            }
        });
    }

    @Override
    public void insert(final List<Subscription> subscriptions) {
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                DBManager.getSubscriptionDao().insertOrReplaceInTx(subscriptions);
                ThreadManager.post(new Runnable() {
                    @Override
                    public void run() {
                        mSubscriptionList.addAll(subscriptions);
                        updateArticleInfo();
                        RefreshManager.getInstance().refresh(subscriptions);
                        // TODO: 10/18/16 how about error ?
                    }
                });
            }
        });
    }

    @Override
    public void updateArticleInfo() {
        LOG_MA("updateArticleInfo");
        final List<Subscription> list = new ArrayList<>();
        list.addAll(mSubscriptionList);

        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                for (final Subscription subscription : list) {
                    final long totalCount = DBManager.getArticleDao().queryBuilder().where(
                            ArticleDao.Properties.SubscriptionId.eq(subscription.getId())).count();
                    final long unreadCount = DBManager.getArticleDao().queryBuilder().where(
                            ArticleDao.Properties.SubscriptionId.eq(subscription.getId()),
                            ArticleDao.Properties.Read.eq(false)).count();

                    ThreadManager.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("zhangsr", subscription.getTitle() + " unreadcount=" + unreadCount);
                            subscription.setTotalCount(totalCount);
                            subscription.setUnreadCount(unreadCount);
                        }
                    });
                }
                ThreadManager.post(new Runnable() {
                    @Override
                    public void run() {
                        LocalSubscriptionModel.this.notifyAll(ResponseState.SUCCESS);
                    }
                });
            }
        });
    }

    @Override
    public void delete(final Subscription subscription) {
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                DBManager.getArticleDao().deleteInTx(ArticleModel.getInstance()
                        .queryBySubscriptionIdSync(subscription.getId()));
                DBManager.getSubscriptionDao().delete(subscription);
                ThreadManager.post(new Runnable() {
                    @Override
                    public void run() {
                        mSubscriptionList.remove(subscription);
                        LocalSubscriptionModel.this.notifyAll(ResponseState.SUCCESS);
                    }
                });
            }
        });
    }

}
