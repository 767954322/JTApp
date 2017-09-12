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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.homechart.app.R;
import com.homechart.app.croplayout.EditPhotoViewMore;
import com.homechart.app.croplayout.EditableImage;
import com.homechart.app.croplayout.handler.OnBoxChangedListener;
import com.homechart.app.croplayout.model.ScalableBox;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.visearch.adapter.HorizontalProductTypeArrayAdapter;
import com.homechart.app.visearch.adapter.SquareImageAdapter;
import com.homechart.app.visearch.adapter.StrechImageAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.TrackParams;
import com.visenze.visearch.android.UploadSearchParams;
import com.visenze.visearch.android.ViSearch;
import com.visenze.visearch.android.model.Box;
import com.visenze.visearch.android.model.Image;
import com.visenze.visearch.android.model.ImageResult;
import com.visenze.visearch.android.model.ProductType;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import it.sephiroth.android.library.widget.HListView;
import me.littlecheesecake.waterfalllayoutview.MultiColumnListView;
import me.littlecheesecake.waterfalllayoutview.WFAdapterView;

/**
 * Created by yulu on 2/17/15.
 * <p>
 * PhotoEditFragment
 */
public class PhotoEditFragment
        extends BaseFragment
        implements ViSearch.ResultListener,
        View.OnClickListener ,ScrollAwareGridView.OnDetectScrollListener{
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
    private Bitmap bitmap;
    private String imId;
    private List<String> productList;
    private ResultList resultList;
    //ViSearch and Search parameters
    private ViSearch viSearch;
    private EditableImage editableImage;

    private static final String APP_KEY = "2317c981400c6b2ca55114cb6bdfb963";
    private ImageView result_back_button;
    private List<ProductType> cachedProductList;

    /**
     * Constructor: get new instance of PhotoEditFragment
     *
     * @return new instance of PhotoEditFragment
     */
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
                result_back_button.setImageResource(R.drawable.tital_back);
                Log.d("test", "当滑动面板完全滑落时被调用");
            }

            //            当滑动面板滑动完全展开时调用。
            @Override
            public void onPanelExpanded(View panel) {

                result_back_button.setImageResource(R.drawable.tital_back_wight);
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
        //初始化VISearch
        try {
            SearchAPI.initSearchAPI(getActivity(), APP_KEY);
            viSearch = SearchAPI.getInstance();
            viSearch.setListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取数据
        imagePath = ((EditPhotoActivity) getActivity()).getImagePath();
        resultList = ((EditPhotoActivity) getActivity()).getResultList();
        imId = resultList.getImId();

        //查询数据
        cachedProductList = DataHelper.copyProductTypeList(resultList.getProductTypes());
        selectedType = DataHelper.getSelectedProductType(cachedProductList).getType();
        productList = DataHelper.getSupportedTypeList(resultList.getSupportedProductTypeList(), selectedType);

        //更新UI
        horizontalAdapter = new HorizontalProductTypeArrayAdapter(getActivity(), productList);
        horizontalAdapter.setSelected(productList.indexOf(selectedType));
        categoryListView.scrollTo(productList.indexOf(selectedType), 0);
        categoryListView.setAdapter(horizontalAdapter);

        //TODO 搜索分类的监听
        categoryListView.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> adapterView, View view, int i, long l) {
                horizontalAdapter.setSelected(i);

                if (null != editableImage) {
                    ScalableBox b = editableImage.getBox();
                    //set search parameters
                    UploadSearchParams uploadSearchParams = new UploadSearchParams();
                    uploadSearchParams.setImId(imId);
                    uploadSearchParams.setBox(new Box(b.getX1(), b.getY1(), b.getX2(), b.getY2()));

                    //set detection
                    selectedType = productList.get(i);
                    DataHelper.setSearchParams(uploadSearchParams, selectedType);

                    viSearch.cancelSearch();
                    viSearch.uploadSearch(uploadSearchParams);
                    changeUploadUI();
                }
            }
        });
        //TODO 添加搜索框
        if (cachedProductList.size() > 0) {
            Box box = cachedProductList.get(0).getBox();
            editableImage = new EditableImage(imagePath);
            editableImage.setBox(getDetectionBox(box));
            editPhotoView.initView(getActivity(), editableImage, true);
        } else {
            editableImage = new EditableImage(imagePath);
            editPhotoView.initView(getActivity(), editableImage, false);
        }

        //TODO 搜索框监听
        editPhotoView.setOnBoxChangedListener(new OnBoxChangedListener() {
            @Override
            public void onChanged(int x1, int y1, int x2, int y2) {
                //set search parameters
                UploadSearchParams uploadSearchParams = new UploadSearchParams();
                uploadSearchParams.setImId(imId);
                uploadSearchParams.setBox(new Box(x1, y1, x2, y2));

                //set detection
                DataHelper.setSearchParams(uploadSearchParams, selectedType);

                viSearch.cancelSearch();
                viSearch.uploadSearch(uploadSearchParams);
                changeUploadUI();
            }
        });


        //set up result view
        switchView();

