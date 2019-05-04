package com.wisn.medial.imagelist.preview;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.ProgressBar;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wisn.medial.GlideApp;
import com.wisn.medial.R;
import com.wisn.medial.imagelist.FingerDragHelper;
import com.wisn.medial.imagelist.glide.ImageUtils;
import com.wisn.medial.imagelist.glide.LoadImageFile;
import com.wisn.medial.imagelist.photoview.PhotoView;
import com.wisn.medial.imagelist.subscale.ImageSource;
import com.wisn.medial.imagelist.subscale.SubsamplingScaleImageView;
import com.wisn.medial.src.Constants;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wisn on 2019-04-25 10:36.
 */
public class FullScreenImageActivity extends AppCompatActivity {
    public static final String EXTRA_DEFAULT_INDEX = "index";
    public static final String EXTRA_EXIT_INDEX = "exit_index";

    public static final String lastItemPosition = "lastItemPosition";
    public static final String firstItemPosition = "firstItemPosition";

    private HashMap<String, View> mTransitionNameToView = new HashMap<>();
    private ImageView iv_target;
    private int defaultIndex;
    private ViewPager vp_target;
    private PagerAdapter adapter;
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreenimage);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//下面这句表示在intent中拿到bitmap对应的数组
        byte[] res = getIntent().getByteArrayExtra("bitmap");
        bitmap = getPicFromBytes(res, null);
//        oneImage();
        vpImage();


    }

    //下面的这个方法是将byte数组转化为Bitmap对象的一个方法
    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
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
                View convertView = View.inflate(FullScreenImageActivity.this, R.layout.item_preview, null);
                final ProgressBar progressBar = convertView.findViewById(R.id.progress_view);
                final FingerDragHelper fingerDragHelper = convertView.findViewById(R.id.fingerDragHelper);
                final SubsamplingScaleImageView scaleView = convertView.findViewById(R.id.photo_view);
                final PhotoView imageGif = convertView.findViewById(R.id.gif_view);
//                scaleView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
                scaleView.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
                scaleView.setDoubleTapZoomDuration(200);
                scaleView.setMinScale(1f);
                scaleView.setMaxScale(5f);
                scaleView.setDoubleTapZoomScale(3f);
                scaleView.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
                imageGif.setZoomTransitionDuration(200);
                imageGif.setMinimumScale(0.7f);
                imageGif.setMaximumScale(5f);
                imageGif.setScaleType(ImageView.ScaleType.FIT_CENTER);
                progressBar.setVisibility(View.GONE);

//                Glide.with(FullScreenImageActivity.this).downloadOnly().
                container.addView(convertView);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    mTransitionNameToView.put(String.valueOf(position), imageGif);
