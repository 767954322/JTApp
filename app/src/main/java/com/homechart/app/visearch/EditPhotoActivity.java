/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 ViSenze Pte. Ltd.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.homechart.app.visearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.homechart.app.R;
import com.homechart.app.home.bean.searchfservice.SearchSBean;
import com.homechart.app.utils.ToastUtils;

/**
 * Created by yulu on 12/3/15.
 */
public class EditPhotoActivity
        extends FragmentActivity{

    private String imagePath;
    private SearchSBean searchSBean;
    private String image_id;
    private String searchstatus;
    private String network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        Intent intent =  getIntent();
        image_id =intent.getStringExtra("image_id");
        imagePath = intent.getStringExtra("imagePath");
        searchstatus = intent.getStringExtra("searchstatus");
        network = intent.getStringExtra("network");
        searchSBean = (SearchSBean)intent.getSerializableExtra("searchSBean");

        if(!TextUtils.isEmpty(searchstatus) && searchstatus.equals("1")){
//            ToastUtils.showCenter(EditPhotoActivity.this,"未识别到相识商品！");
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_holder, PhotoEditFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public String getImagePath() {
        return imagePath;
    }

    public SearchSBean getSearchSBean() {
        return searchSBean;
    }

    public String getNetwork() {
        return network;
    }
}
