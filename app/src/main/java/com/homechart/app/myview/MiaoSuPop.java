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
import com.homechart.app.commont.contract.InterDioalod;
import com.homechart.app.lingganji.common.entity.inspirationdetail.InspirationDetailBean;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;

/**
 * Created by gumenghao on 18/1/4.
 */

public class MiaoSuPop extends PopupWindow implements View.OnClickListener {


    private View view;
    private InspirationDetailBean mInspirationDetailBean;
    private Context mContext;
    private ScrollView sv_content;
    private int width;
    private ImageView iv_user_header;
    private TextView tv_user_name;
    private TextView tv_inspiration_miaosu;
    private ImageView iv_close;

    public MiaoSuPop(Context context, InspirationDetailBean inspirationDetailBean) {
        this.mContext = context;
        this.mInspirationDetailBean = inspirationDetailBean;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_miaosu, null);
        initView();
        initListener();
        initPop();
        initData();
    }

    private void initView() {

        iv_user_header = (ImageView) view.findViewById(R.id.iv_user_header);
        tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
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

        tv_user_name.setText(mInspirationDetailBean.getInfo().getUser_info().getNickname());
        tv_inspiration_miaosu.setText(mInspirationDetailBean.getInfo().getAlbum_info().getDescription());
        ImageUtils.displayRoundImage(mInspirationDetailBean.getInfo().getUser_info().getAvatar().getThumb(), iv_user_header);
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
