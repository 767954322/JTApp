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
import android.text.TextPaint;
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
import com.homechart.app.home.activity.NewTagsListActivity;
import com.homechart.app.home.activity.SearchActivity;
import com.homechart.app.home.activity.TagsListActivity;
import com.homechart.app.home.adapter.MyHuaTiAdapter;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.faxianpingdao.PingDaoBean;
import com.homechart.app.home.bean.faxianpingdao.PingDaoItemBean;
import com.homechart.app.home.bean.search.RecommendItemDataBean;
import com.homechart.app.home.bean.search.SearchDataBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.ui.activity.InspirationSeriesActivity;
import com.homechart.app.myview.ClearEditText;
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
public class HomeFaXianFragment
        extends BaseFragment
        implements OnLoadMoreListener,
        OnRefreshListener, View.OnClickListener, MyHuaTiAdapter.ClickZhuTi {

    private FragmentManager fragmentManager;
    public PingDaoBean pingDaoBean;
    private RecyclerView mRecyclerView1;
    private RecyclerView mRecyclerView2;
    private List<PingDaoItemBean> mListPingDao = new ArrayList<>();
    private List<String> mListPingDao1 = new ArrayList<>();
    private HRecyclerView mRecyclerView;
    private List<SearchItemDataBean> mListData = new ArrayList<>();
    private List<String> mItemIdList = new ArrayList<>();
    private List<String> listTag = new ArrayList<>();
    private int scroll_position;
    private int page_num = 1;
    private MultiItemCommonAdapter<SearchItemDataBean> mAdapter;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private LoadMoreFooterView mLoadMoreFooterView;
    private String tagName = "热门";
    private int position;
    private int width_Pic_Staggered;
    private boolean loginStatus;
    private String userId;
    private AnimationSet animationSet;
    private View headerView;
    private MyListView lv_faxian_header;
    private MyHuaTiAdapter myHuaTiAdapter;
    private ClearEditText cet_clearedit;
    private MultiItemCommonAdapter<PingDaoItemBean> mAdapter1;
    private MultiItemCommonAdapter<String> mAdapter2;
    private int selectPosition = 0;
    private ImageView iv_more;
    private boolean move_tag = true;
    private float mDownY;
    private float mMoveY;
    private boolean ifClickAble = true;
    private Map<String, Integer> mapSearch = new HashMap<>();
    List<RecommendItemDataBean> mListHuaTi = new ArrayList<>();
    private String mUserId;


    public HomeFaXianFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public HomeFaXianFragment() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_faxian;
    }

    @Override
    protected void initView() {
        cet_clearedit = (ClearEditText) rootView.findViewById(R.id.cet_clearedit);
        iv_more = (ImageView) rootView.findViewById(R.id.iv_more);
        mRecyclerView1 = (RecyclerView) rootView.findViewById(R.id.hlv_tab1);
        mRecyclerView2 = (RecyclerView) rootView.findViewById(R.id.hlv_tab2);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_pic);
        headerView = LayoutInflater.from(activity).inflate(R.layout.header_faxian, null);
        lv_faxian_header = (MyListView) headerView.findViewById(R.id.lv_faxian_header);
    }

    @Override
    protected void initListener() {
        super.initListener();
        cet_clearedit.setKeyListener(null);
        cet_clearedit.setOnClickListener(this);
        iv_more.setOnClickListener(this);
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
                                if (mListPingDao1.size() > 0) {
                                    mRecyclerView2.setVisibility(View.VISIBLE);
                                } else {
                                    mRecyclerView2.setVisibility(View.GONE);
                                }
                            } else {
                                mRecyclerView2.setVisibility(View.GONE);
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
        width_Pic_Staggered = PublicUtils.getScreenWidth(activity) / 2 - UIUtils.getDimens(R.dimen.font_15);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        buildHRecyclerView1();
        buildHRecyclerView2();
        initAnimation();
        getPingDaoTag();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cet_clearedit:
                Intent intent = new Intent(activity, SearchActivity.class);
                startActivityForResult(intent, 10);
                break;
            case R.id.iv_more:
                Intent intent1 = new Intent(activity, NewTagsListActivity.class);
                intent1.putExtra("list", (Serializable) mListPingDao);
                startActivityForResult(intent1, 11);
                break;
        }
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
        map.put("evenname", "发现页加载");
        map.put("even", "发现页加载次数");
        MobclickAgent.onEvent(activity, "shijian46", map);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("发现页加载次数")  //事件类别
                .setAction("发现页加载")      //事件操作
                .build());

        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getListData(LOADMORE_STATUS);
    }

    private void buildHRecyclerView1() {
        MultiItemTypeSupport<PingDaoItemBean> support = new MultiItemTypeSupport<PingDaoItemBean>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_pingdao;
            }

            @Override
            public int getItemViewType(int position, PingDaoItemBean s) {
                return 0;
            }
        };
        mAdapter1 = new MultiItemCommonAdapter<PingDaoItemBean>(activity, mListPingDao, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {

                if (position == mListPingDao.size() - 1) {
                    holder.getView(R.id.view_lastone).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.view_lastone).setVisibility(View.GONE);
                }

                ((TextView) holder.getView(R.id.tv_item_pingdao)).setText(mListPingDao.get(position).getChannel_name());
                TextPaint tp = ((TextView) holder.getView(R.id.tv_item_pingdao)).getPaint();
                if (selectPosition == position) {
                    tp.setFakeBoldText(true);
                    ((TextView) holder.getView(R.id.tv_item_pingdao)).setTextColor(UIUtils.getColor(R.color.bg_e79056));
                    ((TextView) holder.getView(R.id.tv_item_pingdao)).setTextSize(16);
                    holder.getView(R.id.view_bottom).setVisibility(View.VISIBLE);
                } else {
                    tp.setFakeBoldText(false);
                    ((TextView) holder.getView(R.id.tv_item_pingdao)).setTextColor(UIUtils.getColor(R.color.bg_464646));
                    ((TextView) holder.getView(R.id.tv_item_pingdao)).setTextSize(14);
                    holder.getView(R.id.view_bottom).setVisibility(View.GONE);
                }
                holder.getView(R.id.rl_pingdao).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListPingDao.size() > position && !tagName.equals(mListPingDao.get(position).getChannel_name())) {
                            CustomProgress.show(activity, "", false, null);
                            selectPosition = position;
                            mAdapter1.notifyDataSetChanged();
                            tagName = mListPingDao.get(position).getChannel_name();

                            //友盟统计
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("evenname", "频道点击");
                            map.put("even", "点击频道标签的次数");
                            MobclickAgent.onEvent(activity, "shijian43", map);
                            //ga统计
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory(tagName + "点击频道标签的次数")  //事件类别
                                    .setAction("频道点击")      //事件操作
                                    .build());
                            onRefresh();
                            List<String> strList = mListPingDao.get(position).getRelation_tag();
                            if (null != strList && strList.size() > 0) {
                                mRecyclerView2.setVisibility(View.VISIBLE);
                                mListPingDao1.clear();
                                mListPingDao1.addAll(strList);
                                mAdapter2.notifyDataSetChanged();
                                mRecyclerView2.scrollToPosition(0);
                            } else {
                                mListPingDao1.clear();
                                mRecyclerView2.setVisibility(View.GONE);
                            }
                        }
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

    private void buildHRecyclerView2() {
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
        mAdapter2 = new MultiItemCommonAdapter<String>(activity, mListPingDao1, support) {
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
                        map.put("even", strLanMu + "发现页点击标签的次数");
                        MobclickAgent.onEvent(activity, "shijian44", map);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory(tagName + "发现页点击频道标签的次数")  //事件类别
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
                        ClassConstant.HomeStatus.FAXIAN_STATUS = 1;
                    }
                });
            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView2.setHasFixedSize(true);//设置固定大小
        mRecyclerView2.setLayoutManager(linearLayoutManager);
        mRecyclerView2.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView2.setAdapter(mAdapter2);
    }

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
                        ImageUtils.displayFilletHalfImage(mListData.get(position).getItem_info().getImage().getImg1(),
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
                holder.getView(R.id.iv_header_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NewUserInfoFragment newUserInfoFragment = new NewUserInfoFragment(getChildFragmentManager());
                        Bundle bundle = new Bundle();
                        bundle.putString(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                        newUserInfoFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.id_main, newUserInfoFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ClassConstant.HomeStatus.FAXIAN_STATUS = 1;
                    }
                });
                holder.getView(R.id.tv_name_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewUserInfoFragment newUserInfoFragment = new NewUserInfoFragment(getChildFragmentManager());
                        Bundle bundle = new Bundle();
                        bundle.putString(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                        newUserInfoFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.id_main, newUserInfoFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ClassConstant.HomeStatus.FAXIAN_STATUS = 1;
                    }
                });

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
                        ClassConstant.HomeStatus.FAXIAN_STATUS = 1;
                    }
                });
                holder.getView(R.id.iv_if_shoucang).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
                        userId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
                        if (!loginStatus) {
                            //友盟统计
                            HashMap<String, String> map4 = new HashMap<String, String>();
                            map4.put("evenname", "登录入口");
                            map4.put("even", "发现页进行图片收藏");
                            MobclickAgent.onEvent(activity, "shijian20", map4);
                            //ga统计
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory("发现页进行图片收藏")  //事件类别
                                    .setAction("登录入口")      //事件操作
                                    .build());
                            Intent intent = new Intent(activity, LoginActivity.class);
                            startActivityForResult(intent, 1);
                        } else {
                            //友盟统计
                            HashMap<String, String> map4 = new HashMap<String, String>();
                            map4.put("evenname", "加灵感辑");
                            map4.put("even", "发现页");
                            MobclickAgent.onEvent(activity, "shijian30", map4);
                            //ga统计
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory("发现页")  //事件类别
                                    .setAction("加灵感辑")      //事件操作
                                    .build());
                            Intent intent = new Intent(activity, InspirationSeriesActivity.class);
                            intent.putExtra("userid", userId);
                            intent.putExtra("image_url", mListData.get(position).getItem_info().getImage().getImg0());
                            intent.putExtra("item_id", mListData.get(position).getItem_info().getItem_id());
                            startActivity(intent);
                        }
                    }
                });


                Animation animation = holder.getView(R.id.iv_shibie_pic).getAnimation();
                if (animation != null) {
                    holder.getView(R.id.iv_shibie_pic).clearAnimation();
                }
