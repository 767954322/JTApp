package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.adapter.HomeTagAdapter;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.color.ColorBean;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.search.SearchDataBean;
import com.homechart.app.home.bean.search.SearchDataColorBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.HomeTabPopWin;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.SelectColorPopupWindow;
import com.homechart.app.myview.SelectColorSeCaiWindow;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/8/14.
 */

public class ColorShaiXuanActivity
        extends BaseActivity
        implements View.OnClickListener,
        ViewPager.OnPageChangeListener,
        HomeTagAdapter.PopupWindowCallBack,
        OnLoadMoreListener,
        OnRefreshListener,
        SelectColorSeCaiWindow.SureColor {

    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private ColorItemBean mColorClick;
    private LinearLayout ll_pic_choose;
    private RoundImageView iv_kongjian;
    private RoundImageView iv_jubu;
    private RoundImageView iv_zhuangshi;
    private RoundImageView iv_shouna;
    private RoundImageView iv_secai;
    private RelativeLayout rl_kongjian;
    private RelativeLayout rl_jubu;
    private RelativeLayout rl_zhuangshi;
    private RelativeLayout rl_shouna;
    private RelativeLayout rl_secai;
    private ImageView iv_change_frag;
    private View view_center;
    private TagDataBean tagDataBean;
    private ColorBean colorBean;
    private HomeTabPopWin homeTabPopWin;
    private int last_id = 0;
    private float mDownY;
    private float mMoveY;
    private TextView tv_color_tital;
    private boolean move_tag = true;
    private HRecyclerView mRecyclerView;
    private RelativeLayout rl_tos_choose;
    private RelativeLayout rl_pic_change;
    private int width_Pic_Staggered;
    private int width_Pic_List;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int TYPE_ONE = 1;
    private int TYPE_TWO = 2;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int page_num = 1;
    private boolean curentListTag = true;
    private int scroll_position = 0;
    private LoadMoreFooterView mLoadMoreFooterView;
    private List<SearchItemDataBean> mListData = new ArrayList<>();
    private List<Integer> mLListDataHeight = new ArrayList<>();
    private List<Integer> mSListDataHeight = new ArrayList<>();
    private List<String> mItemIdList = new ArrayList<>();

    private MultiItemCommonAdapter<SearchItemDataBean> mAdapter;
    private Map<Integer, ColorItemBean> mSelectListData = new HashMap<>();
    private int position;
    private TextView bt_tag_page_item;
    private ImageView iv_chongzhi;
    private ImageView iv_color_icon;
    private SelectColorSeCaiWindow selectColorPopupWindow;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
            } else if (msg.what == 2) {
                String info = (String) msg.obj;
                tagDataBean = GsonUtil.jsonToBean(info, TagDataBean.class);
            } else if (msg.what == 3) {
                String info = (String) msg.obj;
                colorBean = GsonUtil.jsonToBean(info, ColorBean.class);
                Log.d("test", colorBean.toString());
            }
        }
    };

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        mColorClick = (ColorItemBean) getIntent().getSerializableExtra("color");
        tagDataBean = (TagDataBean) getIntent().getSerializableExtra("tagDataBean");
        colorBean = (ColorBean) getIntent().getSerializableExtra("colorBean");
        mSelectListData.put(mColorClick.getColor_id(), mColorClick);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shaixuan_color;
    }

    @Override
    protected void initView() {

        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_color_tital = (TextView) findViewById(R.id.tv_color_tital);
        bt_tag_page_item = (TextView) findViewById(R.id.bt_tag_page_item);
        view_center = findViewById(R.id.view_center);
        ll_pic_choose = (LinearLayout) findViewById(R.id.ll_pic_choose);
        iv_kongjian = (RoundImageView) findViewById(R.id.iv_kongjian);
        iv_jubu = (RoundImageView) findViewById(R.id.iv_jubu);
        iv_zhuangshi = (RoundImageView) findViewById(R.id.iv_zhuangshi);
        iv_shouna = (RoundImageView) findViewById(R.id.iv_shouna);
        iv_secai = (RoundImageView) findViewById(R.id.iv_secai);
        rl_kongjian = (RelativeLayout) findViewById(R.id.rl_kongjian);
        rl_jubu = (RelativeLayout) findViewById(R.id.rl_jubu);
        rl_zhuangshi = (RelativeLayout) findViewById(R.id.rl_zhuangshi);
        rl_shouna = (RelativeLayout) findViewById(R.id.rl_shouna);
        rl_secai = (RelativeLayout) findViewById(R.id.rl_secai);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_info);
        rl_tos_choose = (RelativeLayout) findViewById(R.id.rl_tos_choose);
        rl_pic_change = (RelativeLayout) findViewById(R.id.rl_pic_change);
        iv_change_frag = (ImageView) findViewById(R.id.iv_change_frag);
        iv_chongzhi = (ImageView) findViewById(R.id.iv_chongzhi);
        iv_color_icon = (ImageView) findViewById(R.id.iv_color_icon);

    }

    @Override
    protected void initListener() {
        super.initListener();

        nav_left_imageButton.setOnClickListener(this);
        rl_kongjian.setOnClickListener(this);
        rl_jubu.setOnClickListener(this);
        rl_zhuangshi.setOnClickListener(this);
        rl_shouna.setOnClickListener(this);
        rl_secai.setOnClickListener(this);
        iv_change_frag.setOnClickListener(this);
        iv_chongzhi.setOnClickListener(this);

        tv_color_tital.setOnClickListener(this);
        iv_color_icon.setOnClickListener(this);
        bt_tag_page_item.setOnClickListener(this);

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (move_tag) {
                            mDownY = event.getY();
                            move_tag = false;
                        }
                        mMoveY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        move_tag = true;
                        mMoveY = event.getY();
                        Log.e("UP", "Y" + mMoveY);
                        if (Math.abs((mMoveY - mDownY)) > 20) {
                            if (mMoveY > mDownY) {
                                view_center.setVisibility(View.VISIBLE);
                                rl_tos_choose.setVisibility(View.VISIBLE);
                            } else {
                                view_center.setVisibility(View.GONE);
                                rl_tos_choose.setVisibility(View.GONE);
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("色彩");
        if (mSelectListData != null && mSelectListData.size() > 0) {
            iv_chongzhi.setVisibility(View.VISIBLE);
            bt_tag_page_item.setVisibility(View.VISIBLE);
            tv_color_tital.setVisibility(View.GONE);
            for (Integer key : mSelectListData.keySet()) {

                if(mSelectListData.get(key).getColor_value().equalsIgnoreCase("ffffff")){
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setCornerRadius(50);
                    drawable.setColor(Color.parseColor("#"+mSelectListData.get(key).getColor_value()));
                    drawable.setStroke(1,Color.parseColor("#262626"));
                    bt_tag_page_item.setBackgroundDrawable(drawable);
                    bt_tag_page_item.setTextColor(UIUtils.getColor(R.color.bg_262626));
                }else {

                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setCornerRadius(50);
                    drawable.setColor(Color.parseColor("#"+mSelectListData.get(key).getColor_value()));
                    bt_tag_page_item.setBackgroundDrawable(drawable);
                    bt_tag_page_item.setTextColor(UIUtils.getColor(R.color.white));
                }

                bt_tag_page_item.setText(mSelectListData.get(key).getColor_name());
            }
        }
        width_Pic_Staggered = PublicUtils.getScreenWidth(ColorShaiXuanActivity.this) / 2 - UIUtils.getDimens(R.dimen.font_20);
        width_Pic_List = PublicUtils.getScreenWidth(ColorShaiXuanActivity.this) - UIUtils.getDimens(R.dimen.font_14);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        buildRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ColorShaiXuanActivity.this.finish();
                break;
            case R.id.rl_kongjian:
                //友盟统计
                HashMap<String, String> map4 = new HashMap<String, String>();
                map4.put("evenname", "色彩页筛选");
                map4.put("even", "空间");
                MobclickAgent.onEvent(ColorShaiXuanActivity.this, "jtaction41", map4);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("空间")  //事件类别
                        .setAction("色彩页筛选")      //事件操作
                        .build());
                showPopwindow(R.id.rl_kongjian, 0);
                break;
            case R.id.rl_jubu:
                //友盟统计
                HashMap<String, String> map1 = new HashMap<String, String>();
                map1.put("evenname", "色彩页筛选");
                map1.put("even", "局部");
                MobclickAgent.onEvent(ColorShaiXuanActivity.this, "jtaction41", map1);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("局部")  //事件类别
                        .setAction("色彩页筛选")      //事件操作
                        .build());
                showPopwindow(R.id.rl_jubu, 1);
                break;
            case R.id.rl_zhuangshi:
                //友盟统计
                HashMap<String, String> map2 = new HashMap<String, String>();
                map2.put("evenname", "色彩页筛选");
                map2.put("even", "装饰");
                MobclickAgent.onEvent(ColorShaiXuanActivity.this, "jtaction41", map2);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("装饰")  //事件类别
                        .setAction("色彩页筛选")      //事件操作
                        .build());
                showPopwindow(R.id.rl_zhuangshi, 2);
                break;
            case R.id.rl_shouna:
                //友盟统计
                HashMap<String, String> map3 = new HashMap<String, String>();
                map3.put("evenname", "色彩页筛选");
                map3.put("even", "收纳");
                MobclickAgent.onEvent(ColorShaiXuanActivity.this, "jtaction41", map3);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("收纳")  //事件类别
                        .setAction("色彩页筛选")      //事件操作
                        .build());
                showPopwindow(R.id.rl_shouna, 3);
                break;
            case R.id.rl_secai:
                //友盟统计
                HashMap<String, String> map5 = new HashMap<String, String>();
                map5.put("evenname", "色彩页筛选");
                map5.put("even", "色彩");
                MobclickAgent.onEvent(ColorShaiXuanActivity.this, "jtaction41", map5);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("色彩")  //事件类别
                        .setAction("色彩页筛选")      //事件操作
                        .build());
                showPopwindow(R.id.rl_secai, 4);
                break;
            case R.id.iv_change_frag:
                if (curentListTag) {
                    mRecyclerView.setPadding(UIUtils.getDimens(R.dimen.font_6), 0, UIUtils.getDimens(R.dimen.font_6), 0);
                    iv_change_frag.setImageResource(R.drawable.changtu);
                    mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                    curentListTag = false;
                } else {
                    mRecyclerView.setPadding(0, 0, 0, 0);
                    iv_change_frag.setImageResource(R.drawable.pubuliu);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(ColorShaiXuanActivity.this));
                    curentListTag = true;
                }
                break;
            case R.id.iv_chongzhi:
                //友盟统计
                HashMap<String, String> map7 = new HashMap<String, String>();
                map7.put("evenname", "色彩清空");
                map7.put("even", "色彩页");
                MobclickAgent.onEvent(ColorShaiXuanActivity.this, "jtaction42", map7);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("色彩页")  //事件类别
                        .setAction("色彩清空")      //事件操作
                        .build());
                mSelectListData.clear();
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai1);
                bt_tag_page_item.setVisibility(View.GONE);
                iv_chongzhi.setVisibility(View.GONE);
                tv_color_tital.setVisibility(View.VISIBLE);
                onRefresh();
                break;
            case R.id.tv_color_tital:
            case R.id.iv_color_icon:
            case R.id.bt_tag_page_item:
                //友盟统计
                HashMap<String, String> map6 = new HashMap<String, String>();
                map6.put("evenname", "色彩块的点击");
                map6.put("even", "色彩页");
                MobclickAgent.onEvent(ColorShaiXuanActivity.this, "jtaction43", map6);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("色彩页")  //事件类别
                        .setAction("色彩块的点击")      //事件操作
                        .build());
                if (null == colorBean) {
                    getColorData();
                    ToastUtils.showCenter(ColorShaiXuanActivity.this, "色彩信息获取失败");
                } else {
                    if (selectColorPopupWindow == null) {
                        selectColorPopupWindow = new SelectColorSeCaiWindow(this, this, colorBean, this);
                    }
                    selectColorPopupWindow.setSelectColor(mSelectListData);
                    selectColorPopupWindow.showAtLocation(ColorShaiXuanActivity.this.findViewById(R.id.main),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                            0,
                            0); //设置layout在PopupWindow中显示的位置
                }
                break;
            case R.id.view_pop_top:
            case R.id.view_pop_bottom:
                selectColorPopupWindow.dismiss();
                //友盟统计
                HashMap<String, String> map8 = new HashMap<String, String>();
                map8.put("evenname", "色彩筛选返回");
                map8.put("even", "色彩页");
                MobclickAgent.onEvent(ColorShaiXuanActivity.this, "jtaction19", map8);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("色彩页")  //事件类别
                        .setAction("色彩筛选返回")      //事件操作
                        .build());
                break;

        }
    }

    private void buildRecyclerView() {

        MultiItemTypeSupport<SearchItemDataBean> support = new MultiItemTypeSupport<SearchItemDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_ONE) {
                    return R.layout.item_test_one;
                } else {
                    return R.layout.item_test_pic_pubu;
                }
            }

            @Override
            public int getItemViewType(int position, SearchItemDataBean s) {
                if (curentListTag) {
                    return TYPE_ONE;
                } else {
                    return TYPE_TWO;
                }
            }
        };

        mAdapter = new MultiItemCommonAdapter<SearchItemDataBean>(ColorShaiXuanActivity.this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {
                scroll_position = position;
                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();

//                layoutParams.width = (curentListTag ? width_Pic_List : width_Pic_Staggered);
                layoutParams.height = (curentListTag ? mLListDataHeight.get(position) : mSListDataHeight.get(position));
                holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                String nikeName = mListData.get(position).getUser_info().getNickname();

                if (nikeName != null && curentListTag && nikeName.length() > 8) {
                    nikeName = nikeName.substring(0, 8) + "...";
                }
                if (nikeName != null && !curentListTag && nikeName.length() > 5) {
                    nikeName = nikeName.substring(0, 5) + "...";
                }

                ((TextView) holder.getView(R.id.tv_name_pic)).setText(nikeName);
                if (curentListTag) {
                    ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg0(),
                            (ImageView) holder.getView(R.id.iv_imageview_one));
                } else {
                    ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg1(),
                            (ImageView) holder.getView(R.id.iv_imageview_one));

                }
                ImageUtils.displayFilletImage(mListData.get(position).getUser_info().getAvatar().getBig(),
                        (ImageView) holder.getView(R.id.iv_header_pic));

                holder.getView(R.id.iv_header_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ColorShaiXuanActivity.this, UserInfoActivity.class);
                        intent.putExtra(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                        startActivity(intent);
                    }
                });

                holder.getView(R.id.iv_imageview_one).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //查看单图详情
                        Intent intent = new Intent(ColorShaiXuanActivity.this, ImageDetailScrollActivity.class);
                        intent.putExtra("item_id", mListData.get(position).getItem_info().getItem_id());
                        intent.putExtra("position", position);
                        intent.putExtra("type", "色彩");
                        intent.putExtra("if_click_color", false);
                        intent.putExtra("mSelectListData", (Serializable) mSelectListData);
                        intent.putExtra("shaixuan_tag", "");
                        intent.putExtra("page_num", page_num + 1);
                        intent.putExtra("item_id_list", (Serializable) mItemIdList);
                        startActivity(intent);
                    }
                });

                holder.getView(R.id.tv_shoucang_num).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onShouCang(!mListData.get(position).getItem_info().getIs_collected().trim().equals("1"), position, mListData.get(position));
                    }
                });
                holder.getView(R.id.iv_if_shoucang).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onShouCang(!mListData.get(position).getItem_info().getIs_collected().trim().equals("1"), position, mListData.get(position));
                    }
                });

                if(mListData.get(position).getItem_info().getCollect_num().trim().equals("0")){
                    holder.getView(R.id.tv_shoucang_num).setVisibility(View.INVISIBLE);
                }else {
                    holder.getView(R.id.tv_shoucang_num).setVisibility(View.VISIBLE);
                }
                ((TextView) holder.getView(R.id.tv_shoucang_num)).setText(mListData.get(position).getItem_info().getCollect_num());
                if (!mListData.get(position).getItem_info().getIs_collected().equals("1")) {//未收藏
                    ((ImageView) holder.getView(R.id.iv_if_shoucang)).setImageResource(R.drawable.shoucang);
                } else {//收藏
                    ((ImageView) holder.getView(R.id.iv_if_shoucang)).setImageResource(R.drawable.shoucang1);
                }
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(ColorShaiXuanActivity.this));
        mRecyclerView.setItemAnimator(null);
