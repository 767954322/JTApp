package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.activity.ArticleDetailsActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.searchartile.ArticleBean;
import com.homechart.app.home.bean.searchartile.ArticleListBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.homechart.app.R.id.tv_content_right;

@SuppressLint("ValidFragment")
public class ShaiJiaArticleFragment
        extends BaseFragment
        implements View.OnClickListener,
        CommonAdapter.OnItemClickListener,
        OnLoadMoreListener,
        OnRefreshListener {

    private FragmentManager fragmentManager;
    private ImageView iv_delete_icon;
    private RelativeLayout rl_below;
    private RelativeLayout rl_no_data;
    private TextView tv_shoucang_two;
    private HRecyclerView mRecyclerView;
    private String user_id;
    private List<ArticleBean> mListData = new ArrayList<>();
    private Map<String, ArticleBean> map_delete = new HashMap<>();//选择的唯一标示
    private ChangeArticleUI mChangeUI;
    private CommonAdapter<ArticleBean> mAdapter;

    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int position;
    private int page_num = 1;
    private int guanli_tag = 0;//0:未打开管理   1:打开管理
    private int num_checked = 0; //选择的个数
    private LoadMoreFooterView mLoadMoreFooterView;

    public ShaiJiaArticleFragment() {
    }

    public ShaiJiaArticleFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public ShaiJiaArticleFragment(String user_id, ChangeArticleUI changeUI) {

        this.user_id = user_id;
        this.mChangeUI = changeUI;

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_shaijia_article;
    }

    @Override
    protected void initView() {

        rl_below = (RelativeLayout) rootView.findViewById(R.id.rl_below);
        rl_no_data = (RelativeLayout) rootView.findViewById(R.id.rl_no_data);
        iv_delete_icon = (ImageView) rootView.findViewById(R.id.iv_delete_icon);
        tv_shoucang_two = (TextView) rootView.findViewById(R.id.tv_shoucang_two);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_shoucang);

    }

    @Override
    protected void initListener() {
        iv_delete_icon.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        buildRecyclerView();

    }

    private void buildRecyclerView() {

        mAdapter = new CommonAdapter<ArticleBean>(activity, R.layout.item_article, mListData) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {
                ((TextView) holder.getView(R.id.tv_article_name)).setText(mListData.get(position).getArticle_info().getTitle());
                ((TextView) holder.getView(R.id.tv_article_details)).setText(mListData.get(position).getArticle_info().getSummary());

                try {
                    ((TextView) holder.getView(R.id.tv_youlan_num)).setText(mListSeeNumArticle.get(position) + "");
                } catch (Exception e) {
                    ((TextView) holder.getView(R.id.tv_youlan_num)).setText(0 + "");

                }

                final String item_id = mListData.get(position).getArticle_info().getArticle_id();
                if (guanli_tag == 0) {
                    holder.getView(R.id.cb_check).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.cb_check).setVisibility(View.VISIBLE);

                }
                if (mListData.get(position).getArticle_info().getArticle_id().equals(holder.getView(R.id.iv_article_image).getTag())) {

                } else {
                    holder.getView(R.id.iv_article_image).setTag(mListData.get(position).getArticle_info().getArticle_id());
                    ImageUtils.displayFilletImage(mListData.get(position).getArticle_info().getImage().getImg0(),
                            (ImageView) holder.getView(R.id.iv_article_image));
                }
                ((CheckBox) holder.getView(R.id.cb_check)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ++num_checked;
                            if (!map_delete.containsKey(item_id)) {
                                map_delete.put(item_id, mListData.get(position));
                            }
                        } else {
                            if (map_delete.containsKey(item_id)) {
                                map_delete.remove(item_id);
                            }
                            --num_checked;
                        }
                        upCheckedStatus();
                    }
                });
                holder.getView(R.id.rl_item_click).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (guanli_tag == 0) {//未打开管理
                            jumpImageDetail(mListData.get(position).getArticle_info().getArticle_id(), position);
                        } else {
                            if (((CheckBox) holder.getView(R.id.cb_check)).isChecked()) {
                                ((CheckBox) holder.getView(R.id.cb_check)).setChecked(false);
                            } else {
                                ((CheckBox) holder.getView(R.id.cb_check)).setChecked(true);
                            }
                        }

                    }
                });

                if (map_delete.containsKey(item_id)) {
                    ((CheckBox) holder.getView(R.id.cb_check)).setChecked(true);
                } else {
                    ((CheckBox) holder.getView(R.id.cb_check)).setChecked(false);
                }

            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_delete_icon:

                if (map_delete.size() > 0) {
                    CustomProgress.show(activity, "正在删除...", false, null);
                    String delete_items = "";
                    for (String key : map_delete.keySet()) {
                        delete_items = delete_items + key + ",";
                    }
                    delete_items = delete_items.substring(0, delete_items.length() - 1);
                    OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            CustomProgress.cancelDialog();
                            ToastUtils.showCenter(activity, getString(R.string.error_delete_shaijia));
                        }

                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                                String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                                String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                                if (error_code == 0) {
                                    for (String key : map_delete.keySet()) {
                                        mListData.remove(map_delete.get(key));
                                    }
                                    map_delete.clear();
                                    upCheckedStatus();
                                    mAdapter.notifyDataSetChanged();
                                    if (mListData == null || mListData.size() == 0) {
                                        handler.sendEmptyMessage(0);
                                    }
                                    CustomProgress.cancelDialog();
                                    ToastUtils.showCenter(activity, getString(R.string.succes_delete_shaijia));
                                } else {
                                    CustomProgress.cancelDialog();
                                    ToastUtils.showCenter(activity, error_msg);
                                }
                            } catch (JSONException e) {
                                CustomProgress.cancelDialog();
                                ToastUtils.showCenter(activity, getString(R.string.error_delete_shaijia));
                            }
                        }
                    };
                    MyHttpManager.getInstance().deleteShaiJiaArticle(delete_items, callBack);
                } else {
                    ToastUtils.showCenter(activity, "请选择删除项");
                }

                break;
        }

    }

    public void upCheckedStatus() {
        tv_shoucang_two.setText(map_delete.size() + "");
    }


    //查看图片详情
    private void jumpImageDetail(String item_id, int position) {
        try {
            mListSeeNumArticle.set(position, mListSeeNumArticle.get(position) + 1);
        }catch (Exception e){
        }

        Intent intent = new Intent(activity, ArticleDetailsActivity.class);
        intent.putExtra("article_id", item_id);
        startActivity(intent);

    }

    public void clickRightGuanLi() {
        if (mListData != null && mListData.size() > 0) {
            if (guanli_tag == 0) {
                //打开管理
                mChangeUI.ifShowArticleDelete(true);
                map_delete.clear();
                guanli_tag = 1;
                num_checked = 0;
                tv_shoucang_two.setText(num_checked + "");
                rl_below.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            } else {
                //关闭管理
                mChangeUI.ifShowArticleDelete(false);
                map_delete.clear();
                guanli_tag = 0;
                rl_below.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            ToastUtils.showCenter(activity, "先去发布一些图片吧");
        }
    }

    public boolean ifHasData() {
        if (mListData != null && mListData.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //关闭管理
            rl_no_data.setVisibility(View.VISIBLE);
            map_delete.clear();
            rl_below.setVisibility(View.GONE);
            mChangeUI.ifShowArticleDelete(false);
        }
    };

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

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

    }

    public interface ChangeArticleUI {
        public void ifShowArticleDelete(boolean bool);
    }

    private void getListData(final String state) {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                ToastUtils.showCenter(activity, UIUtils.getString(R.string.error_shaijia));
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
                            ArticleListBean articleListBean = GsonUtil.jsonToBean(data_msg, ArticleListBean.class);
                            if (null != articleListBean.getArticle_list() && 0 != articleListBean.getArticle_list().size()) {
                                changeNone(0);
                                getSeeNum(articleListBean.getArticle_list(), state);
                                updateViewFromData(articleListBean.getArticle_list(), state);
                            } else {
                                changeNone(1);
                                updateViewFromData(null, state);
                            }
                        } else {
                            ToastUtils.showCenter(activity, error_msg);
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
                    ToastUtils.showCenter(activity, UIUtils.getString(R.string.error_shaijia));
                }
            }
        };
        MyHttpManager.getInstance().getShaiJiaArticleList(user_id, (page_num - 1) * 20, "20", callback);
    }

    private List<Integer> mListSeeNumArticle = new ArrayList<>();

    private void getSeeNum(List<ArticleBean> listData, String state) {

        try {
            switch (state) {
                case REFRESH_STATUS:
                    if (null != listData) {
                        mListSeeNumArticle.clear();
                        for (int i = 0; i < listData.size(); i++) {
                            mListSeeNumArticle.add(Integer.parseInt(listData.get(i).getArticle_info().getView_num().trim()));
                        }
                    }
                    break;

                case LOADMORE_STATUS:
                    if (null != listData) {
                        for (int i = 0; i < listData.size(); i++) {
                            mListSeeNumArticle.add(Integer.parseInt(listData.get(i).getArticle_info().getView_num().trim()));
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            Log.d("test", "检查下是不是本地代理掉线啦");
        }
    }

    private void updateViewFromData(List<ArticleBean> listData, String state) {

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
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
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