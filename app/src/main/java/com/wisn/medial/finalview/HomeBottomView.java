package com.wisn.medial.finalview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wisn.medial.GlideApp;
import com.wisn.medial.R;
import com.wisn.medial.finalview.helper.HomeDisplayHelper;
import com.wisn.medial.finalview.layout.HomePagerAdapter;
import com.wisn.medial.finalview.layout.HomeViewPager;
import com.wisn.medial.finalview.nestedScroll.HomeContinuousNestedBottomDelegateLayout;
import com.wisn.medial.finalview.nestedScroll.HomeContinuousNestedBottomRecyclerView;
import com.wisn.medial.finalview.nestedScroll.HomeContinuousNestedBottomView;
import com.wisn.medial.src.Constants;
import com.wisn.medial.tianmao.VideoViewHoderM;

/**
 * Created by Wisn on 2019-05-13 11:08.
 */
public class HomeBottomView extends HomeContinuousNestedBottomDelegateLayout {
    private static String[] tabTxt = {"推荐", "坚果", "肉类", "果铺", "糕点", "饼干"};
    VideoCheck2 check;

    private static String TAG = "QDContinuousBottomView";

    private MyViewPager mViewPager;
    private HomeContinuousNestedBottomRecyclerView mCurrentItemView;
    private int mCurrentPosition = -1;
    private HomeContinuousNestedBottomView.OnScrollNotifier mOnScrollNotifier;
    private TabLayout tabLayout;
    private HomePagerAdapter adapter;

    public HomeBottomView(Context context) {
        super(context);
        check = new VideoCheck2();

    }

