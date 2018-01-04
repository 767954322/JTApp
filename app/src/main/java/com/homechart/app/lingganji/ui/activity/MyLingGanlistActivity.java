package com.homechart.app.lingganji.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;

/**
 * Created by gumenghao on 18/1/4.
 */

public class MyLingGanlistActivity extends BaseActivity implements View.OnClickListener{
    private TextView mTital;
    private TextView mRightCreate;
    private ImageButton mBack;
    private String mUserId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mylinggan_list;
    }
    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        mUserId = (String) getIntent().getSerializableExtra(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initView() {

        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        mRightCreate = (TextView) findViewById(R.id.tv_content_right);
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTital.setText("我的灵感辑");
        mRightCreate.setText("新建");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.nav_left_imageButton){
            MyLingGanlistActivity.this.finish();
        }

    }
}
