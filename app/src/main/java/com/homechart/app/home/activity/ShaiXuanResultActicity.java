package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.FlowLayoutShaiXuan;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.SelectColorPopupWindow;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
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
        SelectColorPopupWindow.SureColor {
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
    private TextView iv_color_tital;
    private ColorBean colorBean;
    private SelectColorPopupWindow selectColorPopupWindow;
    private RoundImageView riv_round_one;
    private RoundImageView riv_round_two;
    private RoundImageView riv_round_three;

    private int mDownY;
    private float mMoveY;
    private boolean move_tag = true;
    private List<ColorInfoBean> listcolor;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shuaixuan;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        shaixuan_tag = getIntent().getStringExtra("shaixuan_tag");
        listcolor = (List<ColorInfoBean>) getIntent().getSerializableExtra("colorlist");

    }

    @Override
    protected void initView() {

//        view = LayoutInflater.from(ShaiXuanResultActicity.this).inflate(R.layout.header_shaixuan, null);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_info);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        his_flowLayout = (FlowLayoutShaiXuan) findViewById(R.id.his_flowLayout);
        iv_color_icon = (ImageView) findViewById(R.id.iv_color_icon);
        iv_color_tital = (TextView) findViewById(R.id.iv_color_tital);
        view_flowlayout = findViewById(R.id.view_flowlayout);
        iv_change_frag = (ImageView) findViewById(R.id.iv_change_frag);
        riv_round_one = (RoundImageView) findViewById(R.id.riv_round_one);
        riv_round_two = (RoundImageView) findViewById(R.id.riv_round_two);
        riv_round_three = (RoundImageView) findViewById(R.id.riv_round_three);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        iv_change_frag.setOnClickListener(this);
        iv_color_icon.setOnClickListener(this);
        iv_color_tital.setOnClickListener(this);
        riv_round_one.setOnClickListener(this);
        riv_round_two.setOnClickListener(this);
        riv_round_three.setOnClickListener(this);

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
        width_Pic_Staggered = PublicUtils.getScreenWidth(ShaiXuanResultActicity.this) / 2 - UIUtils.getDimens(R.dimen.font_20);
        width_Pic_List = PublicUtils.getScreenWidth(ShaiXuanResultActicity.this) - UIUtils.getDimens(R.dimen.font_14);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        getSearchData();
        buildRecyclerView();
        getColorData();
        if (listcolor != null && listcolor.size() > 0) {
            if (mSelectListData == null) {
                mSelectListData = new HashMap<>();
            }
            for (int i = 0; i < listcolor.size(); i++) {
                mSelectListData.put(i, new ColorItemBean(listcolor.get(i).getColor_id(),
                        listcolor.get(i).getColor_value(),
                        listcolor.get(i).getColor_value(),
                        listcolor.get(i).getColor_value()));
            }
            changeColorRound();
        }

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
            case R.id.iv_color_tital:
            case R.id.iv_color_icon:
            case R.id.riv_round_one:
            case R.id.riv_round_two:
            case R.id.riv_round_three:

                if (null == colorBean) {

                    getColorData();
                    ToastUtils.showCenter(ShaiXuanResultActicity.this, "色彩信息获取失败");
                } else {
                    if (selectColorPopupWindow == null) {
                        selectColorPopupWindow = new SelectColorPopupWindow(this, this, colorBean, this);
                    }
//                    selectColorPopupWindow.clearSelect();
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
                    startActivity(intent);
                }
            });
        } else {
            his_flowLayout.setVisibility(View.GONE);
            view_flowlayout.setVisibility(View.GONE);
        }
    }

    private void buildRecyclerView() {

        MultiItemTypeSupport<SearchItemDataBean> support = new MultiItemTypeSupport<SearchItemDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == TYPE_ONE) {
                    return R.layout.item_test_one;
                } else {
                    return R.layout.item_test_pic_pubu;
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

                if (nikeName != null && curentListTag && nikeName.length() > 8) {
                    nikeName = nikeName.substring(0, 8) + "...";
                }
                if (nikeName != null && !curentListTag && nikeName.length() > 5) {
                    nikeName = nikeName.substring(0, 5) + "...";
                }

                ((TextView) holder.getView(R.id.tv_name_pic)).setText(nikeName);
                if (curentListTag) {
                    ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg0(),
                            (ImageView) holder.getView(R.id.iv_imageview_one));
                } else {
                    ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg1(),
                            (ImageView) holder.getView(R.id.iv_imageview_one));

                }
                ImageUtils.displayFilletImage(mListData.get(position).getUser_info().getAvatar().getBig(),
                        (ImageView) holder.getView(R.id.iv_header_pic));


                List<SearchDataColorBean> list_color = mListData.get(position).getColor_info();
                if (null != list_color && list_color.size() == 1) {
                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.GONE);
                    if (list_color.get(0).getColor_value().trim().equalsIgnoreCase("ffffff")) {
                        holder.getView(R.id.iv_color_right).setBackgroundResource(R.drawable.color_line_white);
                    } else {
                        holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                    }
                    if (curentListTag) {
                        holder.getView(R.id.tv_color_tital).setVisibility(View.VISIBLE);
                    }
                } else if (null != mListData.get(position).getColor_info() && mListData.get(position).getColor_info().size() == 2) {

                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.VISIBLE);
                    if (list_color.get(1).getColor_value().trim().equalsIgnoreCase("ffffff")) {
                        holder.getView(R.id.iv_color_right).setBackgroundResource(R.drawable.color_line_white);
                    } else {
                        holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(1).getColor_value()));
                    }
                    if (list_color.get(0).getColor_value().trim().equalsIgnoreCase("ffffff")) {
                        holder.getView(R.id.iv_color_center).setBackgroundResource(R.drawable.color_line_white);
                    } else {
                        holder.getView(R.id.iv_color_center).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                    }
                    if (curentListTag) {
                        holder.getView(R.id.tv_color_tital).setVisibility(View.VISIBLE);
                    }
                } else if (null != mListData.get(position).getColor_info() && mListData.get(position).getColor_info().size() == 3) {
                    holder.getView(R.id.iv_color_right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.VISIBLE);
                    if (list_color.get(2).getColor_value().trim().equalsIgnoreCase("ffffff")) {
                        holder.getView(R.id.iv_color_right).setBackgroundResource(R.drawable.color_line_white);
                    } else {
                        holder.getView(R.id.iv_color_right).setBackgroundColor(Color.parseColor("#" + list_color.get(2).getColor_value()));
                    }
                    if (list_color.get(1).getColor_value().trim().equalsIgnoreCase("ffffff")) {
                        holder.getView(R.id.iv_color_center).setBackgroundResource(R.drawable.color_line_white);
                    } else {
                        holder.getView(R.id.iv_color_center).setBackgroundColor(Color.parseColor("#" + list_color.get(1).getColor_value()));
                    }
                    if (list_color.get(0).getColor_value().trim().equalsIgnoreCase("ffffff")) {
                        holder.getView(R.id.iv_color_left).setBackgroundResource(R.drawable.color_line_white);
                    } else {
                        holder.getView(R.id.iv_color_left).setBackgroundColor(Color.parseColor("#" + list_color.get(0).getColor_value()));
                    }
                    if (curentListTag) {
                        holder.getView(R.id.tv_color_tital).setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.getView(R.id.iv_color_right).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_left).setVisibility(View.GONE);
                    holder.getView(R.id.iv_color_center).setVisibility(View.GONE);
                    if (curentListTag) {
                        holder.getView(R.id.tv_color_tital).setVisibility(View.GONE);
                    }
                }
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
                        Intent intent = new Intent(ShaiXuanResultActicity.this, ImageDetailLongActivity.class);
                        intent.putExtra("item_id", mListData.get(position).getItem_info().getItem_id());
                        startActivity(intent);
                    }
                });

            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(ShaiXuanResultActicity.this));
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
                if (null != listData) {
                    mListData.addAll(listData);
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
                if (null != listData) {
                    position = mListData.size();
                    mListData.addAll(listData);
                    mAdapter.notifyItem(position, mListData, listData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                } else {
                    --page_num;
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

    //选择图片的回调
    @Override
    public void clickSureColor(Map<Integer, ColorItemBean> selectListData) {

        this.mSelectListData = selectListData;
        if (mSelectListData == null) {
            iv_color_tital.setVisibility(View.VISIBLE);
            riv_round_one.setVisibility(View.GONE);
            riv_round_two.setVisibility(View.GONE);
            riv_round_three.setVisibility(View.GONE);
            selectColorPopupWindow.dismiss();
        } else {
            changeColorRound();
        }

        getListData(REFRESH_STATUS);
    }

    List<String> colorList;

    private void changeColorRound() {
        if (colorList == null) {
            colorList = new ArrayList<>();
        } else {
            colorList.clear();
        }
        iv_color_tital.setVisibility(View.GONE);
        for (Integer key : mSelectListData.keySet()) {
            colorList.add(mSelectListData.get(key).getColor_value());
        }
        if (colorList.size() == 1) {
            riv_round_one.setVisibility(View.VISIBLE);
            if (colorList.get(0).equalsIgnoreCase("FFFFFF")) {
                riv_round_one.setBackgroundResource(R.drawable.color_line_white);
            } else {
                riv_round_one.setBackgroundColor(Color.parseColor("#" + colorList.get(0)));
            }

            riv_round_two.setVisibility(View.GONE);
            riv_round_three.setVisibility(View.GONE);
        } else if (colorList.size() == 2) {
            riv_round_one.setVisibility(View.VISIBLE);
            if (colorList.get(0).equalsIgnoreCase("FFFFFF")) {
                riv_round_one.setBackgroundResource(R.drawable.color_line_white);
            } else {
                riv_round_one.setBackgroundColor(Color.parseColor("#" + colorList.get(0)));
            }
            riv_round_two.setVisibility(View.VISIBLE);
            if (colorList.get(1).equalsIgnoreCase("FFFFFF")) {
                riv_round_two.setBackgroundResource(R.drawable.color_line_white);
            } else {
                riv_round_two.setBackgroundColor(Color.parseColor("#" + colorList.get(1)));
            }

            riv_round_three.setVisibility(View.GONE);
        } else if (colorList.size() >= 3) {
            riv_round_one.setVisibility(View.VISIBLE);
            if (colorList.get(0).equalsIgnoreCase("FFFFFF")) {
                riv_round_one.setBackgroundResource(R.drawable.color_line_white);
            } else {
                riv_round_one.setBackgroundColor(Color.parseColor("#" + colorList.get(0)));
            }
            riv_round_two.setVisibility(View.VISIBLE);
            if (colorList.get(1).equalsIgnoreCase("FFFFFF")) {
                riv_round_two.setBackgroundResource(R.drawable.color_line_white);
            } else {
                riv_round_two.setBackgroundColor(Color.parseColor("#" + colorList.get(1)));
            }
            riv_round_three.setVisibility(View.VISIBLE);
            if (colorList.get(2).equalsIgnoreCase("FFFFFF")) {
                riv_round_three.setBackgroundResource(R.drawable.color_line_white);
            } else {
                riv_round_three.setBackgroundColor(Color.parseColor("#" + colorList.get(2)));
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
    }


    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
    }

    private List<SearchItemDataBean> mListData = new ArrayList<>();
    private List<Integer> mLListDataHeight = new ArrayList<>();
    private List<Integer> mSListDataHeight = new ArrayList<>();
    private List<String> strTuiJian;
    private Map<Integer, ColorItemBean> mSelectListData;
    private boolean curentListTag = true;
    private int page_num = 1;
    private int TYPE_ONE = 1;
    private int TYPE_TWO = 2;
    private int scroll_position = 0;
    private int position;


}
