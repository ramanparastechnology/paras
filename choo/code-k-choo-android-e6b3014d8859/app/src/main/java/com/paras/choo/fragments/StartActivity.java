package com.paras.choo.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.paras.choo.R;
import com.paras.choo.SplashScreenActivity;
import com.paras.choo.UnlockActivity;


public class StartActivity extends ActionBarActivity {
    public static SharedPreferences sharedPreferences = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        sharedPreferences = getSharedPreferences("Prefs", MODE_PRIVATE);


        if(getSharedPreferences("AppLock",MODE_PRIVATE).getString("passCode",null) != null) {
            Intent i = new Intent(this, UnlockActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(this,SplashScreenActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
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
}
