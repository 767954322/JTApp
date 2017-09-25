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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.croplayout.EditPhotoViewMore;
import com.homechart.app.croplayout.EditableImage;
import com.homechart.app.croplayout.handler.OnBoxChangedListener;
import com.homechart.app.croplayout.model.ScalableBox;
import com.homechart.app.home.activity.ShopDetailActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.searchfservice.SearchSBean;
import com.homechart.app.home.bean.searchfservice.SearchSCateBean;
import com.homechart.app.home.bean.searchfservice.SearchSObjectBean;
import com.homechart.app.home.bean.searchfservice.SearchSObjectInfoBean;
import com.homechart.app.home.bean.searchshops.SearchShopsBean;
import com.homechart.app.home.bean.shopdetails.MoreShopBean;
import com.homechart.app.home.bean.shopdetails.ShopDetailsItemInfoBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.visearch.adapter.HorizontalProductTypeArrayAdapter;
import com.homechart.app.visearch.adapter.MySquareImageAdapter;
import com.homechart.app.visearch.adapter.MyStrechImageAdapter;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        View.OnClickListener, ScrollAwareGridView.OnDetectScrollListener {
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
    //ViSearch and Search parameters
    private ViSearch viSearch;
    private EditableImage editableImage;

    private static final String APP_KEY = "2317c981400c6b2ca55114cb6bdfb963";
    private ImageView result_back_button;
    private SearchSBean searchSBean;
    private List<SearchSCateBean> listType;
    private List<SearchSObjectBean> listSearch;
    private List<String> strTypeName = new ArrayList<>();
    private List<Integer> strTypeID = new ArrayList<>();
    private int widerImage;
    private int heightImage;
    private int selectedTypeID;
    ;

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
        gridViewLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startDetailActivity(position);
            }
        });
        gridViewLayout.invalidate();
        resultGridView.removeAllViews();
        resultGridView.addView(gridViewLayout);
        //查询数据
//        cachedProductList = DataHelper.copyProductTypeList(resultList.getProductTypes());
//        selectedType = DataHelper.getSelectedProductType(cachedProductList).getType();
//        productList = DataHelper.getSupportedTypeList(resultList.getSupportedProductTypeList(), selectedType);

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

        //set up result view
//        switchView();

        //TODO 搜索分类的监听
        categoryListView.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> adapterView, View view, int i, long l) {
                horizontalAdapter.setSelected(i);
                selectedType = strTypeName.get(i);
                selectedTypeID = strTypeID.get(i);
                changeUploadUI();
                searchShopImage("");
//                if (null != editableImage) {
//                    ScalableBox b = editableImage.getBox();
//                    UploadSearchParams uploadSearchParams = new UploadSearchParams();
//                    uploadSearchParams.setImId(imId);
//                    uploadSearchParams.setBox(new Box(b.getX1(), b.getY1(), b.getX2(), b.getY2()));
//                    selectedType = strType.get(i);
//
////                    DataHelper.setSearchParams(uploadSearchParams, selectedType);
////                    viSearch.cancelSearch();
////                    viSearch.uploadSearch(uploadSearchParams);
////                    changeUploadUI();
//                }
            }
        });

//        //TODO 添加搜索框
//        if (cachedProductList.size() > 0) {
//            Box box = cachedProductList.get(0).getBox();
//            editableImage = new EditableImage(imagePath);
//            editableImage.setBox(getDetectionBox(box));
//            editPhotoView.initView(getActivity(), editableImage, true);
//        } else {
//            editableImage = new EditableImage(imagePath);
//            editPhotoView.initView(getActivity(), editableImage, false);
//        }

        //TODO 搜索框监听
        editPhotoView.setOnBoxChangedListener(new OnBoxChangedListener() {
            @Override
            public void onChanged(int x1, int y1, int x2, int y2) {

                changeUploadUI();
                String mloc = x1 * 1.000000 / widerImage + "-" +
                        y1* 1.000000 / heightImage + "-" +
                        (x2-x1) + "-" +
                        (y2-y1);
                searchShopImage(mloc);
//                //set search parameters
//                UploadSearchParams uploadSearchParams = new UploadSearchParams();
//                uploadSearchParams.setImId(imId);
//                uploadSearchParams.setBox(new Box(x1, y1, x2, y2));
//
//                //set detection
//                DataHelper.setSearchParams(uploadSearchParams, selectedType);
//
//                viSearch.cancelSearch();
//                viSearch.uploadSearch(uploadSearchParams);
//                changeUploadUI();
            }
        });


