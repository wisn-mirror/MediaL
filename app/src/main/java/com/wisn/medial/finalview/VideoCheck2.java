package com.wisn.medial.finalview;

import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.wisn.medial.src.Constants;
import com.wisn.medial.tianmao.VideoViewHoderM;

/**
 * Created by Wisn on 2019-05-09 18:23.
 */
public class VideoCheck2 {
    private String TAG = "VideoCheck";
    public ExoPlayer player;
    private int adapterPosition;
    private View currentPlayView;


    public void playPosition(VideoViewHoderM videoViewHoderM) {
        int index = 0;
        if (videoViewHoderM.getAdapterPosition() == 3) {
            index = 1;
        } else if (videoViewHoderM.getAdapterPosition() == 8) {
            index = 2;
        } else if (videoViewHoderM.getAdapterPosition() == 14) {
            index = 3;
        } else if (videoViewHoderM.getAdapterPosition() == 20) {
            index = 4;
        }
        if (player != null) player.release();
        player = ExoPlayerFactory.newSimpleInstance(videoViewHoderM.playerView.getContext());
        videoViewHoderM.playerView.setPlayer(player);
        currentPlayView = videoViewHoderM.playerView;
        player.setPlayWhenReady(true);
        if (player.getPlaybackState() == Player.STATE_IDLE) {
            ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory("wisn")).createMediaSource(Uri.parse(Constants.ip + Constants.local_resvideo[index]));
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
            player.prepare(mediaSource, true, false);
            player.addListener(new Player.DefaultEventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    String stateString;
                    // actually playing media
                    if (playWhenReady && playbackState == Player.STATE_READY) {
                        Log.d(TAG, "onPlayerStateChanged: actually playing media");
                        videoViewHoderM.preview.setVisibility(View.GONE);
                        videoViewHoderM.playerView.setVisibility(View.VISIBLE);
//                        player.seekTo();
                    }
                    switch (playbackState) {
                        case Player.STATE_IDLE:
                            stateString = "ExoPlayer.STATE_IDLE      -";
                            break;
                        case Player.STATE_BUFFERING:
                            stateString = "ExoPlayer.STATE_BUFFERING -";
                            break;
                        case Player.STATE_READY:
                            stateString = "ExoPlayer.STATE_READY     -";
                            break;
                        case Player.STATE_ENDED:
                            stateString = "ExoPlayer.STATE_ENDED     -";
                            break;
                        default:
                            stateString = "UNKNOWN_STATE             -";
                            break;
                    }
                    Log.d(TAG, "changed state to " + stateString + " playWhenReady: " + playWhenReady);
                }
            });
        }
    }

    private int findPosition(int[] lastPositions, boolean isTop) {
        int best = lastPositions[0];
        for (int value : lastPositions) {
            if (isTop) {
                if (value < best) {
                    best = value;
                }
            } else {
                if (value > best) {
                    best = value;
                }
            }
        }
        return best;
    }

    public int[] getVisiblePosition(StaggeredGridLayoutManager layoutManager) {
        int[] lastpoint = new int[layoutManager.getSpanCount()];
        int[] firstpoint = new int[layoutManager.getSpanCount()];
        int[] firstVisibleItemPositions = layoutManager.findFirstVisibleItemPositions(firstpoint);
        int[] lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(lastpoint);
        int[] result = new int[2];
        result[0] = findPosition(firstVisibleItemPositions, true);
        result[1] = findPosition(lastVisibleItemPositions, false);
//            Log.d(TAG, "childCount:" + childCount + "  first：" + first+ " last：" + last);
        return result;
    }

    public int[] getVisiblePosition(GridLayoutManager layoutManager) {
        int[] result = new int[2];
        result[0] = layoutManager.findFirstVisibleItemPosition();
        result[1] = layoutManager.findLastVisibleItemPosition();
//            Log.d(TAG, "childCount:" + childCount + "  first：" + first+ " last：" + last);
        return result;
    }

    //检查子view是否在父view显示布局里面
    private boolean isPlayRange(View childView, View parentView) {

        if (childView == null || parentView == null) {
            return false;
        }

        int[] childLocal = new int[2];
        childView.getLocationOnScreen(childLocal);

        int[] parentLocal = new int[2];
        parentView.getLocationOnScreen(parentLocal);

        boolean playRange = childLocal[1] >= parentLocal[1] &&
                childLocal[1] <= parentLocal[1] + parentView.getHeight() - childView.getHeight();

        return playRange;
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (currentPlayView != null) {
            boolean playRange = isPlayRange(currentPlayView, recyclerView);
            if (!playRange) {
//                mMediaPlayerTool.reset();
                if (player != null) player.release();

            }
        }
    }


    public void checkVideo(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // 当不滚动时
            int childCount = recyclerView.getChildCount();
//            Log.d(TAG, "childCount:" + childCount);
            VideoViewHoderM videoViewHoder = null;
            for (int i = 0; i < childCount; i++) {
                View view = recyclerView.getChildAt(i);
                if (view == null) continue;
                RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(view);
                if (null != childViewHolder) {
//                    Log.d(TAG, "childViewHolder:" + childViewHolder + "  ViewHoderMType：" + (childViewHolder instanceof ViewHoderM) + "  VideoViewHoderMType：" + (childViewHolder instanceof VideoViewHoderM));
                    if (childViewHolder instanceof VideoViewHoderM) {
                        int[] location = new int[2];
                        childViewHolder.itemView.getLocationOnScreen(location);
                        videoViewHoder = (VideoViewHoderM) childViewHolder;
                        break;
                    }
                }
            }
            if (videoViewHoder != null) {
                int playosition = videoViewHoder.getAdapterPosition();
                if (playosition != adapterPosition) {
                    playPosition(videoViewHoder);
                    adapterPosition = playosition;
                }
            }

        } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            // Log.i("SCROLL_STATE_DRAGGING", "手滑动：SCROLL_STATE_DRAGGING");
        } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
            // Log.i("SCROLL_STATE_SETTLING", "松开惯性滑动：SCROLL_STATE_SETTLING");
        }
    }

    public void setPlayPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }
}