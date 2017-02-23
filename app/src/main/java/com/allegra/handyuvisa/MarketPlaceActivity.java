package com.allegra.handyuvisa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.Util;
import com.allem.onepocket.utils.OPKConstants;

public class MarketPlaceActivity extends WebViewActivity implements FrontBackAnimate.InflateReadyListener {

    public static final int REQUEST_ONEPOCKET_RETURN = 10001;
    public static String URL_OPEN_PDF = "https://docs.google.com/viewer?url=";
    private static final String TAG = "MarketPlaceActivity";

    //private static final String MARKET_PLACE_URL = "http://www.allegra.market/?logo=1&onepocket=1";
    private static final String MARKET_URL_PRODUCTION = Constants.getMarketPlaceUrl();//"https://www.allegra.market/?logo=1&onepocket=1"
   /* private static final String MARKET_PLACE_URL_TEST = "http://dev.allegra.market/?onepocket=1";
    private static final String EMPTY_SHOPPING_CART = "http://dev.allegra.market/purchaseintent/index/verifytransaction?hash=";*/

    private ImageButton arrowBack, arrowF;
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
        mWebView.stopLoading();
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
        setupWebView(root);
        progressBar = (ProgressBar)root.findViewById(R.id.pb_marketplace);
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
                mWebView.clearHistory();
                mWebView.loadUrl("about:blank");
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
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    private void loadWebView() {
        progressBar.setVisibility(View.VISIBLE);

        mWebView.loadUrl(MARKET_URL_PRODUCTION);
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


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        progressBar.setVisibility(View.GONE);
        if (url.equals("about:blank")) {
            mWebView.loadUrl(returnURL);
        }
        loadArrows();
    }

    @Override
    public boolean onShouldOverrideUrlLoading(WebView view, String url) {
        //TODO: continue implementing PDF
        if (url.contains("http://") || url.contains("https://")) {
            if (!url.contains(".pdf")) URL_OPEN_PDF = "";
            view.loadUrl(URL_OPEN_PDF + url);
        }
        return true;
    }

    private void loadArrows(){

        if(mWebView.canGoBack()){
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        }else{
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if(mWebView.canGoForward()){
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        }else{
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }

    public void onGoBack(View view){
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    public void onGoForward(View view){
        if(mWebView.canGoForward()) {
            mWebView.goForward();
        }
    }

    public void openOnePocket(){
        Intent intent = new Intent(MarketPlaceActivity.this, OnepocketPurchaseActivity.class);
        Bundle bundle = Constants.createPurchaseBundle(Constants.getUser(this), onePocketmessage, OPKConstants.TYPE_MARKETPLACE, (com.allegra.handyuvisa.VisaCheckoutApp) getApplication());
        intent.putExtras(bundle);
        startActivityForResult(intent, MarketPlaceActivity.REQUEST_ONEPOCKET_RETURN);

    }

    private void setupWebView(View root) {
        mWebView = (WebView)root.findViewById(R.id.webview_marketplace);
        setupLoadingView(root);
        mWebView.setWebViewClient(new SecureBrowser(this));
        mWebView.addJavascriptInterface(new AppJavaScriptProxyMarketPlace(this), "androidProxy");
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }// http://stackoverflow.com/questions/31509277/webview-images-are-not-showing-with-https
    }

    private void setupLoadingView(View root) {
        mLoadingView = (FrameLayout) root.findViewById(R.id.loading_view);
        mLoadingBar = (ImageView) root.findViewById(R.id.pb_search_loader);
    }
}
