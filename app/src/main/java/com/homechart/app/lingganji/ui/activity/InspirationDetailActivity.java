package com.homechart.app.lingganji.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.common.entity.inspirationdetail.InspirationDetailBean;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationBean;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationListBean;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gumenghao on 18/1/4.
 */

public class InspirationDetailActivity extends BaseActivity implements View.OnClickListener {
    private String mUserId;
    private InspirationBean mInspirationBean;
    private TextView mTital;
    private TextView mRightCreate;
    private ImageButton mBack;
    private ImageButton mRightIcon;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_inspiration_details;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        mUserId = getIntent().getStringExtra("user_id");
        mInspirationBean = (InspirationBean) getIntent().getSerializableExtra("InspirationBean");
    }

    @Override
    protected void initView() {
        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        mRightIcon = (ImageButton) findViewById(R.id.nav_secondary_imageButton);
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
        mRightIcon.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTital.setText("灵感辑");
        getInspirationDetail();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.nav_left_imageButton) {
            InspirationDetailActivity.this.finish();
        } else if (id == R.id.nav_secondary_imageButton) {

        }
    }

    private void getInspirationDetail() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(InspirationDetailActivity.this, "专辑详情获取失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        String newData = "{\"info\": " + data_msg + "}";
                        InspirationDetailBean inspirationDetailBean = GsonUtil.jsonToBean(newData, InspirationDetailBean.class);

                    } else {
                        ToastUtils.showCenter(InspirationDetailActivity.this, "专辑详情获取失败！");
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getInspirationDetail(mInspirationBean.getAlbum_info().getAlbum_id(), callBack);
    }


}
