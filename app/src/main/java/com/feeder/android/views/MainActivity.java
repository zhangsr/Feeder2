package com.feeder.android.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.feeder.android.mvp.IAccountsView;
import com.feeder.android.mvp.MVPPresenter;
import com.feeder.android.presenters.AccountsPresenter;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 7/18/16
 */
public class MainActivity extends BaseActivity {
    private MVPPresenter mAccountsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSystemBar();
        setContentView(R.layout.activity_main);
        initDrawerPanel();
        initDetailsPanel();

        mAccountsPresenter.onCreate();
    }

    private void initDrawerPanel() {
        LinearLayout drawerPanel = (LinearLayout) findViewById(R.id.drawer_panel);
        IAccountsView accountsView = new AccountsView(this);
        LinearLayout.LayoutParams accountsViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        drawerPanel.addView(accountsView, accountsViewLp);
        mAccountsPresenter = new AccountsPresenter(accountsView);

        View settingsEntrance = LayoutInflater.from(this).inflate(R.layout.settings_entrance, drawerPanel, false);
        drawerPanel.addView(settingsEntrance);
        ((ImageView) settingsEntrance.findViewById(R.id.settings_img)).setColorFilter(getResources().getColor(R.color.main_grey_light));
        settingsEntrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }

    private void initDetailsPanel() {
        ViewGroup detailsPanel = (ViewGroup) findViewById(R.id.details_panel);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAccountsPresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAccountsPresenter.onStop();
    }
}
