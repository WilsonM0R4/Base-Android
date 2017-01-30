package com.allegra.handyuvisa;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.allegra.handyuvisa.utils.Constants;

/**
 * Created by RafaelRodriguez on 27/01/16.
 */
public class AppJavaScriptProxyHotels {

    private Activity activity = null;
    private final String  TAG  = "AppJavaScriptProxyHotel";
    public AppJavaScriptProxyHotels(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void postMessage(String message) {
        Log.e(TAG,message);
        Log.e("Repeat message", message);

        ((HotelSearchActivity)activity).onePocketmessage = message;
        if(checkLogin()) ((HotelSearchActivity) activity).openOnePocket();
    }

    private boolean checkLogin() {
        if(((VisaCheckoutApp)activity.getApplication()).getIdSession()==null){
            Intent i =new Intent(activity,LoginActivity.class);
            activity.startActivityForResult(i, Constants.ONE_POCKET_NEEDS_LOGIN);
            return false;
        }

        return true;
    }

}
