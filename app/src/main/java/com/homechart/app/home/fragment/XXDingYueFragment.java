package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.activity.UserInfoActivity;
import com.homechart.app.home.base.LazyLoadFragment;
import com.homechart.app.home.bean.msgdingyue.DingYueItemBean;
import com.homechart.app.home.bean.msgdingyue.MsgDingYue;
import com.homechart.app.home.bean.msgguanzhu.MsgGZBean;
import com.homechart.app.home.bean.msgguanzhu.MsgNoticeBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.ui.activity.InspirationDetailActivity;
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

@SuppressLint("ValidFragment")
public class XXDingYueFragment
        extends LazyLoadFragment
        implements OnLoadMoreListener,
        OnRefreshListener,
        CommonAdapter.OnItemClickListener {


    private List<DingYueItemBean> mListData = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;
    private MultiItemCommonAdapter<DingYueItemBean> mAdapter;
    private HRecyclerView mRecyclerView;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int page_num = 1;
    private String n = "20";//返回数据条数，默认20
    private String user_id;
    private RelativeLayout rl_no_data;
    private FragmentManager fragmentManager;

    public XXDingYueFragment() {
    }

    public XXDingYueFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_xx_dingyue;
    }

    @Override
    protected void lazyLoad() {
        initView();
        initData();
    }

    protected void initView() {

        rl_no_data = (RelativeLayout) rootView.findViewById(R.id.rl_no_data);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_pic);
    }

    protected void initData() {
        buildRecyclerView();
    }

    private void buildRecyclerView() {

        MultiItemTypeSupport<DingYueItemBean> support = new MultiItemTypeSupport<DingYueItemBean>() {
            @Override
            public int getLayoutId(int itemType) {

                if (itemType == 0) {
                    return R.layout.item_message_dingyue1;
                }else {
                    return R.layout.item_message_dingyue;
                }
            }

            @Override
            public int getItemViewType(int position, DingYueItemBean itemMessageBean) {

                if (itemMessageBean.getNotice_class().equals("albumUpdate")) {//订阅的灵感辑更新消息
                    return 0;
                } else {//加入到灵感辑消息
                    return 1;
                }
            }
        };
        mAdapter = new MultiItemCommonAdapter<DingYueItemBean>(activity, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {

                ImageUtils.displayRoundImage(mListData.get(position).getUser_info().getAvatar(), (RoundImageView) holder.getView(R.id.riv_header));
                ImageUtils.disRectangleImage(mListData.get(position).getImage().getImg0(), (ImageView) holder.getView(R.id.iv_imageview));
                ((TextView) holder.getView(R.id.tv_name)).setText(mListData.get(position).getUser_info().getNickname());
                ((TextView) holder.getView(R.id.tv_content)).setText(mListData.get(position).getContent());
                ((TextView) holder.getView(R.id.tv_time)).setText(mListData.get(position).getAdd_time());

                holder.getView(R.id.riv_header).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, UserInfoActivity.class);
                        intent.putExtra(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                        startActivity(intent);
                    }
                });

            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setItemAnimator(new LandingAnimator());
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
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
        if (mLoadMoreFooterView.canLoadMore() && mListData.size() > 0) {
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            getListData(LOADMORE_STATUS);
        }
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        Intent intent = new Intent(activity, InspirationDetailActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("ifHideEdit", true);
        intent.putExtra("album_id", mListData.get(position).getObject_id());
        startActivity(intent);

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
                            changeNone(0);
                            updateViewFromData(list, state);
                        } else {
                            changeNone(1);
                            updateViewFromData(null, state);
                        }
                    } else {
                        --page_num;
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                    --page_num;
                    ToastUtils.showCenter(activity, UIUtils.getString(R.string.error_message));
                }
            }
        };
        MyHttpManager.getInstance().dingYueMSGList(page_num, 20, callback);

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


}