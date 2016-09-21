package com.paras.choo.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.paras.choo.R;
import com.paras.choo.TermsAndCondSendReqActivity;
import com.paras.choo.utils.ChooPref;
import com.paras.choo.utils.ChooSaveDataPref;
import com.paras.choo.utils.FileUtils;
import com.paras.choo.utils.Utilities;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SendRequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SendRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendRequestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootview;
    Typeface headlinefont, fieldsfont;
    ImageView progressbar, left;
    Switch agreeswitch;
    ImageView sendreq;
    EditText promoCode;
    TextView heading, text1, text2, text3, promocodetext, agreeswitchtext;
    SharedPreferences prefs;
    FrameLayout layout_MainMenu;
    SharedPreferences prefs1;
    int position;
    Boolean exception = false;
    Boolean isAGBaccept = false;
    int k=0;
    ProgressDialog p;
    public static int REQUEST_CODE = 4;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter dot1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SendRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SendRequestFragment newInstance(String param1, String param2) {
        SendRequestFragment fragment = new SendRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SendRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        layout_MainMenu = (FrameLayout) getActivity().findViewById( R.id.realtabcontent);
        layout_MainMenu.getForeground().setAlpha(0);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_send_request, container, false);
        headlinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");
        fieldsfont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        left = (ImageView)rootview.findViewById(R.id.left);
        progressbar = (ImageView)rootview.findViewById(R.id.progress8);
        agreeswitch = (Switch)rootview.findViewById(R.id.agreeswitch);
        sendreq = (ImageView)rootview.findViewById(R.id.sendreq);
        promoCode = (EditText)rootview.findViewById(R.id.promocode);
        heading = (TextView)rootview.findViewById(R.id.heading);
        text1 = (TextView)rootview.findViewById(R.id.text1);
        text2 = (TextView)rootview.findViewById(R.id.text2);
        text3 = (TextView)rootview.findViewById(R.id.text3);
        promocodetext = (TextView)rootview.findViewById(R.id.promocodetext);
        agreeswitchtext = (TextView)rootview.findViewById(R.id.agreeswitchtext);
        prefs = getActivity().getSharedPreferences(ChooSaveDataPref.CHOO_SAVEDATA_PREF,getActivity().MODE_PRIVATE);
        prefs1 = getActivity().getSharedPreferences(ChooPref.CHOO_PREF,getActivity().MODE_PRIVATE);
        heading.setTypeface(headlinefont);
        text1.setTypeface(fieldsfont);
        text2.setTypeface(fieldsfont);
        text3.setTypeface(fieldsfont);
        promocodetext.setTypeface(fieldsfont);
        promoCode.setTypeface(fieldsfont);
        agreeswitchtext.setTypeface(fieldsfont);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        agreeswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAGBaccept = isChecked;
                    startActivityForResult(new Intent(getActivity(), TermsAndCondSendReqActivity.class), REQUEST_CODE);
                } else {
                    isAGBaccept = isChecked;
                    sendreq.setImageResource(R.mipmap.send_requestgrey);
                }
            }
        });
        sendreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAGBaccept){
                    if (isNetworkAvailable()) {
                        p = new ProgressDialog(getActivity());
                        p.setCancelable(false);
                        p.setMessage("Submitting Request...");
                        p.show();
                        layout_MainMenu.getForeground().setAlpha(220);
//                        ProgressDialog progressDialog = new ProgressDialog(getActivity());
                        byte[] b = null;//= Base64.decode(prefs1.getString(ChooPref.TICKET_IMAGE, null), Base64.DEFAULT);
                        try {

//                            progressDialog.setTitle("Scaling Image");
//                            progressDialog.show();
                            Bitmap bmp =  Utilities.decodeUri(getActivity(),Uri.parse(prefs1.getString(ChooPref.SELECTED_IMAGE_URI, null)),1654,2338);
//                            ExifInterface ei = null;
//                            try {
//                                ei = new ExifInterface(FileUtils.getPath(getActivity(), Uri.parse(prefs1.getString(ChooPref.SELECTED_IMAGE_URI, null))));
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//
//                            switch(orientation) {
//                                case ExifInterface.ORIENTATION_ROTATE_90:
//                                    Utilities.rotateImage(bmp, 90);
//                                    break;
//                                case ExifInterface.ORIENTATION_ROTATE_180:
//                                    Utilities.rotateImage(bmp, 180);
//                                    break;
//                                // etc.
//                            }

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            b = stream.toByteArray();
//                            progressDialog.dismiss();
                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                            progressDialog.dismiss();
                        }catch (Exception e){
                            e.printStackTrace();
//                            progressDialog.dismiss();
                        }
//                        InputStream iStream = null;
//                        try {
//                            iStream = getActivity().getContentResolver().openInputStream(Uri.parse(prefs1.getString(ChooPref.SELECTED_IMAGE_URI, null)));
//                            try {
//                                b = Utilities.getBytes(iStream);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }

//                    Log.e("","prefs1.getString(ChooPref.TICKET_IMAGE, null)"+prefs1.getString(ChooPref.TICKET_IMAGE, null));
//                        Bitmap bitmapOfScreen = BitmapFactory.decodeByteArray(b, 0, b.length);
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        bitmapOfScreen.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                        byte[] byteArrayOfScreenShot = stream.toByteArray();

                        final ParseFile imgFile = new ParseFile(System.currentTimeMillis() + ".png",
                                b);

                        ParseUser parseUser = ParseUser.getCurrentUser();
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                        // Retrieve the object by id
                        query.getInBackground(parseUser.getObjectId(), new GetCallback<ParseObject>() {
                            public void done(ParseObject user, ParseException e) {
                                if (e == null) {
                                    Log.e("ID" + user.getObjectId(), "Username");
                                    ParseUser parseUser1 = ParseUser.getCurrentUser();
                                    // Now let's update it with some new data. In this case, only cheatMode and score
                                    // will get sent to the Parse Cloud. playerName hasn't changed.
                                    ParseRelation parseRelation = parseUser1.getRelation("TRIPS");
                                    user.put("TRIPS", parseRelation);//Relation Trip Data
                                    user.put("GENDER", prefs.getString(ChooSaveDataPref.TITLE, null));
                                    user.put("FIRSTNAME", prefs.getString(ChooSaveDataPref.FIRST_NAME, null));
                                    user.put("LASTNAME", prefs.getString(ChooSaveDataPref.NAME, null));
                                    user.put("email", prefs.getString(ChooSaveDataPref.EMAIL, null));
                                    user.put("STREET", prefs.getString(ChooSaveDataPref.STREET, null));
                                    user.put("STREETADDITION", prefs.getString(ChooSaveDataPref.ADDRESS, null));
                                    user.put("STREETNUMBER", prefs.getString(ChooSaveDataPref.HOUSE_NUMBER, null));
                                    user.put("ZIPCODE", prefs.getString(ChooSaveDataPref.ZIP_CODE, null));
                                    user.put("CITY", prefs.getString(ChooSaveDataPref.CITY, null));
                                    user.put("COMPANY", prefs.getString(ChooSaveDataPref.COMPANY, null));
                                    try {
                                        Log.e("DATE_OF_BIRTH pREFS", "" + prefs.getString(ChooSaveDataPref.DATE_OF_BIRTH, null));
                                        Log.e("DATE_OF_BIRTH", "" + Utilities.getParseDatee(prefs.getString(ChooSaveDataPref.DATE_OF_BIRTH, null), getActivity()));
                                        if (prefs.getString(ChooSaveDataPref.DATE_OF_BIRTH, null) != null) {
                                            user.put("BIRTHDATE", Utilities.getParseDatee(prefs.getString(ChooSaveDataPref.DATE_OF_BIRTH, null), getActivity()));
                                        }
                                    }catch (Exception e2)
                                    {
                                        e2.printStackTrace();
                                    }
                                    user.put("ACCOUNT_BLZ", prefs.getString(ChooSaveDataPref.BANK_CODE, null));
                                    user.put("ACCOUNT_HOLDER_NAME", prefs.getString(ChooSaveDataPref.ACCOUNT_HOLDER, null));
                                    Log.e("ACCOUNT_HOLDER_NAME", prefs.getString(ChooSaveDataPref.ACCOUNT_HOLDER, null));
                                    user.put("ACCOUNT_NUMBER", prefs.getString(ChooSaveDataPref.ACCOUNT_NUMBER, null));
                                    user.put("USED_CARD", prefs.getBoolean(ChooSaveDataPref.IS_BAHN_TIME_CARD, false));
                                    Log.e("USED_CARD_EXPIRE", "" + Utilities.getParseDatee(prefs.getString(ChooSaveDataPref.EXPIRE_DATE, null), getActivity()));
                                    if(prefs.getString(ChooSaveDataPref.EXPIRE_DATE,null) != null) {
                                        user.put("USED_CARD_EXPIRE", Utilities.getParseDatee(prefs.getString(ChooSaveDataPref.EXPIRE_DATE, null), getActivity()));
                                    }
                                    user.put("USED_CARD_NUMBER", prefs.getString(ChooSaveDataPref.CARD_NUMBER, null));
                                    user.put("USED_CARD_TYPE", prefs.getString(ChooSaveDataPref.TYPE_OF_CARD, null));


                                    user.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Log.e("", "Updated");
                                            } else {
                                                Log.e("", "Updated exception" + e);
                                                exception = true;
                                            }
                                        }
                                    });

                                } else {
                                    Log.e("", "Updatiin Exception" + e);
                                    exception = true;
                                }
                            }
                        });

                        imgFile.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.e("prefs1" + prefs1, "prefs1.getString(ChooPref.TRAIN_NUMBER, null)" + prefs1.getString(ChooPref.TRAIN_NUMBER, null));
                                    final ParseObject object = new ParseObject("TripData");

                                    object.put("ACT_ARRIVAL", Utilities.getParseDate(prefs1.getString(ChooPref.ACTUAL_ARRIVAL, null)));
                                    Log.e("ACT_ARRIVAL", "" + Utilities.getParseDate(prefs1.getString(ChooPref.ACTUAL_ARRIVAL, null)));
                                    // DELAYED
                                    if(prefs1.getBoolean(ChooPref.IS_TRANSITION, false)) {
                                        object.put("DELAYED_TRAIN_NUMBER", prefs1.getString(ChooPref.TRANS_TRAIN_NUMBER, null));
                                        object.put("DELAYED_TRAIN_TYPE", prefs1.getString(ChooPref.TRANS_TRAIN_TYPE, null));
                                        object.put("MISSED_TRAIN_AT_STATION", prefs1.getString(ChooPref.TRANS_STATION, null));
                                        object.put("DELAYED_TRAIN_DEPARTURE", Utilities.getParseDate(prefs1.getString(ChooPref.TRANS_PLANNED_DEPT, null)));
                                        object.put("LAST_SWITCHED_AT_STATION", prefs1.getString(ChooPref.LAST_TRANSITION, null));
                                    }
                                    object.put("DEPARTURE", prefs1.getString(ChooPref.DEPARTURE_STATION, null));
                                    object.put("DESTINATION", prefs1.getString(ChooPref.STATION, null));
                                    object.put("DEST_TRAIN_NUMBER", prefs1.getString(ChooPref.TRAIN_NUMBER, null));
                                    object.put("DEST_TRAIN_TYPE", prefs1.getString(ChooPref.TRAIN_TYPE, null));
