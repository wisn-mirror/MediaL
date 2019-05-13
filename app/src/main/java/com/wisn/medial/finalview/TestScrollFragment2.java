package com.wisn.medial.finalview;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.wisn.medial.GlideApp;
import com.wisn.medial.R;
import com.wisn.medial.finalview.nestedScroll.HomeContinuousNestedBottomAreaBehavior;
import com.wisn.medial.finalview.nestedScroll.HomeContinuousNestedScrollLayout;
import com.wisn.medial.finalview.nestedScroll.HomeContinuousNestedTopAreaBehavior;
import com.wisn.medial.finalview.nestedScroll.HomeContinuousNestedTopDelegateLayout;
import com.wisn.medial.finalview.nestedScroll.HomeContinuousNestedTopRecyclerView;
import com.wisn.medial.src.Constants;
import com.wisn.medial.tianmao.MView;
import com.wisn.medial.tianmao.banner2.Banner;
import com.wisn.medial.tianmao.banner2.BannerConfig;
import com.wisn.medial.tianmao.banner2.CustomData;
import com.wisn.medial.tianmao.banner2.CustomViewHolder2;
import com.wisn.medial.tianmao.banner2.Transformer;
import com.wisn.medial.tianmao.mClassicsHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Wisn on 2019-05-11 14:20.
 */
public class TestScrollFragment2 extends Fragment {
    HomeContinuousNestedScrollLayout mCoordinatorLayout;
    private HomeContinuousNestedTopRecyclerView mTopRecyclerView;
    private static String TAG = "TestScrollFragment";
    private HomeContinuousNestedTopDelegateLayout mTopDelegateLayout;
    private SmartRefreshLayout swipeRefreshLayout;
    private mClassicsHeader header;
    private MView mark;
    private View marktop;
    private View mToobarSmall;
    private View mToolbarSearch;
    private ArgbEvaluator mMArgbEvaluator;
    private int colorBg[] = new int[4];
    private Banner banner1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View view = inflater.inflate(R.layout.fragment_testscroll1, null);
        mMArgbEvaluator = new ArgbEvaluator();
        colorBg[0] = getResources().getColor(R.color.colorPrimary);
        colorBg[1] = getResources().getColor(R.color.colorPrimaryDark);
        colorBg[2] = getResources().getColor(R.color.colorAccent);
        colorBg[3] = getResources().getColor(R.color.mainColor);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mark = view.findViewById(R.id.mark);
        marktop = view.findViewById(R.id.marktop);
        header = view.findViewById(R.id.header);
        mToobarSmall = view.findViewById(R.id.toolbar_small);
        mToolbarSearch = view.findViewById(R.id.toolbar_search);
        header.setBackgroundColor(getResources().getColor(R.color.trans));
        header.setmView(mark);
        mark.setConfig(DensityUtil.dp2px(260), DensityUtil.dp2px(20));
        swipeRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.finishRefresh(false);
                    }
                }, 2000);
            }
        });

        mCoordinatorLayout = view.findViewById(R.id.coordinator);
        mTopDelegateLayout = new HomeContinuousNestedTopDelegateLayout(getContext());
