package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.message.ItemMessageBean;
import com.homechart.app.home.bean.message.MessageBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
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
public class XXMessageFragment
        extends BaseFragment implements View.OnClickListener,
        CommonAdapter.OnItemClickListener,
        OnLoadMoreListener,
        OnRefreshListener {
    private List<ItemMessageBean> mListData = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;
    private MultiItemCommonAdapter<ItemMessageBean> mAdapter;
    private HRecyclerView mRecyclerView;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int page_num = 1;
    private String n = "20";//返回数据条数，默认20
    private String user_id;
    private int TYPE_ACTIVITY = 1;
    private int TYPE_TIPS = 2;
    private RelativeLayout rl_no_data;
    private int width_Pic_List;
    private FragmentManager fragmentManager;

    public XXMessageFragment() {
    }

    public XXMessageFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_xx_message;
    }

    @Override
    protected void initView() {
        rl_no_data = (RelativeLayout) rootView.findViewById(R.id.rl_no_data);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_pic);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        width_Pic_List = PublicUtils.getScreenWidth(activity) - UIUtils.getDimens(R.dimen.font_30);

        buildRecyclerView();

    }

    @Override
    public void onClick(View v) {

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

    //RecyclerView的Item点击事件
    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

    }


    private void buildRecyclerView() {
        MultiItemTypeSupport<ItemMessageBean> support = new MultiItemTypeSupport<ItemMessageBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_ACTIVITY) {
                    return R.layout.item_message_activity;
                } else {
                    return R.layout.item_message_tips;
                }
            }

            @Override
            public int getItemViewType(int position, ItemMessageBean itemMessageBean) {
                if (itemMessageBean.getType().equals("activity")) {
                    return TYPE_ACTIVITY;
                } else {
                    return TYPE_TIPS;
                }

            }
        };

        mAdapter = new MultiItemCommonAdapter<ItemMessageBean>(activity, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, int position) {
                if (getItemViewType(position) == TYPE_ACTIVITY) {
                    ((TextView) holder.getView(R.id.tv_activity_tital)).setText(mListData.get(position).getContent());
                    ((TextView) holder.getView(R.id.tv_activity_time)).setText(mListData.get(position).getAdd_time());

                    ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_activity_image).getLayoutParams();
                    layoutParams.height = (int) (width_Pic_List / 2.03);
                    holder.getView(R.id.iv_activity_image).setLayoutParams(layoutParams);
                    ImageUtils.displayFilletImage(mListData.get(position).getImage().getImg0(), (ImageView) holder.getView(R.id.iv_activity_image));

                } else if (getItemViewType(position) == TYPE_TIPS) {
                    ((TextView) holder.getView(R.id.tv_tips_tital)).setText(mListData.get(position).getContent());
                    ((TextView) holder.getView(R.id.tv_tips_time)).setText(mListData.get(position).getAdd_time());

                    ImageUtils.displayFilletImage(mListData.get(position).getImage().getImg0(), (ImageView) holder.getView(R.id.iv_activity_image));

                }
            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setItemAnimator(new LandingAnimator());

//        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(mAdapter);
//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setDuration(500);

        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
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
                        MessageBean messageBean = GsonUtil.jsonToBean(data_msg, MessageBean.class);
                        List<ItemMessageBean> list = messageBean.getNotice_list();
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
        MyHttpManager.getInstance().xitongMSGList(page_num, 20, callback);

    }

    private void updateViewFromData(List<ItemMessageBean> listData, String state) {

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