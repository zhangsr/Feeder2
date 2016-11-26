package com.feeder.android.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 7/18/16
 */
public abstract class MVPView extends FrameLayout {
    protected View mLoadingView;

    public MVPView(Context context) {
        super(context);

        // TODO: 7/24/16 Mark return
        mLoadingView = LayoutInflater.from(context).inflate(R.layout.view_loading, this, false);
        mLoadingView.setVisibility(GONE);
        addView(mLoadingView);
    }

    public abstract void showLoading();
    public abstract void hideLoading();
    public void notifyDataChanged() {
    }

    public void showToast(String content) {
        Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
    }
}
