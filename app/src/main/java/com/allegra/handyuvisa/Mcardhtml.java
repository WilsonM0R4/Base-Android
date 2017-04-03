package com.allegra.handyuvisa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.allegra.handyuvisa.models.AllemUser;
import com.allegra.handyuvisa.utils.Constants;
import com.allem.onepocket.utils.OPKConstants;

/**
 * Created by jsandoval on 10/06/16.
 */
public class Mcardhtml extends WebViewActivity {

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
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setView(R.layout.fragment_mcard_html, this);
        AllemUser user = Constants.getUser(getActivity()); //getActivity() is temporal
        userEmail = user.email;
        ((MainActivity) getActivity()).statusBarVisibility(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_mcard_html, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }


    public void initViews(View root) {

        menu = (ImageButton) root.findViewById(R.id.menu_image);
        setupWebView(root);
        arrowBack = (ImageButton) root.findViewById(R.id.arrow_back_mcard);
        arrowF = (ImageButton) root.findViewById(R.id.arrow_foward_mcard);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).animate();
            }
        });

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.d("juan", "Go back");
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
    public void onResume() {
        super.onResume();
        loadWebView();
        /*loadWebViewOnepocket();*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_ONEPOCKET_RETURN) {
            if (data != null) {
                returnURL = OPKConstants.oneTransaction.getReturnURL(); //data.getStringExtra("RESULT");
                mWebView.clearHistory();
                mWebView.loadUrl("about:blank");
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    //************PROPER METHODS**************


    private void loadWebView() {
        //url="http://alegra.dracobots.com/Hotel/Flow/Availability?";
        mWebView.addJavascriptInterface(new AppJavaScriptProxyProof(getActivity()),
                "androidProxy"); // getActivity() is temporal
        mWebView.loadUrl(url_prod+userEmail);
    }


    public void openOnePocket(){

        /*** temporally commented ***/

        /*Intent intent = new Intent(Mcardhtml.this, OnepocketPurchaseActivity.class);
        Bundle bundle = Constants.createPurchaseBundle(Constants.getUser(this), onePocketmessage, OPKConstants.TYPE_MCARD, (com.allegra.handyuvisa.VisaCheckoutApp) getApplication());
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.REQUEST_ONEPOCKET_RETURN);*/
    }

    public void goToProof(){
        Context context=this.getActivity().getApplicationContext(); //this.getActivity().getApplicationContext() is temporal
        Intent intent = new Intent(context, ProofOfCoverageDinamicoActivity.class);
        context.startActivity(intent);

    }

    public void onMenu(View view) {
        ((MainActivity) getActivity()).animate();
    }

    public void setupWebView(View root) {
        mWebView = (WebView) root.findViewById(R.id.webmcard);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowContentAccess(false);
        mWebView.getSettings().setAllowFileAccess(false);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(false);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(false);
        mWebView.setWebViewClient(new SecureBrowser(this));
        setupLoadingView(root);
    }

    private void setupLoadingView(View root) {
        mLoadingView = (FrameLayout) root.findViewById(R.id.loading_view);
        mLoadingBar = (ImageView) root.findViewById(R.id.pb_search_loader);
    }

    public boolean onShouldOverrideUrlLoading(WebView webView, String url) {

        /** temporally commented **/
        /*if (url.equals("allegra:touchcall")) {
            Intent i = new Intent(this, CallActivity.class);
            this.startActivity(i);
            return true;
        }else if (url.equals("allegra:chat")){
            Intent i = new Intent(this,ChatActivity.class);
            this.startActivity(i);
            return true;
        }*/
        return super.onShouldOverrideUrlLoading(webView, url);
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

    public void onUp(View view) {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }


    private void loadArrows() {

        if (mWebView.canGoBack()) {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        } else {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if (mWebView.canGoForward()) {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        } else {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }


    public void onGoBack(View view) {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    public void onGoForward(View view) {
        if (mWebView.canGoForward()) {
            mWebView.goForward();
        }
    }

}
