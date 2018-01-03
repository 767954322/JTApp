package com.homechart.app.lingganji.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.umeng.socialize.media.Base;

/**
 * Created by gumenghao on 18/1/3.
 */

public class InspirationCreateActivity extends BaseActivity
        implements View.OnClickListener {

    private String mUserId;
    private ImageView mDismiss;

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_activity_create_inspiration;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        mUserId = getIntent().getStringExtra("userid");
    }

    @Override
    protected void initView() {

        mDismiss = (ImageView) this.findViewById(R.id.iv_dismiss);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mDismiss.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_dismiss) {
            this.finish();
            this.overridePendingTransition(R.anim.pop_exit_anim, 0);
        }
    }
}
