package com.paras.choo.fragments;

import android.app.Activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.paras.choo.R;
import com.paras.choo.adapters.StationListAdapter;
import com.paras.choo.beans.StationItem;
import com.paras.choo.utils.ChooPref;
import com.paras.choo.utils.ChooSaveDataPref;
import com.paras.choo.utils.DateTime;
import com.paras.choo.utils.DateTimePicker;
import com.paras.choo.utils.SimpleDateTimePicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransitionInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransitionInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransitionInfoFragment extends Fragment implements DateTimePicker.OnDateTimeSetListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootview;
    Resources res;
    Animation shake;
    TextView selectStationtext, transitionStationtext, dtPlanned;
    StationListAdapter dataAdapter = null;
    EditText selectStation, transitionStation, trainNumValue;
    ListView lststationlist,lststationlist1;
    SharedPreferences prefs;
    SharedPreferences prefs1;
    SharedPreferences.Editor editor;
    TextView trainType;
    ArrayList<StationItem> arystationList;
    ImageView progressbar, left, right;
    TextView heading, subline1, subline2, traintypetext, trainnumtext, dtplannedtext;
    Typeface headlinefont, sublinefont, fieldsfont;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter dot1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransitionInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransitionInfoFragment newInstance(String param1, String param2) {
        TransitionInfoFragment fragment = new TransitionInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TransitionInfoFragment() {
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
        rootview = inflater.inflate(R.layout.fragment_transition_info, container, false);
        headlinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");
        fieldsfont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        sublinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        progressbar = (ImageView)rootview.findViewById(R.id.progress2);
        left = (ImageView)rootview.findViewById(R.id.left);
        right = (ImageView)rootview.findViewById(R.id.right);
        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        lststationlist = (ListView) rootview.findViewById(R.id.stationlist);
        lststationlist1 = (ListView) rootview.findViewById(R.id.stationlist1);
        selectStationtext = (TextView)rootview.findViewById(R.id.selectStationtext);
        selectStation = (EditText)rootview.findViewById(R.id.selectStation);
        transitionStation = (EditText)rootview.findViewById(R.id.transitionStation);
        heading = (TextView)rootview.findViewById(R.id.heading);
        subline1 = (TextView)rootview.findViewById(R.id.subline1);
        subline2 = (TextView)rootview.findViewById(R.id.subline2);
        traintypetext = (TextView)rootview.findViewById(R.id.traintypetext);
        trainnumtext = (TextView)rootview.findViewById(R.id.trainnumtext);
        dtplannedtext = (TextView)rootview.findViewById(R.id.dtplannedtext);
        transitionStationtext = (TextView)rootview.findViewById(R.id.transitionStationtext);
        trainNumValue = (EditText)rootview.findViewById(R.id.trainnumvalue);
        dtPlanned = (TextView)rootview.findViewById(R.id.dtplanned);
        trainType = (TextView)rootview.findViewById(R.id.traintype);
        prefs = getActivity().getSharedPreferences(ChooPref.CHOO_PREF,getActivity().MODE_PRIVATE);
        prefs1 = getActivity().getSharedPreferences(ChooSaveDataPref.CHOO_SAVEDATA_PREF,getActivity().MODE_PRIVATE);
        editor = getActivity().getSharedPreferences(ChooPref.CHOO_PREF, getActivity().MODE_PRIVATE).edit();
        heading.setTypeface(headlinefont);
        subline1.setTypeface(sublinefont);
        subline2.setTypeface(sublinefont);
        traintypetext.setTypeface(fieldsfont);
        trainType.setTypeface(fieldsfont);
        trainNumValue.setTypeface(fieldsfont);
        trainnumtext.setTypeface(fieldsfont);
        dtPlanned.setTypeface(fieldsfont);
        dtplannedtext.setTypeface(fieldsfont);
        selectStation.setTypeface(fieldsfont);
        selectStationtext.setTypeface(fieldsfont);
        transitionStation.setTypeface(fieldsfont);
        transitionStationtext.setTypeface(fieldsfont);
        arystationList = new ArrayList<StationItem>();
        res = getResources();
        final String[] station = res.getStringArray(R.array.station);
        for(int i = 0;i < station.length; i++){
            arystationList.add(new StationItem(station[i]));
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressbar.setImageResource(R.mipmap.progress_bar3);
            }
        },100);
        if(prefs.getString(ChooPref.TRANS_PLANNED_DEPT,null) == null) {
            dtPlanned.setText(prefs.getString(ChooPref.PLANED_ARRIVAL, null));
        }
        else{
            dtPlanned.setText(prefs.getString(ChooPref.TRANS_PLANNED_DEPT, null));
        }
        if(prefs.getString(ChooPref.TRANS_TRAIN_TYPE,null) != null)
        {
            trainType.setText(prefs.getString(ChooPref.TRANS_TRAIN_TYPE,null));
        }
        if(prefs.getString(ChooPref.TRANS_TRAIN_NUMBER,null) != null)
        {
            trainNumValue.setText(prefs.getString(ChooPref.TRANS_TRAIN_NUMBER,null));
        }
        if(prefs.getString(ChooPref.TRANS_STATION,null) != null)
        {
            selectStation.setText(prefs.getString(ChooPref.TRANS_STATION,null));
        }
        if(prefs.getString(ChooPref.LAST_TRANSITION,null) != null)
        {
            transitionStation.setText(prefs.getString(ChooPref.LAST_TRANSITION,null));
        }
        dtPlanned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dtPlanned.setTextColor(Color.parseColor("#dbdbdb"));
                SimpleDateTimePicker simpleDateTimePicker = SimpleDateTimePicker.make(
                        "Set Date & Time Title",
                        new Date(),
                        TransitionInfoFragment.this,
                        getActivity().getSupportFragmentManager()
                );
                // Show It baby!
                simpleDateTimePicker.show();
            }
        });
        dtPlanned.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
            }
        });
        trainNumValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                trainNumValue.setSelection(trainNumValue.getText().length());
                trainNumValue.setCursorVisible(true);
            }
        });
        trainNumValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trainNumValue.setSelection(trainNumValue.getText().length());
                trainNumValue.setCursorVisible(true);
            }
        });
        trainType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainType.setTextColor(Color.parseColor("#dbdbdb"));
