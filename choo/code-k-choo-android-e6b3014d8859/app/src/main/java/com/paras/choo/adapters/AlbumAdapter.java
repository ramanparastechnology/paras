package com.paras.choo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.paras.choo.R;
import com.paras.choo.beans.AlbumListItems;

import java.util.ArrayList;

/**
 * Created by paras on 14-09-2015.
 */
public class AlbumAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AlbumListItems> albumItems;
//    Typeface fieldsfont;
    public AlbumAdapter(Context context, ArrayList<AlbumListItems> albumItems){
        this.context = context;
        this.albumItems = albumItems;
//        fieldsfont = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
    }

    @Override
    public int getCount() {
        return albumItems.size();
    }

    @Override
    public Object getItem(int position) {
        return albumItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.album_list_item, null);
        }

        ImageView album_img = (ImageView) convertView.findViewById(R.id.album_img);
        TextView albumname = (TextView) convertView.findViewById(R.id.albumname);
        ImageView albumtick = (ImageView) convertView.findViewById(R.id.album_tick);
//        txtTitle.setTypeface(fieldsfont);
        album_img.setImageResource(albumItems.get(position).getIcon());
        albumname.setText(albumItems.get(position).getTitle());
        albumtick.setImageResource(albumItems.get(position).getTick());
        return convertView;
    }
}
