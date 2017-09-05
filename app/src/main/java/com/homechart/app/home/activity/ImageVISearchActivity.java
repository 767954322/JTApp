package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.visearch.EditPhotoActivity;
import com.homechart.app.visearch.ImageHelper;
import com.homechart.app.visearch.IntentHelper;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.UploadSearchParams;
import com.visenze.visearch.android.ViSearch;
import com.visenze.visearch.android.model.Image;
import com.visenze.visearch.android.model.ImageResult;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gumenghao on 17/9/5.
 */

public class ImageVISearchActivity
        extends BaseActivity
        implements ViSearch.ResultListener {
    private String image_url;
    private static final String APP_KEY = "2317c981400c6b2ca55114cb6bdfb963";
    private String imagePath;
    private ViSearch viSearch;



    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            byte[] buffer = (byte[]) msg.obj;
            imagePath = ImageHelper.saveImageByte(ImageVISearchActivity.this, buffer);
            UploadSearchParams uploadSearchParams = new UploadSearchParams(image_url);
            viSearch.uploadSearch(uploadSearchParams);
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_visearch;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        image_url = getIntent().getStringExtra("image_url");
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        viSearch = new ViSearch.Builder(APP_KEY).build(ImageVISearchActivity.this);
        viSearch.setListener(ImageVISearchActivity.this);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    byte[] buffer = getImage(image_url);
                    Message message = new Message();
                    message.obj = buffer;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public void onSearchResult(ResultList resultList) {
        IntentHelper.addObjectForKey(resultList, IntentHelper.SEARCH_RESULT_EXTRA);
        IntentHelper.addObjectForKey(imagePath, IntentHelper.SEARCH_THUMBNAIL_PATH_EXTRA);
        IntentHelper.addObjectForKey(imagePath, IntentHelper.SEARCH_IMAGE_PATH_EXTRA);

        Intent intent = new Intent(ImageVISearchActivity.this, EditPhotoActivity.class);
        startActivity(intent);
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
