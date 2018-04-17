package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.activity.ImageEditActvity;
import com.homechart.app.home.activity.JuBaoActivity;
import com.homechart.app.home.activity.LoginActivity;
import com.homechart.app.home.activity.MyWebViewActivity;
import com.homechart.app.home.activity.PingListActivity;
import com.homechart.app.home.base.LazyLoadFragment;
import com.homechart.app.home.bean.cailike.ImageLikeItemBean;
import com.homechart.app.home.bean.cailike.LikeDataBean;
import com.homechart.app.home.bean.imagedetail.ImageDetailBean;
import com.homechart.app.home.bean.searchfservice.SearchSBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.imagedetail.ImageDetailsActivity;
import com.homechart.app.lingganji.ui.activity.InspirationDetailActivity;
import com.homechart.app.lingganji.ui.activity.InspirationSeriesActivity;
import com.homechart.app.myview.FlowLayoutBiaoQian;
import com.homechart.app.myview.HomeSharedPopWinPublic;
import com.homechart.app.myview.JuBaoPopWin;
import com.homechart.app.myview.ResizeRelativeLayout;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.ShangshabanChangeTextSpaceView;
import com.homechart.app.myview.TopCropImageView;
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

public class NewImageDetaiScrollFragment
        extends LazyLoadFragment implements
        HomeSharedPopWinPublic.ClickInter,
        JuBaoPopWin.ClickInter,
        View.OnClickListener,
        OnLoadMoreListener {


    private View mRootView;
    private boolean ifDelect = false;

    public NewImageDetaiScrollFragment() {

    }

    @SuppressLint("ValidFragment")
    public NewImageDetaiScrollFragment(View mRootView, FragmentManager fragmentManager, String item_id, UserInfo userInfo, int posotion) {
        this.mRootView = mRootView;
        this.fragmentManager = fragmentManager;
        this.item_id = item_id;
        this.mUserInfo = userInfo;
        this.mPosotion = posotion;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_new_image_details;
    }

    @Override
    protected void lazyLoad() {
        try {
            if (firstLoad) {
                initExtraBundle();
                initView();
                initListener();
                initData();
                firstLoad = false;
            }
        } catch (Exception e) {
        }
    }

    private void initExtraBundle() {
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
    }

    private void initView() {
        tv_lingganji = (TextView) mRootView.findViewById(R.id.tv_lingganji);
        iv_edit_image = (ImageButton) mRootView.findViewById(R.id.iv_edit_image);
        iv_shared_image = (ImageButton) mRootView.findViewById(R.id.iv_shared_image);
        iv_more_image = (ImageButton) mRootView.findViewById(R.id.iv_more_image);
        tv_tital_comment = (TextView) mRootView.findViewById(R.id.tv_tital_comment);

        homeSharedPopWinPublic = new HomeSharedPopWinPublic(activity, this);
        juBaoPopWin = new JuBaoPopWin(activity, this);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_info);
        menu_layout = (ResizeRelativeLayout) rootView.findViewById(R.id.menu_layout);
        bt_shiwu = (ImageView) rootView.findViewById(R.id.bt_shiwu);


        if (!ifHasHeader) {
            view = LayoutInflater.from(activity).inflate(R.layout.header_fragment_new_imagedetails, null);
            rl_shop_tital = (RelativeLayout) view.findViewById(R.id.rl_shop_tital);
            tv_shop_price = (TextView) view.findViewById(R.id.tv_shop_price);
            tv_shop_miaosu = (TextView) view.findViewById(R.id.tv_shop_miaosu);
            iv_details_image = (ImageView) view.findViewById(R.id.iv_details_image);
            view_below_image = view.findViewById(R.id.view_below_image);
            bt_shiwu2 = (ImageView) view.findViewById(R.id.bt_shiwu2);
            tv_details_tital = (ShangshabanChangeTextSpaceView) view.findViewById(R.id.tv_details_tital);
            fl_tags_jubu = (FlowLayoutBiaoQian) view.findViewById(R.id.fl_tags_jubu);
            riv_people_header = (RoundImageView) view.findViewById(R.id.riv_people_header);
            tv_people_name = (TextView) view.findViewById(R.id.tv_people_name);
            tv_details_time = (TextView) view.findViewById(R.id.tv_details_time);
            tv_from_where = (TextView) view.findViewById(R.id.tv_from_where);
            tv_goto_shop = (TextView) view.findViewById(R.id.tv_goto_shop);
            tv_maybe_like = (TextView) view.findViewById(R.id.tv_maybe_like);

            iv_more_album = (ImageView) view.findViewById(R.id.iv_more_album);
            rl_album_all = (RelativeLayout) view.findViewById(R.id.rl_album_all);
            rl_images_one = (RelativeLayout) view.findViewById(R.id.rl_images_one);
            rl_images_two = (RelativeLayout) view.findViewById(R.id.rl_images_two);
            rl_images_three = (RelativeLayout) view.findViewById(R.id.rl_images_three);
            rl_header1 = (RelativeLayout) view.findViewById(R.id.rl_header1);
            rl_header2 = (RelativeLayout) view.findViewById(R.id.rl_header2);
            rl_header3 = (RelativeLayout) view.findViewById(R.id.rl_header3);
            riv_header_one = (RoundImageView) view.findViewById(R.id.riv_header_one);
            riv_header_two = (RoundImageView) view.findViewById(R.id.riv_header_two);
            riv_header_three = (RoundImageView) view.findViewById(R.id.riv_header_three);
            iv_img1 = (ImageView) view.findViewById(R.id.iv_img1);
            iv_img2 = (ImageView) view.findViewById(R.id.iv_img2);
            iv_img3 = (ImageView) view.findViewById(R.id.iv_img3);
            iv_img4 = (ImageView) view.findViewById(R.id.iv_img4);
            iv_img5 = (ImageView) view.findViewById(R.id.iv_img5);
            iv_img6 = (ImageView) view.findViewById(R.id.iv_img6);
            iv_img7 = (ImageView) view.findViewById(R.id.iv_img7);
            iv_img8 = (ImageView) view.findViewById(R.id.iv_img8);
            iv_img9 = (ImageView) view.findViewById(R.id.iv_img9);
            iv_img10 = (ImageView) view.findViewById(R.id.iv_img10);
            iv_img11 = (ImageView) view.findViewById(R.id.iv_img11);
            iv_img12 = (ImageView) view.findViewById(R.id.iv_img12);

            tv_pinglun = (TextView) view.findViewById(R.id.tv_pinglun);
            rl_pinglun = (RelativeLayout) view.findViewById(R.id.rl_pinglun);
        }
    }

    private void initListener() {
        bt_shiwu.setOnClickListener(this);
        bt_shiwu2.setOnClickListener(this);
        tv_lingganji.setOnClickListener(this);
        iv_details_image.setOnClickListener(this);
        iv_edit_image.setOnClickListener(this);
        riv_people_header.setOnClickListener(this);
        iv_shared_image.setOnClickListener(this);
        iv_more_image.setOnClickListener(this);
        tv_goto_shop.setOnClickListener(this);
        rl_pinglun.setOnClickListener(this);
        rl_album_all.setOnClickListener(this);
        iv_more_album.setOnClickListener(this);
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
            case R.id.rl_album_all:
            case R.id.iv_more_album:
                if (!ifDelect) {
                    //友盟统计
                    HashMap<String, String> map8 = new HashMap<String, String>();
                    map8.put("evenname", "更多灵感辑");
                    map8.put("even", "点击更多灵感辑进入更多灵感辑页面的次数");
                    MobclickAgent.onEvent(activity, "shijian34", map8);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("点击更多灵感辑进入更多灵感辑页面的次数")  //事件类别
                            .setAction("更多灵感辑")      //事件操作
                            .build());

                    NewMoreLingGanFragment newUserInfoFragment1 = new NewMoreLingGanFragment(fragmentManager);
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("item_id", item_id);
                    newUserInfoFragment1.setArguments(bundle3);
                    FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                    fragmentTransaction2.replace(R.id.id_main, newUserInfoFragment1);
                    fragmentTransaction2.addToBackStack(null);
                    fragmentTransaction2.commit();

                } else {
                    ToastUtils.showCenter(activity, "图片已被删除");
                }

                break;
            case R.id.tv_lingganji:
                if (!ifDelect) {
                    loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
                    if (!loginStatus) {
                        //友盟统计
                        HashMap<String, String> map4 = new HashMap<String, String>();
                        map4.put("evenname", "登录入口");
                        map4.put("even", "图片详情页进行图片收藏");
                        MobclickAgent.onEvent(activity, "shijian20", map4);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("图片详情页进行图片收藏")  //事件类别
                                .setAction("登录入口")      //事件操作
                                .build());
                        Intent intent = new Intent(activity, LoginActivity.class);
                        startActivityForResult(intent, 1);
                    } else {
                        if (null != imageDetailBean) {
                            //友盟统计
                            HashMap<String, String> map4 = new HashMap<String, String>();
                            map4.put("evenname", "加灵感辑");
                            map4.put("even", "图片详情");
                            MobclickAgent.onEvent(activity, "shijian30", map4);
                            //ga统计
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory("图片详情")  //事件类别
                                    .setAction("加灵感辑")      //事件操作
                                    .build());

                            if (mUserInfo.getCurrentPosition() == mPosotion) {
                                mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
                                Intent intent = new Intent(activity, InspirationSeriesActivity.class);
                                intent.putExtra("userid", mUserId);
                                intent.putExtra("image_url", imageDetailBean.getItem_info().getImage().getImg0());
                                intent.putExtra("item_id", imageDetailBean.getItem_info().getItem_id());
                                startActivity(intent);
                            }
                        }
                    }
                } else {
                    ToastUtils.showCenter(activity, "图片已被删除");
                }

                break;
            case R.id.iv_edit_image:
                if (!ifDelect) {
                    if (imageDetailBean != null) {
                        Intent intent = new Intent(activity, ImageEditActvity.class);
                        intent.putExtra("image_value", imageDetailBean);
                        startActivityForResult(intent, 1);
                    }
                } else {
                    ToastUtils.showCenter(activity, "图片已被删除");
                }

                break;
            case R.id.iv_details_image:
                if (!ifDelect) {
                    if (null != imageDetailBean) {
                        List<String> listUrl = new ArrayList<>();
                        listUrl.add(imageDetailBean.getItem_info().getImage().getImg0());
                        Intent intent6 = new Intent(activity, ImageDetailsActivity.class);
                        Bundle bundle6 = new Bundle();
                        bundle6.putSerializable("pic_url_list", (Serializable) listUrl);
                        bundle6.putInt("click_position", 0);
                        bundle6.putInt("ifhinttital", 2);
                        intent6.putExtras(bundle6);
                        startActivity(intent6);
                    }
                } else {
                    ToastUtils.showCenter(activity, "图片已被删除");
                }

                break;
            case R.id.bt_shiwu:
            case R.id.bt_shiwu2:
                if (!ifDelect) {
                    if (PublicUtils.ifHasWriteQuan(activity)) {
                        //有权限
                        if (null != imageDetailBean && null != searchSBean) {
                            //友盟统计
                            HashMap<String, String> map6 = new HashMap<String, String>();
                            map6.put("evenname", "识图入口");
                            map6.put("even", "图片详情－图片详情物体识别");
                            MobclickAgent.onEvent(activity, "shijian6", map6);
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
                            Bundle bundle2 = new Bundle();
                            bundle2.putSerializable("searchSBean", searchSBean);
                            intent4.putExtras(bundle2);
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
                } else {
                    ToastUtils.showCenter(activity, "图片已被删除");
                }

                break;
            case R.id.riv_people_header:
                if (!ifDelect) {
                    if (imageDetailBean != null) {
                        NewUserInfoFragment newUserInfoFragment = new NewUserInfoFragment(fragmentManager);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString(ClassConstant.LoginSucces.USER_ID, imageDetailBean.getUser_info().getUser_id());
                        newUserInfoFragment.setArguments(bundle1);
                        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
                        fragmentTransaction1.replace(R.id.id_main, newUserInfoFragment);
                        fragmentTransaction1.addToBackStack(null);
                        fragmentTransaction1.commit();
                    }
                } else {
                    ToastUtils.showCenter(activity, "图片已被删除");
                }

                break;
            case R.id.iv_shared_image:
                if (!ifDelect) {
                    if (imageDetailBean != null) {
                        homeSharedPopWinPublic.showAtLocation(activity.findViewById(R.id.menu_layout),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                                0,
                                0); //设置layout在PopupWindow中显示的位置
                    }
                } else {
                    ToastUtils.showCenter(activity, "图片已被删除");
                }

                break;
            case R.id.iv_more_image:
                if (!ifDelect) {
                    if (imageDetailBean != null) {
                        juBaoPopWin.showAtLocation(activity.findViewById(R.id.menu_layout),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                                0,
                                0); //设置layout在PopupWindow中显示的位置
                    }
                } else {
                    ToastUtils.showCenter(activity, "图片已被删除");
                }

                break;
            case R.id.tv_goto_shop:
                if (!ifDelect) {
                    //友盟统计
                    HashMap<String, String> map7 = new HashMap<String, String>();
                    map7.put("evenname", "图片来源");
                    map7.put("even", "点击来源跳转浏览器打开的次数");
                    MobclickAgent.onEvent(activity, "shijian33", map7);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("点击来源跳转浏览器打开的次数")  //事件类别
                            .setAction("图片来源")      //事件操作
                            .build());
                    if (null != imageDetailBean &&
                            null != imageDetailBean.getItem_info().getFrom_url() &&
                            !imageDetailBean.getItem_info().getFrom_url().equals("")) {
                        Intent intent = new Intent(activity, MyWebViewActivity.class);
                        intent.putExtra("weburl", imageDetailBean.getItem_info().getFrom_url());
                        startActivity(intent);
                    }
                } else {
                    ToastUtils.showCenter(activity, "图片已被删除");
                }

                break;
            case R.id.rl_pinglun:
                if (!ifDelect) {
                    loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
                    if (!loginStatus) {
                        Intent intent = new Intent(activity, LoginActivity.class);
                        startActivityForResult(intent, 1);
                    } else {
                        Intent intent1 = new Intent(activity, PingListActivity.class);
                        intent1.putExtra("item_id", item_id);
                        intent1.putExtra("ifopen", imageDetailBean.getCounter().getComment_num() == 0 ? "true" : "false");
                        intent1.putExtra("reply_id", "");
                        startActivityForResult(intent1, 2);
                    }
                } else {
                    ToastUtils.showCenter(activity, "图片已被删除");
                }


                break;
        }
    }

    private void initData() {

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        width_Pic = PublicUtils.getScreenWidth(activity) / 2 - UIUtils.getDimens(R.dimen.font_28);
        width_Imgs = (PublicUtils.getScreenWidth(activity) - UIUtils.getDimens(R.dimen.font_146)) / 3;
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) rl_images_one.getLayoutParams();
        layoutParams1.width = width_Imgs;
        layoutParams1.height = width_Imgs;
        layoutParams1.alignWithParent = true;
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) rl_images_two.getLayoutParams();
        layoutParams2.width = width_Imgs;
        layoutParams2.height = width_Imgs;
        layoutParams2.alignWithParent = true;
        layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) rl_images_three.getLayoutParams();
        layoutParams3.width = width_Imgs;
        layoutParams3.height = width_Imgs;
        layoutParams3.alignWithParent = true;
        layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        rl_images_one.setLayoutParams(layoutParams1);
        rl_images_two.setLayoutParams(layoutParams2);
        rl_images_three.setLayoutParams(layoutParams3);
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
                    return R.layout.item_pubu_more_like;
                } else {
                    return R.layout.item_pubu_more_like;
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

                String nikeName = mListData.get(position).getUser_info().getNickname();
                ((TextView) holder.getView(R.id.tv_name_pic)).setText(nikeName);
                ((TextView) holder.getView(R.id.tv_name_ablum)).setText(mListData.get(position).getAlbum_info().getAlbum_name());
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
                if (mListData.get(position).getItem_info().getDescription().trim().equals("") && mListData.get(position).getItem_info().getTag().trim().equals("")) {
                    ((TextView) holder.getView(R.id.tv_image_miaosu)).setVisibility(View.GONE);
                } else {
                    ((TextView) holder.getView(R.id.tv_image_miaosu)).setVisibility(View.VISIBLE);
                    ((TextView) holder.getView(R.id.tv_image_miaosu)).setText(Html.fromHtml(str));
                }


                if (PublicUtils.ifHasWriteQuan(activity)) {
                    if (mListData.get(position).getItem_info().getImage().getRatio() > 0.6) {
                        ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                        layoutParams.height = mListData.get(position).getItem_info().getImage().getRatio() == 0
                                ? width_Pic
                                : Math.round(width_Pic / mListData.get(position).getItem_info().getImage().getRatio());
                        holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                        ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg1(),
                                (ImageView) holder.getView(R.id.iv_imageview_one));
                    } else {
                        if (mListData.get(position).getItem_info().getImage().getRatio() > 0.333) {
                            ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                            layoutParams.height = mListData.get(position).getItem_info().getImage().getRatio() == 0
                                    ? width_Pic
                                    : Math.round(width_Pic / mListData.get(position).getItem_info().getImage().getRatio());
                            holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                            GlideImgManager.glideLoader(activity, mListData.get(position).getItem_info().getImage().getImg1(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_imageview_one), 1);
                        } else {
                            ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                            layoutParams.height = width_Pic * 3;
                            holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                            GlideImgManager.glideLoader(activity, mListData.get(position).getItem_info().getImage().getImg1(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_imageview_one), 1);
                        }
                    }
                    ImageUtils.displayFilletImage(mListData.get(position).getUser_info().getAvatar().getBig(),
                            (ImageView) holder.getView(R.id.iv_header_pic));
                } else {
                    if (mListData.get(position).getItem_info().getImage().getRatio() > 0.333) {
                        ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                        layoutParams.height = mListData.get(position).getItem_info().getImage().getRatio() == 0
                                ? width_Pic
                                : Math.round(width_Pic / mListData.get(position).getItem_info().getImage().getRatio());
                        holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                        GlideImgManager.glideLoader(activity, mListData.get(position).getItem_info().getImage().getImg1(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_imageview_one), 1);
                    } else {
                        ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();
                        layoutParams.height = width_Pic * 3;
                        holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                        GlideImgManager.glideLoader(activity, mListData.get(position).getItem_info().getImage().getImg1(), R.color.white, R.color.white, (ImageView) holder.getView(R.id.iv_imageview_one), 1);
                    }
                    GlideImgManager.glideLoader(activity, mListData.get(position).getUser_info().getAvatar().getBig(), R.color.white, R.color.white, (TopCropImageView) holder.getView(R.id.iv_header_pic), 0);
                }

                holder.getView(R.id.iv_imageview_one).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NewImageDetailsFragment newImageDetailsFragment = new NewImageDetailsFragment(fragmentManager);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("item_id", mListData.get(position).getItem_info().getItem_id());
                        bundle.putInt("position", position);
                        bundle.putString("type", "色彩");
                        bundle.putBoolean("if_click_color", false);
                        bundle.putInt("page_num", page);
                        bundle.putSerializable("item_id_list", (Serializable) mItemIdList);
                        newImageDetailsFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.addToBackStack(null).replace(R.id.id_main, newImageDetailsFragment);
                        fragmentTransaction.commit();
                    }
                });

                holder.getView(R.id.rl_test).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewInspirationDetailsment newInspirationDetailsment = new NewInspirationDetailsment(fragmentManager);
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id", "");
                        bundle.putBoolean("ifHideEdit", true);
                        bundle.putString("album_id", mListData.get(position).getAlbum_info().getAlbum_id());
                        bundle.putString("show_type", mListData.get(position).getAlbum_info().getShow_type());
                        newInspirationDetailsment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.id_main, newInspirationDetailsment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
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
            bt_shiwu.setVisibility(View.GONE);
        } else {
            //在页面上面显示
            bt_shiwu2.setVisibility(View.GONE);
            bt_shiwu.setVisibility(View.VISIBLE);
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

        try {
            //评论
            rl_pinglun.setVisibility(View.VISIBLE);
            if (imageDetailBean.getCounter().getComment_num() == 0) {
                tv_pinglun.setText("添加评论");
            } else {
                tv_pinglun.setText(imageDetailBean.getCounter().getComment_num() + "个评论");
            }
            //商品价格
            if (null == imageDetailBean.getItem_info().getIs_product() || imageDetailBean.getItem_info().getIs_product().equals("0")) {
                rl_shop_tital.setVisibility(View.GONE);
            } else {
                rl_shop_tital.setVisibility(View.VISIBLE);
                tv_shop_miaosu.setText(imageDetailBean.getItem_info().getTitle());
                tv_shop_price.setText(imageDetailBean.getItem_info().getPrice());
            }
            //......详情图片.........
            int wide_num = PublicUtils.getScreenWidth(activity)- UIUtils.getDimens(R.dimen.font_40);
            height_pic = (int) (wide_num / imageDetailBean.getItem_info().getImage().getRatio());
            height_tag = height_pic + UIUtils.getDimens(R.dimen.font_15);
            ViewGroup.LayoutParams layoutParams = iv_details_image.getLayoutParams();
            layoutParams.width = wide_num;
            layoutParams.height = height_pic;
            iv_details_image.setLayoutParams(layoutParams);
            ImageUtils.displayFilletImage(imageDetailBean.getItem_info().getImage().getImg0(), iv_details_image);
            //......改变识图识色图标的显示位置.........
            if ((height_pic + UIUtils.getDimens(R.dimen.font_65)) < PublicUtils.getScreenHeight(activity)) {
                ifchange = false;
                if (!mUserInfo.getIfBack()) {
                    updateShiBieUI(true);
                }
            } else {
                ifchange = true;
                if (!mUserInfo.getIfBack()) {
                    updateShiBieUI(false);
                }
            }
            //......user_header.........
            ImageUtils.displayRoundImage(imageDetailBean.getUser_info().getAvatar().getThumb(), riv_people_header);

            //......nikename.........
            tv_people_name.setText("");
            String strText1 = imageDetailBean.getUser_info().getNickname();
            if (null != strText1) {
                SpannableString spString1 = new SpannableString(strText1);
                spString1.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setTextSize(UIUtils.getDimens(R.dimen.size_14));//设置字体大小
                        ds.setFakeBoldText(false);//设置粗体
                        ds.setColor(UIUtils.getColor(R.color.bg_262626));//设置字体颜色
                        ds.setUnderlineText(false);//设置取消下划线
                    }

                    @Override
                    public void onClick(View widget) {
                        //添加点击事件
                        Message message = new Message();
                        message.what = 2;
                        mHandler.sendMessage(message);
                    }
                }, 0, strText1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_people_name.append(spString1);
            }


            if (imageDetailBean.getItem_info().getGet_way() != null && imageDetailBean.getAlbum_info().getAlbum_name() != null) {
                String strText2 = "   上传至   ";
                if (imageDetailBean.getItem_info().getGet_way().equals("upload")) {
                    strText2 = "   上传至   ";
                } else {
                    strText2 = "   采集至   ";
                }
                SpannableString spString2 = new SpannableString(strText2);
                spString2.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setTextSize(UIUtils.getDimens(R.dimen.size_12));//设置字体大小
                        ds.setFakeBoldText(false);//设置粗体
                        ds.setColor(UIUtils.getColor(R.color.bg_8f8f8f));//设置字体颜色
                        ds.setUnderlineText(false);//设置取消下划线
                    }

                    @Override
                    public void onClick(View widget) {
                    }
                }, 0, strText2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_people_name.append(spString2);
                String strText3 = imageDetailBean.getAlbum_info().getAlbum_name();
                SpannableString spString3 = new SpannableString(strText3);
                spString3.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setTextSize(UIUtils.getDimens(R.dimen.size_14));//设置字体大小
                        ds.setFakeBoldText(false);//设置粗体
                        ds.setColor(UIUtils.getColor(R.color.bg_e79056));//设置字体颜色
                        ds.setUnderlineText(false);//设置取消下划线
                    }

                    @Override
                    public void onClick(View widget) {
                        //添加点击事件
                        Message message = new Message();
                        message.what = 3;
                        mHandler.sendMessage(message);
                    }
                }, 0, strText3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_people_name.append(spString3);
                tv_people_name.setMovementMethod(LinkMovementMethod.getInstance());
                tv_people_name.setHighlightColor(getResources().getColor(android.R.color.transparent));
            }

            //图片时间
            if (imageDetailBean.getItem_info().getGet_way() != null && imageDetailBean.getItem_info().getGet_way().equals("upload")) {
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
                tv_goto_shop.setVisibility(View.GONE);
                tv_from_where.setVisibility(View.GONE);
            } else if (imageDetailBean.getItem_info().getGet_way().equals("grab")) {
                tv_goto_shop.setVisibility(View.VISIBLE);
                tv_from_where.setVisibility(View.VISIBLE);
                tv_details_time.setText("采自 ");
                if (null != imageDetailBean.getItem_info().getFrom_domain()) {
                    tv_from_where.setText(imageDetailBean.getItem_info().getFrom_domain());
                }
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
                    String tag = text.replace("#", "");
                    tag = tag.replace("＃", "");
                    NewLanMuFragment newLanMuFragment = new NewLanMuFragment(fragmentManager);
                    Bundle bundle = new Bundle();
                    bundle.putString("tag_name", tag);
                    newLanMuFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.id_main, newLanMuFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            //设置灵感辑图片
            RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) iv_img1.getLayoutParams();
            layoutParams1.width = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams1.height = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams1.alignWithParent = true;

            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) iv_img2.getLayoutParams();
            layoutParams2.width = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams2.height = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams2.alignWithParent = true;
            layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

            RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) iv_img3.getLayoutParams();
            layoutParams3.width = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams3.height = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams3.alignWithParent = true;
            layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

            RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) iv_img4.getLayoutParams();
            layoutParams4.width = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams4.height = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams4.alignWithParent = true;
            layoutParams4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM | RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

            iv_img1.setLayoutParams(layoutParams1);
            iv_img2.setLayoutParams(layoutParams2);
            iv_img3.setLayoutParams(layoutParams3);
            iv_img4.setLayoutParams(layoutParams4);
            iv_img5.setLayoutParams(layoutParams1);
            iv_img6.setLayoutParams(layoutParams2);
            iv_img7.setLayoutParams(layoutParams3);
            iv_img8.setLayoutParams(layoutParams4);
            iv_img9.setLayoutParams(layoutParams1);
            iv_img10.setLayoutParams(layoutParams2);
            iv_img11.setLayoutParams(layoutParams3);
            iv_img12.setLayoutParams(layoutParams4);

            if (imageDetailBean != null && imageDetailBean.getRelated_albums() != null && imageDetailBean.getRelated_albums().size() > 0) {
                iv_more_album.setVisibility(View.VISIBLE);
                rl_album_all.setVisibility(View.VISIBLE);
                if (imageDetailBean.getRelated_albums().size() == 1) {
                    rl_images_one.setVisibility(View.VISIBLE);
                    rl_images_two.setVisibility(View.GONE);
                    rl_images_three.setVisibility(View.GONE);
                    rl_header1.setVisibility(View.VISIBLE);
                    rl_header2.setVisibility(View.GONE);
                    rl_header3.setVisibility(View.GONE);
                    ImageUtils.displayRoundImage(imageDetailBean.getRelated_albums().get(0).getData_info().getUser_avatar(), riv_header_one);
                } else if (imageDetailBean.getRelated_albums().size() == 2) {

                    rl_images_one.setVisibility(View.VISIBLE);
                    rl_images_two.setVisibility(View.VISIBLE);
                    rl_images_three.setVisibility(View.GONE);
                    rl_header1.setVisibility(View.VISIBLE);
                    rl_header2.setVisibility(View.VISIBLE);
                    rl_header3.setVisibility(View.GONE);
                    ImageUtils.displayRoundImage(imageDetailBean.getRelated_albums().get(0).getData_info().getUser_avatar(), riv_header_one);
                    ImageUtils.displayRoundImage(imageDetailBean.getRelated_albums().get(1).getData_info().getUser_avatar(), riv_header_two);
                } else if (imageDetailBean.getRelated_albums().size() == 3) {
                    rl_images_one.setVisibility(View.VISIBLE);
                    rl_images_two.setVisibility(View.VISIBLE);
                    rl_images_three.setVisibility(View.VISIBLE);
                    rl_header1.setVisibility(View.VISIBLE);
                    rl_header2.setVisibility(View.VISIBLE);
                    rl_header3.setVisibility(View.VISIBLE);
                    ImageUtils.displayRoundImage(imageDetailBean.getRelated_albums().get(0).getData_info().getUser_avatar(), riv_header_one);
                    ImageUtils.displayRoundImage(imageDetailBean.getRelated_albums().get(1).getData_info().getUser_avatar(), riv_header_two);
                    ImageUtils.displayRoundImage(imageDetailBean.getRelated_albums().get(2).getData_info().getUser_avatar(), riv_header_three);
                }
                if (imageDetailBean.getRelated_albums().size() > 0 &&
                        null != imageDetailBean.getRelated_albums().get(0).getData_info() &&
                        null != imageDetailBean.getRelated_albums().get(0).getData_info().getImages() &&
                        imageDetailBean.getRelated_albums().get(0).getData_info().getImages().size() > 0) {
                    ImageUtils.displayFilletLeftTopImage(imageDetailBean.getRelated_albums().get(0).getData_info().getImages().get(0), iv_img1);
                } else {
                    ImageUtils.displayFilletLeftTopImage("drawable://" + R.drawable.test_color, iv_img1);
                }
                if (imageDetailBean.getRelated_albums().size() > 0 &&
                        null != imageDetailBean.getRelated_albums().get(0).getData_info() &&
                        null != imageDetailBean.getRelated_albums().get(0).getData_info().getImages() &&
                        imageDetailBean.getRelated_albums().get(0).getData_info().getImages().size() > 1) {
                    ImageUtils.displayFilletRightTopImage(imageDetailBean.getRelated_albums().get(0).getData_info().getImages().get(1), iv_img2);
                } else {
                    ImageUtils.displayFilletRightTopImage("drawable://" + R.drawable.test_color, iv_img2);
                }
                if (imageDetailBean.getRelated_albums().size() > 0 &&
                        null != imageDetailBean.getRelated_albums().get(0).getData_info() &&
                        null != imageDetailBean.getRelated_albums().get(0).getData_info().getImages() &&
                        imageDetailBean.getRelated_albums().get(0).getData_info().getImages().size() > 2) {
                    ImageUtils.displayFilletLeftBottomImage(imageDetailBean.getRelated_albums().get(0).getData_info().getImages().get(2), iv_img3);
                } else {
                    ImageUtils.displayFilletLeftBottomImage("drawable://" + R.drawable.test_color, iv_img3);
                }
                if (imageDetailBean.getRelated_albums().size() > 0 &&
                        null != imageDetailBean.getRelated_albums().get(0).getData_info() &&
                        null != imageDetailBean.getRelated_albums().get(0).getData_info().getImages() &&
                        imageDetailBean.getRelated_albums().get(0).getData_info().getImages().size() > 3) {
                    ImageUtils.displayFilletRightBottomImage(imageDetailBean.getRelated_albums().get(0).getData_info().getImages().get(3), iv_img4);
                } else {
                    ImageUtils.displayFilletRightBottomImage("drawable://" + R.drawable.test_color, iv_img4);
                }
                if (imageDetailBean.getRelated_albums().size() > 1 &&
                        null != imageDetailBean.getRelated_albums().get(1).getData_info() &&
                        null != imageDetailBean.getRelated_albums().get(1).getData_info().getImages() &&
                        imageDetailBean.getRelated_albums().get(1).getData_info().getImages().size() > 0) {
                    ImageUtils.displayFilletLeftTopImage(imageDetailBean.getRelated_albums().get(1).getData_info().getImages().get(0), iv_img5);
                } else {
                    ImageUtils.displayFilletLeftTopImage("drawable://" + R.drawable.test_color, iv_img5);
                }
                if (imageDetailBean.getRelated_albums().size() > 1 &&
                        null != imageDetailBean.getRelated_albums().get(1).getData_info() &&
                        null != imageDetailBean.getRelated_albums().get(1).getData_info().getImages() &&
                        imageDetailBean.getRelated_albums().get(1).getData_info().getImages().size() > 1) {
                    ImageUtils.displayFilletRightTopImage(imageDetailBean.getRelated_albums().get(1).getData_info().getImages().get(1), iv_img6);
                } else {
                    ImageUtils.displayFilletRightTopImage("drawable://" + R.drawable.test_color, iv_img6);
                }
                if (imageDetailBean.getRelated_albums().size() > 1 &&
                        null != imageDetailBean.getRelated_albums().get(1).getData_info() &&
                        null != imageDetailBean.getRelated_albums().get(1).getData_info().getImages() &&
                        imageDetailBean.getRelated_albums().get(1).getData_info().getImages().size() > 2) {
                    ImageUtils.displayFilletLeftBottomImage(imageDetailBean.getRelated_albums().get(1).getData_info().getImages().get(2), iv_img7);
                } else {
                    ImageUtils.displayFilletLeftBottomImage("drawable://" + R.drawable.test_color, iv_img7);
                }
                if (imageDetailBean.getRelated_albums().size() > 1 &&
                        null != imageDetailBean.getRelated_albums().get(1).getData_info() &&
                        null != imageDetailBean.getRelated_albums().get(1).getData_info().getImages() &&
                        imageDetailBean.getRelated_albums().get(1).getData_info().getImages().size() > 3) {
                    ImageUtils.displayFilletRightBottomImage(imageDetailBean.getRelated_albums().get(1).getData_info().getImages().get(3), iv_img8);
                } else {
                    ImageUtils.displayFilletRightBottomImage("drawable://" + R.drawable.test_color, iv_img8);
                }
                if (imageDetailBean.getRelated_albums().size() > 2 &&
                        null != imageDetailBean.getRelated_albums().get(2).getData_info() &&
                        null != imageDetailBean.getRelated_albums().get(2).getData_info().getImages() &&
                        imageDetailBean.getRelated_albums().get(2).getData_info().getImages().size() > 0) {
                    ImageUtils.displayFilletLeftTopImage(imageDetailBean.getRelated_albums().get(2).getData_info().getImages().get(0), iv_img9);
                } else {
                    ImageUtils.displayFilletLeftTopImage("drawable://" + R.drawable.test_color, iv_img9);
                }
                if (imageDetailBean.getRelated_albums().size() > 2 &&
                        null != imageDetailBean.getRelated_albums().get(2).getData_info() &&
                        null != imageDetailBean.getRelated_albums().get(2).getData_info().getImages() &&
                        imageDetailBean.getRelated_albums().get(2).getData_info().getImages().size() > 1) {
                    ImageUtils.displayFilletRightTopImage(imageDetailBean.getRelated_albums().get(2).getData_info().getImages().get(1), iv_img10);
                } else {
                    ImageUtils.displayFilletRightTopImage("drawable://" + R.drawable.test_color, iv_img10);
                }
                if (imageDetailBean.getRelated_albums().size() > 2 &&
                        null != imageDetailBean.getRelated_albums().get(2).getData_info() &&
                        null != imageDetailBean.getRelated_albums().get(2).getData_info().getImages() &&
                        imageDetailBean.getRelated_albums().get(2).getData_info().getImages().size() > 2) {
                    ImageUtils.displayFilletLeftBottomImage(imageDetailBean.getRelated_albums().get(2).getData_info().getImages().get(2), iv_img11);
                } else {
                    ImageUtils.displayFilletLeftBottomImage("drawable://" + R.drawable.test_color, iv_img11);
                }
                if (imageDetailBean.getRelated_albums().size() > 2 &&
                        null != imageDetailBean.getRelated_albums().get(2).getData_info() &&
                        null != imageDetailBean.getRelated_albums().get(2).getData_info().getImages() &&
                        imageDetailBean.getRelated_albums().get(2).getData_info().getImages().size() > 3) {
                    ImageUtils.displayFilletRightBottomImage(imageDetailBean.getRelated_albums().get(2).getData_info().getImages().get(3), iv_img12);
                } else {
                    ImageUtils.displayFilletRightBottomImage("drawable://" + R.drawable.test_color, iv_img12);
                }

            } else {
                iv_more_album.setVisibility(View.GONE);
                rl_album_all.setVisibility(View.GONE);
                rl_images_one.setVisibility(View.GONE);
                rl_images_two.setVisibility(View.GONE);
                rl_images_three.setVisibility(View.GONE);
                rl_header1.setVisibility(View.GONE);
                rl_header2.setVisibility(View.GONE);
                rl_header3.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            ifDelect = true;
            mRecyclerView.setVisibility(View.GONE);
            ToastUtils.showCenter(activity, "图片已被删除");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getImageDetail();
        } else if (requestCode == 2) {
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
            if (!mUserInfo.getIfBack()) {
                int top = Math.abs(totalDy);
                if (height_tag != 0 && top >= height_tag) {
                    if (!mUserInfo.getIfBack()) {
                        updateShiBieUI(true);
                    }
                } else if (height_tag != 0 && top < height_tag) {
                    if (view_below_image.getLocalVisibleRect(rect)) {/*rect.contains(ivRect)*/
                        //控件在屏幕可见区域-----显现
                        if (!mUserInfo.getIfBack()) {
                            updateShiBieUI(true);
                        }
                    } else {
                        //控件已不在屏幕可见区域（已滑出屏幕）-----隐去
                        if (!mUserInfo.getIfBack()) {
                            updateShiBieUI(false);
                        }
                    }
                }
                if (mUserInfo.getmItemIdList().size() > 1) {

                    int position = mUserInfo.getCurrentPosition();
                    if (position == mPosotion) {
                        int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                        staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions);
                        if (lastPositions != null && lastPositions.length == 2 && lastPositions[0] == 1 && lastPositions[1] == 1) {
                            if (!mUserInfo.getIfBack()) {
                                mUserInfo.changeTital(true);
                            }
                        } else {
                            if (!mUserInfo.getIfBack()) {
                                mUserInfo.changeTital(false);
                            }
                        }
                    }
                }
                mHandler.postDelayed(this, 100);
            }
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
                case 2:
                    if (imageDetailBean != null) {
                        NewUserInfoFragment newUserInfoFragment = new NewUserInfoFragment(fragmentManager);
                        Bundle bundle = new Bundle();
                        bundle.putString(ClassConstant.LoginSucces.USER_ID, imageDetailBean.getUser_info().getUser_id());
                        newUserInfoFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.id_main, newUserInfoFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                    break;
                case 3:
                    if (imageDetailBean != null && imageDetailBean.getAlbum_info() != null && !TextUtils.isEmpty(imageDetailBean.getAlbum_info().getAlbum_id())) {
                        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
                        NewInspirationDetailsment newInspirationDetailsment = new NewInspirationDetailsment(fragmentManager);
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id", imageDetailBean.getUser_info().getUser_id());
                        bundle.putBoolean("ifHideEdit", true);
                        bundle.putString("album_id", imageDetailBean.getAlbum_info().getAlbum_id());
//                        bundle.putString("show_type",  imageDetailBean.getAlbum_info().getShow_type());
                        newInspirationDetailsment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.id_main, newInspirationDetailsment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                    break;
            }
        }
    };

    @Override
    public void onJumpJuBao() {
        juBaoPopWin.dismiss();
        //友盟统计
        HashMap<String, String> map6 = new HashMap<String, String>();
        map6.put("evenname", "举报图片");
        map6.put("even", " 举报不良图片");
        MobclickAgent.onEvent(activity, "shijian32", map6);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory(" 举报不良图片")  //事件类别
                .setAction("举报图片")      //事件操作
                .build());
        Intent intent = new Intent(activity, JuBaoActivity.class);
        intent.putExtra("item_id", item_id);
        startActivity(intent);

    }

    public interface UserInfo {
        void getUserInfo(ImageDetailBean imageDetailBean);

        List<String> getmItemIdList();

        int getCurrentPosition();

        String getCurrentItemId();

        boolean getIfBack();

        void setTitalClickAble(boolean boo);

        void setViewPagerScrollAble(boolean boo);

        void changeTital(boolean boo);
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
            MobclickAgent.onEvent(activity, "jtaction7", map1);
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
            MobclickAgent.onEvent(activity, "jtaction7", map1);
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
            MobclickAgent.onEvent(activity, "jtaction7", map1);
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
            MobclickAgent.onEvent(activity, "jtaction7", map1);
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

    private int mPosotion;
    private String mUserId;
    private String item_id;
    private UserInfo mUserInfo;
    private ImageDetailBean imageDetailBean;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    //全局
    private TextView tv_lingganji;
    private TextView tv_tital_comment;
    private ImageButton iv_edit_image;
    private ImageButton iv_shared_image;
    private ImageButton iv_more_image;
    private JuBaoPopWin juBaoPopWin;
    private HomeSharedPopWinPublic homeSharedPopWinPublic;
    private HRecyclerView mRecyclerView;
    private ResizeRelativeLayout menu_layout;
    private ImageView bt_shiwu;
    //header
    private View view;
    private RelativeLayout rl_shop_tital;
    private TextView tv_shop_price;
    private TextView tv_shop_miaosu;
    private ImageView iv_details_image;
    private View view_below_image;
    private ImageView bt_shiwu2;
    private ShangshabanChangeTextSpaceView tv_details_tital;
    private FlowLayoutBiaoQian fl_tags_jubu;
    private RoundImageView riv_people_header;
    private TextView tv_people_name;
    private TextView tv_details_time;
    private TextView tv_from_where;
    private TextView tv_goto_shop;
    private TextView tv_maybe_like;
    private ImageView iv_more_album;
    private RelativeLayout rl_album_all;
    private RelativeLayout rl_images_one;
    private RelativeLayout rl_images_two;
    private RelativeLayout rl_images_three;
    private RelativeLayout rl_header1;
    private RelativeLayout rl_header2;
    private RelativeLayout rl_header3;
    private RoundImageView riv_header_one;
    private RoundImageView riv_header_two;
    private RoundImageView riv_header_three;
    private ImageView iv_img1;
    private ImageView iv_img2;
    private ImageView iv_img3;
    private ImageView iv_img4;
    private ImageView iv_img5;
    private ImageView iv_img6;
    private ImageView iv_img7;
    private ImageView iv_img8;
    private ImageView iv_img9;
    private ImageView iv_img10;
    private ImageView iv_img11;
    private ImageView iv_img12;
    private int width_Pic;
    private int width_Imgs;
    private TextView tv_pinglun;
    private RelativeLayout rl_pinglun;

    private int height_pic = 0;
    private int height_tag = 0;
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
    private FragmentManager fragmentManager;
}
