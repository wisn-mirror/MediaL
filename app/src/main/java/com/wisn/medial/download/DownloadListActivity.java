package com.wisn.medial.download;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.download.DownloadTask;
import com.download.core.cause.EndCause;
import com.download.core.listener.DownloadListener2;
import com.wisn.medial.R;

import java.io.File;

/**
 * Created by Wisn on 2019-04-23 15:07.
 */
public class DownloadListActivity extends Activity {

    public static final String URL =
            "https://cdn.llscdn.com/yy/files/tkzpx40x-lls-LLS-5.7-785-20171108-111118.apk";
    private String TAG="DownloadListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadlist);
        test1();
    }

    public void test1() {
        String path = Environment.getExternalStorageDirectory().getPath();
        DownloadTask task = new DownloadTask
//                .Builder(URL, new File(path+"/downloadtest/"))
                .Builder(URL, new File(path))
                .setFilename(System.currentTimeMillis()+".apk")
                // if there is the same task has been completed download, just delete it and
                // re-download automatically.
                .setPassIfAlreadyCompleted(false)
                .setMinIntervalMillisCallbackProcess(80)
                // because for the notification we don't need make sure invoke on the ui thread, so
                // just let callback no need callback to the ui thread.
                .setAutoCallbackToUIThread(false)
                .build();
        task.enqueue(new DownloadListener2() {

            @Override
            public void taskStart(@NonNull DownloadTask task) {
                Log.e(TAG,"taskStart");
            }

            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause) {
               if(realCause!=null) realCause.printStackTrace();
                Log.e(TAG,"taskEnd"+cause+" ");

            }
        });
    }
}
