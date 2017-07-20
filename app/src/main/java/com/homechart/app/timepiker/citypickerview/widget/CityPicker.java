package com.homechart.app.timepiker.citypickerview.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
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
import com.homechart.app.timepiker.citypickerview.model.CityModel;
import com.homechart.app.timepiker.citypickerview.model.DistrictModel;
import com.homechart.app.timepiker.citypickerview.model.ProvinceModel;
import com.homechart.app.timepiker.citypickerview.utils.XmlParserHandler;
import com.homechart.app.timepiker.citypickerview.widget.wheel.OnWheelChangedListener;
import com.homechart.app.timepiker.citypickerview.widget.wheel.WheelView;
import com.homechart.app.timepiker.citypickerview.widget.wheel.adapters.ArrayWheelAdapter;
import com.homechart.app.utils.UIUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 省市区三级选择
 * 作者：liji on 2015/12/17 10:40
 * 邮箱：lijiwork@sina.com
 */
public class CityPicker implements CanShow, OnWheelChangedListener {

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
    protected String[] mProvinceDatas;

    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    protected Map<String, List<MyCityBean>> mCitisID = new HashMap<String, List<MyCityBean>>();

    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;

    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;

    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";

    private OnCityItemClickListener listener;

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
    private static final int DEF_VISIBLE_ITEMS = 5;

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
     * Color.BLACK
     */
    private String cancelTextColorStr = "#000000";


    /**
     * Color.BLUE
     */
    private String confirmTextColorStr = "#0000FF";

    /**
     * 标题背景颜色
     */
    private String titleBackgroundColorStr = "#E9E9E9";
    /**
     * 标题颜色
     */
    private String titleTextColorStr = "#E9E9E9";

    /**
     * 第一次默认的显示省份，一般配合定位，使用
     */
    public String defaultProvinceName = "北京市";

    /**
     * 第一次默认得显示城市，一般配合定位，使用
     */
    public String defaultCityName = "朝阳区";

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

    private CityPicker(Builder builder) {

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
        this.confirmTextColorStr = builder.confirmTextColorStr;
        this.cancelTextColorStr = builder.cancelTextColorStr;

        this.defaultCityName = builder.defaultCityName;
        this.defaultProvinceName = builder.defaultProvinceName;

        this.showProvinceAndCity = builder.showProvinceAndCity;
        this.backgroundPop = builder.backgroundPop;
        this.titleTextColorStr = builder.titleTextColorStr;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.pop_citypicker, null);

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
        //初始化城市数据
//        initProvinceDatas(context);
        initProvinceNewDatas();

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

                if (mCurrentCityName.equals("和平区")) {
                    mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[0];
                }
                List<MyCityBean> list = mCitisID.get(mCurrentProviceName);
                MyCityBean myCityBean = null;
                for (int i = 0; i < list.size(); i++) {
                    String cityname = list.get(i).getCityName().trim();
                    if (cityname.equals(mCurrentCityName.trim())) {
                        myCityBean = list.get(i);
                    }
                }

                listener.onSelected(mCurrentProviceName, mCurrentCityName, myCityBean.getProvinceid(), myCityBean.getCityid());

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
        private static final int DEF_VISIBLE_ITEMS = 5;

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
         * Color.BLACK
         */
        private String cancelTextColorStr = "#000000";


        /**
         * Color.BLUE
         */
        private String confirmTextColorStr = "#0000FF";

        /**
         * 标题背景颜色
         */
        private String titleBackgroundColorStr = "#E9E9E9";

        /**
         * 标题颜色
         */
        private String titleTextColorStr = "#E9E9E9";


        /**
         * 第一次默认的显示省份，一般配合定位，使用
         */
        private String defaultProvinceName = "北京市";

        /**
         * 第一次默认得显示城市，一般配合定位，使用
         */
        private String defaultCityName = "朝阳区";

        /**
         * 第一次默认得显示，一般配合定位，使用
         */
        private String defaultDistrict = "新北区";

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
         * 设置popwindow的背景
         *
         * @param backgroundPopColor
         * @return
         */
        public Builder backgroundPop(int backgroundPopColor) {
            this.backgroundPop = backgroundPopColor;
            return this;
        }

        /**
         * 设置标题背景颜色
         *
         * @param colorBg
         * @return
         */
        public Builder titleBackgroundColor(String colorBg) {
            this.titleBackgroundColorStr = colorBg;
            return this;
        }

