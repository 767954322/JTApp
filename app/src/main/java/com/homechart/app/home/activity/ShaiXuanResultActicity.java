package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.color.ColorBean;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.home.bean.imagedetail.ColorInfoBean;
import com.homechart.app.home.bean.search.SearchDataBean;
import com.homechart.app.home.bean.search.SearchDataColorBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.bean.shaixuan.ShaiXuanBean;
import com.homechart.app.home.bean.shouye.SYDataBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.ui.activity.InspirationSeriesActivity;
import com.homechart.app.myview.FlowLayoutShaiXuan;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.SelectColorPopupWindow;
import com.homechart.app.myview.SelectColorSeCaiWindow;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.visearch.SearchLoadingActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/15.
 */

public class ShaiXuanResultActicity
        extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener,
        SelectColorSeCaiWindow.SureColor {
    private String shaixuan_tag;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private FlowLayoutShaiXuan his_flowLayout;
    private String[] myData;
    private HRecyclerView mRecyclerView;
    private MultiItemCommonAdapter<SearchItemDataBean> mAdapter;
    private int width_Pic_Staggered;
    private int width_Pic_List;
    private LoadMoreFooterView mLoadMoreFooterView;

    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ImageView iv_change_frag;
    //    private View view;
    private View view_flowlayout;
    private ImageView iv_color_icon;
    private TextView tv_color_tital;
    private ColorBean colorBean;
    private SelectColorSeCaiWindow selectColorPopupWindow;
    private int mDownY;
    private float mMoveY;
    private boolean move_tag = true;
    private List<ColorInfoBean> listcolor;

    private List<String> mItemIdList = new ArrayList<>();
    private Map<Integer, ColorItemBean> shaixuan_color;
    private ImageView iv_chongzhi;
    private TextView bt_tag_page_item;
    private boolean islist;
    private boolean loginStatus;
    private String mUserId;

    //取消收藏
    private void removeShouCang(final int position, String item_id) {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ifClickShouCang = true;
                ToastUtils.showCenter(ShaiXuanResultActicity.this, "取消收藏失败");
            }

            @Override
            public void onResponse(String s) {
                ifClickShouCang = true;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ToastUtils.showCenter(ShaiXuanResultActicity.this, "取消收藏成功");
                        mListData.get(position).getItem_info().setIs_collected("0");
                        try {
                            int collect_num = Integer.parseInt(mListData.get(position).getItem_info().getCollect_num().trim());
                            mListData.get(position).getItem_info().setCollect_num(--collect_num + "");
                        } catch (Exception e) {
                        }
                        mAdapter.notifyItemChanged(position);
                    } else {
                        ToastUtils.showCenter(ShaiXuanResultActicity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ShaiXuanResultActicity.this, "取消收藏失败");
                }
            }
        };
        MyHttpManager.getInstance().removeShouCang(item_id, callBack);
    }

    private List<SearchItemDataBean> mListData = new ArrayList<>();
    private List<Integer> mLListDataHeight = new ArrayList<>();
    private List<Integer> mSListDataHeight = new ArrayList<>();
    private List<String> strTuiJian;
    private Map<Integer, ColorItemBean> mSelectListData;
    private boolean curentListTag = false;
    private int page_num = 1;
    private int TYPE_ONE = 1;
    private int TYPE_TWO = 2;
    private int scroll_position = 0;
    private int position;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shuaixuan;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();


        islist = getIntent().getBooleanExtra("islist",true);
        shaixuan_tag = getIntent().getStringExtra("shaixuan_tag");
        colorBean = (ColorBean) getIntent().getSerializableExtra("colorBean");
