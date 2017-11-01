package com.homechart.app.home.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import android.text.Html;
import android.text.TextUtils;
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
import com.homechart.app.home.activity.HomeActivity;
import com.homechart.app.home.activity.HuoDongDetailsActivity;
import com.homechart.app.home.activity.HuoDongListActivity;
import com.homechart.app.home.activity.ImageDetailLongActivity;
import com.homechart.app.home.activity.ImageDetailScrollActivity;
import com.homechart.app.home.activity.LoginActivity;
import com.homechart.app.home.activity.MessagesListActivity;
import com.homechart.app.home.activity.SearchActivity;
import com.homechart.app.home.activity.ShaiXuanResultActicity;
import com.homechart.app.home.activity.UserInfoActivity;
import com.homechart.app.home.adapter.HomeTagAdapter;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.color.ColorBean;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.search.SearchDataBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.bean.shaijia.ShaiJiaItemBean;
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
import com.homechart.app.myview.SelectColorSeCaiWindow;
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
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.visearch.PhotoActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ValidFragment")
public class HomePicFragment
        extends BaseFragment
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener,
        ViewPager.OnPageChangeListener,
        HomeTagAdapter.PopupWindowCallBack,
        SelectColorSeCaiWindow.SureColor {

    private FragmentManager fragmentManager;
    private ImageView iv_change_frag;

    private HRecyclerView mRecyclerView;

    private List<SearchItemDataBean> mListData = new ArrayList<>();
    private List<Integer> mLListDataHeight = new ArrayList<>();
    private List<Integer> mSListDataHeight = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;
    private MultiItemCommonAdapter<SearchItemDataBean> mAdapter;
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
    private RelativeLayout rl_secai;
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
        iv_kongjian = (RoundImageView) rootView.findViewById(R.id.iv_kongjian);
        iv_jubu = (RoundImageView) rootView.findViewById(R.id.iv_jubu);
        iv_zhuangshi = (RoundImageView) rootView.findViewById(R.id.iv_zhuangshi);
        iv_shouna = (RoundImageView) rootView.findViewById(R.id.iv_shouna);
        iv_secai = (RoundImageView) rootView.findViewById(R.id.iv_secai);
        rl_kongjian = (RelativeLayout) rootView.findViewById(R.id.rl_kongjian);
        rl_jubu = (RelativeLayout) rootView.findViewById(R.id.rl_jubu);
        rl_zhuangshi = (RelativeLayout) rootView.findViewById(R.id.rl_zhuangshi);
        rl_shouna = (RelativeLayout) rootView.findViewById(R.id.rl_shouna);
        rl_secai = (RelativeLayout) rootView.findViewById(R.id.rl_secai);
        rl_tos_choose = (RelativeLayout) rootView.findViewById(R.id.rl_tos_choose);
        id_main = (RelativeLayout) rootView.findViewById(R.id.id_main);
        view_line_back = rootView.findViewById(R.id.view_line_back);
        rl_shibie = (RelativeLayout) rootView.findViewById(R.id.rl_shibie);


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
        rl_kongjian.setOnClickListener(this);
        rl_jubu.setOnClickListener(this);
        rl_zhuangshi.setOnClickListener(this);
        rl_shouna.setOnClickListener(this);
        iv_chongzhi.setOnClickListener(this);
        rl_secai.setOnClickListener(this);
        iv_center_msgicon.setOnClickListener(this);
        rl_shibie.setOnClickListener(this);

        tv_color_tital.setOnClickListener(this);
        iv_color_icon.setOnClickListener(this);
        bt_tag_page_item.setOnClickListener(this);
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
                    Intent intent1 = new Intent(activity, PhotoActivity.class);
                    startActivity(intent1);
                }
                break;

            case R.id.iv_chongzhi:
                mSelectListData.clear();
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai1);
                bt_tag_page_item.setVisibility(View.GONE);
                iv_chongzhi.setVisibility(View.GONE);
                tv_color_tital.setVisibility(View.VISIBLE);
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
//            rl_unreader_msg_single.setVisibility(View.GONE);
//            rl_unreader_msg_double.setVisibility(View.GONE);
            getUnReaderMsg();
        } else if (requestCode == 1) {
//            onRefresh();
        }
    }

    List<String> listTag = new ArrayList<>();

    private void buildRecyclerView() {

        MultiItemTypeSupport<SearchItemDataBean> support = new MultiItemTypeSupport<SearchItemDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_ONE) {
                    return R.layout.item_list_new;
                } else {
                    return R.layout.item_pubu_new;
                }
            }

            @Override
            public int getItemViewType(int position, SearchItemDataBean s) {
                if (curentListTag) {
                    return TYPE_ONE;
                } else {
                    return TYPE_TWO;
                }
            }
        };

        mAdapter = new MultiItemCommonAdapter<SearchItemDataBean>(activity, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {
                scroll_position = position;
                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();

//                layoutParams.width = (curentListTag ? width_Pic_List : width_Pic_Staggered);
                layoutParams.height = (curentListTag ? mLListDataHeight.get(position) : mSListDataHeight.get(position));
                holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                String nikeName = mListData.get(position).getUser_info().getNickname();

                if (nikeName != null && curentListTag && nikeName.length() > 8) {
                    nikeName = nikeName.substring(0, 8) + "...";
                }
                if (nikeName != null && !curentListTag && nikeName.length() > 5) {
                    nikeName = nikeName.substring(0, 5) + "...";
                }
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
                        strTag = strTag + "# " + listTag.get(i) + "  ";
                    }
                }

                String str = "<font color='#f79056'>" + strTag + "</font>" + mListData.get(position).getItem_info().getDescription();
                ((TextView) holder.getView(R.id.tv_image_miaosu)).setText(Html.fromHtml(str));


                if (curentListTag) {
                    ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg0(),
                            (ImageView) holder.getView(R.id.iv_imageview_one));
                } else {
                    ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg1(),
                            (ImageView) holder.getView(R.id.iv_imageview_one));

                }

                ImageUtils.displayFilletImage(mListData.get(position).getUser_info().getAvatar().getBig(),
                        (ImageView) holder.getView(R.id.iv_header_pic));

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
                        //查看单图详情
                        Intent intent = new Intent(activity, ImageDetailScrollActivity.class);
                        intent.putExtra("item_id", mListData.get(position).getItem_info().getItem_id());
                        intent.putExtra("position", position);
                        intent.putExtra("type", "色彩");
                        intent.putExtra("if_click_color", false);
                        intent.putExtra("mSelectListData", (Serializable) mSelectListData);
                        intent.putExtra("shaixuan_tag", "");
                        intent.putExtra("page_num", page_num + 1);
                        intent.putExtra("item_id_list", (Serializable) mItemIdList);
                        startActivity(intent);
                    }
                });

                holder.getView(R.id.tv_shoucang_num).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
                        if (!loginStatus) {
                            Intent intent = new Intent(activity, LoginActivity.class);
                            startActivityForResult(intent, 1);
                        } else {
                            onShouCang(!mListData.get(position).getItem_info().getIs_collected().trim().equals("1"), position, mListData.get(position));
                        }
                    }
                });
                holder.getView(R.id.iv_if_shoucang).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
                        if (!loginStatus) {
                            Intent intent = new Intent(activity, LoginActivity.class);
                            startActivityForResult(intent, 1);
                        } else {
                            onShouCang(!mListData.get(position).getItem_info().getIs_collected().trim().equals("1"), position, mListData.get(position));
                        }
                    }
                });

                if (mListData.get(position).getItem_info().getCollect_num().trim().equals("0")) {
                    holder.getView(R.id.tv_shoucang_num).setVisibility(View.INVISIBLE);
                } else {
                    holder.getView(R.id.tv_shoucang_num).setVisibility(View.VISIBLE);
                }
                ((TextView) holder.getView(R.id.tv_shoucang_num)).setText(mListData.get(position).getItem_info().getCollect_num());

                if (curentListTag) {
                    if (!mListData.get(position).getItem_info().getIs_collected().equals("1")) {//未收藏
                        ((ImageView) holder.getView(R.id.iv_if_shoucang)).setImageResource(R.drawable.datuxing);
                    } else {//收藏
                        ((ImageView) holder.getView(R.id.iv_if_shoucang)).setImageResource(R.drawable.datuxing1);
                    }
                } else {
                    if (!mListData.get(position).getItem_info().getIs_collected().equals("1")) {//未收藏
                        ((ImageView) holder.getView(R.id.iv_if_shoucang)).setImageResource(R.drawable.xiaotuxing);
                    } else {//收藏
                        ((ImageView) holder.getView(R.id.iv_if_shoucang)).setImageResource(R.drawable.xiaotuxing1);
                    }
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

    @Override
    public void onRefresh() {

        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getListData(REFRESH_STATUS);
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
                        if (null != searchDataBean.getItem_list() && 0 != searchDataBean.getItem_list().size()) {
                            getHeight(searchDataBean.getItem_list(), state);
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
                mListData.clear();
                mItemIdList.clear();
                if (null != listData && listData.size() > 0) {
                    mListData.addAll(listData);
                    for (int i = 0; i < listData.size(); i++) {
                        mItemIdList.add(listData.get(i).getItem_info().getItem_id());
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
                        mItemIdList.add(listData.get(i).getItem_info().getItem_id());
                    }
                } else {
                    --page_num;
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

    private void getHeight(List<SearchItemDataBean> item_list, String state) {
        if (state.equals(REFRESH_STATUS)) {
            mLListDataHeight.clear();
            mSListDataHeight.clear();
        }

        if (item_list.size() > 0) {
            for (int i = 0; i < item_list.size(); i++) {
                mLListDataHeight.add(Math.round(width_Pic_List / 1.333333f));
                if (item_list.get(i).getItem_info().getImage().getRatio() == 0) {
                    mSListDataHeight.add(width_Pic_Staggered);
                } else {
                    mSListDataHeight.add(Math.round(width_Pic_Staggered / item_list.get(i).getItem_info().getImage().getRatio()));
                }
            }
        }
    }

    private void showPopwindow(int id, int position) {
        if (tagDataBean != null && colorBean != null) {
            if (null == homeTabPopWin) {
                homeTabPopWin = new HomeTabPopWin(activity, this, tagDataBean, this, colorBean, null);
            }
            if (homeTabPopWin.isShowing()) {
                if (last_id != 0 && last_id == id) {
                    last_id = 0;
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
                        iv_kongjian.setImageResource(R.drawable.kongjian);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_jubu:
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_zhuangshi:
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi);
                        iv_shouna.setImageResource(R.drawable.shouna1);
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_shouna:
                        iv_kongjian.setImageResource(R.drawable.kongjian1);
                        iv_jubu.setImageResource(R.drawable.jubu1);
                        iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                        iv_shouna.setImageResource(R.drawable.shouna);
                        iv_secai.setImageResource(R.drawable.secai1);
                        break;
                    case R.id.rl_secai:
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
            homeTabPopWin.dismiss();
            this.mColorClick = colorItemBean;

            if (mSelectListData.get(mColorClick.getColor_id()) == null) {
                mSelectListData.clear();
                mSelectListData.put(mColorClick.getColor_id(), mColorClick);
                homeTabPopWin.changeColor(mSelectListData);
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai1);

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
                onRefresh();
            } else {
                onClearColor();
            }

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
    public void onShouCang(boolean ifShouCang, int position, SearchItemDataBean searchItemDataBean) {

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
                addShouCang(position, searchItemDataBean.getItem_info().getItem_id());
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
                removeShouCang(position, searchItemDataBean.getItem_info().getItem_id());
            }

        }

    }

    //收藏
    private void addShouCang(final int position, String item_id) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(activity, "收藏失败");
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
                        mListData.get(position).getItem_info().setIs_collected("1");
                        try {
                            int collect_num = Integer.parseInt(mListData.get(position).getItem_info().getCollect_num().trim());
                            mListData.get(position).getItem_info().setCollect_num(++collect_num + "");
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
                        mListData.get(position).getItem_info().setIs_collected("0");
                        try {
                            int collect_num = Integer.parseInt(mListData.get(position).getItem_info().getCollect_num().trim());
                            mListData.get(position).getItem_info().setCollect_num(--collect_num + "");
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

    @Override
    public void qingkong() {
        selectColorPopupWindow.dismiss();
        onClearColor();
        //友盟统计
        HashMap<String, String> map4 = new HashMap<String, String>();
        map4.put("evenname", "色彩筛选清空");
        map4.put("even", "色彩页");
        MobclickAgent.onEvent(activity, "jtaction20", map4);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("色彩页")  //事件类别
                .setAction("色彩筛选清空")      //事件操作
                .build());
    }

    @Override
    public void clickColor(ColorItemBean colorItemBean) {
        //友盟统计
        HashMap<String, String> map4 = new HashMap<String, String>();
        map4.put("evenname", "色彩单选");
        map4.put("even", "色彩页" + colorItemBean.getColor_name());
        MobclickAgent.onEvent(activity, "jtaction21", map4);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("色彩页" + colorItemBean.getColor_name())  //事件类别
                .setAction("色彩单选")      //事件操作
                .build());
        if (selectColorPopupWindow != null) {
            selectColorPopupWindow.dismiss();
            this.mColorClick = colorItemBean;

            if (mSelectListData.get(mColorClick.getColor_id()) == null) {
                mSelectListData.clear();
                mSelectListData.put(mColorClick.getColor_id(), mColorClick);
                iv_kongjian.setImageResource(R.drawable.kongjian1);
                iv_jubu.setImageResource(R.drawable.jubu1);
                iv_zhuangshi.setImageResource(R.drawable.zhuangshi1);
                iv_shouna.setImageResource(R.drawable.shouna1);
                iv_secai.setImageResource(R.drawable.secai1);
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
                onRefresh();
            } else {
                qingkong();
            }

        }
    }


}
