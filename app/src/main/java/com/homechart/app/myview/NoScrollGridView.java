package com.homechart.app.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by gumenghao on 17/6/15.
 */

public class NoScrollGridView extends GridView {
    public NoScrollGridView(Context context) {
        super(context);
    }


    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