//                                object.put("EVENT_ID", "EVENT_ID");
                                    object.put("createdAt", new Date());

                                    object.put("EXP_ARRIVAL",Utilities.getParseDate(prefs1.getString(ChooPref.PLANED_ARRIVAL, null)));

                                    object.put("EXP_DEPARTURE",Utilities.getParseDate(prefs1.getString(ChooPref.PLANNED_DEPARTURE, null)));
                                    JSONObject ticketObject = new JSONObject();
                                    try {
                                        ticketObject.put("TICKET_ADDITIONS", false);
                                        ticketObject.put("TICKET_PRICE", prefs1.getString(ChooPref.SELECT_PRICE, null));
                                        ticketObject.put("USED_CARD", prefs.getBoolean(ChooSaveDataPref.IS_BAHN_TIME_CARD, false));
                                        JSONObject date = new JSONObject();
                                        date.put("_type", "Date");
                                        date.put("iso", prefs.getString(ChooSaveDataPref.DATE_OF_BIRTH, null));
                                        ticketObject.put("USED_CARD_BIRTHDATE", date);
                                        ticketObject.put("USED_CARD_CLASS", prefs.getString(ChooSaveDataPref.TYPE_OF_CLASS, null));

                                        JSONObject dateExpire = new JSONObject();
                                        dateExpire.put("_type", "Date");
                                        dateExpire.put("iso", prefs.getString(ChooSaveDataPref.EXPIRE_DATE, null));
                                        ticketObject.put("USED_CARD_EXPIRE", dateExpire);
                                        ticketObject.put("USED_CARD_NUMBER", prefs.getString(ChooSaveDataPref.CARD_NUMBER, null));
                                        ticketObject.put("USED_CARD_TYPE", prefs.getString(ChooSaveDataPref.TYPE_OF_CARD, null));


                                    } catch (JSONException e2) {
                                        e2.printStackTrace();
                                    }
                                    object.put("ticketData", ticketObject);
                                    JSONObject myObject = new JSONObject();
                                    try {
                                        myObject.put("ACCOUNT_BLZ", prefs.getString(ChooSaveDataPref.BANK_CODE, null));
                                        myObject.put("ACCOUNT_HOLDER_NAME", prefs.getString(ChooSaveDataPref.ACCOUNT_HOLDER, null));
                                        myObject.put("ACCOUNT_NUMBER", prefs.getString(ChooSaveDataPref.ACCOUNT_NUMBER, null));
                                        JSONObject birthdate = new JSONObject();
                                        birthdate.put("_type", "Date");
                                        birthdate.put("iso", prefs.getString(ChooSaveDataPref.DATE_OF_BIRTH, null));
                                        myObject.put("BIRTHDATE", birthdate);
                                        myObject.put("CITY", prefs.getString(ChooSaveDataPref.CITY, null));
                                        myObject.put("EMAIL", prefs.getString(ChooSaveDataPref.EMAIL, null));
                                        myObject.put("FIRSTNAME", prefs.getString(ChooSaveDataPref.FIRST_NAME, null));
                                        myObject.put("GENDER", prefs.getString(ChooSaveDataPref.TITLE, null));
                                        myObject.put("LASTNAME", prefs.getString(ChooSaveDataPref.NAME, null));
                                        myObject.put("STREET", prefs.getString(ChooSaveDataPref.STREET, null));
                                        myObject.put("STREETNUMBER", prefs.getString(ChooSaveDataPref.HOUSE_NUMBER, null));
                                        myObject.put("ZIPCODE", prefs.getString(ChooSaveDataPref.ZIP_CODE, null));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                    object.put("persData", myObject);
                                    JSONObject phoneData = new JSONObject();
                                    try {
                                        phoneData.put("DEVICE_TYPE",android.os.Build.MODEL +"");
                                        phoneData.put("SYSVERSION ",android.os.Build.VERSION.SDK_INT+"");
                                    }catch (JSONException e1){
                                        e1.printStackTrace();
                                    }

                                    object.put("phoneData",phoneData);
                                    object.put("createdBy", ParseUser.getCurrentUser());
                                    object.put("MINUTES_LATE", prefs1.getInt(ChooPref.MINUTES_DELAYED, 0));
                                    Log.e("ONE_WAY_CHECK", "" + prefs1.getBoolean(ChooPref.IS_TRANSITION, false));
                                    object.put("ONE_WAY_CHECK", prefs1.getBoolean(ChooPref.IS_TRANSITION, false));
                                    object.put("ORDER_ID",(100000 + new Random().nextInt(900000))+"");
                                    object.put("STATUS_ID", "1");
                                    object.put("internal_state",1);
                                    object.put("TICKET_SCAN", imgFile);
                                    if (promoCode.getText().toString().equals("")) {
                                    } else {
                                        object.put("PROMOCODE", promoCode.getText().toString());
                                    }
                                    Log.e("", "myObject" + myObject);
                                    Log.e("", "ticketObject" + ticketObject);
                                    object.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Log.e("", "User Updation ");
                                                ParseUser user = ParseUser.getCurrentUser();
                                                ParseRelation relation = user.getRelation("TRIPS");
                                                relation.add(object);
                                                user.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            Log.e("", "RELATION ADDED");
                                                        } else {
                                                            Log.e("", "RELATION NOT ADDED" + " " + e);
                                                        }
                                                    }
                                                });
                                                p.dismiss();
                                                layout_MainMenu.getForeground().setAlpha(0);
                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                RequestSentFragment fragment = new RequestSentFragment();
                                                fragmentTransaction.replace(R.id.realtabcontent, fragment, "RequestSent").addToBackStack(null);
                                                fragmentTransaction.commit();
                                            } else {
                                                Log.e("", "User Updation Exception" + e);
                                                exception = true;
                                            }
                                        }
                                    });
