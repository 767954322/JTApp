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
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.login.LoginBean;
import com.homechart.app.home.bean.register.JiYanBean;
import com.homechart.app.commont.RegexUtil;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.alertview.AlertView;
import com.homechart.app.utils.alertview.OnItemClickListener;
import com.homechart.app.utils.geetest.GeetestTest;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.homechart.app.R.id.tv_tital_comment;

/**
 * Created by allen on 2017/6/1.
 */

public class RegisterActivity extends BaseActivity
        implements View.OnClickListener,
        GeetestTest.CallBack,
        PublicUtils.ILoginUmeng {


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
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {

        mRLJumpMast = (RelativeLayout) findViewById(R.id.rl_jumpto_mast);
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(tv_tital_comment);
        mTVLoginQQ = (TextView) findViewById(R.id.tv_login_qq);
        mTVLoginSina = (TextView) findViewById(R.id.tv_login_sina);
        mTVLoginWeiXin = (TextView) findViewById(R.id.tv_login_weixin);
        mTVSendJiYan = (TextView) findViewById(R.id.tv_get_yanzhengma);
        mIVIfShowPass = (ImageView) findViewById(R.id.iv_show_pass);
        mBTRegister = (Button) findViewById(R.id.btn_regiter_demand);
        mETPhone = (EditText) findViewById(R.id.et_regiter_phone);
        mETNikeName = (EditText) findViewById(R.id.et_regiter_name);
        mETPassWord = (EditText) findViewById(R.id.et_register_password);
        mETYanZheng = (EditText) findViewById(R.id.et_regiter_yanzhengma);

    }

    @Override
    protected void initListener() {
        super.initListener();

        mIBBack.setOnClickListener(this);
        mTVLoginQQ.setOnClickListener(this);
        mTVLoginWeiXin.setOnClickListener(this);
        mTVLoginSina.setOnClickListener(this);
        mIVIfShowPass.setOnClickListener(this);
        mTVSendJiYan.setOnClickListener(this);
        mBTRegister.setOnClickListener(this);
        mRLJumpMast.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTVTital.setText(R.string.register_tital);
        umAuthListener = new PublicUtils.UmAuthListener(RegisterActivity.this, this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:

                RegisterActivity.this.finish();

                break;

            case R.id.tv_login_qq:
                UMShareAPI.get(RegisterActivity.this).getPlatformInfo(RegisterActivity.this, SHARE_MEDIA.QQ, umAuthListener);

                break;
            case R.id.tv_login_weixin:
                UMShareAPI.get(RegisterActivity.this).getPlatformInfo(RegisterActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);

                break;
            case R.id.tv_login_sina:
                UMShareAPI.get(RegisterActivity.this).getPlatformInfo(RegisterActivity.this, SHARE_MEDIA.SINA, umAuthListener);

                break;

            case R.id.iv_show_pass:


                clickChangePassStatus();

                break;

            case R.id.tv_get_yanzhengma:


                clickSendMessage();

                break;
            case R.id.btn_regiter_demand:
                clickRegister();

                break;
            case R.id.rl_jumpto_mast:

                clickJumpMast();

                break;

        }
    }

    //判断权限是否添加
    private void clickSendMessage() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            new AlertView(UIUtils.getString(R.string.addpromiss),
                    null, UIUtils.getString(R.string.setpromiss), new String[]{UIUtils.getString(R.string.okpromiss)},
                    null, this, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object object, int position) {
                    if (position == -1) {
                        Uri packageURI = Uri.parse(URL_HEADER + RegisterActivity.this.getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        startActivity(intent);
                    }
                }
            }).show();
        } else {
            CustomProgress.show(RegisterActivity.this, getString(R.string.jiazaiing), false, null);
            judgeMobile();
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

        mETPassWord.setSelection(mETPassWord.getText().length());
    }

    //判断手机号码是否合法,合法的话获取
    private void judgeMobile() {

        String phone = mETPhone.getText().toString();
        if (TextUtils.isEmpty(phone) || !phone.matches(RegexUtil.PHONE_REGEX)) {
            CustomProgress.cancelDialog();
            ToastUtils.showCenter(RegisterActivity.this, UIUtils.getString(R.string.phonenum_error));
        } else {
            OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(RegisterActivity.this, getString(R.string.error_judgemobile));
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
                            ToastUtils.showCenter(RegisterActivity.this, error_msg);
                        }
                    } catch (JSONException e) {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(RegisterActivity.this, getString(R.string.error_judgemobile));
                    }
                }
            };
            MyHttpManager.getInstance().judgeMobile(ClassConstant.JiYan.SIGNUP, phone, callBack);
        }
    }

    //从服务器获取极验证需要的三个参数
    private void getGYParams() {

        String phone = mETPhone.getText().toString();
        if (TextUtils.isEmpty(phone) || !phone.matches(RegexUtil.PHONE_REGEX)) {
            CustomProgress.cancelDialog();
            ToastUtils.showCenter(RegisterActivity.this, UIUtils.getString(R.string.phonenum_error));
        } else {
            OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(RegisterActivity.this, getString(R.string.error_judgemobile));
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
                            GeetestTest.openGtTest(RegisterActivity.this, dataObject, RegisterActivity.this);
                        } else {
                            CustomProgress.cancelDialog();
                            ToastUtils.showCenter(RegisterActivity.this, error_msg);
                        }
                    } catch (JSONException e) {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(RegisterActivity.this, getString(R.string.error_judgemobile));
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
                ToastUtils.showCenter(RegisterActivity.this, getString(R.string.error_sendmessage));
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
                        ToastUtils.showCenter(RegisterActivity.this, getString(R.string.succes_sendmessage));
                    } else {
                        ToastUtils.showCenter(RegisterActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.showCenter(RegisterActivity.this, getString(R.string.error_sendmessage));
                }
            }
        };
        MyHttpManager.getInstance().sendMessageByJY(ClassConstant.JiYan.SIGNUP, mobile, challenge, validate, seccode, callBack);
    }

    //点击注册按钮
    private void clickRegister() {

        String phone = mETPhone.getText().toString();
        String yzCode = mETYanZheng.getText().toString();
        String nikeName = mETNikeName.getText().toString();
        String passWord = mETPassWord.getText().toString();
        if (TextUtils.isEmpty(phone) || !phone.matches(RegexUtil.PHONE_REGEX)) {
            ToastUtils.showCenter(RegisterActivity.this, UIUtils.getString(R.string.phonenum_error));
            return;
        }
        if (TextUtils.isEmpty(yzCode)) {
            ToastUtils.showCenter(RegisterActivity.this, UIUtils.getString(R.string.yanzhengma_error));
            return;
        }
        if (TextUtils.isEmpty(nikeName) || !nikeName.matches(RegexUtil.ADDRESS_REGEX_NAME)) {
            ToastUtils.showCenter(RegisterActivity.this, UIUtils.getString(R.string.nikename_error));
            return;
        }
        if (TextUtils.isEmpty(passWord) || !passWord.matches(RegexUtil.ADDRESS_REGEX_PASS)) {
            ToastUtils.showCenter(RegisterActivity.this, UIUtils.getString(R.string.password_error));
            return;
        }

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(RegisterActivity.this, "注册失败，请重新注册！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        LoginBean loginBean = GsonUtil.jsonToBean(data_msg, LoginBean.class);
                        PublicUtils.loginSucces(loginBean);
                        Intent intent_result = getIntent();
                        setResult(1, intent_result);
                        RegisterActivity.this.finish();
                    } else {
                        ToastUtils.showCenter(RegisterActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().registerByMobile(phone, yzCode, nikeName, passWord, callBack);

    }

    private void clickJumpMast() {
        Intent intent = new Intent(this, UserMastActivity.class);
        startActivity(intent);
    }

    /**
     * 友盟登陆的回调
     *
     * @param openid
     * @param token
     * @param plat
     * @param name
     * @param iconurl
     */
    @Override
    public void loginUmengBack(final String openid, final String token, final String plat, final String name, final String iconurl) {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(RegisterActivity.this, UIUtils.getString(R.string.um_login_error));
            }

            @Override
            public void onResponse(String response) {
                CustomProgress.cancelDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    if (error_code == 0) {
                        String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                        LoginBean loginBean = GsonUtil.jsonToBean(data_msg, LoginBean.class);
                        PublicUtils.loginSucces(loginBean);
                        Intent intent_result = getIntent();
                        setResult(1, intent_result);
                        RegisterActivity.this.finish();
                    } else {
                        if (error_code == 1006) {
                            Intent intent_change = new Intent(RegisterActivity.this, NewUserNameActivity.class);
                            intent_change.putExtra(ClassConstant.LoginByYouMeng.OPENID, openid);
                            intent_change.putExtra(ClassConstant.LoginByYouMeng.TOKEN, token);
                            intent_change.putExtra(ClassConstant.LoginByYouMeng.PLATFORM, plat);
                            intent_change.putExtra(ClassConstant.LoginByYouMeng.NIKENAME, name);
                            intent_change.putExtra(ClassConstant.LoginByYouMeng.ICONURL, iconurl);
                            startActivityForResult(intent_change, 0);
                        } else {
                            ToastUtils.showCenter(RegisterActivity.this, error_msg);
                        }
                    }
                } catch (JSONException e) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(RegisterActivity.this, UIUtils.getString(R.string.um_login_nikename_error));
                }

            }
        };
        MyHttpManager.getInstance().userLoginByYouMeng(plat, token, openid, name, callBack);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

        //注册成功后的跳转
        if (requestCode == 0 && resultCode == 1) {
            Intent intent_result = getIntent();
            setResult(1, intent_result);
            RegisterActivity.this.finish();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        CustomProgress.cancelDialog();
        MobclickAgent.onResume(RegisterActivity.this);
    }


    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPause(RegisterActivity.this);
    }
    private RelativeLayout mRLJumpMast;
    private ImageView mIVIfShowPass;
    private ImageButton mIBBack;
    private Button mBTRegister;

    private TextView mTVTital;
    private TextView mTVLoginQQ;
    private TextView mTVLoginSina;
    private TextView mTVSendJiYan;
    private TextView mTVLoginWeiXin;

    private EditText mETPhone;
    private EditText mETYanZheng;
    private EditText mETNikeName;
    private EditText mETPassWord;
    private boolean isChecked = true;
    private PublicUtils.UmAuthListener umAuthListener;
    private final String URL_HEADER = "package:";

}
