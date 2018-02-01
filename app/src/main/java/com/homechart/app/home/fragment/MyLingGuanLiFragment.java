package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
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
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationBean;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationListBean;
import com.homechart.app.lingganji.ui.activity.InspirationDetailActivity;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.glide.GlideImgManager;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class MyLingGuanLiFragment
        extends BaseFragment
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener ,NewInspirationDetailsment.ClickDelete {

    private String user_id;
    private FragmentManager fragmentManager;
    private TextView mTital;
    private TextView mRightCreate;
    private ImageButton mBack;
    private String mUserId;
    private HRecyclerView mRecyclerView;
    private MultiItemCommonAdapter<InspirationBean> mAdapter;
    private List<InspirationBean> mListData = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int page_num = 1;
    private int position;
    private int widthPic;
    private RelativeLayout rl_no_data;

    public MyLingGuanLiFragment() {
    }

    public MyLingGuanLiFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public MyLingGuanLiFragment(String user_id,FragmentManager fragmentManager) {

        this.user_id = user_id;
        this.fragmentManager = fragmentManager;

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_my_lingganji;
    }

    @Override
    protected void initView() {

        rl_no_data = (RelativeLayout) rootView.findViewById(R.id.rl_no_data);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {


        widthPic = (PublicUtils.getScreenWidth(activity) - UIUtils.getDimens(R.dimen.font_30)) / 2;
        buildRecyclerView();

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

    private void buildRecyclerView() {
        MultiItemTypeSupport<InspirationBean> support = new MultiItemTypeSupport<InspirationBean>() {
            @Override
            public int getLayoutId(int itemType) {

                if (itemType == 0) {
                    return R.layout.item_my_inspirationlist_left;
                } else {
                    return R.layout.item_my_inspirationlist_right;
                }
            }

            @Override
            public int getItemViewType(int position, InspirationBean s) {
                if (position % 2 == 0) {
                    return 0;
                } else {
                    return 1;
                }

            }
        };

        mAdapter = new MultiItemCommonAdapter<InspirationBean>(activity, mListData, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {

                ((TextView) holder.getView(R.id.tv_item_name)).setText(mListData.get(position).getAlbum_info().getAlbum_name());
                ((TextView) holder.getView(R.id.tv_item_num_pic)).setText(mListData.get(position).getAlbum_info().getItem_num() + "张");
                ((TextView) holder.getView(R.id.tv_item_num_dingyue)).setText(mListData.get(position).getAlbum_info().getSubscribe_num() + "收藏");
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.iv_item_pic).getLayoutParams();
                layoutParams.height = widthPic;
                layoutParams.width = widthPic;
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                holder.getView(R.id.iv_item_pic).setLayoutParams(layoutParams);
                if (TextUtils.isEmpty(mListData.get(position).getAlbum_info().getCover_image().getImg0())) {
                    GlideImgManager.glideLoader(activity, "", R.drawable.moren, R.drawable.moren, (ImageView) holder.getView(R.id.iv_item_pic));
                } else {
                    ImageUtils.displayFilletHalfImage(mListData.get(position).getAlbum_info().getCover_image().getImg0(), (ImageView) holder.getView(R.id.iv_item_pic));
                }
                holder.getView(R.id.rl_item_inspiration).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewInspirationDetailsment newInspirationDetailsment = new NewInspirationDetailsment(fragmentManager);
                        newInspirationDetailsment.setReflushList(MyLingGuanLiFragment.this);
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id", user_id);
                        bundle.putString("tag", "true");
                        bundle.putString("ifshowtital", "true");
                        bundle.putString("album_id",  mListData.get(position).getAlbum_info().getAlbum_id());
                        bundle.putString("show_type",  mListData.get(position).getAlbum_info().getShow_type());
                        newInspirationDetailsment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.id_main, newInspirationDetailsment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
//                        Intent intent = new Intent(activity, InspirationDetailActivity.class);
//                        intent.putExtra("user_id", user_id);
//                        intent.putExtra("tag", "true");
//                        intent.putExtra("ifshowtital", "true");
//                        intent.putExtra("album_id", mListData.get(position).getAlbum_info().getAlbum_id());
//                        startActivityForResult(intent, 3);
                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnRefreshListener(this);
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
                ToastUtils.showCenter(activity, "专辑列表获取失败！");
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
        MyHttpManager.getInstance().getUserInspirationSeries(user_id, (page_num - 1) * 20 + "", "20", callBack);
    }

    private void updateViewFromData(List<InspirationBean> listData, String state) {

        switch (state) {

            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData && listData.size() > 0) {
                    rl_no_data.setVisibility(View.GONE);
                    mListData.addAll(listData);
                } else {
                    rl_no_data.setVisibility(View.VISIBLE);
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                    page_num = 1;
                    mListData.clear();
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setRefreshing(false);//刷新完毕
                break;

            case LOADMORE_STATUS:
                if (null != listData && listData.size() > 0) {

                    rl_no_data.setVisibility(View.GONE);
                    position = mListData.size();
                    mListData.addAll(listData);
                    mAdapter.notifyItem(position, mListData, listData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                } else {
                    if(mListData.size() == 0){
                        rl_no_data.setVisibility(View.VISIBLE);
                    }
                    --page_num;
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 ){

            onRefresh();
        }

    }

    @Override
    public void clickDelete() {
        onRefresh();
    }
}