package com.homechart.app.home.activity;

import android.os.Bundle;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;

/**
 * Created by gumenghao on 18/3/14.
 */

public class FaBuImageActivity extends BaseActivity {
    private String image_id;
    private String image_url;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fabu_image;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        image_id = getIntent().getStringExtra("image_id");
        image_url = getIntent().getStringExtra("image_url");

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
