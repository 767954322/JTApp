package com.homechart.app.lingganji.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.imageloader.ImageUtils;

/**
 * Created by gumenghao on 18/1/5.
 */

public class InspirationDetailEditActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton mBack;
    private TextView mTital;
    private ImageView iv_image;
    private TextView tv_image_up_time;
    private String imageUrl;
    private String description;
    private EditText et_miaosu;
    private String updata_time;

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
    }

    @Override
    protected void initView() {
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        tv_image_up_time = (TextView) findViewById(R.id.tv_image_up_time);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        et_miaosu = (EditText) findViewById(R.id.et_miaosu);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        mTital.setText("图片描述编辑");
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
                InspirationDetailEditActivity.this.finish();
                break;
        }
    }
}
