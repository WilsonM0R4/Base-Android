package com.allegra.handyuvisa.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.allegra.handyuvisa.VisaCheckoutApp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by victor on 05/07/2014.
 */
public class Util {

    public static SimpleDateFormat D_M_Formatter = new SimpleDateFormat("dd MMM");
    public static SimpleDateFormat Y_Formatter = new SimpleDateFormat("yyyy");
    public static SimpleDateFormat DOW_M_Formatter = new SimpleDateFormat("E dd");
    public static SimpleDateFormat DOW_Formatter = new SimpleDateFormat("E");
    public static SimpleDateFormat D_Formatter = new SimpleDateFormat("dd");
    public static SimpleDateFormat M_Y_Formatter = new SimpleDateFormat("MMM yyyy");
    public static SimpleDateFormat M_Formatter = new SimpleDateFormat("MMM");
    public static SimpleDateFormat Bookings_Formatter = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat Day_Formatter = new SimpleDateFormat("EEE dd");
    public static SimpleDateFormat Bookings_Formatter_Hotels = new SimpleDateFormat("MM/dd/yyyy");


   // public static


    public static boolean packageExists(Context ctx,String targetPackage){
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = ctx.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(targetPackage)) return true;
        }
        return false;
    }

    public static boolean hasInternetConnectivity(Context ctx){

        ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
            if (activeNetwork != null) {
                return activeNetwork.isConnected();
            }
        }
        return false;
    }


    public static boolean isFileUrl(String url, String extension){
        boolean isFile=false;
        url=url.toLowerCase();extension=extension.toLowerCase();
        //if contains extension
        if(url.contains(extension)){
            //if also contains ? query string
            if(url.contains("?")){
                //strip off query string
                url=url.substring(0,url.indexOf("?"));
            }
            //if ends in file extension
            if(url.lastIndexOf(extension)==url.length()-extension.length()){
                //then yep. This is a file
                isFile=true;
            }
        }
        return isFile;
    }

    public static String getFormattedTime(Date dateObj) {
        return new SimpleDateFormat("EEEE, MMM dd, yyyy").format(dateObj);
    }

    public static String getFormattedTime() {
        return new SimpleDateFormat("EEEE, MMM dd, yyyy").format(getCurrentTime());
    }

    public static Date getCurrentTime() {
        return Calendar.getInstance().getTime();
    }

//    public static void initFabButton(Button button, int iconResourceId, TextView view, int statusResourceId) {
//        button = (FabButton) findViewById(R.id.btn_call);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
//            button.setIcon(getResources().getDrawable(R.drawable.flight_result, null),
//                    getResources().getDrawable(R.drawable.flight_result, null));
//        } else {
//            button.setIcon(getResources().getDrawable(R.drawable.flight_result),
//                    getResources().getDrawable(R.drawable.flight_result));
//
//        }
//        tv_status = (TextView)findViewById(R.id.tv_status_otc);
//        tv_status.setText(R.string.txt_lbl_searchFlightsWait);
//
//        button.showProgress(true);
//
//    }

    public static boolean isAuthenticated(Activity activity) {
        return (((VisaCheckoutApp)activity.getApplication()).getIdSession() != null);
    }

    public static String md5Encrypt(String str) {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(str.getBytes());

        byte byteData[] = md.digest();

        // convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        // System.out.println("Digest(in hex format):: " + sb.toString());

        // convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }



}
