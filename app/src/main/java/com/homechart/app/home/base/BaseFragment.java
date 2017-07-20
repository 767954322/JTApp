package com.homechart.app.home.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by GPD on 2017/2/27.
 */

public abstract class BaseFragment extends Fragment {

    protected String TAG = getClass().getSimpleName();
    protected View rootView;
    protected Activity activity;

    public BaseFragment() {
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(getLayoutResId(), container, false);
            initView();
            initExtraBundle();
            initData(savedInstanceState);
            initListener();
        }
        return rootView;
    }

    /**
     * 获取布局的Id
     */
    protected abstract int getLayoutResId();

    /**
     * 查找控件
     */
    protected abstract void initView();

    /**
     * 获取bundle数据
     */
    protected void initExtraBundle() {
    }

    /**
     * 初始化数据操作
     *
     * @param savedInstanceState
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 设置监听
     */
    protected void initListener() {
    }

}