//                showPopup(getActivity());
                PopupMenu popupMenu = new PopupMenu(getActivity(),v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ICE:
                                trainType.setText("ICE");
                                trainType.setTextColor(Color.parseColor("#309DC5"));
//                                editor.putString(ChooPref.TRAIN_TYPE,"ICE");
//                                editor.commit();
                                return true;
                            case R.id.IC:
                                trainType.setText("IC");
                                trainType.setTextColor(Color.parseColor("#309DC5"));
//                                editor.putString(ChooPref.TRAIN_TYPE,"IC");
//                                editor.commit();
                                return true;
                            case R.id.IRE:
                                trainType.setText("IRE");
                                trainType.setTextColor(Color.parseColor("#309DC5"));
//                                editor.putString(ChooPref.TRAIN_TYPE,"IRE");
//                                editor.commit();
                                return true;
                            case R.id.RE:
                                trainType.setText("RE");
                                trainType.setTextColor(Color.parseColor("#309DC5"));
//                                editor.putString(ChooPref.TRAIN_TYPE,"RE");
//                                editor.commit();
                                return true;
                            case R.id.RB:
                                trainType.setText("RB");
                                trainType.setTextColor(Color.parseColor("#309DC5"));
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
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(ChooPref.TRANS_TRAIN_TYPE,trainType.getText().toString());
                editor.putString(ChooPref.TRANS_TRAIN_NUMBER,trainNumValue.getText().toString());
                editor.putString(ChooPref.TRANS_PLANNED_DEPT,dtPlanned.getText().toString());
                editor.putString(ChooPref.TRANS_STATION,selectStation.getText().toString());
                editor.putString(ChooPref.LAST_TRANSITION,transitionStation.getText().toString());
                editor.commit();
                if(selectStation.getText().toString().equals("")) {

                    if (trainNumValue.getText().toString().equals("")) {
                        rootview.findViewById(R.id.selectStation).startAnimation(shake);
                        rootview.findViewById(R.id.trainnumvalue).startAnimation(shake);
                    } else {
                        rootview.findViewById(R.id.selectStation).startAnimation(shake);
                    }

                } else if (trainNumValue.getText().toString().equals("")) {
                    rootview.findViewById(R.id.trainnumvalue).startAnimation(shake);
                } else {
                    if(prefs1.getBoolean(ChooSaveDataPref.IS_BAHN_TIME_CARD,false)) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        BahnTimeCardFragment fragment = new BahnTimeCardFragment();
                        fragmentTransaction.replace(R.id.realtabcontent, fragment, "BahnTimeCard").addToBackStack(null);
                        fragmentTransaction.commit();
                    }else{
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        PersonalAndBankDataFragment fragment = new PersonalAndBankDataFragment();
                        fragmentTransaction.replace(R.id.realtabcontent, fragment, "PersonalAndBankData").addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
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
                transitionStationtext.setTextColor(Color.parseColor("#309DC5"));
                dataAdapter = new StationListAdapter(getActivity(), arystationList, res);
                lststationlist1.setAdapter(dataAdapter);
                lststationlist1.setVisibility(View.VISIBLE);
                transitionStation.setSelection(transitionStation.getText().toString().length());
                transitionStation.setCursorVisible(true);

            }
        });
        transitionStation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    transitionStationtext.setTextColor(Color.parseColor("#309DC5"));
                    dataAdapter = new StationListAdapter(getActivity(), arystationList, res);
                    lststationlist1.setAdapter(dataAdapter);
                    lststationlist1.setVisibility(View.VISIBLE);
                    transitionStation.setSelection(transitionStation.getText().toString().length());
                    transitionStation.setCursorVisible(true);
                }

            }
        });
        lststationlist1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    transitionStation.setText(dataAdapter.
                            filterArray().get(position).getStationName());
                } catch (Exception e) {
                    transitionStation.setText(arystationList.get(position).getStationName());
                }
                transitionStationtext.setTextColor(Color.parseColor("#000000"));


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
                    selectStationtext.setTextColor(Color.parseColor("#309DC5"));
                    dataAdapter = new StationListAdapter(getActivity(), arystationList, res);
                    lststationlist.setAdapter(dataAdapter);
                    lststationlist.setVisibility(View.VISIBLE);
                    selectStation.setSelection(selectStation.getText().toString().length());
                    selectStation.setCursorVisible(true);
                } else {
//                    selectStation.setSelection(selectStation.getText().toString().length());
//                    selectStation.setCursorVisible(true);
                }

            }
        });

        selectStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStationtext.setTextColor(Color.parseColor("#309DC5"));
                dataAdapter = new StationListAdapter(getActivity(), arystationList, res);
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
                selectStationtext.setTextColor(Color.parseColor("#000000"));

