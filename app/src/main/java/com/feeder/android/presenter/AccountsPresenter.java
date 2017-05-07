package com.feeder.android.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.feeder.android.base.AccountViewObserver;
import com.feeder.android.base.IAccountsView;
import com.feeder.android.base.MVPPresenter;
import com.feeder.android.util.EventCenter;
import com.feeder.android.util.MessageEvent;
import com.feeder.domain.model.AccountModel;
import com.feeder.domain.model.DataObserver;
import com.feeder.domain.model.DataType;
import com.feeder.domain.model.ResponseState;
import com.feeder.model.Account;
import com.google.common.base.Strings;

import java.util.List;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 7/18/16
 */
public class AccountsPresenter implements MVPPresenter, DataObserver, AccountViewObserver {
    private Activity mActivity;
    private IAccountsView mAccountsView;

    public AccountsPresenter(Activity activity, IAccountsView accountsView) {
        mActivity = activity;
        mAccountsView = accountsView;
        mAccountsView.setObserver(this);
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

    @Override
    public void onItemClick(View view, List<Account> dataList, int pos) {
        AccountModel.getInstance().switchAccountTo(pos);
        EventCenter.post(MessageEvent.MSG_SWITCH_ACCOUNT);
    }

    @Override
    public boolean onItemLongClick(View view, final List<Account> dataList, final int pos) {

        new MaterialDialog.Builder(mActivity)
                .items(R.array.account_long_click_menu)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                rename(dataList.get(pos));
                                break;
                            case 1:
                                confirmDelete(dataList.get(pos));
                                break;
                        }
                    }
                })
                .show();
        return true;
    }

    private void rename(final Account account) {
        String hint = mActivity.getResources().getString(R.string.account_name);
        new MaterialDialog.Builder(mActivity)
                .title(R.string.rename)
                .content(R.string.local_account)
                .input(hint, account.getName(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (!Strings.isNullOrEmpty(input.toString())) {
                            account.setName(input.toString());
                            AccountModel.getInstance().update(account);
                        }
                    }
                }).show();
    }

    private void confirmDelete(final Account account) {
        new MaterialDialog.Builder(mActivity)
                .title(R.string.delete_account)
                .content(R.string.delete_confirm_tip)
                .positiveText(R.string.upper_confirm)
                .negativeText(R.string.upper_cancel)
                .negativeColor(mActivity.getResources().getColor(R.color.main_grey_normal))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        AccountModel.getInstance().delete(account);
                    }
                })
                .show();
    }
}
