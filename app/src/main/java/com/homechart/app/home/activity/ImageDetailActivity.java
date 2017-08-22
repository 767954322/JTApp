package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.adapter.MyColorGridAdapter;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.color.ColorBean;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.home.bean.imagedetail.ColorInfoBean;
import com.homechart.app.home.bean.imagedetail.ImageDetailBean;
import com.homechart.app.myview.FlowLayoutBiaoQian;
import com.homechart.app.myview.FlowLayoutShaiXuan;
import com.homechart.app.myview.HomeSharedPopWin;
import com.homechart.app.myview.HomeSharedPopWinFaBu;
import com.homechart.app.myview.HomeSharedPopWinPublic;
import com.homechart.app.myview.MyListView;
import com.homechart.app.myview.ShangshabanChangeTextSpaceView;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/26.
 */

public class ImageDetailActivity
        extends BaseActivity
        implements View.OnClickListener,
        HomeSharedPopWin.ClickInter ,
        HomeSharedPopWinFaBu.ClickInter{
    private ImageView iv_details_image;
    private ShangshabanChangeTextSpaceView tv_details_tital;
    private TextView tv_details_time;
    private ImageView iv_bang;
    private ImageView iv_xing;
    private ImageView iv_ping;
    private ImageView iv_shared;
    private TextView tv_bang;
    private TextView tv_xing;
    private TextView tv_ping;
    private TextView tv_shared;
    private String item_id;
    private ImageDetailBean imageDetailBean;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private TextView tv_content_right;
    private HomeSharedPopWin homeSharedPopWin;
    private HomeSharedPopWinFaBu homeSharedPopWinFaBu;
    private boolean ifZan = true;
    private boolean ifShouCang = true;
    private boolean ifPingLun = true;
    private int like_num;
    private int collect_num;
    private int comment_num;
    private int share_num;
    private boolean ifFirst = true;
    private FlowLayoutBiaoQian fl_tags_jubu;
    private LinearLayout ll_color_lines;
    private MyListView dgv_colorlist;
    private TextView tv_color_tips;
    private ImageView iv_ifshow_color;
    private RelativeLayout rl_imagedetails_next;
    private RelativeLayout rl_color_location;
    private ImageView iv_imagedetails_next;
    private TextView tv_imagedetails_next;
    private List<ColorInfoBean> listColor;
    private boolean ifShowColorList = true;
    private List<String> list = new ArrayList<>();
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_detail;
    }


    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        item_id = getIntent().getStringExtra("item_id");
    }

    @Override
    protected void initView() {

        homeSharedPopWin = new HomeSharedPopWin(ImageDetailActivity.this, ImageDetailActivity.this);
        homeSharedPopWinFaBu = new HomeSharedPopWinFaBu(ImageDetailActivity.this,ImageDetailActivity.this);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        iv_details_image = (ImageView) findViewById(R.id.iv_details_image);
        tv_details_tital = (ShangshabanChangeTextSpaceView) findViewById(R.id.tv_details_tital);
        tv_details_time = (TextView) findViewById(R.id.tv_details_time);
        iv_bang = (ImageView) findViewById(R.id.iv_bang);
        iv_xing = (ImageView) findViewById(R.id.iv_xing);
        iv_ping = (ImageView) findViewById(R.id.iv_ping);
        iv_shared = (ImageView) findViewById(R.id.iv_shared);
        tv_bang = (TextView) findViewById(R.id.tv_bang);
        tv_xing = (TextView) findViewById(R.id.tv_xing);
        tv_ping = (TextView) findViewById(R.id.tv_ping);
        tv_shared = (TextView) findViewById(R.id.tv_shared);
        fl_tags_jubu = (FlowLayoutBiaoQian) findViewById(R.id.fl_tags_jubu);
        ll_color_lines = (LinearLayout) findViewById(R.id.ll_color_lines);
        dgv_colorlist = (MyListView)findViewById(R.id.dgv_colorlist);
        tv_color_tips = (TextView) findViewById(R.id.tv_color_tips);
        iv_ifshow_color = (ImageView) findViewById(R.id.iv_ifshow_color);
        rl_imagedetails_next = (RelativeLayout) findViewById(R.id.rl_imagedetails_next);
        rl_color_location = (RelativeLayout) findViewById(R.id.rl_color_location);
        iv_imagedetails_next = (ImageView) findViewById(R.id.iv_imagedetails_next);
        tv_imagedetails_next = (TextView) findViewById(R.id.tv_imagedetails_next);


    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);
        tv_bang.setOnClickListener(this);
        iv_bang.setOnClickListener(this);
        iv_xing.setOnClickListener(this);
        tv_xing.setOnClickListener(this);
        iv_ping.setOnClickListener(this);
        tv_ping.setOnClickListener(this);
        iv_shared.setOnClickListener(this);
        tv_shared.setOnClickListener(this);
        iv_ifshow_color.setOnClickListener(this);
        iv_imagedetails_next.setOnClickListener(this);
        tv_imagedetails_next.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        tv_tital_comment.setText("图片详情");
        tv_content_right.setText("编辑");
        getColorData();
        getImageDetail();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ImageDetailActivity.this.finish();
                break;
            case R.id.tv_content_right:

                if (imageDetailBean != null) {
                    Intent intent = new Intent(ImageDetailActivity.this, ImageEditActvity.class);
                    intent.putExtra("image_value", imageDetailBean);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.iv_bang:
            case R.id.tv_bang:
                if (ifZan) {
                    addZan();
                    ifZan = false;
                } else {
                    removeZan();
                    ifZan = true;
                }
                break;
            case R.id.iv_xing:
            case R.id.tv_xing:
                if (ifShouCang) {
                    //友盟统计
                    HashMap<String, String> map4 = new HashMap<String, String>();
                    map4.put("evenname", "收藏图片");
                    map4.put("even", "图片发布成功页");
                    MobclickAgent.onEvent(ImageDetailActivity.this, "jtaction5", map4);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("图片发布成功页")  //事件类别
                            .setAction("收藏图片")      //事件操作
                            .build());
                    addShouCang();
                    ifShouCang = false;
                } else {
                    //友盟统计
                    HashMap<String, String> map4 = new HashMap<String, String>();
                    map4.put("evenname", "取消收藏图片");
                    map4.put("even", "图片发布成功页");
                    MobclickAgent.onEvent(ImageDetailActivity.this, "jtaction6", map4);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("图片发布成功页")  //事件类别
                            .setAction("取消收藏图片")      //事件操作
                            .build());
                    removeShouCang();
                    ifShouCang = true;
                }
                break;
            case R.id.iv_ping:
            case R.id.tv_ping:

                Intent intent = new Intent(ImageDetailActivity.this, PingListActivity.class);
                intent.putExtra("item_id", item_id);
                startActivityForResult(intent, 2);

                break;
            case R.id.iv_shared:
            case R.id.tv_shared:
                if (imageDetailBean != null) {

                    homeSharedPopWinFaBu.showAtLocation(ImageDetailActivity.this.findViewById(R.id.main),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                            0,
                            0); //设置layout在PopupWindow中显示的位置
//                    sharedItemOpen();
                }
                break;
            case R.id.iv_ifshow_color:

                if (ifShowColorList) {
                    //隐藏
                    ifShowColorList = false;

                    iv_ifshow_color.setVisibility(View.VISIBLE);
                    iv_ifshow_color.setImageResource(R.drawable.zhankai);
                    dgv_colorlist.setVisibility(View.GONE);
                    tv_color_tips.setVisibility(View.GONE);
                    rl_color_location.setVisibility(View.GONE);
                } else {
                    //显示
                    ifShowColorList = true;

                    iv_ifshow_color.setImageResource(R.drawable.shouqi);
                    iv_ifshow_color.setVisibility(View.VISIBLE);
                    dgv_colorlist.setVisibility(View.VISIBLE);
                    tv_color_tips.setVisibility(View.VISIBLE);
                    rl_color_location.setVisibility(View.VISIBLE);

                }

                break;
            case R.id.tv_imagedetails_next:
            case R.id.iv_imagedetails_next:

                if (list.size() > 0 && listColor != null && listColor.size() > 0) {
                    Intent intent2 = new Intent(ImageDetailActivity.this, ShaiXuanResultActicity.class);
                    intent2.putExtra("shaixuan_tag", list.get(0));
                    if (listColor != null && listColor.size() > 0) {
                        ColorInfoBean colorInfoBean = listColor.get(0);
                        if (colorBean != null) {
                            Map<Integer, ColorItemBean> mSelectListData = new HashMap<>();
                            List<ColorItemBean> list = colorBean.getColor_list();
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getColor_id() == colorInfoBean.getColor_id()) {
                                    mSelectListData.put(colorInfoBean.getColor_id(), list.get(i));
                                }
                            }
                            intent2.putExtra("shaixuan_color", (Serializable) mSelectListData);
                        }
                    }
                    intent2.putExtra("islist", true);
                    startActivity(intent2);
                }
                break;
        }
    }
    private void getColorData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(ImageDetailActivity.this, getString(R.string.color_get_error));
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
                        msg.what = 6;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(ImageDetailActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailActivity.this, getString(R.string.color_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getColorListData(callBack);

    }

    //取消收藏
    private void removeShouCang() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(ImageDetailActivity.this, "取消收藏失败");
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
                        ToastUtils.showCenter(ImageDetailActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailActivity.this, "取消收藏失败");
                }
            }
        };
        MyHttpManager.getInstance().removeShouCang(item_id, callBack);
    }

    //收藏
    private void addShouCang() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(ImageDetailActivity.this, "收藏成功");
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
                        ToastUtils.showCenter(ImageDetailActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailActivity.this, "收藏失败");
                }
            }
        };
        MyHttpManager.getInstance().addShouCang(item_id, callBack);
    }

    //取消点赞
    private void removeZan() {


        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(ImageDetailActivity.this, "取消点赞失败");
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
                        ToastUtils.showCenter(ImageDetailActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailActivity.this, "取消点赞失败");
                }
            }
        };
        MyHttpManager.getInstance().removeZan(item_id, callBack);


    }

    //点赞
    private void addZan() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(ImageDetailActivity.this, "点赞失败");
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
                        ToastUtils.showCenter(ImageDetailActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailActivity.this, "点赞失败");
                }
            }
        };
        MyHttpManager.getInstance().addZan(item_id, callBack);

    }

    private void getImageDetail() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(ImageDetailActivity.this, "图片信息获取失败");
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
                        ToastUtils.showCenter(ImageDetailActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(ImageDetailActivity.this, "图片信息获取失败");
                }
            }
        };

        MyHttpManager.getInstance().itemDetailsFaBu(item_id, callBack);

    }

    private ColorBean colorBean;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int code = msg.what;
            switch (code) {
                case 1:
                    String data = (String) msg.obj;
                    imageDetailBean = GsonUtil.jsonToBean(data, ImageDetailBean.class);
                    changeUI(imageDetailBean);
                    break;
                case 2:
                    ToastUtils.showCenter(ImageDetailActivity.this, "你很棒棒哦");
                    iv_bang.setImageResource(R.drawable.bang1);
                    like_num++;
                    tv_bang.setText(like_num + "");
                    tv_bang.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                    break;
                case 3:
                    ToastUtils.showCenter(ImageDetailActivity.this, "已取消点赞");
                    iv_bang.setImageResource(R.drawable.bang);
                    like_num--;
                    tv_bang.setText(like_num + "");
                    tv_bang.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                    break;
                case 4:
                    ToastUtils.showCenter(ImageDetailActivity.this, "收藏成功");
                    iv_xing.setImageResource(R.drawable.xing1);
                    collect_num++;
                    tv_xing.setText(collect_num + "");
                    tv_xing.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                    break;
                case 5:
                    ToastUtils.showCenter(ImageDetailActivity.this, "取消收藏");
                    iv_xing.setImageResource(R.drawable.xing);
                    collect_num--;
                    tv_xing.setText(collect_num + "");
                    tv_xing.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
                    break;
                case 6:

                    String info = (String) msg.obj;
                    colorBean = GsonUtil.jsonToBean(info, ColorBean.class);
                    break;
            }
        }
    };

    private void changeUI(ImageDetailBean imageDetailBean) {
        int wide_num = PublicUtils.getScreenWidth(ImageDetailActivity.this) - UIUtils.getDimens(R.dimen.font_20);
        ViewGroup.LayoutParams layoutParams = iv_details_image.getLayoutParams();
        layoutParams.width = wide_num;
        layoutParams.height = (int) (wide_num / imageDetailBean.getItem_info().getImage().getRatio());
        iv_details_image.setLayoutParams(layoutParams);
        ImageUtils.displayFilletImage(imageDetailBean.getItem_info().getImage().getImg0(), iv_details_image);


        listColor = imageDetailBean.getColor_info();
        int width = iv_details_image.getLayoutParams().width;
        if (listColor != null && listColor.size() > 0) {
            ll_color_lines.setVisibility(View.VISIBLE);
            float float_talte = 0;
            for (int i = 0; i < listColor.size(); i++) {
                float wid = Float.parseFloat(listColor.get(i).getColor_percent().trim());
                float_talte = float_talte + wid;
            }

            for (int i = 0; i < listColor.size(); i++) {
                TextView textView = new TextView(this);
                float wid = Float.parseFloat(listColor.get(i).getColor_percent().trim());
                float per = wid / float_talte;

                if(i == listColor.size()-1){
                    textView.setHeight(width);
                }else {
                    textView.setWidth((int) (width * per));
                }

                textView.setHeight(UIUtils.getDimens(R.dimen.font_30));
                textView.setBackgroundColor(Color.parseColor("#" + listColor.get(i).getColor_value()));
                ll_color_lines.addView(textView);
            }
            rl_imagedetails_next.setVisibility(View.VISIBLE);
            iv_ifshow_color.setVisibility(View.VISIBLE);
            dgv_colorlist.setVisibility(View.VISIBLE);
            tv_color_tips.setVisibility(View.VISIBLE);
            rl_color_location.setVisibility(View.VISIBLE);
            dgv_colorlist.setAdapter(new MyColorGridAdapter(listColor, ImageDetailActivity.this));
        } else {
            rl_imagedetails_next.setVisibility(View.GONE);
            iv_ifshow_color.setVisibility(View.GONE);
        }


        String tag = imageDetailBean.getItem_info().getTag().toString();
        String[] str_tag = tag.split(" ");
        list.clear();
        for (int i = 0; i < str_tag.length; i++) {
            if (!TextUtils.isEmpty(str_tag[i].trim())) {
                list.add(str_tag[i]);
            }
        }

        fl_tags_jubu.cleanTag();
        fl_tags_jubu.setColorful(false);
        fl_tags_jubu.setData(list);
        fl_tags_jubu.setOnTagClickListener(new FlowLayoutBiaoQian.OnTagClickListener() {
            @Override
            public void TagClick(String text) {
                // 跳转搜索结果页
                Intent intent = new Intent(ImageDetailActivity.this, ShaiXuanResultActicity.class);
                String tag = text.replace("#", "");
                intent.putExtra("islist", true);
                intent.putExtra("shaixuan_tag", tag.trim());
                startActivity(intent);
            }
        });
//        tv_details_tital.setSpacing(2);
        tv_details_tital.setText(imageDetailBean.getItem_info().getDescription());
        //处理时间
        String[] str = imageDetailBean.getItem_info().getAdd_time().split(" ");
        String fabuTime = str[0].replace("-", "/");
        tv_details_time.setText(fabuTime + " 发布");
        like_num = imageDetailBean.getCounter().getLike_num();
        collect_num = imageDetailBean.getCounter().getCollect_num();
        comment_num = imageDetailBean.getCounter().getComment_num();
        share_num = imageDetailBean.getCounter().getShare_num();

        tv_bang.setText(like_num + "");
        if (imageDetailBean.getItem_info().getIs_liked().equals(1)) {//已赞
            iv_bang.setImageResource(R.drawable.bang1);
            tv_bang.setTextColor(UIUtils.getColor(R.color.bg_e79056));
            ifZan = false;

        } else {//未赞
            iv_bang.setImageResource(R.drawable.bang);
            tv_bang.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
            ifZan = true;
        }
        tv_xing.setText(collect_num + "");
        if (imageDetailBean.getItem_info().getIs_collected().equals(1)) {//已收藏
            iv_xing.setImageResource(R.drawable.xing1);
            tv_xing.setTextColor(UIUtils.getColor(R.color.bg_e79056));
            ifShouCang = false;
        } else {//未收藏

            ifShouCang = true;
        }

        tv_ping.setText(comment_num + "");
        tv_shared.setText(share_num + "");

        if (ifFirst) {
            homeSharedPopWin.showAtLocation(ImageDetailActivity.this.findViewById(R.id.main),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                    0,
                    0); //设置layout在PopupWindow中显示的位置
            ifFirst = false;
        }
    }

    @Override
    public void onClickWeiXin() {

        sharedItemFaBu(SHARE_MEDIA.WEIXIN);
    }

    @Override
    public void onClickPYQ() {

        sharedItemFaBu(SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    @Override
    public void onClickWeiBo() {

        sharedItemFaBu(SHARE_MEDIA.SINA);
    }

    @Override
    public void onClickQQ() {

        sharedItemFaBu(SHARE_MEDIA.QQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && requestCode == 1) {
            getImageDetail();
        } else if (requestCode == 2) {
            getImageDetail();
        }

    }

    private void sharedItemFaBu(SHARE_MEDIA share_media) {

        UMImage image = new UMImage(ImageDetailActivity.this, imageDetailBean.getItem_info().getImage().getImg0());
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        UMWeb web = new UMWeb("http://h5.idcool.com.cn/photo/" + imageDetailBean.getItem_info().getItem_id());
        web.setTitle("「" + imageDetailBean.getUser_info().getNickname() + "」在晒家｜家图APP");//标题
        web.setThumb(image);  //缩略图
        String desi = imageDetailBean.getItem_info().getDescription() + imageDetailBean.getItem_info().getTag();
        if (desi.length() > 160) {
            desi = desi.substring(0, 160) + "...";
        }
        web.setDescription(desi);//描述
        new ShareAction(ImageDetailActivity.this).
                setPlatform(share_media).
                withMedia(web).
                setCallback(umShareListener).share();
    }

    private void sharedItemOpen(SHARE_MEDIA share_media) {

        UMImage image = new UMImage(ImageDetailActivity.this, imageDetailBean.getItem_info().getImage().getImg0());
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        UMWeb web = new UMWeb("http://h5.idcool.com.cn/photo/" + imageDetailBean.getItem_info().getItem_id());
        web.setTitle("「" + imageDetailBean.getUser_info().getNickname() + "」在晒家｜家图APP");//标题
        web.setThumb(image);  //缩略图
        String desi = imageDetailBean.getItem_info().getDescription() + imageDetailBean.getItem_info().getTag();
        if (desi.length() > 160) {
            desi = desi.substring(0, 160) + "...";
        }
        web.setDescription(desi);//描述
        new ShareAction(ImageDetailActivity.this).
                setPlatform(share_media).
                withMedia(web).
                setCallback(umShareListener).share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            addShared();
            ToastUtils.showCenter(ImageDetailActivity.this, "分享成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.showCenter(ImageDetailActivity.this, "分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showCenter(ImageDetailActivity.this, "分享取消了");
        }
    };

    private void addShared() {
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
                        getImageDetail();
                    } else {
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().addShared(imageDetailBean.getItem_info().getItem_id(), "item", callBack);
    }

    @Override
    public void onClickWeiXinFaBu() {
        sharedItemOpen(SHARE_MEDIA.WEIXIN);
    }

    @Override
    public void onClickPYQFaBu() {
        sharedItemOpen(SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    @Override
    public void onClickWeiBoFaBu() {
        sharedItemOpen(SHARE_MEDIA.SINA);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("图片发布成功页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("图片发布成功页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageEnd("图片发布成功页");
        MobclickAgent.onPause(this);
    }
}
