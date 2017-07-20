package com.homechart.app.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.myview.MyScrollView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.imageloader.ImageUtils;

/**
 * Created by gumenghao on 17/6/9.
 */

public class DesinerInfoHeaderActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton mIBBack;
    private UserCenterInfoBean userCenterInfoBean;
    private TextView tv_info_price;
    private TextView tv_info_biaozun;
    private TextView tv_info_xiangmu;
    private TextView tv_info_quyu;
    private TextView tv_info_liucheng;
    private TextView tv_info_guanyuwo;
    private TextView tv_lianxi_one;
    private TextView tv_lianxi_two;
    private TextView tv_lianxi_three;
    private TextView tv_lianxi_four;
    private TextView tv_lianxi_five;
    private TextView tv_lianxi_six;
    private RoundImageView iv_header_desiner_center;
    private ImageView iv_info_renzheng;
    private TextView tv_userinfo_nikename;
    private MyScrollView sv_scrollview;
    private ImageButton nav_secondary_imageButton;
    private RelativeLayout common_navbar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_desiner_info_header;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        userCenterInfoBean = (UserCenterInfoBean) getIntent().getSerializableExtra("info");

    }

    @Override
    protected void initView() {


        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_info_price = (TextView) findViewById(R.id.tv_info_price);
        tv_info_biaozun = (TextView) findViewById(R.id.tv_info_biaozun);
        tv_info_xiangmu = (TextView) findViewById(R.id.tv_info_xiangmu);
        tv_info_quyu = (TextView) findViewById(R.id.tv_info_quyu);
        tv_info_liucheng = (TextView) findViewById(R.id.tv_info_liucheng);
        tv_info_guanyuwo = (TextView) findViewById(R.id.tv_info_guanyuwo);
        tv_lianxi_one = (TextView) findViewById(R.id.tv_lianxi_one);
        tv_lianxi_two = (TextView) findViewById(R.id.tv_lianxi_two);
        tv_lianxi_three = (TextView) findViewById(R.id.tv_lianxi_three);
        tv_lianxi_four = (TextView) findViewById(R.id.tv_lianxi_four);
        tv_lianxi_five = (TextView) findViewById(R.id.tv_lianxi_five);
        tv_lianxi_six = (TextView) findViewById(R.id.tv_lianxi_six);
        sv_scrollview = (MyScrollView) findViewById(R.id.sv_scrollview);
        common_navbar = (RelativeLayout) findViewById(R.id.common_navbar);


        iv_header_desiner_center = (RoundImageView) findViewById(R.id.iv_header_desiner_center);
        iv_info_renzheng = (ImageView) findViewById(R.id.iv_info_renzheng);
        tv_userinfo_nikename = (TextView) findViewById(R.id.tv_userinfo_nikename);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        ImageUtils.displayRoundImage(userCenterInfoBean.getUser_info().getAvatar().getBig(), iv_header_desiner_center);
        if (!userCenterInfoBean.getUser_info().getProfession().trim().equals("0")) {//专业用户
            iv_info_renzheng.setVisibility(View.VISIBLE);
        } else {
            iv_info_renzheng.setVisibility(View.INVISIBLE);
        }
        tv_userinfo_nikename.setText(userCenterInfoBean.getUser_info().getNickname());
        tv_info_price.setText(userCenterInfoBean.getPro_info().getUnit_price());
        tv_info_biaozun.setText(userCenterInfoBean.getPro_info().getFee_scale());
        tv_info_xiangmu.setText(userCenterInfoBean.getPro_info().getService_items());
        tv_info_quyu.setText(userCenterInfoBean.getPro_info().getService_area());
        tv_info_liucheng.setText(userCenterInfoBean.getPro_info().getService_flow());
        tv_info_guanyuwo.setText(userCenterInfoBean.getPro_info().getDescription());
        tv_lianxi_one.setText(userCenterInfoBean.getPro_info().getMobile());
        tv_lianxi_two.setText(userCenterInfoBean.getPro_info().getEmail());
        tv_lianxi_three.setText(userCenterInfoBean.getPro_info().getHomepage());
        tv_lianxi_four.setText(userCenterInfoBean.getPro_info().getWechat());
        tv_lianxi_five.setText(userCenterInfoBean.getPro_info().getQq());
        tv_lianxi_six.setText(userCenterInfoBean.getPro_info().getLocation());
    }

    @Override
    protected void initListener() {
        super.initListener();
        mIBBack.setOnClickListener(this);

        sv_scrollview.setMyOnScrollChangedListener(new MyScrollView.MyOnScrollChangedListener() {
            @Override
            public void myOnScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < 300) {
                    mIBBack.setImageResource(R.drawable.tital_back_wight);
                    float alpha = (float) (300 - scrollY) / 300;
                    common_navbar.setBackgroundResource(R.color.white);
                    common_navbar.getBackground().setAlpha((int) ((1 - alpha) * 255));
                } else {
                    common_navbar.setBackgroundResource(R.color.white);
                    mIBBack.setImageResource(R.drawable.tital_back);
                    common_navbar.getBackground().setAlpha(255);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                DesinerInfoHeaderActivity.this.finish();
                break;
        }


    }
}
