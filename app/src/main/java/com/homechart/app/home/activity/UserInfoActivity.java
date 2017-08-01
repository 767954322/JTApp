package com.homechart.app.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.searchartile.ArticleBean;
import com.homechart.app.home.bean.searchartile.ArticleListBean;
import com.homechart.app.home.bean.shaijia.ShaiJiaBean;
import com.homechart.app.home.bean.shaijia.ShaiJiaItemBean;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.jaeger.library.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gumenghao on 17/6/11.
 */

public class UserInfoActivity
        extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener {

    private List<ShaiJiaItemBean> mListData = new ArrayList<>();
    private List<ArticleBean> mListDataArticle = new ArrayList<>();
    private List<Integer> mListDataHeight = new ArrayList<>();
    private List<Integer> mListSeeNumArticle = new ArrayList<>();
    private UserCenterInfoBean userCenterInfoBean;
    private ImageButton mIBBack;
    private TextView mTVTital;
    private String user_id;
    private int width_Pic;
    private int position;
    private RoundImageView iv_header_desiner_center;
    private ImageView iv_info_renzheng;
    private RelativeLayout rl_info_zhunaye;
    private TextView tv_userinfo_nikename;
    private TextView tv_info_guanzhu_num;
    private TextView tv_info_shaijia_num;
    private TextView tv_info_fensi_num;
    private Button btn_guanzhu_demand;
    private RelativeLayout rl_info_guanzhu;
    private RelativeLayout rl_info_shaijia;
    private RelativeLayout rl_info_fensi;
    private MultiItemCommonAdapter<ShaiJiaItemBean> mAdapter;
    private HRecyclerView mRecyclerView;
    private LoadMoreFooterView mLoadMoreFooterView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int page_num = 0;
    private int TYPE_LEFT = 1;
    private int TYPE_RIGHT = 2;
    private View headerView;
    private boolean first = true;
    private View view_tiop;
    private TabLayout tl_tab;
    private LinearLayoutManager linearLayoutManager;
    private String sort = "pic";
    private CommonAdapter<ArticleBean> mAdapterArticle;
    private TextView tv_jieshao;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_userinfo_info;
    }

    @Override
    protected void initView() {

        headerView = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.header_userinfo_info, null);
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_info);
        view_tiop = (View) findViewById(R.id.view_tiop);

        tv_jieshao = (TextView) headerView.findViewById(R.id.tv_jieshao);
        tv_userinfo_nikename = (TextView) headerView.findViewById(R.id.tv_userinfo_nikename);
        tv_info_guanzhu_num = (TextView) headerView.findViewById(R.id.tv_info_guanzhu_num);
        tv_info_shaijia_num = (TextView) headerView.findViewById(R.id.tv_info_shaijia_num);
        tv_info_fensi_num = (TextView) headerView.findViewById(R.id.tv_info_fensi_num);
        btn_guanzhu_demand = (Button) headerView.findViewById(R.id.btn_guanzhu_demand);
        rl_info_zhunaye = (RelativeLayout) headerView.findViewById(R.id.rl_info_zhunaye);
        rl_info_guanzhu = (RelativeLayout) headerView.findViewById(R.id.rl_info_guanzhu);
        rl_info_shaijia = (RelativeLayout) headerView.findViewById(R.id.rl_info_shaijia);
        rl_info_fensi = (RelativeLayout) headerView.findViewById(R.id.rl_info_fensi);
        iv_info_renzheng = (ImageView) headerView.findViewById(R.id.iv_info_renzheng);
        iv_header_desiner_center = (RoundImageView) headerView.findViewById(R.id.iv_header_desiner_center);
        tl_tab = (TabLayout) headerView.findViewById(R.id.tl_tab);
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        user_id = (String) getIntent().getSerializableExtra(ClassConstant.LoginSucces.USER_ID);
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
        rl_info_fensi.setOnClickListener(this);

        tl_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                page_num = 0;
                mListData.clear();
                mListDataHeight.clear();
