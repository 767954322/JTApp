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
    private RelativeLayout rl_price;
    private ImageView iv_price_icon;
    private TextView tv_price;
    private ImageView iv_price_delect;
    private RelativeLayout rl_type;
    private ImageView iv_type_icon;
    private TextView tv_type;
    private ImageView iv_type_delect;
    private RelativeLayout rl_guanjianzi;
    private ImageView iv_guanjianzi_icon;
    private TextView tv_guanjianzi;
    private ImageView iv_guanjianzi_delect;

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


        rl_price = (RelativeLayout) findViewById(R.id.rl_price);
        iv_price_icon = (ImageView) findViewById(R.id.iv_price_icon);
        tv_price = (TextView) findViewById(R.id.tv_price);
        iv_price_delect = (ImageView) findViewById(R.id.iv_price_delect);

        rl_type = (RelativeLayout) findViewById(R.id.rl_type);
        iv_type_icon = (ImageView) findViewById(R.id.iv_type_icon);
        tv_type = (TextView) findViewById(R.id.tv_type);
        iv_type_delect = (ImageView) findViewById(R.id.iv_type_delect);

        rl_guanjianzi = (RelativeLayout) findViewById(R.id.rl_guanjianzi);
        iv_guanjianzi_icon = (ImageView) findViewById(R.id.iv_guanjianzi_icon);
        tv_guanjianzi = (TextView) findViewById(R.id.tv_guanjianzi);
        iv_guanjianzi_delect = (ImageView) findViewById(R.id.iv_guanjianzi_delect);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        iv_price_delect.setOnClickListener(this);
        iv_type_delect.setOnClickListener(this);
        iv_guanjianzi_delect.setOnClickListener(this);
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
                rl_set_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, false));
//                rl_add_shuaixuan.setAnimation(AnimationUtils.makeOutAnimation(this, false));
                break;
            case R.id.iv_close_set:
                rl_set_shuaixuan.setVisibility(View.GONE);
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
//                rl_set_shuaixuan.setAnimation(AnimationUtils.makeOutAnimation(this, true));
                break;
            case R.id.tv_price_set:
                rl_price.setVisibility(View.VISIBLE);
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_set_shuaixuan.setVisibility(View.GONE);
                rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
//                rl_set_shuaixuan.setAnimation(AnimationUtils.makeOutAnimation(this, true));
                break;
            case R.id.tv_type_set:
                rl_type.setVisibility(View.VISIBLE);
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_set_shuaixuan.setVisibility(View.GONE);
                rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
//                rl_set_shuaixuan.setAnimation(AnimationUtils.makeOutAnimation(this, true));
                break;
            case R.id.tv_guanjianzi_set:
                rl_guanjianzi.setVisibility(View.VISIBLE);
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_set_shuaixuan.setVisibility(View.GONE);
                rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
//                rl_set_shuaixuan.setAnimation(AnimationUtils.makeOutAnimation(this, true));
                break;
            case R.id.iv_price_delect:
                rl_price.setVisibility(View.GONE);
                break;
            case R.id.iv_type_delect:
                rl_type.setVisibility(View.GONE);
                break;
            case R.id.iv_guanjianzi_delect:
                rl_guanjianzi.setVisibility(View.GONE);
                break;
        }

    }

}
