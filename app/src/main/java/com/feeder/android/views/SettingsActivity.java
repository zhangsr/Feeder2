package com.feeder.android.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.feeder.common.SPManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zsr.feeder.R;

public class SettingsActivity extends BaseActivity {
    public static final String KEY_FONT_SIZE = "font_size";
    public static final int FONT_SIZE_SMALL = 0;
    public static final int FONT_SIZE_MEDIUM = 1;
    public static final int FONT_SIZE_BIG = 2;
    public static final String KEY_SWITCH_SHARE_WECHAT = "switch_share_wechat";
    public static final String KEY_SWITCH_SHARE_MOMENT = "switch_share_moment";
    public static final String KEY_SWITCH_SHARE_WEIBO = "switch_share_weibo";
    public static final String KEY_SWITCH_SHARE_INSTAPAPER = "switch_share_instapaper";
    public static final String KEY_SWITCH_SHARE_GOOGLE_PLUS = "switch_share_google_plus";
    public static final String KEY_SWITCH_SHARE_POCKET = "switch_share_pocket";
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.account_manage_img)
    ImageView mAccountManageImageView;
    @Bind(R.id.account_feedly_icon)
    ImageView mFeedlyIconImageView;
    @Bind(R.id.account_feedly_add)
    ImageView mFeedlyAddImageView;

    @Bind(R.id.font_size_img)
    ImageView mFontSizeImageView;
    @Bind(R.id.font_size_txt)
    TextView mFontSizeTextView;

    @Bind(R.id.wechat_img)
    ImageView mWechatImageView;
    @Bind(R.id.wechat_switch)
    SwitchCompat mWechatSwitch;
    @Bind(R.id.moment_img)
    ImageView mMomentImageView;
    @Bind(R.id.moment_switch)
    SwitchCompat mMomentSwitch;
    @Bind(R.id.weibo_img)
    ImageView mWeiboImageView;
    @Bind(R.id.weibo_switch)
    SwitchCompat mWeiboSwitch;
    @Bind(R.id.instapaper_img)
    ImageView mInstapaperImageView;
    @Bind(R.id.instapaper_switch)
    SwitchCompat mInstapaperSwitch;
    @Bind(R.id.google_plus_img)
    ImageView mGooglePlusImageView;
    @Bind(R.id.google_plus_switch)
    SwitchCompat mGooglePlusSwitch;
    @Bind(R.id.pocket_img)
    ImageView mPocketImageView;
    @Bind(R.id.pocket_switch)
    SwitchCompat mPocketSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        initEnvironment();
        initView();
        setListener();
    }

    private void initEnvironment() {

    }

    private void initView() {
        initSystemBar();

        mAccountManageImageView.setColorFilter(getResources().getColor(R.color.main_grey_light));
        mFeedlyIconImageView.setColorFilter(getResources().getColor(R.color.main_grey_light));
        mFeedlyAddImageView.setColorFilter(getResources().getColor(R.color.main_grey_light));

        mFontSizeImageView.setColorFilter(getResources().getColor(R.color.main_grey_light));
        setFontSizeText(SPManager.getInt(KEY_FONT_SIZE, FONT_SIZE_MEDIUM));

        mWechatSwitch.setChecked(SPManager.getBoolean(KEY_SWITCH_SHARE_WECHAT, true));
        mMomentSwitch.setChecked(SPManager.getBoolean(KEY_SWITCH_SHARE_MOMENT, true));
        mWeiboSwitch.setChecked(SPManager.getBoolean(KEY_SWITCH_SHARE_WEIBO, true));
        mInstapaperSwitch.setChecked(SPManager.getBoolean(KEY_SWITCH_SHARE_INSTAPAPER, true));
        mGooglePlusSwitch.setChecked(SPManager.getBoolean(KEY_SWITCH_SHARE_GOOGLE_PLUS, true));
        mPocketSwitch.setChecked(SPManager.getBoolean(KEY_SWITCH_SHARE_POCKET, true));
    }

    private void setListener() {
        mWechatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPManager.setBoolean(KEY_SWITCH_SHARE_WECHAT, isChecked);
            }
        });
        mMomentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPManager.setBoolean(KEY_SWITCH_SHARE_MOMENT, isChecked);
            }
        });
        mWeiboSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPManager.setBoolean(KEY_SWITCH_SHARE_WEIBO, isChecked);
            }
        });
        mInstapaperSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPManager.setBoolean(KEY_SWITCH_SHARE_INSTAPAPER, isChecked);
            }
        });
        mGooglePlusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPManager.setBoolean(KEY_SWITCH_SHARE_GOOGLE_PLUS, isChecked);
            }
        });
        mPocketSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPManager.setBoolean(KEY_SWITCH_SHARE_POCKET, isChecked);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({
            R.id.account_feedly_layout,
            R.id.font_size_layout,
            R.id.wechat_layout,
            R.id.moment_layout,
            R.id.weibo_layout,
            R.id.instapaper_layout,
            R.id.google_plus_layout,
            R.id.pocket_layout,
    })
    public void layoutOnClick(View view) {
        switch (view.getId()) {
            case R.id.account_feedly_layout:
                startActivity(new Intent(SettingsActivity.this, AuthInoreaderActivity.class));
                break;
            case R.id.font_size_layout:
                showSingleChoice();
                break;
            case R.id.wechat_layout:
                mWechatSwitch.setChecked(!mWechatSwitch.isChecked());
                break;
            case R.id.moment_layout:
                mMomentSwitch.setChecked(!mMomentSwitch.isChecked());
                break;
            case R.id.weibo_layout:
                mWeiboSwitch.setChecked(!mWeiboSwitch.isChecked());
                break;
            case R.id.instapaper_layout:
                mInstapaperSwitch.setChecked(!mInstapaperSwitch.isChecked());
                break;
            case R.id.google_plus_layout:
                mGooglePlusSwitch.setChecked(!mGooglePlusSwitch.isChecked());
                break;
            case R.id.pocket_layout:
                mPocketSwitch.setChecked(!mPocketSwitch.isChecked());
                break;
        }
    }

    private void showSingleChoice() {
        // Index map to size value
        int currentSize = SPManager.getInt(KEY_FONT_SIZE, FONT_SIZE_MEDIUM);
        new MaterialDialog.Builder(this)
                .title(R.string.font)
                .items(R.array.font_size)
                .itemsCallbackSingleChoice(currentSize, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        SPManager.setInt(KEY_FONT_SIZE, which);
                        setFontSizeText(which);
                        dialog.dismiss();
                        return true; // allow selection
                    }
                })
                .show();
    }

    private void setFontSizeText(int fontSize) {
        switch (fontSize) {
            case FONT_SIZE_SMALL:
                mFontSizeTextView.setText(R.string.small);
                break;
            case FONT_SIZE_MEDIUM:
                mFontSizeTextView.setText(R.string.medium);
                break;
            case FONT_SIZE_BIG:
                mFontSizeTextView.setText(R.string.big);
                break;
        }
    }
}
