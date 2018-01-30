package com.feeder.android.view.main;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.feeder.android.view.AuthInoreaderActivity;
import com.feeder.domain.inoreader.InoreaderManager;
import com.feeder.domain.model.AccountModel;
import com.google.common.base.Strings;

import me.zsr.feeder.R;

import static com.feeder.android.view.main.MainActivity.*;

/**
 * @description:
 * @author: Match
 * @date: 18/05/2017
 */

public class MainDialogHelper {

    public static void showCreateLocalAccountDialog(MainActivity activity) {
        new MaterialDialog.Builder(activity)
                .title(R.string.create)
//                .content(R.string.local_account)
                .input(R.string.account_name, R.string.none, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (!Strings.isNullOrEmpty(input.toString())) {
                            AccountModel.getInstance().insert(input.toString());
                        }
                    }
                }).show();
    }

    public static void showCreateAccountDialog(final MainActivity activity) {
        new MaterialDialog.Builder(activity)
                .items(R.array.account_type_selection)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                showCreateLocalAccountDialog(activity);
                                break;
                            case 1:
                                showCreateInoAccountDialog(activity);
                                break;
                        }
                    }
                })
                .show();
    }

    public static void showCreateInoAccountDialog(MainActivity activity) {
        if (AccountModel.getInstance().isTypeExist(InoreaderManager.TYPE)) {
            Toast.makeText(activity, "Ino Account exist !", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(activity, AuthInoreaderActivity.class);
            activity.startActivityForResult(intent, AUTH_INO_CODE);
        }
    }
}