//                                ParseRelation parseRelation = parseUser1.getRelation("TRIPS");
//                                user.put("TRIPS", parseRelation);//Relation Trip Data
                                } else {
                                    p.setMessage("Transmission Error!");
                                    Log.e("Image Exception", "" + e);
                                    p.setMessage("Transmission Error!");
                                    exception = true;
                                    if (exception) {
                                        p.dismiss();
                                        layout_MainMenu.getForeground().setAlpha(0);
                                        showPopup(getActivity());
                                        layout_MainMenu.getForeground().setAlpha(220);
                                    }
                                }
                            }
                        }, new ProgressCallback() {
                            public void done(Integer percentDone) {
                                // Update your progress spinner here. percentDone will be between 0 and 100.
                                p.setMessage(percentDone + "%");
                            }
                        });
//                    final ProgressDialog p = new ProgressDialog(getActivity());
//                    p.setCancelable(false);
//                    p.setMessage("Submitting Request...");
//                    p.show();
//                    layout_MainMenu.getForeground().setAlpha( 220);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            if(exception)
//                            {
//                                p.setMessage("Transmission error!");
//                            new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                p.setMessage("Submitting Request...");
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        p.setMessage("Transmission error!");
//                                        new Handler().postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                p.dismiss();
//                                                layout_MainMenu.getForeground().setAlpha(0);
//                                                showPopup(getActivity());
//                                                layout_MainMenu.getForeground().setAlpha(220);
//                                            }
//                                        }, 2000);
//                                    }
//                                },20000);
//
//                            }
//                            },2000);
//                            }
//                            else {
//                                p.dismiss();
//                                layout_MainMenu.getForeground().setAlpha(0);
//                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                RequestSentFragment fragment = new RequestSentFragment();
//                                fragmentTransaction.replace(R.id.realtabcontent, fragment, "RequestSent").addToBackStack(null);
//                                fragmentTransaction.commit();
//                            }
//                        }
//                    }, 20000);
                    } else {
                        showCautionPopup(getActivity());
                        layout_MainMenu.getForeground().setAlpha(220); // dim
                    }
                }
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressbar.setImageResource(R.mipmap.progress_bar9);
            }
        }, 100);
        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(intent == null){
            agreeswitch.setChecked(false);
            isAGBaccept = false;
            sendreq.setImageResource(R.mipmap.send_requestgrey);
            return;
        }
        Bundle extras = intent.getExtras();
        if(extras != null)
            position = extras.getInt("position",0);
        if(position == 0)
        {
            agreeswitch.setChecked(false);
            isAGBaccept = false;
            sendreq.setImageResource(R.mipmap.send_requestgrey);
        }
        else{
            sendreq.setImageResource(R.mipmap.send_request);

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
        inflater.inflate(R.menu.menu_send_request_info, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    // The method that displays the popup.
    private void showCautionPopup(final Activity context) {
        // Inflate the load_last_data_popup_layoutta_popup_layout.xml
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.caution_check_internet_con, null);

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
    private void showPopup(final Activity context) {
        // Inflate the load_last_data_popup_layoutta_popup_layout.xml
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.submission_error_caution_popup, null);

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

}
