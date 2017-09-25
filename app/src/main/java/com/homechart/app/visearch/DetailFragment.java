/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 ViSenze Pte. Ltd.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.homechart.app.visearch;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.activity.ShopDetailActivity;
import com.homechart.app.home.bean.shopdetails.MoreShopBean;
import com.homechart.app.home.bean.shopdetails.ShopDetailsItemInfoBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.visearch.adapter.SquareImageAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.visenze.visearch.android.IdSearchParams;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.TrackParams;
import com.visenze.visearch.android.ViSearch;
import com.visenze.visearch.android.model.ImageResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by visenze on 28/5/15.
 *
 * @author yulu
 */
public class DetailFragment
        extends Fragment
        implements ScrollAwareGridView.OnDetectScrollListener {
    //inject ui
    @InjectView(R.id.detail_image_view)
    ImageView detailImageView;
    @InjectView(R.id.result_query_image)
    ImageView queryImageView;
    @InjectView(R.id.detail_grid_view)
    ScrollAwareGridView similarListView;
    @InjectView(R.id.sliding_detail_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;
    @InjectView(R.id.edit_query_view)
    TextView editQueryView;
    @InjectView(R.id.tv_buy)
    TextView tv_buy;
    @InjectView(R.id.result_loading)
    ImageView loadingImage;
    @InjectView(R.id.detail_im_name_view)
    TextView imNameView;

    private String url;

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_layout, container, false);
        ButterKnife.inject(this, view);


        String imName = ((DetailActivity) getActivity()).getImName();
        url = ((DetailActivity) getActivity()).getUrl();

        updateUI(url, imName);
        editQueryView.setText("");

        startSearch(imName);

        return view;
    }

    @OnClick(R.id.detail_back_button)
    public void clickBack() {
        getActivity().finish();
    }

    @OnClick(R.id.tv_buy)
    public void clickBuy() {
        Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(((DetailActivity) getActivity()).getBuy_url()));
        startActivity(viewIntent);
    }

    @OnClick(R.id.detail_home_button)
    public void returnHome() {
    }

    @OnClick(R.id.detail_image_view)
    public void expandImageOnImage() {
        ImageFragment imageFragment = ImageFragment.newInstance(url);
        imageFragment.show(getFragmentManager().beginTransaction(), "Dialog");
    }

    @OnClick(R.id.result_query_image)
    public void clickQuery() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void startSearch(String imName) {
        loadingImage.setVisibility(View.VISIBLE);
        similarListView.setVisibility(View.GONE);
        //start scan
        AnimationDrawable anim = (AnimationDrawable) loadingImage.getDrawable();
        anim.start();

        getListData(imName);
    }

    private void updateUI(String url, String imName) {
        Picasso.with(getActivity())
                .load(url)
                .placeholder(R.drawable.empty_image)
                .error(R.drawable.empty_image)
                .tag(getActivity())
                .into(detailImageView);

        Picasso.with(getActivity())
                .load(url)
                .placeholder(R.drawable.empty_image)
                .error(R.drawable.empty_image)
                .tag(getActivity())
                .into(queryImageView);
        imNameView.setText(imName);

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        this.url = url;
    }

    @Override
    public void onUpScrolling() {

    }

    @Override
    public void onDownScrolling() {

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);//打开
    }

    @Override
    public void onTopReached() {

    }

    @Override
    public void onBottomReached() {

    }


    //获取相似商品
    private void getListData(String spu_id) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(getActivity(), "相似商品信息获取失败！");
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
                        final List<ShopDetailsItemInfoBean> list = moreShopBean.getItem_list();
                        loadingImage.setVisibility(View.GONE);
                        similarListView.setVisibility(View.VISIBLE);
                        similarListView.setAdapter(new SquareImageAdapter(getActivity(), list));
                        similarListView.setOnDetectScrollListener(DetailFragment.this);
                        similarListView.invalidate();
                        similarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ShopDetailsItemInfoBean shopDetailsItemInfoBean = list.get(position);
                                updateUI(shopDetailsItemInfoBean.getImage().getImg0(), shopDetailsItemInfoBean.getSpu_id());
                                startSearch(shopDetailsItemInfoBean.getSpu_id());
                                ((DetailActivity) getActivity()).setBuy_url(shopDetailsItemInfoBean.getBuy_url());
                            }
                        });
                    } else {
                        ToastUtils.showCenter(getActivity(), error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(getActivity(), getString(R.string.huodong_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getLikeShop(spu_id, "20", callBack);

    }


}