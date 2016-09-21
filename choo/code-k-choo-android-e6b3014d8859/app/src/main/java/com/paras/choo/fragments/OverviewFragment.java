package com.paras.choo.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.provider.MediaStore;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
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
import com.paras.choo.utils.ImageHelper2;
import com.paras.choo.utils.SimpleDateTimePicker;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OverviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment implements DateTimePicker.OnDateTimeSetListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView progressbar, left, right, img;
    SharedPreferences.Editor editor;
    SharedPreferences prefs, prefs2;
    SharedPreferences.Editor editor1,editor2;
    SharedPreferences prefs1;
    Boolean isBahn, isIce, isTrans;
    View rootview;
    int TimeStatus = 0 ;
    int value;
    AlertDialog levelDialog ;
    ArrayList<StationItem> arystationList;
    Switch iceSwitch, bahnSwitch, tranSwitch;
    EditText departureStation, destinationStation, trainNumber, cardNumber;
    TextView plannedDeparture, plannedArrival, actualArrival, trainType, cardType, expiryDate, dateofBirth, classtype;
    TextView txtSelectTitle;
    Resources res;
    ListView lststationlist,lststationlist1,lststationlist2,lststationlist3;
    StationListAdapter dataAdapter = null;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    int id;
    Animation shake;
    Typeface headlinefont, sublinefont, fieldsfont;
    TextView dtPlanned,trainTypeTrans, selectStationtext, transitionStationtext;
    EditText selectStation, transitionStation, trainNumValue;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    EditText firstname, name, email, street, housenumber, address, zipcode, city, company, accountnumber, bankcode, accountholder,selectprice;
    TextView heading, subline1, subline2, subline3, subline4, subline5, destText1, planneddeparttext, destText, plannedarrivaltext, actualarrivaltext,
            traintypetext, trainnumtext,icesprinttext, bahntext, cardtypetext, classtypetext, cardnumbertext, expdatetext, dobtext, tickettripprice,
            changetranstext, traintypetranstext, trainnumtexttrans, dtplannedtranstext, titletext, firstnametext, nametext, emailtext, streettext,
            housenumtext, addresstext, ziptext, citytext, companytext, accountnumtext, bankcodetext, accountholdertext;
    private OnFragmentInteractionListener mListener;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter dot1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OverviewFragment newInstance(String param1, String param2) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public OverviewFragment() {
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
        rootview = inflater.inflate(R.layout.fragment_overview, container, false);
        headlinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");
        fieldsfont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        sublinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        prefs = getActivity().getSharedPreferences(ChooSaveDataPref.CHOO_SAVEDATA_PREF, getActivity().MODE_PRIVATE);
        editor = prefs.edit();
        prefs1 = getActivity().getSharedPreferences(ChooPref.CHOO_PREF, getActivity().MODE_PRIVATE);
        editor1 = prefs1.edit();
        prefs2 = getActivity().getSharedPreferences("price", getActivity().MODE_PRIVATE);
        editor2 = prefs2.edit();
        left = (ImageView)rootview.findViewById(R.id.left);
        lststationlist = (ListView) rootview.findViewById(R.id.stationlist);
        lststationlist1 = (ListView) rootview.findViewById(R.id.stationlist1);
        lststationlist2 = (ListView) rootview.findViewById(R.id.stationlist2);
        lststationlist3 = (ListView) rootview.findViewById(R.id.stationlist3);
        lststationlist.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        lststationlist.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        lststationlist1.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        lststationlist2.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        lststationlist3.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        arystationList = new ArrayList<StationItem>();
        selectStationtext = (TextView)rootview.findViewById(R.id.selectStationtext);
        selectStation = (EditText)rootview.findViewById(R.id.selectStationtrans);
        transitionStation = (EditText)rootview.findViewById(R.id.transitionStation);
        classtype = (TextView)rootview.findViewById(R.id.classtype);
        transitionStationtext = (TextView)rootview.findViewById(R.id.transitionStationtext);
        trainNumValue = (EditText)rootview.findViewById(R.id.trainnumvaluetrans);
        dtPlanned = (TextView)rootview.findViewById(R.id.dtplannedtrans);
        trainTypeTrans = (TextView)rootview.findViewById(R.id.traintypetrans);
        right = (ImageView)rootview.findViewById(R.id.right);
        progressbar = (ImageView)rootview.findViewById(R.id.progress6);
        departureStation = (EditText)rootview.findViewById(R.id.selectStation1);
        plannedDeparture = (TextView)rootview.findViewById(R.id.dtplanned1);
        destinationStation = (EditText)rootview.findViewById(R.id.selectStation);
        plannedArrival = (TextView)rootview.findViewById(R.id.dtplanned);
        actualArrival = (TextView)rootview.findViewById(R.id.dtactual);
        trainType = (TextView)rootview.findViewById(R.id.traintype);
        trainNumber = (EditText)rootview.findViewById(R.id.trainnumvalue);
        iceSwitch = (Switch)rootview.findViewById(R.id.iceswitch);
        bahnSwitch = (Switch)rootview.findViewById(R.id.bahnSwitch);
        cardType = (TextView)rootview.findViewById(R.id.cardType);
        cardNumber = (EditText)rootview.findViewById(R.id.cardnumber);
        expiryDate = (TextView)rootview.findViewById(R.id.expdate);
        dateofBirth = (TextView)rootview.findViewById(R.id.dob);
        img = (ImageView)rootview.findViewById(R.id.img);
        tranSwitch = (Switch)rootview.findViewById(R.id.transSwitch);
        txtSelectTitle = (TextView)rootview.findViewById(R.id.txtSelectTitle);
        firstname = (EditText)rootview.findViewById(R.id.firstname);
        name = (EditText)rootview.findViewById(R.id.name);
        email = (EditText)rootview.findViewById(R.id.email);
        street = (EditText)rootview.findViewById(R.id.street);
        housenumber = (EditText)rootview.findViewById(R.id.housenumber);
        address = (EditText)rootview.findViewById(R.id.address);
        zipcode = (EditText)rootview.findViewById(R.id.zipcode);
        city = (EditText)rootview.findViewById(R.id.city);
        company = (EditText)rootview.findViewById(R.id.company);
        accountnumber = (EditText)rootview.findViewById(R.id.accountnumber);
        bankcode = (EditText)rootview.findViewById(R.id.bankcode);
        selectprice= (EditText)rootview.findViewById(R.id.selectprice);
        accountholder = (EditText)rootview.findViewById(R.id.accountholder);
        heading = (TextView)rootview.findViewById(R.id.heading);
        subline1 = (TextView)rootview.findViewById(R.id.subline1);
        subline2 = (TextView)rootview.findViewById(R.id.subline2);
        subline3 = (TextView)rootview.findViewById(R.id.subline3);
        subline4 = (TextView)rootview.findViewById(R.id.subline4);
        subline5 = (TextView)rootview.findViewById(R.id.subline5);
        destText1 = (TextView)rootview.findViewById(R.id.destText1);
        planneddeparttext = (TextView)rootview.findViewById(R.id.planneddeparttext);
        destText = (TextView)rootview.findViewById(R.id.destText);
        plannedarrivaltext = (TextView)rootview.findViewById(R.id.plannedarrivaltext);
        actualarrivaltext = (TextView)rootview.findViewById(R.id.actualarrivaltext);
        traintypetext = (TextView)rootview.findViewById(R.id.traintypetext);
        trainnumtext = (TextView)rootview.findViewById(R.id.trainnumtext);
        icesprinttext = (TextView)rootview.findViewById(R.id.icesprinttext);
        bahntext = (TextView)rootview.findViewById(R.id.bahntext);
        cardtypetext = (TextView)rootview.findViewById(R.id.cardtypetext);
        classtypetext = (TextView)rootview.findViewById(R.id.classtypetext);
        cardnumbertext = (TextView)rootview.findViewById(R.id.cardnumbertext);
        expdatetext = (TextView)rootview.findViewById(R.id.expdatetext);
        dobtext = (TextView)rootview.findViewById(R.id.dobtext);
        tickettripprice = (TextView)rootview.findViewById(R.id.tickettripprice);
        changetranstext = (TextView)rootview.findViewById(R.id.changetranstext);
        traintypetranstext = (TextView)rootview.findViewById(R.id.traintypetranstext);
        trainnumtexttrans = (TextView)rootview.findViewById(R.id.trainnumtexttrans);
        dtplannedtranstext = (TextView)rootview.findViewById(R.id.dtplannedtranstext);
        titletext = (TextView)rootview.findViewById(R.id.titletext);
        firstnametext = (TextView)rootview.findViewById(R.id.firstnametext);
        nametext = (TextView)rootview.findViewById(R.id.nametext);
        emailtext = (TextView)rootview.findViewById(R.id.emailtext);
        streettext = (TextView)rootview.findViewById(R.id.streettext);
        housenumtext = (TextView)rootview.findViewById(R.id.housenumtext);
        addresstext = (TextView)rootview.findViewById(R.id.addresstext);
        ziptext = (TextView)rootview.findViewById(R.id.ziptext);
        citytext = (TextView)rootview.findViewById(R.id.citytext);
        companytext = (TextView)rootview.findViewById(R.id.companytext);
        accountnumtext = (TextView)rootview.findViewById(R.id.accountnumtext);
        bankcodetext = (TextView)rootview.findViewById(R.id.bankcodetext);
        accountholdertext = (TextView)rootview.findViewById(R.id.accountholdertext);

        heading.setTypeface(headlinefont);
        subline1.setTypeface(sublinefont);
        subline2.setTypeface(sublinefont);
        subline3.setTypeface(sublinefont);
        subline4.setTypeface(sublinefont);
        subline5.setTypeface(sublinefont);
        destText1.setTypeface(fieldsfont);
        departureStation.setTypeface(fieldsfont);
        planneddeparttext.setTypeface(fieldsfont);
        plannedDeparture.setTypeface(fieldsfont);
        destinationStation.setTypeface(fieldsfont);
        destText.setTypeface(fieldsfont);
        selectStation.setTypeface(fieldsfont);
        plannedarrivaltext.setTypeface(fieldsfont);
        plannedArrival.setTypeface(fieldsfont);
        actualarrivaltext.setTypeface(fieldsfont);
        actualArrival.setTypeface(fieldsfont);
        traintypetext.setTypeface(fieldsfont);
        trainType.setTypeface(fieldsfont);
        trainnumtext.setTypeface(fieldsfont);
        trainNumValue.setTypeface(fieldsfont);
        icesprinttext.setTypeface(fieldsfont);
        bahntext.setTypeface(fieldsfont);
        cardtypetext.setTypeface(fieldsfont);
        cardType.setTypeface(fieldsfont);
        classtype.setTypeface(fieldsfont);
        classtypetext.setTypeface(fieldsfont);
        cardnumbertext.setTypeface(fieldsfont);
        cardNumber.setTypeface(fieldsfont);
        expdatetext.setTypeface(fieldsfont);
        expiryDate.setTypeface(fieldsfont);
        dobtext.setTypeface(fieldsfont);
        dateofBirth.setTypeface(fieldsfont);
        tickettripprice.setTypeface(fieldsfont);
        selectprice.setTypeface(fieldsfont);
        changetranstext.setTypeface(fieldsfont);
        traintypetranstext.setTypeface(fieldsfont);
        trainTypeTrans.setTypeface(fieldsfont);
        trainnumtexttrans.setTypeface(fieldsfont);
        dtplannedtranstext.setTypeface(fieldsfont);
        dtPlanned.setTypeface(fieldsfont);
        selectStationtext.setTypeface(fieldsfont);
        selectStation.setTypeface(fieldsfont);
        transitionStationtext.setTypeface(fieldsfont);
        transitionStation.setTypeface(fieldsfont);
        titletext.setTypeface(fieldsfont);
        txtSelectTitle.setTypeface(fieldsfont);
        firstnametext.setTypeface(fieldsfont);
        firstname.setTypeface(fieldsfont);
        nametext.setTypeface(fieldsfont);
        name.setTypeface(fieldsfont);
        emailtext.setTypeface(fieldsfont);
        email.setTypeface(fieldsfont);
        streettext.setTypeface(fieldsfont);
        street.setTypeface(fieldsfont);
        housenumtext.setTypeface(fieldsfont);
        housenumber.setTypeface(fieldsfont);
        addresstext.setTypeface(fieldsfont);
        address.setTypeface(fieldsfont);
        ziptext.setTypeface(fieldsfont);
        zipcode.setTypeface(fieldsfont);
        citytext.setTypeface(fieldsfont);
        city.setTypeface(fieldsfont);
        company.setTypeface(fieldsfont);
        companytext.setTypeface(fieldsfont);
        accountnumtext.setTypeface(fieldsfont);
        accountnumber.setTypeface(fieldsfont);
        bankcodetext.setTypeface(fieldsfont);
        bankcode.setTypeface(fieldsfont);
        accountholdertext.setTypeface(fieldsfont);
        accountholder.setTypeface(fieldsfont);
        plannedArrival.setTypeface(fieldsfont);
        trainNumber.setTypeface(fieldsfont);

        departureStation.setText(prefs1.getString(ChooPref.DEPARTURE_STATION,null));
        plannedDeparture.setText(prefs1.getString(ChooPref.PLANNED_DEPARTURE,null));
        destinationStation.setText(prefs1.getString(ChooPref.STATION,null));
        plannedArrival.setText(prefs1.getString(ChooPref.PLANED_ARRIVAL,null));
        actualArrival.setText(prefs1.getString(ChooPref.ACTUAL_ARRIVAL,null));
        trainType.setText(prefs1.getString(ChooPref.TRAIN_TYPE,null));
        trainNumber.setText(prefs1.getString(ChooPref.TRAIN_NUMBER,null));
        if(prefs1.getString(ChooPref.SELECT_PRICE,null)!= null) {
            if (prefs2.getInt("selectprice", 0) != 0) {
                selectprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(prefs2.getInt("selectprice", 0))});
            }
            selectprice.setText(prefs1.getString(ChooPref.SELECT_PRICE, null));
        }
        res = getResources();
        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        final String[] station = res.getStringArray(R.array.station);
        for(int i = 0;i < station.length; i++){
            arystationList.add(new StationItem(station[i]));
        }
        if(prefs1.getBoolean(ChooPref.IS_TRANSITION,false)){
            rootview.findViewById(R.id.transInfo).setVisibility(View.VISIBLE);
            tranSwitch.setChecked(true);
        }
        else{
            rootview.findViewById(R.id.transInfo).setVisibility(View.GONE);
            tranSwitch.setChecked(false);
        }
        if(prefs1.getString(ChooPref.IS_ICE_USER,null).equals("true"))
        {
            iceSwitch.setChecked(true);
            isIce = true;
        }
        else{
            iceSwitch.setChecked(false);
            isIce = false;
        }

        if(prefs.getBoolean(ChooSaveDataPref.IS_BAHN_TIME_CARD, false)) {
            rootview.findViewById(R.id.bahnInfo).setVisibility(View.VISIBLE);
            rootview.findViewById(R.id.tripPrice).setVisibility(View.GONE);
            bahnSwitch.setChecked(true);
        }
        else{
            rootview.findViewById(R.id.bahnInfo).setVisibility(View.GONE);
            rootview.findViewById(R.id.tripPrice).setVisibility(View.VISIBLE);
            bahnSwitch.setChecked(false);
        }
        isTrans = prefs1.getBoolean(ChooPref.IS_TRANSITION,false);
        isBahn = prefs.getBoolean(ChooSaveDataPref.IS_BAHN_TIME_CARD,false);
        if(prefs.getString(ChooSaveDataPref.TYPE_OF_CARD,null) != null) {
            cardType.setText(prefs.getString(ChooSaveDataPref.TYPE_OF_CARD, null));
        }
        if(prefs.getString(ChooSaveDataPref.CARD_NUMBER,null) != null) {
            cardNumber.setText(prefs.getString(ChooSaveDataPref.CARD_NUMBER, null));
        }
        if(prefs.getString(ChooSaveDataPref.TYPE_OF_CLASS,null) != null) {
            classtype.setText(prefs.getString(ChooSaveDataPref.TYPE_OF_CLASS, null));
        }
        if(prefs.getString(ChooSaveDataPref.EXPIRE_DATE,null) != null) {
            expiryDate.setText(prefs.getString(ChooSaveDataPref.EXPIRE_DATE, null));
        }
        if(prefs.getString(ChooSaveDataPref.DATE_OF_BIRTH,null) != null) {
            dateofBirth.setText(prefs.getString(ChooSaveDataPref.DATE_OF_BIRTH, null));
        }
