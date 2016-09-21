package com.paras.choo.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.paras.choo.R;
import com.paras.choo.adapters.StationListAdapter;
import com.paras.choo.beans.StationItem;
import com.paras.choo.utils.ChooPref;
import com.paras.choo.utils.ChooSaveDataPref;
import com.paras.choo.utils.DateTime;
import com.paras.choo.utils.DateTimePicker;
import com.paras.choo.utils.SimpleDateTimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DepartureStationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DepartureStationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DepartureStationFragment extends Fragment implements DateTimePicker.OnDateTimeSetListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootview;
    EditText selectStation;
    ImageView progressbar, left, right;
    TextView PlannedDeparture,destText;
    SharedPreferences prefs;
    SharedPreferences prefs1;
    SharedPreferences prefs2;
    Animation shake;
    Resources res;
    ListView lststationlist;
    Switch traschange, bahncarduse;
    LinearLayout ticketprice;
    Boolean isbahnused = false;
    Boolean istransitionchange = false;
    ArrayList<StationItem> arystationList;
    StationListAdapter dataAdapter = null;
    TextView tickettrippricetext;
    EditText selectprice;
    FrameLayout layout_MainMenu;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor1, editor2;
    String newString;
    TextView heading, subline1, subline2, dtplannedtext, transchangetext, bahncardusedtext;
    Typeface headlinefont, sublinefont, fieldsfont;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter dot1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DepartureStationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DepartureStationFragment newInstance(String param1, String param2) {
        DepartureStationFragment fragment = new DepartureStationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DepartureStationFragment() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_departure_station, container, false);
        layout_MainMenu = (FrameLayout) getActivity().findViewById( R.id.realtabcontent);
        layout_MainMenu.getForeground().setAlpha( 0);
        headlinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");
        fieldsfont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        sublinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        progressbar = (ImageView)rootview.findViewById(R.id.progress1);
        prefs = getActivity().getSharedPreferences(ChooPref.CHOO_PREF, getActivity().MODE_PRIVATE);
        prefs1 = getActivity().getSharedPreferences(ChooSaveDataPref.CHOO_SAVEDATA_PREF, getActivity().MODE_PRIVATE);
        prefs2 = getActivity().getSharedPreferences("price", getActivity().MODE_PRIVATE);
        left = (ImageView)rootview.findViewById(R.id.left);
        bahncarduse = (Switch)rootview.findViewById(R.id.bahncardused);
        traschange = (Switch)rootview.findViewById(R.id.change);
        ticketprice = (LinearLayout)rootview.findViewById(R.id.tripPrice);
        lststationlist = (ListView) rootview.findViewById(R.id.stationlist);
        selectStation = (EditText)rootview.findViewById(R.id.selectStation);
        right = (ImageView)rootview.findViewById(R.id.right);
        destText = (TextView)rootview.findViewById(R.id.destText);
        heading = (TextView)rootview.findViewById(R.id.heading);
        subline1 = (TextView)rootview.findViewById(R.id.subline1);
        subline2 = (TextView)rootview.findViewById(R.id.subline2);
        dtplannedtext = (TextView)rootview.findViewById(R.id.dtplannedtext);
        transchangetext = (TextView)rootview.findViewById(R.id.transchangetext);
        bahncardusedtext = (TextView)rootview.findViewById(R.id.bahncardusedtext);
        editor = prefs.edit();
        editor1 = prefs1.edit();
        editor2 = prefs2.edit();
        tickettrippricetext = (TextView)rootview.findViewById(R.id.tickettripprice);
        selectprice = (EditText)rootview.findViewById(R.id.selectprice);
        PlannedDeparture = (TextView)rootview.findViewById(R.id.dtplanned);
        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        heading.setTypeface(headlinefont);
        subline1.setTypeface(sublinefont);
        subline2.setTypeface(sublinefont);
        destText.setTypeface(fieldsfont);
        selectStation.setTypeface(fieldsfont);
        dtplannedtext.setTypeface(fieldsfont);
        PlannedDeparture.setTypeface(fieldsfont);
        transchangetext.setTypeface(fieldsfont);
        bahncardusedtext.setTypeface(fieldsfont);
        tickettrippricetext.setTypeface(fieldsfont);
        selectprice.setTypeface(fieldsfont);
        res = getResources();
        arystationList = new ArrayList<StationItem>();


        String[] station = res.getStringArray(R.array.station);
        for(int i = 0;i < station.length; i++){
            arystationList.add(new StationItem(station[i]));
        }
        selectprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(selectprice.getText().length() == 0)
                {
                    selectprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                }else{}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if(prefs.getString(ChooPref.PLANNED_DEPARTURE,null) == null) {

            Log.e("Planned Arrival"," "+prefs.getString(ChooPref.PLANED_ARRIVAL, null));
//                13/08/15 12:27 PM
            String[] DateTimeAry = prefs.getString(ChooPref.PLANED_ARRIVAL, null).split(" ");
            Log.e("DateTimeAry[0]",""+DateTimeAry[0]);
            String[] DateArray = DateTimeAry[0].split("\\.");
            Log.e("DateArray",""+DateArray[0]);
            String[] TimeArray = DateTimeAry[1].split(":");
            Date myDate;
            Calendar cal = Calendar.getInstance();
            Log.e("", "" + DateArray[0] + "." + DateArray[1] + "." + DateArray[2]);
            cal.set(Calendar.DATE, Integer.parseInt(DateArray[0]));
            cal.set(Calendar.MONTH, Integer.parseInt(DateArray[1])- 1);
            cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(TimeArray[0])-1);
            cal.set(Calendar.MINUTE,Integer.parseInt(TimeArray[1]));
            myDate = cal.getTime();
            DateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm");
            PlannedDeparture.setText(df.format(myDate));
        }
        else{
            PlannedDeparture.setText(prefs.getString(ChooPref.PLANNED_DEPARTURE, null));
        }
        if(prefs.getString(ChooPref.SELECT_PRICE,null) != null)
        {
            if(prefs2.getInt("selectprice", 0) != 0)
            {
                selectprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(prefs2.getInt("selectprice", 0))});
            }
            selectprice.setText(prefs.getString(ChooPref.SELECT_PRICE,null));

        }
        if(prefs.getString(ChooPref.DEPARTURE_STATION,null) != null)
        {
            selectStation.setText(prefs.getString(ChooPref.DEPARTURE_STATION,null));
        }
        if(prefs1.getBoolean(ChooSaveDataPref.IS_BAHN_TIME_CARD,false))
        {
            bahncarduse.setChecked(true);
            isbahnused = true;
            ticketprice.setVisibility(View.GONE);
            selectprice.setText("0.00");
        }else{
            isbahnused = false;
            bahncarduse.setChecked(false);
            ticketprice.setVisibility(View.VISIBLE);
//            selectprice.setText("\u20B9 0.00");
        }
        if(prefs.getBoolean(ChooPref.IS_TRANSITION,false))
        {
            istransitionchange = true;
            traschange.setChecked(true);

        }else{
            istransitionchange = false;
            traschange.setChecked(false);
        }
        PlannedDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlannedDeparture.setTextColor(Color.parseColor("#dbdbdb"));
                SimpleDateTimePicker simpleDateTimePicker = SimpleDateTimePicker.make(
                        "Set Date & Time Title",
                        new Date(),
                        DepartureStationFragment.this,
                        getActivity().getSupportFragmentManager()
                );
                // Show It baby!
                simpleDateTimePicker.show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressbar.setImageResource(R.mipmap.progress_bar2);
            }
        }, 1000);
        bahncarduse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isbahnused = isChecked;
                if (isChecked) {
                    ticketprice.setVisibility(View.GONE);
                    selectprice.setText("0.00");
                } else {
                    ticketprice.setVisibility(View.VISIBLE);
//                    selectprice.setText("\u20B9 0.00");
                }
            }
        });
        traschange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                istransitionchange = isChecked;
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        selectprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectprice.setText("");
                selectprice.setSelection(selectprice.getText().length());
                selectprice.setCursorVisible(true);

            }
        });
        selectprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                selectprice.setText("");
                selectprice.setSelection(selectprice.getText().length());
                selectprice.setCursorVisible(true);
                tickettrippricetext.setTextColor(Color.parseColor("#309DC5"));
            }
        });

        selectprice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    String s = selectprice.getText().toString().replaceAll("\\." , ",");
                    if(s.length() == 0)
                    {}
                    else if(s.length() == 1)
                    {
                        selectprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                        if(s.contains("/u20ac"))
                        {
                            if(s.contains(","))
                            {
                            }
                            else{
                                selectprice.setText(s + ",00");
                            }
                        }
                        else{
                            if(s.contains(","))
                            {
                                selectprice.setText("\u20ac " + s);
                            }
                            else{
                                selectprice.setText("\u20ac " + s + ",00");
                            }

                        }
//                        selectprice.setText("\u20ac " + selectprice.getText().toString() + ",00");
                        editor2.putInt("selectprice", 6);
                        editor2.commit();
                    }
                    else if(s.length() == 2)
                    {
                        selectprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
                        if(s.contains("/u20ac"))
                        {
                            if(s.contains(","))
                            {
                            }
                            else{
                                selectprice.setText(s + ",00");
                            }
                        }
                        else{
                            if(s.contains(","))
                            {
                                selectprice.setText("\u20ac " + s);
                            }
                            else{
                                selectprice.setText("\u20ac " + s + ",00");
                            }

                        }
//                        selectprice.setText("\u20ac " + s + ",00");
                        editor2.putInt("selectprice", 7);
                        editor2.commit();
                    }
                    else if(s.length() == 3)
                    {
                        selectprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                        if(s.contains("/u20ac"))
                        {
                            if(s.contains(","))
                            {
                            }
                            else{
                                selectprice.setText(s + ",00");
                            }
                        }
                        else{
                            if(s.contains(","))
                            {
                                selectprice.setText("\u20ac " + s);
                            }
                            else{
                                selectprice.setText("\u20ac " + s + ",00");
                            }

                        }
//                        selectprice.setText("\u20ac " + s + ",00");
                        editor2.putInt("selectprice", 8);
                        editor2.commit();
                    }
                    else if(s.length() == 4)
                    {
                        selectprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
                        if(s.contains("/u20ac"))
                        {
                            if(s.contains(","))
                            {
                            }
                            else{
                                String x = s;
                                x = x.substring(0, 2) + "," + x.substring(2, x.length());
                                selectprice.setText(x);
                            }
                        }
                        else{
                            if(s.contains(","))
                            {
                                String x = s;
                                x = "\u20ac " + x.substring(0, 2) + "," + x.substring(3, 4) + "0";
                                selectprice.setText(x);
                            }
                            else{
                                String x = s;
                                x = "\u20ac " + x.substring(0, 2) + "," + x.substring(2, x.length());
                                selectprice.setText(x);
                            }

                        }

                        editor2.putInt("selectprice",7);
                        editor2.commit();
                    }
                    else if(s.length() == 5) {
                        selectprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                        if(s.contains("/u20ac"))
                        {
                            if(s.contains(","))
                            {
                            }
                            else{
                                String x = s;
                                x = x.substring(0, 3) + "," + x.substring(3, x.length());
                                selectprice.setText(x);
                            }
                        }
                        else{
                            if(s.contains(","))
                            {
                                selectprice.setText("\u20ac " + s);
                            }
                            else{
                                String x = s;
                                x = "\u20ac " + x.substring(0, 3) + "," + x.substring(3, x.length());
                                selectprice.setText(x);
                            }

                        }
//                        String x = s;
//                        x = "\u20ac " + x.substring(0, 3) + "," + x.substring(3, x.length());
//                        selectprice.setText(x);
                        editor2.putInt("selectprice",8);
                        editor2.commit();
                    }
                    else if(s.length() == 6) {
                        selectprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                        if(s.contains("/u20ac"))
                        {
                            if(s.contains(","))
                            {
                            }
                            else{
                                String x = s;
                                x = x.substring(0, 3) + "," + x.substring(3, x.length());
                                selectprice.setText(x);
                            }
                        }
                        else{
                            if(s.contains(","))
                            {
                                selectprice.setText("\u20ac " + s);
                            }
                            else{
                                String x = s;
                                x = "\u20ac " + x.substring(0, 3) + "," + x.substring(3, x.length());
                                selectprice.setText(x);
                            }

                        }
//                        String x = s;
//                        x = "\u20ac " + x.substring(0, 3) + "," + x.substring(3, x.length());
//                        selectprice.setText(x);
                        editor2.putInt("selectprice",8);
                        editor2.commit();
                    }
