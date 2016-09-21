package com.paras.choo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.paras.choo.utils.ParseUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;

import io.fabric.sdk.android.Fabric;
import java.util.Locale;

/**
 * Created by sony on 05-07-2015.
 */
public class ChooApplication extends Application{
    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    public static Context context;
    public static SharedPreferences sharedPreferences = null ;
    public static String CREDENTIALS_FILENAME = "system_file.json";
    public static String CHOO_EXTERNAL_DIRECTORY = Environment
            .getExternalStorageDirectory() + "";
    public static  String USERNAME = "username" ;
    public static  String PASSWORD = "password" ;
    private static String TAG = ParseUtils.class.getSimpleName();
    private static ChooApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        context = getApplicationContext();
        mInstance = this;

        sharedPreferences = getSharedPreferences("Prefs", MODE_PRIVATE);

        // register with parse
        ParseUtils.registerParse(this);
        try {
            Log.d("info!", "write parse _installation!");

            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String marketingVersion = pInfo.versionName;
            int appVersion = pInfo.versionCode;
            String appVersionStr = String.valueOf(appVersion);
            Log.e("marketingVersion",""+marketingVersion);
            Log.e("appVersionStr",""+appVersionStr);

            Locale current = getResources().getConfiguration().locale;
            Log.e("current Locale", "" + current);

            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("marketingVersion",marketingVersion);
            installation.put("buildVersion",appVersionStr);
            installation.put("localeIdentifier", current + "");
            installation.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.e("", "marketingVersion Updated");
                    } else {
                        Log.e("", "marketingVersion Updated Failed");
                    }
                }
            });
            /*
            installation.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.e("", "marketingVersion Updated");
                    } else {
                        Log.e("", "marketingVersion Updated Failed");
                    }
                }
            });
            */

        }catch (Exception exc) {
            //handle exception
            exc.printStackTrace();
            Log.d("exception!", "exception!");
        }

    }
    public static synchronized ChooApplication getInstance() {
        return mInstance;
    }

    public static Context getChooApplicationContext(){
        return context ;

    }
}
