package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.activity.DesinerInfoHeaderActivity;
import com.homechart.app.home.activity.FenSiListActivity;
import com.homechart.app.home.activity.GuanZuListActivity;
import com.homechart.app.home.activity.ImageDetailScrollActivity;
import com.homechart.app.home.activity.LoginActivity;
import com.homechart.app.home.activity.UserInfoActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.userimage.ImageDataBean;
import com.homechart.app.home.bean.userimage.UserImageBean;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationBean;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationListBean;
import com.homechart.app.lingganji.ui.activity.InspirationDetailActivity;
import com.homechart.app.myview.RoundImageView;
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
import com.jaeger.library.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("ValidFragment")
public class NewUserInfoFragment
        extends BaseFragment
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener {


    public NewUserInfoFragment() {
    }

    public NewUserInfoFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_userinfo_info;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        mBundle = getArguments();
        user_id = (String) mBundle.getString(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initView() {

        headerView = LayoutInflater.from(activity).inflate(R.layout.header_userinfo_info, null);
        mIBBack = (ImageButton) rootView.findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) rootView.findViewById(R.id.tv_tital_comment);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_info);
        view_tiop = (View) rootView.findViewById(R.id.view_tiop);

        tv_jieshao = (TextView) headerView.findViewById(R.id.tv_jieshao);
        tv_userinfo_nikename = (TextView) headerView.findViewById(R.id.tv_userinfo_nikename);
        tv_info_pic_num = (TextView) headerView.findViewById(R.id.tv_info_pic_num);
        tv_info_guanzhu_num = (TextView) headerView.findViewById(R.id.tv_info_guanzhu_num);
        tv_info_shaijia_num = (TextView) headerView.findViewById(R.id.tv_info_shaijia_num);
        tv_info_fensi_num = (TextView) headerView.findViewById(R.id.tv_info_fensi_num);
        btn_guanzhu_demand = (TextView) headerView.findViewById(R.id.btn_guanzhu_demand);
        rl_info_pic = (RelativeLayout) headerView.findViewById(R.id.rl_info_pic);
        rl_info_zhunaye = (RelativeLayout) headerView.findViewById(R.id.rl_info_zhunaye);
        rl_info_guanzhu = (RelativeLayout) headerView.findViewById(R.id.rl_info_guanzhu);
        rl_info_shaijia = (RelativeLayout) headerView.findViewById(R.id.rl_info_shaijia);
        rl_info_fensi = (RelativeLayout) headerView.findViewById(R.id.rl_info_fensi);
        iv_info_renzheng = (ImageView) headerView.findViewById(R.id.iv_info_renzheng);
        iv_header_desiner_center = (RoundImageView) headerView.findViewById(R.id.iv_header_desiner_center);
        tl_tab = (TabLayout) headerView.findViewById(R.id.tl_tab);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mIBBack.setOnClickListener(this);
        rl_info_zhunaye.setOnClickListener(this);
        btn_guanzhu_demand.setOnClickListener(this);
        iv_header_desiner_center.setOnClickListener(this);
        rl_info_guanzhu.setOnClickListener(this);
        rl_info_shaijia.setOnClickListener(this);
        rl_info_shaijia.setOnClickListener(this);
        rl_info_fensi.setOnClickListener(this);
        rl_info_pic.setOnClickListener(this);
        tl_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                page_num = 1;
                mListData.clear();
                if (tab.getText().equals("灵感辑")) {
                    sort = "zhuanji";
                    mListDataImage.clear();
                    mListData.clear();
                    mAdapterImage.notifyDataSetChanged();
                    mRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
                    mRecyclerView.setAdapter(mAdapter);
                    onRefresh();
                } else if (tab.getText().equals("图片")) {
                    //友盟统计
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("evenname", "图片查看");
                    map.put("even", "个人主页");
                    MobclickAgent.onEvent(activity, "shijian41", map);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("个人主页")  //事件类别
                            .setAction("图片查看")      //事件操作
                            .build());
                    sort = "image";
                    mListData.clear();
                    mListDataImage.clear();
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                    mRecyclerView.setAdapter(mAdapterImage);
                    onRefresh();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager = new GridLayoutManager(activity, 2);
        widthPic = (PublicUtils.getScreenWidth(activity) - UIUtils.getDimens(R.dimen.font_3)) / 2 - UIUtils.getDimens(R.dimen.font_1);
        widthImage = (PublicUtils.getScreenWidth(activity) - UIUtils.getDimens(R.dimen.font_24)) / 2;
//        StatusBarUtil.setTranslucentForImageView(activity, 0, null);
//        int statusBarHeight = PublicUtils.getStatusBarHeight(activity);
//        ViewGroup.LayoutParams layoutParams = view_tiop.getLayoutParams();
//        layoutParams.width = PublicUtils.getScreenWidth(activity);
//        layoutParams.height = statusBarHeight;
//        view_tiop.setLayoutParams(layoutParams);

        mTVTital.setText("");
        tl_tab.setTabMode(TabLayout.MODE_FIXED);
        tl_tab.setSelectedTabIndicatorHeight(UIUtils.getDimens(R.dimen.font_3));
        tl_tab.setSelectedTabIndicatorColor(UIUtils.getColor(R.color.bg_e79056));
        tl_tab.addTab(tl_tab.newTab().setText("灵感辑"));
        tl_tab.addTab(tl_tab.newTab().setText("图片"));
        PublicUtils.setIndicator(tl_tab, UIUtils.getDimens(R.dimen.font_15), UIUtils.getDimens(R.dimen.font_15));
        getUserInfo();

        MultiItemTypeSupport<ImageDataBean> supportImage = new MultiItemTypeSupport<ImageDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_userinfo_image;
            }

            @Override
            public int getItemViewType(int position, ImageDataBean s) {
                return 0;
            }
        };
        mAdapterImage = new MultiItemCommonAdapter<ImageDataBean>(activity, mListDataImage, supportImage) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.iv_item_pic).getLayoutParams();
                layoutParams.height = (int) (widthImage / mListDataImage.get(position).getItem_info().getImage().getRatio());
                layoutParams.width = widthImage;
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                holder.getView(R.id.iv_item_pic).setLayoutParams(layoutParams);
                ImageUtils.displayFilletImage(mListDataImage.get(position).getItem_info().getImage().getImg1(), (ImageView) holder.getView(R.id.iv_item_pic));
                holder.getView(R.id.iv_item_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String> item_id_list = new ArrayList<>();
                        item_id_list.add(mListDataImage.get(position).getItem_info().getItem_id());
                        Intent intent = new Intent(activity, ImageDetailScrollActivity.class);
                        intent.putExtra("item_id", mListDataImage.get(position).getItem_info().getItem_id());
                        intent.putExtra("type", "single");
                        intent.putExtra("position", 0);
                        intent.putExtra("item_id_list", (Serializable) item_id_list);
                        startActivity(intent);

                    }
                });
            }
        };

        MultiItemTypeSupport<InspirationBean> support = new MultiItemTypeSupport<InspirationBean>() {
            @Override
            public int getLayoutId(int itemType) {

                if (itemType == 0) {
                    return R.layout.item_my_inspirationlist_left;
                } else {
                    return R.layout.item_my_inspirationlist_right;
                }
            }

            @Override
            public int getItemViewType(int position, InspirationBean s) {
                if (position % 2 == 0) {
                    return 0;
                } else {
                    return 1;
                }

            }
        };
        mAdapter = new MultiItemCommonAdapter<InspirationBean>(activity, mListData, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {

                ((TextView) holder.getView(R.id.tv_item_name)).setText(mListData.get(position).getAlbum_info().getAlbum_name());
                ((TextView) holder.getView(R.id.tv_item_num_pic)).setText(mListData.get(position).getAlbum_info().getItem_num() + "张");
                ((TextView) holder.getView(R.id.tv_item_num_dingyue)).setText(mListData.get(position).getAlbum_info().getSubscribe_num() + "收藏");
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.iv_item_pic).getLayoutParams();
                layoutParams.height = widthPic;
                layoutParams.width = widthPic;
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                holder.getView(R.id.iv_item_pic).setLayoutParams(layoutParams);
                if (TextUtils.isEmpty(mListData.get(position).getAlbum_info().getCover_image().getImg0())) {
                    GlideImgManager.glideLoader(activity, "", R.drawable.moren, R.drawable.moren, (ImageView) holder.getView(R.id.iv_item_pic));
                } else {
                    ImageUtils.displayFilletHalfImage(mListData.get(position).getAlbum_info().getCover_image().getImg0(), (ImageView) holder.getView(R.id.iv_item_pic));
                }
                holder.getView(R.id.rl_item_inspiration).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, InspirationDetailActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("ifHideEdit", true);
                        intent.putExtra("album_id", mListData.get(position).getAlbum_info().getAlbum_id());
                        startActivityForResult(intent, 2);
                    }
                });
            }
        };

        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.addHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);

        onRefresh();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.rl_info_zhunaye:
                //TODO 跳转专业用户资料页
                Intent intent = new Intent(activity, DesinerInfoHeaderActivity.class);
                intent.putExtra("info", userCenterInfoBean);
                startActivity(intent);

                break;
            case R.id.btn_guanzhu_demand:


                loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
                if (!loginStatus) {
                    first = true;
                    Intent intent1 = new Intent(activity, LoginActivity.class);
                    startActivityForResult(intent1, 3);
                } else {
                    if (userCenterInfoBean != null) {
                        if (userCenterInfoBean.getUser_info().getRelation().equals("0")) {//未关注（去关注）
                            getGuanZhu();
                        } else if (userCenterInfoBean.getUser_info().getRelation().equals("1")) {//已关注
                            getQuXiao();
                        } else if (userCenterInfoBean.getUser_info().getRelation().equals("2")) {//相互关注
                            getQuXiao();
                        }
                    } else {
                        ToastUtils.showCenter(activity, "个人信息获取失败");
                    }
                }
                break;
            case R.id.iv_header_desiner_center:

                break;
            case R.id.rl_info_guanzhu:
                if (!TextUtils.isEmpty(user_id)) {
                    Intent intent_guanzu = new Intent(activity, GuanZuListActivity.class);
                    intent_guanzu.putExtra(ClassConstant.LoginSucces.USER_ID, user_id);
                    startActivity(intent_guanzu);
                }
                break;
            case R.id.rl_info_pic:
                if (sort.equals("zhuanji")) {
                    tl_tab.setScrollPosition(1, 0, true);
                    sort = "image";
                    mListData.clear();
                    mListDataImage.clear();
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                    mRecyclerView.setAdapter(mAdapterImage);
                    onRefresh();
                }
                break;
            case R.id.rl_info_shaijia:
                if (sort.equals("image")) {
                    tl_tab.setScrollPosition(0, 0, true);
                    page_num = 1;
                    mListData.clear();
                    sort = "zhuanji";
                    mListDataImage.clear();
                    mListData.clear();
                    mAdapterImage.notifyDataSetChanged();
                    mRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
                    mRecyclerView.setAdapter(mAdapter);
                    onRefresh();
                }
                break;
            case R.id.rl_info_fensi:
                if (!TextUtils.isEmpty(user_id)) {
                    Intent intent_fensi = new Intent(activity, FenSiListActivity.class);
                    intent_fensi.putExtra(ClassConstant.LoginSucces.USER_ID, user_id);
                    startActivity(intent_fensi);
                }
                break;
        }

    }

    //关注用户
    private void getGuanZhu() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (userCenterInfoBean.getUser_info().getRelation().equals("0")) {//未关注（去关注）
                    ToastUtils.showCenter(activity, "关注失败");
                } else if (userCenterInfoBean.getUser_info().getRelation().equals("1")) {//已关注
                    ToastUtils.showCenter(activity, "取消关注失败");
                } else if (userCenterInfoBean.getUser_info().getRelation().equals("0")) {//相互关注
                    ToastUtils.showCenter(activity, "取消关注失败");
                }
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        getUserInfo();
                    } else {
                        ToastUtils.showCenter(activity, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().goGuanZhu(user_id, callBack);

    }

    //取消关注用户
    private void getQuXiao() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtils.showCenter(activity, getString(R.string.userinfo_get_error));

            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {

                        getUserInfo();

                    } else {

                        ToastUtils.showCenter(activity, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().goQuXiaoGuanZhu(user_id, callBack);
    }

    //获取用户信息
    private void getUserInfo() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtils.showCenter(activity, getString(R.string.userinfo_get_error));

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
                        handler.sendMessage(msg);

                    } else {

                        ToastUtils.showCenter(activity, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getUserInfo(user_id, callBack);
    }

    private void changeUI() {

        if (userCenterInfoBean == null) {
            getUserInfo();
        } else {
            ImageUtils.displayRoundImage(userCenterInfoBean.getUser_info().getAvatar().getBig(), iv_header_desiner_center);
            tv_userinfo_nikename.setText(userCenterInfoBean.getUser_info().getNickname());
            tv_info_guanzhu_num.setText(userCenterInfoBean.getCounter().getFollow_num() + "");
            tv_info_pic_num.setText(userCenterInfoBean.getCounter().getItem_num() + "");
            tv_jieshao.setText(userCenterInfoBean.getUser_info().getSlogan());
            tv_info_shaijia_num.setText(userCenterInfoBean.getCounter().getAlbum_num() + "");
            tv_info_fensi_num.setText(userCenterInfoBean.getCounter().getFans_num() + "");

            if (!userCenterInfoBean.getUser_info().getProfession().trim().equals("0")) {//专业用户
                iv_info_renzheng.setVisibility(View.VISIBLE);
                rl_info_zhunaye.setVisibility(View.VISIBLE);
            } else {
                iv_info_renzheng.setVisibility(View.GONE);
                rl_info_zhunaye.setVisibility(View.GONE);
            }

            if (userCenterInfoBean.getUser_info().getRelation().equals("0")) {//未关注
                btn_guanzhu_demand.setVisibility(View.VISIBLE);
                btn_guanzhu_demand.setBackgroundResource(R.drawable.bt_guanzu1);
                btn_guanzhu_demand.setTextColor(UIUtils.getColor(R.color.white));
                btn_guanzhu_demand.setText("关注Ta");
                if (first) {
                    first = false;
                } else {
                    ToastUtils.showCenter(activity, "已取消关注");
                }

            } else if (userCenterInfoBean.getUser_info().getRelation().equals("1")) {//已关注
                btn_guanzhu_demand.setVisibility(View.VISIBLE);
                btn_guanzhu_demand.setBackgroundResource(R.drawable.bt_guanzhu1);
                btn_guanzhu_demand.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                btn_guanzhu_demand.setText("已关注");
                if (first) {
                    first = false;
                } else {
                    ToastUtils.showCenter(activity, "关注成功");
                }

            } else if (userCenterInfoBean.getUser_info().getRelation().equals("2")) {//互相关注
                btn_guanzhu_demand.setVisibility(View.VISIBLE);
                btn_guanzhu_demand.setBackgroundResource(R.drawable.bt_guanzhu1);
                btn_guanzhu_demand.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                btn_guanzhu_demand.setText("互相关注");
                if (first) {
                    first = false;
                } else {
                    ToastUtils.showCenter(activity, "关注成功");
                }
            }

        }

    }

    @Override
    public void onRefresh() {
        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        if (sort.equals("zhuanji")) {
            getInspirationsData(REFRESH_STATUS);
        } else if (sort.equals("image")) {
            getImageByUserId(REFRESH_STATUS);
        }
    }

    @Override
    public void onLoadMore() {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        if (sort.equals("zhuanji")) {
            getInspirationsData(LOADMORE_STATUS);
        } else if (sort.equals("image")) {
            //友盟统计
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("evenname", "图片查看加载");
            map.put("even", "个人主页");
            MobclickAgent.onEvent(activity, "shijian42", map);
            //ga统计
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("个人主页")  //事件类别
                    .setAction("图片查看加载")      //事件操作
                    .build());
            getImageByUserId(LOADMORE_STATUS);
        }
    }

    private void getInspirationsData(final String state) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if (state.equals(LOADMORE_STATUS)) {
                    --page_num;
                } else {
                    page_num = 1;
                }
                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                ToastUtils.showCenter(activity, "专辑列表获取失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        InspirationListBean inspirationListBean = GsonUtil.jsonToBean(data_msg, InspirationListBean.class);
                        if (null != inspirationListBean.getAlbum_list() && 0 != inspirationListBean.getAlbum_list().size()) {
                            updateViewFromData(inspirationListBean.getAlbum_list(), state);
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
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getUserInspirationSeries(user_id, (page_num - 1) * 20 + "", "20", callBack);
    }

    private void getImageByUserId(final String state) {
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
                        String newData = "{\"data\": " + data_msg + "}";
                        UserImageBean userImageBean = GsonUtil.jsonToBean(newData, UserImageBean.class);
                        if (null != userImageBean.getData().getItem_list() && 0 != userImageBean.getData().getItem_list().size()) {
                            updateViewFromDataImage(userImageBean.getData().getItem_list(), state);
                        } else {
                            updateViewFromDataImage(null, state);
                        }
                    } else {
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getImageByUserId(user_id, (page_num - 1) * 20 + "", "20", callBack);
    }

    private void updateViewFromData(List<InspirationBean> listData, String state) {

        switch (state) {
            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData && listData.size() > 0) {
                    mListData.addAll(listData);
                } else {
                    if (mListData.size() == 0) {
                        ((TextView) mRecyclerView.getLoadMoreFooterView().findViewById(R.id.tvTheEnd)).setText("暂无内容");
                    } else {
                        ((TextView) mRecyclerView.getLoadMoreFooterView().findViewById(R.id.tvTheEnd)).setText("已经到底啦");
                    }
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
                } else {
                    if (mListData.size() == 0) {
                        ((TextView) mRecyclerView.getLoadMoreFooterView().findViewById(R.id.tvTheEnd)).setText("暂无内容");
                    } else {
                        ((TextView) mRecyclerView.getLoadMoreFooterView().findViewById(R.id.tvTheEnd)).setText("已经到底啦");
                    }
                    --page_num;
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

    private void updateViewFromDataImage(List<ImageDataBean> listData, String state) {


        switch (state) {
            case REFRESH_STATUS:
                mListDataImage.clear();
                if (null != listData && listData.size() > 0) {
                    mListDataImage.addAll(listData);
                } else {
                    if (mListDataImage.size() == 0) {
                        ((TextView) mRecyclerView.getLoadMoreFooterView().findViewById(R.id.tvTheEnd)).setText("暂无内容");
                    } else {
                        ((TextView) mRecyclerView.getLoadMoreFooterView().findViewById(R.id.tvTheEnd)).setText("已经到底啦");
                    }
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                    page_num = 1;
                    mListDataImage.clear();
                }
                mAdapterImage.notifyDataSetChanged();
                mRecyclerView.setRefreshing(false);//刷新完毕
                break;

            case LOADMORE_STATUS:
                if (null != listData && listData.size() > 0) {
                    position = mListDataImage.size();
                    mListDataImage.addAll(listData);
                    mAdapterImage.notifyItem(position, mListDataImage, listData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                } else {
                    if (mListDataImage.size() == 0) {
                        ((TextView) mRecyclerView.getLoadMoreFooterView().findViewById(R.id.tvTheEnd)).setText("暂无内容");
                    } else {
                        ((TextView) mRecyclerView.getLoadMoreFooterView().findViewById(R.id.tvTheEnd)).setText("已经到底啦");
                    }
                    --page_num;
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(activity);
        MobclickAgent.onPageStart("个人主页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("个人主页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("个人主页");
        MobclickAgent.onPause(activity);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String info = (String) msg.obj;
            userCenterInfoBean = GsonUtil.jsonToBean(info, UserCenterInfoBean.class);
            changeUI();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            onRefresh();
        } else if (requestCode == 2 && resultCode == 2) {
            onRefresh();
        } else if (requestCode == 3) {
            getUserInfo();
        }
    }

    private UserCenterInfoBean userCenterInfoBean;
    private ImageButton mIBBack;
    private TextView mTVTital;
    private String user_id;
    private int position;
    private RoundImageView iv_header_desiner_center;
    private ImageView iv_info_renzheng;
    private RelativeLayout rl_info_zhunaye;
    private TextView tv_userinfo_nikename;
    private TextView tv_info_guanzhu_num;
    private TextView tv_info_shaijia_num;
    private TextView tv_info_fensi_num;
    private TextView btn_guanzhu_demand;
    private RelativeLayout rl_info_guanzhu;
    private RelativeLayout rl_info_shaijia;
    private RelativeLayout rl_info_fensi;
    private MultiItemCommonAdapter<InspirationBean> mAdapter;
    private MultiItemCommonAdapter<ImageDataBean> mAdapterImage;
    private List<InspirationBean> mListData = new ArrayList<>();
    private List<ImageDataBean> mListDataImage = new ArrayList<>();
    private HRecyclerView mRecyclerView;
    private LoadMoreFooterView mLoadMoreFooterView;
    private int page_num = 1;
    private View headerView;
    private boolean first = true;
    private View view_tiop;
    private TextView tv_jieshao;
    private int widthPic;
    private int widthImage;

    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private boolean loginStatus;
    private TabLayout tl_tab;
    private String sort = "zhuanji";
    private GridLayoutManager gridLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private TextView tv_info_pic_num;
    private RelativeLayout rl_info_pic;

    private FragmentManager fragmentManager;
    private Bundle mBundle;

}