package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.adapter.HomeTagAdapter;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.color.ColorBean;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.myview.HomeTabPopWin;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gumenghao on 17/8/14.
 */

public class ColorShaiXuanActivity
        extends BaseActivity
        implements View.OnClickListener,
        ViewPager.OnPageChangeListener,
        HomeTagAdapter.PopupWindowCallBack {
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private ColorItemBean mColorClick;
    private LinearLayout ll_pic_choose;
    private RoundImageView iv_kongjian;
    private RoundImageView iv_jubu;
    private RoundImageView iv_zhuangshi;
    private RoundImageView iv_shouna;
    private RoundImageView iv_secai;
    private RelativeLayout rl_kongjian;
    private RelativeLayout rl_jubu;
    private RelativeLayout rl_zhuangshi;
    private RelativeLayout rl_shouna;
    private RelativeLayout rl_secai;
    private View view_center;
    private TagDataBean tagDataBean;
    private ColorBean colorBean;
    private HomeTabPopWin homeTabPopWin;
    private int last_id = 0;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
            } else if (msg.what == 2) {
                String info = (String) msg.obj;
                tagDataBean = GsonUtil.jsonToBean(info, TagDataBean.class);
            } else if (msg.what == 3) {
                String info = (String) msg.obj;
                colorBean = GsonUtil.jsonToBean(info, ColorBean.class);
                Log.d("test", colorBean.toString());
            }
        }
    };

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        mColorClick = (ColorItemBean) getIntent().getSerializableExtra("color");
        tagDataBean = (TagDataBean) getIntent().getSerializableExtra("tagDataBean");
        colorBean = (ColorBean) getIntent().getSerializableExtra("colorBean");

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shaixuan_color;
    }

    @Override
    protected void initView() {


        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        view_center = findViewById(R.id.view_center);


        ll_pic_choose = (LinearLayout) findViewById(R.id.ll_pic_choose);
        iv_kongjian = (RoundImageView) findViewById(R.id.iv_kongjian);
        iv_jubu = (RoundImageView) findViewById(R.id.iv_jubu);
        iv_zhuangshi = (RoundImageView) findViewById(R.id.iv_zhuangshi);
        iv_shouna = (RoundImageView) findViewById(R.id.iv_shouna);
        iv_secai = (RoundImageView) findViewById(R.id.iv_secai);
        rl_kongjian = (RelativeLayout) findViewById(R.id.rl_kongjian);
        rl_jubu = (RelativeLayout) findViewById(R.id.rl_jubu);
        rl_zhuangshi = (RelativeLayout) findViewById(R.id.rl_zhuangshi);
        rl_shouna = (RelativeLayout) findViewById(R.id.rl_shouna);
        rl_secai = (RelativeLayout) findViewById(R.id.rl_secai);

    }

    @Override
    protected void initListener() {
        super.initListener();

        nav_left_imageButton.setOnClickListener(this);
        rl_kongjian.setOnClickListener(this);
        rl_jubu.setOnClickListener(this);
        rl_zhuangshi.setOnClickListener(this);
        rl_shouna.setOnClickListener(this);
        rl_secai.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("色彩");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ColorShaiXuanActivity.this.finish();
                break;
            case R.id.rl_kongjian:

                showPopwindow(R.id.rl_kongjian, 0);
                break;
            case R.id.rl_jubu:

                showPopwindow(R.id.rl_jubu, 1);
                break;
            case R.id.rl_zhuangshi:

                showPopwindow(R.id.rl_zhuangshi, 2);
                break;
            case R.id.rl_shouna:

                showPopwindow(R.id.rl_shouna, 3);
                break;
            case R.id.rl_secai:

                showPopwindow(R.id.rl_secai, 4);
                break;
        }
    }

    private void showPopwindow(int id, int position) {
        if (tagDataBean != null && colorBean != null) {
            if (null == homeTabPopWin) {
                homeTabPopWin = new HomeTabPopWin(this, this, tagDataBean, this, colorBean);
            }
            if (homeTabPopWin.isShowing()) {
                if (last_id != 0 && last_id == id) {
                    last_id = 0;
                    homeTabPopWin.dismiss();
                    iv_kongjian.setImageResource(R.drawable.kongjian1);
                    iv_jubu.setImageResource(R.drawable.jubu1);
                    iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                    iv_shouna.setImageResource(R.drawable.shouna1);
                    iv_secai.setImageResource(R.drawable.secai1);
                } else {
                    last_id = id;

                    homeTabPopWin.setPagePosition(position);
                    switch (position) {
                        case 0:
                            iv_kongjian.setImageResource(R.drawable.kongjian);
                            iv_jubu.setImageResource(R.drawable.jubu1);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                            iv_shouna.setImageResource(R.drawable.shouna1);
                            iv_secai.setImageResource(R.drawable.secai1);
                            break;
                        case 1:
                            iv_kongjian.setImageResource(R.drawable.kongjian1);
                            iv_jubu.setImageResource(R.drawable.jubu);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                            iv_shouna.setImageResource(R.drawable.shouna1);
                            iv_secai.setImageResource(R.drawable.secai1);
                            break;
                        case 2:
                            iv_kongjian.setImageResource(R.drawable.kongjian1);
                            iv_jubu.setImageResource(R.drawable.jubu1);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi);
                            iv_shouna.setImageResource(R.drawable.shouna1);
                            iv_secai.setImageResource(R.drawable.secai1);
                            break;
                        case 3:
                            iv_kongjian.setImageResource(R.drawable.kongjian1);
                            iv_jubu.setImageResource(R.drawable.jubu1);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                            iv_shouna.setImageResource(R.drawable.shouna);
                            iv_secai.setImageResource(R.drawable.secai1);
                            break;
                        case 4:
                            iv_kongjian.setImageResource(R.drawable.kongjian1);
                            iv_jubu.setImageResource(R.drawable.jubu1);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                            iv_shouna.setImageResource(R.drawable.shouna1);
                            iv_secai.setImageResource(R.drawable.secai);
                            break;
                    }
                }

            } else {

                homeTabPopWin.setPagePosition(position);
                last_id = id;
                if (Build.VERSION.SDK_INT < 24) {
                    homeTabPopWin.showAsDropDown(view_center);
                } else {
                    // 获取控件的位置，安卓系统>7.0
                    int[] location = new int[2];
                    view_center.getLocationOnScreen(location);
                    int screenHeight = PublicUtils.getScreenHeight(ColorShaiXuanActivity.this);
                    homeTabPopWin.setHeight(screenHeight - location[1]);
                    homeTabPopWin.showAtLocation(ll_pic_choose, Gravity.NO_GRAVITY, 0, location[1]);
                }

                switch (id) {
                    case R.id.rl_kongjian:
                        iv_kongjian.setImageResource(R.drawable.kongjian);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_jubu:
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_zhuangshi:
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_shouna:
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna);
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_secai:
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        iv_secai.setImageResource(R.drawable.secai);
                        break;
                }
            }
        } else {
            getTagData();
            getColorData();
            ToastUtils.showCenter(ColorShaiXuanActivity.this, "数据加载中");
        }
    }

    //获取tag空间等信息
    private void getTagData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ColorShaiXuanActivity.this, getString(R.string.filter_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        data_msg = "{ \"tag_id\": " + data_msg + "}";
                        Message msg = new Message();
                        msg.obj = data_msg;
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ColorShaiXuanActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ColorShaiXuanActivity.this, getString(R.string.filter_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getPicTagData(callBack);
    }

    //获取tag颜色信息
    private void getColorData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ColorShaiXuanActivity.this, getString(R.string.color_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        Message msg = new Message();
                        msg.obj = data_msg;
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ColorShaiXuanActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ColorShaiXuanActivity.this, getString(R.string.color_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getColorListData(callBack);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                iv_kongjian.setImageResource(R.drawable.kongjian);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai1);
                break;
            case 1:
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai1);
                break;
            case 2:
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai1);
                break;
            case 3:
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna);
                iv_secai.setImageResource(R.drawable.secai1);
                break;
            case 4:
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDismiss() {
        if (homeTabPopWin != null) {
            homeTabPopWin.dismiss();
            iv_kongjian.setImageResource(R.drawable.kongjian1);
            iv_jubu.setImageResource(R.drawable.jubu1);
            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
            iv_shouna.setImageResource(R.drawable.shouna1);
            iv_secai.setImageResource(R.drawable.secai1);
        }
    }

    @Override
    public void onItemClick(String tagStr) {
        onDismiss();
        //跳转到筛选结果页
        Intent intent = new Intent(this, ShaiXuanResultActicity.class);
        intent.putExtra("shaixuan_tag", tagStr);
        startActivity(intent);
    }

    @Override
    public void onItemColorClick(ColorItemBean colorItemBean) {
        if (homeTabPopWin != null) {
            homeTabPopWin.dismiss();
            this.mColorClick = colorItemBean;
            iv_kongjian.setImageResource(R.drawable.kongjian1);
            iv_jubu.setImageResource(R.drawable.jubu1);
            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
            iv_shouna.setImageResource(R.drawable.shouna1);
            iv_secai.setImageResource(R.drawable.secai1);
        }
    }
}
