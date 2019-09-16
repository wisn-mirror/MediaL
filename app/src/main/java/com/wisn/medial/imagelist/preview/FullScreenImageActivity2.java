package com.wisn.medial.imagelist.preview;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.wisn.medial.R;

/**
 * Created by Wisn on 2019-04-25 10:36.
 */
public class FullScreenImageActivity2 extends AppCompatActivity {
    private FrameLayout fl_content;
    private FullScreenImagefragment imageListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        fl_content = findViewById(R.id.fl_content);
        imageListFragment = new FullScreenImagefragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_content, imageListFragment).commitAllowingStateLoss();

    }

//    public void onActivityReenter(int resultCode, Intent data) {
//        if (imageListFragment != null) {
//            imageListFragment.onActivityReenter(resultCode, data);
//        }
//    }
}
