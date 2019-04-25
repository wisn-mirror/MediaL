package com.wisn.medial.imagelist;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wisn.medial.GlideApp;
import com.wisn.medial.R;
import com.wisn.medial.src.Constants;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Wisn on 2019-04-25 10:36.
 */
public class FullScreenImageActivity extends AppCompatActivity {
    public static final String EXTRA_DEFAULT_INDEX = "index";
    public static final String EXTRA_EXIT_INDEX = "exit_index";

    public static final String lastItemPosition = "lastItemPosition";
    public static final String firstItemPosition = "firstItemPosition";

    private WeakHashMap<String, View> mTransitionNameToView = new WeakHashMap<>();
    private ImageView iv_target;
    private int defaultIndex;
    private ViewPager vp_target;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreenimage);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

//        oneImage();
        vpImage();

    }

    private void vpImage() {
        defaultIndex = getIntent().getIntExtra(EXTRA_DEFAULT_INDEX, 0);
        int lastPosition = getIntent().getIntExtra(lastItemPosition, 0);
        int firstPosition = getIntent().getIntExtra(firstItemPosition, 0);
        supportPostponeEnterTransition();
        vp_target = findViewById(R.id.vp_target);
        adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return Constants.res.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }


            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                ImageView imageView = new ImageView(FullScreenImageActivity.this);
                container.addView(imageView);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mTransitionNameToView.put(String.valueOf(position), imageView);
                    imageView.setTransitionName(String.valueOf(position));
                }
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position <= lastPosition && position >= firstPosition) {
                            defaultIndex = position;
                            finishAfterTransition();
                        } else {
                            finish();
                        }
                    }
                });
                GlideApp.with(FullScreenImageActivity.this).load(Constants.res[position])
                        .onlyRetrieveFromCache(true)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (defaultIndex == position) {
                                    imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                        @Override
                                        public boolean onPreDraw() {
                                            imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                                            Log.e("onBindViewHolder", "onPreDraw ");
                                            supportStartPostponedEnterTransition();
                                            return false;
                                        }
                                    });
                                }
                                return false;
                            }
                        }).into(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//                super.destroyItem(container, position, object);
                container.removeView((View) object);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mTransitionNameToView.remove(String.valueOf(position));
                }
            }
        };
        vp_target.setAdapter(adapter);
        vp_target.setCurrentItem(defaultIndex);
    }


    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }


    private void oneImage() {
        defaultIndex = getIntent().getIntExtra(EXTRA_DEFAULT_INDEX, 0);

        iv_target = findViewById(R.id.iv_target);
        iv_target.setVisibility(View.VISIBLE);
        supportPostponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTransitionNameToView.put(String.valueOf(defaultIndex), iv_target);
            iv_target.setTransitionName(String.valueOf(defaultIndex));
        }
        GlideApp.with(FullScreenImageActivity.this).load(Constants.res[defaultIndex])
                .onlyRetrieveFromCache(true)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        iv_target.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                iv_target.getViewTreeObserver().removeOnPreDrawListener(this);
                                Log.e("onBindViewHolder", "onPreDraw ");
                                supportStartPostponedEnterTransition();
                                return false;
                            }
                        });
                        return false;
                    }
                }).into(iv_target);
        iv_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });
    }

    @Override
    public void finishAfterTransition() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_EXIT_INDEX, defaultIndex);
        setResult(RESULT_OK, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    names.clear();
                    sharedElements.clear();
                    names.add(String.valueOf(defaultIndex));
                    View view = mTransitionNameToView.get(String.valueOf(defaultIndex));
//                    if (view != null) {
//                        sharedElements.put(String.valueOf(defaultIndex), view);
//                    }
                    sharedElements.put(String.valueOf(defaultIndex), view);

                }
            });
        }
        super.finishAfterTransition();
    }
}
