package com.wisn.medial.tianmao.banner2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wisn.medial.R;
import com.wisn.medial.src.Constants;

/**
 * Created by Wisn on 2019-05-07 10:29.
 */
public class CustomViewHolder2 implements BannerViewHolder<CustomData> {

    private TextView mTitle;
    private ImageView image;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
        mTitle = (TextView) view.findViewById(R.id.title);
        image = (ImageView) view.findViewById(R.id.image);
        return view;
    }

    @Override
    public void onBind(Context context, int position, CustomData data) {
        // 数据绑定
        mTitle.setText("可点击：标题" + data.getName() + " position:" + position);
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"position:"+position,Toast.LENGTH_SHORT).show();
            }
        });
         Glide.with(context).load(Constants.res[position]).dontAnimate().into(image);

    }
}
