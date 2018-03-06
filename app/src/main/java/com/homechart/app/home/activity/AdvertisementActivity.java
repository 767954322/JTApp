package com.homechart.app.home.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.KeyConstans;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.allset.AllSetBean;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.jaeger.library.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Allen.Gu .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file AdvertisementActivity.java .
 * @brief 启动动画 .
 */
public class AdvertisementActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_time;
    private ImageView iv_flush;
    private WebView wv_webView;
    private int show_time = 3;
    private TextView tv_time;
    private boolean loadweb_success = true;
    private Handler handler = new Handler() {
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_advertisement;
    }

    @Override
    protected void initView() {
        rl_time = (RelativeLayout) findViewById(R.id.rl_time);
        wv_webView = (WebView) findViewById(R.id.wv_webView);
        iv_flush = (ImageView) findViewById(R.id.iv_flush);
        tv_time = (TextView) findViewById(R.id.tv_time);
    }

    @Override
    protected void initListener() {
        super.initListener();
        rl_time.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_time:
                Intent intent = new Intent(AdvertisementActivity.this, HomeActivity.class);
                startActivity(intent);
                handler.removeCallbacksAndMessages(null);
                finish();
                break;
        }
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void initData(Bundle savedInstanceState) {
        getAllSet();
        handler.postDelayed(runnable, 500);
    }

    private void getAllSet() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        AllSetBean allSetBean = GsonUtil.jsonToBean(data_msg,AllSetBean.class);
                        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.IS_ENABLE_ITEM_SIMILAR, allSetBean.getConfig_info().getIs_enable_item_similar());
                        if(!TextUtils.isEmpty(allSetBean.getConfig_info().getIs_subscribed_tag())){
                            SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.IS_SUBSCRIBED_TAG, allSetBean.getConfig_info().getIs_subscribed_tag());
                        }
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().allSet(callBack);

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(AdvertisementActivity.this, HomeActivity.class);
            startActivity(intent);
            handler.removeCallbacksAndMessages(null);
            AdvertisementActivity.this.finish();
        }
    };

    Runnable runnable_timeui = new Runnable() {
        @Override
        public void run() {
            --show_time;
            if (show_time == -1) {
                Intent intent = new Intent(AdvertisementActivity.this, HomeActivity.class);
                startActivity(intent);
                handler.removeCallbacksAndMessages(null);
                finish();
            } else {
                handler.postDelayed(runnable_timeui, 1000);
                tv_time.setText("跳过" + show_time + "s");
            }
        }
    };

    //退出时的时间
    private long mExitTime;

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 3000) {
            ToastUtils.showCenter(AdvertisementActivity.this, "再次点击返回键退出");
            mExitTime = System.currentTimeMillis();
        } else {
            AdvertisementActivity.this.finish();
            System.exit(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("广告页");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("广告页");
        MobclickAgent.onPause(this);
    }
}

