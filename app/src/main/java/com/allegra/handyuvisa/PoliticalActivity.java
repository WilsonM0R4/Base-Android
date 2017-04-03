package com.allegra.handyuvisa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.allegra.handyuvisa.utils.Constants;


/**
 * Created by jsandoval on 2/06/16.
 */
public class PoliticalActivity extends WebViewActivity {

    private String url = Constants.getPolicyUrl();
    private ImageButton backButton, menuButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setView(R.layout.fragment_political_policy, this); temporally commented
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_political_policy, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setListeners();

    }

    public void initViews(View root) {
        backButton = (ImageButton) root.findViewById(R.id.imageButton8);
        menuButton = (ImageButton) root.findViewById(R.id.menu_image);
        setupWebView(root);
    }

    private void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceLayout(new LegalActivity(), true);
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).animate();
            }
        });
    }

    public void onMenu(View view) {
        ((MainActivity) getActivity()).animate();
    }

    private void setupWebView(View root) {



        mWebView = (WebView) root.findViewById(R.id.webview_legal);
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

}
