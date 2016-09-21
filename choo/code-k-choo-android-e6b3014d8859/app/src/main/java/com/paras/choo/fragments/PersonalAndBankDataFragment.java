package com.paras.choo.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paras.choo.BankAccountInfoActivity;
import com.paras.choo.R;
import com.paras.choo.utils.ChooSaveDataPref;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonalAndBankDataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonalAndBankDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalAndBankDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootview;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    ImageView progressbar, left, right;
    TextView txtSelectTitle;
    TextView firstnameText, nameText, emailText, streetText, housenumText, addressText, zipText, cityText, companyText, accountnumText, bankcodeText, accountholderText;
    EditText firstname, name, email, street, housenumber, address, zipcode, city, company, accountnumber, bankcode, accountholder;
    ImageView info;
    TextView heading, titletext, subline1, subline2;
    AlertDialog levelDialog;
    Typeface headlinefont, sublinefont, fieldsfont;
    private OnFragmentInteractionListener mListener;
    Animation shake;
    Boolean exception = false;
    com.emilsjolander.components.StickyScrollViewItems.StickyScrollView stickyScrollView = null;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter dot1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalAndBankDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalAndBankDataFragment newInstance(String param1, String param2) {
        PersonalAndBankDataFragment fragment = new PersonalAndBankDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PersonalAndBankDataFragment() {
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
        rootview = inflater.inflate(R.layout.fragment_personal_and_bank_data, container, false);
        headlinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");
        fieldsfont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        sublinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        stickyScrollView = (com.emilsjolander.components.StickyScrollViewItems.StickyScrollView)rootview.findViewById(R.id.sticky_scroll);
        progressbar = (ImageView)rootview.findViewById(R.id.progress4);
        heading = (TextView)rootview.findViewById(R.id.heading);
        titletext = (TextView)rootview.findViewById(R.id.titletext);
        subline1 = (TextView)rootview.findViewById(R.id.subline1);
        subline2 = (TextView)rootview.findViewById(R.id.subline2);
        left = (ImageView)rootview.findViewById(R.id.left);
        right = (ImageView)rootview.findViewById(R.id.right);
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
        accountholder = (EditText)rootview.findViewById(R.id.accountholder);
        info = (ImageView)rootview.findViewById(R.id.info);
        firstnameText = (TextView)rootview.findViewById(R.id.firstnameText);
        nameText = (TextView)rootview.findViewById(R.id.nameText);
        emailText = (TextView)rootview.findViewById(R.id.emailText);
        streetText = (TextView)rootview.findViewById(R.id.streetText);
        housenumText = (TextView)rootview.findViewById(R.id.housenumText);
        addressText = (TextView)rootview.findViewById(R.id.addressText);
        zipText = (TextView)rootview.findViewById(R.id.zipText);
        cityText = (TextView)rootview.findViewById(R.id.cityText);
        companyText = (TextView)rootview.findViewById(R.id.companyText);
        accountnumText = (TextView)rootview.findViewById(R.id.accountnumText);
        bankcodeText = (TextView)rootview.findViewById(R.id.bankcodeText);
        accountholderText = (TextView)rootview.findViewById(R.id.accountholderText);


        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        prefs = getActivity().getSharedPreferences(ChooSaveDataPref.CHOO_SAVEDATA_PREF, getActivity().MODE_PRIVATE);
        editor = prefs.edit();
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

        heading.setTypeface(headlinefont);
        subline1.setTypeface(sublinefont);
        subline2.setTypeface(sublinefont);
        titletext.setTypeface(fieldsfont);
        txtSelectTitle.setTypeface(fieldsfont);
        firstname.setTypeface(fieldsfont);
        firstnameText.setTypeface(fieldsfont);
        name.setTypeface(fieldsfont);
        nameText.setTypeface(fieldsfont);
        email.setTypeface(fieldsfont);
        emailText.setTypeface(fieldsfont);
        streetText.setTypeface(fieldsfont);
        street.setTypeface(fieldsfont);
        housenumber.setTypeface(fieldsfont);
        housenumText.setTypeface(fieldsfont);
        addressText.setTypeface(fieldsfont);
        address.setTypeface(fieldsfont);
        zipcode.setTypeface(fieldsfont);
        zipText.setTypeface(fieldsfont);
        city.setTypeface(fieldsfont);
        cityText.setTypeface(fieldsfont);
        company.setTypeface(fieldsfont);
        companyText.setTypeface(fieldsfont);
        accountnumber.setTypeface(fieldsfont);
        accountnumText.setTypeface(fieldsfont);
        bankcode.setTypeface(fieldsfont);
        bankcodeText.setTypeface(fieldsfont);
        accountholder.setTypeface(fieldsfont);
        accountholderText.setTypeface(fieldsfont);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        accountnumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                stickyScrollView.scrollTo(0,stickyScrollView.getMaxScrollAmount());

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
                if (txtSelectTitle.getText().toString().length() == 0) {
                    rootview.findViewById(R.id.layout_title).startAnimation(shake);

                }
                if (firstname.getText().toString().length() == 0) {
                    rootview.findViewById(R.id.layout_firstname).startAnimation(shake);

                }
                if (name.getText().toString().length() == 0) {
                    rootview.findViewById(R.id.layout_name).startAnimation(shake);

                }
                if (email.getText().toString().length() == 0) {
                    rootview.findViewById(R.id.layout_email).startAnimation(shake);

                }
                if (street.getText().toString().length() == 0) {
                    rootview.findViewById(R.id.layout_street).startAnimation(shake);

                }
                if (zipcode.getText().toString().length() == 0) {
                    rootview.findViewById(R.id.layout_zip).startAnimation(shake);

                }
                if (city.getText().toString().length() == 0) {
                    rootview.findViewById(R.id.layout_city).startAnimation(shake);
                    return;
                }
                if (accountnumber.getText().toString().length() == 0) {
                    rootview.findViewById(R.id.layout_accountno).startAnimation(shake);

                }
                if (bankcode.getText().toString().length() == 0) {
                    rootview.findViewById(R.id.layout_bankcode).startAnimation(shake);

                }
                if (housenumber.getText().toString().length() == 0) {
                    rootview.findViewById(R.id.layout_houseno).startAnimation(shake);

                }
                if (bankcode.getText().toString().length() == 0 || accountnumber.getText().toString().length() == 0) {
                    return;
                }
                if (city.getText().toString().length() == 0 || zipcode.getText().toString().length() == 0 || street.getText().toString().length() == 0) {
                    return;
                }
                if (email.getText().toString().length() == 0 || name.getText().toString().length() == 0 || firstname.getText().toString().length() == 0) {
                    return;
                }
                if (txtSelectTitle.getText().toString().length() == 0 || housenumber.getText().toString().length() == 0) {
                    return;
                }
                try{
                    ParseUser parseUser = ParseUser.getCurrentUser();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                    Log.e("getCurrentUser", "" + ParseUser.getCurrentUser());
                    Log.e("parseUser.getObjectId()", "" + parseUser.getObjectId());
                    // Retrieve the object by id
                    query.getInBackground(parseUser.getObjectId(), new GetCallback<ParseObject>() {
                        public void done(ParseObject user, ParseException e) {
                            if (e == null) {
                                if (txtSelectTitle.getText().toString().equals("")) {}
                                else {
                                    user.put("GENDER", txtSelectTitle.getText().toString());
                                }
                                if (firstname.getText().toString().equals("")) {}
                                else {
                                    user.put("FIRSTNAME", firstname.getText().toString());
                                }
                                if (name.getText().toString().equals("")) {}
                                else{
                                    user.put("LASTNAME", name.getText().toString());
                                }
                                if (email.getText().toString().equals("")) {}
                                else {
                                    user.put("email", email.getText().toString());
                                }
                                if (street.getText().toString().equals("")) {}
                                else {
                                    user.put("STREET", street.getText().toString());
                                }
                                if (address.getText().toString().equals("")) {}
                                else {
                                    user.put("STREETADDITION", address.getText().toString());
                                }
                                if (housenumber.getText().toString().equals("")) {}
                                else {
                                    user.put("STREETNUMBER", housenumber.getText().toString());
                                }
                                if (zipcode.getText().toString().equals("")) {}
                                else {
                                    user.put("ZIPCODE", zipcode.getText().toString());
                                }
                                if (city.getText().toString().equals("")) {}
                                else {
                                    user.put("CITY", city.getText().toString());
                                }
                                if (company.getText().toString().equals("")) {}
                                else {
                                    user.put("COMPANY", company.getText().toString());
                                }

                                if (bankcode.getText().toString().equals("")) {}
                                else {
                                    user.put("ACCOUNT_BLZ", bankcode.getText().toString());
                                }
                                if (accountholder.getText().toString().equals("")) {
                                    user.put("ACCOUNT_HOLDER_NAME", accountholder.getText().toString());
                                }

                                if (accountnumber.getText().toString().equals("")) {}
                                else {
                                    user.put("ACCOUNT_NUMBER", accountnumber.getText().toString());
                                }
                                user.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Log.e("", "Updated");
                                            if (isEmailValid(email.getText().toString())) {
                                                editor.putString(ChooSaveDataPref.TITLE, txtSelectTitle.getText().toString());
                                                editor.putString(ChooSaveDataPref.FIRST_NAME, firstname.getText().toString());
                                                editor.putString(ChooSaveDataPref.NAME, name.getText().toString());
                                                editor.putString(ChooSaveDataPref.EMAIL, email.getText().toString());
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
                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                TicketScanFragment fragment = new TicketScanFragment();
                                                fragmentTransaction.replace(R.id.realtabcontent, fragment, "TicketScan").addToBackStack(null);
                                                fragmentTransaction.commit();
                                            } else {
                                                Toast.makeText(getActivity().getApplicationContext(), R.string.invalidEmail, Toast.LENGTH_LONG).show();
                                            }


                                        } else {
                                            Log.e("", "Updated exception" + e);
                                            exception = true;
                                            email.setText("");
                                            editor.putString(ChooSaveDataPref.EMAIL, "");
                                            editor.commit();
                                            if(e.getMessage().contains("the email address") && e.getMessage().contains("has already been taken"))
                                            {
                                                Toast.makeText(getActivity().getApplicationContext(),getString(R.string.emailtaken),Toast.LENGTH_LONG).show();
                                            }else if(e.getMessage().contains("invalid email address"))
                                            {
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
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(),BankAccountInfoActivity.class);
                startActivity(intent);
            }
        });
        txtSelectTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTitleDialog();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressbar.setImageResource(R.mipmap.progress_bar5);
            }
        }, 100);
        firstname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                firstname.setSelection(firstname.getText().toString().length());
                firstnameText.setTextColor(getResources().getColor(R.color.app_theme));
            }
        });
