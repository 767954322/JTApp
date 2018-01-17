package com.homechart.app.home.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.adapter.MyZhongJiangAdapter;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.hddetails.HDDetailsBean;
import com.homechart.app.home.bean.huodongpeople.ZhongBean;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.OnClick;

/**
 * Created by gumenghao on 18/1/17.
 */

public class ZhongJiangListActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_tital_comment;
    private String activity_id;
    private ImageButton mBack;
    private ListView lv_listview;
    private ZhongBean zhongBean;

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        activity_id = getIntent().getStringExtra("activity_id");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_zhongjiang;
    }

    @Override
    protected void initView() {
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        lv_listview = (ListView) findViewById(R.id.lv_listview);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("中奖名单");
        getPrizeUser();
    }

    private void getPrizeUser() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ZhongJiangListActivity.this, "获奖名单获取失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        String strData = "{\"data\":" + data_msg + "}";
                        zhongBean = GsonUtil.jsonToBean(strData, ZhongBean.class);
                        mHandler.sendEmptyMessage(0);
                    } else {
                        ToastUtils.showCenter(ZhongJiangListActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ZhongJiangListActivity.this, "获奖名单获取失败");
                }
            }
        };
        MyHttpManager.getInstance().getPrizeUser(activity_id, callBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ZhongJiangListActivity.this.finish();
                break;
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (zhongBean != null) {
                MyZhongJiangAdapter mAdapter = new MyZhongJiangAdapter(zhongBean.getData().getPrize_list(), ZhongJiangListActivity.this);
                lv_listview.setAdapter(mAdapter);
            }
        }
    };

}
