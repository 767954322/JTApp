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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.homechart.app.R;
import com.homechart.app.utils.CustomProgress;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.UploadSearchParams;
import com.visenze.visearch.android.ViSearch;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yulu on 12/3/15.
 */
public class EditPhotoActivity
        extends FragmentActivity
        implements ViSearch.ResultListener {

    //parameters passed in from result activity
    private String imagePath;
    private String thumbnailPath;
    private ResultList resultList;
    private String image_url;
    private static final String APP_KEY = "2317c981400c6b2ca55114cb6bdfb963";
    private ViSearch viSearch;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int arg = msg.arg1;
            if (arg == 1) {
                byte[] buffer = (byte[]) msg.obj;
                imagePath = ImageHelper.saveImageByte(EditPhotoActivity.this, buffer);
                thumbnailPath = imagePath;
                UploadSearchParams uploadSearchParams = new UploadSearchParams(image_url);
                viSearch.uploadSearch(uploadSearchParams);
            } else if (arg == 2) {
                CustomProgress.cancelDialog();
                //add fragment
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.main_holder, PhotoEditFragment.newInstance());
                fragmentTransaction.commit();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        CustomProgress.show(this, "正在识别中...", false, null);
        image_url = getIntent().getStringExtra("image_url");
        viSearch = new ViSearch.Builder(APP_KEY).build(EditPhotoActivity.this);
        viSearch.setListener(EditPhotoActivity.this);

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    byte[] buffer = getImage(image_url);
                    Message message = new Message();
                    message.obj = buffer;
                    message.arg1 = 1;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public ResultList getResultList() {
        return resultList;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onSearchResult(ResultList resultList) {
        this.resultList = resultList;

        Message message = new Message();
        message.arg1 = 2;
        mHandler.sendMessage(message);
    }

    @Override
    public void onSearchError(String errorMessage) {

    }

    @Override
    public void onSearchCanceled() {

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

}
