package com.wisn.medial.exoplayer;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.wisn.medial.R;
import com.wisn.medial.src.Constants;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * exoplayer-core：核心功能 (必要)
 * <p>
 * exoplayer-dash：支持DASH内容
 * <p>
 * exoplayer-hls：支持HLS内容
 * <p>
 * exoplayer-smoothstreaming：支持SmoothStreaming内容
 * <p>
 * exoplayer-ui：用于ExoPlayer的UI组件和相关的资源。
 * <p>
 * <p>
 * <p>
 * MediaSource：媒体资源，用于定义要播放的媒体，加载媒体，以及从哪里加载媒体。简单的说，MediaSource就是代表我们要播放的媒体文件，可以是本地资源，可以是网络资源。MediaSource在播放开始的时候，通过ExoPlayer.prepare方法注入。
 * <p>
 * Renderer：渲染器，用于渲染媒体文件。当创建播放器的时候，Renderers被注入。
 * <p>
 * TrackSelector：轨道选择器，用于选择MediaSource提供的轨道（tracks），供每个可用的渲染器使用。
 * <p>
 * LoadControl：用于控制MediaSource何时缓冲更多的媒体资源以及缓冲多少媒体资源。LoadControl在创建播放器的时候被注入。
 */
public class ExoPlayerActivity extends AppCompatActivity {

    private static final String TAG = "ExoPlayerActivity";
    private PlayerView playerView;

    private ExoPlayer player;

    private boolean playWhenReady = true;
    private int currentWindow;
    private long playbackPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);
        playerView = findViewById(R.id.playerView);
        player = ExoPlayerFactory.newSimpleInstance(this);
//        player = ExoPlayerFactory.newSimpleInstance( new DefaultRenderersFactory(this),
//                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
//        player.seekTo(currentWindow, playbackPosition);
//        Uri uri = Uri.parse(Constants.ip + "video_ccc.mp4");
//        MediaSource mediaSource = buildMediaSource(uri);
        List<Uri> urls=new ArrayList<>();
        urls.add(Uri.parse(Constants.ip + "video_aaa.mp4"));
        urls.add(Uri.parse(Constants.ip + "video_bbb.mp4"));
        urls.add(Uri.parse(Constants.ip + "video_ccc.mp4"));
        MediaSource mediaSource = buildMediaSources(urls);
        player.prepare(mediaSource, true, false);
        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                String stateString;
                // actually playing media
                if (playWhenReady && playbackState == Player.STATE_READY) {
                    Log.d(TAG, "onPlayerStateChanged: actually playing media");
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

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
//            player.removeListener();
            player.release();
            player = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
//                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                new DefaultHttpDataSourceFactory("wisn")).
                createMediaSource(uri);
    }

    private MediaSource buildMediaSources(List<Uri> uris) {

        DefaultHttpDataSourceFactory dataSourceFactory =
//                new DefaultHttpDataSourceFactory("user-agent");
                new DefaultHttpDataSourceFactory("exoplayer-codelab");
        List<MediaSource> audioSources = new ArrayList<>();
        for (Uri uri : uris) {
            ExtractorMediaSource videoSource =
                    new ExtractorMediaSource.Factory(dataSourceFactory).
                            createMediaSource(uri);
            audioSources.add(videoSource);
        }
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
        concatenatingMediaSource.addMediaSources(audioSources);
        return concatenatingMediaSource;
    }


}
