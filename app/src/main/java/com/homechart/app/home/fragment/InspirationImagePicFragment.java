package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.contract.InterDioalod;
import com.homechart.app.commont.utils.MyDialog;
import com.homechart.app.home.activity.ImageDetailLongActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.shoucang.ShouCangBean;
import com.homechart.app.home.bean.shoucang.ShouCangItemBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.common.entity.inspirationpics.InsPicItemBean;
import com.homechart.app.lingganji.common.entity.inspirationpics.InsPicsBean;
import com.homechart.app.lingganji.ui.activity.InspirationDetailActivity;
import com.homechart.app.lingganji.ui.activity.SelectInspirationActivity;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
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
public class InspirationImagePicFragment
        extends BaseFragment
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener,
        InterDioalod {

    private String mUserId;
    private String mAlbumId;
    private FragmentManager fragmentManager;
    private TextView tv_delete_icon;
    private TextView tv_delete_copy;
    private TextView tv_delete_move;
    private RelativeLayout rl_below;
    private RelativeLayout rl_no_data;
    private TextView tv_shoucang_two;
    private HRecyclerView mRecyclerView;
    private List<InsPicItemBean> mListData = new ArrayList<>();
    private Map<String, InsPicItemBean> map_delete = new HashMap<>();//选择的唯一标示
    private CommonAdapter<InsPicItemBean> mAdapter;

    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int position;
    private int page_num = 1;
    private int guanli_tag = 0;//0:未打开管理   1:打开管理
    private int num_checked = 0; //选择的个数
    private LoadMoreFooterView mLoadMoreFooterView;
    private MyDialog mDialog;
    private InputMethodManager imm;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //关闭管理
            rl_no_data.setVisibility(View.VISIBLE);
            map_delete.clear();
            rl_below.setVisibility(View.GONE);
        }
    };

    public InspirationImagePicFragment() {
    }

    public InspirationImagePicFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public InspirationImagePicFragment(String albumId, String userid) {

        this.mAlbumId = albumId;
        this.mUserId = userid;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_inspirationedit_pic;
    }

    @Override
    protected void initView() {

        rl_below = (RelativeLayout) rootView.findViewById(R.id.rl_below);
        rl_no_data = (RelativeLayout) rootView.findViewById(R.id.rl_no_data);
        tv_delete_icon = (TextView) rootView.findViewById(R.id.tv_delete_icon);
        tv_delete_copy = (TextView) rootView.findViewById(R.id.tv_delete_copy);
        tv_delete_move = (TextView) rootView.findViewById(R.id.tv_delete_move);
        tv_shoucang_two = (TextView) rootView.findViewById(R.id.tv_shoucang_two);
        mRecyclerView = (HRecyclerView) rootView.findViewById(R.id.rcy_recyclerview_shoucang);

    }

    @Override
    protected void initListener() {
        tv_delete_icon.setOnClickListener(this);
        tv_delete_copy.setOnClickListener(this);
        tv_delete_move.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mDialog = new MyDialog(activity, "确认要删除图片吗？删了就没了哦？", this);

        tv_shoucang_two.setText(num_checked + "");
        rl_below.setVisibility(View.VISIBLE);
        buildRecyclerView();

    }

    private void buildRecyclerView() {

        mAdapter = new CommonAdapter<InsPicItemBean>(activity, R.layout.item_shoucang, mListData) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {

                final String item_id = mListData.get(position).getItem_info().getItem_id();
                holder.getView(R.id.cb_check).setVisibility(View.VISIBLE);
                ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg0(),
                        (ImageView) holder.getView(R.id.iv_shoucang_image));
                ((CheckBox) holder.getView(R.id.cb_check)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ++num_checked;
                            if (!map_delete.containsKey(item_id)) {
                                map_delete.put(item_id, mListData.get(position));
                            }
                        } else {
                            if (map_delete.containsKey(item_id)) {
                                map_delete.remove(item_id);
                            }
                            --num_checked;
                        }
                        if (map_delete.size() > 0) {
                            selectPic(true);
                        } else {
                            selectPic(false);
                        }
                        upCheckedStatus();
                    }
                });
                holder.getView(R.id.iv_shoucang_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) holder.getView(R.id.cb_check)).isChecked()) {
                            ((CheckBox) holder.getView(R.id.cb_check)).setChecked(false);
                        } else {
                            ((CheckBox) holder.getView(R.id.cb_check)).setChecked(true);
                        }
                    }
                });

                if (map_delete.containsKey(item_id)) {
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
                    //软键盘如果打开的话，关闭软键盘
                    boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                    if (isOpen) {
                        if (activity.getCurrentFocus() != null) {//强制关闭软键盘
                            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                    mDialog.showAtLocation(rootView.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.tv_delete_copy:
                copyPic();
                break;
            case R.id.tv_delete_move:
                movePic();
                break;
        }

    }

    public void upCheckedStatus() {
        tv_shoucang_two.setText(map_delete.size() + "");
    }

    @Override
    public void onRefresh() {
        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getInspirationPicsData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getInspirationPicsData(LOADMORE_STATUS);
    }

    private void getInspirationPicsData(final String state) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if (state.equals(LOADMORE_STATUS)) {
                    --page_num;
                } else {
                    page_num = 1;
                }
                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                ToastUtils.showCenter(activity, "专辑图片列表获取失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        InsPicsBean insPicsBean = GsonUtil.jsonToBean(data_msg, InsPicsBean.class);
                        if (null != insPicsBean.getItem_list() && 0 != insPicsBean.getItem_list().size()) {
                            changeNone(0);
                            updateViewFromData(insPicsBean.getItem_list(), state);
                        } else {
                            changeNone(1);
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
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getUserInspirationPics(mAlbumId, (page_num - 1) * 20 + "", "20", callBack);
    }

    private void updateViewFromData(List<InsPicItemBean> listData, String state) {

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
            tv_delete_copy.setBackgroundResource(R.drawable.bg_inspiration_move);
            tv_delete_move.setBackgroundResource(R.drawable.bg_inspiration_move);
            tv_delete_icon.setBackgroundResource(R.drawable.bg_inspiration_delete);
        } else {
            tv_delete_icon.setTextColor(UIUtils.getColor(R.color.bg_fff));
            tv_delete_copy.setBackgroundResource(R.drawable.bg_inspiration_default);
            tv_delete_move.setBackgroundResource(R.drawable.bg_inspiration_default);
            tv_delete_icon.setBackgroundResource(R.drawable.bg_inspiration_default);
        }

    }

    private void deletePic() {
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
            MyHttpManager.getInstance().removePic(delete_items, callBack);
        }
    }

    private void copyPic() {
        if (map_delete.size() > 0) {

            Intent intent = new Intent(activity, SelectInspirationActivity.class);
            intent.putExtra("userid", mUserId);
            intent.putExtra("type", "copy");
            startActivityForResult(intent, 2);

        }
    }

    private void movePic() {
        if (map_delete.size() > 0) {
            Intent intent = new Intent(activity, SelectInspirationActivity.class);
            intent.putExtra("userid", mUserId);
            intent.putExtra("type", "move");
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 1) {
            String album_id = data.getStringExtra("album_id");
            if (map_delete.size() > 0) {
                CustomProgress.show(activity, "正在移动中...", false, null);
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
                                ToastUtils.showCenter(activity, "移动成功");
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
                MyHttpManager.getInstance().movePic(delete_items, album_id, callBack);
            }
        } else if (requestCode == 2 && resultCode == 2) {
            String album_id = data.getStringExtra("album_id");
            if (map_delete.size() > 0) {
                CustomProgress.show(activity, "正在复制中...", false, null);
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
                                CustomProgress.cancelDialog();
                                ToastUtils.showCenter(activity, "复制成功");
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
                MyHttpManager.getInstance().copyPic(delete_items, album_id, callBack);
            }
        }

    }

    @Override
    public void onQuXiao() {
        mDialog.dismiss();
    }

    @Override
    public void onQueRen() {
        mDialog.dismiss();
        deletePic();
    }
}