package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.fensi.FenSiBean;
import com.homechart.app.home.bean.fensi.UserListBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.bean.searchshops.SearchShopItemBean;
import com.homechart.app.home.bean.searchshops.SearchShopsBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.SelectColorSeCaiWindow;
import com.homechart.app.myview.ShopPriceWindow;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.glide.GlideImgManager;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.visearch.SearchLoadingActivity;

import org.ielse.widget.RangeSeekBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gumenghao on 17/11/9.
 */

public class NewShopDetailsActivity
        extends BaseActivity
        implements View.OnClickListener,
        ShopPriceWindow.InterPrice,
        OnLoadMoreListener,
        OnRefreshListener {

    private ImageButton nav_left_imageButton;
    private ImageView iv_crop_imageview;
    private TextView tv_tital_comment;
    private String cropImage;
    private ImageView iv_xuanxiang;
    private RelativeLayout rl_add_shuaixuan;
    private RelativeLayout rl_set_shuaixuan;
    private ImageView iv_close_set;
    private TextView tv_price_set;
    private TextView tv_type_set;
    private TextView tv_guanjianzi_set;
    private RelativeLayout rl_price;
    private ImageView iv_price_icon;
    private TextView tv_price;
    private ImageView iv_price_delect;
    private RelativeLayout rl_type;
    private ImageView iv_type_icon;
    private TextView tv_type;
    private ImageView iv_type_delect;
    private RelativeLayout rl_guanjianzi;
    private ImageView iv_guanjianzi_icon;
    private TextView tv_guanjianzi;
    private ImageView iv_guanjianzi_delect;
    private int visiableNum = 0;
    private ShopPriceWindow shopPriceWindow;
    private View view_line_pop;

    private List<SearchShopItemBean> mListData = new ArrayList<>();
    private HRecyclerView mRecyclerView;
    private CommonAdapter<SearchShopItemBean> mAdapter;
    private LoadMoreFooterView mLoadMoreFooterView;
    private int position;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_shop_buy;
    }

    @Override
    protected void initView() {

        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        rl_add_shuaixuan = (RelativeLayout) findViewById(R.id.rl_add_shuaixuan);
        rl_set_shuaixuan = (RelativeLayout) findViewById(R.id.rl_set_shuaixuan);
        iv_crop_imageview = (ImageView) findViewById(R.id.iv_crop_imageview);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        iv_xuanxiang = (ImageView) findViewById(R.id.iv_xuanxiang);
        iv_close_set = (ImageView) findViewById(R.id.iv_close_set);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview);
        view_line_pop = findViewById(R.id.view_line_pop);

        tv_price_set = (TextView) findViewById(R.id.tv_price_set);
        tv_type_set = (TextView) findViewById(R.id.tv_type_set);
        tv_guanjianzi_set = (TextView) findViewById(R.id.tv_guanjianzi_set);


        rl_price = (RelativeLayout) findViewById(R.id.rl_price);
        iv_price_icon = (ImageView) findViewById(R.id.iv_price_icon);
        tv_price = (TextView) findViewById(R.id.tv_price);
        iv_price_delect = (ImageView) findViewById(R.id.iv_price_delect);

        rl_type = (RelativeLayout) findViewById(R.id.rl_type);
        iv_type_icon = (ImageView) findViewById(R.id.iv_type_icon);
        tv_type = (TextView) findViewById(R.id.tv_type);
        iv_type_delect = (ImageView) findViewById(R.id.iv_type_delect);

        rl_guanjianzi = (RelativeLayout) findViewById(R.id.rl_guanjianzi);
        iv_guanjianzi_icon = (ImageView) findViewById(R.id.iv_guanjianzi_icon);
        tv_guanjianzi = (TextView) findViewById(R.id.tv_guanjianzi);
        iv_guanjianzi_delect = (ImageView) findViewById(R.id.iv_guanjianzi_delect);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        iv_price_delect.setOnClickListener(this);
        iv_type_delect.setOnClickListener(this);
        iv_guanjianzi_delect.setOnClickListener(this);
        iv_xuanxiang.setOnClickListener(this);
        iv_close_set.setOnClickListener(this);
        tv_price_set.setOnClickListener(this);
        tv_type_set.setOnClickListener(this);
        tv_guanjianzi_set.setOnClickListener(this);
        rl_price.setOnClickListener(this);
        rl_type.setOnClickListener(this);
        rl_guanjianzi.setOnClickListener(this);
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        cropImage = getIntent().getStringExtra("image_path");
        image_url = getIntent().getStringExtra("image_url");
        mLoc = getIntent().getStringExtra("loc");
        ifMoveKuang = getIntent().getBooleanExtra("ifMoveKuang", true);
        object_sign = getIntent().getStringExtra("object_sign");
        category_id = getIntent().getStringExtra("category_id");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        ImageUtils.disRectangleImage("file://" + cropImage, iv_crop_imageview);
        tv_tital_comment.setText("相似商品");
        initRecyclerView();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                NewShopDetailsActivity.this.finish();
                break;
            case R.id.iv_xuanxiang:
                closeAllPopWin();
                rl_add_shuaixuan.setVisibility(View.GONE);
                rl_set_shuaixuan.setVisibility(View.VISIBLE);
                rl_set_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, false));
                break;
            case R.id.iv_close_set:
                rl_set_shuaixuan.setVisibility(View.GONE);
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
                break;
            case R.id.tv_price_set:
            case R.id.rl_price:
                if (v.getId() == R.id.tv_price_set) {
                    ++visiableNum;
                    tv_price.setText("未选择");
                }
                ifShowAddButton();
                ifShowPopWin(R.id.tv_price_set);
                rl_price.setVisibility(View.VISIBLE);
                tv_price_set.setVisibility(View.GONE);
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_set_shuaixuan.setVisibility(View.GONE);
                if (v.getId() == R.id.tv_price_set) {
                    rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
                }
                break;
            case R.id.tv_type_set:
            case R.id.rl_type:
                if (v.getId() == R.id.tv_type_set) {
                    ++visiableNum;
                    tv_type.setText("未选择");
                }
                ifShowAddButton();
                ifShowPopWin(R.id.tv_type_set);
                rl_type.setVisibility(View.VISIBLE);
                tv_type_set.setVisibility(View.GONE);
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_set_shuaixuan.setVisibility(View.GONE);
                if (v.getId() == R.id.tv_type_set) {
                    rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
                }
                break;
            case R.id.tv_guanjianzi_set:
            case R.id.rl_guanjianzi:
                if (v.getId() == R.id.tv_guanjianzi_set) {
                    ++visiableNum;
                    tv_guanjianzi.setText("未选择");
                }
                ifShowAddButton();
                ifShowPopWin(R.id.tv_guanjianzi_set);
                rl_guanjianzi.setVisibility(View.VISIBLE);
                tv_guanjianzi_set.setVisibility(View.GONE);
                rl_add_shuaixuan.setVisibility(View.VISIBLE);
                rl_set_shuaixuan.setVisibility(View.GONE);
                if (v.getId() == R.id.tv_guanjianzi_set) {
                    rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
                }
                break;
            case R.id.iv_price_delect:
                --visiableNum;
                ifShowAddButton();
                closeCurrentPopWin(R.id.iv_price_delect);
                rl_price.setVisibility(View.GONE);
                tv_price_set.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_type_delect:
                --visiableNum;
                ifShowAddButton();
                closeCurrentPopWin(R.id.iv_type_delect);
                rl_type.setVisibility(View.GONE);
                tv_type_set.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_guanjianzi_delect:
                --visiableNum;
                ifShowAddButton();
                closeCurrentPopWin(R.id.iv_guanjianzi_delect);
                rl_guanjianzi.setVisibility(View.GONE);
                tv_guanjianzi_set.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void initRecyclerView() {
        MultiItemTypeSupport<SearchShopItemBean> support = new MultiItemTypeSupport<SearchShopItemBean>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_pubu_new;
            }

            @Override
            public int getItemViewType(int position, SearchShopItemBean s) {
                return 0;
            }
        };

        mAdapter = new MultiItemCommonAdapter<SearchShopItemBean>(NewShopDetailsActivity.this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {

            }
        };

        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new GridLayoutManager(NewShopDetailsActivity.this, 2));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    //显示或隐藏状态
    private void ifShowPopWin(int id) {
        switch (id) {
            case R.id.tv_price_set:
            case R.id.rl_price:
                if (shopPriceWindow == null) {
                    shopPriceWindow = new ShopPriceWindow(NewShopDetailsActivity.this, this, this);
                }
                closeOtherWin(id);
                if (shopPriceWindow.isShowing()) {
                    shopPriceWindow.dismiss();
                    tabStaus(0);
                } else {
                    tabStaus(id);
                    if (Build.VERSION.SDK_INT < 24) {
                        shopPriceWindow.setSeekBarValut();
                        shopPriceWindow.showAsDropDown(view_line_pop);
                    } else {
                        shopPriceWindow.setSeekBarValut();
                        // 获取控件的位置，安卓系统>7.0
                        int[] location = new int[2];
                        view_line_pop.getLocationOnScreen(location);
                        int screenHeight = PublicUtils.getScreenHeight(NewShopDetailsActivity.this);
                        shopPriceWindow.setHeight(screenHeight - location[1]);
                        shopPriceWindow.showAtLocation(view_line_pop, Gravity.NO_GRAVITY, 0, location[1]);
                    }
                }
                break;
            case R.id.tv_type_set:
            case R.id.rl_type:
                closeOtherWin(id);
                tabStaus(id);
                break;
            case R.id.tv_guanjianzi_set:
            case R.id.rl_guanjianzi:
                closeOtherWin(id);
                tabStaus(id);
                break;
        }

    }

    //➕状态改变
    private void ifShowAddButton() {
        if (visiableNum == 3) {
            iv_xuanxiang.setVisibility(View.GONE);
        } else {
            iv_xuanxiang.setVisibility(View.VISIBLE);
        }
    }

    //显示状态改变
    private void tabStaus(int id) {

        switch (id) {
            case 0:
                rl_price.setBackgroundResource(R.drawable.search_tiaojian_unselect);
                rl_type.setBackgroundResource(R.drawable.search_tiaojian_unselect);
                rl_guanjianzi.setBackgroundResource(R.drawable.search_tiaojian_unselect);

                iv_price_icon.setImageResource(R.drawable.jiage1_1);
                iv_type_icon.setImageResource(R.drawable.pinlei1_1);
                iv_guanjianzi_icon.setImageResource(R.drawable.guanjianzi1_1);

                tv_price.setTextColor(UIUtils.getColor(R.color.bg_464646));
                tv_type.setTextColor(UIUtils.getColor(R.color.bg_464646));
                tv_guanjianzi.setTextColor(UIUtils.getColor(R.color.bg_464646));

                iv_price_delect.setImageResource(R.drawable.guanbitishi_1);
                iv_type_delect.setImageResource(R.drawable.guanbitishi_1);
                iv_guanjianzi_delect.setImageResource(R.drawable.guanbitishi_1);
                break;
            case R.id.tv_price_set:
            case R.id.rl_price:
                rl_price.setBackgroundResource(R.drawable.search_tiaojian_select);
                rl_type.setBackgroundResource(R.drawable.search_tiaojian_unselect);
                rl_guanjianzi.setBackgroundResource(R.drawable.search_tiaojian_unselect);

                iv_price_icon.setImageResource(R.drawable.jiage_1);
                iv_type_icon.setImageResource(R.drawable.pinlei1_1);
                iv_guanjianzi_icon.setImageResource(R.drawable.guanjianzi1_1);

                tv_price.setTextColor(UIUtils.getColor(R.color.white));
                tv_type.setTextColor(UIUtils.getColor(R.color.bg_464646));
                tv_guanjianzi.setTextColor(UIUtils.getColor(R.color.bg_464646));

                iv_price_delect.setImageResource(R.drawable.guanbitishi1_1);
                iv_type_delect.setImageResource(R.drawable.guanbitishi_1);
                iv_guanjianzi_delect.setImageResource(R.drawable.guanbitishi_1);
                break;
            case R.id.tv_type_set:
            case R.id.rl_type:
                rl_price.setBackgroundResource(R.drawable.search_tiaojian_unselect);
                rl_type.setBackgroundResource(R.drawable.search_tiaojian_select);
                rl_guanjianzi.setBackgroundResource(R.drawable.search_tiaojian_unselect);

                iv_price_icon.setImageResource(R.drawable.jiage1_1);
                iv_type_icon.setImageResource(R.drawable.pinlei_1);
                iv_guanjianzi_icon.setImageResource(R.drawable.guanjianzi1_1);

                tv_price.setTextColor(UIUtils.getColor(R.color.bg_464646));
                tv_type.setTextColor(UIUtils.getColor(R.color.white));
                tv_guanjianzi.setTextColor(UIUtils.getColor(R.color.bg_464646));

                iv_price_delect.setImageResource(R.drawable.guanbitishi_1);
                iv_type_delect.setImageResource(R.drawable.guanbitishi1_1);
                iv_guanjianzi_delect.setImageResource(R.drawable.guanbitishi_1);
                break;
            case R.id.tv_guanjianzi_set:
            case R.id.rl_guanjianzi:

                rl_price.setBackgroundResource(R.drawable.search_tiaojian_unselect);
                rl_type.setBackgroundResource(R.drawable.search_tiaojian_unselect);
                rl_guanjianzi.setBackgroundResource(R.drawable.search_tiaojian_select);

                iv_price_icon.setImageResource(R.drawable.jiage1_1);
                iv_type_icon.setImageResource(R.drawable.pinlei1_1);
                iv_guanjianzi_icon.setImageResource(R.drawable.guanjianzi_1);

                tv_price.setTextColor(UIUtils.getColor(R.color.bg_464646));
                tv_type.setTextColor(UIUtils.getColor(R.color.bg_464646));
                tv_guanjianzi.setTextColor(UIUtils.getColor(R.color.white));

                iv_price_delect.setImageResource(R.drawable.guanbitishi_1);
                iv_type_delect.setImageResource(R.drawable.guanbitishi_1);
                iv_guanjianzi_delect.setImageResource(R.drawable.guanbitishi1_1);
                break;
        }

    }

    //关闭其他的popwin
    private void closeOtherWin(int id) {
        switch (id) {
            case R.id.tv_price_set:
            case R.id.rl_price:
                break;
            case R.id.tv_type_set:
            case R.id.rl_type:
                if (null != shopPriceWindow && shopPriceWindow.isShowing()) {
                    shopPriceWindow.dismiss();
                }
                break;
            case R.id.tv_guanjianzi_set:
            case R.id.rl_guanjianzi:
                if (null != shopPriceWindow && shopPriceWindow.isShowing()) {
                    shopPriceWindow.dismiss();
                }
                break;
            case 0:
                if (null != shopPriceWindow && shopPriceWindow.isShowing()) {
                    shopPriceWindow.dismiss();
                }
                break;
        }
    }

    //关闭当前的的popwin
    private void closeCurrentPopWin(int id) {
        switch (id) {
            case R.id.iv_price_delect:
                if (shopPriceWindow != null && shopPriceWindow.isShowing()) {
                    shopPriceWindow.dismiss();
                }
                break;
            case R.id.iv_type_delect:
                break;
            case R.id.iv_guanjianzi_delect:
                break;
        }
    }

    //关闭所有的的popwin
    private void closeAllPopWin() {
        if (shopPriceWindow != null && shopPriceWindow.isShowing()) {
            shopPriceWindow.dismiss();
            tabStaus(0);
        }
    }

    @Override
    public void changePrice(RangeSeekBar view, float min, float max) {
        if (shopPriceWindow != null && shopPriceWindow.isShowing()) {
            tv_price.setText(min + " - " + max);
        }
    }

    private void getListData(final String status) {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(status.equals(REFRESH_STATUS)){
                    pager = 1;
                }else {
                    --pager;
                }
                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
            }

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        SearchShopsBean searchShopsBean = GsonUtil.jsonToBean(data_msg, SearchShopsBean.class);
                        if (null != searchShopsBean.getItem_list() && 0 != searchShopsBean.getItem_list().size()) {
                            updateViewFromData(searchShopsBean.getItem_list(), status);
                        } else {
                            updateViewFromData(null, status);
                        }
                    } else {
                    }
                } catch (JSONException e) {
                }
            }
        };
        if (ifMoveKuang) {//移动了，部分参数不传递
            MyHttpManager.getInstance().getNewShops(image_url, pager + "", num_shop + "", mLoc, "", "", "", "", callback);
        } else {
            MyHttpManager.getInstance().getNewShops(image_url, pager + "", num_shop + "", mLoc, category_id, "", "", object_sign, callback);
        }
    }

    private void updateViewFromData(List<SearchShopItemBean> listData, String state) {
        switch (state) {
            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData && listData.size() > 0) {
                    mListData.addAll(listData);
                } else {
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                    pager = 1;
                    mListData.clear();
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setRefreshing(false);//刷新完毕
                break;

            case LOADMORE_STATUS:
                if (null != listData && listData.size() > 0) {
                    position = mListData.size();
                    mListData.addAll(listData);
                    mAdapter.notifyItem(position, mListData, listData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                } else {
                    --pager;
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        pager = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getListData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++pager;
        getListData(LOADMORE_STATUS);
    }

    private String image_url;
    private int pager = 1;
    private int num_shop = 20;
    private String mLoc;

    private String object_sign;
    private String category_id;
    private boolean ifMoveKuang;

    private float minPrice = -1;
    private float maxPrice = -1;


    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
}
