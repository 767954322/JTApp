package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.homechart.app.home.adapter.MyHuoDongJiangAdapter;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.hddetails.ActivityInfoBean;
import com.homechart.app.home.bean.hddetails.ActivityPrizeItemBean;
import com.homechart.app.home.bean.hddetails.HDDetailsBean;
import com.homechart.app.home.bean.hddetails.ItemUserBean;
import com.homechart.app.home.bean.huodong.HuoDongDataBean;
import com.homechart.app.home.bean.huodong.ItemActivityDataBean;
import com.homechart.app.home.bean.zhuanjihuodong.HuoDataBean;
import com.homechart.app.home.bean.zhuanjihuodong.ItemHuoDataBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.ui.activity.InspirationDetailActivity;
import com.homechart.app.lingganji.ui.activity.MyLingGanlistActivity;
import com.homechart.app.myview.HomeSharedPopWinPublic;
import com.homechart.app.myview.MyListView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.SelectPicPopupWindow;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
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
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by gumenghao on 17/6/29.
 */

public class NewHuoDongDetailsActivity
        extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener {

    private TextView tv_tital_comment;
    private ImageButton nav_left_imageButton;
    private HRecyclerView mRecyclerView;
    private LoadMoreFooterView mLoadMoreFooterView;
    private String activity_id;
    private int n = 20;
    private int page = 1;
    private ImageView iv_add_activity;
    private List<ItemHuoDataBean> mListData = new ArrayList<>();
    private MultiItemCommonAdapter<ItemHuoDataBean> mAdapter;
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
    private HDDetailsBean hdDetailsBean;
    private RelativeLayout rl_zhankai;
    private TextView tv_zhankai;
    private ImageView iv_zhankai;
    private boolean ifFirst = true;
    private boolean ifZhanKai = false;
    private ActivityInfoBean activityInfoBean;
    private ImageView iv_data_last_icon;
    private ImageView iv_data_last_icon1;
    private TextView tv_data_last1;
    private int widthPic;
    private String mUserId;
    private ImageView iv_huodong_start;
    private MyListView lv_jiangpin_list;
    private MyHuoDongJiangAdapter myHuoDongJiangAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_huodong_detail;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        activity_id = "5";
    }

    @Override
    protected void initView() {

        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
        headerView = LayoutInflater.from(NewHuoDongDetailsActivity.this).inflate(R.layout.header_new_huodong_details, null);

        iv_data_last_icon = (ImageView) headerView.findViewById(R.id.iv_data_last_icon);
        iv_data_last_icon1 = (ImageView) headerView.findViewById(R.id.iv_data_last_icon1);
        rl_zhankai = (RelativeLayout) headerView.findViewById(R.id.rl_zhankai);
        tv_zhankai = (TextView) headerView.findViewById(R.id.tv_zhankai);
        iv_zhankai = (ImageView) headerView.findViewById(R.id.iv_zhankai);

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
        iv_huodong_start = (ImageView) headerView.findViewById(R.id.iv_huodong_start);
        lv_jiangpin_list = (MyListView) headerView.findViewById(R.id.lv_jiangpin_list);

        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        iv_add_activity = (ImageView) findViewById(R.id.iv_add_activity);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_info);


    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        iv_add_activity.setOnClickListener(this);
        rl_zhankai.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        widthPic = (PublicUtils.getScreenWidth(this) - UIUtils.getDimens(R.dimen.font_30)) / 2;
        tv_tital_comment.setText("主题活动");
        width_activity = PublicUtils.getScreenWidth(NewHuoDongDetailsActivity.this) - UIUtils.getDimens(R.dimen.font_30);
        buildRecycler();
        getHuoDongData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                NewHuoDongDetailsActivity.this.finish();
                break;
            case R.id.iv_add_activity:

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

        MultiItemTypeSupport<ItemHuoDataBean> support = new MultiItemTypeSupport<ItemHuoDataBean>() {
            @Override
            public int getLayoutId(int itemType) {

                if (itemType == 0) {
                    return R.layout.item_left_huodong_zhuanji;
                } else {
                    return R.layout.item_right_huodong_zhuanji;
                }

            }

            @Override
            public int getItemViewType(int position, ItemHuoDataBean itemMessageBean) {

                if (position % 2 == 0) {
                    return 0;
                } else {
                    return 1;
                }
            }
        };

        mAdapter = new MultiItemCommonAdapter<ItemHuoDataBean>(this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {
                ((TextView) holder.getView(R.id.tv_item_name)).setText(mListData.get(position).getAlbum_info().getAlbum_name());
                ((TextView) holder.getView(R.id.tv_item_num_pic)).setText(mListData.get(position).getAlbum_info().getItem_num() + "张");
                ((TextView) holder.getView(R.id.tv_item_num_dingyue)).setText(mListData.get(position).getAlbum_info().getSubscribe_num() + "订阅");
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.iv_item_pic).getLayoutParams();
                layoutParams.height = widthPic;
                layoutParams.width = widthPic;
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                holder.getView(R.id.iv_item_pic).setLayoutParams(layoutParams);
                if (TextUtils.isEmpty(mListData.get(position).getAlbum_info().getCover_image().getImg0())) {
                    GlideImgManager.glideLoader(NewHuoDongDetailsActivity.this, "", R.drawable.moren, R.drawable.moren, (ImageView) holder.getView(R.id.iv_item_pic));
                } else {
                    ImageUtils.displayFilletHalfImage(mListData.get(position).getAlbum_info().getCover_image().getImg0(), (ImageView) holder.getView(R.id.iv_item_pic));
                }
                holder.getView(R.id.rl_item_inspiration).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NewHuoDongDetailsActivity.this, InspirationDetailActivity.class);
                        intent.putExtra("user_id", mUserId);
                        intent.putExtra("tag", "true");
                        intent.putExtra("album_id", mListData.get(position).getAlbum_info().getAlbum_id());
                        startActivityForResult(intent, 2);
                    }
                });
            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new GridLayoutManager(NewHuoDongDetailsActivity.this, 2));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.addHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);
        getListData();
    }

    //获取活动详情信息
    private void getHuoDongData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(NewHuoDongDetailsActivity.this, getString(R.string.info_get_error));
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
                        ToastUtils.showCenter(NewHuoDongDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(NewHuoDongDetailsActivity.this, getString(R.string.info_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getHuoDongDetails(activity_id, callBack);
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

            if (data < 0) {
                iv_data_last_icon.setVisibility(View.GONE);
                tv_data_last.setVisibility(View.GONE);
                iv_data_last_icon1.setVisibility(View.VISIBLE);
                tv_data_last1.setVisibility(View.VISIBLE);
                tv_data_last1.setText("已结束");
            } else {
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
            if (mTextViewPaint - (PublicUtils.getScreenWidth(NewHuoDongDetailsActivity.this) - UIUtils.getDimens(R.dimen.font_30)) * 4 > 0) {
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
            tv_show_num_people.setText(activityInfoBean.getJoin_user_num() + "人参与夺奖");
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

        ViewGroup.LayoutParams layoutParams1 = iv_huodong_start.getLayoutParams();
        layoutParams1.width = PublicUtils.getScreenWidth(NewHuoDongDetailsActivity.this);
        layoutParams1.height = (int) (PublicUtils.getScreenWidth(NewHuoDongDetailsActivity.this) / 0.5357);
        iv_huodong_start.setLayoutParams(layoutParams1);
        ImageUtils.disRectangleImageHuoDong("", iv_huodong_start);


        List<ActivityPrizeItemBean> listPrize = activityInfoBean.getPrize_info();
        myHuoDongJiangAdapter = new MyHuoDongJiangAdapter(listPrize, NewHuoDongDetailsActivity.this);
        lv_jiangpin_list.setAdapter(myHuoDongJiangAdapter);

    }

    //获取热图片／最新
    private void getListData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                mAdapter.notifyData(mListData);
                ToastUtils.showCenter(NewHuoDongDetailsActivity.this, getString(R.string.huodong_get_error));
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
                        HuoDataBean huoDataBean = GsonUtil.jsonToBean(strData, HuoDataBean.class);
                        List<ItemHuoDataBean> list = huoDataBean.getData().getAlbum_list();
                        if (list != null && list.size() > 0) {
                            updateViewFromData(list);
                        } else {
                            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                        }
                    } else {
                        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                    }
                } catch (JSONException e) {
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                    ToastUtils.showCenter(NewHuoDongDetailsActivity.this, getString(R.string.huodong_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getAddHuoDongList(activity_id, (page - 1) * n + "", n + "", callBack);

    }

    @Override
    public void onLoadMore() {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        getListData();
    }

    private void updateViewFromData(List<ItemHuoDataBean> item_list) {
        page++;
        mListData.addAll(item_list);
        mAdapter.notifyData(mListData);
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
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

}
