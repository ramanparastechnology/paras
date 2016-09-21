package com.paras.choo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.paras.choo.ChangeLockActivity;
import com.paras.choo.LockActivity;
import com.paras.choo.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FrameLayout layout_MainMenu;
    SharedPreferences.Editor editor;
    Switch enablehomeSwitch, protectSwitch;
    Boolean enableHome = true;
    Boolean protect = false;
    View pascodeview;
    SharedPreferences prefs;
    public static SharedPreferences sharedPreferences = null ;
    TextView setpasscode;
    View rootview;
    private static final int REQUEST_CODE_ENABLE = 11;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter dot1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout_MainMenu = (FrameLayout) getActivity().findViewById( R.id.realtabcontent);
        layout_MainMenu.getForeground().setAlpha(0);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_setting, container, false);
        prefs = getActivity().getSharedPreferences("AppLock",getActivity().MODE_PRIVATE);
        editor = getActivity().getSharedPreferences("Prefs", getActivity().MODE_PRIVATE).edit();
        sharedPreferences = getActivity().getSharedPreferences("Prefs", getActivity().MODE_PRIVATE);
        enablehomeSwitch = (Switch)rootview.findViewById(R.id.enablehomeSwitch);
        protectSwitch = (Switch)rootview.findViewById(R.id.protectSwitch);
        pascodeview = (View)rootview.findViewById(R.id.pascodeview);
        setpasscode = (TextView)rootview.findViewById(R.id.setpasscode);
        if(sharedPreferences.getString("homeEnabled",null) == null) {
            enablehomeSwitch.setChecked(enableHome);
        }
        else
        {
            if(sharedPreferences.getString("homeEnabled",null).equals("true"))
            {
                enablehomeSwitch.setChecked(true);
            }
            else{
                enablehomeSwitch.setChecked(false);
            }
        }
        if(prefs.getString("passCode",null) != null)
        {
            protectSwitch.setChecked(true);
            setpasscode.setText(R.string.changepasscode);
            setpasscode.setVisibility(View.VISIBLE);
            pascodeview.setVisibility(View.VISIBLE);
        }
        else{
            protectSwitch.setChecked(false);
            setpasscode.setVisibility(View.GONE);
            pascodeview.setVisibility(View.GONE);
        }
        enablehomeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enableHome = isChecked;
                editor.putString("homeEnabled",enableHome+"");
                editor.commit();
            }
        });
        protectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    setpasscode.setText(R.string.setpasscode);
                    setpasscode.setVisibility(View.VISIBLE);
                    pascodeview.setVisibility(View.VISIBLE);
                }else{
                    setpasscode.setVisibility(View.GONE);
                    pascodeview.setVisibility(View.GONE);
                    prefs.edit().clear().commit();
                }
            }
        });
        setpasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // app lock
                if(setpasscode.getText().equals(getString(R.string.setpasscode))) {
                    Intent intent = new Intent(getActivity(), LockActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_ENABLE);
                }else if(setpasscode.getText().equals(getString(R.string.changepasscode))){
                    Intent intent = new Intent(getActivity(), ChangeLockActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_ENABLE);
                }
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_ENABLE:
                setpasscode.setText(R.string.changepasscode);
//                Toast.makeText(getActivity(), "PinCode enabled", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
