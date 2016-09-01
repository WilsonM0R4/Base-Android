package com.allegra.handyuvisa;
/**
 * Created by jsandoval on 3/06/16.
 */
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private static final String TAG = "ConciergeSearchActivity";
    private String url,urlWebView;
    private WebView webView;
    private ActionBar actionBar;
    private String id_destino_ser = "", nombre_destino_ser = "";
    private ImageButton arrowBack, arrowF;
    private String returnURL;
    public String onePocketmessage;
    public String mcard;
    //ProgressBar progressBar;
    ImageView imgLogoAllegraLoader, progressBar;
    TextView title;
    private TextView txtLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);//_concierge
        //super.setView(R.layout.fragment_search_in_progress, R.drawable.concierge4, R.string.txt_lbl_searchConciergeWait, this);
        setContentView(R.layout.fragment_search_in_progress);
        id_destino_ser = getIntent().getStringExtra("&idCity");
        nombre_destino_ser = getIntent().getStringExtra("&labelCity");
        Log.d("ID DESTINO SER ",String.valueOf(id_destino_ser));
        Log.d("NOMBRE DESTINO SER",String.valueOf(nombre_destino_ser));

        webView = (WebView)findViewById(R.id.webView);//Concierge
        progressBar= (ImageView)findViewById(R.id.pb_search_loader);//pb_concierge
        txtLoading = (TextView)findViewById(R.id.txtLoading);

        progressBar.post(new Runnable() {
            @Override
            public void run() {
                ((AnimationDrawable) progressBar.getBackground()).start();
            }
        });
        imgLogoAllegraLoader =(ImageView)findViewById(R.id.imgProgress);
        title = (TextView)findViewById(R.id.tv_title_secc);//_con
        arrowBack = (ImageButton)findViewById(R.id.arrow_back);//_concierge
        arrowF = (ImageButton)findViewById(R.id.arrow_forward);

        setActionBar(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());

        title.setText(String.valueOf(getString(R.string.title_concierge)));
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

   /* @Override
    public void initViews(View root) {

        webView = (WebView)root.findViewById(R.id.webView);//Concierge
        progressBar= (ImageView) root.findViewById(R.id.pb_search_loader);//pb_concierge
        imgLogoAllegraLoader =(ImageView) root.findViewById(R.id.imgProgress);
        title = (TextView) root.findViewById(R.id.tv_title_secc);//_con
        arrowBack= (ImageButton)root.findViewById(R.id.arrow_back);//_concierge

        setActionBar(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());

        title.setText(String.valueOf(getString(R.string.title_concierge)));
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        loadWebView();
    }

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

   /* @Override
    public void onCancelLoading() {

        finish();
    }*/

    private void loadWebView() {
        //OLD: http://viatorallegra.vuelos.ninja/Actividad/ResultadosGet?id_destino_ser=
        //URL_CONCIERGE_PROD :”http://actividades.allegra.travel/Actividad/ResultadosGet?id_destino_ser="
        url = "http://actividades.allegra.travel/Actividad/ResultadosGet?id_destino_ser=" +
                id_destino_ser + "&nombre_destino_ser1=" + nombre_destino_ser +
                "&Payment=3"+"&Group="+mcard;

        webView.addJavascriptInterface(new AppJavaScriptProxyConcierge(this), "androidProxy");

        String encodedUrl = null;

        try {
            encodedUrl = URLEncoder.encode(nombre_destino_ser, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // Can be safely ignored because UTF-8 is always supported
        }
        url = "http://actividades.allegra.travel/Actividad/ResultadosGet?id_destino_ser=" +
                id_destino_ser + "&nombre_destino_ser1=" + encodedUrl +
                "&Payment=3"+"&Group="+mcard;
        Log.d("url con data",url);
        //webView.postUrl(url, EncodingUtils.getBytes(postData, "BASE64"));
        webView.loadUrl(url);

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.d(TAG, "url: " + url);

            if (Util.hasInternetConnectivity(ConciergeSearchActivity.this)){
                if (url.contains("http://")||url.contains("https://")){
                    view.loadUrl(url);
                    urlWebView=url;
                }else if(url.contains("detalleventaht:")&& Util.hasInternetConnectivity(ConciergeSearchActivity.this)){
//                    urlWebView=HotelsActivity.this.url;
                    // TODO replace with Onepocket pay activity
                }else{
                    view.loadUrl(url);
                    urlWebView=url;
                }
            }else{
                Toast.makeText(ConciergeSearchActivity.this, R.string.err_no_internet, Toast.LENGTH_SHORT).show();
            }
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            imgLogoAllegraLoader.setVisibility(View.GONE);
            txtLoading.setVisibility(View.GONE);
            if (url.equals("about:blank")) {
                webView.loadUrl(returnURL);
            }
            loadArrows();
            //animate();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            progressBar.setVisibility(View.VISIBLE);
            //showProgress(true);
        }
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
                webView.clearHistory();
                webView.loadUrl("about:blank");
            }
        }

    }

    public void openOnePocket(){
        Intent intent = new Intent(this, OnepocketPurchaseActivity.class);
        Bundle bundle = Constants.createPurchaseBundle(Constants.getUser(this), onePocketmessage, OPKConstants.TYPE_HOTEL, (VisaCheckoutApp) getApplication());
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        urlWebView=this.url;
    }

    public void onBackButton(View view){
        finish();
    }

}
