package com.wisn.medial.ad.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Wisn on 2019/4/20 下午4:47.
 */
public class AutoScrollViewPager extends ViewPager {
    public AutoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public AutoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            isTouch=true;
        }else  if(ev.getAction()==MotionEvent.ACTION_UP){
            isTouch=false;
        }
        return super.onTouchEvent(ev);
    }
    public boolean isTouch;
}
