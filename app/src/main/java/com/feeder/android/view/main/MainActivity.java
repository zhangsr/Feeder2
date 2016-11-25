package com.feeder.android.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.feeder.android.base.IAccountsView;
import com.feeder.android.base.ISubscriptionsView;
import com.feeder.android.base.MVPPresenter;
import com.feeder.android.presenter.AccountsPresenter;
import com.feeder.android.presenter.SubscriptionsPresenter;
import com.feeder.android.util.StatManager;
import com.feeder.android.view.AboutActivity;
import com.feeder.android.view.BaseActivity;
import com.feeder.android.view.SettingsActivity;
import com.feeder.common.ThreadManager;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 7/18/16
 */
public class MainActivity extends BaseActivity {
    private MVPPresenter mAccountsPresenter;
    private MVPPresenter mSubscriptionsPresenter;
    private DrawerLayout mDrawerLayout;
    private boolean mCanBackExit;
    private MainToolbarController mToolbarController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSystemBar();
        setContentView(R.layout.activity_main);

        initToolbar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        initDrawerPanel();
        initDetailsPanel();

        mAccountsPresenter.onCreate();
        mSubscriptionsPresenter.onCreate();

        StatManager.trackAppOpened(getIntent());
    }

    private void initToolbar() {
        mToolbarController = new MainToolbarController(this);
        setSupportActionBar(mToolbarController.getToolbar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initDrawerPanel() {
        LinearLayout drawerPanel = (LinearLayout) findViewById(R.id.drawer_panel);
        IAccountsView accountsView = new AccountsView(this);
        LinearLayout.LayoutParams accountsViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        drawerPanel.addView(accountsView, accountsViewLp);
        mAccountsPresenter = new AccountsPresenter(accountsView);

        View settingsEntrance = LayoutInflater.from(this).inflate(R.layout.settings_entrance, drawerPanel, false);
        drawerPanel.addView(settingsEntrance);
        ((ImageView) settingsEntrance.findViewById(R.id.settings_img)).setColorFilter(getResources().getColor(R.color.main_grey_normal));
        settingsEntrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });

        View aboutEntrance = LayoutInflater.from(this).inflate(R.layout.about_entrance, drawerPanel, false);
        drawerPanel.addView(aboutEntrance);
        ((ImageView) aboutEntrance.findViewById(R.id.about_img)).setColorFilter(getResources().getColor(R.color.main_grey_normal));
        aboutEntrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });
    }

    private void initDetailsPanel() {
        LinearLayout detailsPanel = (LinearLayout) findViewById(R.id.details_panel);
        ISubscriptionsView subscriptionsView = new SubscriptionsView(this);
        LinearLayout.LayoutParams subscriptionsViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        detailsPanel.addView(subscriptionsView, subscriptionsViewLp);
        mSubscriptionsPresenter = new SubscriptionsPresenter(this, subscriptionsView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAccountsPresenter.onStart();
        mSubscriptionsPresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAccountsPresenter.onStop();
        mSubscriptionsPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mAccountsPresenter.onDestroy();
        mSubscriptionsPresenter.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (mToolbarController.handleBackPressed()) {
                return;
            }
            if (mCanBackExit) {
                super.onBackPressed();
            } else {
                mCanBackExit = true;
                Toast.makeText(this, R.string.back_exit_hint, Toast.LENGTH_SHORT).show();
                ThreadManager.postDelay(new Runnable() {
                    @Override
                    public void run() {
                        mCanBackExit = false;
                    }
                }, 5000);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mToolbarController.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
