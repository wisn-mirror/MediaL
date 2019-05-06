package com.wisn.medial.tianmao;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wisn.medial.GlideApp;
import com.wisn.medial.R;
import com.wisn.medial.src.Constants;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Wisn on 2019-05-06 11:07.
 */
public class HomeFragment extends Fragment {


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
    private AppBarLayout mAppbar;
    private View mToobarSmall;
    private View mToolbarSearch;
    private View mVToolbarSearchMask;
    private View mVToolbarSmallMask;
    private View mVTitleBigMask;
    private int mMaskColor;     //三个模块的背景主题色



    private HashMap<Integer,Integer> hashMap=new HashMap<>();
    private SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_stickview, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        holderTabLayout = view.findViewById(R.id.tablayout_holder);
        realTabLayout =view. findViewById(R.id.tablayout_real);
        scrollView =view. findViewById(R.id.scrollView);
        recycler_view =view. findViewById(R.id.recycler_view);
        mAppbar = view.findViewById(R.id.appbar);
        viewpage = view.findViewById(R.id.viewpage);
        mVToolbarSmallMask = view.findViewById(R.id.v_toolbar_small_mask);

        mToobarSmall = view.findViewById(R.id.toolbar_small);
        mToolbarSearch = view.findViewById(R.id.toolbar_search);
        mVToolbarSearchMask = view.findViewById(R.id.v_toolbar_search_mask);
        mVTitleBigMask = view.findViewById(R.id.v_title_big_mask);
        swipeRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout);
        mMaskColor = getResources().getColor(R.color.mainColor);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new  ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                // 这只一定是 == ,不能是 <= ,scrollView 会自己调整
                swipeRefreshLayout.setEnabled(scrollView.getScrollY()==0);
            }
        });
        mAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.i("NewHomeFragment", "verticalOffset =" + verticalOffset);
//                mRefreshLayout.setEnableRefresh(verticalOffset >= 0);
                if (verticalOffset >= 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
                float progress = Math.abs(verticalOffset);
                float height = appBarLayout.getTotalScrollRange();//AppBarLayout总的距离px
                int alpha = (int) ((progress / height) * 255);
                Log.i("NewHomeFragment", "alpha =" + alpha);
                Log.i("NewHomeFragment", "height =" + height);
                int argb = Color.argb(alpha, Color.red(mMaskColor), Color.green(mMaskColor), Color.blue(mMaskColor));
                int argbDouble = Color.argb((alpha * 2) > 255 ? 255 : alpha * 2, Color.red(mMaskColor), Color.green(mMaskColor), Color.blue(mMaskColor));
                //appBarLayout上滑一半距离后小图标应该由渐变到全透明
                int title_small_argb = Color.argb(255 - alpha, Color.red(mMaskColor),
                        Color.green(mMaskColor), Color.blue(mMaskColor));
                if (progress <= height / 2) {
                    mToolbarSearch.setVisibility(View.VISIBLE);
                    mToobarSmall.setVisibility(View.INVISIBLE);
                    //为了和下面的大图标渐变区分,乘以2倍渐变
                    mVToolbarSearchMask.setBackgroundColor(argbDouble);
                } else {
                    mToolbarSearch.setVisibility(View.INVISIBLE);
                    mToobarSmall.setVisibility(View.VISIBLE);
                    //appBarLayout上滑1/2距离后小图标应该由渐变到全透明
                    mVToolbarSmallMask.setBackgroundColor(title_small_argb);
                }
                //上滑时遮罩由全透明到半透明
                mVTitleBigMask.setBackgroundColor(argb);
            }
        });
        for (int i = 0; i < tabTxt.length; i++) {
            holderTabLayout.addTab(holderTabLayout.newTab().setText(tabTxt[i]));
            realTabLayout.addTab(realTabLayout.newTab().setText(tabTxt[i]));
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
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
        recycler_view.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                if(i==1){
                    RecyclerView recyclerView = new RecyclerView(getContext());
                    int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
                    recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(llm);
                    recyclerView.setAdapter(new RecyclerView.Adapter() {
                        @NonNull
                        @Override
                        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                            LinearLayout linearLayout=new LinearLayout(getContext());
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            TextView textView=new TextView(getContext());
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
                }else{
                    ScrollView scrollView=new ScrollView(getContext());
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
                if(position%2==1){
                    return 1;
                }else{
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
                Log.d(TAG, "y:" + y+ "  oldy："+oldy);
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
//                int top = anchorList.get(pos).getTop();
                //同样这里滑动要加上顶部内容区域的高度(这里写死的高度)
//                scrollView.smoothScrollTo(0, top + 200 * 3);
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
}
