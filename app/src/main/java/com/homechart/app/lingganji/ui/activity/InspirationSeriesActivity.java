package com.homechart.app.lingganji.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationBean;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationListBean;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
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

public class InspirationSeriesActivity extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener ,
        View.OnTouchListener{


    private View view;
    private Context mContext;
    private String mUserId;
    private RelativeLayout mMain;
    private ImageView mDismiss;
    private ImageView mIVLingGan;
    private View mHeaderInspiration;
    private HRecyclerView mRecyclerView;
    private MultiItemCommonAdapter<InspirationBean> mAdapter;
    private List<InspirationBean> mListData = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int page_num = 1;
    private int position;
    private int defalsePosition = 0;
    private RelativeLayout mRLAddInspiration;
    private TextView mTVSureAdd;
    private EditText mRLWye;
    private String image_url;
    private String item_id;

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_activity_inspiration;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        mUserId = getIntent().getStringExtra("userid");
        image_url = getIntent().getStringExtra("image_url");
        item_id = getIntent().getStringExtra("item_id");
    }

    @Override
    protected void initView() {
        mContext = InspirationSeriesActivity.this;
        mHeaderInspiration = LayoutInflater.from(mContext).inflate(R.layout.header_inspiration, null);
        mMain = (RelativeLayout) this.findViewById(R.id.main);
        mDismiss = (ImageView) this.findViewById(R.id.iv_dismiss_pop);
        mTVSureAdd = (TextView) this.findViewById(R.id.tv_sure_add);
        mRecyclerView = (HRecyclerView) this.findViewById(R.id.rcy_recyclerview);
        mIVLingGan = (ImageView) mHeaderInspiration.findViewById(R.id.iv_linggan);
        mRLWye = (EditText) mHeaderInspiration.findViewById(R.id.rt_linggan_content);
        mRLAddInspiration = (RelativeLayout) mHeaderInspiration.findViewById(R.id.rl_add_inspiration);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mDismiss.setOnClickListener(this);
        mRLAddInspiration.setOnClickListener(this);
        mTVSureAdd.setOnClickListener(this);
        mRLWye.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_dismiss_pop) {
            this.finish();
        } else if (i == R.id.rl_add_inspiration) {

            Intent intent = new Intent(this, InspirationCreateActivity.class);
            intent.putExtra("userid", mUserId);
            startActivityForResult(intent, 1);

        } else if (i == R.id.tv_sure_add) {

            if (mListData.size() > 0) {
                addInspiration();
            } else {
                ToastUtils.showCenter(mContext, "请先创建灵感辑");
            }
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        ImageUtils.displayFilletImage(image_url, mIVLingGan);
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

                ((TextView) holder.getView(R.id.tv_item_name)).setText(mListData.get(position).getAlbum_info().getAlbum_name());
                if (TextUtils.isEmpty(mListData.get(position).getAlbum_info().getCover_image().getImg0())) {
                    ImageUtils.displayFilletDefaulImage("", (ImageView) holder.getView(R.id.iv_item_pic));
                } else {
                    ImageUtils.displayFilletImage(mListData.get(position).getAlbum_info().getCover_image().getImg0(), (ImageView) holder.getView(R.id.iv_item_pic));
                }

                if (defalsePosition == position) {
                    holder.getView(R.id.rl_item_inspiration_content).setBackgroundResource(R.drawable.bg_item_inspiration);
                    holder.getView(R.id.iv_choose).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.rl_item_inspiration_content).setBackgroundResource(R.drawable.bg_item_inspiration_null);
                    holder.getView(R.id.iv_choose).setVisibility(View.GONE);
                }

                holder.getView(R.id.rl_item_inspiration).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.getView(R.id.rl_item_inspiration_content).setBackgroundResource(R.drawable.bg_item_inspiration);
                        holder.getView(R.id.iv_choose).setVisibility(View.VISIBLE);
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
        defalsePosition = 0;
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


    private void addInspiration() {
        CustomProgress.show(InspirationSeriesActivity.this, "加入中...", false, null);
        String strWhy = mRLWye.getText().toString();
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                CustomProgress.cancelDialog();

                ToastUtils.showCenter(InspirationSeriesActivity.this, "加入灵感辑失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(InspirationSeriesActivity.this, "图片已经加入“" + mListData.get(defalsePosition).getAlbum_info().getAlbum_name() + "”");
                        InspirationSeriesActivity.this.finish();
                    } else {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(InspirationSeriesActivity.this, "加入灵感辑失败");
                    }
                } catch (JSONException e) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(InspirationSeriesActivity.this, "加入灵感辑失败");
                }
            }
        };
        MyHttpManager.getInstance().addInpirationFromPic(mListData.get(defalsePosition).getAlbum_info().getAlbum_id(), item_id, strWhy, callBack);


    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ((view.getId() == R.id.rt_linggan_content && canVerticalScroll(mRLWye))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            onRefresh();
        }
    }

    /**
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }
}
