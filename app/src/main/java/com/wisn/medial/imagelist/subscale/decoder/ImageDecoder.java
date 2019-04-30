package com.wisn.medial.imagelist.subscale.decoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;


public interface ImageDecoder {


   Bitmap decode(Context context, @NonNull Uri uri) throws Exception;

}
