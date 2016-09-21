package com.paras.choo.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.paras.choo.R;
import com.paras.choo.adapters.AudienceAdapter;
import com.paras.choo.beans.AudienceListItems;
import com.paras.choo.utils.AlertDialogManager;
import com.paras.choo.utils.ChooPref;
import com.paras.choo.utils.ConnectionDetector;
import com.paras.choo.utils.LocationAddress;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShareFriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShareFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShareFriendsFragment extends Fragment implements LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FrameLayout layout_MainMenu;
    String TWITTER_CONSUMER_KEY = "gQJyO6aRUZcViyxobaz2DgGiR";
    String TWITTER_CONSUMER_SECRET = "pLgKVPoKJi4HoSOz1lftebIsgrSeIWo9eE9F9lxC7cG2nhXKi0";
    //    String ACCESS_TOKEN = "2892024104-fgpAjEInDZmn06AQi5IljhA35xavzXu8ySrrGBU";
//    String ACCESS_TOKEN_SECRET = "imd8IDDSecKbgXLn1B7aYqW8FPPqpZ8rjrovH2VDRrUkj";
    // Progress dialog
    ProgressDialog pDialog;
    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    EditText twitmsg;
    // Internet Connection detector
    private ConnectionDetector cd;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;
    View rootview;
    TextView sharewithfriends;
    LinearLayout likeonfb, followontwitter, circleongoogleplus;
    private OnFragmentInteractionListener mListener;
    GoogleMap googleMap;
    com.google.android.gms.maps.MapFragment supportMapFragment;
    LocationManager locationManager;
    String bestProvider;
    String locationAddress;
    TextView locaddress, add,txtSelectedAudience;
    PopupWindow twindo, ewindo, pwindo, gpswindo;
    ArrayList<AudienceListItems> audiencearray;
    ListView audiencelist;
    AudienceAdapter audienceAdapter;
    SharedPreferences  _prefs = null ;
    View layout = null;
    Button test;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    boolean albumselected = false;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter dot1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShareFriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShareFriendsFragment newInstance(String param1, String param2) {
        ShareFriendsFragment fragment = new ShareFriendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ShareFriendsFragment() {
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
        cd = new ConnectionDetector(getActivity().getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(getActivity(), "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        // Check if twitter keys are set
        if(TWITTER_CONSUMER_KEY.trim().length() == 0 || TWITTER_CONSUMER_SECRET.trim().length() == 0){
            // Internet Connection is not present
            alert.showAlertDialog(getActivity(), "Twitter oAuth tokens", "Please set your twitter oauth tokens first!", false);
            // stop executing code by return
            return;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootview != null) {
            ViewGroup parent = (ViewGroup) rootview.getParent();
            if (parent != null)
                parent.removeView(rootview);
        }
        try {
            rootview = inflater.inflate(R.layout.fragment_share_friends, container, false);
            layout_MainMenu = (FrameLayout) getActivity().findViewById( R.id.realtabcontent);
            layout_MainMenu.getForeground().setAlpha(0);
            FacebookSdk.sdkInitialize(getActivity());
            callbackManager = CallbackManager.Factory.create();
            shareDialog = new ShareDialog(getActivity());
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException e) {

                }
            });

        } catch (InflateException e) {
        /* map is already there, just return view as it is */
            e.printStackTrace();
        }
//        rootview = inflater.inflate(R.layout.fragment_share_friends, container, false);
        _prefs = getActivity().getSharedPreferences(ChooPref.CHOO_PREF,Context.MODE_PRIVATE);
        sharewithfriends = (TextView)rootview.findViewById(R.id.sharewithfriends);
        likeonfb = (LinearLayout)rootview.findViewById(R.id.likeonfb);
        followontwitter = (LinearLayout)rootview.findViewById(R.id.followontwitter);
        circleongoogleplus = (LinearLayout)rootview.findViewById(R.id.circleongoogleplus);
        test = (Button)rootview.findViewById(R.id.test);
        mSharedPreferences = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Bitmap image = BitmapFactory.decodeResource(getActivity().getResources(),
//                        R.drawable.ic_launcher);
//                Log.e("", "image" + image);
//                SharePhoto photo = new SharePhoto.Builder()
//                        .setBitmap(image)
//                        .build();
//                Log.e("", "photo" + photo);
//                SharePhotoContent content = new SharePhotoContent.Builder()
//                        .addPhoto(photo)
//                        .setRef("Reference")
//                        .build();
//                Log.e("","content"+content);
//                Log.e("", "getActivity()" + getActivity());
//                Log.e("","shareDialog"+shareDialog);
////                if(shareDialog.canShow(content)){
//
//                    shareDialog.show(getActivity(),content);
//
////                }
//
//            }
//        });
        Log.e("emailsubject",""+getString(R.string.emailsubject));
        sharewithfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.eMail:
//                                Intent intent = new Intent(Intent.ACTION_SEND);
//                                intent.setType("text/html");
//                                intent.putExtra(Intent.EXTRA_EMAIL, "padam.batish2@gmail.com");
//                                intent.putExtra(Intent.EXTRA_TEXT, "Look under the following link where you can quickly & easily file claim your refund at 'Deutsche Bahn' if your train has been delayed: http://www.chooapp.com");
//                                startActivity(Intent.createChooser(intent, "Send Email"));

                                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

                                shareIntent.setType("text/plain");

                                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.emailsubject));

                                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.msgtext));

