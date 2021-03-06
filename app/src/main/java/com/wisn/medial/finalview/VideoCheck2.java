package com.wisn.medial.finalview;

import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.wisn.medial.src.Constants;
import com.wisn.medial.tianmao.VideoViewHoderM;

import java.util.HashMap;

/**
 * Created by Wisn on 2019-05-09 18:23.
 */
public class VideoCheck2 {
    private String TAG = "VideoCheck";
    public SimpleExoPlayer player;
    private int lastPlayPosition = -1;
    VideoViewHoderM videoViewHoderM;
    private RecyclerView recycleview;
    public String currentPlayUrl;
    HashMap<String, Long> playPosition = new HashMap<>();

    public void releasePlayer() {
        lastPlayPosition=-1;
        if (player != null) {
            if (!TextUtils.isEmpty(currentPlayUrl)) {
                playPosition.put(currentPlayUrl, player.getCurrentPosition());
            }
            player.setPlayWhenReady(false);
            player.release();
        }
    }

    public void playPosition(VideoViewHoderM videoViewHoderM) {
        videoViewHoderM.preview();
        int index = 0;
        if (videoViewHoderM.getAdapterPosition() == 3) {
            index = 0;
        } else if (videoViewHoderM.getAdapterPosition() == 8) {
            index = 1;
        } else if (videoViewHoderM.getAdapterPosition() == 14) {
            index = 2;
        } else if (videoViewHoderM.getAdapterPosition() == 20) {
            index = 3;
        }
        releasePlayer();
        player = ExoPlayerFactory.newSimpleInstance(videoViewHoderM.playerView.getContext());
        videoViewHoderM.playerView.setPlayer(player);
        player.setVolume(0f);
        this.videoViewHoderM = videoViewHoderM;
        player.setPlayWhenReady(true);
        if (player.getPlaybackState() == Player.STATE_IDLE) {
            currentPlayUrl = Constants.ip + Constants.local_resvideo[index];
            ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory("wisn")).createMediaSource(Uri.parse(currentPlayUrl));
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
            //4倍速度播放
            PlaybackParameters param = new PlaybackParameters(1);
            player.setPlaybackParameters(param);
            player.prepare(mediaSource, true, false);
            if(playPosition.containsKey(currentPlayUrl)){
                try {
                    long log = playPosition.get(currentPlayUrl);
                    if (log > 0) {
                        player.seekTo(log);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            player.addListener(new Player.DefaultEventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    String stateString;
                    if (playWhenReady && playbackState == Player.STATE_READY) {
                        Log.d(TAG, "onPlayerStateChanged: actually playing media");
                        videoViewHoderM.unPreview();
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
        if (childView == null || parentView == null) return false;
        int[] childLocal = new int[2];
        childView.getLocationOnScreen(childLocal);
        int[] parentLocal = new int[2];
        parentView.getLocationOnScreen(parentLocal);
        boolean playRange = childLocal[1] >= (parentLocal[1] - childView.getHeight()) &&
                childLocal[1] <= (parentLocal[1] + parentView.getHeight() - childView.getHeight() / 4);
        Log.d(TAG, playRange + " childLocal[1]  " + childLocal[1] + " parentLocal[1]: " + parentLocal[1] + " " + childView.getHeight() + " " + parentView.getHeight());
        return playRange;
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//        onScrolledCheck(recyclerView);
    }

    public void onScrolledCheck(RecyclerView recyclerView) {
        if (videoViewHoderM != null && videoViewHoderM.itemView != null) {
            boolean playRange = isPlayRange(videoViewHoderM.itemView, recyclerView);
            if (!playRange) {
                videoViewHoderM.preview();
                releasePlayer();
                lastPlayPosition = -1;
            }
        }
    }


    public void onPageSelected(RecyclerView recyclerView) {
        if (videoViewHoderM != null) {
            videoViewHoderM.preview();
            releasePlayer();
        }
    }

    public void initCheck(RecyclerView recyclerView) {
        if (this.recycleview != recyclerView) {
            lastPlayPosition = -1;
            checkVideo(recyclerView, RecyclerView.SCROLL_STATE_IDLE);
        }

    }

    public void checkVideo(RecyclerView recyclerView, int newState) {
        this.recycleview = recyclerView;
        Log.d(TAG, " childLocal[1] SCROLL_STATE_IDLE  " + newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            onScrolledCheck(recyclerView);
            // 当不滚动时
            int childCount = recyclerView.getChildCount();
            VideoViewHoderM videoViewHoder = null;
            for (int i = 0; i < childCount; i++) {
                View view = recyclerView.getChildAt(i);
                if (view == null) continue;
                RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(view);
                if (null != childViewHolder) {
                    if (childViewHolder instanceof VideoViewHoderM) {
                        videoViewHoder = (VideoViewHoderM) childViewHolder;
                        break;
                    }
                }
            }
            if (videoViewHoder != null) {
                int playosition = videoViewHoder.getAdapterPosition();
                if (playosition != lastPlayPosition) {
                    playPosition(videoViewHoder);
                    lastPlayPosition = playosition;
                }
            }else{
                releasePlayer();

            }

        }
    }

}