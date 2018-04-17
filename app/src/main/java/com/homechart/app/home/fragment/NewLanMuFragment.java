package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.activity.LoginActivity;
import com.homechart.app.home.adapter.MyHuaTiAdapter;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.guanliantags.GuanLianTagBean;
import com.homechart.app.home.bean.search.RecommendItemDataBean;
import com.homechart.app.home.bean.search.SearchDataBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.ui.activity.InspirationSeriesActivity;
import com.homechart.app.myview.MyListView;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.CustomProgress;
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
public class NewLanMuFragment
        extends BaseFragment
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener ,MyHuaTiAdapter.ClickZhuTi{

    private FragmentManager fragmentManager;
    private Bundle mBundle;
    private String tag_name;
    private ImageButton mBack;
    private TextView mTital;
    private List<String> listHot;
    private List<String> mListPingDao1 = new ArrayList<>();
    private RecyclerView mRecyclerView1;
    private AnimationSet animationSet;
    private int scroll_position;
    private int page_num = 1;
    private MultiItemCommonAdapter<SearchItemDataBean> mAdapter;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private LoadMoreFooterView mLoadMoreFooterView;
    private List<SearchItemDataBean> mListData = new ArrayList<>();
    private List<String> listTag = new ArrayList<>();
    private List<String> mItemIdList = new ArrayList<>();
    private int width_Pic_Staggered;
    private boolean loginStatus;
    private String userId;
    private boolean ifClickAble = true;
    private Map<String, Integer> mapSearch = new HashMap<>();
    private HRecyclerView mRecyclerView;
    private View headerView;
    private MyListView lv_faxian_header;
    private MyHuaTiAdapter myHuaTiAdapter;
    private int position;
    private TextView tv_shoucang;
    private boolean ifClickDingYue = true;
    private GuanLianTagBean guanLianTagBean;
    private MultiItemCommonAdapter<String> mAdapter1;
    List<RecommendItemDataBean> mListHuaTi = new ArrayList<>();
    private String mUserId;
    private float mDownY;
    private float mMoveY;
    private boolean move_tag = true;

    public NewLanMuFragment() {
    }

    public NewLanMuFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_lanmu;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        mBundle = getArguments();
        tag_name = (String) mBundle.getString("tag_name");
    }

    @Override
    protected void initView() {
        mBack = (ImageButton) rootView.findViewById(R.id.nav_left_imageButton);
        mTital = (TextView) rootView.findViewById(R.id.tv_tital_comment);
        tv_shoucang = (TextView) rootView.findViewById(R.id.tv_shoucang);
        mRecyclerView1 = (RecyclerView) rootView.findViewById(R.id.hlv_tab2);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_pic);
        headerView = LayoutInflater.from(activity).inflate(R.layout.header_lanmu, null);
        lv_faxian_header = (MyListView) headerView.findViewById(R.id.lv_faxian_header);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
        tv_shoucang.setOnClickListener(this);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (move_tag) {
                            mDownY = event.getY();
                            move_tag = false;
                        }
                        mMoveY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:

                        move_tag = true;
                        mMoveY = event.getY();
                        Log.e("UP", "Y" + mMoveY);
                        if (Math.abs((mMoveY - mDownY)) > 20) {
                            if (mMoveY > mDownY) {
                                mRecyclerView1.setVisibility(View.VISIBLE);
                            } else {
                                mRecyclerView1.setVisibility(View.GONE);
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTital.setText(tag_name);
        width_Pic_Staggered = PublicUtils.getScreenWidth(activity) / 2 - UIUtils.getDimens(R.dimen.font_28);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        buildRecyclerView1();
        getGuanLianTags();
        initAnimation();
        buildRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                fragmentManager.popBackStack();
                break;
            case R.id.tv_shoucang:
                loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
                if (!loginStatus) {

                    Intent intent = new Intent(activity, LoginActivity.class);
                    startActivityForResult(intent, 4);

                } else {
                    if (ifClickDingYue) {
                        ifClickDingYue = false;
                        if (null != guanLianTagBean && guanLianTagBean.getTag_info().getIs_subscribed().equals("1")) {
                            //友盟统计
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("evenname", "标签订阅");
                            map.put("even", "推荐标签页");
                            MobclickAgent.onEvent(activity, "shijian47", map);
                            //ga统计
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory("推荐标签页")  //事件类别
                                    .setAction("标签订阅")      //事件操作
                                    .build());
                            //取消订阅
                            removeDingYue(guanLianTagBean.getTag_info().getTag_id());
                        } else if (null != guanLianTagBean && guanLianTagBean.getTag_info().getIs_subscribed().equals("0")) {
                            //友盟统计
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("evenname", "取消订阅");
                            map.put("even", "推荐标签页");
                            MobclickAgent.onEvent(activity, "shijian48", map);
                            //ga统计
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory("推荐标签页")  //事件类别
                                    .setAction("取消订阅")      //事件操作
                                    .build());
                            //订阅
                            addDingYue(guanLianTagBean.getTag_info().getTag_id());
                        }
                    }
                }
                break;
        }
    }

    private void getGuanLianTags() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, getString(R.string.searchtag_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        Message msg = new Message();
                        msg.obj = data_msg;
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getGuanLianTags(tag_name, callBack);

    }

    private void buildRecyclerView1() {
        MultiItemTypeSupport<String> support = new MultiItemTypeSupport<String>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_pingdao1;
            }

            @Override
            public int getItemViewType(int position, String s) {
                return 0;
            }
        };
        mAdapter1 = new MultiItemCommonAdapter<String>(activity, mListPingDao1, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {
                ((TextView) holder.getView(R.id.tv_item_pingdao)).setText(mListPingDao1.get(position));
                holder.getView(R.id.rl_lanmu).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String strLanMu = mListPingDao1.get(position);
                        //友盟统计
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("evenname", "标签点击");
                        map.put("even", strLanMu + "推荐标签页点击标签的次数");
                        MobclickAgent.onEvent(activity, "shijian44", map);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory(strLanMu + "推荐标签页点击频道标签的次数")  //事件类别
                                .setAction("标签点击")      //事件操作
                                .build());

                        NewLanMuFragment newLanMuFragment = new NewLanMuFragment(getChildFragmentManager());
                        Bundle bundle = new Bundle();
                        bundle.putString("tag_name", strLanMu);
                        newLanMuFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.id_main, newLanMuFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView1.setHasFixedSize(true);//设置固定大小
        mRecyclerView1.setLayoutManager(linearLayoutManager);
        mRecyclerView1.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView1.setAdapter(mAdapter1);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            switch (code) {
                case 1:
                    String dataStr = (String) msg.obj;
                    guanLianTagBean = GsonUtil.jsonToBean(dataStr, GuanLianTagBean.class);
                    listHot = guanLianTagBean.getTag_info().getRelation_tag();
                    if (null != listHot && listHot.size() > 0) {
                        mRecyclerView1.setVisibility(View.VISIBLE);
                        mListPingDao1.clear();
                        mListPingDao1.addAll(listHot);
                        mAdapter1.notifyDataSetChanged();
                    } else {
                        mRecyclerView1.setVisibility(View.GONE);
                    }
                    tv_shoucang.setVisibility(View.VISIBLE);
                    if (guanLianTagBean.getTag_info().getSubscribe_show().equals("0")) {//没有订阅按钮
                        tv_shoucang.setVisibility(View.GONE);
                    } else {//有订阅按钮
                        if (guanLianTagBean.getTag_info().getIs_subscribed().equals("0")) {//没订阅
                            changeDingYueStatus(false);
                        } else if (guanLianTagBean.getTag_info().getIs_subscribed().equals("1")) {
                            //订阅
                            changeDingYueStatus(true);
                        }
                        tv_shoucang.setVisibility(View.VISIBLE);
                    }
                    break;
                case 2:
                    int clickPosition = msg.getData().getInt("position");
                    SearchDataBean searchDataBean = (SearchDataBean) msg.obj;
                    List<SearchItemDataBean> listSearch = searchDataBean.getItem_list();
                    if (listSearch == null || listSearch.size() == 0) {
                        mAdapter.notifyItemChanged(clickPosition);
                    } else {
                        List<SearchItemDataBean> list = new ArrayList();
                        list.addAll(listSearch);
                        List<String> listId = new ArrayList();
                        for (int i = 0; i < listSearch.size(); i++) {
                            listId.add(listSearch.get(i).getItem_info().getItem_id());
                        }
                        int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                        staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions);
                        if ((lastPositions[0] - 2) <= clickPosition || (lastPositions[1] - 2) <= clickPosition) {
                            mListData.addAll(clickPosition + 1, list);
                            mItemIdList.addAll(clickPosition + 1, listId);
                            mAdapter.notifyItemChanged(clickPosition);
                            mAdapter.notifyItemInserted(clickPosition + 1);
                            mAdapter.notifyItemRangeChanged(clickPosition + 1, list.size()); //比较好的
                        } else {
                            mAdapter.notifyItemChanged(clickPosition);
                        }
                    }
                    ifClickAble = true;
                    break;
            }
        }
    };

    private void buildRecyclerView() {
        MultiItemTypeSupport<SearchItemDataBean> support = new MultiItemTypeSupport<SearchItemDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_pubu_faxian;
            }

            @Override
            public int getItemViewType(int position, SearchItemDataBean s) {
                return 0;
            }
        };
        mAdapter = new MultiItemCommonAdapter<SearchItemDataBean>(activity, mListData, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {
                scroll_position = position;
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

                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                layoutParams.height = Math.round(width_Pic_Staggered / mListData.get(position).getItem_info().getImage().getRatio());
                holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);

                if (PublicUtils.ifHasWriteQuan(activity)) {
                    if (mListData.get(position).getItem_info().getImage().getRatio() > 0.6) {
                        ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg1(),
                                (ImageView) holder.getView(R.id.iv_imageview_one));
                    } else {
                        GlideImgManager.glideLoader(activity, mListData.get(position).getItem_info().getImage().getImg1(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_imageview_one), 1);
                    }
                } else {
                    GlideImgManager.glideLoader(activity, mListData.get(position).getItem_info().getImage().getImg1(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_imageview_one), 1);
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
                        bundle.putString("shaixuan_tag", "");
                        bundle.putInt("page_num", page_num + 1);
                        bundle.putSerializable("item_id_list", (Serializable) mItemIdList);
                        newImageDetailsFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack(null).replace(R.id.id_main, newImageDetailsFragment);
                        fragmentTransaction.commit();
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
        };
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ((DefaultItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.addHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }


    private void initAnimation() {
        //1.AnimationSet
        animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new LinearInterpolator());
        //透明度
//        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.5f);
//        alphaAnimation.setRepeatCount(100);

        //缩放（以某个点为中心缩放）
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.8f, 1, 0.8f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setRepeatCount(100);
        //添加动画
        animationSet.setFillAfter(true);
//        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(500);
        animationSet.setStartOffset(0);
        animationSet.setRepeatCount(Animation.INFINITE);
        animationSet.setRepeatMode(Animation.REVERSE);
    }

    @Override
    public void onRefresh() {
        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getListData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {
        //友盟统计
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("evenname", "推荐标签页加载");
        map.put("even", "推荐标签页加载次数");
        MobclickAgent.onEvent(activity, "shijian49", map);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("推荐标签页加载次数")  //事件类别
                .setAction("推荐标签页加载")      //事件操作
                .build());
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getListData(LOADMORE_STATUS);
    }


    private void getListData(final String state) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
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
                CustomProgress.cancelDialog();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {

                        SearchDataBean searchDataBean = GsonUtil.jsonToBean(data_msg, SearchDataBean.class);
                        if (state.equals(REFRESH_STATUS)) {
                            if (null != searchDataBean.getRecommend_list() && searchDataBean.getRecommend_list().size() > 0) {
                                mListData.clear();
                                changeZhuTi(searchDataBean.getRecommend_list());
                            } else {
                                lv_faxian_header.setVisibility(View.GONE);
                            }
                        }
                        if (null != searchDataBean.getItem_list() && 0 != searchDataBean.getItem_list().size()) {
                            updateViewFromData(searchDataBean.getItem_list(), state);
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
                        ToastUtils.showCenter(activity, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getTagPics("", tag_name, "", (page_num - 1) * 20 + "", "20", callBack);
    }

    private void changeZhuTi(List<RecommendItemDataBean> list) {
        mListHuaTi.clear();
        mListHuaTi.addAll(list);
        if (myHuaTiAdapter == null) {
            myHuaTiAdapter = new MyHuaTiAdapter(list, activity,NewLanMuFragment.this);
            lv_faxian_header.setAdapter(myHuaTiAdapter);
            lv_faxian_header.setVisibility(View.VISIBLE);
        } else {
            myHuaTiAdapter.dataChange(list);
            lv_faxian_header.setVisibility(View.VISIBLE);
        }
        mRecyclerView.scrollToPosition(0);
    }

    private void getSearchImage(String item_id, final int clickPosition) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ifClickAble = true;
                mAdapter.notifyItemChanged(clickPosition);
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        SearchDataBean searchDataBean = GsonUtil.jsonToBean(data_msg, SearchDataBean.class);
                        Message message = new Message();
                        message.obj = searchDataBean;
                        message.what = 2;
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", clickPosition);
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                    } else {

                        mAdapter.notifyItemChanged(clickPosition);
                        ifClickAble = true;
                        ToastUtils.showCenter(activity, error_msg);

                    }
                } catch (JSONException e) {

                    mAdapter.notifyItemChanged(clickPosition);
                    ifClickAble = true;
                }
            }
        };
        MyHttpManager.getInstance().getSearchImage(item_id, (mapSearch.get(item_id) - 1) * 5 + "", "5", callBack);
    }

    private void updateViewFromData(List<SearchItemDataBean> listData, String state) {

        switch (state) {

            case REFRESH_STATUS:
                mListData.clear();
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
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                    page_num = 1;
                    mListData.clear();
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setRefreshing(false);//刷新完毕
                mRecyclerView.scrollToPosition(0);
                break;

            case LOADMORE_STATUS:
                if (null != listData && listData.size() > 0) {
                    position = mListData.size();
                    mListData.addAll(listData);
                    mAdapter.notifyItem(position, mListData, listData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                    for (int i = 0; i < listData.size(); i++) {
                        if (!listData.get(i).getItem_info().getTag().equals("活动")) {
                            mItemIdList.add(listData.get(i).getItem_info().getItem_id());
                        }
                    }
                } else {
                    --page_num;
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

    private void changeDingYueStatus(boolean dingyue) {
        if (dingyue) {
            tv_shoucang.setBackgroundResource(R.drawable.bt_shoucang_no);
            tv_shoucang.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
            tv_shoucang.setText("已订阅");
        } else {
            tv_shoucang.setBackgroundResource(R.drawable.bt_shoucang);
            tv_shoucang.setTextColor(UIUtils.getColor(R.color.white));
            tv_shoucang.setText("＋订阅");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4) {
            getGuanLianTags();
        }
    }


    private void addDingYue(String tag_id) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(activity, "订阅失败！");
                ifClickDingYue = true;
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        changeDingYueStatus(true);
                        getGuanLianTags();
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(activity, "订阅成功！");
                        ifClickDingYue = true;
                    } else {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(activity, "订阅失败！");
                        ifClickDingYue = true;
                    }
                } catch (JSONException e) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(activity, "订阅失败！");
                    ifClickDingYue = true;
                }
            }
        };
        MyHttpManager.getInstance().addTagDingYue(tag_id, callBack);
    }

    private void removeDingYue(String tag_id) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ifClickDingYue = true;
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(activity, "取消订阅失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        changeDingYueStatus(false);
                        getGuanLianTags();
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(activity, "取消订阅成功！");
                        ifClickDingYue = true;
                    } else {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(activity, "取消订阅失败！");
                        ifClickDingYue = true;
                    }
                } catch (JSONException e) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(activity, "取消订阅失败！");
                    ifClickDingYue = true;
                }
            }
        };
        MyHttpManager.getInstance().removeTagDingYue(tag_id, callBack);
    }


    @Override
    public void clickZhuTi(int position) {
        if(mListHuaTi.size() > position){
            //友盟统计
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("evenname", "精选话题");
            map.put("even", "推荐标签页");
            MobclickAgent.onEvent(activity, "shijian45", map);
            //ga统计
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("推荐标签页")  //事件类别
                    .setAction("精选话题")      //事件操作
                    .build());

            mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
            NewInspirationDetailsment newInspirationDetailsment = new NewInspirationDetailsment(fragmentManager);
            Bundle bundle = new Bundle();
            bundle.putString("user_id", mUserId);
            bundle.putBoolean("ifHideEdit", true);
            bundle.putString("album_id", mListHuaTi.get(position).getRecommend_info().getObject_id());
//                        bundle.putString("show_type",  imageDetailBean.getAlbum_info().getShow_type());
            newInspirationDetailsment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.id_main, newInspirationDetailsment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("推荐标签页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("推荐标签页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("推荐标签页");
    }

}