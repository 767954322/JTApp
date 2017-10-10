package com.homechart.app.home.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.imageloader.ImageUtils;

/**
 * Created by gumenghao on 17/10/10.
 */

public class TestActivity extends BaseActivity {
    private ImageView iv_imageview;
    private String image_url;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        image_url =   getIntent().getStringExtra("image_url");
    }

    @Override
    protected void initView() {

        iv_imageview = (ImageView) findViewById(R.id.iv_imageview);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        ImageUtils.displayFilletImage(image_url,iv_imageview);

    }
}
