package com.paras.choo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.github.orangegangsters.lollipin.lib.views.KeyboardButtonView;
import com.github.orangegangsters.lollipin.lib.views.SquareImageView;

public class ConfirmLockActivity extends AppCompatActivity implements View.OnClickListener{
    SquareImageView pin_code_round1, pin_code_round2, pin_code_round3, pin_code_round4;
    KeyboardButtonView pin_code_button_1, pin_code_button_2, pin_code_button_3, pin_code_button_4,
            pin_code_button_5, pin_code_button_6, pin_code_button_7, pin_code_button_8, pin_code_button_9,
            pin_code_button_0, pin_code_button_clear;
    SharedPreferences prefs;
    Animation shake;
    int count = 0;
    String passCode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_lock);
        prefs = getSharedPreferences("AppLock",MODE_PRIVATE);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        pin_code_round1 = (SquareImageView)findViewById(R.id.pin_code_round1);
        pin_code_round2 = (SquareImageView)findViewById(R.id.pin_code_round2);
        pin_code_round3 = (SquareImageView)findViewById(R.id.pin_code_round3);
        pin_code_round4 = (SquareImageView)findViewById(R.id.pin_code_round4);
        pin_code_button_1 = (KeyboardButtonView)findViewById(R.id.pin_code_button_1);
        pin_code_button_2 = (KeyboardButtonView)findViewById(R.id.pin_code_button_2);
        pin_code_button_3 = (KeyboardButtonView)findViewById(R.id.pin_code_button_3);
        pin_code_button_4 = (KeyboardButtonView)findViewById(R.id.pin_code_button_4);
        pin_code_button_5 = (KeyboardButtonView)findViewById(R.id.pin_code_button_5);
        pin_code_button_6 = (KeyboardButtonView)findViewById(R.id.pin_code_button_6);
        pin_code_button_7 = (KeyboardButtonView)findViewById(R.id.pin_code_button_7);
        pin_code_button_8 = (KeyboardButtonView)findViewById(R.id.pin_code_button_8);
        pin_code_button_9 = (KeyboardButtonView)findViewById(R.id.pin_code_button_9);
        pin_code_button_0 = (KeyboardButtonView)findViewById(R.id.pin_code_button_0);
        pin_code_button_clear = (KeyboardButtonView)findViewById(R.id.pin_code_button_clear);

        pin_code_button_1.setOnClickListener(this);
        pin_code_button_2.setOnClickListener(this);
        pin_code_button_3.setOnClickListener(this);
        pin_code_button_4.setOnClickListener(this);
        pin_code_button_5.setOnClickListener(this);
        pin_code_button_6.setOnClickListener(this);
        pin_code_button_7.setOnClickListener(this);
        pin_code_button_8.setOnClickListener(this);
        pin_code_button_9.setOnClickListener(this);
        pin_code_button_0.setOnClickListener(this);
        pin_code_button_clear.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_lock, menu);
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

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("-1")){
            if (passCode.length() > 0){
                passCode = passCode.substring(0,passCode.length() - 1);
                Log.e("","passCode"+passCode);
                removeDrawable();
            }

        }else{
            if (passCode.length() < 4){
                passCode = passCode +v.getTag();
                setDrawable();
                Log.e("","passCode"+passCode);
            }

        }

    }
    private void removeDrawable(){
        if (passCode.length() == 1){
            pin_code_round2.setImageResource(R.drawable.pin_code_round_empty);
        }else if (passCode.length() == 2) {
            pin_code_round3.setImageResource(R.drawable.pin_code_round_empty);
        }else if (passCode.length() == 3) {
            pin_code_round4.setImageResource(R.drawable.pin_code_round_empty);
        }else if (passCode.length() == 0) {
            pin_code_round1.setImageResource(R.drawable.pin_code_round_empty);
        }
    }
    private void setDrawable(){
        if (passCode.length() == 1){
            pin_code_round1.setImageResource(R.drawable.pin_code_round_full);
        }else if (passCode.length() == 2) {
            pin_code_round2.setImageResource(R.drawable.pin_code_round_full);
        }else if (passCode.length() == 3) {
            pin_code_round3.setImageResource(R.drawable.pin_code_round_full);
        }else if (passCode.length() == 4) {
            pin_code_round4.setImageResource(R.drawable.pin_code_round_full);
            Log.e("getIntent().getStringExtra(\"lockcode\")",getIntent().getStringExtra("lockcode"));
            if(getIntent().getStringExtra("lockcode").equals(passCode))
            {
                prefs.edit().putString("passCode",passCode).commit();
                Log.e("passCode",prefs.getString("passCode",null));
                finish();
            }else{
                findViewById(R.id.round_container).startAnimation(shake);
                pin_code_round1.setImageResource(R.drawable.pin_code_round_empty);
                pin_code_round2.setImageResource(R.drawable.pin_code_round_empty);
                pin_code_round3.setImageResource(R.drawable.pin_code_round_empty);
                pin_code_round4.setImageResource(R.drawable.pin_code_round_empty);
                passCode = "";
                Log.e("passCode wrong",passCode);
            }
        }
    }

}
