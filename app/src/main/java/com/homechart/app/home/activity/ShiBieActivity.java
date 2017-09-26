package com.homechart.app.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;

/**
 * Created by gumenghao on 17/9/26.
 */

public class ShiBieActivity
        extends BaseActivity
        implements View.OnClickListener {
    private ImageButton ibBack;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shibie_jilu;
    }

    @Override
    protected void initView() {
        ibBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
    }

    @Override
    protected void initListener() {
        super.initListener();
        ibBack.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nav_left_imageButton:
                ShiBieActivity.this.finish();
                break;
        }
    }
}
