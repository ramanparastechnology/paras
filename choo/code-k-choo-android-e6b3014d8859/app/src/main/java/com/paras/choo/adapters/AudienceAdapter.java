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
import com.paras.choo.beans.AudienceListItems;

import java.util.ArrayList;

/**
 * Created by paras on 14-09-2015.
 */
public class AudienceAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AudienceListItems> audienceItems;
//    Typeface fieldsfont;
    public AudienceAdapter(Context context, ArrayList<AudienceListItems> audienceItems){
        this.context = context;
        this.audienceItems = audienceItems;
//        fieldsfont = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
    }

    @Override
    public int getCount() {
        return audienceItems.size();
    }

    @Override
    public Object getItem(int position) {
        return audienceItems.get(position);
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
            convertView = mInflater.inflate(R.layout.audience_list_item, null);
        }

        ImageView aud_tick = (ImageView) convertView.findViewById(R.id.aud_tick);
        TextView audiencename = (TextView) convertView.findViewById(R.id.audiencename);
//        txtTitle.setTypeface(fieldsfont);
        if (audienceItems.get(position).ischeck())
            aud_tick.setVisibility(View.VISIBLE);else
            aud_tick.setVisibility(View.INVISIBLE);
//        aud_tick.setImageResource(audienceItems.get(position).getTick());
        audiencename.setText(audienceItems.get(position).getTitle());

        return convertView;
    }
}