// finding gmail package name  ---

                                PackageManager pm = getActivity().getPackageManager();

                                List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);

                                for (final ResolveInfo app : activityList) {
                                    if ((app.activityInfo.name).contains("Gmail")) {
                                        final ActivityInfo activity = app.activityInfo;
                                        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                                        shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                        shareIntent.setComponent(name);
                                        getActivity().startActivity(shareIntent);
                                        break;
                                    }
                                }
                                return true;
                            case R.id.Facebook:
                                //Put the package name here...
                                boolean installed =   appInstalledOrNot("com.facebook.katana");
                                if(installed)
                                {
                                    System.out.println("App already installed om your phone");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            layout_MainMenu.getForeground().setAlpha(220); // dim
                                            initiatePopupWindow();
                                        }
                                    }, 500);
                                }
                                else
                                {
                                    System.out.println("App is not installed om your phone");
                                    layout_MainMenu.getForeground().setAlpha(220); // dim
                                    initiateerrorPopupWindow();
                                }

//                                Intent intnt = new Intent(Intent.ACTION_SEND);
//                                intnt.setType("text/plain");
//                                intnt.putExtra(Intent.EXTRA_TEXT, getString(R.string.msgtext));
//                                startActivity(Intent.createChooser(intnt, "Share with"));
//                                Intent shareIntent1 = new Intent(android.content.Intent.ACTION_SEND);
//                                shareIntent1.setType("text/plain");
//                                shareIntent1.putExtra(android.content.Intent.EXTRA_SUBJECT, "Choo");
//
//                                shareIntent1.putExtra(android.content.Intent.EXTRA_TEXT,"");
//
//// finding facebook package name
//
//                                PackageManager pm2 = getActivity().getPackageManager();
//                                List<ResolveInfo> activityList2 = pm2.queryIntentActivities(shareIntent1, 0);
//                                for (final ResolveInfo app : activityList2)
//                                {
//                                    if ((app.activityInfo.name).contains("Facebook"))
//                                    {
//                                        final ActivityInfo activity = app.activityInfo;
//                                        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
//                                        shareIntent1.addCategory(Intent.CATEGORY_LAUNCHER);
//                                        shareIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                                        shareIntent1.setComponent(name);
//                                        getActivity().startActivity(shareIntent1);
//                                        break;
//                                    }
//                                    else{
//                                        Intent intnt = new Intent(Intent.ACTION_SEND);
//                                intnt.setType("text/plain");
//                                intnt.putExtra(Intent.EXTRA_TEXT, "Look under the following link where you can quickly & easily file claim your refund at 'Deutsche Bahn' if your train has been delayed: http://www.chooapp.com");
//                                startActivity(Intent.createChooser(intnt, "Share with"));
//                                    }
//                                }
                                return true;
                            case R.id.Twitter:
                                boolean installed1 =   appInstalledOrNot("com.twitter.android");
                                if(installed1) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            layout_MainMenu.getForeground().setAlpha(220); // dim
                                            initiatetwitterPopupWindow();

                                        }
                                    }, 500);
                                }else{layout_MainMenu.getForeground().setAlpha(220); // dim
                                    initiateerrorPopupWindow();
                                }