//        mSelectListData = (Map<Integer, ColorItemBean>) getIntent().getSerializableExtra("mSelectListData");
        listcolor = (List<ColorInfoBean>) getIntent().getSerializableExtra("colorlist");
        mSelectListData = (Map<Integer, ColorItemBean>) getIntent().getSerializableExtra("shaixuan_color");
        if (mSelectListData == null) {
            mSelectListData = new HashMap<>();
        }
    }


    @Override
    protected void initView() {
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_info);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        his_flowLayout = (FlowLayoutShaiXuan) findViewById(R.id.his_flowLayout);
        view_flowlayout = findViewById(R.id.view_flowlayout);
        iv_change_frag = (ImageView) findViewById(R.id.iv_change_frag);

        bt_tag_page_item = (TextView) findViewById(R.id.bt_tag_page_item);
        iv_chongzhi = (ImageView) findViewById(R.id.iv_chongzhi);
        iv_color_icon = (ImageView) findViewById(R.id.iv_color_icon);
        tv_color_tital = (TextView) findViewById(R.id.tv_color_tital);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        iv_change_frag.setOnClickListener(this);

        iv_chongzhi.setOnClickListener(this);
        tv_color_tital.setOnClickListener(this);
        iv_color_icon.setOnClickListener(this);
        bt_tag_page_item.setOnClickListener(this);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
