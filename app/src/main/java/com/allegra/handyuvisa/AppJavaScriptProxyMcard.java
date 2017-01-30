package com.allegra.handyuvisa;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.allegra.handyuvisa.utils.Constants;

/**
 * Created by sergiofarfan on 23/07/16.
 */
public class AppJavaScriptProxyMcard {

    private Activity activity = null;

    public AppJavaScriptProxyMcard(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void postMessage(String message) {
        Log.e("Message",message);
        Log.e("Repeat message", message);

        ((Mcardhtml)activity).onePocketmessage = message;
        if(checkLogin()) ((Mcardhtml) activity).openOnePocket();
        // temporal change

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
