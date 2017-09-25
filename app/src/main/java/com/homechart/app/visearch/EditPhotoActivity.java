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

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.UrlConstants;
import com.homechart.app.home.activity.FaBuActvity;
import com.homechart.app.home.bean.searchfservice.SearchSBean;
import com.homechart.app.utils.BitmapUtil;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.Md5Util;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.FileHttpManager;
import com.homechart.app.utils.volley.PutFileCallBack;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.UploadSearchParams;
import com.visenze.visearch.android.ViSearch;
import com.visenze.visearch.android.model.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import retrofit2.http.Url;

/**
 * Created by yulu on 12/3/15.
 */
public class EditPhotoActivity
        extends FragmentActivity
        implements PutFileCallBack {

    //parameters passed in from result activity
    private String imagePath;
    private SearchSBean searchSBean;
    private String image_url;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        CustomProgress.show(this, "正在识别中...", false, null);
        image_url = getIntent().getStringExtra("image_url");
        type = getIntent().getStringExtra("type");
        imagePath = image_url;
        upLoaderHeader();
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Get image from newwork
     *
     * @param path The path of image
     * @return byte[]
     * @throws Exception
     */
    public byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(20 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return readStream(inStream);
        }
        return null;
    }

    public SearchSBean getSearchSBean() {
        return searchSBean;
    }

    /**
     * Get data from stream
     *
     * @param inStream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }


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
                FileHttpManager.getInstance().uploadFile(EditPhotoActivity.this, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName + "/"),
                        UrlConstants.CHECK_IMAGE,
                        map,
                        PublicUtils.getPublicHeader(MyApplication.getInstance()));
            }
        }.start();
    }

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
                    message.arg1 = 0;
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.arg1 = 1;
                    handler.sendMessage(message);
                }
            } else {
                ToastUtils.showCenter(EditPhotoActivity.this, error_msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFails() {
        CustomProgress.cancelDialog();
        Message message = new Message();
        message.arg1 = 2;
        handler.sendMessage(message);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           int tag =  msg.arg1;
            switch (tag){
                case 0:
                    CustomProgress.cancelDialog();
                    if(null != searchSBean){
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.main_holder, PhotoEditFragment.newInstance());
                        fragmentTransaction.commit();
                    }
                    break;
                case 1:
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(EditPhotoActivity.this, "未识别到数据！");
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.main_holder, PhotoEditFragment.newInstance());
                    fragmentTransaction.commit();
                    break;
                case 2:
                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(EditPhotoActivity.this, "图片上传失败！");
                    break;
            }
        }
    };

}
