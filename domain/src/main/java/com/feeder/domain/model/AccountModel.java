package com.feeder.domain.model;

import com.feeder.common.ThreadManager;
import com.feeder.model.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 7/22/16
 */
public class AccountModel extends BaseModel {
    private static AccountModel sModel;
    private List<Account> mAccountList = new ArrayList<>();

    private AccountModel(){}

    public static AccountModel getInstance() {
        if (sModel == null) {
            sModel = new AccountModel();
        }
        return sModel;
    }

    @Override
    public List<Account> getDataSource() {
        return mAccountList;
    }

    @Override
    public void requestData() {
        // TODO: 7/22/16 Test
        ThreadManager.postInBackground(new Runnable() {
            @Override
            public void run() {
                if (mAccountList.size() == 0) {
                    AccountModel.this.notifyAll(ResponseState.SUCCESS);
                } else {
                    AccountModel.this.notifyAll(ResponseState.NO_CHANGE);
                }
            }
        });
    }

    @Override
    protected DataType getDataType() {
        return DataType.ACCOUNT;
    }

}
