package com.homechart.app.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by gumenghao on 17/6/30.
 */

public class ResizeRelativeLayout extends RelativeLayout {

    public ResizeRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mListener != null) {
            mListener.OnResizeRelative(w, h, oldw, oldh);
        }
    }

    // 监听接口
    private OnResizeRelativeListener mListener;

    public interface OnResizeRelativeListener {
        void OnResizeRelative(int w, int h, int oldw, int oldh);
    }

    public void setOnResizeRelativeListener(OnResizeRelativeListener l) {
        mListener = l;
    }

}
