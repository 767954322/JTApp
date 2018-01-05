package com.homechart.app.lingganji.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.contract.AppBarStateChangeListener;
import com.homechart.app.commont.contract.InterDioalod;
import com.homechart.app.commont.utils.MyDialog;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.common.entity.inspirationdetail.InspirationDetailBean;
import com.homechart.app.lingganji.common.entity.inspirationpics.InsPicItemBean;
import com.homechart.app.lingganji.common.entity.inspirationpics.InsPicsBean;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InspirationDetailActivity extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener,
        InterDioalod {
    private String mUserId;
    private TextView mTital;
    private TextView mRightCreate;
    private ImageButton mBack;
    private ImageButton mRightIcon;
    private int widthPic;
    private MultiItemCommonAdapter<InsPicItemBean> mAdapter;
    private List<InsPicItemBean> mListData = new ArrayList<>();
    private HRecyclerView mRecyclerView;
    private LoadMoreFooterView mLoadMoreFooterView;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int page_num = 1;
    private int position;
    private String mAlbumId;
    private View mHeaderInspirationPic;
    private CoordinatorLayout mCoordinator;
    private RelativeLayout mTopRelative;
    private AppBarLayout mAppbar;
    private RelativeLayout rl_check_pic1;
    private RelativeLayout rl_check_pic;
    private boolean curentListTag = false;
    private ImageView iv_check_icon1;
    private ImageView iv_check_icon;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int widthPicList;
    private InputMethodManager imm;
    private MyDialog mDialog;
    private RelativeLayout id_main;
    private TextView mInspirationName;
    private TextView mInspirationMiaoSu;
    private ImageView mUserHeader;
    private TextView mUserName;
    private TextView mUserDingYue;
    private TextView mUserPicNum;
    private int deletePosition = -1;
    private TextView tv_check_name1;
    private TextView tv_check_name;
    private RelativeLayout rl_dingyue;
    private RelativeLayout rl_dingyue1;
    private String mMyUserId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_inspiration_details;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        mUserId = getIntent().getStringExtra("user_id");
        mAlbumId =  getIntent().getStringExtra("album_id");
    }

    @Override
    protected void initView() {

        mHeaderInspirationPic = LayoutInflater.from(InspirationDetailActivity.this).inflate(R.layout.header_inspiration_detail, null);
        id_main = (RelativeLayout) this.findViewById(R.id.id_main);
        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        mRightIcon = (ImageButton) findViewById(R.id.nav_secondary_imageButton);
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mRecyclerView = (HRecyclerView) this.findViewById(R.id.rcy_recyclerview);
        mCoordinator = (CoordinatorLayout) this.findViewById(R.id.cl_coordinator);
        mTopRelative = (RelativeLayout) this.findViewById(R.id.rl_top_check);
        mAppbar = (AppBarLayout) this.findViewById(R.id.appbar);
        rl_check_pic1 = (RelativeLayout) this.findViewById(R.id.rl_check_pic1);
        rl_check_pic = (RelativeLayout) this.findViewById(R.id.rl_check_pic);
        iv_check_icon1 = (ImageView) this.findViewById(R.id.iv_check_icon1);
        tv_check_name1 = (TextView) this.findViewById(R.id.tv_check_name1);
        tv_check_name = (TextView) this.findViewById(R.id.tv_check_name);
        iv_check_icon = (ImageView) this.findViewById(R.id.iv_check_icon);

        mInspirationName = (TextView) this.findViewById(R.id.tv_inspiration_name);
        mInspirationMiaoSu = (TextView) this.findViewById(R.id.tv_inspiration_miaosu);
        mUserName = (TextView) this.findViewById(R.id.tv_user_name);
        mUserDingYue = (TextView) this.findViewById(R.id.tv_user_dingyue);
        mUserPicNum = (TextView) this.findViewById(R.id.tv_user_pic_num);
        mUserHeader = (ImageView) this.findViewById(R.id.iv_user_header);
        rl_dingyue = (RelativeLayout) this.findViewById(R.id.rl_dingyue);
        rl_dingyue1 = (RelativeLayout) this.findViewById(R.id.rl_dingyue1);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
        mRightIcon.setOnClickListener(this);
        rl_check_pic.setOnClickListener(this);
        rl_check_pic1.setOnClickListener(this);
        rl_dingyue1.setOnClickListener(this);
        rl_dingyue.setOnClickListener(this);
        mAppbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
//                    mRecyclerView.setPadding(UIUtils.getDimens(R.dimen.font_5), 0, UIUtils.getDimens(R.dimen.font_5), UIUtils.getDimens(R.dimen.font_5));
                    mTopRelative.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
//                    mRecyclerView.setPadding(UIUtils.getDimens(R.dimen.font_5), UIUtils.getDimens(R.dimen.font_45), UIUtils.getDimens(R.dimen.font_5), UIUtils.getDimens(R.dimen.font_5));
                    mTopRelative.setVisibility(View.VISIBLE);
                } else {
                    //中间状态
//                    mRecyclerView.setPadding(UIUtils.getDimens(R.dimen.font_5), 0, UIUtils.getDimens(R.dimen.font_5), UIUtils.getDimens(R.dimen.font_5));
                    mTopRelative.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.nav_left_imageButton:
                InspirationDetailActivity.this.setResult(2, InspirationDetailActivity.this.getIntent());
                InspirationDetailActivity.this.finish();
                break;
            case R.id.rl_check_pic:
            case R.id.rl_check_pic1:
                if (curentListTag) {
                    tv_check_name1.setText("大图");
                    tv_check_name.setText("大图");
                    curentListTag = false;
                    iv_check_icon1.setImageResource(R.drawable.pubuliu1);
                    iv_check_icon.setImageResource(R.drawable.pubuliu1);
                    mRecyclerView.setLayoutManager(staggeredGridLayoutManager);

                } else {
                    tv_check_name1.setText("小图");
                    tv_check_name.setText("小图");
                    curentListTag = true;
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(InspirationDetailActivity.this));
                    iv_check_icon1.setImageResource(R.drawable.changtu1);
                    iv_check_icon.setImageResource(R.drawable.changtu1);
                }
                break;
            case R.id.rl_dingyue1:
            case R.id.rl_dingyue:


                break;
        }

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTital.setText("灵感辑");
        mMyUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        widthPic = (PublicUtils.getScreenWidth(this) - UIUtils.getDimens(R.dimen.font_30)) / 2;
        widthPicList = PublicUtils.getScreenWidth(this) - UIUtils.getDimens(R.dimen.font_20);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mDialog = new MyDialog(InspirationDetailActivity.this, "确定删除灵感辑图片么？", InspirationDetailActivity.this);
        buildRecyclerView();
        getInspirationDetail();
    }

    private void getInspirationDetail() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(InspirationDetailActivity.this, "专辑详情获取失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        String newData = "{\"info\": " + data_msg + "}";
                        InspirationDetailBean inspirationDetailBean = GsonUtil.jsonToBean(newData, InspirationDetailBean.class);

                        changeTopUI(inspirationDetailBean);
                    } else {
                        ToastUtils.showCenter(InspirationDetailActivity.this, "专辑详情获取失败！");
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getInspirationDetail(mAlbumId, callBack);
    }

    private void changeTopUI(InspirationDetailBean inspirationDetailBean) {
        if (null != inspirationDetailBean) {
            if (mMyUserId.equals(inspirationDetailBean.getInfo().getUser_info().getUser_id())) {
                rl_dingyue1.setVisibility(View.GONE);
                rl_dingyue.setVisibility(View.GONE);
            } else {
                rl_dingyue1.setVisibility(View.VISIBLE);
                rl_dingyue.setVisibility(View.VISIBLE);
            }
            mInspirationName.setText(inspirationDetailBean.getInfo().getAlbum_info().getAlbum_name());
            mUserDingYue.setText(inspirationDetailBean.getInfo().getAlbum_info().getSubscribe_num() + " 订阅");
            mUserPicNum.setText(inspirationDetailBean.getInfo().getAlbum_info().getItem_num() + " 张图");
            if (TextUtils.isEmpty(inspirationDetailBean.getInfo().getAlbum_info().getDescription().trim())) {
                mInspirationMiaoSu.setVisibility(View.GONE);
            } else {
                mInspirationMiaoSu.setVisibility(View.VISIBLE);
                mInspirationMiaoSu.setText(inspirationDetailBean.getInfo().getAlbum_info().getDescription());
            }
            if (null != inspirationDetailBean.getInfo().getUser_info()) {
                ImageUtils.displayRoundImage(inspirationDetailBean.getInfo().getUser_info().getAvatar().getThumb(), mUserHeader);
                mUserName.setText(inspirationDetailBean.getInfo().getUser_info().getNickname());
            }
        }
    }

    private void buildRecyclerView() {
        MultiItemTypeSupport<InsPicItemBean> support = new MultiItemTypeSupport<InsPicItemBean>() {
            @Override
            public int getLayoutId(int itemType) {

                return R.layout.item_my_inspirationpic_list;

            }

            @Override
            public int getItemViewType(int position, InsPicItemBean s) {
                return 0;

            }
        };

        mAdapter = new MultiItemCommonAdapter<InsPicItemBean>(InspirationDetailActivity.this, mListData, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {

                if (curentListTag) {//线性
                    holder.getView(R.id.iv_item_delete1).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_item_edite1).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_item_delete).setVisibility(View.GONE);
                    holder.getView(R.id.iv_item_edite).setVisibility(View.GONE);
                    holder.getView(R.id.tv_item_miaosu1).setVisibility(View.VISIBLE);
                    holder.getView(R.id.tv_item_miaosu).setVisibility(View.GONE);
                } else {//瀑布流
                    holder.getView(R.id.iv_item_delete).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_item_edite).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_item_delete1).setVisibility(View.GONE);
                    holder.getView(R.id.iv_item_edite1).setVisibility(View.GONE);
                    holder.getView(R.id.tv_item_miaosu1).setVisibility(View.GONE);
                    holder.getView(R.id.tv_item_miaosu).setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(mListData.get(position).getItem_info().getDescription().trim())) {
                    ((TextView) holder.getView(R.id.tv_item_miaosu)).setVisibility(View.GONE);
                    ((TextView) holder.getView(R.id.tv_item_miaosu1)).setVisibility(View.GONE);
                } else {
                    ((TextView) holder.getView(R.id.tv_item_miaosu)).setText(mListData.get(position).getItem_info().getDescription());
                    ((TextView) holder.getView(R.id.tv_item_miaosu1)).setText(mListData.get(position).getItem_info().getDescription());
                }
                ViewGroup.LayoutParams layoutParams = holder.getView(R.id.iv_item_pic).getLayoutParams();
                if (curentListTag) {
                    layoutParams.width = widthPicList;
                    layoutParams.height = Math.round(widthPicList / mListData.get(position).getItem_info().getImage().getRatio());
                    holder.getView(R.id.iv_item_pic).setLayoutParams(layoutParams);
                    ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg1(), (ImageView) holder.getView(R.id.iv_item_pic));

                } else {
                    layoutParams.width = widthPic;
                    layoutParams.height = Math.round(widthPic / mListData.get(position).getItem_info().getImage().getRatio());
                    holder.getView(R.id.iv_item_pic).setLayoutParams(layoutParams);
                    ImageUtils.displayFilletImage(mListData.get(position).getItem_info().getImage().getImg0(), (ImageView) holder.getView(R.id.iv_item_pic));

                }

                holder.getView(R.id.iv_item_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletePosition = position;
                        //软键盘如果打开的话，关闭软键盘
                        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                        if (isOpen) {
                            if (getCurrentFocus() != null) {//强制关闭软键盘
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        }
                        mDialog.showAtLocation(id_main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                    }
                });
                holder.getView(R.id.iv_item_delete1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletePosition = position;
                        //软键盘如果打开的话，关闭软键盘
                        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                        if (isOpen) {
                            if (getCurrentFocus() != null) {//强制关闭软键盘
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        }
                        mDialog.showAtLocation(id_main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }
                });
                holder.getView(R.id.iv_item_edite).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(InspirationDetailActivity.this, InspirationDetailEditActivity.class);
                        intent.putExtra("url", mListData.get(position).getItem_info().getImage().getImg0());
                        intent.putExtra("description", mListData.get(position).getItem_info().getDescription());
                        intent.putExtra("updata_time", mListData.get(position).getItem_info().getUpdate_time());
                        intent.putExtra("item_id", mListData.get(position).getItem_info().getItem_id());
                        intent.putExtra("position", position);
