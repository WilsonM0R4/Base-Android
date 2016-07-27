package com.allem.alleminmotion.visacheckout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.allem.alleminmotion.visacheckout.models.AllemUser;
import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.alleminmotion.visacheckout.utils.Util;
import com.allem.onepocket.model.OneTransaction;
import com.allem.onepocket.utils.OPKConstants;

public class MarketPlaceActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    public static final int REQUEST_ONEPOCKET_RETURN = 10001;

    private static final String TAG = "MarketPlaceActivity";

    private static final String MARKET_PLACE_URL = "http://www.allegra.market/?logo=1&onepocket=1";
    private static final String MARKET_URL_PRODUCTION = "https://www.allegra.market/?logo=1&onepocket=1";
    private static final String MARKET_PLACE_URL_TEST = "http://dev.allegra.market/?onepocket=1";
    private static final String EMPTY_SHOPPING_CART = "http://dev.allegra.market/purchaseintent/index/verifytransaction?hash=";

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
                    view.loadUrl(url);

                }else{
                    view.loadUrl(url);

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
        Bundle bundle = new Bundle();
        AllemUser user = Constants.getUser(this);
        VisaCheckoutApp app = (VisaCheckoutApp) getApplication();
        OneTransaction transaction = new OneTransaction();
        transaction.add("jsonPayment", onePocketmessage);
        transaction.add("type", OPKConstants.TYPE_MARKETPLACE);
        transaction.add("sessionId", app.getIdSession());
        transaction.add("first", user.nombre);
        transaction.add("last", user.apellido);
        transaction.add("userName", user.email);
        transaction.add("rawPassword", app.getRawPassword());
        transaction.add("idCuenta", Integer.toString(app.getIdCuenta()));

        bundle.putParcelable(OPKConstants.EXTRA_PAYMENT, transaction);
        intent.putExtras(bundle);
        startActivityForResult(intent, MarketPlaceActivity.REQUEST_ONEPOCKET_RETURN);

    }

}
