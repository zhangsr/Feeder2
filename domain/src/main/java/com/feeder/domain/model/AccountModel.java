package com.feeder.domain.model;

import com.feeder.common.SPManager;
import com.feeder.common.ThreadManager;
import com.feeder.domain.AssertManager;
import com.feeder.domain.Constants;
import com.feeder.domain.DBManager;
import com.feeder.model.Account;
import com.feeder.model.ArticleDao;
import com.feeder.model.Subscription;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 7/22/16
 */
public class AccountModel extends BaseModel {
    public static final Long DEFAULT_ACCOUNT_ID = 0L;
    private static String sDefaultAccountName = "Default";
    private static AccountModel sModel;
    private List<Account> mAccountList = new ArrayList<>();
    private Account mCurrentAccount;

    private AccountModel(){
        // Mark : must do in UI thread to make account usable firtst
        final List<Account> accountList = DBManager.getAccountDao().loadAll();
        mAccountList.clear();
        mAccountList.addAll(accountList);
        if (mAccountList.size() == 0) {
            insertFirst();
        } else {
            int index = SPManager.getInt(Constants.KEY_ACCOUNT_INDEX, 0);
            mCurrentAccount = mAccountList.get(index);
        }
    }

    public static AccountModel getInstance() {
        if (sModel == null) {
            sModel = new AccountModel();
        }
        return sModel;
    }

    public static void setDefaultAccountName(String name) {
        sDefaultAccountName = name;
    }

    @Override
    public List<Account> getDataSource() {
        return mAccountList;
    }

    @Override
    public void requestData() {
        LOG_MA("requestData");
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                final List<Account> accountList = DBManager.getAccountDao().loadAll();

                ThreadManager.post(new Runnable() {
                    @Override
                    public void run() {
                        mAccountList.clear();
                        mAccountList.addAll(accountList);
                        fillAndNotifySync();
                    }
                });
            }
        });
    }

    @Override
    protected DataType getDataType() {
        return DataType.ACCOUNT;
    }

    public void insert (String name) {
        insert(name, null);
    }

    public void insert(final String name, final Long id) {
        if (Strings.isNullOrEmpty(name)) {
            return;
        }
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                final Account account = new Account(
                        id,
                        name,
                        "",
                        "",
                        System.currentTimeMillis(),
                        "",
                        "",
                        ""
                );
                DBManager.getAccountDao().insertOrReplaceInTx(account);
                ThreadManager.post(new Runnable() {
                    @Override
                    public void run() {
                        SPManager.setInt(Constants.KEY_ACCOUNT_INDEX, mAccountList.size());
                        mAccountList.add(account);
                        mCurrentAccount = account;
                        AccountModel.this.notifyAll(ResponseState.SUCCESS);
                    }
                });
                fillAndNotifySync();
            }
        });
    }

    // Special insert : avoid getCurrent return null
    private void insertFirst() {
        final Account account = new Account(
                DEFAULT_ACCOUNT_ID,
                sDefaultAccountName,
                "",
                "",
                System.currentTimeMillis(),
                "",
                "",
                ""
        );
        SPManager.setInt(Constants.KEY_ACCOUNT_INDEX, mAccountList.size());
        mAccountList.add(account);
        mCurrentAccount = account;
        AccountModel.this.notifyAll(ResponseState.SUCCESS);

        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                DBManager.getAccountDao().insertOrReplaceInTx(account);
                fillAndNotifySync();
            }
        });
    }

    public void delete(final Account account) {
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                List<Subscription> subscriptionList = SubscriptionModel.getInstance().queryByAccountIdSync(account.getId());
                for (Subscription subscription : subscriptionList) {
                    SubscriptionModel.getInstance().delete(subscription);
                }

                // TODO: 26/04/2017 modify in main thread
                if (mAccountList.size() <= 1) {
                    resetAccount(account);
                    DBManager.getAccountDao().update(account);
                } else {
                    DBManager.getAccountDao().delete(account);
                    mAccountList.remove(account);
                    if (mCurrentAccount == account) {
                        mCurrentAccount = mAccountList.get(0);
                        SPManager.setInt(Constants.KEY_ACCOUNT_INDEX, 0);
                    }
                }
                AccountModel.this.notifyAll(ResponseState.SUCCESS);
            }
        });
    }

    // Mark : reset after delete last account must do here
    private void resetAccount(Account account) {
        account.setName(sDefaultAccountName);
    }


    public void queryUnreadCount(final Account account, final QueryCallback callback) {
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                List<Subscription> subscriptionList = SubscriptionModel.getInstance().queryByAccountIdSync(account.getId());
                for (Subscription subscription : subscriptionList) {
                    count += subscription.getUnreadCount();
                }
                final int finalCount = count;
                ThreadManager.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onResult(finalCount);
                        }
                    }
                });
            }
        });
    }

    public interface QueryCallback {
        void onResult(Object obj);
    }

    void updateArticleInfo() {
        LOG_MA("updateArticleInfo");
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                fillAndNotifySync();
            }
        });
    }

    private synchronized void fillAndNotifySync() {
        for (final Account account : mAccountList) {
            int count = 0;
            List<Subscription> subscriptionList = SubscriptionModel.getInstance().queryByAccountIdSync(account.getId());
            for (Subscription subscription : subscriptionList) {
                long unreadCount = DBManager.getArticleDao().queryBuilder().where(
                        ArticleDao.Properties.SubscriptionId.eq(subscription.getId()),
                        ArticleDao.Properties.Read.eq(false)).count();
                count += unreadCount;
            }

            final int finalCount = count;
            ThreadManager.post(new Runnable() {
                @Override
                public void run() {
                    account.setExt1(String.valueOf(finalCount));
                    AccountModel.this.notifyAll(ResponseState.SUCCESS);
                }
            });
        }
    }

    public Account getCurrentAccount() {
        if (mCurrentAccount == null) {
            AssertManager.fail("No account exist");
        }
        return mCurrentAccount;
    }

    public void switchAccountTo(int pos) {
        if (pos < 0 || pos >= mAccountList.size()) {
            AssertManager.fail("Account index out of bound");
            return;
        }

        mCurrentAccount = mAccountList.get(pos);
        SPManager.setInt(Constants.KEY_ACCOUNT_INDEX, pos);
        AccountModel.this.notifyAll(ResponseState.SUCCESS);
    }

    public void update(final Account account) {
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                DBManager.getAccountDao().update(account);
                AccountModel.this.notifyAll(ResponseState.SUCCESS);
            }
        });
    }
}
