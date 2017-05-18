package com.feeder.domain.model;

import com.feeder.common.StringUtil;
import com.feeder.domain.DBManager;
import com.feeder.domain.inoreader.InoSubscriptionModel;
import com.feeder.domain.inoreader.InoreaderManager;
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
public abstract class SubscriptionModel extends BaseModel {
    private static List<DataObserver> mObserverList = new ArrayList<>();
    private static SubscriptionModel sLocalModel;
    private static SubscriptionModel sInoModel;
    protected static List<Subscription> mSubscriptionList = new ArrayList<>();

    public static SubscriptionModel getInstance() {
        // TODO: 17/05/2017 bad smell
        if (StringUtil.equals(AccountModel.getInstance().getCurrentAccount().getExt2(), InoreaderManager.TYPE)) {
            if (sInoModel == null) {
                sInoModel = new InoSubscriptionModel();
            }
            return sInoModel;
        } else {
            if (sLocalModel == null) {
                sLocalModel = new LocalSubscriptionModel();
            }
            return sLocalModel;
        }
    }

    @Override
    public List<Subscription> getDataSource() {
        return mSubscriptionList;
    }

    @Override
    protected DataType getDataType() {
        return DataType.SUBSCRIPTION;
    }

    public void insert(Subscription... subscriptions) {
        insert(Arrays.asList(subscriptions));
    }

    @Override
    public List<DataObserver> getObserverList() {
        return mObserverList;
    }

    public abstract void insert(final List<Subscription> subscriptions);

    public abstract void updateArticleInfo();

    public abstract void delete(final Subscription subscription);

    public List<Subscription> queryByAccountIdSync(long accountId) {
        QueryBuilder qb = DBManager.getSubscriptionDao().queryBuilder();
        qb.where(SubscriptionDao.Properties.AccountId.eq(accountId));
        return qb.list();
    }
}
