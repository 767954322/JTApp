package com.homechart.app.timepiker.citypickerview.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.city.CityItemBean;
import com.homechart.app.home.bean.city.ProvinceBean;
import com.homechart.app.home.bean.province.MyCityBean;
import com.homechart.app.timepiker.citypickerview.widget.wheel.OnWheelChangedListener;
import com.homechart.app.timepiker.citypickerview.widget.wheel.WheelView;
import com.homechart.app.timepiker.citypickerview.widget.wheel.adapters.ArrayWheelAdapter;
import com.homechart.app.utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省市区三级选择
 * 作者：liji on 2015/12/17 10:40
 * 邮箱：lijiwork@sina.com
 */
public class AgePiker implements CanShow, OnWheelChangedListener {

    private final View view_pop_tital;
    private Context context;

    private PopupWindow popwindow;

    private View popview;

    private WheelView mViewProvince;

    private WheelView mViewCity;

    private WheelView mViewDistrict;

    private RelativeLayout mRelativeTitleBg;

    private TextView mTvOK;

    private TextView mTvTitle;

    private TextView mTvCancel;

    /**
     * 所有省
     */
    protected String[] mProvinceDatas = new String[]{"我是70后", "我是75后", "我是80后", "我是85后", "我是90后", "我是95后"};
    ;


    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;


    private OnCityItemClickListener listener;

    private String beforeAge;

    public void setAgar(String age) {
        beforeAge = age;
        boolean tag = false;
        if (TextUtils.isEmpty(beforeAge)) {
            mViewProvince.setCurrentItem(2);
        } else {
            for (int i = 0; i < mProvinceDatas.length; i++) {
                if (mProvinceDatas[i].equals(beforeAge.trim())) {
                    mViewProvince.setCurrentItem(i);
                    tag = true;
                }
            }
            if (!tag) {
                mViewProvince.setCurrentItem(2);
            }
        }
    }

    public interface OnCityItemClickListener {
        void onSelected(String... citySelected);

        void onCancel();
    }

    public void setOnCityItemClickListener(OnCityItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Default text color
     */
    public static final int DEFAULT_TEXT_COLOR = 0xFF585858;

    /**
     * Default text size
     */
    public static final int DEFAULT_TEXT_SIZE = 18;

    // Text settings
    private int textColor = DEFAULT_TEXT_COLOR;

    private int textSize = DEFAULT_TEXT_SIZE;

    /**
     * 滚轮显示的item个数
     */
    //TODO  一页显示的条数
    private static final int DEF_VISIBLE_ITEMS = 7;

    // Count of visible items
    private int visibleItems = DEF_VISIBLE_ITEMS;

    /**
     * 省滚轮是否循环滚动
     */
    private boolean isProvinceCyclic = true;

    /**
     * 市滚轮是否循环滚动
     */
    private boolean isCityCyclic = true;

    /**
     * 区滚轮是否循环滚动
     */
    private boolean isDistrictCyclic = true;

    /**
     * item间距
     */
    private int padding = 5;


    /**
     * 标题背景颜色
     */
    private String titleBackgroundColorStr = "#E9E9E9";

    /**
     * 第一次默认的显示省份，一般配合定位，使用
     */
    private String defaultProvinceName = "4";

    /**
     * 第一次默认得显示城市，一般配合定位，使用
     */
    private String defaultCityName = "朝阳区";

    /**
     * 两级联动
     */
    private boolean showProvinceAndCity = false;

    /**
     * 标题
     */
    private String mTitle = "选择地区";

    /**
     * 设置popwindow的背景
     */
    private int backgroundPop = 0xa0000000;

    private Builder mBuilder;

    private AgePiker(Builder builder) {

        this.mBuilder = builder;
        this.textColor = builder.textColor;
        this.textSize = builder.textSize;
        this.visibleItems = builder.visibleItems;
        this.isProvinceCyclic = builder.isProvinceCyclic;
        this.isDistrictCyclic = builder.isDistrictCyclic;
        this.isCityCyclic = builder.isCityCyclic;
        this.context = builder.mContext;
        this.padding = builder.padding;
        this.mTitle = builder.mTitle;
        this.titleBackgroundColorStr = builder.titleBackgroundColorStr;

        this.defaultProvinceName = builder.defaultProvinceName;

        this.showProvinceAndCity = builder.showProvinceAndCity;
        this.backgroundPop = builder.backgroundPop;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.pop_agerpicker, null);

        mViewProvince = (WheelView) popview.findViewById(R.id.id_province);
        mViewCity = (WheelView) popview.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) popview.findViewById(R.id.id_district);
        mRelativeTitleBg = (RelativeLayout) popview.findViewById(R.id.rl_title);
        mTvOK = (TextView) popview.findViewById(R.id.tv_confirm);
        mTvTitle = (TextView) popview.findViewById(R.id.tv_title);
        mTvCancel = (TextView) popview.findViewById(R.id.tv_cancel);
        view_pop_tital = (View) popview.findViewById(R.id.view_pop_tital);


        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        //半透明<Button android:background="#e0000000" />
//        透明<Button android:background="#00000000" />
        popwindow.setBackgroundDrawable(new ColorDrawable(UIUtils.getColor(R.color.touming)));
        popwindow.setAnimationStyle(R.style.AnimBottom);
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(true);
        popwindow.setFocusable(true);

