package com.homechart.app.footer;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;

/**
 * @author cjj
 */
public class DefaultFootItem extends FootItem {

    private ProgressBar mProgressBar;
    private TextView mLoadingText;
    private TextView mEndTextView;
    private RelativeLayout rl_footer;

    @Override
    public View onCreateView(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.rv_with_footer_loading, parent, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.rv_with_footer_loading_progress);
        mEndTextView = (TextView) view.findViewById(R.id.rv_with_footer_loading_end);
        mLoadingText = (TextView) view.findViewById(R.id.rv_with_footer_loading_load);
        rl_footer = (RelativeLayout) view.findViewById(R.id.rl_footer);
        return view;
    }

    @Override
    public void onBindData(View view, int state) {
        if (state == RecyclerViewWithFooter.STATE_LOADING ) {
            if (TextUtils.isEmpty(loadingText)) {
                showProgressBar(view.getContext().getResources().getString(R.string.rv_with_footer_loading));
            } else {
                showProgressBar(loadingText);
            }
        } else if (state == RecyclerViewWithFooter.STATE_END) {
            showEnd("暂无更多数据");
        } else if (state == RecyclerViewWithFooter.STATE_EMPTY) {
            showEnd("暂无数据");
        } else if (state == RecyclerViewWithFooter.STATE_FINISH_LOADING ||  state == RecyclerViewWithFooter.STATE_PULL_TO_LOAD) {
            hideFooter();
        }
//        else if (state == RecyclerViewWithFooter.STATE_PULL_TO_LOAD) {
//            if (TextUtils.isEmpty(pullToLoadText)) {
//                showPullToLoad(view.getContext().getResources().getString(R.string.rv_with_footer_pull_load_more));
//            } else {
//                showPullToLoad(loadingText);
//            }
//        }
    }

    public void showPullToLoad(CharSequence message) {
        showEnd(message);
    }

    public void showProgressBar(CharSequence load) {
        mEndTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(load)) {
            mLoadingText.setVisibility(View.VISIBLE);
            mLoadingText.setText(load);
        } else {
            mLoadingText.setVisibility(View.GONE);
        }
    }

    public void showEnd(CharSequence end) {

        if (!TextUtils.isEmpty(end)) {
            mEndTextView.setText(end);
        }
        mEndTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mLoadingText.setVisibility(View.GONE);

    }

    public void hideFooter() {
        mEndTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mLoadingText.setVisibility(View.INVISIBLE);
    }

}
