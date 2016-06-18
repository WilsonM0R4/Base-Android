package com.allem.alleminmotion.visacheckout.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeySaver {
	private static final String AWKEY = "Allemevent";
	private static final String AWPREFIX = "AE_";

    /**
     *
     * @param a
     * @return
     */
	public static String getIMEI( Context a ) {
		TelephonyManager telephonyManager = (TelephonyManager) a.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

    /**
     *
     * @param a
     * @return
     */
	public static String getDeviceID( Context a ) {
		return Secure.getString(a.getContentResolver(), Secure.ANDROID_ID);
	}

    /**
     *
     * @param a
     * @param keyname
     * @param f
     */
	public static void saveShare(Context a,String keyname ,boolean f ) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean( AWPREFIX + keyname, f);
	    editor.commit();
	}

    /**
     *
     * @param a
     * @param keyname
     * @param f
     */
	public static void saveShare(Context a,String keyname ,int f ) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putInt( AWPREFIX + keyname, f);
	    editor.commit();
	}

    /**
     *
     * @param a
     * @param keyname
     * @param f
     */
	public static void saveShare(Context a,String keyname ,String f ) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString( AWPREFIX + keyname, f);
        editor.commit();
	}

    /**
     *
     * @param a
     * @param keyname
     * @param f
     */
    public static void saveShare(Context a,String keyname,Set<String> f){
        SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(AWPREFIX +keyname, f);
        editor.commit();
    }

    /**
     *
     * @param a
     * @param keyname
     * @return
     */
	public static boolean getBoolSavedShare(Context a, String keyname) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
		return settings.getBoolean( AWPREFIX + keyname, false);
	}

    /**
     *
     * @param a
     * @param keyname
     * @return
     */
	public static int getIntSavedShare(Context a, String keyname) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
		return settings.getInt( AWPREFIX + keyname, -1);
	}

    /**
     *
     * @param a
     * @param keyname
     * @return
     */
	public static String getStringSavedShare(Context a, String keyname) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
		return settings.getString( AWPREFIX + keyname, null);
	}

    /**
     *
     * @param a
     * @param keyname
     * @return
     */
    public static Set<String> getStringSetSavedShare(Context a, String keyname){
        SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
        return settings.getStringSet( AWPREFIX + keyname, new HashSet<String>());
    }

    /**
     *
     * @param a
     * @return
     */
	public static Map<String,?> getAll(Context a){
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
		return settings.getAll();
	}

    /**
     *
     * @return
     */
	public static String getPrefix(){
		return AWPREFIX;
	}

    /**
     *
     * @param a
     * @param keyname
     */
	public static void removeKey (Context a,String keyname) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
		if(settings.contains(AWPREFIX+keyname)){
            SharedPreferences.Editor editor = settings.edit();
            editor.remove(AWPREFIX+keyname);
            editor.commit();
        }
	}

    /**
     *
     * @param a
     * @param keyname
     * @return
     */
	public static boolean isExist(Context a, String keyname) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
		if (settings.contains(AWPREFIX+keyname)) return true;
	    else return false;
	}
}
