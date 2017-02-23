package com.allegra.handyuvisa;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.allegra.handyuvisa.views.NetworkIssueDialog;

/**
 * Created by gangchen on 2/13/17.
 */

//Flexible to extend other activity
public abstract class WebViewActivity extends FrontBackAnimate implements SecureBrowser.OnSecureBroswerHandler {

    //any WebView related activitiy is suggested to put it's webView here
    //Please remember to Add
    protected WebView mWebView;
    // must be setup with setupLoadingView() to avoid crash
    protected FrameLayout mLoadingView;
    protected ImageView mLoadingBar;

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        showLoadingView();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        hideLoadingView();
    }

    @Override
    public boolean onShouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    @Override
    public void onSslErrorReceivedHandlerCancelled() {
        onHome(null);
    }

    @Override
    public void onNetworkUnavailable(int error) {
        mWebView.setVisibility(View.GONE);
        showNetworkIssueDialog(error);
    }

    @Override
    public void onNetworkAvailable() {
        if (mWebView == null) {
            return;
        }
        if (mWebView.getVisibility() == View.GONE) {
            mWebView.setVisibility(View.VISIBLE);
            mWebView.invalidate();
            mWebView.loadUrl(mWebView.getUrl());
        }
        hideLoadingView();
    }

    public void showLoadingView() {
        if (mLoadingView.getVisibility() == View.GONE || mLoadingView.getVisibility() == View.INVISIBLE) {
            mLoadingView.setVisibility(View.VISIBLE);
            mLoadingView.invalidate();
        }

        mLoadingBar.setBackgroundResource(R.drawable.run_animation_2);
        ((AnimationDrawable) mLoadingBar.getBackground()).start();
    }

    public void hideLoadingView() {
        if (mLoadingView.getVisibility() == View.GONE) {
            return;
        }
        if (mLoadingView.getVisibility() == View.VISIBLE) {
            ((AnimationDrawable) mLoadingBar.getBackground()).stop();
            mLoadingView.setVisibility(View.INVISIBLE);
        }
    }

    //TODO: Should be utilizing errorcode from onReceiveError
    public void showNetworkIssueDialog(int errorCode) {
        final NetworkIssueDialog errorDialog = new NetworkIssueDialog(this, R.style.ErrorDialog_Theme);
        errorDialog.setErrorCode(R.string.idErrorCodeAB003);
        errorDialog.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
                showLoadingView();
            }
        });
        errorDialog.show();
    }
}