        /**
         * 设置标题背景颜色
         *
         * @param titleTextColorStr
         * @return
         */
        public Builder titleTextColor(String titleTextColorStr) {
            this.titleTextColorStr = titleTextColorStr;
            return this;
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
         * 是否只显示省市两级联动
         *
         * @param flag
         * @return
         */
        public Builder onlyShowProvinceAndCity(boolean flag) {
            this.showProvinceAndCity = flag;
            return this;
        }

        /**
         * 第一次默认的显示省份，一般配合定位，使用
         *
         * @param defaultProvinceName
         * @return
         */
        public Builder province(String defaultProvinceName) {
            this.defaultProvinceName = defaultProvinceName;
            return this;
        }

        /**
         * 第一次默认得显示城市，一般配合定位，使用
         *
         * @param defaultCityName
         * @return
         */
        public Builder city(String defaultCityName) {
            this.defaultCityName = defaultCityName;
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
         * 滚轮显示的item个数
         *
         * @param visibleItems
         * @return
         */
        public Builder visibleItemsCount(int visibleItems) {
            this.visibleItems = visibleItems;
            return this;
        }

        /**
         * 省滚轮是否循环滚动
         *
         * @param isProvinceCyclic
         * @return
         */
        public Builder provinceCyclic(boolean isProvinceCyclic) {
            this.isProvinceCyclic = isProvinceCyclic;
            return this;
        }

        /**
         * 市滚轮是否循环滚动
         *
         * @param isCityCyclic
         * @return
         */
        public Builder cityCyclic(boolean isCityCyclic) {
            this.isCityCyclic = isCityCyclic;
            return this;
        }

        /**
         * 区滚轮是否循环滚动
         *
         * @param isDistrictCyclic
         * @return
         */
        public Builder districtCyclic(boolean isDistrictCyclic) {
            this.isDistrictCyclic = isDistrictCyclic;
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

        public CityPicker build() {
            CityPicker cityPicker = new CityPicker(this);
            return cityPicker;
        }

    }

    private void setUpData() {
        int provinceDefault = -1;
        if (!TextUtils.isEmpty(defaultProvinceName) && mProvinceDatas.length > 0) {
            for (int i = 0; i < mProvinceDatas.length; i++) {
                if (mProvinceDatas[i].contains(defaultProvinceName)) {
                    provinceDefault = i;
                    break;
                }
            }
        }
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<String>(context, mProvinceDatas);
        mViewProvince.setViewAdapter(arrayWheelAdapter);
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != provinceDefault) {
            mViewProvince.setCurrentItem(provinceDefault);
        }
        // 设置可见条目数量
        mViewProvince.setVisibleItems(visibleItems);
        mViewCity.setVisibleItems(visibleItems);
//        mViewDistrict.setVisibleItems(visibleItems);
        mViewProvince.setCyclic(false);
        mViewCity.setCyclic(false);
//        mViewDistrict.setCyclic(isDistrictCyclic);
        arrayWheelAdapter.setPadding(padding);
        arrayWheelAdapter.setTextColor(textColor);
        arrayWheelAdapter.setTextSize(textSize);

        updateCities();
        updateAreas();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];

    }

    /**
     * 构建省市数据
     */
    public void initProvinceNewDatas() {
//        mCitisDatasMap ＝
        mProvinceDatas = new String[mBuilder.mProvinceBean.getData().size()];

        for (int i = 0; i < mBuilder.mProvinceBean.getData().size(); i++) {
            String province_name = mBuilder.mProvinceBean.getData().get(i).getProvince_name();
            mProvinceDatas[i] = province_name;
            List<MyCityBean> list = new ArrayList<>();
            String[] citys = new String[mBuilder.mProvinceBean.getData().get(i).getCitys().size()];
            for (int j = 0; j < mBuilder.mProvinceBean.getData().get(i).getCitys().size(); j++) {
                CityItemBean cityItemBean = mBuilder.mProvinceBean.getData().get(i).getCitys().get(j);
                citys[j] = cityItemBean.getCity_name();
                list.add(new MyCityBean(cityItemBean.getCity_name(), cityItemBean.getCity_id(), cityItemBean.getFather_id()));
            }
            mCitisID.put(province_name, list);
            mCitisDatasMap.put(province_name, citys);
        }


    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }

        int cityDefault = -1;
        if (!TextUtils.isEmpty(defaultCityName) && cities.length > 0) {
            for (int i = 0; i < cities.length; i++) {
                try {
                    if (cities[i].contains(defaultCityName)) {
                        cityDefault = i;
                        break;
                    }
                } catch (Exception e) {

                }
            }
        }
        ArrayWheelAdapter cityWheel = new ArrayWheelAdapter<String>(context, cities);
        // 设置可见条目数量
        cityWheel.setTextColor(textColor);
        cityWheel.setTextSize(textSize);
        mViewCity.setViewAdapter(cityWheel);
        if (-1 != cityDefault) {
            mViewCity.setCurrentItem(cityDefault);
        } else {
            mViewCity.setCurrentItem(0);
        }

        cityWheel.setPadding(padding);
//        updateAreas();
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
        } else if (wheel == mViewCity) {
            updateAreas();
        }
    }
}
