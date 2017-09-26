package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.historyshibie.ItemListBean;
import com.homechart.app.home.bean.historyshibie.ShiBieBean;
import com.homechart.app.home.bean.searchartile.ArticleBean;
import com.homechart.app.home.bean.shoucang.ShouCangBean;
import com.homechart.app.home.bean.shoucang.ShouCangItemBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.visearch.EditPhotoActivity;
import com.homechart.app.visearch.PhotoActivity;
import com.homechart.app.visearch.SearchLoadingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gumenghao on 17/9/26.
 */

public class ShiBieActivity
        extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener {
    private ImageButton ibBack;
    private HRecyclerView mRecyclerView;
    private List<ItemListBean> mListData = new ArrayList<>();
    private CommonAdapter<ItemListBean> mAdapter;
    private LoadMoreFooterView mLoadMoreFooterView;
    private int page_num = 1;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private String user_id;
    private RelativeLayout rl_no_data;
    private int position;
    private int screenWid;
    private TextView tv_tital_comment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shibie_jilu;
    }

    @Override
    protected void initView() {
        user_id = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
        ibBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview);
        rl_no_data = (RelativeLayout) findViewById(R.id.rl_no_data);
    }

    @Override
    protected void initListener() {
        super.initListener();
        ibBack.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        screenWid = PublicUtils.getScreenWidth(this);
        tv_tital_comment.setText("识别纪录");
        buildRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ShiBieActivity.this.finish();
                break;
        }
    }

    private void buildRecyclerView() {

        mAdapter = new CommonAdapter<ItemListBean>(ShiBieActivity.this, R.layout.item_shibie_history, mListData) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {
                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_article_image).getLayoutParams();
                layoutParams.width = screenWid / 3;
                layoutParams.height = screenWid / 3;
                holder.getView(R.id.iv_article_image).setLayoutParams(layoutParams);
                ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage_url(), (ImageView) holder.getView(R.id.iv_article_image));

                holder.getView(R.id.iv_article_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent1 = new Intent(ShiBieActivity.this, SearchLoadingActivity.class);
                        intent1.putExtra("image_url", mListData.get(position).getItem_info().getImage_url());
                        intent1.putExtra("type", "lishi");
                        intent1.putExtra("image_id", mListData.get(position).getItem_info().getImage_id());
                        intent1.putExtra("image_type", "network");
                        startActivity(intent1);
                    }
                });
            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new GridLayoutManager(ShiBieActivity.this, 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(mAdapter);
//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setDuration(500);

        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);
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
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getListData(LOADMORE_STATUS);
    }

    private void getListData(final String state) {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                ToastUtils.showCenter(ShiBieActivity.this, UIUtils.getString(R.string.error_shoucang));
                if (state.equals(LOADMORE_STATUS)) {
                    --page_num;
                } else {
                    page_num = 1;
                }
            }

            @Override
            public void onResponse(String response) {

                try {
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                        String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                        String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                        if (error_code == 0) {
                            ShiBieBean shiBieBean = GsonUtil.jsonToBean(data_msg, ShiBieBean.class);
                            if (null != shiBieBean.getItem_list() && 0 != shiBieBean.getItem_list().size()) {
                                changeNone(0);
                                updateViewFromData(shiBieBean.getItem_list(), state);
                            } else {
                                changeNone(1);
                                updateViewFromData(null, state);
                            }
                        } else {
                            ToastUtils.showCenter(ShiBieActivity.this, error_msg);
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
                    ToastUtils.showCenter(ShiBieActivity.this, "识别数据加载失败，请重新刷新加载");
                }
            }
        };
        MyHttpManager.getInstance().historyShiBie(user_id, (page_num - 1) * 21, "21", callback);
    }

    private void updateViewFromData(List<ItemListBean> listData, String state) {

        switch (state) {

            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData) {
                    rl_no_data.setVisibility(View.GONE);
                    mListData.addAll(listData);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setRefreshing(false);//刷新完毕
                } else {
                    page_num = 1;
                    mListData.clear();
                    mRecyclerView.setRefreshing(false);//刷新完毕
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                }
                break;

            case LOADMORE_STATUS:
                if (null != listData) {
                    rl_no_data.setVisibility(View.GONE);
                    position = mListData.size();
                    mListData.addAll(listData);
                    mAdapter.notifyItem(position, mListData, listData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                } else {
                    --page_num;
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                }
                break;
        }
    }

    private void changeNone(int i) {
        if (i == 0) {
            rl_no_data.setVisibility(View.GONE);
        } else if (i == 1) {
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
            if (mListData.size() > 0) {
                rl_no_data.setVisibility(View.GONE);
            } else {
                rl_no_data.setVisibility(View.VISIBLE);
            }

        }
    }

}
