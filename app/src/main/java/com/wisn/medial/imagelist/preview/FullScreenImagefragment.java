package com.wisn.medial.imagelist.preview;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wisn.medial.GlideApp;
import com.wisn.medial.R;
import com.wisn.medial.src.Constants;

import java.util.WeakHashMap;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Wisn on 2019-04-25 10:36.
 */
public class FullScreenImagefragment extends Fragment {


    public static final String EXTRA_DEFAULT_INDEX = "index";
    public static final String EXTRA_EXIT_INDEX = "exit_index";

    public static final String lastItemPosition = "lastItemPosition";
    public static final String firstItemPosition = "firstItemPosition";

    private WeakHashMap<String, View> mTransitionNameToView = new WeakHashMap<>();
    private int defaultIndex;
    private ViewPager vp_target;
    private PagerAdapter adapter;

    private int mExitPosition;

    private int mEnterPosition;
    private GridLayoutManager layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View mContextView = inflater.inflate(R.layout.activity_fullscreenimage, null);
        initView(mContextView);
        return mContextView;
    }

    private void initView(View view) {
        defaultIndex = getActivity().getIntent().getIntExtra(EXTRA_DEFAULT_INDEX, 0);
        int lastPosition = getActivity().getIntent().getIntExtra(lastItemPosition, 0);
        int firstPosition = getActivity().getIntent().getIntExtra(firstItemPosition, 0);
        getActivity().supportPostponeEnterTransition();
        vp_target = view.findViewById(R.id.vp_target);
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
                ImageView imageView = new ImageView(getContext());
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
                            getActivity().finish();
                        }
                    }
                });
                GlideApp.with(FullScreenImagefragment.this).load(Constants.res[position])
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
                                            getActivity().supportStartPostponedEnterTransition();
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
    public void onPause() {
        getActivity().overridePendingTransition(0, 0);
        super.onPause();
    }

    public void finishAfterTransition() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_EXIT_INDEX, defaultIndex);
        getActivity().setResult(RESULT_OK, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setEnterSharedElementCallback(new SharedElementCallback() {
//                @Override
//                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
//                    names.clear();
//                    sharedElements.clear();
//                    names.add(String.valueOf(defaultIndex));
//                    View view = mTransitionNameToView.get(String.valueOf(defaultIndex));
//////                    if (view != null) {
//////                        sharedElements.put(String.valueOf(defaultIndex), view);
//////                    }
//                    sharedElements.put(String.valueOf(defaultIndex), view);
//
//                }
//            });
        }
//        getActivity().super.finishAfterTransition();
    }
}
