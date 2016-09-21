package com.paras.choo;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.paras.choo.utils.HtmlDownloadhelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.ArrayList;


public class Privacy extends ActionBarActivity {
    private Toolbar toolbar;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy2);
        if("en".equals( getString(R.string.lang) ) ) {
            new LoadHtml().execute();
        }else{
            new LoadDeHtml().execute();
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
        {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
        toolbar.setLogo(R.mipmap.homeback);
        toolbar.setTitle("Choo");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        View logoView = getToolbarLogoView(toolbar);
        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        WebView webView = (WebView)findViewById(R.id.web);
        if("en".equals( getString(R.string.lang) ) ) {
            File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/privacy.html");
            if (myFile.exists()) {
                Log.e("", "File EXISTS");
                webView.loadUrl("file://" + Environment.getExternalStorageDirectory() + "/cache/privacy.html");
            } else {
                Log.e("", "File Not EXISTS");
                webView.loadUrl("file:///android_asset/privacy.html");
            }
        }else{
            File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/privacy_de.html");
            if (myFile.exists()) {
                Log.e("", "File EXISTS");
                webView.loadUrl("file://" + Environment.getExternalStorageDirectory() + "/cache/privacy_de.html");
            } else {
                Log.e("", "File Not EXISTS");
                webView.loadUrl("file:///android_asset/privacy_de.html");
            }
        }
    }
    private View getToolbarLogoView(Toolbar toolbar) {
        //check if contentDescription previously was set
        boolean hadContentDescription = android.text.TextUtils.isEmpty(toolbar.getLogoDescription());
        String contentDescription = String.valueOf(!hadContentDescription ? toolbar.getLogoDescription() : "logoContentDescription");
        toolbar.setLogoDescription(contentDescription);
        ArrayList<View> potentialViews = new ArrayList<View>();
        //find the view based on it's content description, set programatically or with android:contentDescription
        toolbar.findViewsWithText(potentialViews,contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        //Nav icon is always instantiated at this point because calling setLogoDescription ensures its existence
        View logoIcon = null;
        if(potentialViews.size() > 0){
            logoIcon = potentialViews.get(0);
        }
        //Clear content description if not previously present
        if(hadContentDescription)
            toolbar.setLogoDescription(null);
        return logoIcon;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_privacy, menu);
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
                HttpGet request = new HttpGet("http://www.chooapp.com/iOS_files/en/en_app_privacy.html");
                HttpResponse response = client.execute(request);
                result = EntityUtils.toString(response.getEntity());
                Log.e("", "result" + result);
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
                HtmlDownloadhelper.saveHtmlFile("privacy.html", context, "http://www.chooapp.com/iOS_files/en/en_app_privacy.html");
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
                HttpGet request = new HttpGet("http://www.chooapp.com/iOS_files/app_privacy.html");
                HttpResponse response = client.execute(request);
                result = EntityUtils.toString(response.getEntity());
                Log.e("", "result" + result);
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
                HtmlDownloadhelper.saveHtmlFile("privacy_de.html", context, "http://www.chooapp.com/iOS_files/app_privacy.html");
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
