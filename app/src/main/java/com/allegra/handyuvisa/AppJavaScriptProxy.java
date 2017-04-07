package com.allegra.handyuvisa;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.allegra.handyuvisa.utils.Constants;

/**
 * Created by RafaelRodriguez on 27/01/16.
 */
public class AppJavaScriptProxy {

    private WebFragment fragment = null;

    public AppJavaScriptProxy(WebFragment fragment) {
        this.fragment = fragment;
    }

    @JavascriptInterface
    public void postMessage(String message) {
        //Log.d("Message", message);
        fragment.onePocketMessage = message;
        /*if (checkLogin())*/  fragment.openOnePocket();
    }

    private boolean checkLogin() {
        if(((com.allegra.handyuvisa.VisaCheckoutApp)fragment.getActivity().getApplication()).getIdSession()==null){

            Bundle bundle = new Bundle();
            bundle.putInt(LoginActivity.LOGIN_REQUEST_TYPE, Constants.ONE_POCKET_NEEDS_LOGIN);

            LoginActivity loginActivity = new LoginActivity();
            loginActivity.setArguments(bundle);

            ((FragmentMain) fragment.getParentFragment()).replaceLayout(loginActivity, false);

            //Intent i = new Intent(fragment.getActivity(),LoginActivity.class);
            //fragment.getActivity().startActivityForResult(i, Constants.ONE_POCKET_NEEDS_LOGIN);
            return false;
        }

        return true;
    }
}
