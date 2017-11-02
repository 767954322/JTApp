package com.homechart.app.myview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.homechart.app.R;
import com.homechart.app.home.adapter.HomeTagAdapter;
import com.homechart.app.home.bean.color.ColorBean;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.pictag.TagItemDataBean;

import java.util.List;
import java.util.Map;

public class NewHomeTabPopWin extends PopupWindow implements View.OnClickListener {

    private final MyViewPager vp_home_tag;
    private final HomeTagAdapter pageAdapter;
    private final RoundImageView iv_kongjian;
    private final RoundImageView iv_jubu;
    private final RoundImageView iv_zhuangshi;
    private final RoundImageView iv_shouna;
    private final RelativeLayout rl_kongjian;
    private final RelativeLayout rl_jubu;
    private final RelativeLayout rl_zhuangshi;
    private final RelativeLayout rl_shouna;
    private View view;
    private Context mContext;
    private Map<Integer, ColorItemBean> mSelectListData;
    private ColorBean mColorBean;
    private List<TagItemDataBean> mTagList;

    public NewHomeTabPopWin(Context context,
                            ViewPager.OnPageChangeListener onPageChangeListener,
                            TagDataBean tagDataBean,
                            HomeTagAdapter.PopupWindowCallBack popupWindowCallBack,
                            ColorBean colorBean,
                            Map<Integer, ColorItemBean> selectListData) {

        this.mContext = context;
        this.mSelectListData = selectListData;
        this.mColorBean = colorBean;
        mTagList = tagDataBean.getTag_id();
        this.view = LayoutInflater.from(context).inflate(R.layout.wk_flow_popwindow, null);

        //找对象
        vp_home_tag = (MyViewPager) this.view.findViewById(R.id.vp_home_tag);

        iv_kongjian = (RoundImageView) this.view.findViewById(R.id.iv_kongjian);
        iv_jubu = (RoundImageView) this.view.findViewById(R.id.iv_jubu);
        iv_zhuangshi = (RoundImageView) this.view.findViewById(R.id.iv_zhuangshi);
        iv_shouna = (RoundImageView) this.view.findViewById(R.id.iv_shouna);
        rl_kongjian = (RelativeLayout) this.view.findViewById(R.id.rl_kongjian);
        rl_jubu = (RelativeLayout) this.view.findViewById(R.id.rl_jubu);
        rl_zhuangshi = (RelativeLayout) this.view.findViewById(R.id.rl_zhuangshi);
        rl_shouna = (RelativeLayout) this.view.findViewById(R.id.rl_shouna);

        rl_kongjian.setOnClickListener(this);
        rl_jubu.setOnClickListener(this);
        rl_zhuangshi.setOnClickListener(this);
        rl_shouna.setOnClickListener(this);

        iv_kongjian.setImageResource(R.drawable.kongjian);
        vp_home_tag.setOffscreenPageLimit(5);
        vp_home_tag.setScanScroll(true);
        // 设置按钮监听
        vp_home_tag.addOnPageChangeListener(onPageChangeListener);

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
        vp_home_tag.setPageTransformer(true, new DepthPageTransformer());
        pageAdapter = new HomeTagAdapter(mContext, mTagList, popupWindowCallBack, mColorBean, mSelectListData);
        vp_home_tag.setAdapter(pageAdapter);
    }

    public void setPagePosition(int position) {
        vp_home_tag.setCurrentItem(position);
    }

    public void changeColor(Map<Integer, ColorItemBean> selectListData) {
        this.mSelectListData = selectListData;
        pageAdapter.changeData(selectListData);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_kongjian:
                iv_kongjian.setImageResource(R.drawable.kongjian);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                vp_home_tag.setCurrentItem(0);
                break;
            case R.id.rl_jubu:

                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                vp_home_tag.setCurrentItem(1);
                break;
            case R.id.rl_zhuangshi:

                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi);
                iv_shouna.setImageResource(R.drawable.shouna1);
                vp_home_tag.setCurrentItem(2);
                break;
            case R.id.rl_shouna:

                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna);
                vp_home_tag.setCurrentItem(3);
                break;
        }

    }
}
