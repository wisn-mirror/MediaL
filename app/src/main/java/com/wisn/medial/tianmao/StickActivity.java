package com.wisn.medial.tianmao;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.wisn.medial.R;

/**
 * Created by Wisn on 2019-05-05 13:53.
 */
public class StickActivity extends AppCompatActivity {
    private FrameLayout fl_content;
    private HomeFragment imageListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        fl_content = findViewById(R.id.fl_content);
        imageListFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_content, imageListFragment).commitAllowingStateLoss();

    }

}
