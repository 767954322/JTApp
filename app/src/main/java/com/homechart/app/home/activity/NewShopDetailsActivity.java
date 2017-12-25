package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.croplayout.EditableImage;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.searchfservice.TypeNewBean;
import com.homechart.app.home.bean.searchshops.SearchFacetsBean;
import com.homechart.app.home.bean.searchshops.SearchShopItemBean;
import com.homechart.app.home.bean.searchshops.SearchShopsBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.imagedetail.PhotoView;
import com.homechart.app.myview.ShopGuanJianZiWindow;
import com.homechart.app.myview.ShopPriceWindow;
import com.homechart.app.myview.ShopTypeWindow;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.HtmlService;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.visearch.NewSearchResultActivity;
import com.umeng.analytics.MobclickAgent;

import org.ielse.widget.RangeSeekBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gumenghao on 17/11/9.
 */

public class NewShopDetailsActivity
        extends BaseActivity
        implements View.OnClickListener,
        ShopPriceWindow.InterPrice,
        ShopTypeWindow.InterType,
        OnLoadMoreListener,
        OnRefreshListener, ShopGuanJianZiWindow.GuanJianZi {

    private ImageButton nav_left_imageButton;
    private ImageView iv_crop_imageview;
    private TextView tv_tital_comment;
    private String cropImage = "";
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
    private ShopPriceWindow shopPriceWindow;
    private View view_line_pop;

    private List<SearchShopItemBean> mListData = new ArrayList<>();
    private ShopGuanJianZiWindow shopGuanJianZiWindow;
    private CommonAdapter<SearchShopItemBean> mAdapter;
    private LoadMoreFooterView mLoadMoreFooterView;
    public SearchFacetsBean searchFacetsBean;
    private ShopTypeWindow shopTypeWindow;
    private HRecyclerView mRecyclerView;
    private RelativeLayout rl_image_big;
    private PhotoView pv_big_imageview;
    private TypeNewBean typeNewBean;
    private ImageView iv_delete;

    private String photoPath = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES).getPath() + File.separator + "JiaTuApp";
    private String cropSmallImage;
    private String cropName;
    private String network;
    private int x1;
    private int x2;
    private int y1;
    private int y2;

    private boolean ifClickShouCang = true;
    private int openPosition = -1;
    private int position;
    private int wide;
    private int ifneedcrop;


    private RelativeLayout rl_pop_jubao;
    private TextView tv_jubao_one;
    private TextView tv_jubao_two;
    private TextView tv_jubao_three;

    private int current_jubao = 1;
    private int current_position = -1;
    private TextView tv_jubao_close;
    private TextView tv_jubao_sure;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            cropSmallImage = photoPath + "/" + cropName;
            ImageUtils.disRectangleImage("file://" + cropSmallImage, iv_crop_imageview);
        }
    };
    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String htmlContent = bundle.getString("htmlContent");
            String spu_id = bundle.getString("spu_id");
            if (htmlContent.equals("此商品暂时售完") ||
                    htmlContent.contains("该商品已下柜") ||
                    htmlContent.contains("您查看的宝贝不存在") ||
                    htmlContent.contains("此商品已下架") ||
                    htmlContent.contains("此宝贝已下架") ||
                    htmlContent.contains("您查看的商品找不到了")) {
                //下架商品
                OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }

                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                            String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                            String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                            if (error_code == 0) {
                            } else {
                            }
                        } catch (JSONException e) {
                        }
                    }
                };
                MyHttpManager.getInstance().removeShop(spu_id, callBack);

            }
        }
    };

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
        pv_big_imageview = (PhotoView) findViewById(R.id.pv_big_imageview);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        rl_image_big = (RelativeLayout) findViewById(R.id.rl_image_big);

        rl_pop_jubao = (RelativeLayout) findViewById(R.id.rl_pop_jubao);
        tv_jubao_one = (TextView) findViewById(R.id.tv_jubao_one);
        tv_jubao_two = (TextView) findViewById(R.id.tv_jubao_two);
        tv_jubao_three = (TextView) findViewById(R.id.tv_jubao_three);
        tv_jubao_close = (TextView) findViewById(R.id.tv_jubao_close);
        tv_jubao_sure = (TextView) findViewById(R.id.tv_jubao_sure);

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
        iv_crop_imageview.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        rl_pop_jubao.setOnClickListener(this);
        tv_jubao_one.setOnClickListener(this);
        tv_jubao_two.setOnClickListener(this);
        tv_jubao_three.setOnClickListener(this);
        tv_jubao_close.setOnClickListener(this);
        tv_jubao_sure.setOnClickListener(this);
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
        category_name = getIntent().getStringExtra("category_name");
        network = getIntent().getStringExtra("network");
        x1 = getIntent().getIntExtra("x1", 0);
        x2 = getIntent().getIntExtra("x2", 0);
        y1 = getIntent().getIntExtra("y1", 0);
        y2 = getIntent().getIntExtra("y2", 0);
        ifneedcrop = getIntent().getIntExtra("ifneedcrop", 0);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        wide = PublicUtils.getScreenWidth(this) / 2 - UIUtils.getDimens(R.dimen.font_14);

        if (ifneedcrop == 0) {
            cropSmallImage();
        } else {
            ImageUtils.disRectangleImage(image_url, iv_crop_imageview);
        }
        tv_tital_comment.setText("相似商品");
        getTypeData();
        initRecyclerView();
        if (!ifMoveKuang && !TextUtils.isEmpty(category_name)) {
            rl_type.setVisibility(View.VISIBLE);
            tv_type.setText(category_name.trim());
            tv_type_set.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                NewShopDetailsActivity.this.finish();
                break;
            case R.id.iv_xuanxiang:
                closeAllPopWin();
                if (null == typeNewBean) {
                    getTypeData();
                }
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
                if (maxPrice == -1 && minPrice == -1 && v.getId() == R.id.rl_price) {
                    if (null != shopPriceWindow && shopPriceWindow.isShowing()) {
                        shopPriceWindow.dismiss();
                        minPrice = -1;
                        maxPrice = -1;
                        closeCurrentPopWin(R.id.iv_price_delect);
                        rl_price.setVisibility(View.GONE);
                        tv_price_set.setVisibility(View.VISIBLE);
                        ifShowAddButton();
                    }
                } else {
                    if (v.getId() == R.id.tv_price_set) {
                        tv_price.setText("未选择");
                    }
                    ifShowPopWin(R.id.tv_price_set);
                    rl_price.setVisibility(View.VISIBLE);
                    tv_price_set.setVisibility(View.GONE);
                    rl_add_shuaixuan.setVisibility(View.VISIBLE);
                    rl_set_shuaixuan.setVisibility(View.GONE);
                    ifShowAddButton();
                    if (v.getId() == R.id.tv_price_set) {
                        rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
                    }
                }
                break;
            case R.id.tv_type_set:
            case R.id.rl_type:
                if (TextUtils.isEmpty(category_id) && v.getId() == R.id.rl_type) {
                    if (null != shopTypeWindow && shopTypeWindow.isShowing()) {
                        shopTypeWindow.dismiss();
                        category_id = "";
                        category_name = "";
                        closeCurrentPopWin(R.id.iv_type_delect);
                        rl_type.setVisibility(View.GONE);
                        tv_type_set.setVisibility(View.VISIBLE);
                        ifShowAddButton();
                    }
                } else {
                    if (v.getId() == R.id.tv_type_set) {
                        tv_type.setText("未选择");
                    }
                    if (!TextUtils.isEmpty(category_id) && !TextUtils.isEmpty(category_name)) {
                        tv_type.setText(category_name);
                    }
                    ifShowPopWin(R.id.tv_type_set);
                    rl_type.setVisibility(View.VISIBLE);
                    tv_type_set.setVisibility(View.GONE);
                    rl_add_shuaixuan.setVisibility(View.VISIBLE);
                    rl_set_shuaixuan.setVisibility(View.GONE);
                    ifShowAddButton();
                    if (v.getId() == R.id.tv_type_set) {
                        rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
                    }
                }
                break;
            case R.id.tv_guanjianzi_set:
            case R.id.rl_guanjianzi:
                if (TextUtils.isEmpty(kw) && v.getId() == R.id.rl_guanjianzi) {
                    if (null != shopGuanJianZiWindow && shopGuanJianZiWindow.isShowing()) {
                        shopGuanJianZiWindow.dismiss();
                        kw = "";
                        closeCurrentPopWin(R.id.iv_type_delect);
                        rl_guanjianzi.setVisibility(View.GONE);
                        tv_guanjianzi_set.setVisibility(View.VISIBLE);
                        ifShowAddButton();
                    }
                } else {
                    if (v.getId() == R.id.tv_guanjianzi_set) {
                        tv_guanjianzi.setText("未选择");
                    }
                    if (!TextUtils.isEmpty(kw)) {
                        tv_guanjianzi.setText(kw);
                    }
                    ifShowPopWin(R.id.tv_guanjianzi_set);
                    rl_guanjianzi.setVisibility(View.VISIBLE);
                    tv_guanjianzi_set.setVisibility(View.GONE);
                    rl_add_shuaixuan.setVisibility(View.VISIBLE);
                    rl_set_shuaixuan.setVisibility(View.GONE);
                    ifShowAddButton();
                    if (v.getId() == R.id.tv_guanjianzi_set) {
                        rl_add_shuaixuan.setAnimation(AnimationUtils.makeInAnimation(this, true));
                    }
                }
                break;
            case R.id.iv_price_delect:
                minPrice = -1;
                maxPrice = -1;
                closeCurrentPopWin(R.id.iv_price_delect);
                rl_price.setVisibility(View.GONE);
                tv_price_set.setVisibility(View.VISIBLE);
                ifShowAddButton();
                onRefresh();
                break;
            case R.id.iv_type_delect:

                category_id = "";
                category_name = "";
                closeCurrentPopWin(R.id.iv_type_delect);
                rl_type.setVisibility(View.GONE);
                tv_type_set.setVisibility(View.VISIBLE);
                ifShowAddButton();
                onRefresh();
                break;
            case R.id.iv_guanjianzi_delect:
                guanjianzi = "";
                kw = "";
                closeCurrentPopWin(R.id.iv_guanjianzi_delect);
                rl_guanjianzi.setVisibility(View.GONE);
                tv_guanjianzi_set.setVisibility(View.VISIBLE);
                ifShowAddButton();
                onRefresh();
                break;
            case R.id.view_pop_bottom:
                if (minPrice == -1 && maxPrice == -1) {
                    if (null != shopPriceWindow && shopPriceWindow.isShowing()) {
                        shopPriceWindow.dismiss();
                        minPrice = -1;
                        maxPrice = -1;
                        closeCurrentPopWin(R.id.iv_price_delect);
                        rl_price.setVisibility(View.GONE);
                        tv_price_set.setVisibility(View.VISIBLE);
                        ifShowAddButton();
                    }
                } else {
                    closeCurrentPopWin(R.id.iv_price_delect);
                    tabStaus(0);
                }
                break;
            case R.id.bt_sure_price:
                if (shopPriceWindow != null && shopPriceWindow.isShowing()) {
                    minPrice = Float.parseFloat(shopPriceWindow.getChooseMin().trim());
                    maxPrice = Float.parseFloat(shopPriceWindow.getChooseMax().trim());
                    tv_price.setText("¥ " + PublicUtils.formatPrice(minPrice) + " - " + PublicUtils.formatPrice(maxPrice));
                    ifShowPopWin(R.id.rl_price);
                    onRefresh();
                    //友盟统计
                    HashMap<String, String> map6 = new HashMap<String, String>();
                    map6.put("evenname", "筛选相似");
                    map6.put("even", "价格-" + tv_price.toString().trim());
                    MobclickAgent.onEvent(NewShopDetailsActivity.this, "shijian16", map6);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("价格-" + tv_price.toString().trim())  //事件类别
                            .setAction("筛选相似")      //事件操作
                            .build());
                }
                break;
            case R.id.iv_crop_imageview:
                rl_image_big.setVisibility(View.VISIBLE);
                if (ifneedcrop == 0) {
                    if (!TextUtils.isEmpty(cropSmallImage)) {
                        ImageUtils.disRectangleImageTou("file://" + cropSmallImage, pv_big_imageview);
                    } else {
                        ImageUtils.disRectangleImageTou(image_url, pv_big_imageview);
                    }
                } else {
                    ImageUtils.disRectangleImageTou(image_url, pv_big_imageview);
                }
                pv_big_imageview.setZoomable(false);
                break;
            case R.id.iv_delete:
                rl_image_big.setVisibility(View.GONE);
                break;
            case R.id.rl_pop_jubao:
                rl_pop_jubao.setVisibility(View.GONE);
                break;
            case R.id.tv_jubao_one:

                current_jubao = 1;
                changeJuBao(1);
                break;
            case R.id.tv_jubao_two:

                current_jubao = 2;
                changeJuBao(2);
                break;
            case R.id.tv_jubao_three:

                current_jubao = 3;
                changeJuBao(3);
                break;
            case R.id.tv_jubao_sure:
                if (current_position != -1 && mListData.size() > current_position) {

                    String spu_id = mListData.get(position).getItem_info().getSpu_id();
                    juBao(spu_id, current_jubao);
                    current_position = -1;
                    rl_pop_jubao.setVisibility(View.GONE);

                } else {
                    current_position = -1;
                    rl_pop_jubao.setVisibility(View.GONE);
                    ToastUtils.showCenter(NewShopDetailsActivity.this, "抱歉，您的举报没有发送成功，请稍后再试!");
                }
