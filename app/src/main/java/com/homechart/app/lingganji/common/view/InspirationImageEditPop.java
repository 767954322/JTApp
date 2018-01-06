package com.homechart.app.lingganji.common.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.contract.InterDioalod;
import com.homechart.app.lingganji.contract.InterPopBottom;

/**
 * Created by gumenghao on 18/1/4.
 */

public class InspirationImageEditPop extends PopupWindow implements View.OnClickListener {


    private InterPopBottom mInterDioalod;
    private View view;
    private String mContent;
    private Context mContext;
    private RelativeLayout mTvQuXiao;
    private TextView tv_lingganji;
    private TextView tv_guanli;
    private TextView tv_shared;
    private TextView tv_delete;

    public InspirationImageEditPop(Context context, String content, InterPopBottom interDioalod) {
        this.mContext = context;
        this.mContent = content;
        this.mInterDioalod = interDioalod;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_inspiration_editpic, null);
        initView();
        initListener();
        initPop();
        initData();
    }

    private void initView() {

        mTvQuXiao = (RelativeLayout) view.findViewById(R.id.rl_quxiao);
        tv_lingganji = (TextView) view.findViewById(R.id.tv_lingganji);
        tv_guanli = (TextView) view.findViewById(R.id.tv_guanli);
        tv_shared = (TextView) view.findViewById(R.id.tv_shared);
        tv_delete = (TextView) view.findViewById(R.id.tv_delete);

    }

    private void initListener() {
        mTvQuXiao.setOnClickListener(this);
        tv_lingganji.setOnClickListener(this);
        tv_guanli.setOnClickListener(this);
        tv_shared.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_quxiao:
                mInterDioalod.onClose();
                break;
            case R.id.tv_lingganji:
                mInterDioalod.onBianJi();
                break;
            case R.id.tv_guanli:
                mInterDioalod.onGuanLi();
                break;
            case R.id.tv_shared:
                break;
            case R.id.tv_delete:
                break;
        }

    }
}
