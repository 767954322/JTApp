package com.homechart.app.home.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.huodong.HuoDongDataBean;
import com.homechart.app.home.bean.huodong.ItemActivityDataBean;
import com.homechart.app.home.bean.shaijia.ShaiJiaItemBean;
import com.homechart.app.home.bean.shopdetails.MoreShopBean;
import com.homechart.app.home.bean.shopdetails.ShopDetailsBean;
import com.homechart.app.home.bean.shopdetails.ShopDetailsItemInfoBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.imagedetail.ImageDetailsActivity;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gumenghao on 17/9/13.
 */

public class ShopDetailActivity
        extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener {

    private MultiItemCommonAdapter<ShopDetailsItemInfoBean> mAdapter;
    private List<ShopDetailsItemInfoBean> mListData = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;
    private ImageButton nav_left_imageButton;
    private ShopDetailsBean shopDetailsBean;
    private HRecyclerView mRecyclerView;
    private ImageView iv_shop_image;
    private String spu_id;
    private RelativeLayout rl_num_collect;
    private RelativeLayout rl_go_buy;
    private TextView tv_tital_comment;
    private TextView tv_num_collect;
    private View headerView;
    private int widMoreImage;

    private List<String> listUrl = new ArrayList<>();

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int tag = msg.arg1;
            switch (tag) {
                case 0:
                    if (null != shopDetailsBean) {
                        getListData();
                        listUrl.clear();
                        listUrl.add(shopDetailsBean.getItem_info().getImage().getImg0());
                        tv_num_collect.setText(shopDetailsBean.getItem_info().getCollect_num() + "人已加入收藏");
                        ImageUtils.disRectangleImage(shopDetailsBean.getItem_info().getImage().getImg0(), iv_shop_image);
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shop_detail;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        spu_id = getIntent().getStringExtra("spu_id");
    }

    @Override
    protected void initView() {

        headerView = LayoutInflater.from(ShopDetailActivity.this).inflate(R.layout.header_shop_detail, null);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_info);

        iv_shop_image = (ImageView) headerView.findViewById(R.id.iv_shop_image);
        rl_num_collect = (RelativeLayout) headerView.findViewById(R.id.rl_num_collect);
        rl_go_buy = (RelativeLayout) headerView.findViewById(R.id.rl_go_buy);
        tv_num_collect = (TextView) headerView.findViewById(R.id.tv_num_collect);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        tv_tital_comment.setText("商品详情");
        widMoreImage = PublicUtils.getScreenWidth(this) / 2 - UIUtils.getDimens(R.dimen.font_20);
        if (!TextUtils.isEmpty(spu_id)) {
            initRecyclerView();
            getDetailsData();
        } else {
            ToastUtils.showCenter(this, "未找到商品！");
        }

    }

    @Override
    protected void initListener() {
        super.initListener();

        nav_left_imageButton.setOnClickListener(this);
        rl_num_collect.setOnClickListener(this);
        rl_go_buy.setOnClickListener(this);
        iv_shop_image.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                this.finish();
                break;
            case R.id.rl_num_collect:
                break;
            case R.id.rl_go_buy:
                if (null != shopDetailsBean) {
                    Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(shopDetailsBean.getItem_info().getBuy_url()));
                    startActivity(viewIntent);
                } else {
                    ToastUtils.showCenter(this, "商品信息获取失败！");
                }
                break;
            case R.id.iv_shop_image:
                if (null != shopDetailsBean) {
                    Intent intent = new Intent(this, ImageDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("pic_url_list", (Serializable) listUrl);
                    bundle.putInt("click_position", 0);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
        }

    }

    @Override
    public void onLoadMore() {

    }

    private void initRecyclerView() {

        MultiItemTypeSupport<ShopDetailsItemInfoBean> support = new MultiItemTypeSupport<ShopDetailsItemInfoBean>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_shop_morelike;
            }

            @Override
            public int getItemViewType(int position, ShopDetailsItemInfoBean shopDetailsItemInfoBean) {
                return 0;
            }
        };
        mAdapter = new MultiItemCommonAdapter<ShopDetailsItemInfoBean>(this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {

                ImageView iv_imageview = holder.getView(R.id.iv_imageview);
                ViewGroup.LayoutParams layoutParams = iv_imageview.getLayoutParams();
                layoutParams.width = widMoreImage;
                layoutParams.height = widMoreImage;
                iv_imageview.setLayoutParams(layoutParams);
                ImageUtils.displayFilletImage(mListData.get(position).getImage().getImg0(),iv_imageview);

            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new GridLayoutManager(ShopDetailActivity.this, 2));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.addHeaderView(headerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getDetailsData() {
        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {

            @Override
            public void onResponse(String response) {
                try {
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                        String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                        String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                        if (error_code == 0) {
                            shopDetailsBean = GsonUtil.jsonToBean(data_msg, ShopDetailsBean.class);
                            Message message = new Message();
                            message.arg1 = 0;
                            mHandler.sendMessage(message);
                        } else {
                            ToastUtils.showCenter(ShopDetailActivity.this, error_msg);
                        }
                    } else {
                        ToastUtils.showCenter(ShopDetailActivity.this, "商品信息获取失败！");
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ShopDetailActivity.this, "商品信息获取失败！");
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

                ToastUtils.showCenter(ShopDetailActivity.this, "商品信息获取失败！");

            }
        };
        MyHttpManager.getInstance().getShopDetails(spu_id, callback);

    }

    //获取相似商品
    private void getListData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                mAdapter.notifyData(mListData);
                ToastUtils.showCenter(ShopDetailActivity.this, "相似商品信息获取失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        MoreShopBean moreShopBean = GsonUtil.jsonToBean(data_msg, MoreShopBean.class);
                        List<ShopDetailsItemInfoBean> list = moreShopBean.getItem_list();
                        if (list != null && list.size() > 0) {
                            updateViewFromData(list);
                        } else {
                            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                        }
                    } else {
                        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                        ToastUtils.showCenter(ShopDetailActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                    ToastUtils.showCenter(ShopDetailActivity.this, getString(R.string.huodong_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getLikeShop(spu_id, "20", callBack);

    }

    private void updateViewFromData(List<ShopDetailsItemInfoBean> item_list) {
        mListData.addAll(item_list);
        mAdapter.notifyData(mListData);
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
    }

}