//                current_position = -1;
                break;
            case R.id.tv_jubao_close:
                rl_pop_jubao.setVisibility(View.GONE);
                current_position = -1;
                current_jubao = 1;
                changeJuBao(1);
                break;
        }
    }

    private void initRecyclerView() {
        MultiItemTypeSupport<SearchShopItemBean> support = new MultiItemTypeSupport<SearchShopItemBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == 0) {
                    return R.layout.item_new_shopresult_left;
                } else {
                    return R.layout.item_new_shopresult_right;
                }
            }

            @Override
            public int getItemViewType(int position, SearchShopItemBean s) {
                if (position % 2 == 0) {
                    return 0;//左边
                } else {
                    return 1;//右边
                }
            }
        };

        mAdapter = new MultiItemCommonAdapter<SearchShopItemBean>(NewShopDetailsActivity.this, mListData, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {

                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_image_view).getLayoutParams();
                layoutParams.width = wide;
                layoutParams.height = wide;
                holder.getView(R.id.iv_image_view).setLayoutParams(layoutParams);
                ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg0(),
                        (ImageView) holder.getView(R.id.iv_image_view));
                ((TextView) holder.getView(R.id.tv_tital)).setText(mListData.get(position).getItem_info().getTitle());
                ((TextView) holder.getView(R.id.tv_price)).setText("¥ " + mListData.get(position).getItem_info().getPrice());
                ((TextView) holder.getView(R.id.tv_go_buy)).setText("去" + mListData.get(position).getItem_info().getSource_name());
                if (!mListData.get(position).getItem_info().getIs_collected().trim().equals("1")) {
                    //未收藏（显示收藏）
                    ((TextView) holder.getView(R.id.tv_goto_shoucang)).setTextColor(UIUtils.getColor(R.color.white));
                    holder.getView(R.id.tv_goto_shoucang).setBackgroundResource(R.drawable.bt_sousuo);
                    ((TextView) holder.getView(R.id.tv_goto_shoucang)).setText("收藏");
                } else {
                    //已经收藏了（显示已收藏）
                    ((TextView) holder.getView(R.id.tv_goto_shoucang)).setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                    holder.getView(R.id.tv_goto_shoucang).setBackgroundResource(R.drawable.bt_sousuo_quxiao);
                    ((TextView) holder.getView(R.id.tv_goto_shoucang)).setText("已收藏");
                }
                holder.getView(R.id.tv_goto_shoucang).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Boolean loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
                        if (!loginStatus) {
                            //友盟统计
                            HashMap<String, String> map4 = new HashMap<String, String>();
                            map4.put("evenname", "登录入口");
                            map4.put("even", "相似商品页点击商品收藏");
                            MobclickAgent.onEvent(NewShopDetailsActivity.this, "shijian20", map4);
                            //ga统计
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory("相似商品页点击商品收藏")  //事件类别
                                    .setAction("登录入口")      //事件操作
                                    .build());
                            Intent intent = new Intent(NewShopDetailsActivity.this, LoginActivity.class);
                            startActivityForResult(intent, 1);
                        } else {
                            onShouCang(!mListData.get(position).getItem_info().getIs_collected().trim().equals("1"), position, mListData.get(position));
                        }
                    }
                });
                if (position == openPosition) {
                    ((RelativeLayout) holder.getView(R.id.rl_goto_xiangsi)).setVisibility(View.VISIBLE);
                } else {
                    ((RelativeLayout) holder.getView(R.id.rl_goto_xiangsi)).setVisibility(View.GONE);
                }
                holder.getView(R.id.iv_image_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openPosition = position;
                        mAdapter.notifyDataSetChanged();
                        ((RelativeLayout) holder.getView(R.id.rl_goto_xiangsi)).setVisibility(View.VISIBLE);
                    }
                });
                holder.getView(R.id.rl_goto_xiangsi).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //友盟统计
                        HashMap<String, String> map6 = new HashMap<String, String>();
                        map6.put("evenname", "取消找相似");
                        map6.put("even", "取消找相似");
                        MobclickAgent.onEvent(NewShopDetailsActivity.this, "shijian15", map6);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("取消找相似")  //事件类别
                                .setAction("取消找相似")      //事件操作
                                .build());
                        openPosition = -1;
                        ((RelativeLayout) holder.getView(R.id.rl_goto_xiangsi)).setVisibility(View.GONE);
                    }
                });
                holder.getView(R.id.riv_goto_xiangsi).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //友盟统计
                        HashMap<String, String> map6 = new HashMap<String, String>();
                        map6.put("evenname", "找相似");
                        map6.put("even", "找相似");
                        MobclickAgent.onEvent(NewShopDetailsActivity.this, "shijian14", map6);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("找相似")  //事件类别
                                .setAction("找相似")      //事件操作
                                .build());
                        Intent intent = new Intent(NewShopDetailsActivity.this, NewShopDetailsActivity.class);
                        intent.putExtra("image_path", mListData.get(position).getItem_info().getImage().getImg0());
                        intent.putExtra("image_url", mListData.get(position).getItem_info().getImage().getImg0());
                        intent.putExtra("loc", "");
                        intent.putExtra("ifMoveKuang", true);
                        intent.putExtra("ifneedcrop", 2);
                        startActivity(intent);
                    }
                });
                holder.getView(R.id.iv_jubao).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 初始化举报数据
                        current_position = position;
                        current_jubao = 1;
                        changeJuBao(1);
                        rl_pop_jubao.setVisibility(View.VISIBLE);

                    }
                });
                holder.getView(R.id.riv_goto_buy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //友盟统计
                        HashMap<String, String> map6 = new HashMap<String, String>();
                        map6.put("evenname", "去购买");
                        map6.put("even", "相似商品页");
                        MobclickAgent.onEvent(NewShopDetailsActivity.this, "shijian19", map6);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("相似商品页")  //事件类别
                                .setAction("去购买")      //事件操作
                                .build());

                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    if (!mListData.get(position).getItem_info().getSource_name().equals("极有家")) {
//                                    String htmlContent = HtmlService.getHtml("https://item.jd.com/11688078694.html ", "京东");
                                        String htmlContent = HtmlService.getHtml(mListData.get(position).getItem_info().getBuy_url(), mListData.get(position).getItem_info().getSource_name());
                                        Message message = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("htmlContent", htmlContent);
                                        bundle.putString("spu_id", mListData.get(position).getItem_info().getSpu_id());
                                        message.setData(bundle);
                                        handler1.sendMessage(message);
                                    } else {
                                        //TODO 极有家处理
                                        String htmlContent = HtmlService.getJiYouJiaHtml("https://item.taobao.com/item.htm?id=546305631943", mListData.get(position).getItem_info().getSource_name(),mListData.get(position).getItem_info().getSpu_id());
//                                        String htmlContent = HtmlService.getJiYouJiaHtml(mListData.get(position).getItem_info().getBuy_url(), mListData.get(position).getItem_info().getSource_name(),mListData.get(position).getItem_info().getSpu_id());
                                        Message message = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("htmlContent", htmlContent);
                                        bundle.putString("spu_id", mListData.get(position).getItem_info().getSpu_id());
                                        message.setData(bundle);
                                        handler1.sendMessage(message);

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.d("test", "报错" + e.getMessage());
                                }
                            }
                        }.start();
