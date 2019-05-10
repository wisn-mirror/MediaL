package com.wisn.medial.tianmao;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.exoplayer2.ui.PlayerView;
import com.wisn.medial.GlideApp;
import com.wisn.medial.R;
import com.wisn.medial.src.Constants;

/**
 * Created by Wisn on 2019-05-10 11:12.
 */
public class VideoViewHoderM extends RecyclerView.ViewHolder {
    private String TAG = "VideoViewHoderM";

    public final CardView cardview;
    private final int screenWidth;
    public final PlayerView playerView;
    public final ImageView preview;

    public VideoViewHoderM(@NonNull View itemView) {
        super(itemView);
        screenWidth = itemView.getContext().getResources().getDisplayMetrics().widthPixels;
        cardview = itemView.findViewById(R.id.cardview);
        playerView = itemView.findViewById(R.id.playerView);
        preview = itemView.findViewById(R.id.preview);
        cardview.setLayoutParams(new FrameLayout.LayoutParams((int) (screenWidth / 2.4), screenWidth / 2));
    }

    public void preview() {
        preview.setVisibility(View.VISIBLE);
        playerView.setVisibility(View.GONE);
        GlideApp.with(preview.getContext()).load(Constants.res[0]).into(preview);
    }


}
