package com.homechart.app.myview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;

/**
 * Created by gumenghao on 17/6/19.
 */

public class UpApkPopupWindow extends PopupWindow {

    private final TextView tv_go_up;
    private final ImageView iv_close_up;
    private final ProgressBar pb_go_up;
    private View mMenuView;

    public UpApkPopupWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_upapk, null);
        tv_go_up = (TextView) mMenuView.findViewById(R.id.tv_go_up);
        iv_close_up = (ImageView) mMenuView.findViewById(R.id.iv_close_up);
        pb_go_up = (ProgressBar) mMenuView.findViewById(R.id.pb_go_up);

        //设置按钮监听
        tv_go_up.setOnClickListener(itemsOnClick);
        iv_close_up.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xd0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }

    public void changUi(int progress){
        tv_go_up.setVisibility(View.GONE);
        pb_go_up.setVisibility(View.VISIBLE);
        pb_go_up.setProgress(progress);
    }
}
