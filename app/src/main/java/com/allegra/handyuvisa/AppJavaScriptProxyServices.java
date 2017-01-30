package com.allegra.handyuvisa;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.allegra.handyuvisa.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jsandoval on 26/07/16.
 */
public class AppJavaScriptProxyServices {

    private Activity activity = null;

    public AppJavaScriptProxyServices(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void postMessage(String message) {
        Log.e("Message",message);
        Log.e("Repeat message", message);

        if (message.contains("myCurrentLocation")) {
            //Obtengo latitud y longitud


            //Se los envío a la función JavaScript MyGeoLocation



        }

        ((ServiceActivity)activity).onePocketmessage = message;
        //if(checkLogin()) ((ServiceActivity) activity).openOnePocket();
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
