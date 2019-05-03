package com.wisn.medial.imagelist.glide;

/**
 * Created by Wisn on 2019-05-03 16:03.
 */
public interface OnProgressListener {
    void onProgress(String url, boolean isComplete, int percentage, long bytesRead, long totalBytes);

}