//                        intent.putExtra("time",mListData.get(position).getItem_info().g);
                        InspirationDetailActivity.this.startActivityForResult(intent, 1);
                    }
                });
                holder.getView(R.id.iv_item_edite1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(InspirationDetailActivity.this, InspirationDetailEditActivity.class);
                        intent.putExtra("url", mListData.get(position).getItem_info().getImage().getImg0());
                        intent.putExtra("description", mListData.get(position).getItem_info().getDescription());
                        intent.putExtra("updata_time", mListData.get(position).getItem_info().getUpdate_time());
                        intent.putExtra("item_id", mListData.get(position).getItem_info().getItem_id());
//                        intent.putExtra("time",mListData.get(position).getItem_info().g);
                        InspirationDetailActivity.this.startActivityForResult(intent, 1);
                    }
                });
            }
        };
//        mRecyclerView.addHeaderView(mHeaderInspirationPic);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getInspirationPicsData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getInspirationPicsData(LOADMORE_STATUS);
    }

    private void getInspirationPicsData(final String state) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if (state.equals(LOADMORE_STATUS)) {
                    --page_num;
                } else {
                    page_num = 1;
                }
                mRecyclerView.setRefreshing(false);//刷新完毕
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                ToastUtils.showCenter(InspirationDetailActivity.this, "专辑列表获取失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        InsPicsBean insPicsBean = GsonUtil.jsonToBean(data_msg, InsPicsBean.class);
                        if (null != insPicsBean.getItem_list() && 0 != insPicsBean.getItem_list().size()) {
                            updateViewFromData(insPicsBean.getItem_list(), state);
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
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getUserInspirationPics(mAlbumId, (page_num - 1) * 20 + "", "20", callBack);
    }

    private void updateViewFromData(List<InsPicItemBean> listData, String state) {

        switch (state) {

            case REFRESH_STATUS:
                mListData.clear();
                if (null != listData && listData.size() > 0) {
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
                if (null != listData && listData.size() > 0) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onQuXiao() {
        deletePosition = -1;
        mDialog.dismiss();
    }

    @Override
    public void onQueRen() {
        mDialog.dismiss();
        CustomProgress.show(InspirationDetailActivity.this, "正在删除...", false, null);
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(InspirationDetailActivity.this, "删除失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        CustomProgress.cancelDialog();
                        mListData.remove(deletePosition);
                        mAdapter.notifyDataSetChanged();
                        getInspirationDetail();
                    } else {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(InspirationDetailActivity.this, "删除失败！");
                    }
                } catch (JSONException e) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(InspirationDetailActivity.this, "删除失败！");
                }
            }
        };
        MyHttpManager.getInstance().removePic(mListData.get(deletePosition).getItem_info().getItem_id(), callBack);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            InspirationDetailActivity.this.setResult(2, InspirationDetailActivity.this.getIntent());
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 && requestCode == 1) {
            String description = data.getStringExtra("description");
            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                mListData.get(position).getItem_info().setDescription(description);
                mAdapter.notifyDataSetChanged();
            }

        }

    }
}
