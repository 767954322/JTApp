package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.adapter.MyArticlePicAdapter;
import com.homechart.app.home.adapter.MyArticlePingAdapter;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.articledetails.ArticleBean;
import com.homechart.app.home.bean.articlelike.ArticleLikeBean;
import com.homechart.app.home.bean.articlelike.ArticleLikeItemBean;
import com.homechart.app.home.bean.articleping.PingBean;
import com.homechart.app.home.bean.articleping.PingCommentListItemBean;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.myview.MyListView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.SoftKeyBoardListener;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gumenghao on 17/7/18.
 * 文章详情页
 */
public class ArticleDetailsActivity
        extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener,
        MyArticlePingAdapter.HuiFu {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_article_details;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        article_id = getIntent().getStringExtra("article_id");
    }

    @Override
    protected void initView() {
        header = LayoutInflater.from(this).inflate(R.layout.header_articledetital, null);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mRecyclerView = (HRecyclerView) findViewById(R.id.rcy_recyclerview_info);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);

        tv_people_name = (TextView) header.findViewById(R.id.tv_people_name);
        iv_people_tag = (ImageView) header.findViewById(R.id.iv_people_tag);
        riv_people_header = (RoundImageView) header.findViewById(R.id.riv_people_header);
        tv_people_details = (TextView) header.findViewById(R.id.tv_people_details);
        tv_people_guanzhu = (TextView) header.findViewById(R.id.tv_people_guanzhu);
        tv_article_tital = (TextView) header.findViewById(R.id.tv_article_tital);
        tv_readnum_num = (TextView) header.findViewById(R.id.tv_readnum_num);
        tv_yinyan_content = (TextView) header.findViewById(R.id.tv_yinyan_content);
        rl_yinyan_tital_wai = (RelativeLayout) header.findViewById(R.id.rl_yinyan_tital_wai);
        mlv_article_pic_content = (MyListView) header.findViewById(R.id.mlv_article_pic_content);
        //文章评论
        view_top_pingtital = header.findViewById(R.id.view_top_pingtital);
        view_below_pingtital = header.findViewById(R.id.view_below_pingtital);
        tv_ping_tital = (TextView) header.findViewById(R.id.tv_ping_tital);
        tv_look_more_ping = (TextView) header.findViewById(R.id.tv_look_more_ping);
        mlv_article_pinglun = (MyListView) header.findViewById(R.id.mlv_article_pinglun);
        //底部评论点赞
        rl_article_below = (RelativeLayout) header.findViewById(R.id.rl_article_below);
        iv_bang = (ImageView) header.findViewById(R.id.iv_bang);
        iv_xing = (ImageView) header.findViewById(R.id.iv_xing);
        tv_bang = (TextView) header.findViewById(R.id.tv_bang);
        tv_xing = (TextView) header.findViewById(R.id.tv_xing);
        tv_ping = (TextView) header.findViewById(R.id.tv_ping);
        iv_ping = (ImageView) header.findViewById(R.id.iv_ping);
        //底部评论
        rl_edit = (RelativeLayout) findViewById(R.id.rl_edit);
        rl_more_add = (RelativeLayout) findViewById(R.id.rl_more_add);
        cet_clearedit = (ClearEditText) findViewById(R.id.cet_clearedit);
        //外部的点赞，收藏
        iv_wai_bang = (ImageView) findViewById(R.id.iv_wai_bang);
        tv_wai_bang = (TextView) findViewById(R.id.tv_wai_bang);
        iv_wai_xing = (ImageView) findViewById(R.id.iv_wai_xing);
        tv_wai_xing = (TextView) findViewById(R.id.tv_wai_xing);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("文章详情");
        wid_screen = PublicUtils.getScreenWidth(this);
        buildRecycler();
        getArticleDetails();
        //获取文章评论信息
        getArticlePingList(article_id);
    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        tv_people_guanzhu.setOnClickListener(this);
        iv_xing.setOnClickListener(this);
        iv_wai_xing.setOnClickListener(this);
        tv_xing.setOnClickListener(this);
        tv_wai_xing.setOnClickListener(this);
        tv_bang.setOnClickListener(this);
        tv_wai_bang.setOnClickListener(this);
        iv_bang.setOnClickListener(this);
        iv_wai_bang.setOnClickListener(this);
        iv_ping.setOnClickListener(this);
        tv_ping.setOnClickListener(this);
        tv_look_more_ping.setOnClickListener(this);

        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                Log.d("test", "显示");
                rl_more_add.setVisibility(View.GONE);
            }

            @Override
            public void keyBoardHide(int height) {
                Log.d("test", "隐藏");
                reply_id = "";
                rl_more_add.setVisibility(View.VISIBLE);
            }
        });

        cet_clearedit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String searchContext = cet_clearedit.getText().toString().trim();
                    if (TextUtils.isEmpty(searchContext.trim())) {
                        ToastUtils.showCenter(ArticleDetailsActivity.this, "请添加回复内容");
                    } else {
                        cet_clearedit.setText("");
                        // 先隐藏键盘
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(ArticleDetailsActivity.this.getCurrentFocus()
                                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        //隐藏评论输入框布局
//                        rl_edit.setVisibility(View.GONE);

                        if (TextUtils.isEmpty(reply_id.trim())) {
                            //评论文章
                            goPingAll(searchContext);
                        } else {
                            //回复评论
                            goPingSingle(reply_id, searchContext);
                        }
                    }
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ArticleDetailsActivity.this.finish();
                break;
            case R.id.tv_people_guanzhu:
                if (userCenterInfoBean != null) {
                    switch (guanzhuTag) {
                        case 1:
                            getGuanZhu();
                            break;
                        case 2:
                            getQuXiao();
                            break;
                        case 3:
                            getQuXiao();
                            break;
                    }
                }
                break;
            case R.id.iv_bang:
            case R.id.iv_wai_bang:
            case R.id.tv_bang:
            case R.id.tv_wai_bang:

                if (ifZan) {
                    addZan();
                    ifZan = false;
                } else {
                    removeZan();
                    ifZan = true;
                }
                break;
            case R.id.iv_xing:
            case R.id.iv_wai_xing:
            case R.id.tv_wai_xing:
            case R.id.tv_xing:

                if (ifShouCang) {
                    addShouCang();
                    ifShouCang = false;
                } else {
                    removeShouCang();
                    ifShouCang = true;
                }
                break;
            case R.id.iv_ping:
            case R.id.tv_ping:
                rl_edit.setVisibility(View.VISIBLE);
                cet_clearedit.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) cet_clearedit.getContext().getSystemService(ArticleDetailsActivity.this.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                break;
            case R.id.tv_look_more_ping:
                getArticlePingList(article_id);
                break;
        }
    }

    @Override
    public void onLoadMore() {

        ++likepage_num;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        getLikeArticle();

    }

    private void buildRecycler() {
        MultiItemTypeSupport<ArticleLikeItemBean> support = new MultiItemTypeSupport<ArticleLikeItemBean>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_article_like;
            }

            @Override
            public int getItemViewType(int position, ArticleLikeItemBean s) {
                return 0;
            }
        };
        mAdapter = new MultiItemCommonAdapter<ArticleLikeItemBean>(ArticleDetailsActivity.this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {

                ((TextView) holder.getView(R.id.tv_article_name)).setText(mListData.get(position).getArticle_info().getTitle());
                ((TextView) holder.getView(R.id.tv_article_details)).setText(mListData.get(position).getArticle_info().getSummary());
                ((TextView) holder.getView(R.id.tv_youlan_num)).setText(mListData.get(position).getArticle_info().getView_num());
                ImageUtils.displayFilletImage(mListData.get(position).getArticle_info().getImage().getImg0(),
                        (ImageView) holder.getView(R.id.iv_article_image));
                holder.getView(R.id.rl_item_click).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ArticleDetailsActivity.this, ArticleDetailsActivity.class);
                        intent.putExtra("article_id", mListData.get(position).getArticle_info().getArticle_id());
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.addHeaderView(header);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ArticleDetailsActivity.this));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
        onLoadMore();
    }

    //0文章详情-猜你喜欢
    private void getLikeArticle() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                ToastUtils.showCenter(ArticleDetailsActivity.this, "你可能喜欢的文章加载失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ArticleLikeBean articleLikeBean = GsonUtil.jsonToBean(data_msg, ArticleLikeBean.class);
                        List<ArticleLikeItemBean> list = articleLikeBean.getArticle_list();
                        if (list != null && list.size() > 0) {
                            updateViewFromData(list);
                        } else {
                            updateViewFromData(null);
                        }


                    } else {
                        ToastUtils.showCenter(ArticleDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getLikeArticleList(article_id, (likepage_num - 1) * 5, 10, callBack);
    }

    //1.文章详情
    private void getArticleDetails() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ArticleDetailsActivity.this, "收藏失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        articleBean = GsonUtil.jsonToBean(data_msg, ArticleBean.class);
                        changeArticleUI(articleBean);
                    } else {
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getArticleDetails(article_id, callBack);

    }

    //1.更新文章详情
    private void changeArticleUI(ArticleBean articleBean) {

        if (articleBean != null &&
                articleBean.getArticle_info() != null &&
                articleBean.getArticle_info().getArticle_id() != null) {
            //获取作者信息
            getUserInfo(articleBean.getArticle_info().getUser_id());
            //标题，阅读数，引言
            tv_article_tital.setText(articleBean.getArticle_info().getTitle());
            tv_readnum_num.setText(articleBean.getArticle_info().getView_num());
            if (TextUtils.isEmpty(articleBean.getArticle_info().getSummary())) {
                rl_yinyan_tital_wai.setVisibility(View.GONE);
            } else {
                rl_yinyan_tital_wai.setVisibility(View.VISIBLE);
                tv_yinyan_content.setText(articleBean.getArticle_info().getTitle());
            }
            //文章的图文
            if (articleBean.getItem_list() != null && articleBean.getItem_list().size() > 0) {
                MyArticlePicAdapter articlePicAdapter = new MyArticlePicAdapter(ArticleDetailsActivity.this, articleBean, wid_screen);
                mlv_article_pic_content.setAdapter(articlePicAdapter);
            }
            //显示评论点赞分享布局
            rl_article_below.setVisibility(View.GONE);
            //点赞样式
            if (articleBean.getArticle_info().getIs_liked() == 1) {//已赞
                iv_bang.setImageResource(R.drawable.bang1);
                tv_bang.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                iv_wai_bang.setImageResource(R.drawable.bang1);
                tv_wai_bang.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                ifZan = false;
            } else {//未赞
                iv_bang.setImageResource(R.drawable.bang);
                tv_bang.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                iv_wai_bang.setImageResource(R.drawable.bang);
                tv_wai_bang.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                ifZan = true;
            }
            tv_bang.setText(articleBean.getArticle_info().getLike_num());
            tv_wai_bang.setText(articleBean.getArticle_info().getLike_num());
            //收藏样式
            if (articleBean.getArticle_info().getIs_collected() == 1) {//已收藏
                iv_xing.setImageResource(R.drawable.xing1);
                tv_xing.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                iv_wai_xing.setImageResource(R.drawable.xing1);
                tv_wai_xing.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                ifShouCang = false;
            } else {//未收藏
                iv_xing.setImageResource(R.drawable.xing);
                tv_xing.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                iv_wai_xing.setImageResource(R.drawable.xing);
                tv_wai_xing.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                ifShouCang = true;
            }
            tv_xing.setText(articleBean.getArticle_info().getCollect_num());
            tv_wai_xing.setText(articleBean.getArticle_info().getCollect_num());
            tv_ping.setText(articleBean.getArticle_info().getComment_num());
        }
    }

    //2.用户详情
    private void getUserInfo(String user_id) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtils.showCenter(ArticleDetailsActivity.this, getString(R.string.userinfo_get_error));

            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        userCenterInfoBean = GsonUtil.jsonToBean(data_msg, UserCenterInfoBean.class);
                        changeUserUI(userCenterInfoBean);
                    } else {

                        ToastUtils.showCenter(ArticleDetailsActivity.this, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getUserInfo(user_id, callBack);

    }

    //2.更新详情
    private void changeUserUI(UserCenterInfoBean userCenterInfoBean) {

        if (userCenterInfoBean != null) {
            ImageUtils.displayRoundImage(userCenterInfoBean.getUser_info().getAvatar().getBig(), riv_people_header);
            tv_people_name.setText(userCenterInfoBean.getUser_info().getNickname());
            if (!userCenterInfoBean.getUser_info().getProfession().trim().equals("0")) {//专业用户
                iv_people_tag.setVisibility(View.VISIBLE);
            } else {
                iv_people_tag.setVisibility(View.INVISIBLE);
            }
            tv_people_details.setText(userCenterInfoBean.getUser_info().getSlogan());
            tv_people_guanzhu.setVisibility(View.VISIBLE);
            if (userCenterInfoBean.getUser_info().getRelation().equals("0")) {//未关注
                guanzhuTag = 1;
                tv_people_guanzhu.setText("关注");
                tv_people_guanzhu.setBackgroundResource(R.drawable.tv_guanzhu_no);
                tv_people_guanzhu.setTextColor(UIUtils.getColor(R.color.bg_e79056));

            } else if (userCenterInfoBean.getUser_info().getRelation().equals("1")) {//已关注
                guanzhuTag = 2;
                tv_people_guanzhu.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                tv_people_guanzhu.setText("已关注");
                tv_people_guanzhu.setBackgroundResource(R.drawable.tv_guanzhu_has);
            } else if (userCenterInfoBean.getUser_info().getRelation().equals("2")) {//相互关注
                guanzhuTag = 3;
                tv_people_guanzhu.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                tv_people_guanzhu.setText("相互关注");
                tv_people_guanzhu.setBackgroundResource(R.drawable.tv_guanzhu_xianghu);
            }
        }

    }

    //3.关注用户
    private void getGuanZhu() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (guanzhuTag == 1) {//未关注（去关注）
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "关注失败");
                } else if (guanzhuTag == 2) {//已关注
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "取消关注失败");
                } else if (guanzhuTag == 3) {//相互关注
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "取消关注失败");
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
                        ToastUtils.showCenter(ArticleDetailsActivity.this, "关注成功");
                        getUserInfo(userCenterInfoBean.getUser_info().getUser_id());
                    } else {
                        ToastUtils.showCenter(ArticleDetailsActivity.this, error_msg);

                    }
                } catch (JSONException e) {
                }
            }
        };
        if (userCenterInfoBean != null) {
            MyHttpManager.getInstance().goGuanZhu(userCenterInfoBean.getUser_info().getUser_id(), callBack);
        }

    }

    //3.取消关注用户
    private void getQuXiao() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtils.showCenter(ArticleDetailsActivity.this, getString(R.string.userinfo_get_error));

            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ToastUtils.showCenter(ArticleDetailsActivity.this, "取消关注成功");
                        getUserInfo(userCenterInfoBean.getUser_info().getUser_id());
                    } else {
                        ToastUtils.showCenter(ArticleDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        if (userCenterInfoBean != null) {
            MyHttpManager.getInstance().goQuXiaoGuanZhu(userCenterInfoBean.getUser_info().getUser_id(), callBack);
        }

    }

    //4.获取文章评论列表
    private void getArticlePingList(String article_id) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ArticleDetailsActivity.this, "文章评论数据获取失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    ++pingpage_num;
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        pingBean = GsonUtil.jsonToBean(data_msg, PingBean.class);
                        List<PingCommentListItemBean> list = pingBean.getComment_list();
                        if (list != null && list.size() > 0) {
                            mListPing.addAll(list);
                            Message msg = new Message();
                            msg.what = 7;
                            mHandler.sendMessage(msg);
                        }
                        if (mListPing.size() > 0) {
                            //显示评论列表
                            view_top_pingtital.setVisibility(View.VISIBLE);
                            tv_ping_tital.setVisibility(View.VISIBLE);
                            view_below_pingtital.setVisibility(View.VISIBLE);
                            mlv_article_pinglun.setVisibility(View.VISIBLE);
                            tv_look_more_ping.setVisibility(View.VISIBLE);
                            if (list.size() < 5) {
                                //隐藏评论加载更多
                                tv_look_more_ping.setVisibility(View.GONE);
                            }
                        }


                    } else {
                        ToastUtils.showCenter(ArticleDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getArticlePingList(article_id, (pingpage_num - 1) * 5, 5, callBack);

    }

    //5.点赞
    private void addZan() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(ArticleDetailsActivity.this, "点赞失败");
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
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ArticleDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "点赞失败");
                }
            }
        };
        if (!TextUtils.isEmpty(article_id)) {
            MyHttpManager.getInstance().addZanArticle(article_id, callBack);
        }
    }

    //5.取消点赞
    private void removeZan() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(ArticleDetailsActivity.this, "取消点赞失败");
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
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ArticleDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "取消点赞失败");
                }
            }
        };
        if (!TextUtils.isEmpty(article_id)) {
            MyHttpManager.getInstance().removeZanArticle(article_id, callBack);
        }
    }

    //6.收藏文章
    private void addShouCang() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ArticleDetailsActivity.this, "收藏失败");
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
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ArticleDetailsActivity.this, "收藏失败");
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "收藏失败");
                }
            }
        };
        if (!TextUtils.isEmpty(article_id)) {
            MyHttpManager.getInstance().addShouCangArticle(article_id, callBack);
        }
    }

    //6.取消收藏文章
    private void removeShouCang() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ArticleDetailsActivity.this, "收藏失败");
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
                        msg.what = 4;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ArticleDetailsActivity.this, "收藏失败");
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "收藏失败");
                }
            }
        };
        if (!TextUtils.isEmpty(article_id)) {
            MyHttpManager.getInstance().removeShouCangArticle(article_id, callBack);
        }
    }

    //7.对文章发表评论
    private void goPingAll(String searchContext) {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ArticleDetailsActivity.this, "评论失败");
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
                        msg.what = 5;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ArticleDetailsActivity.this, "评论失败");
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "评论失败");
                }
            }
        };
        if (!TextUtils.isEmpty(article_id)) {
            MyHttpManager.getInstance().pingArticleAll(article_id, searchContext, callBack);
        }
    }

    //7.回复对文章发表的评论
    private void goPingSingle(String reply_id, String searchContext) {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ArticleDetailsActivity.this, "回复失败");
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
                        msg.what = 6;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ArticleDetailsActivity.this, "回复失败");
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "回复失败");
                }
            }
        };
        MyHttpManager.getInstance().pingArticleSingle(reply_id, searchContext, callBack);
    }

    private void updateViewFromData(List<ArticleLikeItemBean> listData) {

        if (null != listData) {
            position = mListData.size();
            mListData.addAll(listData);
            mAdapter.notifyItem(position, mListData, listData);
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        } else {
            --likepage_num;
            //没有更多数据
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
        }
    }

    //7点击回复单单条评论的回调
    @Override
    public void clickHuiFu(PingCommentListItemBean pingCommentListItemBean) {
        reply_id = pingCommentListItemBean.getComment_info().getComment_id();
        rl_edit.setVisibility(View.VISIBLE);
        cet_clearedit.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) cet_clearedit.getContext().getSystemService(ArticleDetailsActivity.this.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    //8点击评论里面的赞
    @Override
    public void clickAddZan(PingCommentListItemBean pingCommentListItemBean, int position) {

        if (pingCommentListItemBean.getComment_info().getIs_liked() == 1) {//已经点赞
            removeZanPing(pingCommentListItemBean, position);
//            mListPing.get(position).getComment_info().setIs_liked(0);
        } else {//未点赞
            addZanPing(pingCommentListItemBean, position);
//            mListPing.get(position).getComment_info().setIs_liked(1);
        }
        myArticlePingAdapter.notifyDataSetChanged();

    }

    //8点赞
    public void addZanPing(PingCommentListItemBean pingCommentListItemBean, final int position) {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(ArticleDetailsActivity.this, "点赞失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        reflushPingList(true,position);
                    } else {
                        ToastUtils.showCenter(ArticleDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "点赞失败");
                }
            }
        };
        MyHttpManager.getInstance().addPingZan(pingCommentListItemBean.getComment_info().getComment_id(), callBack);
    }

    //8取消点赞
    public void removeZanPing(PingCommentListItemBean pingCommentListItemBean, final int position) {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(ArticleDetailsActivity.this, "点赞失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        reflushPingList(false,position);
                    } else {
                        ToastUtils.showCenter(ArticleDetailsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "点赞失败");
                }
            }
        };
        MyHttpManager.getInstance().removePingZan(pingCommentListItemBean.getComment_info().getComment_id(), callBack);

    }

    private void reflushPingList(boolean ifZan, int position) {
        if(ifZan){//被点赞
            mListPing.get(position).getComment_info().setIs_liked(1);
        }else {//被取消点赞
            mListPing.get(position).getComment_info().setIs_liked(0);
        }
        myArticlePingAdapter.notifyDataSetChanged();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int tag = msg.what;
            switch (tag) {
                case 1://点赞
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "你很棒棒哦");
                    iv_bang.setImageResource(R.drawable.bang1);
                    tv_bang.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                    iv_wai_bang.setImageResource(R.drawable.bang1);
                    tv_wai_bang.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                    ifZan = false;
                    getArticleDetails();
                    break;
                case 2://取消点赞
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "还是收回我的家图棒吧");
                    iv_bang.setImageResource(R.drawable.bang);
                    tv_bang.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                    iv_wai_bang.setImageResource(R.drawable.bang);
                    tv_wai_bang.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                    ifZan = true;
                    getArticleDetails();
                    break;
                case 3://收藏
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "收藏成功");
                    iv_xing.setImageResource(R.drawable.xing1);
                    tv_xing.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                    iv_wai_xing.setImageResource(R.drawable.xing1);
                    tv_wai_xing.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                    ifShouCang = false;
                    getArticleDetails();
                    break;
                case 4://取消收藏
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "取消收藏成功");
                    iv_xing.setImageResource(R.drawable.xing);
                    tv_xing.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                    iv_wai_xing.setImageResource(R.drawable.xing);
                    tv_wai_xing.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                    ifShouCang = false;
                    getArticleDetails();
                    break;
                case 5://评论文章
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "评论成功");
                    pingpage_num = 1;
                    mListPing.clear();
                    getArticlePingList(article_id);
                    getArticleDetails();
                    break;
                case 6://回复文章评论
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "回复成功");
                    pingpage_num = 1;
                    mListPing.clear();
                    getArticlePingList(article_id);
                    getArticleDetails();
                    break;
                case 7://刷新评论列表
                    myArticlePingAdapter = new MyArticlePingAdapter(ArticleDetailsActivity.this, mListPing, pingBean.getArticle_info().getUser_id(), ArticleDetailsActivity.this);
                    mlv_article_pinglun.setAdapter(myArticlePingAdapter);
                    break;
            }
        }
    };
    private RelativeLayout rl_more_add;
    private View header;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private String article_id;
    private ArticleBean articleBean;
    private UserCenterInfoBean userCenterInfoBean;
    private RoundImageView riv_people_header;
    private TextView tv_people_name;
    private ImageView iv_people_tag;
    private TextView tv_people_details;
    private TextView tv_people_guanzhu;
    private int guanzhuTag;
    private int position;
    private HRecyclerView mRecyclerView;
    private MultiItemCommonAdapter<ArticleLikeItemBean> mAdapter;
    private List<ArticleLikeItemBean> mListData = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;

    //header（文章内容部分）
    private MyListView mlv_article_pic_content;
    private TextView tv_article_tital;
    private TextView tv_readnum_num;
    private TextView tv_yinyan_content;
    private RelativeLayout rl_yinyan_tital_wai;
    private int wid_screen;
    //header（文章评论部分）
    private View view_top_pingtital;
    private TextView tv_ping_tital;
    private View view_below_pingtital;
    private MyListView mlv_article_pinglun;
    private TextView tv_look_more_ping;
    private int pingpage_num = 1;
    private int likepage_num = 0;

    //底部点赞等信息
    private boolean ifZan = false;
    private boolean ifShouCang = false;
    private RelativeLayout rl_article_below;
    private ImageView iv_bang;
    private TextView tv_bang;
    private TextView tv_xing;
    private ImageView iv_xing;
    private TextView tv_ping;
    private ImageView iv_ping;
    private ImageView iv_wai_bang;
    private TextView tv_wai_bang;
    private ImageView iv_wai_xing;
    private TextView tv_wai_xing;
    private RelativeLayout rl_edit;
    private ClearEditText cet_clearedit;
    private PingBean pingBean;
    private MyArticlePingAdapter myArticlePingAdapter;
    private List<PingCommentListItemBean> mListPing = new ArrayList<>();


    private String reply_id = "";
}
