package com.allegra.handyuvisa;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class AllInOneBenefitsActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.activity_all_in_one_benefits, this);
    }

    private ActionBar actionBar;
    private void setActionbar() {
        actionBar = getActionBar();
        if(actionBar!=null){
            actionBar.setIcon(R.drawable.ab_icon_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                    | ActionBar.DISPLAY_SHOW_HOME);
        }
    }
    @Override
    public void initViews(View root) {
        setActionbar();
        initWebView(root);
        initCallButton(root);
    }

    private void initCallButton(View root) {
        Button b = (Button) root.findViewById(R.id.activation_call_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllInOneBenefitsActivity.this, CallActivity.class);
                AllInOneBenefitsActivity.this.startActivity(intent);
            }
        });
    }

    private void initWebView(View root) {
        WebView webView = (WebView) root.findViewById(R.id.benefitsWebView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://www.clicktravelprotection.com/InternationalAssist/faces/beneficios/index-usuarios.html");
    }

    public void onMenu(View view) {
        animate();
    }

    public void back(View view) {
        NavUtils.navigateUpFromSameTask(this);
    }

}