//        //set up result view
//        switchView();
//
////        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);//关闭
//        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);//打开
//
//        if (cachedProductList.size() > 0 && productList.size() > 0) {
//            horizontalAdapter.setSelected(0);
//            ScalableBox b = editableImage.getBox();
//            //set search parameters
//            UploadSearchParams uploadSearchParams = new UploadSearchParams();
//            uploadSearchParams.setImId(imId);
//            uploadSearchParams.setBox(new Box(b.getX1(), b.getY1(), b.getX2(), b.getY2()));
//            //set detection
//            selectedType = productList.get(0);
//            DataHelper.setSearchParams(uploadSearchParams, selectedType);
//            viSearch.cancelSearch();
//            viSearch.uploadSearch(uploadSearchParams);
//            changeUploadUI();
//        }
    }

    private void searchShopImage(String loc) {
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
                            SearchShopsBean searchShopsBean = GsonUtil.jsonToBean(data_msg, SearchShopsBean.class);
//                            if (currentLayout == VIEW_LAYOUT.GRID) {
                            gridViewLayout.setAdapter(new MySquareImageAdapter(getActivity(), searchShopsBean.getItem_list()));
                            gridViewLayout.invalidate();
//                            } else if (currentLayout == VIEW_LAYOUT.WATERFALL) {
//                                waterfallViewLayout.setAdapter(new MyStrechImageAdapter(getActivity(), searchShopsBean.getItem_list()));
//                                waterfallViewLayout.invalidate();
//                            }
                            horizontalAdapter.setSelected(strTypeName.indexOf(selectedType));
                            categoryListView.scrollTo(strTypeName.indexOf(selectedType), 0);

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
//        this.resultList = resultList;
//
//        changeUploadUIBack();
//        if (currentLayout == VIEW_LAYOUT.GRID) {
//            gridViewLayout.setAdapter(new SquareImageAdapter(getActivity(), resultList.getImageList()));
//            gridViewLayout.invalidate();
//        } else if (currentLayout == VIEW_LAYOUT.WATERFALL) {
//            waterfallViewLayout.setAdapter(new StrechImageAdapter(getActivity(), resultList.getImageList()));
//            waterfallViewLayout.invalidate();
//        }
//
//        if (resultList.getImId() != null) {
//            this.imId = resultList.getImId();
//        }
//
//        horizontalAdapter.setSelected(productList.indexOf(selectedType));
//        categoryListView.scrollTo(productList.indexOf(selectedType), 0);
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
//        //get data from intent
//        imagePath = ((EditPhotoActivity) getActivity()).getImagePath();
//        String thumbnailPath = ((EditPhotoActivity) getActivity()).getThumbnailPath();
//
//        if (thumbnailPath != null) {
////            bitmap = ImageHelper.getBitmapFromPath(thumbnailPath);
////            queryImage.setImageBitmap(bitmap);
////            editPhotoView
//            ImageUtils.displayFilletImage("file:///" + imagePath, queryImage);
//        }
//        viSearch.setListener(this);
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
//        if (currentLayout == VIEW_LAYOUT.GRID) {
//            currentLayout = VIEW_LAYOUT.WATERFALL;
//            switchButtonView.setSelected(true);
//
//            if (waterfallViewLayout == null) {
//                waterfallViewLayout = new MultiColumnListView(getActivity());
//            }
//
//            waterfallViewLayout.setAdapter(new StrechImageAdapter(getActivity(), resultList.getImageList()));
//            waterfallViewLayout.setOnItemClickListener(new WFAdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(WFAdapterView<?> parent, View view, int position, long id) {
//                    //TODO
//                    startDetailActivity(resultList.getImageList().get(position));
//                }
//            });
//            waterfallViewLayout.invalidate();
//            resultGridView.removeAllViews();
//            resultGridView.addView(waterfallViewLayout);
//        } else {
//            currentLayout = VIEW_LAYOUT.GRID;
//            switchButtonView.setSelected(false);
//
//            if (gridViewLayout == null) {
//                gridViewLayout = new ScrollAwareGridView(getActivity());
//                gridViewLayout.setOnDetectScrollListener(this);
//                gridViewLayout.setNumColumns(2);
//            }
//
//            gridViewLayout.setAdapter(new SquareImageAdapter(getActivity(), resultList.getImageList()));
//            gridViewLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    startDetailActivity(resultList.getImageList().get(position));
//                }
//            });
//            gridViewLayout.invalidate();
//            resultGridView.removeAllViews();
//            resultGridView.addView(gridViewLayout);
//        }
    }

    private void startDetailActivity(ImageResult imageResult) {

//        Map<String, String>  map = imageResult.getFieldList();
//        Log.d("test",map.toString());
//        Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(imageResult.getProduct_url()));
//        startActivity(viewIntent);

//        viSearch.track(new TrackParams().setAction("click").setReqid(resultList.getTransId()).setImName(imageResult.getImageName()));
//        IntentHelper.addObjectForKey(imageResult.getImageName(), IntentHelper.SEARCH_RESULT_EXTRA);
//        IntentHelper.addObjectForKey(imageResult.getImageUrl(), IntentHelper.SEARCH_IMAGE_PATH_EXTRA);
//        IntentHelper.addObjectForKey(imageResult.getProduct_url(), IntentHelper.BUY_URL);
//        Intent intent = new Intent(getActivity(), DetailActivity.class);
//        startActivity(intent);

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
        Log.d("test", "onUpScrolling");
    }

    @Override
    public void onDownScrolling() {

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);//打开
        Log.d("test", "onDownScrolling");
    }

    @Override
    public void onTopReached() {

//        if(slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED){
//
//        }
//        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);//打开
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

//        //set search parameters
//        UploadSearchParams uploadSearchParams = new UploadSearchParams(image);
//        //set detection
//        DataHelper.setSearchParams(uploadSearchParams, selectedType);
//
//        viSearch.cancelSearch();
//        viSearch.uploadSearch(uploadSearchParams);
    }

    private enum VIEW_LAYOUT {
        GRID, WATERFALL
    }


}