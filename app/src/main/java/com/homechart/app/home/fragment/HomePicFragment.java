package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.activity.ArticleDetailsActivity;
import com.homechart.app.home.activity.HuoDongDetailsActivity;
import com.homechart.app.home.activity.ImageDetailLongActivity;
import com.homechart.app.home.activity.MessagesListActivity;
import com.homechart.app.home.activity.SearchActivity;
import com.homechart.app.home.activity.ShaiXuanResultActicity;
import com.homechart.app.home.activity.UserInfoActivity;
import com.homechart.app.home.adapter.HomeTagAdapter;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.shouye.DataBean;
import com.homechart.app.home.bean.shouye.SYActivityBean;
import com.homechart.app.home.bean.shouye.SYActivityInfoBean;
import com.homechart.app.home.bean.shouye.SYDataBean;
import com.homechart.app.home.bean.shouye.SYDataColorBean;
import com.homechart.app.home.bean.shouye.SYDataObjectBean;
import com.homechart.app.home.bean.shouye.SYDataObjectImgBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.myview.HomeTabPopWin;
import com.homechart.app.myview.RoundImageView;
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
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ValidFragment")
public class HomePicFragment
        extends BaseFragment
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener,
        ViewPager.OnPageChangeListener,
        HomeTagAdapter.PopupWindowCallBack {

    private FragmentManager fragmentManager;
    private ImageView iv_change_frag;

    private HRecyclerView mRecyclerView;

    private List<SYDataBean> mListData = new ArrayList<>();
    private List<Integer> mLListDataHeight = new ArrayList<>();
    private List<Integer> mSListDataHeight = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;
    private MultiItemCommonAdapter<SYDataBean> mAdapter;
    private int page_num = 1;
    private int TYPE_ONE = 1;
    private int TYPE_TWO = 2;
    private int TYPE_THREE = 3;
    private int TYPE_FOUR = 4;
    private int TYPE_FIVE = 5;
    private int position;
    private boolean curentListTag = true;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ClearEditText cet_clearedit;
    private RelativeLayout rl_unreader_msg_double;
    private RelativeLayout rl_unreader_msg_single;
    private TextView tv_unreader_mag_double;
    private TextView tv_unreader_mag_single;
    private int width_Pic_Staggered;
    private int width_Pic_List;
    private HomeTabPopWin homeTabPopWin;
    private LinearLayout ll_pic_choose;
    private RoundImageView iv_kongjian;
    private RoundImageView iv_jubu;
    private RoundImageView iv_zhuangshi;
    private RoundImageView iv_shouna;
    private RelativeLayout rl_kongjian;
    private RelativeLayout rl_jubu;
    private RelativeLayout rl_zhuangshi;
    private RelativeLayout rl_shouna;
    public TagDataBean tagDataBean;
    private View view;
    private Timer timer = new Timer(true);
    private ImageView iv_center_msgicon;
    private RelativeLayout rl_tos_choose;

    private float mDownY;
    private float mMoveY;
    private boolean move_tag = true;
    private RelativeLayout id_main;
    private View view_line_back;

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
        tv_unreader_mag_double = (TextView) rootView.findViewById(R.id.tv_unreader_mag_double);
        tv_unreader_mag_single = (TextView) rootView.findViewById(R.id.tv_unreader_mag_single);
        rl_unreader_msg_single = (RelativeLayout) rootView.findViewById(R.id.rl_unreader_msg_single);
        rl_unreader_msg_double = (RelativeLayout) rootView.findViewById(R.id.rl_unreader_msg_double);

        iv_center_msgicon = (ImageView) rootView.findViewById(R.id.iv_center_msgicon);
        cet_clearedit = (ClearEditText) rootView.findViewById(R.id.cet_clearedit);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_pic);

        ll_pic_choose = (LinearLayout) rootView.findViewById(R.id.ll_pic_choose);
        iv_change_frag = (ImageView) rootView.findViewById(R.id.iv_change_frag);
        iv_kongjian = (RoundImageView) rootView.findViewById(R.id.iv_kongjian);
        iv_jubu = (RoundImageView) rootView.findViewById(R.id.iv_jubu);
        iv_zhuangshi = (RoundImageView) rootView.findViewById(R.id.iv_zhuangshi);
        iv_shouna = (RoundImageView) rootView.findViewById(R.id.iv_shouna);
        rl_kongjian = (RelativeLayout) rootView.findViewById(R.id.rl_kongjian);
        rl_jubu = (RelativeLayout) rootView.findViewById(R.id.rl_jubu);
        rl_zhuangshi = (RelativeLayout) rootView.findViewById(R.id.rl_zhuangshi);
        rl_shouna = (RelativeLayout) rootView.findViewById(R.id.rl_shouna);
        rl_tos_choose = (RelativeLayout) rootView.findViewById(R.id.rl_tos_choose);
        id_main = (RelativeLayout) rootView.findViewById(R.id.id_main);
        view_line_back = rootView.findViewById(R.id.view_line_back);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initListener() {
        super.initListener();
        cet_clearedit.setKeyListener(null);
        cet_clearedit.setOnClickListener(this);
        iv_change_frag.setOnClickListener(this);
        rl_kongjian.setOnClickListener(this);
        rl_jubu.setOnClickListener(this);
        rl_zhuangshi.setOnClickListener(this);
        rl_shouna.setOnClickListener(this);
        iv_center_msgicon.setOnClickListener(this);
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
                                rl_tos_choose.setVisibility(View.VISIBLE);
                            } else {
                                rl_tos_choose.setVisibility(View.GONE);
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
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        width_Pic_Staggered = PublicUtils.getScreenWidth(activity) / 2 - UIUtils.getDimens(R.dimen.font_20);
        width_Pic_List = PublicUtils.getScreenWidth(activity) - UIUtils.getDimens(R.dimen.font_14);
        buildRecyclerView();
        getTagData();
        getUnReaderMsg();
        timer.schedule(task, 0, 1 * 60 * 1000);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cet_clearedit:

                //友盟统计
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("evenname", "关键词搜索");
                map.put("even", "单击首页顶部搜索框");
                MobclickAgent.onEvent(activity, "action1", map);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("单击首页顶部搜索框")  //事件类别
                        .setAction("关键词搜索")      //事件操作
                        .build());

                onDismiss();
                Intent intent = new Intent(activity, SearchActivity.class);
                startActivity(intent);

                break;
            case R.id.iv_change_frag:

                if (curentListTag) {
                    //友盟统计
                    HashMap<String, String> map1 = new HashMap<String, String>();
                    map1.put("evenname", "首页图片单列_双列");
                    map1.put("even", "首页图片单双列切换按钮");
                    MobclickAgent.onEvent(activity, "action15", map1);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("首页图片单双列切换按钮")  //事件类别
                            .setAction("首页图片单列_双列")      //事件操作
                            .build());
                    mRecyclerView.setPadding(UIUtils.getDimens(R.dimen.font_6), 0, UIUtils.getDimens(R.dimen.font_6), 0);
                    iv_change_frag.setImageResource(R.drawable.changtu);
                    mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                    curentListTag = false;
                } else {
                    //友盟统计
                    HashMap<String, String> map1 = new HashMap<String, String>();
                    map1.put("evenname", "首页图片双列_单列");
                    map1.put("even", "首页图片单双列切换按钮");
                    MobclickAgent.onEvent(activity, "action16", map1);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("首页图片单双列切换按钮")  //事件类别
                            .setAction("首页图片双列_单列")      //事件操作
                            .build());
                    mRecyclerView.setPadding(0, 0, 0, 0);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    iv_change_frag.setImageResource(R.drawable.pubuliu);
                    curentListTag = true;
                }
                break;

            case R.id.rl_kongjian:
                showPopwindow(R.id.rl_kongjian, 0);
                break;
            case R.id.rl_jubu:
                showPopwindow(R.id.rl_jubu, 1);

                break;
            case R.id.rl_zhuangshi:
                showPopwindow(R.id.rl_zhuangshi, 2);

                break;
            case R.id.rl_shouna:
                showPopwindow(R.id.rl_shouna, 3);
                break;
            case R.id.iv_center_msgicon:
                onDismiss();
                Intent intent_messages = new Intent(activity, MessagesListActivity.class);
                startActivityForResult(intent_messages, 11);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            rl_unreader_msg_single.setVisibility(View.GONE);
            rl_unreader_msg_double.setVisibility(View.GONE);
        }

    }

    private void buildRecyclerView() {

        MultiItemTypeSupport<SYDataBean> support = new MultiItemTypeSupport<SYDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_ONE) {
                    return R.layout.item_test_one;
                } else if (itemType == TYPE_TWO) {
                    return R.layout.item_test_pic_pubu;
                } else if (itemType == TYPE_THREE) {
                    //活动(线性)
                    return R.layout.item_test_huodong_list;
                } else if (itemType == TYPE_FOUR) {
                    //活动(瀑布)
                    return R.layout.item_test_huodong_pubu;
                } else {
                    //文章(线性)
                    return R.layout.item_test_article_list;
                }

            }

            @Override
            public int getItemViewType(int position, SYDataBean s) {
                if (s.getObject_info().getType().equals("活动")) {
                    if (curentListTag) {
                        return TYPE_THREE;
                    } else {
                        return TYPE_FOUR;
                    }
                } else if (s.getObject_info().getType().equals("single")) {
                    if (curentListTag) {
                        return TYPE_ONE;
                    } else {
                        return TYPE_TWO;
                    }
                } else if (s.getObject_info().getType().equals("article")) {
                    return TYPE_FIVE;
                } else {
                    return TYPE_FIVE;
                }

            }
        };

        mAdapter = new MultiItemCommonAdapter<SYDataBean>(activity, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {

                if (mListData.get(position).getObject_info().getType().equals("活动")
                        || mListData.get(position).getObject_info().getType().equals("single")) {

                    ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                    layoutParams.width = width_Pic_List;
                    if (mListData.get(position).getObject_info().getType().equals("活动")) {

                        layoutParams.height = (curentListTag ? (int) (width_Pic_List / 2.36) : (int) (width_Pic_Staggered / mListData.get(position).getObject_info().getImage().getRatio()));

                    } else {
                        layoutParams.height = (curentListTag ? mLListDataHeight.get(position) : mSListDataHeight.get(position));
                    }
                    holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                    String nikeName = "";
                    if (mListData.get(position).getObject_info().getType().equals("活动")) {
                        nikeName = mListData.get(position).getObject_info().getTag();
                    } else {
                        nikeName = mListData.get(position).getUser_info().getNickname();
                        if (nikeName != null && curentListTag && nikeName.length() > 8) {
                            nikeName = nikeName.substring(0, 8) + "...";
                        }
                        if (nikeName != null && !curentListTag && nikeName.length() > 5) {
                            nikeName = nikeName.substring(0, 5) + "...";
                        }
                    }

                    ((TextView) holder.getView(R.id.tv_name_pic)).setText(nikeName);
                    if (curentListTag) {
                        ImageUtils.displayFilletImage(mListData.get(position).getObject_info().getImage().getImg0(),
                                (ImageView) holder.getView(R.id.iv_imageview_one));
                    } else {
                        ImageUtils.displayFilletImage(mListData.get(position).getObject_info().getImage().getImg1(),
                                (ImageView) holder.getView(R.id.iv_imageview_one));
                    }
                    if (!mListData.get(position).getObject_info().getType().equals("活动")) {
                        ImageUtils.displayFilletImage(mListData.get(position).getUser_info().getAvatar().getBig(),
                                (ImageView) holder.getView(R.id.iv_header_pic));
                    } else {
                        holder.getView(R.id.iv_imageview_one).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //友盟统计
                                HashMap<String, String> map1 = new HashMap<String, String>();
                                map1.put("evenname", "活动封面图点击");
                                map1.put("even", "点击封面图查看");
                                MobclickAgent.onEvent(activity, "action34", map1);
                                //ga统计
                                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                        .setCategory("点击封面图查看")  //事件类别
                                        .setAction("活动封面图点击")      //事件操作
                                        .build());
                                //查看活动详情
                                Intent intent = new Intent(activity, HuoDongDetailsActivity.class);
                                intent.putExtra("activity_id", mListData.get(position).getObject_info().getObject_id());
                                startActivity(intent);
                            }
                        });
                    }

                    if (!mListData.get(position).getObject_info().getType().equals("活动")) {
                        if (curentListTag) {
                            if (!mListData.get(position).getUser_info().getProfession().equals("0")) {
                                holder.getView(R.id.iv_desiner_icon).setVisibility(View.VISIBLE);
                            } else {
                                holder.getView(R.id.iv_desiner_icon).setVisibility(View.GONE);
                            }
                        }
                        List<SYDataColorBean> list_color = mListData.get(position).getColor_info();
                        if (null != list_color && list_color.size() == 1) {
                            holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                            holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                            holder.getView(R.id.iv_color_center).setVisibility(View.GONE);
                            holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                            if (curentListTag) {
                                holder.getView(R.id.tv_color_tital).setVisibility(View.VISIBLE);
                            }
                        } else if (null != mListData.get(position).getColor_info() && mListData.get(position).getColor_info().size() == 2) {

                            holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                            holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                            holder.getView(R.id.iv_color_center).setVisibility(View.VISIBLE);
                            holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(1).getColor_value()));
                            holder.getView(R.id.iv_color_center).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                            if (curentListTag) {
                                holder.getView(R.id.tv_color_tital).setVisibility(View.VISIBLE);
                            }
                        } else if (null != mListData.get(position).getColor_info() && mListData.get(position).getColor_info().size() == 3) {
                            holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                            holder.getView(R.id.iv_color_left).setVisibility(View.VISIBLE);
                            holder.getView(R.id.iv_color_center).setVisibility(View.VISIBLE);
                            holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(2).getColor_value()));
                            holder.getView(R.id.iv_color_center).setBackgroundColor(Color.parseColor("#" + list_color.get(1).getColor_value()));
                            holder.getView(R.id.iv_color_left).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                            if (curentListTag) {
                                holder.getView(R.id.tv_color_tital).setVisibility(View.VISIBLE);
                            }
                        } else {
                            holder.getView(R.id.iv_color_right).setVisibility(View.GONE);
                            holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                            holder.getView(R.id.iv_color_center).setVisibility(View.GONE);
                            if (curentListTag) {
                                holder.getView(R.id.tv_color_tital).setVisibility(View.GONE);
                            }
                        }
                    }

                    if (null != mListData.get(position).getUser_info() && null != mListData.get(position).getUser_info().getUser_id()) {
                        holder.getView(R.id.iv_header_pic).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(activity, UserInfoActivity.class);
                                intent.putExtra(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                                startActivity(intent);
                            }
                        });
                        holder.getView(R.id.iv_imageview_one).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (mListData.get(position).getObject_info().getType().equals("single")) {
                                    //查看单图详情
                                    Intent intent = new Intent(activity, ImageDetailLongActivity.class);
                                    intent.putExtra("item_id", mListData.get(position).getObject_info().getObject_id());
                                    startActivity(intent);
                                }

                            }
                        });
                    }

                }else if(mListData.get(position).getObject_info().getType().equals("article")){

//                    ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
//                    layoutParams.width = width_Pic_List;
//                    layoutParams.height = (curentListTag ? (int) (width_Pic_List / 2.36) : (int) (width_Pic_Staggered / mListData.get(position).getObject_info().getImage().getRatio()));
//                    holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);

                    ImageUtils.displayFilletImage(mListData.get(position).getObject_info().getImage().getImg0(),
                            ((ImageView) holder.getView(R.id.iv_imageview_one)));
                    ((TextView)holder.getView(R.id.tv_name_pic)).setText(mListData.get(position).getObject_info().getTitle().toString());
                    holder.getView(R.id.iv_imageview_one).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity,ArticleDetailsActivity.class);
                            intent.putExtra("article_id",mListData.get(position).getObject_info().getObject_id());

                            startActivity(intent);
                        }
                    });

                }
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);

        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }


    private void onRefreshTong() {

        if (curentListTag) {
            //友盟统计
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("evenname", "首页图片单列刷新");
            map.put("even", "首页图片单列向下翻页");
            MobclickAgent.onEvent(activity, "action11", map);
            //ga统计
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("首页图片单列向下翻页")  //事件类别
                    .setAction("首页图片单列刷新")      //事件操作
                    .build());
        } else {
            //友盟统计
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("evenname", "首页图片双列刷新");
            map.put("even", "首页图片双列向下翻页");
            MobclickAgent.onEvent(activity, "action13", map);
            //ga统计
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("首页图片双列向下翻页")  //事件类别
                    .setAction("首页图片双列刷新")      //事件操作
                    .build());
        }

    }

    @Override
    public void onRefresh() {

        onRefreshTong();
        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getListData(REFRESH_STATUS);
    }

    private void onLoaderTong() {
        if (curentListTag) {
            //友盟统计
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("evenname", "首页图片单列加载");
            map.put("even", "首页图片单列向上翻页");
            MobclickAgent.onEvent(activity, "action12", map);
            //ga统计
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("首页图片单列向上翻页")  //事件类别
                    .setAction("首页图片单列加载")      //事件操作
                    .build());
        } else {
            //友盟统计
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("evenname", "首页图片双列加载");
            map.put("even", "首页图片双列向上翻页");
            MobclickAgent.onEvent(activity, "action14", map);
            //ga统计
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("首页图片双列向上翻页")  //事件类别
                    .setAction("首页图片双列加载")      //事件操作
                    .build());
        }

    }

    @Override
    public void onLoadMore() {
        onLoaderTong();
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getListData(LOADMORE_STATUS);
    }

    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String info = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    String num = jsonObject.getString("notice_num");
                    changeUnReaderMsg(num);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 2) {
                String info = (String) msg.obj;
                tagDataBean = GsonUtil.jsonToBean(info, TagDataBean.class);
            }
        }
    };

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
                    ToastUtils.showCenter(activity, getString(R.string.filter_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getPicTagData(callBack);
    }

    //获取未读消息数
    private void getUnReaderMsg() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, getString(R.string.unreader_msg_get_error));
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
        MyHttpManager.getInstance().getUnReadMsg(callBack);

    }


    private void changeUnReaderMsg(String num) {

        int num_int = Integer.parseInt(num.trim());
        if (num_int == 0) {
            rl_unreader_msg_double.setVisibility(View.GONE);
            rl_unreader_msg_single.setVisibility(View.GONE);
        } else {
            if (num_int < 10) {
                rl_unreader_msg_double.setVisibility(View.GONE);
                rl_unreader_msg_single.setVisibility(View.VISIBLE);
                tv_unreader_mag_single.setText(num_int + "");
            } else if (10 <= num_int && num_int <= 99) {
                rl_unreader_msg_double.setVisibility(View.VISIBLE);
                rl_unreader_msg_single.setVisibility(View.GONE);
                tv_unreader_mag_double.setText(num_int + "");
            } else {
                rl_unreader_msg_double.setVisibility(View.VISIBLE);
                rl_unreader_msg_single.setVisibility(View.GONE);
                tv_unreader_mag_double.setText("99");
            }
        }
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
                ToastUtils.showCenter(activity, getString(R.string.recommend_get_error));

            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        DataBean dataBean = GsonUtil.jsonToBean(data_msg, DataBean.class);
                        List<SYActivityBean> list_activity = dataBean.getActivity_list();
                        List<SYDataBean> list_data = dataBean.getObject_list();
                        List<SYDataBean> list_newdata = new ArrayList<>();
                        if (null != list_data && 0 != list_data.size()) {

                            if (list_activity != null && list_activity.size() > 0) {
                                SYActivityInfoBean syActivityInfoBean = list_activity.get(0).getActivity_info();
                                SYDataBean syDataBean = new SYDataBean(new SYDataObjectBean
                                        (syActivityInfoBean.getId(), "活动", syActivityInfoBean.getTitle(),"",
                                                new SYDataObjectImgBean(syActivityInfoBean.getImage().getImg1_ratio(),
                                                        syActivityInfoBean.getImage().getImg0(), syActivityInfoBean.getImage().getImg1())
                                        ), null, null);
                                if (state.equals(REFRESH_STATUS)) {
                                    list_newdata.add(syDataBean);
                                    list_newdata.addAll(list_data);
                                }

                                getHeight(list_newdata, state);
                                updateViewFromData(list_newdata, state);
                            } else {
                                getHeight(list_data, state);
                                updateViewFromData(list_data, state);
                            }

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
        MyHttpManager.getInstance().getRecommendList((page_num - 1) * 20 + "", "20", callBack);

    }

    private void updateViewFromData(List<SYDataBean> listData, String state) {

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


    private void getHeight(List<SYDataBean> item_list, String state) {
        if (state.equals(REFRESH_STATUS)) {
            mLListDataHeight.clear();
            mSListDataHeight.clear();
        }

        if (item_list.size() > 0) {
            for (int i = 0; i < item_list.size(); i++) {

                if(item_list.get(i).getObject_info().getType().equals("活动")){
                    mLListDataHeight.add(Math.round(width_Pic_List / 1.333333f));
                }else {
                    mLListDataHeight.add(Math.round(width_Pic_List / 2.366666666f));
                }
//                mLListDataHeight.add(Math.round(width_Pic_List / 1.333333f));
                mSListDataHeight.add(Math.round(width_Pic_Staggered / item_list.get(i).getObject_info().getImage().getRatio()));
            }
        }
    }

    private void closeTagTongJi() {
        //友盟统计
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("evenname", "首页图片取消筛选");
        map.put("even", "首页图片取消筛选");
        MobclickAgent.onEvent(activity, "action10", map);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("首页图片取消筛选")  //事件类别
                .setAction("首页图片取消筛选")      //事件操作
                .build());
    }

    private void openTagTongJi(String name) {
        //空间局部装饰收纳
        //友盟统计
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("evenname", "筛选_" + name);
        map.put("even", "点开" + name + "筛选项");
        if (name.equals("空间")) {
            MobclickAgent.onEvent(activity, "action6", map);
        } else if (name.equals("局部")) {
            MobclickAgent.onEvent(activity, "action7", map);
        } else if (name.equals("装饰")) {
            MobclickAgent.onEvent(activity, "action8", map);
        } else if (name.equals("收纳")) {
            MobclickAgent.onEvent(activity, "action9", map);
        }

        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("点开" + name + "筛选项")  //事件类别
                .setAction("筛选_" + name)      //事件操作
                .build());


    }

    private void showPopwindow(int id, int position) {
        if (tagDataBean != null) {
            if (null == homeTabPopWin) {
                homeTabPopWin = new HomeTabPopWin(activity, this, tagDataBean, this);
            }
            if (homeTabPopWin.isShowing()) {
                if (last_id != 0 && last_id == id) {
                    last_id = 0;
                    closeTagTongJi();
                    homeTabPopWin.dismiss();
                    iv_kongjian.setImageResource(R.drawable.kongjian1);
                    iv_jubu.setImageResource(R.drawable.jubu1);
                    iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                    iv_shouna.setImageResource(R.drawable.shouna1);
                } else {
                    last_id = id;

                    homeTabPopWin.setPagePosition(position);
                    switch (position) {
                        case 0:
                            iv_kongjian.setImageResource(R.drawable.kongjian);
                            iv_jubu.setImageResource(R.drawable.jubu1);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                            iv_shouna.setImageResource(R.drawable.shouna1);
                            break;
                        case 1:
                            iv_kongjian.setImageResource(R.drawable.kongjian1);
                            iv_jubu.setImageResource(R.drawable.jubu);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                            iv_shouna.setImageResource(R.drawable.shouna1);
                            break;
                        case 2:
                            iv_kongjian.setImageResource(R.drawable.kongjian1);
                            iv_jubu.setImageResource(R.drawable.jubu1);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi);
                            iv_shouna.setImageResource(R.drawable.shouna1);
                            break;
                        case 3:
                            iv_kongjian.setImageResource(R.drawable.kongjian1);
                            iv_jubu.setImageResource(R.drawable.jubu1);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                            iv_shouna.setImageResource(R.drawable.shouna);
                            break;
                    }
                }

            } else {

                homeTabPopWin.setPagePosition(position);
                last_id = id;
                if (Build.VERSION.SDK_INT < 24) {
                    homeTabPopWin.showAsDropDown(view_line_back);
                } else {
                    // 获取控件的位置，安卓系统>7.0
                    int[] location = new int[2];
                    view_line_back.getLocationOnScreen(location);
                    int screenHeight = PublicUtils.getScreenHeight(activity);
                    homeTabPopWin.setHeight(screenHeight - location[1]);
                    homeTabPopWin.showAtLocation(ll_pic_choose, Gravity.NO_GRAVITY, 0, location[1]);
                }


                switch (id) {
                    case R.id.rl_kongjian:
                        openTagTongJi("空间");
                        iv_kongjian.setImageResource(R.drawable.kongjian);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        break;
                    case R.id.rl_jubu:
                        openTagTongJi("局部");
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        break;
                    case R.id.rl_zhuangshi:
                        openTagTongJi("装饰");
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        break;
                    case R.id.rl_shouna:
                        openTagTongJi("收纳");
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna);
                        break;
                }
            }
        } else {
            getTagData();
            ToastUtils.showCenter(activity, "数据加载中");
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                iv_kongjian.setImageResource(R.drawable.kongjian);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                break;
            case 1:
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                break;
            case 2:
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi);
                iv_shouna.setImageResource(R.drawable.shouna1);
                break;
            case 3:
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {


    }

    //关闭tab弹出框
    @Override
    public void onDismiss() {
        if (homeTabPopWin != null) {
            closeTagTongJi();
            homeTabPopWin.dismiss();
            iv_kongjian.setImageResource(R.drawable.kongjian1);
            iv_jubu.setImageResource(R.drawable.jubu1);
            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
            iv_shouna.setImageResource(R.drawable.shouna1);
        }
    }

    @Override
    public void onItemClick(String tagStr) {
        onDismiss();
        //跳转到筛选结果页
        Intent intent = new Intent(activity, ShaiXuanResultActicity.class);
        intent.putExtra("shaixuan_tag", tagStr);
        startActivity(intent);
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

        MobclickAgent.onResume(activity);
    }


    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPause(activity);
    }

    //任务
    private TimerTask task = new TimerTask() {
        public void run() {
            getUnReaderMsg();
        }
    };
    private int last_id = 0;

}
