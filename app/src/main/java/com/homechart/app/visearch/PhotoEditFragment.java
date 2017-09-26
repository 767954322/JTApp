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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.croplayout.EditPhotoViewMore;
import com.homechart.app.croplayout.EditableImage;
import com.homechart.app.croplayout.handler.OnBoxChangedListener;
import com.homechart.app.croplayout.model.ScalableBox;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.searchfservice.SearchSBean;
import com.homechart.app.home.bean.searchfservice.SearchSCateBean;
import com.homechart.app.home.bean.searchfservice.SearchSObjectBean;
import com.homechart.app.home.bean.searchfservice.SearchSObjectInfoBean;
import com.homechart.app.home.bean.searchshops.SearchShopItemBean;
import com.homechart.app.home.bean.searchshops.SearchShopsBean;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.visearch.adapter.HorizontalProductTypeArrayAdapter;
import com.homechart.app.visearch.adapter.MySquareImageAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.ViSearch;
import com.visenze.visearch.android.model.Box;
import com.visenze.visearch.android.model.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    @InjectView(R.id.result_switch_button)
    ImageView switchButtonView;
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
    private ImageView result_back_button;
    private SearchSBean searchSBean;
    private List<SearchSCateBean> listType;
    private List<SearchSObjectBean> listSearch;
    private List<String> strTypeName = new ArrayList<>();
    private List<Integer> strTypeID = new ArrayList<>();
    private int widerImage;
    private int heightImage;
    private int selectedTypeID;
    private boolean ifFirst = true;

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
        result_back_button = (ImageView) rootView.findViewById(R.id.result_back_button);
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
            }

            //            当滑动面板滑动完全展开时调用。
            @Override
            public void onPanelExpanded(View panel) {

                Log.d("test", "当滑动面板滑动完全展开时调用");
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
        //获取数据
        imagePath = ((EditPhotoActivity) getActivity()).getImagePath();
        searchSBean = ((EditPhotoActivity) getActivity()).getSearchSBean();
        imId = searchSBean.getImage_id();

        listType = searchSBean.getCategory_list();
        listSearch = searchSBean.getObject_list();

        currentLayout = VIEW_LAYOUT.GRID;
        switchButtonView.setSelected(false);

        if (gridViewLayout == null) {
            gridViewLayout = new ScrollAwareGridView(getActivity());
            gridViewLayout.setOnDetectScrollListener(this);
            gridViewLayout.setNumColumns(2);
        }

        gridViewLayout.invalidate();
        resultGridView.removeAllViews();
        resultGridView.addView(gridViewLayout);

        if (listType != null && listType.size() > 0) {
            for (int i = 0; i < listType.size(); i++) {
                strTypeName.add(listType.get(i).getCategory_info().getCategory_name());
                strTypeID.add(listType.get(i).getCategory_info().getCategory_id());
            }
        }
        if (listSearch != null && listSearch.size() > 0) {
            selectedType = listSearch.get(0).getObject_info().getCategory_name();
            selectedTypeID = listSearch.get(0).getObject_info().getCategory_id();
        }
        //更新UI
        if (strTypeName.size() > 0) {
            horizontalAdapter = new HorizontalProductTypeArrayAdapter(getActivity(), strTypeName);
            horizontalAdapter.setSelected(strTypeName.indexOf(selectedType));
            categoryListView.scrollTo(strTypeName.indexOf(selectedType), 0);
            categoryListView.setAdapter(horizontalAdapter);
        }
        //初始化选框
        if (listSearch != null && listSearch.size() > 0) {
            SearchSObjectInfoBean searchSObjectInfoBean = listSearch.get(0).getObject_info();
            editableImage = new EditableImage(imagePath);
            editPhotoView.initView(getActivity(), editableImage, true);
            widerImage = editPhotoView.getEditableImage().getOriginalImage().getWidth();
            heightImage = editPhotoView.getEditableImage().getOriginalImage().getHeight();
            ScalableBox scalableBox = new ScalableBox((int) (searchSObjectInfoBean.getX() * widerImage),
                    (int) (searchSObjectInfoBean.getY() * heightImage),
                    (int) (searchSObjectInfoBean.getX() * widerImage) + (int) (searchSObjectInfoBean.getWidth() * widerImage),
                    (int) (searchSObjectInfoBean.getY() * heightImage) + (int) (searchSObjectInfoBean.getHeight() * heightImage));
            editableImage.setBox(scalableBox);
        } else {
            editableImage = new EditableImage(imagePath);
            editPhotoView.initView(getActivity(), editableImage, false);
        }

        //获取同款商品
        changeUploadUI();
        searchShopImage("");

        //TODO 搜索分类的监听
        categoryListView.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> adapterView, View view, int i, long l) {
                horizontalAdapter.setSelected(i);
                selectedType = strTypeName.get(i);
                selectedTypeID = strTypeID.get(i);
                changeUploadUI();
                searchShopImage("");
                horizontalAdapter.setSelected(i);
            }
        });
        //TODO 搜索框监听
        editPhotoView.setOnBoxChangedListener(new OnBoxChangedListener() {
            @Override
            public void onChanged(int x1, int y1, int x2, int y2) {

                changeUploadUI();
                String mloc = x1 * 1.000000 / widerImage + "-" +
                        y1 * 1.000000 / heightImage + "-" +
                        (x2 - x1) + "-" +
                        (y2 - y1);
                searchShopImage(mloc);
            }
        });
        //start scan
        AnimationDrawable anim = (AnimationDrawable) loadingImage.getDrawable();
        if (anim != null) {
            anim.start();
        }
    }

    private void searchShopImage(String loc) {
        //start scan
        AnimationDrawable anim = (AnimationDrawable) loadingImage.getDrawable();
        if (anim != null) {
            anim.start();
        }
        if (listSearch != null && listSearch.size() > 0) {
            SearchSObjectBean searchSObjectBean = listSearch.get(0);
            String mloc = "";
            if (TextUtils.isEmpty(loc)) {
                mloc = searchSObjectBean.getObject_info().getX() + "-" +
                        searchSObjectBean.getObject_info().getY() + "-" +
                        searchSObjectBean.getObject_info().getWidth() + "-" +
                        searchSObjectBean.getObject_info().getHeight();
            } else {
                mloc = loc;
            }
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
                            gridViewLayout.setAdapter(new MySquareImageAdapter(getActivity(), searchShopsBean.getItem_list()));
                            gridViewLayout.invalidate();
                            gridViewLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    startDetailActivity(searchShopsBean.getItem_list().get(position));
                                }
                            });
                            if (ifFirst) {
//                                horizontalAdapter.setSelected(strTypeName.indexOf(selectedType));
//                                categoryListView.scrollTo(strTypeName.indexOf(selectedType), 0);
                                categoryListView.smoothScrollToPosition(strTypeName.indexOf(selectedType));
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
            MyHttpManager.getInstance().
                    searchShopImage(imId, mloc, selectedTypeID + "", callBack);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.result_back_button:
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
        IntentHelper.addObjectForKey(searchShopItemBean.getItem_info().getSpu_id(), IntentHelper.SEARCH_RESULT_EXTRA);
        IntentHelper.addObjectForKey(searchShopItemBean.getItem_info().getImage().getImg0(), IntentHelper.SEARCH_IMAGE_PATH_EXTRA);
        IntentHelper.addObjectForKey(searchShopItemBean.getItem_info().getBuy_url(), IntentHelper.BUY_URL);
        Intent intent = new Intent(getActivity(), DetailActivity.class);
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
        if(loadingImage != null){
            loadingImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUpScrolling() {
        Log.d("test", "onUpScrolling");
    }

    @Override
    public void onDownScrolling() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);//打开
        Log.d("test", "onDownScrolling");
    }

    @Override
    public void onTopReached() {
        Log.d("test", "onTopReached");
    }

    @Override
    public void onBottomReached() {

        Log.d("test", "onBottomReached");
    }

    @OnClick(R.id.photoedit_rotate_button)
    public void rotateImage() {
        editPhotoView.rotateImageView();
        editableImage.saveEditedImage(imagePath);
        changeUploadUI();
        Image image = new Image(imagePath, Image.ResizeSettings.HIGH);
    }

    private enum VIEW_LAYOUT {
        GRID, WATERFALL
    }

}