package com.homechart.app.home.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.color.ColorItemBean;

/**
 * Created by gumenghao on 17/8/14.
 */

public class ColorShaiXuanActivity
        extends BaseActivity
        implements View.OnClickListener {
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private ColorItemBean mColorClick;

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        mColorClick = (ColorItemBean) getIntent().getSerializableExtra("color");

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shaixuan_color;
    }

    @Override
    protected void initView() {


        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);

    }

    @Override
    protected void initListener() {
        super.initListener();

        nav_left_imageButton.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("色彩");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ColorShaiXuanActivity.this.finish();
                break;
        }
    }
}
