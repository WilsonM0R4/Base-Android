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
        Log.d("OPK", message);
        String data = message.replaceAll("\\\\n", "");
        String data2 = data.replaceAll("\\\\", "");
        String data3 = data2.replace("\"{", "{");
        String data4 = data3.replace("}\"", "}");
        Log.d("OPK", "Filtered: " + data4);

        ((ConciergeSearchActivity)activity).onePocketmessage =data4;
        if(checkLogin())  ((ConciergeSearchActivity) activity).openOnePocket();
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
