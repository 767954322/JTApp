package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.fensi.FenSiBean;
import com.homechart.app.home.bean.fensi.UserListBean;
import com.homechart.app.home.bean.shoucang.ShouCangBean;
import com.homechart.app.home.bean.shoucang.ShouCangItemBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
import com.homechart.app.recyclerlibrary.anims.animators.LandingAnimator;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.timepiker.citypickerview.widget.CityPicker;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/7.
 */

public class ShouCangListActivity
        extends BaseActivity
        implements View.OnClickListener,
        CommonAdapter.OnItemClickListener,
        OnLoadMoreListener,
        OnRefreshListener {
    private ImageButton mIBBack;
    private TextView mTVTital;
    private HRecyclerView mRecyclerView;
    private CommonAdapter<ShouCangItemBean> mAdapter;
    private LoadMoreFooterView mLoadMoreFooterView;

    private int page_num = 1;
    private String mUserId;
    private int guanli_tag = 0;//0:未打开管理   1:打开管理
    private int num_checked = 0; //选择的个数
    private TextView tv_content_right;
    private TextView tv_shoucang_two;
    private RelativeLayout rl_below;
    private Map<String, ShouCangItemBean> map_delete = new HashMap<>();//选择的唯一标示
    private ImageView iv_delete_icon;
    private RelativeLayout rl_no_data;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shoucang_list;
    }

    @Override
    protected void initView() {
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        iv_delete_icon = (ImageView) findViewById(R.id.iv_delete_icon);
        rl_no_data = (RelativeLayout) findViewById(R.id.rl_no_data);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        rl_below = (RelativeLayout) findViewById(R.id.rl_below);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        tv_shoucang_two = (TextView) findViewById(R.id.tv_shoucang_two);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_shoucang);
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mIBBack.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);
        iv_delete_icon.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTVTital.setText("收藏");
        tv_content_right.setText("管理");
        mAdapter = new CommonAdapter<ShouCangItemBean>(this, R.layout.item_shoucang, mListData) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {

                final String item_id = mListData.get(position).getItem_info().getItem_id();
                if (guanli_tag == 0) {
                    holder.getView(R.id.cb_check).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.cb_check).setVisibility(View.VISIBLE);

                }

                if (mListData.get(position).getItem_info().getItem_id().equals(holder.getView(R.id.iv_shoucang_image).getTag())) {

                } else {
                    holder.getView(R.id.iv_shoucang_image).setTag(mListData.get(position).getItem_info().getItem_id());
                    ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg0(),
                            (ImageView) holder.getView(R.id.iv_shoucang_image));
                }


                ((CheckBox) holder.getView(R.id.cb_check)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ++num_checked;
                            if (!map_delete.containsKey(item_id)) {
                                map_delete.put(item_id, mListData.get(position));
                            }
                        } else {
                            if (map_delete.containsKey(item_id)) {
                                map_delete.remove(item_id);
                            }
                            --num_checked;
                        }
                        upCheckedStatus();
                    }
                });
                holder.getView(R.id.iv_shoucang_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (guanli_tag == 0) {//未打开管理

                            //友盟统计
                            HashMap<String, String> map1 = new HashMap<String, String>();
                            map1.put("evenname", "点击查看收藏图");
                            map1.put("even", "点击收藏列表的图进入图片详情");
                            MobclickAgent.onEvent(ShouCangListActivity.this, "action48", map1);
                            //ga统计
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory("点击收藏列表的图进入图片详情")  //事件类别
                                    .setAction("点击查看收藏图")      //事件操作
                                    .build());

                            jumpImageDetail(mListData.get(position).getItem_info().getItem_id());

                        } else {
                            if (((CheckBox) holder.getView(R.id.cb_check)).isChecked()) {
                                ((CheckBox) holder.getView(R.id.cb_check)).setChecked(false);
                            } else {
                                ((CheckBox) holder.getView(R.id.cb_check)).setChecked(true);
                            }
                        }

                    }

                });

                if (map_delete.containsKey(item_id)) {
                    ((CheckBox) holder.getView(R.id.cb_check)).setChecked(true);
                } else {
                    ((CheckBox) holder.getView(R.id.cb_check)).setChecked(false);
                }

            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new GridLayoutManager(ShouCangListActivity.this, 2));
        mRecyclerView.setItemAnimator(null);

