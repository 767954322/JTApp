package com.homechart.app.myview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.homechart.app.R;
import com.homechart.app.home.bean.searchfservice.ItemTypeNewBean;
import com.homechart.app.home.bean.searchfservice.TypeNewBean;
import com.homechart.app.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/19.
 */

public class ShopGuanJianZiWindow extends PopupWindow {

    private final GuanJianZi mGuanJianZi;
    private final ClearEditText cet_clearedit;
    private final Context mContext;
    private final Button bt_sure_guanjianzi;
    private View mMenuView;

    public ShopGuanJianZiWindow(final Context context, View.OnClickListener itemsOnClick, GuanJianZi guanJianZi) {
        super(context);
        this.mGuanJianZi = guanJianZi;
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_shop_guanjianzi, null);

        cet_clearedit = (ClearEditText) mMenuView.findViewById(R.id.cet_clearedit);
        bt_sure_guanjianzi = (Button) mMenuView.findViewById(R.id.bt_sure_guanjianzi);
        mMenuView.findViewById(R.id.view_pop_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGuanJianZi.clickGuanJianZiBottom();
            }
        });
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                mGuanJianZi.clickGuanJianZiBottom();
            }
        });
        bt_sure_guanjianzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(cet_clearedit.getText().toString().trim())){
                    ToastUtils.showCenter(mContext,"请输入关键字");
                }else {
                    mGuanJianZi.clickGuanJianZiSure(cet_clearedit.getText().toString().trim());
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
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    public interface GuanJianZi {
        void clickGuanJianZiBottom();
        void clickGuanJianZiSure(String guanjianzi);
    }

}
