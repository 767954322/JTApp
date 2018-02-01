package com.homechart.app.home.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
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
import com.homechart.app.home.activity.LoginActivity;
import com.homechart.app.home.activity.NewHuoDongDetailsActivity;
import com.homechart.app.home.activity.SearchActivity;
import com.homechart.app.home.adapter.HomeTagAdapter;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.color.ColorBean;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.search.ActivityInfoBean;
import com.homechart.app.home.bean.search.SearchDataBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.bean.search.SearchItemInfoDataBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.common.view.InspirationSeriesPop;
import com.homechart.app.lingganji.ui.activity.InspirationSeriesActivity;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.myview.NewHomeTabPopWin;
import com.homechart.app.myview.SelectColorSeCaiWindow;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

@SuppressLint("ValidFragment")
public class HomePicFragment
        extends BaseFragment
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener,
        HomeTagAdapter.PopupWindowCallBack,
        SelectColorSeCaiWindow.SureColor {

    private FragmentManager fragmentManager;
    private ImageView iv_change_frag;

    private HRecyclerView mRecyclerView;

    private List<SearchItemDataBean> mListData = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;
    private MultiItemCommonAdapter<SearchItemDataBean> mAdapter;
    private int page_num = 1;
    private int TYPE_ONE = 1;
    private int TYPE_TWO = 2;
    private int TYPE_THREE = 3;
    private int TYPE_FOUR = 4;
    private int TYPE_FIVE = 5;
    private int position;
    private boolean curentListTag = false;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ClearEditText cet_clearedit;
    private int width_Pic_Staggered;
    private int width_Pic_List;
    private NewHomeTabPopWin newHomeTabPopWin;
    public TagDataBean tagDataBean;
    public ColorBean colorBean;
    private View view;
    private Timer timer = new Timer(true);

    private float mDownY;
    private float mMoveY;
    private boolean move_tag = true;
    private RelativeLayout id_main;
    private View view_line_back;
    boolean ifShouCang = true;
    private int last_id = 0;
    private int scroll_position;
    private Map<Integer, ColorItemBean> mSelectListData = new HashMap<>();
    private List<String> mItemIdList = new ArrayList<>();
    private RelativeLayout rl_pic_change;
    private ImageView iv_chongzhi;
    private ImageView iv_color_icon;
    private TextView bt_tag_page_item;
    private TextView tv_color_tital;

    private ColorItemBean mColorClick;
    private SelectColorSeCaiWindow selectColorPopupWindow;
    private Boolean loginStatus;
    private View view_line_top;
    private ImageView iv_open_pop;
    private InspirationSeriesPop mInspirationSeriesPop;
    private String userId;
    private ActivityInfoBean mActivityInfoBean;
    private AnimationSet animationSet;
    private boolean ifScroll = false;
    private String is_enable_item_similar;
    private RelativeLayout rl_new_top;

    public HomePicFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public HomePicFragment() {
    }


    public void setDownY(float y) {
        mDownY = y;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_pic;
    }

    @Override
    protected void initView() {

        Log.d("test","initView");
        cet_clearedit = (ClearEditText) rootView.findViewById(R.id.cet_clearedit);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_pic);

        iv_open_pop = (ImageView) rootView.findViewById(R.id.iv_open_pop);
        view_line_top = rootView.findViewById(R.id.view_line_top);
        id_main = (RelativeLayout) rootView.findViewById(R.id.id_main);
        view_line_back = rootView.findViewById(R.id.view_line_back);


        rl_new_top = (RelativeLayout) rootView.findViewById(R.id.rl_new_top);
        rl_pic_change = (RelativeLayout) rootView.findViewById(R.id.rl_pic_change);
        iv_chongzhi = (ImageView) rootView.findViewById(R.id.iv_chongzhi);
        iv_color_icon = (ImageView) rootView.findViewById(R.id.iv_color_icon);
        iv_change_frag = (ImageView) rootView.findViewById(R.id.iv_change_frag);
        bt_tag_page_item = (TextView) rootView.findViewById(R.id.bt_tag_page_item);
        tv_color_tital = (TextView) rootView.findViewById(R.id.tv_color_tital);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initListener() {
        super.initListener();
        cet_clearedit.setKeyListener(null);
        cet_clearedit.setOnClickListener(this);
        iv_change_frag.setOnClickListener(this);
        iv_chongzhi.setOnClickListener(this);
        iv_open_pop.setOnClickListener(this);
        tv_color_tital.setOnClickListener(this);
        iv_color_icon.setOnClickListener(this);
        bt_tag_page_item.setOnClickListener(this);
//        mRecyclerView.addOnScrollListener(new OnVerticalScrollListener(){
//            @Override
//            public void onScrolledUp() {
//                super.onScrolledUp();
//                rl_new_top.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onScrolledDown() {
//                super.onScrolledDown();
//                rl_new_top.setVisibility(View.INVISIBLE);
//            }
//        });
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == 0) {
////                    ifScroll = false;
////                    mAdapter.notifyDataSetChanged();
//                    Log.d("test", "0");
//                } else if (newState == 1) {
//                    rl_new_top.setVisibility(View.GONE);
////                    ifScroll = true;
////                    mAdapter.notifyDataSetChanged();
//                    Log.d("test", "1");
//                } else if (newState == 2) {
//                    Log.d("test", "2");
//                }
//
////                staggeredGridLayoutManager.invalidateSpanAssignments();
//            }
//        });
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
                                rl_new_top.setVisibility(View.VISIBLE);
                            } else {
                                rl_new_top.setVisibility(View.GONE);
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
        initAnimation();
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        width_Pic_Staggered = PublicUtils.getScreenWidth(activity) / 2 - UIUtils.getDimens(R.dimen.font_15);
        width_Pic_List = PublicUtils.getScreenWidth(activity) - UIUtils.getDimens(R.dimen.font_14);
        buildRecyclerView();
        getTagData();
        getColorData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cet_clearedit:

                onDismiss();
                Intent intent = new Intent(activity, SearchActivity.class);
                startActivityForResult(intent, 10);

                break;
            case R.id.iv_change_frag:
                if (curentListTag) {
                    curentListTag = false;
                    mRecyclerView.setPadding(UIUtils.getDimens(R.dimen.font_5), 0, UIUtils.getDimens(R.dimen.font_5), 0);
                    iv_change_frag.setImageResource(R.drawable.changtu);
                    mRecyclerView.setLayoutManager(staggeredGridLayoutManager);

                } else {
                    curentListTag = true;
                    mRecyclerView.setPadding(0, 0, 0, 0);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    iv_change_frag.setImageResource(R.drawable.pubuliu);

                }
                break;
            case R.id.iv_chongzhi:
                mSelectListData.clear();
                bt_tag_page_item.setVisibility(View.GONE);
                iv_chongzhi.setVisibility(View.GONE);
                tv_color_tital.setVisibility(View.VISIBLE);
                mRecyclerView.scrollToPosition(0);
                onRefresh();
                break;
            case R.id.tv_color_tital:
            case R.id.iv_color_icon:
            case R.id.bt_tag_page_item:
                if (null == colorBean) {
                    getColorData();
                    ToastUtils.showCenter(activity, "色彩信息获取失败");
                } else {
                    if (selectColorPopupWindow == null) {
                        selectColorPopupWindow = new SelectColorSeCaiWindow(activity, this, colorBean, this);
                    }
                    selectColorPopupWindow.setSelectColor(mSelectListData);
                    selectColorPopupWindow.showAtLocation(rootView.findViewById(R.id.id_main),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                            0,
                            0); //设置layout在PopupWindow中显示的位置
                }
                break;
            case R.id.view_pop_top:
            case R.id.view_pop_bottom:
                selectColorPopupWindow.dismiss();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {

        } else if (requestCode == 1) {
            onRefresh();
        } else if (requestCode == 10 && resultCode == 10) {
            String search_tag = data.getStringExtra("search_tag");
            String search_info = data.getStringExtra("search_info");

            NewSearchResultFragment newSearchResultFragment = new NewSearchResultFragment(getChildFragmentManager(),search_tag,search_info);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.id_main, newSearchResultFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    List<String> listTag = new ArrayList<>();

    private void buildRecyclerView() {

        MultiItemTypeSupport<SearchItemDataBean> support = new MultiItemTypeSupport<SearchItemDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_ONE) {
                    return R.layout.item_list_new;
                } else if (itemType == TYPE_TWO) {
                    return R.layout.item_pubu_new;
                } else if (itemType == TYPE_THREE) {
                    return R.layout.item_test_huodong_list;
                } else if (itemType == TYPE_FOUR) {
                    return R.layout.item_test_huodong_pubu;
                } else {
                    return R.layout.item_pubu_new;
                }
            }

            @Override
            public int getItemViewType(int position, SearchItemDataBean s) {
                if (curentListTag) {
                    if (s.getItem_info().getTag().equals("活动")) {
                        return TYPE_THREE;
                    } else {
                        return TYPE_ONE;
                    }
                } else {
                    if (s.getItem_info().getTag().equals("活动")) {
                        return TYPE_FOUR;
                    } else {
                        return TYPE_TWO;
                    }
                }
            }
        };

        mAdapter = new MultiItemCommonAdapter<SearchItemDataBean>(activity, mListData, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {
                scroll_position = position;
                is_enable_item_similar = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.IS_ENABLE_ITEM_SIMILAR);
                if (position == 0 && mListData.get(position).getItem_info().getTag().equals("活动") && null != mActivityInfoBean) {
                    ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();

                    if (curentListTag) {
                        layoutParams.height = (int) (width_Pic_List / mActivityInfoBean.getBig_image().getRatio());
                        holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                        ImageUtils.displayFilletImage(mActivityInfoBean.getBig_image().getImg0(), (ImageView) holder.getView(R.id.iv_imageview_one));
                    } else {
                        layoutParams.height = (int) (width_Pic_Staggered / mActivityInfoBean.getSmall_image().getRatio());
                        holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);

                        ImageUtils.displayFilletImage(mActivityInfoBean.getSmall_image().getImg0(), (ImageView) holder.getView(R.id.iv_imageview_one));
                    }
                    holder.getView(R.id.iv_imageview_one).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            NewHuoDongDetailsFragment newHuoDongDetailsFragment = new NewHuoDongDetailsFragment(getChildFragmentManager());
                            Bundle bundle = new Bundle();
                            bundle.putString("activity_id", mActivityInfoBean.getObject_id());
                            newHuoDongDetailsFragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.id_main, newHuoDongDetailsFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
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

                    ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                    layoutParams.height = (curentListTag ? (int) (width_Pic_List / 1.333333f) : Math.round(width_Pic_Staggered / mListData.get(position).getItem_info().getImage().getRatio()));
                    holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);

                    if (curentListTag) {
                        if (PublicUtils.ifHasWriteQuan(activity)) {
                            if (mListData.get(position).getItem_info().getImage().getRatio() > 0.6) {
                                ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg0(),
                                        (ImageView) holder.getView(R.id.iv_imageview_one));
                            } else {
                                GlideImgManager.glideLoader(activity, mListData.get(position).getItem_info().getImage().getImg0(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_imageview_one), 1);
                            }
                        } else {
                            GlideImgManager.glideLoader(activity, mListData.get(position).getItem_info().getImage().getImg0(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_imageview_one), 1);
                        }
                    } else {
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
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.id_main, newUserInfoFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
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
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.id_main, newUserInfoFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
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
                            bundle.putSerializable("mSelectListData", (Serializable) mSelectListData);
                            bundle.putString("shaixuan_tag", "");
                            bundle.putInt("page_num", page_num + 1);
                            bundle.putSerializable("item_id_list", (Serializable) mItemIdList);
                            newImageDetailsFragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                            fragmentTransaction.addToBackStack(null).replace(R.id.id_main, newImageDetailsFragment);
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    });

                    if (!ifScroll) {
                        holder.getView(R.id.iv_shibie_pic).setAlpha(1);
                    } else {
                        holder.getView(R.id.iv_shibie_pic).setAlpha(0.3f);
                    }
                    if (!curentListTag) {
                        if (is_enable_item_similar != null && is_enable_item_similar.equals("0")) {//关闭
                            ((ImageView) holder.getView(R.id.iv_shibie_pic)).setVisibility(View.GONE);
                        } else {//开启
                            ((ImageView) holder.getView(R.id.iv_shibie_pic)).setVisibility(View.VISIBLE);
                        }
                    }
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
                            map.put("even", "首页");
                            MobclickAgent.onEvent(activity, "shijian31", map);
                            //ga统计
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory("首页")  //事件类别
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
                    holder.getView(R.id.iv_if_shoucang).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
                            userId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
                            if (!loginStatus) {
                                //友盟统计
                                HashMap<String, String> map4 = new HashMap<String, String>();
                                map4.put("evenname", "登录入口");
                                map4.put("even", "看图列表页进行图片收藏");
                                MobclickAgent.onEvent(activity, "shijian20", map4);
                                //ga统计
                                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                        .setCategory("看图列表页进行图片收藏")  //事件类别
                                        .setAction("登录入口")      //事件操作
                                        .build());
                                Intent intent = new Intent(activity, LoginActivity.class);
                                startActivityForResult(intent, 1);
                            } else {
                                //友盟统计
                                HashMap<String, String> map4 = new HashMap<String, String>();
                                map4.put("evenname", "加灵感辑");
                                map4.put("even", "看图列表页");
                                MobclickAgent.onEvent(activity, "shijian30", map4);
                                //ga统计
                                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                        .setCategory("看图列表页")  //事件类别
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
                }

            }
        };

        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ((DefaultItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
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
        map7.put("evenname", "看图列表页加载次数");
        map7.put("even", "看图列表页");
        MobclickAgent.onEvent(activity, "shijian4", map7);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("看图列表页")  //事件类别
                .setAction("看图列表页加载次数")      //事件操作
                .build());

        mapSearch.clear();
        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getListData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {

        //友盟统计
        HashMap<String, String> map7 = new HashMap<String, String>();
        map7.put("evenname", "看图列表页加载次数");
        map7.put("even", "看图列表页");
        MobclickAgent.onEvent(activity, "shijian4", map7);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("看图列表页")  //事件类别
                .setAction("看图列表页加载次数")      //事件操作
                .build());

        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getListData(LOADMORE_STATUS);
    }

    //获取tag信息
    private void getTagData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, getString(R.string.filter_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        data_msg = "{ \"tag_id\": " + data_msg + "}";
                        Message msg = new Message();
                        msg.obj = data_msg;
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
//                    ToastUtils.showCenter(activity, getString(R.string.filter_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getPicTagData(callBack);
    }

    private void getColorData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, getString(R.string.color_get_error));
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
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(activity, getString(R.string.color_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getColorListData(callBack);

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

    //关闭tab弹出框
    @Override
    public void onDismiss() {
        if (newHomeTabPopWin != null) {
            newHomeTabPopWin.dismiss();
            iv_open_pop.setImageResource(R.drawable.shaixuan);
        }
    }

    @Override
    public void onItemClick(String tagStr, int mPosition) {
    }

    @Override
    public void onItemColorClick(ColorItemBean colorItemBean) {
        if (newHomeTabPopWin != null) {
            newHomeTabPopWin.dismiss();
            this.mColorClick = colorItemBean;

            if (mSelectListData.get(mColorClick.getColor_id()) == null) {
                mSelectListData.clear();
                mSelectListData.put(mColorClick.getColor_id(), mColorClick);
                newHomeTabPopWin.changeColor(mSelectListData);

                if (mSelectListData != null && mSelectListData.size() > 0) {
                    bt_tag_page_item.setVisibility(View.VISIBLE);
                    iv_chongzhi.setVisibility(View.VISIBLE);
                    tv_color_tital.setVisibility(View.GONE);
                    for (Integer key : mSelectListData.keySet()) {
                        bt_tag_page_item.setText(mSelectListData.get(key).getColor_name());

                        if (mSelectListData.get(key).getColor_value().equalsIgnoreCase("ffffff")) {
                            GradientDrawable drawable = new GradientDrawable();
                            drawable.setCornerRadius(50);
                            drawable.setColor(Color.parseColor("#" + mSelectListData.get(key).getColor_value()));
                            drawable.setStroke(1, Color.parseColor("#262626"));
                            bt_tag_page_item.setBackgroundDrawable(drawable);
                            bt_tag_page_item.setTextColor(UIUtils.getColor(R.color.bg_262626));
                        } else {

                            GradientDrawable drawable = new GradientDrawable();
                            drawable.setCornerRadius(50);
                            drawable.setColor(Color.parseColor("#" + mSelectListData.get(key).getColor_value()));
                            bt_tag_page_item.setBackgroundDrawable(drawable);
                            bt_tag_page_item.setTextColor(UIUtils.getColor(R.color.white));
                        }
                    }
                }
                mRecyclerView.scrollToPosition(0);
                onRefresh();
            } else {
                onClearColor();
            }

        }
    }

    @Override
    public void onClearColor() {
        mSelectListData.clear();
        bt_tag_page_item.setVisibility(View.GONE);
        iv_chongzhi.setVisibility(View.GONE);
        tv_color_tital.setVisibility(View.VISIBLE);
        mRecyclerView.scrollToPosition(0);
        onRefresh();
    }

    /**
     * HashMap<String, String> map = new HashMap<String, String>();
     * map.put("evenname", "离开启动页");
     * map.put("even", "离开启动页");
     * MobclickAgent.onEvent(context, "test", map);
     */
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("首页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("首页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页");
    }

    private int notice_num = 0;
    private int follow_notice = 0;
    private int collect_notice = 0;
    private int comment_notice = 0;
    private int system_notice = 0;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
            } else if (msg.what == 2) {
                String info = (String) msg.obj;
                tagDataBean = GsonUtil.jsonToBean(info, TagDataBean.class);
            } else if (msg.what == 3) {

                String info = (String) msg.obj;
                colorBean = GsonUtil.jsonToBean(info, ColorBean.class);
                Log.d("test", colorBean.toString());
            } else if (msg.what == 4) {
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

                    if (!curentListTag) {
                        int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                        staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions);
                        if ((lastPositions[0] - 2) <= clickPosition || (lastPositions[1] - 2) <= clickPosition) {
                            mListData.addAll(clickPosition + 1, list);
                            mItemIdList.addAll(clickPosition, listId);
                            mAdapter.notifyItemChanged(clickPosition);
                            mAdapter.notifyItemInserted(clickPosition + 1);
                            mAdapter.notifyItemRangeChanged(clickPosition + 1, list.size()); //比较好的
//                            mAdapter.notifyItemRangeInserted(clickPosition + 1, list.size());
//                            Log.d("test", "clickPosition:" + clickPosition + "  ;  " + "list个数:" + list.size() + "  ;  "
//                                    + "list内容:" + list.toString() + "  ;  " + "mListData:" + mListData.toString());
                        } else {
                            mAdapter.notifyItemChanged(clickPosition);
                        }
                    }
                }

                ifClickAble = true;
            }
        }
    };

    @Override
    public void qingkong() {
        selectColorPopupWindow.dismiss();
        onClearColor();
    }

    @Override
    public void clickColor(ColorItemBean colorItemBean) {

        if (selectColorPopupWindow != null) {
            selectColorPopupWindow.dismiss();
            this.mColorClick = colorItemBean;

            if (mSelectListData.get(mColorClick.getColor_id()) == null) {
                mSelectListData.clear();
                mSelectListData.put(mColorClick.getColor_id(), mColorClick);
                if (mSelectListData != null && mSelectListData.size() > 0) {
                    bt_tag_page_item.setVisibility(View.VISIBLE);
                    iv_chongzhi.setVisibility(View.VISIBLE);
                    tv_color_tital.setVisibility(View.GONE);
                    for (Integer key : mSelectListData.keySet()) {
                        bt_tag_page_item.setText(mSelectListData.get(key).getColor_name());
                        if (mSelectListData.get(key).getColor_value().equalsIgnoreCase("ffffff")) {
                            GradientDrawable drawable = new GradientDrawable();
                            drawable.setCornerRadius(50);
                            drawable.setColor(Color.parseColor("#" + mSelectListData.get(key).getColor_value()));
                            drawable.setStroke(1, Color.parseColor("#262626"));
                            bt_tag_page_item.setBackgroundDrawable(drawable);
                            bt_tag_page_item.setTextColor(UIUtils.getColor(R.color.bg_262626));
                        } else {

                            GradientDrawable drawable = new GradientDrawable();
                            drawable.setCornerRadius(50);
                            drawable.setColor(Color.parseColor("#" + mSelectListData.get(key).getColor_value()));
                            bt_tag_page_item.setBackgroundDrawable(drawable);
                            bt_tag_page_item.setTextColor(UIUtils.getColor(R.color.white));
                        }
                    }
                }
                //友盟统计
                HashMap<String, String> map7 = new HashMap<String, String>();
                map7.put("evenname", "色彩选择");
                map7.put("even", "看图列表页-" + mColorClick.getColor_name());
                MobclickAgent.onEvent(activity, "shijian5", map7);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("看图列表页-" + mColorClick.getColor_name())  //事件类别
                        .setAction("色彩选择")      //事件操作
                        .build());
                mRecyclerView.scrollToPosition(0);
                onRefresh();
            } else {
                qingkong();
            }

        }
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
                        message.what = 4;
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

    private boolean ifClickAble = true;
    private Map<String, Integer> mapSearch = new HashMap<>();

}
