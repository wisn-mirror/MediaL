package com.wisn.medial.tianmao;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * Created by Wisn on 2019-05-05 13:53.
 */
public class StickActivity extends AppCompatActivity {

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
    private HashMap<Integer,Integer>  hashMap=new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickview);
        holderTabLayout = findViewById(R.id.tablayout_holder);
        realTabLayout = findViewById(R.id.tablayout_real);
        scrollView = findViewById(R.id.scrollView);
        recycler_view = findViewById(R.id.recycler_view);
        viewpage = findViewById(R.id.viewpage);
        for (int i = 0; i < tabTxt.length; i++) {
            holderTabLayout.addTab(holderTabLayout.newTab().setText(tabTxt[i]));
            realTabLayout.addTab(realTabLayout.newTab().setText(tabTxt[i]));
        }


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
                RecyclerView recyclerView = new RecyclerView(StickActivity.this);
                recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                GridLayoutManager layout = new GridLayoutManager(StickActivity.this, 2);
                recyclerView.setLayoutManager(layout);
                recyclerView.setAdapter(new RecyclerView.Adapter() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        LinearLayout linearLayout = new LinearLayout(StickActivity.this);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        TextView textView = new TextView(StickActivity.this);
                        ImageView imageView = new ImageView(StickActivity.this);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        linearLayout.addView(imageView);
                        linearLayout.addView(textView);
                        return new ViewHoderM(linearLayout);
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
                        LinearLayout linearLayout = (LinearLayout) viewHolder.itemView;
                        ImageView imageView = (ImageView) linearLayout.getChildAt(0);
                        int screenWidth = StickActivity.this.getResources().getDisplayMetrics().widthPixels;
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 2, screenWidth / 2));
                        TextView tv = (TextView) linearLayout.getChildAt(1);
                        tv.setText(tabTxt[positionTab]);
                        GlideApp.with(StickActivity.this).load(Constants.res[position]).into(imageView);
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
        recycler_view.setLayoutManager(new LinearLayoutManager(StickActivity.this));
        recycler_view.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                if(i==1){
                    RecyclerView recyclerView = new RecyclerView(StickActivity.this);
                    int screenWidth = StickActivity.this.getResources().getDisplayMetrics().widthPixels;
                    recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    LinearLayoutManager llm = new LinearLayoutManager(StickActivity.this);
                    llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(llm);
                    recyclerView.setAdapter(new RecyclerView.Adapter() {
                        @NonNull
                        @Override
                        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                            LinearLayout linearLayout=new LinearLayout(StickActivity.this);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            TextView textView=new TextView(StickActivity.this);
                            ImageView imageView = new ImageView(StickActivity.this);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            linearLayout.addView(imageView);
                            linearLayout.addView(textView);
                            return new ViewHoderM(linearLayout);
                        }

                        @Override
                        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
                            LinearLayout linearLayout = (LinearLayout) viewHolder.itemView;
                            ImageView imageView = (ImageView) linearLayout.getChildAt(0);
                            int screenWidth = StickActivity.this.getResources().getDisplayMetrics().widthPixels;
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 3, screenWidth / 3));
                            TextView tv = (TextView) linearLayout.getChildAt(1);
                            tv.setText("横向的⌚️滑动");
                            GlideApp.with(StickActivity.this).load(Constants.res[position]).into(imageView);
                        }

                        @Override
                        public int getItemCount() {
                            return Constants.res.length;
                        }
                    });
                    return new ViewHoderM(recyclerView);
                }else{
                    ScrollView scrollView=new ScrollView(StickActivity.this);
                    TextView textView = new TextView(StickActivity.this);
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
