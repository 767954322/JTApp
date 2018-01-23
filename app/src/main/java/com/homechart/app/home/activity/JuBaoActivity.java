package com.homechart.app.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.myview.MyListView;

/**
 * Created by gumenghao on 18/1/23.
 */

public class JuBaoActivity extends BaseActivity implements View.OnClickListener {
    private String item_id;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private TextView tv_content_right;
    private TextView et_liyou;
    private MyListView mlv_listview;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_jubao;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        item_id = getIntent().getStringExtra("item_id");
    }

    @Override
    protected void initView() {

        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        mlv_listview = (MyListView) findViewById(R.id.mlv_listview);
        et_liyou = (TextView) findViewById(R.id.et_liyou);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("举报图片");
        tv_content_right.setText("提交");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                JuBaoActivity.this.finish();
                break;
            case R.id.tv_content_right:
                break;
        }
    }
}
