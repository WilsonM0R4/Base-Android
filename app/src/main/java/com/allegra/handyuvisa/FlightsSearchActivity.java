package com.allegra.handyuvisa;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.Util;
import com.allem.onepocket.utils.OPKConstants;

/**      Activity for search flights in WebSite and display them inside a webView.        **/
public class FlightsSearchActivity extends WebViewActivity {//LoadAnimate  implements LoadAnimate.InflateReadyListener

    //******************GLOBAL ATTRIBUTES**********************
    private static final String TAG = "FlightsSearchActivity";
    private ActionBar actionBar;
    //private WebView webView;
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
    private ImageButton buttonBack;

    //******************OVERRIDE METHODS**********************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_search_in_progress);


     /*   Log.d("paramCabin",String.valueOf(paramCabin));
        Log.d("paramAdult",String.valueOf(paramAdult));
        Log.d("paramChildren",String.valueOf(paramChildren));
        Log.d("paramDepartDate",String.valueOf(paramDepartDate));
        Log.d("paramArriveDate",String.valueOf(paramArriveDate));
        Log.d("paramDepartFrom",String.valueOf(paramDepartFrom));
        Log.d("paramDepartFromName",String.valueOf(paramDepartFromName));
        Log.d("paramArriveAt",String.valueOf(paramArriveAt));
        Log.d("paramArriveAtName",String.valueOf(paramArriveAtName));
        Log.d("paramInfant",String.valueOf(paramInfant));*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_search_in_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        setActionbar(true);
        buttonBack = (ImageButton) view.findViewById(R.id.ib_settings);

        mWebView = (WebView) view.findViewById(R.id.webView);
        txtLoading = (TextView) view.findViewById(R.id.txtLoading);
        //btn_buy = (Button) findViewById(R.id.btn_buy);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new SecureBrowser(this));
        TextView title = (TextView) view.findViewById(R.id.tv_title_secc);
        title.setText(R.string.title_flight_results);
        arrowBack = (ImageButton) view.findViewById(R.id.arrow_back);
        arrowF = (ImageButton) view.findViewById(R.id.arrow_forward);
        imgProgress = (ImageView) view.findViewById(R.id.imgProgress);
        progressBar = (ImageView) view.findViewById(R.id.pb_search_loader);
        //mWebView.setVisibility(View.GONE);
        //Animation
        /*progressBar.setVisibility(View.VISIBLE);
        progressBar.post(new Runnable() {
            @Override
            public void run() {
                ((AnimationDrawable) progressBar.getBackground()).start();
            }
        });*/

        paramTrip = getArguments().getInt("trip", 0);
        if (paramTrip == 1){//One Way
            flightType = "OW";
        } else {
            flightType = "RT";
        }

