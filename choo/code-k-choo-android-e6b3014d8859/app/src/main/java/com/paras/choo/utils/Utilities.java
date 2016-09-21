package com.paras.choo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;


import com.paras.choo.ChooApplication;
import com.paras.choo.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by paras on 14-09-2015.
 */
public class Utilities {
    public static String printKeyHash(Activity context){
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }
    public static Bitmap decodeUri(Context c, Uri uri, final int width,final int height)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < width || height_tmp / 2 < height)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }
    public static  byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    public static void writeJsonCache(JSONObject object, String filename) {
        try {
            Log.e("", "---------------" + object.getString(ChooApplication.USERNAME));
            File dir = new File(ChooApplication.CHOO_EXTERNAL_DIRECTORY + "/configs");
            dir.mkdir();
            File file1 = new File(dir, filename);

            FileOutputStream fOut = new FileOutputStream(file1);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(object.toString());
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String readJsonFile(String filename) {
        String jsonStr = null;
        try {
            File jsonFile = new File(ChooApplication.CHOO_EXTERNAL_DIRECTORY
                    + "/configs/" + filename);

            if(!jsonFile.exists()) {
                Log.e("readJsonFile", "File Not Found");
                    return  null;
//                jsonFile = new File(ChooApplication.CHOO_EXTERNAL_DIRECTORY
//                        + "/configs/" + filename);
            } else {
                Log.e("readJsonFile", "File Found");
            }

            FileInputStream stream = new FileInputStream(jsonFile);
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer byteBuffer = fc.map(
                        FileChannel.MapMode.READ_ONLY, 0, fc.size());
                jsonStr = Charset.defaultCharset().decode(byteBuffer)
                        .toString();
            } finally {
                stream.close();
            }
        } catch (Exception e) {
            Log.e("readJsonFile", e.getMessage());
        }

        return jsonStr;
    }
    public void uploadCachedAnswers(String filename, Context context) {

        try {
            String jsonContent = readJsonFile(filename);
            JSONObject object = new JSONObject();
            File file = new File(ChooApplication.CHOO_EXTERNAL_DIRECTORY
                    + "/configs/" + filename);

            if(jsonContent.equals("")) {
                Log.e("JSON", "No cached answers.");
            } else {


                    JSONObject obj = new JSONObject(jsonContent);
                    String franchisee_id = obj.getString("franchisee_id");
                    String reviewer = obj.getString("reviewer");
                    long timestamp = obj.getLong("timestamp");
                    String skill_answers = obj.getString("skill_answers");

                    object.put("skill_answers", new JSONObject(skill_answers));
                    object.put("franchisee_id", franchisee_id);
                    object.put("reviewer", reviewer);
                    object.put("timestamp", timestamp);




            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void writeJsonFile(JSONObject object, String filename) {

        try {

//            JSONObject obj = new JSONObject();
            String jsonContent = readJsonFile(filename);




            File file = new File(ChooApplication.CHOO_EXTERNAL_DIRECTORY + "/configs/" +
                    filename);
            file.createNewFile();



            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(object.toString());
            myOutWriter.close();
            fOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  static Date getParseDate(String dateInString){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy HH:mm");
        Date date = null ;
        try {

             date = formatter.parse(dateInString);
            System.out.println(date);
            System.out.println(formatter.format(date));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }
    public  static Date getParseDatee(String dateInString, Activity a){
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf;
        if("en".equals( a.getString(R.string.lang) ) ) {
            sdf = new SimpleDateFormat(myFormat, Locale.US);
        }else{
            sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);
        }
        Date date = null ;
        try {

            date = sdf.parse(dateInString);
            System.out.println(date);
            System.out.println(sdf.format(date));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }
}
