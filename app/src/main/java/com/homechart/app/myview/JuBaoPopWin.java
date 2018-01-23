package com.homechart.app.myview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;

public class JuBaoPopWin
        extends PopupWindow {

    private final RelativeLayout rl_bottom;
    private final View view_top;
    private Context mContext;
    private View view;
    private ClickInter mClickInter;

    public JuBaoPopWin(Context context, ClickInter clickInter) {

        this.mContext = context;
        this.mClickInter = clickInter;
        this.view = LayoutInflater.from(context).inflate(R.layout.jubao_pop, null);

        view_top = view.findViewById(R.id.view_top);
        rl_bottom = (RelativeLayout) view.findViewById(R.id.rl_bottom);

        view_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JuBaoPopWin.this.dismiss();
            }
        });
        rl_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickInter.onJumpJuBao();
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

        void onJumpJuBao();

    }

}
