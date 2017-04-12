package com.feeder.android.view.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.feeder.android.util.AnimationHelper;
import com.feeder.android.util.Constants;
import com.feeder.android.util.OPMLHelper;
import com.feeder.android.util.StatManager;
import com.feeder.android.view.AboutActivity;
import com.feeder.android.view.BaseActivity;
import com.feeder.android.view.SettingsActivity;
import com.feeder.android.view.articlelist.ArticleListActivity;
import com.feeder.common.ThreadManager;
import com.feeder.domain.ArticleModel;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 7/18/16
 */
// TODO: 12/17/16 reformat
public class MainActivity extends BaseActivity {
    private static final int OPML_FILE_SELECT_CODE = 1;
    private static final int PERMISSION_REQUEST_WIRTE_EXTERNAL_STORAGE = 1;
    private MVPPresenter mAccountsPresenter;
    private MVPPresenter mSubscriptionsPresenter;
    private DrawerLayout mDrawerLayout;
    private boolean mCanBackExit;
    private MainToolbarController mToolbarController;
    private OPMLHelper mOPMLHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        initDrawerPanel();
        initDetailsPanel();

        mAccountsPresenter.onCreate();
        mSubscriptionsPresenter.onCreate();

        mOPMLHelper = new OPMLHelper(this);

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

        View importEntrance = LayoutInflater.from(this).inflate(R.layout.import_entrance, drawerPanel, false);
        drawerPanel.addView(importEntrance);
        ((ImageView) importEntrance.findViewById(R.id.import_img)).setColorFilter(getResources().getColor(R.color.main_grey_normal));
        importEntrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_CALENDAR);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    pickFile();
                    closeDrawer(1000);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_WIRTE_EXTERNAL_STORAGE);
                    closeDrawer(0);
                }

                StatManager.statEvent(MainActivity.this, StatManager.EVENT_IMPORT_OPML_CLICK);
            }
        });

        View favEntrance = LayoutInflater.from(this).inflate(R.layout.fav_entrance, drawerPanel, false);
        drawerPanel.addView(favEntrance);
        ((ImageView) favEntrance.findViewById(R.id.fav_img)).setColorFilter(getResources().getColor(R.color.main_grey_normal));
        favEntrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ArticleListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(Constants.KEY_BUNDLE_SUBSCRIPTION_ID, ArticleModel.ID_FAV);
                bundle.putString(Constants.KEY_BUNDLE_SUBSCRIPTION_TITLE, getString(R.string.fav));
                bundle.putString(Constants.KEY_BUNDLE_SUBSCRIPTION_ICON_URL, "");
                intent.putExtras(bundle);
                startActivity(intent);

                AnimationHelper.overridePendingTransition(MainActivity.this);
                closeDrawer(1000);
            }
        });

        View settingsEntrance = LayoutInflater.from(this).inflate(R.layout.settings_entrance, drawerPanel, false);
        drawerPanel.addView(settingsEntrance);
        ((ImageView) settingsEntrance.findViewById(R.id.settings_img)).setColorFilter(getResources().getColor(R.color.main_grey_normal));
        settingsEntrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                AnimationHelper.overridePendingTransition(MainActivity.this);
                closeDrawer(1000);
            }
        });

        View aboutEntrance = LayoutInflater.from(this).inflate(R.layout.about_entrance, drawerPanel, false);
        drawerPanel.addView(aboutEntrance);
        ((ImageView) aboutEntrance.findViewById(R.id.about_img)).setColorFilter(getResources().getColor(R.color.main_grey_normal));
        aboutEntrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                AnimationHelper.overridePendingTransition(MainActivity.this);
                closeDrawer(1000);
            }
        });
    }

    private void pickFile() {
        new MaterialFilePicker()
                .withActivity(MainActivity.this)
                .withRequestCode(OPML_FILE_SELECT_CODE)
                .withFilterDirectories(true)
                .start();
    }

    private void initDetailsPanel() {
        LinearLayout detailsPanel = (LinearLayout) findViewById(R.id.details_panel);
        ISubscriptionsView subscriptionsView = SubscriptionsViewFactory.build(this);
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
            closeDrawer(0);
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

    private void closeDrawer(long delay) {
        ThreadManager.postDelay(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }, delay);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OPML_FILE_SELECT_CODE && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            StatManager.statEvent(MainActivity.this, StatManager.EVENT_IMPORT_OPML_GET_FILE);
            mOPMLHelper.add(filePath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_WIRTE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFile();
                }
            }
        }
    }
}
