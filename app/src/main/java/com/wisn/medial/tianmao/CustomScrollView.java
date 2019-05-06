package com.wisn.medial.tianmao;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by Wisn on 2019-05-05 13:51.
 */
public class CustomScrollView extends NestedScrollView {

    private int slop;
    private int touch;
    public Callbacks mCallbacks;

    public CustomScrollView(Context context) {
        super(context);
        setSlop(context);

    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSlop(context);

    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSlop(context);

    }

  /*  public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setSlop(context);

    }*/

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(l, t, oldl, oldt);
        }
    }

    //定义接口用于回调
    public interface Callbacks {
        void onScrollChanged(int x, int y, int oldx, int oldy);
    }


    /**
     * 是否intercept当前的触摸事件
     * @param ev 触摸事件
     * @return true：调用onMotionEvent()方法，并完成滑动操作
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //  保存当前touch的纵坐标值
                touch = (int) ev.getRawY();
                break;
           /* case MotionEvent.ACTION_MOVE:
                //  滑动距离大于slop值时，返回true
                if (Math.abs((int) ev.getRawY() - touch) > slop) return false;
                break;*/
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 获取相应context的touch slop值（即在用户滑动之前，能够滑动的以像素为单位的距离）
     * @param context ScrollView对应的context
     */
    private void setSlop(Context context) {
        slop = ViewConfiguration.get(context).getScaledTouchSlop()*2;
    }

}
