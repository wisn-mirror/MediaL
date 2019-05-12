package com.wisn.medial.tianmao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.wisn.medial.tianmao.refresh.LyfMineRefreshHeader;

/**
 * Created by Wisn on 2019-05-08 11:34.
 */
public class mClassicsHeader extends LyfMineRefreshHeader {
    MView mView;

    public mClassicsHeader(Context context) {
        super(context);
    }

    public mClassicsHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public mClassicsHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        super.onMoving(isDragging, percent, offset, height, maxDragHeight);
        if (mView != null) mView.onMoving(isDragging, percent, offset, height, maxDragHeight);
    }

    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (mView != null) mView.onFinish(layout, success);
        return super.onFinish(layout, success);
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        super.onReleased(refreshLayout, height, maxDragHeight);
        if (mView != null) mView.onReleased(refreshLayout, height, maxDragHeight);
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        super.onStateChanged(refreshLayout, oldState, newState);
//        mView.onStateChanged(refreshLayout,oldState,newState);
    }

    public void setmView(MView mView) {
        this.mView = mView;
    }
}
