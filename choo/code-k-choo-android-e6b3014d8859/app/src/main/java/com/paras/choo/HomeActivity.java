package com.paras.choo;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.paras.choo.MainActivity;
import com.paras.choo.adapters.HomeListAdapter;
import com.paras.choo.beans.HomeListItems;
import com.paras.choo.fragments.ArrivalTrainFragment;
import com.paras.choo.fragments.DepartureStationFragment;
import com.paras.choo.fragments.InformationFragment;
import com.paras.choo.fragments.MyDataFragment;
import com.paras.choo.fragments.MyDataInfo;
import com.paras.choo.fragments.RefundCheck;
import com.paras.choo.fragments.SettingFragment;
import com.paras.choo.fragments.ShareFriendsFragment;
import com.paras.choo.fragments.TransitionInfoFragment;

import java.util.ArrayList;
import java.util.Locale;


public class HomeActivity extends ActionBarActivity implements  MyDataFragment.OnFragmentInteractionListener,RefundCheck.OnFragmentInteractionListener,MyDataInfo.OnFragmentInteractionListener,InformationFragment.OnFragmentInteractionListener,ShareFriendsFragment.OnFragmentInteractionListener,SettingFragment.OnFragmentInteractionListener,ArrivalTrainFragment.OnFragmentInteractionListener,DepartureStationFragment.OnFragmentInteractionListener,TransitionInfoFragment.OnFragmentInteractionListener{
    ListView homelist;
    TextView reqmoneyback, trainslate;
    Typeface fieldsfont;
    ArrayList<HomeListItems> homeitemarray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        fieldsfont = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(Locale.getDefault().getLanguage());
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_home);

        homelist = (ListView)findViewById(R.id.homelist);
        reqmoneyback = (TextView)findViewById(R.id.reqmoneyback);
        trainslate = (TextView)findViewById(R.id.trainslate);
        trainslate.setTypeface(fieldsfont);
        res = getResources();
        homeitemarray = new ArrayList<HomeListItems>();

        homeitemarray.add(new HomeListItems(res.getStringArray(R.array.home_list_items)[0],R.drawable.refund_icon));

        homeitemarray.add(new HomeListItems(res.getStringArray(R.array.home_list_items)[1],R.drawable.mydata));

        homeitemarray.add(new HomeListItems(res.getStringArray(R.array.home_list_items)[2],R.drawable.sharefriends));

        homeitemarray.add(new HomeListItems(res.getStringArray(R.array.home_list_items)[3],R.drawable.information));

        homeitemarray.add(new HomeListItems(res.getStringArray(R.array.home_list_items)[4],R.drawable.settings));


        homelist.setAdapter(new HomeListAdapter(this,homeitemarray));

        reqmoneyback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                reqmoneyback.setBackgroundResource(R.drawable.rect_back1);
                Intent i = new Intent(HomeActivity.this,MainActivity.class);
                i.putExtra("pos",1);
                startActivity(i);
                finish();
            }
        });
        homelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayView(position);
            }
        });
    }
    private void displayView(int position) {
        // update the main content by replacing fragments
        Intent i = new Intent(HomeActivity.this,MainActivity.class);
        switch (position) {
            case 0:
                i.putExtra("pos",1);
                startActivity(i);
                break;
            case 1:
                i.putExtra("pos",2);
                startActivity(i);
                break;
            case 2:
                i.putExtra("pos",3);
                startActivity(i);
                break;
            case 3:
                i.putExtra("pos",4);
                startActivity(i);
                break;
            case 4:
                i.putExtra("pos",5);
                startActivity(i);
                break;
            default:
                break;
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        reqmoneyback.setBackgroundResource(R.drawable.rect_back1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
