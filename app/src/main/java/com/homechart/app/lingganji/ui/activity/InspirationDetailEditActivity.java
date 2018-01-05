package com.homechart.app.lingganji.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.contract.InterDioalod;
import com.homechart.app.commont.utils.MyDialog;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gumenghao on 18/1/5.
 */

public class InspirationDetailEditActivity extends BaseActivity
        implements View.OnClickListener,
        InterDioalod {
    private ImageButton mBack;
    private TextView mTital;
    private ImageView iv_image;
    private TextView tv_image_up_time;
    private String imageUrl;
    private String description;
    private EditText et_miaosu;
    private String updata_time;
    private TextView tv_content_right;
    private String item_id;
    private int position;
    private MyDialog mDialog;
    private InputMethodManager imm;
    private RelativeLayout id_main;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_inspiration_edit;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        imageUrl = getIntent().getStringExtra("url");
        description = getIntent().getStringExtra("description");
        updata_time = getIntent().getStringExtra("updata_time");
        item_id = getIntent().getStringExtra("item_id");
        position = getIntent().getIntExtra("position", -1);
    }

    @Override
    protected void initView() {

        id_main = (RelativeLayout) this.findViewById(R.id.id_main);
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        tv_image_up_time = (TextView) findViewById(R.id.tv_image_up_time);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        et_miaosu = (EditText) findViewById(R.id.et_miaosu);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mDialog = new MyDialog(InspirationDetailEditActivity.this, "是否保存修改？", InspirationDetailEditActivity.this);
        mTital.setText("图片描述编辑");
        tv_content_right.setText("完成");
        if (!TextUtils.isEmpty(description)) {
            et_miaosu.setText(description);
            et_miaosu.setSelection(et_miaosu.getText().length());
        }
        String time = updata_time.substring(0, 10);
        time = time.replace("-", "/");
        tv_image_up_time.setText("最近更新 " + time);
        ImageUtils.displayFilletImage(imageUrl, iv_image);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                if (et_miaosu.getText().toString().equals(description)) {
                    InspirationDetailEditActivity.this.finish();
                } else {
                    //软键盘如果打开的话，关闭软键盘
                    boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                    if (isOpen) {
                        if (getCurrentFocus() != null) {//强制关闭软键盘
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                    mDialog.showAtLocation(id_main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.tv_content_right:
                if (et_miaosu.getText().toString().equals(description)) {
                    InspirationDetailEditActivity.this.finish();
                } else {
                    CustomProgress.show(InspirationDetailEditActivity.this, "修改中...", false, null);
                    upInspirationDescription(et_miaosu.getText().toString());
                }
                break;
        }
    }

    private void upInspirationDescription(final String et_miaosu) {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(InspirationDetailEditActivity.this, "编辑失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(InspirationDetailEditActivity.this, "编辑成功！");
                        Intent intent = InspirationDetailEditActivity.this.getIntent();
                        intent.putExtra("position", position);
                        intent.putExtra("description", et_miaosu);
                        InspirationDetailEditActivity.this.setResult(1, intent);
                        InspirationDetailEditActivity.this.finish();
                    } else {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(InspirationDetailEditActivity.this, "编辑失败！");
                    }
                } catch (JSONException e) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(InspirationDetailEditActivity.this, "编辑失败！");
                }
            }
        };
        MyHttpManager.getInstance().editImage(item_id, et_miaosu, callBack);

    }


    @Override
    public void onQuXiao() {
        InspirationDetailEditActivity.this.finish();
    }

    @Override
    public void onQueRen() {
        CustomProgress.show(InspirationDetailEditActivity.this, "修改中...", false, null);
        upInspirationDescription(et_miaosu.getText().toString());
    }
}