//                    selectprice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});

//                        if (s.toString().contains(".")) {
//                            newString = s.toString().replace(".", ",");
//                        } else if(s.toString().contains(",")){
//                                newString = s.toString();
//                            } else {
//                                newString = s.toString() + ",00";
//                            }
//
//                    if(s.toString().contains("\u20ac"))
//                    {}else{
//                        newString = "\u20ac " + s.toString();
//                    }
//                        selectprice.setText(newString);
                    tickettrippricetext.setTextColor(Color.parseColor("#000000"));
                    selectprice.setSelection(selectprice.getText().length());
                    selectprice.setCursorVisible(false);
                }
                return false;
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
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

                int totalminutes = 0;

//                CharSequence ampm = PlannedDeparture.getText().subSequence(17, 19);
//                Log.e("ampm", ampm + "");
                CharSequence hours = PlannedDeparture.getText().subSequence(9, 11);
                Log.e("hours", hours + "");
                CharSequence minutes = PlannedDeparture.getText().subSequence(12, 14);
                Log.e("minutes", minutes + "");

                plannedyear = Integer.parseInt((PlannedDeparture.getText().subSequence(6, 8)).toString());
                Log.e("plannedyear", "" + plannedyear);
                plannedmonth = Integer.parseInt((PlannedDeparture.getText().subSequence(3, 5)).toString());
                Log.e("plannedmonth", "" + plannedmonth);
                planneddate = Integer.parseInt((PlannedDeparture.getText().subSequence(0, 2)).toString());
                Log.e("planneddate", "" + planneddate);
                plannedhours = Integer.parseInt(hours.toString().trim());
                Log.e("plannedhours", "" + plannedhours);
                plannedminutes = Integer.parseInt(minutes.toString());
                Log.e("plannedminutes", "" + plannedminutes);

                Log.e("", "PLANNED" + PlannedDeparture.getText() + "PLANED_ARRIVAL" + prefs.getString(ChooPref.PLANED_ARRIVAL, null));

                CharSequence hours1 = prefs.getString(ChooPref.PLANED_ARRIVAL, null).subSequence(9, 11);
                Log.e("hours1", hours1 + "");
                CharSequence minutes1 = prefs.getString(ChooPref.PLANED_ARRIVAL, null).subSequence(12, 14);
                Log.e("minutes1", minutes1 + "");