//        personal info
        txtSelectTitle.setText(prefs.getString(ChooSaveDataPref.TITLE,null));
        firstname.setText(prefs.getString(ChooSaveDataPref.FIRST_NAME,null));
        name.setText(prefs.getString(ChooSaveDataPref.NAME,null));
        email.setText(prefs.getString(ChooSaveDataPref.EMAIL,null));
        street.setText(prefs.getString(ChooSaveDataPref.STREET,null));
        housenumber.setText(prefs.getString(ChooSaveDataPref.HOUSE_NUMBER,null));
        address.setText(prefs.getString(ChooSaveDataPref.ADDRESS,null));
        zipcode.setText(prefs.getString(ChooSaveDataPref.ZIP_CODE,null));
        city.setText(prefs.getString(ChooSaveDataPref.CITY,null));
        company.setText(prefs.getString(ChooSaveDataPref.COMPANY,null));
        accountnumber.setText(prefs.getString(ChooSaveDataPref.ACCOUNT_NUMBER,null));
        bankcode.setText(prefs.getString(ChooSaveDataPref.BANK_CODE,null));
        accountholder.setText(prefs.getString(ChooSaveDataPref.ACCOUNT_HOLDER,null));
        selectprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (selectprice.getText().length() == 0) {
                    selectprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                } else {
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if(prefs1.getString(ChooPref.TRANS_PLANNED_DEPT,null) != null) {
            dtPlanned.setText(prefs1.getString(ChooPref.TRANS_PLANNED_DEPT, null));
        }
        else{
            dtPlanned.setText(prefs1.getString(ChooPref.PLANED_ARRIVAL, null));
        }
        if(prefs1.getString(ChooPref.TRANS_TRAIN_TYPE,null) != null)
        {
            trainTypeTrans.setText(prefs1.getString(ChooPref.TRANS_TRAIN_TYPE,null));
        }
        if(prefs1.getString(ChooPref.TRANS_TRAIN_NUMBER,null) != null)
        {
            trainNumValue.setText(prefs1.getString(ChooPref.TRANS_TRAIN_NUMBER,null));
        }
        if(prefs1.getString(ChooPref.TRANS_STATION,null) != null)
        {
            selectStation.setText(prefs1.getString(ChooPref.TRANS_STATION,null));
        }
        if(prefs1.getString(ChooPref.LAST_TRANSITION,null) != null)
        {
            transitionStation.setText(prefs1.getString(ChooPref.LAST_TRANSITION,null));
        }
        dtPlanned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeStatus = 4 ;
                dtPlanned.setTextColor(Color.parseColor("#dbdbdb"));

                SimpleDateTimePicker simpleDateTimePicker = SimpleDateTimePicker.make(
                        "Set Date & Time Title",
                        new Date(),
                        OverviewFragment.this,
                        getActivity().getSupportFragmentManager()
                );
                // Show It baby!
                simpleDateTimePicker.show();
            }
        });
        if( prefs1.getString(ChooPref.TICKET_IMAGE,null) != null ){
            byte[] b = Base64.decode(prefs1.getString(ChooPref.TICKET_IMAGE, null), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            img.setImageBitmap(bitmap);
        }
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        transitionStation.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                try {
                    Log.d("", "*** Search value changed: " + s.toString());
                    String text = transitionStation.getText().toString().toLowerCase(Locale.getDefault());
                    dataAdapter.getFilter().filter(text);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        transitionStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                transitionStationtext.setTextColor(Color.parseColor("#309DC5"));
                dataAdapter = new StationListAdapter(getActivity(), arystationList, res);
                lststationlist2.setAdapter(dataAdapter);
                lststationlist2.setVisibility(View.VISIBLE);
                transitionStation.setSelection(transitionStation.getText().toString().length());
                transitionStation.setCursorVisible(true);

            }
        });
        transitionStation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    transitionStationtext.setTextColor(Color.parseColor("#309DC5"));
                    dataAdapter = new StationListAdapter(getActivity(), arystationList, res);
                    lststationlist2.setAdapter(dataAdapter);
                    lststationlist2.setVisibility(View.VISIBLE);
                    transitionStation.setSelection(transitionStation.getText().toString().length());
                    transitionStation.setCursorVisible(true);
                }

            }
        });
        lststationlist2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                transitionStation.setText(arystationList.get(position).getStationName());
