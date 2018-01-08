package com.homechart.app.myview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.lingganji.common.entity.inspirationdetail.InspirationDetailBean;
import com.homechart.app.lingganji.common.entity.inspirationpics.InsPicItemBean;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;

/**
 * Created by gumenghao on 18/1/4.
 */

public class InspritionPop extends PopupWindow implements View.OnClickListener {


    private View view;
    private InsPicItemBean mInsPicItemBean;
    private Context mContext;
    private ScrollView sv_content;
    private int width;
    private ImageView iv_pic;
    private TextView tv_inspiration_miaosu;
    private ImageView iv_close;

    public InspritionPop(Context context, InsPicItemBean insPicItemBean) {
        this.mContext = context;
        this.mInsPicItemBean = insPicItemBean;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_pic_miaosu, null);
        initView();
        initListener();
        initPop();
        initData();
    }

    private void initView() {

        iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
        tv_inspiration_miaosu = (TextView) view.findViewById(R.id.tv_inspiration_miaosu);
        iv_close = (ImageView) view.findViewById(R.id.iv_close);
        sv_content = (ScrollView) view.findViewById(R.id.sv_content);

    }

    private void initListener() {
        iv_close.setOnClickListener(this);
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

        width = PublicUtils.getScreenWidth(mContext) - UIUtils.getDimens(R.dimen.font_30);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) sv_content.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = (int) (PublicUtils.getScreenHeight(mContext) * 0.6);
        sv_content.setLayoutParams(layoutParams);
        tv_inspiration_miaosu.setText(mInsPicItemBean.getItem_info().getDescription());
        ImageUtils.displayFilletImage(mInsPicItemBean.getItem_info().getImage().getImg0(), iv_pic);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_close:
                this.dismiss();
        }
    }
}
