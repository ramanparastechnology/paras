package com.paras.choo.adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paras.choo.R;
import com.paras.choo.beans.HomeListItems;

import java.util.ArrayList;

public class HomeListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HomeListItems> homeItems;
    Typeface fieldsfont;
    public HomeListAdapter(Context context, ArrayList<HomeListItems> homeItems){
        this.context = context;
        this.homeItems = homeItems;
        fieldsfont = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
    }

    @Override
    public int getCount() {
        return homeItems.size();
    }

    @Override
    public Object getItem(int position) {
        return homeItems.get(position);
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
            convertView = mInflater.inflate(R.layout.home_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        txtTitle.setTypeface(fieldsfont);
        imgIcon.setImageResource(homeItems.get(position).getIcon());
        txtTitle.setText(homeItems.get(position).getTitle());
        return convertView;
    }

}