//                        mDownY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        if (move_tag) {
                            mDownY = (int) event.getY();
                            move_tag = false;
                        }
                        mMoveY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        mMoveY = event.getY();
                        move_tag = true;
                        if (Math.abs((mMoveY - mDownY)) > 20) {
                            if (mMoveY > mDownY) {
                                //  gone
                                his_flowLayout.setVisibility(View.VISIBLE);
                                view_flowlayout.setVisibility(View.VISIBLE);
                            } else {
                                // show
                                his_flowLayout.setVisibility(View.GONE);
                                view_flowlayout.setVisibility(View.GONE);
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText(shaixuan_tag);

        if (mSelectListData != null && mSelectListData.size() > 0) {
            iv_chongzhi.setVisibility(View.VISIBLE);
            bt_tag_page_item.setVisibility(View.VISIBLE);
            tv_color_tital.setVisibility(View.GONE);
            for (Integer key : mSelectListData.keySet()) {

                if(mSelectListData.get(key).getColor_value().equalsIgnoreCase("ffffff")){
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setCornerRadius(50);
                    drawable.setColor(Color.parseColor("#"+mSelectListData.get(key).getColor_value()));
                    drawable.setStroke(1,Color.parseColor("#262626"));
                    bt_tag_page_item.setBackgroundDrawable(drawable);
                    bt_tag_page_item.setTextColor(UIUtils.getColor(R.color.bg_262626));
                }else {

                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setCornerRadius(50);
                    drawable.setColor(Color.parseColor("#"+mSelectListData.get(key).getColor_value()));
                    bt_tag_page_item.setBackgroundDrawable(drawable);
                    bt_tag_page_item.setTextColor(UIUtils.getColor(R.color.white));
                }

                bt_tag_page_item.setText(mSelectListData.get(key).getColor_name());
            }
        }

        width_Pic_Staggered = PublicUtils.getScreenWidth(ShaiXuanResultActicity.this) / 2 - UIUtils.getDimens(R.dimen.font_20);
        width_Pic_List = PublicUtils.getScreenWidth(ShaiXuanResultActicity.this) - UIUtils.getDimens(R.dimen.font_14);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        getSearchData();
        buildRecyclerView();
        getColorData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ShaiXuanResultActicity.this.finish();
                break;
            case R.id.iv_change_frag:
                if (curentListTag) {
                    mRecyclerView.setPadding(UIUtils.getDimens(R.dimen.font_6), 0, UIUtils.getDimens(R.dimen.font_6), 0);
                    iv_change_frag.setImageResource(R.drawable.changtu);
                    mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                    curentListTag = false;
                } else {
                    mRecyclerView.setPadding(0, 0, 0, 0);
                    iv_change_frag.setImageResource(R.drawable.pubuliu);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(ShaiXuanResultActicity.this));
                    curentListTag = true;
                }
                break;
            case R.id.iv_chongzhi:
                mSelectListData.clear();
                bt_tag_page_item.setVisibility(View.GONE);
                iv_chongzhi.setVisibility(View.GONE);
                tv_color_tital.setVisibility(View.VISIBLE);
                onRefresh();
                break;
            case R.id.tv_color_tital:
            case R.id.iv_color_icon:
            case R.id.bt_tag_page_item:
                if (null == colorBean) {
                    getColorData();
                    ToastUtils.showCenter(this, "色彩信息获取失败");
                } else {
                    if (selectColorPopupWindow == null) {
                        selectColorPopupWindow = new SelectColorSeCaiWindow(this, this, colorBean, this);
                    }
                    selectColorPopupWindow.setSelectColor(mSelectListData);
                    selectColorPopupWindow.showAtLocation(ShaiXuanResultActicity.this.findViewById(R.id.shaixuan_main),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                            0,
                            0); //设置layout在PopupWindow中显示的位置
                }
                break;
            case R.id.view_pop_top:
            case R.id.view_pop_bottom:
                selectColorPopupWindow.dismiss();
                break;
        }
    }

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

    //获取相关信息
    private void getSearchData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ShaiXuanResultActicity.this, getString(R.string.shaixuan_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        Message msg = new Message();
                        msg.obj = data_msg;
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ShaiXuanResultActicity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ShaiXuanResultActicity.this, getString(R.string.shaixuan_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getTuiJianTagData(shaixuan_tag, callBack);

    }

    private void getColorData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ShaiXuanResultActicity.this, getString(R.string.color_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        Message msg = new Message();
                        msg.obj = data_msg;
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ShaiXuanResultActicity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ShaiXuanResultActicity.this, getString(R.string.color_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getColorListData(callBack);

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String info = (String) msg.obj;

                ShaiXuanBean shaiXuanBean = GsonUtil.jsonToBean(info, ShaiXuanBean.class);
                strTuiJian = shaiXuanBean.getTag_list();
                changeUI();

            } else if (msg.what == 2) {
                String info = (String) msg.obj;
                colorBean = GsonUtil.jsonToBean(info, ColorBean.class);
                Log.d("test", colorBean.toString());
            }
        }
    };

    private void changeUI() {

        if (null != strTuiJian && strTuiJian.size() > 0) {
            his_flowLayout.setVisibility(View.VISIBLE);
            view_flowlayout.setVisibility(View.VISIBLE);
            myData = new String[strTuiJian.size()];
            for (int i = 0; i < strTuiJian.size(); i++) {
                myData[i] = strTuiJian.get(i);
            }
            his_flowLayout.setColorful(false);
            his_flowLayout.setData(myData);
            his_flowLayout.setOnTagClickListener(new FlowLayoutShaiXuan.OnTagClickListener() {
                @Override
                public void TagClick(String text) {

                    // 跳转搜索结果页
                    Intent intent = new Intent(ShaiXuanResultActicity.this, ShaiXuanResultActicity.class);
                    intent.putExtra("shaixuan_tag", text);
                    intent.putExtra("shaixuan_color", (Serializable) mSelectListData);
                    intent.putExtra("islist", curentListTag);
                    startActivity(intent);
                }
            });
        } else {
            his_flowLayout.setVisibility(View.GONE);
            view_flowlayout.setVisibility(View.GONE);
        }
    }

    List<String> listTag = new ArrayList<>();
    private void buildRecyclerView() {

        MultiItemTypeSupport<SearchItemDataBean> support = new MultiItemTypeSupport<SearchItemDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_ONE) {
                    return R.layout.item_list_new;
                } else {
                    return R.layout.item_pubu_new;
                }
            }

            @Override
            public int getItemViewType(int position, SearchItemDataBean s) {
                if (curentListTag) {
                    return TYPE_ONE;
                } else {
                    return TYPE_TWO;
                }
            }
        };

        mAdapter = new MultiItemCommonAdapter<SearchItemDataBean>(ShaiXuanResultActicity.this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {
                scroll_position = position;
                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_imageview_one).getLayoutParams();

//                layoutParams.width = (curentListTag ? width_Pic_List : width_Pic_Staggered);
                layoutParams.height = (curentListTag ? mLListDataHeight.get(position) : mSListDataHeight.get(position));
                holder.getView(R.id.iv_imageview_one).setLayoutParams(layoutParams);
                String nikeName = mListData.get(position).getUser_info().getNickname();
//
//                if (nikeName != null && curentListTag && nikeName.length() > 8) {
//                    nikeName = nikeName.substring(0, 8) + "...";
//                }
//                if (nikeName != null && !curentListTag && nikeName.length() > 5) {
//                    nikeName = nikeName.substring(0, 5) + "...";
//                }
                ((TextView) holder.getView(R.id.tv_name_pic)).setText(nikeName);


                String strTag = "";
                String tag = mListData.get(position).getItem_info().getTag();
                if (!TextUtils.isEmpty(tag)) {
                    String[] str_tag = tag.split(" ");
                    listTag.clear();
                    for (int i = 0; i < str_tag.length; i++) {
                        if (!TextUtils.isEmpty(str_tag[i].trim())) {
                            listTag.add(str_tag[i]);
                        }
                    }
                    for (int i = 0; i < listTag.size(); i++) {
                        strTag = strTag + "#" + listTag.get(i) + "  ";
                    }
                }

                String str = mListData.get(position).getItem_info().getDescription() + " " + "<font color='#464646'>" + strTag + "</font>";
                ((TextView) holder.getView(R.id.tv_image_miaosu)).setText(Html.fromHtml(str));


                if (curentListTag) {
                    ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg0(),
                            (ImageView) holder.getView(R.id.iv_imageview_one));
                } else {
                    ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg1(),
                            (ImageView) holder.getView(R.id.iv_imageview_one));

                }

                ImageUtils.displayFilletImage(mListData.get(position).getUser_info().getAvatar().getBig(),
                        (ImageView) holder.getView(R.id.iv_header_pic));

                holder.getView(R.id.iv_header_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShaiXuanResultActicity.this, UserInfoActivity.class);
                        intent.putExtra(ClassConstant.LoginSucces.USER_ID, mListData.get(position).getUser_info().getUser_id());
                        startActivity(intent);
                    }
                });

                holder.getView(R.id.iv_imageview_one).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //查看单图详情
                        Intent intent = new Intent(ShaiXuanResultActicity.this, ImageDetailScrollActivity.class);
                        intent.putExtra("item_id", mListData.get(position).getItem_info().getItem_id());
                        intent.putExtra("position", position);
                        intent.putExtra("type", "色彩");
                        intent.putExtra("if_click_color", false);
                        intent.putExtra("mSelectListData", (Serializable) mSelectListData);
                        intent.putExtra("shaixuan_tag", "");
                        intent.putExtra("page_num", page_num + 1);
                        intent.putExtra("item_id_list", (Serializable) mItemIdList);
                        startActivity(intent);
                    }
                });

                holder.getView(R.id.iv_if_shoucang).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
                        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
                        if (!loginStatus) {
                            Intent intent = new Intent(ShaiXuanResultActicity.this, LoginActivity.class);
                            startActivityForResult(intent, 1);
                        } else {
                            HashMap<String, String> map4 = new HashMap<String, String>();
                            map4.put("evenname", "加图");
                            map4.put("even", "标签页");
                            MobclickAgent.onEvent(ShaiXuanResultActicity.this, "shijian23", map4);
                            //ga统计
                            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                    .setCategory("标签页")  //事件类别
                                    .setAction("加图")      //事件操作
                                    .build());
                            Intent intent = new Intent(ShaiXuanResultActicity.this, InspirationSeriesActivity.class);
                            intent.putExtra("userid", mUserId);
                            intent.putExtra("image_url", mListData.get(position).getItem_info().getImage().getImg0());
                            intent.putExtra("item_id", mListData.get(position).getItem_info().getItem_id());
                            startActivity(intent);
                        }
                    }
                });

                holder.getView(R.id.iv_shibie_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(ShaiXuanResultActicity.this, SearchLoadingActivity.class);
//                        Intent intent1 = new Intent(ShiBieActivity.this, TestActivity.class);
                        intent1.putExtra("image_url", mListData.get(position).getItem_info().getImage().getImg1());
                        intent1.putExtra("type", "lishi");
                        intent1.putExtra("image_id", mListData.get(position).getItem_info().getImage().getImage_id());
                        intent1.putExtra("image_type", "network");
                        intent1.putExtra("image_ratio", mListData.get(position).getItem_info().getImage().getRatio());
                        startActivity(intent1);
                    }
                });
            }
        };

        if(islist){
            mRecyclerView.setPadding(0, 0, 0, 0);
            iv_change_frag.setImageResource(R.drawable.pubuliu);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(ShaiXuanResultActicity.this));
            curentListTag = true;
        }else {
            mRecyclerView.setPadding(UIUtils.getDimens(R.dimen.font_6), 0, UIUtils.getDimens(R.dimen.font_6), 0);
            iv_change_frag.setImageResource(R.drawable.changtu);
            mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
            curentListTag = false;
        }
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(ShaiXuanResultActicity.this));
        mRecyclerView.setItemAnimator(null);
