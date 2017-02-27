package com.allegra.handyuvisa;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.allegra.handyuvisa.utils.Constants;

/**
 * Created by jsandoval on 3/06/16.
 */
public class AppJavaScriptProxyConcierge {
    private Activity activity = null;


    public AppJavaScriptProxyConcierge(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void postMessage(String message) {
        //Log.d("Message", message);

        ((ConciergeSearchActivity)activity).onePocketmessage = message;
        if(checkLogin())  ((ConciergeSearchActivity) activity).openOnePocket();
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
