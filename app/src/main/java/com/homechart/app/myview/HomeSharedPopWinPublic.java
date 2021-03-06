package com.homechart.app.myview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;

public class HomeSharedPopWinPublic
        extends PopupWindow{

    private final View view_top;
    private final TextView tv_shared_weixin_friends;
    private final TextView tv_shared_weixin_quan;
    private final TextView tv_shared_xinlang;
    private final RelativeLayout rl_cancle;
    private final TextView tv_shared_qq_friends;
    private Context mContext;
    private View view;
    private ClickInter mClickInter;

    public HomeSharedPopWinPublic(Context context, ClickInter clickInter) {

        this.mContext = context;
        this.mClickInter = clickInter;
        this.view = LayoutInflater.from(context).inflate(R.layout.wk_shared_popwindow_public, null);

        view_top = view.findViewById(R.id.view_top);
        tv_shared_weixin_friends = (TextView) view.findViewById(R.id.tv_shared_weixin_friends);
        tv_shared_weixin_quan = (TextView) view.findViewById(R.id.tv_shared_weixin_quan);
        tv_shared_xinlang = (TextView) view.findViewById(R.id.tv_shared_xinlang);
        tv_shared_qq_friends = (TextView) view.findViewById(R.id.tv_shared_qq_friends);
        rl_cancle = (RelativeLayout) view.findViewById(R.id.rl_cancle);

        view_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeSharedPopWinPublic.this.dismiss();
            }
        });
        tv_shared_weixin_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeSharedPopWinPublic.this.dismiss();
                mClickInter.onClickWeiXin();
            }
        });
        tv_shared_weixin_quan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeSharedPopWinPublic.this.dismiss();
                mClickInter.onClickPYQ();
            }
        });
        tv_shared_xinlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeSharedPopWinPublic.this.dismiss();
                mClickInter.onClickWeiBo();
            }
        });
        tv_shared_qq_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeSharedPopWinPublic.this.dismiss();
                mClickInter.onClickQQ();
            }
        });
        rl_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeSharedPopWinPublic.this.dismiss();

            }
        });
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setOutsideTouchable(false);
        // 设置弹出窗体可点击
        this.setFocusable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    public interface ClickInter {

        void onClickWeiXin();

        void onClickPYQ();

        void onClickWeiBo();

        void onClickQQ();

    }

}
