package com.homechart.app.lingganji.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.contract.InterDioalod;
import com.homechart.app.commont.utils.MyDialog;
import com.homechart.app.home.activity.IssueBackActivity;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.media.Base;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by gumenghao on 18/1/3.
 */

public class InspirationCreateActivity extends BaseActivity
        implements View.OnClickListener, InterDioalod {

    private String mUserId;
    private ImageView mDismiss;
    private EditText mETName;
    private EditText mETMiaoSu;
    private TextView mTVSure;
    private MyDialog mDialog;
    private RelativeLayout id_main;
    private InputMethodManager imm;
    private String show_type = "1";
    private ImageView iv_icon_pubu;
    private TextView tv_big_name;
    private RoundImageView riv_icon_back;
    private RoundImageView riv_icon;
    private ImageView iv_icon_list;
    private TextView tv_small_name;
    private RoundImageView riv_icon_back1;
    private RoundImageView riv_icon1;


    @Override
    protected int getLayoutResId() {
        return R.layout.layout_activity_create_inspiration;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        mUserId = getIntent().getStringExtra("userid");
    }

    @Override
    protected void initView() {

        id_main = (RelativeLayout) this.findViewById(R.id.id_main);
        mDismiss = (ImageView) this.findViewById(R.id.iv_dismiss);
        mETName = (EditText) this.findViewById(R.id.et_name);
        mETMiaoSu = (EditText) this.findViewById(R.id.et_miaosu);
        mTVSure = (TextView) this.findViewById(R.id.tv_sure);

        iv_icon_pubu = (ImageView) this.findViewById(R.id.iv_icon_pubu);
        tv_big_name = (TextView) this.findViewById(R.id.tv_big_name);
        riv_icon_back = (RoundImageView) this.findViewById(R.id.riv_icon_back);
        riv_icon = (RoundImageView) this.findViewById(R.id.riv_icon);


        iv_icon_list = (ImageView) this.findViewById(R.id.iv_icon_list);
        tv_small_name = (TextView) this.findViewById(R.id.tv_small_name);
        riv_icon_back1 = (RoundImageView) this.findViewById(R.id.riv_icon_back1);
        riv_icon1 = (RoundImageView) this.findViewById(R.id.riv_icon1);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mDismiss.setOnClickListener(this);
        mTVSure.setOnClickListener(this);

        iv_icon_pubu.setOnClickListener(this);
        tv_big_name.setOnClickListener(this);
        riv_icon.setOnClickListener(this);

        iv_icon_list.setOnClickListener(this);
        tv_small_name.setOnClickListener(this);
        riv_icon1.setOnClickListener(this);

        mETName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String str = s.toString();
                if (str.length() > 20) {
                    ToastUtils.showCenter(InspirationCreateActivity.this, "灵感辑名称最多只能输入20个字哦");
                    mETName.setText(str.substring(0, 20));
                    mETName.setSelection(20);
                }
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mDialog = new MyDialog(InspirationCreateActivity.this, "是否要保存灵感辑？", InspirationCreateActivity.this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.iv_dismiss:
                String name = mETName.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    this.finish();
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
            case R.id.tv_sure:
                ifAddInspiration();
                break;
            case R.id.iv_icon_pubu:
            case R.id.tv_big_name:
            case R.id.riv_icon:
                riv_icon_back.setVisibility(View.GONE);
                riv_icon_back1.setVisibility(View.VISIBLE);
                riv_icon.setBackgroundColor(UIUtils.getColor(R.color.bg_e79056));
                riv_icon1.setBackgroundColor(UIUtils.getColor(R.color.white));
                show_type = "2";
                break;
            case R.id.iv_icon_list:
            case R.id.tv_small_name:
            case R.id.riv_icon1:

                riv_icon_back.setVisibility(View.VISIBLE);
                riv_icon_back1.setVisibility(View.GONE);
                riv_icon1.setBackgroundColor(UIUtils.getColor(R.color.bg_e79056));
                riv_icon.setBackgroundColor(UIUtils.getColor(R.color.white));
                show_type = "1";
                break;
        }
    }

    private void ifAddInspiration() {

        String name = mETName.getText().toString();
        if (TextUtils.isEmpty(name.trim())) {
            ToastUtils.showCenter(this, "请输入灵感辑名称");
            return;
        }
        if (!checkAccountMark(name)) {
            ToastUtils.showCenter(this, "灵感辑名称不能带特殊字符哦");
            return;
        }

        String miaosu = mETMiaoSu.getText().toString();
        CustomProgress.show(InspirationCreateActivity.this, "创建中...", false, null);
        addInspiration(name, miaosu);
    }


    //验证用户名只包含字母，数字，中文
    public boolean checkAccountMark(String account) {
        String all = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";
        Pattern pattern = Pattern.compile(all);
        return pattern.matches(all, account);
    }


    private void addInspiration(String name, String miaosu) {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                CustomProgress.cancelDialog();
                ToastUtils.showCenter(InspirationCreateActivity.this, "灵感辑创建失败");
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
                        InspirationCreateActivity.this.setResult(1, InspirationCreateActivity.this.getIntent());
                        InspirationCreateActivity.this.finish();
                    } else {
                        if (error_code == 1043) {
                            CustomProgress.cancelDialog();
                            ToastUtils.showCenter(InspirationCreateActivity.this, "灵感辑名称已存在");
                        } else if (error_code == 1042) {
                            CustomProgress.cancelDialog();
                            ToastUtils.showCenter(InspirationCreateActivity.this, "灵感辑名称不能为空");
                        } else if (error_code == 1046) {
                            CustomProgress.cancelDialog();
                            ToastUtils.showCenter(InspirationCreateActivity.this, "灵感辑名称不能超过20个字");
                        }
                    }
                } catch (JSONException e) {
                    CustomProgress.cancelDialog();

                    ToastUtils.showCenter(InspirationCreateActivity.this, "灵感辑创建失败");
                }
            }
        };
        MyHttpManager.getInstance().createInspiration(show_type,name, miaosu, callBack);


    }

    @Override
    public void onQuXiao() {
        mDialog.dismiss();
        this.finish();
    }

    @Override
    public void onQueRen() {
        mDialog.dismiss();
        ifAddInspiration();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("创建灵感辑");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("创建灵感辑");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("创建灵感辑");
        MobclickAgent.onPause(this);
    }
}
