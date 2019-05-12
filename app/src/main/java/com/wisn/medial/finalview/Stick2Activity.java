package com.wisn.medial.finalview;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.wisn.medial.R;

/**
 * Created by Wisn on 2019-05-05 13:53.
 */
public class Stick2Activity extends AppCompatActivity {
    private FrameLayout fl_content;
    private TestScrollFragment imageListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fl_content = findViewById(R.id.fl_content);
        imageListFragment = new TestScrollFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_content, imageListFragment).commitAllowingStateLoss();
    }

}
