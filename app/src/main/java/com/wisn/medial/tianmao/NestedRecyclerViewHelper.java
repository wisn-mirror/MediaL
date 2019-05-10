package com.wisn.medial.tianmao;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Wisn on 2019-05-10 12:55.
 */
public class NestedRecyclerViewHelper {
    private static final String TAG = NestedRecyclerViewHelper.class.getSimpleName();
    int hfOffsetY = 0;
    RecyclerView.LayoutManager layoutManager;
    //外层包裹的nestedScrollView的高度
    private CustomScrollView headZoomNestScrollview;


    public NestedRecyclerViewHelper(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    /**
     * 设置外层高度
     *
     * @param nestedScrollView
     */
    public void setNestedScrollView(CustomScrollView nestedScrollView) {
        this.headZoomNestScrollview = nestedScrollView;
        headZoomNestScrollview.setCallback2(new CustomScrollView.Callback2() {
            @Override
            public void onScrollChanged(int x, int y, int oldx, int oldy) {
                setOffset(y);
            }
        });
    }


    /**
     * 设置偏移量
     *
     * @param nestedScrollOffset
     */
    public void setOffset(int nestedScrollOffset) {
        this.hfOffsetY = nestedScrollOffset;
    }

    /**
     * 判断child是否可见
     *
     * @param child
     *
     * @return
     */
    public boolean isChildVisible(View child) {
        return this.isChildVisible(child, hfOffsetY);
    }

    /**
     * 判断当前View是否展示
     *
     * @param child
     * @param nestedScrollOffset
     */
    public boolean isChildVisible(View child, int nestedScrollOffset) {
        this.hfOffsetY = nestedScrollOffset;

        int start = getParentStart();
        int end = getParentEnd();
        int childStart = getChildStart(child);
        int childEnd = getChildEnd(child);

        if (childEnd > start && childStart < end) {
            return true;
        }
        return false;
    }

    /**
     * 获取child的结束位置
     *
     * @param view
     *
     * @return
     */
    public int getChildEnd(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return layoutManager.getDecoratedBottom(view) + params.bottomMargin + +hfOffsetY;
    }

    /**
     * 获取child的开始位置
     *
     * @param view
     *
     * @return
     */
    public int getChildStart(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedTop(view) - params.topMargin;
    }

    /**
     * 获取child的布局位置
     *
     * @param child
     *
     * @return
     */
    public int getDecoratedTop(View child) {
        return child.getTop() - layoutManager.getTopDecorationHeight(child) + hfOffsetY;
    }

    /**
     * @return
     */
    public int getParentStart() {
        //48是导航高度
        return hfOffsetY > dip2px(48) ? hfOffsetY :dip2px(48);
    }

    public int getParentEnd() {
        //offset 滑动减头部
        if (headZoomNestScrollview == null) {
            return 0;
        }
        return headZoomNestScrollview.getHeight();
    }

    public int dip2px(float dpValue) {
        final float scale =headZoomNestScrollview.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