//        mRecyclerView.addHeaderView(view);

        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    private void getListData(final String state) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                if (state.equals(LOADMORE_STATUS)) {
                    --page_num;
                } else {
                    page_num = 1;
                }
                ToastUtils.showCenter(ShaiXuanResultActicity.this, getString(R.string.search_result_error));

            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        SearchDataBean searchDataBean = GsonUtil.jsonToBean(data_msg, SearchDataBean.class);
                        if (null != searchDataBean.getItem_list() && 0 != searchDataBean.getItem_list().size()) {
                            getHeight(searchDataBean.getItem_list(), state);
                            updateViewFromData(searchDataBean.getItem_list(), state);
                        } else {
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
                        ToastUtils.showCenter(ShaiXuanResultActicity.this, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getSearchList(mSelectListData, "", shaixuan_tag, (page_num - 1) * 20 + "", "20", callBack);

    }


    private void getHeight(List<SearchItemDataBean> item_list, String state) {
        if (state.equals(REFRESH_STATUS)) {
            mLListDataHeight.clear();
            mSListDataHeight.clear();
        }

        if (item_list.size() > 0) {
            for (int i = 0; i < item_list.size(); i++) {
                mLListDataHeight.add(Math.round(width_Pic_List / 1.333333f));
                if (item_list.get(i).getItem_info().getImage().getRatio() == 0) {
                    mSListDataHeight.add(width_Pic_Staggered);
                } else {
                    mSListDataHeight.add(Math.round(width_Pic_Staggered / item_list.get(i).getItem_info().getImage().getRatio()));
                }
            }
        }
    }

    private void updateViewFromData(List<SearchItemDataBean> listData, String state) {

        switch (state) {

            case REFRESH_STATUS:
                mListData.clear();
                mItemIdList.clear();
                if (null != listData && listData.size() > 0) {
                    mListData.addAll(listData);
                    for (int i = 0; i < listData.size(); i++) {
                        mItemIdList.add(listData.get(i).getItem_info().getItem_id());
                    }
                } else {
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                    page_num = 1;
                    mListData.clear();
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setRefreshing(false);//刷新完毕
                break;

            case LOADMORE_STATUS:
                if (null != listData && listData.size() > 0) {
                    position = mListData.size();
                    mListData.addAll(listData);
                    mAdapter.notifyItem(position, mListData, listData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                    for (int i = 0; i < listData.size(); i++) {
                        mItemIdList.add(listData.get(i).getItem_info().getItem_id());
                    }
                } else {
                    --page_num;
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("标签页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("标签页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("标签页");
        MobclickAgent.onPause(this);
    }


    boolean ifClickShouCang = true;
    //收藏或者取消收藏，图片
    public void onShouCang(boolean ifShouCang, int position, SearchItemDataBean searchItemDataBean) {

        if (ifClickShouCang) {
            ifClickShouCang = false;
            if (ifShouCang) {
                //未被收藏，去收藏
                addShouCang(position, searchItemDataBean.getItem_info().getItem_id());
            } else {
                //被收藏，去取消收藏
                removeShouCang(position, searchItemDataBean.getItem_info().getItem_id());
            }
        }

    }

    //收藏
    private void addShouCang(final int position, String item_id) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ifClickShouCang = true;
                ToastUtils.showCenter(ShaiXuanResultActicity.this, "收藏成功");
            }

            @Override
            public void onResponse(String s) {
                ifClickShouCang = true;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ToastUtils.showCenter(ShaiXuanResultActicity.this, "收藏成功");
                        mListData.get(position).getItem_info().setIs_collected("1");
                        try {
                            int collect_num = Integer.parseInt(mListData.get(position).getItem_info().getCollect_num().trim());
                            mListData.get(position).getItem_info().setCollect_num(++collect_num + "");
                        } catch (Exception e) {
                        }
                        mAdapter.notifyItemChanged(position);
                    } else {
                        ToastUtils.showCenter(ShaiXuanResultActicity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ShaiXuanResultActicity.this, "收藏失败");
                }
            }
        };
        MyHttpManager.getInstance().addShouCang(item_id, callBack);
    }
    @Override
    public void qingkong() {

        selectColorPopupWindow.dismiss();
        mSelectListData.clear();
        bt_tag_page_item.setVisibility(View.GONE);
        iv_chongzhi.setVisibility(View.GONE);
        tv_color_tital.setVisibility(View.VISIBLE);

        onRefresh();
    }

    @Override
    public void clickColor(ColorItemBean colorItemBean) {
        if (selectColorPopupWindow != null) {
            selectColorPopupWindow.dismiss();

            if (mSelectListData.get(colorItemBean.getColor_id()) == null) {
                mSelectListData.clear();
                mSelectListData.put(colorItemBean.getColor_id(), colorItemBean);
                if (mSelectListData != null && mSelectListData.size() > 0) {
                    bt_tag_page_item.setVisibility(View.VISIBLE);
                    iv_chongzhi.setVisibility(View.VISIBLE);
                    tv_color_tital.setVisibility(View.GONE);
                    for (Integer key : mSelectListData.keySet()) {
                        bt_tag_page_item.setText(mSelectListData.get(key).getColor_name());
                        if(mSelectListData.get(key).getColor_value().equalsIgnoreCase("ffffff")){
                            GradientDrawable drawable = new GradientDrawable();
                            drawable.setCornerRadius(50);
                            drawable.setColor(Color.parseColor("#"+mSelectListData.get(key).getColor_value()));
                            drawable.setStroke(1,Color.parseColor("#262626"));
                            bt_tag_page_item.setBackgroundDrawable(drawable);
                            bt_tag_page_item.setTextColor(UIUtils.getColor(R.color.bg_262626));
                        }else {

                            GradientDrawable drawable = new GradientDrawable();
                            drawable.setCornerRadius(50);
                            drawable.setColor(Color.parseColor("#"+mSelectListData.get(key).getColor_value()));
                            bt_tag_page_item.setBackgroundDrawable(drawable);
                            bt_tag_page_item.setTextColor(UIUtils.getColor(R.color.white));
                        }
                    }
                }
                onRefresh();
            } else {
                qingkong();
            }
        }
    }
}
