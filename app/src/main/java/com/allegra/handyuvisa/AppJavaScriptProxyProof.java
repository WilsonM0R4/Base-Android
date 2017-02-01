package com.allegra.handyuvisa;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.allegra.handyuvisa.utils.Constants;

/**
 * Created by jsandoval on 1/12/16.
 */

public class AppJavaScriptProxyProof {

    private Activity activity = null;

    public AppJavaScriptProxyProof(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void postMessage(String message) {
        //Log.e("Message", message);
        //Log.e("Repeat message", message);

        if (message.contains("proofOfCoverage")) {
            ((com.allegra.handyuvisa.Mcardhtml)activity).goToProof();
        }else{
            ((com.allegra.handyuvisa.Mcardhtml) activity).onePocketmessage = message;
            if (checkLogin()) ((com.allegra.handyuvisa.Mcardhtml) activity).openOnePocket();
        }
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
