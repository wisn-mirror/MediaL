package com.wisn.medial;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wisn.medial.ad.ExoPlayerActivity;
import com.wisn.medial.ad.ImageSlideAcitivity;
import com.wisn.medial.ad.VideoViewActivity;
import com.wisn.medial.download.DownloadListActivity;
import com.wisn.medial.finalview.Stick2Activity;
import com.wisn.medial.imagelist.ImageListActivity;
import com.wisn.medial.tianmao.StickActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        startActivity(new Intent(this, Stick2Activity.class));

    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.bt_ad_video:
                startActivity(new Intent(this, VideoViewActivity.class));
                break;
            case R.id.bt_ad_videoexplayer:
                startActivity(new Intent(this, ExoPlayerActivity.class));
                break;
            case R.id.bt_ad_image:
                startActivity(new Intent(this, ImageSlideAcitivity.class));
                break;
            case R.id.bt_imglist:
                startActivity(new Intent(this, ImageListActivity.class));
                break;
            case R.id.bt_videoimglist:
                break;
            case R.id.bt_downloadlist:
                startActivity(new Intent(this, DownloadListActivity.class));
                break;
            case R.id.bt_scale:
                startActivity(new Intent(this, DownloadListActivity.class));
                break;
            case R.id.bt_demo:
//                startActivity(new Intent(this, SlideDemo.class));
                startActivity(new Intent(this, StickActivity.class));
                break;

        }
    }
}
