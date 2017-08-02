package com.homechart.app.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by gumenghao on 17/6/12.
 */

public class GuanYuActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton nav_left_imageButton;
    private TextView tv_guanyu_bb_content;
    private TextView tv_tital_comment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_guanyu;
    }

    @Override
    protected void initView() {

        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_guanyu_bb_content = (TextView) findViewById(R.id.tv_guanyu_bb_content);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("关于家图");
        String banben_code = PublicUtils.getVersionName(GuanYuActivity.this);
        tv_guanyu_bb_content.setText("V" + banben_code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.nav_left_imageButton:
                GuanYuActivity.this.finish();
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("关于家图页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("关于家图页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("关于家图页");
        MobclickAgent.onPause(this);

    }
}
