package com.allegra.handyuvisa;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.Util;
/*import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;*/

import java.util.List;

public class EntertainmentActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private static final String URLSAMPLE = "http://allemevent.com/demo/visacheckout/restaurantesgenericos/home/";
    private final String TAG = "EntertainmentActivity";
    private ActionBar actionBar;
    private WebView webView;
    private ProgressBar progressBar;
    private String url,urlWebView;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Entertainment Activity Creating");
        ctx = this;
        super.setView(R.layout.fragment_entertainment, this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause(){
        super.onPause();
        webView.stopLoading();
        ((VisaCheckoutApp)this.getApplication()).setUrlHotel(urlWebView);
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadWebView();
    }

    public void onMenu(View view) {
        animate();
    }

    /*private void getParseURL() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Config");
        query.whereEqualTo("name", "entertaimentURL");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e==null){
                    url=list.get(0).getString("value");
                    webView.loadUrl(url);
                }else{
                    //Toast.makeText(ctx,"Revise su conexión a Internet",Toast.LENGTH_LONG).show();
                    webView.loadUrl(URLSAMPLE);
                }
            }
        });
    }*/

    @Override
    public void initViews(View root) {
        Log.d("ON INIT VIEW ","ON INIT VIEW");
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);  //permits the action bar overlay over the screen
        setActionbar();
        webView = (WebView)root.findViewById(R.id.webView);
        progressBar = (ProgressBar)root.findViewById(R.id.pb_entertainment);
        //btn_buy = (Button) findViewById(R.id.btn_buy);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        progressBar.setVisibility(View.VISIBLE);

        setListeners();
    }

    private void setListeners() {
        /*btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(ctx,FALogin.class);
                FAHotel.this.startActivityForResult(i, Constants.ACTIVITY_LOGIN);
            }
        });*/
    }

    private void checkLogin() {
        webView.loadUrl(URLSAMPLE);
        /*if((((AsobancariaApplication)this.getApplication()).getIdSession()==null)){
            webView.loadUrl(URLSAMPLE);
        }else{
            //btn_buy.setVisibility(View.GONE);
            loadWebView();
        }*/
    }

    private void loadWebView() {
        progressBar.setVisibility(View.VISIBLE);
//        if (((VisaCheckoutApp)this.getApplication()).getUrlHotel()!=null){
//            webView.loadUrl(((VisaCheckoutApp)this.getApplication()).getUrlHotel());
//        }else{
            //getParseURL();
//        }
    }

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_allemmarket, menu);
//        /*if((((AsobancariaApplication)this.getApplication()).getIdSession()!=null)){
//            menu.findItem(R.id.menu_regist).setVisible(false);
//        }*/
//        menu.findItem(R.id.menu_regist).setVisible(false);
//        return true;
//    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        /*if((((AsobancariaApplication)this.getApplication()).getIdSession()!=null)){
//            menu.findItem(R.id.menu_regist).setVisible(false);
//        }*/
//        menu.findItem(R.id.menu_regist).setVisible(false);
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                urlWebView=this.url;
                this.finish();
                return true;
//            case R.id.menu_regist:
//                Intent i =new Intent(ctx,FALogin.class);
//                this.startActivityForResult(i, Constants.ACTIVITY_LOGIN);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG,"url: "+url);
            if (Util.hasInternetConnectivity(ctx)){
                if (url.contains("http://")||url.contains("https://")){
                    view.loadUrl(url);
                    urlWebView=url;
                }else if(url.contains("detalleventaht:")&& Util.hasInternetConnectivity(ctx)){
//                    urlWebView=HotelsActivity.this.url;
                    // TODO replace with Onepocket pay activity

                }else{
                    view.loadUrl(url);
                    urlWebView=url;
                }
            }else{
                Toast.makeText(ctx, R.string.err_no_internet, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.ACTIVITY_LOGIN) {
            if(resultCode == RESULT_OK){
                Log.d(TAG,"usuario logueado");
                EntertainmentActivity.this.invalidateOptionsMenu();
                webView.loadUrl("about:blank");
                loadWebView();
            }
            if (resultCode == RESULT_CANCELED) {

            }
//        }else if(requestCode == Constants.ACTIVITY_BUYOUTS_DETAIL){
//            if(resultCode == Constants.RESULT_TO_MAIN){
//                Intent i = new Intent(HotelsActivity.this, MainActivity.class);
//                // set the new task and clear flags
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//            }
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        urlWebView=this.url;
//    }
}