//                transitionStationtext.setTextColor(Color.parseColor("#000000"));


//                    editor.putString(ChooPref.STATION,arystationList.get(position).getStationName());
//                    editor.commit();
//                    Log.e("STATION",prefs.getString(ChooPref.STATION,null));
                lststationlist1.setVisibility(View.GONE);
                transitionStation.setSelection(transitionStation.getText().toString().length());
                transitionStation.setCursorVisible(false);

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
        selectStation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    selectStationtext.setTextColor(Color.parseColor("#309DC5"));
                    dataAdapter = new StationListAdapter(getActivity(), arystationList, res);
                    lststationlist3.setAdapter(dataAdapter);
                    lststationlist3.setVisibility(View.VISIBLE);
//                    selectStation.setSelection(selectStation.getText().toString().length());
//                    selectStation.setCursorVisible(true);
                } else {
//                    selectStation.setSelection(selectStation.getText().toString().length());
//                    selectStation.setCursorVisible(true);
                }

            }
        });

        selectStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectStationtext.setTextColor(Color.parseColor("#309DC5"));
                dataAdapter = new StationListAdapter(getActivity(), arystationList, res);
                lststationlist3.setAdapter(dataAdapter);
                lststationlist3.setVisibility(View.VISIBLE);
                selectStation.setSelection(selectStation.getText().toString().length());
                selectStation.setCursorVisible(true);
            }
        });
        lststationlist3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectStation.setText(arystationList.get(position).getStationName());
