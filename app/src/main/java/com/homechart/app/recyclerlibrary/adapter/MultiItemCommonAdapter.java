package com.homechart.app.recyclerlibrary.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;

import java.util.List;

/**
 * Created by lvr on 2017/5/24.
 */

public abstract class MultiItemCommonAdapter<T> extends CommonAdapter<T>
{
    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    public MultiItemCommonAdapter(Context context, List<T> datas,
                                  MultiItemTypeSupport<T> multiItemTypeSupport)
    {
        super(context, -1, datas);
        mMultiItemTypeSupport = multiItemTypeSupport;
    }

    @Override
    public int getItemViewType(int position)
    {
        return mMultiItemTypeSupport.getItemViewType(position, mDatas.get(position));
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        int layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
        BaseViewHolder holder = new BaseViewHolder(mContext, parent, layoutId);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}
