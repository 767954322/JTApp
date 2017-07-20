package com.homechart.app.home.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.KeyConstans;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.login.LoginBean;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by allen on 2017/6/1.
 */
public class LoginActivity extends BaseActivity
        implements View.OnClickListener,
        PublicUtils.ILoginUmeng {


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        mTVToRegister = (TextView) findViewById(R.id.tv_goto_register);
        mTVGorgetPass = (TextView) findViewById(R.id.tv_gorget_pass);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        mTVLoginWeiXin = (TextView) findViewById(R.id.tv_login_weixin);
        mTVLoginSina = (TextView) findViewById(R.id.tv_login_sina);
        mTVLoginQQ = (TextView) findViewById(R.id.tv_login_qq);
        mIVIfShowPass = (ImageView) findViewById(R.id.iv_show_pass);
        mBTSendDemand = (Button) findViewById(R.id.btn_send_demand);
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mETLoginPass = (EditText) findViewById(R.id.et_login_password);
        mETLoginName = (EditText) findViewById(R.id.et_login_name);

    }

    @Override
    protected void initListener() {
        super.initListener();

        mBTSendDemand.setOnClickListener(this);
        mTVGorgetPass.setOnClickListener(this);
        mIVIfShowPass.setOnClickListener(this);
        mTVToRegister.setOnClickListener(this);
        mTVLoginQQ.setOnClickListener(this);
        mTVLoginWeiXin.setOnClickListener(this);
        mTVLoginSina.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //设置权限
        PublicUtils.verifyStoragePermissions(LoginActivity.this);
        boolean login_status = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
        if (login_status) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        } else {
            PublicUtils.changeEditTextHint(getString(R.string.login_name_hint), mETLoginName, 14);
            PublicUtils.changeEditTextHint(getString(R.string.login_pass_hint), mETLoginPass, 14);
            umAuthListener = new PublicUtils.UmAuthListener(LoginActivity.this, this);
            mTVTital.setText(R.string.login_tital);
            mIBBack.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_login_qq:
                //友盟统计
                HashMap<String, String> map_qq = new HashMap<String, String>();
                map_qq.put("evenname", "qq第三方登录");
                map_qq.put("even", "点击使用QQ作为第三方登录按钮");
                MobclickAgent.onEvent(LoginActivity.this, "action31", map_qq);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击使用QQ作为第三方登录按钮")  //事件类别
                        .setAction("qq第三方登录")      //事件操作
                        .build());
                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);

                break;
            case R.id.tv_login_weixin:
                //友盟统计
                HashMap<String, String> map_weixin = new HashMap<String, String>();
                map_weixin.put("evenname", "wechat第三方登录");
                map_weixin.put("even", "点击使用微信作为第三方登录按钮");
                MobclickAgent.onEvent(LoginActivity.this, "action29", map_weixin);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击使用微信作为第三方登录按钮")  //事件类别
                        .setAction("wechat第三方登录")      //事件操作
                        .build());
                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.tv_login_sina:
                //友盟统计
                HashMap<String, String> map_sina = new HashMap<String, String>();
                map_sina.put("evenname", "weibo第三方登录");
                map_sina.put("even", "点击使用微博作为第三方登录按钮");
                MobclickAgent.onEvent(LoginActivity.this, "action27", map_sina);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击使用微博作为第三方登录按钮")  //事件类别
                        .setAction("weibo第三方登录")      //事件操作
                        .build());
                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, umAuthListener);

                break;

            case R.id.btn_send_demand:

                //友盟统计
                HashMap<String, String> map1 = new HashMap<String, String>();
                map1.put("evenname", "登录");
                map1.put("even", "点击登录");
                MobclickAgent.onEvent(LoginActivity.this, "action26", map1);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击登录")  //事件类别
                        .setAction("登录")      //事件操作
                        .build());
                clickLoginUser();

                break;
            case R.id.tv_gorget_pass:
                //友盟统计
                HashMap<String, String> map_forget = new HashMap<String, String>();
                map_forget.put("evenname", "忘记密码");
                map_forget.put("even", "点击忘记密码进入密码重置流程");
                MobclickAgent.onEvent(LoginActivity.this, "action83", map_forget);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击忘记密码进入密码重置流程")  //事件类别
                        .setAction("忘记密码")      //事件操作
                        .build());
                clickGorgetPass();

                break;
            case R.id.iv_show_pass:

                clickChangePassStatus();

                break;
            case R.id.tv_goto_register:

                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 0);

                break;
        }
    }

    //用户账号密码登陆
    private void clickLoginUser() {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showCenter(LoginActivity.this, UIUtils.getString(R.string.login_error));
                try {
                    Log.d("test", "失败：" + error.getMessage().toString());
                } catch (Exception e) {
                }
            }

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    if (error_code == 0) {
                        String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                        LoginBean loginBean = GsonUtil.jsonToBean(data_msg, LoginBean.class);
                        PublicUtils.loginSucces(loginBean);
                        //友盟统计
                        HashMap<String, String> map_forget = new HashMap<String, String>();
                        map_forget.put("evenname", "登陆成功提示");
                        map_forget.put("even", "登陆成功后的提示");
                        MobclickAgent.onEvent(LoginActivity.this, "action33", map_forget);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("登陆成功后的提示")  //事件类别
                                .setAction("登陆成功提示")      //事件操作
                                .build());
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    } else {
                        ToastUtils.showCenter(LoginActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(LoginActivity.this, UIUtils.getString(R.string.login_error));
                }


            }
        };
        MyHttpManager.getInstance().userLogin(mETLoginName.getText().toString(), mETLoginPass.getText().toString(), callback);

    }

    //密码显示隐藏状态改变
    private void clickChangePassStatus() {

        if (isChecked) {
            //选择状态 显示明文--设置为可见的密码
            mETLoginPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mIVIfShowPass.setImageResource(R.drawable.zhengyan);
            isChecked = false;
        } else {
            //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
            mETLoginPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mIVIfShowPass.setImageResource(R.drawable.biyan);
            isChecked = true;
        }
        mETLoginPass.setSelection(mETLoginPass.getText().length());

    }

    //点击忘记密码
    private void clickGorgetPass() {

//        Intent intent1 = new Intent();
//        intent1.setAction(KeyConstans.GORGETPASS_ACTION);
//        Uri content_url = Uri.parse(KeyConstans.GORGETPASS_URL);
//        intent1.setData(content_url);
//        startActivity(intent1);

        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
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
                ToastUtils.showCenter(LoginActivity.this, UIUtils.getString(R.string.um_login_error));
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
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    } else {
                        if (error_code == 1006 || error_code == 1012) {
                            Intent intent_change = new Intent(LoginActivity.this, NewUserNameActivity.class);
                            intent_change.putExtra(ClassConstant.LoginByYouMeng.OPENID, openid);
                            intent_change.putExtra(ClassConstant.LoginByYouMeng.TOKEN, token);
                            intent_change.putExtra(ClassConstant.LoginByYouMeng.PLATFORM, plat);
                            intent_change.putExtra(ClassConstant.LoginByYouMeng.NIKENAME, name);
                            intent_change.putExtra(ClassConstant.LoginByYouMeng.ICONURL, iconurl);
                            startActivityForResult(intent_change, 0);
                        } else {
                            ToastUtils.showCenter(LoginActivity.this, error_msg);
                        }
                    }
                } catch (JSONException e) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(LoginActivity.this, UIUtils.getString(R.string.um_login_nikename_error));
                }

            }
        };
        MyHttpManager.getInstance().userLoginByYouMeng(plat, token, openid, name, callBack);
    }

    /**
     * 权限的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断请求码
        switch (requestCode) {
            case 1:
                if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
                }
                break;
            case 2:
                //TODO 最后的权限回调
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

        //注册成功后的跳转
        if (requestCode == 0 && resultCode == 1) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

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
            ToastUtils.showCenter(LoginActivity.this, "再次点击返回键退出");
            mExitTime = System.currentTimeMillis();
        } else {
            LoginActivity.this.finish();
            System.exit(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CustomProgress.cancelDialog();
        MobclickAgent.onResume(LoginActivity.this);
    }


    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPause(LoginActivity.this);
    }

    private TextView mTVToRegister;
    private TextView mTVGorgetPass;
    private TextView mTVLoginWeiXin;
    private TextView mTVLoginSina;
    private TextView mTVLoginQQ;
    private TextView mTVTital;

    private EditText mETLoginPass;
    private EditText mETLoginName;

    private ImageView mIVIfShowPass;
    private Button mBTSendDemand;
    private ImageButton mIBBack;

    private boolean isChecked = true;
    private PublicUtils.UmAuthListener umAuthListener;


}