//                selectStationtext.setTextColor(Color.parseColor("#000000"));

//                    editor.putString(ChooPref.STATION,arystationList.get(position).getStationName());
//                    editor.commit();
//                    Log.e("STATION",prefs.getString(ChooPref.STATION,null));
                lststationlist3.setVisibility(View.GONE);
                selectStation.setSelection(selectStation.getText().toString().length());
                selectStation.setCursorVisible(true);


            }
        });
        transitionStation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    lststationlist2.setVisibility(View.GONE);
                    transitionStation.setSelection(transitionStation.getText().toString().length());
                }
                return false;
            }
        });
        selectStation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    lststationlist3.setVisibility(View.GONE);
//                    lststationlist.setSelection(transitionStation.getText().toString().length());
                    selectStation.setCursorVisible(false);
                }
                return false;
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().length() == 0) {
                    rootview.findViewById(R.id.emaillayout).startAnimation(shake);
                    return;
                }
                editor.putBoolean(ChooSaveDataPref.IS_BAHN_TIME_CARD, isBahn);
                editor.putString(ChooSaveDataPref.TYPE_OF_CARD, cardType.getText().toString());
                editor.putString(ChooSaveDataPref.TYPE_OF_CLASS, classtype.getText().toString());
                editor.putString(ChooSaveDataPref.CARD_NUMBER, cardNumber.getText().toString());
                if(expiryDate.getText().toString().equals(""))
                {

                }else {
                    editor.putString(ChooSaveDataPref.EXPIRE_DATE, expiryDate.getText().toString());
                }
                if(dateofBirth.getText().toString().equals(""))
                {

                }else {
                    editor.putString(ChooSaveDataPref.DATE_OF_BIRTH, dateofBirth.getText().toString());
                }
                editor.putString(ChooSaveDataPref.TITLE, txtSelectTitle.getText().toString());
                editor.putString(ChooSaveDataPref.FIRST_NAME, firstname.getText().toString());
                editor.putString(ChooSaveDataPref.NAME, name.getText().toString());
                if(email.getText().toString().equals("")){}else {
                    editor.putString(ChooSaveDataPref.EMAIL, email.getText().toString());
                }
                editor.putString(ChooSaveDataPref.STREET, street.getText().toString());
                editor.putString(ChooSaveDataPref.HOUSE_NUMBER, housenumber.getText().toString());
                editor.putString(ChooSaveDataPref.ADDRESS, address.getText().toString());
                editor.putString(ChooSaveDataPref.ZIP_CODE, zipcode.getText().toString());
                editor.putString(ChooSaveDataPref.CITY, city.getText().toString());
                editor.putString(ChooSaveDataPref.COMPANY, company.getText().toString());
                editor.putString(ChooSaveDataPref.ACCOUNT_NUMBER, accountnumber.getText().toString());
                editor.putString(ChooSaveDataPref.BANK_CODE, bankcode.getText().toString());
                editor.putString(ChooSaveDataPref.ACCOUNT_HOLDER, accountholder.getText().toString());
                editor.commit();

                editor1.putString(ChooPref.DEPARTURE_STATION, departureStation.getText().toString());
                editor1.putString(ChooPref.PLANNED_DEPARTURE, plannedDeparture.getText().toString());
                editor1.putString(ChooPref.STATION, destinationStation.getText().toString());
                editor1.putString(ChooPref.PLANED_ARRIVAL, plannedArrival.getText().toString());
                editor1.putString(ChooPref.ACTUAL_ARRIVAL, actualArrival.getText().toString());
                editor1.putString(ChooPref.TRAIN_TYPE, trainType.getText().toString());
                editor1.putString(ChooPref.TRAIN_NUMBER, trainNumber.getText().toString());
                editor1.putString(ChooPref.SELECT_PRICE, selectprice.getText().toString());
                editor1.putString(ChooPref.IS_ICE_USER, "" + isIce);
                editor1.putBoolean(ChooPref.IS_TRANSITION, isTrans);
                if(isTrans) {
                    editor1.putString(ChooPref.TRANS_TRAIN_TYPE, trainTypeTrans.getText().toString());
                    editor1.putString(ChooPref.TRANS_TRAIN_NUMBER, trainNumValue.getText().toString());
                    editor1.putString(ChooPref.TRANS_PLANNED_DEPT, dtPlanned.getText().toString());
                    editor1.putString(ChooPref.TRANS_STATION, selectStation.getText().toString());
                    editor1.putString(ChooPref.LAST_TRANSITION, transitionStation.getText().toString());
                }
                editor1.commit();
                try{
                    ParseUser parseUser = ParseUser.getCurrentUser();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                    Log.e("getCurrentUser", "" + ParseUser.getCurrentUser());
                    Log.e("parseUser.getObjectId()", "" + parseUser.getObjectId());
                    // Retrieve the object by id
                    query.getInBackground(parseUser.getObjectId(), new GetCallback<ParseObject>() {
                        public void done(ParseObject user, ParseException e) {
                            if (e == null) {
                                if (email.getText().toString().equals("")) {
                                }else{
                                    user.put("email", email.getText().toString());
                                }
                                user.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Log.e("", "Updated");
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            SendRequestFragment fragment = new SendRequestFragment();
                                            fragmentTransaction.replace(R.id.realtabcontent, fragment, "SendRequest").addToBackStack(null);
                                            fragmentTransaction.commit();
                                        } else {
                                            Log.e("", "Updated exception" + e);
                                            email.setText("");
                                            editor.putString(ChooSaveDataPref.EMAIL, "");
                                            editor.commit();
                                            if(e.getMessage().contains("the email address") && e.getMessage().contains("has already been taken"))
                                            {
                                                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.emailtaken), Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(getActivity().getApplicationContext(), R.string.invalidEmail, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                });

                            } else {
                                Log.e("", "Updatiin Exception" + e);
                            }
                        }
                    });
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        txtSelectTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTitleDialog();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.gallery:
                                value = 0;
                                Intent i = new Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                startActivityForResult(i, RESULT_LOAD_IMAGE);
                                return true;
                            case R.id.camera:
                                value = 1;
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.photopopup);
                popupMenu.show();
            }
        });
        dateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 1;
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        expiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 0;
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        classtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(),view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.class1:
                                classtype.setText(R.string.class1);
                                classtype.setTextColor(Color.parseColor("#309DC5"));
                                return true;
                            case R.id.class2:
                                classtype.setText(R.string.class2);
                                classtype.setTextColor(Color.parseColor("#309DC5"));
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.classtype_popup);
                popupMenu.show();
            }
        });
        cardType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bahn:
                                cardType.setText("BahnCard100");
                                cardType.setTextColor(Color.parseColor("#309DC5"));
                                return true;
                            case R.id.timecard:
                                cardType.setText(R.string.timecard);
                                cardType.setTextColor(Color.parseColor("#309DC5"));
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.popup);
                popupMenu.show();
            }
        });
        plannedDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeStatus = 0 ;
                SimpleDateTimePicker simpleDateTimePicker = SimpleDateTimePicker.make(
                        "Set Date & Time Title",
                        new Date(),
                        OverviewFragment.this,
                        getActivity().getSupportFragmentManager()
                );
                // Show It baby!
                simpleDateTimePicker.show();
            }
        });
        plannedArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeStatus = 1;
                SimpleDateTimePicker simpleDateTimePicker = SimpleDateTimePicker.make(
                        "Set Date & Time Title",
                        new Date(),
                        OverviewFragment.this,
                        getActivity().getSupportFragmentManager()
                );
                // Show It baby!
                simpleDateTimePicker.show();
            }
        });
        actualArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeStatus = 2;
                SimpleDateTimePicker simpleDateTimePicker = SimpleDateTimePicker.make(
                        "Set Date & Time Title",
                        new Date(),
                        OverviewFragment.this,
                        getActivity().getSupportFragmentManager()
                );
                // Show It baby!
                simpleDateTimePicker.show();
            }
        });
        selectprice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    //do something
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    if (selectprice.getText().length() == 0) {
                    } else if (selectprice.getText().length() == 1) {
                        selectprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                        if (selectprice.getText().toString().contains("/u20ac")) {
                            if (selectprice.getText().toString().contains(",")) {
                            } else {
                                selectprice.setText(selectprice.getText().toString() + ",00");
                            }
                        } else {
                            if (selectprice.getText().toString().contains(",")) {
                                selectprice.setText("\u20ac " + selectprice.getText().toString());
                            } else {
                                selectprice.setText("\u20ac " + selectprice.getText().toString() + ",00");
                            }

                        }
//                        selectprice.setText("\u20ac " + selectprice.getText().toString() + ",00");
                        editor2.putInt("selectprice", 6);
                        editor2.commit();
                    } else if (selectprice.getText().length() == 2) {
                        selectprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
                        if (selectprice.getText().toString().contains("/u20ac")) {
                            if (selectprice.getText().toString().contains(",")) {
                            } else {
                                selectprice.setText(selectprice.getText().toString() + ",00");
                            }
                        } else {
                            if (selectprice.getText().toString().contains(",")) {
                                selectprice.setText("\u20ac " + selectprice.getText().toString());
                            } else {
                                selectprice.setText("\u20ac " + selectprice.getText().toString() + ",00");
                            }

                        }
//                        selectprice.setText("\u20ac " + selectprice.getText().toString() + ",00");
                        editor2.putInt("selectprice", 7);
                        editor2.commit();
                    } else if (selectprice.getText().length() == 3) {
                        selectprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                        if (selectprice.getText().toString().contains("/u20ac")) {
                            if (selectprice.getText().toString().contains(",")) {
                            } else {
                                selectprice.setText(selectprice.getText().toString() + ",00");
                            }
                        } else {
                            if (selectprice.getText().toString().contains(",")) {
                                selectprice.setText("\u20ac " + selectprice.getText().toString());
                            } else {
                                selectprice.setText("\u20ac " + selectprice.getText().toString() + ",00");
                            }

                        }
//                        selectprice.setText("\u20ac " + selectprice.getText().toString() + ",00");
                        editor2.putInt("selectprice", 8);
                        editor2.commit();
                    } else if (selectprice.getText().length() == 4) {
                        selectprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
                        if (selectprice.getText().toString().contains("/u20ac")) {
                            if (selectprice.getText().toString().contains(",")) {
                            } else {
                                String x = selectprice.getText().toString();
                                x = x.substring(0, 2) + "," + x.substring(2, x.length());
                                selectprice.setText(x);
                            }
                        } else {
                            if (selectprice.getText().toString().contains(",")) {
                                selectprice.setText("\u20ac " + selectprice.getText().toString());
                            } else {
                                String x = selectprice.getText().toString();
                                x = "\u20ac " + x.substring(0, 2) + "," + x.substring(2, x.length());
                                selectprice.setText(x);
                            }

                        }

                        editor2.putInt("selectprice", 7);
                        editor2.commit();
                    } else if (selectprice.getText().length() == 5) {
                        selectprice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                        if (selectprice.getText().toString().contains("/u20ac")) {
                            if (selectprice.getText().toString().contains(",")) {
                            } else {
                                String x = selectprice.getText().toString();
                                x = x.substring(0, 3) + "," + x.substring(3, x.length());
                                selectprice.setText(x);
                            }
                        } else {
                            if (selectprice.getText().toString().contains(",")) {
                                selectprice.setText("\u20ac " + selectprice.getText().toString());
                            } else {
                                String x = selectprice.getText().toString();
                                x = "\u20ac " + x.substring(0, 3) + "," + x.substring(3, x.length());
                                selectprice.setText(x);
                            }

                        }
//                        String x = selectprice.getText().toString();
//                        x = "\u20ac " + x.substring(0, 3) + "," + x.substring(3, x.length());
//                        selectprice.setText(x);
                        editor2.putInt("selectprice", 8);
                        editor2.commit();
                    }
