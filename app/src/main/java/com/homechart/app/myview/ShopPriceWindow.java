package com.homechart.app.myview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.bean.color.ColorItemBean;

import org.ielse.widget.RangeSeekBar;

/**
 * Created by gumenghao on 17/6/19.
 */

public class ShopPriceWindow extends PopupWindow {


    private View mMenuView;
    public RangeSeekBar rsb_seekbar;
    private View view_pop_bottom;
    private Button bt_sure_price;
    private TextView tv_price_right;
    private TextView tv_price_left;
    private float mMinP = -1;
    private float mMaxP = -1;
    private InterPrice mInterPrice;

    public String chooseMin = "";
    public String chooseMax = "";

    public ShopPriceWindow(final Context context, View.OnClickListener itemsOnClick, InterPrice interPrice) {
        super(context);
        this.mInterPrice = interPrice;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_shop_price, null);

        rsb_seekbar = (RangeSeekBar) mMenuView.findViewById(R.id.rsb_seekbar);
        view_pop_bottom = mMenuView.findViewById(R.id.view_pop_bottom);
        bt_sure_price = (Button) mMenuView.findViewById(R.id.bt_sure_price);
        tv_price_right = (TextView) mMenuView.findViewById(R.id.tv_price_right);
        tv_price_left = (TextView) mMenuView.findViewById(R.id.tv_price_left);
        rsb_seekbar.setValue(0, 100);

        if (mMinP != -1 && mMaxP != -1) {
            tv_price_left.setText("¥ " + mMinP);
            tv_price_right.setText("¥ " + mMaxP);
        }
        rsb_seekbar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max) {

                if (mMinP != -1 && mMaxP != -1) {
                    float price = mMaxP - mMinP;
                    if (min < 1) {
                        chooseMin = mMinP + "";
                        tv_price_left.setText("¥ " + PublicUtils.formatPrice(mMinP));
                    } else {
                        chooseMin = PublicUtils.formatPrice(min / 100 * price);
                        tv_price_left.setText("¥ " + PublicUtils.formatPrice(min / 100 * price));
                    }
                    if (max < 1) {
                        chooseMax = mMinP + "";
                        tv_price_right.setText("¥ " + PublicUtils.formatPrice(mMinP));
                    }else {
                        chooseMax = PublicUtils.formatPrice(max / 100 * price);
                        tv_price_right.setText("¥ " + PublicUtils.formatPrice(max / 100 * price));
                    }
                    mInterPrice.changePrice(view, min, max);
                }
            }
        });
        view_pop_bottom.setOnClickListener(itemsOnClick);
        bt_sure_price.setOnClickListener(itemsOnClick);
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

    public void setSeekBarValut() {
        rsb_seekbar.setValue(0, 100);
    }

    public void setPriceData(float min, float max) {
        this.mMinP = min;
        this.mMaxP = max;
        this.chooseMin = PublicUtils.formatPrice(mMinP) + "";
        this.chooseMax = PublicUtils.formatPrice(mMaxP) + "";

        if (mMinP != -1 && mMaxP != -1) {
            tv_price_left.setText("¥ " + PublicUtils.formatPrice(mMinP));
            tv_price_right.setText("¥ " + PublicUtils.formatPrice(mMaxP));
        }
    }

    public RangeSeekBar getRsb_seekbar() {
        return rsb_seekbar;
    }

    public void setRsb_seekbar(RangeSeekBar rsb_seekbar) {
        this.rsb_seekbar = rsb_seekbar;
    }
}
