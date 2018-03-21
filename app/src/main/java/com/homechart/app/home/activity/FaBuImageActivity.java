package com.homechart.app.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.faxiantags.FaXianTagBean;
import com.homechart.app.home.bean.faxiantags.TagListItemBean;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.pictag.TagItemDataChildBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationBean;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationListBean;
import com.homechart.app.lingganji.ui.activity.InspirationCreateActivity;
import com.homechart.app.myview.FlowLayoutFaBu;
import com.homechart.app.myview.RoundJiaoImageView;
import com.homechart.app.myview.SerializableHashMap;
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
import com.homechart.app.utils.glide.GlideImgManager;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 18/3/14.
 */

public class FaBuImageActivity
        extends BaseActivity
        implements View.OnClickListener,
        FlowLayoutFaBu.OnTagClickListener,
        OnLoadMoreListener,
        OnRefreshListener,
        View.OnTouchListener {

    private View view;
    private Context mContext;
    private String mUserId;
    private RelativeLayout mMain;
    private ImageView mDismiss;
    private RoundJiaoImageView mIVLingGan;
    private View mHeaderInspiration;
    private HRecyclerView mRecyclerView;
    private MultiItemCommonAdapter<InspirationBean> mAdapter;
    private List<InspirationBean> mListData = new ArrayList<>();
    private LoadMoreFooterView mLoadMoreFooterView;
    private final String REFRESH_STATUS = "refresh";
    private final String LOADMORE_STATUS = "loadmore";
    private int page_num = 1;
    private int position;
    private int defalsePosition = 0;
    private RelativeLayout mRLAddInspiration;
    private TextView mTVSureAdd;
    private EditText mRLWye;
    private String image_url;
    private String image_id;
    private TagDataBean tagDataBean;
    private FlowLayoutFaBu fl_tag_flowLayout;
    private List<String> listTag = new ArrayList<>();
    private Map<String, String> selectTags = new HashMap<>();
    private List<TagListItemBean> listZiDingSelect;
    private List<String> tags;
    private FaXianTagBean faXianTagBean;
    private String mWebUrl;
    private String type;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fabu_image;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
        image_url = getIntent().getStringExtra("image_url");
        image_id = getIntent().getStringExtra("image_id");
        mWebUrl = getIntent().getStringExtra("webUrl");
        type = getIntent().getStringExtra("type");
        tags = (List<String>) getIntent().getSerializableExtra("tags");
    }

    @Override
    protected void initView() {
        mContext = FaBuImageActivity.this;
        mHeaderInspiration = LayoutInflater.from(mContext).inflate(R.layout.header_fabu_image, null);
        mMain = (RelativeLayout) this.findViewById(R.id.main);
        mDismiss = (ImageView) this.findViewById(R.id.iv_dismiss_pop);
        mTVSureAdd = (TextView) this.findViewById(R.id.tv_sure_add);
        mRecyclerView = (HRecyclerView) this.findViewById(R.id.rcy_recyclerview);
        fl_tag_flowLayout = (FlowLayoutFaBu) mHeaderInspiration.findViewById(R.id.fl_tag_flowLayout);
        mIVLingGan = (RoundJiaoImageView) mHeaderInspiration.findViewById(R.id.iv_linggan);
        mRLWye = (EditText) mHeaderInspiration.findViewById(R.id.rt_linggan_content);
        mRLAddInspiration = (RelativeLayout) mHeaderInspiration.findViewById(R.id.rl_add_inspiration);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mDismiss.setOnClickListener(this);
        mRLAddInspiration.setOnClickListener(this);
        mTVSureAdd.setOnClickListener(this);
        mRLWye.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_dismiss_pop) {
            //友盟统计
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("evenname", "取消加入灵感辑");
            map.put("even", "点击我的-灵感辑-收藏灵感辑下的内容的次数");
            MobclickAgent.onEvent(FaBuImageActivity.this, "shijian38", map);
            //ga统计
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("点击我的-灵感辑-收藏灵感辑下的内容的次数")  //事件类别
                    .setAction("取消加入灵感辑")      //事件操作
                    .build());
            this.finish();
        } else if (i == R.id.rl_add_inspiration) {

            Intent intent = new Intent(this, InspirationCreateActivity.class);
            intent.putExtra("userid", mUserId);
            startActivityForResult(intent, 11);

        } else if (i == R.id.tv_sure_add) {

            if (mListData.size() > 0) {
                addInspiration();
            } else {
                ToastUtils.showCenter(mContext, "请先创建灵感辑");
            }
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(type) && type.equals("location")) {
            GlideImgManager.glideLoader(FaBuImageActivity.this, "file://" + image_url, R.color.white, R.color.white, mIVLingGan, 1);
//            ImageUtils.displayFilletImage("file://"  + image_url, mIVLingGan);
        } else {
            GlideImgManager.glideLoader(FaBuImageActivity.this, image_url, R.color.white, R.color.white, mIVLingGan, 1);
//            ImageUtils.displayFilletImage(image_url, mIVLingGan);
        }
        buildRecyclerView();

        if (tags != null && tags.size() > 0) {
            listTag.addAll(tags);
            for (int i = 0; i < listTag.size(); i++) {
                selectTags.put(listTag.get(i), listTag.get(i));
            }
        }

        fl_tag_flowLayout.setColorful(false);
        fl_tag_flowLayout.setListData(listTag);
        fl_tag_flowLayout.setOnTagClickListener(this);
