/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ViSenze Pte. Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.homechart.app.visearch.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.shopdetails.ShopDetailsItemInfoBean;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.squareup.picasso.Picasso;
import com.visenze.visearch.android.model.ImageResult;

import java.util.List;

/**
 * Created by visenze on 15/12/15.
 */
public class SquareImageAdapter extends ArrayAdapter<ShopDetailsItemInfoBean> {
    private Activity mContext;
    private List<ShopDetailsItemInfoBean> imageList;

    public SquareImageAdapter(Activity c, List<ShopDetailsItemInfoBean> imageList) {
        super(c, R.layout.result_layout, imageList);
        this.mContext = c;
        this.imageList = imageList;
    }

    // create a new imageLayout for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View imageLayout = convertView;
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();

            imageLayout = inflater.inflate(R.layout.grid_item_details, null);
            viewHolder = new ViewHolder();

            viewHolder.scoreView = (TextView)imageLayout.findViewById(R.id.score_view);
            viewHolder.tv_item_name = (TextView)imageLayout.findViewById(R.id.tv_item_name);
            viewHolder.tv_item_price = (TextView)imageLayout.findViewById(R.id.tv_item_price);
            viewHolder.imageView = (ImageView)imageLayout.findViewById(R.id.result_image_view);

            imageLayout.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)imageLayout.getTag();
        }
        viewHolder.tv_item_name.setText(imageList.get(position).getTitle());
        viewHolder.tv_item_price.setText("¥ "+imageList.get(position).getPrice());
        ImageUtils.displayFilletImage(imageList.get(position).getImage().getImg0(),viewHolder.imageView);

//        Picasso.with(mContext)
//                .load(imageList.get(position).getImageUrl())
//                .placeholder(R.drawable.spinner_10)
//                .error(R.drawable.spinner_10)
//                .centerInside()
//                .fit()
//                .tag(mContext)
//                .into(viewHolder.imageView);
//        int score = 0;
//        if(imageList.get(position).getScore() != null){
//            score = (int)(imageList.get(position).getScore() * 100);
//        }
//        viewHolder.scoreView.setText("相识度 " + String.valueOf(score) + "%");

        return imageLayout;
    }

    class ViewHolder {
        public TextView scoreView;
        public TextView tv_item_name;
        public TextView tv_item_price;
        public ImageView imageView;
    }
}
