package com.homechart.app.myview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.homechart.app.R;

public class CaiJiPopWin1
        extends PopupWindow {

    private RelativeLayout rl_xiangce;
    private RelativeLayout rl_paizhao;
    private final View view_top;
    private Context mContext;
    private View view;
    private ClickInter mClickInter;

    public CaiJiPopWin1(Context context, ClickInter clickInter) {

        this.mContext = context;
        this.mClickInter = clickInter;
        this.view = LayoutInflater.from(context).inflate(R.layout.caiji_pop1, null);

        view_top = view.findViewById(R.id.view_top);
        rl_xiangce = (RelativeLayout) view.findViewById(R.id.rl_xiangce);
        rl_paizhao = (RelativeLayout) view.findViewById(R.id.rl_paizhao);

        view_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaiJiPopWin1.this.dismiss();
            }
        });
        rl_xiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CaiJiPopWin1.this.dismiss();
                mClickInter.xiangceCaiJi();
            }
        });
        rl_paizhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CaiJiPopWin1.this.dismiss();
                mClickInter.paizhaoCaiJi();
            }
        });
        // 设置外部可点击
        this.setOutsideTouchable(false);
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

        void xiangceCaiJi();
        void paizhaoCaiJi();

    }

}
