package com.homechart.app.visearch;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.newhistory.HistoryBean;
import com.homechart.app.home.bean.newhistory.HistoryDataBean;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.visearch.media.IMediaCallback;
import com.homechart.app.visearch.media.MediaErrorCode;
import com.homechart.app.visearch.media.MediaManager;
import com.homechart.app.visearch.media.MediaTools;
import com.umeng.analytics.MobclickAgent;
import com.visenze.visearch.android.model.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by gumenghao on 17/9/11.
 */

public class PhotoActivity
        extends BaseActivity
        implements View.OnClickListener,
        CameraPreview.ImageCapturedCallback,
        ScaleGestureDetector.OnScaleGestureListener {

    private String photoPath = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES).getPath() + File.separator + "JiaTuApp";

    private String name;
    private int page = 1;
    private int degree = -1;
    private ImageView iv_back;
    private ImageView iv_sanguang;
    private SurfaceView media_preview;
    private MediaManager photoManager;
    private RecyclerView mRecyclerView;
    private TextView camera_album_button;
    private boolean allowLoadMore = true;
    private ImageView iv_camera_shutter_button;
    private ScaleGestureDetector scaleGestureDetector;
    private MultiItemCommonAdapter<HistoryDataBean> mAdapter;
    private List<HistoryDataBean> mListData = new ArrayList<>();
    private Boolean ifLogin;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_photo_shibie;
    }

    @Override
    protected void initView() {
        iv_camera_shutter_button = (ImageView) findViewById(R.id.iv_camera_shutter_button);
        media_preview = (SurfaceView) findViewById(R.id.media_preview);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_sanguang = (ImageView) findViewById(R.id.iv_sanguang);
        camera_album_button = (TextView) findViewById(R.id.camera_album_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcy_recyclerview_pic);
        photoManager = new MediaManager(PhotoActivity.this, media_preview);
        scaleGestureDetector = new ScaleGestureDetector(this, this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        ifLogin = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);

        initLoginRecyclerView();
        if(ifLogin){
            getHistoryImage();
        }else {

            getUnloginImage();
        }

    }

    private void getUnloginImage() {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                allowLoadMore = true;
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
                            String history = "{\"data\":" + data_msg + "}";
                            HistoryBean historyBean = GsonUtil.jsonToBean(history, HistoryBean.class);
                            if (historyBean != null && historyBean.getData() != null && historyBean.getData().size() > 0) {
                                Message message = new Message();
                                message.arg1 = 1;
                                message.obj = history;
                                handler.sendMessage(message);
                            }else {
                                allowLoadMore = true;
                            }
                        } else {
                            ToastUtils.showCenter(PhotoActivity.this, error_msg);
                        }
                    } else {
                        allowLoadMore = true;
                    }
                } catch (JSONException e) {
                    allowLoadMore = true;
                }
            }
        };
        MyHttpManager.getInstance().newHistoryDefault(callback);

    }

    private void initLoginRecyclerView() {

        MultiItemTypeSupport<HistoryDataBean> support = new MultiItemTypeSupport<HistoryDataBean>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_scroll_history;
            }

            @Override
            public int getItemViewType(int position, HistoryDataBean s) {
                return 0;
            }
        };

        mAdapter = new MultiItemCommonAdapter<HistoryDataBean>(PhotoActivity.this, mListData, support) {
            @Override
            public void convert(BaseViewHolder holder, final int position) {

                ImageUtils.disRectangleImage(mListData.get(position).getImage_url(), (ImageView) holder.getView(R.id.iv_item_image));
                holder.getView(R.id.iv_item_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(PhotoActivity.this, SearchLoadingActivity.class);
                        intent1.putExtra("image_url", mListData.get(position).getImage_url());
                        intent1.putExtra("type", "lishi");
                        intent1.putExtra("image_id", mListData.get(position).getImage_id());
                        intent1.putExtra("image_type", "network");
                        startActivity(intent1);
                    }
                });
                if (ifLogin && allowLoadMore && mDatas.size() > 0 && mDatas.size() % 40 == 0 && position > (mDatas.size() - 20)) {
                    allowLoadMore = false;
                    ++page;
                    getHistoryImage();
                }
            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PhotoActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setHasFixedSize(true);//设置固定大小
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getHistoryImage() {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError error) {
                allowLoadMore = true;
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
                            String history = "{\"data\":" + data_msg + "}";
                            HistoryBean historyBean = GsonUtil.jsonToBean(history, HistoryBean.class);
                            if (historyBean != null && historyBean.getData() != null && historyBean.getData().size() > 0) {
                                Message message = new Message();
                                message.arg1 = 1;
                                message.obj = history;
                                handler.sendMessage(message);
                            }else {
                                allowLoadMore = true;
                            }
                        } else {
                            ToastUtils.showCenter(PhotoActivity.this, error_msg);
                        }
                    } else {
                        allowLoadMore = true;
                    }
                } catch (JSONException e) {
                    allowLoadMore = true;
                }
            }
        };
        MyHttpManager.getInstance().newHistoryShiBie((page - 1) * 40, "40", callback);

    }

    @Override
    protected void initListener() {
        super.initListener();

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        iv_back.setOnClickListener(this);
        camera_album_button.setOnClickListener(this);
        iv_camera_shutter_button.setOnClickListener(this);
        iv_sanguang.setOnClickListener(this);
        photoManager.setMediaCallback(new IMediaCallback() {
            @Override
            public void Error(MediaErrorCode errorCode) {
                switch (errorCode) {
                    case TAKEPICTURE_FAIL:
                        Toast.makeText(PhotoActivity.this, "拍照失败", Toast.LENGTH_SHORT).show();
                        break;
                    case NO_CAMERA:
                        Toast.makeText(PhotoActivity.this, "没有摄像头", Toast.LENGTH_SHORT).show();
                        break;
                    case NO_FRONT_CAMERA:
                        Toast.makeText(PhotoActivity.this, "没有前置摄像头", Toast.LENGTH_SHORT).show();
                        break;
                    case OPEN_CAMERA_FAIL:
                        Toast.makeText(PhotoActivity.this, "打开摄像头失败", Toast.LENGTH_SHORT).show();
                        break;
                    case OPEN_PREVIEW_FAIL:
                        Toast.makeText(PhotoActivity.this, "打开预览界面失败", Toast.LENGTH_SHORT).show();
                        break;
                    case START_RECORD_FAIL:
                        Toast.makeText(PhotoActivity.this, "启动录制视频失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void takePicture(String path, String name) {
                Intent intent1 = new Intent(PhotoActivity.this, SearchLoadingActivity.class);
                intent1.putExtra("image_url", path + "/" + name);
                intent1.putExtra("type", "location");
                intent1.putExtra("image_type", "location");
                startActivity(intent1);
                CustomProgress.cancelDialog();
                PhotoActivity.this.finish();

            }

            @Override
            public void recordStop() {
            }
        });
        media_preview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return scaleGestureDetector.onTouchEvent(event);
            }
        });
        sm.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (Sensor.TYPE_ACCELEROMETER != event.sensor.getType()) {
                    return;
                }

                float[] values = event.values;
                float ax = values[0];
                float ay = values[1];

                double g = Math.sqrt(ax * ax + ay * ay);
                double cos = ay / g;
                if (cos > 1) {
                    cos = 1;
                } else if (cos < -1) {
                    cos = -1;
                }
                double rad = Math.acos(cos);
                if (ax < 0) {
                    rad = 2 * Math.PI - rad;
                }

                int uiRot = getWindowManager().getDefaultDisplay().getRotation();
                double uiRad = Math.PI / 2 * uiRot;
                rad -= uiRad;

                degree = (int) (180 * rad / Math.PI);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                PhotoActivity.this.finish();
                break;
            case R.id.iv_camera_shutter_button:
                CustomProgress.show(PhotoActivity.this, "拍照中...", false, null);
                name = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
                Log.d("test", degree + "");
                photoManager.tackPicture(photoPath, name, degree);
                break;
            case R.id.camera_album_button:
                GalleryFinal.openGallerySingle(0, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList != null && resultList.size() > 0) {
                            Message message = new Message();
                            message.arg1 = 0;
                            message.obj = resultList.get(0).getPhotoPath().toString();
                            handler.sendMessage(message);
                        } else {
                            ToastUtils.showCenter(PhotoActivity.this, "图片资源获取失败");
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                    }
                });
                break;
            case R.id.iv_sanguang:
                if (!MediaTools.checkFlash(this)) {
                    Toast.makeText(PhotoActivity.this, "没有闪光灯", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ifOpenFlash) {
                    photoManager.openFlush();
                    ifOpenFlash = false;
                } else {
                    photoManager.closeFlush();
                    ifOpenFlash = true;
                }

                break;
        }
    }

    boolean ifOpenFlash = true;

    @Override
    public void OnImageCaptured(Image image, String imagePath) {
        Intent intent1 = new Intent(PhotoActivity.this, SearchLoadingActivity.class);
        intent1.putExtra("image_url", imagePath);
        intent1.putExtra("type", "location");
        intent1.putExtra("image_type", "location");
        startActivity(intent1);
        PhotoActivity.this.finish();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int code = msg.arg1;
            switch (code) {
                case 0:
                    String url_Imag = (String) msg.obj;
                    Intent intent1 = new Intent(PhotoActivity.this, SearchLoadingActivity.class);
                    intent1.putExtra("image_url", url_Imag);
                    intent1.putExtra("type", "location");
                    intent1.putExtra("image_type", "location");
                    startActivity(intent1);
                    PhotoActivity.this.finish();
                    break;
                case 1:
                    String history = (String) msg.obj;
                    HistoryBean historyBean = GsonUtil.jsonToBean(history, HistoryBean.class);
                    if (historyBean.getData() != null && historyBean.getData().size() > 0) {
                        mListData.addAll(historyBean.getData());
                        mAdapter.notifyDataSetChanged();
                        allowLoadMore = true;
                    }else {
                        allowLoadMore = true;
                    }
                    break;
            }

        }
    };


    @Override
    protected void onResume() {
        super.onPause();
        photoManager.release();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("拍照页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("拍照页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }


    @Override
    protected void onPause() {
        super.onPause();
        ifOpenFlash = true;
        photoManager.release();
        MobclickAgent.onPageEnd("拍照页");
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }


}
