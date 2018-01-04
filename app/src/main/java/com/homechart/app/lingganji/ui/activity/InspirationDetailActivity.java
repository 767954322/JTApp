package com.homechart.app.lingganji.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationBean;

/**
 * Created by gumenghao on 18/1/4.
 */

public class InspirationDetailActivity extends BaseActivity implements View.OnClickListener {
    private String mUserId;
    private InspirationBean mInspirationBean;
    private TextView mTital;
    private TextView mRightCreate;
    private ImageButton mBack;
    private ImageButton mRightIcon;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_inspiration_details;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        mUserId = getIntent().getStringExtra("user_id");
        mInspirationBean = (InspirationBean) getIntent().getSerializableExtra("InspirationBean");
    }

    @Override
    protected void initView() {
        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        mRightIcon = (ImageButton) findViewById(R.id.nav_secondary_imageButton);
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
        mRightIcon.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTital.setText("灵感辑");
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.nav_left_imageButton) {
            InspirationDetailActivity.this.finish();
        } else if (id == R.id.nav_secondary_imageButton) {

        }

    }
}
