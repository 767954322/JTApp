package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.homechart.app.home.activity.ImageDetailScrollActivity;
import com.homechart.app.home.activity.ImageEditActvity;
import com.homechart.app.home.activity.LoginActivity;
import com.homechart.app.home.activity.ShaiXuanResultActicity;
import com.homechart.app.home.activity.UserInfoActivity;
import com.homechart.app.home.adapter.MyColorGridAdapter;
import com.homechart.app.home.base.LazyLoadFragment;
import com.homechart.app.home.bean.cailike.ImageLikeItemBean;
import com.homechart.app.home.bean.cailike.LikeDataBean;
import com.homechart.app.home.bean.imagedetail.ColorInfoBean;
import com.homechart.app.home.bean.imagedetail.ImageDetailBean;
import com.homechart.app.home.bean.searchfservice.SearchSBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.imagedetail.ImageDetailsActivity;
import com.homechart.app.lingganji.ui.activity.InspirationSeriesActivity;
import com.homechart.app.myview.FlowLayoutBiaoQian;
import com.homechart.app.myview.HomeSharedPopWinPublic;
import com.homechart.app.myview.MyListView;
import com.homechart.app.myview.ResizeRelativeLayout;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.ShangshabanChangeTextSpaceView;
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
import com.homechart.app.visearch.NewSearchResultActivity;
import com.homechart.app.visearch.SearchLoadingActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gumenghao on 17/8/8.
 */

