package com.wisn.medial.tianmao.refresh;


import android.content.Context;
import android.graphics.drawable.RotateDrawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.wisn.medial.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.scwang.smartrefresh.layout.constant.RefreshState.None;
import static com.scwang.smartrefresh.layout.constant.RefreshState.PullDownToRefresh;
import static com.scwang.smartrefresh.layout.constant.RefreshState.RefreshFinish;
import static com.scwang.smartrefresh.layout.constant.RefreshState.RefreshReleased;
import static com.scwang.smartrefresh.layout.constant.RefreshState.Refreshing;
import static com.scwang.smartrefresh.layout.constant.RefreshState.ReleaseToRefresh;

/**
 * Created by Wisn on 2018/5/18 下午2:24.
 */
public class LyfMineRefreshHeader extends InternalAbstract implements RefreshHeader {


    private static final String TAG = "LyfRefreshHeader";
    public static final int MODEL_ONE = 1;//1：表示progress+文字；
    public static final int MODEL_TWO = 2;//2：表示只有progress ，完成刷新后弹出toast；
    protected int mPaddingTop = 10;
    protected int mPaddingBottom = 10;
    protected int mInitPaddingBottom = 4;
    protected int mInitPaddingTop = 4;
    private ImageView icon;
    private TextView mTitleText;
    private TextView mRefreshResult;

    private RefreshKernel mRefreshKernel;
    protected ImageView mProgressView;
    private int delay = 0;
    private Context mContext;
    private int model = 1;
    private RotateDrawable mProgressDrawable;
    private RotateAnimation mRotateAnimation;

    public LyfMineRefreshHeader(Context context) {
        this(context, null);
    }

