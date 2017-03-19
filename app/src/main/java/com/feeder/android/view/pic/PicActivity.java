package com.feeder.android.view.pic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.feeder.android.util.AnimationHelper;
import com.feeder.android.util.Constants;
import com.feeder.android.view.BaseActivity;
import com.github.chrisbanes.photoview.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 3/19/17
 */

// TODO: 3/19/17 Animate scale from origin location
// TODO: 3/19/17 slide down to exit
public class PicActivity extends BaseActivity implements View.OnClickListener{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        findViewById(R.id.root).setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String imageUrl = bundle.getString(Constants.KEY_BUNDLE_IMAGE_URL);

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        photoView.setOnClickListener(this);

        ImageLoader.getInstance().displayImage(imageUrl, photoView);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationHelper.setFadeTransition(this);
    }
}
