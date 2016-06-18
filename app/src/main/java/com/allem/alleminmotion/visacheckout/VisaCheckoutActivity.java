package com.allem.alleminmotion.visacheckout;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.allem.alleminmotion.visacheckout.utils.Util;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class VisaCheckoutActivity extends Activity {

    private static final String TAG = "VisaCheckoutActivity";
    private static final String SIGNUP_URL = "https://secure.checkout.visa.com/createAccount";
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa_checkout);

        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        webView = (WebView)findViewById(R.id.webview_visacheckout);
        progressBar = (ProgressBar)findViewById(R.id.pb_visacheckout);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        progressBar.setVisibility(View.VISIBLE);

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

    private void loadWebView() {
        progressBar.setVisibility(View.VISIBLE);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Config");
        query.whereEqualTo("name", "visaCheckoutSignupURL");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    webView.loadUrl(list.get(0).getString("value"));
                } else {
                    webView.loadUrl(SIGNUP_URL);
                }
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "url: " + url);
            if (Util.hasInternetConnectivity(VisaCheckoutActivity.this)){
                if (url.contains("http://")||url.contains("https://")){
                    view.loadUrl(url);
//                    urlWebView=url;
//                }else if(url.contains("detalleventaht:")&& Util.hasInternetConnectivity(ctx)){
//                    urlWebView=HotelsActivity.this.url;
//                    Intent i =new Intent(ctx,FAPayDetalle.class);
//                    i.putExtra(Constants.ACTIVITY_KEY,Constants.ACTIVITY_ALLEM_RESTO);
//                    String [] values = url.split(":");
//                    i.putExtra(Constants.STORE_KEY_STORE_NAME,values[1]);
//                    i.putExtra(Constants.KEY_COMPRAS_REF,values[2]);
//                    i.putExtra(Constants.STORE_KEY_VALUE,values[3]);
//                    i.putExtra(Constants.KEY_COMPRAS_IVA,values[4]);
//                    i.putExtra(Constants.KEY_COMPRAS_MONEDA,values[5]);
//                    i.putExtra(Constants.KEY_ID_ACCOUNT,values[6]);
//                    i.putExtra(Constants.KEY_COMERCIO_ID,values[7]);
//                    i.putExtra(Constants.STORE_KEY_NUM_ORDEN,values[6]);
//                    HotelsActivity.this.startActivityForResult(i, Constants.ACTIVITY_BUYOUTS_DETAIL);
                }else{
                    view.loadUrl(url);

                }
            }else{
                Toast.makeText(VisaCheckoutActivity.this, R.string.err_no_internet, Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

}
