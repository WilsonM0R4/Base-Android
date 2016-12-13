package com.allegra.handyuvisa;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.Util;
import com.allem.onepocket.utils.OPKConstants;

/**      Activity for search flights in WebSite and display them inside a webView.        **/
public class FlightsSearchActivity extends Activity {//LoadAnimate  implements LoadAnimate.InflateReadyListener

    //******************GLOBAL ATTRIBUTES**********************
    private static final String TAG = "FlightsSearchActivity";
    private ActionBar actionBar;
    private WebView webView;
    private String url,urlWebView;
    private ImageButton arrowBack, arrowF;
    private int paramTrip = 0; // 0 - round trip; 1 - one way
    private int paramCabin = 0; // 0 - economy; 1 - business; 2 - first
    private int paramAdult = 0;
    private int paramChildren = 0;
    private int paramInfant = 0;
    private String paramDepartDate = "";
    private String paramArriveDate = "";
    private String paramDepartFrom;
    private String paramDepartFromName;
    private String paramArriveAt;
    private String paramArriveAtName;
    private String flightType = "";
    public String onePocketmessage;
    private String returnURL;
    private ImageView imgProgress, progressBar;
    private TextView txtLoading;

    //******************OVERRIDE METHODS**********************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_in_progress);
        setActionbar(true);
        webView = (WebView)findViewById(R.id.webView);
        txtLoading = (TextView)findViewById(R.id.txtLoading);
        //btn_buy = (Button) findViewById(R.id.btn_buy);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        TextView title = (TextView) findViewById(R.id.tv_title_secc);
        title.setText(R.string.title_flight_results);
        arrowBack = (ImageButton)findViewById(R.id.arrow_back);
        arrowF = (ImageButton)findViewById(R.id.arrow_forward);
        imgProgress = (ImageView) findViewById(R.id.imgProgress);
        progressBar = (ImageView) findViewById(R.id.pb_search_loader);
        webView.setVisibility(View.GONE);
        //Animation
        progressBar.setVisibility(View.VISIBLE);
        progressBar.post(new Runnable() {
            @Override
            public void run() {
                ((AnimationDrawable) progressBar.getBackground()).start();
            }
        });

        paramTrip = getIntent().getIntExtra("trip", 0);
        if (paramTrip == 1){//One Way
            flightType = "OW";
        } else {
            flightType = "RT";
        }

        paramCabin = getIntent().getIntExtra("cabin", 0);// 0 - economy; 1 - business; 2 - first
        paramAdult = getIntent().getIntExtra("adult", 0);
        paramChildren = getIntent().getIntExtra("children", 0);
        paramInfant = getIntent().getIntExtra("infant", 0);
        paramDepartDate = getIntent().getStringExtra("departOn");
        paramArriveDate = getIntent().getStringExtra("arriveOn");
        paramDepartFrom = getIntent().getStringExtra("departFrom");//IATA CODE DEPARTURE
        paramDepartFromName = getIntent().getStringExtra("departFromName");//LARGE NAME DEPARTURE
        paramArriveAt = getIntent().getStringExtra("arriveAt");//IATA CODE ARRIVAL
        paramArriveAtName = getIntent().getStringExtra("arriveAtName");//LARGE NAME ARRIVAL