//                                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//                                intent.setType("text/plain");
//
//                                intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
//
//                                intent.putExtra(android.content.Intent.EXTRA_TEXT,getString(R.string.msgtext));
//                                PackageManager pm1 = getActivity().getPackageManager();
//
//                                List<ResolveInfo> activityList1 = pm1.queryIntentActivities(intent, 0);
//
//                                for (final ResolveInfo app : activityList1)
//                                {
//                                    if ("com.twitter.android.PostActivity".equals(app.activityInfo.name))
//                                    {
//                                        final ActivityInfo activity = app.activityInfo;
//                                        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
//                                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                                        intent.setComponent(name);
//                                        getActivity().startActivity(intent);
//                                        break;
//                                    }
//                                    else{
//
//                                    }
//                                }
//                                Toast.makeText(getActivity().getApplicationContext(),"Twitter app is not available",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.Message:
                                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                sendIntent.setData(Uri.parse("sms:"));
                                sendIntent.putExtra("sms_body", getString(R.string.msgtext));
                                startActivity(sendIntent);
                                return true;
                            case R.id.cancel:
                                popupMenu.dismiss();
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.sharewithfriends_popup);
                popupMenu.show();
            }
        });
        likeonfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeonfb.setBackgroundColor(getResources().getColor(R.color.app_theme_light));
                followontwitter.setBackgroundColor(Color.parseColor("#ffffff"));
                circleongoogleplus.setBackgroundColor(Color.parseColor("#ffffff"));
                String facebookUrl = "https://www.facebook.com/Chooapp";
                try {
                    int versionCode = getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) {
                        Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        ;
                    } else {
                        // open the Facebook app using the old method (fb://profile/id or fb://page/id)
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/874481042564683")));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    // Facebook is not installed. Open the browser
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
                }
            }
        });
        followontwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeonfb.setBackgroundColor(Color.parseColor("#ffffff"));
                followontwitter.setBackgroundColor(getResources().getColor(R.color.app_theme_light));
                circleongoogleplus.setBackgroundColor(Color.parseColor("#ffffff"));
                Intent intent = null;
                try {
                    // get the Twitter app if possible
                    getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=choo_app"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    // no Twitter app, revert to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mobile.twitter.com/choo_app"));
                }
                getActivity().startActivity(intent);
            }
        });
        circleongoogleplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeonfb.setBackgroundColor(Color.parseColor("#ffffff"));
                followontwitter.setBackgroundColor(Color.parseColor("#ffffff"));
                circleongoogleplus.setBackgroundColor(getResources().getColor(R.color.app_theme_light));
                Intent intent = null;
                try {
                    intent.setData(Uri.parse("https://plus.google.com/102811849531842765911/posts"));
                    intent.setPackage("com.google.android.apps.plus"); // don't open the browser, make sure it opens in Google+ app
                } catch (Exception e) {
                    // no Twitter app, revert to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/102811849531842765911/posts"));
                }
                getActivity().startActivity(intent);


            }
        });
        return rootview;
    }
    private boolean appInstalledOrNot(String uri)
    {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed = false;
        try
        {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            app_installed = false;
        }
        return app_installed ;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void initiatePopupWindow() {
        try {
// We need to get the instance of the LayoutInflater
            LinearLayout viewGroup = (LinearLayout) getActivity().findViewById(R.id.fb);
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (layout != null) {
                ViewGroup parent = (ViewGroup) layout.getParent();
                if (parent != null)
                    parent.removeView(layout);
            }
            try {
                layout = inflater.inflate(R.layout.facebook_popup,null);
            } catch (InflateException e) {
        /* map is already there, just return view as it is */
            }

            pwindo = new PopupWindow(getActivity());
            final  PopupWindow popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
//            pwindo.setContentView(layout);
//            pwindo.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
//            pwindo.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            popupWindow.showAtLocation(layout, Gravity.TOP, 0, 50);
            TextView cancel = (TextView) layout.findViewById(R.id.cancel);
            TextView post = (TextView) layout.findViewById(R.id.post);
            final EditText msg = (EditText) layout.findViewById(R.id.msg);
            LinearLayout loc = (LinearLayout) layout.findViewById(R.id.location);
            LinearLayout fbback = (LinearLayout) layout.findViewById(R.id.fbback);
            LinearLayout album = (LinearLayout) layout.findViewById(R.id.album);
            LinearLayout audience = (LinearLayout) layout.findViewById(R.id.audience);
            final LinearLayout l1 = (LinearLayout) layout.findViewById(R.id.l1);
            final LinearLayout l2 = (LinearLayout) layout.findViewById(R.id.l2);
            final LinearLayout l3 = (LinearLayout) layout.findViewById(R.id.l3);
            final LinearLayout l4 = (LinearLayout) layout.findViewById(R.id.l4);
            LinearLayout albumitem = (LinearLayout) layout.findViewById(R.id.album_item);
            LinearLayout fbalbumback = (LinearLayout) layout.findViewById(R.id.fbalbumback);
            LinearLayout fbaudienceback = (LinearLayout) layout.findViewById(R.id.fbaudienceback);
            final LinearLayout albumphotolayout = (LinearLayout) layout.findViewById(R.id.albumphotolayout);
            final ListView albumlist = (ListView) layout.findViewById(R.id.album_list);
            audiencelist = (ListView) layout.findViewById(R.id.audience_list);
            ImageView album_img = (ImageView) layout.findViewById(R.id.album_img);
            TextView albumname = (TextView) layout.findViewById(R.id.albumname);
            final ImageView albumtick = (ImageView) layout.findViewById(R.id.album_tick);
            final ImageView nonetick = (ImageView) layout.findViewById(R.id.nonetick);
            final ImageView locationtick = (ImageView) layout.findViewById(R.id.locationtick);
            LinearLayout nonelayout = (LinearLayout) layout.findViewById(R.id.nonelayout);
            LinearLayout locationlayout = (LinearLayout) layout.findViewById(R.id.locationlayout);
            add = (TextView) layout.findViewById(R.id.add);
            txtSelectedAudience = (TextView) layout.findViewById(R.id.txtSelectedAudience);
            locaddress = (TextView) layout.findViewById(R.id.loc);
            supportMapFragment =
                    (com.google.android.gms.maps.MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map1);
            final   int Audience = _prefs.getInt(ChooPref.AUDIENCE_FACEBOOK, -1);
            audiencearray = new ArrayList<AudienceListItems>();
            audiencearray.add(new AudienceListItems(getString(R.string.publicstr),false));
            audiencearray.add(new AudienceListItems(getString(R.string.yourfriends), false));
            audiencearray.add(new AudienceListItems(getString(R.string.onlyme), false));
            if(_prefs.getInt(ChooPref.AUDIENCE_FACEBOOK, -1) == -1){
                audiencearray.get(1).setIscheck(true);
                txtSelectedAudience.setText(audiencearray.get(1).getTitle());
            }else{
                audiencearray.get(_prefs.getInt(ChooPref.AUDIENCE_FACEBOOK, -1)).setIscheck(true);
                txtSelectedAudience.setText(audiencearray.get(_prefs.getInt(ChooPref.AUDIENCE_FACEBOOK, -1)).getTitle());
            }
            audienceAdapter = new AudienceAdapter(getActivity(), audiencearray);

            audiencelist.setAdapter(audienceAdapter);
            audiencelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AudienceListItems audienceListItems = audiencearray.get(position);
                    txtSelectedAudience.setText(audienceListItems.getTitle());
                    if (_prefs.getInt(ChooPref.AUDIENCE_FACEBOOK, -1) == -1) {
                        audiencearray.get(1).setIscheck(false);
                    } else {
                        audiencearray.get(_prefs.getInt(ChooPref.AUDIENCE_FACEBOOK, -1)).setIscheck(false);
                    }
                    audiencearray.get(position).setIscheck(true);
                    _prefs.edit().putInt(ChooPref.AUDIENCE_FACEBOOK, position).commit();
                    audienceAdapter.notifyDataSetChanged();
                    l1.setVisibility(View.VISIBLE);
                    l3.setVisibility(View.GONE);

                }
            });
            msg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    layout_MainMenu.getForeground().setAlpha(0);
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            });
            fbalbumback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l4.setVisibility(View.GONE);
                    l1.setVisibility(View.VISIBLE);
                }
            });
            fbaudienceback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l3.setVisibility(View.GONE);
                    l1.setVisibility(View.VISIBLE);
                }
            });
            album.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l1.setVisibility(View.GONE);
                    l4.setVisibility(View.VISIBLE);