//        firstname.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firstname.setSelection(firstname.getText().toString().length());
//            }
//        });
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                name.setSelection(name.getText().toString().length());
                nameText.setTextColor(getResources().getColor(R.color.app_theme));
                firstnameText.setTextColor(Color.parseColor("#000000"));
            }
        });
//        name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                name.setSelection(name.getText().toString().length());
//            }
//        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                email.setSelection(email.getText().toString().length());
                emailText.setTextColor(getResources().getColor(R.color.app_theme));
                nameText.setTextColor(Color.parseColor("#000000"));
            }
        });
//        email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                email.setSelection(email.getText().toString().length());
//            }
//        });
        street.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                street.setSelection(street.getText().toString().length());
                streetText.setTextColor(getResources().getColor(R.color.app_theme));
                emailText.setTextColor(Color.parseColor("#000000"));
            }
        });
//        street.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                street.setSelection(street.getText().toString().length());
//            }
//        });
        housenumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                housenumber.setSelection(housenumber.getText().toString().length());
                housenumText.setTextColor(getResources().getColor(R.color.app_theme));
                streetText.setTextColor(Color.parseColor("#000000"));
            }
        });
//        housenumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                housenumber.setSelection(housenumber.getText().toString().length());
//            }
//        });
        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                address.setSelection(address.getText().toString().length());
                addressText.setTextColor(getResources().getColor(R.color.app_theme));
                housenumText.setTextColor(Color.parseColor("#000000"));
            }
        });
