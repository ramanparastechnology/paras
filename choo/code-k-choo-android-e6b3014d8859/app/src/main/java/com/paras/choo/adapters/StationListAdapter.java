package com.paras.choo.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paras.choo.R;
import com.paras.choo.beans.StationItem;

import java.util.ArrayList;

/**
 * Created by hbslenovo-3 on dot6/27/2015.
 */
public class StationListAdapter extends BaseAdapter implements Filterable {

    /**
     * ******** Declare Used Variables ********
     */
    private Activity activity;
    private ArrayList<StationItem> originalData;
    private ArrayList<StationItem> filteredData;
    // Typeface  Font = Typeface.createFromAsset(getAssets(), "fonts/RobotoCondensed-Light.ttf");
    // private ArrayList<Product> filteredData; // Original Values
    //  private ArrayList<Product> mDisplayedValues;    // Values to be displayed
    // LayoutInflater inflater;
    private static LayoutInflater inflater = null;
    public Resources res;
    StationItem tempValues = null;
    ArrayList<StationItem> FilteredArrList;
    /**
     * **********  CustomAdapter Constructor ****************
     */
    public StationListAdapter(Activity a, ArrayList<StationItem> d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        originalData = d;
        filteredData = d;
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
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    /**
     * ****** Create a holder Class to contain inflated xml file elements ********
     */
    public static class ViewHolder {
        public TextView stationitem;
    }

    /**
     * *** Depends upon originalData size called for each row , Create each ListView row ****
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;


        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.stations_item, null);


            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();


            holder.stationitem = (TextView) vi.findViewById(R.id.stationitem);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (originalData.size() <= 0) {
//           holder.Type.setText("No originalData");

        } else
        /***** Get each Model object from Arraylist ********/
            tempValues = null;
        tempValues = originalData.get(position);

//            mainLinearLayout
//            else
//                mainLinearLayout.setcolor(R.color.color2);
        //  Log.e(""+tempValues.getImageurl() +"-"+tempValues.getImageurl(),"-----------"+tempValues.getImageurl());
        /************  Set Model values in Holder elements ***********/
        if (tempValues.getStationName().equals("0")) {
            //  holder.Type.setImageResource(R.drawable.previous_order);
        } else {
            //  holder.Type.setImageResource( type[Integer.parseInt(tempValues.getPid())]);
        }


        holder.stationitem.setText(tempValues.getStationName());
        if (position % 2 == 0) {
        }
        return vi;
    }


    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                originalData = (ArrayList<StationItem>) results.values; // has the filtered values
                notifyDataSetChanged();
                // notifies the originalData with new filtered value
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                FilteredArrList = new ArrayList<StationItem>();

                if (filteredData == null) {
                    filteredData = originalData; // saves the original originalData in filteredData

                    Log.e("", "" + filteredData);
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the filteredData(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = filteredData.size();
                    results.values = filteredData;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < filteredData.size(); i++) {
                        String originalData1 = filteredData.get(i).getStationName();
                        if (originalData1.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new StationItem (filteredData.get(i).getStationName()));
                        }
                    }
                    // set the Filtered result to return
                    //  results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
    public ArrayList<StationItem> filterArray(){
       return  FilteredArrList;
    }

}