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
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.myview.HomeTabPopWin;
import com.homechart.app.myview.NewHomeTabPopWin;
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
import com.homechart.app.visearch.SearchLoadingActivity;
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
    private RelativeLayout rl_shibie;
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

        cet_clearedit = (ClearEditText) rootView.findViewById(R.id.cet_clearedit);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_pic);

        iv_open_pop = (ImageView) rootView.findViewById(R.id.iv_open_pop);
        view_line_top = rootView.findViewById(R.id.view_line_top);
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
        iv_chongzhi.setOnClickListener(this);
        rl_shibie.setOnClickListener(this);
        iv_open_pop.setOnClickListener(this);

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
//                            if (mMoveY > mDownY) {
//                                rl_tos_choose.setVisibility(View.VISIBLE);
//                            } else {
//                                rl_tos_choose.setVisibility(View.GONE);
//                            }
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
            case R.id.iv_open_pop:
                showPopwindow();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {

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

                holder.getView(R.id.iv_shibie_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(activity, SearchLoadingActivity.class);
//                        Intent intent1 = new Intent(ShiBieActivity.this, TestActivity.class);
                        intent1.putExtra("image_url", mListData.get(position).getItem_info().getImage().getImg1());
                        intent1.putExtra("type", "lishi");
                        intent1.putExtra("image_id", mListData.get(position).getItem_info().getImage().getImage_id());
                        intent1.putExtra("image_type", "network");
                        intent1.putExtra("image_ratio", mListData.get(position).getItem_info().getImage().getRatio());
                        startActivity(intent1);
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

    private void showPopwindow() {

        if (tagDataBean != null) {
            if (null == newHomeTabPopWin) {
                newHomeTabPopWin = new NewHomeTabPopWin(activity, tagDataBean, this, colorBean, null);
            }
            if (newHomeTabPopWin.isShowing()) {
                iv_open_pop.setImageResource(R.drawable.shaixuan);
                newHomeTabPopWin.dismiss();
            } else {
                if (Build.VERSION.SDK_INT < 24) {
                    iv_open_pop.setImageResource(R.drawable.shaixuan1);
                    newHomeTabPopWin.showAsDropDown(view_line_top);
                } else {
                    iv_open_pop.setImageResource(R.drawable.shaixuan1);
                    // 获取控件的位置，安卓系统>7.0
                    int[] location = new int[2];
                    view_line_top.getLocationOnScreen(location);
                    int screenHeight = PublicUtils.getScreenHeight(activity);
                    newHomeTabPopWin.setHeight(screenHeight - location[1]);
                    newHomeTabPopWin.showAtLocation(view_line_top, Gravity.NO_GRAVITY, 0, location[1]);
                }
            }
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
