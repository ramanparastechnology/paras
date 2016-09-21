package com.paras.choo.fragments;

import android.annotation.TargetApi;
import android.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;


import com.paras.choo.R;
import com.paras.choo.adapters.StationListAdapter;
import com.paras.choo.beans.StationItem;
import com.paras.choo.utils.ChooPref;
import com.paras.choo.utils.DateTime;
import com.paras.choo.utils.DateTimePicker;
import com.paras.choo.utils.SimpleDateTimePicker;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RefundCheck.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RefundCheck#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RefundCheck extends Fragment implements DateTimePicker.OnDateTimeSetListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    View rootview ;
    TextView dtPlanned,dtActual,destText;
    EditText selectStation;
    ListView lststationlist;
    Button checkNow;
    Switch iceUser;
    StationListAdapter dataAdapter = null;
    ArrayList<StationItem> arystationList;
    Resources res;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    Animation shake;
    DateFormat df;
    Boolean isICEuser = false;
    TextView heading;
    FrameLayout layout_MainMenu;
    TextView dtplannedtext, dtactualtext, icesprinttext;
    Typeface headlinefont, fieldsfont;
    //The "x" and "y" position of the "Show Button" on screen.
    Point p;
    int totalminutes = 0;
    //    0 for planned time and dot1 for actual time
    int TimeStatus = 0 ;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter dot1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RefundCheck.
     */
    // TODO: Rename and change types and number of parameters
    public static RefundCheck newInstance(String param1, String param2) {
        RefundCheck fragment = new RefundCheck();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RefundCheck() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview  = inflater.inflate(R.layout.fragment_refund_check, container, false);
        layout_MainMenu = (FrameLayout) getActivity().findViewById( R.id.realtabcontent);
        layout_MainMenu.getForeground().setAlpha(0);
        headlinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");
        fieldsfont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        heading = (TextView)rootview.findViewById(R.id.heading);
        selectStation = (EditText)rootview.findViewById(R.id.selectStation);
        lststationlist = (ListView) rootview.findViewById(R.id.stationlist);
        destText = (TextView)rootview.findViewById(R.id.destText);
        dtplannedtext = (TextView)rootview.findViewById(R.id.dtplannedtext);
        dtactualtext = (TextView)rootview.findViewById(R.id.dtactualtext);
        icesprinttext = (TextView)rootview.findViewById(R.id.icesprinttext);
        dtPlanned = (TextView)rootview.findViewById(R.id.dtplanned);
        dtActual = (TextView)rootview.findViewById(R.id.dtactual);
        checkNow = (Button)rootview.findViewById(R.id.checknow);
        iceUser = (Switch)rootview.findViewById(R.id.iceuser);
        heading.setTypeface(headlinefont);
        destText.setTypeface(fieldsfont);
        selectStation.setTypeface(fieldsfont);
        dtplannedtext.setTypeface(fieldsfont);
        dtPlanned.setTypeface(fieldsfont);
        dtactualtext.setTypeface(fieldsfont);
        dtActual.setTypeface(fieldsfont);
        icesprinttext.setTypeface(fieldsfont);
        shake = AnimationUtils.loadAnimation(getActivity(),R.anim.shake);
        arystationList = new ArrayList<StationItem>();
        res = getResources();
        editor = getActivity().getSharedPreferences(ChooPref.CHOO_PREF, getActivity().MODE_PRIVATE).edit();
        prefs = getActivity().getSharedPreferences(ChooPref.CHOO_PREF, getActivity().MODE_PRIVATE);
        df = new SimpleDateFormat("dd.MM.yy HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        String[] station = res.getStringArray(R.array.station);
        for(int i = 0;i < station.length; i++){
            arystationList.add(new StationItem(station[i]));
        }
        if(prefs.getString(ChooPref.BACK,null) == "nopopup")
        {

        }else {
            if (prefs.getString(ChooPref.STATION,null) != null){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        showPopup(getActivity());
                        layout_MainMenu.getForeground().setAlpha(220); // dim
                    }
                }, 500);
            }
        }
        if(prefs.getString(ChooPref.STATION,null) != null)
        {
            selectStation.setText(prefs.getString(ChooPref.STATION,null));
        }
        if(prefs.getString(ChooPref.PLANED_ARRIVAL,null) == null)
        {
            dtPlanned.setText(date+"");
        }else{
            dtPlanned.setText(prefs.getString(ChooPref.PLANED_ARRIVAL,null));
        }
        if(prefs.getString(ChooPref.ACTUAL_ARRIVAL,null) == null)
        {
            dtActual.setText(date+"");
        }else{
            dtActual.setText(prefs.getString(ChooPref.ACTUAL_ARRIVAL,null));
        }
        if(prefs.getString(ChooPref.IS_ICE_USER,null) != null)
        {
            if(prefs.getString(ChooPref.IS_ICE_USER,null).equals("true"))
            {
                iceUser.setChecked(true);
                isICEuser = true;
            }else if(prefs.getString(ChooPref.IS_ICE_USER,null).equals("false")){
                iceUser.setChecked(false);
                isICEuser = false;
            }
        }
        iceUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                isICEuser = isChecked;
            }
        });
        dtPlanned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeStatus = 0 ;
                Log.e("","dtPlanned"+dtPlanned.getText()+ " "+dtPlanned.getText().toString().length());
