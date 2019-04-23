package com.wisn.medial.download;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.download.DownloadContext;
import com.download.DownloadContextListener;
import com.download.DownloadTask;
import com.download.SpeedCalculator;
import com.download.core.cause.EndCause;
import com.download.core.cause.ResumeFailedCause;
import com.download.core.listener.DownloadListener1;
import com.download.core.listener.assist.Listener1Assist;
import com.wisn.medial.R;
import com.wisn.medial.src.Constants;

import java.io.File;

/**
 * Created by Wisn on 2019-04-23 15:07.
 */
public class DownloadListActivity extends Activity {

    public static final String URL =
            "https://cdn.llscdn.com/yy/files/tkzpx40x-lls-LLS-5.7-785-20171108-111118.apk";
    private String TAG = "----";

    private static final int CURRENT_PROGRESS = 2;
    ScrollView scroll_info;
    TextView testResult;
    String path = Environment.getExternalStorageDirectory().getPath();
    private DownloadContext downloadContext;
    private DownloadTask task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadlist);
        scroll_info = findViewById(R.id.scroll_info);
        testResult = findViewById(R.id.testResult);

    }


    public void OnClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.downloadOne:
                test1();
                break;
            case R.id.downloadBatch:
                // start
                final long startTime = SystemClock.uptimeMillis();
                 final DownloadContext.Builder builder = new DownloadContext.QueueSet()
                        .setParentPathFile(new File(path))
                        .setMinIntervalMillisCallbackProcess(300)
                        .commit();
                Log.d(TAG, "before bind bunch task consume "
                        + (SystemClock.uptimeMillis() - startTime) + "ms");
                for (int i = 0; i < Constants.res.length; i++) {
                    builder.bind(Constants.res[i]).addTag(1, i);
                }
                Log.d(TAG, "before build bunch task consume "
                        + (SystemClock.uptimeMillis() - startTime) + "ms");
                downloadContext = builder.setListener(new DownloadContextListener() {
                      @Override public void taskEnd(@NonNull DownloadContext context,
                                                    @NonNull DownloadTask task,
                                                    @NonNull EndCause cause,
                                                    @Nullable Exception realCause,
                                                    int remainCount) {
                      }

                      @Override public void queueEnd(@NonNull DownloadContext context) {
  //                        v.setTag(null);
  //                        radioGroup.setEnabled(true);
  //                        deleteContainerView.setEnabled(true);
  //                        startOrCancelTv.setText(R.string.start);
                      }
                  }).build();

                SpeedCalculator     speedCalculator = new SpeedCalculator();
                Log.d(TAG, "before bunch task consume "
                        + (SystemClock.uptimeMillis() - startTime) + "ms");
                downloadContext.start(new DownloadListener1() {


                    @Override
                    public void taskStart(@NonNull DownloadTask task, @NonNull Listener1Assist.Listener1Model model) {
                        updateView(TAG+"taskStart",true);
                    }

                    @Override
                    public void retry(@NonNull DownloadTask task, @NonNull ResumeFailedCause cause) {
                        updateView(TAG+"retry",true);

                    }

                    @Override
                    public void connected(@NonNull DownloadTask task, int blockCount, long currentOffset, long totalLength) {
                        updateView(TAG+"connected",true);

                    }

                    @Override
                    public void progress(@NonNull DownloadTask task, long currentOffset, long totalLength) {

                        final Object progressValue = task.getTag(CURRENT_PROGRESS);
                        final long preOffset = progressValue == null ? 0 : (long) progressValue;
                        final long increase = currentOffset - preOffset;
                        speedCalculator.downloading(increase);
                        task.addTag(CURRENT_PROGRESS, currentOffset);
                        updateView(TAG+speedCalculator .speed()+" progress currentOffset:"+currentOffset+ " totalLength:"+totalLength,true);
                    }

                    @Override
                    public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull Listener1Assist.Listener1Model model) {
                        updateView(TAG+"taskEnd"+task.getUrl()+ " "+task.getTag(1)+" cause"+cause,true);

                    }
                }, true);
                Log.d(TAG,
                        "start bunch task consume " + (SystemClock
                                .uptimeMillis() - startTime) + "ms");
                break;
            case R.id.deleteAll:
                if(downloadContext!=null){
                    downloadContext.stop();
                }
                if(task!=null){
                    task.cancel();
                }
                break;
        }

    }

    public void test1() {
        task = new DownloadTask
//                .Builder(URL, new File(path+"/downloadtest/"))
                .Builder(URL, new File(path))
                .setFilename(System.currentTimeMillis() + ".apk")
                // if there is the same task has been completed download, just delete it and
                // re-download automatically.
                .setPassIfAlreadyCompleted(false)
                .setMinIntervalMillisCallbackProcess(80)
                // because for the notification we don't need make sure invoke on the ui thread, so
                // just let callback no need callback to the ui thread.
                .setAutoCallbackToUIThread(false)
                .build();
        SpeedCalculator     speedCalculator = new SpeedCalculator();

        task.enqueue(new DownloadListener1() {


            @Override
            public void taskStart(@NonNull DownloadTask task, @NonNull Listener1Assist.Listener1Model model) {

            }

            @Override
            public void retry(@NonNull DownloadTask task, @NonNull ResumeFailedCause cause) {

            }

            @Override
            public void connected(@NonNull DownloadTask task, int blockCount, long currentOffset, long totalLength) {

            }

            @Override
            public void progress(@NonNull DownloadTask task, long currentOffset, long totalLength) {

                final Object progressValue = task.getTag(CURRENT_PROGRESS);
                final long preOffset = progressValue == null ? 0 : (long) progressValue;
                final long increase = currentOffset - preOffset;
                speedCalculator.downloading(increase);
                task.addTag(CURRENT_PROGRESS, currentOffset);
                updateView(TAG+speedCalculator .speed()+" progress currentOffset:"+currentOffset+ " totalLength:"+totalLength,true);

            }

            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull Listener1Assist.Listener1Model model) {
                updateView(TAG+"taskEnd"+task.getUrl()+ " "+task.getTag(1)+" cause"+cause,true);

            }
        });
    }


    public void updateView(final String msg, final boolean isAppend) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isAppend) {
                    testResult.append(msg +"\n");
                } else {
                    testResult.setText(msg);
                }
                scroll_info.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
            }
        });
    }

}
