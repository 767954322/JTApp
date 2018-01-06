package com.homechart.app.lingganji.ui.activity;

import android.content.Context;
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
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by gumenghao on 18/1/3.
 */

public class InspirationEditActivity extends BaseActivity
        implements View.OnClickListener, InterDioalod {

    private String mUserId;
    private EditText mETName;
    private EditText mETMiaoSu;
    private TextView mTVSure;
    private MyDialog mDialog;
    private RelativeLayout id_main;
    private InputMethodManager imm;
    private ImageButton mBack;
    private TextView mTital;
    private String mAlbum_id;
    private String mName;
    private String mDescription;

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_activity_edit_inspiration;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        mUserId = getIntent().getStringExtra("userid");
        mAlbum_id = getIntent().getStringExtra("album_id");
        mName = getIntent().getStringExtra("name");
        mDescription = getIntent().getStringExtra("description");
    }

    @Override
    protected void initView() {

        id_main = (RelativeLayout) this.findViewById(R.id.id_main);
        mBack = (ImageButton) this.findViewById(R.id.nav_left_imageButton);
        mETName = (EditText) this.findViewById(R.id.et_name);
        mETMiaoSu = (EditText) this.findViewById(R.id.et_miaosu);
        mTVSure = (TextView) this.findViewById(R.id.tv_content_right);
        mTital = (TextView) this.findViewById(R.id.tv_tital_comment);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
        mTVSure.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTital.setText("编辑灵感辑");
        mTVSure.setText("完成");
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mDialog = new MyDialog(InspirationEditActivity.this, "是否保存修改？", InspirationEditActivity.this);
        mETName.setText(mName);
        mETName.setSelection(mName.length());
        mETMiaoSu.setText(mDescription);
        mETMiaoSu.setSelection(mDescription.length());
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.nav_left_imageButton) {
            String name = mETName.getText().toString();
            String miaosu = mETMiaoSu.getText().toString();
            if (name.equals(mName) && miaosu.equals(mDescription)) {
                this.setResult(3, this.getIntent());
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

        } else if (i == R.id.tv_content_right) {

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
        CustomProgress.show(InspirationEditActivity.this, "创建中...", false, null);
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
                ToastUtils.showCenter(InspirationEditActivity.this, "灵感辑编辑失败");
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
                        InspirationEditActivity.this.setResult(3, InspirationEditActivity.this.getIntent());
                        InspirationEditActivity.this.finish();
                    } else {
                        ToastUtils.showCenter(InspirationEditActivity.this, "灵感辑编辑失败");
                    }
                } catch (JSONException e) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(InspirationEditActivity.this, "灵感辑编辑失败");
                }
            }
        };
        MyHttpManager.getInstance().editInspiration(mAlbum_id, name, miaosu, callBack);
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

}