//                    AlbumListItems item = new AlbumListItems();
//                    ArrayList<AlbumListItems> albumarray = new ArrayList<AlbumListItems>();
//                    item.setTitle("Android Photos");
//                    albumarray.add(item);
//                    albumlist.setAdapter(new AlbumAdapter(getActivity(), albumarray));
                }
            });
            albumitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(albumselected)
                    {
                        albumtick.setVisibility(View.INVISIBLE);
                        albumselected = false;
                        l4.setVisibility(View.GONE);
                        l1.setVisibility(View.VISIBLE);
                        albumphotolayout.setVisibility(View.GONE);
                    }else{
                        albumtick.setVisibility(View.VISIBLE);
                        albumselected = true;
                        l4.setVisibility(View.GONE);
                        l1.setVisibility(View.VISIBLE);
                        albumphotolayout.setVisibility(View.VISIBLE);
                    }
                }
            });
            audience.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l1.setVisibility(View.GONE);
                    l3.setVisibility(View.VISIBLE);
                    audienceAdapter.notifyDataSetChanged();
                }
            });

            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bitmap image = BitmapFactory.decodeResource(getActivity().getResources(),
                            R.drawable.choo1);
                    Bitmap image1 = BitmapFactory.decodeResource(getActivity().getResources(),
                            R.drawable.choo2);
                    Log.e("", "image" + image);
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
                            .build();
                    SharePhoto photo1 = new SharePhoto.Builder()
                            .setBitmap(image1)
                            .build();
                    List<SharePhoto> photos = new ArrayList<SharePhoto>();
                    if(albumselected) {
                        photos.add(photo);
                        photos.add(photo1);
                        Log.e("", "photo" + photo);
                        SharePhotoContent content = new SharePhotoContent.Builder()
                                .addPhotos(photos)
                                .setRef(msg.getText().toString())
                                .build();
                        Log.e("","content"+content);
                        Log.e("", "getActivity()" + getActivity());
                        Log.e("", "shareDialog" + shareDialog);
//                if(shareDialog.canShow(content)){

                        shareDialog.show(getActivity(), content);
                    }
                    else{
                        Log.e("", "photo" + photo);
                        SharePhotoContent content = new SharePhotoContent.Builder()
                                .addPhoto(photo)
                                .setRef(msg.getText().toString())
                                .build();
                        Log.e("","content"+content);
                        Log.e("", "getActivity()" + getActivity());
                        Log.e("", "shareDialog" + shareDialog);
                        shareDialog.show(getActivity(), content);
                    }

                }
            });
            fbback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.GONE);
                }
            });
            locationlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationtick.setVisibility(View.VISIBLE);
                    nonetick.setVisibility(View.GONE);
                }
            });
            nonelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationtick.setVisibility(View.GONE);
                    nonetick.setVisibility(View.VISIBLE);
                    add.setText("None");
                }
            });
            loc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps("Facebook");
                    } else {
                        l1.setVisibility(View.GONE);
                        l2.setVisibility(View.VISIBLE);
                        googleMap = supportMapFragment.getMap();
                        googleMap.setMyLocationEnabled(true);
                        Criteria criteria = new Criteria();
                        bestProvider = locationManager.getBestProvider(criteria, true);
                        Location location = locationManager.getLastKnownLocation(bestProvider);
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                }
            });
