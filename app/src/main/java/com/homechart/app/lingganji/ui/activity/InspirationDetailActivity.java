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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.contract.AppBarStateChangeListener;
import com.homechart.app.commont.contract.InterDioalod;
import com.homechart.app.commont.contract.InterDioalod1;
import com.homechart.app.commont.utils.MyDialog;
import com.homechart.app.commont.utils.MyDialog1;
import com.homechart.app.home.activity.ImageDetailLongActivity;
import com.homechart.app.home.activity.UserInfoActivity;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.common.entity.inspirationdetail.InspirationDetailBean;
import com.homechart.app.lingganji.common.entity.inspirationpics.InsPicItemBean;
import com.homechart.app.lingganji.common.entity.inspirationpics.InsPicsBean;
import com.homechart.app.lingganji.common.view.InspirationImageEditPop;
import com.homechart.app.lingganji.contract.InterPopBottom;
import com.homechart.app.myview.HomeSharedPopWinPublic;
import com.homechart.app.myview.InspritionPop;
import com.homechart.app.myview.MiaoSuPop;
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
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InspirationDetailActivity extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener,
        OnRefreshListener,
        InterDioalod,
        InterDioalod1,
        InterPopBottom,
        HomeSharedPopWinPublic.ClickInter {
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
    private boolean ifHideEdit;
    private TextView tv_dingyue_name;
    private TextView tv_dingyue_name1;
    private InspirationDetailBean inspirationDetailBean;
    private boolean ifClickDingYue = true;
    private int ifUser = -1;// 1:不是本人  2:是本人
    private InspirationImageEditPop mInspirationImageEditPop;
    private RelativeLayout rl_top_check1;
    private RelativeLayout rl_nodata;
    private TextView mInspirationNameNo;
    private RelativeLayout rl_dingyueno;
    private TextView tv_dingyue_name_no;
    private TextView mInspirationMiaoSuNo;
    private ImageView mUserHeaderNo;
    private TextView mUserNameNo;
    private TextView mUserDingYueNo;
    private TextView mUserPicNumNo;
    private TextView tv_test;
    private ImageView iv_more_miaosu;
    private ImageView iv_more_miaosu_no;
    private ImageView iv_item_miaosu_more;
    private ImageView iv_item_miaosu_more1;
    private TextView tv_test_list;
    private TextView tv_test_list_pubu;
    private HomeSharedPopWinPublic homeSharedPopWinPublic;
    private MyDialog1 mDialog1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_inspiration_details;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        mUserId = getIntent().getStringExtra("user_id");
        mAlbumId = getIntent().getStringExtra("album_id");
        ifHideEdit = getIntent().getBooleanExtra("ifHideEdit", false);
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
        mInspirationNameNo = (TextView) this.findViewById(R.id.tv_inspiration_name_no);
        mInspirationMiaoSu = (TextView) this.findViewById(R.id.tv_inspiration_miaosu);
        mInspirationMiaoSuNo = (TextView) this.findViewById(R.id.tv_inspiration_miaosu_no);
        mUserName = (TextView) this.findViewById(R.id.tv_user_name);
        mUserNameNo = (TextView) this.findViewById(R.id.tv_user_name_no);
        mUserDingYue = (TextView) this.findViewById(R.id.tv_user_dingyue);
        mUserDingYueNo = (TextView) this.findViewById(R.id.tv_user_dingyue_no);
        mUserPicNum = (TextView) this.findViewById(R.id.tv_user_pic_num);
        mUserPicNumNo = (TextView) this.findViewById(R.id.tv_user_pic_num_no);
        mUserHeader = (ImageView) this.findViewById(R.id.iv_user_header);
        mUserHeaderNo = (ImageView) this.findViewById(R.id.iv_user_header_no);
        rl_dingyue = (RelativeLayout) this.findViewById(R.id.rl_dingyue);
        rl_dingyue1 = (RelativeLayout) this.findViewById(R.id.rl_dingyue1);
        rl_dingyueno = (RelativeLayout) this.findViewById(R.id.rl_dingyue_no);
        tv_dingyue_name = (TextView) this.findViewById(R.id.tv_dingyue_name);
        tv_dingyue_name1 = (TextView) this.findViewById(R.id.tv_dingyue_name1);
        tv_dingyue_name_no = (TextView) this.findViewById(R.id.tv_dingyue_name_no);
        tv_test = (TextView) this.findViewById(R.id.tv_test);
        tv_test_list = (TextView) this.findViewById(R.id.tv_test_list);
        tv_test_list_pubu = (TextView) this.findViewById(R.id.tv_test_list_pubu);
        iv_more_miaosu = (ImageView) this.findViewById(R.id.iv_more_miaosu);
        iv_more_miaosu_no = (ImageView) this.findViewById(R.id.iv_more_miaosu_no);

        iv_item_miaosu_more = (ImageView) this.findViewById(R.id.iv_item_miaosu_more);
        iv_item_miaosu_more1 = (ImageView) this.findViewById(R.id.iv_item_miaosu_more1);

        rl_top_check1 = (RelativeLayout) this.findViewById(R.id.rl_top_check1);
        rl_nodata = (RelativeLayout) this.findViewById(R.id.rl_nodata);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
        mRightIcon.setOnClickListener(this);
        rl_check_pic.setOnClickListener(this);
        rl_check_pic1.setOnClickListener(this);
        rl_dingyue1.setOnClickListener(this);
        rl_dingyueno.setOnClickListener(this);
        rl_dingyue.setOnClickListener(this);
        iv_more_miaosu.setOnClickListener(this);
        iv_more_miaosu_no.setOnClickListener(this);
        mUserHeader.setOnClickListener(this);
        mUserHeaderNo.setOnClickListener(this);
        mUserNameNo.setOnClickListener(this);
        mUserName.setOnClickListener(this);
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
                    tv_check_name1.setText("小图");
                    tv_check_name.setText("小图");
                    curentListTag = false;
                    iv_check_icon1.setImageResource(R.drawable.pubuliu1);
                    iv_check_icon.setImageResource(R.drawable.pubuliu1);
                    mRecyclerView.setLayoutManager(staggeredGridLayoutManager);

                } else {
                    tv_check_name1.setText("大图");
                    tv_check_name.setText("大图");
                    curentListTag = true;
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(InspirationDetailActivity.this));
                    iv_check_icon1.setImageResource(R.drawable.changtu1);
                    iv_check_icon.setImageResource(R.drawable.changtu1);
                }
                break;
            case R.id.rl_dingyue1:
            case R.id.rl_dingyue:
            case R.id.rl_dingyue_no:
                if (ifClickDingYue) {
                    ifClickDingYue = false;
                    if (null != inspirationDetailBean && inspirationDetailBean.getInfo().getAlbum_info().getIs_subscribed().equals("1")) {
                        //取消订阅
                        removeDingYue();
                    } else if (null != inspirationDetailBean && inspirationDetailBean.getInfo().getAlbum_info().getIs_subscribed().equals("0")) {
                        //订阅
                        addDingYue();
                    }
                }
                break;
            case R.id.nav_secondary_imageButton:
                if (ifUser == 1) {//不是本人
                    if (inspirationDetailBean != null) {
                        homeSharedPopWinPublic.showAtLocation(InspirationDetailActivity.this.findViewById(R.id.id_main),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                                0,
                                0); //设置layout在PopupWindow中显示的位置
                    }
                } else if (ifUser == 2) {//本人
                    //软键盘如果打开的话，关闭软键盘
                    boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                    if (isOpen) {
                        if (getCurrentFocus() != null) {//强制关闭软键盘
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                    mInspirationImageEditPop.showAtLocation(id_main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }

                break;
            case R.id.iv_more_miaosu_no:
            case R.id.iv_more_miaosu:
                if (null != inspirationDetailBean) {
                    MiaoSuPop miaoSuPop = new MiaoSuPop(InspirationDetailActivity.this, inspirationDetailBean);
                    //软键盘如果打开的话，关闭软键盘
                    boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                    if (isOpen) {
                        if (getCurrentFocus() != null) {//强制关闭软键盘
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                    miaoSuPop.showAtLocation(id_main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.iv_user_header:
            case R.id.iv_user_header_no:
            case R.id.tv_user_name_no:
            case R.id.tv_user_name:
                if (null != inspirationDetailBean) {
                    Intent intent = new Intent(InspirationDetailActivity.this, UserInfoActivity.class);
                    intent.putExtra(ClassConstant.LoginSucces.USER_ID, inspirationDetailBean.getInfo().getUser_info().getUser_id());
                    startActivity(intent);
                }
                break;
        }

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        homeSharedPopWinPublic = new HomeSharedPopWinPublic(InspirationDetailActivity.this, InspirationDetailActivity.this);
        mTital.setText("灵感辑");
        mRightIcon.setImageResource(R.drawable.gengduo);
        mRightIcon.setVisibility(View.GONE);
        mMyUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        widthPic = (PublicUtils.getScreenWidth(this) - UIUtils.getDimens(R.dimen.font_30)) / 2;
        widthPicList = PublicUtils.getScreenWidth(this) - UIUtils.getDimens(R.dimen.font_20);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mDialog = new MyDialog(InspirationDetailActivity.this, "确定删除灵感辑图片么？", InspirationDetailActivity.this);
        mDialog1 = new MyDialog1(InspirationDetailActivity.this, "确认要删除选中灵感辑？删除后其中所有图片也没有啦", InspirationDetailActivity.this);
        mInspirationImageEditPop = new InspirationImageEditPop(InspirationDetailActivity.this, "确定删除灵感辑图片么？", InspirationDetailActivity.this);
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
                        inspirationDetailBean = GsonUtil.jsonToBean(newData, InspirationDetailBean.class);

                        changeTopUI(inspirationDetailBean);
                    } else {
                        ToastUtils.showCenter(InspirationDetailActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getInspirationDetail(mAlbumId, callBack);
    }

    private void changeTopUI(InspirationDetailBean inspirationDetailBean) {
        if (null != inspirationDetailBean) {
            if (null != inspirationDetailBean.getInfo().getUser_info() && !mMyUserId.equals(inspirationDetailBean.getInfo().getUser_info().getUser_id())) {
                mRightIcon.setImageResource(R.drawable.shared_icon);
                ifUser = 1;
                rl_dingyue1.setVisibility(View.VISIBLE);
                rl_dingyueno.setVisibility(View.VISIBLE);
                rl_dingyue.setVisibility(View.VISIBLE);
                if (inspirationDetailBean.getInfo().getAlbum_info().getIs_subscribed().equals("1")) {
                    //订阅了
                    changeDingYueStatus(true);
                } else {
                    //未订阅了
                    changeDingYueStatus(false);
                }
            } else {
                ifUser = 2;
                mRightIcon.setImageResource(R.drawable.gengduo);
                rl_dingyue1.setVisibility(View.GONE);
                rl_dingyueno.setVisibility(View.GONE);
                rl_dingyue.setVisibility(View.GONE);
            }
            mRightIcon.setVisibility(View.VISIBLE);
            mInspirationName.setText(inspirationDetailBean.getInfo().getAlbum_info().getAlbum_name());
            mInspirationNameNo.setText(inspirationDetailBean.getInfo().getAlbum_info().getAlbum_name());
            mUserDingYue.setText(inspirationDetailBean.getInfo().getAlbum_info().getSubscribe_num() + " 订阅");
            mUserDingYueNo.setText(inspirationDetailBean.getInfo().getAlbum_info().getSubscribe_num() + " 订阅");
            mUserPicNum.setText(inspirationDetailBean.getInfo().getAlbum_info().getItem_num() + " 张图");
            mUserPicNumNo.setText(inspirationDetailBean.getInfo().getAlbum_info().getItem_num() + " 张图");
            if (TextUtils.isEmpty(inspirationDetailBean.getInfo().getAlbum_info().getDescription().trim())) {
                mInspirationMiaoSu.setVisibility(View.GONE);
                mInspirationMiaoSuNo.setVisibility(View.GONE);
            } else {
                mInspirationMiaoSu.setVisibility(View.VISIBLE);
                mInspirationMiaoSuNo.setVisibility(View.VISIBLE);
                tv_test.setText(inspirationDetailBean.getInfo().getAlbum_info().getDescription());
                mInspirationMiaoSu.setText(inspirationDetailBean.getInfo().getAlbum_info().getDescription());
                mInspirationMiaoSuNo.setText(inspirationDetailBean.getInfo().getAlbum_info().getDescription());
                int count = tv_test.getLineCount();
                if (count > 3) {
                    iv_more_miaosu.setVisibility(View.VISIBLE);
                    iv_more_miaosu_no.setVisibility(View.VISIBLE);
                } else {
                    iv_more_miaosu.setVisibility(View.GONE);
                    iv_more_miaosu_no.setVisibility(View.GONE);
                }

            }
            if (null != inspirationDetailBean.getInfo().getUser_info()) {
                ImageUtils.displayRoundImage(inspirationDetailBean.getInfo().getUser_info().getAvatar().getThumb(), mUserHeader);
                ImageUtils.displayRoundImage(inspirationDetailBean.getInfo().getUser_info().getAvatar().getThumb(), mUserHeaderNo);
                mUserName.setText(inspirationDetailBean.getInfo().getUser_info().getNickname());
                mUserNameNo.setText(inspirationDetailBean.getInfo().getUser_info().getNickname());
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
                    if (ifHideEdit) {
                        holder.getView(R.id.iv_item_delete1).setVisibility(View.GONE);
                        holder.getView(R.id.iv_item_edite1).setVisibility(View.GONE);
                        holder.getView(R.id.iv_item_delete).setVisibility(View.GONE);
                        holder.getView(R.id.iv_item_edite).setVisibility(View.GONE);
                    } else {
                        holder.getView(R.id.iv_item_delete1).setVisibility(View.VISIBLE);
                        holder.getView(R.id.iv_item_edite1).setVisibility(View.VISIBLE);
                        holder.getView(R.id.iv_item_delete).setVisibility(View.GONE);
                        holder.getView(R.id.iv_item_edite).setVisibility(View.GONE);
                    }
                    holder.getView(R.id.tv_item_miaosu1).setVisibility(View.VISIBLE);
                    holder.getView(R.id.tv_item_miaosu).setVisibility(View.GONE);
                    holder.getView(R.id.iv_item_miaosu_more).setVisibility(View.GONE);
                    holder.getView(R.id.iv_item_miaosu_more1).setVisibility(View.VISIBLE);
                    holder.getView(R.id.view_bottom).setVisibility(View.GONE);
                } else {//瀑布流
                    if (ifHideEdit) {
                        holder.getView(R.id.view_bottom).setVisibility(View.VISIBLE);
                        holder.getView(R.id.iv_item_delete).setVisibility(View.GONE);
                        holder.getView(R.id.iv_item_edite).setVisibility(View.GONE);
                        holder.getView(R.id.iv_item_delete1).setVisibility(View.GONE);
                        holder.getView(R.id.iv_item_edite1).setVisibility(View.GONE);
                    } else {
                        holder.getView(R.id.view_bottom).setVisibility(View.GONE);
                        holder.getView(R.id.iv_item_delete).setVisibility(View.VISIBLE);
                        holder.getView(R.id.iv_item_edite).setVisibility(View.VISIBLE);
                        holder.getView(R.id.iv_item_delete1).setVisibility(View.GONE);
                        holder.getView(R.id.iv_item_edite1).setVisibility(View.GONE);
                    }
                    holder.getView(R.id.tv_item_miaosu1).setVisibility(View.GONE);
                    holder.getView(R.id.tv_item_miaosu).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_item_miaosu_more).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_item_miaosu_more1).setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(mListData.get(position).getItem_info().getDescription().trim())) {
                    ((TextView) holder.getView(R.id.tv_item_miaosu)).setVisibility(View.GONE);
                    ((TextView) holder.getView(R.id.tv_item_miaosu1)).setVisibility(View.GONE);
                    ((ImageView) holder.getView(R.id.iv_item_miaosu_more1)).setVisibility(View.GONE);
                    ((ImageView) holder.getView(R.id.iv_item_miaosu_more)).setVisibility(View.GONE);
                } else {
                    ((TextView) holder.getView(R.id.tv_item_miaosu)).setText(mListData.get(position).getItem_info().getDescription());
                    ((TextView) holder.getView(R.id.tv_item_miaosu1)).setText(mListData.get(position).getItem_info().getDescription());
                    int countList = getListTextLines(mListData.get(position).getItem_info().getDescription());
                    int countPuBu = getPuBuTextLines(mListData.get(position).getItem_info().getDescription());

                    if (curentListTag) {
                        if (countList > 1) {
                            ((ImageView) holder.getView(R.id.iv_item_miaosu_more1)).setVisibility(View.VISIBLE);
                            ((ImageView) holder.getView(R.id.iv_item_miaosu_more)).setVisibility(View.GONE);
                        } else {
                            ((ImageView) holder.getView(R.id.iv_item_miaosu_more1)).setVisibility(View.GONE);
                            ((ImageView) holder.getView(R.id.iv_item_miaosu_more)).setVisibility(View.GONE);
                        }
                    } else {
                        if (countPuBu > 1) {
                            ((ImageView) holder.getView(R.id.iv_item_miaosu_more1)).setVisibility(View.GONE);
                            ((ImageView) holder.getView(R.id.iv_item_miaosu_more)).setVisibility(View.VISIBLE);
                        } else {
                            ((ImageView) holder.getView(R.id.iv_item_miaosu_more1)).setVisibility(View.GONE);
                            ((ImageView) holder.getView(R.id.iv_item_miaosu_more)).setVisibility(View.GONE);
                        }
                    }

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
                holder.getView(R.id.iv_item_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(InspirationDetailActivity.this, ImageDetailLongActivity.class);
                        intent.putExtra("item_id", mListData.get(position).getItem_info().getItem_id());
                        startActivity(intent);
                    }
                });
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
                        intent.putExtra("position", position);
//                        intent.putExtra("time",mListData.get(position).getItem_info().g);
                        InspirationDetailActivity.this.startActivityForResult(intent, 1);
                    }
                });

                holder.getView(R.id.iv_item_miaosu_more1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InspritionPop inspritionPop = new InspritionPop(InspirationDetailActivity.this, mListData.get(position));
                        //软键盘如果打开的话，关闭软键盘
                        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                        if (isOpen) {
                            if (getCurrentFocus() != null) {//强制关闭软键盘
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        }
                        inspritionPop.showAtLocation(id_main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }
                });
                holder.getView(R.id.iv_item_miaosu_more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InspritionPop inspritionPop = new InspritionPop(InspirationDetailActivity.this, mListData.get(position));
                        //软键盘如果打开的话，关闭软键盘
                        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                        if (isOpen) {
                            if (getCurrentFocus() != null) {//强制关闭软键盘
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        }
                        inspritionPop.showAtLocation(id_main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

                    rl_top_check1.setVisibility(View.VISIBLE);
                    mCoordinator.setVisibility(View.VISIBLE);
                    rl_nodata.setVisibility(View.GONE);
                    mListData.addAll(listData);
                } else {
                    //没有更多数据
                    rl_nodata.setVisibility(View.VISIBLE);
                    mCoordinator.setVisibility(View.GONE);
                    rl_top_check1.setVisibility(View.GONE);
                    mTopRelative.setVisibility(View.GONE);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                    page_num = 1;
                    mListData.clear();
                }
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setRefreshing(false);//刷新完毕
                break;

            case LOADMORE_STATUS:
                if (null != listData && listData.size() > 0) {
                    rl_top_check1.setVisibility(View.VISIBLE);
                    mCoordinator.setVisibility(View.VISIBLE);
                    rl_nodata.setVisibility(View.GONE);
                    position = mListData.size();
                    mListData.addAll(listData);
                    mAdapter.notifyItem(position, mListData, listData);
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                } else {
                    if (mListData.size() == 0) {
                        rl_nodata.setVisibility(View.VISIBLE);
                        mCoordinator.setVisibility(View.GONE);
                        mTopRelative.setVisibility(View.GONE);
                        rl_top_check1.setVisibility(View.GONE);
                    }
                    --page_num;
                    //没有更多数据
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                }
                break;
        }
    }

    private void changeDingYueStatus(boolean dingyue) {
        if (dingyue) {
            rl_dingyueno.setBackgroundResource(R.drawable.bg_dingyue);
            rl_dingyue.setBackgroundResource(R.drawable.bg_dingyue);
            rl_dingyue1.setBackgroundResource(R.drawable.bg_dingyue);
            tv_dingyue_name.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
            tv_dingyue_name_no.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
            tv_dingyue_name1.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
            tv_dingyue_name.setText("已订阅");
            tv_dingyue_name_no.setText("已订阅");
            tv_dingyue_name1.setText("已订阅");
        } else {
            rl_dingyueno.setBackgroundResource(R.drawable.bg_dingyue_quxiao);
            rl_dingyue.setBackgroundResource(R.drawable.bg_dingyue_quxiao);
            rl_dingyue1.setBackgroundResource(R.drawable.bg_dingyue_quxiao);
            tv_dingyue_name.setTextColor(UIUtils.getColor(R.color.white));
            tv_dingyue_name_no.setTextColor(UIUtils.getColor(R.color.white));
            tv_dingyue_name1.setTextColor(UIUtils.getColor(R.color.white));
            tv_dingyue_name.setText("＋ 订阅");
            tv_dingyue_name_no.setText("＋ 订阅");
            tv_dingyue_name1.setText("＋ 订阅");
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
    public void onQuXiao1() {
        mDialog1.dismiss();
    }

    @Override
    public void onQueRen1() {
        CustomProgress.show(InspirationDetailActivity.this, "删除中...", false, null);
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
                        ToastUtils.showCenter(InspirationDetailActivity.this, "删除成功！");
                        InspirationDetailActivity.this.setResult(2, InspirationDetailActivity.this.getIntent());
                        InspirationDetailActivity.this.finish();
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
        MyHttpManager.getInstance().removeInspiration(mAlbumId, callBack);
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

        } else if (resultCode == 2 && requestCode == 2) {
            onRefresh();
        } else if (resultCode == 3 && requestCode == 3) {
            getInspirationDetail();
        }

    }

    private void addDingYue() {
        CustomProgress.show(InspirationDetailActivity.this, "订阅中...", false, null);
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(InspirationDetailActivity.this, "订阅失败！");
                ifClickDingYue = true;
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        changeDingYueStatus(true);
                        getInspirationDetail();
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(InspirationDetailActivity.this, "订阅成功！");
                        ifClickDingYue = true;
                    } else {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(InspirationDetailActivity.this, "订阅失败！");
                        ifClickDingYue = true;
                    }
                } catch (JSONException e) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(InspirationDetailActivity.this, "订阅失败！");
                    ifClickDingYue = true;
                }
            }
        };
        MyHttpManager.getInstance().addDingYue(mAlbumId, callBack);
    }

    private void removeDingYue() {
        CustomProgress.show(InspirationDetailActivity.this, "取消订阅中...", false, null);
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ifClickDingYue = true;
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(InspirationDetailActivity.this, "取消订阅失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        changeDingYueStatus(false);
                        getInspirationDetail();
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(InspirationDetailActivity.this, "取消订阅成功！");
                        ifClickDingYue = true;
                    } else {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(InspirationDetailActivity.this, "取消订阅失败！");
                        ifClickDingYue = true;
                    }
                } catch (JSONException e) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(InspirationDetailActivity.this, "取消订阅失败！");
                    ifClickDingYue = true;
                }
            }
        };
        MyHttpManager.getInstance().removeDingYue(mAlbumId, callBack);
    }

    @Override
    public void onClose() {
        mInspirationImageEditPop.dismiss();
    }

    @Override
    public void onBianJi() {
        mInspirationImageEditPop.dismiss();
        Intent intent = new Intent(this, InspirationEditActivity.class);
        intent.putExtra("userid", mMyUserId);
        intent.putExtra("album_id", mAlbumId);
        intent.putExtra("name", inspirationDetailBean.getInfo().getAlbum_info().getAlbum_name());
        intent.putExtra("description", inspirationDetailBean.getInfo().getAlbum_info().getDescription());
        startActivityForResult(intent, 3);
    }

    @Override
    public void onGuanLi() {
        mInspirationImageEditPop.dismiss();
        Intent intent = new Intent(InspirationDetailActivity.this, EditInsprationImageListActivity.class);
        intent.putExtra("albumId", mAlbumId);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onDeleteInspiration() {
        mInspirationImageEditPop.dismiss();
        //软键盘如果打开的话，关闭软键盘
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen) {
            if (getCurrentFocus() != null) {//强制关闭软键盘
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        mDialog1.showAtLocation(id_main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    @Override
    public void onSharedInspiration() {
        mInspirationImageEditPop.dismiss();
        if (inspirationDetailBean != null) {
            homeSharedPopWinPublic.showAtLocation(InspirationDetailActivity.this.findViewById(R.id.id_main),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                    0,
                    0); //设置layout在PopupWindow中显示的位置
        }
    }

    public int getListTextLines(String str) {

        tv_test_list.setText(str);
        int count = tv_test_list.getLineCount();
        return count;
    }

    public int getPuBuTextLines(String str) {
        tv_test_list_pubu.setText(str);
        int count = tv_test_list_pubu.getLineCount();
        return count;
    }

    @Override
    public void onClickWeiXin() {
        sharedItemOpen(SHARE_MEDIA.WEIXIN);
    }

    @Override
    public void onClickPYQ() {
        sharedItemOpen(SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    @Override
    public void onClickWeiBo() {
        sharedItemOpen(SHARE_MEDIA.SINA);
    }

    @Override
    public void onClickQQ() {

        sharedItemOpen(SHARE_MEDIA.QQ);
    }

    private void sharedItemOpen(SHARE_MEDIA share_media) {

        UMImage image;
        if (mListData != null && mListData.size() > 0) {
            image = new UMImage(InspirationDetailActivity.this, mListData.get(0).getItem_info().getImage().getImg1());
        } else {
            image = new UMImage(InspirationDetailActivity.this, R.drawable.icon_app);
        }
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        UMWeb web = new UMWeb("http://h5.idcool.com.cn/album/" + inspirationDetailBean.getInfo().getAlbum_info().getAlbum_id());

        if (share_media == SHARE_MEDIA.WEIXIN) {
            web.setTitle("我正在看「" + inspirationDetailBean.getInfo().getUser_info().getNickname() + "」的灵感专辑「" + inspirationDetailBean.getInfo().getAlbum_info().getAlbum_name() + "」");//标题
        } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
            web.setTitle("我正在看「" + inspirationDetailBean.getInfo().getUser_info().getNickname() + "」的灵感专辑「" + inspirationDetailBean.getInfo().getAlbum_info().getAlbum_name() + "」");//标题
        } else if (share_media == SHARE_MEDIA.SINA) {
            web.setTitle("我正在看「" + inspirationDetailBean.getInfo().getUser_info().getNickname() + "」的灵感专辑「" + inspirationDetailBean.getInfo().getAlbum_info().getAlbum_name() + "」" +
                    "http://h5.idcool.com.cn/album/" + inspirationDetailBean.getInfo().getAlbum_info().getAlbum_id());//标题
        } else if (share_media == SHARE_MEDIA.QQ) {
            web.setTitle("OMG！「" + inspirationDetailBean.getInfo().getUser_info().getNickname() + "」的家图灵感辑撩到我了！");//标题
        }
        web.setThumb(image);  //缩略图
        String desi = "";
        if (share_media == SHARE_MEDIA.WEIXIN) {
            desi = inspirationDetailBean.getInfo().getAlbum_info().getDescription();
        } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
            desi = "我正在看「" + inspirationDetailBean.getInfo().getUser_info().getNickname() + "」的家图灵感辑「" + inspirationDetailBean.getInfo().getAlbum_info().getAlbum_name() + "」";
        } else if (share_media == SHARE_MEDIA.SINA) {
            desi = inspirationDetailBean.getInfo().getAlbum_info().getDescription();
        } else if (share_media == SHARE_MEDIA.QQ) {
            desi = inspirationDetailBean.getInfo().getAlbum_info().getDescription();
        }
        if (desi.length() > 160) {
            desi = desi.substring(0, 160) + "...";
        }
        web.setDescription(desi);//描述
        new ShareAction(InspirationDetailActivity.this).
                setPlatform(share_media).
                withMedia(web).
                setCallback(umShareListener).share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
//            //分享开始的回调
//            Log.d("tset","sdsds");
//            addShared();
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform == SHARE_MEDIA.WEIXIN) {
                ToastUtils.showCenter(InspirationDetailActivity.this, "微信好友分享成功啦");
            } else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
                ToastUtils.showCenter(InspirationDetailActivity.this, "微信朋友圈分享成功啦");
            } else if (platform == SHARE_MEDIA.SINA) {
                ToastUtils.showCenter(InspirationDetailActivity.this, "新浪微博分享成功啦");
            } else if (platform == SHARE_MEDIA.QQ) {
                ToastUtils.showCenter(InspirationDetailActivity.this, "QQ分享成功啦");
            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.showCenter(InspirationDetailActivity.this, "分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showCenter(InspirationDetailActivity.this, "分享取消了");
        }
    };


}
