package com.wisn.medial.ad;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.wisn.medial.R;

/**
 * Created by Wisn on 2019/4/20 下午3:22.
 */
public class VideoViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        VideoView videoView = (VideoView) findViewById(R.id.main_video);
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"jiaoxue.mp4";
        /**
         * 本地播放
         */
//        videoView.setVideoPath("path");
        /**
         * 网络播放
         */
        videoView.setVideoURI(Uri.parse("http://192.168.1.110:8080/video_ccc.mp4"));
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.release();
                    mp = new MediaPlayer();
                }
//                mp.setVolume(0, 0);//静音播放
                mp.setLooping(true);
                mp.start();
            }
        });
        videoView.setFocusable(false);
        videoView.start();
        /**
         * 将控制器和播放器进行互相关联
         */
       /* MediaController controller = new MediaController(this);//实例化控制器
        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);*/


    }

}