//        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(mAdapter);
//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setDuration(500);

        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:

                ShouCangListActivity.this.finish();
                break;
            case R.id.tv_content_right:

                if (mListData != null && mListData.size() > 0) {

                    if (guanli_tag == 0) {

                        //友盟统计
                        HashMap<String, String> map4 = new HashMap<String, String>();
                        map4.put("evenname", "点击收藏管理");
                        map4.put("even", "点击收藏管理按钮");
                        MobclickAgent.onEvent(ShouCangListActivity.this, "action46", map4);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("点击收藏管理按钮")  //事件类别
                                .setAction("点击收藏管理")      //事件操作
                                .build());

                        //打开管理
                        tv_content_right.setText("取消");
                        map_delete.clear();
                        guanli_tag = 1;
                        num_checked = 0;
                        tv_shoucang_two.setText(num_checked + "");
                        rl_below.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        //关闭管理
                        tv_content_right.setText("管理");
                        map_delete.clear();
                        guanli_tag = 0;
                        rl_below.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                    }

                } else {
                    ToastUtils.showCenter(ShouCangListActivity.this, "先去收藏一些图片吧");
                }


                break;
            case R.id.iv_delete_icon:

                if (map_delete.size() > 0) {

                    //友盟统计
                    HashMap<String, String> map4 = new HashMap<String, String>();
                    map4.put("evenname", "点击删除收藏");
                    map4.put("even", "点击收藏删除按钮");
                    MobclickAgent.onEvent(ShouCangListActivity.this, "action47", map4);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("点击收藏删除按钮")  //事件类别
                            .setAction("点击删除收藏")      //事件操作
                            .build());

                    CustomProgress.show(ShouCangListActivity.this, "正在删除...", false, null);
                    String delete_items = "";
                    for (String key : map_delete.keySet()) {
                        delete_items = delete_items + key + ",";
                    }
                    delete_items = delete_items.substring(0, delete_items.length() - 1);
                    OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            CustomProgress.cancelDialog();
                            ToastUtils.showCenter(ShouCangListActivity.this, getString(R.string.error_delete_shoucang));
                        }

                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                                String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                                String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                                if (error_code == 0) {
                                    for (String key : map_delete.keySet()) {
                                        mListData.remove(map_delete.get(key));
                                    }
                                    map_delete.clear();
                                    upCheckedStatus();
                                    mAdapter.notifyDataSetChanged();
                                    if(mListData == null || mListData.size() == 0){
                                        handler.sendEmptyMessage(0);
                                    }
                                    CustomProgress.cancelDialog();
                                    ToastUtils.showCenter(ShouCangListActivity.this, getString(R.string.succes_delete_shoucang));
                                } else {
                                    CustomProgress.cancelDialog();
                                    ToastUtils.showCenter(ShouCangListActivity.this, error_msg);
                                }
                            } catch (JSONException e) {
                                CustomProgress.cancelDialog();
                                ToastUtils.showCenter(ShouCangListActivity.this, getString(R.string.error_delete_shoucang));
                            }
                        }
                    };
                    MyHttpManager.getInstance().deleteShouCang(delete_items, callBack);
                } else {
                    ToastUtils.showCenter(ShouCangListActivity.this, "请选择删除项");
                }

                break;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //关闭管理
            rl_no_data.setVisibility(View.VISIBLE);
            tv_content_right.setText("管理");
            map_delete.clear();
            guanli_tag = 0;
            rl_below.setVisibility(View.GONE);
        }
    };
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

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        ToastUtils.showCenter(ShouCangListActivity.this, "查看详情");
    }

    private void getListData(final String state) {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                ToastUtils.showCenter(ShouCangListActivity.this, UIUtils.getString(R.string.error_shoucang));
                if (state.equals(LOADMORE_STATUS)) {
                    --page_num;
                } else {
                    page_num = 1;
                }
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
                            ShouCangBean shouCangBean = GsonUtil.jsonToBean(data_msg, ShouCangBean.class);
                            if (null != shouCangBean.getItem_list() && 0 != shouCangBean.getItem_list().size()) {
                                changeNone(0);
                                updateViewFromData(shouCangBean.getItem_list(), state);
                            } else {
                                changeNone(1);
//                                if (page_num == 1) {
////                                    ToastUtils.showCenter(ShouCangListActivity.this, "您还没收藏图片呢，先去收藏一些图片吧!");
//                                } else {
////                                    ToastUtils.showCenter(ShouCangListActivity.this, "暂无更多数据!");
//                                }
                                updateViewFromData(null, state);
                            }
                        } else {
                            ToastUtils.showCenter(ShouCangListActivity.this, error_msg);
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
                    ToastUtils.showCenter(ShouCangListActivity.this, UIUtils.getString(R.string.error_shoucang));
                }
            }
        };
        MyHttpManager.getInstance().getShouCangList(mUserId, (page_num - 1) * 20, "20", callback);
//        MyHttpManager.getInstance().getShouCangList("100050", (page_num - 1) * 20, "20", callback);

    }

    private void updateViewFromData(List<ShouCangItemBean> listData, String state) {

        switch (state) {

            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData) {

                    rl_no_data.setVisibility(View.GONE);
                    mListData.addAll(listData);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setRefreshing(false);//刷新完毕
                } else {
                    page_num = 1;
                    mListData.clear();
                    mRecyclerView.setRefreshing(false);//刷新完毕
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;

            case LOADMORE_STATUS:
                if (null != listData) {
                    rl_no_data.setVisibility(View.GONE);
                    position = mListData.size();
                    mListData.addAll(listData);
                    mAdapter.notifyItem(position, mListData, listData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                } else {
                    --page_num;
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int position;

    private List<ShouCangItemBean> mListData = new ArrayList<>();

    //点击图片选择后，更新地步ui
    public void upCheckedStatus() {
        tv_shoucang_two.setText(map_delete.size() + "");
        Log.d("test", "个数：" + map_delete.size());
        Log.d("test", "数据：" + map_delete.toString());
    }

    //查看图片详情
    private void jumpImageDetail(String item_id) {

        Intent intent = new Intent(ShouCangListActivity.this, ImageDetailLongActivity.class);
        intent.putExtra("item_id", item_id);
        startActivity(intent);

    }

    private void changeNone(int i) {
        if (i == 0) {
            rl_no_data.setVisibility(View.GONE);
        } else if (i == 1) {
            if (mListData.size() > 0) {
                rl_no_data.setVisibility(View.GONE);
            } else {
                rl_no_data.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
