package com.homechart.app.visearch.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by krubo on 2016/1/9.
 */
public class MediaManager implements SurfaceHolder.Callback {

    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private MediaRecorder mediaRecorder;
    private Context context;
    private IMediaCallback mediaCallback;
    private boolean isFlash = false;//是否使用闪光灯
    private boolean isFront = false;//前置摄像头
    private int width, height;
    private int mediaType;
    private boolean isRecording;
    private boolean isVideo;//是否是录像界面

    public MediaManager(Context context, SurfaceView surfaceView) {
        this.context = context;
        this.surfaceHolder = surfaceView.getHolder();
        this.surfaceHolder.addCallback(this);
        this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void setError(MediaErrorCode errorCode) {
        if (mediaCallback != null) {
            mediaCallback.Error(errorCode);
        }
    }

    private void initCamera() {
        if (!MediaTools.checkCamera(context)) {
            setError(MediaErrorCode.NO_CAMERA);
            return;
        }
        if (camera == null) {
            mediaType = Camera.CameraInfo.CAMERA_FACING_BACK;
            if (isFront()) {
                if (MediaTools.checkFrontCamera(context)) {
                    mediaType = Camera.CameraInfo.CAMERA_FACING_FRONT;
                } else {
                    setError(MediaErrorCode.NO_FRONT_CAMERA);
                }
            }
            try {
                camera = MediaTools.getDefaultCamera(mediaType);
                camera.setPreviewDisplay(surfaceHolder);
                camera.setDisplayOrientation(90);
                setCameraParameters();
            } catch (Exception e) {
                e.printStackTrace();
                setError(MediaErrorCode.OPEN_CAMERA_FAIL);
            }
        }
    }

    /**
     * 重置
     */
    public void release() {
        stopRecord();
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (camera == null || holder.getSurface() == null) {
            return;
        }
        this.width = width;
        this.height = height;
        setCameraParameters();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    private void setCameraParameters() {
        camera.stopPreview();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(isVideo ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
                : Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        if (MediaTools.checkFlash(context)) {
            parameters.setFlashMode(isFlash ? Camera.Parameters.FLASH_MODE_ON
                    : Camera.Parameters.FLASH_MODE_OFF);
        }
        if (width != 0 && height != 0) {
            Camera.Size optimalPictureSize = MediaTools.getOptimalSize(
                    parameters.getSupportedPictureSizes(), height, width);
            parameters.setPictureSize(optimalPictureSize.width, optimalPictureSize.height);
            Camera.Size optimalPreviewSize = MediaTools.getOptimalSize(
                    parameters.getSupportedPreviewSizes(), height, width);
            parameters.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);
        }
        camera.setParameters(parameters);
        camera.startPreview();
    }

    private File createFile(String path, String name) {
        File folder = new File(path);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                return null;
            }
        }
        return new File(folder, name);
    }

    /**
     * 拍照
     *
     * @param path
     * @param name
     */
    public void tackPicture(final String path, final String name) {
        if (camera == null || TextUtils.isEmpty(path) || TextUtils.isEmpty(name)) {
            setError(MediaErrorCode.TAKEPICTURE_FAIL);
            return;
        }

        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                File file = createFile(path, name);
                if (file == null) {
                    setError(MediaErrorCode.TAKEPICTURE_FAIL);
                    return;
                }
                //照片旋转
                int rotate = 0;
                if (mediaType == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    rotate = 90;
                } else {
                    rotate = 270;
                }
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
                matrix.preRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    if (mediaCallback != null) {
                        mediaCallback.takePicture(path, name);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    setError(MediaErrorCode.TAKEPICTURE_FAIL);
                } catch (IOException e) {
                    e.printStackTrace();
                    setError(MediaErrorCode.TAKEPICTURE_FAIL);
                }
                bitmap.recycle();
                bitmap = null;
                camera.startPreview();
            }
        });
    }

    /**
     * 开始视频录制
     *
     * @param path
     * @param name
     */
    public void startRecord(String path, String name) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(name) || createFile(path, name) == null) {
            setError(MediaErrorCode.START_RECORD_FAIL);
            return;
        }
        if (mediaRecorder == null && camera != null) {
            camera.unlock();
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setCamera(camera);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setProfile(CamcorderProfile.get(mediaType, CamcorderProfile.QUALITY_HIGH));
            mediaRecorder.setOutputFile(path + File.separator + name);
            int rotate;
            if (mediaType == Camera.CameraInfo.CAMERA_FACING_BACK) {
                rotate = 90;
            } else {
                rotate = 270;
            }
            mediaRecorder.setOrientationHint(rotate);
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                isRecording = true;
            } catch (IOException e) {
                e.printStackTrace();
                releaseMediaRecorder();
                setError(MediaErrorCode.START_RECORD_FAIL);
            }
        }
    }

    /**
     * 停止视频录制
     */
    public void stopRecord() {
        if (isRecording) {
            mediaRecorder.stop();
            releaseMediaRecorder();
            isRecording = false;
            releaseCamera();
            initCamera();
            if (mediaCallback != null) {
                mediaCallback.recordStop();
            }
        }
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            camera.lock();
        }
    }

    public void setMediaCallback(IMediaCallback mediaCallback) {
        this.mediaCallback = mediaCallback;
    }

    /**
     * 获取最大缩放数
     */
    public int getMaxZoom() {
        int maxZoom = 0;
        if (camera != null) {
            maxZoom = camera.getParameters().getMaxZoom();
        }
        return maxZoom;
    }

    /**
     * 得到当前缩放数
     *
     * @return
     */
    public int getZoom() {
        int zoom = 0;
        if (camera != null) {
            zoom = camera.getParameters().getZoom();
        }
        return zoom;
    }

    /**
     * 设置缩放数
     */
    public void setZoom(int zoom) {
        if (camera != null && camera.getParameters().isZoomSupported()) {
            zoom = zoom > getMaxZoom() ? getMaxZoom() : zoom;
            zoom = zoom < 0 ? 0 : zoom;
            camera.startSmoothZoom(zoom);
        }
    }

    /**
     * 设置是否是录制界面
     *
     * @param isVideo
     */
    public void setIsVideo(boolean isVideo) {
        this.isVideo = isVideo;
        release();
        initCamera();
    }

    /**
     * 是否是录制界面
     *
     * @return
     */
    public boolean isVideo() {
        return isVideo;
    }

    /**
     * 是否正在录制视频
     *
     * @return
     */
    public boolean isRecording() {
        return isRecording;
    }

    /**
     * 是否使用闪光灯
     *
     * @param isFlash
     */
    public void setIsFlash(boolean isFlash) {
        this.isFlash = isFlash;
        if (camera != null && MediaTools.checkFlash(context)) {
            camera.stopPreview();
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(isFlash ? Camera.Parameters.FLASH_MODE_ON
                    : Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.startPreview();
        }
    }

    /**
     * 是否使用闪光灯
     *
     * @return
     */
    public boolean isFlash() {
        return isFlash;
    }

    /**
     * 是否使用前置摄像头
     *
     * @param isFront
     */
    public void setIsFront(boolean isFront) {
        this.isFront = isFront;
        release();
        initCamera();
    }

    /**
     * 是否使用前置摄像头
     *
     * @return
     */
    public boolean isFront() {
        return isFront;
    }
}