//                    selectprice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});

//                        if (selectprice.getText().toString().contains(".")) {
//                            newString = selectprice.getText().toString().replace(".", ",");
//                        } else if(selectprice.getText().toString().contains(",")){
//                                newString = selectprice.getText().toString();
//                            } else {
//                                newString = selectprice.getText().toString() + ",00";
//                            }
//
//                    if(selectprice.getText().toString().contains("\u20ac"))
//                    {}else{
//                        newString = "\u20ac " + selectprice.getText().toString();
//                    }
//                        selectprice.setText(newString);
//                    tickettrippricetext.setTextColor(Color.parseColor("#000000"));
                    selectprice.setSelection(selectprice.getText().length());
                    selectprice.setCursorVisible(false);
                }
                return false;
            }
        });
        trainType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               trainType.setTextColor(Color.parseColor("#dbdbdb"));
//                showPopup(getActivity());
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ICE:
                                trainType.setText("ICE");
                                trainType.setTextColor(Color.parseColor("#309DC5"));
                                editor.putString(ChooPref.TRAIN_TYPE, "ICE");
                                editor.commit();
                                return true;
                            case R.id.IC:
                                trainType.setText("IC");
                                trainType.setTextColor(Color.parseColor("#309DC5"));
                                editor.putString(ChooPref.TRAIN_TYPE, "IC");
                                editor.commit();
                                return true;
                            case R.id.IRE:
                                trainType.setText("IRE");
                                trainType.setTextColor(Color.parseColor("#309DC5"));
                                editor.putString(ChooPref.TRAIN_TYPE, "IRE");
                                editor.commit();
                                return true;
                            case R.id.RE:
                                trainType.setText("RE");
                                trainType.setTextColor(Color.parseColor("#309DC5"));
                                editor.putString(ChooPref.TRAIN_TYPE, "RE");
                                editor.commit();
                                return true;
                            case R.id.RB:
                                trainType.setText("RB");
                                trainType.setTextColor(Color.parseColor("#309DC5"));
                                editor.putString(ChooPref.TRAIN_TYPE, "RB");
                                editor.commit();
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.popupmenu);
                popupMenu.show();
            }
        });
        departureStation.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                try {
                    Log.d("", "*** Search value changed: " + s.toString());
                    String text = departureStation.getText().toString().toLowerCase(Locale.getDefault());
                    dataAdapter.getFilter().filter(text);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        departureStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                transitionStationtext.setTextColor(Color.parseColor("#309DC5"));
                dataAdapter = new StationListAdapter(getActivity(), arystationList, res);
                lststationlist1.setAdapter(dataAdapter);
                lststationlist1.setVisibility(View.VISIBLE);
            }
        });
        departureStation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    transitionStationtext.setTextColor(Color.parseColor("#309DC5"));
                    dataAdapter = new StationListAdapter(getActivity(), arystationList, res);
                    lststationlist1.setAdapter(dataAdapter);
                    lststationlist1.setVisibility(View.VISIBLE);
                }

            }
        });
        lststationlist1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    departureStation.setText(dataAdapter.
                            filterArray().get(position).getStationName());
                }catch (Exception e)
                {
                    departureStation.setText(arystationList.get(position).getStationName());
                }