        view_pop_tital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindow.dismiss();
            }
        });

        /**
         * 设置标题背景颜色
         */
        if (!TextUtils.isEmpty(this.titleBackgroundColorStr)) {
            mRelativeTitleBg.setBackgroundColor(Color.parseColor(this.titleBackgroundColorStr));
        }


        //只显示省市两级联动
        mViewDistrict.setVisibility(View.GONE);
//        initProvinceNewDatas();

        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                hide();
            }
        });
        mTvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onSelected(mCurrentProviceName);

                hide();
            }
        });

    }


    public static class Builder {
        /**
         * Default text color
         */
        public static final int DEFAULT_TEXT_COLOR = 0xFF585858;

        /**
         * Default text size
         */
        public static final int DEFAULT_TEXT_SIZE = 18;

        // Text settings
        private int textColor = DEFAULT_TEXT_COLOR;

        private int textSize = DEFAULT_TEXT_SIZE;

        /**
         * 滚轮显示的item个数
         */
        private static final int DEF_VISIBLE_ITEMS = 7;

        // Count of visible items
        private int visibleItems = DEF_VISIBLE_ITEMS;

        /**
         * 省滚轮是否循环滚动
         */
        private boolean isProvinceCyclic = false;

        /**
         * 市滚轮是否循环滚动
         */
        private boolean isCityCyclic = true;

        /**
         * 区滚轮是否循环滚动
         */
        private boolean isDistrictCyclic = true;

        private Context mContext;
        public ProvinceBean mProvinceBean;

        /**
         * item间距
         */
        private int padding = 5;


        /**
         * 标题背景颜色
         */
        private String titleBackgroundColorStr = "#E9E9E9";


        /**
         * 第一次默认的显示省份，一般配合定位，使用
         */
        private String defaultProvinceName = "4";

        /**
         * 标题
         */
        private String mTitle = "选择地区";

        /**
         * 两级联动
         */
        private boolean showProvinceAndCity = false;

        /**
         * 设置popwindow的背景
         */
        private int backgroundPop = 0xa0000000;

        public Builder(Context context, ProvinceBean provinceBean) {
            this.mContext = context;
            this.mProvinceBean = provinceBean;
        }


        /**
         * 设置标题
         *
         * @param mtitle
         * @return
         */
        public Builder title(String mtitle) {
            this.mTitle = mtitle;
            return this;
        }


        /**
         * item文字颜色
         *
         * @param textColor
         * @return
         */
        public Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        /**
         * item文字大小
         *
         * @param textSize
         * @return
         */
        public Builder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        /**
         * item间距
         *
         * @param itemPadding
         * @return
         */
        public Builder itemPadding(int itemPadding) {
            this.padding = itemPadding;
            return this;
        }

        public AgePiker build() {
            AgePiker cityPicker = new AgePiker(this);
            return cityPicker;
        }

    }

    private void setUpData() {

        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<String>(context, mProvinceDatas);
        mViewProvince.setViewAdapter(arrayWheelAdapter);
        //获取所设置的省的位置，直接定位到该位置
        //TODO  位置
        boolean tag = false;
        if (TextUtils.isEmpty(beforeAge)) {
            mViewProvince.setCurrentItem(2);
        } else {

            for (int i = 0; i < mProvinceDatas.length; i++) {
                if (mProvinceDatas[i].equals(beforeAge.trim())) {
                    mViewProvince.setCurrentItem(i);
                    tag = true;
                }
            }
            if (!tag) {
                mViewProvince.setCurrentItem(2);
            }
        }
        // 设置可见条目数量
        mViewProvince.setVisibleItems(visibleItems);
        mViewCity.setVisibleItems(visibleItems);
        mViewProvince.setCyclic(false);
        mViewCity.setCyclic(false);
        arrayWheelAdapter.setPadding(padding);
        arrayWheelAdapter.setTextColor(textColor);
        arrayWheelAdapter.setTextSize(textSize);

        updateCities();
    }


//    /**
//     * 构建省市数据
//     */
//    public void initProvinceNewDatas() {
//
//        //TODO  内容
//        mProvinceDatas = new String[]{"我是70后", "我是75后", "我是80后", "我是85后", "我是90后", "我是95后"};
//
//    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
    }

    @Override
    public void setType(int type) {
    }

    @Override
    public void show() {
        if (!isShow()) {
            setUpData();
            popwindow.showAtLocation(popview, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void hide() {
        if (isShow()) {
            popwindow.dismiss();
        }
    }

    @Override
    public boolean isShow() {
        return popwindow.isShowing();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {

            updateCities();
        }
    }
}
