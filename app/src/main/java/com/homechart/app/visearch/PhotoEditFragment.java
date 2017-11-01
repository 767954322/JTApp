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
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.croplayout.EditPhotoViewMore;
import com.homechart.app.croplayout.EditableImage;
import com.homechart.app.croplayout.handler.OnBoxChangedListener;
import com.homechart.app.croplayout.model.ScalableBox;
import com.homechart.app.home.activity.ShopDetailActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.searchfservice.ItemTypeNewBean;
import com.homechart.app.home.bean.searchfservice.SearchSBean;
import com.homechart.app.home.bean.searchfservice.SearchSCateBean;
import com.homechart.app.home.bean.searchfservice.SearchSObjectBean;
import com.homechart.app.home.bean.searchfservice.SearchSObjectInfoBean;
import com.homechart.app.home.bean.searchfservice.TypeNewBean;
import com.homechart.app.home.bean.searchshops.SearchShopItemBean;
import com.homechart.app.home.bean.searchshops.SearchShopsBean;
import com.homechart.app.utils.BitmapUtil;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.visearch.adapter.HorizontalProductTypeArrayAdapter;
import com.homechart.app.visearch.adapter.MySquareImageAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.umeng.analytics.MobclickAgent;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.ViSearch;
import com.visenze.visearch.android.model.Box;
import com.visenze.visearch.android.model.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import it.sephiroth.android.library.widget.HListView;
import me.littlecheesecake.waterfalllayoutview.MultiColumnListView;

/**
 * Created by yulu on 2/17/15.
 * <p>
 * PhotoEditFragment
 */
