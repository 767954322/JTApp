package com.homechart.app.myview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/3/10/010.
 */

public class MyViewPager extends ViewPager {

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

}
