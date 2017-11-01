package com.homechart.app.home.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.activity.ArticleDetailsActivity;
import com.homechart.app.home.activity.ColorShaiXuanActivity;
import com.homechart.app.home.activity.HuoDongDetailsActivity;
import com.homechart.app.home.activity.ImageDetailLongActivity;
import com.homechart.app.home.activity.MessagesListActivity;
import com.homechart.app.home.activity.SearchActivity;
import com.homechart.app.home.activity.ShaiXuanResultActicity;
import com.homechart.app.home.activity.UserInfoActivity;
import com.homechart.app.home.adapter.HomeTagAdapter;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.color.ColorBean;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.shouye.DataBean;
import com.homechart.app.home.bean.shouye.SYActivityBean;
import com.homechart.app.home.bean.shouye.SYActivityInfoBean;
import com.homechart.app.home.bean.shouye.SYDataBean;
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
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.visearch.PhotoActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ValidFragment")
public class HomePicFragment1
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
    public ColorBean colorBean;
    private View view;
    private Timer timer = new Timer(true);
    private ImageView iv_center_msgicon;
    private RelativeLayout rl_tos_choose;

    private float mDownY;
    private float mMoveY;
    private boolean move_tag = true;
    private RelativeLayout id_main;
    private View view_line_back;
    boolean ifShouCang = true;
    private RoundImageView iv_secai;
    //任务
    private TimerTask task = new TimerTask() {
        public void run() {
            getUnReaderMsg();
        }
    };
    private int last_id = 0;
    private RelativeLayout rl_shibie;

    public HomePicFragment1(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public HomePicFragment1() {
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
        iv_secai = (RoundImageView) rootView.findViewById(R.id.iv_secai);
        rl_kongjian = (RelativeLayout) rootView.findViewById(R.id.rl_kongjian);
        rl_jubu = (RelativeLayout) rootView.findViewById(R.id.rl_jubu);
        rl_zhuangshi = (RelativeLayout) rootView.findViewById(R.id.rl_zhuangshi);
        rl_shouna = (RelativeLayout) rootView.findViewById(R.id.rl_shouna);
        rl_tos_choose = (RelativeLayout) rootView.findViewById(R.id.rl_tos_choose);
        id_main = (RelativeLayout) rootView.findViewById(R.id.id_main);
        view_line_back = rootView.findViewById(R.id.view_line_back);
        rl_shibie = (RelativeLayout) rootView.findViewById(R.id.rl_shibie);

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
        rl_shibie.setOnClickListener(this);
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
        getColorData();
        getUnReaderMsg();
        timer.schedule(task, 0, 1 * 60 * 1000);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cet_clearedit:

                onDismiss();
                Intent intent = new Intent(activity, SearchActivity.class);
                startActivity(intent);


                break;
            case R.id.iv_change_frag:

                if (curentListTag) {
                    curentListTag = false;
                    mRecyclerView.setPadding(UIUtils.getDimens(R.dimen.font_6), 0, UIUtils.getDimens(R.dimen.font_6), 0);
                    iv_change_frag.setImageResource(R.drawable.changtu);
                    mRecyclerView.setLayoutManager(staggeredGridLayoutManager);

                } else {
                    curentListTag = true;
                    mRecyclerView.setPadding(0, 0, 0, 0);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    iv_change_frag.setImageResource(R.drawable.pubuliu);

                }
                break;

            case R.id.rl_kongjian:
                //友盟统计
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("evenname", "首页筛选");
                map.put("even", "空间");
                MobclickAgent.onEvent(activity, "jtaction34", map);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("空间")  //事件类别
                        .setAction("首页筛选")      //事件操作
                        .build());
                showPopwindow(R.id.rl_kongjian, 0);
                break;
            case R.id.rl_jubu:
                //友盟统计
                HashMap<String, String> map1 = new HashMap<String, String>();
                map1.put("evenname", "首页筛选");
                map1.put("even", "局部");
                MobclickAgent.onEvent(activity, "jtaction34", map1);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("局部")  //事件类别
                        .setAction("首页筛选")      //事件操作
                        .build());
                showPopwindow(R.id.rl_jubu, 1);

                break;
            case R.id.rl_zhuangshi:
                //友盟统计
                HashMap<String, String> map2 = new HashMap<String, String>();
                map2.put("evenname", "首页筛选");
                map2.put("even", "装饰");
                MobclickAgent.onEvent(activity, "jtaction34", map2);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("装饰")  //事件类别
                        .setAction("首页筛选")      //事件操作
                        .build());
                showPopwindow(R.id.rl_zhuangshi, 2);

                break;
            case R.id.rl_shouna:
                //友盟统计
                HashMap<String, String> map3 = new HashMap<String, String>();
                map3.put("evenname", "首页筛选");
                map3.put("even", "收纳");
                MobclickAgent.onEvent(activity, "jtaction34", map3);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("收纳")  //事件类别
                        .setAction("首页筛选")      //事件操作
                        .build());
                showPopwindow(R.id.rl_shouna, 3);
                break;
            case R.id.rl_secai:
                //友盟统计
                HashMap<String, String> map4 = new HashMap<String, String>();
                map4.put("evenname", "首页筛选");
                map4.put("even", "色彩");
                MobclickAgent.onEvent(activity, "jtaction34", map4);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("色彩")  //事件类别
                        .setAction("首页筛选")      //事件操作
                        .build());
                showPopwindow(R.id.rl_secai, 4);
                break;
            case R.id.iv_center_msgicon:
                onDismiss();

                //友盟统计
                HashMap<String, String> map5 = new HashMap<String, String>();
                map5.put("evenname", "消息入口");
                map5.put("even", "首页");
                MobclickAgent.onEvent(activity, "jtaction37", map5);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("首页")  //事件类别
                        .setAction("消息入口")      //事件操作
                        .build());

                Intent intent_messages = new Intent(activity, MessagesListActivity.class);
                intent_messages.putExtra("notice_num", notice_num);
                intent_messages.putExtra("follow_notice", follow_notice);
                intent_messages.putExtra("collect_notice", collect_notice);
                intent_messages.putExtra("comment_notice", comment_notice);
                intent_messages.putExtra("system_notice", system_notice);
                startActivityForResult(intent_messages, 11);
                break;
            case R.id.rl_shibie:
                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA}, 0);
                } else {

                    //友盟统计
                    HashMap<String, String> map6 = new HashMap<String, String>();
                    map6.put("evenname", "找同款拍照入口");
                    map6.put("even", "记录用户启动拍照识别入口的次数");
                    MobclickAgent.onEvent(activity, "jtaction52", map6);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("记录用户启动拍照识别入口的次数")  //事件类别
                            .setAction("找同款拍照入口")      //事件操作
                            .build());

                    Intent intent1 = new Intent(activity, PhotoActivity.class);
                    startActivity(intent1);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
