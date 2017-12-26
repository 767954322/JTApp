package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.cailike.ImageLikeItemBean;
import com.homechart.app.home.bean.cailike.LikeDataBean;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.home.bean.imagedetail.ImageDetailBean;
import com.homechart.app.home.bean.search.SearchDataBean;
import com.homechart.app.home.bean.searchfservice.TypeNewBean;
import com.homechart.app.home.fragment.ImageDetailFragment;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.CustomViewPager;
import com.homechart.app.myview.HomeSharedPopWinPublic;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/26.
 */

public class ImageDetailScrollActivity
        extends BaseActivity
        implements View.OnClickListener,
        ImageDetailFragment.UserInfo,
        HomeSharedPopWinPublic.ClickInter {
    private String item_id;
    private String mUserId;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private int mPosition;
    private List<String> mItemIdList;
    private CustomViewPager mViewPager;
    private MyImagePageAdater mAdapter;
    private TextView tv_content_right;
    private ImageButton nav_secondary_imageButton;
    private ImageDetailBean mImageDetailBean;
    private HomeSharedPopWinPublic homeSharedPopWinPublic;
    private String type;
    private int wei = 0;


    //公用的判断是否正在加载中
    private boolean more_loding = false;
    //筛选
    private Map<Integer, ColorItemBean> mSelectListData;
    private String shaixuan_tag;
    private int shuaixuan_page_num = 2;
    //你可能喜欢
    private String like_maybe_id = "";
    private int like_maybe_page_num = 2;
    private boolean if_click_color;
    public boolean ifShowColorList = true;
    private TypeNewBean typeNewBean;
    private RelativeLayout rl_back_unclick;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_detail_scroll;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        item_id = getIntent().getStringExtra("item_id");
        type = getIntent().getStringExtra("type");
        mPosition = getIntent().getIntExtra("position", -1);
        mItemIdList = (List<String>) getIntent().getSerializableExtra("item_id_list");
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
        if_click_color = getIntent().getBooleanExtra("if_click_color", false);

        if (if_click_color) {
            ifShowColorList = true;
        } else {
            ifShowColorList = false;
        }
    }

    @Override
    protected void initView() {

        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        rl_back_unclick = (RelativeLayout) findViewById(R.id.rl_back_unclick);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        nav_secondary_imageButton = (ImageButton) findViewById(R.id.nav_secondary_imageButton);
        mViewPager = (CustomViewPager) findViewById(R.id.vp_viewpager);
        homeSharedPopWinPublic = new HomeSharedPopWinPublic(ImageDetailScrollActivity.this, ImageDetailScrollActivity.this);

    }

    int before_position = 0;
    boolean ifFirst = true;
    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        mViewPager.setScanScroll(true);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(ifFirst){
                    before_position = position;
                    ifFirst = false;
                }else {
                    if(position - before_position > 0){//向右滑动了
                        //友盟统计
                        HashMap<String, String> map1 = new HashMap<String, String>();
                        map1.put("evenname", "右滑");
                        map1.put("even", "图片详情页");
                        MobclickAgent.onEvent(ImageDetailScrollActivity.this, "shijian10", map1);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("图片详情页")  //事件类别
                                .setAction("右滑")      //事件操作
                                .build());
                    }else {//向左滑动了
                        //友盟统计
                        HashMap<String, String> map1 = new HashMap<String, String>();
                        map1.put("evenname", "左滑");
                        map1.put("even", "图片详情页");
                        MobclickAgent.onEvent(ImageDetailScrollActivity.this, "shijian11", map1);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("图片详情页")  //事件类别
                                .setAction("左滑")      //事件操作
                                .build());
                    }
                }


                ifShowColorList = false;
                if (mItemIdList.size() >= position && (mItemIdList.size() - position) < 3 && !more_loding) {
                    if (!TextUtils.isEmpty(type) && type.equals("筛选")) {
                        getMoreShaiXuan();
                    } else if (!TextUtils.isEmpty(type) && type.equals("你可能喜欢")) {
                        getMoreLikeMayBe();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        tv_tital_comment.setText("图片详情");
        mAdapter = new MyImagePageAdater(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.setOffscreenPageLimit(3);

        if (!TextUtils.isEmpty(type)) {
            if (type.equals("筛选")) {
                mSelectListData = (Map<Integer, ColorItemBean>) getIntent().getSerializableExtra("mSelectListData");
                shaixuan_tag = getIntent().getStringExtra("shaixuan_tag");
                shuaixuan_page_num = getIntent().getIntExtra("page_num", 2);
                if (mItemIdList.size() == 20 * (shuaixuan_page_num - 1)) {
                    getMoreShaiXuan();
                }
            } else if (type.equals("你可能喜欢")) {
                like_maybe_id = getIntent().getStringExtra("like_id");
                like_maybe_page_num = getIntent().getIntExtra("page_num", 2);
                if (mItemIdList.size() == 20 * (like_maybe_page_num - 1)) {
                    getMoreLikeMayBe();
                }
            } else if (type.equals("色彩")) {
                mSelectListData = (Map<Integer, ColorItemBean>) getIntent().getSerializableExtra("mSelectListData");
                shaixuan_tag = getIntent().getStringExtra("shaixuan_tag");
                shuaixuan_page_num = getIntent().getIntExtra("page_num", 2);
                if (mItemIdList.size() == 20 * (shuaixuan_page_num - 1)) {
                    getMoreShaiXuan();
                }
            }
        }
        getTypeData();

    }
    private void getTypeData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        typeNewBean = GsonUtil.jsonToBean(data_msg, TypeNewBean.class);
                    } else {
                        ToastUtils.showCenter(ImageDetailScrollActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getShopTypes(callBack);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ImageDetailScrollActivity.this.finish();
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("图片详情页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("图片详情页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("图片详情页");
        MobclickAgent.onPause(this);
    }

    //分享
    @Override
    public void onClickWeiXin() {

        sharedItemOpen(SHARE_MEDIA.WEIXIN);
    }

    //分享
    @Override
    public void onClickPYQ() {

        sharedItemOpen(SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    //分享
    @Override
    public void onClickWeiBo() {

        sharedItemOpen(SHARE_MEDIA.SINA);
    }

    @Override
    public void onClickQQ() {

        sharedItemOpen(SHARE_MEDIA.QQ);
    }

    //分享
    private void sharedItemOpen(SHARE_MEDIA share_media) {

        if (share_media == SHARE_MEDIA.WEIXIN) {
            //友盟统计
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("evenname", "分享图片");
            if (wei == 0) {
                map1.put("even", "图片详情+上+微信好友");
            } else {
                map1.put("even", "图片详情+下+微信好友");
            }
            MobclickAgent.onEvent(ImageDetailScrollActivity.this, "jtaction7", map1);
            //ga统计
            if (wei == 0) {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("图片详情+上+微信好友")  //事件类别
                        .setAction("分享图片")      //事件操作
                        .build());
            } else {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("图片详情+下+微信好友")  //事件类别
                        .setAction("分享图片")      //事件操作
                        .build());
            }


        } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
            //友盟统计
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("evenname", "分享图片");
            if (wei == 0) {
                map1.put("even", "图片详情+上+微信朋友圈");
            } else {
                map1.put("even", "图片详情+下+微信朋友圈");
            }
            MobclickAgent.onEvent(ImageDetailScrollActivity.this, "jtaction7", map1);
            //ga统计
            if (wei == 0) {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("图片详情+上+微信朋友圈")  //事件类别
                        .setAction("分享图片")      //事件操作
                        .build());
            } else {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("图片详情+下+微信朋友圈")  //事件类别
                        .setAction("分享图片")      //事件操作
                        .build());
            }
        } else if (share_media == SHARE_MEDIA.SINA) {
            //友盟统计
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("evenname", "分享图片");
            if (wei == 0) {
                map1.put("even", "图片详情+上+新浪微博");
            } else {
                map1.put("even", "图片详情+下+新浪微博");
            }
            MobclickAgent.onEvent(ImageDetailScrollActivity.this, "jtaction7", map1);
            //ga统计
            if (wei == 0) {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("图片详情+上+新浪微博")  //事件类别
                        .setAction("分享图片")      //事件操作
                        .build());
            } else {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("图片详情+下+新浪微博")  //事件类别
                        .setAction("分享图片")      //事件操作
                        .build());
            }
        }else if (share_media == SHARE_MEDIA.QQ) {
            //友盟统计
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("evenname", "分享图片");
            if (wei == 0) {
                map1.put("even", "图片详情+上+QQ");
            } else {
                map1.put("even", "图片详情+下+QQ");
            }
            MobclickAgent.onEvent(ImageDetailScrollActivity.this, "jtaction7", map1);
            //ga统计
            if (wei == 0) {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("图片详情+上+QQ")  //事件类别
                        .setAction("分享图片")      //事件操作
                        .build());
            } else {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("图片详情+下+QQ")  //事件类别
                        .setAction("分享图片")      //事件操作
                        .build());
            }
        }
        UMImage image = new UMImage(ImageDetailScrollActivity.this, mImageDetailBean.getItem_info().getImage().getImg0());
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        UMWeb web = new UMWeb("http://h5.idcool.com.cn/photo/" + mImageDetailBean.getItem_info().getItem_id());
        web.setTitle("「" + mImageDetailBean.getUser_info().getNickname() + "」在晒家｜家图APP");//标题
        web.setThumb(image);  //缩略图
        String desi = mImageDetailBean.getItem_info().getDescription() + mImageDetailBean.getItem_info().getTag();
        if (desi.length() > 160) {
            desi = desi.substring(0, 160) + "...";
        }
        web.setDescription(desi);//描述
        new ShareAction(ImageDetailScrollActivity.this).
                setPlatform(share_media).
                withMedia(web).
                setCallback(umShareListener).share();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    //分享
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform == SHARE_MEDIA.WEIXIN) {
                ToastUtils.showCenter(ImageDetailScrollActivity.this, "微信好友分享成功啦");
            } else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
                ToastUtils.showCenter(ImageDetailScrollActivity.this, "微信朋友圈分享成功啦");
            } else if (platform == SHARE_MEDIA.SINA) {
                ToastUtils.showCenter(ImageDetailScrollActivity.this, "新浪微博分享成功啦");
            } else if (platform == SHARE_MEDIA.QQ) {
                ToastUtils.showCenter(ImageDetailScrollActivity.this, "QQ分享成功啦");
            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.showCenter(ImageDetailScrollActivity.this, "分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showCenter(ImageDetailScrollActivity.this, "分享取消了");
        }
    };

    //回调
    @Override
    public void getUserInfo(ImageDetailBean imageDetailBean) {

        this.mImageDetailBean = imageDetailBean;
        if (null != imageDetailBean && null != imageDetailBean.getUser_info() && null != imageDetailBean.getUser_info().getUser_id() && !TextUtils.isEmpty(mUserId)) {
            if (!imageDetailBean.getUser_info().getUser_id().trim().equals(mUserId.trim())) {
                nav_secondary_imageButton.setVisibility(View.VISIBLE);
                tv_content_right.setVisibility(View.GONE);
                nav_secondary_imageButton.setImageResource(R.drawable.shared_icon);
            } else {
                tv_content_right.setVisibility(View.VISIBLE);
                nav_secondary_imageButton.setVisibility(View.GONE);
                tv_content_right.setText("编辑");
            }
        }

    }

    //获取更多筛选图片
    private void getMoreShaiXuan() {
        more_loding = true;
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
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
                            ++shuaixuan_page_num;
                            for (int i = 0; i < searchDataBean.getItem_list().size(); i++) {
                                mItemIdList.add(searchDataBean.getItem_list().get(i).getItem_info().getItem_id());
                            }
                            mAdapter.notifyDataSetChanged();
                            more_loding = false;
//                            mViewPager.setCurrentItem(mViewPager.getCurrentItem());
                        }
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getSearchList(mSelectListData, "", shaixuan_tag, (shuaixuan_page_num - 1) * 20 + "", "20", callBack);


    }

    //获取更多你可能喜欢
    private void getMoreLikeMayBe() {
        more_loding = true;
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ++like_maybe_page_num;
                        String data = "{\"data\": " + data_msg + "}";
                        LikeDataBean likeDataBean = GsonUtil.jsonToBean(data, LikeDataBean.class);

                        if (null != likeDataBean.getData().getItem_list() && likeDataBean.getData().getItem_list().size() != 0) {
                            List<ImageLikeItemBean> list = likeDataBean.getData().getItem_list();
                            for (int i = 0; i < list.size(); i++) {
                                mItemIdList.add(list.get(i).getItem_info().getItem_id());
                            }
                            mAdapter.notifyDataSetChanged();
                            more_loding = false;
                        }
                    } else {
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().caiLikeImage(like_maybe_id, (like_maybe_page_num - 1) * 20 + "", 20 + "", callBack);
    }

    public class MyImagePageAdater extends FragmentStatePagerAdapter {

        public MyImagePageAdater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ImageDetailFragment(mItemIdList.get(position), ImageDetailScrollActivity.this);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return mItemIdList.size();
        }
    }

    public TypeNewBean getTypeNewBean() {
        return typeNewBean;
    }

    public void setTypeNewBean(TypeNewBean typeNewBean) {
        this.typeNewBean = typeNewBean;
    }

    public  void  setTitalClickAble(boolean boo){
        if(boo){
            rl_back_unclick.setVisibility(View.GONE);
        }else {
            rl_back_unclick.setVisibility(View.VISIBLE);
        }

    }
    public  void  setViewPagerScrollAble(boolean boo){
        if(boo){
            mViewPager.setScanScroll(true);
        }else {
            mViewPager.setScanScroll(false);
        }

    }
}
