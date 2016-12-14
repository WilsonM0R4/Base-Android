package com.allegra.handyuvisa;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.CustomizedTextView;
import com.allegra.handyuvisa.utils.Util;
import com.allem.onepocket.utils.OPKConstants;

public class HotelSearchActivity extends Activity {//LoadAnimate implements LoadAnimate.InflateReadyListener

    //******************GLOBAL ATTRIBUTES**********************
    private static final String TAG = "HotelSearchActivity";
    public static final int REQUEST_ONEPOCKET_RETURN = 10001;
    private ActionBar actionBar;
    private String paramDestination;
    private String paramDestinationName;
    private String paramCheckIn;
    private String paramCheckOut;
    private int hotel = 1;
    private int paramRooms, onlyOnceProgressBar = 0;
    private WebView webView;
    private int paramAdults = 0, paramAdults2 = 0, paramAdults3 = 0, paramAdults4 = 0;
    private int paramChildren = 0, paramChildren2 = 0, paramChildren3 = 0, paramChildren4 = 0;
    private int paramInfant = 0, paramInfant2 = 0, paramInfant3 = 0,paramInfant4 = 0;
    private String paramDepartDate = "";
    private String paramArriveDate = "";
    private String paramDepartFrom;
    private String paramDepartFromName;
    private String paramArriveAt;
    private String paramArriveAtName;
    private String url,urlWebView;
    private ImageButton arrowBack, arrowF;
    public String onePocketmessage;
    public String mcard;
    private String returnURL;
    private ImageView imgLogoAllegra, progressBar;
    private TextView txtLoading;

    //******************OVERRIDE METHODS**********************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_hotel_in_progress);
        setActionbar(true);
        //Find views by Id
        webView = (WebView)findViewById(R.id.webView);
      /*  progressBar = (ImageView) findViewById(R.id.pb_search_loader);
        imgLogoAllegra = (ImageView)findViewById(R.id.imgProgress);
        txtLoading = (TextView)findViewById(R.id.txtLoading);*/
        TextView title = (TextView)findViewById(R.id.tv_title_secc);
        arrowBack= (ImageButton)findViewById(R.id.arrow_back);
        arrowF= (ImageButton)findViewById(R.id.arrow_forward);
        //Set webView settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        /*webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);*/
        webView.setWebViewClient(new MyWebViewClient());

        title.setText(R.string.title_hotel_results);
        /*webView.setVisibility(View.GONE);*/
        // Set Animation
        /*progressBar.setVisibility(View.VISIBLE);
        progressBar.post(new Runnable() {
            @Override
            public void run() {
                ((AnimationDrawable) progressBar.getBackground()).start();
            }
        });*/
        //Get intent params from HotelsActivity
        paramChildren = getIntent().getIntExtra("&childHotel1", 0);
        paramAdults = getIntent().getIntExtra("&adultHotel1", 1);
        paramRooms = getIntent().getIntExtra("&roomHotel", 1);

        switch (paramRooms){
            case 2:
                paramChildren2 = getIntent().getIntExtra("&childHotel2", 0);
                paramAdults2 = getIntent().getIntExtra("&adultHotel2", 0);
                break;
            case 3:
                paramChildren2 = getIntent().getIntExtra("&childHotel2", 0);
                paramAdults2 = getIntent().getIntExtra("&adultHotel2", 0);
                paramChildren3 = getIntent().getIntExtra("&childHotel3", 0);
                paramAdults3 = getIntent().getIntExtra("&adultHotel3", 0);
                break;
            case 4:
                paramChildren2 = getIntent().getIntExtra("&childHotel2", 0);
                paramAdults2 = getIntent().getIntExtra("&adultHotel2", 0);
                paramChildren3 = getIntent().getIntExtra("&childHotel3", 0);
                paramAdults3 = getIntent().getIntExtra("&adultHotel3", 0);
                paramChildren4 = getIntent().getIntExtra("&childHotel4", 0);
                paramAdults4 = getIntent().getIntExtra("&adultHotel4", 0);
                break;
        }

        paramCheckIn = getIntent().getStringExtra("&arrivalHotel");
        paramCheckOut = getIntent().getStringExtra("&departureHotel");
        paramDestinationName = getIntent().getStringExtra("&cityHotel");
        paramDestination = getIntent().getStringExtra("&cityHotelHidden");