//                13/08/15 12:27 PM
                String[] DateTimeAry = dtPlanned.getText().toString().split(" ");
                Log.e("DateTimeAry[0]",""+DateTimeAry[0]);
                String[] DateArray = DateTimeAry[0].split("\\.");
                Log.e("DateArray",""+DateArray[0]);
                String[] TimeArray = DateTimeAry[1].split(":");
                Date myDate;
                Calendar cal = Calendar.getInstance();
                Log.e("", "" + DateArray[0] + "." + DateArray[1] + "." + DateArray[2]);
                cal.set(Calendar.DATE, Integer.parseInt(DateArray[0]));
                cal.set(Calendar.MONTH, Integer.parseInt(DateArray[1])- 1);
//                cal.set(Calendar.YEAR, Integer.parseInt(DateArray[2]));

                cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(TimeArray[0]));
                cal.set(Calendar.MINUTE, Integer.parseInt(TimeArray[1]));

                myDate = cal.getTime();
                SimpleDateTimePicker simpleDateTimePicker = SimpleDateTimePicker.make(
                        "Set Date & Time",
                        myDate,
                        RefundCheck.this,
                        getActivity().getSupportFragmentManager()
                );
                // Show It baby!
                simpleDateTimePicker.show();
            }
        });

        dtActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeStatus = 1 ;
                Log.e("","dtActual"+dtActual.getText()+ " "+dtActual.getText().toString().length());
//                13/08/15 12:27 PM
                String[] DateTimeAry = dtActual.getText().toString().split(" ");
                Log.e("DateTimeAry[0]",""+DateTimeAry[0]);
                String[] DateArray = DateTimeAry[0].split("\\.");
                Log.e("DateArray",""+DateArray[0]);
                String[] TimeArray = DateTimeAry[1].split(":");
                Date myDate;
                Calendar cal = Calendar.getInstance();
                Log.e("", "" + DateArray[0] + "." + DateArray[1] + "." + DateArray[2]);
                cal.set(Calendar.DATE, Integer.parseInt(DateArray[0]));
                cal.set(Calendar.MONTH, Integer.parseInt(DateArray[1])- 1);
//                cal.set(Calendar.YEAR, Integer.parseInt(DateArray[2]));

                cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(TimeArray[0]));
                cal.set(Calendar.MINUTE,Integer.parseInt(TimeArray[1]));
