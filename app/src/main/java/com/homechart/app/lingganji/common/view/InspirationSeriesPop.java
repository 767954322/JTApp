package com.homechart.app.lingganji.common.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.bean.cailike.ImageLikeItemBean;
import com.homechart.app.home.bean.search.SearchDataBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationBean;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationListBean;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
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
 * Created by gumenghao on 18/1/3.
 */

public class InspirationSeriesPop extends PopupWindow
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener {

    private View view;
    private Context mContext;
    private String mUserId;
    private RelativeLayout mMain;
    private ImageView mDismissPop;
    private ImageView mIVLingGan;
    private View mHeaderInspiration;
    private HRecyclerView mRecyclerView;
    private SearchItemDataBean mSearchItemDataBean;
    private MultiItemCommonAdapter<InspirationBean> mAdapter;
    private List<InspirationBean> mListData = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int page_num = 1;
    private int position;
    private int defalsePosition = 0;

    public InspirationSeriesPop(Context context, SearchItemDataBean searchItemDataBean, String userId) {
        this.mContext = context;
        this.mUserId = userId;
        this.mSearchItemDataBean = searchItemDataBean;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_inspiration, null);
        initView();
        initListener();
        initPop();
        initData();
    }

    private void initView() {

        mHeaderInspiration = LayoutInflater.from(mContext).inflate(R.layout.header_inspiration, null);
        mMain = (RelativeLayout) this.view.findViewById(R.id.main);
        mDismissPop = (ImageView) this.view.findViewById(R.id.iv_dismiss_pop);
        mRecyclerView = (HRecyclerView) this.view.findViewById(R.id.rcy_recyclerview);
        mIVLingGan = (ImageView) mHeaderInspiration.findViewById(R.id.iv_linggan);

    }

    private void initListener() {
        mDismissPop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_dismiss_pop) {
            this.dismiss();
        }
    }

    private void initPop() {
        // 设置外部可点击
        this.setOutsideTouchable(true);
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);
    }

    private void initData() {

        ImageUtils.displayFilletImage(mSearchItemDataBean.getItem_info().getImage().getImg0(), mIVLingGan);
        buildRecyclerView();

    }

    private void buildRecyclerView() {
        MultiItemTypeSupport<InspirationBean> support = new MultiItemTypeSupport<InspirationBean>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_inspirationlist;
            }

            @Override
            public int getItemViewType(int position, InspirationBean s) {
                return 0;
            }
        };

        mAdapter = new MultiItemCommonAdapter<InspirationBean>(mContext, mListData, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {

                mIVLingGan.setFocusable(true);
                mIVLingGan.setFocusableInTouchMode(true);

                ((TextView) holder.getView(R.id.tv_item_name)).setText(mListData.get(position).getAlbum_info().getAlbum_name());
                ImageUtils.displayFilletImage(mListData.get(position).getAlbum_info().getCover_image().getImg0(), (ImageView) holder.getView(R.id.iv_item_pic));

                if (defalsePosition == position) {
                    holder.getView(R.id.rl_item_inspiration_content).setBackgroundResource(R.drawable.bg_item_inspiration);
                } else {
                    holder.getView(R.id.rl_item_inspiration_content).setBackgroundResource(R.drawable.bg_item_inspiration_null);
                }

                holder.getView(R.id.rl_item_inspiration).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.getView(R.id.rl_item_inspiration_content).setBackgroundResource(R.drawable.bg_item_inspiration);
                        int beforePosition = defalsePosition;
                        defalsePosition = position;
                        mAdapter.notifyItemChanged(beforePosition);
                    }
                });

            }
        };
        mRecyclerView.addHeaderView(mHeaderInspiration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
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
                ToastUtils.showCenter(mContext, "专辑列表获取失败！");
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
        MyHttpManager.getInstance().getUserInspirationSeries(mUserId, (page_num - 1) * 20 + "", "20", callBack);
    }

    private void updateViewFromData(List<InspirationBean> listData, String state) {

        switch (state) {

            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData && listData.size() > 0) {
                    mListData.addAll(listData);
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
                } else {
                    --page_num;
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }


    @Override
    public void onRefresh() {
        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getInspirationsData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getInspirationsData(LOADMORE_STATUS);
    }


}
