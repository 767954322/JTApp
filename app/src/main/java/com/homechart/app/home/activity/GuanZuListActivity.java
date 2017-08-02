package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.guanzhu.GuanZhuBean;
import com.homechart.app.home.bean.guanzhu.GuanZhuUserListBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
import com.homechart.app.recyclerlibrary.anims.adapters.ScaleInAnimationAdapter;
import com.homechart.app.recyclerlibrary.anims.animators.LandingAnimator;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
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

/**
 * Created by gumenghao on 17/6/7.
 */

public class GuanZuListActivity
        extends BaseActivity
        implements View.OnClickListener,
        CommonAdapter.OnItemClickListener,
        OnLoadMoreListener,
        OnRefreshListener {

    private List<GuanZhuUserListBean> mListData = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;
    private CommonAdapter<GuanZhuUserListBean> mAdapter;
    private HRecyclerView mRecyclerView;
    private ImageButton mIBBack;
    private TextView mTVTital;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private String last_id = "0";//上一页最后一条数据id,第一次传0值
    private String n = "20";//返回数据条数，默认20
    private String user_id;
    private RelativeLayout rl_no_data;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_guanzu_list;
    }

    @Override
    protected void initView() {

        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_pic);
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        rl_no_data = (RelativeLayout) findViewById(R.id.rl_no_data);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mIBBack.setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);

    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        user_id = (String) getIntent().getSerializableExtra(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTVTital.setText("关注");

        mAdapter = new CommonAdapter<GuanZhuUserListBean>(this, R.layout.item_guanzhu, mListData) {
            @Override
            public void convert(BaseViewHolder holder, int position) {
                RoundImageView iv_fensi_header = (RoundImageView) holder.getView(R.id.iv_fensi_header);
                holder.setText(R.id.tv_fensi_name, mListData.get(position).getNickname());
                if (mListData.get(position).getId().equals(holder.getView(R.id.iv_fensi_header).getTag())) {

                } else {
                    holder.getView(R.id.iv_fensi_header).setTag(mListData.get(position).getId());

                    ImageUtils.displayRoundImage(mListData.get(position).getAvatar().getBig(),
                            (RoundImageView) holder.getView(R.id.iv_fensi_header));
                }


                if (!mListData.get(position).getProfession().equals("0")) {
                    holder.getView(R.id.iv_fensi_zhuanye).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.iv_fensi_zhuanye).setVisibility(View.GONE);
                }

            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(GuanZuListActivity.this));
        mRecyclerView.setItemAnimator(null);

//        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(mAdapter);
//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setDuration(500);

        mAdapter.setHasStableIds(true);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                GuanZuListActivity.this.finish();
                break;
        }
    }

    //RecyclerView的Item点击事件
    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        Intent intent = new Intent(GuanZuListActivity.this, UserInfoActivity.class);
        intent.putExtra(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_id());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        last_id = "0";
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getListData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {

        if (mLoadMoreFooterView.canLoadMore() && mListData.size() > 0) {
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            last_id = mListData.get(mListData.size() - 1).getId();
            getListData(LOADMORE_STATUS);
        }

    }

    private void getListData(final String state) {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                ToastUtils.showCenter(GuanZuListActivity.this, UIUtils.getString(R.string.error_fensi));
            }

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        GuanZhuBean fenSiBean = GsonUtil.jsonToBean(data_msg, GuanZhuBean.class);
                        if (null != fenSiBean.getUser_list() && 0 != fenSiBean.getUser_list().size()) {
                            changeNone(0);
                            updateViewFromData(fenSiBean.getUser_list(), state);
                        } else {
                            changeNone(1);
                            updateViewFromData(null, state);
                        }
                    } else {
                        ToastUtils.showCenter(GuanZuListActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(GuanZuListActivity.this, UIUtils.getString(R.string.error_fensi));
                }
            }
        };
        MyHttpManager.getInstance().getGuanZuList(user_id, last_id, n, callback);
//        MyHttpManager.getInstance().getGuanZuList("100050", last_id, n, callback);

    }

    private void updateViewFromData(List<GuanZhuUserListBean> listData, String state) {

        switch (state) {

            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData) {
                    mListData.addAll(listData);
                    last_id = mListData.get(mListData.size() - 1).getId();
                } else {
                    mListData.clear();
                    last_id = "0";
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setRefreshing(false);//刷新完毕
                break;

            case LOADMORE_STATUS:
                if (null != listData) {

                    position = mListData.size();
                    mListData.addAll(listData);
//                    mAdapter.notifyData(mListData);
                    mAdapter.notifyItem(position, mListData, listData);
//                    mAdapter.notifyItemInserted(mListData.size() + 1);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                    last_id = mListData.get(mListData.size() - 1).getId();
                } else {
                    if (null == mListData) {
                        last_id = "0";
                    } else {
                        last_id = mListData.get(mListData.size() - 1).getId();
                    }
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
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

    private int position;
}