    public LyfMineRefreshHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyfMineRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {

        View view = inflate(context, R.layout.lyf_minerefresh_header, null);
        mTitleText = view.findViewById(R.id.title_text);
        mProgressView = view.findViewById(R.id.progress_view);
        mRefreshResult = view.findViewById(R.id.refresh_result);

        mProgressView.setImageResource(R.drawable.progressbar_rotate_white);
        mProgressDrawable = (RotateDrawable) mProgressView.getDrawable();

        final View thisView = this;
        final DensityUtil density = new DensityUtil();

        mTitleText.setTextColor(0x8affffff);
//        mProgressView.setImageDrawable(getResources().getDrawable(R.drawable.progressbar_white));

        LayoutParams layoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        this.addView(view, layoutParams);
        if (thisView.getPaddingTop() == 0) {
            if (thisView.getPaddingBottom() == 0) {
                thisView.setPadding(thisView.getPaddingLeft(), mPaddingTop = density.dip2px(mInitPaddingTop), thisView.getPaddingRight(), mPaddingBottom = density.dip2px(mInitPaddingBottom));
            } else {
                thisView.setPadding(thisView.getPaddingLeft(), mPaddingTop = density.dip2px(mInitPaddingTop), thisView.getPaddingRight(), mPaddingBottom = thisView.getPaddingBottom());
            }
        } else {
            if (thisView.getPaddingBottom() == 0) {
                thisView.setPadding(thisView.getPaddingLeft(), mPaddingTop = thisView.getPaddingTop(), thisView.getPaddingRight(), mPaddingBottom = density.dip2px(mInitPaddingBottom));
            } else {
                mPaddingTop = thisView.getPaddingTop();
                mPaddingBottom = thisView.getPaddingBottom();
            }
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final View thisView = this;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            thisView.setPadding(thisView.getPaddingLeft(), 0, thisView.getPaddingRight(), 0);
        } else {
            thisView.setPadding(thisView.getPaddingLeft(), mPaddingTop, thisView.getPaddingRight(), mPaddingBottom);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        mRefreshKernel = kernel;
        mRefreshKernel.requestDrawBackgroundFor(this, 0x00000000);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            final View progressView = mProgressView;
//            progressView.animate().cancel();
//        }

    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return delay;
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        super.onMoving(isDragging, percent, offset, height, maxDragHeight);
        if (percent < 1) {
            mTitleText.setText("下拉刷新");
        }
        if (isDragging && mProgressDrawable != null) {
            mProgressDrawable.setLevel((int) (10000 * percent));
            Log.i("LyfMineRefreshHeader", "percent==" + percent + ",maxDragHeight==" + maxDragHeight);
        }
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        super.onReleased(refreshLayout, height, maxDragHeight);
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        //progressbar开始旋转动画

    }


    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        super.onStateChanged(refreshLayout, oldState, newState);
        Log.i("LyfMineRefreshHeader", "oldState==" + oldState + ",newState==" + newState);
        if (oldState == None && newState == PullDownToRefresh) {
            hideProgressView(false);
            mTitleText.setText("下拉刷新");
        }
        if (oldState == PullDownToRefresh && newState == ReleaseToRefresh) {
            mTitleText.setText("松开刷新");
        }
        if (oldState == ReleaseToRefresh && newState == RefreshReleased) {
            mTitleText.setText("刷新中...");
            startRotate();
        }
        if (oldState == Refreshing && newState == RefreshFinish) {
            mTitleText.setText("刷新完成");

            if (model == MODEL_TWO) {
                mRefreshResult.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.lib_refresh_scal_show));
                hideProgressView(true);
                stopRotate();
                mRefreshResult.setVisibility(VISIBLE);
                mRefreshResult.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshResult.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.lib_refresh_scal_hide));
                        mRefreshResult.setVisibility(INVISIBLE);
                    }
                }, 1000);
            }

        }
        if (oldState == RefreshFinish && newState == None) {
            stopRotate();
        }

    }


    private void startRotate() {
        if (mProgressDrawable != null && mProgressView != null) {
            int start = mProgressDrawable.getLevel() / 10000 * 360;
            mRotateAnimation = new RotateAnimation(start, start + 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mRotateAnimation.setInterpolator(new LinearInterpolator());
            mRotateAnimation.setRepeatCount(Animation.INFINITE);
            mRotateAnimation.setRepeatMode(Animation.RESTART);
            mRotateAnimation.setDuration(1500);
            mRotateAnimation.setFillAfter(true);
            mProgressView.startAnimation(mRotateAnimation);
        }
    }

    private void stopRotate() {
        if (mProgressView != null) {
            mProgressView.clearAnimation();
            mRotateAnimation = null;
        }
    }

    /**
     * 刷新样式模式
     */
    public LyfMineRefreshHeader setModel(int model) {
        this.model = model;
        switch (model) {
            case MODEL_ONE:
                hideTextView(false);
                hideToastResultView(true);
                delay = 1500; //默认延迟1500毫秒
                break;
            case MODEL_TWO:
                hideTextView(true);
                hideToastResultView(true);
                delay = 1500; //默认延迟1500毫秒
                break;
        }
        return this;
    }

    /**
     * 隐藏titletext
     */
    private void hideTextView(boolean isHide) {
        if (mTitleText != null) {
            mTitleText.setVisibility(isHide ? GONE : VISIBLE);
        }

    }

    /**
     * 隐藏Toast
     */
    private void hideToastResultView(boolean isHide) {
        if (mRefreshResult != null) {
            mRefreshResult.setVisibility(isHide ? INVISIBLE : VISIBLE);
        }

    }

    /**
     * 隐藏progressView
     */
    private void hideProgressView(boolean isHide) {
        if (mProgressView != null) {
            mProgressView.setVisibility(isHide ? INVISIBLE : VISIBLE);

        }

    }

    /**
     * 设置progress的背景drawable
     */
    public LyfMineRefreshHeader setProgressDrawable(int drawable) {

        if (mProgressView != null) {
            mProgressView.setImageResource(drawable);
            mProgressDrawable = (RotateDrawable) mProgressView.getDrawable();
        }
        return this;
    }


    /**
     * @param result toast展示内容
     * @param delay  toast显示时间 毫秒
     *               如果需要在请求返回后指定toast内容，则代码需要在SmartRefreshLayout.finish之前执行
     *               如果不需要在请求返回后指定toast内容，怎在初始化后指定即可。
     */
    public void setRefreshResult(String result, int delay) {

        if (TextUtils.isEmpty(result) || mRefreshResult == null)
            return;
        if (delay < 1000) {
            delay = 1000;  //保障动画弹出时，刷新控件还没有开始回弹
        }
        this.delay = delay + 500;
        mRefreshResult.setText(result);

    }

    public void setmRefreshResult(String result) {
        setRefreshResult(result, 1000);
    }


}
