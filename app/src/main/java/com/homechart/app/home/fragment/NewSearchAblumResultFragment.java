package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.flyco.tablayout.CustomViewPagerTab;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.activity.LoginActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.search.SearchDataBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.bean.searchablums.SearchBean;
import com.homechart.app.home.bean.searchablums.SearchItemBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.ui.activity.InspirationSeriesActivity;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.myview.RoundImageView;
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
import com.homechart.app.visearch.SearchLoadingActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

@SuppressLint("ValidFragment")
public class NewSearchAblumResultFragment
        extends BaseFragment
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener ,
        NewInspirationDetailsment.ClickDelete {

    private HRecyclerView mRecyclerView;
    private RelativeLayout rl_no_data;
    private int width_Pic_Staggered;
    private int page_num = 1;
    private int TYPE_ONE = 1;
    private int TYPE_TWO = 2;
    private MultiItemCommonAdapter<SearchItemBean> mAdapter;
    private LoadMoreFooterView mLoadMoreFooterView;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private List<SearchItemBean> mListData = new ArrayList<>();
    private int position;
    private boolean loginStatus;
    private Bundle bundle;
    private int widthPic;

    public NewSearchAblumResultFragment() {
    }

    public NewSearchAblumResultFragment(FragmentManager fragmentManager, String search_tag, String search_info) {
        this.fragmentManager = fragmentManager;
        this.search_tag = search_tag;
        this.search_info = search_info;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_search_result;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        bundle = getArguments();
        search_info =  bundle.getString("search_info");
        search_tag =  bundle.getString("search_tag");
    }

    @Override
    protected void initView() {

        cet_clearedit = (ClearEditText) rootView.findViewById(R.id.cet_clearedit);
        tv_quxiao = (TextView) rootView.findViewById(R.id.tv_quxiao);

        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_info);
        rl_no_data = (RelativeLayout) rootView.findViewById(R.id.rl_no_data);

        //初始位置
//        mViewPager.setCurrentItem(0);

    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_quxiao.setOnClickListener(this);
        cet_clearedit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(activity.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    String searchContext = cet_clearedit.getText().toString().trim();
                    if (TextUtils.isEmpty(searchContext.trim())) {
                        ToastUtils.showCenter(activity, "请输入搜索内容");
                    } else {
                        search_info = searchContext;
                        search_tag = "";
                        onRefresh();
//                        searchPicFragment.setSearchInfo(search_info);
                    }

                    return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        widthPic = (PublicUtils.getScreenWidth(activity) - UIUtils.getDimens(R.dimen.font_30)) / 2;
        if (!TextUtils.isEmpty(search_info)) {
            cet_clearedit.setText(search_info);
        } else {
            cet_clearedit.setText(search_tag);
        }
        cet_clearedit.setSelection(cet_clearedit.getText().length());

        width_Pic_Staggered = PublicUtils.getScreenWidth(activity) / 2 - UIUtils.getDimens(R.dimen.font_20);
        buildRecyclerView();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_quxiao:
                fragmentManager.popBackStack();
                break;
        }
    }

    private void buildRecyclerView() {

        MultiItemTypeSupport<SearchItemBean> support = new MultiItemTypeSupport<SearchItemBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == 0) {
                    return R.layout.item_dingyue_list_left;
                } else {
                    return R.layout.item_dingyue_list_right;
                }
            }

            @Override
            public int getItemViewType(int position, SearchItemBean s) {
                if (position % 2 == 0) {
                    return 0;
                } else {
                    return 1;
                }
            }
        };

        mAdapter = new MultiItemCommonAdapter<SearchItemBean>(activity, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {

                final String album_id = mListData.get(position).getAlbum_info().getAlbum_id();
                ((TextView) holder.getView(R.id.tv_item_name)).setText(mListData.get(position).getAlbum_info().getAlbum_name());
                ((TextView) holder.getView(R.id.tv_item_num_pic)).setText(mListData.get(position).getAlbum_info().getItem_num() + "张");
                ((TextView) holder.getView(R.id.tv_item_num_dingyue)).setVisibility(View.GONE);
                ImageUtils.displayRoundImage(mListData.get(position).getUser_info().getAvatar().getThumb(), (RoundImageView) holder.getView(R.id.riv_header));
                holder.getView(R.id.riv_header).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewUserInfoFragment newUserInfoFragment = new NewUserInfoFragment(fragmentManager);
                        Bundle bundle = new Bundle();
                        bundle.putString(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                        newUserInfoFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.id_main, newUserInfoFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.iv_shoucang_image).getLayoutParams();
                layoutParams.width = widthPic;
                layoutParams.height = widthPic;
                holder.getView(R.id.iv_shoucang_image).setLayoutParams(layoutParams);
                if (TextUtils.isEmpty(mListData.get(position).getAlbum_info().getCover_image())) {
                    GlideImgManager.glideLoader(activity, "", R.drawable.moren, R.drawable.moren, (ImageView) holder.getView(R.id.iv_shoucang_image));
                } else {
                    ImageUtils.displayFilletHalfImage(mListData.get(position).getAlbum_info().getCover_image(),
                            (ImageView) holder.getView(R.id.iv_shoucang_image));
                }

                holder.getView(R.id.rl_item_dingyue).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            NewInspirationDetailsment newInspirationDetailsment = new NewInspirationDetailsment(fragmentManager);
                            newInspirationDetailsment.setReflushList(NewSearchAblumResultFragment.this);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("ifHideEdit", true);
                            bundle.putString("album_id", mListData.get(position).getAlbum_info().getAlbum_id());
                            bundle.putString("show_type", mListData.get(position).getAlbum_info().getShow_type());
                            newInspirationDetailsment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.id_main, newInspirationDetailsment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                    }
                });

            }
        };

        mRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        //友盟统计
        HashMap<String, String> map7 = new HashMap<String, String>();
        map7.put("evenname", "搜索结果页加载次数");
        map7.put("even", "搜索结果页");
        MobclickAgent.onEvent(activity, "shijian12", map7);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("搜索结果页")  //事件类别
                .setAction("搜索结果页加载次数")      //事件操作
                .build());

        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getListData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {
        //友盟统计
        HashMap<String, String> map7 = new HashMap<String, String>();
        map7.put("evenname", "搜索结果页加载次数");
        map7.put("even", "搜索结果页");
        MobclickAgent.onEvent(activity, "shijian12", map7);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("搜索结果页")  //事件类别
                .setAction("搜索结果页加载次数")      //事件操作
                .build());
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
                        SearchBean searchDataBean = GsonUtil.jsonToBean(data_msg, SearchBean.class);
                        if (null != searchDataBean.getAlbum_list() && 0 != searchDataBean.getAlbum_list().size()) {
                            changeNone(0);
                            updateViewFromData(searchDataBean.getAlbum_list(), state);
                        } else {
//                            ToastUtils.showCenter(NewSearchResultActivity.this, "暂时没搜到您要的结果，不如换个关键词试试？");
                            updateViewFromData(null, state);
                            changeNone(1);
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
        MyHttpManager.getInstance().searchAblums(search_info, (page_num - 1) * 20 + "", "20", callBack);
    }


    private void updateViewFromData(List<SearchItemBean> listData, String state) {

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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("搜索列表页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("搜索列表页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("搜索列表页");
    }


    private FragmentManager fragmentManager;
    private Bundle mBundle;
    private String search_info;
    private String search_tag;
    private ClearEditText cet_clearedit;
    private TextView tv_quxiao;
    private final String[] mTitles = {"图片"};
    private CustomViewPagerTab mViewPager;
    private SlidingTabLayout mTabLayout;
    //页卡标题集合
    private List<Fragment> mFragmentsList = new ArrayList<>();//页卡视图集合
    List<String> listTag = new ArrayList<>();

    @Override
    public void clickDelete() {
        onRefresh();
    }
}