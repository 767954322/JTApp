package com.homechart.app.home.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.glide.GlideImgManager;
import com.homechart.app.utils.imageloader.ImageUtils;

/**
 * Created by gumenghao on 17/11/9.
 */

public class NewShopDetailsActivity
        extends BaseActivity
        implements View.OnClickListener {

    private ImageButton nav_left_imageButton;
    private ImageView iv_crop_imageview;
    private TextView tv_tital_comment;
    private String cropImage;
    private ImageView iv_xuanxiang;
    private RelativeLayout rl_add_shuaixuan;
    private RelativeLayout rl_set_shuaixuan;
    private ImageView iv_close_set;
    private TextView tv_price_set;
    private TextView tv_guanjianzi_set;
    private TextView tv_type_set;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_shop_buy;
    }

    @Override
    protected void initView() {

        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        rl_add_shuaixuan = (RelativeLayout) findViewById(R.id.rl_add_shuaixuan);
        rl_set_shuaixuan = (RelativeLayout) findViewById(R.id.rl_set_shuaixuan);
        iv_crop_imageview = (ImageView) findViewById(R.id.iv_crop_imageview);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        iv_xuanxiang = (ImageView) findViewById(R.id.iv_xuanxiang);
        iv_close_set = (ImageView) findViewById(R.id.iv_close_set);

        tv_price_set = (TextView) findViewById(R.id.tv_price_set);
        tv_type_set = (TextView) findViewById(R.id.tv_type_set);
        tv_guanjianzi_set = (TextView) findViewById(R.id.tv_guanjianzi_set);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        iv_xuanxiang.setOnClickListener(this);
        iv_close_set.setOnClickListener(this);
        tv_price_set.setOnClickListener(this);
        tv_type_set.setOnClickListener(this);
        tv_guanjianzi_set.setOnClickListener(this);
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        cropImage = getIntent().getStringExtra("image_path");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        ImageUtils.disRectangleImage("file://" + cropImage, iv_crop_imageview);
        tv_tital_comment.setText("相似商品");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                NewShopDetailsActivity.this.finish();
                break;
            case R.id.iv_xuanxiang:
                rl_add_shuaixuan.setVisibility(View.GONE);
                rl_set_shuaixuan.setVisibility(View.VISIBLE);
                // 向右边滑动到左边
                rl_set_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, false));
                rl_add_shuaixuan.setAnimation(AnimationUtils.makeOutAnimation(this, false));
                break;
            case R.id.iv_close_set:
                // 向左边移出
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_set_shuaixuan.setVisibility(View.GONE);
                // 向左边移入
                rl_set_shuaixuan.setAnimation(AnimationUtils.makeOutAnimation(this, true));
                rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
                break;
            case R.id.tv_price_set:
                // 向左边移出
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_set_shuaixuan.setVisibility(View.GONE);
                // 向左边移入
                rl_set_shuaixuan.setAnimation(AnimationUtils.makeOutAnimation(this, true));
                rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
                break;
            case R.id.tv_type_set:
                // 向左边移出
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_set_shuaixuan.setVisibility(View.GONE);
                // 向左边移入
                rl_set_shuaixuan.setAnimation(AnimationUtils.makeOutAnimation(this, true));
                rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
                break;
            case R.id.tv_guanjianzi_set:
                // 向左边移出
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_set_shuaixuan.setVisibility(View.GONE);
                // 向左边移入
                rl_set_shuaixuan.setAnimation(AnimationUtils.makeOutAnimation(this, true));
                rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
                break;
        }

    }
}
