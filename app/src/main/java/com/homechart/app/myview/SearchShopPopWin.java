package com.homechart.app.myview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.croplayout.EditPhotoViewMore;
import com.homechart.app.croplayout.EditableImage;
import com.homechart.app.croplayout.handler.OnBoxChangedListener;
import com.homechart.app.croplayout.model.ScalableBox;
import com.homechart.app.home.bean.imagedetail.ImageDetailBean;
import com.homechart.app.home.bean.imagedetail.ShiBieItemBean;
import com.homechart.app.home.bean.searchfservice.SearchSBean;
import com.homechart.app.home.bean.searchfservice.SearchSCateBean;
import com.homechart.app.home.bean.searchfservice.SearchSObjectBean;
import com.homechart.app.home.bean.searchshops.SearchShopsBean;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.visearch.ScrollAwareGridView;
import com.homechart.app.visearch.adapter.HorizontalProductTypeArrayAdapter;
import com.homechart.app.visearch.adapter.MySquareImageAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.widget.HListView;

public class SearchShopPopWin
        extends PopupWindow
        implements View.OnClickListener,
        ScrollAwareGridView.OnDetectScrollListener,
        HorizontalProductTypeArrayAdapter.OnClickText {

    private ImageButton mBack;
    private View view;
    private Context mContext;
    private EditPhotoViewMore editPhotoView;
    private ImageDetailBean mImageDetailBean;
    private int widerImage;
    private int heightImage;
    private int mPosition;
    public Bitmap imageBitMap;
    private EditableImage editableImage;
    private SearchSBean searchSBean;
    private FrameLayout resultGridView;
    private HorizontalProductTypeArrayAdapter horizontalAdapter;
    private HListView categoryListView;
    private ShiBieItemBean shiBieItemBean;
    private ImageView loadingImage;
    private String imageID;
    private String mloc = "";

    public SearchShopPopWin(Context context) {

        this.mContext = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.search_shop_popwindow, null);

        initView();
        initListener();
        initData();

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setOutsideTouchable(false);
        // 设置弹出窗体可点击
        this.setFocusable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        ColorDrawable dw = new ColorDrawable(R.color.touming);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private void initView() {

        resultGridView = (FrameLayout) view.findViewById(R.id.result_grid_view);
        mBack = (ImageButton) view.findViewById(R.id.nav_left_imageButton);
        loadingImage = (ImageView) view.findViewById(R.id.result_loading);
        editPhotoView = (EditPhotoViewMore) view.findViewById(R.id.photoedit_image_view);
        categoryListView = (HListView) view.findViewById(R.id.category_list_view);
    }


    private void initListener() {
        mBack.setOnClickListener(this);
    }


    private void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                if (this.isShowing()) {
                    this.dismiss();
                }
                break;
        }

    }


    private void initImage() {
        //初始化图片及添加选择框
        if (null != mImageDetailBean && mPosition >= 0) {
            editableImage = new EditableImage(mImageDetailBean.getItem_info().getImage().getImg0(), true);
            editPhotoView.setUrlImage(mImageDetailBean.getItem_info().getImage().getImg0());
            editPhotoView.initView(mContext, editableImage, true);

            widerImage = editableImage.getOriginalImage().getWidth();
            heightImage = editableImage.getOriginalImage().getHeight();

            int x1 = (int) (Float.parseFloat(mImageDetailBean.getObject_list().get(mPosition).getObject_info().getX()) * widerImage);
            int y1 = (int) (Float.parseFloat(mImageDetailBean.getObject_list().get(mPosition).getObject_info().getY()) * heightImage);
            int x2 = (int) (Float.parseFloat(mImageDetailBean.getObject_list().get(mPosition).getObject_info().getX()) * widerImage)
                    + (int) (Float.parseFloat(mImageDetailBean.getObject_list().get(mPosition).getObject_info().getWidth()) * widerImage);
            int y2 = (int) (Float.parseFloat(mImageDetailBean.getObject_list().get(mPosition).getObject_info().getY()) * heightImage)
                    + (int) (Float.parseFloat(mImageDetailBean.getObject_list().get(mPosition).getObject_info().getHeight()) * heightImage);
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
            editPhotoView.changeBox(scalableBox);
        }
    }


    public void setImageData(ImageDetailBean imageDetailBean, int position) {
        this.mImageDetailBean = imageDetailBean;
        this.mPosition = position;
        shiBieItemBean = mImageDetailBean.getObject_list().get(position);
        imageID = mImageDetailBean.getItem_info().getImage().getImage_id();
        initImage();
        searchByImageId(imageID);
    }

    //通过image_id直接识别网络图片
    private void searchByImageId(String imageid) {
        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                            searchSBean = GsonUtil.jsonToBean(data_msg, SearchSBean.class);
                            if (null != searchSBean.getObject_list() && searchSBean.getObject_list().size() > 0) {
                                Message message = new Message();
                                message.arg1 = 0;
                                mHandler.sendMessage(message);
                            } else {
                                Message message = new Message();
                                message.arg1 = 1;
                                mHandler.sendMessage(message);
                            }
                        } else {
                            ToastUtils.showCenter(mContext, error_msg);
                        }
                    } else {
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().searchByImageId(imageid, callback);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listType = searchSBean.getCategory_list();
            listSearch = searchSBean.getObject_list();
            selectedType = mImageDetailBean.getObject_list().get(mPosition).getObject_info().getCategory_name();
            selectedTypeID = mImageDetailBean.getObject_list().get(mPosition).getObject_info().getCategory_id();
            if (listType != null && listType.size() > 0) {
                for (int i = 0; i < listType.size(); i++) {
                    strTypeName.add(listType.get(i).getCategory_info().getCategory_name());
                    strTypeID.add(listType.get(i).getCategory_info().getCategory_id());
                }
            }
            if (gridViewLayout == null) {
                gridViewLayout = new ScrollAwareGridView(mContext);
                gridViewLayout.setOnDetectScrollListener(SearchShopPopWin.this);
                gridViewLayout.setNumColumns(2);
            }
            gridViewLayout.invalidate();
            resultGridView.removeAllViews();
            resultGridView.addView(gridViewLayout);
            //搜索分类列表
            if (strTypeName.size() > 0) {
                horizontalAdapter = new HorizontalProductTypeArrayAdapter((Activity) mContext, strTypeName,SearchShopPopWin.this);
                horizontalAdapter.setSelected(strTypeName.indexOf(selectedType));
                categoryListView.setAdapter(horizontalAdapter);
                int position = strTypeName.indexOf(selectedType);
                categoryListView.setSelection(position);
            }
            //搜索相似商品
            mloc = shiBieItemBean.getObject_info().getX() + "-" +
                    shiBieItemBean.getObject_info().getY() + "-" +
                    shiBieItemBean.getObject_info().getWidth() + "-" +
                    shiBieItemBean.getObject_info().getHeight();
            searchShopImage(mloc);
            // 搜索框监听
            editPhotoView.setOnBoxChangedListener(new OnBoxChangedListener() {
                @Override
                public void onChanged(int x1, int y1, int x2, int y2) {
                    if (loadingImage != null) {
                        loadingImage.setVisibility(View.GONE);
                    }
                    mloc = x1 * 1.000000 / widerImage + "-" +
                            y1 * 1.000000 / heightImage + "-" +
                            (x2 - x1) + "-" +
                            (y2 - y1);
                    searchShopImage(mloc);
                }
            });
//            SearchShopPopWin.this.categoryListView.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> parent, View view, int position, long id) {
//
//                }
//            });
        }
    };

    private void searchShopImage(String loc) {
        //start scan
        AnimationDrawable anim = (AnimationDrawable) loadingImage.getDrawable();
        if (anim != null) {
            anim.start();
        }
        if (listSearch != null && listSearch.size() > 0) {
            loadingImage.setVisibility(View.VISIBLE);
            OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    ToastUtils.showCenter(mContext, "搜索同款失败！");
                    if (loadingImage != null) {
                        loadingImage.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                        String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                        String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                        if (error_code == 0) {
                            if (loadingImage != null) {
                                loadingImage.setVisibility(View.GONE);
                            }
                            final SearchShopsBean searchShopsBean = GsonUtil.jsonToBean(data_msg, SearchShopsBean.class);
                            if (null != gridViewLayout) {
                                if (searchShopsBean != null && searchShopsBean.getItem_list() != null && searchShopsBean.getItem_list().size() > 0) {
                                    try {
                                        gridViewLayout.setAdapter(new MySquareImageAdapter((Activity) mContext, searchShopsBean.getItem_list()));
                                    }catch (Exception e){
                                    }
                                }
                                gridViewLayout.invalidate();
                                gridViewLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        ToastUtils.showCenter(mContext,""+position);
                                    }
                                });
                            }
                        } else {
                            ToastUtils.showCenter(mContext, error_msg);
                        }
                    } catch (JSONException e) {
                    }
                }
            };
            MyHttpManager.getInstance().searchShopImage(imageID, loc, selectedTypeID + "", callBack);
        } else {
        }
    }

    @Override
    public void onUpScrolling() {

    }

    @Override
    public void onDownScrolling() {

    }

    @Override
    public void onTopReached() {

    }

    @Override
    public void onBottomReached() {

    }

    @Override
    public void onClickTextItem(int position) {
        selectedType = strTypeName.get(position);
        selectedTypeID = strTypeID.get(position)+"";
        loadingImage.setVisibility(View.VISIBLE);
        searchShopImage(mloc);
        horizontalAdapter.setSelected(position);
    }

    private List<SearchSCateBean> listType;
    private List<SearchSObjectBean> listSearch;
    private ScrollAwareGridView gridViewLayout;
    private List<String> strTypeName = new ArrayList<>();
    private List<Integer> strTypeID = new ArrayList<>();
    private String selectedType;
    private String selectedTypeID;

}
