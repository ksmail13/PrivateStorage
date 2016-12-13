package org.androidtown.privatecloud;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neverstop on 2016-12-10.
 */

public class IconTextGridAdapter extends BaseAdapter {
    private Context mContext;

    private List<IconTextItem> mItems = new ArrayList<IconTextItem>();

    public IconTextGridAdapter(Context context){
        mContext = context;
    }

    public void addItem(IconTextItem item){
        mItems.add(item);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    public int getItem() {
        return mItems.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IconTextView itemView;
        if(convertView == null) {
            itemView = new IconTextView(mContext, mItems.get(position));
        } else {
            itemView = (IconTextView) convertView;
        }
        IconTextItem item = mItems.get(position);
        if(item.getThumbnail() != null) {
            Picasso.with(mContext).load(item.getThumbnail()).into(itemView.mIcon);
        }
        else {
            itemView.setIcon(item.getIcon());
        }

        itemView.setText(0, item.getData());
        return itemView;
    }
}
