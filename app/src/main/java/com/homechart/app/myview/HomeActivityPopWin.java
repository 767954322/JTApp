package com.homechart.app.myview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.adapter.HomeTagAdapter;
import com.homechart.app.home.bean.fabu.ActivityItemDataBean;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.pictag.TagItemDataBean;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HomeActivityPopWin extends PopupWindow {

    private final View view_up;
    private final View view_below;
    private final ImageView iv_activity_image;
    private final TextView tv_activity_image_tital;
    private final TextView tv_activity_detail_content;
    private final RelativeLayout rl_data_last;
    private final TextView tv_data_last;
    private final ImageView iv_data_last_icon;
    private View view;
    private Context mContext;
    private List<TagItemDataBean> mTagList;
    private ActivityItemDataBean mActivityItemDataBean;

    public void setData(ActivityItemDataBean activityItemDataBean) {
        this.mActivityItemDataBean = activityItemDataBean;
        changeUI();
    }

    private void changeUI() {

        if (mActivityItemDataBean != null) {
            if (mActivityItemDataBean.getActivity_info().getState_id().equals("1")) {//未开始

            } else if (mActivityItemDataBean.getActivity_info().getState_id().equals("2")) {//已结束
                iv_data_last_icon.setImageResource(R.drawable.shijian1);
                tv_data_last.setText("已结束");
                tv_data_last.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
            } else if (mActivityItemDataBean.getActivity_info().getState_id().equals("3")) {//进行中
                iv_data_last_icon.setImageResource(R.drawable.shijian);
                tv_data_last.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                //计算时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                long data = PublicUtils.diffDay(mActivityItemDataBean.getActivity_info().getEnd_time(), str, "yyyy-MM-dd HH:mm:ss");
                tv_data_last.setText("还剩" + data + "天");
            }
            ImageUtils.displayFilletImage(mActivityItemDataBean.getActivity_info().getImage().getImg0(), iv_activity_image);
            tv_activity_image_tital.setText(mActivityItemDataBean.getActivity_info().getTitle());
            tv_activity_detail_content.setText("活动介绍：" + mActivityItemDataBean.getActivity_info().getDescription());
        }

    }

    public HomeActivityPopWin(Context context) {

        this.mContext = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.wk_activity_popwindow, null);

        rl_data_last = (RelativeLayout) view.findViewById(R.id.rl_data_last);
        tv_data_last = (TextView) view.findViewById(R.id.tv_data_last);
        view_up = view.findViewById(R.id.view_up);
        view_below = view.findViewById(R.id.view_below);
        iv_activity_image = (ImageView) view.findViewById(R.id.iv_activity_image);
        tv_activity_image_tital = (TextView) view.findViewById(R.id.tv_activity_image_tital);
        tv_activity_detail_content = (TextView) view.findViewById(R.id.tv_activity_detail_content);
        iv_data_last_icon = (ImageView) view.findViewById(R.id.iv_data_last_icon);


        view_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivityPopWin.this.dismiss();
            }
        });
        view_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivityPopWin.this.dismiss();
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


}
