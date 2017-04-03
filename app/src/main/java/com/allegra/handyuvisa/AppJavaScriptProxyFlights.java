package com.allegra.handyuvisa;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.allegra.handyuvisa.utils.Constants;

/**
 * Created by RafaelRodriguez on 27/01/16.
 */
public class AppJavaScriptProxyFlights {

    private Fragment fragment = null;
    private final String  TAG  = "AppJavaScriptProxyFligh";

    public AppJavaScriptProxyFlights(Fragment fragment) {
        this.fragment = fragment;
    }

    @JavascriptInterface
    public void postMessage(String message) {
        //Log.d("Message", message);
        ((FlightsSearchActivity) fragment).onePocketmessage = message;
        if(checkLogin())  ((FlightsSearchActivity) fragment).openOnePocket();
    }

    private boolean checkLogin() {
        if(((com.allegra.handyuvisa.VisaCheckoutApp)fragment.getActivity().getApplication()).getIdSession()==null){
            Intent i =new Intent(fragment.getActivity(),LoginActivity.class);
            fragment.getActivity().startActivityForResult(i, Constants.ONE_POCKET_NEEDS_LOGIN);
            return false;
        }

        return true;
    }
}
