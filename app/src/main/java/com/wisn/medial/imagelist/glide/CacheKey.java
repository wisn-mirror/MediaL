package com.wisn.medial.imagelist.glide;

import com.bumptech.glide.load.Key;

import java.security.MessageDigest;

/**
 * Created by Wisn on 2019-05-03 16:49.
 */
public class CacheKey implements Key {
    private final Key sourceKey;
    private final Key signature;

    public CacheKey(Key sourceKey, Key signature) {
        this.sourceKey = sourceKey;
        this.signature = signature;
    }

    public Key getSourceKey() {
        return sourceKey;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CacheKey) {
            CacheKey other = (CacheKey) o;
            return sourceKey.equals(other.sourceKey) && signature.equals(other.signature);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = sourceKey.hashCode();
        result = 31 * result + signature.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CacheKey{" + "sourceKey=" + sourceKey + ", signature=" + signature + '}';
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        sourceKey.updateDiskCacheKey(messageDigest);
        signature.updateDiskCacheKey(messageDigest);
    }
}
