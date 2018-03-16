package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.userimage.ImageDataBean;
import com.homechart.app.home.bean.userimage.UserImageBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.TopCropImageView;
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
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class NewPicCenterFragment
        extends BaseFragment
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener {


    private String mUserId;
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
    private FragmentManager fragmentManager;

    public NewPicCenterFragment() {
    }

    public NewPicCenterFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pic_center;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
    }

    @Override
    protected void initView() {

        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview);

    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        widthPic = (PublicUtils.getScreenWidth(activity) - UIUtils.getDimens(R.dimen.font_36)) / 2;
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        buildRecyclerView();
    }

    @Override
    public void onClick(View v) {
    }

    private void buildRecyclerView() {
        MultiItemTypeSupport<ImageDataBean> support = new MultiItemTypeSupport<ImageDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == 0) {
                    return R.layout.item_top_image;
                } else {
                    return R.layout.item_top_image;
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

        mAdapter = new MultiItemCommonAdapter<ImageDataBean>(activity, mListDataImage, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {

                if (PublicUtils.ifHasWriteQuan(activity)) {
                    if (mListDataImage.get(position).getItem_info().getImage().getRatio() > 0.6) {
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.iv_item_pic).getLayoutParams();
                        layoutParams.height = (int) (widthPic / mListDataImage.get(position).getItem_info().getImage().getRatio());
                        layoutParams.width = widthPic;
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        holder.getView(R.id.iv_item_pic).setLayoutParams(layoutParams);
                        ImageUtils.displayFilletHalfImage(mListDataImage.get(position).getItem_info().getImage().getImg1(),
                                (TopCropImageView) holder.getView(R.id.iv_item_pic));
                    } else {
                        if (mListDataImage.get(position).getItem_info().getImage().getRatio() > 0.333) {
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.iv_item_pic).getLayoutParams();
                            layoutParams.height = (int) (widthPic / mListDataImage.get(position).getItem_info().getImage().getRatio());
                            layoutParams.width = widthPic;
                            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            holder.getView(R.id.iv_item_pic).setLayoutParams(layoutParams);
                            GlideImgManager.glideLoader(activity, mListDataImage.get(position).getItem_info().getImage().getImg1(), R.color.white, R.color.white, (TopCropImageView) holder.getView(R.id.iv_item_pic), 1);
                        } else {
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.iv_item_pic).getLayoutParams();
                            layoutParams.height = widthPic * 3;
                            layoutParams.width = widthPic;
                            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            holder.getView(R.id.iv_item_pic).setLayoutParams(layoutParams);
                            GlideImgManager.glideLoader(activity, mListDataImage.get(position).getItem_info().getImage().getImg1(), R.color.white, R.color.white, (TopCropImageView) holder.getView(R.id.iv_item_pic), 1);
                        }
                    }
                } else {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.iv_item_pic).getLayoutParams();
                    layoutParams.height = (int) (widthPic / mListDataImage.get(position).getItem_info().getImage().getRatio());
                    layoutParams.width = widthPic;
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    holder.getView(R.id.iv_item_pic).setLayoutParams(layoutParams);
                    GlideImgManager.glideLoader(activity, mListDataImage.get(position).getItem_info().getImage().getImg1(), R.color.white, R.color.white, (TopCropImageView) holder.getView(R.id.iv_item_pic), 1);
                }
                holder.getView(R.id.iv_item_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String> item_id_list = new ArrayList<>();
                        item_id_list.add(mListDataImage.get(position).getItem_info().getItem_id());

                        NewImageDetailsFragment newImageDetailsFragment = new NewImageDetailsFragment(fragmentManager);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("item_id", mListDataImage.get(position).getItem_info().getItem_id());
                        bundle.putInt("position", 0);
                        bundle.putString("type", "single");
                        bundle.putSerializable("item_id_list", (Serializable) item_id_list);
                        newImageDetailsFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.addToBackStack(null).replace(R.id.id_main, newImageDetailsFragment);
                        fragmentTransaction.commit();

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
                        ToastUtils.showCenter(activity, error_msg);
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

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("我的图片列表页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("我的图片列表页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的图片列表页");
    }
}