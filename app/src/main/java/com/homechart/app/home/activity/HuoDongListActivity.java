package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.fensi.FenSiBean;
import com.homechart.app.home.bean.fensi.UserListBean;
import com.homechart.app.home.bean.huodonglist.HDItemBean;
import com.homechart.app.home.bean.huodonglist.HDListBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gumenghao on 17/8/7.
 */

public class HuoDongListActivity
        extends BaseActivity
        implements OnLoadMoreListener,
        OnRefreshListener,
        View.OnClickListener {
    private ImageButton mBack;
    private TextView tv_tital_comment;
    private HRecyclerView mRecyclerView;
    private CommonAdapter<HDItemBean> mAdapter;
    private List<HDItemBean> mListData = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int page_num = 1;
    private int position;
    private int width_Pic_List;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_huodong_list;
    }

    @Override
    protected void initView() {
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_huodong);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        width_Pic_List = PublicUtils.getScreenWidth(HuoDongListActivity.this) - UIUtils.getDimens(R.dimen.font_14);
        tv_tital_comment.setText("活动列表");
        mAdapter = new CommonAdapter<HDItemBean>(this, R.layout.item_huodong_list, mListData) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {
                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                layoutParams.width = width_Pic_List;
                layoutParams.height = (int) (width_Pic_List / 2.36);
                holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                ImageUtils.displayFilletImage(mListData.get(position).getActivity_info().getImage().getImg0(),
                        (ImageView) holder.getView(R.id.iv_imageview_one));
                String tital = "";
                if(mListData.get(position).getActivity_info().getTitle().length() > 12){
                    tital =  mListData.get(position).getActivity_info().getTitle().substring(0,12)+"...";
                }else {
                    tital =  mListData.get(position).getActivity_info().getTitle();
                }
                ((TextView) holder.getView(R.id.tv_name_pic)).setText(tital);

                if (mListData.get(position).getActivity_info().getState_id().equals("3")) {
                    //计算时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());
                    String str = formatter.format(curDate);
                    long data = PublicUtils.diffDay(mListData.get(position).getActivity_info().getEnd_time(), str, "yyyy-MM-dd HH:mm:ss");
                    ((ImageView) holder.getView(R.id.iv_data_last_icon)).setImageResource(R.drawable.shijian);
                    ((TextView) holder.getView(R.id.tv_data_last)).setText("还剩" + data + "天");
                    ((TextView) holder.getView(R.id.tv_data_last)).setTextColor(R.color.bg_e79056);
                } else if (mListData.get(position).getActivity_info().getState_id().equals("2")) {
                    ((ImageView) holder.getView(R.id.iv_data_last_icon)).setImageResource(R.drawable.shijian);
                    ((TextView) holder.getView(R.id.tv_data_last)).setText("敬请期待");
                    ((TextView) holder.getView(R.id.tv_data_last)).setTextColor(R.color.bg_e79056);
                } else if (mListData.get(position).getActivity_info().getState_id().equals("1")) {
                    ((ImageView) holder.getView(R.id.iv_data_last_icon)).setImageResource(R.drawable.shijian1);
                    ((TextView) holder.getView(R.id.tv_data_last)).setTextColor(R.color.bg_d7d7db);
                    ((TextView) holder.getView(R.id.tv_data_last)).setText("已结束");
                }

                holder.getView(R.id.rl_huodong_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HuoDongListActivity.this, HuoDongDetailsActivity.class);
                        intent.putExtra("activity_id", mListData.get(position).getActivity_info().getActivity_id());
                        startActivity(intent);
                    }
                });
            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(HuoDongListActivity.this));
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
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                HuoDongListActivity.this.finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getListData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {
        if (mLoadMoreFooterView.canLoadMore() && mListData.size() > 0) {
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            getListData(LOADMORE_STATUS);
        }
    }

    private void getListData(final String state) {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                ToastUtils.showCenter(HuoDongListActivity.this, UIUtils.getString(R.string.error_fensi));
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ++page_num;
                        HDListBean hdListBean = GsonUtil.jsonToBean(data_msg, HDListBean.class);
                        if (null != hdListBean.getActivity_list() && 0 != hdListBean.getActivity_list().size()) {
                            updateViewFromData(hdListBean.getActivity_list(), state);
                        } else {
                            updateViewFromData(null, state);
                        }
                    } else {
                        ToastUtils.showCenter(HuoDongListActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(HuoDongListActivity.this, UIUtils.getString(R.string.error_fensi));
                }
            }
        };
        MyHttpManager.getInstance().getHuoDongiList((page_num - 1) * 20 + "", "20", callback);
    }


    private void updateViewFromData(List<HDItemBean> listData, String state) {

        switch (state) {
            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData) {
                    mListData.addAll(listData);
                } else {
                    page_num = 1;
                }
                mAdapter.notifyDataSetChanged();
                if (listData.size() < 20) {
                    mRecyclerView.setRefreshing(false);//刷新完毕
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                } else {
                    mRecyclerView.setRefreshing(false);//刷新完毕
                }
                break;

            case LOADMORE_STATUS:
                if (null != listData) {
                    position = mListData.size();
                    mListData.addAll(listData);
                    mAdapter.notifyItem(position, mListData, listData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                } else {
                    if (null == mListData) {
                        page_num = 1;
                    } else {
                        --page_num;
                    }
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

}
