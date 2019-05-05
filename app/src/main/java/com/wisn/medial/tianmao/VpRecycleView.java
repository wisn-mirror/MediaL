package com.wisn.medial.tianmao;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Wisn on 2018/12/4 上午10:50.
 *
 */

public class VpRecycleView extends RecyclerView {
    private int position = 0;


    public VpRecycleView(Context context) {
        super(context);
        init();
    }

    public VpRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.setLayoutManager(llm);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(this);//居中显示RecyclerView
        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                try {
                    LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        int firs = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                        int count = ((LinearLayoutManager) layoutManager).getItemCount();
                        if (count <= 1) {
                            position = 0;
                        } else {
                            if (firs == 0) {
                                View viewByPosition = layoutManager.findViewByPosition(firs);
//                                int left = Math.abs(viewByPosition.getLeft());
                                if (viewByPosition.getLeft()<0) {
                                    position = 1;
                                } else {
                                    position = 0;
                                }
                            } else {
                                position = firs + 1;

                            }
                        }
                    }
                    if (onpagerChageListener != null)
                        onpagerChageListener.onPagerChage(position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        this.requestDisallowInterceptTouchEvent(true); //事件不传递给父布局
        return super.dispatchTouchEvent(ev);
    }

    public void setOnPagerPosition(int position) {
        RecyclerView.LayoutManager layoutManager = this.getLayoutManager();
        layoutManager.scrollToPosition(position);
    }

    public int getOnPagerPosition() {
        RecyclerView.LayoutManager layoutManager = this.getLayoutManager();
        return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
    }

    public interface onPagerChageListener {
        void onPagerChage(int position);
    }

    private onPagerChageListener onpagerChageListener;

    public void setOnPagerChageListener(onPagerChageListener onpagerChageListener) {
        this.onpagerChageListener = onpagerChageListener;
    }

}