//                mAdapter.notifyDataSetChanged();
                if (tab.getText().equals("图片")) {
                    sort = "pic";
                    mListData.clear();
                    mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    getListData(sort);
                } else if (tab.getText().equals("文章")) {
                    sort = "artical";
                    mListDataArticle.clear();
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mRecyclerView.setAdapter(mAdapterArticle);
                    getListDataArticle(sort);
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
        linearLayoutManager = new LinearLayoutManager(this);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
        int statusBarHeight = PublicUtils.getStatusBarHeight(this);
        ViewGroup.LayoutParams layoutParams = view_tiop.getLayoutParams();
        layoutParams.width = PublicUtils.getScreenWidth(this);
        layoutParams.height = statusBarHeight;
        view_tiop.setLayoutParams(layoutParams);

        mTVTital.setText("");
        tl_tab.setTabMode(TabLayout.MODE_FIXED);
        tl_tab.setSelectedTabIndicatorHeight(UIUtils.getDimens(R.dimen.font_3));
        tl_tab.setSelectedTabIndicatorColor(UIUtils.getColor(R.color.bg_e79056));
        tl_tab.addTab(tl_tab.newTab().setText("图片"));
        tl_tab.addTab(tl_tab.newTab().setText("文章"));
        PublicUtils.setIndicator(tl_tab, UIUtils.getDimens(R.dimen.font_15), UIUtils.getDimens(R.dimen.font_15));

        width_Pic = PublicUtils.getScreenWidth(UserInfoActivity.this) / 2 - UIUtils.getDimens(R.dimen.font_14);
        getUserInfo();

        MultiItemTypeSupport<ArticleBean> supportArticle = new MultiItemTypeSupport<ArticleBean>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_search_article;
            }

            @Override
            public int getItemViewType(int position, ArticleBean s) {
                return 0;
            }
        };
        mAdapterArticle = new MultiItemCommonAdapter<ArticleBean>(UserInfoActivity.this, mListDataArticle, supportArticle) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {

                ((TextView) holder.getView(R.id.tv_article_name)).setText(mListDataArticle.get(position).getArticle_info().getTitle());
                ((TextView) holder.getView(R.id.tv_article_details)).setText(mListDataArticle.get(position).getArticle_info().getSummary());
                try {
                    ((TextView) holder.getView(R.id.tv_youlan_num)).setText(mListSeeNumArticle.get(position) + "");
                } catch (Exception e) {
                    ((TextView) holder.getView(R.id.tv_youlan_num)).setText(0 + "");
                }

                ImageUtils.disRectangleImage(mListDataArticle.get(position).getArticle_info().getImage().getImg0(), (ImageView) holder.getView(R.id.iv_article_image));
                holder.getView(R.id.rl_item_click).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            mListSeeNumArticle.add(position, mListSeeNumArticle.get(position) + 1);
                        }catch (Exception e){
                        }
                        Intent intent = new Intent(UserInfoActivity.this, ArticleDetailsActivity.class);
                        intent.putExtra("article_id", mListDataArticle.get(position).getArticle_info().getArticle_id());
                        startActivity(intent);
                    }
                });
            }
        };


        MultiItemTypeSupport<ShaiJiaItemBean> support = new MultiItemTypeSupport<ShaiJiaItemBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_LEFT) {
                    return R.layout.item_userinfo_left;
                } else {
                    return R.layout.item_userinfo_right;
                }
            }

            @Override
            public int getItemViewType(int position, ShaiJiaItemBean itemMessageBean) {
                if (position % 2 == 0) {
                    return TYPE_LEFT;
                } else {
                    return TYPE_RIGHT;
                }

            }
        };

        mAdapter = new MultiItemCommonAdapter<ShaiJiaItemBean>(this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {
                String item_id = mListData.get(position).getItem_info().getItem_id();
                ((TextView) holder.getView(R.id.tv_shoucang_num)).setText(mListData.get(position).getItem_info().getCollect_num());

                if (mListData.get(position).getItem_info().getIs_collected().equals("0")) {//未被收藏
                    ((ImageView) holder.getView(R.id.iv_if_shoucang)).setImageResource(R.drawable.shoucang);
                } else {//收藏
                    ((ImageView) holder.getView(R.id.iv_if_shoucang)).setImageResource(R.drawable.shoucang1);
                }

                if (item_id.equals(holder.getView(R.id.iv_shoucang_image).getTag())) {
                } else {
                    holder.getView(R.id.iv_shoucang_image).setTag(item_id);
                    ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_shoucang_image).getLayoutParams();
                    layoutParams.width = width_Pic;
                    layoutParams.height = mListDataHeight.get(position);
                    holder.getView(R.id.iv_shoucang_image).setLayoutParams(layoutParams);
                    ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg1(),
                            (ImageView) holder.getView(R.id.iv_shoucang_image));
                }

                holder.getView(R.id.iv_shoucang_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //查看单图详情
                        Intent intent = new Intent(UserInfoActivity.this, ImageDetailLongActivity.class);
                        intent.putExtra("item_id", mListData.get(position).getItem_info().getItem_id());
                        startActivity(intent);
                    }
                });

                holder.getView(R.id.tv_shoucang_num).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //收藏
                        onShouCang(mListData.get(position).getItem_info().getIs_collected().equals("0"), position, mListData.get(position));

                    }
                });
                holder.getView(R.id.iv_if_shoucang).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //取消收藏
                        onShouCang(mListData.get(position).getItem_info().getIs_collected().equals("0"), position, mListData.get(position));
                    }
                });

            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.addHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);

        getListData(sort);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                UserInfoActivity.this.finish();
                break;
            case R.id.rl_info_zhunaye:

                //友盟统计
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("evenname", "专业用户资料");
                map.put("even", "点击专业用户资料查看");
                MobclickAgent.onEvent(UserInfoActivity.this, "action81", map);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击专业用户资料查看")  //事件类别
                        .setAction("专业用户资料")      //事件操作
                        .build());
                //TODO 跳转专业用户资料页
                Intent intent = new Intent(UserInfoActivity.this, DesinerInfoHeaderActivity.class);
                intent.putExtra("info", userCenterInfoBean);
                startActivity(intent);

                break;
            case R.id.btn_guanzhu_demand:

                if (userCenterInfoBean != null) {
                    if (userCenterInfoBean.getUser_info().getRelation().equals("0")) {//未关注（去关注）
                        getGuanZhu();
                    } else if (userCenterInfoBean.getUser_info().getRelation().equals("1")) {//已关注
                        getQuXiao();
                    } else if (userCenterInfoBean.getUser_info().getRelation().equals("2")) {//相互关注
                        getQuXiao();
                    }
                } else {
                    ToastUtils.showCenter(UserInfoActivity.this, "个人信息获取失败");
                }

                break;
            case R.id.iv_header_desiner_center:
                //友盟统计
                HashMap<String, String> map1 = new HashMap<String, String>();
                map1.put("evenname", "主页头像");
                map1.put("even", "点击个人主页头像");
                MobclickAgent.onEvent(UserInfoActivity.this, "action80", map1);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击个人主页头像")  //事件类别
                        .setAction("主页头像")      //事件操作
                        .build());
                break;
            case R.id.rl_info_guanzhu:
                if (!TextUtils.isEmpty(user_id)) {
                    Intent intent_guanzu = new Intent(UserInfoActivity.this, GuanZuListActivity.class);
                    intent_guanzu.putExtra(ClassConstant.LoginSucces.USER_ID, user_id);
                    startActivity(intent_guanzu);
                }
                break;
            case R.id.rl_info_shaijia:
                if (!TextUtils.isEmpty(user_id)) {
                    Intent intent_shaijia = new Intent(UserInfoActivity.this, ShaiJiaListActivity.class);
                    intent_shaijia.putExtra(ClassConstant.LoginSucces.USER_ID, user_id);
                    startActivity(intent_shaijia);
                }
                break;
            case R.id.rl_info_fensi:
                if (!TextUtils.isEmpty(user_id)) {
                    Intent intent_fensi = new Intent(UserInfoActivity.this, FenSiListActivity.class);
                    intent_fensi.putExtra(ClassConstant.LoginSucces.USER_ID, user_id);
                    startActivity(intent_fensi);
                }
                break;
        }

    }

    //收藏或者取消收藏，图片
    public void onShouCang(boolean ifShouCang, int position, ShaiJiaItemBean shaiJiaItemBean) {

        if (ifShouCang) {
            //未被收藏，去收藏
            addShouCang(position, shaiJiaItemBean.getItem_info().getItem_id());
        } else {
            //被收藏，去取消收藏
            removeShouCang(position, shaiJiaItemBean.getItem_info().getItem_id());
        }

    }

    //收藏
    private void addShouCang(final int position, String item_id) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(UserInfoActivity.this, "收藏成功");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        mListData.get(position).getItem_info().setIs_collected("1");
                        int num = Integer.parseInt(mListData.get(position).getItem_info().getCollect_num().trim());
                        mListData.get(position).getItem_info().setCollect_num((++num) + "");
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showCenter(UserInfoActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(UserInfoActivity.this, "收藏失败");
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
                ToastUtils.showCenter(UserInfoActivity.this, "取消收藏失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        mListData.get(position).getItem_info().setIs_collected("0");
                        int num = Integer.parseInt(mListData.get(position).getItem_info().getCollect_num().trim());
                        mListData.get(position).getItem_info().setCollect_num((--num) + "");
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showCenter(UserInfoActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(UserInfoActivity.this, "取消收藏失败");
                }
            }
        };
        MyHttpManager.getInstance().removeShouCang(item_id, callBack);
    }

    //关注用户
    private void getGuanZhu() {

        //友盟统计
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("evenname", "主页关注");
        map.put("even", "点击个人主页的关注按钮");
        MobclickAgent.onEvent(UserInfoActivity.this, "action82", map);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("点击个人主页的关注按钮")  //事件类别
                .setAction("主页关注")      //事件操作
                .build());
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (userCenterInfoBean.getUser_info().getRelation().equals("0")) {//未关注（去关注）
                    ToastUtils.showCenter(UserInfoActivity.this, "关注失败");
                } else if (userCenterInfoBean.getUser_info().getRelation().equals("1")) {//已关注
                    ToastUtils.showCenter(UserInfoActivity.this, "取消关注失败");
                } else if (userCenterInfoBean.getUser_info().getRelation().equals("0")) {//相互关注
                    ToastUtils.showCenter(UserInfoActivity.this, "取消关注失败");
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
                        ToastUtils.showCenter(UserInfoActivity.this, error_msg);

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

                ToastUtils.showCenter(UserInfoActivity.this, getString(R.string.userinfo_get_error));

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

                        ToastUtils.showCenter(UserInfoActivity.this, error_msg);

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

                ToastUtils.showCenter(UserInfoActivity.this, getString(R.string.userinfo_get_error));

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

                        ToastUtils.showCenter(UserInfoActivity.this, error_msg);

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
            tv_jieshao.setText(userCenterInfoBean.getUser_info().getSlogan());
            int num1 = userCenterInfoBean.getCounter().getSingle_num();
            int num2 = userCenterInfoBean.getCounter().getArticle_num();
            tv_info_shaijia_num.setText((num1 + num2) + "");
            tv_info_fensi_num.setText(userCenterInfoBean.getCounter().getFans_num() + "");

            if (!userCenterInfoBean.getUser_info().getProfession().trim().equals("0")) {//专业用户
                iv_info_renzheng.setVisibility(View.VISIBLE);
                rl_info_zhunaye.setVisibility(View.VISIBLE);
            } else {
                iv_info_renzheng.setVisibility(View.INVISIBLE);
                rl_info_zhunaye.setVisibility(View.INVISIBLE);
            }

            if (userCenterInfoBean.getUser_info().getRelation().equals("0")) {//未关注

                btn_guanzhu_demand.setBackgroundResource(R.drawable.bt_guanzu);
                btn_guanzhu_demand.setTextColor(UIUtils.getColor(R.color.white));
                btn_guanzhu_demand.setText("关注Ta");
                if (first) {
                    first = false;
                } else {
                    ToastUtils.showCenter(UserInfoActivity.this, "已取消关注");
                }

            } else if (userCenterInfoBean.getUser_info().getRelation().equals("1")) {//已关注
                btn_guanzhu_demand.setBackgroundResource(R.drawable.bt_guanzhu);
                btn_guanzhu_demand.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                btn_guanzhu_demand.setText("已关注");
                if (first) {
                    first = false;
                } else {
                    ToastUtils.showCenter(UserInfoActivity.this, "关注成功");
                }

            } else if (userCenterInfoBean.getUser_info().getRelation().equals("2")) {//互相关注
                btn_guanzhu_demand.setBackgroundResource(R.drawable.bt_guanzhu);
                btn_guanzhu_demand.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                btn_guanzhu_demand.setText("互相关注");
                if (first) {
                    first = false;
                } else {
                    ToastUtils.showCenter(UserInfoActivity.this, "关注成功");
                }
            }

        }

    }

    @Override
    public void onLoadMore() {

        if (sort.equals("pic")) {
            getListData(sort);
        } else if (sort.equals("artical")) {
            getListDataArticle(sort);
        }


    }

    private void getListData(String tag) {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                ToastUtils.showCenter(UserInfoActivity.this, UIUtils.getString(R.string.error_shaijia));
                --page_num;
            }

            @Override
            public void onResponse(String response) {
                try {
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                        String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                        String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                        if (error_code == 0) {
                            ShaiJiaBean shouCangBean = GsonUtil.jsonToBean(data_msg, ShaiJiaBean.class);
                            //获取图片的高度
                            getHeight(shouCangBean.getItem_list());
                            updateViewFromData(shouCangBean.getItem_list());
                        } else {
                            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                            ToastUtils.showCenter(UserInfoActivity.this, error_msg);
                        }
                    } else {
                        --page_num;
                        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);

                    }
                } catch (JSONException e) {
                    --page_num;
                    ToastUtils.showCenter(UserInfoActivity.this, UIUtils.getString(R.string.error_shaijia));
                }
            }
        };