//                CharSequence ampm1 = prefs.getString(ChooPref.ACTUAL_ARRIVAL, null).subSequence(17, 19);
//                Log.e("ampm1", ampm1 + "");

                actualyear = Integer.parseInt((prefs.getString(ChooPref.PLANED_ARRIVAL, null).subSequence(6, 8)).toString());
                Log.e("actualyear", "" + actualyear);
                actualmonth = Integer.parseInt((prefs.getString(ChooPref.PLANED_ARRIVAL, null).subSequence(3, 5)).toString());
                Log.e("actualmonth", "" + actualmonth);
                actualdate = Integer.parseInt((prefs.getString(ChooPref.PLANED_ARRIVAL, null).subSequence(0, 2)).toString());
                Log.e("actualdate", "" + actualdate);
                actualhours = Integer.parseInt(hours1.toString());
                Log.e("actualhours", "" + actualhours);
                actualminutes = Integer.parseInt(minutes1.toString());
                Log.e("actualminutes", "" + actualminutes);
//               --Subtraction--
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

                editor.putString(ChooPref.DEPARTURE_STATION, selectStation.getText().toString());
                editor.putString(ChooPref.PLANNED_DEPARTURE, PlannedDeparture.getText().toString());
                if(!isbahnused) {
                    editor.putString(ChooPref.SELECT_PRICE, selectprice.getText().toString());
                }
                editor.putBoolean(ChooPref.IS_TRANSITION, istransitionchange);
                editor.commit();

                editor1.putBoolean(ChooSaveDataPref.IS_BAHN_TIME_CARD, isbahnused);
                editor1.commit();

                String[] DateTimeAry = prefs.getString(ChooPref.PLANED_ARRIVAL, null).split(" ");
                Log.e("DateTimeAry[0]",""+DateTimeAry[0]);
                String[] DateArray = DateTimeAry[0].split("\\.");
                Log.e("DateArray",""+DateArray[0]);
                String[] TimeArray = DateTimeAry[1].split(":");
                Date myDate;
                Calendar cal = Calendar.getInstance();
                Log.e("", "" + DateArray[0] + "." + DateArray[1] + "." + DateArray[2]);
                cal.set(Calendar.DATE, Integer.parseInt(DateArray[0]));
                cal.set(Calendar.MONTH, Integer.parseInt(DateArray[1])- 1);
                cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(TimeArray[0]));
                cal.set(Calendar.MINUTE,Integer.parseInt(TimeArray[1]));
                myDate = cal.getTime();

                DateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm");
                if (!CheckDates(PlannedDeparture.getText().toString(),df.format(myDate))) {
                    showDepCautionPopup(getActivity());
                    layout_MainMenu.getForeground().setAlpha(220); // dim

                } else if (selectStation.getText().toString().equals("")) {
                    if (selectprice.getText().toString().equals("")) {
                        rootview.findViewById(R.id.tripPrice).startAnimation(shake);
                        rootview.findViewById(R.id.st).startAnimation(shake);
                    } else {
                        rootview.findViewById(R.id.st).startAnimation(shake);
                    }

                } else if (selectprice.getText().toString().equals("")) {
                    rootview.findViewById(R.id.tripPrice).startAnimation(shake);
                } else {
                    if (istransitionchange) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        TransitionInfoFragment fragment = new TransitionInfoFragment();
                        fragmentTransaction.replace(R.id.realtabcontent, fragment, "DepartureStation").addToBackStack(null);
                        fragmentTransaction.commit();
                    } else if (isbahnused) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        BahnTimeCardFragment fragment = new BahnTimeCardFragment();
                        fragmentTransaction.replace(R.id.realtabcontent, fragment, "BahnTimeCard").addToBackStack(null);
                        fragmentTransaction.commit();
