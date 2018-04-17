package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.footer.DefaultFootItem;
import com.homechart.app.footer.RecyclerViewWithFooter;
import com.homechart.app.home.activity.DingYueGuanLiActivity;
import com.homechart.app.home.activity.LoginActivity;
import com.homechart.app.home.activity.NewHuoDongDetailsActivity;
import com.homechart.app.home.activity.SearchActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.allset.AllSetBean;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.home.bean.search.ActivityInfoBean;
import com.homechart.app.home.bean.search.SearchDataBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.bean.search.SearchItemInfoDataBean;
import com.homechart.app.lingganji.ui.activity.InspirationDetailActivity;
import com.homechart.app.lingganji.ui.activity.InspirationSeriesActivity;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class HomePicFragment1
        extends BaseFragment
        implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private RecyclerViewWithFooter mRecyclerView;

    private List<SearchItemDataBean> mListData = new ArrayList<>();
    private MultiItemCommonAdapter<SearchItemDataBean> mAdapter;
    private int page_num = 1;
    private int TYPE_TWO = 2;
    private int TYPE_FOUR = 4;
    private int position;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private List<String> listTag = new ArrayList<>();
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ClearEditText cet_clearedit;
    private int width_Pic_Staggered;
    private Map<Integer, ColorItemBean> mSelectListData = new HashMap<>();
    private List<String> mItemIdList = new ArrayList<>();
    private Boolean loginStatus;
    private String userId;
    private ActivityInfoBean mActivityInfoBean;
    private String is_subscribed_tag;
    private RelativeLayout rl_go_dingyue;
    private RelativeLayout rl_close_dingyue;
    private String is_enable_item_similar;
    private MaterialRefreshLayout materialRefreshLayout;
    private DefaultFootItem defaultFootItem;
    private boolean ifLoading = false;

    public HomePicFragment1(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public HomePicFragment1() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_pic;
    }

    @Override
    protected void initView() {

        cet_clearedit = (ClearEditText) rootView.findViewById(R.id.cet_clearedit);
        mRecyclerView = (RecyclerViewWithFooter) rootView.findViewById(R.id.rcy_recyclerview_pic);
        rl_close_dingyue = (RelativeLayout) rootView.findViewById(R.id.rl_close_dingyue);
        rl_go_dingyue = (RelativeLayout) rootView.findViewById(R.id.rl_go_dingyue);
        materialRefreshLayout = (MaterialRefreshLayout) rootView.findViewById(R.id.refresh);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initListener() {
        super.initListener();
        cet_clearedit.setKeyListener(null);
        cet_clearedit.setOnClickListener(this);
        rl_go_dingyue.setOnClickListener(this);
        rl_close_dingyue.setOnClickListener(this);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRecyclerRefresh();
                    }
                }, 500);
            }

            @Override
            public void onfinish() {
            }

        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        width_Pic_Staggered = PublicUtils.getScreenWidth(activity) / 2 - UIUtils.getDimens(R.dimen.font_28);
        getAllSet();
        buildRecyclerView();
        is_subscribed_tag = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.IS_SUBSCRIBED_TAG);
        if (!TextUtils.isEmpty(is_subscribed_tag)) {
            //是否已订阅标签 1:是 0:否
            if (is_subscribed_tag.equals("0")) {//没订阅，显示查看订阅管理页
                rl_go_dingyue.setVisibility(View.VISIBLE);
            } else {//已经订阅过了，隐藏
                rl_go_dingyue.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cet_clearedit:
                Intent intent = new Intent(activity, SearchActivity.class);
                startActivityForResult(intent, 10);
                break;
            case R.id.rl_go_dingyue:
                loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
                userId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
                if (!loginStatus) {
                    Intent intent1 = new Intent(activity, LoginActivity.class);
                    startActivityForResult(intent1, 13);
                } else {
                    Intent intent_dingyue = new Intent(activity, DingYueGuanLiActivity.class);
                    startActivityForResult(intent_dingyue, 12);
                }
                break;
            case R.id.rl_close_dingyue:
                rl_go_dingyue.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            scrollRecyclerView();
            materialRefreshLayout.autoRefresh();//刷新完毕
        } else if (requestCode == 10 && resultCode == 10) {
            String search_tag = data.getStringExtra("search_tag");
            String search_info = data.getStringExtra("search_info");

            NewSearchDataFragment newSearchResultFragment = new NewSearchDataFragment(getChildFragmentManager(), search_tag, search_info);
            Bundle bundle = new Bundle();
            bundle.putString("search_tag", search_tag);
            bundle.putString("search_info", search_info);
            bundle.putString("type", "image");
            newSearchResultFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.id_main, newSearchResultFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
            ClassConstant.HomeStatus.IMAGE_STATUS = 1;
        } else if (requestCode == 12) {
            getAllSet();
        } else if (requestCode == 13) {
            loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
            if (loginStatus) {
                getAllSet1();
            }
        } else if (requestCode == 10 && resultCode == 17) {
            String item_id = data.getStringExtra("item_id");
            List<String> item_id_list = new ArrayList<>();
            item_id_list.add(item_id);
            NewImageDetailsFragment newImageDetailsFragment = new NewImageDetailsFragment(getChildFragmentManager());
            Bundle bundle = new Bundle();
            bundle.putSerializable("item_id", item_id);
            bundle.putInt("position", 0);
            bundle.putString("type", "single");
            bundle.putSerializable("item_id_list", (Serializable) item_id_list);
            newImageDetailsFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.id_main, newImageDetailsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
            ClassConstant.HomeStatus.IMAGE_STATUS = 1;

        } else if (requestCode == 10 && resultCode == 18) {

            String album_id = data.getStringExtra("album_id");
            NewInspirationDetailsment newInspirationDetailsment = new NewInspirationDetailsment(getChildFragmentManager());
            Bundle bundle = new Bundle();
            bundle.putBoolean("ifHideEdit", true);
            bundle.putString("album_id", album_id);
            newInspirationDetailsment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.id_main, newInspirationDetailsment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
            ClassConstant.HomeStatus.FAXIAN_STATUS = 1;
        } else if (requestCode == 10 && resultCode == 19) {

            String search_tag = data.getStringExtra("search_tag");
            String search_info = data.getStringExtra("search_info");
            NewSearchDataFragment newSearchResultFragment = new NewSearchDataFragment(getChildFragmentManager(), search_tag, search_info);
            Bundle bundle = new Bundle();
            bundle.putString("search_tag", search_tag);
            bundle.putString("search_info", search_info);
            bundle.putString("type", "album");
            newSearchResultFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.id_main, newSearchResultFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
            ClassConstant.HomeStatus.IMAGE_STATUS = 1;
        }
    }

    private void buildRecyclerView() {

        MultiItemTypeSupport<SearchItemDataBean> support = new MultiItemTypeSupport<SearchItemDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_TWO) {
                    return R.layout.item_pubu_last;
                } else if (itemType == TYPE_FOUR) {
                    return R.layout.item_test_huodong_pubu;
                } else {
                    return R.layout.item_pubu_last;
                }
            }

            @Override
            public int getItemViewType(int position, SearchItemDataBean s) {
                if (s.getItem_info().getTag().equals("活动")) {
                    return TYPE_FOUR;
                } else {
                    return TYPE_TWO;
                }
            }
        };

        mAdapter = new MultiItemCommonAdapter<SearchItemDataBean>(activity, mListData, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {
                is_enable_item_similar = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.IS_ENABLE_ITEM_SIMILAR);
                if (position == 0 && mListData.get(position).getItem_info().getTag().equals("活动") && null != mActivityInfoBean) {
                    ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();

                    layoutParams.height = (int) (width_Pic_Staggered / mActivityInfoBean.getSmall_image().getRatio());
                    holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);

                    ImageUtils.displayFilletImage(mActivityInfoBean.getSmall_image().getImg0(), (ImageView) holder.getView(R.id.iv_imageview_one));
                    holder.getView(R.id.iv_imageview_one).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            NewHuoDongDetailsFragment newHuoDongDetailsFragment = new NewHuoDongDetailsFragment(getChildFragmentManager());
                            Bundle bundle = new Bundle();
                            bundle.putString("activity_id", mActivityInfoBean.getObject_id());
                            newHuoDongDetailsFragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.id_main, newHuoDongDetailsFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            ClassConstant.HomeStatus.IMAGE_STATUS = 1;
                        }
                    });
                } else {

                    String nikeName = mListData.get(position).getUser_info().getNickname();
                    ((TextView) holder.getView(R.id.tv_name_pic)).setText(nikeName);
                    String strTag = "";
                    String tag = mListData.get(position).getItem_info().getTag();
                    if (!TextUtils.isEmpty(tag)) {
                        String[] str_tag = tag.split(" ");
                        listTag.clear();
                        for (int i = 0; i < str_tag.length; i++) {
                            if (!TextUtils.isEmpty(str_tag[i].trim())) {
                                listTag.add(str_tag[i]);
                            }
                        }
                        for (int i = 0; i < listTag.size(); i++) {
                            strTag = strTag + "#" + listTag.get(i) + "  ";
                        }
                    }

                    String str = mListData.get(position).getItem_info().getDescription() + " " + "<font color='#464646'>" + strTag + "</font>";
                    if (TextUtils.isEmpty(mListData.get(position).getItem_info().getDescription().trim()) && TextUtils.isEmpty(strTag.trim())) {
                        ((TextView) holder.getView(R.id.tv_image_miaosu)).setVisibility(View.GONE);
                    } else {
                        ((TextView) holder.getView(R.id.tv_image_miaosu)).setVisibility(View.VISIBLE);
                    }
                    ((TextView) holder.getView(R.id.tv_image_miaosu)).setText(Html.fromHtml(str));
                    ((TextView) holder.getView(R.id.tv_name_ablum)).setText(mListData.get(position).getAlbum_info().getAlbum_name());

                    if (PublicUtils.ifHasWriteQuan(activity)) {
                        if (mListData.get(position).getItem_info().getImage().getRatio() > 0.6) {
                            ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                            layoutParams.height = Math.round(width_Pic_Staggered / mListData.get(position).getItem_info().getImage().getRatio());
                            holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                            ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg1(),
                                    (ImageView) holder.getView(R.id.iv_imageview_one));
                        } else {
                            if (mListData.get(position).getItem_info().getImage().getRatio() > 0.333) {
                                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                                layoutParams.height = Math.round(width_Pic_Staggered / mListData.get(position).getItem_info().getImage().getRatio());
                                holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                                GlideImgManager.glideLoader(activity, mListData.get(position).getItem_info().getImage().getImg1(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_imageview_one), 1);
                            } else {
                                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                                layoutParams.height = width_Pic_Staggered * 3;
                                holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                                GlideImgManager.glideLoader(activity, mListData.get(position).getItem_info().getImage().getImg1(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_imageview_one), 1);
                            }
                        }
                    } else {
                        if (mListData.get(position).getItem_info().getImage().getRatio() > 0.333) {
                            ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                            layoutParams.height = Math.round(width_Pic_Staggered / mListData.get(position).getItem_info().getImage().getRatio());
                            holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                            GlideImgManager.glideLoader(activity, mListData.get(position).getItem_info().getImage().getImg1(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_imageview_one), 1);
                        } else {
                            ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                            layoutParams.height = width_Pic_Staggered * 3;
                            holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                            GlideImgManager.glideLoader(activity, mListData.get(position).getItem_info().getImage().getImg1(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_imageview_one), 1);
                        }
                    }

                    if (PublicUtils.ifHasWriteQuan(activity)) {
                        ImageUtils.displayFilletImage(mListData.get(position).getUser_info().getAvatar().getBig(),
                                (ImageView) holder.getView(R.id.iv_header_pic));
                    } else {
                        GlideImgManager.glideLoader(activity, mListData.get(position).getUser_info().getAvatar().getBig(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_header_pic), 0);
                    }

                    holder.getView(R.id.iv_imageview_one).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            NewImageDetailsFragment newImageDetailsFragment = new NewImageDetailsFragment(getChildFragmentManager());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("item_id", mListData.get(position).getItem_info().getItem_id());
                            if (mListData.get(0).getItem_info().getTag().equals("活动")) {
                                bundle.putInt("position", position - 1);
                            } else {
                                bundle.putInt("position", position);
                            }
                            bundle.putString("type", "色彩");
                            bundle.putBoolean("if_click_color", false);
                            bundle.putSerializable("mSelectListData", (Serializable) mSelectListData);
                            bundle.putString("shaixuan_tag", "");
                            bundle.putInt("page_num", page_num + 1);
                            bundle.putSerializable("item_id_list", (Serializable) mItemIdList);
                            newImageDetailsFragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                            fragmentTransaction.addToBackStack(null).replace(R.id.id_main, newImageDetailsFragment);
                            fragmentTransaction.commit();
                            ClassConstant.HomeStatus.IMAGE_STATUS = 1;
                        }
                    });

                    holder.getView(R.id.rl_test).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NewInspirationDetailsment newInspirationDetailsment = new NewInspirationDetailsment(getChildFragmentManager());
                            Bundle bundle = new Bundle();
                            bundle.putString("user_id", "");
                            bundle.putBoolean("ifHideEdit", true);
                            bundle.putString("album_id", mListData.get(position).getAlbum_info().getAlbum_id());
                            bundle.putString("show_type", mListData.get(position).getAlbum_info().getShow_type());
                            newInspirationDetailsment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.id_main, newInspirationDetailsment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });
                }

            }
        };
        defaultFootItem = new DefaultFootItem();
        mRecyclerView.setFootItem(defaultFootItem);//默认是这种
        mRecyclerView.setOnLoadMoreListener(new com.homechart.app.footer.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!ifLoading) {
                    ifLoading = true;
                    defaultFootItem.onBindData(mRecyclerView, RecyclerViewWithFooter.STATE_LOADING);
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            onRecyclerLoadMore();
                        }
                    });
                }
            }
        });
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ((DefaultItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setAdapter(mAdapter);
        scrollRecyclerView();
        materialRefreshLayout.autoRefresh();
    }

    private void onRecyclerRefresh() {
        page_num = 1;
        getListData(REFRESH_STATUS);
    }

    private void onRecyclerLoadMore() {
        ++page_num;
        getListData(LOADMORE_STATUS);
    }

    private void getListData(final String state) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ifLoading = false;
                materialRefreshLayout.finishRefresh();//刷新完毕
                defaultFootItem.onBindData(mRecyclerView, RecyclerViewWithFooter.STATE_FINISH_LOADING);
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
                        ifLoading = false;
                        SearchDataBean searchDataBean = GsonUtil.jsonToBean(data_msg, SearchDataBean.class);
                        if (state.equals(REFRESH_STATUS)) {
                            mListData.clear();
                            mActivityInfoBean = searchDataBean.getAd_info();
                            if (null != mActivityInfoBean) {
                                SearchItemDataBean searchItemDataBean = new SearchItemDataBean();
                                SearchItemInfoDataBean searchItemInfoDataBean = new SearchItemInfoDataBean();
                                searchItemInfoDataBean.setTag("活动");
                                searchItemDataBean.setItem_info(searchItemInfoDataBean);
                                mListData.add(searchItemDataBean);
                            }
                        }
                        if (null != searchDataBean.getItem_list() && 0 != searchDataBean.getItem_list().size()) {
                            updateViewFromData(searchDataBean.getItem_list(), state);
                        } else {
                            updateViewFromData(null, state);
                        }
                    } else {
                        ifLoading = false;
                        if (state.equals(LOADMORE_STATUS)) {
                            --page_num;
                            //没有更多数据
                            defaultFootItem.onBindData(mRecyclerView, RecyclerViewWithFooter.STATE_END);
                        } else {
                            page_num = 1;
                            materialRefreshLayout.finishRefresh();//刷新完毕
                        }
                        ToastUtils.showCenter(activity, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getSearchList(mSelectListData, "", "", (page_num - 1) * 20 + "", "20", callBack);
    }


    private void updateViewFromData(List<SearchItemDataBean> listData, String state) {

        switch (state) {
            case REFRESH_STATUS:
                mItemIdList.clear();
                if (null != listData && listData.size() > 0) {
                    mListData.addAll(listData);
                    for (int i = 0; i < listData.size(); i++) {
                        if (!listData.get(i).getItem_info().getTag().equals("活动")) {
                            mItemIdList.add(listData.get(i).getItem_info().getItem_id());
                        }
                    }
                } else {
                    //没有更多数据
                    defaultFootItem.onBindData(mRecyclerView, RecyclerViewWithFooter.STATE_END);
                    page_num = 1;
                    mListData.clear();
                }
                mAdapter.notifyDataSetChanged();
                materialRefreshLayout.finishRefresh();//刷新完毕
                break;

            case LOADMORE_STATUS:
                if (null != listData && listData.size() > 0) {
                    position = mListData.size();
                    mListData.addAll(listData);
                    mAdapter.notifyItem(position, mListData, listData);
                    defaultFootItem.onBindData(mRecyclerView, RecyclerViewWithFooter.STATE_FINISH_LOADING);
                    for (int i = 0; i < listData.size(); i++) {
                        if (!listData.get(i).getItem_info().getTag().equals("活动")) {
                            mItemIdList.add(listData.get(i).getItem_info().getItem_id());
                        }
                    }
                    ifLoading = false;
                } else {
                    --page_num;
                    //没有更多数据
                    defaultFootItem.onBindData(mRecyclerView, RecyclerViewWithFooter.STATE_END);
                    ifLoading = false;
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("首页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        t.setScreenName("首页");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页");
    }

    public void scrollRecyclerView() {
        if (mListData.size() > 0) {
            mRecyclerView.scrollToPosition(0);
        }
    }

    private void getAllSet() {

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
                        AllSetBean allSetBean = GsonUtil.jsonToBean(data_msg, AllSetBean.class);
                        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.IS_ENABLE_ITEM_SIMILAR, allSetBean.getConfig_info().getIs_enable_item_similar());
                        if (!TextUtils.isEmpty(allSetBean.getConfig_info().getIs_subscribed_tag())) {
                            SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.IS_SUBSCRIBED_TAG, allSetBean.getConfig_info().getIs_subscribed_tag());
                        }
                        Message message = new Message();
                        message.what = 5;
                        mHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().allSet(callBack);

    }

    private void getAllSet1() {

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
                        AllSetBean allSetBean = GsonUtil.jsonToBean(data_msg, AllSetBean.class);
                        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.IS_ENABLE_ITEM_SIMILAR, allSetBean.getConfig_info().getIs_enable_item_similar());
                        if (!TextUtils.isEmpty(allSetBean.getConfig_info().getIs_subscribed_tag())) {
                            SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.IS_SUBSCRIBED_TAG, allSetBean.getConfig_info().getIs_subscribed_tag());
                        }
                        Message message = new Message();
                        message.what = 6;
                        mHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().allSet(callBack);

    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 5) {
                is_subscribed_tag = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.IS_SUBSCRIBED_TAG);
                if (!TextUtils.isEmpty(is_subscribed_tag)) {
                    //是否已订阅标签 1:是 0:否
                    if (is_subscribed_tag.equals("0")) {//没订阅，显示查看订阅管理页
                        rl_go_dingyue.setVisibility(View.VISIBLE);
                    } else {//已经订阅过了，隐藏
                        rl_go_dingyue.setVisibility(View.GONE);
                        scrollRecyclerView();
                        materialRefreshLayout.autoRefresh();//刷新完毕;
                    }
                }
            } else if (msg.what == 6) {
                is_subscribed_tag = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.IS_SUBSCRIBED_TAG);
                if (!TextUtils.isEmpty(is_subscribed_tag)) {
                    //是否已订阅标签 1:是 0:否
                    if (is_subscribed_tag.equals("0")) {//没订阅，显示查看订阅管理页
                        rl_go_dingyue.setVisibility(View.VISIBLE);
                        Intent intent_dingyue = new Intent(activity, DingYueGuanLiActivity.class);
                        startActivityForResult(intent_dingyue, 12);
                    } else {//已经订阅过了，隐藏
                        rl_go_dingyue.setVisibility(View.GONE);
                    }
                }
            }
        }
    };

}
