package com.allegra.handyuvisa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.allegra.handyuvisa.utils.Constants;
import com.allem.onepocket.utils.OPKConstants;

public class MarketPlaceActivity extends WebViewActivity {

    public static final int REQUEST_ONEPOCKET_RETURN = 10001;
    public static String URL_OPEN_PDF = "https://docs.google.com/viewer?url=";
    private static final String TAG = "MarketPlaceActivity";

    //private static final String MARKET_PLACE_URL = "http://www.allegra.market/?logo=1&onepocket=1";
    private static final String MARKET_URL_PRODUCTION = Constants.getMarketPlaceUrl();//"https://www.allegra.market/?logo=1&onepocket=1"
   /* private static final String MARKET_PLACE_URL_TEST = "http://dev.allegra.market/?onepocket=1";
    private static final String EMPTY_SHOPPING_CART = "http://dev.allegra.market/purchaseintent/index/verifytransaction?hash=";*/

    private ImageButton arrowBack, arrowF, menuButton;
    private ProgressBar progressBar;
    private ImageButton goUp;
    public String onePocketmessage;
    private String returnURL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //System.gc();
        //super.setView(R.layout.activity_market_place, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIsntanceState){
        super.onCreateView(inflater, container, savedIsntanceState);
        return inflater.inflate(R.layout.activity_market_place, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).statusBarVisibility(false);
        initViews(view);
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.stopLoading();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWebView();
    }

    public void initViews(View root) {
        android.app.ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setupWebView(root);
        progressBar = (ProgressBar)root.findViewById(R.id.pb_marketplace);
        progressBar.setVisibility(View.VISIBLE);
        goUp = (ImageButton) root.findViewById(R.id.ib_up);
        arrowBack= (ImageButton)root.findViewById(R.id.arrow_back);
        arrowF= (ImageButton)root.findViewById(R.id.arrow_forward);
        menuButton = (ImageButton) root.findViewById(R.id.menu_image);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).animate();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        ((MainActivity) getActivity()).animate();
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
        //Intent intent = new Intent(MarketPlaceActivity.this, OnepocketPurchaseActivity.class);
        Bundle bundle = Constants.createPurchaseBundle(Constants.getUser(getActivity()),
                onePocketmessage,
                OPKConstants.TYPE_MARKETPLACE,
                (com.allegra.handyuvisa.VisaCheckoutApp) getActivity().getApplication());

        OnepocketPurchaseActivity fragmentOPKPurchase = new OnepocketPurchaseActivity();
        fragmentOPKPurchase.setArguments(bundle);

        ((MainActivity) getActivity()).replaceLayout(fragmentOPKPurchase, false);
        //intent.putExtras(bundle);
        //startActivityForResult(intent, MarketPlaceActivity.REQUEST_ONEPOCKET_RETURN);*/

    }

    private void setupWebView(View root) {
        mWebView = (WebView)root.findViewById(R.id.webview_marketplace);
        setupLoadingView(root);
        mWebView.setWebViewClient(new SecureBrowser(this));
        //mWebView.addJavascriptInterface(new AppJavaScriptProxy(this), "androidProxy"); //getActivity() is temporal
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }// http://stackoverflow.com/questions/31509277/webview-images-are-not-showing-with-https
        webSettings.setJavaScriptEnabled(true);
    }

    private void setupLoadingView(View root) {
        mLoadingView = (FrameLayout) root.findViewById(R.id.loading_view);
        mLoadingBar = (ImageView) root.findViewById(R.id.pb_search_loader);
    }
}
