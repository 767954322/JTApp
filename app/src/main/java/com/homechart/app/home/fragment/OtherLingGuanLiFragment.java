package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.activity.UserInfoActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.dingyue.DingYueBean;
import com.homechart.app.home.bean.dingyue.DingYueItemBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.ui.activity.InspirationDetailActivity;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
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
import com.homechart.app.utils.glide.GlideImgManager;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class OtherLingGuanLiFragment
        extends BaseFragment
        implements View.OnClickListener,
        CommonAdapter.OnItemClickListener,
        OnLoadMoreListener,
        OnRefreshListener ,NewInspirationDetailsment.ClickDelete {

    private FragmentManager fragmentManager;
    private TextView tv_delete_icon;
    private RelativeLayout rl_below;
    private RelativeLayout rl_no_data;
    private TextView tv_shoucang_two;
    private HRecyclerView mRecyclerView;
    private String user_id;
    private List<DingYueItemBean> mListData = new ArrayList<>();
    private Map<String, DingYueItemBean> map_delete = new HashMap<>();//选择的唯一标示
    private ChangeUI mChangeUI;
    private CommonAdapter<DingYueItemBean> mAdapter;

    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int position;
    private int page_num = 1;
    private int guanli_tag = 0;//0:未打开管理   1:打开管理
    private int num_checked = 0; //选择的个数
    private LoadMoreFooterView mLoadMoreFooterView;
    private int widthPic;

    public OtherLingGuanLiFragment() {
    }

    public OtherLingGuanLiFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public OtherLingGuanLiFragment(String user_id, ChangeUI changeUI, FragmentManager fragmentManager) {
        this.user_id = user_id;
        this.mChangeUI = changeUI;
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_other_lingganji;
    }

    @Override
    protected void initView() {

        rl_below = (RelativeLayout) rootView.findViewById(R.id.rl_below);
        rl_no_data = (RelativeLayout) rootView.findViewById(R.id.rl_no_data);
        tv_delete_icon = (TextView) rootView.findViewById(R.id.tv_delete_icon);
        tv_shoucang_two = (TextView) rootView.findViewById(R.id.tv_shoucang_two);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_shoucang);

    }

    @Override
    protected void initListener() {
        tv_delete_icon.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        widthPic = (PublicUtils.getScreenWidth(activity) - UIUtils.getDimens(R.dimen.font_30)) / 2;

        buildRecyclerView();

    }

    private void buildRecyclerView() {
        MultiItemTypeSupport<DingYueItemBean> support = new MultiItemTypeSupport<DingYueItemBean>() {
            @Override
            public int getLayoutId(int itemType) {

                if (itemType == 0) {
                    return R.layout.item_dingyue_list_left;
                } else {
                    return R.layout.item_dingyue_list_right;
                }
            }

            @Override
            public int getItemViewType(int position, DingYueItemBean s) {
                if (position % 2 == 0) {
                    return 0;
                } else {
                    return 1;
                }
            }
        };

        mAdapter = new MultiItemCommonAdapter<DingYueItemBean>(activity, mListData, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {

                final String album_id = mListData.get(position).getAlbum_info().getAlbum_id();
                if (guanli_tag == 0) {
                    holder.getView(R.id.cb_check).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.cb_check).setVisibility(View.VISIBLE);

                }
                ((TextView) holder.getView(R.id.tv_item_name)).setText(mListData.get(position).getAlbum_info().getAlbum_name());
                ((TextView) holder.getView(R.id.tv_item_num_pic)).setText(mListData.get(position).getAlbum_info().getItem_num() + "张");
                ((TextView) holder.getView(R.id.tv_item_num_dingyue)).setText(mListData.get(position).getAlbum_info().getSubscribe_num() + "收藏");

                ImageUtils.displayRoundImage(mListData.get(position).getUser_info().getAvatar().getThumb(), (RoundImageView) holder.getView(R.id.riv_header));
                holder.getView(R.id.riv_header).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NewUserInfoFragment newUserInfoFragment = new NewUserInfoFragment(fragmentManager);
                        Bundle bundle = new Bundle();
                        bundle.putString(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                        newUserInfoFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.id_main, newUserInfoFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.iv_shoucang_image).getLayoutParams();
                layoutParams.width = widthPic;
                layoutParams.height = widthPic;
                holder.getView(R.id.iv_shoucang_image).setLayoutParams(layoutParams);
                if (TextUtils.isEmpty(mListData.get(position).getAlbum_info().getCover_image().getImg0())) {
                    GlideImgManager.glideLoader(activity, "", R.drawable.moren, R.drawable.moren, (ImageView) holder.getView(R.id.iv_shoucang_image));

                } else {
                    ImageUtils.displayFilletHalfImage(mListData.get(position).getAlbum_info().getCover_image().getImg0(),
                            (ImageView) holder.getView(R.id.iv_shoucang_image));
                }

                ((CheckBox) holder.getView(R.id.cb_check)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ++num_checked;
                            if (!map_delete.containsKey(album_id)) {
                                map_delete.put(album_id, mListData.get(position));
                            }
                        } else {
                            if (map_delete.containsKey(album_id)) {
                                map_delete.remove(album_id);
                            }
                            --num_checked;
                        }
                        upCheckedStatus();

                        if (map_delete.size() > 0) {
                            selectPic(true);
                        } else {
                            selectPic(false);
                        }
                    }
                });
                holder.getView(R.id.rl_item_dingyue).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (guanli_tag == 0) {//未打开管理

                            NewInspirationDetailsment newInspirationDetailsment = new NewInspirationDetailsment(fragmentManager);
                            newInspirationDetailsment.setReflushList(OtherLingGuanLiFragment.this);
                            Bundle bundle = new Bundle();
                            bundle.putString("user_id", user_id);
                            bundle.putBoolean("ifHideEdit", true);
                            bundle.putString("album_id", mListData.get(position).getAlbum_info().getAlbum_id());
                            bundle.putString("show_type", mListData.get(position).getAlbum_info().getShow_type());
                            newInspirationDetailsment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.id_main, newInspirationDetailsment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else {
                            if (((CheckBox) holder.getView(R.id.cb_check)).isChecked()) {
                                ((CheckBox) holder.getView(R.id.cb_check)).setChecked(false);
                            } else {
                                ((CheckBox) holder.getView(R.id.cb_check)).setChecked(true);
                            }
                        }

                    }
                });

                if (map_delete.containsKey(album_id)) {
                    ((CheckBox) holder.getView(R.id.cb_check)).setChecked(true);
                } else {
                    ((CheckBox) holder.getView(R.id.cb_check)).setChecked(false);
                }

            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_delete_icon:

                if (map_delete.size() > 0) {
                    CustomProgress.show(activity, "正在删除...", false, null);
                    String delete_items = "";
                    for (String key : map_delete.keySet()) {
                        delete_items = delete_items + key + ",";
                    }
                    delete_items = delete_items.substring(0, delete_items.length() - 1);
                    OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            CustomProgress.cancelDialog();
                            ToastUtils.showCenter(activity, getString(R.string.error_delete_shaijia));
                        }

                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                                String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                                String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                                if (error_code == 0) {
                                    for (String key : map_delete.keySet()) {
                                        mListData.remove(map_delete.get(key));
                                    }
                                    map_delete.clear();
                                    upCheckedStatus();
                                    mAdapter.notifyDataSetChanged();
                                    if (mListData == null || mListData.size() == 0) {
                                        handler.sendEmptyMessage(0);
                                    }
                                    CustomProgress.cancelDialog();
                                    ToastUtils.showCenter(activity, getString(R.string.succes_delete_shaijia));
                                } else {
                                    CustomProgress.cancelDialog();
                                    ToastUtils.showCenter(activity, error_msg);
                                }
                            } catch (JSONException e) {
                                CustomProgress.cancelDialog();
                                ToastUtils.showCenter(activity, getString(R.string.error_delete_shaijia));
                            }
                        }
                    };
                    MyHttpManager.getInstance().deleteDingYue(delete_items, callBack);
                }
                break;
        }

    }

    public void upCheckedStatus() {
        tv_shoucang_two.setText(map_delete.size() + "");
        Log.d("test", "个数：" + map_delete.size());
        Log.d("test", "数据：" + map_delete.toString());
    }

    public void clickRightGuanLi() {
        if (mListData != null && mListData.size() > 0) {
            if (guanli_tag == 0) {
                //打开管理
                mChangeUI.ifShowDelete(true);
                map_delete.clear();
                guanli_tag = 1;
                num_checked = 0;
                tv_shoucang_two.setText(num_checked + "");
                rl_below.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            } else {
                //关闭管理
                mChangeUI.ifShowDelete(false);
                map_delete.clear();
                guanli_tag = 0;
                rl_below.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            ToastUtils.showCenter(activity, "先去订阅一些专辑吧");
        }
    }

    public boolean ifHasData() {
        if (mListData != null && mListData.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //关闭管理
            rl_no_data.setVisibility(View.VISIBLE);
            map_delete.clear();
            rl_below.setVisibility(View.GONE);
            mChangeUI.ifShowDelete(false);
        }
    };

    @Override
    public void onRefresh() {
        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getListData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getListData(LOADMORE_STATUS);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void clickDelete() {
        onRefresh();
    }

    public interface ChangeUI {
        public void ifShowDelete(boolean bool);
    }

    private void getListData(final String state) {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                ToastUtils.showCenter(activity, UIUtils.getString(R.string.error_shoucang));
                if (state.equals(LOADMORE_STATUS)) {
                    --page_num;
                } else {
                    page_num = 1;
                }
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
                            DingYueBean dingYueBean = GsonUtil.jsonToBean(data_msg, DingYueBean.class);
                            if (null != dingYueBean.getAlbum_list() && 0 != dingYueBean.getAlbum_list().size()) {
                                changeNone(0);
                                updateViewFromData(dingYueBean.getAlbum_list(), state);
                            } else {
                                changeNone(1);
                                updateViewFromData(null, state);
                            }
                        } else {
                            ToastUtils.showCenter(activity, error_msg);
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
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(activity, UIUtils.getString(R.string.error_shoucang));
                }
            }
        };
        MyHttpManager.getInstance().getDingYueList(user_id, (page_num - 1) * 20, "20", callback);
    }

    private void updateViewFromData(List<DingYueItemBean> listData, String state) {

        switch (state) {

            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData) {
                    rl_no_data.setVisibility(View.GONE);
                    mListData.addAll(listData);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setRefreshing(false);//刷新完毕
                } else {
                    page_num = 1;
                    mListData.clear();
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setRefreshing(false);//刷新完毕
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                }
                break;

            case LOADMORE_STATUS:
                if (null != listData) {
                    rl_no_data.setVisibility(View.GONE);
                    position = mListData.size();
                    mListData.addAll(listData);
                    mAdapter.notifyItem(position, mListData, listData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                } else {
                    --page_num;
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


    private void selectPic(boolean boo) {

        if (boo) {
            tv_delete_icon.setTextColor(UIUtils.getColor(R.color.bg_e79056));
            tv_delete_icon.setBackgroundResource(R.drawable.bg_inspiration_delete);
        } else {
            tv_delete_icon.setTextColor(UIUtils.getColor(R.color.bg_fff));
            tv_delete_icon.setBackgroundResource(R.drawable.bg_inspiration_default);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 2) {
            onRefresh();
        }
    }

}