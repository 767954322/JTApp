package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.cailike.ImageLikeItemBean;
import com.homechart.app.home.bean.cailike.LikeDataBean;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.home.bean.imagedetail.ImageDetailBean;
import com.homechart.app.home.bean.search.SearchDataBean;
import com.homechart.app.home.bean.searchfservice.TypeNewBean;
import com.homechart.app.myview.CustomViewPager;
import com.homechart.app.myview.HomeSharedPopWinPublic;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class NewImageDetailsFragment
        extends BaseFragment
        implements View.OnClickListener,
        NewImageDetaiScrollFragment.UserInfo {


    private String item_id;
    private String mUserId;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private int mPosition;
    private List<String> mItemIdList;
    private CustomViewPager mViewPager;
    private MyImagePageAdater mAdapter;
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
    private TypeNewBean typeNewBean;
    private RelativeLayout rl_back_unclick;
    private ImageButton iv_edit_image;
    private ImageButton iv_shared_image;
    private ImageButton iv_more_image;
    private TextView tv_lingganji;
    private ImageButton nav_left_imageButton_tital;
    private TextView tv_tital;

    private FragmentManager fragmentManager;
    int before_position = 0;
    boolean ifFirst = true;
    private Bundle mBundle;

    public NewImageDetailsFragment() {
    }

    public NewImageDetailsFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_new_imagedetails;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        mBundle = getArguments();

        item_id = (String) mBundle.getString("item_id");
        type = (String) mBundle.getString("type");
        mPosition = mBundle.getInt("position", -1);
        mItemIdList = (List<String>) mBundle.getSerializable("item_id_list");
    }

    @Override
    protected void initView() {


        nav_left_imageButton = (ImageButton) rootView.findViewById(R.id.nav_left_imageButton);
        nav_left_imageButton_tital = (ImageButton) rootView.findViewById(R.id.nav_left_imageButton_tital);
        rl_back_unclick = (RelativeLayout) rootView.findViewById(R.id.rl_back_unclick);
        tv_tital_comment = (TextView) rootView.findViewById(R.id.tv_tital_comment);
        tv_tital = (TextView) rootView.findViewById(R.id.tv_tital);
        iv_edit_image = (ImageButton) rootView.findViewById(R.id.iv_edit_image);
        iv_shared_image = (ImageButton) rootView.findViewById(R.id.iv_shared_image);
        iv_more_image = (ImageButton) rootView.findViewById(R.id.iv_more_image);
        tv_lingganji = (TextView) rootView.findViewById(R.id.tv_lingganji);
        mViewPager = (CustomViewPager) rootView.findViewById(R.id.vp_viewpager);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        nav_left_imageButton_tital.setOnClickListener(this);
        mViewPager.setScanScroll(true);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (ifFirst) {
                    before_position = position;
                    ifFirst = false;
                } else {
                    if (position - before_position > 0) {//向右滑动了
                        ++mPosition;
                        //友盟统计
                        HashMap<String, String> map1 = new HashMap<String, String>();
                        map1.put("evenname", "右滑");
                        map1.put("even", "图片详情页");
                        MobclickAgent.onEvent(activity, "shijian10", map1);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("图片详情页")  //事件类别
                                .setAction("右滑")      //事件操作
                                .build());
                    } else {//向左滑动了
                        --mPosition;
                        //友盟统计
                        HashMap<String, String> map1 = new HashMap<String, String>();
                        map1.put("evenname", "左滑");
                        map1.put("even", "图片详情页");
                        MobclickAgent.onEvent(activity, "shijian11", map1);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("图片详情页")  //事件类别
                                .setAction("左滑")      //事件操作
                                .build());
                    }
                }

                if (mItemIdList.size() >= position && (mItemIdList.size() - position) < 4 && !more_loding) {
                    if (!TextUtils.isEmpty(type) && (type.equals("筛选") || type.equals("色彩"))) {
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
        tv_tital.setText("你可能喜欢");
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
        mAdapter = new MyImagePageAdater(fragmentManager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.setOffscreenPageLimit(3);

        if (!TextUtils.isEmpty(type)) {
            if (type.equals("筛选")) {
                mSelectListData = (Map<Integer, ColorItemBean>) mBundle.getSerializable("mSelectListData");
                shaixuan_tag = mBundle.getString("shaixuan_tag");
                shuaixuan_page_num = mBundle.getInt("page_num", 2);
                if (mItemIdList.size() == 20 * (shuaixuan_page_num - 1)) {
                    getMoreShaiXuan();
                }
            } else if (type.equals("你可能喜欢")) {
                like_maybe_id = mBundle.getString("like_id");
                like_maybe_page_num = mBundle.getInt("page_num", 2);
                if (mItemIdList.size() == 20 * (like_maybe_page_num - 1)) {
                    getMoreLikeMayBe();
                }
            } else if (type.equals("色彩")) {
                mSelectListData = (Map<Integer, ColorItemBean>) mBundle.getSerializable("mSelectListData");
                shaixuan_tag = mBundle.getString("shaixuan_tag");
                shuaixuan_page_num = mBundle.getInt("page_num", 2);
                getMoreShaiXuan();
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
                        ToastUtils.showCenter(activity, error_msg);
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
            case R.id.nav_left_imageButton_tital:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(activity);
        MobclickAgent.onPageStart("图片详情页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("图片详情页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("图片详情页");
        MobclickAgent.onPause(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }

    //回调
    @Override
    public void getUserInfo(ImageDetailBean imageDetailBean) {
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
        this.mImageDetailBean = imageDetailBean;
        if (!TextUtils.isEmpty(mUserId)
                && null != imageDetailBean
                && null != imageDetailBean.getUser_info()
                && null != imageDetailBean.getUser_info().getUser_id()
                && imageDetailBean.getUser_info().getUser_id().equals(mUserId)) {
            iv_edit_image.setVisibility(View.VISIBLE);
            iv_shared_image.setVisibility(View.VISIBLE);
            iv_more_image.setVisibility(View.VISIBLE);
            tv_lingganji.setVisibility(View.VISIBLE);
        } else {
            iv_edit_image.setVisibility(View.GONE);
            iv_shared_image.setVisibility(View.VISIBLE);
            iv_more_image.setVisibility(View.VISIBLE);
            tv_lingganji.setVisibility(View.VISIBLE);
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
            return new NewImageDetaiScrollFragment(mItemIdList.get(position), NewImageDetailsFragment.this, position);
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

    public String getCurrentItemId() {
        return mItemIdList.get(mViewPager.getCurrentItem());
    }

    public int getCurrentPosition() {
        return mViewPager.getCurrentItem();
    }

    public List<String> getmItemIdList() {
        return mItemIdList;
    }

    public TypeNewBean getTypeNewBean() {
        return typeNewBean;
    }

    public void setTypeNewBean(TypeNewBean typeNewBean) {
        this.typeNewBean = typeNewBean;
    }

    public void setTitalClickAble(boolean boo) {
        if (boo) {
            rl_back_unclick.setVisibility(View.GONE);
        } else {
            rl_back_unclick.setVisibility(View.VISIBLE);
        }

    }

    public void setViewPagerScrollAble(boolean boo) {
        if (boo) {
            mViewPager.setScanScroll(true);
        } else {
            mViewPager.setScanScroll(false);
        }

    }

}