//        mTopDelegateLayout.setBackgroundColor(Color.LTGRAY);
        View head = LayoutInflater.from(getContext()).inflate(R.layout.home_include_title_big, null);
        mTopDelegateLayout.setHeaderView(head);

        mTopRecyclerView = new HomeContinuousNestedTopRecyclerView(getContext());
        mTopRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
        dealTopRecycleView(mTopRecyclerView);
        mTopDelegateLayout.setDelegateView(mTopRecyclerView);
        int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
        CoordinatorLayout.LayoutParams topLp = new CoordinatorLayout.LayoutParams(
                matchParent, matchParent);
        topLp.setBehavior(new HomeContinuousNestedTopAreaBehavior(getContext()));
        mCoordinatorLayout.setTopAreaView(mTopDelegateLayout, topLp);
        HomeBottomView mBottomView = new HomeBottomView(getContext());
        CoordinatorLayout.LayoutParams recyclerViewLp = new CoordinatorLayout.LayoutParams(
                matchParent, matchParent);
        recyclerViewLp.setBehavior(new HomeContinuousNestedBottomAreaBehavior());
        mCoordinatorLayout.setBottomAreaView(mBottomView, recyclerViewLp);
        float i1 = dip2px(103);

        mCoordinatorLayout.addOnScrollListener(new HomeContinuousNestedScrollLayout.OnScrollListener() {
            @Override
            public void onScroll(int topCurrent, int topRange, int offsetCurrent,
                                 int offsetRange, int bottomCurrent, int bottomRange) {
                Log.i(TAG, String.format("topCurrent = %d; topRange = %d; " +
                                "offsetCurrent = %d; offsetRange = %d; " +
                                "bottomCurrent = %d, bottomRange = %d",
                        topCurrent, topRange, offsetCurrent, offsetRange, bottomCurrent, bottomRange));
              /*  int  scrollY=  scrollView.getScrollY();
                float sc = (float) scrollY / i1;*/

                float sc = (float) topCurrent / i1;
                int alpha = (int) sc * 255;

//                Log.e(TAG, "onScrollChange: " + i1 + "scrollY:" + scrollY + "----" + sc + "alpha =" + alpha + " oldScrollY:" + oldScrollY + "  v.getScaleY:" + v.getScaleY());
                if (sc > 0.2 && sc < 0.8) {
                    mToolbarSearch.setVisibility(View.VISIBLE);
                    mToobarSmall.setVisibility(View.VISIBLE);
                }
                if (sc <= 0.5) {
                    mToolbarSearch.setVisibility(View.VISIBLE);
                    mToobarSmall.setVisibility(View.GONE);
                }
                if (sc > 0.5) {
                    mToolbarSearch.setVisibility(View.GONE);
                    mToobarSmall.setVisibility(View.VISIBLE);
                }
                mToolbarSearch.setAlpha(1f - sc);
                mToobarSmall.setAlpha(sc + 0.2f);


                mark.setTranslationY(-topCurrent);
                if(topCurrent>=1){
                    swipeRefreshLayout.setEnableRefresh(false);
                    banner1.setAutoPlay(false);
                }else{
                    swipeRefreshLayout.setEnableRefresh(true);
                    banner1.setAutoPlay(true);
                }
            }

            @Override
            public void onScrollStateChange(int newScrollState, boolean fromTopBehavior) {

            }
        });
    }

    private void dealTopRecycleView(HomeContinuousNestedTopRecyclerView mTopRecyclerView) {
        List<CustomData> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(new CustomData(Constants.res[i], String.valueOf(i), true));
        }
        mTopRecyclerView.setAdapter(new RecyclerView.Adapter() {
            private int lastPosition = 0;
            private List<ImageView> indicatorImages = new ArrayList<>();

            private int mIndicatorSelectedResId = R.drawable.indicator;
            private int mIndicatorUnselectedResId = R.drawable.indicator2;

            private void initIndicator(LinearLayout indicator) {
                for (int i = 0; i < list.size(); i++) {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    LinearLayout.LayoutParams custom_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    custom_params.leftMargin = 2;
                    custom_params.rightMargin = 2;
                    if (i == 0) {
                        imageView.setImageResource(mIndicatorSelectedResId);
                    } else {
                        imageView.setImageResource(mIndicatorUnselectedResId);
                    }
                    indicatorImages.add(imageView);
                    indicator.addView(imageView, custom_params);
                }
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                if (i == 2) {
                    LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_recycle, null);
                    banner1 = linearLayout.findViewById(R.id.banner1);
                    LinearLayout indicator = linearLayout.findViewById(R.id.indicator);
                    banner1.setAutoPlay(true)
                            .setDelayTime(2700)
                            .setPages(list, new CustomViewHolder2())
                            .setBannerStyle(BannerConfig.NOT_INDICATOR)
                            .setBannerAnimation(Transformer.Scale)
                            .start();
                    initIndicator(indicator);
                    banner1.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                             int color = (int) mMArgbEvaluator.evaluate(positionOffset, colorBg[position % colorBg.length],
                                    colorBg[(position + 1) % colorBg.length]);
                            Log.d("onPageScrolled", "position:" + position + " positionOffset:" + positionOffset + " color：" + color);

                            mark.setColor(color);
                            marktop.setBackgroundColor(color);

                        }

                        @Override
                        public void onPageSelected(int position) {
                            indicatorImages.get((lastPosition + list.size()) % list.size()).setImageResource(mIndicatorUnselectedResId);
                            indicatorImages.get((position + list.size()) % list.size()).setImageResource(mIndicatorSelectedResId);
                            lastPosition = position;
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    return new ViewHoderM(linearLayout);
                } else if (i == 1) {
                    RecyclerView recyclerView = new RecyclerView(getContext());
                    recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(llm);
                    recyclerView.setAdapter(new RecyclerView.Adapter() {
                        @NonNull
                        @Override
                        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                            LinearLayout linearLayout = new LinearLayout(getContext());
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            TextView textView = new TextView(getContext());
                            ImageView imageView = new ImageView(getContext());
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            linearLayout.addView(imageView);
                            linearLayout.addView(textView);
                            return new ViewHoderM(linearLayout);
                        }

                        @Override
                        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
                            LinearLayout linearLayout = (LinearLayout) viewHolder.itemView;
                            ImageView imageView = (ImageView) linearLayout.getChildAt(0);
                            int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 3, screenWidth / 3));
                            TextView tv = (TextView) linearLayout.getChildAt(1);
                            tv.setText("横向的⌚️滑动");
                            GlideApp.with(getContext()).load(Constants.res[position]).into(imageView);
                        }

                        @Override
                        public int getItemCount() {
                            return Constants.res.length;
                        }
                    });
                    return new ViewHoderM(recyclerView);
                } else {
                    ScrollView scrollView = new ScrollView(getContext());
                    TextView textView = new TextView(getContext());
                    textView.setText("内容 各种条目实现：" + i);
                    textView.append("内容 各种条目实现：" + i);
                    textView.setTextSize(new Random().nextInt(40) + 10);
                    scrollView.addView(textView);
                    return new ViewHoderM(scrollView);
                }

            }

            @Override
            public int getItemViewType(int position) {
                if (position == 0) {
                    return 2;
                } else if (position % 2 == 1) {
                    return 1;
                } else {
                    return 0;
                }
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }

            @Override
            public int getItemCount() {
                return 10;
            }
        });
    }

    public static class ViewHoderM extends RecyclerView.ViewHolder {

        public ViewHoderM(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public float dip2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }
}
