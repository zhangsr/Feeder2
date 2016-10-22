package com.feeder.domain;

import com.feeder.common.ThreadManager;
import com.feeder.model.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 7/22/16
 */
public class AccountController extends BaseController {
    private static AccountController sAccountController;
    private List<Account> mAccountList = new ArrayList<>();

    private AccountController(){}

    public static AccountController getInstance() {
        if (sAccountController == null) {
            sAccountController = new AccountController();
        }
        return sAccountController;
    }

    @Override
    public List<Account> getDataSource() {
        return mAccountList;
    }

    @Override
    public void requestUpdate() {
        // TODO: 7/22/16 Test
        ThreadManager.postDelay(new Runnable() {
            @Override
            public void run() {
                if (mAccountList.size() == 0) {
                    AccountController.this.notifyAll(ResponseState.SUCCESS);
                } else {
                    AccountController.this.notifyAll(ResponseState.NO_CHANGE);
                }
            }
        }, 5000);
    }

}
