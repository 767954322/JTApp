package com.homechart.app.myview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.homechart.app.R;
import com.homechart.app.home.bean.color.ColorItemBean;

import org.ielse.widget.RangeSeekBar;

/**
 * Created by gumenghao on 17/6/19.
 */

public class ShopPriceWindow extends PopupWindow {


    private final View mMenuView;
    private final RangeSeekBar rsb_seekbar;
    private InterPrice mInterPrice;

    public ShopPriceWindow(final Context context, View.OnClickListener itemsOnClick,InterPrice interPrice) {
        super(context);
        this.mInterPrice = interPrice;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_shop_price, null);

        rsb_seekbar = (RangeSeekBar) mMenuView.findViewById(R.id.rsb_seekbar);
        rsb_seekbar.setValue(0,100);
        rsb_seekbar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max) {
                mInterPrice.changePrice(view,min,max);
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

    public interface InterPrice {
        void changePrice(RangeSeekBar view, float min, float max);
    }

    public void setSeekBarValut(){
        rsb_seekbar.setValue(0,100);
    }
}
