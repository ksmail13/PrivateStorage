package org.androidtown.privatecloud;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by neverstop on 2016-12-10.
 */

public class IconTextView extends LinearLayout {
    ImageView mIcon;
    TextView mText;

    public IconTextView(Context context, IconTextItem aItem) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item, this, true);

        mIcon = (ImageView) findViewById(R.id.iconItem);
        //mIcon.setImageDrawable(aItem.getIcon());
        mText = (TextView) findViewById(R.id.dataItem);
        mText.setText(aItem.getData());
    }
    public void  setText(int index, String data){
        if(index ==0) {
            mText.setText(data);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setIcon(Drawable icon){
        mIcon.setImageDrawable(icon);
    }
}