//            locaddress.setText(locationAddress);
//            locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initiatetwitterPopupWindow() {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.twitter_popup,
                    (ViewGroup)getActivity().findViewById(R.id.twitter));
            twindo = new PopupWindow(layout);
            twindo.setContentView(layout);
            twindo.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            twindo.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            twindo.showAtLocation(layout, Gravity.TOP, 0, 50);
            TextView cancel = (TextView) layout.findViewById(R.id.cancel);
            TextView post = (TextView) layout.findViewById(R.id.post);
            twitmsg = (EditText) layout.findViewById(R.id.twitmsg);
            LinearLayout loc = (LinearLayout) layout.findViewById(R.id.location);
            final ImageView nonetick = (ImageView) layout.findViewById(R.id.nonetick);
            final ImageView locationtick = (ImageView) layout.findViewById(R.id.locationtick);
            LinearLayout nonelayout = (LinearLayout) layout.findViewById(R.id.nonelayout);
            LinearLayout locationlayout = (LinearLayout) layout.findViewById(R.id.locationlayout);
            add = (TextView) layout.findViewById(R.id.add);
            locaddress = (TextView) layout.findViewById(R.id.loc);
            LinearLayout twitback = (LinearLayout) layout.findViewById(R.id.twitback);
            final LinearLayout l1 = (LinearLayout) layout.findViewById(R.id.l1);
            final LinearLayout l2 = (LinearLayout) layout.findViewById(R.id.l2);
            supportMapFragment =
                    (com.google.android.gms.maps.MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
            twitmsg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    twindo.dismiss();
                    layout_MainMenu.getForeground().setAlpha(0);
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            });
            locationlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationtick.
                            setVisibility(View.VISIBLE);
                    nonetick.setVisibility(View.GONE);
                }
            });
            nonelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationtick.setVisibility(View.GONE);
                    nonetick.setVisibility(View.VISIBLE);
                    add.setText("None");
                }
            });
            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isTwitterLoggedInAlready()) {
                        // Call login twitter function
                        new LoginToTwitter().execute();
                        /** This if conditions is tested once is
                         * redirected from twitter page. Parse the uri to get oAuth
                         * Verifier
                         **/
                        Thread thread = new Thread()
                        {
                            @Override
                            public void run() {
                                if (!isTwitterLoggedInAlready()) {
                                    Uri uri = getActivity().getIntent().getData();
                                    Log.e("uriii",uri+"");
                                    if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
                                        // oAuth verifier
                                        String verifier = uri
                                                .getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
                                        Log.e("verifier",""+verifier);
                                        String oauth_token = uri.getQueryParameter(URL_TWITTER_OAUTH_TOKEN);
                                        Log.e("oauth_token",""+oauth_token);
                                        try {

                                            // Get the access token
                                            AccessToken accessToken = twitter.getOAuthAccessToken(
                                                    requestToken, verifier);

                                            // Shared Preferences
                                            SharedPreferences.Editor e = mSharedPreferences.edit();

                                            // After getting access token, access token secret
                                            // store them in application preferences
                                            e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
                                            e.putString(PREF_KEY_OAUTH_SECRET,
                                                    accessToken.getTokenSecret());
                                            // Store login status - true
                                            e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
                                            e.commit(); // save changes

                                            Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

                                            // Hide login button
//                        btnLoginTwitter.setVisibility(View.GONE);

                                            // Show Update Twitter
//                        lblUpdate.setVisibility(View.VISIBLE);
//                        txtUpdate.setVisibility(View.VISIBLE);
//                        btnUpdateStatus.setVisibility(View.VISIBLE);
//                        btnLogoutTwitter.setVisibility(View.VISIBLE);

                                            // Getting user details from twitter
                                            // For now i am getting his name only
//                                            long userID = accessToken.getUserId();
//                                            User user = twitter.showUser(userID);
//                                            String username = user.getName();

                                            // Displaying in xml ui
//                        lblUserName.setText(Html.fromHtml("<b>Welcome " + username + "</b>"));
                                        } catch (TwitterException e) {
                                            // Check log for login errors
                                            Log.e("Twitter Login Error", "> " + e.getMessage());
                                        }
                                    }
                                }
                            }
                        };

                        thread.start();
                    }
                    else {
                        // user already logged into twitter
//                        Toast.makeText(getActivity(),"Already Logged into twitter", Toast.LENGTH_LONG).show();
                        // Call update status function
                        // Get the status from EditText
                        String status = twitmsg.getText().toString();

                        // Check for blank text
                        if (status.trim().length() > 0) {
                            // update status
                            new updateTwitterStatus().execute(status);
                        } else {
                            // EditText is empty
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Please enter status message", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                }

            });
            twitback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.GONE);
                }
            });
            loc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps("Twitter");
                    } else {
                        l1.setVisibility(View.GONE);
                        l2.setVisibility(View.VISIBLE);
                        googleMap = supportMapFragment.getMap();
                        googleMap.setMyLocationEnabled(true);
                        Criteria criteria = new Criteria();
                        bestProvider = locationManager.getBestProvider(criteria, true);
                        Location location = locationManager.getLastKnownLocation(bestProvider);
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                }
            });
