package com.homechart.app.visearch.media;

/**
 * Created by krubo on 2016/1/9.
 */
public interface IMediaCallback {
    void Error(MediaErrorCode errorCode);

    void takePicture(String path, String name);

    void recordStop();
}