        Log.d("paramCabin",String.valueOf(paramCabin));
        Log.d("paramAdult",String.valueOf(paramAdult));
        Log.d("paramChildren",String.valueOf(paramChildren));
        Log.d("paramDepartDate",String.valueOf(paramDepartDate));
        Log.d("paramArriveDate",String.valueOf(paramArriveDate));
        Log.d("paramDepartFrom",String.valueOf(paramDepartFrom));
        Log.d("paramDepartFromName",String.valueOf(paramDepartFromName));
        Log.d("paramArriveAt",String.valueOf(paramArriveAt));
        Log.d("paramArriveAtName",String.valueOf(paramArriveAtName));
        Log.d("paramInfant",String.valueOf(paramInfant));
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.stopLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWebView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.ACTIVITY_LOGIN) {
            if(resultCode == RESULT_OK){
                Log.d(TAG, "usuario logueado");
                FlightsSearchActivity.this.invalidateOptionsMenu();
                webView.loadUrl("about:blank");
                loadWebView();
            }
            if (resultCode == RESULT_CANCELED) {

            }

        }else if (requestCode == Constants.ONE_POCKET_NEEDS_LOGIN){
            openOnePocket();
        } else if (requestCode == Constants.REQUEST_ONEPOCKET_RETURN) {
            if (data != null) {
                returnURL = data.getStringExtra("RESULT");
                //webView.clearHistory();
                webView.loadUrl("about:blank");
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        urlWebView=this.url;
    }

    //******************PROPER METHODS**********************

    public void onUp(View view) {
        super.onBackPressed();
    }

    private void setActionbar(boolean hide) {

        actionBar = getActionBar();
        if(actionBar!=null){
            if (hide) {
                actionBar.hide();
            } else {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    private void loadWebView() {

        url = Constants.getSearchFlightsUrl();
        String flightClass ="";
        switch (paramCabin){
            case 0:
                flightClass="Economy";
                break;
            case 1:
                flightClass="Business";
                break;
            case 2:
                flightClass="First";
                break;
        }
        webView.addJavascriptInterface(new AppJavaScriptProxyFlights(this), "androidProxy");

        String postData = "AirFlightType="+flightType+"&AirOriginLocation="+paramDepartFromName+
                "&AirOriginLocationIATA="+paramDepartFrom+"&AirDestinationLocation="+paramArriveAtName+
                "&AirReturnDateTime="+paramArriveDate+"&AirDestinationLocationIATA="+paramArriveAt+
                "&AirDepartureDateTime="+paramDepartDate+"&AirTravelAvailAdt="+paramAdult+
                "&AirTravelAvailChd="+paramChildren+"&AirTravelAvailInf="+paramInfant+
                "&AirBookingClassPref="+flightClass+"&AirlineCode=*&AirFlexDateDeparture=*&AirTravelTimeStart=" +
                "*&AirFlexDatesReturn=*&AirTravelTimeEnd=*&AirTravelDic=*&AirRestricTar=*&AirSeg=1&Payment=3";
        url = url + postData;
        Log.d("url con data",url);
        webView.loadUrl(url);
    }

    public void onalertDialogDepartureOrArriveNotSelected(){

        final Context context = this;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_no_flights_dialog);
        dialog.show();

        Button btnOk = (Button)dialog.findViewById(R.id.ButtonOkAlertDialogNoFlights);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, FlightsActivity.class);
                context.startActivity(i);
                dialog.dismiss();
            }
        });
    }

    public void openOnePocket(){
        Intent intent = new Intent(this, OnepocketPurchaseActivity.class);
        Bundle bundle = Constants.createPurchaseBundle(Constants.getUser(this), onePocketmessage,
                OPKConstants.TYPE_FLIGHT, (VisaCheckoutApp) getApplication());
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.REQUEST_ONEPOCKET_RETURN);
    }

    private void loadArrows(){

        if(webView.canGoBack()){
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        }else{
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if(webView.canGoForward()){
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        }else{
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }

    public void onGoBack(View view){
        if(webView.canGoBack()) {
            webView.goBack();
        }
    }

    public void onGoForward(View view){
        if(webView.canGoForward()) {
            webView.goForward();
        }
    }

    public void onBackButton(View view){
        finish();
    }

    //******************INNER CLASSES**********************
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.d(TAG, "url: " + url);
            if (Util.hasInternetConnectivity(FlightsSearchActivity.this)){
                if (url.contains("http://")||url.contains("https://")){
                    view.loadUrl(url);
                    urlWebView=url;
                }else if(url.contains("detalleventaht:")&& Util.hasInternetConnectivity
                        (FlightsSearchActivity.this)){
                //urlWebView=HotelsActivity.this.url;
                    // TODO replace with Onepocket pay activity
                }else if(url.equals("allegra:callbackFlights")){
                    onalertDialogDepartureOrArriveNotSelected();
                }
                else{
                    view.loadUrl(url);
                    urlWebView=url;
                }
            }else{
                Toast.makeText(FlightsSearchActivity.this, R.string.err_no_internet,
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            imgProgress.setVisibility(View.GONE);
            txtLoading.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            if (url.equals("about:blank")) {
                webView.loadUrl(returnURL);
            }
            loadArrows();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

}
