package com.allegra.handyuvisa;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * Created by jsandoval on 12/12/16.
 */

public class CheckMyTrip extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private WebView webcheck;
    private ProgressBar progressBar;
    private ImageButton arrowBack, arrowF;
    private ImageButton menu;
    private String returnURL;
    private String url = "http://iataiapps.com/visabusiness/checkmytrip/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_checkmytrip,this);
    }
    @Override
    public void initViews(View root) {

        menu = (ImageButton) root.findViewById(R.id.menu_image);
        webcheck = (WebView) root.findViewById(R.id.webcheck);
        webcheck.getSettings().setJavaScriptEnabled(true);
        webcheck.loadUrl(url);
        webcheck.setWebViewClient(new CheckMyTrip.MyBrowser(this));
        arrowBack = (ImageButton) root.findViewById(R.id.arrow_back_check);
        arrowF = (ImageButton) root.findViewById(R.id.arrow_foward_check);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar_check);

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

       // Log.d("juan", url);
    }

    public void onMenu(View view) {
        animate();
    }

    private class MyBrowser extends WebViewClient {

        private Context context;

        public MyBrowser(Context context) {
            this.context = context;
        }

        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            if (url.equals("about:blank")) {
                webcheck.loadUrl(returnURL);
            }
            loadArrows();
        }

    }


    public void onUp(View view) {
        if (webcheck.canGoBack()) {
            webcheck.goBack();
        }
    }


    private void loadArrows() {

        if (webcheck.canGoBack()) {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        } else {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if (webcheck.canGoForward()) {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        } else {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }


    public void onGoBack(View view) {
        if (webcheck.canGoBack()) {
            webcheck.goBack();
        }
    }

    public void onGoForward(View view) {
        if (webcheck.canGoForward()) {
            webcheck.goForward();
        }
    }

}
