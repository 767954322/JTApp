package com.homechart.app.home.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.RegexUtil;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.login.LoginBean;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.alertview.AlertView;
import com.homechart.app.utils.alertview.OnItemClickListener;
import com.homechart.app.utils.geetest.GeetestTest;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import static com.homechart.app.R.id.tv_tital_comment;

/**
 * Created by gumenghao on 17/6/5.
 */

public class ResetPasswordActivity
        extends BaseActivity
        implements View.OnClickListener,
        GeetestTest.CallBack {
    private ImageButton mIBBack;
    private TextView mTVTital;
    private TextView mTVSendJiYan;
    private ImageView mIVIfShowPass;
    private Button mBTRegister;
    private EditText mETPhone;
    private EditText mETPassWord;
    private EditText mETYanZheng;
    private boolean isChecked = true;
    private final String URL_HEADER = "package:";
    CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            mTVSendJiYan.setText(millisUntilFinished / 1000 + UIUtils.getString(R.string.yanzhengma_end));
        }

        @Override
        public void onFinish() {
            mTVSendJiYan.setEnabled(true);
            mTVSendJiYan.setText(R.string.yanzhengma_hint);
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void initView() {

        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(tv_tital_comment);
        mTVSendJiYan = (TextView) findViewById(R.id.tv_get_yanzhengma);
        mIVIfShowPass = (ImageView) findViewById(R.id.iv_show_pass);
        mBTRegister = (Button) findViewById(R.id.btn_regiter_demand);
        mETPhone = (EditText) findViewById(R.id.et_regiter_phone);
        mETPassWord = (EditText) findViewById(R.id.et_register_password);
        mETYanZheng = (EditText) findViewById(R.id.et_regiter_yanzhengma);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mIBBack.setOnClickListener(this);
        mIVIfShowPass.setOnClickListener(this);
        mTVSendJiYan.setOnClickListener(this);
        mBTRegister.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTVTital.setText(R.string.reset_pass_tital);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:

                ResetPasswordActivity.this.finish();

                break;

            case R.id.iv_show_pass:


                clickChangePassStatus();

                break;

            case R.id.tv_get_yanzhengma:


                clickSendMessage();

                break;
            case R.id.btn_regiter_demand:

                clickResetPWD();

                break;

        }
    }

    //密码显示隐藏状态改变
    private void clickChangePassStatus() {

        if (isChecked) {
            mETPassWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mIVIfShowPass.setImageResource(R.drawable.zhengyan);
            isChecked = false;
        } else {
            mETPassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mIVIfShowPass.setImageResource(R.drawable.biyan);
            isChecked = true;
        }

    }

    //判断权限是否添加
    private void clickSendMessage() {
        if (ContextCompat.checkSelfPermission(ResetPasswordActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            new AlertView(UIUtils.getString(R.string.addpromiss),
                    null, UIUtils.getString(R.string.setpromiss), new String[]{UIUtils.getString(R.string.okpromiss)},
                    null, this, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object object, int position) {
                    if (position == -1) {
                        Uri packageURI = Uri.parse(URL_HEADER + ResetPasswordActivity.this.getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        startActivity(intent);
                    }
                }
            }).show();
        } else {
            CustomProgress.show(ResetPasswordActivity.this, getString(R.string.jiazaiing), false, null);
            judgeMobile();
        }
    }


    //判断手机号码是否合法,合法的话获取
    private void judgeMobile() {

        String phone = mETPhone.getText().toString();
        if (TextUtils.isEmpty(phone) || !phone.matches(RegexUtil.PHONE_REGEX)) {
            CustomProgress.cancelDialog();
            ToastUtils.showCenter(ResetPasswordActivity.this, UIUtils.getString(R.string.phonenum_error));
        } else {
            OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(ResetPasswordActivity.this, getString(R.string.error_judgemobile));
                }

                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                        String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                        if (error_code == 0) {
                            getGYParams();
                        } else {
                            CustomProgress.cancelDialog();
                            ToastUtils.showCenter(ResetPasswordActivity.this, error_msg);
                        }
                    } catch (JSONException e) {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(ResetPasswordActivity.this, getString(R.string.error_judgemobile));
                    }
                }
            };
            MyHttpManager.getInstance().judgeMobile(ClassConstant.JiYan.FINDPWD, phone, callBack);
        }
    }


    //从服务器获取极验证需要的三个参数
    private void getGYParams() {

        String phone = mETPhone.getText().toString();
        if (TextUtils.isEmpty(phone) || !phone.matches(RegexUtil.PHONE_REGEX)) {
            CustomProgress.cancelDialog();
            ToastUtils.showCenter(ResetPasswordActivity.this, UIUtils.getString(R.string.phonenum_error));
        } else {
            OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(ResetPasswordActivity.this, getString(R.string.error_judgemobile));
                }

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                        String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                        String data = jsonObject.getString(ClassConstant.Parame.DATA);
                        if (error_code == 0) {
                            JSONObject dataObject = new JSONObject(data);
                            GeetestTest.openGtTest(ResetPasswordActivity.this, dataObject, ResetPasswordActivity.this);
                        } else {
                            CustomProgress.cancelDialog();
                            ToastUtils.showCenter(ResetPasswordActivity.this, error_msg);
                        }
                    } catch (JSONException e) {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(ResetPasswordActivity.this, getString(R.string.error_judgemobile));
                    }
                }
            };
            MyHttpManager.getInstance().getParamsFromMyServiceJY(callBack);
        }
    }

    //极验滑块后的回调
    @Override
    public void geetestCallBack(final String challenge, final String validate, final String seccode) {

        String mobile = mETPhone.getText().toString().trim();
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ResetPasswordActivity.this, getString(R.string.error_sendmessage));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    if (error_code == 0) {
                        mTVSendJiYan.setEnabled(false);
                        timer.start();
                        ToastUtils.showCenter(ResetPasswordActivity.this, getString(R.string.succes_sendmessage));
                    } else {
                        ToastUtils.showCenter(ResetPasswordActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.showCenter(ResetPasswordActivity.this, getString(R.string.error_sendmessage));
                }
            }
        };
        MyHttpManager.getInstance().sendMessageByJY(ClassConstant.JiYan.FINDPWD, mobile, challenge, validate, seccode, callBack);
    }


    //点击修改密码
    private void clickResetPWD() {

        String phone = mETPhone.getText().toString();
        String yzCode = mETYanZheng.getText().toString();
        String passWord = mETPassWord.getText().toString();
        if (TextUtils.isEmpty(phone)  ) {
            ToastUtils.showCenter(ResetPasswordActivity.this, UIUtils.getString(R.string.phonenum_error));
            return;
        }
        if (TextUtils.isEmpty(yzCode)) {
            ToastUtils.showCenter(ResetPasswordActivity.this, "请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(passWord)) {
            ToastUtils.showCenter(ResetPasswordActivity.this, "请输入重置密码");
            return;
        }

        if(!phone.matches(RegexUtil.PHONE_REGEX)){
            ToastUtils.showCenter(ResetPasswordActivity.this, "请输入正确的手机号码");
            return;
        }

        if (!passWord.matches(RegexUtil.ADDRESS_REGEX_PASS)) {
            ToastUtils.showCenter(ResetPasswordActivity.this, " 请输入正确的登陆密码");
            return;
        }

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ResetPasswordActivity.this, getString(R.string.resetpwd_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ToastUtils.showCenter(ResetPasswordActivity.this, UIUtils.getString(R.string.resetpwd_succes));
                        ResetPasswordActivity.this.finish();
                    } else {
                        ToastUtils.showCenter(ResetPasswordActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().resetPassWord(phone, yzCode, passWord, callBack);

    }

}
