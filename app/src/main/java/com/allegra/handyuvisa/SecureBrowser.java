package com.allegra.handyuvisa;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.allegra.handyuvisa.utils.Util;
import com.allegra.handyuvisa.views.NetworkIssueDialog;
import com.allegra.handyuvisa.views.SecurityIssueDialog;

import java.util.HashMap;

import static android.net.http.SslError.SSL_UNTRUSTED;

/**
 * Created by gangchen on 2/13/17.
 */

//Defensive to handle networkConnectivity issue. Can get more active with reloading
public class SecureBrowser extends WebViewClient {
    private WebViewActivity mContext;
    private OnSecureBroswerHandler onSecureBroswerHandler;

    public interface OnSecureBroswerHandler {
        void onSslErrorReceivedHandlerCancelled();

        void onPageStarted(WebView view, String url, Bitmap favicon);

        void onPageFinished(WebView view, String url);

        void onNetworkUnavailable(int error);

        void onNetworkAvailable();

        boolean onShouldOverrideUrlLoading(WebView view, String url);
    }

    //any context should handle interface
    public SecureBrowser(WebViewActivity activity) {
        mContext = activity;
        onSecureBroswerHandler = activity;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        onSecureBroswerHandler.onPageStarted(view, url, favicon);
    }

    //Feel free to override
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        if (Util.hasInternetConnectivity(mContext)) {
//            onSecureBroswerHandler.onNetworkAvailable();
//
//        } else {
//            onSecureBroswerHandler.onNetworkUnavailable(ERROR_CONNECT);
//        }
        //if other operation should be done, return true after overriding
        // or this will do the normal loading
        if (!onSecureBroswerHandler.onShouldOverrideUrlLoading(view, url)) {
            if (url.contains("http://") || url.contains("https://")) {
                view.loadUrl(url);
            } else {
                //TODO: Handle smtp .etc
            }
        }
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
//        if (Util.hasInternetConnectivity(mContext)) {
//            onSecureBroswerHandler.onNetworkAvailable();
//        } else {
//            onSecureBroswerHandler.onNetworkUnavailable(ERROR_CONNECT);
//        }
        onSecureBroswerHandler.onPageFinished(view, url);
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
        if (error.hasError(SSL_UNTRUSTED)) {
            //TODO: change handler.proceed() to handle SSL UNTRUSTED error
            handler.proceed();
        } else {
            final SecurityIssueDialog errorDialog = new SecurityIssueDialog(mContext, R.style.ErrorDialog_Theme);
            errorDialog.setErrorCode(R.string.idErrorCodeAB0051);
            errorDialog.getButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    errorDialog.dismiss();
                    handler.cancel();
                    onSecureBroswerHandler.onSslErrorReceivedHandlerCancelled();
                }
            });
            errorDialog.show();
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        switch (errorCode) {
            case ERROR_TIMEOUT:
                onSecureBroswerHandler.onNetworkUnavailable(errorCode);
                break;
            default:
                onSecureBroswerHandler.onNetworkUnavailable(errorCode);
                break;
        }
    }
}
