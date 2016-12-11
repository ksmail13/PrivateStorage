package org.androidtown.privatecloud;

import android.graphics.drawable.Drawable;

/**
 * Created by neverstop on 2016-12-10.
 */

public class IconTextItem {
    private Drawable mIcon;
    private String mData;

    public IconTextItem(Drawable icon, String obj){
        mIcon = icon;
        mData = obj;
    }


    public String getData() {
        return mData;
    }

    public String getData(int index){
        if (mData == null){
            return null;
        }

        return mData;
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
