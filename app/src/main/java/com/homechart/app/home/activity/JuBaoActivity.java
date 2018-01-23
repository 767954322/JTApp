package com.homechart.app.home.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.contract.ItemClickJuBao;
import com.homechart.app.home.adapter.MyJuBaoAdapter;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.jubaobean.JuBaoBean;
import com.homechart.app.home.bean.jubaobean.JuBaoItemBean;
import com.homechart.app.myview.MyListView;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gumenghao on 18/1/23.
 */

public class JuBaoActivity extends BaseActivity implements View.OnClickListener, ItemClickJuBao {
    private String item_id;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private TextView tv_content_right;
    private TextView et_liyou;
    private MyListView mlv_listview;
    private int clickPosition = 0;
    private List<JuBaoItemBean> mList = new ArrayList<>();
    private MyJuBaoAdapter myAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_jubao;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        item_id = getIntent().getStringExtra("item_id");
    }

    @Override
    protected void initView() {

        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        mlv_listview = (MyListView) findViewById(R.id.mlv_listview);
        et_liyou = (TextView) findViewById(R.id.et_liyou);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("举报图片");
        tv_content_right.setText("提交");

        myAdapter = new MyJuBaoAdapter(JuBaoActivity.this, mList, this);
        mlv_listview.setAdapter(myAdapter);
        mlv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickPosition = position;
                myAdapter.notifyDataSetChanged();
            }
        });
        getJuBaoList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                JuBaoActivity.this.finish();
                break;
            case R.id.tv_content_right:
                break;
        }
    }

    private void getJuBaoList() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(JuBaoActivity.this, "举报选项获取失败！");
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
                        msg.obj = "{\"data\":" + data_msg + "}";
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(JuBaoActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(JuBaoActivity.this, "举报选项获取失败！");
                }
            }
        };
        MyHttpManager.getInstance().getJuBaoList("item", callBack);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    String str = (String) msg.obj;
                    JuBaoBean juBaoBean = GsonUtil.jsonToBean(str, JuBaoBean.class);
                    mList.addAll(juBaoBean.getData());
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void clickItem(int position) {
        myAdapter.notifyDataSetChanged();
    }
}