//        address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                address.setSelection(address.getText().toString().length());
//            }
//        });
        zipcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                zipcode.setSelection(zipcode.getText().toString().length());
                zipText.setTextColor(getResources().getColor(R.color.app_theme));
                addressText.setTextColor(Color.parseColor("#000000"));
            }
        });
//        zipcode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                zipcode.setSelection(zipcode.getText().toString().length());
//            }
//        });
        city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                city.setSelection(city.getText().toString().length());
                cityText.setTextColor(getResources().getColor(R.color.app_theme));
                zipText.setTextColor(Color.parseColor("#000000"));
            }
        });
//        city.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                city.setSelection(city.getText().toString().length());
//            }
//        });
        company.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                company.setSelection(company.getText().toString().length());
                companyText.setTextColor(getResources().getColor(R.color.app_theme));
                cityText.setTextColor(Color.parseColor("#000000"));
            }
        });
//        company.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                company.setSelection(company.getText().toString().length());
//            }
//        });
        accountnumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                accountnumber.setSelection(accountnumber.getText().toString().length());
                accountnumText.setTextColor(getResources().getColor(R.color.app_theme));
                companyText.setTextColor(Color.parseColor("#000000"));
            }
        });
//        accountnumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                accountnumber.setSelection(accountnumber.getText().toString().length());
//            }
//        });
        bankcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                bankcode.setSelection(bankcode.getText().toString().length());
                bankcodeText.setTextColor(getResources().getColor(R.color.app_theme));
                accountnumText.setTextColor(Color.parseColor("#000000"));
            }
        });
//        bankcode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bankcode.setSelection(bankcode.getText().toString().length());
//            }
//        });
        accountholder.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                accountholder.setSelection(accountholder.getText().toString().length());
                accountholderText.setTextColor(getResources().getColor(R.color.app_theme));
                bankcodeText.setTextColor(Color.parseColor("#000000"));
            }
        });
//        accountholder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                accountholder.setSelection(accountholder.getText().toString().length());
//            }
//        });
        accountholder.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                accountholderText.setTextColor(Color.parseColor("#000000"));
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_personal_and_bank_data_info, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

}
