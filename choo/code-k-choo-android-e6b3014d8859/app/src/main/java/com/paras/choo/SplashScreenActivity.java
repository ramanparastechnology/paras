package com.paras.choo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewFlipper;
import com.github.orangegangsters.lollipin.lib.PinActivity;


public class SplashScreenActivity extends PinActivity {
    public static SharedPreferences sharedPreferences = null ;
    public static SharedPreferences pref = null ;
    SharedPreferences.Editor editor;
    public static Context context = null;
    Boolean isFirstRun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = getApplicationContext();
//        Log.e("", "Utilities.printKeyHash(this);" + Utilities.printKeyHash(this));
        pref = context.getSharedPreferences("FirstRun", MODE_PRIVATE);
        editor = context.getSharedPreferences("FirstRun", MODE_PRIVATE).edit();
        isFirstRun = pref.getBoolean("isfirstrun", true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFirstRun) {
                    sharedPreferences = context.getSharedPreferences("Prefs", MODE_PRIVATE);
                    if (sharedPreferences.getString("homeEnabled", null) == null) {
                        Intent i = new Intent(context, HomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    } else {
                        if (sharedPreferences.getString("homeEnabled", null).equals("true")) {
                            Intent i = new Intent(context, HomeActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        } else {
                            Intent i = new Intent(context, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    }
                } else {
                    editor.putBoolean("isfirstrun", false);
                    editor.commit();
                    Intent flip = new Intent(context, ViewFlipperActivity.class);
                    flip.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(flip);
                }
                finish();
            }
        }, 5000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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