//                transitionStationtext.setTextColor(Color.parseColor("#000000"));


//                    editor.putString(ChooPref.STATION,arystationList.get(position).getStationName());
//                    editor.commit();
//                    Log.e("STATION",prefs.getString(ChooPref.STATION,null));
                lststationlist1.setVisibility(View.GONE);

            }
        });
        destinationStation.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                try {
                    Log.d("", "*** Search value changed: " + s.toString());
                    String text = destinationStation.getText().toString().toLowerCase(Locale.getDefault());
                    dataAdapter.getFilter().filter(text);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        destinationStation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    selectStationtext.setTextColor(Color.parseColor("#309DC5"));
                    dataAdapter = new StationListAdapter(getActivity(), arystationList, res);
                    lststationlist.setAdapter(dataAdapter);
                    lststationlist.setVisibility(View.VISIBLE);
                }

            }
        });
        destinationStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectStationtext.setTextColor(Color.parseColor("#309DC5"));
                dataAdapter = new StationListAdapter(getActivity(), arystationList, res);
                lststationlist.setAdapter(dataAdapter);
                lststationlist.setVisibility(View.VISIBLE);
            }
        });
        lststationlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    destinationStation.setText(dataAdapter.
                            filterArray().get(position).getStationName());
                }catch (Exception e)
                {
                    destinationStation.setText(arystationList.get(position).getStationName());
                }
