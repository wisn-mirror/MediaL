package com.wisn.medial.imagelist.subscale.decoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;


public interface ImageRegionDecoder {

    @NonNull Point init(Context context, @NonNull Uri uri) throws Exception;


    @NonNull Bitmap decodeRegion(@NonNull Rect sRect, int sampleSize);

    /**
     * Status check. Should return false before initialisation and after recycle.
     * @return true if the decoder is ready to be used.
     */
    boolean isReady();

    /**
     * This method will be called when the decoder is no longer required. It should clean up any resources still in use.
     */
    void recycle();

}
