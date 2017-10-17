package com.homechart.app.home.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.homechart.app.myview.GridSpacingItemDecoration;
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
    private ImageView iv_shoucang_shop;
    private String spu_id;
    private TextView tv_shop_details_tital;
    private RelativeLayout rl_shoucang;
    private TextView tv_tital_comment;
    private TextView tv_num_people;
    private TextView tv_price_two;
    private TextView tv_price_three;
    private TextView tv_buy;
    private View headerView;
    private int widMoreImage;
    private List<String> listUrl = new ArrayList<>();
    private boolean ifShouCang = false;
    private boolean allowClickShouCang = true;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int tag = msg.arg1;
            switch (tag) {
                case 0:
                    if (null != shopDetailsBean) {
                        ViewGroup.LayoutParams layoutParams = iv_shop_image.getLayoutParams();
                        layoutParams.width = PublicUtils.getScreenWidth(ShopDetailActivity.this);
                        layoutParams.height = PublicUtils.getScreenWidth(ShopDetailActivity.this);
                        iv_shop_image.setLayoutParams(layoutParams);
                        ImageUtils.disRectangleImage(shopDetailsBean.getItem_info().getImage().getImg0(), iv_shop_image);
                        getListData();
                        listUrl.clear();
                        listUrl.add(shopDetailsBean.getItem_info().getImage().getImg0());
                        tv_shop_details_tital.setText(shopDetailsBean.getItem_info().getTitle());
                        String price = shopDetailsBean.getItem_info().getPrice().toString().trim();
                        String[] strPrice = price.split("\\.");
                        if (strPrice.length > 1) {
                            tv_price_two.setText(strPrice[0]);
                            tv_price_three.setText("." + strPrice[1]);
                        } else {
                            tv_price_two.setText(shopDetailsBean.getItem_info().getPrice().toString());
                            tv_price_three.setText("");
                        }
                        if (shopDetailsBean.getItem_info().getIs_collected().equals("1")) {//已收藏
                            ifShouCang = true;
                            tv_num_people.setText(shopDetailsBean.getItem_info().getCollect_num());
                            iv_shoucang_shop.setImageResource(R.drawable.xing1);
                        } else {//未收藏
                            ifShouCang = false;
                            tv_num_people.setText(shopDetailsBean.getItem_info().getCollect_num());
                            iv_shoucang_shop.setImageResource(R.drawable.xing);
                        }
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
        iv_shoucang_shop = (ImageView) headerView.findViewById(R.id.iv_shoucang_shop);
        tv_shop_details_tital = (TextView) headerView.findViewById(R.id.tv_shop_details_tital);
        tv_price_two = (TextView) headerView.findViewById(R.id.tv_price_two);
        tv_price_three = (TextView) headerView.findViewById(R.id.tv_price_three);
        tv_num_people = (TextView) headerView.findViewById(R.id.tv_num_people);
        rl_shoucang = (RelativeLayout) headerView.findViewById(R.id.rl_shoucang);
        tv_buy = (TextView) headerView.findViewById(R.id.tv_buy);

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
        tv_buy.setOnClickListener(this);
        rl_shoucang.setOnClickListener(this);
        iv_shop_image.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                this.finish();
                break;
            case R.id.rl_shoucang:
                if (null != shopDetailsBean && allowClickShouCang) {
                    allowClickShouCang = false;
                    if (ifShouCang) {
                        //取消收藏
                        removeShouCang(shopDetailsBean.getItem_info().getSpu_id());
                    } else {
                        //收藏商品
                        addShouCang(shopDetailsBean.getItem_info().getSpu_id());
                    }
                }
                break;
            case R.id.tv_buy:
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
                layoutParams.height = (int) ((widMoreImage * 1.0000) / mListData.get(position).getImage().getRatio());
                iv_imageview.setLayoutParams(layoutParams);
                ImageUtils.displayFilletImage(mListData.get(position).getImage().getImg0(), iv_imageview);
                ((TextView) holder.getView(R.id.tv_shop_item_name)).setText(mListData.get(position).getTitle());
                ((TextView) holder.getView(R.id.tv_price)).setText("¥ " + mListData.get(position).getPrice());
                holder.getView(R.id.rl_item_shop_all).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 跳转到商品详情
                        Intent intent = new Intent(ShopDetailActivity.this, ShopDetailActivity.class);
                        intent.putExtra("spu_id", mListData.get(position).getSpu_id());
                        startActivity(intent);
                    }
                });

            }
        };
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
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
        MyHttpManager.getInstance().getLikeShop(spu_id, "40", callBack);
    }

    private void updateViewFromData(List<ShopDetailsItemInfoBean> item_list) {
        mListData.addAll(item_list);
        mAdapter.notifyData(mListData);
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
    }

    private void removeShouCang(String spu_id) {
        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onResponse(String response) {
                allowClickShouCang = true;
                try {
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                        String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                        String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                        if (error_code == 0) {
                            ifShouCang = false;
                            int num = Integer.parseInt(tv_num_people.getText().toString().trim());
                            tv_num_people.setText(--num + "");
                            iv_shoucang_shop.setImageResource(R.drawable.xing);
                            ToastUtils.showCenter(ShopDetailActivity.this, "取消收藏成功！");
                        } else {
                            ToastUtils.showCenter(ShopDetailActivity.this, error_msg);
                        }
                    } else {
                        ToastUtils.showCenter(ShopDetailActivity.this, "取消收藏失败！");
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ShopDetailActivity.this, "取消收藏失败！");
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                allowClickShouCang = true;
                ToastUtils.showCenter(ShopDetailActivity.this, "取消收藏失败！");
            }
        };
        MyHttpManager.getInstance().deleteShop(spu_id, callback);
    }

    private void addShouCang(String spu_id) {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onResponse(String response) {
                allowClickShouCang = true;
                try {
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                        String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                        String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                        if (error_code == 0) {
                            ifShouCang = true;
                            int num = Integer.parseInt(tv_num_people.getText().toString().trim());
                            tv_num_people.setText(++num + "");
                            iv_shoucang_shop.setImageResource(R.drawable.xing1);
                            ToastUtils.showCenter(ShopDetailActivity.this, "商品收藏成功！");
                        } else {
                            ToastUtils.showCenter(ShopDetailActivity.this, error_msg);
                        }
                    } else {
                        ToastUtils.showCenter(ShopDetailActivity.this, "商品收藏失败！");
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ShopDetailActivity.this, "商品收藏失败！");
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                allowClickShouCang = true;
                ToastUtils.showCenter(ShopDetailActivity.this, "商品收藏失败！");
            }
        };
        MyHttpManager.getInstance().shoucangShop(spu_id, callback);
    }

}
