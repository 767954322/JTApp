package com.homechart.app.myview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.adapter.MyColorAdapter;
import com.homechart.app.home.bean.color.ColorBean;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/19.
 */

public class SelectColorPopupWindow extends PopupWindow {

    private final RelativeLayout rl_pop_main;
    private final View view_pop_top;
    private final View view_pop_bottom;
    private final GridView gv_color_gridview;
    private final TextView tv_makesure_color;
    private MyColorAdapter colorAdapter;
    private List<ColorItemBean> mListData;
    private Map<Integer, ColorItemBean> mSelectListData;
    private View mMenuView;
    private SureColor mSureColor;

    public void clearSelect(){
        mSelectListData.clear();
        colorAdapter.notifyDataSetChanged();
    }

    public SelectColorPopupWindow(final Context context, View.OnClickListener itemsOnClick, ColorBean colorBean, SureColor sureColor,Map<Integer, ColorItemBean> selectListData) {
        super(context);
        if (colorBean != null) {
            mListData = colorBean.getColor_list();

            if(mSelectListData != null && mSelectListData.size() >0){
                mSelectListData = selectListData;
            }else {
                mSelectListData = new HashMap<>();
            }
        }
        this.mSureColor = sureColor;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_color, null);
        rl_pop_main = (RelativeLayout) mMenuView.findViewById(R.id.rl_pop_main);
        tv_makesure_color = (TextView) mMenuView.findViewById(R.id.tv_makesure_color);
        view_pop_top = mMenuView.findViewById(R.id.view_pop_top);
        view_pop_bottom = mMenuView.findViewById(R.id.view_pop_bottom);
        gv_color_gridview = (GridView) mMenuView.findViewById(R.id.gv_color_gridview);
        if (colorBean != null) {
            colorAdapter = new MyColorAdapter(context, mListData, mSelectListData);
            gv_color_gridview.setAdapter(colorAdapter);
        }
        gv_color_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == mListData.size()){
                    mSureColor.qingkong();
                    mSelectListData.clear();
                    colorAdapter.changeData(mListData, mSelectListData);
                }else {
                    int colorId = mListData.get(position).getColor_id();
                    if (mSelectListData.containsKey(colorId)) {
                        mSelectListData.remove(colorId);
                    } else {

                        if (mSelectListData.size() < 3) {
                            mSelectListData.put(colorId, mListData.get(position));
                        } else {
                            ToastUtils.showCenter(context, "最多选择三种色彩搭配哦～");
                        }
                    }

                    if (colorAdapter != null) {
                        colorAdapter.changeData(mListData, mSelectListData);
                    }
                }
            }
        });

        //设置按钮监听
        rl_pop_main.setOnClickListener(itemsOnClick);
        view_pop_top.setOnClickListener(itemsOnClick);
        view_pop_bottom.setOnClickListener(itemsOnClick);
        tv_makesure_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectListData.size() != 0) {
                    mSureColor.clickSureColor(mSelectListData);
                    SelectColorPopupWindow.this.dismiss();
                } else {
//                    ToastUtils.showCenter(context, "请先选择颜色再点击确认");
                    mSureColor.clickSureColor(null);
                }

            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }

    public interface SureColor {
        void clickSureColor(Map<Integer, ColorItemBean> selectListData);
        void qingkong();
    }
}
