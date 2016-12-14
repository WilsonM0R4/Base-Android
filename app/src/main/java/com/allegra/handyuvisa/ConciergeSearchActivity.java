package com.allegra.handyuvisa;
/**
 * Created by jsandoval on 3/06/16.
 */
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.Util;
import com.allem.onepocket.utils.OPKConstants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ConciergeSearchActivity extends Activity{//LoadAnimate implements LoadAnimate.InflateReadyListener

    //***********************GLOBAL ATTRIBUTES********************
    private static final String TAG = "ConciergeSearchActivity";
    private String url,urlWebView;
    private WebView webView;
    private ActionBar actionBar;
    private String id_destino_ser = "", nombre_destino_ser = "";
    private ImageButton arrowBack, arrowF;
    private String returnURL;
    public String onePocketmessage;
    public String mcard;
    ImageView imgLogoAllegraLoader, progressBar, separator;
    TextView title;
    private TextView txtLoading;

    //***********************OVERRIDE METHODS********************

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_hotel_in_progress);
        setActionBar(true);
        //Get intent params
        id_destino_ser = getIntent().getStringExtra("&idCity");
        nombre_destino_ser = getIntent().getStringExtra("&labelCity");
        Log.d("ID DESTINO SER ",String.valueOf(id_destino_ser));
        Log.d("NOMBRE   DESTINO SER",String.valueOf(nombre_destino_ser));
        //Find views by Id
        webView = (WebView)findViewById(R.id.webView);//Concierge
       /* progressBar= (ImageView)findViewById(R.id.pb_search_loader);//pb_concierge
        txtLoading = (TextView)findViewById(R.id.txtLoading);
        imgLogoAllegraLoader =(ImageView)findViewById(R.id.imgProgress);*/
        //separator =(ImageView)findViewById(R.id.iv_header);
        title = (TextView)findViewById(R.id.tv_title_secc);//_con
        arrowBack = (ImageButton)findViewById(R.id.arrow_back);//_concierge
        arrowF = (ImageButton)findViewById(R.id.arrow_forward);
        //Webview settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());

        //webView.setVisibility(View.GONE);
        //Hide separator for better UI
        //separator.setVisibility(View.GONE);

        /*progressBar.setVisibility(View.VISIBLE);
        //Set animation
        progressBar.post(new Runnable() {
            @Override
            public void run() {
                ((AnimationDrawable) progressBar.getBackground()).start();
            }
        });*/
        title.setText(String.valueOf(getString(R.string.title_concierge)));
        //Validate mCard in order to send it
        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        mcard = prefs.getString("idMcard", "0");
        if(mcard.equals("0")){
            mcard = "Standard";
        }
        else {
            mcard = "mcard";
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.stopLoading();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        urlWebView=this.url;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWebView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.ACTIVITY_LOGIN) {
            if(resultCode == RESULT_OK){
                Log.d(TAG, "usuario logueado");
                ConciergeSearchActivity.this.invalidateOptionsMenu();
                webView.loadUrl("about:blank");
                loadWebView();
            }
            if (resultCode == RESULT_CANCELED) {

            }

        }else if (requestCode == Constants.ONE_POCKET_NEEDS_LOGIN){
            openOnePocket();
        } else if (requestCode == Constants.REQUEST_ONEPOCKET_RETURN) {
            if (data != null) {
                returnURL = data.getStringExtra("RESULT");
                webView.loadUrl("about:blank");
                //progressBar.setVisibility(View.VISIBLE);
            }
        }

    }

    //***********************PROPER METHODS********************

    private void setActionBar(boolean b) {

        actionBar = getActionBar();
        if(actionBar!=null){
            if (b) {
                actionBar.hide();
            } else {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    private void loadWebView() {

        webView.addJavascriptInterface(new AppJavaScriptProxyConcierge(this), "androidProxy");
        url = Constants.getSearchConciergeUrl() +
                id_destino_ser + "&nombre_destino_ser1=" + nombre_destino_ser +
                "&Payment=3"+"&Group="+mcard;
        Log.d("url con data",url);
        webView.loadUrl(url);
    }

    public void openOnePocket(){
        Intent intent = new Intent(this, OnepocketPurchaseActivity.class);
        Bundle bundle = Constants.createPurchaseBundle(Constants.getUser(this), onePocketmessage,
                OPKConstants.TYPE_HOTEL, (VisaCheckoutApp) getApplication());
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.REQUEST_ONEPOCKET_RETURN);
    }


    private void loadArrows(){

        if(webView.canGoBack()){
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        }else{
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if(webView.canGoForward()){
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        }else{
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }

    public void onGoBack(View view){
        if(webView.canGoBack()) {
            webView.goBack();
        }
    }

    public void onGoForward(View view){
        if(webView.canGoForward()) {
            webView.goForward();
        }
    }

    public void onBackButton(View view){
        finish();
    }

    //***********************INNER CLASSES********************
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.d(TAG, "url: " + url);
            if (Util.hasInternetConnectivity(ConciergeSearchActivity.this)){
                if (url.contains("http://")||url.contains("https://")){
                    view.loadUrl(url);
                    urlWebView=url;
                }else if(url.contains("detalleventaht:")&& Util.hasInternetConnectivity
                        (ConciergeSearchActivity.this)){
                    //urlWebView=HotelsActivity.this.url;
                    // TODO replace with Onepocket pay activity
                }else{
                    view.loadUrl(url);
                    urlWebView=url;
                }
            }else{
                Toast.makeText(ConciergeSearchActivity.this, R.string.err_no_internet,
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
           /* progressBar.setVisibility(View.GONE);
            imgLogoAllegraLoader.setVisibility(View.GONE);
            txtLoading.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);*/
            if (url.equals("about:blank")) {
                webView.loadUrl(returnURL);
            }
            loadArrows();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            //progressBar.setVisibility(View.VISIBLE);
        }
    }

}
