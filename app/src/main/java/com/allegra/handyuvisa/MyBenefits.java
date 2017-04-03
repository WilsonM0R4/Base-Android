package com.allegra.handyuvisa;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.allegra.handyuvisa.utils.Constants;

/**
 * Created by jsandoval on 25/07/16.
 */
public class MyBenefits extends WebViewActivity  {

    private String url = Constants.getMyBenefitsUrl();

    private ImageButton arrowBack, arrowF, back;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setView(R.layout.fragment_my_benefits,this); temporally commented
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_my_benefits, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedIsntanceState){
        super.onViewCreated(view, savedIsntanceState);

        initViews(view);
    }

    public void initViews(View root) {
        setupWebView(root);


        arrowBack = (ImageButton) root.findViewById(R.id.arrow_back_benefits);
        arrowF = (ImageButton) root.findViewById(R.id.arrow_foward_benefits);
        back = (ImageButton) root.findViewById(R.id.back_navigation);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar_benefits);

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoBack(v);
            }
        });

        arrowF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoForward(v);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MyBenefits", "back arrow clicked");
                onUp(v);
            }
        });

    }

    private void setupWebView(View root) {
        mWebView = (WebView) root.findViewById(R.id.webBenefits);
        setupLoadingView(root);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowContentAccess(false);
        mWebView.getSettings().setAllowFileAccess(false);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(false);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(false);
        mWebView.setWebViewClient(new SecureBrowser(this));
        mWebView.loadUrl(url);
    }

    protected void setupLoadingView(View root) {
        mLoadingBar = (ImageView) root.findViewById(R.id.pb_search_loader);
        mLoadingView = (FrameLayout) root.findViewById(R.id.loading_view);
    }

    public void onMenu(View view) {
        ((MainActivity) getActivity()).animate();
    }

    public void onUp(View view) {

        ((MainActivity) getActivity()).replaceLayout(new MyAccountMenuActivity(), true);
        //super.onBackPressed(); temporally commented
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        progressBar.setVisibility(View.GONE);
        loadArrows();
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
