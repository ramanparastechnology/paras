package com.paras.choo;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.paras.choo.utils.HtmlDownloadhelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;


public class ArrivalTrainInfoActivity extends ActionBarActivity {
    private Toolbar toolbar;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrival_train_info);
        if("en".equals( getString(R.string.lang) ) ) {
            new LoadHtml().execute();
        }else{
            new LoadDeHtml().execute();
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
        {
            toolbar.setTitle("");
            toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
            setSupportActionBar(toolbar);
        }
        WebView webView = (WebView)findViewById(R.id.web);
        if("en".equals( getString(R.string.lang) ) ) {
            File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/arrival.html");
            if (myFile.exists()) {
                Log.e("", "File EXISTS");
                webView.loadUrl("file://" + Environment.getExternalStorageDirectory() + "/cache/arrival.html");
            } else {
                Log.e("", "File Not EXISTS");
                webView.loadUrl("file:///android_asset/arrival.html");
            }
        }else{
            File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/arrival_de.html");

            if (myFile.exists()) {
                Log.e("", "File EXISTS");
                webView.loadUrl("file://" + Environment.getExternalStorageDirectory() + "/cache/arrival_de.html");
            } else {
                Log.e("", "File NoT EXISTS");
                webView.loadUrl("file:///android_asset/arrival_de.html");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_data_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (true) {
//            overridePendingTransition(R.animator.card_flip_right_in, R.animator.card_flip_right_out);
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.card_flip_right_in, R.animator.card_flip_right_out);
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
                HttpGet request = new HttpGet("http://www.chooapp.com/iOS_files/en/en_app_erstattung_ankunft-info.html");
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
                HtmlDownloadhelper.saveHtmlFile("arrival.html", context, "http://www.chooapp.com/iOS_files/en/en_app_erstattung_ankunft-info.html");
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
                HttpGet request = new HttpGet("http://www.chooapp.com/iOS_files/app_erstattung_ankunft-info.html");
                HttpResponse response = client.execute(request);
                result = EntityUtils.toString(response.getEntity());
                Log.e("","result"+result);
                //get header by 'key'
                server = response.getFirstHeader("Last-Modified").getValue();
                Log.e("Key : Last-Modified", "Value : " + server);

            }catch (Exception e)
            {

            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null){
                /**
                 * if Last Modified in preferences null set new modified and Load new Webpage
                 * else
                 * if last modified in preferrences is smaller than header value then loads new webpage
                 * else
                 * if last modified in preferrences is equal header value then loads new webpage
                 *
                 */
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
                HtmlDownloadhelper.saveHtmlFile("arrival_de.html", context, "http://www.chooapp.com/iOS_files/app_erstattung_ankunft-info.html");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null){
                /**
                 * if Last Modified in preferences null set new modified and Load new Webpage
                 * else
                 * if last modified in preferrences is smaller than header value then loads new webpage
                 * else
                 * if last modified in preferrences is equal header value then loads new webpage
                 *
                 */
            }
        }

    }
}
