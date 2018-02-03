package com.homechart.app.myview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.adapter.MyColorGridAdapter;
import com.homechart.app.home.bean.imagedetail.ColorInfoBean;
import com.homechart.app.home.bean.imagedetail.ImageDetailBean;
import com.homechart.app.utils.UIUtils;

import java.util.List;

public class ColorPopWin
        extends PopupWindow {

    private Context mContext;
    private View view;
    private RelativeLayout rl_color;
    private View iv_close_color;
    private MyListView dgv_colorlist;
    private LinearLayout ll_color_lines;
    private View color_bottom;
    private ImageDetailBean imageDetailBean;

    public ColorPopWin(Context context,ImageDetailBean imageDetailBean) {

        this.imageDetailBean = imageDetailBean;
        this.mContext = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.color_pop, null);


        rl_color = (RelativeLayout) view.findViewById(R.id.rl_color);
        iv_close_color = (ImageView) view.findViewById(R.id.iv_close_color);
        dgv_colorlist = (MyListView) view.findViewById(R.id.dgv_colorlist);
        ll_color_lines = (LinearLayout) view.findViewById(R.id.ll_color_lines);
        color_bottom = view.findViewById(R.id.color_bottom);
        iv_close_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        initData();

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

    private void initData() {
        List<ColorInfoBean> listColor = imageDetailBean.getColor_info();
        if (listColor != null && listColor.size() > 0) {
            int width = PublicUtils.getScreenWidth(mContext) - UIUtils.getDimens(R.dimen.font_40);
            float float_talte = 0;
            for (int i = 0; i < listColor.size(); i++) {
                float wid = Float.parseFloat(listColor.get(i).getColor_percent().trim());
                float_talte = float_talte + wid;
            }

            for (int i = 0; i < listColor.size(); i++) {
                TextView textView = new TextView(mContext);
                float wid = Float.parseFloat(listColor.get(i).getColor_percent().trim());
                float per = wid / float_talte;

                if (i == listColor.size() - 1) {
                    textView.setWidth(width);
                } else {
                    textView.setWidth((int) (width * per));
                }
                textView.setHeight(UIUtils.getDimens(R.dimen.font_30));
                textView.setBackgroundColor(Color.parseColor("#" + listColor.get(i).getColor_value()));
                ll_color_lines.addView(textView);
            }
            dgv_colorlist.setAdapter(new MyColorGridAdapter(listColor, mContext));
        }
    }

}
