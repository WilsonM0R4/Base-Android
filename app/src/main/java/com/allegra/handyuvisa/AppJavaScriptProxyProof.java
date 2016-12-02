package com.allegra.handyuvisa;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;

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
        Log.e("Message", message);
        Log.e("Repeat message", message);

        if (message.contains("proofOfCoverage")) {

            ((Mcardhtml)activity).goToProof();

        }
    }

}
