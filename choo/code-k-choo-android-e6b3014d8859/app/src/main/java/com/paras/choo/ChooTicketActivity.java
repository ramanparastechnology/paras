package com.paras.choo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.paras.choo.R;

import com.paras.choo.beans.Singleton;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

public class ChooTicketActivity extends ActionBarActivity {
    ImageView backbutton, img, progressimage;
    ParseObject tempValues = null;
    SimpleDateFormat formatter;
    TextView choono, departure, planneddeparttime, submitdate, orderid, destination, plannedarrival, actualarrival, delay, trainnum, tickettype, tickettripprice, transferto, accountnumber, bankcode, statustext, textbasedonStatus, amountofrefund, date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choo_ticket);
        int pos = getIntent().getIntExtra("pos",0);
        choono = (TextView)findViewById(R.id.choono);
        departure = (TextView)findViewById(R.id.departure);
        planneddeparttime = (TextView)findViewById(R.id.planneddeparttime);
        submitdate = (TextView)findViewById(R.id.submitdate);
        backbutton = (ImageView)findViewById(R.id.back);
        img = (ImageView)findViewById(R.id.img);
        progressimage = (ImageView)findViewById(R.id.progressimage);
        orderid = (TextView)findViewById(R.id.orderid);
        destination = (TextView)findViewById(R.id.destination);
        plannedarrival = (TextView)findViewById(R.id.plannedarrival);
        actualarrival = (TextView)findViewById(R.id.actualarrival);
        delay = (TextView)findViewById(R.id.delay);
        trainnum = (TextView)findViewById(R.id.trainnum);
        tickettype = (TextView)findViewById(R.id.tickettype);
        tickettripprice = (TextView)findViewById(R.id.ticketprice);
        transferto = (TextView)findViewById(R.id.transferto);
        accountnumber = (TextView)findViewById(R.id.accountnumber);
        bankcode = (TextView)findViewById(R.id.bankcode);
        textbasedonStatus = (TextView)findViewById(R.id.textbasedonstatus);
        statustext = (TextView)findViewById(R.id.statustext);
        amountofrefund = (TextView)findViewById(R.id.amountofrefund);
        date = (TextView)findViewById(R.id.date);

        formatter = new SimpleDateFormat("dd.MM.yy HH:mm");
        tempValues = Singleton.getInstance().getTicketListTemp().get(pos);

        choono.setText("Choo-No:" + tempValues.getString("ORDER_ID"));
        departure.setText(tempValues.getString("DEPARTURE"));
        planneddeparttime.setText(formatter.format(tempValues.getDate("EXP_DEPARTURE")));
        try {
            if(tempValues.getJSONObject("ticketData").getString("TICKET_PRICE").equals("\u20ac 0,0"))
            {
                tickettripprice.setText("n.A.");
                tickettype.setText(tempValues.getJSONObject("ticketData").getString("USED_CARD_TYPE"));
            }else {
                String ticketprice = tempValues.getJSONObject("ticketData").getString("TICKET_PRICE").replace("\u20ac ", "");
                Log.e("ticketprice", ticketprice + " \u20ac");
                tickettripprice.setText(ticketprice + " \u20ac");
                tickettype.setText("Standardticket");
            }
            accountnumber.setText(tempValues.getJSONObject("persData").getString("ACCOUNT_NUMBER"));
            bankcode.setText(tempValues.getJSONObject("persData").getString("ACCOUNT_BLZ"));

        }catch (JSONException e){
            Log.e("exception","exception"+"exception");
        }

        try {
            if(tempValues.getJSONObject("persData").getString("ACCOUNT_HOLDER_NAME").equals("")){
                transferto.setText(tempValues.getJSONObject("persData").getString("FIRSTNAME") + " " + tempValues.getJSONObject("persData").getString("LASTNAME"));
                Log.e("FIRSTNAME LASTNAME",""+tempValues.getJSONObject("persData").getString("FIRSTNAME")+" "+tempValues.getJSONObject("persData").getString("LASTNAME"));
            }else {
                transferto.setText(tempValues.getJSONObject("persData").getString("ACCOUNT_HOLDER_NAME"));
                Log.e("ACCOUNT_HOLDER_NAME", "" + tempValues.getJSONObject("persData").getString("ACCOUNT_HOLDER_NAME"));
            }
        }
        catch (JSONException e)
        {
            Log.e("exception","ACCOUNT_HOLDER_NAME empty"+"exception");
            try {
                transferto.setText(tempValues.getJSONObject("persData").getString("FIRSTNAME") + " " + tempValues.getJSONObject("persData").getString("LASTNAME"));
                Log.e("FIRSTNAME LASTNAME",""+tempValues.getJSONObject("persData").getString("FIRSTNAME")+" "+tempValues.getJSONObject("persData").getString("LASTNAME"));
            }catch (Exception j){

            }
        }
        submitdate.setText(formatter.format(tempValues.getCreatedAt()));
        orderid.setText(tempValues.getString("ORDER_ID"));
        destination.setText(tempValues.getString("DESTINATION"));
        plannedarrival.setText(formatter.format(tempValues.getDate("EXP_ARRIVAL")));
        actualarrival.setText(formatter.format(tempValues.getDate("ACT_ARRIVAL")));
        Log.e("STATUS_ID", tempValues.getString("STATUS_ID"));

        if(tempValues.getString("STATUS_ID").equals("1"))
        {
            amountofrefund.setText(getString(R.string.outstanding));
            amountofrefund.setTextColor(getResources().getColor(R.color.statecolor));
            date.setText(getString(R.string.outstanding));
            date.setTextColor(getResources().getColor(R.color.statecolor));
            textbasedonStatus.setText(getString(R.string.text1));
            statustext.setText(getString(R.string.status1));
            statustext.setTextColor(getResources().getColor(R.color.statecolor));
            progressimage.setImageResource(R.drawable.stateiicon);
        }else if(tempValues.getString("STATUS_ID").equals("3"))
        {
            amountofrefund.setText(tempValues.getInt("REFUND")+"");
            amountofrefund.setTextColor(getResources().getColor(R.color.statecolorthree));
            date.setText(tempValues.getDate("PAYDATE")+"");
            date.setTextColor(getResources().getColor(R.color.statecolorthree));
            textbasedonStatus.setText(getString(R.string.text3));
            statustext.setText(getString(R.string.status3));
            statustext.setTextColor(getResources().getColor(R.color.statecolorthree));
            progressimage.setImageResource(R.drawable.stateiconthree);
        }else if(tempValues.getString("STATUS_ID").equals("5"))
        {
            amountofrefund.setText(getString(R.string.outstanding));
            amountofrefund.setTextColor(getResources().getColor(R.color.statecolor));
            date.setText(getString(R.string.outstanding));
            date.setTextColor(getResources().getColor(R.color.statecolor));
            textbasedonStatus.setText(getString(R.string.text5));
            statustext.setText(getString(R.string.status5));
            statustext.setTextColor(getResources().getColor(R.color.statecolornine));
            progressimage.setImageResource(R.drawable.stateiconnine);
        }else if(tempValues.getString("STATUS_ID").equals("9"))
        {
            amountofrefund.setText(getString(R.string.outstanding));
            amountofrefund.setTextColor(getResources().getColor(R.color.statecolor));
            date.setText(getString(R.string.outstanding));
            date.setTextColor(getResources().getColor(R.color.statecolor));
            textbasedonStatus.setText(getString(R.string.text9));
            statustext.setText(getString(R.string.status9));
            statustext.setTextColor(getResources().getColor(R.color.statecolornine));
            progressimage.setImageResource(R.drawable.stateiconnine);
        }


        if("en".equals(getString(R.string.lang)) ) {
            delay.setText(tempValues.getInt("MINUTES_LATE") + " Minutes");
        }
        else{
            delay.setText(tempValues.getInt("MINUTES_LATE") + " Minuten");
        }
        trainnum.setText(tempValues.getString("DEST_TRAIN_NUMBER"));
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ParseFile image = tempValues.getParseFile("TICKET_SCAN");

        image.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitpic = BitmapFactory.decodeByteArray(data, 0, data.length);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitpic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                img.setImageBitmap(bitpic);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choo_ticket, menu);
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
