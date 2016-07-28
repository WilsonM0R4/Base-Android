package com.allegra.handyuvisa;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.allegra.handyuvisa.utils.Constants;

/**
 * Created by RafaelRodriguez on 27/01/16.
 */
public class AppJavaScriptProxyMarketPlace {

    private Activity activity = null;

    public AppJavaScriptProxyMarketPlace(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void postMessage(String message) {
        Log.d("Message", message);
        ((MarketPlaceActivity)activity).onePocketmessage =message;
        if (checkLogin())  ((MarketPlaceActivity)activity).openOnePocket();
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
