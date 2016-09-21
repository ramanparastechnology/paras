package com.paras.choo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.paras.choo.utils.ChooPrefsUser;
import com.paras.choo.utils.MyStringRandomGen;
import com.paras.choo.utils.Utilities;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by paras on 23-07-2015.
 */
public class SingletonUserSignUp {
    public static  SharedPreferences sharedPreferences = null;
    public static SharedPreferences.Editor editor = null;
    private static SingletonUserSignUp singleton = new SingletonUserSignUp( );
    private  static String USERNAME = null;
    private  static String PASSWORD = null;
    private  static File file = null ;

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private SingletonUserSignUp(){ }

    /* Static 'instance' method */
    public static SingletonUserSignUp getInstance( ) {
        sharedPreferences = ChooApplication.getChooApplicationContext().getSharedPreferences(ChooPrefsUser.CHOO_PREF_USER, Context.MODE_PRIVATE);
        return singleton;
    }
    /* Other methods protected by singleton-ness */
    public  static  void loginMethod( ) {
//        ParseUser currentUser = ParseUser.getCurrentUser();

        Log.e("","sharedPreferences.getString(ChooPrefsUser.USERNAME,null)"+sharedPreferences.getString(ChooPrefsUser.USERNAME,null));
        /**
         * 1 Check for Json file in memory card
         * if(Exist read data and do login)
         * else
         * sign up and create json file and store Credentials
         */
        file = new File(ChooApplication.CHOO_EXTERNAL_DIRECTORY + "/configs/" +ChooApplication.CREDENTIALS_FILENAME);
        if (file.exists()){
//            read data from file and do login
            userLogin();
            ParseUser parseUser = ParseUser.getCurrentUser();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        }else{
            userSignUp();
        }


//          if(sharedPreferences.getString(ChooPrefsUser.USERNAME,null) != null){
////              do login
//
//              userLogin();
//          }else{
////              do signup
//              userSignUp();
//          }
    }
    private static void userLogin(){
//        Read file form
        String jsonContent  = Utilities.readJsonFile(ChooApplication.CREDENTIALS_FILENAME);
        try{
            JSONObject object = new JSONObject(jsonContent);
            USERNAME =  object.getString(ChooApplication.USERNAME);
            PASSWORD =  object.getString(ChooApplication.PASSWORD);
            Log.e("",""+USERNAME);
//            String RunCount = object.getString("RunCount");
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (jsonContent != null){
            ParseUser.logInInBackground(USERNAME, PASSWORD, new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (e == null && user != null) {
                        // Hooray! The user is logged in.

                        Log.e("", "Hooray! The user is logged in." + user.getObjectId());
//                        saveData();
//                        sharedPreferences.edit().putString(ChooPrefsUser.OBJECT_ID,user.getObjectId()).commit();
                        SaveUserPointerToInstallation();
                    } else if (user == null) {
                        Log.e("usernameOrPasswordIsInvalid", "" + USERNAME + "-->" + PASSWORD);
                        File folder = Environment.getExternalStorageDirectory();
                        String fileName = folder.getPath() + "/configs/system_file.json";
                        File myFile = new File(fileName);
                        if (myFile.exists())
                            myFile.delete();
                        if (myFile.exists()) {
                            Log.e("", "FIle not deleted");
                        } else {
                            Log.e("", "File deleted");
                            userSignUp();
                        }
                    } else {
                        // Signup failed. Look at the ParseException to see what happened.
                        Log.e("", "login failed. Look at the ParseException to see what happened.");
                    }
                }
            });
        }else {
            userSignUp();
        }

    }
    private  static void userSignUp(){

      final String UserName = new MyStringRandomGen(12).generateRandomString();
      final String Password = new MyStringRandomGen(6).generateRandomString();
        ParseUser user = new ParseUser();
        user.setUsername(UserName);
        user.setPassword(Password);
        user.put("RunCount", 1);
        user.put("filled","1");
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Log.e("", "Sign up done" + e);
                    SaveUserPointerToInstallation();
                    USERNAME = UserName;
                    PASSWORD = Password ;
                    JSONObject object = null ;
//                    Create and STORE TO FILE

                    Log.e("","UserName"+UserName);
                    Log.e("","PA"+Password);
                    try {
                        object = new JSONObject();
                        object.put(ChooApplication.USERNAME,UserName);
                        object.put(ChooApplication.PASSWORD,Password);
                        object.put("RunCount", "1");

                    }catch (JSONException e1){
                        e1.printStackTrace();
                    }
                    Utilities.writeJsonCache(object,ChooApplication.CREDENTIALS_FILENAME);


                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.e("", "Sign up Exception" + e);
                }
            }
        });
    }

    private static void SaveUserPointerToInstallation(){
         ParseInstallation parseInstallation =  ParseInstallation.getCurrentInstallation();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("_Installation");
        Log.e("","parseInstallation.getObjectId()"+parseInstallation.getObjectId());
// Retrieve the object by id
        query.getInBackground(parseInstallation.getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    Log.e("","ParseUser.getCurrentUser()"+ ParseUser.getCurrentUser().getUsername());
                    Log.e("deviceToken = ", "" + ParseInstallation.getCurrentInstallation().getInstallationId());
                    gameScore.put("user", ParseUser.getCurrentUser());
                    gameScore.put("deviceModel",getDeviceName());
                    gameScore.put("runningOSVersion", Build.VERSION.RELEASE);

//                    gameScore.put("appVersion", "2251");
//                    gameScore.put("appName", "Choo");

                    gameScore.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.e("", "data Added");
                                Log.e("getDeviceName()", "" + getDeviceName());
//                                Log.e("android.os.Build.VERSION.RELEASE",""+android.os.Build.VERSION.RELEASE);
                            } else {
                                Log.e("", "data not Added");
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
//        ParseObject save = new ParseObject("_Installation");
//        save.put("user", ParseUser.getCurrentUser());
//
//
//        save.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e == null){
//                    Log.e("","Done poinetr User");
//                }else{
//                    Log.e("","Exception User");
//                }
//            }
//        });
    }
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
//    private static void  saveData(){
//        ParseUser parseUser = ParseUser.getCurrentUser();
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
//        // Retrieve the object by id
//        query.getInBackground(parseUser.getObjectId(), new GetCallback<ParseObject>() {
//            public void done(ParseObject user, ParseException e) {
//                if (e == null) {
//                    Log.e("ID" + user.getObjectId(), "Username" + user.getString("FIRSTNAME"));
//
//                    user.increment("RunCount");
//                    user.put("EMAIL","choo@gmail.com");
//                    user.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if (e == null) {
//                                Log.e("", "Updated");
//
//
//                            } else {
//                                Log.e("", "Updated exception" + e);
////                                exception = true;
//                            }
//                        }
//                    });
//
//                } else {
//                    Log.e("", "Updatiin Exception" + e);
////                    exception = true;
//                }
//            }
//        });
//    }
}
