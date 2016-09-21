package com.paras.choo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paras.choo.utils.HtmlDownloadhelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;


public class TermsAndCondSendReqActivity extends ActionBarActivity {
    LinearLayout bottom;
    TextView cancel, accept;
    WebView webView;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_cond_send_req);
        if("en".equals( getString(R.string.lang) ) ) {
            new LoadHtml().execute();
        }else{
            new LoadDeHtml().execute();
        }
        bottom = (LinearLayout)findViewById(R.id.bottom);
        cancel = (TextView)findViewById(R.id.cancel);
        accept = (TextView)findViewById(R.id.accept);
        webView = (WebView)findViewById(R.id.web);
        if("en".equals( getString(R.string.lang) ) ) {
            File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/termsandconditions.html");
            if (myFile.exists()) {
                Log.e("", "File EXISTS");
                webView.loadUrl("file://" + Environment.getExternalStorageDirectory() + "/cache/termsandconditions.html");
            } else {
                Log.e("", "File Not EXISTS");
                webView.loadUrl("file:///android_asset/termsandconditions.html");
            }
        }else{
            File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/termsandconditions_de.html");
            if (myFile.exists()) {
                Log.e("", "File EXISTS");
                webView.loadUrl("file://" + Environment.getExternalStorageDirectory() + "/cache/termsandconditions_de.html");
            } else {
                Log.e("", "File Not EXISTS");
                webView.loadUrl("file:///android_asset/termsandconditions_de.html");
            }
        }
        bottom.bringToFront();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a new intent...
                Intent intent = new Intent();
                intent.putExtra("position", 0);
                setResult(RESULT_OK,intent);
                //close this Activity...
                finish();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a new intent...
                Intent intent = new Intent();
                intent.putExtra("position", 1);
                setResult(RESULT_OK,intent);
                //close this Activity...
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_terms_and_cond_send_req, menu);
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
    public class LoadHtml extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            String server = null;
            try {

                HttpClient client = HttpClientBuilder.create().build();
                HttpGet request = new HttpGet("http://www.chooapp.com/iOS_files/en/en_app_agb.html");
                HttpResponse response = client.execute(request);
                result = EntityUtils.toString(response.getEntity());
                Log.e("", "result" + result);
                server = response.getFirstHeader("Last-Modified").getValue();
                Log.e("Key : Last-Modified" , "Value : " + server);

            }catch (Exception e)
            {

            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null){
                new DownloadHtml().execute();


            }
        }
    }
    public class DownloadHtml extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;

            try {
                HtmlDownloadhelper.saveHtmlFile("termsandconditions.html", context, "http://www.chooapp.com/iOS_files/en/en_app_agb.html");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null){
            }
        }

    }
    public class LoadDeHtml extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            String server = null;
            try {

                HttpClient client = HttpClientBuilder.create().build();
                HttpGet request = new HttpGet("http://www.chooapp.com/iOS_files/app_agb.html");
                HttpResponse response = client.execute(request);
                result = EntityUtils.toString(response.getEntity());
                Log.e("", "result" + result);
                server = response.getFirstHeader("Last-Modified").getValue();
                Log.e("Key : Last-Modified" , "Value : " + server);

            }catch (Exception e)
            {

            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null){
                new DownloadDeHtml().execute();


            }
        }
    }
    public class DownloadDeHtml extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;

            try {
                HtmlDownloadhelper.saveHtmlFile("termsandconditions_de.html", context, "http://www.chooapp.com/iOS_files/app_agb.html");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null){
            }
        }

    }
}
