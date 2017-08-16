package com.homechart.app.myview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/3/10/010.
 */

public class MyViewPager extends ViewPager {

    private boolean isCanScroll = false;
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 解决切换需要经过中间页
     */
    @Override
    public void setCurrentItem(int item) {
        //super.setCurrentItem(item);源码
        super.setCurrentItem(item, false);//false表示切换的时候,不经过两个页面的中间页
    }


    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }


    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        if (isCanScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

}
