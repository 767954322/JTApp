package com.homechart.app.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;

/**
 * Created by gumenghao on 17/7/18.
 * 文章详情页
 */

public class ArticleDetailsActivity
        extends BaseActivity
        implements View.OnClickListener {
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private String article_id;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_article_details;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        article_id =  getIntent().getStringExtra("article_id");
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
        tv_tital_comment.setText("文章详情");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nav_left_imageButton:
                ArticleDetailsActivity.this.finish();
                break;
        }
    }
}
