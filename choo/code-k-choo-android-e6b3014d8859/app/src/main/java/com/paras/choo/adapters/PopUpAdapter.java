package com.paras.choo.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.paras.choo.R;
import com.paras.choo.beans.PopupItem;

import java.util.ArrayList;

/**
 * Created by hbslenovo-3 on dot6/27/2015.
 */
public class PopUpAdapter extends BaseAdapter {

    /**
     * ******** Declare Used Variables ********
     */
    private Activity activity;
    private ArrayList<PopupItem> originalData;
    // Typeface  Font = Typeface.createFromAsset(getAssets(), "fonts/RobotoCondensed-Light.ttf");
    // private ArrayList<Product> filteredData; // Original Values
    //  private ArrayList<Product> mDisplayedValues;    // Values to be displayed
    // LayoutInflater inflater;
    private static LayoutInflater inflater = null;
    public Resources res;


    /**
     * **********  CustomAdapter Constructor ****************
     */
    public PopUpAdapter(Activity a, ArrayList<PopupItem> d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        originalData = d;
        res = resLocal;
//        Font = Typeface.createFromAsset(activity.getAssets(), "fonts/RobotoCondensed-Light.ttf");

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /**
     * ***** What is the size of Passed Arraylist Size ***********
     */
    public int getCount() {

        if (originalData.size() <= 0)
            return originalData.size();
        return originalData.size();
    }

    public Object getItem(int position) {
        return originalData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }



    /**
     * *** Depends upon originalData size called for each row , Create each ListView row ****
     */
    public View getView(int position, View convertView, ViewGroup parent) {
      TextView item;
        convertView = inflater.inflate(R.layout.popup_item,null);
        item = (TextView)convertView.findViewById(R.id.item);
        item.setText(originalData.get(position).getStationtype());

        return convertView;
    }


}