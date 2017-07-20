package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.login.LoginBean;
import com.homechart.app.myview.ProEditText;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by allen on 2017/6/05.
 */

public class NewUserNameActivity extends BaseActivity
        implements View.OnClickListener,
        ProEditText.RightPicOnclickListener {

    private RelativeLayout mToMast;
    private ImageView mHeader;
    private ImageButton mBack;
    private ProEditText mName;
    private TextView mTital;
    private Button mLogin;

    private String iconurl;
    private String openid;
    private String token;
    private String mNikeName;
    private String mPlatform;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_newuser_name;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        openid = getIntent().getStringExtra(ClassConstant.LoginByYouMeng.OPENID);
        token = getIntent().getStringExtra(ClassConstant.LoginByYouMeng.TOKEN);
        mPlatform = getIntent().getStringExtra(ClassConstant.LoginByYouMeng.PLATFORM);
        mNikeName = getIntent().getStringExtra(ClassConstant.LoginByYouMeng.NIKENAME);
        iconurl = getIntent().getStringExtra(ClassConstant.LoginByYouMeng.ICONURL);
    }

    @Override
    protected void initView() {
        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        mHeader = (ImageView) findViewById(R.id.iv_user_header);
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mName = (ProEditText) findViewById(R.id.et_login_name);
        mLogin = (Button) findViewById(R.id.btn_login_demand);
        mToMast = (RelativeLayout) findViewById(R.id.rl_jumpto_mast);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mToMast.setOnClickListener(this);
        mName.addTextChangedListener(watcher);
        mName.setRightPicOnclickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTital.setText(R.string.um_nikename_tital);
        mName.setText(mNikeName);
        ImageUtils.displayRoundImage(iconurl, mHeader);
        ToastUtils.showCenter(this, UIUtils.getString(R.string.um_login_nikename_hased));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                NewUserNameActivity.this.finish();
                break;
            case R.id.btn_login_demand:
                platLogin(openid, token, mPlatform, mNikeName);
                break;
            case R.id.rl_jumpto_mast:
                Intent intent = new Intent(this, UserMastActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void rightPicClick() {
        mNikeName = "";
        mName.setText("");
    }

    private void platLogin
            (final String openid,
             final String token,
             final String platform,
             final String name) {

        CustomProgress.show(NewUserNameActivity.this, "修改中...", false, null);
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(NewUserNameActivity.this, UIUtils.getString(R.string.um_login_error));
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
                        Intent intent_result = getIntent();
                        setResult(1, intent_result);
                        CustomProgress.cancelDialog();
                        NewUserNameActivity.this.finish();
                    } else {
                        CustomProgress.cancelDialog();
                        if (error_code == 1006) {
                            ToastUtils.showCenter(NewUserNameActivity.this, UIUtils.getString(R.string.um_login_nikename_hased));
                        } else {
                            ToastUtils.showCenter(NewUserNameActivity.this, error_msg);
                        }
                    }
                } catch (JSONException e) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(NewUserNameActivity.this, UIUtils.getString(R.string.um_login_nikename_error));
                }
            }
        };
        MyHttpManager.getInstance().userLoginByYouMeng(platform, token, openid, name, callBack);
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mNikeName = s.toString();
        }
    };

}
