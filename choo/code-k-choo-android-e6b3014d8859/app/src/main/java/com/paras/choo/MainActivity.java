package com.paras.choo;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;


import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.paras.choo.adapters.DrawerItemCustomAdapter;
import com.paras.choo.beans.Message;
import com.paras.choo.beans.ObjectDrawerItem;
import com.paras.choo.fragments.ArrivalTrainFragment;
import com.paras.choo.fragments.BahnTimeCardFragment;
import com.paras.choo.fragments.DepartureStationFragment;
import com.paras.choo.fragments.InformationFragment;
import com.paras.choo.fragments.MyDataFragment;
import com.paras.choo.fragments.MyDataInfo;
import com.paras.choo.fragments.OverviewFragment;
import com.paras.choo.fragments.PersonalAndBankDataFragment;
import com.paras.choo.fragments.RefundCheck;
import com.paras.choo.fragments.RequestSentFragment;
import com.paras.choo.fragments.SendRequestFragment;
import com.paras.choo.fragments.SettingFragment;
import com.paras.choo.fragments.ShareFriendsFragment;
import com.paras.choo.fragments.TicketScanFragment;
import com.paras.choo.fragments.TransitionInfoFragment;
import com.paras.choo.utils.ChooPref;
import com.paras.choo.utils.ChooSaveDataPref;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements MyDataFragment.OnFragmentInteractionListener,RefundCheck.OnFragmentInteractionListener,MyDataInfo.OnFragmentInteractionListener,InformationFragment.OnFragmentInteractionListener,ShareFriendsFragment.OnFragmentInteractionListener,SettingFragment.OnFragmentInteractionListener,ArrivalTrainFragment.OnFragmentInteractionListener,DepartureStationFragment.OnFragmentInteractionListener,TransitionInfoFragment.OnFragmentInteractionListener, BahnTimeCardFragment.OnFragmentInteractionListener, PersonalAndBankDataFragment.OnFragmentInteractionListener, TicketScanFragment.OnFragmentInteractionListener, OverviewFragment.OnFragmentInteractionListener, SendRequestFragment.OnFragmentInteractionListener, RequestSentFragment.OnFragmentInteractionListener

