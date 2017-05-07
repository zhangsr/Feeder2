package com.feeder.domain.model;

import com.feeder.common.ThreadManager;
import com.feeder.domain.DBManager;
import com.feeder.domain.RefreshManager;
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
 * @date: 8/8/16
 */
public class SubscriptionModel extends BaseModel {
    private static SubscriptionModel sModel;
    private List<Subscription> mSubscriptionList = new ArrayList<>();

    public static SubscriptionModel getInstance() {
        if (sModel == null) {
            sModel = new SubscriptionModel();
        }
        return sModel;
    }

    @Override
    public List<Subscription> getDataSource() {
        return mSubscriptionList;
    }

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
    protected DataType getDataType() {
        return DataType.SUBSCRIPTION;
    }

    public void insert(Subscription... subscriptions) {
        insert(Arrays.asList(subscriptions));
    }

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
                            subscription.setTotalCount(totalCount);
                            subscription.setUnreadCount(unreadCount);
                        }
                    });
                }
                ThreadManager.post(new Runnable() {
                    @Override
                    public void run() {
                        SubscriptionModel.this.notifyAll(ResponseState.SUCCESS);
                    }
                });
            }
        });
    }

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
                        SubscriptionModel.this.notifyAll(ResponseState.SUCCESS);
                    }
                });
            }
        });
    }

    List<Subscription> queryByAccountIdSync(long accountId) {
        QueryBuilder qb = DBManager.getSubscriptionDao().queryBuilder();
        qb.where(SubscriptionDao.Properties.AccountId.eq(accountId));
        return qb.list();
    }
}
