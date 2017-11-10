package com.homechart.app.myview;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.activity.ShaiXuanResultActicity;
import com.homechart.app.home.bean.searchfservice.ItemTypeNewBean;
import com.homechart.app.home.bean.searchfservice.TypeNewBean;
import com.homechart.app.utils.ToastUtils;

import org.ielse.widget.RangeSeekBar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/19.
 */

public class ShopTypeWindow extends PopupWindow implements ShopTypeTagLayout.OnTagClickListener {


    private final ShopTypeTagLayout his_flowLayout;
    private final Button bt_sure_type;
    private final InterType mInterType;
    private final View view_pop_bottom;
    private Map<String, Integer> mapType = new HashMap<>();
    private Map<String, Integer> mapSelect = new HashMap<>();
    private View mMenuView;
    private TypeNewBean mTypeNewBean;
    public String chooseName = "";

    public ShopTypeWindow(final Context context, View.OnClickListener itemsOnClick, TypeNewBean typeNewBean, InterType interType) {
        super(context);
        this.mTypeNewBean = typeNewBean;
        this.mInterType = interType;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_shop_type, null);
        his_flowLayout = (ShopTypeTagLayout) mMenuView.findViewById(R.id.his_flowLayout);
        bt_sure_type = (Button) mMenuView.findViewById(R.id.bt_sure_type);
        view_pop_bottom = mMenuView.findViewById(R.id.view_pop_bottom);

        view_pop_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterType.closePop();
            }
        });

        bt_sure_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapSelect.size() > 0) {
                    mInterType.onClickSure(chooseName, mapSelect.get(chooseName));
                } else {
                    ToastUtils.showCenter(context, "请选择品类");
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

    public void setTypeData(TypeNewBean typeNewBean) {
        this.mTypeNewBean = typeNewBean;
        List<ItemTypeNewBean> list = typeNewBean.getCategory_list();
        mapType.clear();
        for (int i = 0; i < list.size(); i++) {
            mapType.put(list.get(i).getCategory_info().getCategory_name(), list.get(i).getCategory_info().getCategory_id());
        }
        his_flowLayout.setColorful(false);
        his_flowLayout.removeAllViews();
        his_flowLayout.setListData(list, mapSelect, this);
    }

    public void setTypes(TypeNewBean typeNewBean, String category_id, String category_name) {
        this.mTypeNewBean = typeNewBean;
        mapSelect.clear();
        mapSelect.put(category_name, Integer.parseInt(category_id));
        List<ItemTypeNewBean> list = typeNewBean.getCategory_list();
        mapType.clear();
        for (int i = 0; i < list.size(); i++) {
            mapType.put(list.get(i).getCategory_info().getCategory_name(), list.get(i).getCategory_info().getCategory_id());
        }
        his_flowLayout.setColorful(false);
        his_flowLayout.removeAllViews();
        his_flowLayout.setSelectText(null);
        his_flowLayout.setUnSelectText(null);
        his_flowLayout.setListData(list, mapSelect, this);
    }

    @Override
    public void tagClick(String text, int position, Map<String, Integer> map) {
        chooseName = text;
        mInterType.clickType(text, mapType.get(text));
        mapSelect = map;

    }

    @Override
    public void removeTagClick(String text, int position, Map<String, Integer> selectMap) {
        mInterType.clickType("", 0);
        mapSelect.clear();
    }

    public interface InterType {
        void clickType(String category_name, Integer category_id);

        void onClickSure(String category_name, Integer category_id);

        void closePop();
    }

    public Map<String, Integer> getMapType() {
        return mapType;
    }

    public void setMapType(Map<String, Integer> mapType) {
        this.mapType = mapType;
    }

    public Map<String, Integer> getMapSelect() {
        return mapSelect;
    }

    public void setMapSelect(Map<String, Integer> mapSelect) {
        this.mapSelect = mapSelect;
    }

    public String getChooseName() {
        return chooseName;
    }

    public void setChooseName(String chooseName) {
        this.chooseName = chooseName;
    }
}
