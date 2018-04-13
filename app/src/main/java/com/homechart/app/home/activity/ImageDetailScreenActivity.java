package com.homechart.app.home.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.fragment.HomeCenterFragment;
import com.homechart.app.home.fragment.ImageDetailsScreenFragment;

import java.util.List;

/**
 * Created by gumenghao on 18/4/13.
 */

public class ImageDetailScreenActivity extends BaseActivity {

    private RelativeLayout main;
    private ImageDetailsScreenFragment mImageDetailsScreenFragment;
    private String item_id;
    private String type;
    private int mPosition;
    private List<String> mItemIdList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_screen;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        item_id = getIntent().getStringExtra("item_id");
        type = getIntent().getStringExtra("type");
        mPosition = getIntent().getIntExtra("position", -1);
        mItemIdList = (List<String>) getIntent().getSerializableExtra("item_id_list");

    }

    @Override
    protected void initView() {
        main = (RelativeLayout) findViewById(R.id.main);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (null == mImageDetailsScreenFragment) {
            mImageDetailsScreenFragment = new ImageDetailsScreenFragment(getSupportFragmentManager(),item_id,type,mPosition,mItemIdList);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.main_content, mImageDetailsScreenFragment).commit();

    }
}
