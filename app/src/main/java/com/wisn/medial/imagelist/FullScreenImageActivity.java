package com.wisn.medial.imagelist;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

    private WeakHashMap<String, View> mTransitionNameToView = new WeakHashMap<>();
    private ImageView iv_target;
    private int defaultIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreenimage);
        supportPostponeEnterTransition();
        iv_target = findViewById(R.id.iv_target);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        defaultIndex = getIntent().getIntExtra(EXTRA_DEFAULT_INDEX, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTransitionNameToView.put(String.valueOf(defaultIndex), iv_target);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
                                Log.e("onBindViewHolder","onPreDraw ");
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
