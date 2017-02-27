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
        //Log.d("Message", message);

        ((com.allegra.handyuvisa.Mcardhtml)activity).onePocketmessage = message;
        if(checkLogin()) ((com.allegra.handyuvisa.Mcardhtml) activity).openOnePocket();
        // temporal change

    }

    private boolean checkLogin() {
        if(((com.allegra.handyuvisa.VisaCheckoutApp)activity.getApplication()).getIdSession()==null){
            Intent i =new Intent(activity,LoginActivity.class);
            activity.startActivityForResult(i, Constants.ONE_POCKET_NEEDS_LOGIN);
            return false;
        }

        return true;
    }
}