//        mRecyclerView.addHeaderView(view);

        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    private void showPopwindow(int id, int position) {
        if (tagDataBean != null && colorBean != null) {
            if (null == homeTabPopWin) {
                homeTabPopWin = new HomeTabPopWin(this, this, tagDataBean, this, colorBean, mSelectListData);
            }
            homeTabPopWin.changeColor(mSelectListData);
            if (homeTabPopWin.isShowing()) {
                if (last_id != 0 && last_id == id) {
                    last_id = 0;
                    homeTabPopWin.dismiss();
                    iv_kongjian.setImageResource(R.drawable.kongjian1);
                    iv_jubu.setImageResource(R.drawable.jubu1);
                    iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                    iv_shouna.setImageResource(R.drawable.shouna1);
                    iv_secai.setImageResource(R.drawable.secai1);
                } else {
                    last_id = id;

                    homeTabPopWin.setPagePosition(position);
                    switch (position) {
                        case 0:
                            iv_kongjian.setImageResource(R.drawable.kongjian);
                            iv_jubu.setImageResource(R.drawable.jubu1);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                            iv_shouna.setImageResource(R.drawable.shouna1);
                            iv_secai.setImageResource(R.drawable.secai1);
                            break;
                        case 1:
                            iv_kongjian.setImageResource(R.drawable.kongjian1);
                            iv_jubu.setImageResource(R.drawable.jubu);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                            iv_shouna.setImageResource(R.drawable.shouna1);
                            iv_secai.setImageResource(R.drawable.secai1);
                            break;
                        case 2:
                            iv_kongjian.setImageResource(R.drawable.kongjian1);
                            iv_jubu.setImageResource(R.drawable.jubu1);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi);
                            iv_shouna.setImageResource(R.drawable.shouna1);
                            iv_secai.setImageResource(R.drawable.secai1);
                            break;
                        case 3:
                            iv_kongjian.setImageResource(R.drawable.kongjian1);
                            iv_jubu.setImageResource(R.drawable.jubu1);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                            iv_shouna.setImageResource(R.drawable.shouna);
                            iv_secai.setImageResource(R.drawable.secai1);
                            break;
                        case 4:
                            iv_kongjian.setImageResource(R.drawable.kongjian1);
                            iv_jubu.setImageResource(R.drawable.jubu1);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                            iv_shouna.setImageResource(R.drawable.shouna1);
                            iv_secai.setImageResource(R.drawable.secai);
                            break;
                    }
                }

            } else {

                homeTabPopWin.setPagePosition(position);
                last_id = id;
                if (Build.VERSION.SDK_INT < 24) {
                    homeTabPopWin.showAsDropDown(view_center);
                } else {
                    // 获取控件的位置，安卓系统>7.0
                    int[] location = new int[2];
                    view_center.getLocationOnScreen(location);
                    int screenHeight = PublicUtils.getScreenHeight(ColorShaiXuanActivity.this);
                    homeTabPopWin.setHeight(screenHeight - location[1]);
                    homeTabPopWin.showAtLocation(ll_pic_choose, Gravity.NO_GRAVITY, 0, location[1]);
                }

                switch (id) {
                    case R.id.rl_kongjian:
                        iv_kongjian.setImageResource(R.drawable.kongjian);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_jubu:
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_zhuangshi:
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_shouna:
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna);
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_secai:
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        iv_secai.setImageResource(R.drawable.secai);
                        break;
                }
            }
        } else {
            getTagData();
            getColorData();
            ToastUtils.showCenter(ColorShaiXuanActivity.this, "数据加载中");
        }
    }

    //获取tag空间等信息
    private void getTagData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ColorShaiXuanActivity.this, getString(R.string.filter_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        data_msg = "{ \"tag_id\": " + data_msg + "}";
                        Message msg = new Message();
                        msg.obj = data_msg;
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ColorShaiXuanActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ColorShaiXuanActivity.this, getString(R.string.filter_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getPicTagData(callBack);
    }

    //获取tag颜色信息
    private void getColorData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ColorShaiXuanActivity.this, getString(R.string.color_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        Message msg = new Message();
                        msg.obj = data_msg;
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ColorShaiXuanActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ColorShaiXuanActivity.this, getString(R.string.color_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getColorListData(callBack);

    }
    boolean ifClickShouCang = true;
    //收藏或者取消收藏，图片
    public void onShouCang(boolean ifShouCang, int position, SearchItemDataBean searchItemDataBean) {

        if(ifClickShouCang){
            ifClickShouCang = false;
            if (ifShouCang) {
                //友盟统计
                HashMap<String, String> map4 = new HashMap<String, String>();
                map4.put("evenname", "收藏图片");
                map4.put("even", "色彩页");
                MobclickAgent.onEvent(ColorShaiXuanActivity.this, "jtaction5", map4);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("色彩页")  //事件类别
                        .setAction("收藏图片")      //事件操作
                        .build());
                //未被收藏，去收藏
                addShouCang(position, searchItemDataBean.getItem_info().getItem_id());
            } else {
                //友盟统计
                HashMap<String, String> map4 = new HashMap<String, String>();
                map4.put("evenname", "取消收藏图片");
                map4.put("even", "色彩页");
                MobclickAgent.onEvent(ColorShaiXuanActivity.this, "jtaction6", map4);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("色彩页")  //事件类别
                        .setAction("取消收藏图片")      //事件操作
                        .build());
                //被收藏，去取消收藏
                removeShouCang(position, searchItemDataBean.getItem_info().getItem_id());
            }
        }
    }

    //收藏
    private void addShouCang(final int position, String item_id) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ifClickShouCang = true;
                ToastUtils.showCenter(ColorShaiXuanActivity.this, "收藏成功");
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
                        ToastUtils.showCenter(ColorShaiXuanActivity.this, "收藏成功");
                        mListData.get(position).getItem_info().setIs_collected("1");
                        try {
                            int collect_num = Integer.parseInt(mListData.get(position).getItem_info().getCollect_num().trim());
                            mListData.get(position).getItem_info().setCollect_num(++collect_num + "");
                        } catch (Exception e) {
                        }
                        mAdapter.notifyItemChanged(position);
                    } else {
                        ToastUtils.showCenter(ColorShaiXuanActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ColorShaiXuanActivity.this, "收藏失败");
                }
            }
        };
        MyHttpManager.getInstance().addShouCang(item_id, callBack);
    }

    //取消收藏
    private void removeShouCang(final int position, String item_id) {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ifClickShouCang = true;
                ToastUtils.showCenter(ColorShaiXuanActivity.this, "取消收藏失败");
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
                        ToastUtils.showCenter(ColorShaiXuanActivity.this, "取消收藏成功");
                        mListData.get(position).getItem_info().setIs_collected("0");
                        try {
                            int collect_num = Integer.parseInt(mListData.get(position).getItem_info().getCollect_num().trim());
                            mListData.get(position).getItem_info().setCollect_num(--collect_num + "");
                        } catch (Exception e) {
                        }
                        mAdapter.notifyItemChanged(position);
                    } else {
                        ToastUtils.showCenter(ColorShaiXuanActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ColorShaiXuanActivity.this, "取消收藏失败");
                }
            }
        };
        MyHttpManager.getInstance().removeShouCang(item_id, callBack);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                iv_kongjian.setImageResource(R.drawable.kongjian);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai1);
                break;
            case 1:
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai1);
                break;
            case 2:
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai1);
                break;
            case 3:
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna);
                iv_secai.setImageResource(R.drawable.secai1);
                break;
            case 4:
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDismiss() {
        if (homeTabPopWin != null) {
            homeTabPopWin.dismiss();
            iv_kongjian.setImageResource(R.drawable.kongjian1);
            iv_jubu.setImageResource(R.drawable.jubu1);
            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
            iv_shouna.setImageResource(R.drawable.shouna1);
            iv_secai.setImageResource(R.drawable.secai1);
        }
    }

    @Override
    public void onItemClick(String tagStr) {
        onDismiss();
        //跳转到筛选结果页
        Intent intent = new Intent(this, ShaiXuanResultActicity.class);
        intent.putExtra("shaixuan_tag", tagStr);
        intent.putExtra("islist", curentListTag);
        intent.putExtra("shaixuan_color", (Serializable) mSelectListData);
        startActivity(intent);
    }

    @Override
    public void onItemColorClick(ColorItemBean colorItemBean) {
        if (homeTabPopWin != null) {
            homeTabPopWin.dismiss();
            this.mColorClick = colorItemBean;

            if (mSelectListData.get(mColorClick.getColor_id()) == null) {
                mSelectListData.clear();
                mSelectListData.put(mColorClick.getColor_id(), mColorClick);
                homeTabPopWin.changeColor(mSelectListData);
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai1);

                if (mSelectListData != null && mSelectListData.size() > 0) {
                    bt_tag_page_item.setVisibility(View.VISIBLE);
                    iv_chongzhi.setVisibility(View.VISIBLE);
                    tv_color_tital.setVisibility(View.GONE);
                    for (Integer key : mSelectListData.keySet()) {
                        bt_tag_page_item.setText(mSelectListData.get(key).getColor_name());

                        if(mSelectListData.get(key).getColor_value().equalsIgnoreCase("ffffff")){
                            GradientDrawable drawable = new GradientDrawable();
                            drawable.setCornerRadius(50);
                            drawable.setColor(Color.parseColor("#"+mSelectListData.get(key).getColor_value()));
                            drawable.setStroke(1,Color.parseColor("#262626"));
                            bt_tag_page_item.setBackgroundDrawable(drawable);
                            bt_tag_page_item.setTextColor(UIUtils.getColor(R.color.bg_262626));
                        }else {

                            GradientDrawable drawable = new GradientDrawable();
                            drawable.setCornerRadius(50);
                            drawable.setColor(Color.parseColor("#"+mSelectListData.get(key).getColor_value()));
                            bt_tag_page_item.setBackgroundDrawable(drawable);
                            bt_tag_page_item.setTextColor(UIUtils.getColor(R.color.white));
                        }
                    }
                }
                onRefresh();
            } else {
                onClearColor();
            }

        }
    }

    @Override
    public void onClearColor() {
        if (homeTabPopWin != null) {
            homeTabPopWin.dismiss();
            homeTabPopWin.changeColor(mSelectListData);
        }
        mSelectListData.clear();
        iv_kongjian.setImageResource(R.drawable.kongjian1);
        iv_jubu.setImageResource(R.drawable.jubu1);
        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
        iv_shouna.setImageResource(R.drawable.shouna1);
        iv_secai.setImageResource(R.drawable.secai1);

        bt_tag_page_item.setVisibility(View.GONE);
        iv_chongzhi.setVisibility(View.GONE);
        tv_color_tital.setVisibility(View.VISIBLE);

        onRefresh();
    }

    @Override
    public void onRefresh() {

        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getListData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {

        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getListData(LOADMORE_STATUS);

    }

    private void getListData(final String state) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                if (state.equals(LOADMORE_STATUS)) {
                    --page_num;
                } else {
                    page_num = 1;
                }
                ToastUtils.showCenter(ColorShaiXuanActivity.this, getString(R.string.search_result_error));

            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        SearchDataBean searchDataBean = GsonUtil.jsonToBean(data_msg, SearchDataBean.class);
                        if (null != searchDataBean.getItem_list() && 0 != searchDataBean.getItem_list().size()) {
                            getHeight(searchDataBean.getItem_list(), state);
                            updateViewFromData(searchDataBean.getItem_list(), state);
                        } else {
                            updateViewFromData(null, state);
                        }
                    } else {
                        if (state.equals(LOADMORE_STATUS)) {
                            --page_num;
                            //没有更多数据
                            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                        } else {
                            page_num = 1;
                            mRecyclerView.setRefreshing(false);//刷新完毕
                        }
                        ToastUtils.showCenter(ColorShaiXuanActivity.this, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getSearchList(mSelectListData, "", "", (page_num - 1) * 20 + "", "20", callBack);
    }

    private void getHeight(List<SearchItemDataBean> item_list, String state) {
        if (state.equals(REFRESH_STATUS)) {
            mLListDataHeight.clear();
            mSListDataHeight.clear();
        }

        if (item_list.size() > 0) {
            for (int i = 0; i < item_list.size(); i++) {
                mLListDataHeight.add(Math.round(width_Pic_List / 1.333333f));
                if (item_list.get(i).getItem_info().getImage().getRatio() == 0) {
                    mSListDataHeight.add(width_Pic_Staggered);
                } else {
                    mSListDataHeight.add(Math.round(width_Pic_Staggered / item_list.get(i).getItem_info().getImage().getRatio()));
                }
            }
        }
    }

    private void updateViewFromData(List<SearchItemDataBean> listData, String state) {

        switch (state) {

            case REFRESH_STATUS:
                mListData.clear();
                mItemIdList.clear();
                if (null != listData && listData.size() > 0) {
                    mListData.addAll(listData);
                    for (int i = 0; i < listData.size(); i++) {
                        mItemIdList.add(listData.get(i).getItem_info().getItem_id());
                    }
                } else {
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                    page_num = 1;
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
                    for (int i = 0; i < listData.size(); i++) {
                        mItemIdList.add(listData.get(i).getItem_info().getItem_id());
                    }
                } else {
                    --page_num;
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

    @Override
    public void qingkong() {
        selectColorPopupWindow.dismiss();
        onClearColor();
        //友盟统计
        HashMap<String, String> map4 = new HashMap<String, String>();
        map4.put("evenname", "色彩筛选清空");
        map4.put("even", "色彩页");
        MobclickAgent.onEvent(ColorShaiXuanActivity.this, "jtaction20", map4);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("色彩页")  //事件类别
                .setAction("色彩筛选清空")      //事件操作
                .build());
    }

    @Override
    public void clickColor(ColorItemBean colorItemBean) {
        //友盟统计
        HashMap<String, String> map4 = new HashMap<String, String>();
        map4.put("evenname", "色彩单选");
        map4.put("even", "色彩页" + colorItemBean.getColor_name());
        MobclickAgent.onEvent(ColorShaiXuanActivity.this, "jtaction21", map4);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("色彩页" + colorItemBean.getColor_name())  //事件类别
                .setAction("色彩单选")      //事件操作
                .build());
        if (selectColorPopupWindow != null) {
            selectColorPopupWindow.dismiss();
            this.mColorClick = colorItemBean;

            if (mSelectListData.get(mColorClick.getColor_id()) == null) {
                mSelectListData.clear();
                mSelectListData.put(mColorClick.getColor_id(), mColorClick);
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai1);
                if (mSelectListData != null && mSelectListData.size() > 0) {
                    bt_tag_page_item.setVisibility(View.VISIBLE);
                    iv_chongzhi.setVisibility(View.VISIBLE);
                    tv_color_tital.setVisibility(View.GONE);
                    for (Integer key : mSelectListData.keySet()) {
                        bt_tag_page_item.setText(mSelectListData.get(key).getColor_name());
                        if(mSelectListData.get(key).getColor_value().equalsIgnoreCase("ffffff")){
                            GradientDrawable drawable = new GradientDrawable();
                            drawable.setCornerRadius(50);
                            drawable.setColor(Color.parseColor("#"+mSelectListData.get(key).getColor_value()));
                            drawable.setStroke(1,Color.parseColor("#262626"));
                            bt_tag_page_item.setBackgroundDrawable(drawable);
                            bt_tag_page_item.setTextColor(UIUtils.getColor(R.color.bg_262626));
                        }else {

                            GradientDrawable drawable = new GradientDrawable();
                            drawable.setCornerRadius(50);
                            drawable.setColor(Color.parseColor("#"+mSelectListData.get(key).getColor_value()));
                            bt_tag_page_item.setBackgroundDrawable(drawable);
                            bt_tag_page_item.setTextColor(UIUtils.getColor(R.color.white));
                        }
                    }
                }
                onRefresh();
            } else {
                qingkong();
            }

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("色彩页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("色彩页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageEnd("色彩页");
        MobclickAgent.onPause(this);
    }
}