public class ImageDetaiScrollFragment
        extends LazyLoadFragment implements
        HomeSharedPopWinPublic.ClickInter,
        View.OnClickListener,
        OnLoadMoreListener {

    public ImageDetaiScrollFragment() {

    }

    @SuppressLint("ValidFragment")
    public ImageDetaiScrollFragment(String item_id, UserInfo userInfo) {
        this.item_id = item_id;
        this.mUserInfo = userInfo;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_image_details;
    }

    @Override
    protected void lazyLoad() {
        if (firstLoad) {
            initExtraBundle();
            initView();
            initListener();
            initData();
            firstLoad = false;
        }
    }

    private void initExtraBundle() {
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
    }

    private void initView() {
        imageScrollActivity = (ImageDetailScrollActivity) activity;
        rl_navbar = (RelativeLayout) activity.findViewById(R.id.rl_navbar);
        rl_navbar_tital = (RelativeLayout) activity.findViewById(R.id.rl_navbar_tital);
        tv_lingganji = (TextView) activity.findViewById(R.id.tv_lingganji);
        iv_edit_image = (ImageButton) activity.findViewById(R.id.iv_edit_image);
        iv_shared_image = (ImageButton) activity.findViewById(R.id.iv_shared_image);
        iv_more_image = (ImageButton) activity.findViewById(R.id.iv_more_image);
        tv_tital_comment = (TextView) activity.findViewById(R.id.tv_tital_comment);

        homeSharedPopWinPublic = new HomeSharedPopWinPublic(activity, this);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_info);
        menu_layout = (ResizeRelativeLayout) rootView.findViewById(R.id.menu_layout);
        bt_shiwu = (ImageView) rootView.findViewById(R.id.bt_shiwu);
        bt_shise = (ImageView) rootView.findViewById(R.id.bt_shise);

        rl_color = (RelativeLayout) rootView.findViewById(R.id.rl_color);
        iv_close_color = (ImageView) rootView.findViewById(R.id.iv_close_color);
        dgv_colorlist = (MyListView) rootView.findViewById(R.id.dgv_colorlist);
        ll_color_lines = (LinearLayout) rootView.findViewById(R.id.ll_color_lines);
        color_bottom = rootView.findViewById(R.id.color_bottom);

        if (!ifHasHeader) {
            view = LayoutInflater.from(activity).inflate(R.layout.header_fragment_scroll_imagedetails, null);
            iv_details_image = (ImageView) view.findViewById(R.id.iv_details_image);
            view_below_image = view.findViewById(R.id.view_below_image);
            bt_shiwu2 = (ImageView) view.findViewById(R.id.bt_shiwu2);
            bt_shise2 = (ImageView) view.findViewById(R.id.bt_shise2);
            tv_details_tital = (ShangshabanChangeTextSpaceView) view.findViewById(R.id.tv_details_tital);
            fl_tags_jubu = (FlowLayoutBiaoQian) view.findViewById(R.id.fl_tags_jubu);
            riv_people_header = (RoundImageView) view.findViewById(R.id.riv_people_header);
            tv_people_name = (TextView) view.findViewById(R.id.tv_people_name);
            iv_people_tag = (ImageView) view.findViewById(R.id.iv_people_tag);
            tv_details_time = (TextView) view.findViewById(R.id.tv_details_time);
            tv_maybe_like = (TextView) view.findViewById(R.id.tv_maybe_like);
        }
    }

    private void initListener() {
        bt_shiwu.setOnClickListener(this);
        bt_shiwu2.setOnClickListener(this);
        bt_shise.setOnClickListener(this);
        bt_shise2.setOnClickListener(this);
        tv_lingganji.setOnClickListener(this);
        iv_close_color.setOnClickListener(this);
        iv_details_image.setOnClickListener(this);
        iv_edit_image.setOnClickListener(this);
        riv_people_header.setOnClickListener(this);
        iv_shared_image.setOnClickListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalDy -= dy;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_lingganji:
                loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
                if (!loginStatus) {
                    //友盟统计
                    HashMap<String, String> map4 = new HashMap<String, String>();
                    map4.put("evenname", "登录入口");
                    map4.put("even", "图片详情页进行图片收藏");
                    MobclickAgent.onEvent(imageScrollActivity, "shijian20", map4);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("图片详情页进行图片收藏")  //事件类别
                            .setAction("登录入口")      //事件操作
                            .build());
                    Intent intent = new Intent(imageScrollActivity, LoginActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    if (null != imageDetailBean) {
                        //友盟统计
                        HashMap<String, String> map4 = new HashMap<String, String>();
                        map4.put("evenname", "加图");
                        map4.put("even", "图片详情");
                        MobclickAgent.onEvent(activity, "shijian23", map4);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("图片详情")  //事件类别
                                .setAction("加图")      //事件操作
                                .build());

                        Intent intent = new Intent(activity, InspirationSeriesActivity.class);
                        intent.putExtra("userid", mUserId);
                        intent.putExtra("image_url", imageDetailBean.getItem_info().getImage().getImg0());
                        intent.putExtra("item_id", imageDetailBean.getItem_info().getItem_id());
                        startActivity(intent);
                    }
                }
                break;
            case R.id.iv_edit_image:
                if (imageDetailBean != null) {
                    Intent intent = new Intent(activity, ImageEditActvity.class);
                    intent.putExtra("image_value", imageDetailBean);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.iv_details_image:
                if (null != imageDetailBean) {
                    List<String> listUrl = new ArrayList<>();
                    listUrl.add(imageDetailBean.getItem_info().getImage().getImg0());
                    Intent intent6 = new Intent(activity, ImageDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("pic_url_list", (Serializable) listUrl);
                    bundle.putInt("click_position", 0);
                    bundle.putInt("ifhinttital", 2);
                    intent6.putExtras(bundle);
                    startActivity(intent6);
                }
                break;
            case R.id.bt_shiwu:
            case R.id.bt_shiwu2:
                if (PublicUtils.ifHasWriteQuan(activity)) {
                    //有权限
                    if (null != imageDetailBean && null != searchSBean) {
                        //友盟统计
                        HashMap<String, String> map6 = new HashMap<String, String>();
                        map6.put("evenname", "识图入口");
                        map6.put("even", "图片详情－图片详情物体识别");
                        MobclickAgent.onEvent(imageScrollActivity, "shijian6", map6);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("图片详情－图片详情物体识别")  //事件类别
                                .setAction("识图入口")      //事件操作
                                .build());
                        Intent intent4 = new Intent(activity, NewSearchResultActivity.class);
                        intent4.putExtra("image_id", imageDetailBean.getItem_info().getImage().getImage_id());
                        intent4.putExtra("imagePath", imageDetailBean.getItem_info().getImage().getImg0());
                        intent4.putExtra("searchstatus", "0");
                        intent4.putExtra("network", "true");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("searchSBean", searchSBean);
                        intent4.putExtras(bundle);
                        startActivity(intent4);
                    } else {
                        if (null != imageDetailBean) {
                            getSearchPosition(imageDetailBean.getItem_info().getImage().getImage_id());
                        }
                        ToastUtils.showCenter(activity, "正在识别中");
                    }
                } else {
                    //无权限
                    ToastUtils.showCenter(activity, "您没有授权该权限，请在设置中打开授权");
                }
                break;
            case R.id.bt_shise:
            case R.id.bt_shise2:
                //友盟统计
                HashMap<String, String> map6 = new HashMap<String, String>();
                map6.put("evenname", "色彩详情");
                map6.put("even", "用户点开色彩详情的次数");
                MobclickAgent.onEvent(imageScrollActivity, "shijian9", map6);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("用户点开色彩详情的次数")  //事件类别
                        .setAction("色彩详情")      //事件操作
                        .build());
                bt_shiwu.setClickable(false);
                bt_shiwu2.setClickable(false);
                bt_shise.setClickable(false);
                bt_shise2.setClickable(false);
                ((ImageDetailScrollActivity) activity).setTitalClickAble(false);
                ((ImageDetailScrollActivity) activity).setViewPagerScrollAble(false);
                rl_color.setVisibility(View.VISIBLE);
                color_bottom.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_close_color:
                bt_shiwu2.setClickable(true);
                bt_shiwu.setClickable(true);
                bt_shise2.setClickable(true);
                bt_shise.setClickable(true);
                ((ImageDetailScrollActivity) activity).setTitalClickAble(true);
                ((ImageDetailScrollActivity) activity).setViewPagerScrollAble(true);
                rl_color.setVisibility(View.GONE);
                color_bottom.setVisibility(View.GONE);
                break;
            case R.id.riv_people_header:
                if (imageDetailBean != null) {
                    Intent intent_info = new Intent(activity, UserInfoActivity.class);
                    intent_info.putExtra(ClassConstant.LoginSucces.USER_ID, imageDetailBean.getUser_info().getUser_id());
                    startActivityForResult(intent_info, 3);
                }
                break;
            case R.id.iv_shared_image:
                if (imageDetailBean != null) {
                    homeSharedPopWinPublic.showAtLocation(activity.findViewById(R.id.menu_layout),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                            0,
                            0); //设置layout在PopupWindow中显示的位置
                }
                break;
        }
    }

    private void initData() {

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        width_Pic = PublicUtils.getScreenWidth(activity) / 2 - UIUtils.getDimens(R.dimen.font_14);
        rect = new Rect(0, 0, PublicUtils.getScreenWidth(activity), PublicUtils.getScreenHeight(activity));
        getImageDetail();
        buildRecyclerView();
        mRecyclerView.scrollTo(0, 0);
        mHandler.postDelayed(runnable, 100);
    }

    private void buildRecyclerView() {

        MultiItemTypeSupport<ImageLikeItemBean> support = new MultiItemTypeSupport<ImageLikeItemBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_ONE) {
                    return R.layout.item_image_details;
                } else {
                    return R.layout.item_image_details;
                }
            }

            @Override
            public int getItemViewType(int position, ImageLikeItemBean s) {
                return TYPE_ONE;
            }
        };
        mAdapter = new MultiItemCommonAdapter<ImageLikeItemBean>(activity, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {
                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                layoutParams.height = mListData.get(position).getItem_info().getImage().getRatio() == 0
                        ? width_Pic
                        : Math.round(width_Pic / mListData.get(position).getItem_info().getImage().getRatio());
                holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);

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
                ((TextView) holder.getView(R.id.tv_image_miaosu)).setText(Html.fromHtml(str));

                if (PublicUtils.ifHasWriteQuan(activity)) {
                    //有权限
                    if (mListData.get(position).getItem_info().getImage().getRatio() > 0.5) {
                        ImageUtils.displayFilletHalfImage(mListData.get(position).getItem_info().getImage().getImg1(),
                                (ImageView) holder.getView(R.id.iv_imageview_one));
                    } else {
                        GlideImgManager.glideLoader(activity, mListData.get(position).getItem_info().getImage().getImg1(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_imageview_one), 1);
                    }
                    ImageUtils.displayFilletImage(mListData.get(position).getUser_info().getAvatar().getBig(),
                            (ImageView) holder.getView(R.id.iv_header_pic));
                } else {
                    GlideImgManager.glideLoader(activity, mListData.get(position).getItem_info().getImage().getImg1(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_imageview_one), 1);
                    GlideImgManager.glideLoader(activity, mListData.get(position).getUser_info().getAvatar().getBig(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_header_pic), 0);
                }

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
                        intent.putExtra("page_num", page);
                        intent.putExtra("item_id_list", (Serializable) mItemIdList);
                        startActivity(intent);
                    }
                });

                holder.getView(R.id.iv_if_shoucang).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
                        if (!loginStatus) {
                            //友盟统计
                            HashMap<String, String> map4 = new HashMap<String, String>();
                            map4.put("evenname", "登录入口");
                            map4.put("even", "猜你喜欢列表进行图片收藏");
                            MobclickAgent.onEvent(imageScrollActivity, "shijian20", map4);
                            //ga统计
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory("猜你喜欢列表进行图片收藏")  //事件类别
                                    .setAction("登录入口")      //事件操作
                                    .build());
                            Intent intent = new Intent(activity, LoginActivity.class);
                            startActivityForResult(intent, 1);
                        } else {//友盟统计
                            HashMap<String, String> map4 = new HashMap<String, String>();
                            map4.put("evenname", "加图");
                            map4.put("even", "你可能喜欢");
                            MobclickAgent.onEvent(activity, "shijian23", map4);
                            //ga统计
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory("你可能喜欢")  //事件类别
                                    .setAction("加图")      //事件操作
                                    .build());

                            Intent intent = new Intent(activity, InspirationSeriesActivity.class);
                            intent.putExtra("userid", mUserId);
                            intent.putExtra("image_url", mListData.get(position).getItem_info().getImage().getImg0());
                            intent.putExtra("item_id", mListData.get(position).getItem_info().getItem_id());
                            startActivity(intent);
                        }
                    }
                });

                holder.getView(R.id.iv_shibie_pic).setVisibility(View.GONE);
                holder.getView(R.id.iv_shibie_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //友盟统计
                        HashMap<String, String> map6 = new HashMap<String, String>();
                        map6.put("evenname", "识图入口");
                        map6.put("even", "图片详情－图片识别");
                        MobclickAgent.onEvent(imageScrollActivity, "shijian6", map6);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("图片详情－图片识别")  //事件类别
                                .setAction("识图入口")      //事件操作
                                .build());
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
            }
        };
        if (!ifHasHeader) {
            mRecyclerView.addHeaderView(view);
            ifHasHeader = true;
        }
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
        getImageListData();
    }

    @Override
    public void onLoadMore() {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        getImageListData();
    }

    private void getImageListData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                CustomProgress.cancelDialog();
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        String data = "{\"data\": " + data_msg + "}";
                        LikeDataBean likeDataBean = GsonUtil.jsonToBean(data, LikeDataBean.class);
                        updateViewFromData(likeDataBean.getData().getItem_list());
                    } else {
                        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().caiLikeImage(item_id, (page - 1) * 20 + "", 20 + "", callBack);

    }

    private void getImageDetail() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
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
                    ToastUtils.showCenter(activity, "图片信息获取失败");
                }
            }
        };

        MyHttpManager.getInstance().itemDetailsFaBu(item_id, callBack);

    }

    private void getSearchPosition(String image_id) {
        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                        String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                        String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                        if (error_code == 0) {
                            searchSBean = GsonUtil.jsonToBean(data_msg, SearchSBean.class);
                        } else {
                            ToastUtils.showCenter(activity, error_msg);
                        }
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().searchByImageIdUnRember(image_id, callback);
    }

    private void updateShiBieUI(boolean boo) {
        if (boo) {
            //在图片上面显示
            bt_shiwu2.setVisibility(View.VISIBLE);
            bt_shise2.setVisibility(View.VISIBLE);
            bt_shiwu.setVisibility(View.GONE);
            bt_shise.setVisibility(View.GONE);
        } else {
            //在页面上面显示
            bt_shiwu2.setVisibility(View.GONE);
            bt_shise2.setVisibility(View.GONE);
            bt_shiwu.setVisibility(View.VISIBLE);
            bt_shise.setVisibility(View.VISIBLE);
        }
    }

    private void updateViewFromData(List<ImageLikeItemBean> item_list) {

        if (item_list == null || item_list.size() == 0) {
            mRecyclerView.setLoadMoreEnabled(false);
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
        } else {
            mRecyclerView.setLoadMoreEnabled(true);
            page++;
            int position = mListData.size();
            mListData.addAll(item_list);
            mAdapter.notifyItem(position, mListData, item_list);
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
            for (int i = 0; i < item_list.size(); i++) {
                mItemIdList.add(item_list.get(i).getItem_info().getItem_id());
            }
        }
    }

    private void changeUI(ImageDetailBean imageDetailBean) {
        //......详情图片.........
        int wide_num = PublicUtils.getScreenWidth(activity);
        height_pic = (int) (wide_num / imageDetailBean.getItem_info().getImage().getRatio());
        ViewGroup.LayoutParams layoutParams = iv_details_image.getLayoutParams();
        layoutParams.width = wide_num;
        layoutParams.height = height_pic;
        iv_details_image.setLayoutParams(layoutParams);
        ImageUtils.disRectangleImage(imageDetailBean.getItem_info().getImage().getImg0(), iv_details_image);
        //......改变识图识色图标的显示位置.........
        if ((height_pic + UIUtils.getDimens(R.dimen.font_50)) < PublicUtils.getScreenHeight(activity)) {
            ifchange = false;
            updateShiBieUI(true);
        } else {
            ifchange = true;
            updateShiBieUI(false);
        }
        //......user_header.........
        ImageUtils.displayRoundImage(imageDetailBean.getUser_info().getAvatar().getThumb(), riv_people_header);

        //......nikename.........
        String nikeName = imageDetailBean.getUser_info().getNickname();
        if (nikeName.length() > 8) {
            nikeName = nikeName.substring(0, 8) + "...";
        }
        tv_people_name.setText(nikeName);
        //......专业用户，非专业用户标识.........
        if (!imageDetailBean.getUser_info().getProfession().equals("0")) {
            iv_people_tag.setVisibility(View.VISIBLE);
        } else {
            iv_people_tag.setVisibility(View.GONE);
        }
        //图片时间
        String[] str = imageDetailBean.getItem_info().getAdd_time().split(" ");
        String fabuTime = str[0].replace("-", "/");
        String[] str1 = str[0].split("-");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String strCurrent = formatter.format(curDate);
        long data = PublicUtils.diffMathDay(imageDetailBean.getItem_info().getAdd_time(), strCurrent, "yyyy-MM-dd HH:mm:ss");
        if (data <= 7) {
            if (str1.length == 3) {
                tv_details_time.setText(str1[0] + "年" + str1[1] + "月" + str1[2] + "日 " + " 发布");
            }
        } else if (data > 7 && data <= 30) {
            tv_details_time.setText("1周以前" + " 发布");
        } else {
            tv_details_time.setText("1月以前" + " 发布");
        }
        //图片标题
        if (TextUtils.isEmpty(imageDetailBean.getItem_info().getDescription().trim())) {
            tv_details_tital.setVisibility(View.GONE);
        } else {
            tv_details_tital.setText(imageDetailBean.getItem_info().getDescription().trim());
            tv_details_tital.setVisibility(View.VISIBLE);
        }
        //设置标签
        String tag = imageDetailBean.getItem_info().getTag().toString();
        String[] str_tag = tag.split(" ");
        listTag.clear();
        for (int i = 0; i < str_tag.length; i++) {
            if (!TextUtils.isEmpty(str_tag[i].trim())) {
                listTag.add(str_tag[i]);
            }
        }
        fl_tags_jubu.cleanTag();
        fl_tags_jubu.setColorful(false);
        fl_tags_jubu.setData(listTag);
        fl_tags_jubu.setOnTagClickListener(new FlowLayoutBiaoQian.OnTagClickListener() {
            @Override
            public void TagClick(String text) {
                // 跳转搜索结果页
                Intent intent = new Intent(activity, ShaiXuanResultActicity.class);
                String tag = text.replace("#", "");
                intent.putExtra("islist", true);
                intent.putExtra("shaixuan_tag", tag.trim());
                startActivity(intent);
            }
        });
        //更新颜色
        if (ifFirst && imageDetailBean != null) {
            ifFirst = false;
            List<ColorInfoBean> listColor = imageDetailBean.getColor_info();
            if (listColor != null && listColor.size() > 0) {
                int width = PublicUtils.getScreenWidth(activity) - UIUtils.getDimens(R.dimen.font_40);
                float float_talte = 0;
                for (int i = 0; i < listColor.size(); i++) {
                    float wid = Float.parseFloat(listColor.get(i).getColor_percent().trim());
                    float_talte = float_talte + wid;
                }

                for (int i = 0; i < listColor.size(); i++) {
                    TextView textView = new TextView(activity);
                    float wid = Float.parseFloat(listColor.get(i).getColor_percent().trim());
                    float per = wid / float_talte;

                    if (i == listColor.size() - 1) {
                        textView.setWidth(width);
                    } else {
                        textView.setWidth((int) (width * per));
                    }
                    textView.setHeight(UIUtils.getDimens(R.dimen.font_30));
                    textView.setBackgroundColor(Color.parseColor("#" + listColor.get(i).getColor_value()));
                    ll_color_lines.addView(textView);
                }
                dgv_colorlist.setAdapter(new MyColorGridAdapter(listColor, activity));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getImageDetail();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int top = Math.abs(totalDy);
            if (height_pic != 0 && top >= height_pic) {
                updateShiBieUI(true);
            } else if (height_pic != 0 && top < height_pic) {
                if (view_below_image.getLocalVisibleRect(rect)) {/*rect.contains(ivRect)*/
                    //控件在屏幕可见区域-----显现
                    updateShiBieUI(true);
                } else {
                    //控件已不在屏幕可见区域（已滑出屏幕）-----隐去
                    updateShiBieUI(false);
                }
            }
            if (imageScrollActivity.getmItemIdList().size() > 1) {

                String currentItemId = imageScrollActivity.getCurrentItemId();

                if (currentItemId.equals(item_id)) {
                    int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions);
                    if (lastPositions != null && lastPositions.length == 2 && lastPositions[0] == 1 && lastPositions[1] == 1) {
                        rl_navbar.setVisibility(View.VISIBLE);
                        rl_navbar_tital.setVisibility(View.INVISIBLE);
                    } else {
                        rl_navbar.setVisibility(View.INVISIBLE);
                        rl_navbar_tital.setVisibility(View.VISIBLE);
                    }
                }
            }
            mHandler.postDelayed(this, 100);
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            switch (code) {
                case 1:
                    String data = (String) msg.obj;
                    imageDetailBean = GsonUtil.jsonToBean(data, ImageDetailBean.class);
                    if (null != imageDetailBean
                            && null != imageDetailBean.getItem_info()
                            && null != imageDetailBean.getItem_info().getItem_id()) {
                        getSearchPosition(imageDetailBean.getItem_info().getImage().getImage_id());
                    }
                    changeUI(imageDetailBean);
                    mUserInfo.getUserInfo(imageDetailBean);
                    break;
            }
        }
    };

    public interface UserInfo {
        void getUserInfo(ImageDetailBean imageDetailBean);
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

    private void sharedItemOpen(SHARE_MEDIA share_media) {

        if (share_media == SHARE_MEDIA.WEIXIN) {
            //友盟统计
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("evenname", "分享图片");
            map1.put("even", "图片详情+微信好友");
            MobclickAgent.onEvent(imageScrollActivity, "jtaction7", map1);
            //ga统计
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("图片详情+微信好友")  //事件类别
                    .setAction("分享图片")      //事件操作
                    .build());

        } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
            //友盟统计
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("evenname", "分享图片");
            map1.put("even", "图片详情+微信朋友圈");
            MobclickAgent.onEvent(imageScrollActivity, "jtaction7", map1);
            //ga统计
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("图片详情+微信朋友圈")  //事件类别
                    .setAction("分享图片")      //事件操作
                    .build());
        } else if (share_media == SHARE_MEDIA.SINA) {
            //友盟统计
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("evenname", "分享图片");
            map1.put("even", "图片详情+新浪微博");
            MobclickAgent.onEvent(imageScrollActivity, "jtaction7", map1);
            //ga统计
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("图片详情+新浪微博")  //事件类别
                    .setAction("分享图片")      //事件操作
                    .build());
        } else if (share_media == SHARE_MEDIA.QQ) {
            //友盟统计
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("evenname", "分享图片");
            map1.put("even", "图片详情+QQ");
            MobclickAgent.onEvent(imageScrollActivity, "jtaction7", map1);
            //ga统计
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("图片详情+QQ")  //事件类别
                    .setAction("分享图片")      //事件操作
                    .build());
        }
        UMImage image = new UMImage(activity, imageDetailBean.getItem_info().getImage().getImg0());
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        UMWeb web = new UMWeb("http://h5.idcool.com.cn/photo/" + imageDetailBean.getItem_info().getItem_id());
        web.setTitle("「" + imageDetailBean.getUser_info().getNickname() + "」在晒家｜家图APP");//标题
        web.setThumb(image);  //缩略图
        String desi = imageDetailBean.getItem_info().getDescription() + imageDetailBean.getItem_info().getTag();
        if (desi.length() > 160) {
            desi = desi.substring(0, 160) + "...";
        }
        web.setDescription(desi);//描述
        new ShareAction(activity).
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
            if (platform == SHARE_MEDIA.WEIXIN) {
                ToastUtils.showCenter(activity, "微信好友分享成功啦");
            } else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {

                ToastUtils.showCenter(activity, "微信朋友圈分享成功啦");
            } else if (platform == SHARE_MEDIA.SINA) {

                ToastUtils.showCenter(activity, "新浪微博分享成功啦");
            } else if (platform == SHARE_MEDIA.QQ) {
                ToastUtils.showCenter(activity, "QQ分享成功啦");
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.showCenter(activity, "分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showCenter(activity, "分享取消了");
        }
    };

    private boolean firstLoad = true;
    private boolean ifHasHeader = false;

    private String mUserId;
    private String item_id;
    private UserInfo mUserInfo;
    private ImageDetailBean imageDetailBean;
    private ImageDetailScrollActivity imageScrollActivity;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    //全局
    private TextView tv_lingganji;
    private TextView tv_tital_comment;
    private RelativeLayout rl_navbar;
    private RelativeLayout rl_navbar_tital;
    private ImageButton iv_edit_image;
    private ImageButton iv_shared_image;
    private ImageButton iv_more_image;
    private HomeSharedPopWinPublic homeSharedPopWinPublic;
    private HRecyclerView mRecyclerView;
    private ResizeRelativeLayout menu_layout;
    private ImageView bt_shiwu;
    private ImageView bt_shise;
    private RelativeLayout rl_color;
    private View iv_close_color;
    private MyListView dgv_colorlist;
    private LinearLayout ll_color_lines;
    private View color_bottom;
    //header
    private View view;
    private ImageView iv_details_image;
    private View view_below_image;
    private ImageView bt_shiwu2;
    private ImageView bt_shise2;
    private ShangshabanChangeTextSpaceView tv_details_tital;
    private FlowLayoutBiaoQian fl_tags_jubu;
    private RoundImageView riv_people_header;
    private TextView tv_people_name;
    private ImageView iv_people_tag;
    private TextView tv_details_time;
    private TextView tv_maybe_like;
    private int width_Pic;

    private int height_pic = 0;
    private boolean ifchange = false;
    private boolean loginStatus = false;
    private boolean ifFirst = true;
    private SearchSBean searchSBean;
    private LoadMoreFooterView mLoadMoreFooterView;
    private List<String> listTag = new ArrayList<>();
    private List<String> mItemIdList = new ArrayList<>();
    private List<ImageLikeItemBean> mListData = new ArrayList<>();
    private MultiItemCommonAdapter<ImageLikeItemBean> mAdapter;
    private int TYPE_ONE = 1;
    private int page = 1;
    private int totalDy = 0;
    private Rect rect;

}
