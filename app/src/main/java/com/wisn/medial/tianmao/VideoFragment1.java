package com.wisn.medial.tianmao;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wisn.medial.GlideApp;
import com.wisn.medial.R;
import com.wisn.medial.src.Constants;

/**
 * Created by Wisn on 2019-05-09 10:07.
 */
public class VideoFragment1 extends Fragment {
    private static String TAG = "VideoFragment1";

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager layout;
    private int adapterPosition;
    private VideoCheck check;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View view = inflater.inflate(R.layout.fragment_video, null);
        check = new VideoCheck();
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layout);

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
                    Log.d(TAG, "preview position:" + position+" "+videoViewHoderM);
                }

            }

            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                super.onViewRecycled(holder);

                Log.d(TAG, "preview position onViewRecycled:" + holder.getAdapterPosition()+" "+holder+" "+(holder instanceof VideoViewHoderM));

            }
            @Override
            public int getItemViewType(int position) {
                if (position == 3 || position == 8||position == 14||position == 20) {
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
                if (getActivity() == null || getActivity().isFinishing()) {
                    return;
                }
                check.checkVideo(recyclerView,newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 当不滚动时
                    Glide.with(getActivity()).resumeRequests();
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    // Log.i("SCROLL_STATE_DRAGGING", "手滑动：SCROLL_STATE_DRAGGING");
                    Glide.with(getActivity()).pauseRequests();
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    // Log.i("SCROLL_STATE_SETTLING", "松开惯性滑动：SCROLL_STATE_SETTLING");
                    Glide.with(getActivity()).resumeRequests();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
               check.onScrolled(recyclerView,dx,dy);
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
    }

    static class ViewHoderM extends RecyclerView.ViewHolder {
        public ViewHoderM(@NonNull View itemView) {
            super(itemView);
        }
    }


}
