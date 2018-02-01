package com.homechart.app.home.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gumenghao on 18/2/1.
 */

public class DingYueGuanLiActivity extends BaseActivity implements View.OnClickListener {


    private TextView mTital;
    private TextView mRight;
    private ImageButton mBack;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            switch (code) {
                case 1:
                    break;
            }
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_guanli_dingyue;
    }

    @Override
    protected void initView() {

        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        mRight = (TextView) findViewById(R.id.tv_content_right);
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        mTital.setText("订阅管理");
        mRight.setText("完成");

        getDingYueData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                DingYueGuanLiActivity.this.finish();
                break;
        }
    }


    private void getDingYueData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(DingYueGuanLiActivity.this, "订阅列表获取失败");
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
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(DingYueGuanLiActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getDingYueGuanLiList(callBack);


    }

}
