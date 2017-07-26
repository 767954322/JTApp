package com.homechart.app.home.activity;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.adapter.MyArticlePicAdapter;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.articledetails.ArticleBean;
import com.homechart.app.home.bean.cailike.ImageLikeItemBean;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.MyListView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gumenghao on 17/7/18.
 * 文章详情页
 */

public class ArticleDetailsActivity
        extends BaseActivity
        implements View.OnClickListener,
        OnLoadMoreListener {


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

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("文章详情");
        wid_screen = PublicUtils.getScreenWidth(this);
        buildRecycler();
        getArticleDetails();
    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        tv_people_guanzhu.setOnClickListener(this);
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
        }
    }

    @Override
    public void onLoadMore() {

    }

    private void buildRecycler() {
        MultiItemTypeSupport<ImageLikeItemBean> support = new MultiItemTypeSupport<ImageLikeItemBean>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_like_pic;
            }

            @Override
            public int getItemViewType(int position, ImageLikeItemBean s) {
                return 0;
            }
        };
        mAdapter = new MultiItemCommonAdapter<ImageLikeItemBean>(ArticleDetailsActivity.this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {
            }
        };
        mRecyclerView.addHeaderView(header);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
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
                MyArticlePicAdapter articlePicAdapter = new MyArticlePicAdapter(ArticleDetailsActivity.this, articleBean,wid_screen);
                mlv_article_pic_content.setAdapter(articlePicAdapter);
            }

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

    //＊收藏文章
    private void shoucang() {
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
                        ToastUtils.showCenter(ArticleDetailsActivity.this, "收藏成功");
                    } else {
                        ToastUtils.showCenter(ArticleDetailsActivity.this, "收藏失败");
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ArticleDetailsActivity.this, "收藏失败");
                }
            }
        };
        MyHttpManager.getInstance().addShouCangArticle(article_id, callBack);
    }

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
    private HRecyclerView mRecyclerView;
    private MultiItemCommonAdapter<ImageLikeItemBean> mAdapter;
    private List<ImageLikeItemBean> mListData = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;

    //header
    private MyListView mlv_article_pic_content;
    private TextView tv_article_tital;
    private TextView tv_readnum_num;
    private TextView tv_yinyan_content;
    private RelativeLayout rl_yinyan_tital_wai;
    private int wid_screen;

}
