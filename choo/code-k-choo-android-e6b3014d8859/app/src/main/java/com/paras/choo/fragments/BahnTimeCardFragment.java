package com.paras.choo.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.paras.choo.R;
import com.paras.choo.utils.ChooSaveDataPref;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BahnTimeCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BahnTimeCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BahnTimeCardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootview;
    int id;
    Animation shake;
    TextView cardtype, expdate, dob, classtype;
    EditText cardnum;
    Calendar myCalendar;
    Calendar myCalendar1;
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener date1;
    ImageView progressbar, left, right;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    Typeface headlinefont, sublinefont, fieldsfont;
    TextView heading, subline1, cardtypetext, classtypetext, cardnumtext, expdatetext, dobtext;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter dot1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BahnTimeCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BahnTimeCardFragment newInstance(String param1, String param2) {
        BahnTimeCardFragment fragment = new BahnTimeCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BahnTimeCardFragment() {
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
        rootview = inflater.inflate(R.layout.fragment_bahn_time_card, container, false);
        headlinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");
        fieldsfont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        sublinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        progressbar = (ImageView)rootview.findViewById(R.id.progress3);
        left = (ImageView)rootview.findViewById(R.id.left);
        right = (ImageView)rootview.findViewById(R.id.right);
        cardtype = (TextView)rootview.findViewById(R.id.cardtype);
        classtype = (TextView)rootview.findViewById(R.id.classtype);
        expdate = (TextView)rootview.findViewById(R.id.expDate);
        dob = (TextView)rootview.findViewById(R.id.dateofbirth);
        cardnum = (EditText)rootview.findViewById(R.id.cardnumvalue);
        heading = (TextView)rootview.findViewById(R.id.heading);
        subline1 = (TextView)rootview.findViewById(R.id.subline1);
        cardtypetext = (TextView)rootview.findViewById(R.id.cardtypetext);
        classtypetext = (TextView)rootview.findViewById(R.id.classtypetext);
        cardnumtext = (TextView)rootview.findViewById(R.id.cardnumtext);
        expdatetext = (TextView)rootview.findViewById(R.id.expDatetext);
        dobtext = (TextView)rootview.findViewById(R.id.dateofbirthtext);
        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        prefs = getActivity().getSharedPreferences(ChooSaveDataPref.CHOO_SAVEDATA_PREF, getActivity().MODE_PRIVATE);
        editor = prefs.edit();
        heading.setTypeface(headlinefont);
        subline1.setTypeface(sublinefont);
        cardtypetext.setTypeface(fieldsfont);
        cardtype.setTypeface(fieldsfont);
        classtype.setTypeface(fieldsfont);
        classtypetext.setTypeface(fieldsfont);
        cardnumtext.setTypeface(fieldsfont);
        cardnum.setTypeface(fieldsfont);
        expdatetext.setTypeface(fieldsfont);
        expdate.setTypeface(fieldsfont);
        dob.setTypeface(fieldsfont);
        dobtext.setTypeface(fieldsfont);
        myCalendar = Calendar.getInstance();
        myCalendar1 = Calendar.getInstance();

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
        if(prefs.getString(ChooSaveDataPref.TYPE_OF_CARD,null)== null)
        {}
        else
            cardtype.setText(prefs.getString(ChooSaveDataPref.TYPE_OF_CARD,null));
        if(prefs.getString(ChooSaveDataPref.CARD_NUMBER,null) == null)
        {}
        else
            cardnum.setText(prefs.getString(ChooSaveDataPref.CARD_NUMBER,null));
        if(prefs.getString(ChooSaveDataPref.TYPE_OF_CLASS,null) == null)
        {}
        else
            classtype.setText(prefs.getString(ChooSaveDataPref.TYPE_OF_CLASS,null));
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        cardnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardnum.setSelection(cardnum.getText().length());
                cardnum.setCursorVisible(true);
            }
        });
        cardnum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                cardnum.setSelection(cardnum.getText().length());
                cardnum.setCursorVisible(true);
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardnum.getText().toString().equals("")) {
                    rootview.findViewById(R.id.cardnumlayout).startAnimation(shake);
                } else {
                    editor.putString(ChooSaveDataPref.TYPE_OF_CARD, cardtype.getText().toString());
                    editor.putString(ChooSaveDataPref.TYPE_OF_CLASS, classtype.getText().toString());
                    editor.putString(ChooSaveDataPref.CARD_NUMBER, cardnum.getText().toString());
                    if (expdate.getText().toString().equals("")) {
                    } else {
                        editor.putString(ChooSaveDataPref.EXPIRE_DATE, expdate.getText().toString());
                    }
                    if (dob.getText().toString().equals("")) {
                    } else {
                        editor.putString(ChooSaveDataPref.DATE_OF_BIRTH, dob.getText().toString());
                    }
                    editor.commit();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    PersonalAndBankDataFragment fragment = new PersonalAndBankDataFragment();
                    fragmentTransaction.replace(R.id.realtabcontent, fragment, "DepartureStation").addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        cardnum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    cardnum.setSelection(cardnum.getText().toString().length());
                    cardnum.setCursorVisible(false);
                }
                return false;
            }
        });
        cardtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(),v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bahn:
                                cardtype.setText("BahnCard100");
                                cardtype.setTextColor(Color.parseColor("#309DC5"));
                                return true;
                            case R.id.timecard:
                                cardtype.setText(R.string.timecard);
                                cardtype.setTextColor(Color.parseColor("#309DC5"));
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.popup);
                popupMenu.show();
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
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 1;
                String[] DateArray = dob.getText().toString().split("-");
                Log.e("DateArray", "" + DateArray[0]+""+DateArray[1] + "" +getInt(DateArray[1]));
                new DatePickerDialog(getActivity(), date, Integer.parseInt(DateArray[2]),getInt(DateArray[1]) - 1,
                        Integer.parseInt(DateArray[0])).show();
            }
        });
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date();
        if(prefs.getString(ChooSaveDataPref.EXPIRE_DATE,null) == null) {
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
//        System.out.println(dateFormat.format(date));
        expdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 0;
                String[] DateArray = expdate.getText().toString().split("-");
                Log.e("DateArray", "" + DateArray[0]+""+DateArray[1] + "" +getInt(DateArray[1]));
                new DatePickerDialog(getActivity(), date1, Integer.parseInt(DateArray[2]),getInt(DateArray[1]) - 1,
                        Integer.parseInt(DateArray[0])).show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressbar.setImageResource(R.mipmap.progress_bar4);
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
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bahn_time_card_info, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

}
