package com.wisn.medial.imagelist.subscale.decoder;

import android.support.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;

public interface DecoderFactory<T> {

    @NonNull T make() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException;

}
