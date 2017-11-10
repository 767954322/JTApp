package com.homechart.app.home.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homechart.app.R;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.myview.SelectColorSeCaiWindow;
import com.homechart.app.myview.ShopPriceWindow;
import com.homechart.app.utils.UIUtils;
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
    private TextView tv_type_set;
    private TextView tv_guanjianzi_set;
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
    private int visiableNum = 0;
    private ShopPriceWindow shopPriceWindow;
    private View view_line_pop;


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
        view_line_pop = findViewById(R.id.view_line_pop);

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
        rl_price.setOnClickListener(this);
        rl_type.setOnClickListener(this);
        rl_guanjianzi.setOnClickListener(this);
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
                closeAllPopWin();
                rl_add_shuaixuan.setVisibility(View.GONE);
                rl_set_shuaixuan.setVisibility(View.VISIBLE);
                rl_set_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, false));
                break;
            case R.id.iv_close_set:
                rl_set_shuaixuan.setVisibility(View.GONE);
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
                break;
            case R.id.tv_price_set:
            case R.id.rl_price:
                if (v.getId() == R.id.tv_price_set) {
                    ++visiableNum;
                }
                ifShowAddButton();
                ifShowPopWin(R.id.tv_price_set);
                rl_price.setVisibility(View.VISIBLE);
                tv_price_set.setVisibility(View.GONE);
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_set_shuaixuan.setVisibility(View.GONE);
                if (v.getId() == R.id.tv_price_set) {
                    rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
                }
                break;
            case R.id.tv_type_set:
            case R.id.rl_type:
                if (v.getId() == R.id.tv_type_set) {
                    ++visiableNum;
                }
                ifShowAddButton();
                ifShowPopWin(R.id.tv_type_set);
                rl_type.setVisibility(View.VISIBLE);
                tv_type_set.setVisibility(View.GONE);
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_set_shuaixuan.setVisibility(View.GONE);
                if (v.getId() == R.id.tv_type_set) {
                    rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
                }
                break;
            case R.id.tv_guanjianzi_set:
            case R.id.rl_guanjianzi:
                if (v.getId() == R.id.tv_guanjianzi_set) {
                    ++visiableNum;
                }
                ifShowAddButton();
                ifShowPopWin(R.id.tv_guanjianzi_set);
                rl_guanjianzi.setVisibility(View.VISIBLE);
                tv_guanjianzi_set.setVisibility(View.GONE);
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_set_shuaixuan.setVisibility(View.GONE);
                if (v.getId() == R.id.tv_guanjianzi_set) {
                    rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
                }
                break;
            case R.id.iv_price_delect:
                --visiableNum;
                ifShowAddButton();
                closeCurrentPopWin(R.id.iv_price_delect);
                rl_price.setVisibility(View.GONE);
                tv_price_set.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_type_delect:
                --visiableNum;
                ifShowAddButton();
                closeCurrentPopWin(R.id.iv_type_delect);
                rl_type.setVisibility(View.GONE);
                tv_type_set.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_guanjianzi_delect:
                --visiableNum;
                ifShowAddButton();
                closeCurrentPopWin(R.id.iv_guanjianzi_delect);
                rl_guanjianzi.setVisibility(View.GONE);
                tv_guanjianzi_set.setVisibility(View.VISIBLE);
                break;
        }

    }

    //显示或隐藏状态
    private void ifShowPopWin(int id) {
        switch (id) {
            case R.id.tv_price_set:
            case R.id.rl_price:
                if (shopPriceWindow == null) {
                    shopPriceWindow = new ShopPriceWindow(NewShopDetailsActivity.this, this);
                }
                closeOtherWin(id);
                if (shopPriceWindow.isShowing()) {
                    shopPriceWindow.dismiss();
                    tabStaus(0);
                } else {
                    tabStaus(id);
                    if (Build.VERSION.SDK_INT < 24) {
                        shopPriceWindow.showAsDropDown(view_line_pop);
                    } else {
                        // 获取控件的位置，安卓系统>7.0
                        int[] location = new int[2];
                        view_line_pop.getLocationOnScreen(location);
                        int screenHeight = PublicUtils.getScreenHeight(NewShopDetailsActivity.this);
                        shopPriceWindow.setHeight(screenHeight - location[1]);
                        shopPriceWindow.showAtLocation(view_line_pop, Gravity.NO_GRAVITY, 0, location[1]);
                    }
                }
                break;
            case R.id.tv_type_set:
            case R.id.rl_type:
                closeOtherWin(id);
                tabStaus(id);
                break;
            case R.id.tv_guanjianzi_set:
            case R.id.rl_guanjianzi:
                closeOtherWin(id);
                tabStaus(id);
                break;
        }

    }

    //➕状态改变
    private void ifShowAddButton() {
        if (visiableNum == 3) {
            iv_xuanxiang.setVisibility(View.GONE);
        } else {
            iv_xuanxiang.setVisibility(View.VISIBLE);
        }
    }

    //显示状态改变
    private void tabStaus(int id) {

        switch (id) {
            case 0:
                rl_price.setBackgroundResource(R.drawable.search_tiaojian_unselect);
                rl_type.setBackgroundResource(R.drawable.search_tiaojian_unselect);
                rl_guanjianzi.setBackgroundResource(R.drawable.search_tiaojian_unselect);

                iv_price_icon.setImageResource(R.drawable.jiage1_1);
                iv_type_icon.setImageResource(R.drawable.pinlei1_1);
                iv_guanjianzi_icon.setImageResource(R.drawable.guanjianzi1_1);

                tv_price.setTextColor(UIUtils.getColor(R.color.bg_464646));
                tv_type.setTextColor(UIUtils.getColor(R.color.bg_464646));
                tv_guanjianzi.setTextColor(UIUtils.getColor(R.color.bg_464646));

                iv_price_delect.setImageResource(R.drawable.guanbitishi_1);
                iv_type_delect.setImageResource(R.drawable.guanbitishi_1);
                iv_guanjianzi_delect.setImageResource(R.drawable.guanbitishi_1);
                break;
            case R.id.tv_price_set:
            case R.id.rl_price:
                rl_price.setBackgroundResource(R.drawable.search_tiaojian_select);
                rl_type.setBackgroundResource(R.drawable.search_tiaojian_unselect);
                rl_guanjianzi.setBackgroundResource(R.drawable.search_tiaojian_unselect);

                iv_price_icon.setImageResource(R.drawable.jiage_1);
                iv_type_icon.setImageResource(R.drawable.pinlei1_1);
                iv_guanjianzi_icon.setImageResource(R.drawable.guanjianzi1_1);

                tv_price.setTextColor(UIUtils.getColor(R.color.white));
                tv_type.setTextColor(UIUtils.getColor(R.color.bg_464646));
                tv_guanjianzi.setTextColor(UIUtils.getColor(R.color.bg_464646));

                iv_price_delect.setImageResource(R.drawable.guanbitishi1_1);
                iv_type_delect.setImageResource(R.drawable.guanbitishi_1);
                iv_guanjianzi_delect.setImageResource(R.drawable.guanbitishi_1);
                break;
            case R.id.tv_type_set:
            case R.id.rl_type:
                rl_price.setBackgroundResource(R.drawable.search_tiaojian_unselect);
                rl_type.setBackgroundResource(R.drawable.search_tiaojian_select);
                rl_guanjianzi.setBackgroundResource(R.drawable.search_tiaojian_unselect);

                iv_price_icon.setImageResource(R.drawable.jiage1_1);
                iv_type_icon.setImageResource(R.drawable.pinlei_1);
                iv_guanjianzi_icon.setImageResource(R.drawable.guanjianzi1_1);

                tv_price.setTextColor(UIUtils.getColor(R.color.bg_464646));
                tv_type.setTextColor(UIUtils.getColor(R.color.white));
                tv_guanjianzi.setTextColor(UIUtils.getColor(R.color.bg_464646));

                iv_price_delect.setImageResource(R.drawable.guanbitishi_1);
                iv_type_delect.setImageResource(R.drawable.guanbitishi1_1);
                iv_guanjianzi_delect.setImageResource(R.drawable.guanbitishi_1);
                break;
            case R.id.tv_guanjianzi_set:
            case R.id.rl_guanjianzi:

                rl_price.setBackgroundResource(R.drawable.search_tiaojian_unselect);
                rl_type.setBackgroundResource(R.drawable.search_tiaojian_unselect);
                rl_guanjianzi.setBackgroundResource(R.drawable.search_tiaojian_select);

                iv_price_icon.setImageResource(R.drawable.jiage1_1);
                iv_type_icon.setImageResource(R.drawable.pinlei1_1);
                iv_guanjianzi_icon.setImageResource(R.drawable.guanjianzi_1);

                tv_price.setTextColor(UIUtils.getColor(R.color.bg_464646));
                tv_type.setTextColor(UIUtils.getColor(R.color.bg_464646));
                tv_guanjianzi.setTextColor(UIUtils.getColor(R.color.white));

                iv_price_delect.setImageResource(R.drawable.guanbitishi_1);
                iv_type_delect.setImageResource(R.drawable.guanbitishi_1);
                iv_guanjianzi_delect.setImageResource(R.drawable.guanbitishi1_1);
                break;
        }

    }

    //关闭其他的popwin
    private void closeOtherWin(int id) {
        switch (id) {
            case R.id.tv_price_set:
            case R.id.rl_price:
                break;
            case R.id.tv_type_set:
            case R.id.rl_type:
                if(null != shopPriceWindow && shopPriceWindow.isShowing()){
                    shopPriceWindow.dismiss();
                }
                break;
            case R.id.tv_guanjianzi_set:
            case R.id.rl_guanjianzi:
                if(null != shopPriceWindow && shopPriceWindow.isShowing()){
                    shopPriceWindow.dismiss();
                }
                break;
            case 0:
                if(null != shopPriceWindow && shopPriceWindow.isShowing()){
                    shopPriceWindow.dismiss();
                }
                break;
        }
    }

    //关闭当前的的popwin
    private void closeCurrentPopWin(int id) {
        switch (id) {
            case R.id.iv_price_delect:
                if (shopPriceWindow != null && shopPriceWindow.isShowing()) {
                    shopPriceWindow.dismiss();
                }
                break;
            case R.id.iv_type_delect:
                break;
            case R.id.iv_guanjianzi_delect:
                break;
        }
    }

    //关闭所有的的popwin
    private void closeAllPopWin() {
        if (shopPriceWindow != null && shopPriceWindow.isShowing()) {
            shopPriceWindow.dismiss();
            tabStaus(0);
        }
    }
}
