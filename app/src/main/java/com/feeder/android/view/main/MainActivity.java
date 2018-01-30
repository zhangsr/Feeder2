package com.feeder.android.view.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.feeder.android.base.IAccountsView;
import com.feeder.android.base.ISubscriptionsView;
import com.feeder.android.base.MVPPresenter;
import com.feeder.android.presenter.AccountsPresenter;
import com.feeder.android.presenter.SubscriptionsPresenter;
import com.feeder.android.util.AnimationHelper;
import com.feeder.android.util.Constants;
import com.feeder.android.util.EventCenter;
import com.feeder.android.util.MessageEvent;
import com.feeder.android.util.OPMLHelper;
import com.feeder.android.util.StatManager;
import com.feeder.android.view.AboutActivity;
import com.feeder.android.view.AuthInoreaderActivity;
import com.feeder.android.view.BaseActivity;
import com.feeder.android.view.SettingsActivity;
import com.feeder.android.view.articlelist.ArticleListActivity;
import com.feeder.common.ThreadManager;
import com.feeder.domain.inoreader.InoUserInfo;
import com.feeder.domain.inoreader.InoreaderManager;
import com.feeder.domain.model.AccountModel;
import com.feeder.domain.model.ArticleModel;
import com.google.common.base.Strings;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.greenrobot.eventbus.Subscribe;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 7/18/16
 */
// TODO: 12/17/16 reformat
public class MainActivity extends BaseActivity implements MainToolbarController.MainToolbarUIListener {
    private static final int OPML_FILE_SELECT_CODE = 1;
    public static final int AUTH_INO_CODE = 2;
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private MVPPresenter mAccountsPresenter;
    private MVPPresenter mSubscriptionsPresenter;
    private DrawerLayout mDrawerLayout;
    private boolean mCanBackExit;
    private MainToolbarController mToolbarController;

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

        StatManager.trackAppOpened(getIntent());
    }


    private void initToolbar() {
        mToolbarController = new MainToolbarController(this, this);
        setSupportActionBar(mToolbarController.getToolbar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_add_white_24dp);
        mToolbarController.getToolbar().setOverflowIcon(drawable);
    }

    private void initDrawerPanel() {
        LinearLayout drawerPanel = (LinearLayout) findViewById(R.id.drawer_panel);
        IAccountsView accountsView = new AccountsView(this);
        LinearLayout.LayoutParams accountsViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        drawerPanel.addView(accountsView, accountsViewLp);
        mAccountsPresenter = new AccountsPresenter(this, accountsView);

        View createAccountEntrance = LayoutInflater.from(this).inflate(R.layout.create_account_entrance, drawerPanel, false);
        drawerPanel.addView(createAccountEntrance);
        ((ImageView) createAccountEntrance.findViewById(R.id.create_account_img)).setColorFilter(getResources().getColor(R.color.main_grey_normal));
        createAccountEntrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainDialogHelper.showCreateLocalAccountDialog(MainActivity.this);
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
        EventCenter.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAccountsPresenter.onStop();
        mSubscriptionsPresenter.onStop();
        EventCenter.unregister(this);
    }

    @Subscribe
    public void onMessageEvent(String msg) {
        switch (msg) {
            case MessageEvent.MSG_SWITCH_ACCOUNT:
                closeDrawer(300);
                break;
        }
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
            OPMLHelper.getInstance().add(filePath, MainActivity.this, AccountModel.getInstance().getCurrentAccount().getId());
        } else if (requestCode == AUTH_INO_CODE && resultCode == AuthInoreaderActivity.AUTH_SUCCESS) {
            InoreaderManager.getInstance().requestUserInfo(new InoreaderManager.UserInfoCallBack() {
                @Override
                public void onResult(InoUserInfo userInfo) {
                    String name = userInfo.userName;
                    if (Strings.isNullOrEmpty(name)) {
                        name = userInfo.userEmail;
                    }
                    AccountModel.getInstance().insert(name, null, InoreaderManager.TYPE);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFile();
                }
            }
        }
    }

    @Override
    public void onImportOPMLClick() {
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_CALENDAR);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            pickFile();
            closeDrawer(1000);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            closeDrawer(0);
        }

        StatManager.statEvent(MainActivity.this, StatManager.EVENT_IMPORT_OPML_CLICK);
    }
}