//                    holder.getView(R.id.iv_shibie_pic).setAlpha(0.3f);
                holder.getView(R.id.iv_shibie_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //友盟统计
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("evenname", "找相似图");
                        map.put("even", "发现页");
                        MobclickAgent.onEvent(activity, "shijian31", map);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("发现页")  //事件类别
                                .setAction("找相似图")      //事件操作
                                .build());
                        if (ifClickAble) {
                            holder.getView(R.id.iv_shibie_pic).startAnimation(animationSet);
//                                ifClickAble = false;
                            if (mapSearch.containsKey(mListData.get(position).getItem_info().getItem_id())) {
                                mapSearch.put(mListData.get(position).getItem_info().getItem_id(), mapSearch.get(mListData.get(position).getItem_info().getItem_id()) + 1);
                            } else {
                                mapSearch.put(mListData.get(position).getItem_info().getItem_id(), 1);
                            }
                            getSearchImage(mListData.get(position).getItem_info().getItem_id(), position);
                        }
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

    private void changeZhuTi(List<RecommendItemDataBean> list) {
        mListHuaTi.clear();
        mListHuaTi.addAll(list);
        if (myHuaTiAdapter == null) {
            myHuaTiAdapter = new MyHuaTiAdapter(list, activity, HomeFaXianFragment.this);
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

    private void getPingDaoTag() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, "频道列表信息获取失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        Message message = new Message();
                        message.obj = data_msg;
                        message.what = 1;
                        mHandler.sendMessage(message);
                        Log.d("test", data_msg);
                    } else {
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(activity, "频道列表信息获取失败");
                }
            }
        };
        MyHttpManager.getInstance().getPingDaoTags(callBack);

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
        MyHttpManager.getInstance().getChannelPics("", tagName, "", (page_num - 1) * 20 + "", "20", callBack);
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

    private void initAnimation() {
        animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new LinearInterpolator());
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.8f, 1, 0.8f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setRepeatCount(100);
        animationSet.setFillAfter(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(500);
        animationSet.setStartOffset(0);
        animationSet.setRepeatCount(Animation.INFINITE);
        animationSet.setRepeatMode(Animation.REVERSE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 10) {
            String search_tag = data.getStringExtra("search_tag");
            String search_info = data.getStringExtra("search_info");

            NewSearchResultFragment newSearchResultFragment = new NewSearchResultFragment(getChildFragmentManager(), search_tag, search_info);
            Bundle bundle = new Bundle();
            bundle.putString("search_tag", search_tag);
            bundle.putString("search_info", search_info);
            newSearchResultFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.id_main, newSearchResultFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
            ClassConstant.HomeStatus.FAXIAN_STATUS = 1;
        } else if (requestCode == 11 && resultCode == 11) {
            int clickPosition = data.getIntExtra("position", 0);
            String tag_name = data.getStringExtra("tag_name");

            NewLanMuFragment newLanMuFragment = new NewLanMuFragment(getChildFragmentManager());
            Bundle bundle = new Bundle();
            bundle.putString("tag_name", tag_name);
            newLanMuFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.id_main, newLanMuFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
            ClassConstant.HomeStatus.FAXIAN_STATUS = 1;

//            CustomProgress.show(activity, "", false, null);
//            selectPosition = clickPosition;
//            mAdapter1.notifyDataSetChanged();
//            tagName = mListPingDao.get(selectPosition).getChannel_name();
//            onRefresh();
//            List<String> strList = mListPingDao.get(selectPosition).getRelation_tag();
//            if (null != strList && strList.size() > 0) {
//                mRecyclerView2.setVisibility(View.VISIBLE);
//                mListPingDao1.clear();
//                mListPingDao1.addAll(strList);
//                mAdapter2.notifyDataSetChanged();
//                mRecyclerView2.scrollToPosition(0);
//            } else {
//                mRecyclerView2.setVisibility(View.GONE);
//            }
//            mRecyclerView1.scrollToPosition(selectPosition);

        }
    }

    boolean ifFirst = true;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String dataStr = (String) msg.obj;
                    pingDaoBean = GsonUtil.jsonToBean(dataStr, PingDaoBean.class);
                    List<PingDaoItemBean> list1 = pingDaoBean.getChannel_list();
                    mListPingDao.clear();
                    mListPingDao.addAll(list1);
                    mAdapter1.notifyDataSetChanged();
                    if (list1.size() > 0 && ifFirst) {
                        tagName = list1.get(0).getChannel_name();
                        buildRecyclerView();
                        ifFirst = false;
                    }
                    if (mListPingDao.size() > 0 && mListPingDao.get(0).getRelation_tag().size() > 0) {
                        mListPingDao1.clear();
                        mListPingDao1.addAll(mListPingDao.get(0).getRelation_tag());
                        mAdapter2.notifyDataSetChanged();
                        mRecyclerView2.setVisibility(View.VISIBLE);
                    } else {
                        mRecyclerView2.setVisibility(View.GONE);
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


    @Override
    public void clickZhuTi(int position) {
        if (mListHuaTi.size() > position) {

            //友盟统计
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("evenname", "精选话题");
            map.put("even", "发现");
            MobclickAgent.onEvent(activity, "shijian45", map);
            //ga统计
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("发现")  //事件类别
                    .setAction("精选话题")      //事件操作
                    .build());

            mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
            NewInspirationDetailsment newInspirationDetailsment = new NewInspirationDetailsment(getChildFragmentManager());
            Bundle bundle = new Bundle();
            bundle.putString("user_id", mUserId);
            bundle.putBoolean("ifHideEdit", true);
            bundle.putString("album_id", mListHuaTi.get(position).getRecommend_info().getObject_id());
//                        bundle.putString("show_type",  imageDetailBean.getAlbum_info().getShow_type());
            newInspirationDetailsment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.id_main, newInspirationDetailsment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            ClassConstant.HomeStatus.FAXIAN_STATUS = 1;
        }
    }

    public void scrollRecyclerView() {
        if (mListData.size() > 0) {
            mRecyclerView.scrollToPosition(0);
            if (mListPingDao1.size() > 0) {
                mRecyclerView2.setVisibility(View.VISIBLE);
            } else {
                mRecyclerView2.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("发现页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("发现页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("发现页");
    }
}
