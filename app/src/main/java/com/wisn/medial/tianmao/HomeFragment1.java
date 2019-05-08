package com.wisn.medial.tianmao;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.wisn.medial.src.Constants;
import com.wisn.medial.tianmao.banner2.Banner;
import com.wisn.medial.tianmao.banner2.BannerConfig;
import com.wisn.medial.tianmao.banner2.CustomData;
import com.wisn.medial.tianmao.banner2.CustomViewHolder2;
import com.wisn.medial.tianmao.banner2.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * Created by Wisn on 2019-05-06 11:07.
 */
public class HomeFragment1 extends Fragment {
    /**
     * 占位tablayout，用于滑动过程中去确定实际的tablayout的位置
     */
    private TabLayout holderTabLayout;
    /**
     * 实际操作的tablayout，
     */
    private TabLayout realTabLayout;
    private CustomScrollView scrollView;
    private String[] tabTxt = {"推荐", "坚果", "肉类", "果铺", "糕点", "饼干"};
    private boolean isScroll;
    //记录上一次位置，防止在同一内容块里滑动 重复定位到tablayout
    private int lastPos = 0;
    //监听判断最后一个模块的高度，不满一屏时让最后一个模块撑满屏幕
    private ViewTreeObserver.OnGlobalLayoutListener listener;
    private ViewPager viewpage;
    private RecyclerView recycler_view;
    private String TAG = "StickActivity";
    private PagerAdapter adapter;
    private View mToobarSmall;
    private View mToolbarSearch;
    private int mMaskColor;     //三个模块的背景主题色
    private HashMap<Integer, Integer> hashMap = new HashMap<>();
    private SmartRefreshLayout swipeRefreshLayout;
    private View botton_big;
    private MView mark;
    private ArgbEvaluator mMArgbEvaluator;
    private View marktop;
    private int colorBg[] = new int[4];
    private mClassicsHeader header;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        View view = inflater.inflate(R.layout.fragment_stickview1, null);
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
        holderTabLayout = view.findViewById(R.id.tablayout_holder);
        realTabLayout = view.findViewById(R.id.tablayout_real);
        scrollView = view.findViewById(R.id.scrollView);
        botton_big = view.findViewById(R.id.botton_big);
        recycler_view = view.findViewById(R.id.recycler_view);
        viewpage = view.findViewById(R.id.viewpage);

        header = view.findViewById(R.id.header);
        header.setBackgroundColor(getResources().getColor(R.color.trans));
        header.setmView(mark);
        mark.setConfig(DensityUtil.dp2px(260), DensityUtil.dp2px(20));
        mToobarSmall = view.findViewById(R.id.toolbar_small);
        mToolbarSearch = view.findViewById(R.id.toolbar_search);
        swipeRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout);
        mMaskColor = getResources().getColor(R.color.mainColor);
        mToolbarSearch.setVisibility(View.VISIBLE);
        mToobarSmall.setVisibility(View.GONE);
        float i1 = dip2px(103);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);

            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float sc = (float) scrollY / i1;
                mark.setTranslationY(-scrollY);
                int alpha = (int) sc * 255;

                Log.e(TAG, "onScrollChange: " + i1 + "scrollY:" + scrollY + "----" + sc+"alpha =" + alpha + " oldScrollY:" + oldScrollY + "  v.getScaleY:" + v.getScaleY());
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

                //监听滚动状态

                if (scrollY > oldScrollY) {//向下滚动
                    Log.i(TAG, "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {//向上滚动
                    Log.i(TAG, "Scroll UP");
                }

                if (scrollY == 0) {// 滚动到顶
                    Log.i(TAG, "TOP SCROLL");
                }
                // 滚动到底
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Log.i(TAG, "BOTTOM SCROLL");
                }

            }
        });
        for (int i = 0; i < tabTxt.length; i++) {
            holderTabLayout.addTab(holderTabLayout.newTab().setText(tabTxt[i]));
            realTabLayout.addTab(realTabLayout.newTab().setText(tabTxt[i]));
        }
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

        listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                realTabLayout.setVisibility(View.INVISIBLE);
                viewpage.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
            }
        };
        viewpage.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        viewpage.setFocusable(false);
        mMArgbEvaluator = new ArgbEvaluator();

        adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return tabTxt.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int positionTab) {
                RecyclerView recyclerView = new RecyclerView(getContext());
                recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                GridLayoutManager layout = new GridLayoutManager(getContext(), 2);
                recyclerView.setLayoutManager(layout);
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
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 2, screenWidth / 2));
                        TextView tv = (TextView) linearLayout.getChildAt(1);
                        tv.setText(tabTxt[positionTab]);
                        GlideApp.with(getContext()).load(Constants.res[position]).into(imageView);
                    }

                    @Override
                    public int getItemCount() {
                        return Constants.res.length;
                    }
                });
                container.addView(recyclerView);
                return recyclerView;
            }
        };
        viewpage.setAdapter(adapter);
        viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setScrollPos(i);
                Integer lastposition = hashMap.get(i);
//                if(lastposition==null||lastposition==0){
//                     scrollView.scrollTo(0,  viewpage.getTop()-realTabLayout.getHeight());
//                }else{
//                    scrollView.scrollTo(0,  lastposition);
//                }
//                viewpage.setTranslationY(0);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        List<CustomData> list = new ArrayList<>();
        for (int i = 0; i < colorBg.length; i++) {
            list.add(new CustomData(Constants.res[i], String.valueOf(i), true));
        }
        recycler_view.setAdapter(new RecyclerView.Adapter() {
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
                    Banner banner1 = linearLayout.findViewById(R.id.banner1);
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
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isScroll = true;
                }
                return false;
            }
        });

        //监听scrollview滑动
        scrollView.setCallbacks(new CustomScrollView.Callbacks() {
            @Override
            public void onScrollChanged(int x, int y, int oldx, int oldy) {
                //根据滑动的距离y(不断变化的) 和 holderTabLayout距离父布局顶部的距离(这个距离是固定的)对比，
                //当y < holderTabLayout.getTop()时，holderTabLayout 仍在屏幕内，realTabLayout不断移动holderTabLayout.getTop()距离，覆盖holderTabLayout
                //当y > holderTabLayout.getTop()时，holderTabLayout 移出，realTabLayout不断移动y，相对的停留在顶部，看上去是静止的
                int translation = Math.max(y, holderTabLayout.getTop());
                Log.d(TAG, "y:" + y + "  oldy：" + oldy);
//                Log.d(TAG, "translation:" + translation+ "  "+viewpage.getTop());
                realTabLayout.setTranslationY(translation);
                realTabLayout.setVisibility(View.VISIBLE);
//                hashMap.put(viewpage.getCurrentItem(),viewpage.getTop());
//                hashMap.put(viewpage.getCurrentItem(),viewpage.getTop());

            }
        });

        //实际的tablayout的点击切换
        realTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isScroll = false;
                int pos = tab.getPosition();
                viewpage.setCurrentItem(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private void setScrollPos(int newPos) {
        if (lastPos != newPos) {
            realTabLayout.setScrollPosition(newPos, 0, true);
            holderTabLayout.setScrollPosition(newPos, 0, true);
        }
        lastPos = newPos;
    }

    static class ViewHoderM extends RecyclerView.ViewHolder {
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
