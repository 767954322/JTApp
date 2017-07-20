package com.homechart.app.utils.widget;

import android.support.v4.view.PagerAdapter;

/**
 * @author allen .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file AutoInterface.java .
 * @brief  .
 */
public interface AutoInterface {
    void updateIndicatorView(int size);

    void setAdapter(PagerAdapter adapter);

    void startScorll();

    void endScorll();
}
