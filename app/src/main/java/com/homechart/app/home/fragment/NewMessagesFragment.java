package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.msgdingyue.DingYueItemBean;
import com.homechart.app.home.bean.msgdingyue.MsgDingYue;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.anims.animators.LandingAnimator;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class NewMessagesFragment
        extends BaseFragment
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener,
        CommonAdapter.OnItemClickListener {


    private ImageButton mIBBack;
    private TextView mTVTital;
    private HRecyclerView mRecyclerView;
    private List<DingYueItemBean> mListData = new ArrayList<>();
    private MultiItemCommonAdapter<DingYueItemBean> mAdapter;
    private LoadMoreFooterView mLoadMoreFooterView;
    private int page_num = 1;
    private String n = "20";//返回数据条数，默认20
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int width_Pic_List;
    private RelativeLayout rl_no_data;
    private String mUserId;
    private FragmentManager fragmentManager;
    private BackMessage mBackMessage;

    public NewMessagesFragment() {
    }

    public NewMessagesFragment(FragmentManager fragmentManager, BackMessage backMessage) {
        this.fragmentManager = fragmentManager;
        this.mBackMessage = backMessage;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_new_messges;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initView() {

        rl_no_data = (RelativeLayout) rootView.findViewById(R.id.rl_no_data);
        mIBBack = (ImageButton) rootView.findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) rootView.findViewById(R.id.tv_tital_comment);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_pic);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mIBBack.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        width_Pic_List = PublicUtils.getScreenWidth(activity) - UIUtils.getDimens(R.dimen.font_30);
        mTVTital.setText("消息");
        buildRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                mBackMessage.clickBackMessage();
                fragmentManager.popBackStack();
                break;
        }
    }

    private void buildRecyclerView() {

        MultiItemTypeSupport<DingYueItemBean> support = new MultiItemTypeSupport<DingYueItemBean>() {
            @Override
            public int getLayoutId(int itemType) {

                if (itemType == 0) {
                    return R.layout.item_message_activity;
                } else if (itemType == 1) {
                    return R.layout.item_message_tips;
                } else if (itemType == 2) {
                    return R.layout.item_message_guanzhu;
                } else if (itemType == 3) {
                    return R.layout.item_message_shoucang;
                } else if (itemType == 4) {
                    return R.layout.item_message_shoucang;
                } else if (itemType == 5) {
                    return R.layout.item_message_dingyue;
                } else if (itemType == 6) {
                    return R.layout.item_message_dingyue1;
                } else {
                    return R.layout.item_message_shoucang;
                }
            }

            @Override
            public int getItemViewType(int position, DingYueItemBean itemMessageBean) {

                if (itemMessageBean.getNotice_class().equals("system")) {//系统消息
                    if (itemMessageBean.getObject_type().equals("activity")) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else if (itemMessageBean.getNotice_class().equals("follow")) {//关注消息
                    return 2;
                } else if (itemMessageBean.getNotice_class().equals("comment")) {//评论消息
                    return 3;
                } else if (itemMessageBean.getNotice_class().equals("collect")) {//收藏消息
                    return 4;
                } else if (itemMessageBean.getNotice_class().equals("addToAlbum")) {//加入灵感辑消息
                    return 5;
                } else if (itemMessageBean.getNotice_class().equals("albumUpdate")) {//专辑更新消息
                    return 6;
                } else {
                    return 6;
                }
            }
        };
        mAdapter = new MultiItemCommonAdapter<DingYueItemBean>(activity, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {
                if (getItemViewType(position) == 0) {

                    ((TextView) holder.getView(R.id.tv_activity_tital)).setText(mListData.get(position).getContent());
                    ((TextView) holder.getView(R.id.tv_activity_time)).setText(mListData.get(position).getAdd_time());
                    ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_activity_image).getLayoutParams();
                    layoutParams.height = (int) (width_Pic_List / 2.03);
                    holder.getView(R.id.iv_activity_image).setLayoutParams(layoutParams);
                    ImageUtils.displayFilletImage(mListData.get(position).getImage().getImg0(), (ImageView) holder.getView(R.id.iv_activity_image));

                } else if (getItemViewType(position) == 1) {

                    ((TextView) holder.getView(R.id.tv_tips_tital)).setText(mListData.get(position).getContent());
                    ((TextView) holder.getView(R.id.tv_tips_time)).setText(mListData.get(position).getAdd_time());
                    ImageUtils.displayFilletImage(mListData.get(position).getImage().getImg0(), (ImageView) holder.getView(R.id.iv_activity_image));

                } else if (getItemViewType(position) == 2) {

                    ImageUtils.displayRoundImage(mListData.get(position).getUser_info().getAvatar(), (RoundImageView) holder.getView(R.id.riv_header));
                    ((TextView) holder.getView(R.id.tv_content)).setText(mListData.get(position).getUser_info().getNickname() + mListData.get(position).getContent());
                    ((TextView) holder.getView(R.id.tv_time)).setText(mListData.get(position).getAdd_time());
                    holder.getView(R.id.riv_header).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            NewUserInfoFragment newUserInfoFragment = new NewUserInfoFragment(fragmentManager);
                            Bundle bundle = new Bundle();
                            bundle.putString(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                            newUserInfoFragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.id_main, newUserInfoFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });

                } else if (getItemViewType(position) == 3) {

                    ImageUtils.displayRoundImage(mListData.get(position).getUser_info().getAvatar(), (RoundImageView) holder.getView(R.id.riv_header));
                    ImageUtils.disRectangleImage(mListData.get(position).getImage().getImg0(), (ImageView) holder.getView(R.id.iv_imageview));
                    ((TextView) holder.getView(R.id.tv_name)).setText(mListData.get(position).getUser_info().getNickname());
                    ((TextView) holder.getView(R.id.tv_content)).setText(mListData.get(position).getContent());
                    ((TextView) holder.getView(R.id.tv_time)).setText(mListData.get(position).getAdd_time());
                    holder.getView(R.id.riv_header).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NewUserInfoFragment newUserInfoFragment = new NewUserInfoFragment(fragmentManager);
                            Bundle bundle = new Bundle();
                            bundle.putString(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                            newUserInfoFragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.id_main, newUserInfoFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });
                } else if (getItemViewType(position) == 4) {

                    ImageUtils.displayRoundImage(mListData.get(position).getUser_info().getAvatar(), (RoundImageView) holder.getView(R.id.riv_header));
                    ImageUtils.disRectangleImage(mListData.get(position).getImage().getImg0(), (ImageView) holder.getView(R.id.iv_imageview));
                    ((TextView) holder.getView(R.id.tv_name)).setText(mListData.get(position).getUser_info().getNickname());
                    ((TextView) holder.getView(R.id.tv_content)).setText(mListData.get(position).getContent());
                    ((TextView) holder.getView(R.id.tv_time)).setText(mListData.get(position).getAdd_time());
                    holder.getView(R.id.riv_header).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NewUserInfoFragment newUserInfoFragment = new NewUserInfoFragment(fragmentManager);
                            Bundle bundle = new Bundle();
                            bundle.putString(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                            newUserInfoFragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.id_main, newUserInfoFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });

                } else if (getItemViewType(position) == 5 || getItemViewType(position) == 6) {
                    ImageUtils.displayRoundImage(mListData.get(position).getUser_info().getAvatar(), (RoundImageView) holder.getView(R.id.riv_header));
                    if (TextUtils.isEmpty(mListData.get(position).getImage().getImg0())) {
                        ImageUtils.disRectangleDefaultImage("", (ImageView) holder.getView(R.id.iv_imageview));
                    } else {
                        ImageUtils.disRectangleImage(mListData.get(position).getImage().getImg0(), (ImageView) holder.getView(R.id.iv_imageview));
                    }
                    ((TextView) holder.getView(R.id.tv_name)).setText(mListData.get(position).getUser_info().getNickname());
                    ((TextView) holder.getView(R.id.tv_content)).setText(mListData.get(position).getContent());
                    ((TextView) holder.getView(R.id.tv_time)).setText(mListData.get(position).getAdd_time());

                    holder.getView(R.id.riv_header).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NewUserInfoFragment newUserInfoFragment = new NewUserInfoFragment(fragmentManager);
                            Bundle bundle = new Bundle();
                            bundle.putString(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                            newUserInfoFragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.id_main, newUserInfoFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });

                }

            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setItemAnimator(new LandingAnimator());
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
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
        ++page_num;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        getListData(LOADMORE_STATUS);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        if (mListData.get(position).getNotice_class().equals("system")) {//系统消息
            if (mListData.get(position).getObject_type().equals("activity")) {
                NewHuoDongDetailsFragment newHuoDongDetailsFragment = new NewHuoDongDetailsFragment(fragmentManager);
                Bundle bundle = new Bundle();
                bundle.putString("activity_id", mListData.get(position).getObject_id());
                newHuoDongDetailsFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.id_main, newHuoDongDetailsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } else if (mListData.get(position).getNotice_class().equals("follow")) {//关注消息
            NewUserInfoFragment newUserInfoFragment = new NewUserInfoFragment(fragmentManager);
            Bundle bundle = new Bundle();
            bundle.putString(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
            newUserInfoFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.id_main, newUserInfoFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (mListData.get(position).getNotice_class().equals("comment")) {//评论消息
            if (mListData.get(position).getObject_type().trim().equals("item")) {//图片

                List<String> item_id_list = new ArrayList<>();
                item_id_list.add(mListData.get(position).getObject_id());
                NewImageDetailsFragment newImageDetailsFragment = new NewImageDetailsFragment(fragmentManager);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item_id", mListData.get(position).getObject_id());
                bundle.putInt("position", 0);
                bundle.putString("type", "single");
                bundle.putSerializable("item_id_list", (Serializable) item_id_list);
                newImageDetailsFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null).replace(R.id.id_main, newImageDetailsFragment);
                fragmentTransaction.commit();
            } else if (mListData.get(position).getObject_type().trim().equals("article")) {//文章
            }
        } else if (mListData.get(position).getNotice_class().equals("collect")) {//收藏消息
            NewInspirationDetailsment newInspirationDetailsment = new NewInspirationDetailsment(fragmentManager);
            Bundle bundle = new Bundle();
            bundle.putString("user_id", mUserId);
            bundle.putBoolean("ifHideEdit", true);
            bundle.putString("album_id", mListData.get(position).getObject_id());
            newInspirationDetailsment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.id_main, newInspirationDetailsment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (mListData.get(position).getNotice_class().equals("addToAlbum") || mListData.get(position).getNotice_class().equals("albumUpdate")) {//加入灵感辑消息
            NewInspirationDetailsment newInspirationDetailsment = new NewInspirationDetailsment(fragmentManager);
            Bundle bundle = new Bundle();
            bundle.putString("user_id", mUserId);
            bundle.putBoolean("ifHideEdit", true);
            bundle.putString("album_id", mListData.get(position).getObject_id());
            newInspirationDetailsment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.id_main, newInspirationDetailsment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }

    private void getListData(final String state) {
        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                --page_num;
                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        MsgDingYue msgDingYue = GsonUtil.jsonToBean(data_msg, MsgDingYue.class);
                        List<DingYueItemBean> list = msgDingYue.getNotice_list();
                        if (null != list && 0 != list.size()) {
                            changeNone(0);
                            updateViewFromData(list, state);
                        } else {
                            changeNone(1);
                            updateViewFromData(null, state);
                        }
                    } else {
                        --page_num;
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                    --page_num;
                    ToastUtils.showCenter(activity, UIUtils.getString(R.string.error_message));
                }
            }
        };
        MyHttpManager.getInstance().allMSGList(page_num, 20, callback);

    }

    private void updateViewFromData(List<DingYueItemBean> listData, String state) {
        switch (state) {
            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData) {
                    mListData.addAll(listData);
                } else {
                    mListData.clear();
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setRefreshing(false);//刷新完毕
                break;
            case LOADMORE_STATUS:
                if (null != listData) {
                    mListData.addAll(listData);
                    mAdapter.notifyData(mListData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                } else {
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }


    private void changeNone(int i) {
        if (i == 0) {
            rl_no_data.setVisibility(View.GONE);
        } else if (i == 1) {
            if (mListData.size() > 0) {
                rl_no_data.setVisibility(View.GONE);
            } else {
                rl_no_data.setVisibility(View.VISIBLE);
            }

        }
    }

    interface BackMessage {
        void clickBackMessage();
    }

}