package com.homechart.app.lingganji.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.activity.IssueBackActivity;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.socialize.media.Base;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by gumenghao on 18/1/3.
 */

public class InspirationCreateActivity extends BaseActivity
        implements View.OnClickListener {

    private String mUserId;
    private ImageView mDismiss;
    private EditText mETName;
    private EditText mETMiaoSu;
    private TextView mTVSure;

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

        mDismiss = (ImageView) this.findViewById(R.id.iv_dismiss);
        mETName = (EditText) this.findViewById(R.id.et_name);
        mETMiaoSu = (EditText) this.findViewById(R.id.et_miaosu);
        mTVSure = (TextView) this.findViewById(R.id.tv_sure);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mDismiss.setOnClickListener(this);
        mTVSure.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_dismiss) {
            this.finish();
            this.overridePendingTransition(R.anim.pop_exit_anim, 0);
        } else if (i == R.id.tv_sure) {

            ifAddInspiration();

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
                Log.d("test", "失败");
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
        MyHttpManager.getInstance().createInspiration(name, miaosu, callBack);


    }
}
