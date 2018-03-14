package com.homechart.app.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.glide.GlideImgManager;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.utils.widget.CustomProgressTouMing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gumenghao on 18/3/13.
 */

public class CaiJiImgListActivity
        extends BaseActivity
        implements View.OnClickListener {
    private List<String> imageLists;
    private GridView gv_gridview;
    private ImageView iv_quxiao;
    private CommonAdapter<String> mAdapter;
    private int widthScreen;
    private MyAdapter myAdapter;
    private String title;
    private TextView tv_tital;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_caiji_list;
    }


    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        imageLists = (List<String>) getIntent().getSerializableExtra("pic_url_list");
        title = getIntent().getStringExtra("title");

    }

    @Override
    protected void initView() {

        gv_gridview = (GridView) findViewById(R.id.gv_gridview);
        iv_quxiao = (ImageView) findViewById(R.id.iv_quxiao);
        tv_tital = (TextView) findViewById(R.id.tv_tital);

    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_quxiao.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        if (!TextUtils.isEmpty(title)) {
            tv_tital.setText("家图网－" + title);
        }

        widthScreen = (PublicUtils.getScreenWidth(this) - UIUtils.getDimens(R.dimen.font_26)) / 3;
//        widthScreen = PublicUtils.getScreenWidth(this) / 3;
        myAdapter = new MyAdapter(this, imageLists);
        gv_gridview.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_quxiao:
                CaiJiImgListActivity.this.finish();
                break;
        }
    }

    class MyAdapter extends BaseAdapter {

        private Context context;
        private List<String> list;

        public MyAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            MyHolder myHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_caiji_image, null);
                myHolder = new MyHolder();
                myHolder.iv_imageview = (ImageView) convertView.findViewById(R.id.iv_item_caiji);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) myHolder.iv_imageview.getLayoutParams();
            layoutParams.width = widthScreen;
            layoutParams.height = widthScreen;
            myHolder.iv_imageview.setLayoutParams(layoutParams);
            GlideImgManager.glideLoader(CaiJiImgListActivity.this, list.get(position), R.color.white, R.color.white, myHolder.iv_imageview, 1);
            myHolder.iv_imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomProgressTouMing.show(CaiJiImgListActivity.this, "", false, null);
                    upImage(list.get(position));
                }
            });
            return convertView;
        }

        class MyHolder {
            private ImageView iv_imageview;
        }
    }

    private void upImage(final String url) {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgressTouMing.cancelDialog();
                ToastUtils.showCenter(CaiJiImgListActivity.this, "图片上传失败,请重新上传");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(data_msg);
                            String image_id = jsonObject1.getString("image_id");
                            getDefaultTag(image_id,url);
                        }catch (Exception e){
                            ToastUtils.showCenter(CaiJiImgListActivity.this, "图片上传失败,请重新上传");
                        }

                    } else {
                        CustomProgressTouMing.cancelDialog();
                        ToastUtils.showCenter(CaiJiImgListActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    CustomProgressTouMing.cancelDialog();
                    ToastUtils.showCenter(CaiJiImgListActivity.this, "图片上传失败,请重新上传");
                }
            }
        };
        MyHttpManager.getInstance().grabPicture(url,title,"" , callBack);


    }

    private void getDefaultTag(final String image_id,final String url) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgressTouMing.cancelDialog();
                ToastUtils.showCenter(CaiJiImgListActivity.this, "默认标签获取失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        List<String> list = new ArrayList<>();
                        JSONObject jsonObject1 = new JSONObject(data_msg);
                        JSONArray jsonArray = jsonObject1.getJSONArray(image_id);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String tags = jsonArray.get(i).toString();
                                list.add(tags);
                            }
                        }
                        Intent intent = new Intent(CaiJiImgListActivity.this,FaBuImageActivity.class);
                        intent.putExtra("image_id",image_id);
                        intent.putExtra("image_url",url);
                        intent.putExtra("tags", (Serializable) list);
                        CustomProgressTouMing.cancelDialog();
                        startActivity(intent);
                    } else {
                        CustomProgressTouMing.cancelDialog();
                        ToastUtils.showCenter(CaiJiImgListActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    CustomProgressTouMing.cancelDialog();
                    ToastUtils.showCenter(CaiJiImgListActivity.this, "默认标签获取失败");
                }
            }
        };
        MyHttpManager.getInstance().getImgDefaultTags(image_id, callBack);

    }

}
