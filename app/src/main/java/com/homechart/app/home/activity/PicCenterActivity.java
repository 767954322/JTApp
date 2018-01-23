package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.userimage.ImageDataBean;
import com.homechart.app.home.bean.userimage.UserImageBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationBean;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationListBean;
import com.homechart.app.lingganji.ui.activity.InspirationDetailActivity;
import com.homechart.app.lingganji.ui.activity.MyLingGanlistActivity;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.glide.GlideImgManager;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gumenghao on 18/1/23.
 */

public class PicCenterActivity
        extends BaseActivity
        implements View.OnClickListener ,
        OnLoadMoreListener,
        OnRefreshListener {
    private String mUserId;
    private TextView mTital;
    private ImageButton mBack;
    private HRecyclerView mRecyclerView;
    private int widthPic;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int page_num = 1;
    private MultiItemCommonAdapter<ImageDataBean> mAdapter;
    private List<ImageDataBean> mListDataImage = new ArrayList<>();
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private LoadMoreFooterView mLoadMoreFooterView;
    private int position;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pic_center;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initView() {

        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mRecyclerView = (HRecyclerView) this.findViewById(R.id.rcy_recyclerview);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTital.setText("图片");

        widthPic = (PublicUtils.getScreenWidth(this) - UIUtils.getDimens(R.dimen.font_36)) / 2;
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        buildRecyclerView();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.nav_left_imageButton:
                PicCenterActivity.this.finish();
                break;
        }

    }

    private void buildRecyclerView() {
        MultiItemTypeSupport<ImageDataBean> support = new MultiItemTypeSupport<ImageDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == 0) {
                    return R.layout.item_center_image;
                } else {
                    return R.layout.item_center_image;
                }
            }

            @Override
            public int getItemViewType(int position, ImageDataBean s) {
                if (position % 2 == 0) {
                    return 0;
                } else {
                    return 1;
                }

            }
        };

        mAdapter = new MultiItemCommonAdapter<ImageDataBean>(PicCenterActivity.this, mListDataImage, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.iv_item_pic).getLayoutParams();
                layoutParams.height = (int) (widthPic / mListDataImage.get(position).getItem_info().getImage().getRatio());
                layoutParams.width = widthPic;
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                holder.getView(R.id.iv_item_pic).setLayoutParams(layoutParams);
                ImageUtils.displayFilletImage(mListDataImage.get(position).getItem_info().getImage().getImg0(), (ImageView) holder.getView(R.id.iv_item_pic));
                holder.getView(R.id.iv_item_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String> item_id_list = new ArrayList<>();
                        item_id_list.add(mListDataImage.get(position).getItem_info().getItem_id());
                        Intent intent = new Intent(PicCenterActivity.this, ImageDetailScrollActivity.class);
                        intent.putExtra("item_id", mListDataImage.get(position).getItem_info().getItem_id());
                        intent.putExtra("type", "single");
                        intent.putExtra("position", 0);
                        intent.putExtra("item_id_list", (Serializable) item_id_list);
                        startActivity(intent);

                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getImageByUserId(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getImageByUserId(LOADMORE_STATUS);
    }

    private void getImageByUserId(final String state) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        String newData = "{\"data\": " + data_msg + "}";
                        UserImageBean userImageBean = GsonUtil.jsonToBean(newData, UserImageBean.class);
                        if (null != userImageBean.getData().getItem_list() && 0 != userImageBean.getData().getItem_list().size()) {
                            updateViewFromDataImage(userImageBean.getData().getItem_list(), state);
                        } else {
                            updateViewFromDataImage(null, state);
                        }
                    } else {
                        ToastUtils.showCenter(PicCenterActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getImageByUserId(mUserId, (page_num - 1) * 20 + "", "20", callBack);
    }

    private void updateViewFromDataImage(List<ImageDataBean> listData, String state) {
        switch (state) {
            case REFRESH_STATUS:
                mListDataImage.clear();
                if (null != listData && listData.size() > 0) {
                    mListDataImage.addAll(listData);
                } else {
                    if (mListDataImage.size() == 0) {
                        ((TextView) mRecyclerView.getLoadMoreFooterView().findViewById(R.id.tvTheEnd)).setText("暂无内容");
                    } else {
                        ((TextView) mRecyclerView.getLoadMoreFooterView().findViewById(R.id.tvTheEnd)).setText("已经到底啦");
                    }
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                    page_num = 1;
                    mListDataImage.clear();
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setRefreshing(false);//刷新完毕
                break;

            case LOADMORE_STATUS:
                if (null != listData && listData.size() > 0) {
                    position = mListDataImage.size();
                    mListDataImage.addAll(listData);
                    mAdapter.notifyItem(position, mListDataImage, listData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                } else {
                    if (mListDataImage.size() == 0) {
                        ((TextView) mRecyclerView.getLoadMoreFooterView().findViewById(R.id.tvTheEnd)).setText("暂无内容");
                    } else {
                        ((TextView) mRecyclerView.getLoadMoreFooterView().findViewById(R.id.tvTheEnd)).setText("已经到底啦");
                    }
                    --page_num;
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

}
