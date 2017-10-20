/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 ViSenze Pte. Ltd.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.homechart.app.visearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.UrlConstants;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.searchfservice.SearchSBean;
import com.homechart.app.utils.BitmapUtil;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.Md5Util;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.FileHttpManager;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.utils.volley.PutFileCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by yulu on 12/3/15.
 */
public class SearchLoadingActivity
        extends BaseActivity
        implements PutFileCallBack, View.OnClickListener {

    private String imagePath;
    private SearchSBean searchSBean;
    private String image_url;
    private String type;
    private String image_id;
    private ImageView iv_image_search;
    private String image_type;
    private ImageView iv_quxiao_loading;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_loading;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        image_url = getIntent().getStringExtra("image_url");
        type = getIntent().getStringExtra("type");
        image_id = getIntent().getStringExtra("image_id");
        image_type = getIntent().getStringExtra("image_type");
        imagePath = image_url;
    }

    @Override
    protected void initView() {

        iv_image_search = (ImageView) findViewById(R.id.iv_image_search);
        iv_quxiao_loading = (ImageView) findViewById(R.id.iv_quxiao_loading);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (image_type.equals("location")) {
            ImageUtils.disRectangleImage("file://" + imagePath, iv_image_search);
        } else {
            ImageUtils.disRectangleImage(imagePath, iv_image_search);
        }
        shibie();
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_quxiao_loading.setOnClickListener(this);
    }

    private void shibie() {
        if (type.equals("lishi") && !TextUtils.isEmpty(image_id)) {//网络图片
            //讲网络图片保存到本地
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    SimpleDateFormat timesdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String fileName = timesdf.format(new Date()).toString();//获取系统时间
//                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName + "/";
//                    Bitmap bitmap_before = ImageLoader.getInstance().loadImageSync(image_url);
//                    Bitmap bitmap_compress_press = BitmapUtil.compressImage(bitmap_before);
//                    try {
//                        boolean status = BitmapUtil.saveBitmap(bitmap_compress_press, path);
//                        if (status) {
//                            imagePath = path;
//                        }
//                        //直接识别网络图片
//                        searchByImageId(image_id);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.start();

            //直接识别网络图片
            searchByImageId(image_id);
        } else {//直接识别本地图片
            upLoaderHeader();
        }
    }

    //通过image_id直接识别网络图片
    private void searchByImageId(String imageid) {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ToastUtils.showCenter(SearchLoadingActivity.this, "识别失败");
            }

            @Override
            public void onResponse(String response) {
                try {
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                        String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                        String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                        if (error_code == 0) {
                            searchSBean = GsonUtil.jsonToBean(data_msg, SearchSBean.class);
                            if (null != searchSBean.getObject_list() && searchSBean.getObject_list().size() > 0) {
                                Message message = new Message();
                                message.arg1 = 0;
                                handler.sendMessage(message);
                            } else {
                                Message message = new Message();
                                message.arg1 = 1;
                                handler.sendMessage(message);
                            }
                        } else {
                            ToastUtils.showCenter(SearchLoadingActivity.this, error_msg);
                        }
                    } else {
                        ToastUtils.showCenter(SearchLoadingActivity.this, "识别失败");
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(SearchLoadingActivity.this, "识别失败");
                }
            }
        };
        MyHttpManager.getInstance().searchByImageId(imageid, callback);
    }

    //直接识别本地图片
    private void upLoaderHeader() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                SimpleDateFormat timesdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String fileName = timesdf.format(new Date()).toString();//获取系统时间
                //压缩图片
                Bitmap bitmap_before = null;
                bitmap_before = BitmapUtil.getBitmap(image_url);
                if (bitmap_before != null) {
                    Bitmap bitmap_compress_press = BitmapUtil.compressImage(bitmap_before);
                    try {
                        boolean status = BitmapUtil.saveBitmap(bitmap_compress_press, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName + "/");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                    String signString = PublicUtils.getSinaString(map);
                    String tabMd5String = Md5Util.getMD5twoTimes(signString);
                    map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                    FileHttpManager.getInstance().uploadFile(SearchLoadingActivity.this, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName + "/"),
                            UrlConstants.CHECK_IMAGE,
                            map,
                            PublicUtils.getPublicHeader(MyApplication.getInstance()));
                }
            }
        }.start();
    }

    //上传本地图片的成功回调
    @Override
    public void onSucces(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
            String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
            String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
            if (error_code == 0) {
                searchSBean = GsonUtil.jsonToBean(data_msg, SearchSBean.class);
                if (null != searchSBean.getObject_list() && searchSBean.getObject_list().size() > 0) {
                    Message message = new Message();
                    message.arg1 = 3;
                    handler.sendMessage(message);
                } else {
                    Message message = new Message();
                    message.arg1 = 4;
                    handler.sendMessage(message);
                }
            } else {
                ToastUtils.showCenter(SearchLoadingActivity.this, error_msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //上传本地图片的失败回调
    @Override
    public void onFails() {
        Message message = new Message();
        message.arg1 = 2;
        handler.sendMessage(message);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int tag = msg.arg1;
            switch (tag) {
                case 0://识别网络图片
                    if (!ifCloseSearch) {
                        Intent intent = new Intent(SearchLoadingActivity.this, EditPhotoActivity.class);
                        intent.putExtra("image_id", image_id);
                        intent.putExtra("imagePath", image_url);
                        intent.putExtra("searchstatus", "0");
                        intent.putExtra("network", "true");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("searchSBean", searchSBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        SearchLoadingActivity.this.finish();
                    }
                    break;
                case 3://识别本地图片
                    if (!ifCloseSearch) {
                        Intent intent = new Intent(SearchLoadingActivity.this, EditPhotoActivity.class);
                        intent.putExtra("image_id", image_id);
                        intent.putExtra("imagePath", imagePath);
                        intent.putExtra("searchstatus", "0");
                        intent.putExtra("network", "false");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("searchSBean", searchSBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        SearchLoadingActivity.this.finish();
                    }
                    break;
                case 1:
                    if (!ifCloseSearch) {
                        Intent intent1 = new Intent(SearchLoadingActivity.this, EditPhotoActivity.class);
                        intent1.putExtra("image_id", image_id);
                        intent1.putExtra("imagePath", image_url);
                        intent1.putExtra("searchstatus", "1");
                        intent1.putExtra("network", "true");
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("searchSBean", searchSBean);
                        intent1.putExtras(bundle1);
                        startActivity(intent1);
                        SearchLoadingActivity.this.finish();
                        break;
                    }

                case 4:
                    if (!ifCloseSearch) {
                        Intent intent1 = new Intent(SearchLoadingActivity.this, EditPhotoActivity.class);
                        intent1.putExtra("image_id", image_id);
                        intent1.putExtra("imagePath", imagePath);
                        intent1.putExtra("searchstatus", "1");
                        intent1.putExtra("network", "false");
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("searchSBean", searchSBean);
                        intent1.putExtras(bundle1);
                        startActivity(intent1);
                        SearchLoadingActivity.this.finish();
                        break;
                    }
                case 2:
                    ToastUtils.showCenter(SearchLoadingActivity.this, "图片上传失败！");
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_quxiao_loading:
                ifCloseSearch = true;
                SearchLoadingActivity.this.finish();
                break;
        }

    }

    boolean ifCloseSearch = false;
}
