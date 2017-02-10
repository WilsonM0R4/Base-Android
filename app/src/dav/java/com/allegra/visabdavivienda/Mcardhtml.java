package com.allegra.handyuvisa;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.allegra.handyuvisa.models.AllemUser;
import com.allegra.handyuvisa.utils.Constants;
import com.allem.onepocket.utils.OPKConstants;

/**
 * Created by jsandoval on 10/06/16.
 */
public class Mcardhtml extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private WebView webmcard;
    private ProgressBar progressBar;
    private ImageButton arrowBack, arrowF;
    private ImageButton menu;
    private String returnURL, userEmail;
    public String onePocketmessage;
    public String goToProof;
    //private String url = "http://52.71.117.239:8080/McardMembresiaAllus/app/app/app/index_320.xhtml?email=";
    private String url_prod = Constants.getMcardhtml();
    //private String url = "http://allegra.global/membresias/planes/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_mcard_html, this);
        AllemUser user = Constants.getUser(this);
        userEmail = user.email;
    }

    @Override
    public void initViews(View root) {

        menu = (ImageButton) root.findViewById(R.id.menu_image);
        webmcard = (WebView) root.findViewById(R.id.webmcard);
        webmcard.getSettings().setJavaScriptEnabled(true);
        webmcard.loadUrl(url_prod);
        webmcard.setWebViewClient(new MyBrowser(this));
        arrowBack = (ImageButton) root.findViewById(R.id.arrow_back_mcard);
        arrowF = (ImageButton) root.findViewById(R.id.arrow_foward_mcard);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Log.d("juan", "Go back");
                onGoBack(v);

            }
        });

        arrowF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoForward(v);
            }
        });

       // Log.d("juan", url_prod);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWebView();
        /*loadWebViewOnepocket();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_ONEPOCKET_RETURN) {
            if (data != null) {
                returnURL = OPKConstants.oneTransaction.getReturnURL(); //data.getStringExtra("RESULT");
                webmcard.clearHistory();
                webmcard.loadUrl("about:blank");
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    //************PROPER METHODS**************


    private void loadWebView() {
        //url="http://alegra.dracobots.com/Hotel/Flow/Availability?";
        webmcard.addJavascriptInterface(new AppJavaScriptProxyProof(this), "androidProxy");
        webmcard.loadUrl(url_prod+userEmail);
    }

    /*private void loadWebViewOnepocket(){
        webmcard.addJavascriptInterface(new AppJavaScriptProxyMcard(this), "androidProxy");
        webmcard.loadUrl(url_prod+userEmail);
    }*/

    public void openOnePocket(){

        Intent intent = new Intent(Mcardhtml.this, OnepocketPurchaseActivity.class);
        Bundle bundle = Constants.createPurchaseBundle(Constants.getUser(this), onePocketmessage, OPKConstants.TYPE_MCARD, (com.allegra.handyuvisa.VisaCheckoutApp) getApplication());
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.REQUEST_ONEPOCKET_RETURN);
    }

    public void goToProof(){
        Context context=this;
        Intent intent = new Intent(context, ProofOfCoverageDinamicoActivity.class);
        context.startActivity(intent);

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
            if (url.equals("allegra:touchcall")) {
                Intent i = new Intent(context, CallActivity.class);
                context.startActivity(i);
                return true;
            }else if (url.equals("allegra:chat")){
                Intent i = new Intent(context,ChatActivity.class);
                context.startActivity(i);
                return true;
            }
            return super.shouldOverrideUrlLoading(webView, url);
        }

        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            if (url.equals("about:blank")) {
                webmcard.loadUrl(returnURL);
            }
            loadArrows();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

    }


    public void onUp(View view) {
        if (webmcard.canGoBack()) {
            webmcard.goBack();
        }
    }


    private void loadArrows() {

        if (webmcard.canGoBack()) {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        } else {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if (webmcard.canGoForward()) {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        } else {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }


    public void onGoBack(View view) {
        if (webmcard.canGoBack()) {
            webmcard.goBack();
        }
    }

    public void onGoForward(View view) {
        if (webmcard.canGoForward()) {
            webmcard.goForward();
        }
    }

}