//            rl_unreader_msg_single.setVisibility(View.GONE);
//            rl_unreader_msg_double.setVisibility(View.GONE);
            getUnReaderMsg();
        }

    }

    private void tongjiYuan(String item_id) {
        //友盟统计
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("evenname", "三个色彩点");
        map.put("even", "首页");
        MobclickAgent.onEvent(activity, "jtaction3", map);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("首页")  //事件类别
                .setAction("三个色彩点")      //事件操作
                .build());
        //查看单图详情
        Intent intent = new Intent(activity, ImageDetailLongActivity.class);
        intent.putExtra("item_id", item_id);
        intent.putExtra("if_click_color", true);
        startActivity(intent);
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


                    if (mListData.get(position).getObject_info().getType().equals("single")) {

                        if (mListData.get(position).getObject_info().getCollect_num().trim().equals("0")) {
                            holder.getView(R.id.tv_shoucang_num).setVisibility(View.INVISIBLE);
                        } else {
                            holder.getView(R.id.tv_shoucang_num).setVisibility(View.VISIBLE);
                        }
                        ((TextView) holder.getView(R.id.tv_shoucang_num)).setText(mListData.get(position).getObject_info().getCollect_num());

                        if (!mListData.get(position).getObject_info().getIs_collected().equals("1")) {//未收藏
                            ((ImageView) holder.getView(R.id.iv_if_shoucang)).setImageResource(R.drawable.shoucang);
                        } else {//收藏
                            ((ImageView) holder.getView(R.id.iv_if_shoucang)).setImageResource(R.drawable.shoucang1);
                        }
                        holder.getView(R.id.tv_shoucang_num).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onShouCang(!mListData.get(position).getObject_info().getIs_collected().equals("1"), position, mListData.get(position));
                            }
                        });
                        holder.getView(R.id.iv_if_shoucang).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onShouCang(!mListData.get(position).getObject_info().getIs_collected().equals("1"), position, mListData.get(position));
                            }
                        });


                    }

                    ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                    if (curentListTag) {

                        layoutParams.width = width_Pic_List;
                    } else {

                        layoutParams.width = width_Pic_Staggered;
                    }
                    if (mListData.get(position).getObject_info().getType().equals("活动")) {

                        layoutParams.height = (curentListTag ? (int) (width_Pic_List / 2.36) : (int) (width_Pic_Staggered / mListData.get(position).getObject_info().getImage().getRatio()));

                    } else if (mListData.get(position).getObject_info().getType().equals("article")) {
                        layoutParams.height = (curentListTag ? (int) (width_Pic_List / 2.36666) : mSListDataHeight.get(position));
                    } else {
                        layoutParams.height = (curentListTag ? (int) (width_Pic_List / 1.5) : mSListDataHeight.get(position));
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
                } else if (mListData.get(position).getObject_info().getType().equals("article")) {

//                    ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
//                    layoutParams.width = width_Pic_List;
//                    layoutParams.height = (curentListTag ? (int) (width_Pic_List / 2.36) : (int) (width_Pic_Staggered / mListData.get(position).getObject_info().getImage().getRatio()));
//                    holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);

                    ImageUtils.displayFilletImage(mListData.get(position).getObject_info().getImage().getImg0(),
                            ((ImageView) holder.getView(R.id.iv_imageview_one)));
                    ((TextView) holder.getView(R.id.tv_name_pic)).setText(mListData.get(position).getObject_info().getTitle().toString());
                    holder.getView(R.id.iv_imageview_one).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //友盟统计
                            HashMap<String, String> map4 = new HashMap<String, String>();
                            map4.put("evenname", "文章入口");
                            map4.put("even", "首页");
                            MobclickAgent.onEvent(activity, "jtaction36", map4);
                            //ga统计
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory("首页")  //事件类别
                                    .setAction("文章入口")      //事件操作
                                    .build());

                            Intent intent = new Intent(activity, ArticleDetailsActivity.class);
                            intent.putExtra("article_id", mListData.get(position).getObject_info().getObject_id());
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

        } else {

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

        } else {

        }

    }

    @Override
    public void onLoadMore() {
        //友盟统计
        HashMap<String, String> map4 = new HashMap<String, String>();
        map4.put("evenname", "首页加载次数");
        map4.put("even", "首页");
        MobclickAgent.onEvent(activity, "jtaction51", map4);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("首页")  //事件类别
                .setAction("首页加载次数")      //事件操作
                .build());
        onLoaderTong();
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

    //获取未读消息数
    private void getUnReaderMsg() {

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
                                        (syActivityInfoBean.getId(), "活动", syActivityInfoBean.getTitle(), "",
                                                new SYDataObjectImgBean(syActivityInfoBean.getImage().getImg1_ratio(),
                                                        syActivityInfoBean.getImage().getImg0(), syActivityInfoBean.getImage().getImg1())
                                                , "", "", ""), null, null);
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
                if (null != listData && listData.size() > 0) {
                    mListData.addAll(listData);
                } else {
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


    private void getHeight(List<SYDataBean> item_list, String state) {
        if (state.equals(REFRESH_STATUS)) {
            mLListDataHeight.clear();
            mSListDataHeight.clear();
        }

        if (item_list.size() > 0) {
            for (int i = 0; i < item_list.size(); i++) {

                if (item_list.get(i).getObject_info().getType().equals("活动")) {
                    mLListDataHeight.add(Math.round(width_Pic_List / 1.333333f));
                } else {
                    mLListDataHeight.add(Math.round(width_Pic_List / 2.366666666f));
                }
//                mLListDataHeight.add(Math.round(width_Pic_List / 1.333333f));
                mSListDataHeight.add(Math.round(width_Pic_Staggered / item_list.get(i).getObject_info().getImage().getRatio()));
            }
        }
    }

    private void closeTagTongJi() {
    }

    private void openTagTongJi(String name) {

    }

    private void showPopwindow(int id, int position) {
        if (tagDataBean != null && colorBean != null) {
            if (null == homeTabPopWin) {
                homeTabPopWin = new HomeTabPopWin(activity, this, tagDataBean, this, colorBean, null);
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
                    iv_secai.setImageResource(R.drawable.secai1);
                } else {
                    last_id = id;

                    homeTabPopWin.setPagePosition(position);
                    switch (position) {
                        case 0:
                            iv_kongjian.setImageResource(R.drawable.kongjian);
                            iv_jubu.setImageResource(R.drawable.jubu1);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                            iv_shouna.setImageResource(R.drawable.shouna1);
                            iv_secai.setImageResource(R.drawable.secai1);
                            break;
                        case 1:
                            iv_kongjian.setImageResource(R.drawable.kongjian1);
                            iv_jubu.setImageResource(R.drawable.jubu);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                            iv_shouna.setImageResource(R.drawable.shouna1);
                            iv_secai.setImageResource(R.drawable.secai1);
                            break;
                        case 2:
                            iv_kongjian.setImageResource(R.drawable.kongjian1);
                            iv_jubu.setImageResource(R.drawable.jubu1);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi);
                            iv_shouna.setImageResource(R.drawable.shouna1);
                            iv_secai.setImageResource(R.drawable.secai1);
                            break;
                        case 3:
                            iv_kongjian.setImageResource(R.drawable.kongjian1);
                            iv_jubu.setImageResource(R.drawable.jubu1);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                            iv_shouna.setImageResource(R.drawable.shouna);
                            iv_secai.setImageResource(R.drawable.secai1);
                            break;
                        case 4:
                            iv_kongjian.setImageResource(R.drawable.kongjian1);
                            iv_jubu.setImageResource(R.drawable.jubu1);
                            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                            iv_shouna.setImageResource(R.drawable.shouna1);
                            iv_secai.setImageResource(R.drawable.secai);
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
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_jubu:
                        openTagTongJi("局部");
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_zhuangshi:
                        openTagTongJi("装饰");
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_shouna:
                        openTagTongJi("收纳");
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna);
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_secai:
                        openTagTongJi("色彩");
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        iv_secai.setImageResource(R.drawable.secai);
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
                iv_secai.setImageResource(R.drawable.secai1);
                break;
            case 1:
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai1);
                break;
            case 2:
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai1);
                break;
            case 3:
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna);
                iv_secai.setImageResource(R.drawable.secai1);
                break;
            case 4:
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai);
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
            iv_secai.setImageResource(R.drawable.secai1);
        }
    }

    @Override
    public void onItemClick(String tagStr) {
        onDismiss();
        //跳转到筛选结果页
        Intent intent = new Intent(activity, ShaiXuanResultActicity.class);
        intent.putExtra("shaixuan_tag", tagStr);
        intent.putExtra("islist", true);
        startActivity(intent);
    }

    @Override
    public void onItemColorClick(ColorItemBean colorItemBean) {
        if (homeTabPopWin != null) {
            closeTagTongJi();
            homeTabPopWin.dismiss();
            iv_kongjian.setImageResource(R.drawable.kongjian1);
            iv_jubu.setImageResource(R.drawable.jubu1);
            iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
            iv_shouna.setImageResource(R.drawable.shouna1);
            iv_secai.setImageResource(R.drawable.secai1);

            //友盟统计
            HashMap<String, String> map4 = new HashMap<String, String>();
            map4.put("evenname", "色彩单选");
            map4.put("even", "首页－" + colorItemBean.getColor_name());
            MobclickAgent.onEvent(activity, "jtaction35", map4);
            //ga统计
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("首页－" + colorItemBean.getColor_name())  //事件类别
                    .setAction("色彩单选")      //事件操作
                    .build());

            Intent intent = new Intent(activity, ColorShaiXuanActivity.class);
            intent.putExtra("color", colorItemBean);
            intent.putExtra("tagDataBean", tagDataBean);
            intent.putExtra("colorBean", colorBean);
            startActivity(intent);
        }
    }

    @Override
    public void onClearColor() {

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
        getUnReaderMsg();
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

    boolean ifClickShouCang = true;

    //收藏或者取消收藏，图片
    public void onShouCang(boolean ifShouCang, int position, SYDataBean syDataBean) {

        if (ifClickShouCang) {
            ifClickShouCang = false;
            if (ifShouCang) {
                //友盟统计
                HashMap<String, String> map4 = new HashMap<String, String>();
                map4.put("evenname", "收藏图片");
                map4.put("even", "首页");
                MobclickAgent.onEvent(activity, "jtaction5", map4);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("首页")  //事件类别
                        .setAction("收藏图片")      //事件操作
                        .build());
                //未被收藏，去收藏
                addShouCang(position, syDataBean.getObject_info().getObject_id());
            } else {
                //友盟统计
                HashMap<String, String> map4 = new HashMap<String, String>();
                map4.put("evenname", "取消收藏图片");
                map4.put("even", "首页");
                MobclickAgent.onEvent(activity, "jtaction6", map4);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("首页")  //事件类别
                        .setAction("取消收藏图片")      //事件操作
                        .build());
                //被收藏，去取消收藏
                removeShouCang(position, syDataBean.getObject_info().getObject_id());
            }

        }

    }

    //收藏
    private void addShouCang(final int position, String item_id) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(activity, "收藏成功");
                ifClickShouCang = true;
            }

            @Override
            public void onResponse(String s) {
                ifClickShouCang = true;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ToastUtils.showCenter(activity, "收藏成功");
                        mListData.get(position).getObject_info().setIs_collected("1");
                        try {
                            int collect_num = Integer.parseInt(mListData.get(position).getObject_info().getCollect_num().trim());
                            mListData.get(position).getObject_info().setCollect_num(++collect_num + "");
                        } catch (Exception e) {
                        }
                        mAdapter.notifyItemChanged(position);
                    } else {
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(activity, "收藏失败");
                }
            }
        };
        MyHttpManager.getInstance().addShouCang(item_id, callBack);
    }

    //取消收藏
    private void removeShouCang(final int position, String item_id) {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ifClickShouCang = true;
                ToastUtils.showCenter(activity, "取消收藏失败");

            }

            @Override
            public void onResponse(String s) {
                ifClickShouCang = true;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ToastUtils.showCenter(activity, "取消收藏成功");
                        mListData.get(position).getObject_info().setIs_collected("0");
                        try {
                            int collect_num = Integer.parseInt(mListData.get(position).getObject_info().getCollect_num().trim());
                            mListData.get(position).getObject_info().setCollect_num(--collect_num + "");
                        } catch (Exception e) {
                        }
                        mAdapter.notifyItemChanged(position);
                    } else {
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(activity, "取消收藏失败");
                }
            }
        };
        MyHttpManager.getInstance().removeShouCang(item_id, callBack);
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
                String info = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    notice_num = jsonObject.getInt("notice_num");
                    follow_notice = jsonObject.getInt("follow_notice");
                    collect_notice = jsonObject.getInt("collect_notice");
                    comment_notice = jsonObject.getInt("comment_notice");
                    system_notice = jsonObject.getInt("system_notice");
                    changeUnReaderMsg(notice_num + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 2) {
                String info = (String) msg.obj;
                tagDataBean = GsonUtil.jsonToBean(info, TagDataBean.class);
            } else if (msg.what == 3) {

                String info = (String) msg.obj;
                colorBean = GsonUtil.jsonToBean(info, ColorBean.class);
                Log.d("test", colorBean.toString());
            }
        }
    };


}