//                if(DateTimeAry[2].equals("PM"))
//                cal.set(Calendar.AM_PM,0);
//                else
//                    cal.set(Calendar.AM_PM,1);
                myDate = cal.getTime();
                SimpleDateTimePicker simpleDateTimePicker = SimpleDateTimePicker.make(
                        "Set Date & Time",
                        myDate,
                        RefundCheck.this,
                        getActivity().getSupportFragmentManager()
                );
                // Show It baby!
                simpleDateTimePicker.show();
            }
        });

        selectStation.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                try{
                    Log.d("", "*** Search value changed: " + s.toString());
                    String text = selectStation.getText().toString().toLowerCase(Locale.getDefault());
                    dataAdapter.getFilter().filter(text);
                    selectStation.setSelection(selectStation.getText().toString().length());
                    selectStation.setCursorVisible(true);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        selectStation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    selectStation.setSelection(selectStation.getText().length());
                    selectStation.setCursorVisible(true);
//                    destText.setTextColor(Color.parseColor("#309DC5"));
//                    dataAdapter = new StationListAdapter(getActivity(), arystationList, res);
//                    lststationlist.setAdapter(dataAdapter);
//                    lststationlist.setVisibility(View.VISIBLE);
                }

            }
        });
        selectStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destText.setTextColor(Color.parseColor("#309DC5"));
                dataAdapter = new StationListAdapter(getActivity(),arystationList, res);
                lststationlist.setAdapter(dataAdapter);
                lststationlist.setVisibility(View.VISIBLE);
                selectStation.setSelection(selectStation.getText().toString().length());
                selectStation.setCursorVisible(true);
            }
        });
        lststationlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectStation.setText(dataAdapter.
                            filterArray().get(position).getStationName());
                } catch (Exception e) {
                    selectStation.setText(arystationList.get(position).getStationName());
                }
                lststationlist.setVisibility(View.GONE);
                destText.setTextColor(Color.parseColor("#000000"));
                selectStation.setSelection(selectStation.getText().toString().length());
                selectStation.setCursorVisible(true);
            }
        });
        selectStation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    destText.setTextColor(Color.parseColor("#000000"));
                    lststationlist.setVisibility(View.GONE);
//                    lststationlist.setSelection(transitionStation.getText().toString().length());
                    selectStation.setCursorVisible(false);
                }
                return false;
            }
        });
        checkNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int plannedyear = 0;
                int plannedmonth = 0;
                int planneddate = 0;
                int plannedhours = 0;
                int plannedminutes = 0;

                int actualyear = 0;
                int actualmonth = 0;
                int actualdate = 0;
                int actualhours = 0;
                int actualminutes = 0;

                int remainingyear = 0;
                int remainingmonth = 0;
                int remainingdate = 0;
                int remaininghours = 0;
                int remainingminutes = 0;



//                CharSequence ampm = dtPlanned.getText().subSequence(17, 19);
//                Log.e("ampm", ampm + "");
                CharSequence hours = dtPlanned.getText().subSequence(9, 11);
                Log.e("hours", hours + "");
                CharSequence minutes = dtPlanned.getText().subSequence(12, 14);
                Log.e("minutes", minutes + "");

                plannedyear = Integer.parseInt((dtPlanned.getText().subSequence(6,8)).toString());
                Log.e("plannedyear", "" + plannedyear);
                plannedmonth = Integer.parseInt((dtPlanned.getText().subSequence(3,5)).toString());
                Log.e("plannedmonth", "" + plannedmonth);
                planneddate = Integer.parseInt((dtPlanned.getText().subSequence(0,2)).toString());
                Log.e("planneddate", "" + planneddate);
                plannedhours = Integer.parseInt(hours.toString().trim());
                Log.e("plannedhours", "" + plannedhours);
                plannedminutes = Integer.parseInt(minutes.toString().trim());
                Log.e("plannedminutes", "" + plannedminutes);

                Log.e("", "PLANNED" + dtPlanned.getText() + "ACTuAL" + dtActual.getText() + "STATION" + selectStation.getText());

                CharSequence hours1 = dtActual.getText().subSequence(9, 11);
                Log.e("hours1", hours1 + "");
                CharSequence minutes1 = dtActual.getText().subSequence(12, 14);
                Log.e("minutes1", minutes1 + "");
//                CharSequence ampm1 = dtActual.getText().subSequence(17, 19);
//                Log.e("ampm1", ampm1 + "");

                actualyear = Integer.parseInt((dtActual.getText().subSequence(6,8)).toString());
                Log.e("actualyear", "" + actualyear);
                actualmonth = Integer.parseInt((dtActual.getText().subSequence(3,5)).toString());
                Log.e("actualmonth", "" + actualmonth);
                actualdate = Integer.parseInt((dtActual.getText().subSequence(0,2)).toString());
                Log.e("actualdate", "" + actualdate);
                actualhours = Integer.parseInt(hours1.toString().trim());
                Log.e("actualhours", "" + actualhours);
                actualminutes = Integer.parseInt(minutes1.toString());
                Log.e("actualminutes", "" + actualminutes);
