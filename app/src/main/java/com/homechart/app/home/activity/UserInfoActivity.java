package com.homechart.app.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.homechart.app.home.bean.shaijia.ShaiJiaBean;
import com.homechart.app.home.bean.shaijia.ShaiJiaItemBean;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
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
    private List<Integer> mListDataHeight = new ArrayList<>();
    private UserCenterInfoBean userCenterInfoBean;
    private ImageButton mIBBack;
    private TextView mTVTital;
    private String user_id;
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
    private TextView tv_info_pic_tital;
    private View view_tiop;

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

        tv_userinfo_nikename = (TextView) headerView.findViewById(R.id.tv_userinfo_nikename);
        tv_info_pic_tital = (TextView) headerView.findViewById(R.id.tv_info_pic_tital);
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
    }
    /**
     * 获取状态栏高度
     * ！！这个方法来自StatusBarUtil,因为作者将之设为private，所以直接copy出来
     *
     * @param context context
     * @return 状态栏高度
     */
    private int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
    @Override
    protected void initData(Bundle savedInstanceState) {

        StatusBarUtil.setTranslucentForImageView(this, 0, null);
        int statusBarHeight = getStatusBarHeight(this);
        ViewGroup.LayoutParams layoutParams = view_tiop.getLayoutParams();
        layoutParams.width = PublicUtils.getScreenWidth(this);
        layoutParams.height = statusBarHeight;
        view_tiop.setLayoutParams(layoutParams);

        mTVTital.setText("");
        width_Pic = PublicUtils.getScreenWidth(UserInfoActivity.this) / 2 - UIUtils.getDimens(R.dimen.font_14);
        getUserInfo();
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

                String[] str = mListData.get(position).getItem_info().getAdd_time().split(" ");
                if (str.length > 0) {
                    ((TextView) holder.getView(R.id.item_info_time)).
                            setText(str[0] + "   发布");
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

            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //解决item之间互换位置的bug
//        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(null);
//        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(mAdapter);
//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setDuration(500);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.addHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);
        getListData();
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

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String info = (String) msg.obj;
            userCenterInfoBean = GsonUtil.jsonToBean(info, UserCenterInfoBean.class);
            changeUI();
        }
    };

    private void changeUI() {

        if (userCenterInfoBean == null) {
            getUserInfo();
        } else {
            ImageUtils.displayRoundImage(userCenterInfoBean.getUser_info().getAvatar().getBig(), iv_header_desiner_center);
            tv_userinfo_nikename.setText(userCenterInfoBean.getUser_info().getNickname());
            tv_info_guanzhu_num.setText(userCenterInfoBean.getCounter().getFollow_num() + "");
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
        getListData();
    }

    private void getListData() {
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
            mAdapter.notifyItem(position, mListData, item_list);
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
            if (item_list == null || item_list.size() == 0) {
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private int width_Pic;
    private int position;
}
