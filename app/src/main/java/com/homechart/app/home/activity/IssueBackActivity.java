package com.homechart.app.home.activity;

import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.UrlConstants;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.Md5Util;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.glide.GlideImgManager;
import com.homechart.app.utils.volley.FileHttpManager;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.utils.volley.PutFileCallBack;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class IssueBackActivity
        extends BaseActivity
        implements View.OnClickListener,
        PutFileCallBack {
    private GridView mAddPic;
    private final int REQUEST_CODE_GALLERY = 1;
    private List<String> list;
    private MyIssueAdapter adapter;
    public static final int IMAGE_REQUEST_CODE = 0x102;
    private ImageButton mBack;
    private EditText mETContent;
    private EditText mETPhone;
    private Button btn_send_issue;
    private List<String> listPicId;
    private TextView mTital;
    private TextView tv_last_num;

    @Override
    protected int getLayoutResId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_issueback;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("使用反馈页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("使用反馈页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void initView() {
        mAddPic = (GridView) findViewById(R.id.gv_addissue_image);
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        tv_last_num = (TextView) findViewById(R.id.tv_last_num);
        mETContent = (EditText) findViewById(R.id.rt_content_issue);
        mETPhone = (EditText) findViewById(R.id.rt_phone_issue);
        btn_send_issue = (Button) findViewById(R.id.btn_send_issue);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
        btn_send_issue.setOnClickListener(this);
        mETContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int textSize = s.length();
                Message message = new Message();
                message.arg1 = textSize;
                handler.sendMessage(message);
            }
        });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_last_num.setText((500 - msg.arg1) + "");
        }
    };

    @Override
    protected void initData(Bundle savedInstanceState) {
        listPicId = new ArrayList<String>();
        list = new ArrayList<String>();
        mTital.setText("使用反馈");
        adapter = new MyIssueAdapter(list, IssueBackActivity.this);
        mAddPic.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                IssueBackActivity.this.finish();
                break;
            case R.id.btn_send_issue:
                listPicId.clear();
                String content = mETContent.getText().toString();
                if (TextUtils.isEmpty(content) && list.size() == 0) {
                    ToastUtils.showCenter(IssueBackActivity.this, "给点建议吧");
                } else {
                    CustomProgress.show(IssueBackActivity.this, "发送中...", false, null);
                    if (list.size() != 0) {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                                String signString = PublicUtils.getSinaString(map);
                                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                                for (int i = 0; i < list.size(); i++) {

                                    FileHttpManager.getInstance().uploadFile(IssueBackActivity.this, new File(list.get(i)),
                                            UrlConstants.PUT_IMAGE,
                                            map,
                                            PublicUtils.getPublicHeader(MyApplication.getInstance()));

                                }
                            }
                        }.start();
                    } else {
                        sendIssue();
                    }
                }


                break;
        }
    }

    @Override
    public void onSucces(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
            String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
            String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
            if (error_code == 0) {
                JSONObject jsonObject1 = new JSONObject(data_msg);
                String immage_id = jsonObject1.getString(ClassConstant.IssueBack.IMMAGE_ID);
                listPicId.add(immage_id);
                if (listPicId.size() == list.size()) {
                    sendIssue();
                }
            } else {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(IssueBackActivity.this, error_msg);
            }
        } catch (JSONException e) {
        }

    }

    private void sendIssue() {

        String content = mETContent.getText().toString();
        String phone = mETPhone.getText().toString();
        String image_id = "";
        if (listPicId.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < listPicId.size(); i++) {
                if (i == listPicId.size() - 1) {
                    sb.append(listPicId.get(i));
                } else {
                    sb.append(listPicId.get(i) + ",");
                }
            }
            image_id = sb.toString();
        }

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(IssueBackActivity.this, "发送失败");
            }

            @Override
            public void onResponse(String s) {
                CustomProgress.cancelDialog();
                IssueBackActivity.this.finish();
                ToastUtils.showCenter(IssueBackActivity.this, "反馈成功");
            }
        };

        MyHttpManager.getInstance().issueBack(phone, content, image_id, callBack);

    }

    @Override
    public void onFails() {

        Log.d("test", "error");
    }

    class MyIssueAdapter extends BaseAdapter {

        private List<String> picList;
        private Context context;

        public MyIssueAdapter(List<String> picList, Context context) {
            this.picList = picList;
            this.context = context;
        }

        @Override
        public int getCount() {
            if (list.size() != 4) {
                return picList.size() + 1;
            } else {
                return 4;
            }
        }

        @Override
        public Object getItem(int position) {
            return position == picList.size() ? "" : picList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MyHolder myHolder;
            if (convertView == null) {
                myHolder = new MyHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_addissue_pic, null);
                myHolder.iv_Pic = (ImageView) convertView.findViewById(R.id.iv_issue_add_pic);
                myHolder.iv_Del = (ImageView) convertView.findViewById(R.id.iv_issue_delete_pic);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            ViewGroup.LayoutParams layoutParams = myHolder.iv_Pic.getLayoutParams();
            layoutParams.height = layoutParams.width;
            myHolder.iv_Pic.setLayoutParams(layoutParams);
            if (position == list.size()) {
                myHolder.iv_Del.setVisibility(View.GONE);
                GlideImgManager.glideLoader(context, "", R.drawable.addpic, R.drawable.addpic, myHolder.iv_Pic);
            } else {
                myHolder.iv_Del.setVisibility(View.VISIBLE);
                GlideImgManager.glideLoader(context, list.get(position), R.color.white, R.color.white, myHolder.iv_Pic);
            }
            myHolder.iv_Pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.size() != 4) {
                        if (position == list.size()) {
                            addPic();
                        }
                    }
                }
            });
            myHolder.iv_Del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePic(position);
                }
            });
            return convertView;
        }

        class MyHolder {
            private ImageView iv_Pic;
            private ImageView iv_Del;
        }
    }

    //添加反馈图片
    private void addPic() {
        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList != null && resultList.size() > 0) {
                    if (reqeustCode == REQUEST_CODE_GALLERY) {
                        String picPath = resultList.get(0).getPhotoPath();
                        list.add(picPath);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showCenter(IssueBackActivity.this, "图片资源获取失败");
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
            }
        });
    }

    //删除反馈图片
    private void deletePic(int position) {
        list.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            ToastUtils.showCenter(IssueBackActivity.this, "路径" + imagePath);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("使用反馈页");
        MobclickAgent.onPause(this);
    }
}