//                --Subtraction--
                remainingyear = actualyear - plannedyear;
                Log.e("remainingyear", "" + remainingyear);
                remainingmonth = actualmonth - plannedmonth;
                Log.e("remainingmonth", "" + remainingmonth);
                remainingdate = actualdate - planneddate;
                Log.e("remainingdate", "" + remainingdate);
                remaininghours = actualhours - plannedhours;
                Log.e("remaininghours", "" + remaininghours);
                remainingminutes = actualminutes - plannedminutes;
                Log.e("remainingminutes", "" + remainingminutes);
                totalminutes = (remainingyear * 525949) + (remainingmonth * 43829) + (remainingdate * 1440) + (remaininghours * 60) + remainingminutes;
                Log.e("totalminutes", "" + totalminutes);
                if (selectStation.getText().toString().equals("")) {
                    rootview.findViewById(R.id.st).startAnimation(shake);
                } else {
                    if (totalminutes <= 0) {
                        showCautionPopup(getActivity());
                        layout_MainMenu.getForeground().setAlpha( 220); // dim
                    } else if (totalminutes > 0 && totalminutes <= 29) {
                        showNoRefundPopup(getActivity());
                        layout_MainMenu.getForeground().setAlpha( 220); // dim
                    } else if(totalminutes > 29 && totalminutes <= 59)
                    {
                        if(isICEuser){
                            showRefundPopup(getActivity(),0);
                            layout_MainMenu.getForeground().setAlpha( 220); // dim
                        }else{
                            showNoRefundPopup(getActivity());
                            layout_MainMenu.getForeground().setAlpha( 220); // dim
                        }
                    }
                    else if (totalminutes > 59 && totalminutes <= 119) {
                        showRefundPopup(getActivity(),25);
                        layout_MainMenu.getForeground().setAlpha( 220); // dim
                    } else {
                        showRefundPopup(getActivity(),50);
                        layout_MainMenu.getForeground().setAlpha( 220); // dim
                    }
                }
            }

        });

        return rootview;
    }

    // The method that displays the popup.
    private void showPopup(final Activity context) {
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            // Inflate the load_last_data_popup_layoutta_popup_layout.xml
            RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = layoutInflater.inflate(R.layout.load_last_data_popup_layout, null);

            // Creating the PopupWindow
            final PopupWindow popup = new PopupWindow(context);
            popup.setContentView(layout);
            popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            popup.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        popup.setFocusable(true);
            popup.showAtLocation(layout,
                    Gravity.CENTER | Gravity.CENTER, Gravity.CENTER, Gravity.CENTER);
            // Getting a reference to Close button, and close the popup when clicked.
            LinearLayout info = (LinearLayout) layout.findViewById(R.id.info);
            Button goOn = (Button) layout.findViewById(R.id.goon);
            Button newData = (Button) layout.findViewById(R.id.newdata);
            info.bringToFront();
            goOn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    popup.dismiss();
                    layout_MainMenu.getForeground().setAlpha(0); // restore
                }
            });
            newData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm");
                    String date = df.format(Calendar.getInstance().getTime());
                    editor.clear();
                    editor.commit();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    selectStation.setText("");
                    dtActual.setText(date);
                    dtPlanned.setText(date);
                    iceUser.setChecked(false);
                    popup.dismiss();
                    layout_MainMenu.getForeground().setAlpha(0); // restore
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // The method that displays the popup.
    private void showCautionPopup(final Activity context) {
        // Inflate the load_last_data_popup_layoutta_popup_layout.xml
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.caution_popup_layout, null);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        popup.setFocusable(true);
        popup.showAtLocation(layout,
                Gravity.CENTER|Gravity.CENTER, Gravity.CENTER,Gravity.CENTER);
        // Getting a reference to Close button, and close the popup when clicked.
        LinearLayout info = (LinearLayout) layout.findViewById(R.id.warning);
        Button ok = (Button) layout.findViewById(R.id.ok);
        info.bringToFront();
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
                layout_MainMenu.getForeground().setAlpha( 0); // restore
            }
        });
    }
    // The method that displays the popup.
    private void showNoRefundPopup(final Activity context) {
        // Inflate the load_last_data_popup_layoutta_popup_layout.xml
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.no_refund_popup_layout, null);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        popup.setFocusable(true);
        popup.showAtLocation(layout,
                Gravity.CENTER|Gravity.CENTER, Gravity.CENTER,Gravity.CENTER);
        // Getting a reference to Close button, and close the popup when clicked.
        LinearLayout info = (LinearLayout) layout.findViewById(R.id.hand);
        Button oktoobad = (Button) layout.findViewById(R.id.oktoobad);
        info.bringToFront();
        oktoobad.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
                layout_MainMenu.getForeground().setAlpha( 0); // restore
            }
        });
    }
    // The method that displays the popup.
    private void showRefundPopup(final Activity context,int percent) {
        // Inflate the load_last_data_popup_layoutta_popup_layout.xml
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.choo_check_result_popup_layout, null);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        popup.setFocusable(true);
        popup.showAtLocation(layout,
                Gravity.CENTER|Gravity.CENTER, Gravity.CENTER,Gravity.CENTER);
        // Getting a reference to Close button, and close the popup when clicked.
        LinearLayout info = (LinearLayout) layout.findViewById(R.id.check);
        TextView refundpercent = (TextView) layout.findViewById(R.id.percent);
        final TextView ice = (TextView)layout.findViewById(R.id.ice);
        Button reclaimmoney = (Button) layout.findViewById(R.id.reclaimmoney);
        Button checkagain = (Button)layout.findViewById(R.id.checkagain);
        if(isICEuser)
        {
            if(percent == 25)
            {
                refundpercent.setText(R.string.l3);
                ice.setVisibility(View.VISIBLE);
                ice.setText(R.string.l4);
            }
            else if(percent == 50)
            {
                refundpercent.setText(R.string.l5);
                ice.setVisibility(View.VISIBLE);
                ice.setText(R.string.l6);
            }
            else if(percent == 0)
            {
                refundpercent.setText(R.string.l7);
            }
        }else{
            if(percent == 25)
            {
                refundpercent.setText(R.string.l8);
            }
            else if(percent == 50)
            {
                refundpercent.setText(R.string.l9);
            }
        }

        info.bringToFront();
        checkagain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
                layout_MainMenu.getForeground().setAlpha( 0); // restore
            }
        });
        reclaimmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ArrivalTrainFragment fragment = new ArrivalTrainFragment();
                fragmentTransaction.replace(R.id.realtabcontent,fragment,"ArrivalTrain").addToBackStack(null);
                fragmentTransaction.commit();
                editor.putString(ChooPref.STATION, selectStation.getText().toString());
                editor.putString(ChooPref.PLANED_ARRIVAL,dtPlanned.getText().toString());
                editor.putString(ChooPref.ACTUAL_ARRIVAL,dtActual.getText().toString());
                editor.putString(ChooPref.IS_ICE_USER,isICEuser+"");
                editor.putInt(ChooPref.MINUTES_DELAYED,totalminutes);
                editor.commit();
                popup.dismiss();
                layout_MainMenu.getForeground().setAlpha( 0); // restore
            }
        });
    }
    @Override
    public void DateTimeSet(Date date) {
        DateTime mDateTime = new DateTime(date);
        // Show in the LOGCAT the selected Date and Time
        Log.v("TEST_TAG", "Date and Time selected: " + mDateTime.getDateString());
        if(TimeStatus == 0){
            dtPlanned.setText(mDateTime.getDateString());

        }else if(TimeStatus == 1){
            dtActual.setText(mDateTime.getDateString());
        }

    }
    public String  getFormattedDateTime(String value){
        String SplittedDateTime[] = value.split(" ");
        String Date = SplittedDateTime[0];
        String Time = SplittedDateTime[1];
        String SplittedDate[] = Date.split("-");
        String finalDate = SplittedDate[2] +"/" + SplittedDate[1] + "/" + SplittedDate[0].substring(1,2);
        Log.e("Date"+Date,"Time"+Time);
        String finalTime =  "";
        try {
            String _24HourTime = Time;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            Log.e("","_12HourSDF.format(_24HourDt)"+_12HourSDF.format(_24HourDt));
            System.out.println(_12HourSDF.format(_24HourDt));
            finalTime = _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return finalDate + finalTime;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments </a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refund_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}

