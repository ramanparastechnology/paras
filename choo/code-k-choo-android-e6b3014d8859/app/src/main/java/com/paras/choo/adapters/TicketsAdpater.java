package com.paras.choo.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paras.choo.R;
import com.paras.choo.utils.ChooPref;
import com.paras.choo.utils.ChooSaveDataPref;
import com.parse.ParseObject;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by paras on 22-08-2015.
 */
public class TicketsAdpater extends BaseAdapter {

    /**
     * ******** Declare Used Variables ********
     */
    private Activity activity;
    List<ParseObject> ticketList;
    SharedPreferences prefs1, prefs;
    //    private ArrayList<StationItem> filteredData;
    // Typeface  Font = Typeface.createFromAsset(getAssets(), "fonts/RobotoCondensed-Light.ttf");
    // private ArrayList<Product> filteredData; // Original Values
    //  private ArrayList<Product> mDisplayedValues;    // Values to be displayed
    // LayoutInflater inflater;
    private static LayoutInflater inflater = null;
    public Resources res;
    ParseObject tempValues = null;
    Typeface fieldsfont;
    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy");
    String date = df.format(Calendar.getInstance().getTime());
    /**
     * **********  CustomAdapter Constructor ****************
     */
    public TicketsAdpater(Activity a, List<ParseObject> ticketList, Resources resLocal) {

        /********** Take passed values **********/
        fieldsfont = Typeface.createFromAsset(a.getAssets(), "Roboto-Light.ttf");
        activity = a;
        res = resLocal;
        prefs1 = a.getSharedPreferences(ChooPref.CHOO_PREF,a.MODE_PRIVATE);
        prefs = a.getSharedPreferences(ChooSaveDataPref.CHOO_SAVEDATA_PREF, a.MODE_PRIVATE);
        this.ticketList = ticketList;
        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /**
     * ***** What is the size of Passed Arraylist Size ***********
     */
    public int getCount() {
        return ticketList.size();
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
        public TextView currentdate;
        public TextView dest;
        public TextView delay;
        public TextView pricetype;
        public TextView submitnow ;
        public TextView choono, desttext, delaytext, tickettriptext, submittext;
        public ImageView submitimage;

    }

    /**
     * *** Depends upon originalData size called for each row , Create each ListView row ****
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;


        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.ticket_item, null);


            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();


            holder.currentdate = (TextView) vi.findViewById(R.id.currentdate);
            holder.dest = (TextView) vi.findViewById(R.id.dest);
            holder.delay = (TextView) vi.findViewById(R.id.delay);
            holder.pricetype = (TextView) vi.findViewById(R.id.pricetype);
            holder.submitnow = (TextView) vi.findViewById(R.id.submitnow);
            holder.choono = (TextView) vi.findViewById(R.id.choono);
            holder.desttext = (TextView) vi.findViewById(R.id.desttext);
            holder.delaytext = (TextView) vi.findViewById(R.id.delaytext);
            holder.tickettriptext = (TextView) vi.findViewById(R.id.tickettriptext);
            holder.submittext = (TextView) vi.findViewById(R.id.submittext);
            holder.submitimage = (ImageView) vi.findViewById(R.id.submitimage);

            holder.currentdate.setTypeface(fieldsfont);
            holder.dest.setTypeface(fieldsfont);
            holder.delay.setTypeface(fieldsfont);
            holder.pricetype.setTypeface(fieldsfont);
            holder.submitnow.setTypeface(fieldsfont);
            holder.choono.setTypeface(fieldsfont);
            holder.desttext.setTypeface(fieldsfont);
            holder.delaytext.setTypeface(fieldsfont);
            holder.tickettriptext.setTypeface(fieldsfont);
            holder.submittext.setTypeface(fieldsfont);
            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (ticketList.size() <= 0) {
//           holder.Type.setText("No originalData");

        } else
        /***** Get each Model object from Arraylist ********/
        tempValues = null;


            tempValues = ticketList.get(position);
        if(prefs1.getString(ChooPref.STATION, null) != null && position == 0)
        {
            holder.currentdate.setText(tempValues.getCreatedAt() + "");
            holder.dest.setText(tempValues.getString("DESTINATION"));
            Log.e("", "tempValues.getJSONObject(ticketData)" + tempValues.getJSONObject("ticketData"));
            Log.e("", "tempValues.getJSONObject(persData)" + tempValues.getJSONObject("persData"));
            if(prefs.getBoolean(ChooSaveDataPref.IS_BAHN_TIME_CARD, false)){
                holder.pricetype.setText(tempValues.getString("USED_CARD_TYPE"));
            }else {
                try {
                    String ticketprice = prefs1.getString(ChooPref.SELECT_PRICE, null).replace("\u20ac ", "");
                    Log.e("ticketprice", ticketprice + " \u20ac");
                    holder.pricetype.setText(ticketprice + " \u20ac");
//                holder.pricetype.setText(prefs1.getString(ChooPref.SELECT_PRICE, null));
                }catch (Exception e){
                    holder.pricetype.setText(prefs1.getString(ChooPref.SELECT_PRICE, null));
                }
            }
            if("en".equals( activity.getString(R.string.lang) ) ) {
                holder.delay.setText(tempValues.getInt("MINUTES_LATE") + " Minutes");
            }else{
                holder.delay.setText(tempValues.getInt("MINUTES_LATE") + " Minuten");
            }
            holder.choono.setText("Choo-No:" + tempValues.getString("ORDER_ID"));
            holder.submittext.setText(R.string.notyetsubmit);
            holder.submittext.setTextColor(Color.parseColor("#ff0000"));
            holder.submitimage.setImageResource(R.drawable.pen);
            holder.submitnow.setText(R.string.submitnow);
            holder.submitnow.setTextColor(activity.getResources().getColor(R.color.app_theme));
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy HH:mm");
            Log.e("formatter.format(tempValues.getCreatedAt())", formatter.format(tempValues.getCreatedAt()));
            holder.currentdate.setText(formatter.format(tempValues.getCreatedAt()));
            holder.dest.setText(tempValues.getString("DESTINATION"));
            holder.submittext.setText(activity.getString(R.string.status));
            holder.submittext.setTextColor(activity.getResources().getColor(R.color.black));
            Log.e("", "tempValues.getJSONObject(ticketData)" + tempValues.getJSONObject("ticketData"));
            Log.e("", "tempValues.getJSONObject(persData)" + tempValues.getJSONObject("persData"));

            Log.e("STATUS_ID", tempValues.getString("STATUS_ID"));
            if(tempValues.getString("STATUS_ID").equals("1")) {
                holder.submitnow.setText(activity.getString(R.string.status1));
                holder.submitnow.setTextColor(activity.getResources().getColor(R.color.statecolor));
                holder.submitimage.setImageResource(R.drawable.stateiicon);
            }else if(tempValues.getString("STATUS_ID").equals("3")){
                holder.submitnow.setText(activity.getString(R.string.status3));
                holder.submitnow.setTextColor(activity.getResources().getColor(R.color.statecolorthree));
                holder.submitimage.setImageResource(R.drawable.stateiconthree);
            }else if(tempValues.getString("STATUS_ID").equals("5")){
                holder.submitnow.setText(activity.getString(R.string.status5));
                holder.submitnow.setTextColor(activity.getResources().getColor(R.color.statecolornine));
                holder.submitimage.setImageResource(R.drawable.stateiconnine);
            }else if(tempValues.getString("STATUS_ID").equals("9")){
                holder.submitnow.setText(activity.getString(R.string.status9));
                holder.submitnow.setTextColor(activity.getResources().getColor(R.color.statecolornine));
                holder.submitimage.setImageResource(R.drawable.stateiconnine);
            }


            try {
                if(tempValues.getJSONObject("ticketData").getString("TICKET_PRICE").equals("\u20ac 0,0"))
                {
                    holder.pricetype.setText(tempValues.getJSONObject("ticketData").getString("USED_CARD_TYPE"));
                }else {
                    String ticketprice = tempValues.getJSONObject("ticketData").getString("TICKET_PRICE").replace("\u20ac ", "");
                    Log.e("ticketprice", ticketprice + " \u20ac");
                    holder.pricetype.setText(ticketprice + " \u20ac");
//                    holder.pricetype.setText(tempValues.getJSONObject("ticketData").getString("TICKET_PRICE"));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            if("en".equals( activity.getString(R.string.lang) ) ) {
                holder.delay.setText(tempValues.getInt("MINUTES_LATE") + " Minutes");
            }else{
                holder.delay.setText(tempValues.getInt("MINUTES_LATE") + " Minuten");
            }
            holder.choono.setText("Choo-No:" + tempValues.getString("ORDER_ID"));
        }


        return vi;
    }
}