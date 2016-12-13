package org.androidtown.privatecloud;

import android.graphics.drawable.Drawable;

/**
 * Created by neverstop on 2016-12-10.
 */

public class IconTextItem {
    private Drawable mIcon;
    private String mData;
    private String mThumbnail;

    public IconTextItem(Drawable icon, String obj){
        this(icon, obj, null);
    }

    public IconTextItem(Drawable mIcon, String mData, String mThumbnail) {
        this.mIcon = mIcon;
        this.mData = mData;
        this.mThumbnail = mThumbnail;
    }

    public String getData() {
        return mData;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public void setData(String obj) {
        mData = obj;
    }

    public void setIcon(Drawable icon){
        mIcon = icon;
    }

    public Drawable getIcon() {
        return mIcon;
    }
}
