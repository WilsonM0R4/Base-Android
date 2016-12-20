package com.allegra.handyuvisa;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.allegra.handyuvisa.utils.CustomizedTextView;
import com.allegra.handyuvisa.utils.Util;

import java.lang.ref.WeakReference;

/**
 * Created by jsandoval on 25/07/16.
 */
public class MyBenefits extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    public final String TAG = "MyBenefits";
    private WebView webBenefits;
    private String url = "http://allegra.global/visa/app/es/beneficios/index.html";
    private String returnURL;
    private ImageButton arrowBack, arrowF, back;
    private LinearLayout progressBarHolder;
    private ImageView pb_search_loader, imgProgress;
    private CustomizedTextView tvLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_my_benefits,this);
    }

    @Override
    public void initViews(View root) {

        webBenefits = (WebView)root.findViewById(R.id.webBenefits);
        webBenefits.getSettings().setJavaScriptEnabled(true);
        webBenefits.setWebViewClient(new MyBrowser(this));
        webBenefits.loadUrl(url);
        progressBarHolder = (LinearLayout) root.findViewById(R.id.progressBarHolder);
        progressBarHolder.setVisibility(View.VISIBLE);
        pb_search_loader = (ImageView)root.findViewById(R.id.pb_search_loader);
        pb_search_loader.setBackgroundResource(R.drawable.run_animation_2);
        imgProgress = (ImageView)root.findViewById(R.id.imgProgress);
        tvLoading = (CustomizedTextView)root.findViewById(R.id.txtLoading);

        arrowBack = (ImageButton) root.findViewById(R.id.arrow_back_benefits);
        arrowF = (ImageButton) root.findViewById(R.id.arrow_foward_benefits);
        back = (ImageButton) root.findViewById(R.id.back_image);
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
                onUp(v);
            }
        });

    }

    private static class MyBrowser extends WebViewClient {
        private final WeakReference<MyBenefits> mActivity;

        public MyBrowser(MyBenefits activity) {
            mActivity = new WeakReference<MyBenefits>(activity);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url){
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (mActivity != null) {
                mActivity.get().onWebViewFinished(url);
            }
        }
    }

    public void onWebViewFinished(String url) {
        if (Util.hasInternetConnectivity(this)) {
            webBenefits.setVisibility(View.VISIBLE);
            progressBarHolder.setVisibility(View.GONE);
            pb_search_loader.setVisibility(View.GONE);
            imgProgress.setVisibility(View.GONE);
            tvLoading.setVisibility(View.GONE);
            ((AnimationDrawable) pb_search_loader.getBackground()).stop();
            if (url.equals("about:blank")) {
                webBenefits.loadUrl(returnURL);
            }
            loadArrows();
        } else {
            pb_search_loader.setVisibility(View.VISIBLE);
            imgProgress.setVisibility(View.VISIBLE);
            tvLoading.setVisibility(View.VISIBLE);
            webBenefits.setVisibility(View.GONE);
            ((AnimationDrawable) pb_search_loader.getBackground()).start();
            Toast.makeText(this, getString(R.string.err_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    public void onMenu(View view) {
        animate();
    }

    public void onUp(View view) {
        super.onBackPressed();
    }


    private void loadArrows() {

        if (webBenefits.canGoBack()) {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        } else {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if (webBenefits.canGoForward()) {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        } else {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }


    public void onGoBack(View view) {
        if (webBenefits.canGoBack()) {
            webBenefits.goBack();
        }
    }

    public void onGoForward(View view) {
        if (webBenefits.canGoForward()) {
            webBenefits.goForward();
        }
    }



}
