package com.homechart.app.visearch.media;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import java.util.List;

/**
 * Created by krubo on 2016/1/9.
 */
public class MediaTools {

    /**
     * 是否有摄像头
     *
     * @param context
     * @return
     */
    public static boolean checkCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * 是否有前置摄像头
     *
     * @param context
     * @return
     */
    public static boolean checkFrontCamera(Context context) {
        return checkCamera(context) && Camera.getNumberOfCameras() > 1;
    }

    /**
     * 是否有闪光灯
     *
     * @param context
     * @return
     */
    public static boolean checkFlash(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    /**
     * 获取最适当的大小
     *
     * @param sizes 摄像头预览界面大小数组
     * @param w     宽度
     * @param h     高度
     * @return
     */
    public static Camera.Size getOptimalSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) {
            return null;
        }
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }


    /**
     * 得到指定摄像头
     *
     * @param position Camera.CameraInfo.CAMERA_FACING_BACK
     *                 Camera.CameraInfo.CAMERA_FACING_FRONT
     * @return
     */
    public static Camera getDefaultCamera(int position) {
        int mNumberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < mNumberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == position) {
                return Camera.open(i);
            }
        }
        return null;
    }
}
