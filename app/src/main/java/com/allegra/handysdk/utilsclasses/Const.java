package com.allegra.handysdk.utilsclasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by sergiofarfan on 20/06/16.
 */
public class Const {
    public static String DEVICE_TOKEN="";
    public static int menu_selection=1;
    static File cacheDir;
    public static String DEVICE_ID="";
    public static String SENDER_ID="";


    public static boolean IsCoreect(final Context context, String text){

        if (TextUtils.isEmpty(text))
            return true;
        else
            return false;
    }

    public static boolean checkEmail(String uemail) {
        return email_pattern.matcher(uemail).matches();
    }

    // email pattern
    private static Pattern email_pattern = Pattern
            .compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");

    public static boolean IsInternetConnectionFound(final Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }

    public static void executeLogcat(Context context){
        Log.d("System out", "Create Log file..");

        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"Handy");
        else
            cacheDir=context.getCacheDir();

        if(!cacheDir.exists())
            cacheDir.mkdirs();

        File logFile = new File(cacheDir, "logoutput.log"); // log file name
        int sizePerFile = 60; // size in kilobytes
        int rotationCount = 10; // file rotation count
        String filter = "D"; // Debug priority

        String[] args = new String[] { "logcat",
                "-v", "time",
                "-f",logFile.getAbsolutePath(),
                "-r", Integer.toString(sizePerFile),
                "-n", Integer.toString(rotationCount),
                "*:" + filter };
        try {
            Runtime.getRuntime().exec(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
