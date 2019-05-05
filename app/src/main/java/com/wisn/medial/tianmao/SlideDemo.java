package com.wisn.medial.tianmao;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.wisn.medial.GlideApp;
import com.wisn.medial.R;
import com.wisn.medial.src.Constants;

/**
 * Created by Wisn on 2019-05-05 10:48.
 */
public class SlideDemo extends AppCompatActivity {

    private VpRecycleView vrv_mindle;

    float widthPixels = 0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tianmao);
        vrv_mindle = findViewById(R.id.vrv_mindle);

        widthPixels = getScreenWidth();
        vrv_mindle.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                ImageView imageView = new ImageView(SlideDemo.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return new ViewHoderM(imageView);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
                ImageView imageView = (ImageView) viewHolder.itemView;
//                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                if (getItemCount() == 1) {
                    params.width = (int) widthPixels;
                } else {
                    if (position == 0) {
                        //第一个 //若超过一个活动，单个item屏幕90%比例
                        params.width = (int) (widthPixels / 10 * 8.8);
//                        mMarginLeft = px_margin_dp;
//                        mMarginRight = px_margin_dp_helf;

                    } else if (position == getItemCount() - 1) {
                        //最后一个
                        params.width = (int) (widthPixels / 10 * 8.8);
//                        mMarginLeft = px_margin_dp_helf;
//                        mMarginRight = px_margin_dp;
                    } else {
                        //中间的
                        params.width = (int) (widthPixels / 10 * 8);
//                        mMarginLeft = px_margin_dp_helf;
//                        mMarginRight = px_margin_dp_helf;

                    }
                }
                imageView.setLayoutParams(params);
//                params.setMargins(mMarginLeft, 0, mMarginRight, 0);

                GlideApp.with(SlideDemo.this).load(Constants.res[position]).into(imageView);
            }

            @Override
            public int getItemCount() {
//                return Constants.res.length;
                return 3;
            }
        });

    }

    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.widthPixels;
    }

    static class ViewHoderM extends RecyclerView.ViewHolder {
        public ViewHoderM(@NonNull View itemView) {
            super(itemView);
        }
    }
}
