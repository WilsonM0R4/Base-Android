package com.allem.alleminmotion.visacheckout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.onepocket.model.OneTransaction;
import com.allem.onepocket.utils.OPKConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by RafaelRodriguez on 27/01/16.
 */
public class AppJavaScriptProxyFlights {

    private Activity activity = null;


    public AppJavaScriptProxyFlights(Activity activity) {
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

        ((FlightsSearchActivity)activity).onePocketmessage =data4;
        if(checkLogin())  ((FlightsSearchActivity) activity).openOnePocket();
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
