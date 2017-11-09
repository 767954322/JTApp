package com.homechart.app.myview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;

/**
 * Created by gumenghao on 17/6/19.
 */

public class BigImagePopWin extends PopupWindow {

    private View mMenuView;

    public BigImagePopWin(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_big_image, null);
//        rl_pop_main = (RelativeLayout) mMenuView.findViewById(R.id.rl_pop_main);
//        iv_takephoto = (TextView) mMenuView.findViewById(R.id.tv_takephoto);
//        iv_pick_photo = (TextView) mMenuView.findViewById(R.id.tv_pic);
//        iv_bufabu = (ImageView) mMenuView.findViewById(R.id.iv_bufabu);


        //设置按钮监听
//        iv_takephoto.setOnClickListener(itemsOnClick);
//        iv_pick_photo.setOnClickListener(itemsOnClick);
//        rl_pop_main.setOnClickListener(itemsOnClick);
//        iv_bufabu.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xd0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }
}
