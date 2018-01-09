package com.homechart.app.commont.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.contract.InterDioalod;
import com.homechart.app.commont.contract.InterDioalod1;

/**
 * Created by gumenghao on 18/1/4.
 */

public class MyDialog1 extends PopupWindow implements View.OnClickListener {


    private InterDioalod1 mInterDioalod;
    private View view;
    private String mContent;
    private Context mContext;
    private TextView mTvQuXiao;
    private TextView mTvQueRen;
    private TextView mTvContent;

    public MyDialog1(Context context, String content, InterDioalod1 interDioalod) {
        this.mContext = context;
        this.mContent = content;
        this.mInterDioalod = interDioalod;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_dialog, null);
        initView();
        initListener();
        initPop();
        initData();
    }

    private void initView() {

        mTvQuXiao = (TextView) view.findViewById(R.id.tv_quxiao);
        mTvQueRen = (TextView) view.findViewById(R.id.tv_queren);
        mTvContent = (TextView) view.findViewById(R.id.tv_content);

    }

    private void initListener() {
        mTvQuXiao.setOnClickListener(this);
        mTvQueRen.setOnClickListener(this);
    }

    private void initPop() {
        // 设置外部可点击
        this.setOutsideTouchable(true);
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 设置弹出窗体显示时的动画，从底部向上弹出
//        this.setAnimationStyle(R.style.take_photo_anim);
    }

    private void initData() {
        mTvContent.setText(mContent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_quxiao) {
            mInterDioalod.onQuXiao1();
        } else if (id == R.id.tv_queren) {
            mInterDioalod.onQueRen1();
        }
    }
}
