package com.homechart.app.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.hotwords.HotWordsBean;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.myview.FlowLayoutSearch;
import com.homechart.app.myview.MyListView;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by gumenghao on 17/6/13.
 */

public class SearchActivity
        extends BaseActivity
        implements View.OnClickListener {
    private TextView tv_quxiao;
    private FlowLayoutSearch his_flowLayout;
    private String[] myData;
    private ClearEditText cet_clearedit;
    private MyListView lv_hostory_list;
    private List<String> mListHostory;
    private HostoryAdapter hostoryAdapter;
    private RelativeLayout rl_search_hostory;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {

        tv_quxiao = (TextView) findViewById(R.id.tv_quxiao);
        his_flowLayout = (FlowLayoutSearch) findViewById(R.id.his_flowLayout);
        cet_clearedit = (ClearEditText) findViewById(R.id.cet_clearedit);
        lv_hostory_list = (MyListView) findViewById(R.id.lv_hostory_list);
        rl_search_hostory = (RelativeLayout) findViewById(R.id.rl_search_hostory);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        getTagData();
        mListHostory = PublicUtils.getSearchHostory();
        if (mListHostory.size() > 0) {
            rl_search_hostory.setVisibility(View.VISIBLE);
            lv_hostory_list.setVisibility(View.VISIBLE);
            hostoryAdapter = new HostoryAdapter(SearchActivity.this, mListHostory);
            lv_hostory_list.setAdapter(hostoryAdapter);
        } else {
            rl_search_hostory.setVisibility(View.GONE);
            lv_hostory_list.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_quxiao.setOnClickListener(this);
        cet_clearedit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    search();
                    return true;
                }

                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_quxiao:
                SearchActivity.this.finish();
                break;
        }
    }

    private void getTagData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(SearchActivity.this, getString(R.string.searchtag_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        Message msg = new Message();
                        msg.obj = data_msg;
                        handler.sendMessage(msg);

                    } else {
                        ToastUtils.showCenter(SearchActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getSearchHotWords(callBack);

    }


    // 搜索功能
    private void search() {

        String searchContext = cet_clearedit.getText().toString().trim();
        if (TextUtils.isEmpty(searchContext.trim())) {
            ToastUtils.showCenter(this, "请输入搜索内容");
        } else {
            PublicUtils.saveSearchHostory(searchContext.trim());
            Intent intent = new Intent();
            intent.putExtra("search_info", searchContext);
            intent.putExtra("search_tag", "");
            setResult(10, intent);
            SearchActivity.this.finish();
        }

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String dataStr = (String) msg.obj;
            HotWordsBean hotWordsBean = GsonUtil.jsonToBean(dataStr, HotWordsBean.class);
            List<String> listHot = hotWordsBean.getHot_words();
            myData = new String[listHot.size()];
            for (int i = 0; i < listHot.size(); i++) {
                myData[i] = listHot.get(i);
            }
            his_flowLayout.setColorful(false);
            his_flowLayout.setData(myData);
            his_flowLayout.setOnTagClickListener(new FlowLayoutSearch.OnTagClickListener() {
                @Override
                public void TagClick(String text) {
                    Intent intent = new Intent();
                    intent.putExtra("search_tag", text);
                    intent.putExtra("search_info", "");
                    setResult(10, intent);
                    SearchActivity.this.finish();
                }
            });
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && requestCode == 1) {

            SearchActivity.this.finish();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
    }


    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
    }


    class HostoryAdapter extends BaseAdapter {

        private Context context;
        private List<String> list;

        public HostoryAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            MyHolder myHolder = null;
            if (null == convertView) {
                myHolder = new MyHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_search_hostory, null);
                myHolder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
                myHolder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            myHolder.tv_item_name.setText(list.get(position));
            myHolder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublicUtils.replaceSearchHostory(list.get(position));
                    Intent intent = new Intent();
                    intent.putExtra("search_info", list.get(position));
                    intent.putExtra("search_tag", "");
                    setResult(10, intent);
                    SearchActivity.this.finish();
                }
            });
            return convertView;
        }

        class MyHolder {
            private RelativeLayout rl_item;
            private TextView tv_item_name;
        }
    }


}