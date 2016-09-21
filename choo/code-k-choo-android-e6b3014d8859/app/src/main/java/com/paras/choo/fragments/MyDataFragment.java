package com.paras.choo.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.paras.choo.BankAccountInfoActivity;
import com.paras.choo.ChooTicketActivity;
import com.paras.choo.R;
import com.paras.choo.adapters.TicketsAdpater;
import com.paras.choo.beans.Singleton;
import com.paras.choo.utils.ChooPref;
import com.paras.choo.utils.ChooPrefsUser;
import com.paras.choo.utils.ChooSaveDataPref;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyDataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    View rootView ;
    Switch bahnSwitch;
    int id;
    LinearLayout personallayout, submission, submissionlayout;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor1;
    SharedPreferences prefs;
    SharedPreferences prefs1;
    LinearLayout bahnInfo;
    TextView txtSelectTitle;
    Calendar myCalendar;
    Calendar myCalendar1;
    DatePickerDialog.OnDateSetListener date2;
    DatePickerDialog.OnDateSetListener date1;
    AlertDialog levelDialog ;
    TextView cardType;
    Boolean bahnValue = false;
    ImageView info;
    TextView expdate,dob,currentdate,dest,pricetype,delay, classtype;
    EditText firstname, name, email, street, housenumber, address, zipcode, city, company, accountnumber, bankcode, accountholder, cardnumber;
    TextView save,txtSubmissions, personaldata, reclaim;
    FrameLayout layout_MainMenu;
    com.emilsjolander.components.StickyScrollViewItems.StickyScrollView Sticky;
    SharedPreferences sharedPreferencesUser = null ;
    ListView lstSubmissions;
    TextView heading, subline1, subline2, subline3, titletext, firstnametext, nametext, emailtext, streettext,
            housenumtext, addresstext, ziptext, citytext, companytext, accountnumtext, bankcodetext, accountholdertext,
            bahntext, cardtypetext, classtypetext, cardnumbertext, expdatetext, dobtext;
    TicketsAdpater ticketsAdpater = null ;
    TextView choonotext, datte, k1text;
    Typeface headlinefont, sublinefont, fieldsfont;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyDataFragment newInstance(String param1, String param2) {
        MyDataFragment fragment = new MyDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout_MainMenu = (FrameLayout) getActivity().findViewById( R.id.realtabcontent);
        layout_MainMenu.getForeground().setAlpha(0);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if(!isAdded()) {
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_my_data, container, false);
        setWidgets();
        return rootView;
    }
    public void setWidgets(){
        myCalendar = Calendar.getInstance();
        myCalendar1 = Calendar.getInstance();
        date2 = new DatePickerDialog.OnDateSetListener() {

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
        date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        Sticky =   (com.emilsjolander.components.StickyScrollViewItems.StickyScrollView)rootView.findViewById(R.id.sticky_scroll);
        txtSubmissions = (TextView)rootView.findViewById(R.id.txtSubmissions);
        personaldata = (TextView)rootView.findViewById(R.id.personaldata);
        personallayout = (LinearLayout)rootView.findViewById(R.id.personallayout);
        submissionlayout = (LinearLayout)rootView.findViewById(R.id.submissionlayout);
        submission = (LinearLayout)rootView.findViewById(R.id.submission);
        reclaim = (TextView)rootView.findViewById(R.id.reclaim);
        currentdate = (TextView)rootView.findViewById(R.id.currentdate);
        dest = (TextView)rootView.findViewById(R.id.dest);
        pricetype = (TextView)rootView.findViewById(R.id.pricetype);
        delay = (TextView)rootView.findViewById(R.id.delay);
        cardType = (TextView)rootView.findViewById(R.id.cardType);
        classtype = (TextView)rootView.findViewById(R.id.classtype);
        bahnSwitch = (Switch)rootView.findViewById(R.id.bahnSwitch);
        expdate = (TextView)rootView.findViewById(R.id.expdate);
        dob = (TextView)rootView.findViewById(R.id.dob);
        info = (ImageView)rootView.findViewById(R.id.info);
        save = (TextView)rootView.findViewById(R.id.save);
        firstname = (EditText)rootView.findViewById(R.id.firstname);
        name = (EditText)rootView.findViewById(R.id.name);
        email = (EditText)rootView.findViewById(R.id.email);
        street = (EditText)rootView.findViewById(R.id.street);
        housenumber = (EditText)rootView.findViewById(R.id.housenumber);
        address = (EditText)rootView.findViewById(R.id.address);
        zipcode = (EditText)rootView.findViewById(R.id.zipcode);
        city = (EditText)rootView.findViewById(R.id.city);
        company = (EditText)rootView.findViewById(R.id.company);
        accountnumber = (EditText)rootView.findViewById(R.id.accountnumber);
        bankcode = (EditText)rootView.findViewById(R.id.bankcode);
        accountholder = (EditText)rootView.findViewById(R.id.accountholder);
        cardnumber = (EditText)rootView.findViewById(R.id.cardnumber);
        lstSubmissions = (ListView)rootView.findViewById(R.id.lstSubmissions);
        heading = (TextView)rootView.findViewById(R.id.heading);
        subline1 = (TextView)rootView.findViewById(R.id.subline1);
        subline2 = (TextView)rootView.findViewById(R.id.subline2);
        subline3 = (TextView)rootView.findViewById(R.id.subline3);
        titletext = (TextView)rootView.findViewById(R.id.titletext);
        firstnametext = (TextView)rootView.findViewById(R.id.firstnametext);
        nametext = (TextView)rootView.findViewById(R.id.nametext);
        emailtext = (TextView)rootView.findViewById(R.id.emailtext);
        streettext = (TextView)rootView.findViewById(R.id.streettext);
        housenumtext = (TextView)rootView.findViewById(R.id.housenumtext);
        addresstext = (TextView)rootView.findViewById(R.id.addresstext);
        ziptext = (TextView)rootView.findViewById(R.id.ziptext);
        citytext = (TextView)rootView.findViewById(R.id.citytext);
        companytext = (TextView)rootView.findViewById(R.id.companytext);
        accountnumtext = (TextView)rootView.findViewById(R.id.accountnumtext);
        bankcodetext = (TextView)rootView.findViewById(R.id.bankcodetext);
        accountholdertext = (TextView)rootView.findViewById(R.id.accountholdertext);
        bahntext = (TextView)rootView.findViewById(R.id.bahntext);
        cardtypetext = (TextView)rootView.findViewById(R.id.cardtypetext);
        classtypetext = (TextView)rootView.findViewById(R.id.classtypetext);
        cardnumbertext = (TextView)rootView.findViewById(R.id.cardnumbertext);
        expdatetext = (TextView)rootView.findViewById(R.id.expdatetext);
        dobtext = (TextView)rootView.findViewById(R.id.dobtext);
        bahnInfo = (LinearLayout)rootView.findViewById(R.id.bahnInfo);
        txtSelectTitle = (TextView)rootView.findViewById(R.id.txtSelectTitle);
        choonotext = (TextView)rootView.findViewById(R.id.choonotext);
        datte = (TextView)rootView.findViewById(R.id.date);
        k1text = (TextView)rootView.findViewById(R.id.k1text);

        headlinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");
        fieldsfont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        sublinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        sharedPreferencesUser = getActivity().getSharedPreferences(ChooPrefsUser.CHOO_PREF_USER, getActivity().MODE_PRIVATE);
        editor = getActivity().getSharedPreferences(ChooSaveDataPref.CHOO_SAVEDATA_PREF, getActivity().MODE_PRIVATE).edit();
        editor1 = getActivity().getSharedPreferences(ChooPref.CHOO_PREF, getActivity().MODE_PRIVATE).edit();
        prefs = getActivity().getSharedPreferences(ChooSaveDataPref.CHOO_SAVEDATA_PREF, getActivity().MODE_PRIVATE);
        prefs1 = getActivity().getSharedPreferences(ChooPref.CHOO_PREF, getActivity().MODE_PRIVATE);

        Log.e("MINUTES_DELAYED",""+ prefs1.getInt(ChooPref.MINUTES_DELAYED,0));

        if(prefs.getString(ChooSaveDataPref.TITLE,null) == null)
        {
            txtSelectTitle.setText("Mr./Ms.");
//            editor.putString(ChooSaveDataPref.TITLE,"Herr");
        }else {
            txtSelectTitle.setText(prefs.getString(ChooSaveDataPref.TITLE, null));
        }
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
        bankcode.setText(prefs.getString(ChooSaveDataPref.BANK_CODE, null));
        accountholder.setText(prefs.getString(ChooSaveDataPref.ACCOUNT_HOLDER, null));
        if(prefs.getBoolean(ChooSaveDataPref.IS_BAHN_TIME_CARD,false))
        {
            bahnValue = true;
            bahnSwitch.setChecked(true);
            bahnInfo.setVisibility(View.VISIBLE);
        }else{
            bahnValue = false;
            bahnSwitch.setChecked(false);
            bahnInfo.setVisibility(View.GONE);
        }

        if(prefs.getString(ChooSaveDataPref.TYPE_OF_CARD,null) == null){
            cardType.setText("BahnCard100");
        }
        else {
            cardType.setText(prefs.getString(ChooSaveDataPref.TYPE_OF_CARD, null));
        }
        if(prefs.getString(ChooSaveDataPref.TYPE_OF_CLASS,null) == null){
            classtype.setText("1. Class");
        }
        else {
            classtype.setText(prefs.getString(ChooSaveDataPref.TYPE_OF_CLASS, null));
        }
        cardnumber.setText(prefs.getString(ChooSaveDataPref.CARD_NUMBER,null));

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date();
        if(prefs.getString(ChooSaveDataPref.EXPIRE_DATE, null) == null) {
            expdate.setText(dateFormat.format(addMonths(date, 6)));
        }
        else {
            expdate.setText(prefs.getString(ChooSaveDataPref.EXPIRE_DATE, null));
        }
        if(prefs.getString(ChooSaveDataPref.DATE_OF_BIRTH,null) == null) {
            dob.setText(dateFormat.format(subtractYear(date, -18)));
        }
        else {
            dob.setText(prefs.getString(ChooSaveDataPref.DATE_OF_BIRTH, null));
        }

        heading.setTypeface(headlinefont);
        subline1.setTypeface(sublinefont);
        subline2.setTypeface(sublinefont);
        subline3.setTypeface(sublinefont);
        bahntext.setTypeface(fieldsfont);
        cardtypetext.setTypeface(fieldsfont);
        cardType.setTypeface(fieldsfont);
        classtype.setTypeface(fieldsfont);
        classtypetext.setTypeface(fieldsfont);
        cardnumbertext.setTypeface(fieldsfont);
        cardnumber.setTypeface(fieldsfont);
        expdatetext.setTypeface(fieldsfont);
        expdate.setTypeface(fieldsfont);
        dobtext.setTypeface(fieldsfont);
        dob.setTypeface(fieldsfont);
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
        choonotext.setTypeface(fieldsfont);
        datte.setTypeface(fieldsfont);
        k1text.setTypeface(fieldsfont);
        expdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 0;
                String[] DateArray = expdate.getText().toString().split("-");
                Log.e("DateArray", "" + DateArray[0] + "" + DateArray[1] + "" + getInt(DateArray[1]));
                new DatePickerDialog(getActivity(), date1, Integer.parseInt(DateArray[2]), getInt(DateArray[1]) - 1,
                        Integer.parseInt(DateArray[0])).show();
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 1;
                String[] DateArray = dob.getText().toString().split("-");
                Log.e("DateArray", "" + DateArray[0] + "" + DateArray[1] + "" + getInt(DateArray[1]));
                new DatePickerDialog(getActivity(), date2, Integer.parseInt(DateArray[2]), getInt(DateArray[1]) - 1,
                        Integer.parseInt(DateArray[0])).show();
            }
        });
        bahnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bahnInfo.setVisibility(View.VISIBLE);
                    bahnValue = isChecked;
                } else {
                    bahnInfo.setVisibility(View.GONE);
                    bahnValue = isChecked;
                }
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BankAccountInfoActivity.class);
                startActivity(intent);
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
                PopupMenu popupMenu = new PopupMenu(getActivity(),v);
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
        lstSubmissions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(prefs1.getString(ChooPref.STATION, null) != null && position == 0)
                {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    RefundCheck fragment = new RefundCheck();
                    fragmentTransaction.replace(R.id.realtabcontent, fragment,"RefundCheck");
                    fragmentTransaction.commit();
                    editor1.putString(ChooPref.BACK, "k");
                    editor1.commit();
                }else
                {
                    Intent i = new Intent(getActivity(), ChooTicketActivity.class);
                    i.putExtra("pos", position);
                    startActivity(i);
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEmailValid(email.getText().toString())){
                    if(txtSelectTitle.getText().toString().equals(""))
                    {}
                    else {
                        editor.putString(ChooSaveDataPref.TITLE, txtSelectTitle.getText().toString());
                    }
                    if(firstname.getText().toString().equals(""))
                    {}
                    else {
                        editor.putString(ChooSaveDataPref.FIRST_NAME, firstname.getText().toString());
                    }
                    if(name.getText().toString().equals(""))
                    {}
                    else {
                        editor.putString(ChooSaveDataPref.NAME, name.getText().toString());
                    }
                    if(email.getText().toString().equals(""))
                    {}
                    else {
                        editor.putString(ChooSaveDataPref.EMAIL, email.getText().toString());
                    }
                    if(street.getText().toString().equals(""))
                    {}
                    else {
                        editor.putString(ChooSaveDataPref.STREET, street.getText().toString());
                    }
                    if(housenumber.getText().toString().equals(""))
                    {}
                    else {
                        editor.putString(ChooSaveDataPref.HOUSE_NUMBER, housenumber.getText().toString());
                    }
                    if(address.getText().toString().equals(""))
                    {}
                    else {
                        editor.putString(ChooSaveDataPref.ADDRESS, address.getText().toString());
                    }
                    if(zipcode.getText().toString().equals(""))
                    {}
                    else {
                        editor.putString(ChooSaveDataPref.ZIP_CODE, zipcode.getText().toString());
                    }
                    if(city.getText().toString().equals(""))
                    {}
                    else {
                        editor.putString(ChooSaveDataPref.CITY, city.getText().toString());
                    }
                    if(company.getText().toString().equals(""))
                    {}
                    else {
                        editor.putString(ChooSaveDataPref.COMPANY, company.getText().toString());
                    }
                    if(accountnumber.getText().toString().equals(""))
                    {}
                    else {
                        editor.putString(ChooSaveDataPref.ACCOUNT_NUMBER, accountnumber.getText().toString());
                    }
                    if(bankcode.getText().toString().equals(""))
                    {}
                    else {
                        editor.putString(ChooSaveDataPref.BANK_CODE, bankcode.getText().toString());
                    }
                    if(accountholder.getText().toString().equals(""))
                    {
                        editor.putString(ChooSaveDataPref.ACCOUNT_HOLDER,"");
                    }
                    else {
                        editor.putString(ChooSaveDataPref.ACCOUNT_HOLDER, accountholder.getText().toString());
                    }
                    editor.putBoolean(ChooSaveDataPref.IS_BAHN_TIME_CARD, bahnValue);
                    if(prefs.getBoolean(ChooSaveDataPref.IS_BAHN_TIME_CARD, false)) {
                        if (cardType.getText().toString().equals("")) {
                        } else {
                            editor.putString(ChooSaveDataPref.TYPE_OF_CARD, cardType.getText().toString());
                        }
                        if (classtype.getText().toString().equals("")) {
                        } else {
                            editor.putString(ChooSaveDataPref.TYPE_OF_CLASS, classtype.getText().toString());
                        }
                        if (cardnumber.getText().toString().equals("")) {
                        } else {
                            editor.putString(ChooSaveDataPref.CARD_NUMBER, cardnumber.getText().toString());
                        }
                        if (expdate.getText().toString().equals("")) {
                        } else {
                            editor.putString(ChooSaveDataPref.EXPIRE_DATE, expdate.getText().toString());
                        }
                        if (dob.getText().toString().equals("")) {
                        } else {
                            editor.putString(ChooSaveDataPref.DATE_OF_BIRTH, dob.getText().toString());
                        }
                    }
                    editor.commit();
                    saveData();
//                    showSaveDataPopup(getActivity());
//                    layout_MainMenu.getForeground().setAlpha(220); // dim
                }
                else{
                    email.setText("");
                    Toast.makeText(getActivity().getApplicationContext(),R.string.invalidEmail,Toast.LENGTH_LONG).show();
                }
            }
        });
        txtSelectTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTitleDialog();
            }
        });
        reclaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RefundCheck fragment = new RefundCheck();
                fragmentTransaction.replace(R.id.realtabcontent, fragment, "RefundCheck");
                fragmentTransaction.commit();
                editor1.putString(ChooPref.BACK, "k");
                editor1.commit();
            }
        });
        txtSubmissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submissionlayout.setVisibility(View.VISIBLE);
                ParseQuery<ParseObject> query = ParseQuery.getQuery("TripData");
                query.whereEqualTo("createdBy", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> ticketList, ParseException e) {
                        if (e == null) {
                            Singleton.getInstance().getTicketListTemp().clear();
                            Log.e("TicketListTemp().size",""+ Singleton.getInstance().getTicketListTemp().size());
                            Log.d("score", "Retrieved " + ticketList.size() + " scores");
                            if(prefs1.getString(ChooPref.STATION, null) != null)
                            {
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                String currentdate = df.format(Calendar.getInstance().getTime());
                                ParseObject v = new ParseObject("TripData");
                                v.put("createdAt",currentdate + "");
                                v.put("DESTINATION",prefs1.getString(ChooPref.STATION, null));
                                if(prefs.getString(ChooSaveDataPref.TYPE_OF_CARD, null) == null){
                                    v.put("USED_CARD_TYPE","BahnCard100");
                                }else{
                                    v.put("USED_CARD_TYPE",prefs.getString(ChooSaveDataPref.TYPE_OF_CARD, null));
                                }

                                v.put("MINUTES_LATE", prefs1.getInt(ChooPref.MINUTES_DELAYED, 0));
                                v.put("ORDER_ID","");
                                Singleton.getInstance().getTicketListTemp().add(v);

                            }
                            Log.d("score", "Retrieved " + ticketList.size() + " scores");
                            Singleton.getInstance().getTicketListTemp().addAll(ticketList);
                            if (Singleton.getInstance().getTicketListTemp().size() == 0) {
                                if(prefs1.getString(ChooPref.STATION, null) != null)
                                {
                                    submission.setVisibility(View.GONE);
                                }else {
                                    submission.setVisibility(View.VISIBLE);
                                }
                            }else {
                                lstSubmissions.setVisibility(View.VISIBLE);
                                ticketsAdpater = new TicketsAdpater(getActivity(), Singleton.getInstance().getTicketListTemp(), getResources());
                                lstSubmissions.setAdapter(ticketsAdpater);
                            }
                        } else {
                            Log.d("score", "Error: " + e.getMessage());
                        }
                    }
                });
                txtSubmissions.setBackgroundColor(getResources().getColor(R.color.app_theme));
                txtSubmissions.setTextColor(Color.parseColor("#ffffff"));
                personaldata.setBackgroundColor(Color.parseColor("#ffffff"));
                personaldata.setTextColor(getResources().getColor(R.color.app_theme));
                personallayout.setVisibility(View.GONE);
                Sticky.setVisibility(View.GONE);
            }
        });
        personaldata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personaldata.setBackgroundColor(getResources().getColor(R.color.app_theme));
                personaldata.setTextColor(Color.parseColor("#ffffff"));
                txtSubmissions.setBackgroundColor(Color.parseColor("#ffffff"));
                txtSubmissions.setTextColor(getResources().getColor(R.color.app_theme));
                personallayout.setVisibility(View.VISIBLE);
                submissionlayout.setVisibility(View.GONE);
                Sticky.setVisibility(View.VISIBLE);

            }
        });
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
            expdate.setText(sdf.format(myCalendar1.getTime()));
        }
        else if(id == 1){
            dob.setText(sdf.format(myCalendar.getTime()));
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
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public int getInt(String month)
    {
        int mon = 0;
        if(month.equals("Jan")){
            mon = 1;
        }else if(month.equals("Feb"))
        {
            mon = 2;
        }else if(month.equals(getString(R.string.march)))
        {
            mon = 3;
        }else if(month.equals("Apr"))
        {
            mon = 4;
        }else if(month.equals(getString(R.string.may)))
        {
            mon = 5;
        }else if(month.equals("Jun"))
        {
            mon = 6;
        }else if(month.equals("Jul"))
        {
            mon = 7;
        }else if(month.equals("Aug"))
        {
            mon = 8;
        }else if(month.equals("Sep"))
        {
            mon = 9;
        }else if(month.equals(getString(R.string.october)))
        {
            mon = 10;
        }else if(month.equals("Nov"))
        {
            mon = 11;
        }else if(month.equals(getString(R.string.december)))
        {
            mon = 12;
        }
        return mon;
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
    public static Date addMonths(Date date, int months)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months); //minus number would decrement the days
        return cal.getTime();
    }
    public static Date subtractYear(Date date, int years)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years); //minus number would decrement the days
        return cal.getTime();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    // The method that displays the popup.
    private void showSaveDataPopup(final Activity context) {
        // Inflate the load_last_data_popup_layout_popup_layout.xml
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.saved_mydata_popup_layout, null);

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
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_data_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    private void saveData() {
        try{
            ParseUser parseUser = ParseUser.getCurrentUser();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            Log.e("getCurrentUser", "" + ParseUser.getCurrentUser());
            Log.e("parseUser.getObjectId()", "" + parseUser.getObjectId());
            // Retrieve the object by id
            query.getInBackground(parseUser.getObjectId(), new GetCallback<ParseObject>() {
                public void done(ParseObject user, ParseException e) {
                    if (e == null) {
                        Log.e("ID" + user.getObjectId(), "Username" + user.getString("FIRSTNAME"));
                        ParseUser parseUser1 = ParseUser.getCurrentUser();
                        // Now let's update it with some new data. In this case, only cheatMode and score
                        // will get sent to the Parse Cloud. playerName hasn't changed.
                        user.increment("RunCount");
                        if (prefs.getString(ChooSaveDataPref.TITLE, null) != null) {
                            user.put("GENDER", prefs.getString(ChooSaveDataPref.TITLE, null));
                        }
                        if (prefs.getString(ChooSaveDataPref.FIRST_NAME, null) != null) {
                            user.put("FIRSTNAME", prefs.getString(ChooSaveDataPref.FIRST_NAME, null));
                        }
                        if (prefs.getString(ChooSaveDataPref.NAME, null) != null) {
                            user.put("LASTNAME", prefs.getString(ChooSaveDataPref.NAME, null));
                        }
                        if (prefs.getString(ChooSaveDataPref.EMAIL, null) != null) {
                            user.put("email", prefs.getString(ChooSaveDataPref.EMAIL, null));
                        }
                        if (prefs.getString(ChooSaveDataPref.STREET, null) != null) {
                            user.put("STREET", prefs.getString(ChooSaveDataPref.STREET, null));
                        }
                        if (prefs.getString(ChooSaveDataPref.ADDRESS, null) != null) {
                            user.put("STREETADDITION", prefs.getString(ChooSaveDataPref.ADDRESS, null));
                        }
                        if (prefs.getString(ChooSaveDataPref.HOUSE_NUMBER, null) != null) {
                            user.put("STREETNUMBER", prefs.getString(ChooSaveDataPref.HOUSE_NUMBER, null));
                        }
                        if (prefs.getString(ChooSaveDataPref.ZIP_CODE, null) != null) {
                            user.put("ZIPCODE", prefs.getString(ChooSaveDataPref.ZIP_CODE, null));
                        }
                        if (prefs.getString(ChooSaveDataPref.CITY, null) != null) {
                            user.put("CITY", prefs.getString(ChooSaveDataPref.CITY, null));
                        }
                        if (prefs.getString(ChooSaveDataPref.COMPANY, null) != null) {
                            user.put("COMPANY", prefs.getString(ChooSaveDataPref.COMPANY, null));
                        }
//                    user.put("BIRTHDATE", Utilities.getParseDatee(prefs.getString(ChooSaveDataPref.DATE_OF_BIRTH, null),getActivity()));
                        if (prefs.getString(ChooSaveDataPref.BANK_CODE, null) != null) {
                            user.put("ACCOUNT_BLZ", prefs.getString(ChooSaveDataPref.BANK_CODE, null));
                        }
                        if (prefs.getString(ChooSaveDataPref.ACCOUNT_HOLDER, null) != null) {
                            user.put("ACCOUNT_HOLDER_NAME", prefs.getString(ChooSaveDataPref.ACCOUNT_HOLDER, null));
                        }
//                    ParseRelation parseRelation = parseUser1.getRelation("TRIPS");
                        if (prefs.getString(ChooSaveDataPref.ACCOUNT_NUMBER, null) != null) {
                            user.put("ACCOUNT_NUMBER", prefs.getString(ChooSaveDataPref.ACCOUNT_NUMBER, null));
                        }
//                    user.put("TRIPS", parseRelation);//Relation Trip Data

                        user.put("USED_CARD", prefs.getBoolean(ChooSaveDataPref.IS_BAHN_TIME_CARD, false));

//                    user.put("USED_CARD_EXPIRE", Utilities.getParseDatee(prefs.getString(ChooSaveDataPref.EXPIRE_DATE, null), getActivity()));

                        if (prefs.getString(ChooSaveDataPref.CARD_NUMBER, null) != null) {
                            user.put("USED_CARD_NUMBER", prefs.getString(ChooSaveDataPref.CARD_NUMBER, null));
                        }
                        if (prefs.getString(ChooSaveDataPref.TYPE_OF_CARD, null) != null) {
                            user.put("USED_CARD_TYPE", prefs.getString(ChooSaveDataPref.TYPE_OF_CARD, null));
                        }
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.e("", "Updated");
                                    showSaveDataPopup(getActivity());
                                    layout_MainMenu.getForeground().setAlpha(220); // dim

                                } else {
                                    Log.e("", "Updated exception" + e);
//                                exception = true;
                                    email.setText("");
                                    editor.putString(ChooSaveDataPref.EMAIL, "");
                                    editor.commit();
                                    if(e.getMessage().contains("the email address") && e.getMessage().contains("has already been taken"))
                                    {
                                        Toast.makeText(getActivity().getApplicationContext(),getString(R.string.emailtaken),Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });

                    } else {
                        Log.e("", "Updatiin Exception" + e);
//                    exception = true;
                    }
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
