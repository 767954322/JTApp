package com.homechart.app.lingganji.ui.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.homechart.app.commont.contract.AppBarStateChangeListener;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.base.BaseActivity1;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.common.entity.inspirationdetail.InspirationDetailBean;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationBean;
import com.homechart.app.lingganji.common.entity.inspirationpics.InsPicItemBean;
import com.homechart.app.lingganji.common.entity.inspirationpics.InsPicsBean;
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

import android.support.v7.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gumenghao on 18/1/4.
 */

public class InspirationDetailActivity extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener {
    private String mUserId;
    private InspirationBean mInspirationBean;
    private TextView mTital;
    private TextView mRightCreate;
    private ImageButton mBack;
    private ImageButton mRightIcon;
    private int widthPic;
    private MultiItemCommonAdapter<InsPicItemBean> mAdapter;
    private List<InsPicItemBean> mListData = new ArrayList<>();
    private HRecyclerView mRecyclerView;
    private LoadMoreFooterView mLoadMoreFooterView;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int page_num = 1;
    private int position;
    private String mAlbumId;
    private View mHeaderInspirationPic;
    private Toolbar toolbar;
    private CoordinatorLayout mCoordinator;
    private RelativeLayout mTopRelative;
    private AppBarLayout mAppbar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_inspiration_details;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        mUserId = getIntent().getStringExtra("user_id");
        mInspirationBean = (InspirationBean) getIntent().getSerializableExtra("InspirationBean");
    }

    @Override
    protected void initView() {

        mHeaderInspirationPic = LayoutInflater.from(InspirationDetailActivity.this).inflate(R.layout.header_inspiration_detail, null);
        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRightIcon = (ImageButton) findViewById(R.id.nav_secondary_imageButton);
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mRecyclerView = (HRecyclerView) this.findViewById(R.id.rcy_recyclerview);
        mCoordinator = (CoordinatorLayout) this.findViewById(R.id.cl_coordinator);
        mTopRelative = (RelativeLayout) this.findViewById(R.id.rl_top_check);
        mAppbar = (AppBarLayout) this.findViewById(R.id.appbar);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
        mRightIcon.setOnClickListener(this);
        mAppbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    mTopRelative.setVisibility(View.GONE);
                    Log.d("test", "展开状态");

                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    mTopRelative.setVisibility(View.VISIBLE);
                    Log.d("test", "折叠状态");

                } else {
                    //中间状态
                    mTopRelative.setVisibility(View.GONE);
                    Log.d("test", "中间状态");
                }
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTital.setText("灵感辑");
        widthPic = (PublicUtils.getScreenWidth(this) - UIUtils.getDimens(R.dimen.font_30)) / 2;
        mAlbumId = mInspirationBean.getAlbum_info().getAlbum_id();
        buildRecyclerView();
        getInspirationDetail();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.nav_left_imageButton) {
            InspirationDetailActivity.this.finish();
        } else if (id == R.id.nav_secondary_imageButton) {

        }
    }

    private void getInspirationDetail() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(InspirationDetailActivity.this, "专辑详情获取失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        String newData = "{\"info\": " + data_msg + "}";
                        InspirationDetailBean inspirationDetailBean = GsonUtil.jsonToBean(newData, InspirationDetailBean.class);

                    } else {
                        ToastUtils.showCenter(InspirationDetailActivity.this, "专辑详情获取失败！");
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getInspirationDetail(mInspirationBean.getAlbum_info().getAlbum_id(), callBack);
    }

    private void buildRecyclerView() {
        MultiItemTypeSupport<InsPicItemBean> support = new MultiItemTypeSupport<InsPicItemBean>() {
            @Override
            public int getLayoutId(int itemType) {

                if (itemType == 0) {
                    return R.layout.item_my_inspirationpic_left;
                } else {
                    return R.layout.item_my_inspirationpic_right;
                }
            }

            @Override
            public int getItemViewType(int position, InsPicItemBean s) {
                if (position % 2 == 0) {
                    return 0;
                } else {
                    return 1;
                }

            }
        };

        mAdapter = new MultiItemCommonAdapter<InsPicItemBean>(InspirationDetailActivity.this, mListData, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {

                ((TextView) holder.getView(R.id.tv_item_miaosu)).setText(mListData.get(position).getItem_info().getDescription());
                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_item_pic).getLayoutParams();
                layoutParams.height = widthPic;
                layoutParams.width = widthPic;
                holder.getView(R.id.iv_item_pic).setLayoutParams(layoutParams);
                ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg0(), (ImageView) holder.getView(R.id.iv_item_pic));
            }
        };
//        mRecyclerView.addHeaderView(mHeaderInspirationPic);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
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
        getInspirationPicsData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getInspirationPicsData(LOADMORE_STATUS);
    }

    private void getInspirationPicsData(final String state) {
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
                ToastUtils.showCenter(InspirationDetailActivity.this, "专辑列表获取失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        InsPicsBean insPicsBean = GsonUtil.jsonToBean(data_msg, InsPicsBean.class);
                        if (null != insPicsBean.getItem_list() && 0 != insPicsBean.getItem_list().size()) {
                            updateViewFromData(insPicsBean.getItem_list(), state);
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
        MyHttpManager.getInstance().getUserInspirationPics(mAlbumId, (page_num - 1) * 20 + "", "20", callBack);
    }

    private void updateViewFromData(List<InsPicItemBean> listData, String state) {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
