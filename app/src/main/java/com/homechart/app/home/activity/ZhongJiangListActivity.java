package com.homechart.app.home.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;

/**
 * Created by gumenghao on 18/1/17.
 */

public class ZhongJiangListActivity extends BaseActivity {
    private TextView tv_tital_comment;
    private String activity_id;

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        activity_id =  getIntent().getStringExtra("activity_id");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_zhongjiang;
    }

    @Override
    protected void initView() {
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("中奖名单");
    }
}
