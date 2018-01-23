package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.homechart.app.home.bean.msgdingyue.DingYueItemBean;
import com.homechart.app.home.bean.msgdingyue.MsgDingYue;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.anims.animators.LandingAnimator;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gumenghao on 18/1/23.
 */

public class UserMessageActivity
        extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener,
        CommonAdapter.OnItemClickListener {


    private ImageButton mIBBack;
    private TextView mTVTital;
    private HRecyclerView mRecyclerView;
    private List<DingYueItemBean> mListData = new ArrayList<>();
    private MultiItemCommonAdapter<DingYueItemBean> mAdapter;
    private LoadMoreFooterView mLoadMoreFooterView;
    private int page_num = 1;
    private String n = "20";//返回数据条数，默认20
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int width_Pic_List;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_msg;
    }

    @Override
    protected void initView() {

        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_pic);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mIBBack.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        width_Pic_List = PublicUtils.getScreenWidth(UserMessageActivity.this) - UIUtils.getDimens(R.dimen.font_30);
        mTVTital.setText("消息");
        buildRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                UserMessageActivity.this.finish();
                break;
        }
    }

    private void buildRecyclerView() {

        MultiItemTypeSupport<DingYueItemBean> support = new MultiItemTypeSupport<DingYueItemBean>() {
            @Override
            public int getLayoutId(int itemType) {

                if (itemType == 0) {
                    return R.layout.item_message_activity;
                } else if (itemType == 1) {
                    return R.layout.item_message_tips;
                } else if (itemType == 2) {
                    return R.layout.item_message_guanzhu;
                } else if (itemType == 3) {
                    return R.layout.item_message_shoucang;
                } else if (itemType == 4) {
                    return R.layout.item_message_shoucang;
                } else if (itemType == 5) {
                    return R.layout.item_message_dingyue;
                } else if (itemType == 6) {
                    return R.layout.item_message_dingyue1;
                } else {
                    return R.layout.item_message_shoucang;
                }
            }

            @Override
            public int getItemViewType(int position, DingYueItemBean itemMessageBean) {

                if (itemMessageBean.getNotice_class().equals("system")) {//系统消息
                    if (itemMessageBean.getType().equals("activity")) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else if (itemMessageBean.getNotice_class().equals("follow")) {//关注消息
                    return 2;
                } else if (itemMessageBean.getNotice_class().equals("comment")) {//评论消息
                    return 3;
                } else if (itemMessageBean.getNotice_class().equals("collect")) {//收藏消息
                    return 4;
                } else if (itemMessageBean.getNotice_class().equals("addToAlbum")) {//加入灵感辑消息
                    return 5;
                } else if (itemMessageBean.getNotice_class().equals("albumUpdate")) {//专辑更新消息
                    return 6;
                } else {
                    return 6;
                }
            }
        };
        mAdapter = new MultiItemCommonAdapter<DingYueItemBean>(UserMessageActivity.this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {
                if (getItemViewType(position) == 0) {

                    ((TextView) holder.getView(R.id.tv_activity_tital)).setText(mListData.get(position).getContent());
                    ((TextView) holder.getView(R.id.tv_activity_time)).setText(mListData.get(position).getAdd_time());
                    ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_activity_image).getLayoutParams();
                    layoutParams.height = (int) (width_Pic_List / 2.03);
                    holder.getView(R.id.iv_activity_image).setLayoutParams(layoutParams);
                    ImageUtils.displayFilletImage(mListData.get(position).getImage().getImg0(), (ImageView) holder.getView(R.id.iv_activity_image));

                } else if (getItemViewType(position) == 1) {

                    ((TextView) holder.getView(R.id.tv_tips_tital)).setText(mListData.get(position).getContent());
                    ((TextView) holder.getView(R.id.tv_tips_time)).setText(mListData.get(position).getAdd_time());
                    ImageUtils.displayFilletImage(mListData.get(position).getImage().getImg0(), (ImageView) holder.getView(R.id.iv_activity_image));

                } else if (getItemViewType(position) == 2) {

//                    ImageUtils.displayRoundImage(mListData.get(position).getUser_info().getAvatar(), (RoundImageView) holder.getView(R.id.riv_header));
                    ((TextView) holder.getView(R.id.tv_content)).setText(mListData.get(position).getContent());
                    ((TextView) holder.getView(R.id.tv_time)).setText(mListData.get(position).getAdd_time());

                } else if (getItemViewType(position) == 3) {

//                    ImageUtils.displayRoundImage(mListData.get(position).getUser_info().getAvatar(), (RoundImageView) holder.getView(R.id.riv_header));
                    ImageUtils.disRectangleImage(mListData.get(position).getImage().getImg0(), (ImageView) holder.getView(R.id.iv_imageview));
//                    ((TextView) holder.getView(R.id.tv_name)).setText(mListData.get(position).getUser_info().getNickname());
                    ((TextView) holder.getView(R.id.tv_content)).setText(mListData.get(position).getContent());
                    ((TextView) holder.getView(R.id.tv_time)).setText(mListData.get(position).getAdd_time());

                } else if (getItemViewType(position) == 4) {

//                    ImageUtils.displayRoundImage(mListData.get(position).getUser_info().getAvatar(), (RoundImageView) holder.getView(R.id.riv_header));
                    ImageUtils.disRectangleImage(mListData.get(position).getImage().getImg0(), (ImageView) holder.getView(R.id.iv_imageview));
//                    ((TextView) holder.getView(R.id.tv_name)).setText(mListData.get(position).getUser_info().getNickname());
                    ((TextView) holder.getView(R.id.tv_content)).setText(mListData.get(position).getContent());
                    ((TextView) holder.getView(R.id.tv_time)).setText(mListData.get(position).getAdd_time());
                    holder.getView(R.id.riv_header).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(UserMessageActivity.this, UserInfoActivity.class);
//                            intent.putExtra(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                            startActivity(intent);
                        }
                    });

                } else if (getItemViewType(position) == 5 || getItemViewType(position) == 6) {
//                    ImageUtils.displayRoundImage(mListData.get(position).getUser_info().getAvatar(), (RoundImageView) holder.getView(R.id.riv_header));
                    if (TextUtils.isEmpty(mListData.get(position).getImage().getImg0())) {
                        ImageUtils.disRectangleDefaultImage("", (ImageView) holder.getView(R.id.iv_imageview));
                    } else {
                        ImageUtils.disRectangleImage(mListData.get(position).getImage().getImg0(), (ImageView) holder.getView(R.id.iv_imageview));
                    }
//                    ((TextView) holder.getView(R.id.tv_name)).setText(mListData.get(position).getUser_info().getNickname());
                    ((TextView) holder.getView(R.id.tv_content)).setText(mListData.get(position).getContent());
                    ((TextView) holder.getView(R.id.tv_time)).setText(mListData.get(position).getAdd_time());

                    holder.getView(R.id.riv_header).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(UserMessageActivity.this, UserInfoActivity.class);
//                            intent.putExtra(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                            startActivity(intent);
                        }
                    });

                }

            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(UserMessageActivity.this));
        mRecyclerView.setItemAnimator(new LandingAnimator());
        mRecyclerView.setOnRefreshListener(UserMessageActivity.this);
        mRecyclerView.setOnLoadMoreListener(UserMessageActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(UserMessageActivity.this);
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
        ++page_num;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        getListData(LOADMORE_STATUS);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

    }

    private void getListData(final String state) {
        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                --page_num;
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
                        MsgDingYue msgDingYue = GsonUtil.jsonToBean(data_msg, MsgDingYue.class);
                        List<DingYueItemBean> list = msgDingYue.getNotice_list();
                        if (null != list && 0 != list.size()) {
                            updateViewFromData(list, state);
                        } else {
                            updateViewFromData(null, state);
                        }
                    } else {
                        --page_num;
                        ToastUtils.showCenter(UserMessageActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    --page_num;
                    ToastUtils.showCenter(UserMessageActivity.this, UIUtils.getString(R.string.error_message));
                }
            }
        };
        MyHttpManager.getInstance().allMSGList(page_num, 20, callback);

    }

    private void updateViewFromData(List<DingYueItemBean> listData, String state) {
        switch (state) {
            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData) {
                    mListData.addAll(listData);
                } else {
                    mListData.clear();
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setRefreshing(false);//刷新完毕
                break;
            case LOADMORE_STATUS:
                if (null != listData) {
                    mListData.addAll(listData);
                    mAdapter.notifyData(mListData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                } else {
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

}
