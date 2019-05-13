package com.wisn.medial.tianmao;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

/**
 * Created by Wisn on 2019-05-08 11:35.
 */
public class MView extends RelativeLayout {
    protected Path mPath;
    protected Paint mBackPaint;
    protected float mWaveHeightPre;
    protected float mWaveHeight;
    protected float mHeadHeight;
    protected boolean mWavePulling = false;
    protected boolean isAnimation = false;
    private int viewHeight;
    private int color;
    private int minHeiht;

    public MView(Context context) {
        super(context);
        init(context, null);
    }

    public MView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setColor(int color) {
        this.color = color;
        mBackPaint.setColor(color);
        invalidate();
    }

    public void setConfig(int minHeiht, int mWaveHeightPre) {
        this.minHeiht = minHeiht;
        this.mWaveHeightPre = mWaveHeightPre;
        this.mWaveHeight=mWaveHeightPre;
        setMinimumHeight(minHeiht);
        invalidate();
    }

    public void init(Context context, AttributeSet attrs) {
        setMinimumHeight(minHeiht);
        mBackPaint = new Paint();
        mBackPaint.setColor(color);
        mBackPaint.setAntiAlias(true);
        mPath = new Path();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        final int viewWidth = getWidth();
        viewHeight = getHeight();
        mHeadHeight = viewHeight / 2f;
        drawWave(canvas, viewWidth, viewHeight);
        invalidate();
        super.dispatchDraw(canvas);
    }

    private void drawWave(Canvas canvas, int viewWidth, int viewHeight) {
        float baseHeight = Math.min(mHeadHeight, viewHeight);
        if (mWaveHeight != 0) {
            mPath.reset();
            mPath.lineTo(viewWidth, 0);
            mPath.lineTo(viewWidth, baseHeight);
            mPath.quadTo(viewWidth / 2f, baseHeight + mWaveHeight * 2, 0, baseHeight);
            mPath.close();
            canvas.drawPath(mPath, mBackPaint);
        }else {
            canvas.drawRect(0, 0, viewWidth, baseHeight, mBackPaint);
            mPath.reset();
            mPath.lineTo(viewWidth, 0);
            mPath.lineTo(viewWidth, baseHeight);
            mPath.quadTo(viewWidth / 2f, baseHeight + mWaveHeight * 2, 0, baseHeight);
            mPath.close();
            canvas.drawPath(mPath, mBackPaint);
        }
    }

    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        Log.d("MView", "onMoving isDragging:" + isDragging + " percent:" + percent + " offset:" + offset + " height:" + height + " maxDragHeight:" + maxDragHeight);
        if (isDragging) {
            mWavePulling = true;
            mHeadHeight = height;
            mWaveHeight = Math.max(offset*1.6f - height * 0.6f, mWaveHeightPre) * .8f;
        }
    }


    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        Log.d("MView", "onReleased height:" + height + " maxDragHeight:" + maxDragHeight);
        mWavePulling = false;
        mHeadHeight = height;
        vaveanimator();
    }

    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        Log.d("MView", "onFinish success:" + success);
        final int DURATION_FINISH = 800; //动画时长
        mWavePulling = false;
        vaveanimator();
        return DURATION_FINISH;
    }


    private void vaveanimator() {
        isAnimation = true;
        DecelerateInterpolator interpolator = new DecelerateInterpolator();
        final float reboundHeight = Math.min(mWaveHeight * 0.8f, mHeadHeight / 1.6f);
//        ValueAnimator waveAnimator = ValueAnimator.ofFloat(
//                mWaveHeight, mWaveHeightPre,
//                -(reboundHeight * 1.0f), mWaveHeightPre,
//                -(reboundHeight * 0.4f), mWaveHeightPre
//        );
        ValueAnimator waveAnimator = ValueAnimator.ofFloat(
                mWaveHeight, mWaveHeightPre
        );
        waveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float curValue = (float) animation.getAnimatedValue();
                Log.d("MView", "onAnimationUpdate curValue:" + curValue);
//                if (!mWavePulling) {
//                    mWaveHeight = curValue;
//                }
                mWaveHeight = curValue;

                if (0f == curValue) {
                    Log.d("MView", "onAnimationUpdate aaaaa:" + curValue);
                    isAnimation = true;
                }
                MView.this.invalidate();
            }
        });
        waveAnimator.setInterpolator(interpolator);
        waveAnimator.setDuration(300);
        waveAnimator.start();
    }

}
