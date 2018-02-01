package com.homechart.app.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;

/**
 * Created by gumenghao on 18/2/1.
 */

public class DingYueGuanLiActivity extends BaseActivity implements View.OnClickListener{


    private TextView mTital;
    private TextView mRight;
    private ImageButton mBack;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_guanli_dingyue;
    }

    @Override
    protected void initView() {

       mTital =(TextView) findViewById(R.id.tv_tital_comment);
       mRight =(TextView) findViewById(R.id.tv_content_right);
       mBack =(ImageButton) findViewById(R.id.nav_left_imageButton);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        mTital.setText("订阅管理");
        mRight.setText("完成");

        getDingYueData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nav_left_imageButton:
                DingYueGuanLiActivity.this.finish();
                break;
        }
    }


    private void getDingYueData() {

    }

}
