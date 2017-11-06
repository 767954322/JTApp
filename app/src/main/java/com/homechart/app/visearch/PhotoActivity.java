package com.homechart.app.visearch;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.home.activity.FaBuActvity;
import com.homechart.app.home.activity.HomeActivity;
import com.homechart.app.home.activity.ShiBieActivity;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.myview.HorizontalListView;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.visearch.media.IMediaCallback;
import com.homechart.app.visearch.media.MediaErrorCode;
import com.homechart.app.visearch.media.MediaManager;
import com.homechart.app.visearch.media.MediaTools;
import com.umeng.analytics.MobclickAgent;
import com.visenze.visearch.android.model.Image;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

    private ImageView iv_camera_shutter_button;
    //    private CameraPreview camera_preview;
    private ImageView iv_back;
    private TextView camera_album_button;
    //    private TextView tv_shibiejilu;
    private SurfaceView media_preview;
    private MediaManager photoManager;
    private ScaleGestureDetector scaleGestureDetector;
    private String name;
    private int degree = -1;
    private ImageView iv_sanguang;
    private HorizontalListView hlv_listview;

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
        hlv_listview = (HorizontalListView) findViewById(R.id.hlv_listview);
//        tv_shibiejilu = (TextView) findViewById(R.id.tv_shibiejilu);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        photoManager = new MediaManager(PhotoActivity.this, media_preview);
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        scaleGestureDetector = new ScaleGestureDetector(this, this);
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
    protected void initListener() {
        super.initListener();
        iv_back.setOnClickListener(this);
        camera_album_button.setOnClickListener(this);
        iv_camera_shutter_button.setOnClickListener(this);
//        tv_shibiejilu.setOnClickListener(this);
        iv_sanguang.setOnClickListener(this);
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
//                camera_preview.takePhoto(this);
                break;
            case R.id.camera_album_button:
//                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(openAlbumIntent, RESULT_LOAD_IMAGE_FROM_GALLERY);
                GalleryFinal.openGallerySingle(0, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList != null && resultList.size() > 0) {
                            Message message = new Message();
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
//            case R.id.tv_shibiejilu:
//                //友盟统计
//                HashMap<String, String> map6 = new HashMap<String, String>();
//                map6.put("evenname", "检测图片记录");
//                map6.put("even", "点击查看识别图片历史记录的次数");
//                MobclickAgent.onEvent(PhotoActivity.this, "jtaction53", map6);
//                //ga统计
//                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
//                        .setCategory("点击查看识别图片历史记录的次数")  //事件类别
//                        .setAction("检测图片记录")      //事件操作
//                        .build());
//
//                Intent intent = new Intent(PhotoActivity.this, ShiBieActivity.class);
//                startActivity(intent);
//                break;
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
            String url_Imag = (String) msg.obj;
            Intent intent1 = new Intent(PhotoActivity.this, SearchLoadingActivity.class);
            intent1.putExtra("image_url", url_Imag);
            intent1.putExtra("type", "location");
            intent1.putExtra("image_type", "location");
            startActivity(intent1);
            PhotoActivity.this.finish();
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
