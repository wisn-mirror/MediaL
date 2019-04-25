package com.wisn.medial.ad;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Scroller;

import com.wisn.medial.GlideApp;
import com.wisn.medial.R;
import com.wisn.medial.ad.view.AutoScrollViewPager;
import com.wisn.medial.src.Constants;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wisn on 2019/4/20 下午4:19.
 */
public class ImageSlideAcitivity extends Activity {

    private AutoScrollViewPager vp_scroll;
    private ImageView iv_test;
    private PagerAdapter adapter;
    MyHandler handler ;
    private List<ImageView> allImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new MyHandler(this);
        setContentView(R.layout.activity_imageslide);
        vp_scroll = (AutoScrollViewPager) findViewById(R.id.vp_scroll);
        iv_test = (ImageView) findViewById(R.id.iv_test);
//        GlideApp.lo
        allImageView = new ArrayList<>();

        GlideApp.with(ImageSlideAcitivity.this).load(Constants.res[0]).into(iv_test);

        //最后一个添加到第一个
        ImageView imageViewone = new ImageView(this);
        imageViewone.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        GlideApp.with(ImageSlideAcitivity.this).load(Constants.res[3]).into(imageViewone);
        allImageView.add(imageViewone);
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            GlideApp.with(ImageSlideAcitivity.this).load(Constants.res[i]).into(imageView);
            allImageView.add(imageView);
        }
        //第一个添加到最后一个
        ImageView imageViewlast = new ImageView(this);
        imageViewlast.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        GlideApp.with(ImageSlideAcitivity.this).load(Constants.res[0]).into(imageViewlast);
        allImageView.add(imageViewlast);
        adapter = new PagerAdapter() {

            @Override
            public int getCount() {
                return allImageView.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//                super.destroyItem(container, position, object);
                container.removeView((View) object);
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
//                return super.instantiateItem(container, position);
                ImageView imageView = allImageView.get(position);
                container.addView(imageView);
                return imageView;
            }

        };
        vp_scroll.setAdapter(adapter);
        //修改滑动速度
        final MyScroller myScroller = new MyScroller(this);
        myScroller.setmDuration(2000);
        myScroller.acttchToViewPager(vp_scroll);
        vp_scroll.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(final int position) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (position == allImageView.size() - 1) {
                            vp_scroll.setCurrentItem(1, false);//不要动画
                        } else if (position == 0) {
                            vp_scroll.setCurrentItem(allImageView.size() - 2, false);//不要动画
                        }
                    }
                },myScroller.getmDuration());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        vp_scroll.setCurrentItem(1, false);
        handler.sendEmptyMessageDelayed(100,2000);

    }
    public void nextPage(){
        if(!vp_scroll.isTouch){
            int index= ( vp_scroll.getCurrentItem()+1)%allImageView.size();
            vp_scroll.setCurrentItem(index);
        }

    }
    static  class MyScroller extends Scroller{
        private int mDuration = 1500; // default time is 1500ms

        public MyScroller(Context context) {
            super(context);
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public MyScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }
        public void setmDuration(int time) {
            mDuration = time;
        }
        public int getmDuration() {
            return mDuration;
        }
        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void acttchToViewPager(ViewPager viewPager) {
            try {
                Field mField = ViewPager.class.getDeclaredField("mScroller");
                mField.setAccessible(true);
                mField.set(viewPager, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class MyHandler extends Handler {
        WeakReference<ImageSlideAcitivity> imageSlideAcitivityWeakReference;
        public MyHandler(ImageSlideAcitivity acitivity){
            imageSlideAcitivityWeakReference=new WeakReference<ImageSlideAcitivity>(acitivity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageSlideAcitivity imageSlideAcitivity = imageSlideAcitivityWeakReference.get();
            if(imageSlideAcitivity!=null){
                if(msg.what==100){
                    imageSlideAcitivity.nextPage();
                    this.sendEmptyMessageDelayed(100,2000);
                }

            }
        }
    }


}
