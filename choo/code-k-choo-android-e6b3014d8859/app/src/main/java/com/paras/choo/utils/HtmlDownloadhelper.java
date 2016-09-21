package com.paras.choo.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by paras on 13-08-2015.
 */
public class HtmlDownloadhelper {

    public static File getCacheFolder(Context context) {
        File cacheDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(Environment.getExternalStorageDirectory(), "cache");
            if(!cacheDir.isDirectory()) {
                cacheDir.mkdirs();
            }
        }

        if(!cacheDir.isDirectory()) {
            cacheDir = context.getCacheDir(); //get system cache folder
        }

        return cacheDir;
    }
    public File getDataFolder(Context context) {
        File dataDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dataDir = new File(Environment.getExternalStorageDirectory(), "data");
            if(!dataDir.isDirectory()) {
                dataDir.mkdirs();
            }
        }

        if(!dataDir.isDirectory()) {
            dataDir = context.getFilesDir();
        }

        return dataDir;
    }
    public static boolean saveHtmlFile(String filename ,Context context,String Url){
        boolean isComplete = false ;
        try {
        URL webURL = new URL(Url);
        URLConnection connection = webURL.openConnection();
        connection.connect();
        InputStream inputStream = new BufferedInputStream(webURL.openStream(), 10240);
        File cacheDir = getCacheFolder(context);
        File cacheFile = new File(cacheDir, filename);
        FileOutputStream outputStream = new FileOutputStream(cacheFile);

        byte buffer[] = new byte[1024];
        int dataSize;
        int loadedSize = 0;
        while ((dataSize = inputStream.read(buffer)) != -1) {
            loadedSize += dataSize;
            outputStream.write(buffer, 0, dataSize);
        }

        outputStream.close();
        if (dataSize == dataSize){
            isComplete = true ;
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("", "isComplete" + isComplete);
        return  isComplete;
    }
}
