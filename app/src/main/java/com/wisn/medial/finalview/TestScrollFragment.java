package com.wisn.medial.finalview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wisn.medial.GlideApp;
import com.wisn.medial.R;
import com.wisn.medial.src.Constants;
import com.wisn.medial.tianmao.VideoViewHoderM;
import com.wisn.medial.tianmao.banner2.Banner;
import com.wisn.medial.tianmao.banner2.BannerConfig;
import com.wisn.medial.tianmao.banner2.CustomData;
import com.wisn.medial.tianmao.banner2.CustomViewHolder2;
import com.wisn.medial.tianmao.banner2.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Wisn on 2019-05-11 14:20.
 */
public class TestScrollFragment extends Fragment {

    private RecyclerView recycler_view, recycler_view2;
    private static String TAG = "TestScrollFragment";
    private PagerSnapHelper mSnapHelper;
    private LinearLayoutManager mPagerLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View view = inflater.inflate(R.layout.fragment_testscroll, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recycler_view = view.findViewById(R.id.recycler_view);

        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        List<CustomData> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
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
                   /*         int color = (int) mMArgbEvaluator.evaluate(positionOffset, colorBg[position % colorBg.length],
                                    colorBg[(position + 1) % colorBg.length]);
                            Log.d("onPageScrolled", "position:" + position + " positionOffset:" + positionOffset + " color：" + color);

                            mark.setColor(color);
                            marktop.setBackgroundColor(color);*/

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
        recycler_view2 = view.findViewById(R.id.recycler_view2);
        mPagerLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view2.setLayoutManager(mPagerLayoutManager);
        recycler_view2.setAdapter(new RecyclerView.Adapter() {
            public static final int video = 1;
            public static final int product = 0;
            int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
            int heightPixels = getContext().getResources().getDisplayMetrics().heightPixels;

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                RecyclerView recyclerView = new RecyclerView(getContext());
//                recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPixels));
                GridLayoutManager layout = new GridLayoutManager(getContext(), 2);
                recyclerView.setLayoutManager(layout);
                return new ViewHoderM(recyclerView);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position111) {
                RecyclerView v=  ((RecyclerView) viewHolder.itemView);
               v.setAdapter(new RecyclerView.Adapter() {

                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        if (i == product) {
                            LinearLayout linearLayout = new LinearLayout(getContext());
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            TextView textView = new TextView(getContext());
                            ImageView imageView = new ImageView(getContext());
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            linearLayout.addView(imageView);
                            linearLayout.addView(textView);
                            return new ViewHoderM(linearLayout);
                        } else {
                            FrameLayout linearLayout = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_exo_player, null);
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
                        if (position111 == 1) {
                            return Constants.res.length / 2;
                        } else if (position111 == 2) {
                            return Constants.res.length / 3;
                        } else if (position111 == 3) {
                            return Constants.res.length / 4;
                        }
                        return Constants.res.length;
                    }
                });
                v.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        GridLayoutManager layout = (GridLayoutManager) v.getLayoutManager();
                        int count=  recyclerView.getChildCount();
                        int firstVisibleItemPosition = layout.findFirstVisibleItemPosition();
                        int lastVisibleItemPosition = layout.findLastVisibleItemPosition();
                        Log.d(TAG, "countcount:" + count + " firstVisibleItemPosition" + firstVisibleItemPosition+"lastVisibleItemPosition: "+lastVisibleItemPosition);

                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }
                });
            }
            @Override
            public int getItemCount() {
                return Constants.res.length;
            }
        });

        mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(recycler_view2);
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