//            Toast.makeText(getActivity().getApplicationContext(),locationAddress,Toast.LENGTH_LONG).show();
//            locaddress.setText(locationAddress);
//            locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initiateerrorPopupWindow() {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.error_popup,
                    (ViewGroup)getActivity().findViewById(R.id.error));
            ewindo = new PopupWindow(layout);
            ewindo.setContentView(layout);
            ewindo.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            ewindo.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            ewindo.showAtLocation(layout, Gravity.TOP, 0, 300);

            TextView ok = (TextView) layout.findViewById(R.id.ok);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ewindo.dismiss();
                    layout_MainMenu.getForeground().setAlpha(0);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void buildAlertMessageNoGps(String type) {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.enable_gps_popup,
                    (ViewGroup)getActivity().findViewById(R.id.gps));
            gpswindo = new PopupWindow(layout);
            gpswindo.setContentView(layout);
            gpswindo.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            gpswindo.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            gpswindo.showAtLocation(layout, Gravity.TOP, 0, 450);

            TextView cancel = (TextView) layout.findViewById(R.id.cancel);
            TextView settings = (TextView) layout.findViewById(R.id.settings);
            TextView gps2 = (TextView) layout.findViewById(R.id.gps2);
            gps2.setText("Allow " + type + " to Determine");
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 100);
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gpswindo.dismiss();
//                    layout_MainMenu.getForeground().setAlpha(0);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class LoginToTwitter extends AsyncTask<String, Void, Void>{
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * getting Places JSON
         * */
        protected Void doInBackground(String... args) {
            // Check if already logged in

            ConfigurationBuilder builder = new ConfigurationBuilder();

            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            try {
                requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
//                    OAuthToken{token='pTcMXQAAAAAAhvfcAAABUBLuK5Y', tokenSecret='uKdX7JDptTWuyXuZaz4ohUfhx2UcmJxQ', secretKeySpec=null}
//                    String k  = Uri.parse(requestToken.getAuthenticationURL()) + "";
//                    Log.e(" Twitter Uri  ", k + "");
//                    String s = k.replace("http", "https");
//                    Log.e(" Twitter Uri  ", s + "");
                Log.e("requestToken",""+requestToken);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog and show
         * the data in UI Always use runOnUiThread(new Runnable()) to update UI
         * from background thread, otherwise you will get error
         * **/
        protected void onPostExecute(String file_url) {
        }
    }
    /**
     * Function to update status
     * */
    class updateTwitterStatus extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        protected void onPreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Updating to twitter...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Places JSON
         * */
        protected String doInBackground(String... args) {
            Log.d("Tweet Text", "> " + args[0]);
            String status = args[0];
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

                // Access Token
                String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
                // Access Token Secret
                String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

                // Update status
                twitter4j.Status response = twitter.updateStatus(status);

                Log.d("Status", "> " + response.getText());
            } catch (TwitterException e) {
                // Error in updating status
                Log.d("Twitter Update Error", e.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        "Duplicate Status", Toast.LENGTH_SHORT)
                        .show();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog and show
         * the data in UI Always use runOnUiThread(new Runnable()) to update UI
         * from background thread, otherwise you will get error
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Status tweeted successfully", Toast.LENGTH_SHORT)
                            .show();
                    twindo.dismiss();
                    layout_MainMenu.getForeground().setAlpha(0);
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    // Clearing EditText field
//                    txtUpdate.setText("");
                }
            });
        }

    }
    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     * */
    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share_friends_info, menu);
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

    @Override
    public void onResume() {
        super.onResume();
        likeonfb.setBackgroundColor(Color.parseColor("#ffffff"));
        followontwitter.setBackgroundColor(Color.parseColor("#ffffff"));
        circleongoogleplus.setBackgroundColor(Color.parseColor("#ffffff"));

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", "" + requestCode);
        Log.e("resultCode", "" + resultCode);
        if (requestCode == 100) {
            gpswindo.dismiss();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(latitude, longitude,
                getActivity().getApplicationContext(), new GeocoderHandler());
//        Log.e("Latitude:" + latitude , "Longitude:" + longitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
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
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
//                    Toast.makeText(getActivity().getApplicationContext(),locationAddress,Toast.LENGTH_LONG).show();
                    break;
                default:
                    locationAddress = null;
            }
            locaddress.setText(locationAddress);
            add.setText(locationAddress);
        }
    }
}
