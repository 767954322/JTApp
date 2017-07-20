package com.homechart.app.myview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.fabu.ActivityItemDataBean;
import com.homechart.app.home.bean.pictag.TagItemDataBean;
import com.homechart.app.utils.imageloader.ImageUtils;

import java.util.List;

public class HomeSharedPopWin extends PopupWindow {

    private final View view_top;
    private final TextView tv_shared_weixin_friends;
    private final TextView tv_shared_weixin_quan;
    private final TextView tv_shared_xinlang;
    private final RelativeLayout rl_cancle;
    private Context mContext;
    private View view;
    private ClickInter mClickInter;

    public HomeSharedPopWin(Context context,ClickInter clickInter) {

        this.mContext = context;
        this.mClickInter = clickInter;
        this.view = LayoutInflater.from(context).inflate(R.layout.wk_shared_popwindow, null);

        view_top = view.findViewById(R.id.view_top);
        tv_shared_weixin_friends = (TextView) view.findViewById(R.id.tv_shared_weixin_friends);
        tv_shared_weixin_quan = (TextView) view.findViewById(R.id.tv_shared_weixin_quan);
        tv_shared_xinlang = (TextView) view.findViewById(R.id.tv_shared_xinlang);
        rl_cancle = (RelativeLayout) view.findViewById(R.id.rl_cancle);

        view_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeSharedPopWin.this.dismiss();
            }
        });
        tv_shared_weixin_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeSharedPopWin.this.dismiss();
                mClickInter.onClickWeiXin();
            }
        });
        tv_shared_weixin_quan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeSharedPopWin.this.dismiss();
                mClickInter.onClickPYQ();
            }
        });
        tv_shared_xinlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeSharedPopWin.this.dismiss();
                mClickInter.onClickWeiBo();
            }
        });
        rl_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeSharedPopWin.this.dismiss();

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

    }

}
