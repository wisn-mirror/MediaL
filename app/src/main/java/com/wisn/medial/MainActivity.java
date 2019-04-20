package com.wisn.medial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wisn.medial.ad.ImageSlideAcitivity;
import com.wisn.medial.ad.VideoViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.bt_ad:
//                startActivity(new Intent(this, VideoViewActivity.class));
                startActivity(new Intent(this, ImageSlideAcitivity.class));
                break;
            case R.id.bt_imglist:
                break;
            case R.id.bt_videoimglist:
                break;

        }
    }
}