//                        Intent intent  = new Intent(NewShopDetailsActivity.this,TestActivity.class);
//                        intent.putExtra("image_url",mListData.get(position).getItem_info().getBuy_url());
//                        startActivity(intent);
                        Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(mListData.get(position).getItem_info().getBuy_url()));
                        startActivity(viewIntent);

                    }
                });

            }
        };

        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }


    private void cropSmallImage() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (!TextUtils.isEmpty(cropImage)) {
                    FileInputStream fis = null;
                    Bitmap bitmap1 = null;
                    try {
                        if (network.equals("true")) {
                            bitmap1 = getBitmap(cropImage);
                        } else {
                            fis = new FileInputStream(cropImage);
                            bitmap1 = BitmapFactory.decodeStream(fis);
                        }
                        if (null != bitmap1) {
                            Bitmap bitmap = imageCrop(bitmap1);
                            cropName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
//                            cropName = "JT_IMG_" + "CROP" + ".jpg";
                            File file = createFile(photoPath, cropName);

                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            fos.flush();
                            fos.close();
                            handler.sendEmptyMessage(0);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        }.start();

    }

    public Bitmap getBitmap(String path) throws IOException {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按正方形裁切图片
     */
    public Bitmap imageCrop(Bitmap bitmap) {

        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wCrop = x2 - x1;// 裁切后所取的正方形区域宽
        int hCrop = y2 - y1;// 裁切后所取的正方形区域高

        if (x1 == 0 && x2 == 0) {
            wCrop = w;
        }
        if (y1 == 0 && y2 == 0) {
            hCrop = h;
        }

        int retX = x1;//基于原图，取正方形左上角x坐标
        int retY = y1;
        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, wCrop, hCrop, null, false);
    }

    private File createFile(String path, String name) {
        File folder = new File(path);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                return null;
            }
        }
        return new File(folder, name);
    }

    //收藏或者取消收藏，图片
    public void onShouCang(boolean ifShouCang, int position, SearchShopItemBean searchShopItemBean) {

        if (ifClickShouCang) {
            ifClickShouCang = false;
            if (ifShouCang) {

                //友盟统计
                HashMap<String, String> map6 = new HashMap<String, String>();
                map6.put("evenname", "收藏商品");
                map6.put("even", "相似商品页");
                MobclickAgent.onEvent(NewShopDetailsActivity.this, "shijian17", map6);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("相似商品页")  //事件类别
                        .setAction("收藏商品")      //事件操作
                        .build());
                //未被收藏，去收藏
                addShouCang(position, searchShopItemBean.getItem_info().getSpu_id());
            } else {
                //友盟统计
                HashMap<String, String> map6 = new HashMap<String, String>();
                map6.put("evenname", "取消收藏商品");
                map6.put("even", "相似商品页");
                MobclickAgent.onEvent(NewShopDetailsActivity.this, "shijian18", map6);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("相似商品页")  //事件类别
                        .setAction("取消收藏商品")      //事件操作
                        .build());
                //被收藏，去取消收藏
                removeShouCang(position, searchShopItemBean.getItem_info().getSpu_id());
            }
        }

    }

    private void addShouCang(final int position, String spu_id) {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onResponse(String response) {
                ifClickShouCang = true;
                try {
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                        String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                        String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                        if (error_code == 0) {
                            ToastUtils.showCenter(NewShopDetailsActivity.this, "收藏成功！");
                            mListData.get(position).getItem_info().setIs_collected("1");
                            mAdapter.notifyItemChanged(position);
                        } else {
                            ToastUtils.showCenter(NewShopDetailsActivity.this, error_msg);
                        }
                    } else {
                        ToastUtils.showCenter(NewShopDetailsActivity.this, "收藏失败！");
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(NewShopDetailsActivity.this, "商品收藏失败！");
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                ifClickShouCang = true;
                ToastUtils.showCenter(NewShopDetailsActivity.this, "商品收藏失败！");
            }
        };
        MyHttpManager.getInstance().shoucangShop(spu_id, callback);
    }

    //取消收藏
    private void removeShouCang(final int position, String spu_id) {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ifClickShouCang = true;
                ToastUtils.showCenter(NewShopDetailsActivity.this, "取消收藏失败");

            }

            @Override
            public void onResponse(String s) {
                ifClickShouCang = true;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ToastUtils.showCenter(NewShopDetailsActivity.this, "取消收藏成功");
                        mListData.get(position).getItem_info().setIs_collected("0");
                        mAdapter.notifyItemChanged(position);
                    } else {
                        ToastUtils.showCenter(NewShopDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(NewShopDetailsActivity.this, "取消收藏失败");
                }
            }
        };
        MyHttpManager.getInstance().deleteShop(spu_id, callBack);
    }

    private void getTypeData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        typeNewBean = GsonUtil.jsonToBean(data_msg, TypeNewBean.class);
                        if (shopTypeWindow == null) {
                            shopTypeWindow = new ShopTypeWindow(NewShopDetailsActivity.this, NewShopDetailsActivity.this, typeNewBean, NewShopDetailsActivity.this);
                        }
                        shopTypeWindow.setTypeData(typeNewBean);
                    } else {
                        ToastUtils.showCenter(NewShopDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getShopTypes(callBack);

    }

    //显示或隐藏状态
    private void ifShowPopWin(int id) {
        switch (id) {
            case R.id.tv_price_set:
            case R.id.rl_price:
                if (shopPriceWindow == null) {
                    shopPriceWindow = new ShopPriceWindow(NewShopDetailsActivity.this, this, this);
                }
                if (searchFacetsBean != null && searchFacetsBean.getPrice() != null && minPrice == -1 && maxPrice == -1) {
                    shopPriceWindow.setPriceData(searchFacetsBean.getPrice().getMin(), searchFacetsBean.getPrice().getMax());
                    shopPriceWindow.setSeekBarValut();
                }
                closeOtherWin(id);
                if (shopPriceWindow.isShowing()) {
                    shopPriceWindow.dismiss();
                    tabStaus(0);
                } else {
                    tabStaus(id);
                    if (Build.VERSION.SDK_INT < 24) {
                        shopPriceWindow.showAsDropDown(view_line_pop);
                    } else {
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
                if (shopTypeWindow == null) {
                    shopTypeWindow = new ShopTypeWindow(NewShopDetailsActivity.this, this, typeNewBean, this);
                }
                if (TextUtils.isEmpty(category_name) && TextUtils.isEmpty(category_id)) {
                    shopTypeWindow.setChooseName("");
                    shopTypeWindow.setMapSelect(new HashMap<String, Integer>());
                    shopTypeWindow.setMapType(new HashMap<String, Integer>());
                }
                if (typeNewBean == null) {
                    getTypeData();
                } else if (typeNewBean != null && TextUtils.isEmpty(category_id)) {
                    shopTypeWindow.setTypeData(typeNewBean);
                } else if (typeNewBean != null && !TextUtils.isEmpty(category_id) && !TextUtils.isEmpty(category_name)) {
                    shopTypeWindow.setTypes(typeNewBean, category_id, category_name);
                }
                closeOtherWin(id);
                if (shopTypeWindow.isShowing()) {
                    shopTypeWindow.dismiss();
                    tabStaus(0);
                } else {
                    tabStaus(id);
                    if (Build.VERSION.SDK_INT < 24) {
                        shopTypeWindow.showAsDropDown(view_line_pop);
                    } else {
                        // 获取控件的位置，安卓系统>7.0
                        int[] location = new int[2];
                        view_line_pop.getLocationOnScreen(location);
                        int screenHeight = PublicUtils.getScreenHeight(NewShopDetailsActivity.this);
                        shopTypeWindow.setHeight(screenHeight - location[1]);
                        shopTypeWindow.showAtLocation(view_line_pop, Gravity.NO_GRAVITY, 0, location[1]);
                    }
                }
                break;
            case R.id.tv_guanjianzi_set:
            case R.id.rl_guanjianzi:
                if (shopGuanJianZiWindow == null) {
                    shopGuanJianZiWindow = new ShopGuanJianZiWindow(NewShopDetailsActivity.this, this, NewShopDetailsActivity.this);

                    shopGuanJianZiWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                    shopGuanJianZiWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
                closeOtherWin(id);
                if (shopGuanJianZiWindow.isShowing()) {
                    shopGuanJianZiWindow.dismiss();
                    tabStaus(0);
                } else {
                    tabStaus(id);
                    if (Build.VERSION.SDK_INT < 24) {
                        shopGuanJianZiWindow.showAsDropDown(view_line_pop);
                    } else {
                        // 获取控件的位置，安卓系统>7.0
                        int[] location = new int[2];
                        view_line_pop.getLocationOnScreen(location);
                        int screenHeight = PublicUtils.getScreenHeight(NewShopDetailsActivity.this);
                        shopGuanJianZiWindow.setHeight(screenHeight - location[1]);
                        shopGuanJianZiWindow.showAtLocation(view_line_pop, Gravity.NO_GRAVITY, 0, location[1]);
                    }
                }
                break;
        }

    }

    //➕状态改变
    private void ifShowAddButton() {
        if (rl_price.getVisibility() == View.VISIBLE && rl_type.getVisibility() == View.VISIBLE && rl_guanjianzi.getVisibility() == View.VISIBLE) {

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
                if (null != shopTypeWindow && shopTypeWindow.isShowing()) {
                    shopTypeWindow.dismiss();
                }
                if (null != shopGuanJianZiWindow && shopGuanJianZiWindow.isShowing()) {
                    shopGuanJianZiWindow.dismiss();
                }
                break;
            case R.id.tv_type_set:
            case R.id.rl_type:
                if (null != shopPriceWindow && shopPriceWindow.isShowing()) {
                    shopPriceWindow.dismiss();
                }
                if (null != shopGuanJianZiWindow && shopGuanJianZiWindow.isShowing()) {
                    shopGuanJianZiWindow.dismiss();
                }
                break;
            case R.id.tv_guanjianzi_set:
            case R.id.rl_guanjianzi:
                if (null != shopPriceWindow && shopPriceWindow.isShowing()) {
                    shopPriceWindow.dismiss();
                }
                if (null != shopTypeWindow && shopTypeWindow.isShowing()) {
                    shopTypeWindow.dismiss();
                }
                break;
            case 0:
                if (null != shopPriceWindow && shopPriceWindow.isShowing()) {
                    shopPriceWindow.dismiss();
                }
                if (null != shopTypeWindow && shopTypeWindow.isShowing()) {
                    shopTypeWindow.dismiss();
                }
                if (null != shopGuanJianZiWindow && shopGuanJianZiWindow.isShowing()) {
                    shopGuanJianZiWindow.dismiss();
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
                if (shopTypeWindow != null && shopTypeWindow.isShowing()) {
                    shopTypeWindow.dismiss();
                }
                break;
            case R.id.iv_guanjianzi_delect:
                if (shopGuanJianZiWindow != null && shopGuanJianZiWindow.isShowing()) {
                    shopGuanJianZiWindow.dismiss();
                }
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
        if (shopPriceWindow != null && shopPriceWindow.isShowing() && searchFacetsBean != null) {
            float price = shopPriceWindow.getmMaxP() - shopPriceWindow.getmMinP();
            if (min < 1 && max > 1) {
                tv_price.setText("¥ " + PublicUtils.formatPrice(shopPriceWindow.getmMinP()) + " - " + PublicUtils.formatPrice(max / 100 * price + shopPriceWindow.getmMinP()));
            } else if (min < 1 && max < 1) {
                tv_price.setText("¥ " + PublicUtils.formatPrice(shopPriceWindow.getmMinP()) + " - " + PublicUtils.formatPrice(shopPriceWindow.getmMinP()));
            } else {
                tv_price.setText("¥ " + PublicUtils.formatPrice(min / 100 * price + shopPriceWindow.getmMinP()) + " - " + PublicUtils.formatPrice(max / 100 * price + shopPriceWindow.getmMinP()));
            }
        }
    }

    @Override
    public void clickType(String categoryName, Integer categoryId) {
//        category_id = categoryId + "";
//        category_name = categoryName;
        if (TextUtils.isEmpty(categoryName) && categoryId == 0) {
            tv_type.setText("未选择");
        } else {
            tv_type.setText(categoryName);
        }
    }

    @Override
    public void onClickSure(String categoryname, Integer categoryid) {
        category_id = categoryid + "";
        category_name = categoryname;
        shopPriceWindow = null;
        if (shopTypeWindow != null && shopTypeWindow.isShowing()) {
            ifShowPopWin(R.id.rl_type);
            minPrice = -1;
            maxPrice = -1;
            rl_price.setVisibility(View.GONE);
            tv_price_set.setVisibility(View.VISIBLE);
            ifShowAddButton();
            onRefresh();
            //友盟统计
            HashMap<String, String> map6 = new HashMap<String, String>();
            map6.put("evenname", "筛选相似");
            map6.put("even", "品类-" + categoryname);
            MobclickAgent.onEvent(NewShopDetailsActivity.this, "shijian16", map6);
            //ga统计
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("品类-" + categoryname)  //事件类别
                    .setAction("筛选相似")      //事件操作
                    .build());
        }

    }

    @Override
    public void closePop() {

        ifShowPopWin(R.id.rl_type);
        if (TextUtils.isEmpty(category_id) && TextUtils.isEmpty(category_name)) {
            rl_type.setVisibility(View.GONE);
            tv_type_set.setVisibility(View.VISIBLE);
        } else {
            rl_type.setVisibility(View.VISIBLE);
            tv_type_set.setVisibility(View.GONE);
            tv_type.setText(category_name);
        }
        ifShowAddButton();
    }

    private void getListData(final String status) {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (status.equals(REFRESH_STATUS)) {
                    pager = 1;
                } else {
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
                        try {
                            SearchShopsBean searchShopsBean = GsonUtil.jsonToBean(data_msg, SearchShopsBean.class);
                            if (null != searchShopsBean.getItem_list() && 0 != searchShopsBean.getItem_list().size()) {
                                if (pager == 1) {
                                    searchFacetsBean = searchShopsBean.getFacets();
                                    if (shopPriceWindow != null && searchFacetsBean != null && searchFacetsBean.getPrice() != null && !ifSetPrice) {
                                        ifSetPrice = true;
                                        shopPriceWindow.setPriceData(searchFacetsBean.getPrice().getMin(), searchFacetsBean.getPrice().getMax());
                                    }
                                }
                                updateViewFromData(searchShopsBean.getItem_list(), status);
                            } else {
                                updateViewFromData(null, status);
                            }
                        } catch (Exception e) {
                            updateViewFromData(null, status);
                        }
                    } else {
                    }
                } catch (JSONException e) {
                }
            }
        };
        if (ifMoveKuang) {//移动了，部分参数不传递
            MyHttpManager.getInstance().getNewShops(image_url, pager + "", num_shop + "", mLoc, category_id, minPrice == -1 || maxPrice == -1 ? "" : minPrice + "-" + maxPrice, kw, "", callback);
        } else {
            MyHttpManager.getInstance().getNewShops(image_url, pager + "", num_shop + "", mLoc, category_id, minPrice == -1 || maxPrice == -1 ? "" : minPrice + "-" + maxPrice, kw, object_sign, callback);
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
        openPosition = -1;
//        mRecyclerView.setRefreshing(true);
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getListData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++pager;
        getListData(LOADMORE_STATUS);
    }

    @Override
    public void clickGuanJianZiBottom() {

        if (TextUtils.isEmpty(guanjianzi) && shopGuanJianZiWindow != null) {
            closeCurrentPopWin(R.id.iv_guanjianzi_delect);
            rl_guanjianzi.setVisibility(View.GONE);
            tv_guanjianzi_set.setVisibility(View.VISIBLE);
            ifShowAddButton();
        } else {
            closeCurrentPopWin(R.id.iv_guanjianzi_delect);
            tabStaus(0);
        }
    }

    @Override
    public void clickGuanJianZiSure(String guanjianzi) {
        this.guanjianzi = guanjianzi;
        this.kw = guanjianzi;
        shopPriceWindow = null;
        minPrice = -1;
        maxPrice = -1;
        rl_price.setVisibility(View.GONE);
        tv_price_set.setVisibility(View.VISIBLE);
        ifShowAddButton();
        tv_guanjianzi.setText(kw);
        closeCurrentPopWin(R.id.iv_guanjianzi_delect);
        tabStaus(0);
        onRefresh();
        //友盟统计
        HashMap<String, String> map6 = new HashMap<String, String>();
        map6.put("evenname", "筛选相似");
        map6.put("even", "关键字-" + kw);
        MobclickAgent.onEvent(NewShopDetailsActivity.this, "shijian16", map6);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("关键字-" + kw)  //事件类别
                .setAction("筛选相似")      //事件操作
                .build());
    }

    private void changeJuBao(int position) {
        switch (position) {
            case 1:
                tv_jubao_one.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                tv_jubao_two.setTextColor(UIUtils.getColor(R.color.bg_262626));
                tv_jubao_three.setTextColor(UIUtils.getColor(R.color.bg_262626));
                tv_jubao_one.setBackgroundResource(R.drawable.jubao_back1);
                tv_jubao_two.setBackgroundResource(R.drawable.jubao_back);
                tv_jubao_three.setBackgroundResource(R.drawable.jubao_back);
                break;
            case 2:
                tv_jubao_one.setTextColor(UIUtils.getColor(R.color.bg_262626));
                tv_jubao_two.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                tv_jubao_three.setTextColor(UIUtils.getColor(R.color.bg_262626));
                tv_jubao_one.setBackgroundResource(R.drawable.jubao_back);
                tv_jubao_two.setBackgroundResource(R.drawable.jubao_back1);
                tv_jubao_three.setBackgroundResource(R.drawable.jubao_back);
                break;
            case 3:
                tv_jubao_one.setTextColor(UIUtils.getColor(R.color.bg_262626));
                tv_jubao_two.setTextColor(UIUtils.getColor(R.color.bg_262626));
                tv_jubao_three.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                tv_jubao_one.setBackgroundResource(R.drawable.jubao_back);
                tv_jubao_two.setBackgroundResource(R.drawable.jubao_back);
                tv_jubao_three.setBackgroundResource(R.drawable.jubao_back1);
                break;
        }
    }

    private void juBao(String object_id, int report_id) {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ToastUtils.showCenter(NewShopDetailsActivity.this, "家图已经收到了您的举报，非常感谢!");
                    } else {
                        ToastUtils.showCenter(NewShopDetailsActivity.this, "抱歉，您的举报没有发送成功，请稍后再试!");
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().juBao(ClassConstant.JuBao.PRODUCT, object_id, report_id + "", callBack);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(" 相似商品页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName(" 相似商品页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(" 相似商品页");
        MobclickAgent.onPause(this);
    }

    private String image_url;
    private int pager = 1;
    private int num_shop = 20;
    private String mLoc;

    private String kw = "";

    private String object_sign;
    private String category_id = "";
    private String category_name = "";
    private boolean ifMoveKuang;

    private float minPrice = -1;
    private float maxPrice = -1;

    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";

    private boolean ifSetPrice = false;

    private String guanjianzi = "";

}