{
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    protected FrameLayout frameLayout;
    SharedPreferences.Editor editor, editor1;
    SharedPreferences prefs;
    private ListView listView;
    private MessageAdapter adapter;
    private static String TAG = MainActivity.class.getSimpleName();
    private List<Message> listMessages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.facebook.samples.loginhowto",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//                Toast.makeText(getApplicationContext(),"kkkkkkkkkkkkkkkkkkkkkkkkkk"+Base64.encodeToString(md.digest(), Base64.DEFAULT),Toast.LENGTH_SHORT).show();
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
        setContentView(R.layout.activity_main);
        Log.e("OnC", "Main");
        initView();
        listView.setAdapter(adapter);
        prefs = getSharedPreferences(ChooSaveDataPref.CHOO_SAVEDATA_PREF, MODE_PRIVATE);
        editor = getSharedPreferences(ChooPref.CHOO_PREF,MODE_PRIVATE).edit();
        editor1 = prefs.edit();
        if (toolbar != null)
        {
            toolbar.setTitle("");

            setSupportActionBar(toolbar);
        }
//        if (prefs.getString(ChooSaveDataPref.EMAIL, null) != null) {
//            ParseUtils.subscribeWithEmail(prefs.getString(ChooSaveDataPref.EMAIL, null));
//        }else{
//            Log.e(TAG, "Email is null. Not subscribing to parse!");
//        }
        initDrawer();

        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l)
            {
                drawerLayout.closeDrawers();
                displayFragmentIndex(index);
            }
        });
        if(getIntent().getIntExtra("pos",1) != -1){
            displayFragmentIndex(getIntent().getIntExtra("pos",1));
        }
        //Convert binary image file to String data
        Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.ap_icon);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte [] ba = bao.toByteArray();
        String ba1=Base64.encodeToString(ba, Base64.DEFAULT);
        Log.e("ba1","ba1 "+ba1);
    }
    private void displayFragmentIndex(int index){
        switch (index){
            case 0:{
                Intent i = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
                break ;
            }
            case 1:{
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RefundCheck fragment = new RefundCheck();
                fragmentTransaction.replace(R.id.realtabcontent, fragment,"RefundCheck");
                fragmentTransaction.commit();
                editor.putString(ChooPref.BACK, "k");
                editor.commit();

                break ;
            }
            case 2:{
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MyDataFragment fragment = new MyDataFragment();
                fragmentTransaction.replace(R.id.realtabcontent, fragment,"MyDataFragment");
                fragmentTransaction.commit();

                break ;
            }

            case 3:{
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ShareFriendsFragment fragment = new ShareFriendsFragment();
                fragmentTransaction.replace(R.id.realtabcontent, fragment,"ShareFriendsFragment");
                fragmentTransaction.commit();

                break ;
            }
            case 4:{
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                InformationFragment fragment = new InformationFragment();
                fragmentTransaction.replace(R.id.realtabcontent, fragment,"InformationFragment");
                fragmentTransaction.commit();

                break ;
            }
            case 5:{
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SettingFragment fragment = new SettingFragment();
                fragmentTransaction.replace(R.id.realtabcontent, fragment,"SettingFragment");
                fragmentTransaction.commit();

                break ;
            }
            default:{
                break;
            }
        }
    }
    private void initView()
    {
        leftDrawerList = (ListView) findViewById(R.id.navList);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        frameLayout = (FrameLayout) findViewById(R.id.realtabcontent);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new MessageAdapter(this);

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[6];

        drawerItem[0] = new ObjectDrawerItem(R.drawable.home_nav, getResources().getStringArray(R.array.drawer_list_items)[0]);
        drawerItem[1] = new ObjectDrawerItem(R.drawable.document_nav, getResources().getStringArray(R.array.drawer_list_items)[1]);
        drawerItem[2] = new ObjectDrawerItem(R.drawable.mydata_nav, getResources().getStringArray(R.array.drawer_list_items)[2]);
        drawerItem[3] = new ObjectDrawerItem(R.drawable.chat_nav, getResources().getStringArray(R.array.drawer_list_items)[3]);
        drawerItem[4] = new ObjectDrawerItem(R.drawable.info_nav,getResources().getStringArray(R.array.drawer_list_items)[4]);
        drawerItem[5] = new ObjectDrawerItem(R.drawable.setting_nav,getResources().getStringArray(R.array.drawer_list_items)[5]);
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem);
        leftDrawerList.setAdapter(adapter);
    }
    private void initDrawer()
    {

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().findFragmentByTag("MyDataFragment") != null) {
            if (getSupportFragmentManager().findFragmentByTag("MyDataFragment")
                    .isVisible()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(getSupportFragmentManager().findFragmentByTag(
                                "MyDataFragment")).commit();
                finish();
            }
        }
        if(getSupportFragmentManager().findFragmentByTag("ShareFriendsFragment") != null) {
            if (getSupportFragmentManager().findFragmentByTag("ShareFriendsFragment")
                    .isVisible()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(getSupportFragmentManager().findFragmentByTag(
                                "ShareFriendsFragment")).commit();
            }
        }
        if(getSupportFragmentManager().findFragmentByTag("InformationFragment") != null) {
            if (getSupportFragmentManager().findFragmentByTag("InformationFragment")
                    .isVisible()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(getSupportFragmentManager().findFragmentByTag(
                                "InformationFragment")).commit();
            }
        }
        if(getSupportFragmentManager().findFragmentByTag("SettingFragment") != null) {
            if (getSupportFragmentManager().findFragmentByTag("SettingFragment")
                    .isVisible()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(getSupportFragmentManager().findFragmentByTag(
                                "SettingFragment")).commit();
            }
        }
        if(getSupportFragmentManager().findFragmentByTag("RequestSent") != null){
            Log.e("RequestFragment","");
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String message = intent.getStringExtra("message");

        Message m = new Message(message, System.currentTimeMillis());
        listMessages.add(0, m);
        adapter.notifyDataSetChanged();
    }
    private class MessageAdapter extends BaseAdapter {

        LayoutInflater inflater;

        public MessageAdapter(Activity activity) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return listMessages.size();
        }

        @Override
        public Object getItem(int position) {
            return listMessages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.list_row, null);
            }

            TextView txtMessage = (TextView) view.findViewById(R.id.message);
            TextView txtTimestamp = (TextView) view.findViewById(R.id.timestamp);

            Message message = listMessages.get(position);
            txtMessage.setText(message.getMessage());

            CharSequence ago = DateUtils.getRelativeTimeSpanString(message.getTimestamp(), System.currentTimeMillis(),
                    0L, DateUtils.FORMAT_ABBREV_ALL);

            txtTimestamp.setText(String.valueOf(ago));

            return view;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.information) {
            Intent intent = new Intent (this,RefundCheckInfoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.card_flip_right_in, R.animator.card_flip_right_out);
//                    getFragmentManager()
//                .beginTransaction()
//
//                // Replace the default fragment animations with animator resources representing
//                // rotations when switching to the back of the card, as well as animator
//                // resources representing rotations when flipping back to the front (e.g. when
//                // the system Back button is pressed).
//                .setCustomAnimations(
//                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
//                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
//
//                // Replace any fragments currently in the container view with a fragment
//                // representing the next page (indicated by the just-incremented currentPage
//                // variable).
//                .replace(R.id.realtabcontent, new MyDataInfo())
//
//                // Add this transaction to the back stack, allowing users to press Back
//                // to get to the front of the card.
//                .addToBackStack(null)
//
//                // Commit the transaction.
//                .commit();
            return true;
        }
        if(id == R.id.my_data_info){
            Intent intent = new Intent (this,MyDataInfoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.card_flip_right_in, R.animator.card_flip_right_out);
        }
        if(id == R.id.arrival_information){
            Intent intent = new Intent (this,ArrivalTrainInfoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.card_flip_right_in, R.animator.card_flip_right_out);
        }
        if(id == R.id.departure_information){
            Intent intent = new Intent (this,DepartureTrainInfoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.card_flip_right_in, R.animator.card_flip_right_out);
        }
        if(id == R.id.transition_information){
            Intent intent = new Intent (this,TransitionInfoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.card_flip_right_in, R.animator.card_flip_right_out);
        }
        if(id == R.id.bahntimecard_info){
            Intent intent = new Intent (this,BahnTimeCardInfoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.card_flip_right_in, R.animator.card_flip_right_out);
        }
        if(id == R.id.personalbankdata_info){
            Intent intent = new Intent (this,PersonalAndBankDataInfoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.card_flip_right_in, R.animator.card_flip_right_out);
        }
        if(id == R.id.ticketscan_information){
            Intent intent = new Intent (this,TicketScanInfoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.card_flip_right_in, R.animator.card_flip_right_out);
        }
        if(id == R.id.overview_information){
            Intent intent = new Intent (this,OverviewInfoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.card_flip_right_in, R.animator.card_flip_right_out);
        }
        if(id == R.id.sendreq_information){
            Intent intent = new Intent (this,SendRequestInfoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.card_flip_right_in, R.animator.card_flip_right_out);
        }
        if(id == R.id.sharefriends_info){
            Intent intent = new Intent (this,ShareFriendsInfoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.card_flip_right_in, R.animator.card_flip_right_out);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("OnA","Main");
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag("TicketScan");
//        fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        if (fragments != null) {
//            for (Fragment fragment : fragments) {
//                fragment.onActivityResult(requestCode, resultCode, data);
//            }
//        }
    }
}
