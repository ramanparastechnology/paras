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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paras.choo.R;
import com.paras.choo.utils.ChooPref;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArrivalTrainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArrivalTrainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArrivalTrainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootview ;
    SharedPreferences.Editor editor;
    ImageView progressbar, left, right;
    SharedPreferences prefs;
    Animation shake;
    EditText trainNumber;
    TextView dtplanned, dtactual, destStation, trainType ,dtplannedtext, dtactualtext, destStationtext, trainTypetext, subline1;
    Typeface headlinefont, sublinefont, fieldsfont;
    TextView heading, trainNumbertext;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter dot1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArrivalTrainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArrivalTrainFragment newInstance(String param1, String param2) {
        ArrivalTrainFragment fragment = new ArrivalTrainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ArrivalTrainFragment() {
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
        rootview = inflater.inflate(R.layout.fragment_arrival_train, container, false);
        progressbar = (ImageView)rootview.findViewById(R.id.progress0);
        headlinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");
        fieldsfont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        sublinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        dtplannedtext = (TextView)rootview.findViewById(R.id.dtplannedtext);
        dtactualtext = (TextView)rootview.findViewById(R.id.dtactualtext);
        destStationtext = (TextView)rootview.findViewById(R.id.destStationtext);
        trainTypetext = (TextView)rootview.findViewById(R.id.traintypetext);
        subline1 = (TextView)rootview.findViewById(R.id.subline1);
        heading = (TextView)rootview.findViewById(R.id.heading);
        left = (ImageView)rootview.findViewById(R.id.left);
        trainNumbertext = (TextView)rootview.findViewById(R.id.trainnotext);
        right = (ImageView)rootview.findViewById(R.id.right);
        dtplanned = (TextView)rootview.findViewById(R.id.dtplanned);
        dtactual = (TextView)rootview.findViewById(R.id.dtactual);
        destStation = (TextView)rootview.findViewById(R.id.destStation);
        trainType = (TextView)rootview.findViewById(R.id.traintype);
        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        trainNumber = (EditText)rootview.findViewById(R.id.trainno);
        heading.setTypeface(headlinefont);
        subline1.setTypeface(sublinefont);
        destStationtext.setTypeface(fieldsfont);
        destStation.setTypeface(fieldsfont);
        dtplannedtext.setTypeface(fieldsfont);
        dtplanned.setTypeface(fieldsfont);
        dtactual.setTypeface(fieldsfont);
        dtactualtext.setTypeface(fieldsfont);
        trainTypetext.setTypeface(fieldsfont);
        trainType.setTypeface(fieldsfont);
        trainNumber.setTypeface(fieldsfont);
        trainNumbertext.setTypeface(fieldsfont);
        prefs = getActivity().getSharedPreferences(ChooPref.CHOO_PREF, getActivity().MODE_PRIVATE);
        editor = prefs.edit();
        trainNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trainNumber.setCursorVisible(true);
                trainNumber.setSelection(trainNumber.getText().length());
            }
        });
        trainNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                trainNumber.setCursorVisible(true);
                trainNumber.setSelection(trainNumber.getText().length());
            }
        });
        trainNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    trainNumber.setCursorVisible(false);
                }
                return false;
            }
        });
        if(prefs.getString(ChooPref.TRAIN_NUMBER,null) != null){
            trainNumber.setText(prefs.getString(ChooPref.TRAIN_NUMBER,null));
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressbar.setImageResource(R.mipmap.progress_bar1);
            }
        },100);
        if(prefs.getString(ChooPref.TRAIN_TYPE,null) != null)
        {
            trainType.setText(prefs.getString(ChooPref.TRAIN_TYPE,null));
        }
        else{
            trainType.setText("ICE");
        }
        destStation.setText(prefs.getString(ChooPref.STATION,null));
        dtactual.setText(prefs.getString(ChooPref.ACTUAL_ARRIVAL,null));
        dtplanned.setText(prefs.getString(ChooPref.PLANED_ARRIVAL,null));
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(ChooPref.BACK,"nopopup");
                editor.commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((trainNumber.getText().toString().equals(""))){
                    rootview.findViewById(R.id.trainNO).startAnimation(shake);
                }else {
                    editor.putString(ChooPref.TRAIN_TYPE, trainType.getText().toString());
                    editor.putString(ChooPref.TRAIN_NUMBER,trainNumber.getText().toString());
                    editor.commit();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    DepartureStationFragment fragment = new DepartureStationFragment();
                    fragmentTransaction.replace(R.id.realtabcontent, fragment, "DepartureStation").addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        trainType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               trainType.setTextColor(Color.parseColor("#dbdbdb"));
//                showPopup(getActivity());
                PopupMenu popupMenu = new PopupMenu(getActivity(),v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ICE:
                                trainType.setText("ICE");
                                trainType.setTextColor(Color.parseColor("#309DC5"));
                                editor.putString(ChooPref.TRAIN_TYPE,"ICE");
                                editor.commit();
                                return true;
                            case R.id.IC:
                                trainType.setText("IC");
                                trainType.setTextColor(Color.parseColor("#309DC5"));
                                editor.putString(ChooPref.TRAIN_TYPE,"IC");
                                editor.commit();
                                return true;
                            case R.id.IRE:
                                trainType.setText("IRE");
                                trainType.setTextColor(Color.parseColor("#309DC5"));
                                editor.putString(ChooPref.TRAIN_TYPE,"IRE");
                                editor.commit();
                                return true;
                            case R.id.RE:
                                trainType.setText("RE");
                                trainType.setTextColor(Color.parseColor("#309DC5"));
                                editor.putString(ChooPref.TRAIN_TYPE,"RE");
                                editor.commit();
                                return true;
                            case R.id.RB:
                                trainType.setText("RB");
                                trainType.setTextColor(Color.parseColor("#309DC5"));
                                editor.putString(ChooPref.TRAIN_TYPE,"RB");
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
        inflater.inflate(R.menu.menu_arrival_train_info, menu);
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
//    // The method that displays the popup.
//    private void showPopup(final Activity context) {
//        // Inflate the load_last_data_popup_layoutta_popup_layout.xml
//        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
//        LayoutInflater layoutInflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View layout = layoutInflater.inflate(R.layout.select_type_popup_layout, null);
//
//        // Creating the PopupWindow
//        final PopupWindow popup = new PopupWindow(context);
//        popup.setContentView(layout);
//        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
//        popup.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
////        popup.setFocusable(true);
//        popup.showAtLocation(layout,
//                Gravity.BOTTOM|Gravity.BOTTOM, Gravity.BOTTOM,Gravity.BOTTOM);
//        // Getting a reference to Close button, and close the popup when clicked.
//        final ListView typelist = (ListView)layout.findViewById(R.id.typelist);
//        final String[] values = new String[] {"ICE",
//                "IC",
//                "IRE",
//                "RE",
//                "RB"};
//        final ArrayList<PopupItem> arr = new ArrayList<PopupItem>();
//        for(int i=0;i < values.length;i++) {
//            PopupItem item = new PopupItem();
//            item.setStationtype(values[i]);
//            arr.add(item);
//        }
//        Resources res = getResources();
//        PopUpAdapter adapter = new PopUpAdapter(getActivity(),arr,res);
//        typelist.setAdapter(adapter);
//        typelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                trainType.setText(arr.get(position).getStationtype());
//                trainType.setTextColor(Color.parseColor("#309DC5"));
//                popup.dismiss();
//            }
//        });
//
//    }

}