//        MyHttpManager.getInstance().getShaiJiaList(user_id, (page_num - 1) * 20, "20", callback);
        MyHttpManager.getInstance().getShaiJiaList(user_id, (page_num - 1) * 20, "20", callback);

    }


    private void getListDataArticle(final String state) {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                --page_num;
                ToastUtils.showCenter(UserInfoActivity.this, "文章数据加载失败！");

            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ArticleListBean articleListBean = GsonUtil.jsonToBean(data_msg, ArticleListBean.class);
                        if (null != articleListBean.getArticle_list() && 0 != articleListBean.getArticle_list().size()) {

                            setYLNum(articleListBean.getArticle_list(), state);
                            updateViewFromDataArticle(articleListBean.getArticle_list(), state);
                        } else {
                            updateViewFromDataArticle(null, state);
                        }
                    } else {
                        --page_num;
                        //没有更多数据
                        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                        ToastUtils.showCenter(UserInfoActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };

        MyHttpManager.getInstance().getShaiJiaArticleList(user_id, (page_num - 1) * 20, "20", callBack);

    }

    private void getHeight(List<ShaiJiaItemBean> item_list) {

        if (item_list.size() > 0) {
            for (int i = 0; i < item_list.size(); i++) {
                mListDataHeight.add(Math.round(width_Pic / item_list.get(i).getItem_info().getImage().getRatio()));
            }
        }
    }

    private void updateViewFromData(List<ShaiJiaItemBean> item_list) {

        position = mListData.size();
        mListData.addAll(item_list);
        if (mListData == null || mListData.size() == 0) {

            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
        } else {
            mAdapter.notifyData(mListData);
//            mAdapter.notifyItem(position, mListData, item_list);
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
            if (item_list == null || item_list.size() == 0) {
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
            }
        }


    }

    private void setYLNum(List<ArticleBean> listData, String state) {

        try {
            if (null != listData) {
                for (int i = 0; i < listData.size(); i++) {
                    mListSeeNumArticle.add(Integer.parseInt(listData.get(i).getArticle_info().getView_num().trim()));
                }
            }
        } catch (Exception e) {
            Log.d("test", "检查下是不是本地代理掉线啦");
        }
    }

    private void updateViewFromDataArticle(List<ArticleBean> listData, String state) {

        if (null != listData) {
            position = mListDataArticle.size();
            mListDataArticle.addAll(listData);
            mAdapterArticle.notifyData(mListDataArticle);
//            mAdapterArticle.notifyItem(position, mListDataArticle, listData);
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        } else {
            --page_num;
            //没有更多数据
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        mAdapterArticle.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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


}