//                    editor.putString(ChooPref.STATION,arystationList.get(position).getStationName());
//                    editor.commit();
//                    Log.e("STATION",prefs.getString(ChooPref.STATION,null));
                lststationlist.setVisibility(View.GONE);
                selectStation.setSelection(selectStation.getText().toString().length());
                selectStation.setCursorVisible(true);


            }
        });
        trainNumValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    trainNumValue.setSelection(trainNumValue.getText().toString().length());
                    trainNumValue.setCursorVisible(false);
                }
                return false;
            }
        });
        transitionStation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    transitionStationtext.setTextColor(Color.parseColor("#000000"));
                    lststationlist1.setVisibility(View.GONE);
                    transitionStation.setSelection(transitionStation.getText().toString().length());
                    transitionStation.setCursorVisible(false);
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
                    selectStationtext.setTextColor(Color.parseColor("#000000"));
                    lststationlist.setVisibility(View.GONE);
//                    lststationlist.setSelection(transitionStation.getText().toString().length());
                    selectStation.setSelection(selectStation.getText().toString().length());
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
        inflater.inflate(R.menu.menu_transition_info, menu);
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
        dtPlanned.setText(mDateTime.getDateString());
        dtPlanned.setTextColor(Color.parseColor("#309DC5"));
//            editor.putString(ChooPref.PLANED_ARRIVAL,mDateTime.getDateString());
//            editor.commit();
        Log.e("Planned",prefs.getString(ChooPref.PLANED_ARRIVAL,null));
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
