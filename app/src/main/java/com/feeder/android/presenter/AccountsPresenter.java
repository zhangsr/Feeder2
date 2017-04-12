package com.feeder.android.presenter;

import com.feeder.android.base.IAccountsView;
import com.feeder.android.base.MVPPresenter;
import com.feeder.domain.AccountModel;
import com.feeder.domain.DataObserver;
import com.feeder.domain.DataType;
import com.feeder.domain.ResponseState;

/**
 * @description:
 * @author: Match
 * @date: 7/18/16
 */
public class AccountsPresenter implements MVPPresenter, DataObserver {
    private IAccountsView mAccountsView;

    public AccountsPresenter(IAccountsView accountsView) {
        mAccountsView = accountsView;
    }

    @Override
    public void onCreate() {
        mAccountsView.showLoading();
        mAccountsView.setDataSource(AccountModel.getInstance().getDataSource());
    }

    @Override
    public void onStart() {
        AccountModel.getInstance().registerObserver(this);
        AccountModel.getInstance().requestData();
    }

    @Override
    public void onStop() {
        AccountModel.getInstance().unRegisterObserver(this);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onDataResponse(ResponseState state, DataType type) {
        switch (state) {
            case SUCCESS:
                mAccountsView.hideLoading();
                mAccountsView.notifyDataChanged();
                break;
            case NO_CHANGE:
                mAccountsView.hideLoading();
                break;
        }
    }
}
