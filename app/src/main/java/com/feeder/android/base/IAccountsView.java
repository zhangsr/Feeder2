package com.feeder.android.base;

import android.content.Context;

import com.feeder.model.Account;

import java.util.List;

/**
 * @description:
 * @author: Match
 * @date: 7/18/16
 */
public abstract class IAccountsView extends MVPView {

    public IAccountsView(Context context) {
        super(context);
    }

    public abstract void setDataSource(List<Account> accountList);
}
