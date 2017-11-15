package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.activity.ArticleDetailsActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.searchartile.ArticleBean;
import com.homechart.app.home.bean.searchartile.ArticleListBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
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
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("ValidFragment")
public class SearchArticleFragment
        extends BaseFragment
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener {


    private FragmentManager fragmentManager;
    private HRecyclerView mRecyclerView;
    private RelativeLayout rl_no_data;
    private int page_num = 1;
    private int TYPE_ONE = 1;
    private int TYPE_TWO = 2;
    private MultiItemCommonAdapter<ArticleBean> mAdapter;
    private LoadMoreFooterView mLoadMoreFooterView;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private List<ArticleBean> mListData = new ArrayList<>();
    private List<Integer> mListDataHeight = new ArrayList<>();
    private String search_info;
    private String search_tag;
    private int position;

    public SearchArticleFragment() {
    }
    public SearchArticleFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public SearchArticleFragment(String search_info, String search_tag) {
        this.search_info = search_info;
        this.search_tag = search_tag;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_search_article;
    }

    @Override
    protected void initView() {
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_info);
        rl_no_data = (RelativeLayout) rootView.findViewById(R.id.rl_no_data);
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        buildRecyclerView();
    }

    @Override
    public void onClick(View v) {

    }

    public void setSearchInfo(String search_info) {

        this.search_info = search_info;
        search_tag = "";
        getListData(REFRESH_STATUS);

    }

    private void buildRecyclerView() {

        MultiItemTypeSupport<ArticleBean> support = new MultiItemTypeSupport<ArticleBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_ONE) {
                    return R.layout.item_search_article;
                } else {
                    return R.layout.item_search_article;
                }
            }

            @Override
            public int getItemViewType(int position, ArticleBean s) {
                return TYPE_ONE;
            }
        };

        mAdapter = new MultiItemCommonAdapter<ArticleBean>(activity, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {

                ((TextView) holder.getView(R.id.tv_article_name)).setText(mListData.get(position).getArticle_info().getTitle());
                ((TextView) holder.getView(R.id.tv_youlan_num)).setText(mListData.get(position).getArticle_info().getView_num());
                ((TextView) holder.getView(R.id.tv_article_details)).setText(mListData.get(position).getArticle_info().getSummary().trim());
                ImageUtils.disRectangleImage(mListData.get(position).getArticle_info().getImage().getImg0(), (ImageView) holder.getView(R.id.iv_article_image));
                holder.getView(R.id.rl_item_click).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickPosition = position;
                        Intent intent = new Intent(activity, ArticleDetailsActivity.class);
                        intent.putExtra("article_id",mListData.get(position).getArticle_info().getArticle_id());
                        startActivityForResult(intent,1);
                    }
                });
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
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
        getListData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {

        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getListData(LOADMORE_STATUS);

    }

    private void getListData(final String state) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                if (state.equals(LOADMORE_STATUS)) {
                    --page_num;
                } else {
                    page_num = 1;
                }
                ToastUtils.showCenter(activity, getString(R.string.search_result_error));

            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ArticleListBean articleListBean = GsonUtil.jsonToBean(data_msg, ArticleListBean.class);
                        if (null != articleListBean.getArticle_list() && 0 != articleListBean.getArticle_list().size()) {
                            changeNone(0);
                            updateViewFromData(articleListBean.getArticle_list(), state);
                        } else {
                            changeNone(1);
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
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        if (!TextUtils.isEmpty(search_info)) {

            MyHttpManager.getInstance().getArticleSearchList(search_info, (page_num - 1) * 20 + "", "20", callBack);

        } else if (!TextUtils.isEmpty(search_tag)) {

            MyHttpManager.getInstance().getArticleSearchList(search_tag, (page_num - 1) * 20 + "", "20", callBack);

        }
    }


    private void updateViewFromData(List<ArticleBean> listData, String state) {

        switch (state) {

            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData) {
                    mListData.addAll(listData);
                } else {
                    page_num = 1;
                    mListData.clear();
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setRefreshing(false);//刷新完毕
                if (mListData.size() > 0) {
                    mRecyclerView.smoothScrollToPosition(0);
                }
                break;

            case LOADMORE_STATUS:
                if (null != listData) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && clickPosition >= 0&&mListData.size()>position) {
            int seeNum = Integer.parseInt(mListData.get(clickPosition).getArticle_info().getView_num());
            mListData.get(clickPosition).getArticle_info().setView_num(++seeNum + "");
            mAdapter.notifyItemChanged(clickPosition);
        }

    }


    //设置你可能喜欢地阅览数字加1
    private int clickPosition = -1;
}