    public HomeBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @NonNull
    @Override
    protected View onCreateHeaderView() {
        tabLayout = new TabLayout(getContext());
        for (int i = 0; i < tabTxt.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabTxt[i]));
        }
        return tabLayout;
    }

    @Override
    protected int getHeaderHeightLayoutParam() {
        return HomeDisplayHelper.dp2px(getContext(), 50);
    }

    @Override
    protected int getHeaderStickyHeight() {
        return HomeDisplayHelper.dp2px(getContext(), 50);
    }


    @NonNull
    @Override
    protected View onCreateContentView() {
        mViewPager = new MyViewPager(getContext());
        adapter = new HomePagerAdapter() {

            @Override
            protected Object hydrate(ViewGroup container, int position) {
                HomeContinuousNestedBottomRecyclerView recyclerView = new HomeContinuousNestedBottomRecyclerView(getContext());
                GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getContext(), 2) {
                    @Override
                    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                };
                recyclerView.setLayoutManager(staggeredGridLayoutManager);

                recyclerView.setAdapter(new RecyclerView.Adapter() {
                    public static final int video = 1;
                    public static final int product = 0;
                    int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;

                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        if (i == product) {
                            LinearLayout linearLayout = new LinearLayout(getContext());
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (screenWidth / 2)));
                            TextView textView = new TextView(getContext());
                            ImageView imageView = new ImageView(getContext());
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            linearLayout.addView(imageView);
                            linearLayout.addView(textView);
                            return new ViewHoderM(linearLayout);
                        } else {
                            FrameLayout linearLayout = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_exo_player, viewGroup, false);
                            return new VideoViewHoderM(linearLayout);
                        }
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
                        int i = getItemViewType(position);
                        if (i == product) {
                            LinearLayout linearLayout = (LinearLayout) viewHolder.itemView;
                            ImageView imageView = (ImageView) linearLayout.getChildAt(0);
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 2, screenWidth / 2));
                            TextView tv = (TextView) linearLayout.getChildAt(1);
                            tv.setText("position:" + position);
                            GlideApp.with(getContext()).load(Constants.res[position]).into(imageView);
                        } else {
                            VideoViewHoderM videoViewHoderM = (VideoViewHoderM) viewHolder;
                            videoViewHoderM.preview();
                            Log.d(TAG, "preview position:" + position + " " + videoViewHoderM);
                        }

                    }

                    @Override
                    public int getItemViewType(int position) {
                        if (position == 3 || position == 8 || position == 14 || position == 20) {
                            return video;
                        } else {
                            return product;
                        }
                    }

                    @Override
                    public int getItemCount() {
                        return Constants.res.length;
                    }
                });

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                    //用来标记是否正在向最后一个滑动
                    boolean isSlidingToLast = false;

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        check.checkVideo(recyclerView, newState);
                       /* if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            // 当不滚动时
                            Glide.with(getActivity()).resumeRequests();
                        } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                            // Log.i("SCROLL_STATE_DRAGGING", "手滑动：SCROLL_STATE_DRAGGING");
                            Glide.with(getActivity()).pauseRequests();
                        } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                            // Log.i("SCROLL_STATE_SETTLING", "松开惯性滑动：SCROLL_STATE_SETTLING");
                            Glide.with(getActivity()).resumeRequests();
                        }*/
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        check.onScrolled(recyclerView, dx, dy);
                        //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                        if (dy > 0) {
                            //大于0表示正在向右滚动
                            isSlidingToLast = true;
                        } else {
                            //小于等于0表示停止或向左滚动
                            isSlidingToLast = false;
                        }
                    }
                });
                return recyclerView;
            }

            @Override
            protected void populate(ViewGroup container, Object item, int position) {
                container.addView((View) item);
            }

            @Override
            protected void destroy(ViewGroup container, int position, Object object) {
                container.removeView((View) object);

            }

            @Override
            public int getCount() {
                return tabTxt.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @Override
            public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                super.setPrimaryItem(container, position, object);
                mCurrentItemView = (HomeContinuousNestedBottomRecyclerView) object;
                mCurrentPosition = position;
                if (mOnScrollNotifier != null) {
                    mCurrentItemView.injectScrollNotifier(mOnScrollNotifier);
                }
                check.initCheck(mCurrentItemView);
            }
        };
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                tabLayout.setScrollPosition(i, 0, true);
                check.onPageSelected(mCurrentItemView);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        return mViewPager;
    }

    class MyViewPager extends HomeViewPager implements HomeContinuousNestedBottomView {

        public MyViewPager(Context context) {
            super(context);
        }

        @Override
        public void consumeScroll(int dyUnconsumed) {
            if (mCurrentItemView != null) {
                mCurrentItemView.consumeScroll(dyUnconsumed);
            }

        }

        @Override
        public void smoothScrollYBy(int dy, int duration) {
            if (mCurrentItemView != null) {
                mCurrentItemView.smoothScrollYBy(dy, duration);
            }
        }

        @Override
        public void stopScroll() {
            if (mCurrentItemView != null) {
                mCurrentItemView.stopScroll();
            }
        }

        @Override
        public int getContentHeight() {
            if (mCurrentItemView != null) {
                return mCurrentItemView.getContentHeight();
            }
            return 0;
        }

        @Override
        public int getCurrentScroll() {
            if (mCurrentItemView != null) {
                return mCurrentItemView.getCurrentScroll();
            }
            return 0;
        }

        @Override
        public int getScrollOffsetRange() {
            if (mCurrentItemView != null) {
                return mCurrentItemView.getScrollOffsetRange();
            }
            return getHeight();
        }

        @Override
        public void injectScrollNotifier(OnScrollNotifier notifier) {
            mOnScrollNotifier = notifier;
            if (mCurrentItemView != null) {
                mCurrentItemView.injectScrollNotifier(notifier);
            }
        }

        @Override
        public Object saveScrollInfo() {
            if (mCurrentItemView != null) {
                return new ScrollInfo(mCurrentPosition, mCurrentItemView.saveScrollInfo());
            }
            return null;
        }

        @Override
        public void restoreScrollInfo(Object scrollInfo) {
            if (mCurrentItemView != null && (scrollInfo instanceof ScrollInfo)) {
                ScrollInfo si = (ScrollInfo) scrollInfo;
                if (si.currentPosition == mCurrentPosition) {
                    mCurrentItemView.restoreScrollInfo(si.currentScrollInfo);
                }
            }
        }

        class ScrollInfo {
            int currentPosition;
            Object currentScrollInfo;

            public ScrollInfo(int currentPosition, Object currentScrollInfo) {
                this.currentPosition = currentPosition;
                this.currentScrollInfo = currentScrollInfo;
            }
        }
    }

    static class ViewHoderM extends RecyclerView.ViewHolder {
        public ViewHoderM(@NonNull View itemView) {
            super(itemView);
        }
    }

}