//        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);//关闭
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);//打开

        if (cachedProductList.size() > 0 && productList.size() > 0) {
            horizontalAdapter.setSelected(0);
            ScalableBox b = editableImage.getBox();
            //set search parameters
            UploadSearchParams uploadSearchParams = new UploadSearchParams();
            uploadSearchParams.setImId(imId);
            uploadSearchParams.setBox(new Box(b.getX1(), b.getY1(), b.getX2(), b.getY2()));
            //set detection
            selectedType = productList.get(0);
            DataHelper.setSearchParams(uploadSearchParams, selectedType);
            viSearch.cancelSearch();
            viSearch.uploadSearch(uploadSearchParams);
            changeUploadUI();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.result_back_button:
                activity.finish();
//                int childCound = editPhotoView.getChildCount();
//                if (childCound > 1) {
//                    editPhotoView.removeView();
//                } else {
//                    ToastUtils.showCenter(activity, "没覆盖物了！");
//                }
//                if (bol) {
//                    ScalableBox box = getDetectionBox(cachedProductList.get(1).getBox());
//                    editPhotoView.changeBox(box);
//                    bol = false;
//                } else {
//                    ScalableBox box = getDetectionBox(cachedProductList.get(0).getBox());
//                    editPhotoView.changeBox(box);
//                    bol = true;
//                }

                break;
        }

    }


    @Override
    public void onSearchResult(ResultList resultList) {
        Log.d(PHOTO_EDIT_ACTIVITY, "upload ok");
        this.resultList = resultList;

        changeUploadUIBack();
        if (currentLayout == VIEW_LAYOUT.GRID) {
            gridViewLayout.setAdapter(new SquareImageAdapter(getActivity(), resultList.getImageList()));
            gridViewLayout.invalidate();
        } else if (currentLayout == VIEW_LAYOUT.WATERFALL) {
            waterfallViewLayout.setAdapter(new StrechImageAdapter(getActivity(), resultList.getImageList()));
            waterfallViewLayout.invalidate();
        }

        if (resultList.getImId() != null) {
            this.imId = resultList.getImId();
        }

        horizontalAdapter.setSelected(productList.indexOf(selectedType));
        categoryListView.scrollTo(productList.indexOf(selectedType), 0);
    }

    @Override
    public void onSearchError(String errorMessage) {
        Log.d(PHOTO_EDIT_ACTIVITY, "upload error: " + errorMessage);
        if (isAdded()) {
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            changeUploadUIBack();
        }
    }

    @Override
    public void onSearchCanceled() {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //get data from intent
        imagePath = ((EditPhotoActivity) getActivity()).getImagePath();
        String thumbnailPath = ((EditPhotoActivity) getActivity()).getThumbnailPath();

        if (thumbnailPath != null) {
//            bitmap = ImageHelper.getBitmapFromPath(thumbnailPath);
//            queryImage.setImageBitmap(bitmap);
//            editPhotoView
            ImageUtils.displayFilletImage("file:///" + imagePath, queryImage);
        }
        viSearch.setListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (bitmap != null)
            bitmap.recycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);

        if (null != editableImage && editableImage.getOriginalImage() != null)
            editableImage.getOriginalImage().recycle();
    }

    @OnClick(R.id.result_back_button)
    public void closeClicked() {


    }

    @OnClick(R.id.result_query_image)
    public void showCrop() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        result_back_button.setImageResource(R.drawable.tital_back);
    }

    @OnClick(R.id.result_switch_button)
    public void switchView() {
        if (currentLayout == VIEW_LAYOUT.GRID) {
            currentLayout = VIEW_LAYOUT.WATERFALL;
            switchButtonView.setSelected(true);

            if (waterfallViewLayout == null) {
                waterfallViewLayout = new MultiColumnListView(getActivity());
            }

            waterfallViewLayout.setAdapter(new StrechImageAdapter(getActivity(), resultList.getImageList()));
            waterfallViewLayout.setOnItemClickListener(new WFAdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(WFAdapterView<?> parent, View view, int position, long id) {
                    //TODO
                    startDetailActivity(resultList.getImageList().get(position));
                }
            });
            waterfallViewLayout.invalidate();
            resultGridView.removeAllViews();
            resultGridView.addView(waterfallViewLayout);
        } else {
            currentLayout = VIEW_LAYOUT.GRID;
            switchButtonView.setSelected(false);

            if (gridViewLayout == null) {
                gridViewLayout = new ScrollAwareGridView(getActivity());
                gridViewLayout.setOnDetectScrollListener(this);
                gridViewLayout.setNumColumns(2);
            }

            gridViewLayout.setAdapter(new SquareImageAdapter(getActivity(), resultList.getImageList()));
            gridViewLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startDetailActivity(resultList.getImageList().get(position));
                }
            });
            gridViewLayout.invalidate();
            resultGridView.removeAllViews();
            resultGridView.addView(gridViewLayout);
        }
    }

    private void startDetailActivity(ImageResult imageResult) {
        viSearch.track(new TrackParams().setAction("click").setReqid(resultList.getTransId()).setImName(imageResult.getImageName()));

        IntentHelper.addObjectForKey(imageResult.getImageName(), IntentHelper.SEARCH_RESULT_EXTRA);
        IntentHelper.addObjectForKey(imageResult.getImageUrl(), IntentHelper.SEARCH_IMAGE_PATH_EXTRA);

        Intent intent = new Intent(getActivity(), EditPhotoActivity.class);
        intent.putExtra("image_url", imageResult.getImageUrl());
        startActivity(intent);
    }

    //change UI when uploading starts
    private void changeUploadUI() {
        loadingImage.setVisibility(View.VISIBLE);
        ButterKnife.apply(photoUIs, HIDE);

//        //start scan
//        AnimationDrawable anim = (AnimationDrawable) loadingImage.getDrawable();
//        anim.start();
    }

    //When cancel button is clicked, bring UI back
    private void changeUploadUIBack() {
        ButterKnife.apply(photoUIs, SHOW);
        loadingImage.setVisibility(View.GONE);
    }

    private ScalableBox getDetectionBox(Box box) {
        ScalableBox searchBox = new ScalableBox();
        if (box != null) {
            searchBox.setX1(box.getX1());
            searchBox.setX2(box.getX2());
            searchBox.setY1(box.getY1());
            searchBox.setY2(box.getY2());
        }

        return searchBox;
    }

    @Override
    public void onUpScrolling() {
        Log.d("test","onUpScrolling");
    }

    @Override
    public void onDownScrolling() {

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);//打开
        Log.d("test","onDownScrolling");
    }

    @Override
    public void onTopReached() {

//        if(slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED){
//
//        }
//        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);//打开
        Log.d("test","onTopReached");
    }

    @Override
    public void onBottomReached() {

        Log.d("test","onBottomReached");
    }

    private enum VIEW_LAYOUT {
        GRID, WATERFALL
    }


}