//                selectStationtext.setTextColor(Color.parseColor("#000000"));

//                    editor.putString(ChooPref.STATION,arystationList.get(position).getStationName());
//                    editor.commit();
//                    Log.e("STATION",prefs.getString(ChooPref.STATION,null));
                lststationlist.setVisibility(View.GONE);

            }
        });
        bahnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isBahn = isChecked;
                if(isChecked){
                    rootview.findViewById(R.id.bahnInfo).setVisibility(View.VISIBLE);
                    rootview.findViewById(R.id.tripPrice).setVisibility(View.GONE);

                }else{
                    rootview.findViewById(R.id.bahnInfo).setVisibility(View.GONE);
                    rootview.findViewById(R.id.tripPrice).setVisibility(View.VISIBLE);

                }
//                if(!isChecked){
//                rootview.findViewById(R.id.bahnInfo).setVisibility(View.GONE);
//                    rootview.findViewById(R.id.tripPrice).setVisibility(View.VISIBLE);
//                    selectprice.setText("\u20B9 0.00");
////                    tripPrice
//                }
            }
        });
        trainTypeTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainTypeTrans.setTextColor(Color.parseColor("#dbdbdb"));
//                showPopup(getActivity());
                PopupMenu popupMenu = new PopupMenu(getActivity(),v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ICE:
                                trainTypeTrans.setText("ICE");
                                trainTypeTrans.setTextColor(Color.parseColor("#309DC5"));
//                                editor.putString(ChooPref.TRAIN_TYPE,"ICE");
//                                editor.commit();
                                return true;
                            case R.id.IC:
                                trainTypeTrans.setText("IC");
                                trainTypeTrans.setTextColor(Color.parseColor("#309DC5"));
//                                editor.putString(ChooPref.TRAIN_TYPE,"IC");
//                                editor.commit();
                                return true;
                            case R.id.IRE:
                                trainTypeTrans.setText("IRE");
                                trainTypeTrans.setTextColor(Color.parseColor("#309DC5"));
//                                editor.putString(ChooPref.TRAIN_TYPE,"IRE");
//                                editor.commit();
                                return true;
                            case R.id.RE:
                                trainTypeTrans.setText("RE");
                                trainTypeTrans.setTextColor(Color.parseColor("#309DC5"));
//                                editor.putString(ChooPref.TRAIN_TYPE,"RE");
//                                editor.commit();
                                return true;
                            case R.id.RB:
                                trainTypeTrans.setText("RB");
                                trainTypeTrans.setTextColor(Color.parseColor("#309DC5"));
//                                editor.putString(ChooPref.TRAIN_TYPE,"RB");
//                                editor.commit();
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.popupmenu);
                popupMenu.show();
            }
        });
        tranSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isTrans = isChecked;
                if(isChecked){
                    rootview.findViewById(R.id.transInfo).setVisibility(View.VISIBLE);
                    Log.e("","(prefs1.getString(ChooPref.TRANS_PLANNED_DEPT,null)"+(prefs1.getString(ChooPref.TRANS_TRAIN_TYPE,null)));

                }else{
                    rootview.findViewById(R.id.transInfo).setVisibility(View.GONE);
                }
            }
        });
        iceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isIce = isChecked;
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressbar.setImageResource(R.mipmap.progress_bar7);
            }
        },100);
        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf;
        if("en".equals( getString(R.string.lang) ) ) {
            sdf = new SimpleDateFormat(myFormat, Locale.US);
        }else{
            sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);
        }
        if(id == 0)
        {
            expiryDate.setText(sdf.format(myCalendar.getTime()));
        }
        else if(id == 1){
            dateofBirth.setText(sdf.format(myCalendar.getTime()));
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_overview_info, menu);
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
        if(TimeStatus == 0){
            plannedDeparture.setText(mDateTime.getDateString());

        }else if(TimeStatus == 1){
            plannedArrival.setText(mDateTime.getDateString());

        }
        else if(TimeStatus == 2){
            actualArrival.setText(mDateTime.getDateString());
        }
        else if(TimeStatus == 4){
            dtPlanned.setText(mDateTime.getDateString());
            dtPlanned.setTextColor(Color.parseColor("#309DC5"));
        }
    }
    public void showTitleDialog(){
        final CharSequence[] items = {getString(R.string.mr),getString(R.string.mrs)};
        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Title");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {


                switch(item)
                {
                    case 0:
                        // Your code when first option seletced
                        txtSelectTitle.setText(R.string.mr);
                        break;
                    case 1:
                        // Your code when 2nd  option seletced
                        txtSelectTitle.setText(R.string.mrs);
                        break;
                    case 2:
                        // Your code when 3rd option seletced
                        break;
                    case 3:
                        // Your code when 4th  option seletced
                        break;

                }
                levelDialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("requestCode",""+requestCode);
        Log.e("resultCode",""+resultCode);
        Log.e("data",""+data);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = 4 * displayMetrics.widthPixels;
        int screenHeight = 4 * displayMetrics.heightPixels;
        Log.e("screenWidth, screenHeight ",screenWidth + " " + screenHeight);
        Uri selectedImage1 = null;
        String picturePath;
        Bitmap photo;
        if(value == 0) {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
                selectedImage1 = data.getData();
                Log.e("selectedImage1"+selectedImage1, "c"+getActivity());
                if(selectedImage1 == null)
                    return;
                editor.putString(ChooPref.SELECTED_IMAGE_URI, selectedImage1+"").commit();
                picturePath = ImageHelper2.compressImage(selectedImage1, getActivity());
                img.setAdjustViewBounds(true);
                photo = ImageHelper2.decodeSampledBitmapFromFile(picturePath,screenWidth,screenHeight);
                img.setImageBitmap(photo);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BitmapFactory.decodeFile(picturePath).compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();

                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                editor1.putString(ChooPref.TICKET_IMAGE,encodedImage);
                editor1.commit();
                // String picturePath contains the path of selected Image
            }
        }else if(value == 1) {
            if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {

                Log.i("TAG", "Inside PICK_FROM_CAMERA");


                // Describe the columns you'd like to have returned. Selecting from
                // the Thumbnails location gives you both the Thumbnail Image ID, as
                // well as the original image ID

                try {
                    Log.i("TAG", "inside Samsung Phones");
                    String[] projection = {
                            MediaStore.Images.Thumbnails._ID, // The columns we want
                            MediaStore.Images.Thumbnails.IMAGE_ID,
                            MediaStore.Images.Thumbnails.KIND,
                            MediaStore.Images.Thumbnails.DATA };
                    String selection = MediaStore.Images.Thumbnails.KIND + "=" + // Select
                            // only
                            // mini's
                            MediaStore.Images.Thumbnails.MINI_KIND;

                    String sort = MediaStore.Images.Thumbnails._ID + " DESC";

                    // At the moment, this is a bit of a hack, as I'm returning ALL
                    // images, and just taking the latest one. There is a better way
                    // to
                    // narrow this down I think with a WHERE clause which is
                    // currently
                    // the selection variable
                    Cursor myCursor = getActivity().managedQuery(
                            MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                            projection, selection, null, sort);

                    long imageId = 0l;
                    long thumbnailImageId = 0l;
                    String thumbnailPath = "";

                    try {
                        myCursor.moveToFirst();
                        imageId = myCursor
                                .getLong(myCursor
                                        .getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID));
                        thumbnailImageId = myCursor
                                .getLong(myCursor
                                        .getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID));
                        thumbnailPath = myCursor
                                .getString(myCursor
                                        .getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
                    } finally {
                        // myCursor.close();
                    }

                    // Create new Cursor to obtain the file Path for the large image

                    String[] largeFileProjection = {
                            MediaStore.Images.ImageColumns._ID,
                            MediaStore.Images.ImageColumns.DATA };

                    String largeFileSort = MediaStore.Images.ImageColumns._ID
                            + " DESC";
                    myCursor = getActivity().managedQuery(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            largeFileProjection, null, null, largeFileSort);
                    String largeImagePath = "";

                    try {
                        myCursor.moveToFirst();

                        // This will actually give yo uthe file path location of the
                        // image.
                        largeImagePath = myCursor
                                .getString(myCursor
                                        .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
                        selectedImage1 = Uri.fromFile(new File(
                                largeImagePath));

                    } finally {
                        // myCursor.close();
                    }
                    // These are the two URI's you'll be interested in. They give
                    // you a
                    // handle to the actual images
                    Uri uriLargeImage = Uri.withAppendedPath(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            String.valueOf(imageId));
                    Uri uriThumbnailImage = Uri.withAppendedPath(
                            MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                            String.valueOf(thumbnailImageId));

                    // I've left out the remaining code, as all I do is assign the
                    // URI's
                    // to my own objects anyways...
                } catch (Exception e) {

                    Log.i("TAG",
                            "inside catch Samsung Phones exception " + e.toString());

                }


                try {

                    Log.i("TAG", "URI Normal:" + selectedImage1.getPath());
                } catch (Exception e) {
                    Log.i("TAG", "Excfeption inside Normal URI :" + e.toString());
                }

                img.setAdjustViewBounds(true);
                picturePath = ImageHelper2.compressImage(selectedImage1, getActivity());
                photo = ImageHelper2.decodeSampledBitmapFromFile(picturePath,screenWidth,screenHeight);
                img.setImageBitmap(photo);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();

                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                editor1.putString(ChooPref.TICKET_IMAGE, encodedImage);
                editor.putString(ChooPref.SELECTED_IMAGE_URI, selectedImage1+"");
                editor1.commit();
            }
        }
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

}
