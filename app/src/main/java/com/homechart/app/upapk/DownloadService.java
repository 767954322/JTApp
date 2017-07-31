package com.homechart.app.upapk;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.activity.ArticleDetailsActivity;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Gu FanFan.
 * Date: 2017/5/16, 17:18.
 * 下载文件.
 */

public class DownloadService extends IntentService {

    private Context mContext;
    private long mTotalNum;
    private long mWrittenNum;
    private Intent mIntent;
    private String FILE_PATH = Environment.getExternalStorageDirectory() + "/DITing";
    private String DOWNLOAD_ACTION = "DITing.action.download";

    public DownloadService() {
        super("DownLoad");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mIntent = new Intent();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String downloadPath;
            //apk链接
            final String downloadUrl = intent.getStringExtra("downloadUrl");
            String fileName = intent.getStringExtra("fileName");
            // final String type = intent.getStringExtra(Constants.BUNDLE_TYPE);
            //apk链接
            downloadPath = FILE_PATH + "/" + fileName;
            FileUtils.createOrExistsDir(FILE_PATH);
            final String filePath =
                    FILE_PATH + "/" + String.valueOf(downloadUrl.hashCode()) + ".apk";
            boolean isFileExists = FileUtils.isFileExists(filePath);
            if (!isFileExists) {
                final File file = FileUtils.getFileByPath(downloadPath);
                //TODO  更改为volley了
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

                            }
                        } catch (JSONException e) {
                        }
                    }
                };
                MyHttpManager.getInstance().downLoadApk(downloadUrl, callBack);


//                OkHttpClient okHttpClient = new OkHttpClient();
//                final Request request = new Request.Builder().url(downloadUrl).build();
//                okHttpClient.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        InputStream inputStream = null;
//                        byte[] bytes = new byte[2048];
//                        int length;
//                        int progress = 0;
//                        mIntent.putExtra("progress", progress);
//                        BroadcastUtil.send(mContext, mIntent, DOWNLOAD_ACTION);
//                        FileOutputStream fileOutputStream = null;
//                        try {
//                            mTotalNum = response.body().contentLength();
//                            inputStream = response.body().byteStream();
//                            fileOutputStream = new FileOutputStream(file);
//                            while ((length = inputStream.read(bytes)) != -1) {
//                                mWrittenNum += length;
//                                fileOutputStream.write(bytes, 0, length);
//                                progress = (int) (mWrittenNum * 1.0f / mTotalNum * 100);
//                                mIntent.putExtra("progress", progress == 100 ? 99 : progress);
//                                BroadcastUtil.send(mContext, mIntent, DOWNLOAD_ACTION);
//                            }
//                            fileOutputStream.flush();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } finally {
//                            CloseIOUtils.closeIO(inputStream, fileOutputStream);
//                            boolean isRename = FileUtils.rename(file, String.valueOf(downloadUrl.hashCode()) + ".apk");
//                            mIntent.putExtra("path", filePath);
//                            mIntent.putExtra("rename", isRename);
//                            mIntent.putExtra("progress", 100);
//                            BroadcastUtil.send(mContext, mIntent, DOWNLOAD_ACTION);
//                        }
//                    }
//                });
            } else {
                mIntent.putExtra("progress", 100);
                mIntent.putExtra("path", filePath);
                mIntent.putExtra("rename", true);
                BroadcastUtil.send(mContext, mIntent, DOWNLOAD_ACTION);
            }
        }
    }
}