public class PhotoEditFragment extends BaseFragment
        implements View.OnClickListener, ScrollAwareGridView.OnDetectScrollListener {
    final static ButterKnife.Action<View> SHOW = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.VISIBLE);
        }
    };
    final static ButterKnife.Action<View> HIDE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.GONE);
        }
    };
    private static final String PHOTO_EDIT_ACTIVITY = "PhotoEditActivity";
    //UI element
    @InjectView(R.id.result_loading)
    ImageView loadingImage;
    @InjectView(R.id.message)
    TextView tital;
    @InjectView(R.id.result_query_image)
    ImageView queryImage;
    @InjectViews({R.id.result_grid_view})
    List<View> photoUIs;
    @InjectView(R.id.photoedit_image_view)
    EditPhotoViewMore editPhotoView;
    @InjectView(R.id.result_grid_view)
    FrameLayout resultGridView;
    @InjectView(R.id.category_list_view)
    HListView categoryListView;
    @InjectView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;
    //Dynamic UI elements
    private HorizontalProductTypeArrayAdapter horizontalAdapter;
    private MultiColumnListView waterfallViewLayout;
    private ScrollAwareGridView gridViewLayout;
    private VIEW_LAYOUT currentLayout;
    //parameters passed in from camera activity
    private String imagePath;
    private String selectedType;
    private String imId;
    private EditableImage editableImage;
    private ImageButton result_back_button;
    private SearchSBean searchSBean;
    private List<SearchSObjectBean> listSearch;
    private List<String> strTypeName = new ArrayList<>();
    private List<Integer> strTypeID = new ArrayList<>();
    private int widerImage;
    private int heightImage;
    private int selectedTypeID;
    private boolean ifFirst = true;
    private TextView tv_tital_comment;
    private String network;
    private TextView tv_none_search;
    private String mloc;
    private TypeNewBean typeNewBean;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (typeNewBean != null && typeNewBean.getCategory_list() != null) {
                List<ItemTypeNewBean> typeList = typeNewBean.getCategory_list();
                if (typeList != null && typeList.size() > 0) {
                    for (int i = 0; i < typeList.size(); i++) {
                        if (i == 0) {
                            strTypeName.add("全部");
                            strTypeID.add(-1);
                        }
                        strTypeName.add(typeList.get(i).getCategory_info().getCategory_name());
                        strTypeID.add(typeList.get(i).getCategory_info().getCategory_id());
                    }
                }
                if (listSearch != null && listSearch.size() > 0) {
                    selectedType = listSearch.get(0).getObject_info().getCategory_name();
                    selectedTypeID = listSearch.get(0).getObject_info().getCategory_id();
                } else {
                    selectedType = "全部";
                    selectedTypeID = -1;
                }
                //更新UI
                if (strTypeName.size() > 0) {
                    horizontalAdapter = new HorizontalProductTypeArrayAdapter(getActivity(), strTypeName);
                    horizontalAdapter.setSelected(strTypeName.indexOf(selectedType));
                    categoryListView.setAdapter(horizontalAdapter);
                }
            } else {
                getTypeData();
            }
        }
    };


    public static PhotoEditFragment newInstance() {
        return new PhotoEditFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.result_edit_main;
    }

    @Override
    protected void initView() {
        ButterKnife.inject(this, rootView);
        result_back_button = (ImageButton) rootView.findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) rootView.findViewById(R.id.tv_tital_comment);
        tv_none_search = (TextView) rootView.findViewById(R.id.tv_none_search);
    }

    @Override
    protected void initListener() {
        super.initListener();
        result_back_button.setOnClickListener(this);
        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            //            当滑动窗格的位置更改时调用。
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

                Log.d("test", "当滑动窗格的位置更改时调用");
            }

            //            当滑动面板完全滑落时被调用。
            @Override
            public void onPanelCollapsed(View panel) {
                Log.d("test", "当滑动面板完全滑落时被调用");
                tital.setText("拖动收起");
            }

            //            当滑动面板滑动完全展开时调用。
            @Override
            public void onPanelExpanded(View panel) {

                Log.d("test", "当滑动面板滑动完全展开时调用");
                tital.setText("拖动展开");
            }

            //            当滑动面板固定时调用。
            @Override
            public void onPanelAnchored(View panel) {

                Log.d("test", "当滑动面板固定时调用");
            }

            //            当滑动面板完全隐藏时调用。
            @Override
            public void onPanelHidden(View panel) {

                Log.d("test", "当滑动面板完全隐藏时调用");
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("相似商品");

        //获取数据
        imagePath = ((EditPhotoActivity) getActivity()).getImagePath();
        network = ((EditPhotoActivity) getActivity()).getNetwork();
        searchSBean = ((EditPhotoActivity) getActivity()).getSearchSBean();
        imId = searchSBean.getImage_id();

        listSearch = searchSBean.getObject_list();

        currentLayout = VIEW_LAYOUT.GRID;

        if (gridViewLayout == null) {
            gridViewLayout = new ScrollAwareGridView(getActivity());
            gridViewLayout.setOnDetectScrollListener(this);
            gridViewLayout.setNumColumns(2);
        }

        gridViewLayout.invalidate();
        resultGridView.removeAllViews();
        resultGridView.addView(gridViewLayout);

        getTypeData();

        //初始化选框
        if (listSearch != null && listSearch.size() > 0 && editPhotoView != null) {
            SearchSObjectInfoBean searchSObjectInfoBean = listSearch.get(0).getObject_info();

            if (network.equals("true")) {
                editableImage = new EditableImage(imagePath, true);
            } else {
                editableImage = new EditableImage(imagePath);
                editPhotoView.setUrlImage("file://" + imagePath, true);
            }
            editPhotoView.initView(getActivity(), editableImage, true);

            if (null != editPhotoView.getEditableImage() && null != editPhotoView.getEditableImage().getOriginalImage()) {
                widerImage = editPhotoView.getEditableImage().getOriginalImage().getWidth();
                heightImage = editPhotoView.getEditableImage().getOriginalImage().getHeight();

                int x1 = (int) (searchSObjectInfoBean.getX() * widerImage);
                int y1 = (int) (searchSObjectInfoBean.getY() * heightImage);
                int x2 = (int) (searchSObjectInfoBean.getX() * widerImage) + (int) (searchSObjectInfoBean.getWidth() * widerImage);
                int y2 = (int) (searchSObjectInfoBean.getY() * heightImage) + (int) (searchSObjectInfoBean.getHeight() * heightImage);
                if (Math.abs(x1 - x2) < 120) {
                    int xAdd = (120 - Math.abs(x1 - x2)) / 2;
                    x1 = x1 - xAdd;
                    x2 = x2 + xAdd;
                }
                if (Math.abs(y1 - y2) < 120) {
                    int yAdd = (120 - Math.abs(y1 - y2)) / 2;
                    y1 = y1 - yAdd;
                    y2 = y2 + yAdd;
                }
                ScalableBox scalableBox = new ScalableBox(x1, y1, x2, y2);
                editableImage.setBox(scalableBox);

                SearchSObjectBean searchSObjectBean = listSearch.get(0);
                mloc = searchSObjectBean.getObject_info().getX() + "-" +
                        searchSObjectBean.getObject_info().getY() + "-" +
                        searchSObjectBean.getObject_info().getWidth() + "-" +
                        searchSObjectBean.getObject_info().getHeight();

                searchShopImage(mloc);
            }
        } else {
            if (network.equals("true")) {
                editableImage = new EditableImage(imagePath, true);
            } else {
                editableImage = new EditableImage(imagePath);
                editPhotoView.setUrlImage("file://" + imagePath, true);
            }
            editPhotoView.initView(getActivity(), editableImage, true);
            //整张图片展示框
            int x1 = 0;
            int y1 = 0;
            int x2 = widerImage;
            int y2 = heightImage;
            ScalableBox scalableBox = new ScalableBox(x1, y1, x2, y2);
            editableImage.setBox(scalableBox);

            mloc = 0 + "-" + 0 + "-" + 1 + "-" + 1;
            searchShopImage(mloc);
        }

        //获取同款商品
        changeUploadUI();

        //TODO 搜索分类的监听
        categoryListView.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> adapterView, View view, int i, long l) {
                horizontalAdapter.setSelected(i);
                selectedType = strTypeName.get(i);
                selectedTypeID = strTypeID.get(i);
                changeUploadUI();
                searchShopImage(mloc);
                horizontalAdapter.setSelected(i);
            }
        });
        //TODO 搜索框监听
        editPhotoView.setOnBoxChangedListener(new OnBoxChangedListener() {
            @Override
            public void onChanged(int x1, int y1, int x2, int y2) {
                selectedTypeID = -1;
                selectedType = "全部";
                horizontalAdapter.setSelected(0);
                categoryListView.setSelection(0);
                changeUploadUI();
                mloc = x1 * 1.000000 / widerImage + "-" +
                        y1 * 1.000000 / heightImage + "-" +
                        (x2 - x1) * 1.000000 / widerImage + "-" +
                        (y2 - y1) * 1.000000 / heightImage;
                searchShopImage(mloc);
            }
        });
        //start scan
        AnimationDrawable anim = (AnimationDrawable) loadingImage.getDrawable();
        if (anim != null) {
            anim.start();
        }
    }

    private void getTypeData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, "商品类别获取失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        typeNewBean = GsonUtil.jsonToBean(data_msg, TypeNewBean.class);
                        mHandler.sendEmptyMessage(0);
                    } else {
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getShopTypes(callBack);

    }


    private void searchShopImage(String loc) {
        //start scan
        AnimationDrawable anim = (AnimationDrawable) loadingImage.getDrawable();
        if (anim != null) {
            anim.start();
        }
        tv_none_search.setVisibility(View.GONE);
        loadingImage.setVisibility(View.VISIBLE);
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                changeUploadUIBack();
                ToastUtils.showCenter(activity, "搜索同款失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        changeUploadUIBack();
                        final SearchShopsBean searchShopsBean = GsonUtil.jsonToBean(data_msg, SearchShopsBean.class);
                        if (null != gridViewLayout) {
                            if (searchShopsBean != null && searchShopsBean.getItem_list() != null && searchShopsBean.getItem_list().size() > 0) {
                                try {
                                    gridViewLayout.setAdapter(new MySquareImageAdapter(getActivity(), searchShopsBean.getItem_list()));
                                } catch (Exception e) {
                                }
                            }
                            gridViewLayout.invalidate();
                            gridViewLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    startDetailActivity(searchShopsBean.getItem_list().get(position));
                                }
                            });
                        }
                        if (ifFirst) {
//                                horizontalAdapter.setSelected(strTypeName.indexOf(selectedType));
//                                categoryListView.scrollTo(strTypeName.indexOf(selectedType), 0);
                            if (categoryListView != null) {
                                categoryListView.smoothScrollToPosition(strTypeName.indexOf(selectedType));
                            }
                            ifFirst = false;
                        }

                    } else {
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(activity, getString(R.string.huodong_get_error));
                }
            }
        };
        if (selectedTypeID == -1) {

            MyHttpManager.getInstance().searchShopImage(imId, loc, "", callBack);
        } else {

            MyHttpManager.getInstance().searchShopImage(imId, loc, selectedTypeID + "", callBack);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                activity.finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
        if (null != editableImage && editableImage.getOriginalImage() != null)
            editableImage.getOriginalImage().recycle();
    }

    @OnClick(R.id.result_query_image)
    public void showCrop() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        result_back_button.setImageResource(R.drawable.tital_back);
    }

    private void startDetailActivity(SearchShopItemBean searchShopItemBean) {
        //友盟统计
        HashMap<String, String> map6 = new HashMap<String, String>();
        map6.put("evenname", "查看相似商品");
        map6.put("even", "点击推荐商品的次数");
        MobclickAgent.onEvent(activity, "jtaction57", map6);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("点击推荐商品的次数")  //事件类别
                .setAction("查看相似商品")      //事件操作
                .build());
        // 跳转到商品详情
        Intent intent = new Intent(getActivity(), ShopDetailActivity.class);
        intent.putExtra("spu_id", searchShopItemBean.getItem_info().getSpu_id());
        startActivity(intent);
    }

    //change UI when uploading starts
    private void changeUploadUI() {
        loadingImage.setVisibility(View.VISIBLE);
        ButterKnife.apply(photoUIs, HIDE);
    }

    //When cancel button is clicked, bring UI back
    private void changeUploadUIBack() {
        if (photoUIs != null) {
            ButterKnife.apply(photoUIs, SHOW);
        }
        if (loadingImage != null) {
            loadingImage.setVisibility(View.GONE);
        }
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
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);//关闭
    }

    @Override
    public void onBottomReached() {
    }

    private enum VIEW_LAYOUT {
        GRID, WATERFALL
    }

}