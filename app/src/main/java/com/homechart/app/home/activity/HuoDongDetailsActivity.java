package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.hddetails.ActivityInfoBean;
import com.homechart.app.home.bean.hddetails.HDDetailsBean;
import com.homechart.app.home.bean.hddetails.ItemUserBean;
import com.homechart.app.home.bean.huodong.ColorInfoBean;
import com.homechart.app.home.bean.huodong.HuoDongDataBean;
import com.homechart.app.home.bean.huodong.ItemActivityDataBean;
import com.homechart.app.home.bean.shouye.SYDataBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.HomeSharedPopWinPublic;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.SelectPicPopupWindow;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by gumenghao on 17/6/29.
 */

public class HuoDongDetailsActivity
        extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener,
        HomeSharedPopWinPublic.ClickInter {

    private TextView tv_tital_comment;
    private ImageButton nav_left_imageButton;
    private ImageButton nav_secondary_imageButton;
    private HRecyclerView mRecyclerView;
    private LoadMoreFooterView mLoadMoreFooterView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private List<Integer> mListDataHeight = new ArrayList<>();

    private String activity_id;
    private int n = 20;
    private int page = 1;
    private String sort = "hot";// hot：热度 new:最新
    private TextView tv_add_activity;
    private SelectPicPopupWindow menuWindow;
    private List<ItemActivityDataBean> mListData = new ArrayList<>();
    private MultiItemCommonAdapter<ItemActivityDataBean> mAdapter;
    private int width_Pic;
    private int position;
    private View headerView;
    private ImageView iv_huodong_image;
    private int width_activity;
    private TextView tv_tital_huodong;
    private TextView tv_data_last;
    private TextView tv_huodong_miaoshu;
    private TextView tv_no_people;
    private RelativeLayout rl_people_list;
    private RelativeLayout rl_five;
    private RelativeLayout rl_four;
    private RelativeLayout rl_three;
    private RelativeLayout rl_two;
    private RelativeLayout rl_one;
    private RoundImageView riv_five;
    private RoundImageView riv_four;
    private RoundImageView riv_three;
    private RoundImageView riv_two;
    private RoundImageView riv_one;
    private TextView tv_show_num_people;
    private TabLayout tl_tab;
    private HDDetailsBean hdDetailsBean;
    private int wid_imag;
    private HomeSharedPopWinPublic homeSharedPopWinPublic;
    private ImageView iv_shared;
    private TextView tv_content_right;
    private RelativeLayout rl_zhankai;
    private TextView tv_zhankai;
    private ImageView iv_zhankai;
    private boolean ifFirst = true;
    private boolean ifZhanKai = false;
    private ActivityInfoBean activityInfoBean;
    private ImageView iv_data_last_icon;
    private ImageView iv_data_last_icon1;
    private TextView tv_data_last1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_huodong_detail;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        activity_id = getIntent().getStringExtra("activity_id");
    }

    @Override
    protected void initView() {

        headerView = LayoutInflater.from(HuoDongDetailsActivity.this).inflate(R.layout.header_huodong_details, null);

        homeSharedPopWinPublic = new HomeSharedPopWinPublic(HuoDongDetailsActivity.this, HuoDongDetailsActivity.this);


        iv_data_last_icon = (ImageView) headerView.findViewById(R.id.iv_data_last_icon);
        iv_data_last_icon1 = (ImageView) headerView.findViewById(R.id.iv_data_last_icon1);
        rl_zhankai = (RelativeLayout) headerView.findViewById(R.id.rl_zhankai);
        tv_zhankai = (TextView) headerView.findViewById(R.id.tv_zhankai);
        iv_zhankai = (ImageView) headerView.findViewById(R.id.iv_zhankai);

        iv_shared = (ImageView) headerView.findViewById(R.id.iv_shared);
        iv_huodong_image = (ImageView) headerView.findViewById(R.id.iv_huodong_image);
        tv_tital_huodong = (TextView) headerView.findViewById(R.id.tv_tital_huodong);
        tv_data_last = (TextView) headerView.findViewById(R.id.tv_data_last);
        tv_data_last1 = (TextView) headerView.findViewById(R.id.tv_data_last1);
        tv_huodong_miaoshu = (TextView) headerView.findViewById(R.id.tv_huodong_miaoshu);
        tv_no_people = (TextView) headerView.findViewById(R.id.tv_no_people);
        rl_people_list = (RelativeLayout) headerView.findViewById(R.id.rl_people_list);
        rl_five = (RelativeLayout) headerView.findViewById(R.id.rl_five);
        rl_four = (RelativeLayout) headerView.findViewById(R.id.rl_four);
        rl_three = (RelativeLayout) headerView.findViewById(R.id.rl_three);
        rl_two = (RelativeLayout) headerView.findViewById(R.id.rl_two);
        rl_one = (RelativeLayout) headerView.findViewById(R.id.rl_one);
        riv_five = (RoundImageView) headerView.findViewById(R.id.riv_five);
        riv_four = (RoundImageView) headerView.findViewById(R.id.riv_four);
        riv_three = (RoundImageView) headerView.findViewById(R.id.riv_three);
        riv_two = (RoundImageView) headerView.findViewById(R.id.riv_two);
        riv_one = (RoundImageView) headerView.findViewById(R.id.riv_one);
        tv_show_num_people = (TextView) headerView.findViewById(R.id.tv_show_num_people);
        tl_tab = (TabLayout) headerView.findViewById(R.id.tl_tab);

        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_add_activity = (TextView) findViewById(R.id.tv_add_activity);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        nav_secondary_imageButton = (ImageButton) findViewById(R.id.nav_secondary_imageButton);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_info);


    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);
        iv_shared.setOnClickListener(this);
        tv_add_activity.setOnClickListener(this);
        rl_zhankai.setOnClickListener(this);
        tl_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                page = 1;
                mListData.clear();
                mListDataHeight.clear();
                if (tab.getText().equals("最新")) {
                    sort = "new";
                } else if (tab.getText().equals("最热")) {
                    sort = "hot";
                }
                getListData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("主题活动");
        tv_content_right.setText("往期活动");
        width_activity = PublicUtils.getScreenWidth(HuoDongDetailsActivity.this) - UIUtils.getDimens(R.dimen.font_30);
        width_Pic = PublicUtils.getScreenWidth(HuoDongDetailsActivity.this) / 2 - UIUtils.getDimens(R.dimen.font_14);
        menuWindow = new SelectPicPopupWindow(HuoDongDetailsActivity.this, HuoDongDetailsActivity.this);
        buildRecycler();
        getHuoDongData();
        tl_tab.setTabMode(TabLayout.MODE_FIXED);
        tl_tab.setSelectedTabIndicatorHeight(UIUtils.getDimens(R.dimen.font_3));
        tl_tab.setSelectedTabIndicatorColor(UIUtils.getColor(R.color.bg_e79056));
        tl_tab.addTab(tl_tab.newTab().setText("最热"));
        tl_tab.addTab(tl_tab.newTab().setText("最新"));
        PublicUtils.setIndicator(tl_tab, UIUtils.getDimens(R.dimen.font_20), UIUtils.getDimens(R.dimen.font_20));
    }

    private void fabu() {
        //友盟统计
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("evenname", "发布入口");
        map.put("even", "活动页");
        MobclickAgent.onEvent(HuoDongDetailsActivity.this, "jtaction1", map);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("活动页")  //事件类别
                .setAction("发布入口")      //事件操作
                .build());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                HuoDongDetailsActivity.this.finish();
                break;
            case R.id.tv_add_activity:
                fabu();
                //参与
                menuWindow.showAtLocation(HuoDongDetailsActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                        0,
                        0); //设置layout在PopupWindow中显示的位置
                break;
            case R.id.tv_takephoto:
                menuWindow.dismiss();
                GalleryFinal.openCamera(0, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList != null && resultList.size() > 0) {

                            Message message = new Message();
                            message.obj = resultList.get(0).getPhotoPath().toString();
                            handler.sendMessage(message);
                        } else {
                            ToastUtils.showCenter(HuoDongDetailsActivity.this, "拍照资源获取失败");
                        }
                    }
                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {

                    }
                });
                break;
            case R.id.tv_pic:
                menuWindow.dismiss();
                GalleryFinal.openGallerySingle(0, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList != null && resultList.size() > 0) {
                            Message message = new Message();
                            message.obj = resultList.get(0).getPhotoPath().toString();
                            handler.sendMessage(message);
                        } else {
                            ToastUtils.showCenter(HuoDongDetailsActivity.this, "图片资源获取失败");
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                    }
                });
                break;
            case R.id.rl_pop_main:
                menuWindow.dismiss();
                break;
            case R.id.iv_bufabu:
                menuWindow.dismiss();
                break;
            case R.id.iv_shared:

                if (hdDetailsBean != null) {
                    homeSharedPopWinPublic.showAtLocation(HuoDongDetailsActivity.this.findViewById(R.id.main),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                            0,
                            0); //设置layout在PopupWindow中显示的位置

                }

                break;
            case R.id.tv_content_right:
                Intent intent = new Intent(this, HuoDongListActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_zhankai:

                if (ifZhanKai) {//展开了，点击关闭

                    tv_huodong_miaoshu.setMaxLines(4);
                    if (activityInfoBean != null) {
                        tv_zhankai.setText("展开");
                        iv_zhankai.setImageResource(R.drawable.zhankai1);
                        tv_huodong_miaoshu.setText(activityInfoBean.getDescription());
                    }
                    ifZhanKai = false;

                } else {//关开了，点击展开

                    tv_huodong_miaoshu.setMaxLines(1000);

                    if (activityInfoBean != null) {
                        tv_zhankai.setText("收起");
                        iv_zhankai.setImageResource(R.drawable.shouqi1);
                        tv_huodong_miaoshu.setText(activityInfoBean.getDescription());
                    }
                    ifZhanKai = true;
                }

                break;
        }

    }

    private void buildRecycler() {

        MultiItemTypeSupport<ItemActivityDataBean> support = new MultiItemTypeSupport<ItemActivityDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == 0) {
                    return R.layout.item_huodong_image;
                } else {
                    return R.layout.item_huodong_image;
                }
            }

            @Override
            public int getItemViewType(int position, ItemActivityDataBean itemMessageBean) {
                return 0;

            }
        };

        mAdapter = new MultiItemCommonAdapter<ItemActivityDataBean>(this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {
                if(mListData.get(position).getItem_info().getCollect_num().trim().equals("0")){
                    holder.getView(R.id.tv_shoucang_num).setVisibility(View.INVISIBLE);
                }else {
                    holder.getView(R.id.tv_shoucang_num).setVisibility(View.VISIBLE);
                }
                ((TextView) holder.getView(R.id.tv_shoucang_num)).setText(mListData.get(position).getItem_info().getCollect_num());

                if (!mListData.get(position).getItem_info().getIs_collected().equals("1")) {//未收藏
                    ((ImageView) holder.getView(R.id.iv_if_shoucang)).setImageResource(R.drawable.shoucang);
                } else {//收藏
                    ((ImageView) holder.getView(R.id.iv_if_shoucang)).setImageResource(R.drawable.shoucang1);
                }
                holder.getView(R.id.tv_shoucang_num).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onShouCang(!mListData.get(position).getItem_info().getIs_collected().equals("1"), position, mListData.get(position));
                    }
                });
                holder.getView(R.id.iv_if_shoucang).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onShouCang(!mListData.get(position).getItem_info().getIs_collected().equals("1"), position, mListData.get(position));
                    }
                });

                ItemActivityDataBean itemData = mListData.get(position);
                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                layoutParams.width = width_Pic;
                layoutParams.height = mListDataHeight.get(position);
                holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);

                ((TextView) holder.getView(R.id.tv_name_pic)).setText(itemData.getUser_info().getNickname());
                ImageUtils.displayFilletImage(itemData.getItem_info().getImage().getImg1(),
                        (ImageView) holder.getView(R.id.iv_imageview_one));
                ImageUtils.displayFilletImage(mListData.get(position).getUser_info().getAvatar().getThumb(),
                        (ImageView) holder.getView(R.id.iv_header_pic));
                holder.getView(R.id.iv_imageview_one).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //查看单图详情
                        Intent intent = new Intent(HuoDongDetailsActivity.this, ImageDetailLongActivity.class);
                        intent.putExtra("item_id", mListData.get(position).getItem_info().getItem_id());
                        startActivity(intent);
                    }
                });
                holder.getView(R.id.iv_header_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HuoDongDetailsActivity.this, UserInfoActivity.class);
                        intent.putExtra(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                        startActivity(intent);
                    }
                });
                List<ColorInfoBean> list_color = itemData.getColor_info();
                if (null != list_color && list_color.size() == 1) {
                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                } else if (null != list_color && list_color.size() == 2) {

                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(1).getColor_value()));
                    holder.getView(R.id.iv_color_center).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                } else if (null != list_color && list_color.size() == 3) {
                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(2).getColor_value()));
                    holder.getView(R.id.iv_color_center).setBackgroundColor(Color.parseColor("#" + list_color.get(1).getColor_value()));
                    holder.getView(R.id.iv_color_left).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                } else {
                    holder.getView(R.id.iv_color_right).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.GONE);
                }
                holder.getView(R.id.iv_color_right).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickColorQiu(mListData.get(position).getItem_info().getItem_id());
                    }
                });
                holder.getView(R.id.iv_color_left).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        clickColorQiu(mListData.get(position).getItem_info().getItem_id());
                    }
                });
                holder.getView(R.id.iv_color_center).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        clickColorQiu(mListData.get(position).getItem_info().getItem_id());
                    }
                });
            }
        };

        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.addHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);
        getListData();
    }

    public void clickColorQiu(String item_id) {
        //友盟统计
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("evenname", "三个色彩点");
        map.put("even", "活动页");
        MobclickAgent.onEvent(HuoDongDetailsActivity.this, "jtaction3", map);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("活动页")  //事件类别
                .setAction("三个色彩点")      //事件操作
                .build());
        Intent intent = new Intent(HuoDongDetailsActivity.this, ImageDetailLongActivity.class);
        intent.putExtra("item_id", item_id);
        intent.putExtra("if_click_color", true);
        startActivity(intent);
    }

    //获取活动详情信息
    private void getHuoDongData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(HuoDongDetailsActivity.this, getString(R.string.info_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        String strData = "{\"data\":" + data_msg + "}";
                        hdDetailsBean = GsonUtil.jsonToBean(strData, HDDetailsBean.class);
                        changeTopUI(hdDetailsBean);
                    } else {
                        ToastUtils.showCenter(HuoDongDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(HuoDongDetailsActivity.this, getString(R.string.info_get_error));
                }
            }
        };
        MyHttpManager.getInstance().activityDetails(activity_id, callBack);
    }

    //获得活动详情，更新ui
    private void changeTopUI(HDDetailsBean hdDetailsBean) {

        activityInfoBean = hdDetailsBean.getData().getActivity_info();
        ViewGroup.LayoutParams layoutParams = iv_huodong_image.getLayoutParams();
        layoutParams.width = width_activity;
        layoutParams.height = (int) (width_activity / 2.36);
        iv_huodong_image.setLayoutParams(layoutParams);

        ImageUtils.displayFilletImage(activityInfoBean.getImage().getImg0(), iv_huodong_image);
        tv_tital_huodong.setText(activityInfoBean.getTitle());

        if (activityInfoBean.getState_id().equals("3")) {
            //计算时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            long data = PublicUtils.diffDay(activityInfoBean.getEnd_time(), str, "yyyy-MM-dd HH:mm:ss");

            if(data < 0 ){
                iv_data_last_icon.setVisibility(View.GONE);
                tv_data_last.setVisibility(View.GONE);
                iv_data_last_icon1.setVisibility(View.VISIBLE);
                tv_data_last1.setVisibility(View.VISIBLE);
                tv_data_last1.setText("已结束");
            }else {
                iv_data_last_icon.setVisibility(View.VISIBLE);
                tv_data_last.setVisibility(View.VISIBLE);
                iv_data_last_icon1.setVisibility(View.GONE);
                tv_data_last1.setVisibility(View.GONE);
                tv_data_last.setText("还剩" + Math.abs(data) + "天");
            }

        } else if (activityInfoBean.getState_id().equals("2")) {
            iv_data_last_icon.setVisibility(View.VISIBLE);
            tv_data_last.setVisibility(View.VISIBLE);
            iv_data_last_icon1.setVisibility(View.GONE);
            tv_data_last1.setVisibility(View.GONE);
            tv_data_last.setText("敬请期待");
        } else if (activityInfoBean.getState_id().equals("1")) {
            iv_data_last_icon.setVisibility(View.GONE);
            tv_data_last.setVisibility(View.GONE);
            iv_data_last_icon1.setVisibility(View.VISIBLE);
            tv_data_last1.setVisibility(View.VISIBLE);
            tv_data_last1.setText("已结束");
        }
        TextPaint textPaint = tv_huodong_miaoshu.getPaint();
        float mTextViewPaint = textPaint.measureText(activityInfoBean.getDescription());

        if (ifFirst) {
            if (mTextViewPaint - (PublicUtils.getScreenWidth(HuoDongDetailsActivity.this) - UIUtils.getDimens(R.dimen.font_30)) * 4 > 0) {
                //超过四行
                rl_zhankai.setVisibility(View.VISIBLE);
                tv_huodong_miaoshu.setMaxLines(4);
                tv_huodong_miaoshu.setText(activityInfoBean.getDescription());
            } else {
                //未超过四行
                rl_zhankai.setVisibility(View.GONE);
                tv_huodong_miaoshu.setMaxLines(4);
                tv_huodong_miaoshu.setText(activityInfoBean.getDescription());
            }
            ifFirst = false;
        }

        List<ItemUserBean> list_user = hdDetailsBean.getData().getUser_list();
        if (list_user == null || list_user.size() == 0) {
            rl_people_list.setVisibility(View.GONE);
            tv_no_people.setVisibility(View.VISIBLE);
        } else {
            tv_show_num_people.setText(activityInfoBean.getJoin_user_num() + "人参与晒图");
            rl_people_list.setVisibility(View.VISIBLE);
            tv_no_people.setVisibility(View.GONE);
            if (list_user.size() == 1) {
                rl_one.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(0).getAvatar().getThumb(), riv_one);
                rl_two.setVisibility(View.GONE);
                rl_three.setVisibility(View.GONE);
                rl_four.setVisibility(View.GONE);
                rl_five.setVisibility(View.GONE);
            } else if (list_user.size() == 2) {
                rl_one.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(0).getAvatar().getThumb(), riv_one);
                rl_two.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(1).getAvatar().getThumb(), riv_two);
                rl_three.setVisibility(View.GONE);
                rl_four.setVisibility(View.GONE);
                rl_five.setVisibility(View.GONE);

            } else if (list_user.size() == 3) {
                rl_one.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(0).getAvatar().getThumb(), riv_one);
                rl_two.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(1).getAvatar().getThumb(), riv_two);
                rl_three.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(2).getAvatar().getThumb(), riv_three);
                rl_four.setVisibility(View.GONE);
                rl_five.setVisibility(View.GONE);

            } else if (list_user.size() == 4) {
                rl_one.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(0).getAvatar().getThumb(), riv_one);
                rl_two.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(1).getAvatar().getThumb(), riv_two);
                rl_three.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(2).getAvatar().getThumb(), riv_three);
                rl_four.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(3).getAvatar().getThumb(), riv_four);
                rl_five.setVisibility(View.GONE);
            } else if (list_user.size() >= 5) {
                rl_one.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(0).getAvatar().getThumb(), riv_one);
                rl_two.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(1).getAvatar().getThumb(), riv_two);
                rl_three.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(2).getAvatar().getThumb(), riv_three);
                rl_four.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(3).getAvatar().getThumb(), riv_four);
                rl_five.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(list_user.get(4).getAvatar().getThumb(), riv_five);
            }
        }

    }

    //获取热图片／最新
    private void getListData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                mAdapter.notifyData(mListData);
                ToastUtils.showCenter(HuoDongDetailsActivity.this, getString(R.string.huodong_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        String strData = "{\"data\":" + data_msg + "}";
                        HuoDongDataBean huoDongDataBean = GsonUtil.jsonToBean(strData, HuoDongDataBean.class);
                        List<ItemActivityDataBean> list = huoDongDataBean.getData().getItem_list();
                        if (list != null && list.size() > 0) {
                            getHeight(list);
                            updateViewFromData(list);
                        } else {
                            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
//                            ToastUtils.showCenter(HuoDongDetailsActivity.this, getString(R.string.info_get_no));
                        }
                    } else {
                        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                        ToastUtils.showCenter(HuoDongDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                    ToastUtils.showCenter(HuoDongDetailsActivity.this, getString(R.string.huodong_get_error));
                }
            }
        };
        MyHttpManager.getInstance().huoDongImageList(activity_id, (page - 1) * n + "", n + "", sort, callBack);

    }

    @Override
    public void onLoadMore() {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        getListData();
    }

    private void updateViewFromData(List<ItemActivityDataBean> item_list) {
        page++;
        position = mListData.size();
        mListData.addAll(item_list);
        mAdapter.notifyData(mListData);
//        mAdapter.notifyItem(position, mListData, item_list);
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
    }

    private void getHeight(List<ItemActivityDataBean> item_list) {

        if (item_list.size() > 0) {
            for (int i = 0; i < item_list.size(); i++) {
                mListDataHeight.add(Math.round(width_Pic / item_list.get(i).getItem_info().getImage().getRatio()));
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String url_Imag = (String) msg.obj;
            Intent intent = new Intent(HuoDongDetailsActivity.this, FaBuActvity.class);
            intent.putExtra("image_path", url_Imag);
            intent.putExtra("activity_id", activity_id);
            intent.putExtra("type", "huodong");
            startActivity(intent);

        }
    };

    private void sharedItemOpen(SHARE_MEDIA share_media) {
        UMImage image = new UMImage(HuoDongDetailsActivity.this, hdDetailsBean.getData().getActivity_info().getImage().getImg0());
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        UMWeb web = new UMWeb("http://h5.idcool.com.cn/activity/" + hdDetailsBean.getData().getActivity_info().getActivity_id());
        web.setTitle(hdDetailsBean.getData().getActivity_info().getTitle() + "｜家图APP");//标题
        web.setThumb(image);  //缩略图
        String desi = hdDetailsBean.getData().getActivity_info().getDescription();
        if (desi.length() > 160) {
            desi = desi.substring(0, 160) + "...";
        }
        web.setDescription(desi);//描述
        new ShareAction(HuoDongDetailsActivity.this).
                setPlatform(share_media).
                withMedia(web).
                setCallback(umShareListener).share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            addShared();
            ToastUtils.showCenter(HuoDongDetailsActivity.this, "分享成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.showCenter(HuoDongDetailsActivity.this, "分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showCenter(HuoDongDetailsActivity.this, "分享取消了");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void addShared() {
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
                        getHuoDongData();
                    } else {
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().addShared(hdDetailsBean.getData().getActivity_info().getActivity_id(), "activity", callBack);
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("活动页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("活动页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("活动页");
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClickWeiXin() {
        sharedItemOpen(SHARE_MEDIA.WEIXIN);
    }

    @Override
    public void onClickPYQ() {
        sharedItemOpen(SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    @Override
    public void onClickWeiBo() {
        sharedItemOpen(SHARE_MEDIA.SINA);
    }

    @Override
    public void onClickQQ() {

        sharedItemOpen(SHARE_MEDIA.QQ);
    }

    boolean ifClickShouCang = true;

    //收藏或者取消收藏，图片
    public void onShouCang(boolean ifShouCang, int position, ItemActivityDataBean itemActivityDataBean) {

        if(ifClickShouCang){
            ifClickShouCang = false;
            if (ifShouCang) {
                //未被收藏，去收藏
                addShouCang(position, itemActivityDataBean.getItem_info().getItem_id());
            } else {
                //被收藏，去取消收藏
                removeShouCang(position, itemActivityDataBean.getItem_info().getItem_id());
            }
        }
    }

    //收藏
    private void addShouCang(final int position, String item_id) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ifClickShouCang = true;
                ToastUtils.showCenter(HuoDongDetailsActivity.this, "收藏成功");
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
                        ToastUtils.showCenter(HuoDongDetailsActivity.this, "收藏成功");
                        mListData.get(position).getItem_info().setIs_collected("1");
                        try {
                            int collect_num = Integer.parseInt(mListData.get(position).getItem_info().getCollect_num().trim());
                            mListData.get(position).getItem_info().setCollect_num(++collect_num + "");
                        } catch (Exception e) {
                        }
                        mAdapter.notifyItemChanged(position);
                    } else {
                        ToastUtils.showCenter(HuoDongDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(HuoDongDetailsActivity.this, "收藏失败");
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
                ifClickShouCang = true;
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(HuoDongDetailsActivity.this, "取消收藏失败");
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
                        ToastUtils.showCenter(HuoDongDetailsActivity.this, "取消收藏成功");
                        mListData.get(position).getItem_info().setIs_collected("0");
                        try {
                            int collect_num = Integer.parseInt(mListData.get(position).getItem_info().getCollect_num().trim());
                            mListData.get(position).getItem_info().setCollect_num(--collect_num + "");
                        } catch (Exception e) {
                        }
                        mAdapter.notifyItemChanged(position);
                    } else {
                        ToastUtils.showCenter(HuoDongDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(HuoDongDetailsActivity.this, "取消收藏失败");
                }
            }
        };
        MyHttpManager.getInstance().removeShouCang(item_id, callBack);
    }
}
