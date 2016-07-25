package com.feeder.domain;

import com.feeder.common.ThreadManager;
import com.feeder.model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * @description:
 * @author: Match
 * @date: 7/22/16
 */
public class AccountController {
    private static AccountController sAccountController;
    private List<Account> mAccountList = new ArrayList<>();
    private List<AccountObserver> mObserverList = new ArrayList<>();

    private AccountController(){}

    public static AccountController getInstance() {
        if (sAccountController == null) {
            sAccountController = new AccountController();
        }
        return sAccountController;
    }

    public List<Account> getDataSource() {
        return mAccountList;
    }

    public void requestUpdate() {
        // TODO: 7/22/16 Test
        ThreadManager.postDelay(new Runnable() {
            @Override
            public void run() {
                if (mAccountList.size() == 0) {
                    mAccountList.add(new Account(1L, "TestAccount1", ""));
                    mAccountList.add(new Account(2L, "TestAccount2", ""));
                    mAccountList.add(new Account(3L, "TestAccount3", ""));
                    AccountController.this.notifyAll(ResponseState.SUCCESS);
                } else {
                    AccountController.this.notifyAll(ResponseState.NO_CHANGE);
                }
            }
        }, 5000);
    }

    public void registerObserver(AccountObserver observer) {
        mObserverList.add(observer);
    }

    public void unRegisterObserver(AccountObserver observer) {
        mObserverList.remove(observer);
    }

    private void notifyAll(ResponseState state) {
        for (AccountObserver observer : mObserverList) {
            if (observer != null) {
                observer.onResponse(state);
            }
        }
    }
}
