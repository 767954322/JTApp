package com.homechart.app.myview;

import android.content.Context;
import android.util.AttributeSet;

import com.homechart.app.home.CallBack;

/**
 * Created by gumenghao on 18/4/17.
 */

public class MyTextView extends android.support.v7.widget.AppCompatEditText {
    private Context mContext;

    public MyTextView(Context context) {
        super(context);
        mContext = context;
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public boolean onTextContextMenuItem(int id) {
        if (id == android.R.id.paste || id == 16908337) {
            ((CallBack) mContext).pasteCall();
        }
        return super.onTextContextMenuItem(id);
    }
}
