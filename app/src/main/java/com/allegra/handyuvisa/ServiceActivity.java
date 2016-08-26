package com.allegra.handyuvisa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.allegra.handyuvisa.models.AllemUser;
import com.allegra.handyuvisa.utils.Constants;
import com.allem.onepocket.utils.OPKConstants;

/**
 * Created by Sergio Farfan on 6/06/16.
 */
public class ServiceActivity extends  FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private WebView webServices;
    private ProgressBar progressBar;
    private ImageButton arrowBack, arrowF;
    private ImageButton menu;
    private String returnURL, userNombre, mcard, userMail;
    public String onePocketmessage;
    private String url;
    //private String url = "http://allegra.global/membresias/planes/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.activity_services, this);
        AllemUser user = Constants.getUser(this);
        userNombre = user.nombre;
        userMail = user.email;
        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        mcard = prefs.getString("idMcard", "0");
        if(mcard.equals("0")){
            mcard = "no";
        }
        else {
            mcard = "yes";
        }

        Log.d("JUAN SANDOVAL ", mcard);
        /*editor.putString("idMcard", strIdMcard);
        editor.apply();
        Log.d("strIdMcard","Es "+strIdMcard);*/
    }

    @Override
    public void initViews(View root) {


        url = "http://allegra.global/app/servicios_v2/search/?name="+userNombre+"&havemcard="+mcard+"&email="+userMail+"&v=1";
        menu = (ImageButton) root.findViewById(R.id.menu_image);
        webServices = (WebView) root.findViewById(R.id.webView3);
        webServices.getSettings().setJavaScriptEnabled(true);
        webServices.getSettings().setBuiltInZoomControls(true);
        webServices.getSettings().setGeolocationEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webServices.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webServices.setWebChromeClient(new GeoWebChromeClient());
        webServices.loadUrl(url);
        webServices.setWebViewClient(new MyBrowser(this));
        arrowBack = (ImageButton) root.findViewById(R.id.arrow_back_services);
        arrowF = (ImageButton) root.findViewById(R.id.arrow_foward_services);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBarServices);

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Log.d("juan", url);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWebView();
    }

    //************PROPER METHODS**************


    private void loadWebView() {
        //url="http://alegra.dracobots.com/Hotel/Flow/Availability?";
        webServices.addJavascriptInterface(new AppJavaScriptProxyServices(this), "androidProxy");
        webServices.loadUrl(url);
    }

    public void openOnePocket(){

        Intent intent = new Intent(ServiceActivity.this, OnepocketPurchaseActivity.class);
        Bundle bundle = Constants.createPurchaseBundle(Constants.getUser(this), onePocketmessage, OPKConstants.TYPE_MCARD, (VisaCheckoutApp) getApplication());
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.REQUEST_ONEPOCKET_RETURN);
    }

    public void onMenu(View view) {
        animate();
    }

    private class MyBrowser extends WebViewClient {

        private Context context;

        public MyBrowser(Context context) {
            this.context = context;
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if (url.equals("allegra:touchcallService")) {
                Intent i = new Intent(context, CallActivityServices.class);
                context.startActivity(i);
                return true;
            }
            return super.shouldOverrideUrlLoading(webView, url);
            }
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            if (url.equals("about:blank")) {
                webServices.loadUrl(returnURL);
            }
            loadArrows();
        }

    }

    public class GeoWebChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            // Always grant permission since the app itself requires location
            // permission and the user has therefore already granted it
            callback.invoke(origin, true, false);
        }
    }


    public void onUp(View view) {
        if (webServices.canGoBack()) {
            webServices.goBack();
        }
    }


    private void loadArrows() {

        if (webServices.canGoBack()) {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        } else {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if (webServices.canGoForward()) {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        } else {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }


    public void onGoBack(View view) {
        if (webServices.canGoBack()) {
            webServices.goBack();
        }
    }

    public void onGoForward(View view) {
        if (webServices.canGoForward()) {
            webServices.goForward();
        }
    }
}