        paramCabin = getArguments().getInt("cabin", 0);// 0 - economy; 1 - business; 2 - first
        paramAdult = getArguments().getInt("adult", 0);
        paramChildren = getArguments().getInt("children", 0);
        paramInfant = getArguments().getInt("infant", 0);
        paramDepartDate = getArguments().getString("departOn");
        paramArriveDate = getArguments().getString("arriveOn");
        paramDepartFrom = getArguments().getString("departFrom");//IATA CODE DEPARTURE
        paramDepartFromName = getArguments().getString("departFromName");//LARGE NAME DEPARTURE
        paramArriveAt = getArguments().getString("arriveAt");//IATA CODE ARRIVAL
        paramArriveAtName = getArguments().getString("arriveAtName");//LARGE NAME ARRIVAL

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceLayout(new FlightsActivity(), true);
            }
        });

        setupLoadingView(getView());

    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.stopLoading();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWebView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.ACTIVITY_LOGIN) {
            if(resultCode == MainActivity.RESULT_OK){
                //Log.d(TAG, "usuario logueado");
                getActivity().invalidateOptionsMenu();
                mWebView.loadUrl("about:blank");
                loadWebView();
            }
            if (resultCode == MainActivity.RESULT_CANCELED) {

            }

        }else if (requestCode == Constants.ONE_POCKET_NEEDS_LOGIN){
            openOnePocket();
        } else if (requestCode == Constants.REQUEST_ONEPOCKET_RETURN) {
            if (data != null) {
                returnURL = data.getStringExtra("RESULT");
                //webView.clearHistory();
                mWebView.loadUrl("about:blank");
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    /** REVIEW USAGE **/
    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        urlWebView=this.url;
    }*/

    @Override
    public boolean onShouldOverrideUrlLoading(WebView webView, String url){

        Log.d(TAG, "url: " + url);
        if (Util.hasInternetConnectivity(getActivity())){
            if (url.contains("http://")||url.contains("https://")){
                webView.loadUrl(url);
                urlWebView=url;
            }else if(url.contains("detalleventaht:")&& Util.hasInternetConnectivity
                    (getActivity())){
                //urlWebView=HotelsActivity.this.url;
                // TODO replace with Onepocket pay activity
            }else if(url.equals("allegra:callbackFlights")){
                onalertDialogDepartureOrArriveNotSelected();
            }else if(url.equals("")){

            }
            else{
                webView.loadUrl(url);
                urlWebView=url;
            }
        }else{
            Toast.makeText(getActivity(), R.string.err_no_internet,
                    Toast.LENGTH_SHORT).show();
        }
        return true;

    }

    //******************PROPER METHODS**********************

    public void onUp(View view) {
        //super.onBackPressed();
    }

    private void setActionbar(boolean hide) {

        actionBar = getActivity().getActionBar();
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
        mWebView.addJavascriptInterface(new AppJavaScriptProxyFlights(this), "androidProxy");

        String postData = "AirFlightType="+flightType+"&AirOriginLocation="+paramDepartFromName+
                "&AirOriginLocationIATA="+paramDepartFrom+"&AirDestinationLocation="+paramArriveAtName+
                "&AirReturnDateTime="+paramArriveDate+"&AirDestinationLocationIATA="+paramArriveAt+
                "&AirDepartureDateTime="+paramDepartDate+"&AirTravelAvailAdt="+paramAdult+
                "&AirTravelAvailChd="+paramChildren+"&AirTravelAvailInf="+paramInfant+
                "&AirBookingClassPref="+flightClass+"&AirlineCode=*&AirFlexDateDeparture=*&AirTravelTimeStart=" +
                "*&AirFlexDatesReturn=*&AirTravelTimeEnd=*&AirTravelDic=*&AirRestricTar=*&AirSeg=1&Payment=3";
        url = url + postData;
        //Log.d("url con data",url);
        mWebView.loadUrl(url);
    }

    public void onalertDialogDepartureOrArriveNotSelected(){

        final Context context = getActivity();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_no_flights_dialog);
        dialog.show();

        Button btnOk = (Button)dialog.findViewById(R.id.ButtonOkAlertDialogNoFlights);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(context, FlightsActivity.class);
                context.startActivity(i);*/
                ((MainActivity) getActivity()).replaceLayout(new FlightsActivity(), true);
                dialog.dismiss();
            }
        });
    }

    public void openOnePocket(){
        /** CHANGE THE METHOD, NO MORE USED THIS WAY **/
        Intent intent = new Intent(getActivity(), OnepocketPurchaseActivity.class);
        Bundle bundle = Constants.createPurchaseBundle(Constants.getUser(getActivity()), onePocketmessage,
                OPKConstants.TYPE_FLIGHT,
                (com.allegra.handyuvisa.VisaCheckoutApp) getActivity().getApplication());
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.REQUEST_ONEPOCKET_RETURN);
    }

    private void loadArrows(){

        if(mWebView.canGoBack()){
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        }else{
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if(mWebView.canGoForward()){
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        }else{
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }

    public void onGoBack(View view){
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    public void onGoForward(View view){
        if(mWebView.canGoForward()) {
            mWebView.goForward();
        }
    }

    public void onBackButton(View view){
        //finish();
    }
    private void setupLoadingView(View root) {
        mLoadingView = (FrameLayout) root.findViewById(R.id.loading_view);
        mLoadingBar = (ImageView) root.findViewById(R.id.pb_search_loader);
    }

    //******************INNER CLASSES**********************
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.d(TAG, "url: " + url);
            if (Util.hasInternetConnectivity(getActivity())){
                if (url.contains("http://")||url.contains("https://")){
                    view.loadUrl(url);
                    urlWebView=url;
                }else if(url.contains("detalleventaht:")&& Util.hasInternetConnectivity
                        (getActivity())){
                //urlWebView=HotelsActivity.this.url;
                    // TODO replace with Onepocket pay activity
                }else if(url.equals("allegra:callbackFlights")){
                    onalertDialogDepartureOrArriveNotSelected();
                }else if(url.equals("")){

                }
                else{
                    view.loadUrl(url);
                    urlWebView=url;
                }
            }else{
                Toast.makeText(getActivity(), R.string.err_no_internet,
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            imgProgress.setVisibility(View.GONE);
            txtLoading.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            if (url.equals("about:blank")) {
                mWebView.loadUrl(returnURL);
            }
            loadArrows();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

}
