package com.allegra.handyuvisa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.Util;
import com.allem.onepocket.utils.OPKConstants;

public class MarketPlaceActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    public static final int REQUEST_ONEPOCKET_RETURN = 10001;
    public static String URL_OPEN_PDF = "https://docs.google.com/viewer?url=";
    private static final String TAG = "MarketPlaceActivity";

    //private static final String MARKET_PLACE_URL = "http://www.allegra.market/?logo=1&onepocket=1";
    private static final String MARKET_URL_PRODUCTION = Constants.getMarketPlaceUrl();//"https://www.allegra.market/?logo=1&onepocket=1"
   /* private static final String MARKET_PLACE_URL_TEST = "http://dev.allegra.market/?onepocket=1";
    private static final String EMPTY_SHOPPING_CART = "http://dev.allegra.market/purchaseintent/index/verifytransaction?hash=";*/

    private ImageButton arrowBack, arrowF;
    private WebView webView;
    private ProgressBar progressBar;
    private ImageButton goUp;
    public String onePocketmessage;
    private String returnURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.gc();
        super.setView(R.layout.activity_market_place, this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.stopLoading();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadWebView();
    }

    @Override
    public void initViews(View root) {
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        webView = (WebView)root.findViewById(R.id.webview_marketplace);
        progressBar = (ProgressBar)root.findViewById(R.id.pb_marketplace);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }// http://stackoverflow.com/questions/31509277/webview-images-are-not-showing-with-https
        webView.setWebViewClient(new MyWebViewClient());
        webView.addJavascriptInterface(new AppJavaScriptProxyMarketPlace(this), "androidProxy");
        progressBar.setVisibility(View.VISIBLE);
        goUp = (ImageButton) root.findViewById(R.id.ib_up);
        arrowBack= (ImageButton)root.findViewById(R.id.arrow_back);
        arrowF= (ImageButton)root.findViewById(R.id.arrow_forward);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Receiving","Receiving Activity ONE");
        if (requestCode == REQUEST_ONEPOCKET_RETURN) {
            if (data != null) {
                returnURL = data.getStringExtra("RESULT");
                webView.clearHistory();
                webView.loadUrl("about:blank");
            }
        }else if (requestCode == Constants.ONE_POCKET_NEEDS_LOGIN){
            openOnePocket();

        }
    }

    public void onMenu(View view) {
        animate();
//        super.onBackPressed();
//        overridePendingTransition(R.animator.back_slide_in, R.animator.front_slide_out);
    }

    public void onUp(View view) {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    private void loadWebView() {
        progressBar.setVisibility(View.VISIBLE);

        webView.loadUrl(MARKET_URL_PRODUCTION);
        /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Config");
        query.whereEqualTo("name", "marketPlaceURL");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    webView.loadUrl(list.get(0).getString("value"));
                } else {
                    webView.loadUrl(MARKET_PLACE_URL);
                }
            }
        });*/

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (Util.hasInternetConnectivity(MarketPlaceActivity.this)){
                if (url.contains("http://")||url.contains("https://")){
                    if (!url.contains(".pdf"))  URL_OPEN_PDF = "";
                    view.loadUrl(URL_OPEN_PDF+url);
                }
            }else{
                Toast.makeText(MarketPlaceActivity.this, R.string.err_no_internet, Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            if (url.equals("about:blank")) {
                webView.loadUrl(returnURL);
            }
            loadArrows();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            progressBar.setVisibility(View.VISIBLE);
        }
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

    public void openOnePocket(){
        Intent intent = new Intent(MarketPlaceActivity.this, OnepocketPurchaseActivity.class);
        Bundle bundle = Constants.createPurchaseBundle(Constants.getUser(this), onePocketmessage, OPKConstants.TYPE_MARKETPLACE, (VisaCheckoutApp) getApplication());
        intent.putExtras(bundle);
        startActivityForResult(intent, MarketPlaceActivity.REQUEST_ONEPOCKET_RETURN);

    }

}