//                    imageGif.setTransitionName(String.valueOf(position));
//                }
                File glideCacheFile = LoadImageFile.getGlideCacheFile(FullScreenImageActivity.this, Constants.res[position]);
                if (glideCacheFile != null && glideCacheFile.exists()) {
                    boolean gifImageWithMime = ImageUtils.isGifImageWithMime(glideCacheFile.getAbsolutePath());
                    if (gifImageWithMime) {
                        // gif
                        imageGif.setVisibility(View.VISIBLE);
                        scaleView.setVisibility(View.GONE);
                        mTransitionNameToView.put(String.valueOf(position), imageGif);
                        imageGif.setTransitionName(String.valueOf(position));
                        imageGif.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                defaultIndex = position;
                                if (position <= lastPosition && position >= firstPosition) {
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
                                            imageGif.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                                @Override
                                                public boolean onPreDraw() {
                                                    imageGif.getViewTreeObserver().removeOnPreDrawListener(this);
                                                    supportStartPostponedEnterTransition();
                                                    return false;
                                                }
                                            });
                                        }
                                        return false;
                                    }
                                }).into(imageGif);
                    } else {
                        imageGif.setVisibility(View.GONE);
                        scaleView.setVisibility(View.VISIBLE);
                        mTransitionNameToView.put(String.valueOf(position), scaleView);
                        scaleView.setTransitionName(String.valueOf(position));
                        scaleView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                defaultIndex = position;
//                                scaleView.setMinScale(1f);
//                                scaleView.setMaxScale(1f);
                                if (position <= lastPosition && position >= firstPosition) {
                                    finishAfterTransition();
                                } else {
                                    finish();
                                }
                            }
                        });
                        boolean longImage = ImageUtils.isLongImage(FullScreenImageActivity.this, glideCacheFile.getAbsolutePath());
                        if (longImage) {
                            //long
                            scaleView.setDoubleTapZoomDuration(200);
                            scaleView.setMinScale(0.2f);
//                            scaleView.setMinScale(ImageUtils.getLongImageMinScale(FullScreenImageActivity.this, glideCacheFile.getAbsolutePath()));
                            scaleView.setMaxScale(ImageUtils.getLongImageMaxScale(FullScreenImageActivity.this, glideCacheFile.getAbsolutePath()));
                            scaleView.setDoubleTapZoomScale(ImageUtils.getLongImageMaxScale(FullScreenImageActivity.this, glideCacheFile.getAbsolutePath()));
                            scaleView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_START);
                        } else {
                            boolean wideImage = ImageUtils.isWideImage(FullScreenImageActivity.this, glideCacheFile.getAbsolutePath());
                            boolean smallImage = ImageUtils.isSmallImage(FullScreenImageActivity.this, glideCacheFile.getAbsolutePath());
                            if (wideImage) {
                                scaleView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
                                scaleView.setMinScale(0.2f);
                                scaleView.setMaxScale(5f);
//                                scaleView.setDoubleTapZoomScale(ImageUtils.getWideImageDoubleScale(FullScreenImageActivity.this, glideCacheFile.getAbsolutePath()));

                            } else if (smallImage) {
                                scaleView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
//                                scaleView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
//                                scaleView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_START);

                                scaleView.setMinScale(ImageUtils.getSmallImageMinScale(FullScreenImageActivity.this, glideCacheFile.getAbsolutePath()));
                                scaleView.setMaxScale(ImageUtils.getSmallImageMaxScale(FullScreenImageActivity.this, glideCacheFile.getAbsolutePath()));
//                                scaleView.setDoubleTapZoomScale(ImageUtils.getSmallImageMaxScale(FullScreenImageActivity.this, glideCacheFile.getAbsolutePath()));
                            } else {
                                scaleView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
                                scaleView.setMinScale(1f);
                                scaleView.setMaxScale(5f);
                                scaleView.setDoubleTapZoomScale(3f);
                            }
                        }
//                        scaleView.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
                        ImageSource imageSource = ImageSource.uri(Uri.fromFile(glideCacheFile));
//                        if (ImageUtils.isBmpImageWithMime(glideCacheFile.getAbsolutePath())) {
//                            imageSource.tilingDisabled();
//                        }
                        scaleView.setImage(imageSource);
                        if (defaultIndex == position) {
                            scaleView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                @Override
                                public boolean onPreDraw() {
                                    scaleView.getViewTreeObserver().removeOnPreDrawListener(this);
                                    supportStartPostponedEnterTransition();
                                    return false;
                                }
                            });
                        }
                    }
                } else {
                    View put = mTransitionNameToView.put(String.valueOf(position), imageGif);
                    imageGif.setTransitionName(String.valueOf(position));
                    imageGif.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            defaultIndex = position;
                            if (position <= lastPosition && position >= firstPosition) {
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
                                        imageGif.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                            @Override
                                            public boolean onPreDraw() {
                                                imageGif.getViewTreeObserver().removeOnPreDrawListener(this);
                                                supportStartPostponedEnterTransition();
                                                return false;
                                            }
                                        });
                                    }
                                    return false;
                                }
                            }).into(imageGif);

                }


                return convertView;
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
        vp_target.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                defaultIndex = i;

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
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
                    Log.e("onBindViewHolder ", "111setEnterSharedElementCallback:names size：" + names.size() + " sharedElements:" + sharedElements.size());

                    names.clear();
                    sharedElements.clear();
                    names.add(String.valueOf(defaultIndex));
//
                    View view = mTransitionNameToView.get(String.valueOf(defaultIndex));
                    if (view != null) {
                        sharedElements.put(String.valueOf(defaultIndex), view);
                    }

                }
            });
        }
        super.finishAfterTransition();
    }
}