//                        Toast.makeText(getActivity().getApplicationContext(), "BAHN FRAGMENT", Toast.LENGTH_SHORT).show();
                    } else {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        PersonalAndBankDataFragment fragment = new PersonalAndBankDataFragment();
                        fragmentTransaction.replace(R.id.realtabcontent, fragment, "PersonalAndBankData").addToBackStack(null);
                        fragmentTransaction.commit();
//                        Toast.makeText(getActivity().getApplicationContext(), "Personal and Bank Data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        selectStation.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                try {
                    Log.d("", "*** Search value changed: " + s.toString());
                    String text = selectStation.getText().toString().toLowerCase(Locale.getDefault());
                    dataAdapter.getFilter().filter(text);
                    selectStation.setSelection(selectStation.getText().toString().length());
                    selectStation.setCursorVisible(true);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        selectStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destText.setTextColor(Color.parseColor("#309DC5"));
                dataAdapter = new StationListAdapter(getActivity(), arystationList, res);
                lststationlist.setAdapter(dataAdapter);
                lststationlist.setVisibility(View.VISIBLE);
                selectStation.setSelection(selectStation.getText().toString().length());
                selectStation.setCursorVisible(true);
            }
        });
        selectStation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    selectStation.setSelection(selectStation.getText().toString().length());
                    selectStation.setCursorVisible(true);
                    destText.setTextColor(Color.parseColor("#309DC5"));
                    dataAdapter = new StationListAdapter(getActivity(), arystationList, res);
                    lststationlist.setAdapter(dataAdapter);
                    lststationlist.setVisibility(View.VISIBLE);
                }

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
                    lststationlist.setVisibility(View.GONE);
                    destText.setTextColor(Color.parseColor("#000000"));
//                    lststationlist.setSelection(transitionStation.getText().toString().length());
                    selectStation.setCursorVisible(false);
                }
                return false;
            }
        });
        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    // The method that displays the popup.
    private void showDepCautionPopup(final Activity context) {
        // Inflate the load_last_data_popup_layoutta_popup_layout.xml
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.depcaution, null);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_departure_train_info, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void DateTimeSet(Date date) {
        DateTime mDateTime = new DateTime(date);
        // Show in the LOGCAT the selected Date and Time
        Log.v("TEST_TAG", "Date and Time selected: " + mDateTime.getDateString());
        PlannedDeparture.setText(mDateTime.getDateString());
        PlannedDeparture.setTextColor(Color.parseColor("#309DC5"));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }

    }
    public boolean CheckDates(String startDate, String endDate) {

        SimpleDateFormat dfDate = new SimpleDateFormat("dd.MM.yy HH:mm");
        Log.e("startDate",startDate);
        Log.e("endDate",endDate);
        boolean b = false;

        try {
            if (dfDate.parse(startDate).before(dfDate.parse(endDate))) {
                b = true;  // If start date is before end date.
            } else {
                b = false; // If start date is after the end date.
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("b",""+b);
        return b;
    }

}