//        getTagData();
        getDingYueData();
    }

    private void buildRecyclerView() {
        MultiItemTypeSupport<InspirationBean> support = new MultiItemTypeSupport<InspirationBean>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_inspirationlist;
            }

            @Override
            public int getItemViewType(int position, InspirationBean s) {
                return 0;
            }
        };

        mAdapter = new MultiItemCommonAdapter<InspirationBean>(mContext, mListData, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {

                ((TextView) holder.getView(R.id.tv_item_name)).setText(mListData.get(position).getAlbum_info().getAlbum_name());
                if (TextUtils.isEmpty(mListData.get(position).getAlbum_info().getCover_image().getImg0())) {
                    GlideImgManager.glideLoader(FaBuImageActivity.this, "", R.drawable.moren2, R.drawable.moren2, (ImageView) holder.getView(R.id.iv_item_pic));
                } else {
                    ImageUtils.displayFilletImage(mListData.get(position).getAlbum_info().getCover_image().getImg0(), (ImageView) holder.getView(R.id.iv_item_pic));
                }

                if (defalsePosition == position) {
                    holder.getView(R.id.rl_item_inspiration_content).setBackgroundResource(R.drawable.bg_item_inspiration);
                    holder.getView(R.id.iv_choose).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.rl_item_inspiration_content).setBackgroundResource(R.drawable.bg_item_inspiration_null);
                    holder.getView(R.id.iv_choose).setVisibility(View.GONE);
                }

                holder.getView(R.id.rl_item_inspiration).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.getView(R.id.rl_item_inspiration_content).setBackgroundResource(R.drawable.bg_item_inspiration);
                        holder.getView(R.id.iv_choose).setVisibility(View.VISIBLE);
                        int beforePosition = defalsePosition;
                        defalsePosition = position;
                        mAdapter.notifyItemChanged(beforePosition);
                    }
                });

            }
        };
        mRecyclerView.addHeaderView(mHeaderInspiration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    private void getInspirationsData(final String state) {
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
                ToastUtils.showCenter(mContext, "专辑列表获取失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        InspirationListBean inspirationListBean = GsonUtil.jsonToBean(data_msg, InspirationListBean.class);
                        if (null != inspirationListBean.getAlbum_list() && 0 != inspirationListBean.getAlbum_list().size()) {
                            updateViewFromData(inspirationListBean.getAlbum_list(), state);
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
        MyHttpManager.getInstance().getUserInspirationSeries(mUserId, (page_num - 1) * 20 + "", "20", callBack);
    }

    private void updateViewFromData(List<InspirationBean> listData, String state) {

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
    public void onRefresh() {
        defalsePosition = 0;
        page_num = 1;
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        getInspirationsData(REFRESH_STATUS);
    }

    @Override
    public void onLoadMore() {
        mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
        ++page_num;
        getInspirationsData(LOADMORE_STATUS);
    }

    private void addInspiration() {


        CustomProgress.show(FaBuImageActivity.this, "发布中...", false, null);
        String strWhy = mRLWye.getText().toString();
        StringBuffer sb = new StringBuffer();
        for (String key : selectTags.keySet()) {
            sb.append(key + " ");
        }
        String tagStr = sb.toString();
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                CustomProgress.cancelDialog();

                ToastUtils.showCenter(FaBuImageActivity.this, "发布失败");
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
                        ToastUtils.showCenter(FaBuImageActivity.this, "发布成功");
                        JSONObject jsonObject1 = new JSONObject(data_msg);
                        JSONObject item_info =  jsonObject1.getJSONObject("item_info");
                        String item_id = item_info.getString("item_id");
                        Intent intent = new Intent();
                        intent.putExtra("item_id",item_id);
                        setResult(5,intent);
                        FaBuImageActivity.this.finish();
                    } else {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(FaBuImageActivity.this, "发布失败");
                    }
                } catch (JSONException e) {
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(FaBuImageActivity.this, "发布失败");
                }
            }
        };
        MyHttpManager.getInstance().saveCaiJiImage(mListData.get(defalsePosition).getAlbum_info().getAlbum_id(), image_id, strWhy, tagStr, mWebUrl, callBack);


    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ((view.getId() == R.id.rt_linggan_content && canVerticalScroll(mRLWye))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("添加灵感辑");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("添加灵感辑");
        MobclickAgent.onPause(this);
    }


    @Override
    public void TagClick(String text, int position) {

//        ToastUtils.showCenter(FaBuActvity.this, "点击" + position + text);
    }

    @Override
    public void DeleteTag(String text, int position) {
        fl_tag_flowLayout.cleanTag();
        listTag.remove(position);
        fl_tag_flowLayout.setListData(listTag);
        if (selectTags.containsKey(text)) {
            selectTags.remove(text);
        }
        if (null != listZiDingSelect) {
            for (int i = 0; i < listZiDingSelect.size(); i++) {
                if (text.equals(listZiDingSelect.get(i).getTag_info().getTag_name())) {
                    listZiDingSelect.remove(i);
                }
            }
        }
    }

    @Override
    public void AddTag(String text, int position) {
        Intent intent = new Intent(FaBuImageActivity.this, FaBuImageTagsActivity.class);
        SerializableHashMap myMap = new SerializableHashMap();
        myMap.setMap(selectTags);
        Bundle bundle = new Bundle();
        bundle.putSerializable("tags_select", myMap);
        intent.putExtras(bundle);
        intent.putExtra("zidingyi", (Serializable) listZiDingSelect);
        intent.putExtra("faXianTagBean", faXianTagBean);
        startActivityForResult(intent, 1);

    }

    private void getDingYueData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(FaBuImageActivity.this, "标签导航列表获取失败");
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
                        msg.what = 7;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(FaBuImageActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getTagList(callBack);
    }
//    //获取tag信息
//    private void getTagData() {
//
//        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                ToastUtils.showCenter(FaBuImageActivity.this, getString(R.string.fabutags_get_error));
//            }
//
//            @Override
//            public void onResponse(String s) {
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
//                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
//                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
//                    if (error_code == 0) {
//                        data_msg = "{ \"tag_id\": " + data_msg + "}";
//                        Message msg = new Message();
//                        msg.obj = data_msg;
//                        msg.what = 5;
//                        mHandler.sendMessage(msg);
//                    } else {
//                        ToastUtils.showCenter(FaBuImageActivity.this, error_msg);
//                    }
//                } catch (JSONException e) {
//                    ToastUtils.showCenter(FaBuImageActivity.this, getString(R.string.fabutags_get_error));
//                }
//            }
//        };
//        MyHttpManager.getInstance().getPicTagData(callBack);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 1) {
            Bundle bundle = data.getExtras();
            SerializableHashMap serializableHashMap = (SerializableHashMap) bundle.get("tags_select");
            listZiDingSelect = (List<TagListItemBean>) data.getSerializableExtra("zidingyi");
            if (serializableHashMap != null && serializableHashMap.getMap() != null && serializableHashMap.getMap().size() > 0) {
                selectTags = serializableHashMap.getMap();
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            } else {
                selectTags.clear();
                Message message = new Message();
                message.what = 2;
                mHandler.sendMessage(message);
            }

        } else if (requestCode == 1) {
            onRefresh();
        } else if (requestCode == 11) {
            onRefresh();
        }

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            if (code == 1) {
                listTag.clear();
                for (String key : selectTags.keySet()) {
                    listTag.add(key);
                }
                fl_tag_flowLayout.cleanTag();
                fl_tag_flowLayout.setListData(listTag);
            } else if (code == 2) {
                listTag.clear();
                fl_tag_flowLayout.cleanTag();
                fl_tag_flowLayout.setListData(listTag);
            } else if (code == 5) {

                String info = (String) msg.obj;
                tagDataBean = GsonUtil.jsonToBean(info, TagDataBean.class);
            } else if (code == 6) {
                fl_tag_flowLayout.cleanTag();
                fl_tag_flowLayout.setListData(listTag);
            } else if (code == 7) {
                String json = "{\"data\": " + (String) msg.obj + "}";
                faXianTagBean = GsonUtil.jsonToBean(json, FaXianTagBean.class);
            }
        }
    };


}