        Log.d("paramAdult",String.valueOf(paramAdults));
        Log.d("paramChildren",String.valueOf(paramChildren));
        Log.d("paramAdult2",String.valueOf(paramAdults2));
        Log.d("paramChildren2",String.valueOf(paramChildren2));
        Log.d("paramAdult3",String.valueOf(paramAdults3));
        Log.d("paramChildren3",String.valueOf(paramChildren3));
        Log.d("paramAdult4",String.valueOf(paramAdults4));
        Log.d("paramChildren4",String.valueOf(paramChildren4));
        Log.d("paramInfant",String.valueOf(paramInfant));
        Log.d("paramCheckIn",String.valueOf(paramCheckIn));
        Log.d("paramCheckOut",String.valueOf(paramCheckOut));
        Log.d("roomHotel", String.valueOf(paramRooms));
        Log.d("cityHotel", String.valueOf(paramDestinationName));
        Log.d("cityHotelHidden", String.valueOf(paramDestination));
        //Validate mCard in order to send it
        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        mcard = prefs.getString("idMcard", "0");
        if(mcard.equals("0")){
            mcard = "Standard";
        }
        else {
            mcard = "mcard";
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
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
    public void onBackPressed() {
        super.onBackPressed();
        urlWebView=this.url;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.ACTIVITY_LOGIN) {
            if(resultCode == RESULT_OK){
                Log.d(TAG,"usuario logueado");
                HotelSearchActivity.this.invalidateOptionsMenu();
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
                webView.loadUrl("about:blank");
                //progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    //******************PROPER METHODS********************
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


     // Now is requesting by GET method
    private void loadWebView() {

        url = Constants.getSearchHotelsUrl();
        String postData = "";
        //TODO: Siempre enviar "es-CO" cuando la locale sea "es", cuando sea "en" se envía "en-US"
        switch(paramRooms) {
            case 1:
               postData = "cityHotel="+paramDestinationName+
                        "&cityHotelHidden="+paramDestination+
                        "&arrivalHotel="+paramCheckIn+
                        "&departureHotel="+ paramCheckOut+
                        "&roomHotel="+ paramRooms+
                        "&adultHotel1=" +paramAdults+
                        "&childHotel1="+paramChildren+
                        "&language=es-CO&PaymentMethod=OnePocket"+
                        "&Group="+mcard;
                break;
            case 2:
                postData = "cityHotel="+paramDestinationName+
                        "&cityHotelHidden="+paramDestination+
                        "&arrivalHotel="+paramCheckIn+
                        "&departureHotel="+ paramCheckOut+
                        "&roomHotel="+ paramRooms+
                        "&adultHotel1=" +paramAdults+
                        "&childHotel1="+paramChildren+
                        "&adultHotel2=" +paramAdults2+
                        "&childHotel2="+paramChildren2+
                        "&language=es-CO&PaymentMethod=OnePocket"+
                        "&Group="+mcard;
                break;

            case 3:
                postData = "cityHotel="+paramDestinationName+
                        "&cityHotelHidden="+paramDestination+
                        "&arrivalHotel="+paramCheckIn+
                        "&departureHotel="+ paramCheckOut+
                        "&roomHotel="+ paramRooms+
                        "&adultHotel1=" +paramAdults+
                        "&childHotel1="+paramChildren+
                        "&adultHotel2=" +paramAdults2+
                        "&childHotel2="+paramChildren2+
                        "&adultHotel3=" +paramAdults3+
                        "&childHotel3="+paramChildren3+
                        "&language=es-CO&PaymentMethod=OnePocket"+
                        "&Group="+mcard;
                break;

            case 4:
                postData = "cityHotel="+paramDestinationName+
                        "&cityHotelHidden="+paramDestination+
                        "&arrivalHotel="+paramCheckIn+
                        "&departureHotel="+ paramCheckOut+
                        "&roomHotel="+ paramRooms+
                        "&adultHotel1=" +paramAdults+
                        "&childHotel1="+paramChildren+
                        "&adultHotel2=" +paramAdults2+
                        "&childHotel2="+paramChildren2+
                        "&adultHotel3=" +paramAdults3+
                        "&childHotel3="+paramChildren3+
                        "&adultHotel4=" +paramAdults4+
                        "&childHotel4="+paramChildren4+
                        "&language=es-CO&PaymentMethod=OnePocket"+
                        "&Group="+mcard;
                break;
        }
        webView.addJavascriptInterface(new AppJavaScriptProxyHotels(this), "androidProxy");
        url = url + postData;
        Log.d("murl",url);
        webView.loadUrl(url);
    }

    public void openOnePocket(){

        Intent intent = new Intent(HotelSearchActivity.this, OnepocketPurchaseActivity.class);
        Bundle bundle = Constants.createPurchaseBundle(Constants.getUser(this), onePocketmessage,
                OPKConstants.TYPE_HOTEL, (VisaCheckoutApp) getApplication());
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

    //**************INNER CLASSES******************


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.d(TAG, "url: " + url);
            if (Util.hasInternetConnectivity(HotelSearchActivity.this)){
                if (url.contains("http://")||url.contains("https://")){
                    view.loadUrl(url);
                    urlWebView=url;
                }else if(url.contains("detalleventaht:")&& Util.hasInternetConnectivity
                        (HotelSearchActivity.this)){
                    //urlWebView=HotelsActivity.this.url;
                    // TODO replace with Onepocket pay activity
                }else{
                    view.loadUrl(url);
                    urlWebView=url;
                }
            }else{
                Toast.makeText(HotelSearchActivity.this, R.string.err_no_internet,
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            /*progressBar.setVisibility(View.GONE);
            imgLogoAllegra.setVisibility(View.GONE);
            txtLoading.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);*/
            if (url.equals("about:blank")) {
                webView.loadUrl(returnURL);
            }
            loadArrows();
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            //progressBar.setVisibility(View.VISIBLE);
        }
    }
}
